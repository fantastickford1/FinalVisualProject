/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualfinalproject;

import NoteBlock.*;
import Paint.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karlos
 */
public class UserUIFXMLController implements Initializable {
    
    
    @FXML private TextField nameField, apellidoField, direcField,phoneField, curpField, mailField;
    @FXML private TreeView treeView;
    
    private Conexion con = null;
    private ResultSet rs = null;
    private Scene scene;
    private Stage stage;
    private Parent root;
    private LogginController loggin;
    public GestorArchivos Archivos;
    private long phoneN;

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
    
    @FXML private void Perfil(){
        
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        loggin= new LogginController();
        
        System.out.println(loggin.getUser());
        
        rs = con.buscar("SELECT * FROM datos WHERE user = '"+loggin.getUser()+"'");
        
       
        try {
            nameField.setText(rs.getString("nombre"));
            apellidoField.setText(rs.getString("apellido"));
            phoneField.setText(rs.getString("telefono"));
            direcField.setText(rs.getString("direccion"));
            mailField.setText(rs.getString("email"));
            curpField.setText(rs.getString("curp"));
        } catch (SQLException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML private void Update(){
        
        try {
            con = new Conexion("root", "root", "sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loggin= new LogginController();
        
        phoneN = Long.parseLong(phoneField.getText());

        
        boolean update = con.actualizar("UPDATE datos SET nombre = '"+nameField.getText()+"', apellido = '"+apellidoField.getText()+"', telefono = "+phoneN+", direccion = '"+direcField.getText()+"', email = '"+mailField.getText()+"', curp = '"+curpField.getText()+"' WHERE user = '"+loggin.getUser()+"';");
        
    }
    
    @FXML private void openPaint(){
        stage = new Stage();
        FXMLLoader myLoader = new FXMLLoader(
        getClass().getResource("/Paint/FXMLminipaint.fxml"));
        
        try {
            root = (Parent) myLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FXMLminipaintController myController = myLoader.getController();
        
        Scene paint = new Scene(root);
        myController.setScene(paint);
        
        loggin = new LogginController();
        
        myController.setDirection(".//"+loggin.getUser()+"//");
        
        stage.setScene(paint);
        stage.setTitle("Paint");

        stage.show();
        
        
        
    }
    
    @FXML private void openBlock(){
        stage = new Stage();
        FXMLLoader myLoader = new FXMLLoader(
        getClass().getResource("/NoteBlock/FXMLDocument.fxml"));
        
        try {
            root = (Parent) myLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FXMLNoteBlockController myController = myLoader.getController();
        
        Scene blockDeNotas = new Scene(root);
        myController.setScene(blockDeNotas);
        
        loggin = new LogginController();
        
        myController.setDireccion(".//"+loggin.getUser()+"//");
        
        stage.setScene(blockDeNotas);
        stage.setTitle("Block de notas");

        stage.show();
        
    }
    
    public void setDireccion(String path) {
        this.Archivos = new GestorArchivos(path);
        Archivos.checkDirectory();
    }
    
}
