import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import frc.robot.subsystems.vision.VisionSub;

public class VisionTest {
        
    @Test
    public void createVision() {
        // Instantiate Vision
        try {
        new VisionSub();
        } catch (Exception e) {
        e.printStackTrace();
        fail("Failed to instantiate the Vision, see stack trace above.");
        }
    }
}
