package Model;

import Components.Battery;
import Components.Camera;
import Physics.Location;


public class Info {

    private Location currentLocation;
    //direction aka degrees
    private double currentDirection;
    private Battery battery;
    private Camera camera;

    public Info(Location currentLocation, double currentDirection, Battery battery, Camera camera) {
        this.currentLocation = currentLocation;
        this.currentDirection = currentDirection;
        this.battery = battery;
        this.camera = camera;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public double getCurrentDirection() {
        return this.currentDirection;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public void setCurrentDirection(double direction) {
        this.currentDirection = direction;
    }

    public void getBatteryStatus() {
        battery.checkBattery();
    }

    public boolean checkPhoto() {
        if (this.camera.getTakePicture()) {
            this.camera.takePicture = false;
            return true;
        }
        else {
            return false;
        }
    }
}
