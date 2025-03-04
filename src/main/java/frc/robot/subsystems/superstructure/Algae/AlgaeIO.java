package frc.robot.subsystems.superstructure.Algae;

import edu.wpi.first.wpilibj2.command.Command;

public interface AlgaeIO {

    //declare algae states
    //starts with letter of corresponding system
    enum Algaestate{
        IDLE(-303, 0),
        INTAKE(-341, -.3),
        KEEP_BALL(-341, 0),
        OUTTAKE(-360, .6);
    
        public double POSITION;
        public double OUTPUT;

        private Algaestate(double POSITION, double OUTPUT){
            this.POSITION = POSITION;
            this.OUTPUT = OUTPUT;
        }

    }


    public Command IDLE();

    public Command INTAKE();

    public Command KEEP_BALL();

    public Command OUTTAKE();

    //misc for data logging or other
    public void periodic();

}
