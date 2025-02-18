package frc.robot.subsystems.superstructure;

import frc.robot.subsystems.superstructure.Algae.AlgaeIO.Algaestate;
import frc.robot.subsystems.superstructure.Coral.CoralIO.CoralState;

public enum SuperState {
    IDLE(Algaestate.IDLE, CoralState.IDLE),
    CORAL_STATION(Algaestate.IDLE, CoralState.CORAL_STATION),;

    public Algaestate algaeState;
    public CoralState coralState;

    private SuperState(Algaestate WantedalgaeState, CoralState WantedcoralState) {
        this.algaeState = WantedalgaeState;
        this.coralState = WantedcoralState;
    }
}
