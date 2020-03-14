import lejos.hardware.Brick;
import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;
//import lejos.hardware.lcd.GraphicsLCD;
//import lejos.hardware.sensor.EV3ColorSensor;

/*
 * MeasureDistance.java
 * Exemple: mesure de la distance parcourue par le robot
 * 
 */

public class MesureDistance {
	// Rayon des roues (en cm)
	private static float WHEEL_RADIUS = 2.2f;

	public static void main(String[] args) {
		// Remettre le "tachoCount" des deux moteurs (branch�s sur les ports A et C) � 0
		// Le "tachoCount" mesure l'angle total parcouru par le moteur depuis le dernier reset.
		Motor.A.resetTachoCount();
		Motor.D.resetTachoCount();
		Motor.A.setSpeed(300);
		Motor.D.setSpeed(100);
		Motor.A.forward();
		Motor.D.forward();
		Delay.msDelay(2000);
		Motor.A.stop();
		Motor.D.stop();
		//float f = Brick.getPower().getBatteryCurrent();

		// R�cup�rer l'angle parcouru par les deux moteurs et calculer la moyenne
		float totalAngle = (Motor.A.getTachoCount() + Motor.D.getTachoCount()) / 2;
		
		// Et transformer l'angle en distance en utilisant le rayon des roues
		float totalDistance = Move.convertAngleToDistance(totalAngle, WHEEL_RADIUS);
		
		// Afficher la distance � l'�cran
		System.out.println("  D = " + totalDistance + " "+Motor.A.getTachoCount() + "  " + Motor.D.getTachoCount());
		Delay.msDelay(5000);
	}

}