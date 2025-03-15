package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.vision.VisionSub;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.json.simple.parser.ParseException;

public class Odometry {


    //init subsystems
    private final DriveControlSystem mDrive;
    private final VisionSub mVision;

    //pose2d thread safe reference
    private final AtomicReference<Pose2d> pose2dReference = new AtomicReference<>(new Pose2d());
  
    //pose estimator
    private final SwerveDrivePoseEstimator mPoseEstimator;
    private final RobotConfig config;

    

    public Odometry(DriveControlSystem drive, VisionSub vision) {

        this.mDrive = drive;
        this.mVision = vision;

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

        //cache pose2d
        pose2dReference.set(mPoseEstimator.getEstimatedPosition());

        //update vision
    //    mVision.setPose2d(getPose2d());

        //add vision measurement
//    mPoseEstimator.addVisionMeasurement(mVision.getVisionEstimate().get().getPose2d(), mVision.getVisionEstimate().get().getTimestamp());

    }
    

    //get pose2d
    @Logged(name = "Pose2d")
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
        try {
            return AutoBuilder.followPath(PathPlannerPath.fromPathFile(pathName));
        //catch any problems with running path
        } catch (FileVersionException | IOException | ParseException e) {
            DriverStation.reportError("ODOMETRY THREAD: " + e.getMessage(), true);
        }
        //return null if path fails
            return null;
    }
 
        



}