package org.usfirst.frc.team1635.robot.commands;

import org.usfirst.frc.team1635.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SpinToAngle extends Command {
    private double angle;
    
    public SpinToAngle(double angle) {
    	this.angle = angle;
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Robot.drivetrain.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.spin();
    }

    // Make this return true when the robot starts climbing the defense
    protected boolean isFinished() {
        return (Robot.drivetrain.obtainYaw() >= angle);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
