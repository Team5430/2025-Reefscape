package com.team5430.util;

import java.util.function.DoubleSupplier;

public class MathHelpers {

  private MathHelpers() {
    throw new AssertionError("utility class");
  }

  public double lastAngle = 0;

  // **the wheel will go to the position that is greater than 0.2, otherwise stop power when less
  // than or equal to*/
  public double deadzone(double angle, double power) {
    // If the input given is less than 0.3 the rotation will reset to 0
    if (power < -0.3) {
      lastAngle = angle;
    }
    return lastAngle;
  }

  /** The bigger the input, smaller the output; meant to mimic breaking in a car */
  public static double VariableSpeedDecline(double input) {
    return 1 - input;
  }

  public static double WrapRad(double wantedRad, double currentRad) {

    if (Math.abs(wantedRad - currentRad) > Math.PI) {
      return wantedRad -= 2 * Math.PI;
    }
    return wantedRad;
  }

  @SuppressWarnings("unused")
  private final DoubleSupplier Zeroed =
          () -> 0;
}
