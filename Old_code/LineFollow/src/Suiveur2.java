import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;


public class Suiveur2 {
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
		int lent=150;
		int rapide=300;
		int speedA = rapide;
		int speedD = rapide;
		Motor.A.setSpeed(speedA);
		Motor.D.setSpeed(speedD);
					
		Thread th = new Thread() {
            public synchronized void run() {
            	while(true){
            		int button = Button.waitForAnyPress();
            		if (button == Button.ID_ESCAPE){
            			System.exit(0);
            		}
            	}
            }
		};
		th.start();
		
		EV3ColorSensor sensorD = null;
		EV3ColorSensor sensorG = null;
		try {
			sensorD = new EV3ColorSensor(SensorPort.S1);
			sensorG = new EV3ColorSensor(SensorPort.S2);
			
		}catch (Exception e) {
			LCD.drawString("No sensor detected", 0, 0);
			Delay.msDelay(3000);
			System.exit(-1);
		}
		SensorMode sensorModeD = sensorD.getRGBMode();
		SensorMode sensorModeG= sensorG.getRGBMode();
		int [][] colors1=null;
		try{
			colors1 = Stock.getColors("colors1.txt");
		}catch(Exception e){

			System.out.println("Pb colors1.txt");
			Delay.msDelay(3000);
			System.exit(-1);
		}
		int [][] colors2=null;
		try{
			colors2 = Stock.getColors("colors2.txt");
		}catch(Exception e){

			System.out.println("Pb colors1.txt");
			Delay.msDelay(3000);
			System.exit(-1);
		}
		int colorD;
		int colorG;
		
		boolean tmp=true;
		
		//Le robot commence a tourner
		Motor.A.setSpeed(speedA);
		Motor.D.setSpeed(speedD);
		Motor.A.forward();
		Motor.D.forward();
		
		
		
		while(tmp){
			colorD = colorFromSensorMode(sensorModeD,colors1);
			colorG = colorFromSensorMode(sensorModeG,colors2);
			
			if(colorD==0){
				speedD=lent;
			}else{
				speedD=rapide;
			}
			if(colorG==0){
				speedA=lent;
			}else{
				speedA=rapide;
			}
	
			Motor.A.setSpeed(speedA);
			Motor.D.setSpeed(speedD);
			Motor.A.forward();
			Motor.D.forward();
			
			
		}
	}
}
