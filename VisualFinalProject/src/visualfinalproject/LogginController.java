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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

/**
 *
 * @author Karlos
 */
public class LogginController implements Initializable {
    
    @FXML TextField userField;
    @FXML PasswordField passField;
    @FXML RadioButton rbAdmin, rbUser;
    @FXML Button btnlogin;
    @FXML AnchorPane anPane;
    
    private ImageView login;
    private Conexion con = null;
    private ResultSet rs= null;
    private Stage loggin;
    private Scene scene;
    private Stage stage;
    private Parent root;
    
    public static String user;
    private String password;
    
    public void setStage(Stage stage){
        this.loggin = stage;
    }
    
    @FXML private void logginAction(ActionEvent e){
        this.user= userField.getText();
        this.password= passField.getText();
        
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        rs= con.buscar("SELECT * FROM usuarios WHERE user= '"+user+"' AND password= '"+password+"'");
        
        if (rs != null) {
            try {
                if (rs.getString("type").equals("admin")) {
                    AdminWindow();
                }else if(rs.getString("type").equals("normal")){
                    UserWindow();
                }
            } catch (SQLException ex) {
                Logger.getLogger(LogginController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        //Parent root = null;
        try {
            root = (Parent) myLoader.load();
            System.out.println("Here");
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
        
        //Parent root = null;
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
        login = new ImageView(new Image(getClass().getResourceAsStream("/img/login.png")));
        login.setFitHeight(40);
        login.setFitWidth(40);
        btnlogin.setGraphic(login);
        
        anPane.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/img/hipster.jpg")),null,null,null,null)));
        
    }    
    
}
