package com.github.schuettec.cobra2d.renderer.j2d;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class TransparencyFilter extends RGBImageFilter {

	private final int transparentRGB;

	public TransparencyFilter(final Color color) {
		this.transparentRGB = color.getRGB();
	}

	@Override
	public int filterRGB(final int x, final int y, final int rgb) {
		if (rgb != this.transparentRGB) {
			return rgb | 0xff000000;
		}

		return rgb & 0xffffff;
	}

}
