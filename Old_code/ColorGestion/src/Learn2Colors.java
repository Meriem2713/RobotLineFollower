import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class Learn2Colors {
	public static int[][] average(int [][] colors, int nb_colors, int nb_scans){

 		int averageColor[][]= new int[nb_colors][3];
		for(int i=0;i<nb_colors;i++){
			for(int j=0;j<nb_scans;j++){
				for(int k=0;k<3;k++){
					averageColor[i][k]=averageColor[i][k]+colors[i*nb_scans+j][k];
				}
			}
			for(int k=0;k<3;k++){
				averageColor[i][k]=averageColor[i][k]/nb_scans;
			}
		}
		return averageColor;
	}
	
	public static void main(String[] args) {
		

		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();

		int button;
		int nb_colors = 0;
		int nb_scans = 0;
		g.drawString("How many colors ?", 0, 20, GraphicsLCD.BASELINE);
		g.drawString(String.valueOf(nb_colors), 0, 40, GraphicsLCD.BASELINE);
		while ((button = Button.waitForAnyPress()) != Button.ID_ENTER ) {
			switch (button) {
			case Button.ID_ESCAPE:
				System.exit(-1);
				break;
			case Button.ID_LEFT:
				nb_colors = nb_colors == 0 ? 0 : nb_colors+1;
				break;
			case Button.ID_RIGHT:
				nb_colors++;
				break;
				
				
			}

			g.clear();
			g.drawString("How many colors ?", 0, 20, GraphicsLCD.BASELINE);
			g.drawString(String.valueOf(nb_colors), 0, 40, GraphicsLCD.BASELINE);
		}
		
		g.clear();
		g.refresh();

		g.drawString("How many scans ?", 0, 20, GraphicsLCD.BASELINE);
		g.drawString(String.valueOf(nb_scans), 0, 40, GraphicsLCD.BASELINE);
		
		while ((button = Button.waitForAnyPress()) != Button.ID_ENTER ) {
			switch (button) {
			case Button.ID_ESCAPE:
				System.exit(-1);
				break;
			case Button.ID_LEFT:
				nb_scans = nb_scans == 0 ? 0 : nb_scans+1;
				break;
			case Button.ID_RIGHT:
				nb_scans++;
				break;
			}
			
			g.clear();
			g.drawString("How many scans ?", 0, 20, GraphicsLCD.BASELINE);
			g.drawString(String.valueOf(nb_scans), 0, 40, GraphicsLCD.BASELINE);
		}
		
		EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1);
		// sensor is now designing our color sensor
		
		SensorMode sm = sensor.getRGBMode();
		
		
		int[][] colors = new int[nb_colors][3];
		float[] rgb = new float[3];
		int [][] colorsBis = new int [nb_colors*nb_scans][3];
		
		for (int w=0; w<nb_colors*nb_scans; w++) {
			System.out.println("Press Enter to scan "+1+w/nb_scans+"."+w%nb_scans);
			while ((button = Button.waitForAnyPress()) != Button.ID_ENTER);
		
			sm.fetchSample(rgb, 0);
		
			for (int i=0; i<3; i++) {
				System.out.println(rgb[i]*256);
				colorsBis[w][i] = (int) (rgb[i] * 256);
			}
		}
		colors=average(colorsBis, nb_colors, nb_scans);
		try {
			Stocker.stockColors(colors,"colors1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sensor.close();
		
		
		
		sensor = new EV3ColorSensor(SensorPort.S2);
		// sensor is now designing our color sensor
		
		sm = sensor.getRGBMode();
		
		
		colors = new int[nb_colors][3];
		rgb = new float[3];
		colorsBis = new int [nb_colors*nb_scans][3];
		
		for (int w=0; w<nb_colors*nb_scans; w++) {
			System.out.println("Press Enter to scan "+2+w/nb_scans+"."+w%nb_scans);
			while ((button = Button.waitForAnyPress()) != Button.ID_ENTER);
		
			sm.fetchSample(rgb, 0);
		
			for (int i=0; i<3; i++) {
				System.out.println(rgb[i]*256);
				colorsBis[w][i] = (int) (rgb[i] * 256);
			}
		}
		colors=average(colorsBis, nb_colors, nb_scans);
		try {
			Stocker.stockColors(colors,"colors2.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sensor.close();
	}
}
