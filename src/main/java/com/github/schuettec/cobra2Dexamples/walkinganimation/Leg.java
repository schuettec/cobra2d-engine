package com.github.schuettec.cobra2Dexamples.walkinganimation;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;
import static com.github.schuettec.cobra2d.math.Math2D.toDegrees;
import static java.lang.Math.acos;
import static java.lang.Math.pow;

import java.util.List;

import com.github.schuettec.cobra2Dexamples.walkinganimation.LegAnimationController.AnimationResult;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class Leg {

  private final double verhältnisOberschenkelUnterschenkel;
  private final double längeFuß;

  private final int maxStep;
  private final double legLength;

  public record LegRenderable(Point oberschenkelStart,
      Point oberschenkelEnde, Point unterschenkelStart,
      Point unterschenkelEnde, Point fußStart, Point fußEnde,
      List<Point> debugPoints) {

    /**
     * @param renderer
     * @param screenTranslation A translation movin the local
     *        coord-system to world/screen coordinates.
     */
    public void render(RendererAccess renderer,
        Point screenTranslation) {
      Point oberschenkelStart2 = screenTranslation.clone()
          .translate(oberschenkelStart);
      Point oberschenkelEnde2 = screenTranslation.clone()
          .translate(oberschenkelEnde);
      Point unterschenkelStart2 = screenTranslation.clone()
          .translate(unterschenkelStart);
      Point unterschenkelEnde2 = screenTranslation.clone()
          .translate(unterschenkelEnde);
      Point fußStart2 = screenTranslation.clone()
          .translate(fußStart);
      Point fußEnde2 = screenTranslation.clone()
          .translate(fußEnde);
      renderer.drawLine(oberschenkelStart2.getRoundX(),
          oberschenkelStart2.getRoundY(),
          oberschenkelEnde2.getRoundX(),
          oberschenkelEnde2.getRoundY(), Color.BLUE);
      renderer.drawLine(unterschenkelStart2.getRoundX(),
          unterschenkelStart2.getRoundY(),
          unterschenkelEnde2.getRoundX(),
          unterschenkelEnde2.getRoundY(), Color.GREEN);
      renderer.drawLine(fußStart2.getRoundX(),
          fußStart2.getRoundY(), fußEnde2.getRoundX(),
          fußEnde2.getRoundY(), Color.YELLOW);

      for (Point d : debugPoints) {
        Point sP = d.clone()
            .translate(screenTranslation);
        float debugRadius = 1f;
        renderer.fillOval(sP.getFloatX() - debugRadius,
            sP.getFloatY() - debugRadius, 2 * debugRadius,
            2 * debugRadius, Color.MAGENTA);
      }
    }
  }

  public static class LegBuilder {
    private double verhältnisOberschenkelUnterschenkel = 1.2d;
    private double längeFuß = 14d;

    private int maxStep = 100;
    private double legLength;

    // Builder-Methoden für jeden Parameter
    public LegBuilder setVerhältnisOberschenkelUnterschenkel(
        double verhältnis) {
      this.verhältnisOberschenkelUnterschenkel = verhältnis;
      return this;
    }

    public LegBuilder setLängeFuß(double länge) {
      this.längeFuß = länge;
      return this;
    }

    public LegBuilder setMaxStep(int maxStep) {
      this.maxStep = maxStep;
      return this;
    }

    public LegBuilder setLegLength(double length) {
      this.legLength = length;
      return this;
    }

    // Methode zum Erstellen der LegConfiguration
    public Leg build() {
      return new Leg(this);
    }
  }

  private Leg(LegBuilder builder) {
    this.verhältnisOberschenkelUnterschenkel = builder.verhältnisOberschenkelUnterschenkel;
    this.längeFuß = builder.längeFuß;

    this.maxStep = builder.maxStep;
    this.legLength = builder.legLength;

  }

  public static LegBuilder newLeg() {
    return new LegBuilder();
  }

  // Inverse Kinematik-Methode
  public LegRenderable berechneWinkel(AnimationResult result,
      boolean left) {

    Point start = result.bodyPosition();
    Point ziel = result.targetPoint();

    double oberschenkelLänge = getOberschenkelLänge();
    double unterschenkelLänge = getUnterschenkelLänge();

    double winkelOffset = Math2D.getAngle(start, ziel);

    double a = Math2D.getEntfernung(start, ziel);
    double b = oberschenkelLänge;
    double c = unterschenkelLänge;

    double alpha = acos(
        (pow(b, 2) + pow(c, 2) - pow(a, 2)) / (2. * b * c));
    double gamma = acos(
        (pow(a, 2) + pow(b, 2) - pow(c, 2)) / (2. * a * b));

    double oberschenkelWinkel = Double.isNaN(gamma)
        ? winkelOffset
        : normalizeAngle(toDegrees(gamma) + winkelOffset);

    double unterschenkelwinkel = Double.isNaN(alpha)
        ? oberschenkelWinkel : normalizeAngle(
            toDegrees(alpha) + oberschenkelWinkel + 180d);

    double fussWinkel = normalizeAngle(
        unterschenkelwinkel + (unterschenkelwinkel / 3d));

    // Flip direction if left
    if (left) {
      oberschenkelWinkel = normalizeAngle(
          180d - oberschenkelWinkel);
      unterschenkelwinkel = normalizeAngle(
          180d - unterschenkelwinkel);
      fussWinkel = normalizeAngle(180d - fussWinkel);
    }

    return createLegRenderableFromWinkel(result, start,
        oberschenkelWinkel, unterschenkelwinkel, fussWinkel);

  }

  private double getOberschenkelLänge() {
    return legLength - getUnterschenkelLänge();
  }

  private double getUnterschenkelLänge() {
    return legLength / (verhältnisOberschenkelUnterschenkel + 1);
  }

  public LegRenderable calculateStep(Point worldCoordinates,
      LegAnimationController animationController, boolean left,
      double currentStep) {
    AnimationResult result = animationController
        .calculateTargetByStep(worldCoordinates, currentStep);

    return berechneWinkel(result, left);
  }

  private LegRenderable createLegRenderableFromWinkel(
      AnimationResult result, Point worldCoordinates,
      double winkelOberschenkel, double winkelUnterschenkel,
      double winkelFuss) {

    double oberschenkelLänge = getOberschenkelLänge();
    double unterschenkelLänge = getUnterschenkelLänge();

    Point o1S = worldCoordinates.clone();
    Point o1E = Math2D.getCircle(o1S, oberschenkelLänge,
        winkelOberschenkel);

    Point u1S = o1E.clone();
    Point u1E = Math2D.getCircle(u1S, unterschenkelLänge,
        winkelUnterschenkel);

    Point f1S = u1E.clone();
    Point f1E = Math2D.getCircle(f1S, längeFuß, winkelFuss);

    return new LegRenderable(o1S, o1E, u1S, u1E, f1S, f1E,
        result.debugPoints());
  }

  public double getLängeFuß() {
    return längeFuß;
  }

  public int getMaxStep() {
    return maxStep;
  }

  public double getLegLength() {
    return legLength;
  }

}
