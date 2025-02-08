package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;
import com.team5430.util.booleans;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.DriveControlSystem;
import frc.robot.subsystems.vision.VisionSub;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.simple.parser.ParseException;

public class OdometryThread implements Runnable {


//TODO: run odom loop on Notifier, use this to run and manage auto paths, for automation, which then gets used in robot Container
    private static final int SLEEP_DURATION_MS = 20;

    //init subsystems
    private final DriveControlSystem mDrive;
    private final VisionSub mVision;

        
    private SwerveDrivePoseEstimator mPoseEstimator;
    public AtomicReference<Pose2d> pose2dReference = new AtomicReference<Pose2d>(getPose2d());
    RobotConfig config;

    //thread management
    private final ExecutorService executorService;
    private Future<?> future;
    private Lock lock = new ReentrantLock();

    public OdometryThread(DriveControlSystem drive, VisionSub vision) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.mDrive = drive;
        this.mVision = vision;

        //get robot config
        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
           throw new RuntimeException();
        }
        
        //configure path planner
        configurePathPlanner();
    }

    //starts the thread through executor service
    public void start() {
        // Submit the thread and store in Future object
        future = executorService.submit(this);
    }

    //stops the thread
    public void stop() {
        // Cancel the task if it is running
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    
        executorService.shutdownNow();
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
    

    //configure path planner
    private void configurePathPlanner() {
        AutoBuilder.configure(
                this::getPose2d,
                this::resetPose2d,
                mDrive::getCurrentSpeeds,
                mDrive::autoControl,
                Constants.SwerveConstants.AUTO_FOLLOWER_CONFIG,
                config,
                booleans.shouldFlip(),
                mDrive);
    }

    //help to run pre made paths in deploy/pathplanner/paths
    private Command runPath(String pathName){
        try {
            return AutoBuilder.followPath(PathPlannerPath.fromPathFile(""));
        } catch (FileVersionException | IOException | ParseException e) {
            DriverStation.reportError("ODOMETRY THREAD: " +e.getMessage(), true);
        }
            return null;
    }

    public void updateOdometry(){

        mPoseEstimator.update(mDrive.getRotation2d(), mDrive.getModulePositions());

        pose2dReference.set(mPoseEstimator.getEstimatedPosition());

        mVision.setPose2d(getPose2d());

        mPoseEstimator.addVisionMeasurement(mVision.getPose2d().get(), Timer.getFPGATimestamp());

    }


    @Override
    public void run() {
        DriverStation.reportWarning("Odometry thread started", false);
        //threading settings
        Thread.currentThread().setName("Odometry Thread");

        mPoseEstimator = new SwerveDrivePoseEstimator(
                Constants.SwerveConstants.KINEMATICS, mDrive.getRotation2d(), mDrive.getModulePositions(), new Pose2d());

                //vision std deviations
        mPoseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.5, .5, .7));

        //while thread is not interrupted
        while (true) {

            try {
                lock.lock();
                mPoseEstimator.update(mDrive.getRotation2d(), mDrive.getModulePositions());

                pose2dReference.set(mPoseEstimator.getEstimatedPosition());

                mVision.setPose2d(getPose2d());

                mPoseEstimator.addVisionMeasurement(mVision.getPose2d().get(), Timer.getFPGATimestamp());

                //thread runs every 20ms
                Thread.sleep(SLEEP_DURATION_MS);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
                
            } catch (Exception e) {
                DriverStation.reportError("Odometry thread exception: " + e.getMessage(), true);

            } finally {
                lock.unlock();
            }
        }
    }
}