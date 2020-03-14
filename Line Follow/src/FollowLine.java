import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class FollowLine {
	
	public static final int LINE = 0;
	public static final int NOTLINE = 1;
	public static final int STOP = 2;
	public static final int UTURN = 3;
	private static  Log log = new Log("color.log");
	private static LinkedList<String> lines = new LinkedList<String>();
	private static int writing = 0;
	
	public static int colorFromSensorMode(SensorMode sm, int[][] colors){
		float[] rgb = new float[3];
		sm.fetchSample(rgb, 0);
		if (writing > 0){
			writing--;
			log.write(rgb[0]*255+":"+rgb[1]*255+":"+rgb[2]*255+"\n");
		}else{
			lines.add(rgb[0]*255+":"+rgb[1]*255+":"+rgb[2]*255+"\n");
			if (lines.size()>5){
				lines.removeFirst();
			}
		}
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
		int timePassedInUTURN = 0;
		boolean isInUTURN = false;
		boolean isInLINE = false ;
		
		while(!conditionArret){
			try{
				Thread.sleep(10);
			}catch(Exception e){
				System.out.println(e);
			}
			int color = colorFromSensorMode(sensorMode,colors);
			switch(color){
				case LINE:
					isInLINE = true;
					isInUTURN = false;
					timePassedInNOTLINE = 0;
					timePassedInUTURN= 0;
					break;
				case NOTLINE:
					if(isInLINE){
						String s;
						while (!lines.isEmpty()){
							s=lines.getFirst();
							lines.removeFirst();
							log.write(s);
						}
						log.write("LINETONOTLINE------\n");
						writing = 5;
						
						Motor.A.setSpeed(aBigger ? speedLow : speedHigh);
						Motor.D.setSpeed(aBigger ? speedHigh : speedLow);
						Motor.A.forward();
						Motor.D.forward();
						aBigger = !aBigger;
					}
					if(timePassedInNOTLINE > 300){
						Motor.A.setSpeed(aBigger ? speedHigh : 0);
						Motor.D.setSpeed(aBigger ? 0 : speedHigh);
						Motor.A.forward();
						Motor.D.forward();
					}
					isInLINE = false;
					isInUTURN = false;
					timePassedInNOTLINE++;
					timePassedInUTURN= 0;
					
					break;
				case STOP:
					conditionArret=true;
					Motor.A.stop();
					Motor.D.stop();
					break;
					
				case UTURN:
					if(!isInUTURN){
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
				default:
					break;
			}
		}
	}
}
			