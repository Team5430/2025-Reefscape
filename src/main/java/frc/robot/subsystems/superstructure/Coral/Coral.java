package frc.robot.subsystems.superstructure.Coral;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Coral implements CoralIO{
//declared wantedState that will be updated over time.
    //By default for every robot turned on, set it to IDLE
    private CoralState savedState = CoralState.IDLE;

    //declare motor variables
    private TalonFX CoralArmPivot;
    private TalonFX CoralGripperPivot;
    private TalonSRX CoralGripper;

    //initalize all the FX and SRX libraries into the motor variables to be used as FX and SRX
    private Coral(){
        CoralArmPivot = new TalonFX(0);
        CoralGripperPivot = new TalonFX(0);
        CoralGripper = new TalonSRX(0);
    }

    
    private void MotorConfig(){

        var talonFXConfigs = new TalonFXConfiguration();
        var slot0Configs = talonFXConfigs.Slot0;
        

        slot0Configs.kP = 1.5;
        //slot0 to have propotional saved as 1.5
        //up to slot3 available for switching PID profiles


        CoralArmPivot.getConfigurator().apply(talonFXConfigs);
        CoralGripperPivot.getConfigurator().apply(talonFXConfigs);
    }

        //Return the states to the "command scheduler " that determines which function or end of state to run 
    public Command LOWER(){
        return setState(CoralState.LOWER);
    }
    public Command RAISE(){
        return setState(CoralState.RAISE);
    }
    public Command IDLE(){
        return setState(CoralState.IDLE);
    }

    //save the states into a triggers to be checked or displayed 
    public Trigger LOWER = new Trigger(() -> savedState == CoralState.LOWER);
    public Trigger RAISE = new Trigger(() -> savedState == CoralState.RAISE);
    public Trigger IDLE = new Trigger(() -> savedState == CoralState.IDLE);


    



    @Override
    public Command setState(CoralState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setState'");
    }

    @Override
    public void periodic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'periodic'");
    }
}
