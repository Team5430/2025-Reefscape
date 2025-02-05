package frc.robot.subsystems.superstructure;

import frc.robot.subsystems.superstructure.Algae.AlgaeIntake;


//enum for superstructure states containing all possible states for the superstructure
public enum SuperState {
    
//TODO: LIST DOWN ALL STATES AND THEIR CORRESPONDING INNER STATES
    IDLE(AlgaeIntake.state.IDLE),
    CORAL_STATION(AlgaeIntake.state.INTAKE);


    AlgaeIntake.state algaeState;


    private SuperState(AlgaeIntake.state algaeState){
        this.algaeState = algaeState;

    }

}
