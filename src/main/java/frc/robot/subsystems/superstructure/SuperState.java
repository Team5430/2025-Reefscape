package frc.robot.subsystems.superstructure;

import frc.robot.subsystems.superstructure.Algae.AlgaeIO.Algaestate;
import frc.robot.subsystems.superstructure.Coral.CoralIO.CoralState;

public enum SuperState {
    IDLE(Algaestate.IDLE, CoralState.IDLE),
    CORAL_STATION(Algaestate.IDLE, CoralState.CORAL_STATION),
    ALGAE_IN(Algaestate.INTAKE, CoralState.IDLE),
    ALGAE_OUT(Algaestate.OUTTAKE, CoralState.IDLE),
    ;


    public Algaestate algaeState;
    public CoralState coralState;

    private SuperState(Algaestate WantedalgaeState, CoralState WantedcoralState) {
        this.algaeState = WantedalgaeState;
        this.coralState = WantedcoralState;
    }
}
