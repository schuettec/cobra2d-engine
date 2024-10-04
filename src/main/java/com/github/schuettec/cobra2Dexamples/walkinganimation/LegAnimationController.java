package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2d.math.Point;

public interface LegAnimationController {

  public record AnimationResult(Point bodyPosition,
      Point targetPoint) {
  }

  AnimationResult calculateTargetByStep(Point worldCoordinates,
      double currentStep);

}
