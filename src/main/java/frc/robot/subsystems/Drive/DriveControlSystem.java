package frc.robot.subsystems.drive;

import java.util.concurrent.atomic.AtomicReference;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;
import com.team5430.control.ControlSystem;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants;
import frc.robot.subsystems.drive.Requests.*;

public class DriveControlSystem extends ControlSystem {

    // Swerve Config
    private final SwerveModuleConstants mConfig = Constants.SwerveConstants;

    // Swerve DriveTrain
    protected SwerveModuleGroup driveTrain;

    
    // Gyro
    public AHRS mGyro;

    private Rotation2d gyroOffset = new Rotation2d(Math.PI / 2);

    // Rotation2d reference
    public final AtomicReference<Rotation2d> rotation2dRef = new AtomicReference<>(new Rotation2d());

    // Tuning
    private SysIdRoutine TuningRoutine;

    public DriveControlSystem() {
        initializeDriveTrain();
        ResetHeading();
        }

        private void initializeDriveTrain() {
        switch (Constants.getRobot()) {
            case TUNING_ROBOT:
                initializeTuningDriveTrain();
                break;
            case REAL_ROBOT:
                initializeRealDriveTrain();
                break;
            case SIM_ROBOT:
                initializeSimDriveTrain();
                break;
            default:
                DriverStation.reportError("Invalid Robot Type", true);
                break;
        }
    }

    private void initializeTuningDriveTrain() {
        driveTrain = new SwerveModuleGroup(mConfig,
            new SwerveModuleIO(0, mConfig),
            new SwerveModuleIO(1, mConfig),
            new SwerveModuleIO(2, mConfig),
            new SwerveModuleIO(3, mConfig));
        TuningRoutine = driveTrain.SysIdTuning(this);
    }

    private void initializeRealDriveTrain() {
        mGyro = new AHRS(NavXComType.kMXP_SPI);
        driveTrain = new SwerveModuleGroup(mConfig,
            new SwerveModuleIO(0, mConfig),
            new SwerveModuleIO(1, mConfig),
            new SwerveModuleIO(2, mConfig),
            new SwerveModuleIO(3, mConfig));
    }

    private void initializeSimDriveTrain() {
        driveTrain = new SwerveModuleGroup(mConfig,
            new SimModuleIO(),
            new SimModuleIO(),
            new SimModuleIO(),
            new SimModuleIO());
    }

    // Drive methods
    public void control(Requests request) {
        driveTrain.control(request);
    }

    public void autoControl(ChassisSpeeds speeds) {
        driveTrain.control(new RobotCentricRequest().withSpeeds(speeds));
    }

    public synchronized void ResetHeading() {
        if (mGyro != null) {
            mGyro.reset();
        }
    }

    public synchronized Rotation2d getRotation2d() {
        rotation2dRef.set(mGyro != null ? mGyro.getRotation2d().rotateBy(gyroOffset) : driveTrain.getRotation2d());
        return rotation2dRef.get();
    }

    public synchronized SwerveModulePosition[] getModulePositions() {
        return driveTrain.getPositions(true);
    }

    public synchronized SwerveModuleState[] getModuleStates() {
        return driveTrain.getStates(true);
    }

    public ChassisSpeeds getCurrentSpeeds() {
        return driveTrain.getCurrentSpeeds();

    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return TuningRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return TuningRoutine.dynamic(direction);
    }

    @Override
    public boolean configureTest() {
        if (RobotState.isTest()) {
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

    @Override
    public boolean checkStatus() {
        if (mGyro == null || driveTrain == null) return false;
        return mGyro.isConnected() && driveTrain.getCurrentSpeeds().equals(getCurrentSpeeds());
    }

    @Override
    public synchronized void Stop() {
        driveTrain.Stop();
    }


    @Override
    public void simulationPeriodic() {
        // Simulation-specific code
    }
}