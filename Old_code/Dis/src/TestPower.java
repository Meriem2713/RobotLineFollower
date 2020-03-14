import lejos.hardware.Battery;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;

public class TestPower {
	public static void main(String[] args){
		System.out.println(Motor.A.getMaxSpeed()+" "+Motor.D.getMaxSpeed());
		Delay.msDelay(3000);
	}
}
