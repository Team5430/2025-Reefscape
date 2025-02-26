import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import frc.robot.subsystems.superstructure.Superstructure;
import frc.robot.subsystems.superstructure.Algae.AlgaeIntakeSRX;

public class SuperStructureTest {
    
    @Test
    public void createSuperStructure() {
        // Instantiate SuperStructure
        try {
          new Superstructure(
            new AlgaeIntakeSRX()
          );
        } catch (Exception e) {
          e.printStackTrace();
          fail("Failed to instantiate the SuperStructure, see stack trace above.");
        }
      }
}
