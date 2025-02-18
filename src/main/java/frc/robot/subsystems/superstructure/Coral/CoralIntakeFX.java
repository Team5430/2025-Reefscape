package frc.robot.subsystems.superstructure.Coral;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class CoralIntakeFX implements CoralIO {



    private Talon pivotMotor = new Talon(0);
    private Talon wristMotor = new Talon(1);
    private Talon intakeMotor = new Talon(2);

    private CoralState savedState = CoralState.IDLE;


    public Command setState(CoralState state) {
        savedState = state;
        return Commands.runOnce(
            () -> {
                pivotMotor.set(state.PIVOT_POSITION);
                wristMotor.set(state.WRIST_POSITION);
                intakeMotor.set(state.OUTPUT);
            }
        )
        .withName("Coral State: " + state.toString());

    }

    public void periodic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'periodic'");
    }
    
}
