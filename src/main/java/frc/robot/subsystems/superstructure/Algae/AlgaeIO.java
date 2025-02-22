package frc.robot.subsystems.superstructure.Algae;

import edu.wpi.first.wpilibj2.command.Command;

public interface AlgaeIO {

    //declare algae states is the position for every state
    //starts with letter of corresponding system
    enum Algaestate{
        IDLE(0,0),
        INTAKE(45, -1),
        OUTTAKE(25, 1);
        
    //postion is first x, output is y
        public double POSITION;
        public double OUTPUT;
         //function to be used for getting the states and states will give out position and output
        private Algaestate(double POSITION, double OUTPUT){
            this.POSITION = POSITION;
            this.OUTPUT = OUTPUT;
        }

    }

    //all implmenting classes must set the algae intake's state
    public Command setState(Algaestate state);

    //misc for data logging and/or other in periodic
    public void periodic();

}
