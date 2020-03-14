import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;


public class Main {

	public static void main(String[] args) {
		LCD.drawString("Hello Giovanni", 0, 4);
		Motor.A.setSpeed(720);
		Motor.A.forward();
		Delay.msDelay(2000);
		Motor.A.backward();
		Delay.msDelay(2000);
		Motor.A.stop();
	}

}
