/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualfinalproject;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Karlos
 */
public class LogginController implements Initializable {
    
    @FXML TextField userField;
    @FXML PasswordField passField;
    @FXML RadioButton rbAdmin, rbUser;
    
    private Conexion con = null;
    private ResultSet rs= null;
    private Stage loggin;
    private Scene scene;
    private Stage stage;
    
    public static String user;
    private String password,type;
    
    public void setStage(Stage stage){
        this.loggin = stage;
    }
    
    @FXML private void logginAction(ActionEvent e){
        this.user= userField.getText();
        this.password= passField.getText();
        if (rbAdmin.isSelected()) {
            this.type = "admin";
        }
        if (rbUser.isSelected()) {
            this.type = "normal";
        }
        
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        rs= con.buscar("SELECT * FROM usuarios WHERE user= '"+user+"' AND password= '"+password+"' AND type= '"+type+"' ");
        
        if (rs != null) {
            if (rbAdmin.isSelected()) {
                AdminWindow();
            }
            if (rbUser.isSelected()) {
                UserWindow();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loggin error");
            alert.setHeaderText("User or Password is incorect");
            alert.setContentText("Change the password or user");
            alert.showAndWait();
        }
    }
    
    
    @FXML
    private void AdminWindow(){
        stage = new Stage();
        //if condition
        FXMLLoader myLoader = new FXMLLoader(
        getClass().getResource("/visualfinalproject/adminFXML.fxml"));
        
        Parent root = null;
        try {
            root = (Parent) myLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(LogginController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
     @FXML
    private void UserWindow(){
        stage = new Stage();
        //if condition
        FXMLLoader myLoader = new FXMLLoader(
        getClass().getResource("/visualfinalproject/userUIFXML.fxml"));
        
        Parent root = null;
        try {
            root = (Parent) myLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(LogginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        UserUIFXMLController myUserUIController = myLoader.getController();
        
        Scene userScene = new Scene(root);
        myUserUIController.setScene(userScene);
        //Later for log 
        myUserUIController.setDireccion(".//" + userField.getText() + "//");
        /////////////
        stage.setScene(userScene);
        stage.show();
        this.loggin.close();

    }
    
    public String getUser(){
        return this.user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
