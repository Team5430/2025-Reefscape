package frc.robot.subsystems.superstructure.elevator;

import edu.wpi.first.wpilibj2.command.Command;

public interface ElevatorIO {

     //declare Elevator states for each location
    enum ElevatorState{
        //LEVEL0 is considered idle
        LEVEL0(0),
        LEVEL1(128),
        LEVEL2(290),
        LEVEl3(310);
         //declare variables for function ElevatorState
        public double POSITION;
        public double OUTPUT;

        //Function to set state for enum states above
        private ElevatorState(double POSITION){
            this.POSITION = POSITION;

        }

    }
    
    //all implmenting classes must set the Elevator's state
    public Command setState(ElevatorState state);

    //misc for data logging or other
    public void periodic();

}
