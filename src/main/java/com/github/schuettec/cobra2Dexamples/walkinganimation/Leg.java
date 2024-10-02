package com.github.schuettec.cobra2Dexamples.walkinganimation;

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

    public void render(RendererAccess renderer, Point position) {
      Point oberschenkelStart2 = position.clone()
          .translate(oberschenkelStart);
      Point oberschenkelEnde2 = position.clone()
          .translate(oberschenkelEnde);
      Point unterschenkelStart2 = position.clone()
          .translate(unterschenkelStart);
      Point unterschenkelEnde2 = position.clone()
          .translate(unterschenkelEnde);
      Point fußStart2 = position.clone()
          .translate(fußStart);
      Point fußEnde2 = position.clone()
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
  public LegRenderable berechneWinkel(Point position,
      Point ziel) {

    // Zielposition relativ zur Hüftposition berechnen
    double hüfteX = position.x;
    double hüfteY = position.y;

    double zielX = ziel.x;
    double zielY = ziel.y;

    double minWinkelOberschenkel = 0;
    double minWinkelUnterschenkel = 0;
    double maxWinkelOberschenkel = 360;
    double maxWinkelUnterschenkel = 360;

    // Zielposition relativ zur Hüftposition berechnen
    double deltaX = zielX - hüfteX;
    double deltaY = zielY - hüfteY;

    // Abstand vom Hüftgelenk zum Ziel (Pythagoras)
    double d = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    // Maximale Reichweite des Beins
    double maximaleReichweite = oberschenkelLänge
        + unterschenkelLänge;

    // Wenn das Ziel zu weit entfernt ist, wird das Ziel auf den
    // nächstmöglichen Punkt gesetzt
    if (d > maximaleReichweite) {
      // Ziel skalieren, sodass es auf die maximale Reichweite
      // fällt
      deltaX *= maximaleReichweite / d;
      deltaY *= maximaleReichweite / d;
      d = maximaleReichweite; // Setze d auf die maximale
                              // Reichweite
      zielX = hüfteX + deltaX;
      zielY = hüfteY + deltaY;
    }

    // Wenn das Ziel zu nah an der Hüfte ist, setze es auf die
    // minimale Reichweite
    double minimaleReichweite = Math
        .abs(oberschenkelLänge - unterschenkelLänge);
    if (d < minimaleReichweite) {
      deltaX *= minimaleReichweite / d;
      deltaY *= minimaleReichweite / d;
      d = minimaleReichweite; // Setze d auf die minimale
                              // Reichweite
      zielX = hüfteX + deltaX;
      zielY = hüfteY + deltaY;
    }

    // Berechnung des winkelUnterschenkel (Kosinussatz)
    double cosWinkelUnterschenkel = (oberschenkelLänge
        * oberschenkelLänge
        + unterschenkelLänge * unterschenkelLänge - d * d)
        / (2 * oberschenkelLänge * unterschenkelLänge);
    double winkelUnterschenkel = Math
        .acos(Math.max(-1, Math.min(1, cosWinkelUnterschenkel))); // Begrenzung
                                                                  // auf
                                                                  // den
                                                                  // Wertebereich
                                                                  // [-1,
                                                                  // 1]

    // Berechnung des winkelOberschenkel
    double cosWinkelOberschenkel = (d * d
        + oberschenkelLänge * oberschenkelLänge
        - unterschenkelLänge * unterschenkelLänge)
        / (2 * oberschenkelLänge * d);
    double winkelOberschenkel = Math
        .acos(Math.max(-1, Math.min(1, cosWinkelOberschenkel)));

    // Winkel zwischen der Zielrichtung und der Horizontalen
    double zielWinkel = Math.atan2(deltaY, deltaX);
    double winkelOberschenkelFinal = zielWinkel
        + winkelOberschenkel;

    // Beschränkungen auf die Oberschenkel- und
    // Unterschenkelwinkel anwenden
    winkelOberschenkelFinal = beschränkeWinkel(
        winkelOberschenkelFinal, minWinkelOberschenkel,
        maxWinkelOberschenkel);
    winkelUnterschenkel = beschränkeWinkel(winkelUnterschenkel,
        minWinkelUnterschenkel, maxWinkelUnterschenkel);

    return createLegRenderableFromWinkel(position,
        Math.toDegrees(winkelOberschenkelFinal),
        Math.toDegrees(winkelUnterschenkel), 0);

  }

  // Methode zur Beschränkung der Winkel
  private double beschränkeWinkel(double winkel,
      double minWinkel, double maxWinkel) {
    return Math.max(minWinkel, Math.min(maxWinkel, winkel));
  }

  public LegRenderable calculateStep(Point position,
      int currentStep) {

    double winkelOberschenkel = toOberschenkelWinkel(
        currentStep);

    double winkelUnterschenkel = toUnterschenkelWinkel(
        currentStep);

    double winkelFuss = toFussWinkel(currentStep);

    return createLegRenderableFromWinkel(position,
        winkelOberschenkel, winkelUnterschenkel, winkelFuss);

  }

  private LegRenderable createLegRenderableFromWinkel(
      Point position, double winkelOberschenkel,
      double winkelUnterschenkel, double winkelFuss) {
    Point o1S = position.clone()
        .translate(position);
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
