import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;
import dev.robocode.tankroyale.botapi.graphics.Color;


public class Christiaan extends Bot {

    boolean peek; // Don't turn if there's a bot there
    double moveAmount; // How much to move

    // The main method starts our bot
    public static void main(String[] args) {
        new Christiaan().start();
    }

    // Called when a new round is started -> initialize and do some movement
    @Override
    public void run() {
        // Set colors
        setBodyColor(new Color(0xFF8700));      // McLaren F1 papaya orange (#FF8700)
        setTurretColor(new Color(0x1C1C1C));    // Black accents (#1C1C1C)
        setRadarColor(new Color(0xFFFFFF));     // White for visibility (#FFFFFF)
        setBulletColor(new Color(0xFF8700));    // Papaya orange bullets (#FF8700)
        setScanColor(new Color(0x1C1C1C));      // Black scan lines (#1C1C1C)

        // Initialize moveAmount to the maximum possible for the arena
        moveAmount = Math.max(getArenaWidth(), getArenaHeight());
        // Initialize peek to false
        peek = false;

        // turn to face a wall.
        // `getDirection() % 90` means the remainder of getDirection() divided by 90.
        turnRight(getDirection() % 90);
        forward(moveAmount);

        // Turn the gun to turn right 90 degrees.
        peek = true;
        turnGunLeft(90);
        turnLeft(90);

        // Main loop
        while (isRunning()) {
            // Peek before we turn when forward() completes.
            peek = true;
            // Move up the wall
            forward(moveAmount);
            // Don't peek now
            peek = false;
            // Turn to the next wall
            turnLeft(90);
        }
    }

    // We hit another bot -> move away a bit
    @Override
    public void onHitBot(HitBotEvent e) {
        // If he's in front of us, set back up a bit.
        var bearing = bearingTo(e.getX(), e.getY());
        if (bearing > -90 && bearing < 90) {
            back(100);
        } else { // else he's in back of us, so set ahead a bit.
            forward(100);
        }
    }

    // We scanned another bot -> fire!
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        fire(2);
        // Note that scan is called automatically when the bot is turning.
        // By calling it manually here, we make sure we generate another scan event if there's a bot
        // on the next wall, so that we do not start moving up it until it's gone.
        if (peek) {
            rescan();
        }
    }
}