package com.team5430.control;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class ControlSystemManager {

        List<ControlSystem> controlSystems;
        private static ControlSystemManager instance;
    
        //**Manage tests for Control Systems with ease! */
        private ControlSystemManager(ControlSystem... controlSystems) {
        //init list
        this.controlSystems = new ArrayList<ControlSystem>();
        }
    
        //get instance
        public static ControlSystemManager getInstance() {
            if (instance == null) {
                instance = new ControlSystemManager();
            }
            return instance;
        }
    
        //add control systems to the list
        public ControlSystemManager addControlSystem(ControlSystem... cs){
            if (instance == null) {
                instance = getInstance();
            }else{
                for (ControlSystem c : cs) {
                    controlSystems.add(c);
                }
            }
            return instance;
        }

    
        public static SendableChooser<Boolean> buildTestChooser() {
            SendableChooser<Boolean> controlSystemChooser = new SendableChooser<Boolean>();
            
            //save list of control systems
            var _controlSystems = getInstance().controlSystems;
            
            //add default option as None
            controlSystemChooser.setDefaultOption("None", false);
    
            //add options to test each control system
            for (ControlSystem cs : _controlSystems ) {
            controlSystemChooser.addOption(cs.getName(), cs.configureTest());
        }

        //assume all tests will pass
        var testAll = true;

        //any failed will cause testAll to be false
        for (ControlSystem cs : _controlSystems) {
            testAll = testAll && cs.configureTest();
        }

        //add option to test all systems
        controlSystemChooser.addOption("Test All Systems [MAKE SURE ROBOT IS ON BLOCKS]", testAll);

        return controlSystemChooser;

        }

        //stop all control systems concurrently
        public void stopAll(){
            for (ControlSystem cs : controlSystems) {
                cs.Stop();
            }
        }   
    
}
