package Components;

import java.io.File;
import java.util.ArrayList;

public class Photo {
    int maxPhotos;
    int numOfPhotos;
    ArrayList<File> photos = new ArrayList<>();

    public Photo(){
        maxPhotos = 200;
    }

    public void savePhoto(File photo){
        photos.add(photo);
        if(numOfPhotos >= maxPhotos){
            photos.remove(0);
            photos.add(photo);
            numOfPhotos = photos.size();
        }

    }

    public void changeMaxPhotos(int max){
        maxPhotos = max;
    }
}
