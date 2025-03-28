package frc.robot.subsystems.superstructure.Coral;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorSensorV3.ProximitySensorMeasurementRate;
import com.revrobotics.ColorSensorV3.ProximitySensorResolution;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.superstructure.SuperConstants;

public class CoralIntakeFX extends SubsystemBase {


   public enum CoralState {
        IDLE(0.0, 0.5),
        L1(0.1, 0.6),
        L2(0.2, 0.7),
        L3(0.3, 0.8),
        L4(0.4, 0.9),
        CORAL_STATION(0.5, .7);

        public double OUTPUT;
        public double WRIST_POSITION;
        public double PIVOT_POSITION;

        private CoralState(double Output, double PivotPosition) {
            this.OUTPUT = Output;
            this.PIVOT_POSITION = PivotPosition;
        }
    }

    
//TODO: MOTORS

    private TalonFX pivotMotor = new TalonFX(SuperConstants.CoralConstants.PIVOT_CANID);
    private TalonSRX intakeMotor = new TalonSRX(SuperConstants.CoralConstants.INTAKE_CANID);

//COLOR SENSOR
    private ColorSensorV3 coralDetector = new ColorSensorV3(Port.kOnboard);

    private CoralState savedState = CoralState.IDLE;

    public CoralIntakeFX(){
        coralDetector.configureProximitySensor(ProximitySensorResolution.kProxRes9bit, ProximitySensorMeasurementRate.kProxRate25ms);
        
        motorConfig();
    }

    private void motorConfig(){
        TalonFXConfiguration  pivotMotorConfig = new TalonFXConfiguration();
    
        pivotMotorConfig.Slot0.kP = SuperConstants.CoralConstants.kP;
        pivotMotorConfig.Feedback.SensorToMechanismRatio = SuperConstants.CoralConstants.PIVOT_GRATIO;

 
    }

    public void setState(CoralState state) {
        savedState = state;
    
                pivotMotor.set(state.PIVOT_POSITION);
                intakeMotor.set(TalonSRXControlMode.PercentOutput, state.OUTPUT);
    }

    
    public Command IDLE(){
        return new InstantCommand(
        () -> setState(CoralState.IDLE), this
        );
    }

    public Command L1(){
        return new SequentialCommandGroup(
            new InstantCommand(
        () -> setState(CoralState.L1), this
        ),
            new WaitUntilCommand(() -> { return !getDetected();}),
            IDLE()
        );
    }

    public Command L2(){
        return new SequentialCommandGroup(
            new InstantCommand(
        () -> setState(CoralState.L2), this
        ),
            new WaitUntilCommand(() -> { return !getDetected();}),
            IDLE()
        );
    }

    public Command L3(){
        return new SequentialCommandGroup(
            new InstantCommand(
        () -> setState(CoralState.L3), this
        ),
            new WaitUntilCommand(() -> { return !getDetected();}),
            IDLE()
        );
    }
    
    public Command L4(){
        return new SequentialCommandGroup(
            new InstantCommand(
        () -> setState(CoralState.L4), this
        ),
            new WaitUntilCommand(() -> { return !getDetected();}),
            IDLE()
        );
    }

    public Command CORAL_STATION(){
        return new SequentialCommandGroup(
            new InstantCommand(
        () -> setState(CoralState.CORAL_STATION), this
        ),
            new WaitUntilCommand(this::getDetected),
            IDLE()
        );        
    }
    
    public boolean getDetected(){
        return coralDetector.getProximity() > 50;
    }


    private String getState(){
        return savedState.name();
    }

    public void periodic() {
        SmartDashboard.putString(getName(), getState());
        SmartDashboard.putBoolean("CORAL DETECTED", getDetected());
    }
 }
