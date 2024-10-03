package com.github.schuettec.cobra2Dexamples.walkinganimation;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;
import static com.github.schuettec.cobra2d.math.Math2D.toDegrees;
import static java.lang.Math.acos;
import static java.lang.Math.pow;

import com.github.schuettec.cobra2d.math.HarmonicOscillation;
import com.github.schuettec.cobra2d.math.HarmonicOscillation.Type;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class Leg {

  private final double verhältnisOberschenkelUnterschenkel;
  private final double längeFuß;

  private final int maxStep;
  private final double legLength;

  private double ellipsisWalkAnimMaxX;
  private double ellipsisWalkAnimMaxY;
  private HarmonicOscillation ellipsisOscilatorX;
  private HarmonicOscillation ellipsisOscilatorY;
  private double radius;

  record LegRenderable(Point oberschenkelStart,
      Point oberschenkelEnde, Point unterschenkelStart,
      Point unterschenkelEnde, Point fußStart, Point fußEnde) {

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
    }
  }

  public static class LegBuilder {
    private double verhältnisOberschenkelUnterschenkel = 1.2d;
    private double längeFuß = 14d;

    private int maxStep = 100;
    private double legLength;

    private double ellipsisWalkAnimMaxX = 250d;
    private double ellipsisWalkAnimMaxY = 80d;

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

    public LegBuilder setEllipsisWalkAnimMaxX(
        double ellipsisWalkAnimMaxX) {
      this.ellipsisWalkAnimMaxX = ellipsisWalkAnimMaxX;
      return this;
    }

    public LegBuilder setEllipsisWalkAnimMaxY(
        double ellipsisWalkAnimMaxY) {
      this.ellipsisWalkAnimMaxY = ellipsisWalkAnimMaxY;
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
    this.radius = legLength;

    this.ellipsisWalkAnimMaxX = builder.ellipsisWalkAnimMaxX;
    this.ellipsisWalkAnimMaxY = builder.ellipsisWalkAnimMaxY;

    this.ellipsisOscilatorX = new HarmonicOscillation(maxStep,
        ellipsisWalkAnimMaxX, 0.5d, Type.COSINUS, false);
    this.ellipsisOscilatorY = new HarmonicOscillation(maxStep,
        ellipsisWalkAnimMaxY, 0.5d, Type.SINUS, true);
  }

  public static LegBuilder newLeg() {
    return new LegBuilder();
  }

  // Inverse Kinematik-Methode
  public LegRenderable berechneWinkel(Point start, boolean left,
      Point ziel) {

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

    return createLegRenderableFromWinkel(start,
        oberschenkelWinkel, unterschenkelwinkel, fussWinkel);

  }

  private double getOberschenkelLänge() {
    return legLength - getUnterschenkelLänge();
  }

  private double getUnterschenkelLänge() {
    return legLength / (verhältnisOberschenkelUnterschenkel + 1);
  }

  public LegRenderable calculateStep(Point worldCoordinates,
      boolean left, double currentStep) {

    ellipsisOscilatorX.setAmplitude(ellipsisWalkAnimMaxX);
    ellipsisOscilatorY.setAmplitude(ellipsisWalkAnimMaxY);
    double sX = ellipsisOscilatorX.apply(currentStep);
    double sY = ellipsisOscilatorY.apply(currentStep);

    Point positionSchwingungselipseZentrum = Math2D.getCircle(
        worldCoordinates.clone()
            .translate(0, ellipsisWalkAnimMaxY / 2.),
        radius, 270d);

    Point schwingungsEllipsePunkt = new Point(sX, sY).clone()
        .translate(positionSchwingungselipseZentrum);

    return berechneWinkel(worldCoordinates, left,
        schwingungsEllipsePunkt);

  }

  private LegRenderable createLegRenderableFromWinkel(
      Point worldCoordinates, double winkelOberschenkel,
      double winkelUnterschenkel, double winkelFuss) {

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

    return new LegRenderable(o1S, o1E, u1S, u1E, f1S, f1E);
  }

  public double getEllipsisWalkAnimMaxX() {
    return ellipsisWalkAnimMaxX;
  }

  public void setEllipsisWalkAnimMaxX(
      double ellipsisWalkAnimMaxX) {
    this.ellipsisWalkAnimMaxX = ellipsisWalkAnimMaxX;
  }

  public double getEllipsisWalkAnimMaxY() {
    return ellipsisWalkAnimMaxY;
  }

  public void setEllipsisWalkAnimMaxY(
      double ellipsisWalkAnimMaxY) {
    this.ellipsisWalkAnimMaxY = ellipsisWalkAnimMaxY;
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

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  }

}
