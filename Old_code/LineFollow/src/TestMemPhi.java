import java.awt.geom.Point2D;
import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;

public class TestMemPhi {
	private static float WHEEL_RADIUS = 2.15f;	
	private static float DIA = 12.6f;
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
	}
	
	public static void avance(float distance){
		
		int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		int angle= (int) ((360*distance)/(2*Math.PI*WHEEL_RADIUS));
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedHigh);
		Motor.A.rotate(angle,true);
		Motor.D.rotate(angle,true);
		while(Motor.A.isMoving() || Motor.D.isMoving());
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
		
		//int timePassedInNOTLINE = 0;
		boolean isInUTURN = false;
		boolean isInLINE = false;
		
		LinkedList<MovePhi> Move2s = new LinkedList<MovePhi>();
		
		while(!conditionArret){
			int color = colorFromSensorMode(sensorMode,colors);
			switch(color){
				case LINE:
					isInLINE = true;
					isInUTURN = false;
					//timePassedInNOTLINE = 0;
					break;
				case NOTLINE:
					if(isInLINE){
						Move2s.add(new MovePhi(Move.convertAngleToDistance( Motor.A.getTachoCount(), WHEEL_RADIUS),Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS)));
						Motor.A.resetTachoCount();
						Motor.D.resetTachoCount();
						Motor.A.setSpeed(aBigger ? speedLow : speedHigh);
						Motor.D.setSpeed(aBigger ? speedHigh : speedLow);
						Motor.A.forward();
						Motor.D.forward();
						aBigger = !aBigger;
					}
					/*if(timePassedInNOTLINE > 500){
						Move2s.add(new MovePhi(Move.convertAngleToDistance( Motor.A.getTachoCount(), WHEEL_RADIUS),Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS)));
						Motor.A.setSpeed(aBigger ? speedHigh : 0);
						Motor.D.setSpeed(aBigger ? 0 : speedHigh);
						Motor.A.forward();
						Motor.D.forward();
					}*/
					isInLINE = false;
					isInUTURN = false;
					//timePassedInNOTLINE++;
					
					break;
				case STOP:
					Move2s.add(new MovePhi(Move.convertAngleToDistance( Motor.A.getTachoCount(), WHEEL_RADIUS),Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS)));
					Motor.A.resetTachoCount();
					Motor.D.resetTachoCount();
					conditionArret=true;
					Motor.A.stop();
					Motor.D.stop();
					break;
					
				case UTURN:
					if(!isInUTURN){
						Move2s.add(new MovePhi(Move.convertAngleToDistance( Motor.A.getTachoCount(), WHEEL_RADIUS),Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS)));
						Motor.A.resetTachoCount();
						Motor.D.resetTachoCount();
						Motor.A.setSpeed(aBigger ? 0 : speedHigh);
						Motor.D.setSpeed(aBigger ? speedHigh : 0);
						Motor.A.forward();
						Motor.D.forward();
						aBigger = !aBigger;
					}
					isInUTURN = true;
					isInLINE = false;
					//timePassedInNOTLINE = 0;
					break;
			}
		}
		try{
			System.out.println("calcul point");
			Thread.sleep(3000);
		}catch(Exception e){
			
		}
		LinkedList<Point2D.Float> listPoint = new LinkedList<Point2D.Float>();
		listPoint.add(new Point2D.Float(0,0));
		float angle=0;
		Log log=new Log("point.txt");
		Log log2=new Log("infoMove.txt");
		log.initialize();
		log2.initialize();
		for(int i=0;i<Move2s.size();i++){
			MovePhi move = Move2s.get(i);
			float angle0=move.getAngle();
			log2.write("angle"+angle0+"\n");
			log2.write("rayon"+move.getCorvatureRadius()+"\n");
			log2.write("dx"+move.getDX()+"\n");
			log2.write("dy"+move.getDY()+"\n");
			log2.write("dy"+move.getRotatedDX(angle0)+"\n");
			log2.write("dy"+move.getRotatedDY(angle0)+"\n");
			log2.write("\n");
			Point2D.Float point0 = listPoint.get(i);
			float x=(float) (move.getRotatedDX(-angle)+point0.getX());
			float y =(float) (move.getRotatedDY(-angle)+point0.getY());
			Point2D.Float point = new Point2D.Float(x,y);
			listPoint.add(point);
			angle+=move.getAngle();
			angle%=2*Math.PI;
			log.write(point.getX()+"\n"+point.getY()+"\n");
		}

		for(int i=0;i<listPoint.size()-2;i++){
			Point2D.Float p1=listPoint.get(i);
			Point2D.Float p2=listPoint.get(i+1);
			Point2D.Float p3=listPoint.get(i+2);
			float v1x = p2.x-p1.x;
			float v1y = p2.y-p1.y;
			float v2x = p3.x-p2.x;
			float v2y = p3.y-p2.y;
			float angleBis= (float) (Math.atan2(v2x,v2y) - Math.atan2(v1x,v1y));
			float distance=(float) Math.sqrt( Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
			avance(distance); 
			tourne(angleBis);
		}
			
			
			
			
		th.interrupt();
		System.exit(0);
	}
}