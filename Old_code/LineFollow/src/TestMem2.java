import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;

public class TestMem2 {
	private static float WHEEL_RADIUS = 2.15f;
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
		Motor.A.resetTachoCount();
		Motor.D.resetTachoCount();
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
		
		LinkedList<Move2> Move2s = new LinkedList<Move2>();
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
						Move2s.add(new Move2(aBigger,System.currentTimeMillis()-previousTime));
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
					Move2s.add(new Move2(aBigger,System.currentTimeMillis()-previousTime));
					previousTime = System.currentTimeMillis();
					conditionArret=true;
					Motor.A.stop();
					Motor.D.stop();
					break;
					
				case UTURN:
					if(!isInUTURN){
						Move2s.add(new Move2(aBigger,System.currentTimeMillis()-previousTime));
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
		float roueDroite = Move.convertAngleToDistance(Motor.A.getTachoCount(), WHEEL_RADIUS);
		float roueGauche = Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS);
		double a  = Orientation.aimOrientation(Orientation.getOrientation(roueDroite, roueGauche),0);
		if (a>0)
			Motor.A.rotate((int) Move.convertDistanceToAngle((float) a, WHEEL_RADIUS));
		else
			Motor.D.rotate((int) Move.convertDistanceToAngle((float) -a, WHEEL_RADIUS));
		Delay.msDelay(1000);
		for (Move2 Move2 : Move2s) {
			Motor.A.setSpeed(Move2.aBigger() ? speedHigh : speedLow);
			Motor.D.setSpeed(Move2.aBigger()? speedLow : speedHigh);
			Motor.A.forward();
			Motor.D.forward();
			Delay.msDelay(Move2.duration());
		}
		Motor.A.stop();
		Motor.D.stop();
		
		th.interrupt();
		System.exit(0);
	}
}