// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.xrp.XRPReflectanceSensor;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This command drives the robot at a given speed.
 */
public class FollowLine extends Command {
  private final Drivetrain m_drivetrain;
  private final XRPReflectanceSensor reflectanceSensor;

  public FollowLine(Drivetrain drivetrain, XRPReflectanceSensor m_reflectanceSensor) {
    m_drivetrain = drivetrain;
    reflectanceSensor = m_reflectanceSensor;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drivetrain.arcadeDrive(0, 0);
    m_drivetrain.resetEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (reflectanceSensor.getRightReflectanceValue()>0.8) {
          m_drivetrain.arcadeDrive(0.75,-0.5);
        }
    
        
        else if (reflectanceSensor.getLeftReflectanceValue()>0.8) {
          m_drivetrain.arcadeDrive(0.75,0.5);
        }
    
        else {
          m_drivetrain.arcadeDrive(0.75,0);
        }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivetrain.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
