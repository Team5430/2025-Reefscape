package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import com.pathplanner.lib.util.FlippingUtil;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.team5430.vision.LimelightHelpers;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.drive.DriveControlSystem;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.json.simple.parser.ParseException;

public class Odometry {


    //init subsystems
    private final DriveControlSystem mDrive;

    //pose2d thread safe reference
    private final AtomicReference<Pose2d> pose2dReference = new AtomicReference<>(new Pose2d());
  
    //pose estimator
    private final SwerveDrivePoseEstimator mPoseEstimator;
    private final RobotConfig config;
    private final Field2d field;

    private final Pose2d proccesor = new Pose2d(6, .5, new Rotation2d(-Math.PI/2));

    private PathConstraints constraints = new PathConstraints(
      3, 3,
      Units.degreesToRadians(540), Units.degreesToRadians(720));

    

    public Odometry(DriveControlSystem drive) {

        this.field = new Field2d();
        this.mDrive = drive;

        //get robot config for path planner
        try {
            //stored in deploy/settings.json
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
           throw new RuntimeException();
        }
        
        //configure path planner
        configurePathPlanner();

        //init pose estimator        
        mPoseEstimator = new SwerveDrivePoseEstimator(Constants.SwerveConstants.KINEMATICS, mDrive.getRotation2d(), mDrive.getModulePositions(), new Pose2d());

        
    }
 
    //configure path planner
    private void configurePathPlanner() {
        AutoBuilder.configure(
                this::getPose2d,
                this::resetPose2d,
                mDrive::getCurrentSpeeds,
                mDrive::control,
                Constants.SwerveConstants.AUTO_FOLLOWER_CONFIG,
                config,
                Constants.shouldFlip(),
                mDrive);
    }

    //update odometry
    public void updateOdometry(){

        //update pose estimator
        mPoseEstimator.update(mDrive.getRotation2d(), mDrive.getModulePositions());
        
            boolean useMegaTag2 = false; //set to false to use MegaTag1
            boolean doRejectUpdate = false;
            //make sure data isnt null
            if(useMegaTag2 == false && LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.VisionConstants.CameraName) != null)
            {
            
              LimelightHelpers.PoseEstimate mt1 = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.VisionConstants.CameraName);
              
              if(mt1.tagCount == 1 && mt1.rawFiducials.length == 1)
              {
                if(mt1.rawFiducials[0].ambiguity > .7)
                {
                  doRejectUpdate = true;
                }
                if(mt1.rawFiducials[0].distToCamera > 3)
                {
                  doRejectUpdate = true;
                }
              }
              if(mt1.tagCount == 0)
              {
                doRejectUpdate = true;
              }
        
              if(!doRejectUpdate)
                {
                mPoseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.5,.5,9999999));
                mPoseEstimator.addVisionMeasurement(
                    mt1.pose,
                    mt1.timestampSeconds);
              }
            }
            //make sure data is not null
            else if (useMegaTag2 == true && LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.CameraName) != null)
            {
              LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.CameraName, mPoseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
              LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.CameraName);
              if(mt2.tagCount == 0)
              {
                doRejectUpdate = true;
              }
              if(!doRejectUpdate)
              {
                mPoseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
                mPoseEstimator.addVisionMeasurement(
                    mt2.pose,
                    mt2.timestampSeconds);
              }
            }
          

        //cache pose2d
        pose2dReference.set(mPoseEstimator.getEstimatedPosition());

            // Logging callback for current robot pose
          PathPlannerLogging.setLogCurrentPoseCallback((pose) -> {
              // Do whatever you want with the pose here
              field.setRobotPose(pose);
          });

          // Logging callback for target robot pose
          PathPlannerLogging.setLogTargetPoseCallback((pose) -> {
              // Do whatever you want with the pose here
              field.getObject("target pose").setPose(pose);
          });

          // Logging callback for the active path, this is sent as a list of poses
          PathPlannerLogging.setLogActivePathCallback((poses) -> {
              // Do whatever you want with the poses here
              field.getObject("path").setPoses(poses);
          });
        
        //field upload
        SmartDashboard.putData(field);
          

    }

    //get pose2d
    public Pose2d getPose2d() {
        return pose2dReference == null ? new Pose2d() : pose2dReference.get();
    }

    //reset pose2d
    public void resetPose2d(Pose2d poseSupplier) {
         mPoseEstimator.resetPosition(mDrive.getRotation2d(), mDrive.getModulePositions(), poseSupplier);
    }
    


}