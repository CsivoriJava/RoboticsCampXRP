// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.xrp.XRPRangefinder;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This autonomous routine drives out a specified distance, then turns around and drives back.
 */
public class AutonomousDistance extends SequentialCommandGroup {

  private static final double SPEED = 0.75;

  public AutonomousDistance(Drivetrain drivetrain, XRPRangefinder distanceSensor) {
    addCommands(
      Commands.repeatingSequence(
          new DriveSpeed(drivetrain, 0.95).repeatedly().onlyWhile(() -> distanceSensor.getDistanceInches() > 10),
          new TurnDegrees(drivetrain, 0.75, 90),
          new DriveDistance(drivetrain, -0.95, 3).onlyIf(() -> distanceSensor.getDistanceInches() < 10),

          new DriveSpeed(drivetrain, 0.95).repeatedly().onlyWhile(() -> distanceSensor.getDistanceInches() > 10),
          new TurnDegrees(drivetrain, -0.75, 95),
          new DriveDistance(drivetrain, -0.95, 3).onlyIf(() -> distanceSensor.getDistanceInches() < 10)
          ));
    ;
  }

}
