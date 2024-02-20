package com.github.schuettec.cobra2d.utils;

import static java.util.Objects.nonNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TimedBoolean {
  protected Long start;

  public TimedBoolean() {
  }

  public TimedBoolean start() {
    // If not reset
    if (isFalse()) {
      this.start = System.nanoTime();
    }
    return this;
  }

  public void reset() {
    this.start = null;
  }

  public Duration getDuration() {
    long resetTime = System.nanoTime();
    Duration duration = Duration.of(resetTime - start, ChronoUnit.NANOS);
    return duration;
  }

  public boolean isTrue() {
    return nonNull(start);
  }

  public boolean isFalse() {
    return !isTrue();
  }

}
