import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.Move;

/*
 * MoveForward.java
 * Exemple:  Faire avancer le robot de 30 cm
 * 
 */

public class MoveForward {
	// Rayon des roues (en cm)
	private static float WHEEL_RADIUS = 2.5f;
	
	public static void main(String[] args) {
		// Distance à parcourir: 30 cm
		float desiredDistance = 30f;
		
		// Calculer l'angle de rotation des moteur à l'aide de la distance
		// et du rayon des roues
		int desiredAngle = (int) Move.convertDistanceToAngle(desiredDistance, WHEEL_RADIUS);
		
		// Demander au 1er moteur de tourner
		// Le 2nd argument est "true": de cette façon, le programme continue
		// à s'exécuter et n'attend pas que le moteur ait fini sa rotation
		Motor.A.rotate(desiredAngle, true);
		// Demander au 2nd moteur de tourner
		// Cette fois, le 2nd argument est "false": on attend que le moteur
		// ait fini de tourner avant de passer à la suite
		Motor.C.rotate(desiredAngle, false);
		
		System.out.println("OK!");
		Button.waitForAnyPress();
	}

}