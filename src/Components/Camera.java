package Components;

public class Camera implements ICamera{
    int memory = 200;
    public boolean takePicture = false;
    boolean photoSaved = false;


    /**
     * returns true if camera took picture and false if
     * memory was full;
     * @return
     */
    @Override
    public boolean takePicture() {
        if(checkMemory()){
            memory--;
            takePicture = true;
            return true;
        }
        else {
            takePicture = false;
            return false;
        }
    }

    @Override
    public boolean checkMemory() {
        if(memory <= 0){
            photoSaved = false;
            return false;
        }
        photoSaved = true;
        return true;
    }
    public boolean getPhotoSaved(){
        return this.photoSaved;
    }
    public boolean getTakePicture(){
        return this.takePicture;
    }
}
