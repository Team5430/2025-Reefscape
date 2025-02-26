import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import frc.robot.subsystems.drive.DriveControlSystem;

public class DriveTest  {
    
    @Test
    public void createDrive() {
        // Instantiate Drive
        try {
          new DriveControlSystem();
        } catch (Exception e) {
          e.printStackTrace();
          fail("Failed to instantiate the Drive, see stack trace above.");
        }
      }
    
}
