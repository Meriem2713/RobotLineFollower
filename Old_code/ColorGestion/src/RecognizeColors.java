import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class RecognizeColors {
	
	private static final String filename = "rc.log";
	public static void main(String[] args) throws IOException {
		
		HiTechnicColorSensor sensor = null;
		try {
			sensor = new HiTechnicColorSensor(SensorPort.S1);
			
		}catch (Exception e) {
			LCD.drawString("No sensor detected", 0, 0);
			Delay.msDelay(3000);
			System.exit(-1);
		}
		SensorMode sensorMode = sensor.getRGBMode();
		
		int[][] colors = Stocker.getColors("colors.txt");
		
		System.out.println("Press Enter to scan a color");
		
		float[] rgb = new float[3];
		int color;
		int button;
		
		while ((button = Button.waitForAnyPress()) != Button.ID_ESCAPE) {
			if (button == Button.ID_ENTER) {
				sensorMode.fetchSample(rgb, 0);
				color = getColor(colors,rgb);
				System.out.println("You scanned the color "+color+"\nPress Enter to scan another color");
			}
		}		
	}
	
	private static int getColor(int[][] colors, float[] rgbFloat) throws IOException {
		
		FileWriter fw = new FileWriter(filename);
		PrintWriter writer = new PrintWriter(fw);
		
		writer.println("Scanned float:"+rgbFloat[0]+":"+rgbFloat[1]+":"+rgbFloat[2]);
		//TODO create log file
		int[][] differences = new int[colors.length][3];
		
		/* on convertit le contenur de rgbFloat allant de 0f ï¿½ 1f en int allant de 0 ï¿½ 255 */
		int[] rgb = new int[3];
		for (int i=0; i<3;i++) {
			rgb[i] = (int) (rgbFloat[i] * 256);
		}
		
		for (int i=0; i<colors.length;i++) {
			for (int j=0;j<3;j++) {
				differences[i][j] = colors[i][j] > rgb[j] ? (colors[i][j] - rgb[j]) : (rgb[j] - colors[i][j]);
				writer.write(i+":"+j+":"+differences[i][j]+"\n");
			}
		}
		
		/* on a les differences entre les couleurs primaires des couleurs stockes et de la couleur stocke
		 * mnt on dois decider de quelle couleurs a le moins de differences avec la couleur stocke
		 */
		
		/*on fais la somme des differences et on renvois la couleur avec la somme la plus faible */
		//TODO faire la distance abrtuis bête
		int result = 0;
		int colorResult = 0;
		int sum = 0;
		
		for (int j=0; j<3;j++) {
			sum += differences[0][j];
		}
		result = sum;
		
		for (int i=1; i<differences.length;i++) {
			sum = 0;
			for (int j=0; j<3;j++) {
				sum += differences[i][j];
			}
			writer.write(i+":"+sum+"\n");
			if (sum <result) {
				result = sum;
				colorResult = i;
			}
		}
		writer.flush();
		writer.close();
		return colorResult;
	}
}
