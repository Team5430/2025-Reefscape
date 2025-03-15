import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import frc.robot.subsystems.superstructure.Algae.AlgaeIntakeSRX;
import frc.robot.subsystems.superstructure.Coral.CoralIntakeFX;
import frc.robot.subsystems.superstructure.Elevator.ElevatorFX;

public class SuperStructureTest {
    
    @Test
    public void createSuperStructure() {
        // Instantiate SuperStructure
        try {
          new ElevatorFX();
          new AlgaeIntakeSRX();
          new CoralIntakeFX();
          
        } catch (Exception e) {
          e.printStackTrace();
          fail("Failed to instantiate the SuperStructure, see stack trace above.");
        }
      }
}
