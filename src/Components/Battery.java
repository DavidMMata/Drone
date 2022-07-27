package Components;

public class Battery {

    private double batteryLevel = 100;

    public void checkBattery() {
        if(batteryLevel >= 20){
            okBattery();
        }else {
            if(batteryLevel < 5) {
                lowBattery();
            }else{
                warningBattery();
            }
        }
    }

    public void reduceBattery(){
        batteryLevel -= 1;
    }

    public void reduceBattery(double bl){
        batteryLevel -= bl;
    }

    public void okBattery(){
        //Set LED to Green
        System.out.println("Battery Level Ok.");
    }

    public void warningBattery(){
        //Set LED to red
        System.out.println("Battery Level Less Than 20%!");
    }

    public void lowBattery(){
        //goHome();
        System.out.println("Battery Level Less Than 5%!");
    }
}
