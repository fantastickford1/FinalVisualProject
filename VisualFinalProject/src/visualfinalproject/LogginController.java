/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualfinalproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Karlos
 */
public class LogginController implements Initializable {
    
    @FXML TextField userField;
    @FXML PasswordField passField;
    
    private Stage loggin;
    private Scene scene;
    private Stage stage;
    
    public void setStage(Stage stage){
        this.loggin = stage;
    }
    
    @FXML
    private void loginAction(ActionEvent event) throws Exception {
        stage = new Stage();
        //if condition
        FXMLLoader myLoader = new FXMLLoader(
        getClass().getResource("/visualfinalproject/adminFXML.fxml"));
        
        Parent root = (Parent) myLoader.load();
        AdminFXMLController myController = myLoader.getController();
        
        Scene userOrAdmin = new Scene(root);
        myController.setScene(userOrAdmin);
        //Later for log 
        //myController.setDireccion(".//" + userField.getText() + "//");
        /////////////
        stage.setScene(userOrAdmin);
        stage.show();
        this.loggin.close();

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
