/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualfinalproject;

import NoteBlock.*;
import Paint.*;
import java.io.File;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karlos
 */
public class UserUIFXMLController implements Initializable {
    
    
    @FXML private TextField nameField, apellidoField, direcField,phoneField, curpField, mailField;
    @FXML private TreeView<File> treeView;
    @FXML private Button btnPaint, btnBlockN;
    
    private Node pngFile;
    private Conexion con = null;
    private ResultSet rs = null;
    private Scene scene;
    private Stage stage;
    private Parent root;
    private LogginController loggin;
    public GestorArchivos Archivos;
    private long phoneN;
    private String primaryPath;
    private ImageView paint, block;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loggin = new LogginController();
        
        //folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon16.png")));
        //txtFile = new ImageView(new Image(getClass().getResourceAsStream("/img/textIcon16.png")));
        pngFile = new ImageView(new Image(getClass().getResourceAsStream("/img/pngIcon16.png")));
        
        //treeViewRefresher();
        
        paint = new ImageView(new Image(getClass().getResourceAsStream("/img/paint.png")));
        paint.setFitHeight(30);
        paint.setFitWidth(30);
        btnPaint.setGraphic(paint); 
        
        block = new ImageView(new Image(getClass().getResourceAsStream("/img/nota.png")));
        block.setFitHeight(30);
        block.setFitWidth(30);
        btnBlockN.setGraphic(block);
        
    }
    
    
    private void findFiles(File dir, TreeItem<File> parent){
        Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon16.png")));
        TreeItem<File> root = new TreeItem<>(dir,folder);
        root.setExpanded(true);
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()){
                    System.out.println("Directory: " + file.getCanonicalPath());
                    findFiles(file,root);
                }else{
                    Node txtFile = new ImageView(new Image(getClass().getResourceAsStream("/img/textIcon16.png")));
                    System.out.println("-> File: " + file.getCanonicalPath());
                    root.getChildren().add(new TreeItem<>(file,txtFile));
                }
            }
            if (parent == null){
                System.out.println("treeView:root -> root");
                treeView.setRoot(root);
            }else {
                System.out.println("parent:children -> root");
                parent.getChildren().add(root);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML private void treeViewRefresher(){
        File currentDirectoty = new File(this.primaryPath);
        findFiles(currentDirectoty,null);
    }
    
    @FXML private void deleteFile(){
        TreeItem<File> selectedItem = (TreeItem<File>) treeView.getSelectionModel().selectedItemProperty().getValue();
        try {
            selectedItem.getValue().delete();
        }catch (SecurityException e){
            e.printStackTrace();
        }
        treeViewRefresher();
    }
    
    @FXML private void Perfil(){
        
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
        //loggin= new LogginController();
        
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
        
        //loggin= new LogginController();
        
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
        
        //loggin = new LogginController();
        
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
        
        //loggin = new LogginController();
        
        myController.setDireccion(".//"+loggin.getUser()+"//");
        
        stage.setScene(blockDeNotas);
        stage.setTitle("Block de notas");

        stage.show();
        
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    
    public void setDireccion(String path) {
        this.primaryPath = path;
        this.Archivos = new GestorArchivos(path);
        Archivos.checkDirectory();
        if (Archivos.checkDirectory()) {
            treeViewRefresher();
        }
    }
    
    
     private void newWindow(String xmlfile){
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(xmlfile));
        } catch (IOException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    private void creditsWindow(ActionEvent e){
        
       newWindow("creditsFXML.fxml");
    }
    
}
