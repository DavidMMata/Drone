package Controllers;

import Model.Info;
import Physics.Location;
import Model.Direction;

import java.text.DecimalFormat;

public class MotionController {

    Info droneInfo;
    double degrees;
    Location location;
    double deltaDegrees = 10;

    public MotionController(Info droneInfo){
        this.droneInfo = droneInfo;
        this.location = droneInfo.getCurrentLocation();
        this.degrees = droneInfo.getCurrentDirection();
    }
//    /**
//     * Prints the the status of the drone with location and degrees.
//     */
//    public void printStatus(){
//        DecimalFormat df = new DecimalFormat("0.00");
//        System.out.println("Position x: " + df.format(location.getX()) + " y: " +
//                df.format(location.getY()) + " z: " + df.format(location.getZ()));
//        System.out.println("Degrees " + displayDegrees(degrees + 90));
//
//
//    }

    /**
     * Checks the direction the drone is moving to. Returns a positive or negative value
     * depending on the direction the drone is headed based on the grid
     * @param moveby
     * @param direction
     * @return
     */
    public double checkNegativeDirection(double moveby, Direction direction){
        switch (direction){
            case LEFT:
                moveby *= -1;
                break;
            case BACKWARD:
                moveby *= -1;
                break;
        }
        return moveby;
    }

    /**
     * Two cases
     * First case: When When Degrees is 0 90 180 270 or 360 only one a move in one direction will matter
     * @param moveBy
     * @param direction
     */
    public void moveXYPlane(double moveBy, Direction direction){
        double moveX = Math.cos(Math.toRadians(degrees)) * moveBy;
        double moveY = Math.sin(Math.toRadians(degrees + 90)) * moveBy;
        double moveByAgain =(checkNegativeDirection(moveBy,direction));
        moveX = checkNegativeDirection(moveX,direction);
        moveY = checkNegativeDirection(moveY,direction);

        //When Degrees is 0 90 180 270 or 360 only one a move in one direction will matter
        if(degrees == 0 || degrees == 90 || degrees == 180 || degrees == 270 || degrees == 360){
            //moves left and right when its in starting position
            if((degrees == 0 || degrees == 180 || degrees == 360) &&
                    (direction == Direction.RIGHT || direction == Direction.LEFT)){
                location.setX(location.getX() + moveX);
            }
            // moves the drone on the y plane when moves are left and right
            //and drone is facing 90 or 270 degrees
            else if (((degrees) == 90 || (degrees) == 270) &&
                    (direction == Direction.RIGHT || direction == direction.LEFT)){

                location.setY(location.getY() + moveByAgain);

            }
            //moves the drone forward and backwards when its facing 90 degrees or 270 degrees and based on these commands
            else if(((degrees+ 90) == 90 || (degrees+90) == 270) &&
                    (direction == Direction.FORWARD || direction == direction.BACKWARD)){
                location.setY(location.getY() + moveY);
            }
            //moves the drone forward and backwards when its 180 or 0 degrees
            else{
                location.setX(location.getX() + moveByAgain);
            }
        }
        //When degrees is not 0 90 180 270 or 360
        else{
            // Movement Left and Right
            if(direction == Direction.RIGHT || direction == Direction.LEFT){
                moveY = Math.cos(Math.toRadians(degrees)) * moveBy;
                moveY = checkNegativeDirection(moveY,direction);
            }
            // Movement Forward and Backwards.
            else{
                moveX = Math.sin(Math.toRadians(degrees + 90)) * moveBy;
                moveX = checkNegativeDirection(moveX,direction);
            }
            location.setX(location.getX() + moveX);
            location.setY(location.getY() + moveY);
        }
        droneInfo.setCurrentLocation(this.location);
    }
    /**
    Move in the Z (Up/ Down) Direction
     */
    public void moveZPlane(double moveBy, Direction direction){
        if(direction.equals(Direction.UP)){
            if((location.getZ() + Math.cos(degrees) < 400)){
                location.setZ(location.getZ() + moveBy);
            }else{
                location.setZ(400);
            }
        }
        else{
            location.setZ(location.getZ() - moveBy);
        }
        droneInfo.setCurrentLocation(this.location);
    }
    /**
    Sets the Degrees in which the drone is facing.
    Degrees is only between 0 and 360 inclusive.
     */
    public void setDegrees(Direction direction){
        if(direction.equals(Direction.RIGHT)){
            if( degrees + deltaDegrees > 360) degrees = degrees  + deltaDegrees - 360;
            else degrees += deltaDegrees;
        }
        else{
            if(degrees - deltaDegrees < 0)
                degrees = 360 - Math.abs(degrees - deltaDegrees);
            else degrees -= deltaDegrees;
        }
        droneInfo.setCurrentDirection(this.degrees);
    }

    /**
     * returns the degrees in which the drone is facing.
     * @param degrees
     * @return
     */
    public double displayDegrees(double degrees){
        if( degrees   > 360) degrees = degrees  - 360;
        if(degrees   < 0) degrees = 360 - Math.abs(degrees );
        return  degrees;
    }

    /**
     * Takes the drone home with the default values
     */
    public void goHome(){
        location.setX(0);
        location.setY(0);
        location.setZ(0);
        degrees = 0;
        droneInfo.setCurrentLocation(this.location);
        droneInfo.setCurrentDirection(this.degrees);
    }
}
