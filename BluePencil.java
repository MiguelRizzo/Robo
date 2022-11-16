package RoboBluePencil;
import robocode.*;
import java.awt.*;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class BluePencil extends AdvancedRobot {
	int dist = 50;
	int count = 0; 
	double gunTurnAmt;
	String trackName;
	boolean movingForward;
	int turnDirection = 1; 

	public void run() {
		setBodyColor(Color.orange);
		setGunColor(Color.orange);
		setRadarColor(Color.black);
		setScanColor(Color.magenta);
		setBulletColor(Color.white);

		while (true) {
			setTurnRight(10000);
			setMaxVelocity(5);
			ahead(10000);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		if (trackName != null && !e.getName().equals(trackName)) {
			return;
		}

		if (trackName == null) {
			trackName = e.getName();
			out.println("Tracking " + trackName);
		}

		count = 0;

		if (e.getDistance() > 150) {
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

			turnGunRight(gunTurnAmt); 
			turnRight(e.getBearing());
			ahead(e.getDistance() - 140);
			return;
		}

		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);

		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				back(80);
			} else {
				ahead(40);
			}
		}
		scan();
	}

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}

	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());

		if (e.getEnergy() > 16) {
			fire(3);
		} else if (e.getEnergy() > 10) {
			fire(2);
		} else if (e.getEnergy() > 4) {
			fire(1);
		} else if (e.getEnergy() > 2) {
			fire(.5);
		} else if (e.getEnergy() > .4) {
			fire(.1);
		}
		ahead(40);
	}

	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}

	public void reverseDirection() {
		if (movingForward) {
			setBack(25000);
			movingForward = false;
		} else {
			setAhead(25000);
			movingForward = true;
		}
	}
}
