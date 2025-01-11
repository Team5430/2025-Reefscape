package frc.robot.subsystems;

import java.util.concurrent.atomic.AtomicReference;

import com.team5430.vision.LimeLight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;

public class Vision extends SubsystemBase {

  // TODO: integrate vision with pose estimator

  // init camera
  protected LimeLight camera =
      new LimeLight(Constants.VisionConstants.CameraName, Constants.VisionConstants.CameraToRobot);

  // init led control
  protected LimeLight.LEDs led_control = camera.new LEDs();

  protected static Vision mInstance = new Vision();

    private final AtomicReference<Pose2d> pose2dRef = new AtomicReference<>(new Pose2d());

  public static Vision getInstance(){
    return mInstance;
  }

  public Vision() {}

  // add triggers
  public Trigger TagInRange() {
    return camera.AprilTagDetected;
  }


  //Megatag2 getter for pose !!!
  public Pose2d getPose2d(double robotAngle){
    Pose2d newPose = camera.getPose2d(robotAngle);
    pose2dRef.set(newPose);
    return newPose;
  }

  // Get the latest pose in a thread-safe manner
  public Pose2d getLatestPose() {
      return pose2dRef.get();
  }  

  /**timestamp of {@code getPose2d}*/
  public double getPoseTimestamp(){
    return camera.poseEstimateTimestamp();
  }

  // math w/ camera
  public double proportionalAim() {
    return camera.ProportinalAim(Constants.SwerveConstants.MAX_OMEGA_RADIANS);
  }

  public double proportionalRange() {
    return camera.ProportinalRange(Constants.SwerveConstants.MAX_VELOCITY_MPS);
  }

  @Override
  public void periodic() {
  //update camera results 
    camera.updateResults();
    //TODO: modify periodic loop time for vison; add practical functionality to {@code Limelight.LEDs}


  }

}
