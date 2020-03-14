import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class MovingRotors {
	
	
	public static void displayA(int speed) {
		LCD.clear(0,0,4);
		LCD.drawInt(speed, 0, 5);
	}
	public static void displayB(int speed) {
		LCD.clear(0,0,4);
		LCD.drawInt(speed, 10, 5);
	}
	public static void main(String[] args) {
		
		LCD.setAutoRefresh(false);
		int speedA = 0;
		int speedB = 0;
		int button;
		
		LCD.drawString("Moving Rotors", 0, 0);
		LCD.drawString("A", 0, 4);
		LCD.drawString("B", 10, 4);
		
		displayA(speedA);
		displayB(speedB);
		
		Motor.A.setSpeed(speedA);
		Motor.B.setSpeed(speedB);
		Motor.A.forward();
		Motor.B.forward();
		
		while ((button = Button.waitForAnyPress()) != Button.ID_ESCAPE) {
			switch(button) {
			
			case Button.ID_LEFT:
				speedA += 100; 
				displayA(speedA);
				Motor.A.setSpeed(speedA);
				Motor.A.forward();
				break;
				
			case Button.ID_RIGHT:
				speedB += 100;
				displayB(speedB);
				Motor.B.setSpeed(speedB);
				Motor.B.forward();
				break;
			
			case Button.ID_ENTER:
				speedA = speedB = 0;
				Motor.A.setSpeed(speedA);
				Motor.B.setSpeed(speedB);
				Motor.A.stop();
				Motor.B.stop();
				displayA(speedA);
				displayB(speedB);
				break;
			
			}
		}
		Motor.A.stop();
		Motor.B.stop();
	}
}