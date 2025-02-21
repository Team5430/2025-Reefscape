package frc.robot.subsystems.superstructure.Coral;

import edu.wpi.first.wpilibj2.command.Command;

public interface CoralIO {
    
    enum CoralState{
        IDLE(0),
        RAISE(128),
        LOWER(-128);

    public double POSITION;
        
    //Function to set state for enum states above
    private CoralState(double POSITION){
            this.POSITION = POSITION;

        }
    }

        public Command setState(CoralState state);
        public void periodic();
    
}
