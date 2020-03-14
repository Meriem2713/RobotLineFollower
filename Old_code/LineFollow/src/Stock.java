import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Stock {
	
	
	public static int[][] getColors(String filename) throws IOException{
		File file = new File(filename);
		 
		if (!file.exists())
			return null;
		
		
		FileReader fr = new FileReader(filename);
		BufferedReader br= new BufferedReader(fr);
		
		String s="";
		String[] ss = new String[4];
		
		s = br.readLine();
		int[][] map = new int[Integer.valueOf(s)][3];
		
		for (int i=0; i<map.length;i++) {
			s = br.readLine();
			ss = s.split(":");
			int[] rgb = new int[3];
			for (int j=0; j<3; j++)
				rgb[j] = Integer.valueOf(ss[j]);
			map[i] = rgb;
		}
		br.close();
		
		return map;
	}
	
	public static void stockColors(int[][] colors, String filename) throws IOException{
		int[] rgb;
		FileWriter fw = new FileWriter(filename);
		PrintWriter pw = new PrintWriter(fw);
			
		pw.write(String.valueOf(colors.length) + "\n");
		for (int i=0; i<colors.length; i++) {
			rgb = colors[i];
			pw.write(toString(rgb));
		}
		
		pw.flush();
		pw.close();
	}
	
	private static String toString(int[] t) {
		String result = "";
		for (int i = 0; i<t.length;i++) {
			result += t[i] + ":";
		}
		result += "\n";
		return result;
	}
	private static double distance_color(int[] color1, int[] color2){
		int x=color1[0]-color2[0];
		int y=color1[1]-color2[1];
		int z=color1[2]-color2[2];
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public static int getColor(int[][] colors, float[] rgbFloat) throws IOException {	
		
		/*FileWriter fw = new FileWriter("inutile.txt");
		PrintWriter writer = new PrintWriter(fw);
		
		writer.println("Scanned float:"+rgbFloat[0]+":"+rgbFloat[1]+":"+rgbFloat[2]);*/
		//TODO create log file
		double distances[]=new double[colors.length];
		
		/* on convertit le contenu de rgbFloat allant de 0f a 1f en int allant de 0 a 255 */
		int[] rgb = new int[3];
		for (int i=0; i<3;i++) {
			rgb[i] = (int) (rgbFloat[i] * 256);
			//sSystem.out.println(rgb[i]);
		}
		
		/*On calcule la distance de rgb avec chacune des couleurs de reference*/
		
		for (int i=0; i<colors.length;i++) {
			distances[i]=distance_color(rgb,colors[i]);
		}
		
		/*On recupere l'id de la couleur dont la distance a rgb est la plus basse*/
		
		int id_min=0;
		double distance_min=distances[0];
		
		for(int i=1;i<distances.length;i++){
			if(distances[i]<distance_min){
				distance_min=distances[i];
				id_min=i;
			}
		}
		
		//writer.flush();
		//writer.close();
		return id_min;
	}
	
}
