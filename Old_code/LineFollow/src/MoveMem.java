
public class MovePhi {
	private float distanceA;
	private float distanceD;
	private float angle;
	private float corvatureRadius;
	private float dx;
	private float dy;
	
	private static float DIA = 12.6f;
	
	public MovePhi(float dA, float dD){
		distanceA=dA;
		distanceD=dD;
	}
	
	public void createInfo(){
		//corvatureRadius=(DIA*(distanceA+distanceD))/(2*(Math.max(distanceA, distanceD)-Math.min(distanceA, distanceD)));
		corvatureRadius=18.9f;
		angle=((distanceA+distanceD)/2)/(corvatureRadius);
		
		dx =(float) (corvatureRadius*(1-Math.cos(angle)));
		if(distanceA > distanceD){
			dx=-dx;
		}
		dy= (float) (corvatureRadius*Math.sin(angle));
		if(distanceD>distanceA){
			angle=-angle;
		}
	}
	public float getDistanceA(){
		return distanceA;
	}
	public float getDistanceD(){
		return distanceD;
	}
	public float getAngle(){
		//return (float) ((distanceA-distanceD)/DIA);
		//return ((TestMemPhi.speedHigh/TestMemPhi.speedLow)*(Math.min(distanceA, distanceD)-1))/TestMemPhi.DIA;
		//return ((distanceA+distanceD)/2)/TestMemPhi.RAYON;
		//return (DIA*(distanceA+distanceD))/(2*(Math.max(distanceA, distanceD)-Math.min(distanceA, distanceD)));
		return angle;
	}

	public float getCorvatureRadius(){
		return corvatureRadius;
	}
	public float getDX(){
		return dx;
	}
	public float getDY(){
		return dy;
	}
	
	public float getRotatedDX(float angle){
		return (float) (getDX()*Math.cos(angle) +getDY()*Math.sin(angle));
	}
	public float getRotatedDY(float angle){
		return (float) (getDY()*Math.cos(angle) - getDX()*Math.sin(angle));
	}
	
	//TODO essayer rayon de courbure= (speedHigh/speedLow)*diametre
	//TODO essayer angle = max(dA,dD)/diametre
}