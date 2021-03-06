
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;

public class totalMain {
	public static void main(String[] args) {
		
		// rnn for movement
		// commands are: moveForward(), moveBackward(), turnLeft(), steerLeft(),
		// turnRight(), steerRight(), stawp(), shut()
		// explanations for each command are in go2.java
		go2 rnn = new go2();
		
		// slap for slapping and slap counting
		// define how many slaps with slap.Slap(int)
		slapMotor slap = new slapMotor();
		
		// a die to randomly turn
		DieRoll die = new DieRoll();
		die.Roll();
		
		//leds
		LEDS leds = new LEDS();
		
		// telem (short for telemetry), to check distance
		EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S1);
		IRSensor telem = new IRSensor(irSensor);
		telem.start();
		
		// press ESCAPE on the EV3 to quit
		while (!Button.ESCAPE.isDown()) {
			
			
			LCD.clear();
			LCD.drawString("Distance: " + telem.GetDistance(), 0, 0);
			LCD.drawString("WHAT SHALL I", 0, 2);
			LCD.drawString("DESTROY THIS TIME?", 0, 3);
			LCD.drawString("slap count: " + slap.getSlapCount(), 1, 5);
			LCD.drawString("die: " + die.getRoll(), 1, 6);
			Delay.msDelay(50);
			
			// Search for a target
			if (telem.GetDistance() > 49.0f){
				if (die.getRoll() == 1) {
					rnn.turnRight();
					leds.searchlight();
				}
				if (die.getRoll() == 2) {
					rnn.turnLeft();
					leds.searchlight();
				}
			}
			// approach the target
			if (telem.GetDistance() > 8.0f && telem.GetDistance() < 49.0f) {
				rnn.moveForward();
				leds.approach();
			}
			// destroy the target
			// slap.Slap(int)
			if (telem.GetDistance() < 8.0f){
				rnn.stawp();
				
				slap.Slap(2);
				
				leds.slap();
				rnn.moveBackward();
				die.Roll();
				Delay.msDelay(1500);
			}
		}
		// shut everything down
		leds.shutDown();
		rnn.shut();
		slap.shutSlap();
		telem.stopSampling();
		Delay.msDelay(500);
		irSensor.close();
	}
}
