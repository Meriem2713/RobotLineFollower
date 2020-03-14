
public class Move {
	private boolean aBigger;
	private long duration;
	
	public Move(boolean aBigger, long duration) {
		this.aBigger = aBigger;
		this.duration = duration;
	}
	
	public boolean aBigger() {
		return aBigger;
	}
	
	public long duration() {
		return duration;
	}
}
