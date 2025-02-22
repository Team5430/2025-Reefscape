package frc.robot.subsystems.superstructure.Coral;
//library of all commmands to use for talonfx and teleop+auto commands
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
//use CoralIO to implement states for line 18
public class CoralIntakeFX implements CoralIO {


//declaring the motor and ID
    private Talon pivotMotor = new Talon(0);
    private Talon wristMotor = new Talon(1);
    private Talon intakeMotor = new Talon(2);

    private CoralState savedState = CoralState.IDLE;

//Command that run the motors to move in the state of position
    public Command setState(CoralState state) {
        savedState = state;
        return Commands.runOnce(
            () -> {
                pivotMotor.set(state.PIVOT_POSITION);
                wristMotor.set(state.WRIST_POSITION);
                intakeMotor.set(state.OUTPUT);
            }
        )
            //after, print what state it is in the driver station log
        .withName("Coral State: " + state.toString());

    }

    public void periodic() {
        // TODO Auto-generated method stub, (required to run with this function)
        throw new UnsupportedOperationException("Unimplemented method 'periodic'");
    }
    
}
