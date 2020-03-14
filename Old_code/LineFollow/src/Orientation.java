public class Orientation {

	/* orientation du robot
	 * 			  0
	 * 
	 *  pi/2	ROBOT	 3pi/2
	 * 	
	 * 			 pi 
	 */
	
	//TODO mesurer la vraie valeur
	private static float chassis = 12.8f;
	
	/*
	 * return l'orientation du robot selon les distance parcourus par les deux roux
	 */
	public static double getOrientation(double roueDroite, double roueGauche) {
		double distance = roueDroite - roueGauche;
		
		double tmp = distance / chassis;
		
		double result = tmp % 2*Math.PI;
		
		return result;
	}
	
	/*
	 * return la distance a parcourir lorsque l'orientaion actuelle est teta, pour avoir une orientation aim
	 * si la distance est positive, c'est la roue droite qui doit tourner
	 * sinon c'est la roue gauche
	 */
	public static double aimOrientation(double teta, double aim) {
		double diff = aim-teta;
		
		double result = diff*chassis;
		
		return result;
	}
}