package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import com.team5430.vision.LimelightHelpers;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
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

    

    public Odometry(DriveControlSystem drive) {

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
                mDrive::autoControl,
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

        //update vision
    //    mVision.setPose2d(getPose2d());

        //add vision measurement
  //  mPoseEstimator.addVisionMeasurement(mVision.getVisionEstimate().get().getPose2d(), mVision.getVisionEstimate().get().getTimestamp());

    }
    

    //get pose2d
    public Pose2d getPose2d() {

        return pose2dReference == null ? new Pose2d() : pose2dReference.get();

    }

    //reset pose2d
    public void resetPose2d(Pose2d poseSupplier) {
         mPoseEstimator.resetPosition(mDrive.getRotation2d(), mDrive.getModulePositions(), poseSupplier);
    }
    

    //help to run pre made paths in deploy/pathplanner/paths
    private Command runPath(String pathName){
        //try to run path
            return new DeferredCommand(() -> {
                try {
                    return AutoBuilder.followPath(PathPlannerPath.fromPathFile(pathName));
        //catch any problems with running path
                } catch (FileVersionException | IOException | ParseException e) {
                    DriverStation.reportError("ODOMETRY THREAD: " + e.getMessage(), true);
                }
                        //run nothing if it fails
                    return Commands.none();
                }, 
                 Set.of(mDrive));

    }
 
        



}