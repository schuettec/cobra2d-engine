package com.github.schuettec.cobra2Dexamples.walkinganimation;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;
import static com.github.schuettec.cobra2d.math.Math2D.toDegrees;
import static java.lang.Math.acos;
import static java.lang.Math.pow;

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

    this.unterschenkelLänge = legLength
        / (verhältnisOberschenkelUnterschenkel + 1);
    this.oberschenkelLänge = legLength - unterschenkelLänge;
  }

  public static LegBuilder newLeg() {
    return new LegBuilder();
  }

  // Inverse Kinematik-Methode
  public LegRenderable berechneWinkel(Point start, Point ziel) {

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

    // FLip
    // oberschenkelWinkel = normalizeAngle(
    // 180d - oberschenkelWinkel);
    // unterschenkelwinkel = normalizeAngle(
    // 180d - unterschenkelwinkel);
    // fussWinkel = normalizeAngle(180d - fussWinkel);

    return createLegRenderableFromWinkel(start,
        oberschenkelWinkel, unterschenkelwinkel, fussWinkel);

  }

  // Methode zur Beschränkung der Winkel
  private double beschränkeWinkel(double winkel,
      double minWinkel, double maxWinkel) {
    return Math.max(minWinkel, Math.min(maxWinkel, winkel));
  }

  public LegRenderable calculateStep(Point worldCoordinates,
      int currentStep) {

    double winkelOberschenkel = toOberschenkelWinkel(
        currentStep);

    double winkelUnterschenkel = toUnterschenkelWinkel(
        currentStep);

    double winkelFuss = toFussWinkel(currentStep);

    return createLegRenderableFromWinkel(worldCoordinates,
        winkelOberschenkel, winkelUnterschenkel, winkelFuss);

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

  private double toFussWinkel(int step) {
    return winkelNeutral + winkelVorschwungFuß - (maxWinkelFuß
        / 2.0d)
        * (Math.cos(((2d * Math.PI) / maxStep) * step + Math.PI)
            + 1d);
  }

  /**
   * @param step Time 0-{@link #maxStep}
   * @return Gibt den Winkel des Oberschenkels zurück
   *         normalisiert in Welt-Winkel.
   */
  private double toOberschenkelWinkel(double step) {
    return winkelNeutral + winkelVorschwungOberschenkel
        - (maxWinkelOberschenkel / 2.0d) * (Math.cos(
            ((2d * Math.PI) / maxStep) * step + Math.PI) + 1d);
  }

  /**
   * @param step Time 0-{@link #maxStep}
   * @return Gibt den Winkel des Oberschenkels zurück
   *         normalisiert in Welt-Winkel.
   */
  private double toUnterschenkelWinkel(double step) {
    return winkelNeutral + winkelVorschwungUnterschenkel
        - (maxWinkelUnterschenkel / 2.0d) * (Math.cos(
            ((2d * Math.PI) / maxStep) * step + Math.PI) + 1d);
  }

}
