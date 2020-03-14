import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class TestMem {
	
	public static final int LINE = 0;
	public static final int NOTLINE = 1;
	public static final int STOP = 2;
	public static final int UTURN = 3;
	
	public static int colorFromSensorMode(SensorMode sm, int[][] colors){
		float[] rgb = new float[3];
		sm.fetchSample(rgb, 0);
		int color;
		try{
			color = Stock.getColor(colors,rgb);
		}catch(Exception e){
			color=-1;
		}
		return color;
	}
	
	public static void main(String[] args) {

		int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		int speedLow = speedHigh/2;
		
		boolean aBigger = true;
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedLow);
					
		Thread th = new Thread() {
            public void run() {
            	while(true){
            		int button = Button.waitForAnyPress();
            		if (button == Button.ID_ESCAPE){
            			Motor.A.stop();
            			Motor.D.stop();
            			Delay.msDelay(100);
            			System.exit(0);
            		}
            	}
            }
		};
		th.start();
		
		HiTechnicColorSensor sensor = null;
		try {
			sensor = new HiTechnicColorSensor(SensorPort.S1);
			
		}catch (Exception e) {
			LCD.drawString("No sensor detected", 0, 0);
			Delay.msDelay(3000);
			System.exit(-1);
		}
		
		SensorMode sensorMode = sensor.getRGBMode();
		int [][] colors=null;
		try{
			colors = Stock.getColors("colors.txt");
		}catch(Exception e){

			System.out.println("Pb colors.txt");
			Delay.msDelay(3000);
			System.exit(-1);
		}
		
		//Le robot commence a tourner
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedLow);
		
		Motor.A.forward();
		Motor.D.forward();
		
		boolean conditionArret=false;
		
		int timePassedInNOTLINE = 0;
		boolean isInUTURN = false;
		boolean isInLINE = false;
		
		LinkedList<Move> moves = new LinkedList<Move>();
		long previousTime = System.currentTimeMillis();
		
		while(!conditionArret){
			int color = colorFromSensorMode(sensorMode,colors);
			switch(color){
				case LINE:
					isInLINE = true;
					isInUTURN = false;
					timePassedInNOTLINE = 0;
					break;
				case NOTLINE:
					if(isInLINE){
						moves.add(new Move(aBigger,System.currentTimeMillis()-previousTime));
						previousTime = System.currentTimeMillis();
						Motor.A.setSpeed(aBigger ? speedLow : speedHigh);
						Motor.D.setSpeed(aBigger ? speedHigh : speedLow);
						Motor.A.forward();
						Motor.D.forward();
						aBigger = !aBigger;
					}
					/*if(timePassedInNOTLINE > 500){
						Motor.A.setSpeed(aBigger ? speedHigh : 0);
						Motor.D.setSpeed(aBigger ? 0 : speedHigh);
						Motor.A.forward();
						Motor.D.forward();
					}*/
					isInLINE = false;
					isInUTURN = false;
					timePassedInNOTLINE++;
					
					break;
				case STOP:
					moves.add(new Move(aBigger,System.currentTimeMillis()-previousTime));
					previousTime = System.currentTimeMillis();
					conditionArret=true;
					Motor.A.stop();
					Motor.D.stop();
					break;
					
				case UTURN:
					if(!isInUTURN){
						moves.add(new Move(aBigger,System.currentTimeMillis()-previousTime));
						previousTime = System.currentTimeMillis();
						Motor.A.setSpeed(aBigger ? 0 : speedHigh);
						Motor.D.setSpeed(aBigger ? speedHigh : 0);
						Motor.A.forward();
						Motor.D.forward();
						aBigger = !aBigger;
					}
					isInUTURN = true;
					isInLINE = false;
					timePassedInNOTLINE = 0;
					break;
			}
		}
		Delay.msDelay(1000);
		for (Move move : moves) {
			Motor.A.setSpeed(move.aBigger() ? speedHigh : speedLow);
			Motor.D.setSpeed(move.aBigger()? speedLow : speedHigh);
			Motor.A.forward();
			Motor.D.forward();
			Delay.msDelay(move.duration());
		}
		Motor.A.stop();
		Motor.D.stop();
		
		th.interrupt();
	}
}