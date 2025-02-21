// Copyright (c) 2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.trajectory;

import static org.littletonrobotics.vehicletrajectoryservice.VehicleTrajectoryServiceOuterClass.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import java.util.*;
import java.util.function.Function;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod({TrajectoryGenerationHelpers.class})
public class DriveTrajectories {
  public static final Map<String, List<PathSegment>> paths = new HashMap<>();
  public static final List<Function<Set<String>, Map<String, List<PathSegment>>>> suppliedPaths =
      new ArrayList<>(); // List of functions that take a set of completed paths and return a map of

  // trajectories to generate (or null if they cannot be generated yet)

  // Drive straight path
  // (Used for preload of trajectory classes in drive constructor)
  static {
    paths.put(
        "driveStraight",
        List.of(
            PathSegment.newBuilder()
                .addPoseWaypoint(Pose2d.kZero)
                .addPoseWaypoint(new Pose2d(3.0, 2.0, Rotation2d.fromDegrees(180.0)))
                .build()));

    paths.put(
        "BLOB",
        List.of(
            PathSegment.newBuilder()
                .addPoseWaypoint(Pose2d.kZero)
                .addTranslationWaypoint(new Translation2d(2, 3))
                .addPoseWaypoint(new Pose2d(0, 3, Rotation2d.fromDegrees(270.0)))
                .addPoseWaypoint(new Pose2d(2, 0.6, Rotation2d.fromDegrees(30.0)))
                .build()));
  }
}