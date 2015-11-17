/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NoteBlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Karlos
 */
public class GestorArchivos {
    private static FileChooser fileDialog;
    private static File file;
    private static File myPath;
    
    public GestorArchivos(String Path){
        
        this.fileDialog = new FileChooser();
        this.myPath = new File(Path);
        this.file = null;
    }
    
    public boolean checkDirectory(){
        
        if(this.myPath.exists()){
            System.out.println("El direcctorio existe");
            return true;
        }else{
            System.out.println("Fichero no existe");
            
            try {
                this.myPath.mkdir();
            } catch (Exception e) {
                System.out.println("El fichero no pudo ser creado");
                return false;
            }
        }
        return false;
        
    }
    
    
    public String openFile(){
        fileDialog.setTitle("Open Resource File");
        fileDialog.setInitialDirectory(this.myPath);
        fileDialog.getExtensionFilters().addAll(
        new ExtensionFilter("Text Files", "*.txt"),
        new ExtensionFilter("All Files", "*.*"));
        
        file = fileDialog.showOpenDialog(new Stage());
        
        if(file == null)
            return "";
        else{
            try {
                String texto = "";
                List<String> ls = Files.readAllLines(this.file.toPath());
                for (int i = 0; i < ls.size(); i++) {
                    texto += ls.get(i) + "\n";
                }
                return texto;
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }
    
    public void saveFileAs(String texto){
        fileDialog.setTitle("Guardar");
        fileDialog.setInitialDirectory(this.myPath);
        fileDialog.getExtensionFilters().addAll(
        new ExtensionFilter("Text Files", "*.txt"),
        new ExtensionFilter("All Files", "*.*"));
        
        file = fileDialog.showSaveDialog(new Stage());
        
        if(file == null)
            return;
        try {
            Files.write(file.toPath(), texto.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveFile(String texto){
        fileDialog.setTitle("Guardar");
        if(file != null){
            try {
                Files.write(file.toPath(), texto.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            fileDialog.setInitialDirectory(this.myPath);
            fileDialog.getExtensionFilters().addAll(
            new ExtensionFilter("Text Files", "*.txt"),
            new ExtensionFilter("All Files", "*.*"));
        
            file = fileDialog.showSaveDialog(new Stage());
        
            if(file == null)
                return;
            try {
                Files.write(file.toPath(), texto.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public File getPathDirectoty(){
        return this.myPath;
    }
    
    
    
}
