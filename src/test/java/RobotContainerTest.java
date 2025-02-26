import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import edu.wpi.first.hal.HAL;
import frc.robot.RobotContainer;

public class RobotContainerTest {

    @Test
    public void createRobotContainer() {
      // Instantiate RobotContainer
      try {
        assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
        new RobotContainer();
      } catch (Exception e) {
        e.printStackTrace();
        fail("Failed to instantiate the RobotContainer, see stack trace above.");
      }
    }

}
