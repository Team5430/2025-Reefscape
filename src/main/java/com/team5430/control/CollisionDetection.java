// Inspired by NavX-Mxp example for collision detection
package com.team5430.control;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import java.util.function.BooleanSupplier;

public class CollisionDetection {
  // TODO Test in real life to see performance
  
    static BuiltInAccelerometer accelerometer;
    // threshold to be tuned for robot
    double CollisionThreshold = .3f;
  
    double PreviousLinearXAccel;
  
    double PreviousLinearYAccel;
    // filters to average out output to mitigate noise
    LinearFilter xFilter = LinearFilter.movingAverage(10);
  
    LinearFilter yFilter = LinearFilter.movingAverage(10);
  
    /** Collision Detection based on RIO's accelerometer! */
  public CollisionDetection() {
      accelerometer = new BuiltInAccelerometer();
    }


  // use if needed for triggers
  public BooleanSupplier getDetection = this::CollisionDetected;

  public boolean CollisionDetected() {
    // calculate jerk and see utilise any spikes too come out as true
    double LinearXAccel = accelerometer.getX();
    double currentJerkX = LinearXAccel - PreviousLinearXAccel;
    PreviousLinearXAccel = LinearXAccel;

    double LinearYAccel = accelerometer.getY();
    double currentJerkY = LinearYAccel - PreviousLinearYAccel;
    PreviousLinearYAccel = LinearYAccel;

    double filteredXJerk = xFilter.calculate(currentJerkX);
    double filteredYJerk = yFilter.calculate(currentJerkY);

    return (Math.abs(filteredXJerk) > CollisionThreshold)
        || (Math.abs(filteredYJerk) > CollisionThreshold);
  }

}
