import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class RobotContainerTest implements AutoCloseable {

  @Test
  public void createRobotContainer() {
    // Instantiate RobotContainer
    try {
      new RobotContainer();
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to instantiate the RobotContainer, see stack trace above.");
    }
  }
}
