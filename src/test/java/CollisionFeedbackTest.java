import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.team5430.control.CollisionDetection;

import edu.wpi.first.hal.HAL;

public class CollisionFeedbackTest {
    

    @Test
    public void createCollisionDetection() {
        // Instantiate Feedback
        try {
          assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
          new CollisionDetection();
        } catch (Exception e) {
          e.printStackTrace();
          fail("Failed to instantiate CollisionDetection, see stack trace above.");
        }
      }
}
