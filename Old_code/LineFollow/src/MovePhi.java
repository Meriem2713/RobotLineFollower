
public class MovePhi {
	private float distanceA;
	private float distanceD;
	
	private static float DIA = 12.6f;
	
	public MovePhi(float dA, float dD){
		distanceA=dA;
		distanceD=dD;
	}
	
	public float getAngle(){
		return (float) (((distanceA-distanceD)/DIA)%(2*Math.PI));
	}

	public float getCorvatureRadius(){
		return (distanceA+distanceD)/(2*getAngle());
	}
	public float getDX(){
		return (float) (getCorvatureRadius()*(1-Math.cos(getAngle())));
	}
	public float getDY(){
		return (float) (getCorvatureRadius()*Math.sin(getAngle()));
	}
	
	public float getRotatedDX(float angle){
		return (float) (getDX()*Math.cos(angle) +getDY()*Math.sin(angle));
	}
	public float getRotatedDY(float angle){
		return (float) (getDY()*Math.cos(angle) - getDX()*Math.sin(angle));
	}
	
}
