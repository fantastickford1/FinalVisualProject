/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualfinalproject;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author Karlos
 */
public class AdminFXMLController implements Initializable {

    private Scene scene;
    
    @FXML private TextField txtUserA, txtPassA, txtUserB;
    @FXML private MenuButton mbOpcionA, mbOpcionB;
    @FXML private MenuItem miUserA, miAdminA;
    @FXML private Text txtUserList,txtAdminList;
    @FXML private TextFlow txtFlow;
    
    private Conexion con = null;
    private ResultSet rs = null;
    
    String type;
    
    
    @FXML private void changeNameU(){
        mbOpcionA.setText("Usuario");
    }
    
    @FXML private void changeNameA(){
        mbOpcionA.setText("Administrador");
    }
    
    @FXML private void Agregar(){
    
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        if (mbOpcionA.getText() == "Usuario") {
            this.type= "normal";
        }else if (mbOpcionA.getText()== "Administrador" ){
            this.type= "admin";
        }
    
        boolean insert = con.insertar("INSERT INTO usuarios (user, password, type) VALUES ('"+txtUserA.getText()+"', '"+txtPassA.getText()+"', '"+type+"');");
        if (type == "normal") {
            boolean insertinfo = con.insertar("INSERT INTO datos (user,nombre,apellido,telefono,direccion,email,curp) VALUES ('"+txtUserA.getText()+"','','',0,'','','');");
            System.out.println(insertinfo);
        }
        
        System.out.println(insert);
        if (insert) {
            Alert delet = new Alert(Alert.AlertType.CONFIRMATION);
            delet.setTitle("Succed");
            delet.setContentText("The user is added");
            delet.showAndWait();
            txtUserB.setText("");
        }else{
            Alert nodelet = new Alert(Alert.AlertType.WARNING);
            nodelet.setTitle("Warning");
            nodelet.setHeaderText("Can't connect to the server");
            nodelet.setContentText("Failed to reach the server");
            txtUserB.setText("");
        }
    }
    
    @FXML private void changeNameUB(){
        mbOpcionB.setText("Usuario");
    }
    
    @FXML private void changeNameAB(){
        mbOpcionB.setText("Administrador");
    }
    
    @FXML private void Borrar(){
    
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        if (mbOpcionB.getText() == "Usuario") {
            this.type= "normal";
        }else if (mbOpcionB.getText()== "Administrador" ){
            this.type= "admin";
        }
    
        boolean delete = con.borrar("DELETE FROM usuarios WHERE user='"+txtUserB.getText()+"' AND type='"+type+"';");
        
        System.out.println(delete);
    }
    
    @FXML private void Consultar(){ //make it work
        
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        rs = con.buscar("SELECT * FROM usuarios WHERE type = 'admin'");
        
        try {
            while (rs.next()) {                
                txtAdminList.setText(" " + rs.getString("user") + "\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    void setScene(Scene scene) {
        this.scene = scene;
    }
    
}
