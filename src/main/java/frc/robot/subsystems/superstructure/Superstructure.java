package frc.robot.subsystems.superstructure;

import com.team5430.control.ControlSystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.superstructure.Algae.AlgaeIO;


public class Superstructure extends ControlSystem{


    SuperState savedState = SuperState.IDLE;


//declare all superstructure components
    AlgaeIO algae;

//constructor for all superstructure components
    public Superstructure(AlgaeIO algae){
        this.algae = algae;


    //defaults all to idle
        setDefaultCommand(
            IDLE()
        );

    }

    //set superstructure state
    private Command setState(SuperState state){
        savedState = state;
        return Commands.runOnce(
            () -> {
                algae.setState(state.algaeState);
            }, this
            
        ).withName("Superstructure State: " + state.toString());
    }

    //commands for superstructure
    public Command ALGAE_IN(){
        return setState(SuperState.ALGAE_IN);
    }

    public Command ALGAE_OUT(){
        return setState(SuperState.ALGAE_OUT);
    }

    public Command CORAL_STATION(){
        return setState(SuperState.CORAL_STATION);
    }

    public Command IDLE(){
        return setState(SuperState.IDLE);
    }



//TODO: implement
    @Override
    public void Stop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Stop'");
    }

    @Override
    public boolean configureTest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'configureTest'");
    }

    @Override
    public boolean checkStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkStatus'");
    }
    
    @Override
    public void  periodic() {
        algae.periodic();    
    }
}
