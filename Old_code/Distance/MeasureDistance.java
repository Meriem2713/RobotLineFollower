import lejos.nxt.Motor;
import lejos.robotics.navigation.Move;

/*
 * MeasureDistance.java
 * Exemple: mesure de la distance parcourue par le robot
 * 
 */

public class MeasureDistance {
	// Rayon des roues (en cm)
	private static float WHEEL_RADIUS = 2.5f;
	
	public static void main(String[] args) {
		// Remettre le "tachoCount" des deux moteurs (branch�s sur les ports A et C) � 0
		// Le "tachoCount" mesure l'angle total parcouru par le moteur depuis le dernier reset.
		Motor.A.resetTachoCount();
		Motor.C.resetTachoCount();

		// ...
		// Faire avancer le robot 
		// ...

		// R�cup�rer l'angle parcouru par les deux moteurs et calculer la moyenne
		float totalAngle = (Motor.A.getTachoCount() + Motor.C.getTachoCount()) / 2;
		
		// Et transformer l'angle en distance en utilisant le rayon des roues
		float totalDistance = Move.convertAngleToDistance(totalAngle, WHEEL_RADIUS);
		
		// Afficher la distance � l'�cran
		System.out.println("  D = " + totalDistance);
	}

}