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
             setState(SuperState.IDLE)
        );

    }


    //set superstructure state
    private Command setState(SuperState state){
        savedState = state;
        return Commands.parallel(
            algae.setState(state.algaeState)
        );
    }

    //commands for superstructure


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
