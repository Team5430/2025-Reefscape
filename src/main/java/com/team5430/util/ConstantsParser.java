package com.team5430.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.wpilibj.Filesystem;

public class ConstantsParser {
    
    private final SubsystemProperties mechanismJson;
    private LocalConstants localConstants;

    public ConstantsParser(String FileDirectory) throws IOException {

        //parse the json file
        File jsonFile = new File(Filesystem.getDeployDirectory(), FileDirectory);
        if (!jsonFile.exists() || !jsonFile.canRead()) {
            throw new IOException("File not found or not readable: " + jsonFile.getAbsolutePath());
        }

        mechanismJson = new ObjectMapper().readValue(jsonFile, SubsystemProperties.class);
        
        // make sure the file is not empty
        assert mechanismJson != null;
    }

    public LocalConstants createLocalConstants() {
        if(localConstants == null) {
            return new LocalConstants(mechanismJson);
        }
        return localConstants;
    }

    public class LocalConstants {
        private final SubsystemProperties mechanismJson;

        public LocalConstants(SubsystemProperties mechanismJson) {
            this.mechanismJson = mechanismJson;
        }

        public double[] getGearRatio() {
            return mechanismJson.gearRatio;
        }

        public double[] getMaxVelocity() {
            return mechanismJson.maxVelocity;
        }

        public double[] getKP() {
            return mechanismJson.kP;
        }

        public double[] getKI() {
            return mechanismJson.kI;
        }

        public double[] getKD() {
            return mechanismJson.kD;
        }

        public int[] getCANids() {
            return mechanismJson.CANids;
        }

        public boolean[] getIsInverted() {
            return mechanismJson.isInverted;
        }

        public String getName() {
            return mechanismJson.name;
        }

        public double[] getMinPosition() {
            return mechanismJson.minPosition;
        }

        public double[] getMaxPosition() {
            return mechanismJson.maxPosition;
        }

    }
}
