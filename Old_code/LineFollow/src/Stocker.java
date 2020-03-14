import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Stocker {

	/* Le but de cette classe est de faire l'interface entre nos programme et un fichier stocke dans la brique
	 * La HashMap demande en argument de donne en resultat est une HashMap contenant des tableau de 3 cases
	 * 	representant des couleurs au format rgb, associe a un des int representant des couleurs.
	 */
	
	
	private static final String filename = "colors.txt";
	
	
	public static int[][] getColors() throws IOException{
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
	
	public static void stockColors(int[][] colors) throws IOException{
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
}
