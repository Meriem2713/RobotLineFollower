import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;

 public class OrientationTest {
		volatile static float orientation = 0;
		volatile static int angleA,angleD;
		private static float WHEEL_RADIUS = 2.2f;
		private static float DIA = 12.7f;
		private static float PI = (float) Math.PI;
		public static void main(String[] args) {

			float maxSpeed;
			float distanceA,distanceB;
			
			NXTRegulatedMotor motorA = Motor.A;
			NXTRegulatedMotor motorD = Motor.D;
			motorA.resetTachoCount();
			motorD.resetTachoCount();
			
			maxSpeed = motorA.getMaxSpeed();
			angleA = motorA.getTachoCount();
			angleD = motorD.getTachoCount();
			motorA.setSpeed(maxSpeed);
			motorA.forward();
			
			Delay.msDelay(2000);
			motorA.stop();
			angleA = motorA.getTachoCount();
			angleD = motorD.getTachoCount();
			distanceA = Move.convertAngleToDistance(angleA, WHEEL_RADIUS);
			distanceB = Move.convertAngleToDistance(angleD, WHEEL_RADIUS);
			orientation = (distanceA - distanceB) % (DIA * PI);
			System.out.println(orientation);
			Delay.msDelay(3000);
			motorD.setSpeed(maxSpeed/2);
			motorA.forward();
			motorD.forward();
			Delay.msDelay(5000);
			motorA.stop();
			motorD.stop();
			angleA = motorA.getTachoCount();
			angleD = motorD.getTachoCount();
			distanceA = Move.convertAngleToDistance(angleA, WHEEL_RADIUS);
			distanceB = Move.convertAngleToDistance(angleD, WHEEL_RADIUS);
			orientation = (distanceA - distanceB) % (DIA * PI);
			System.out.println(orientation);
			Delay.msDelay(2000);
			motorD.setSpeed(maxSpeed*3f/4f);
			motorA.setSpeed(maxSpeed/3f);
			motorA.forward();
			motorD.forward();
			Delay.msDelay(7000);
			motorA.stop();
			motorD.stop();
			angleA = motorA.getTachoCount();
			angleD = motorD.getTachoCount();
			distanceA = Move.convertAngleToDistance(angleA, WHEEL_RADIUS);
			distanceB = Move.convertAngleToDistance(angleD, WHEEL_RADIUS);
			orientation = (distanceA - distanceB) % (DIA * PI);
			System.out.println(orientation);
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
