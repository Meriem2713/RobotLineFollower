import java.awt.geom.Point2D;
import java.util.LinkedList;


public class TestCalcul {
	public static void main(String[] args){

		Log log=new Log("point.txt");
		log.initialize();
		Point2D.Float p1= new Point2D.Float(0,0);
		Point2D.Float p2= new Point2D.Float(1,3);
		Point2D.Float p3= new Point2D.Float(3,-3);
		float v1x = p2.x-p1.x;
		float v1y = p2.y-p1.y;
		float v2x = p3.x-p1.x;
		float v2y = p3.y-p1.y;
		//float angleBis = (float) Math.acos((v1x*v2x+v1y*v2y)/(Math.sqrt(v1x*v1x +v1y*v1y)*Math.sqrt(v2x*v2x +v2y*v2y)));
		
		//System.out.println(angleBis*180/Math.PI);
		float angle= (float) (Math.atan2(v2x,v2y) - Math.atan2(v1x,v1y));
		System.out.println(angle*180/Math.PI);
	
	}
}
