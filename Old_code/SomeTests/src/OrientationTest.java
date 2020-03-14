import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;

public class OrientationTest {
	volatile static double orientation = 0;
	volatile static double angleA,angleD;
	
	private static float WHEEL_RADIUS  = 2.15f;
	public static void main(String[] args) {

		float maxSpeed;
		
		NXTRegulatedMotor motorA = Motor.A;
		NXTRegulatedMotor motorD = Motor.D;
		motorA.resetTachoCount();
		motorD.resetTachoCount();
		
		maxSpeed = motorA.getMaxSpeed();
		
		angleA = motorA.getTachoCount();
		angleD = motorD.getTachoCount();
		
		Thread th =new Thread() {
			public void run() {
				angleA = motorA.getTachoCount();
				angleD = motorD.getTachoCount();
				double distanceA = Move.convertAngleToDistance((float)angleA, WHEEL_RADIUS);
				double distanceD = Move.convertAngleToDistance((float)angleD, WHEEL_RADIUS);
				orientation = Orientation.getOrientation(distanceA, distanceD);
				System.out.println(orientation);
				Delay.msDelay(12);
			}
		};
		th.start();
		motorA.setSpeed(maxSpeed);
		motorA.forward();
		Delay.msDelay(2000);
		motorA.stop();
		Delay.msDelay(3000);
		motorD.setSpeed(maxSpeed/2);
		motorA.forward();
		motorD.forward();
		Delay.msDelay(5000);
		motorA.stop();
		motorD.stop();
		motorD.setSpeed(maxSpeed*3f/4f);
		motorA.setSpeed(maxSpeed/3f);
		motorA.forward();
		motorD.forward();
		Delay.msDelay(7000);
		motorA.stop();
		motorD.stop();
		Delay.msDelay(5000);
		System.exit(0);
	}
}
