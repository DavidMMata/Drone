package Controllers;

import Components.Battery;
import Components.Camera;
import Components.Photo;
import Model.Info;
import Physics.Location;
import Physics.Collision;
import Model.Direction;
import javafx.css.Size;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    //State variables
    double time = 1;
    double speed = 1;
    double distance = speed * time;
    double velocity = distance / time;
    boolean isAutoPilot;
    private Location homeLocation = new Location(0, 0, 0);
    private Location currentLocation = homeLocation;
    private LinkedList<Direction> collisions = new LinkedList<Direction>();
    private Collision collision = new Collision();
    private Battery battery = new Battery();
    private Camera camera = new Camera();
    private Photo photo = new Photo();
    private Info droneInfo = new Info(currentLocation, 0, battery, camera);

    final int SIZE = 11;
    final int HOME = (int) SIZE/2;
    char grid[][] = new char[SIZE][SIZE];
    char altitude[] = new char[SIZE];

    double maxVelocity = 5;
    String prevInput;
    MotionController motionController;
    static int stepCounter = 0;


    private void fillGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = '.';
            }

            altitude[i] = '.';
        }

        grid[HOME][HOME] = 'D';
        altitude[0] = 'D';

    }

    private void checkDubs(String input) {
        if (input.equals(prevInput) && velocity < maxVelocity) {
            velocity += 1;
        } else if (velocity >= maxVelocity && input.equals(prevInput)) {
            velocity = 5;
        } else {
            velocity = 1;
        }
    }

    void manualControl() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        motionController = new MotionController(droneInfo);

        while (running) {
            System.out.println("   COMMAND:");
            isAutoPilot = false;
            boolean batteryCharge = true;
            boolean currentAltitude = true;
            boolean currentLocation = true;
            boolean currentDirection = true;

            //events
            String clearedCollision = "";
            String newCollision = "";
            String groundCollision = "";
            String motionControl = "";

            String report = "x";
            String transmit = "x";
            String goHome = "";
            String changeVector = "v";
            String takePicture = "";
            String rotate = "";
            String takePhoto = "";
            String newPhoto = "";
            String savePhoto = "";

            String input;
            switch (input = scanner.nextLine()) {
                case "rr": // Rotate Right
                    motionController.setDegrees(Direction.RIGHT);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Rotate right");
                    rotate = input;

                    break;
                case "rl": // Rotate Left
                    motionController.setDegrees(Direction.LEFT);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Rotate left");
                    rotate  = input;
                    break;
                //Some sort of call/output here
                case "u": // Move Up
                    checkDubs(input);
                    motionController.moveZPlane(velocity, Direction.UP);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Elevation up");
                    break;
                case "d": // Move down
                    checkDubs(input);
                    motionController.moveZPlane(velocity, Direction.DOWN);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Elevation down");
                    break;
                case "f": // Move Forward
                    checkDubs(input);
                    motionController.moveXYPlane(velocity, Direction.FORWARD);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Travel forward");
                    break;
                case "b":  // Move Backwards
                    checkDubs(input);
                    motionController.moveXYPlane(velocity, Direction.BACKWARD);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Travel backwards");
                    break;
                case "l": // Move Left
                    checkDubs(input);
                    motionController.moveXYPlane(velocity, Direction.LEFT);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Travel left");
                    break;
                case "r": // Move Right
                    checkDubs(input);
                    motionController.moveXYPlane(velocity, Direction.RIGHT);
                    checkCollisions();
                    battery.reduceBattery(0.5);
                    System.out.println("Travel right");
                    break;
                    //TODO
                case "stop":
                    checkDubs(input);
                    System.out.println("Stops the drone");
                    battery.reduceBattery(0.5);
                    running = false;
                    changeVector = "";
                    break;
                case "home":
                    isAutoPilot = true;
                    motionController.goHome();
                    battery.reduceBattery(0.5);
                    //battery.checkBattery();
                    goHome = input;
                    break;
                case "p":
                    camera.takePicture();
                    takePicture = "p";
                    takePhoto = "p";
                    if(camera.getTakePicture()) newPhoto = "p"; //
                    if(camera.getPhotoSaved()) savePhoto = "p"; // if there is enough memory


                default:
                    System.out.println("Enter a valid input");
                    changeVector = "";
                    break;
            }
            prevInput = input;
            step();
        }
    }

    private void printState() {
        System.out.println("Current State:");
        System.out.println("Info:");
        System.out.println("Velocity: " + velocity + " mph");
        System.out.print("Current Location: ");
        droneInfo.getCurrentLocation().printLocation();
        System.out.print("Current Altitude: ");
        droneInfo.getCurrentLocation().printAltitude();
        System.out.println("Collisions: " + collisions.toString());
        System.out.println("Current Direction: " + droneInfo.getCurrentDirection() + " degrees");
        System.out.print("Battery status: ");
        droneInfo.getBatteryStatus();
        System.out.println(droneInfo.checkPhoto() ? "Photo taken, saved photo." : "No photo taken.");
        System.out.println();
    }

    private void translateCoords() {
        Location location = droneInfo.getCurrentLocation();
        int x = (int) (location.getX() + 50) / 10;
        int y = (int) (-1 * location.getY() + 50) / 10;
        int z = (int) (location.getZ()) / 40;

        if(location.getZ() < 0){
            System.out.println("Error: You crashed.");
            System.exit(0);
        }else if(location.getZ() >= 400) {
            // altitude collision error
            System.out.println("Error: trying to fly past 400ft limit");
            altitude[SIZE-1] = 'D';
        }

        if(location.getX() < -50 || location.getY() < -50
                || location.getX() > 50 || location.getY() > 50){
            System.out.println("Error: You crashed.");
            System.exit(0);
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = '.';
            }

            altitude[i] = '.';
        }

        if(y >= SIZE || y < 0 || x >= SIZE || x < 0) {
            // XY collision error
        } else {
            grid[y][x] = 'D';
            altitude[z] = 'D';
        }


    }

    private void printGrid() {
        translateCoords();

        // Print Drone in XY Plane
        System.out.print("  ");
        for(int i = 0; i < SIZE*2; i++) {
            System.out.print("-");
        }
        System.out.print("    -\n");
        for(int i = 0; i < SIZE; i++) {
            System.out.print("| ");
            for(int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.print("|   ");
            System.out.print(altitude[SIZE - 1 - i] + "\n");
        }
        System.out.print("  ");
        for(int i = 0; i < SIZE*2; i++) {
            System.out.print("-");
        }
        System.out.print("    -\n");

    }

    public void step(){
        stepCounter++;

        System.out.println("   STEP " + stepCounter);
        System.out.println();
        printState();
        printGrid();

        System.out.println();


    }

    public void checkCollisions() {
        collisions.clear();

        Location location = droneInfo.getCurrentLocation();
        double angle = droneInfo.getCurrentDirection();
        char direction;

        if(angle<45 || angle>315) direction = 'N';
        else if(angle<135) direction = 'E';
        else if(angle<225) direction = 'S';
        else direction = 'W';

        if(location.getX() >= 45){
            switch(direction) {
                case('N'):
                    collisions.add(Direction.RIGHT);
                    break;
                case ('E'):
                    collisions.add(Direction.FRONT);
                    break;
                case ('S'):
                    collisions.add(Direction.LEFT);
                    break;
                case ('W'):
                    collisions.add(Direction.BACKWARD);
                    break;
                default :
            }
        }
        if(location.getX() <= -45){
            switch(direction) {
                case('N'):
                    collisions.add(Direction.LEFT);
                    break;
                case ('E'):
                    collisions.add(Direction.BACKWARD);
                    break;
                case ('S'):
                    collisions.add(Direction.RIGHT);
                    break;
                case ('W'):
                    collisions.add(Direction.FRONT);
                    break;
                default :
            }
        }
        if(location.getY() >= 45){
            switch(direction) {
                case ('N'):
                    collisions.add(Direction.FRONT);
                    break;
                case ('E'):
                    collisions.add(Direction.LEFT);
                    break;
                case ('S'):
                    collisions.add(Direction.BACKWARD);
                    break;
                case ('W'):
                    collisions.add(Direction.RIGHT);
                    break;
                default:
            }
        }
        if(location.getY() <= -45){
            switch(direction) {
                case ('N'):
                    collisions.add(Direction.BACKWARD);
                    break;
                case ('E'):
                    collisions.add(Direction.RIGHT);
                    break;
                case ('S'):
                    collisions.add(Direction.FRONT);
                    break;
                case ('W'):
                    collisions.add(Direction.LEFT);
                    break;
                default:
            }
        }

        if(location.getZ() < 5){
            collisions.add(Direction.BOTTOM);
        }

    }

    public static void main(String[] args) {

        Main main = new Main();
        main.fillGrid();
        main.manualControl();
    }
}

