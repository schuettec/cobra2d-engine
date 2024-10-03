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
  private final double winkelVorschwungOberschenkel;
  private final double winkelRückschwungOberschenkel;
  private final double maxWinkelOberschenkel;

  private final double winkelVorschwungUnterschenkel;
  private final double winkelRückschwungUnterschenkel;
  private final double maxWinkelUnterschenkel;

  private final double winkelVorschwungFuß;
  private final double winkelRückschwungFuß;
  private final double maxWinkelFuß;

  private final double winkelNeutral;
  private final int maxStep;
  private final double legLength;

  private double unterschenkelLänge;
  private double oberschenkelLänge;

  private double ellipsisWalkAnimMaxX;
  private double ellipsisWalkAnimMaxY;
  private HarmonicOscillation ellipsisOscilatorX;
  private HarmonicOscillation ellipsisOscilatorY;

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
    private double winkelVorschwungOberschenkel = 45;
    private double winkelRückschwungOberschenkel = 30;

    private double winkelVorschwungUnterschenkel = 30;
    private double winkelRückschwungUnterschenkel = 90;

    private double winkelVorschwungFuß = 120;
    private double winkelRückschwungFuß = 80;

    private double winkelNeutral = 270;
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

    public LegBuilder setWinkelVorschwungOberschenkel(
        double winkel) {
      this.winkelVorschwungOberschenkel = winkel;
      return this;
    }

    public LegBuilder setWinkelRückschwungOberschenkel(
        double winkel) {
      this.winkelRückschwungOberschenkel = winkel;
      return this;
    }

    public LegBuilder setWinkelVorschwungUnterschenkel(
        double winkel) {
      this.winkelVorschwungUnterschenkel = winkel;
      return this;
    }

    public LegBuilder setWinkelRückschwungUnterschenkel(
        double winkel) {
      this.winkelRückschwungUnterschenkel = winkel;
      return this;
    }

    public LegBuilder setWinkelVorschwungFuß(double winkel) {
      this.winkelVorschwungFuß = winkel;
      return this;
    }

    public LegBuilder setWinkelRückschwungFuß(double winkel) {
      this.winkelRückschwungFuß = winkel;
      return this;
    }

    public LegBuilder setWinkelNeutral(double winkel) {
      this.winkelNeutral = winkel;
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
    this.winkelVorschwungOberschenkel = builder.winkelVorschwungOberschenkel;
    this.winkelRückschwungOberschenkel = builder.winkelRückschwungOberschenkel;
    this.maxWinkelOberschenkel = winkelVorschwungOberschenkel
        + winkelRückschwungOberschenkel;

    this.winkelVorschwungUnterschenkel = builder.winkelVorschwungUnterschenkel;
    this.winkelRückschwungUnterschenkel = builder.winkelRückschwungUnterschenkel;
    this.maxWinkelUnterschenkel = winkelVorschwungUnterschenkel
        + winkelRückschwungUnterschenkel;

    this.winkelVorschwungFuß = builder.winkelVorschwungFuß;
    this.winkelRückschwungFuß = builder.winkelRückschwungFuß;
    this.maxWinkelFuß = winkelVorschwungFuß
        + winkelRückschwungFuß;

    this.winkelNeutral = builder.winkelNeutral;
    this.maxStep = builder.maxStep;
    this.legLength = builder.legLength;

    this.ellipsisWalkAnimMaxX = builder.ellipsisWalkAnimMaxX;
    this.ellipsisWalkAnimMaxY = builder.ellipsisWalkAnimMaxY;

    this.unterschenkelLänge = legLength
        / (verhältnisOberschenkelUnterschenkel + 1);
    this.oberschenkelLänge = legLength - unterschenkelLänge;

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

    double winkelOffset = Math2D.getAngle(start, ziel);

    double a = Math2D.getEntfernung(start, ziel);
    double b = oberschenkelLänge;
    double c = unterschenkelLänge;

    double alpha = acos(
        (pow(b, 2) + pow(c, 2) - pow(a, 2)) / (2. * b * c));
    double beta = acos(
        (pow(a, 2) + pow(c, 2) - pow(b, 2)) / (2. * a * c));
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

  public LegRenderable calculateStep(Point worldCoordinates,
      boolean left, double currentStep) {

    double sX = ellipsisOscilatorX.apply(currentStep);
    double sY = ellipsisOscilatorY.apply(currentStep);

    Point positionSchwingungselipseZentrum = Math2D.getCircle(
        worldCoordinates.clone()
            .translate(0, ellipsisWalkAnimMaxY / 2.),
        legLength, 270d);

    Point schwingungsEllipsePunkt = new Point(sX, sY).clone()
        .translate(positionSchwingungselipseZentrum);

    return berechneWinkel(worldCoordinates, left,
        schwingungsEllipsePunkt);

  }

  private LegRenderable createLegRenderableFromWinkel(
      Point worldCoordinates, double winkelOberschenkel,
      double winkelUnterschenkel, double winkelFuss) {
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
}
