package frc.robot.subsystems.superstructure.Coral;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralIntakeFX extends SubsystemBase {


   public enum CoralState {
        IDLE(0.0, 0.5),
        L1(0.1, 0.6),
        L2(0.2, 0.7),
        L3(0.3, 0.8),
        L4(0.4, 0.9),
        CORAL_STATION(0.5, 1.0);

        public double OUTPUT;
        public double WRIST_POSITION;
        public double PIVOT_POSITION;

        private CoralState(double Output, double PivotPosition) {
            this.OUTPUT = Output;
            this.PIVOT_POSITION = PivotPosition;
        }
    }



    
//TODO: REPLACE Talon WITH TALONFX

    private TalonFX pivotMotor = new TalonFX(0);
    private TalonFX wristMotor = new TalonFX(1);
    private TalonFX intakeMotor = new TalonFX(2);

    private CoralState savedState = CoralState.IDLE;

    public void setState(CoralState state) {
        savedState = state;
    
                pivotMotor.set(state.PIVOT_POSITION);
                wristMotor.set(state.WRIST_POSITION);
                intakeMotor.set(state.OUTPUT);
    }


    private String getState(){
        return savedState.name();
    }

    public void periodic() {
        SmartDashboard.putString(getName(), getState());
    }
    
}
