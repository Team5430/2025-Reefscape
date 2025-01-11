package com.team5430.vision;

import com.team5430.vision.LimelightHelpers.LimelightResults;
import com.team5430.vision.LimelightHelpers.LimelightTarget_Fiducial;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.function.BooleanSupplier;

public class LimeLight {

  LimelightResults results;

  protected String LimelightName;
  protected Pose3d CameraToRobot;

  // 2D Targeting data
  double tx; // horizontal offset from crosshair (on dashboard) in degress
  double ty; // Vertical offset from crosshair to target in degrees
  double ta; // Target area (0% to 100% of image)

  // 3D Pose Data
  Pose3d robotPoseField; // Robot's pose in field space
  Pose3d cameraPoseTag; // Camera's pose relative to tag
  Pose3d robotPoseTag; // Robot's pose relative to tag
  Pose3d tagPoseCamera; // Tag's pose relative to camera
  Pose3d tagPoseRobot; // Tag's pose relative to

  double AprilTagid; // Tag ID
  String TagFamily; // Tag family (e.g., "16h5")

  /**
   * Limelight Object that captures and calculates what you need!
   *
   * @param hostname name given to limelight on setup
   * @param offsetFromCenter
   */
  public LimeLight(String hostname, Pose3d offsetFromCenter) {
    LimelightName = hostname;
    CameraToRobot = offsetFromCenter;
    configureCameraPose(CameraToRobot);
    // get results
    results = LimelightHelpers.getLatestResults(hostname);
  }

  private void configureCameraPose(Pose3d pose3d) {
    LimelightHelpers.setCameraPose_RobotSpace(
        LimelightName,
        pose3d.getX(),
        pose3d.getY(),
        pose3d.getZ(),
        // if doesn't work convert to degrees Units.radiansToDegrees()
        pose3d.getRotation().getX(),
        pose3d.getRotation().getY(),
        pose3d.getZ());
  }

  /** Update variables/what is seen */
  public void updateResults() {
    // check for new results, if objects are in sight
    if (results.valid) {

      // AprilTags/Fiducials
      if (results.targets_Fiducials.length > 0) {
        LimelightTarget_Fiducial tag = results.targets_Fiducials[0];
        AprilTagid = tag.fiducialID; // Tag ID
        TagFamily = tag.fiducialFamily; // Tag family (e.g., "16h5")

        // 3D Pose Data
        robotPoseField = tag.getRobotPose_FieldSpace(); // Robot's pose in field space
        cameraPoseTag = tag.getCameraPose_TargetSpace(); // Camera's pose relative to tag
        robotPoseTag = tag.getRobotPose_TargetSpace(); // Robot's pose relative to tag
        tagPoseCamera = tag.getTargetPose_CameraSpace(); // Tag's pose relative to camera
        tagPoseRobot = tag.getTargetPose_RobotSpace(); // Tag's pose relative to robot

        // 2D targeting data
        tx = tag.tx; // Horizontal offset from crosshair
        ty = tag.ty; // Vertical offset from crosshair
        ta = tag.ta; // Target area (0-100% of image)
      }
    }
  }

  // AprilTag Detection
  public boolean isAprilTagDetected() {

    updateResults();
    // check if results exist
    if (results.valid) {
      // see if april tags exist
      return results.targets_Fiducials.length > 0;
    }
    // no apriltags detected
    return false;
  }

  // check if apriltag is in sight
  BooleanSupplier AprilTagInSight = this::isAprilTagDetected;

  // command based usage
  public Trigger AprilTagDetected = new Trigger(AprilTagInSight);

  /**
   * Configure Limelight to only track these IDs
   *
   * @param Fiducials array of wanted IDs to track
   */
  public void SetWantedIDs(int... Fiducials) {
    LimelightHelpers.SetFiducialIDFiltersOverride(LimelightName, Fiducials);
  }

  
  //get pose for pose estimation with vision
  public Pose2d getPose2d(double robotAngle){

    LimelightHelpers.PoseEstimate limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue(LimelightName);

    LimelightHelpers.SetRobotOrientation(LimelightName, robotAngle,0 , 0,0 , 0, 0);
    return limelightMeasurement.pose;

  }

  //timestamp of pose
  public double poseEstimateTimestamp(){
    LimelightHelpers.PoseEstimate limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue(LimelightName);
    return limelightMeasurement.timestampSeconds;
  } 

  /** Proportional rotation value for turning */
  public double ProportinalAim(double MAX_OMEGA) {

    double kP = .35;

    double wantedAngleVelocity = tx * kP;

    // invert since tx is positive when the target is to the right of the crosshair
    // and cap out at max angular velocity
    return -wantedAngleVelocity * MAX_OMEGA ;
  }

  /** Proportinal speed to drive distance to tag */
  public double ProportinalRange(double MAX_MPS) {

    double kP = .125;

    double wantedThrottleVelocity = ty * kP;

    //cap out max velocity
    return wantedThrottleVelocity * MAX_MPS;
  }

  /** Control your LEDs! */
  public class LEDs {
    public void ON() {
      LimelightHelpers.setLEDMode_ForceOn(LimelightName);
    }

    public void OFF() {
      LimelightHelpers.setLEDMode_ForceOff(LimelightName);
    }

    public void BLINK() {
      LimelightHelpers.setLEDMode_ForceBlink(LimelightName);
    }
  }
}
