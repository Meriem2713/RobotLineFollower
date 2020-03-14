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

public class FollowMem {
	static float WHEEL_RADIUS = 2.15f;	
	static float DIA = 12.6f;
	public static final int LINE = 0;
	public static final int NOTLINE = 1;
	public static final int STOP = 2;
	public static final int UTURN = 3;
	static Log log2=new Log("infoMove.txt");
	
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
		float distance = (angleRotation)*DIA/2;
		int angle= (int) ((360*distance)/(2*Math.PI*WHEEL_RADIUS));
		//int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		int speedHigh=300;
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedHigh);
		Motor.A.rotate(angle,true);
		Motor.D.rotate(-angle,true);
		while(Motor.A.isMoving() || Motor.D.isMoving());
	}
	
	public static void avance(float distance){
		
		int angle= (int) ((360*distance)/(2*Math.PI*WHEEL_RADIUS));
		int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		//int speedHigh=300;
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedHigh);
		Motor.A.rotate(angle,true);
		Motor.D.rotate(angle,true);
		while(Motor.A.isMoving() || Motor.D.isMoving());
	}
	
	public static void main(String[] args) {
		Motor.A.resetTachoCount();
		Motor.D.resetTachoCount();
		
		boolean aBigger = true;

		//int speedHigh = (int) Motor.A.getMaxSpeed() - 10;
		int speedHigh=400;
		int speedLow = speedHigh/2;
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedLow);
					
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
		
		//Le robot commence a tourner
		
		Motor.A.setSpeed(speedHigh);
		Motor.D.setSpeed(speedLow);
		
		Motor.A.forward();
		Motor.D.forward();
		
		boolean conditionArret=false;
		
		//int timePassedInNOTLINE = 0;
		int timePassedInStop =0;
		boolean isInUTURN = false;
		boolean isInLINE = false;
		
		LinkedList<MoveMem> Move2s = new LinkedList<MoveMem>();
		float orientation=0;
		int inc=0;
		while(!conditionArret){
			int color = colorFromSensorMode(sensorMode,colors);
			switch(color){
				case LINE:
					isInLINE = true;
					isInUTURN = false;
					//timePassedInNOTLINE = 0;
					timePassedInStop=0;
					break;
				case NOTLINE:
					if(isInLINE){
						//Move2s.add(new MovePhi(Move.convertAngleToDistance( Motor.A.getTachoCount(), WHEEL_RADIUS),Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS)));
						//MovePhi move=new MovePhi(Move.convertAngleToDistance( Motor.A.getTachoCount(), WHEEL_RADIUS),Move.convertAngleToDistance(Motor.D.getTachoCount(), WHEEL_RADIUS));
					
						float disAbis=Motor.A.getTachoCount();
						float disDbis=Motor.D.getTachoCount();
						Motor.A.resetTachoCount();
						Motor.D.resetTachoCount();
						Motor.A.setSpeed(aBigger ? speedLow : speedHigh);
						Motor.D.setSpeed(aBigger ? speedHigh : speedLow);
						Motor.A.forward();
						Motor.D.forward();
						aBigger = !aBigger;
						
						/////
						/////
						//Motor.A.stop();
						//Motor.D.stop();
						////
						float disA=Move.convertAngleToDistance( disAbis, WHEEL_RADIUS);
						float disD=Move.convertAngleToDistance( disDbis, WHEEL_RADIUS);
						/*MovePhi move= new MovePhi(disA,disD);
						move.createInfo();
						Move2s.add(move);*/
						MoveMem move2= new MoveMem(disA/2,disD/2);
						Move2s.add(move2);
						Move2s.add(move2);
						//orientation+=move.getAngle();
						System.out.println(inc);
						
						inc++;
						/*if(inc>=15){
							conditionArret=true;
						}*/
						//move.createInfo();
						//orientation=orientation+=move.getAngle();
						//System.out.println("a "+(int)(move.getAngle()*360/(2*Math.PI)));
						//System.out.println("o "+(int)(orientation*360/(2*Math.PI)));
						//Button.waitForAnyPress();
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
					timePassedInStop=0;
					//timePassedInNOTLINE++;
					
					break;
				case STOP:
					timePassedInStop++;
					if(timePassedInStop>20){
						float disAbis=Motor.A.getTachoCount();
						float disDbis=Motor.D.getTachoCount();
						float disA=Move.convertAngleToDistance( disAbis, WHEEL_RADIUS);
						float disD=Move.convertAngleToDistance( disDbis, WHEEL_RADIUS);
						/*MovePhi move= new MovePhi(disA,disD);
						move.createInfo();
						Move2s.add(move);*/
						MoveMem move2= new MoveMem(disA/2,disD/2);
						Move2s.add(move2);
						Move2s.add(move2);
						Motor.A.resetTachoCount();
						Motor.D.resetTachoCount();
						conditionArret=true;
						Motor.A.stop();
						Motor.D.stop();
					}
					break;
					
				case UTURN:
					if(!isInUTURN){
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
		Motor.A.stop();
		Motor.D.stop();
		/*for(int i=0;i<Move2s.size();i++){
			MovePhi move=Move2s.get(i);
			move.createInfo();
			orientation+=move.getAngle();
		}
		System.out.println((int)(orientation*360/(2*Math.PI)));*/
		
		try{
			System.out.println("calcul trajectoire");
			Thread.sleep(5000);
		}catch(Exception e){
		
		}
		LinkedList<Point2D.Float> listPoint = new LinkedList<Point2D.Float>();
		listPoint.add(new Point2D.Float(0,0));
		float angle=0;
		for(int i=0;i<Move2s.size();i++){
			MoveMem move = Move2s.get(i);
			move.createInfo();
			Point2D.Float point0 = listPoint.get(i);
			float x=(float) (move.getRotatedDX(-angle)+point0.getX());
			float y =(float) (move.getRotatedDY(-angle)+point0.getY());
			Point2D.Float point = new Point2D.Float(x,y);
			listPoint.add(point);
			angle+=move.getAngle();
			angle=(float) (angle%(2*Math.PI));
		}
		LinkedList<Point2D.Float> pointIntel = new LinkedList<Point2D.Float>();
		pointIntel.add(new Point2D.Float(0,0));
		for(int i=0;i<listPoint.size()-2;i+=2){
			Point2D.Float p1=listPoint.get(i);
			Point2D.Float p2=listPoint.get(i+1);
			Point2D.Float p3=listPoint.get(i+2);

			Point2D.Float pBis=new Point2D.Float((p1.x+p3.x)/2,(p1.y+p3.y)/2);
			Point2D.Float p=new Point2D.Float((pBis.x+p2.x)/2,(pBis.y+p2.y)/2);
			pointIntel.add(p); 
		}
		pointIntel.add(new Point2D.Float(0,0));
		for(int i=0;i<pointIntel.size()-2;i+=1){
			Point2D.Float p1=pointIntel.get(i);
			Point2D.Float p2=pointIntel.get(i+1);
			Point2D.Float p3=pointIntel.get(i+2);
			float v1x =  (float) (p2.getX()-p1.getX());
			float v1y = (float) (p2.getY()-p1.getY());
			float v2x = (float) (p3.getX()-p2.getX());
			float v2y = (float) (p3.getY()-p2.getY());
			float angleBis= (float) (Math.atan2(v1x,v1y) - Math.atan2(v2x,v2y));
			if(angleBis>Math.PI){
				angleBis-=(2*Math.PI);
			}
			if(angleBis<-Math.PI){
				angleBis+=(2*Math.PI);
			}
			
			float distance=(float) Math.sqrt( Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
			avance(distance); 
			tourne(angleBis);
		}

		Point2D.Float p1=pointIntel.get(pointIntel.size()-1);
		Point2D.Float p2=pointIntel.get(pointIntel.size()-2);
		float distance=(float) Math.sqrt( Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
		avance(distance); 
		
			
			
			
		th.interrupt();
		System.exit(0);
		//TODO AFFICHER TOUS LES RAYONS DES MOVEPHI APRES UN TOUR
		
	}
}