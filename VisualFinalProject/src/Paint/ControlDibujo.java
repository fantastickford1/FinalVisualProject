/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author sbpaco
 */
public class ControlDibujo {
    
    private Image currentImage;
    private ArrayList<Image> arrayImages = new ArrayList<Image>();
    private int pos = -1;
    
    public int getSizeArrayImages(){
        return arrayImages.size();
    }
    
    public ControlDibujo(){
        System.out.println(pos);
    }

    public void setCurrentImage(Image currentImage){
        ++pos;
        this.currentImage = currentImage;
        arrayImages.add(currentImage);
        System.out.println(pos);
    }
    
    public Image getUndo(){
        if (pos > 0) {
            pos--;
        }
        
        System.out.println(pos);
        return arrayImages.get(pos);
    }
    
    public Image getRedo(){
        if (pos < (arrayImages.size()-1)) {
            pos++;
        }
        
        System.out.println(pos);
        return arrayImages.get(pos);
    }
    
    public void resetArray(){
        arrayImages.clear();
        pos = 0;
    }
}
