import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class Line2Follow {

	
	public static void main (String[] args) throws IOException {
		int[][] colors1 = Stocker.getColors("colors1.txt");
		int[][] colors2 = Stocker.getColors("colors2.txt");
		
		NXTRegulatedMotor left = Motor.D;
		NXTRegulatedMotor right = Motor.A;
		
		EV3ColorSensor sleft = new EV3ColorSensor(SensorPort.S4);
		EV3ColorSensor sright = new EV3ColorSensor(SensorPort.S1);
		
		SensorMode smLeft = sleft.getRGBMode();
		
		SensorMode smRight = sright.getRGBMode();
		
	}
}
