package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;

import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.vision.VisionSub;

import java.util.concurrent.atomic.AtomicReference;


public class Odometry {


    //init subsystems
    private final DriveControlSystem mDrive;
    private final VisionSub mVision;

    //pose2d thread safe reference
    private final AtomicReference<Pose2d> pose2dReference = new AtomicReference<>(new Pose2d());
  
    //pose estimator
    private final SwerveDrivePoseEstimator mPoseEstimator;

    

    public Odometry(DriveControlSystem drive, VisionSub vision) {

        this.mDrive = drive;
        this.mVision = vision;

        
        //configure path planner
        configurePathPlanner();

        //init pose estimator        
        mPoseEstimator = new SwerveDrivePoseEstimator(Constants.SwerveConstants.KINEMATICS, mDrive.getRotation2d(), mDrive.getModulePositions(), new Pose2d());

        
    }
 
    //configure path planner
    private void configurePathPlanner() {
   
    }

    //update odometry
    public void updateOdometry(){

        //update pose estimator
        mPoseEstimator.update(mDrive.getRotation2d(), mDrive.getModulePositions());

        //cache pose2d
        pose2dReference.set(mPoseEstimator.getEstimatedPosition());

        //update vision
        mVision.setPose2d(getPose2d());

        //add vision measurement
        mPoseEstimator.addVisionMeasurement(mVision.getVisionEstimate().get().getPose2d(), mVision.getVisionEstimate().get().getTimestamp());

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
    

        



}