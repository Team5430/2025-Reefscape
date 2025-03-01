package frc.robot.subsystems.superstructure.Algae;

public interface AlgaeIO {

    //declare algae states
    //starts with letter of corresponding system
    enum Algaestate{
        IDLE(0,0),
        INTAKE(45, -1),
        OUTTAKE(25, 1);
    
        public double POSITION;
        public double OUTPUT;

        private Algaestate(double POSITION, double OUTPUT){
            this.POSITION = POSITION;
            this.OUTPUT = OUTPUT;
        }

    }

    //all implmenting classes must set the algae intake's state
    public void setState(Algaestate state);

    //misc for data logging or other
    public void periodic();

}
