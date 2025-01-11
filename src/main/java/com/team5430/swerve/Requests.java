package com.team5430.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

/**
 * MODELED AFER CTRE SWERVE REQUESTS
 * <p>
 * because they look super cool
 * </p>
 * The Requests interface defines methods for obtaining chassis speeds and robot angle,
 * and for setting chassis speeds.
 * 
 * RequestParammeters holds a Pose2d that is used to get x and y translation
 * and rotational translation.
 * 
 * Requests are used to control the robot's movement.
 * such as {@code FieldCentricRequest}, {@code RobotCentricRequest}, {@code StopRequest}, and {@code TestRequest}.
 * This can be used with any motors, it just needs setup for the usage of ChassisSpeeds.
 * 
 */
public interface Requests {

//TODO: add lock pose request ?? add tank drive request ???

    //speeds to apply
    public ChassisSpeeds Speeds();
    //modify speeds
    public Requests withSpeeds(ChassisSpeeds speeds);
    //get robot angle
    public Rotation2d robotAngle();


    //parameters for requests
    public class RequestParammeters{
        public Pose2d pose;
    }
    
    //request for field centric driving
    public class FieldCentricRequest implements Requests{

        private ChassisSpeeds speedsToApply;
        private Rotation2d robotAngle2d;
        private double x;
        private double y;
        private double rot;
        

        //if there is parameters use inputs other wise use withSpeeds
        public FieldCentricRequest(RequestParammeters params){
            speedsToApply = new ChassisSpeeds();
            this.robotAngle2d = params.pose.getRotation();
            this.x = 0;
            this.y = 0;
            this.rot = 0;
        }

        public FieldCentricRequest(){
            this.x = 0;
            this.y = 0;
            this.rot = 0;
            this.robotAngle2d = new Rotation2d();
        }
        
    //method chaining for setting x, y, and rot
        public FieldCentricRequest withX(double x){
            this.x = x;
            return this;
        }

        public FieldCentricRequest withY(double y){
            this.y = y;
            return this;
        }

        public FieldCentricRequest withRot(double rot){
            this.rot = rot;
            return this;
        }

        public FieldCentricRequest withRobotAngle(Rotation2d angle){
            this.robotAngle2d = angle;
            return this;
        }


//updating in speeds here when given inputs vs withSpeed ????????
        public ChassisSpeeds Speeds(){            
            return speedsToApply; 
        }
//setting speeds
        public FieldCentricRequest withSpeeds(ChassisSpeeds speeds){
            this.speedsToApply = speeds;
            return this;
        }
//apply given inputs 
        public FieldCentricRequest apply(){
            this.speedsToApply = new ChassisSpeeds(x, y, rot);
            return this;
        }
//get reported robot angle
        public Rotation2d robotAngle(){
            return robotAngle2d;
        }

    }

    //request for robot centric driving
    public class RobotCentricRequest implements Requests{
            
        private ChassisSpeeds speedsToApply;

        private double x;
        private double y;
        private double rot;
        

        public RobotCentricRequest(RequestParammeters params){
            speedsToApply = new ChassisSpeeds();
            this.x = 0;
            this.y = 0;
            this.rot = 0;
        }

        public RobotCentricRequest(){
            this.x = 0;
            this.y = 0;
            this.rot = 0;
        }
    
        public ChassisSpeeds Speeds(){
            return speedsToApply;
        }

        public RobotCentricRequest withX(double x){
            this.x = x;
            return this;
        }

        public RobotCentricRequest withY(double y){
            this.y = y;
            return this;
        }

        public RobotCentricRequest withRot(double rot){
            this.rot = rot;
            return this;
        }

        public RobotCentricRequest apply(){
            this.speedsToApply = new ChassisSpeeds(x, y, rot);
            return this;
        }
         
        public RobotCentricRequest withSpeeds(ChassisSpeeds speeds){
            this.speedsToApply = speeds;
            return this;
        }

        public Rotation2d robotAngle(){
            return new Rotation2d();
        }
    }

    //request for stopping the robot
    public class StopRequest implements Requests{


        public StopRequest(){}
    
        public ChassisSpeeds Speeds(){
            return new ChassisSpeeds();
        }

        public StopRequest withSpeeds(ChassisSpeeds speeds){
            return this;
        }

        public Rotation2d robotAngle(){
            return new Rotation2d();
        }
    }

    //request for testing the robot
    public class TestRequest implements Requests{

        private ChassisSpeeds speedsToApply = new ChassisSpeeds( );

        public TestRequest(){
            speedsToApply = new ChassisSpeeds(.1, .1 , Math.PI/4);
        }
        public ChassisSpeeds Speeds(){
            return speedsToApply;
        }

        public TestRequest withSpeeds(ChassisSpeeds speeds){
            speedsToApply = speeds;
            return this;
        }

        public Rotation2d robotAngle(){
            return new Rotation2d();
        }
    }

}
