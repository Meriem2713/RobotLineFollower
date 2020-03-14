//import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
//import lejos.hardware.lcd.GraphicsLCD;
//import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class TestFlo {
	
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
	
	private static int highSpeedModification(int i) {
		return 0;
	}
	private static int lowSpeedModification(int i) {
		return 0;
	}
	public static void main(String[] args) {
		
		//int countColor=0;
		//int COLORMARGIN=1;
		
		int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		int speedLow = speedHigh/2;
		
		//GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		boolean aBigger = true;
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedLow);
		boolean sync;
					
		Thread th = new Thread() {
            public void run() {
            	while(true){
            		int button = Button.waitForAnyPress();
            		if (button == Button.ID_ESCAPE){
            			Motor.A.stop();
            			Motor.D.stop();
            			Delay.msDelay(1000);
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
		int color;
		
		//Le robot commence a tourner
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedLow);
		
		Motor.A.forward();
		Motor.D.forward();
		
		color = colorFromSensorMode(sensorMode,colors);
		boolean conditionArret=false;
		while(!conditionArret){
			color = colorFromSensorMode(sensorMode,colors);
			switch(color){
				case LINE:
					break;
				case NOTLINE:
					if (aBigger) {
						Motor.A.setSpeed(speedLow);
						Motor.D.setSpeed(speedHigh);
					}else {
						Motor.A.setSpeed(speedHigh);
						Motor.D.setSpeed(speedLow);
					}
					aBigger = !aBigger;
					Motor.A.forward();
					Motor.D.forward();
					int timePassedOutsideOfLine=0;
					
					while(color==NOTLINE){
						color = colorFromSensorMode(sensorMode,colors);
						if(aBigger){
							Motor.A.setSpeed(speedHigh+highSpeedModification(timePassedOutsideOfLine) < 1000 ? speedHigh+highSpeedModification(timePassedOutsideOfLine) : 1000);
							Motor.D.setSpeed((speedLow-lowSpeedModification(timePassedOutsideOfLine)) > 0 ? (speedLow-lowSpeedModification(timePassedOutsideOfLine)) : 0);
							Motor.A.forward();
							Motor.D.forward();
						}else{
							Motor.A.setSpeed((speedLow-lowSpeedModification(timePassedOutsideOfLine)) > 0 ? (speedLow-lowSpeedModification(timePassedOutsideOfLine)) : 0);
							Motor.D.setSpeed(speedHigh+highSpeedModification(timePassedOutsideOfLine) < 1000 ? speedHigh+highSpeedModification(timePassedOutsideOfLine) : 1000);
							Motor.A.forward();
							Motor.D.forward();
						}
						timePassedOutsideOfLine++;
					}
					break;
				case STOP:
					conditionArret=true;
					Motor.A.setSpeed(0);
					Motor.D.setSpeed(0);
					Motor.A.forward();
					Motor.D.forward();
					Motor.A.stop();
					Motor.D.stop();
					System.exit(0);
					break;
				case UTURN:
					Motor.A.setSpeed(aBigger ? 0 : 600);
					Motor.D.setSpeed(aBigger ? 600 : 0);
					Motor.A.forward();
					Motor.D.forward();
					while(colorFromSensorMode(sensorMode,colors) != LINE);
					Motor.A.setSpeed(aBigger ? speedLow : speedHigh);
					Motor.D.setSpeed(aBigger ? speedHigh : speedLow);
					Motor.A.forward();
					Motor.D.forward();
					aBigger = !aBigger;
					break;
			}
				
		}
		/*while(color!=0){
			color = colorFromSensorMode(sensorMode,colors);
		}
		countColor=0;
		long moyenne = 0;
		while(tmp){

			while(color==0){
				color = colorFromSensorMode(sensorMode,colors);
			}
			countColor=0;
			if (aBigger) {
				Motor.A.setSpeed(speedLow);
				Motor.D.setSpeed(speedHigh);
			}else {
				Motor.A.setSpeed(speedHigh);
				Motor.D.setSpeed(speedLow);
			}
			aBigger = !aBigger;
			Motor.A.forward();
			Motor.D.forward();
			int timePassedOutsideOfLine=0;
			long startTime = System.currentTimeMillis();
			long endTime = 0;
			long differenceTime = 0;
			
			while(color!=0){
				startTime = System.currentTimeMillis();
				color = colorFromSensorMode(sensorMode,colors);
				if(aBigger){
					Motor.A.setSpeed(speedHigh+highSpeedModification(timePassedOutsideOfLine) < 1000 ? speedHigh+highSpeedModification(timePassedOutsideOfLine) : 1000);
					Motor.D.setSpeed((speedLow-lowSpeedModification(timePassedOutsideOfLine)) > 0 ? (speedLow-lowSpeedModification(timePassedOutsideOfLine)) : 0);
					Motor.A.forward();
					Motor.D.forward();
				}else{
					Motor.A.setSpeed((speedLow-lowSpeedModification(timePassedOutsideOfLine)) > 0 ? (speedLow-lowSpeedModification(timePassedOutsideOfLine)) : 0);
					Motor.D.setSpeed(speedHigh+highSpeedModification(timePassedOutsideOfLine) < 1000 ? speedHigh+highSpeedModification(timePassedOutsideOfLine) : 1000);
					Motor.A.forward();
					Motor.D.forward();
				}
				timePassedOutsideOfLine++;
				endTime = System.currentTimeMillis();
				differenceTime = endTime - startTime;
				moyenne = (moyenne * (timePassedOutsideOfLine - 1) + differenceTime) / timePassedOutsideOfLine;
				g.clear();
				g.drawString(moyenne+"", 10, 10, 0);
				g.refresh();
				
			}
			countColor=0;
		}*/
	}

}
