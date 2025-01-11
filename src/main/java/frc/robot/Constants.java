package frc.robot;

import com.team5430.swerve.SwerveModuleConstants;
import edu.wpi.first.math.geometry.Pose3d;

public  class Constants {

  public static SwerveModuleConstants SwerveConstants = new SwerveModuleConstants();

  public static class CANConstants {
    public static int LeftHangMotor = 10;
    public static int RightHangMotor = 11;
  } 

  public static class VisionConstants {
    // position of camera relative to the robots center
    public static Pose3d CameraToRobot = new Pose3d();
    // name of camera as set in the settings
    public static String CameraName = "5430_Camera";
    // Tag ids to track; ignore others
    public static int[] ids = {1, 2, 3};
  }
}
