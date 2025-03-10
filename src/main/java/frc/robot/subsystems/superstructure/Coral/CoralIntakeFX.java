package frc.robot.subsystems.superstructure.Coral;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralIntakeFX extends SubsystemBase implements CoralIO {


//TODO: REPLACE Talon WITH TALONFX

    private TalonFX pivotMotor = new TalonFX(0);
    private TalonFX wristMotor = new TalonFX(1);
    private TalonFX intakeMotor = new TalonFX(2);

    private CoralState savedState = CoralState.IDLE;

    @Override
    public Command setState(CoralState state) {
        savedState = state;
        return Commands.runOnce(
            () -> {
                pivotMotor.set(state.PIVOT_POSITION);
                wristMotor.set(state.WRIST_POSITION);
                intakeMotor.set(state.OUTPUT);
            }, this
        )
        .withName("Coral State: " + state.toString());

    }

    public void periodic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'periodic'");
    }
    
}
