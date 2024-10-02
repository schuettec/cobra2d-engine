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

	record LegRenderable(Point oberschenkelStart, Point oberschenkelEnde, Point unterschenkelStart,
			Point unterschenkelEnde, Point fußStart, Point fußEnde) {

		public void render(RendererAccess renderer, Point position) {
			renderer.drawLine(oberschenkelStart.getRoundX(), oberschenkelStart.getRoundY(),
					oberschenkelEnde.getRoundX(), oberschenkelEnde.getRoundY(), Color.BLUE);
			renderer.drawLine(unterschenkelStart.getRoundX(), unterschenkelStart.getRoundY(),
					unterschenkelEnde.getRoundX(), unterschenkelEnde.getRoundY(), Color.GREEN);
			renderer.drawLine(fußStart.getRoundX(), fußStart.getRoundY(), fußEnde.getRoundX(), fußEnde.getRoundY(),
					Color.YELLOW);

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
		public LegBuilder setVerhältnisOberschenkelUnterschenkel(double verhältnis) {
			this.verhältnisOberschenkelUnterschenkel = verhältnis;
			return this;
		}

		public LegBuilder setLängeFuß(double länge) {
			this.längeFuß = länge;
			return this;
		}

		public LegBuilder setWinkelVorschwungOberschenkel(double winkel) {
			this.winkelVorschwungOberschenkel = winkel;
			return this;
		}

		public LegBuilder setWinkelRückschwungOberschenkel(double winkel) {
			this.winkelRückschwungOberschenkel = winkel;
			return this;
		}

		public LegBuilder setWinkelVorschwungUnterschenkel(double winkel) {
			this.winkelVorschwungUnterschenkel = winkel;
			return this;
		}

		public LegBuilder setWinkelRückschwungUnterschenkel(double winkel) {
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
		this.maxWinkelOberschenkel = winkelVorschwungOberschenkel + winkelRückschwungOberschenkel;

		this.winkelVorschwungUnterschenkel = builder.winkelVorschwungUnterschenkel;
		this.winkelRückschwungUnterschenkel = builder.winkelRückschwungUnterschenkel;
		this.maxWinkelUnterschenkel = winkelVorschwungUnterschenkel + winkelRückschwungUnterschenkel;

		this.winkelVorschwungFuß = builder.winkelVorschwungFuß;
		this.winkelRückschwungFuß = builder.winkelRückschwungFuß;
		this.maxWinkelFuß = winkelVorschwungFuß + winkelRückschwungFuß;

		this.winkelNeutral = builder.winkelNeutral;
		this.maxStep = builder.maxStep;
		this.legLength = builder.legLength;
	}

	public static LegBuilder newLeg() {
		return new LegBuilder();
	}

	public LegRenderable calculateStep(Point position, int currentStep) {

		double unterschenkelLänge = legLength / (verhältnisOberschenkelUnterschenkel + 1);
		double oberschenkelLänge = legLength - unterschenkelLänge;

		double winkelOberschenkel = toOberschenkelWinkel(currentStep);

		Point o1S = position.clone().translate(position);
		Point o1E = Math2D.getCircle(o1S, oberschenkelLänge, winkelOberschenkel);

		double winkelUnterschenkel = toUnterschenkelWinkel(currentStep);

		Point u1S = o1E.clone();
		Point u1E = Math2D.getCircle(u1S, unterschenkelLänge, winkelUnterschenkel);

		double winkelFuss = toFussWinkel(currentStep);

		Point f1S = u1E.clone();
		Point f1E = Math2D.getCircle(f1S, längeFuß, winkelFuss);

		return new LegRenderable(o1S, o1E, u1S, u1E, f1S, f1E);

	}

	private double toFussWinkel(int step) {
		return winkelNeutral + winkelVorschwungFuß
				- (maxWinkelFuß / 2.0d) * (Math.cos(((2d * Math.PI) / maxStep) * step + Math.PI) + 1d);
	}

	/**
	 * @param step Time 0-{@link #maxStep}
	 * @return Gibt den Winkel des Oberschenkels zurück normalisiert in Welt-Winkel.
	 */
	private double toOberschenkelWinkel(double step) {
		return winkelNeutral + winkelVorschwungOberschenkel
				- (maxWinkelOberschenkel / 2.0d) * (Math.cos(((2d * Math.PI) / maxStep) * step + Math.PI) + 1d);
	}

	/**
	 * @param step Time 0-{@link #maxStep}
	 * @return Gibt den Winkel des Oberschenkels zurück normalisiert in Welt-Winkel.
	 */
	private double toUnterschenkelWinkel(double step) {
		return winkelNeutral + winkelVorschwungUnterschenkel
				- (maxWinkelUnterschenkel / 2.0d) * (Math.cos(((2d * Math.PI) / maxStep) * step + Math.PI) + 1d);
	}

}
