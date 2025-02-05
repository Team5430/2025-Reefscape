package frc.robot.subsystems.superstructure;

import frc.robot.subsystems.superstructure.Algae.AlgaeIO.Astate;

public enum SuperState {
    IDLE(Astate.IDLE),
    CORAL_STATION(Astate.INTAKE);

    Astate algaeState;

    private SuperState(Astate algaeState) {
        this.algaeState = algaeState;
    }
}
