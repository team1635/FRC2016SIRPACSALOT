package org.usfirst.frc.team1635.robot.subsystems;

import org.usfirst.frc.team1635.robot.RobotMap;
import org.usfirst.frc.team1635.robot.commands.IntakeWithJoystick;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author : Jing Wei Li (SKRUB_HUNTER) , Miguel Cruz (@Acelogic_ )
 */
public class Intaker extends Subsystem {
	TalonSRX talon;
	AnalogInput pressuresensor;
	Solenoid intakerLifter;
	boolean onTarget, isPressureChecked;
	double currentpl, pl1, pl2, pl3, pl4, pl5;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new IntakeWithJoystick());
	}

	public Intaker() {

		talon = new TalonSRX(RobotMap.kIntakerTalonPort);
		pressuresensor = new AnalogInput(RobotMap.kPressureAnalogPort);
		intakerLifter = new Solenoid(RobotMap.kIntakerLifterPort);

		onTarget = false;

	}

	public double ObtainTalonLastSetValue() { // Made by miguel
		double getTalonValue = talon.get();
		return getTalonValue;

	}

	public void log() {
		//SmartDashboard.putNumber("Pressurelevel", obtainPressureLevel());
		//SmartDashboard.putNumber("TalonSRXlastSetValue", ObtainTalonLastSetValue());

	}

	public void operate(Joystick joy1) {
		boolean leftInput = joy1.getRawButton(5);
		boolean rightInput = joy1.getRawButton(6);
		double output = 0.0;

		if (leftInput && rightInput) {
			// both triggers are pressed so we shouldn't move the roller
			output = 0.0;
		} else {
			if (leftInput) {
				// the left button LB should roll the ball in
				output = -1;
				// rollIn();

			} else if (rightInput) {
				// the right button RB should roll the ball out
				output = 1;
				// rollOutShooting();
			}
		}
		PressureCheck(output, joy1);
		// checkPressureHistory(joy1);
	}

	public boolean checkPressureHistory() {
		isPressureChecked = false;
		if ((currentpl < RobotMap.kPressureLimit) && (pl1 < RobotMap.kPressureLimit)
				&& (pl2 < RobotMap.kPressureLimit)) {
			isPressureChecked = true;
		}
		return isPressureChecked;
	}

	public void PressureCheck(double input, Joystick joy_x) {

		double outputspd = 0.0;
		// the following method sets the current pressure level currentpl
		double lastPressure = obtainPressureLevel();
		System.out.println(" pressure" + lastPressure);

		if (checkPressureHistory() && joy_x.getRawButton(5)) {
			// Timer.delay(0.05);// delay the locking mechanism to allow more
			// grip
			// on the ball
			// for debugging System.out.println("Pressure Detected");
			outputspd = 0.0;
			stopIntaker();
			raiseIntaker();
		} else {
			outputspd = input;
			// for debugging System.out.println("Pressure null");
		}

		operateIntaker(outputspd);
	}

	public void operateIntaker(double finalInput) {
		talon.set(finalInput);
	}

	/**
	 * obtain the pressure from the pressure sensor
	 */
	public double obtainPressureLevel() {
		pl5 = pl4;
		pl4 = pl3;
		pl3 = pl2;
		pl2 = pl1;
		pl1 = currentpl;
		currentpl = pressuresensor.getValue();
		return currentpl;

		// double pressureLevel = pressuresensor.getValue();
		// return pressureLevel;
	}

//	public void raise_LowerIntaker(Joystick joy2) {
//		if (joy2.getRawButton(3)) {
//			intakerLifter.set(true);
//		} else if (joy2.getRawButton(4)) {
//			intakerLifter.set(false);
//		}
//	}
	public void raise_LowerIntaker(Joystick joy2) {
		if (joy2.getRawAxis(2)>0) {
			intakerLifter.set(true);
		} else if (joy2.getRawAxis(3)>0) {
			intakerLifter.set(false);
		}
	}

	public void intakerLiftOneButton(Joystick joyy) {
		if (joyy.getRawButton(4)) {
			if (intakerLifter.get()) {
				intakerLifter.set(false);
			} else if (!intakerLifter.get()) {
				intakerLifter.set(true);
			}
		}
	}

	public void rollIn() {
		if (obtainPressureLevel() < RobotMap.kPressureLimit) {
			// Timer.delay(0.05);
			talon.set(0);
			onTarget = true;

		} else {
			talon.set(-1);
			// Timer.delay(1);
			// talon.set(0);
		}
	}

	/**
	 * shoot the ball to target
	 */
	public void rollOutShooting() {
		talon.set(1);
		Timer.delay(1);
		talon.set(0);
		onTarget = true;
	}

	/**
	 * pass the ball to alliance robot
	 */
	public void rollOutPassing() {
		talon.set(1);
		Timer.delay(0.5);// less force when passing
		talon.set(0);
		onTarget = true;
	}
	
	public void resetOnTarget() {
		onTarget = false;
	}

	public void raiseIntaker() {
		intakerLifter.set(false);
		if (!intakerLifter.get()) {
			onTarget = true;
		}
	}

	public void lowerIntaker() {
		intakerLifter.set(true);
		if (intakerLifter.get()) {
			onTarget = true;
		}
	}

	public boolean isOnTarget() {
		return onTarget;
	}

	public void stopIntaker() {
		talon.set(0);
	}
}
