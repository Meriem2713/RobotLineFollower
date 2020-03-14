import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.navigation.Move;


public class TestMove {
	private static float WHEEL_RADIUS = 2.15f;	
	private static float DIA = 12.6f;
	
	
	public static void tourne(float angleRotation){ //angle dans [-pi;pi]
		int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		float distance = (angleRotation)*DIA/2;
		int angle= (int) ((360*distance)/(2*Math.PI*WHEEL_RADIUS));
		if(angleRotation < 0){
			angle=-angle;
		}
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedHigh);
		Motor.A.rotate(angle,true);
		Motor.D.rotate(-angle,true);
		while(Motor.A.isMoving() || Motor.D.isMoving());
		System.out.println("ok");
	}
	
	public static void avance(float distance){
		
		int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		int angle= (int) ((360*distance)/(2*Math.PI*WHEEL_RADIUS));
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedHigh);
		Motor.A.rotate(angle,true);
		Motor.D.rotate(angle,true);
		while(Motor.A.isMoving() || Motor.D.isMoving());
		System.out.println("ok");
	}
	
	
	public static void main(String[] args) {
		
		
			tourne((float) (2*Math.PI));
	}

}
