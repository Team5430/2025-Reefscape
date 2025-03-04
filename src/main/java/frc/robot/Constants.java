package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.drive.SwerveModuleConstants;

public  class Constants {

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


  public static SwerveModuleConstants SwerveConstants = new SwerveModuleConstants();

  public static class CANConstants {
    public static int LeftHangMotor = 10;
    public static int RightHangMotor = 11;

    
  } 

  public static class VisionConstants {

  //TODO: format and use this rather than plugin in values directly
    // position of camera relative to the robots center
    public static Transform3d CameraToRobot = new Transform3d();
    // name of camera as set in the settings
    public static String CameraName = "limelight-driver";
    // Tag ids to track; ignore others
    public static int[] ids = {1, 2, 3};
    //vision assisted driving PID values
    public static double kP = .7;
    

  }
}
