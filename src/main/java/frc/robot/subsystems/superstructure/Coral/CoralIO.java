package frc.robot.subsystems.superstructure.Coral;

import edu.wpi.first.wpilibj2.command.Command;

public interface CoralIO {
    
    enum CoralState {
        IDLE(0.0, 0.4, 0.5),
        L1(0.1, 0.5, 0.6),
        L2(0.2, 0.6, 0.7),
        L3(0.3, 0.7, 0.8),
        L4(0.4, 0.8, 0.9),
        CORAL_STATION(0.5, 0.9, 1.0);

        public double OUTPUT;
        public double WRIST_POSITION;
        public double PIVOT_POSITION;

        private CoralState(double Output, double WristPosition, double PivotPosition) {
            this.OUTPUT = Output;
            this.WRIST_POSITION = WristPosition;
            this.PIVOT_POSITION = PivotPosition;
        }
    }

    public Command setState(CoralState state);

    public void periodic();
}
