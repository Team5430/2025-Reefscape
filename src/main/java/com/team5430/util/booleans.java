package com.team5430.util;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;

import java.util.function.BooleanSupplier;

public class booleans {

  private booleans() {}

  public enum RobotType{
    SIM_ROBOT,
    REAL_ROBOT,
    TUNING_ROBOT
  }

  public static BooleanSupplier shouldFlip() {
    return   () -> {
      // Boolean supplier that controls when the path will be mirrored for the red alliance
      // This will flip the path being followed to the red side of the field.
      // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

      var alliance = DriverStation.getAlliance();
      if (alliance.isPresent()) {
        return alliance.get() == DriverStation.Alliance.Red;
      }
      return false;  
    };
  }
  
  public static BooleanSupplier isTeleop() {
    return DriverStation::isTeleop;
  }

  public static BooleanSupplier isAutonomous() {
    return DriverStation::isAutonomous;
  }

  public static RobotType getRobot() {
    if(!Robot.isReal()){
      return RobotType.SIM_ROBOT;
    }
      return RobotType.REAL_ROBOT;
  }
  
  public static BooleanSupplier RobotisReal(){
    return () -> getRobot() == RobotType.REAL_ROBOT;
  }
}
