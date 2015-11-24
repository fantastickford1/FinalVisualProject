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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

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
    @FXML private TreeView<String> userTree,adminTree;
    @FXML private Button btnAgregar, btnBorrar, btnConsultar;
    
    private ImageView agregar; 
    private ImageView borrar;
    private ImageView consultar;        
    private Conexion con = null;
    private ResultSet rs = null;
    private Stage stage;
    private Parent root;
    
    String type;
    
    @FXML private void close()
    {
        Stage stage = new Stage();
        FXMLLoader myLoader = new FXMLLoader(
        getClass().getResource("/visualfinalproject/LogginFXML.fxml"));
        try {
            root = (Parent) myLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        LogginController myController = myLoader.getController();
        Scene loggin = new Scene(root);
        myController.setStage(stage);
        myController.setScene(loggin);
        stage.setScene(loggin);
        stage.setTitle("Loggin");
        stage.show();
        this.stage.close();
        
    }
    
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
        
        TreeItem<String> root3 = new TreeItem<>("Admins");
        TreeItem<String> root2 = new TreeItem<>("Users");
        root3.setExpanded(true);
        root2.setExpanded(true);
        
        try {
            while (rs.next()) {
                root3.getChildren().addAll(new TreeItem<String>(rs.getString("user")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rs = con.buscar("SELECT * FROM usuarios WHERE type = 'normal'");
        
        try {
            while (rs.next()) {
                root2.getChildren().addAll(new TreeItem<String>(rs.getString("user")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        adminTree.setRoot(root3);
        userTree.setRoot(root2);
        
    }
    
    
    private void newWindow(String xmlfile){
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(xmlfile));
        } catch (IOException ex) {
            Logger.getLogger(AdminFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    private void creditsWindow(ActionEvent e){
       newWindow("creditsFXML.fxml");
    }
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        agregar = new ImageView(new Image(getClass().getResourceAsStream("/img/agregar.png")));
        agregar.setFitHeight(30);
        agregar.setFitWidth(30);
        btnAgregar.setGraphic(agregar);
    
        borrar = new ImageView(new Image(getClass().getResourceAsStream("/img/borrar.png")));
        borrar.setFitHeight(30);
        borrar.setFitWidth(30);
        btnBorrar.setGraphic(borrar);
    
        consultar = new ImageView(new Image(getClass().getResourceAsStream("/img/buscar.png")));
        consultar.setFitHeight(30);
        consultar.setFitWidth(30);
        btnConsultar.setGraphic(consultar);
    } 
   
    
    void setScene(Scene scene) {
        this.scene = scene;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
