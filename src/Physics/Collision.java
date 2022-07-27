package Physics;

public class Collision {
    boolean ftSensor = false;//Front
    boolean rSensor = false;//Right
    boolean rrSensor = false;//Rear
    boolean lSensor = false;//Left
    boolean bSensor = false;//Bottom
    boolean landing = false;//If



    public void checkSensor(){
        if(ftSensor){
            System.out.println("Front Sensor Activated!");
        }else if(rSensor){
            System.out.println("Right Sensor Activated!");
        }else if(rrSensor){
            System.out.println("Rear Sensor Activated!");
        }else if(lSensor){
            System.out.println("Left Sensor Activated!");
        }else if(bSensor & !landing){
            System.out.println("Bottom Sensor Activated!");
        }
    }

    public void setLanding(){
        landing = true;
    }

    public void setNotLanding(){
        landing = false;
    }

    public void setFtSensor(){
        ftSensor = true;
    }

    public void setrSensor(){
        rSensor = true;
    }

    public void setRrSensor(){
        rrSensor = true;
    }

    public void setlSensor(){
        lSensor = true;
    }

    public void setBSensor(){
        bSensor = true;
    }

    public void resetSensors(){
        ftSensor = false;
        rSensor = false;
        rrSensor = false;
        lSensor = false;
        bSensor = false;
    }
}
