package frc.robot.subsystems.superstructure.Algae;

public interface AlgaeIO {

    //declare algae states
    //starts with letter of corresponding system
    enum Algaestate{
        IDLE(-303, 0),
        INTAKE(-341, .2),
        OUTTAKE(25, .2);
    
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
