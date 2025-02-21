package frc.robot.subsystems.drive;

import java.util.concurrent.atomic.AtomicReference;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;
import com.team5430.control.ControlSystem;
import com.team5430.util.booleans;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.Logged.Importance;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.drive.Requests.*;

public class DriveControlSystem extends ControlSystem {

//TODO: make it look pretty !!!
    // Swerve Config
    private final SwerveModuleConstants mConfig = Constants.SwerveConstants;

    // Swerve DriveTrain
    protected SwerveModuleGroup driveTrain;

    // Gyro
    public AHRS mGyro;

    private Rotation2d gyroOffset = new Rotation2d(Math.PI/2);
    // Singleton
    private static DriveControlSystem mInstance = new DriveControlSystem(); 

    public static DriveControlSystem getInstance(){
        return mInstance;
    }

    // Rotation2d reference
    public final AtomicReference<Rotation2d> rotation2dRef = new AtomicReference<>(new Rotation2d());

    // Tuning
    private SysIdRoutine TuningRoutine;

    private DriveControlSystem() {
//TODO: maybe a system wide robot type in @RobotContainer (???)
        // Initialize based on robot type
        switch (booleans.getRobot()) {

            case TUNING_ROBOT:

                //create tuning drivetrain
                driveTrain = new SwerveModuleGroup(mConfig,
                    new SwerveModuleIO(0, mConfig),
                    new SwerveModuleIO(1, mConfig),
                    new SwerveModuleIO(2, mConfig),
                    new SwerveModuleIO(3, mConfig));    

                //save tuning routine
                TuningRoutine = driveTrain.SysIdTuning(this);

                break;

            case REAL_ROBOT:

                //create real drivetrain
                mGyro = new AHRS(NavXComType.kMXP_SPI);
                driveTrain = new SwerveModuleGroup(mConfig,
                    new SwerveModuleIO(0, mConfig),
                    new SwerveModuleIO(1, mConfig),
                    new SwerveModuleIO(2, mConfig),
                    new SwerveModuleIO(3, mConfig));    
    
                break;

            case SIM_ROBOT:

                //create sim drivetrain
                driveTrain = new SwerveModuleGroup(mConfig,
                    new SimModuleIO(),
                    new SimModuleIO(),
                    new SimModuleIO(),
                    new SimModuleIO());
                
                break;

            default:
                DriverStation.reportError("Invalid Robot Type", true);
                break;
            

            }

        ResetHeading();
    }

    // Drive methods
    public void control(Requests request) { 
        driveTrain.control(request);
    }

    //control robot autonmously 
    public void autoControl(ChassisSpeeds speeds) {
        driveTrain.control(new RobotCentricRequest().withSpeeds(speeds));
    }
    
    // Zeros the gyro
    public synchronized void ResetHeading() {
        if (mGyro != null)
        mGyro.reset();
    }
    
//getters
    // Get heading as a Rotation2d
    @Logged(name = "Heading", importance = Importance.INFO)
     public synchronized Rotation2d getRotation2d() {
            //check if gyro is connected or not null before using delta data 
    rotation2dRef.set(mGyro != null ? mGyro.getRotation2d().rotateBy(gyroOffset) : driveTrain.getRotation2d());
        return rotation2dRef.get();

    }

    //get robot modules positions !!!
    public synchronized SwerveModulePosition[] getModulePositions(){
        return Robot.isReal() 
        ? driveTrain.getPositions(true) : driveTrain.getPositions(true);
    }

    //get robot modules states !!!
    @Logged(name = "States", importance = Importance.INFO)
    public synchronized SwerveModuleState[] getModuleStates(){
        return Robot.isReal() 
        ? driveTrain.getStates(true) : driveTrain.getStates(true);
    }
    
    //get robot input chassis speeds 
    public ChassisSpeeds getCurrentSpeeds(){
        return Robot.isReal() 
        ? driveTrain.getCurrentSpeeds() : driveTrain.getCurrentSpeeds();
    }

//for tuning
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return TuningRoutine.quasistatic(direction);
     }
     
     public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return TuningRoutine.dynamic(direction);
     }

//control system implementation
    //run and rotate slowly
    @Override
    public boolean configureTest(){

    if(RobotState.isTest()){
        try {
                control(new TestRequest());
                DriverStation.reportWarning("Drive Test Succeeded", false);
                return true;
            } catch (Exception e) {
                DriverStation.reportError("Drive Test Failed: " + e.getMessage(), true);
                return false;
            }
        }
        
        return false;
    }

    //check if gyro and drivetrain are connected
    @Override
    public boolean checkStatus() {
        if(mGyro == null || driveTrain == null) return false;
        
        return mGyro.isConnected() && driveTrain.getCurrentSpeeds().equals(getCurrentSpeeds());
    }

    // Stops the DriveTrain
    @Override
    public synchronized void Stop() {
        driveTrain.Stop();
        }


    //sim updating
    @Override 
    public void simulationPeriodic(){

    }
    
}