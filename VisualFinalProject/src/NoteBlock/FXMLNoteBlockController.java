package NoteBlock;

import FileBrowser.OpenFileBrowserFXMLController;
import FileBrowser.SaveFileBrowserFXMLController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import visualfinalproject.UserUIFXMLController;

/**
 *
 * @author none
 */

public class FXMLNoteBlockController implements Initializable {
    
    //Declaraciones de Componentes
    @FXML private Label label;
    @FXML private TextArea TextoArea;
    @FXML private ComboBox fontChose,fontSizeChose;
    //Variables
    private Stage stage;
    private Parent root;
    private Scene scene;
    
    //Instancias Utiles
    GestorArchivos Archivos;
    Controla ct = new Controla();
    private Version v = new Version();  //Gestion de entrada de datos, texto
    private File recoveredFile;
    
    //Evento de entrada de teclado
    @FXML private void TextAreaArray(Event event){
        System.out.println("Entrada");
        ct.setString(TextoArea.getText());
              
        //Modificacion de Hacer y deshacer.     
        v.setSiguiente(new Version(TextoArea.getText()));
        v = v.getSiguiente();
    }
    
    @FXML private void rehacer(ActionEvent event){   
        if(v.getSiguiente() != null){
            v = v.getSiguiente();
            TextoArea.setText(v.getTexto());
        }
    }
    
    @FXML private void deshacer(ActionEvent event){
       
        if(v.getAnterior() != null){
            v = v.getAnterior();
            TextoArea.setText(v.getTexto());
        }
    }
    
    @FXML private void salir(ActionEvent event){
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("¿Desea salir?");
        alert.setHeaderText("Importante");
        alert.setContentText("¿Desea salir realmente?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK)
            System.exit(0);   
        
    }
    
     @FXML private void creditos(ActionEvent event){
        
        Alert alert1 = new Alert(AlertType.INFORMATION);
        alert1.setTitle("Universidad Politécnica de Chiapas");
        alert1.setHeaderText("Creditos");
        alert1.setContentText("Alumno: ********");
        alert1.showAndWait();
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fontChose.getItems().addAll("Lucida Bright","Arial","Bell MT","Calibri","Century Gothic","Comic Sans MS");
        fontSizeChose.getItems().addAll(8,9,10,11,12,14,16,18,20,22,24);
    }    
    
    @FXML
    private void FontStyle(){
        String font = "";
        int size = 4;
        try {
            size = (int) fontSizeChose.getValue();
            font =  (String) fontChose.getValue();
        } catch (Exception e) {
        }
        
        TextoArea.setFont(new Font(font, size));
        
    }
    
    
    @FXML private void abrir(ActionEvent event){
        //this.TextoArea.setText(this.Archivos.openFile());
        stage = new Stage();
        FXMLLoader myloader = new FXMLLoader(
        getClass().getResource("/FileBrowser/OpenFileBrowserFXML.fxml"));
        try {
            root = (Parent) myloader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLNoteBlockController.class.getName()).log(Level.SEVERE,null,ex);
        }
        OpenFileBrowserFXMLController myController = myloader.getController();
        Scene fileBrowser = new Scene(root);
        myController.setScene(fileBrowser);
        myController.setStage(stage);
        stage.setScene(fileBrowser);
        stage.setTitle("Open File");
        stage.showAndWait();
        String text = myController.getReadedString();
        String text2[] = text.split("#separatorKR17@");
        text = text2[1];
        this.TextoArea.setText(text2[0]);
        String text3[] = text.split(",");
        this.TextoArea.setFont(new Font(text3[0], Integer.parseInt(text3[1])));
        try {
           this.recoveredFile =  new File (myController.getFilePath()); 
        } catch (Exception e) {
            System.err.println("Can't recover enything");
        }
        
    }
    
    @FXML private void guardarComo(ActionEvent event){
        //this.Archivos.saveFileAs(this.TextoArea.getText());
        stage = new Stage();
        FXMLLoader myloader = new FXMLLoader(
        getClass().getResource("/FileBrowser/SaveFileBrowserFXML.fxml"));
        try {
            root = (Parent) myloader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLNoteBlockController.class.getName()).log(Level.SEVERE,null,ex);
        }
        SaveFileBrowserFXMLController myController = myloader.getController();
        Scene fileBrowser = new Scene(root);
        myController.setScene(fileBrowser);
        myController.setStage(stage);
        myController.setTextToSave(this.TextoArea.getText() + "#separatorKR17@" + (String) fontChose.getValue() + "," + (int)fontSizeChose.getValue()+",");
        stage.setScene(fileBrowser);
        stage.setTitle("Save As");
        stage.show();
    }
    
    @FXML private void guardar(ActionEvent event){
        //this.Archivos.saveFile(this.TextoArea.getText());
        SaveFileBrowserFXMLController saveFile = new SaveFileBrowserFXMLController();
        saveFile.setFile(this.recoveredFile);
        saveFile.setTextToSave(this.TextoArea.getText()  + "#$%separatorKR17&@!" + (String) fontChose.getValue() + "," + (int)fontSizeChose.getValue()+",");
        saveFile.justSaveIt();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setDireccion(String path) {
        this.Archivos = new GestorArchivos(path);
    }
    
    
}
