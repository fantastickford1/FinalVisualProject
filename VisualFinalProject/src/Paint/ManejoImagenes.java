/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author sbpaco
 */
public class ManejoImagenes {
    private final FileChooser fileDialog;
    private File file;
    private File myPath;
    
    private String imageFormat = "png";
    private Image imagen = null;
    
    private Canvas canvas;

    public ManejoImagenes() {
        this.fileDialog = new FileChooser();
        this.myPath = null;
        this.file = null;
    }

    public ManejoImagenes(String PATH) {
        this.fileDialog = new FileChooser();
        this.myPath = new File(PATH);
        this.file = null;
    }
    
    public Image openFile(){
        fileDialog.setTitle("Abrir imagen");
        fileDialog.setInitialDirectory(this.myPath);

        file = fileDialog.showOpenDialog(new Stage());
        
        if (file == null) {
            return imagen;
        } else {
            try {
                imagen = new Image(file.toURI().toString());
            } catch (Exception e) {
                e.printStackTrace();    
            }
        }
        
        return imagen;
    }

    public void saveAsImage(GraphicsContext gc) {
        fileDialog.setTitle("Guardar Imagen");
        fileDialog.setInitialDirectory(this.myPath);
        fileDialog.setInitialFileName("untitled");
        
        FileChooser.ExtensionFilter allExt = new FileChooser.ExtensionFilter("All files", "*.*");
        FileChooser.ExtensionFilter pngExt = new FileChooser.ExtensionFilter("png", "*.png");
        FileChooser.ExtensionFilter jpgExt = new FileChooser.ExtensionFilter("jpg", "*.jpg");
        
        //Agrega las extensiones 
        fileDialog.getExtensionFilters().addAll(allExt, pngExt, jpgExt);
        file = fileDialog.showSaveDialog(new Stage());
        
        if (file == null) {
            return;
        }
        
        ExtensionFilter selectedExt = fileDialog.getSelectedExtensionFilter();
        
        if (selectedExt == jpgExt) {
            imageFormat = "jpg";
        }

        //Agrega la extensión al nombre del archivo, si no fue especificada
        String fileName = file.getName().toLowerCase();
        switch (imageFormat) {
            case "jpg":
                if (!fileName.endsWith(".jpeg") && !fileName.endsWith(".jpg")) {
                    file = new File(file.getParentFile(), file.getName() + ".jpg");
                }
                break;
            case "png":
                if (!fileName.endsWith(".png")) {
                    file = new File(file.getParentFile(), file.getName() + ".png");
                }
        }
        
        canvas=gc.getCanvas();
        
        int wd = (int)canvas.getWidth();
        int hg = (int)canvas.getHeight();
        
        
        WritableImage image = new WritableImage(wd, hg);
        canvas.snapshot(null, image);
        
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), imageFormat, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveImage(GraphicsContext gc){
        if (file != null) {            
            canvas = gc.getCanvas();

            int wd = (int) canvas.getWidth();
            int hg = (int) canvas.getHeight();

            WritableImage image = new WritableImage(wd, hg);
            canvas.snapshot(null, image);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), imageFormat, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            saveAsImage(gc);
        }
    }
}
