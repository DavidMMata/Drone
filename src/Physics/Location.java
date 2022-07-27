package Physics;

public class Location {

    private double x,y,z;
    private final int WALLSIZE = 50;

    boolean crashed = false;

    public Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x) {
        if(x <= WALLSIZE && x >= (WALLSIZE * -1)) {
            this.crashed = true;
        }
        this.x = x;

    }

    public void setY(double y) {
        if(y <= WALLSIZE && y >= (WALLSIZE * -1)) {
            this.crashed = true;
        }
        this.y = y;
    }

    public void setZ(double z) {
       this.z = z;
    }

    public void printLocation() {
        System.out.println("(x: " + this.x + " , y: " + this.y + ")");
    }

    public String getLocation(){return "(x: " + this.x + " , y: " + this.y + ")";}

    public void printAltitude() {
        System.out.println(this.z + " ft");
    }
}
