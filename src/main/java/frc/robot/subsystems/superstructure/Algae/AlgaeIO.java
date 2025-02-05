package frc.robot.subsystems.superstructure.Algae;

import edu.wpi.first.wpilibj2.command.Command;

public interface AlgaeIO {

    //declare algae states
    //starts with letter of corresponding system
    enum Astate{
        IDLE(0,0),
        INTAKE(45, -1),
        OUTTAKE(25, 1);
    
        public double POSITION;
        public double OUTPUT;

        private Astate(double POSITION, double OUTPUT){
            this.POSITION = POSITION;
            this.OUTPUT = OUTPUT;
        }

    }

    //all implmenting classes must set the algae intake's state
    public Command setState(Astate state);

    //misc for data logging or other
    public void periodic();

}
