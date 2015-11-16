package NoteBlock;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author none
 */

public class FXMLNoteBlockController implements Initializable {
    
    //Declaraciones de Componentes
    @FXML private Label label;
    @FXML TextArea TextoArea;
    //Variables
    private Scene scene;
    
    //Instancias Utiles
    GestorArchivos Archivos;
    Controla ct = new Controla();
    private Version v = new Version();  //Gestion de entrada de datos, texto
    
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
        // TODO
    }    
    
    @FXML private void abrir(ActionEvent event){
        this.TextoArea.setText(this.Archivos.openFile());
    }
    
    @FXML private void guardarComo(ActionEvent event){
        this.Archivos.saveFileAs(this.TextoArea.getText());
    }
    
    @FXML private void guardar(ActionEvent event){
        this.Archivos.saveFile(this.TextoArea.getText());
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setDireccion(String path) {
        this.Archivos = new GestorArchivos(path);
    }
    
    
}
