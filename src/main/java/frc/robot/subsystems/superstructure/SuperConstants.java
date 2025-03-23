package frc.robot.subsystems.superstructure;

public class SuperConstants {


    public class AlgaeConstants {
        public static final int ROLLER_CANID = 20;
        public static final int R_PIVOT_CANID = 21;
        public static final int L_PIVOT_CANID = 22;

    
        //pivot gear ratio
        public static final double PIVOT_GRATIO = 1;

        //TODO: tune these values

        //PID values for pivot
        public static final double PIVOT_KP = 5;
        public static final double PIVOT_KI = 0;
        public static final double PIVOT_KD = 0;

        //current limit for pivot
        public static final int PIVOT_CURRENT_LIMIT = 30;
    
        
    }

    public class CoralConstants{
        public static final int PIVOT_CANID = 30;
        public static final int INTAKE_CANID = 31;

        //pivot gear ratio
        public static final double PIVOT_GRATIO = 1;

        //wrist gear ratio
        public static final double WRIST_GRATIO = 1;


    }

    public class ElevatorConstants{
        public static final int L_CANID = 40;
        public static final int R_CANID = 41; 

        //elevator gear ratio
        public static final int ELEVATOR_GRATIO = 7;

        //PID constants
        public static final int ELEVATOR_KP = 1;

    }
    
}
