package com.github.schuettec.cobra2Dexamples.walkinganimation;

import java.util.List;

import com.github.schuettec.cobra2d.math.Point;

public interface LegAnimationController {

  public record AnimationResult(Point bodyPosition,
      Point targetPoint, List<Point> debugPoints) {
  }

  AnimationResult calculateTargetByStep(Point worldCoordinates,
      double currentStep);

}
