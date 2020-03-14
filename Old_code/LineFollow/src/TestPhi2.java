import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class TestPhi2 {
	
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
		Motor.A.setSpeed(speedLow);
		Motor.D.setSpeed(speedHigh);
		
		Motor.A.forward();
		Motor.D.forward();
		
		boolean conditionArret=false;
		
		//long timePassedInNOTLINE = 0;
		long timeLeaveLine = 0;
		boolean isInUTURN = false;
		boolean isInLINE = true;
		boolean isInNOTLINE = false;
		boolean estADroite = true;
		
		while(!conditionArret){
			try{
				Thread.sleep(10);
			}catch(Exception e){
				System.out.println(e);
			}
			int color = colorFromSensorMode(sensorMode,colors);
			switch(color){
				case LINE:
					if(isInNOTLINE){
						if(System.currentTimeMillis() - timeLeaveLine > 2000){
							estADroite = !estADroite;
						}
						Motor.A.setSpeed(estADroite ? speedLow : speedHigh);
						Motor.D.setSpeed(estADroite ? speedHigh : speedLow);
						Motor.A.forward();
						Motor.D.forward();
					}
					isInLINE = true;
					isInUTURN = false;
					isInNOTLINE = false;
					break;
				case NOTLINE:
					if(isInLINE){
						timeLeaveLine = System.currentTimeMillis();
						Motor.A.setSpeed(estADroite ? speedHigh	: speedLow);
						Motor.D.setSpeed(estADroite ? speedLow : speedHigh);
						Motor.A.forward();
						Motor.D.forward();				
					}
					isInLINE = false;
					isInUTURN = false;
					isInNOTLINE = true;
					
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
					isInNOTLINE = false;
					break;
			}
		}
	}
}