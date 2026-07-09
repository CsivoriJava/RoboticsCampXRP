// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This command reads the position of the left joystick to drive the robot.
 */
public class ArcadeDrive extends Command {
  private final Drivetrain m_drivetrain;
  private final CommandXboxController m_controller;

  public ArcadeDrive(Drivetrain drivetrain, CommandXboxController controller) {
    m_drivetrain = drivetrain;
    m_controller = controller;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drivetrain.arcadeDrive(-m_controller.getLeftY() + m_controller.getRawAxis(3), -m_controller.getLeftX() + m_controller.getRawAxis(2));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
