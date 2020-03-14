import java.io.IOException;
import java.util.HashMap;

public class StockerTest {
	
	public static void main(String[] args) throws IOException {
		
		int[][] colors = new int[3][3];
		
		int[] black = {0,0,0};
		int[] red = {255,0,0};
		int[] green = {0,255,0};
		int[] blue = {0,0,255};
		int[] white = {255,255,255};
		
		colors[0] = black;
		colors[1] = white;
		colors[2] = red;
		
		Stocker.stockColors(colors,"colorsTest.txt");
		
	}
}
