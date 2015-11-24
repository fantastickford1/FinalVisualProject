/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualfinalproject;

import FileBrowser.FileBrowserFXMLController;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    @FXML private TreeView<String> treeView;
    @FXML private Button btnPaint, btnBlockN;
    
    private Conexion con = null;
    private ResultSet rs = null;
    private Scene scene;
    private ImageView paint, block;
    private Stage stage;
    private Stage userUI;
    private Parent root;
    private File parentDirectory;
    private TreeItem<String> selectedItem;
    private TreeItem<String> parentFolder;
    private LogginController loggin;
    private long phoneN;
    private String primaryPath;
    private String userName;
    public GestorArchivos Archivos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loggin = new LogginController();
        this.userName = loggin.getUser();
        Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon16.png")));
        parentDirectory = new File(".//"+ userName +"//");
        parentFolder = new TreeItem<>(userName,folder);
        addTreeItem(parentDirectory,parentFolder,userName);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedItem = (TreeItem<String>) newValue;
                updateTreeView(selectedItem);
            }
        });
        //////////////////////////////////////////////////////////
        paint = new ImageView(new Image(getClass().getResourceAsStream("/img/paint.png")));
        paint.setFitHeight(30);
        paint.setFitWidth(30);
        btnPaint.setGraphic(paint); 
        
        block = new ImageView(new Image(getClass().getResourceAsStream("/img/nota.png")));
        block.setFitHeight(30);
        block.setFitWidth(30);
        btnBlockN.setGraphic(block);
        /////////////////////////////////////////////////////////
    }
    
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
    
    @FXML private void Perfil()
    {      
        try {
            con= new Conexion("root", "root","sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
        System.out.println(this.userName);
        rs = con.buscar("SELECT * FROM datos WHERE user = '"+userName+"'");
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
    
    @FXML private void Update()
    {  
        try {
            con = new Conexion("root", "root", "sistema");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        phoneN = Long.parseLong(phoneField.getText());

        boolean update = con.actualizar("UPDATE datos SET nombre = '"+nameField.getText()+"', apellido = '"+apellidoField.getText()+"', telefono = "+phoneN+", direccion = '"+direcField.getText()+"', email = '"+mailField.getText()+"', curp = '"+curpField.getText()+"' WHERE user = '"+this.userName+"';");
        System.out.println(update);
        if (update) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succed");
            alert.setHeaderText("You successfully added a user ");
            alert.setContentText(null);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Something went wrong");
            alert.setHeaderText("Looks like you are having trouble");
            alert.setContentText("Some uruk hai are messing around");
        }
    }
    
    @FXML private void openPaint()
    {
        Stage stage = new Stage();
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
                
        myController.setDirection(".//"+userName+"//");
        
        stage.setScene(paint);
        stage.setTitle("Paint");

        stage.show(); 
    }
    
    @FXML private void openBlock()
    {
        Stage stage = new Stage();
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
                
        myController.setDireccion(".//"+userName+"//");
        
        stage.setScene(blockDeNotas);
        stage.setTitle("Block de notas");

        stage.show();
    }
    
    @FXML private void fileBrowserWindow()
    {
        Stage stage = new Stage();
        FXMLLoader myloader = new FXMLLoader(
        getClass().getResource("/FileBrowser/FileBrowserFXML.fxml"));
        try {
            root = (Parent) myloader.load();
        } catch (IOException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE,null,ex);
        }
        FileBrowserFXMLController myController = myloader.getController();
        Scene fileBrowser = new Scene(root);
        myController.setScene(fileBrowser);
        stage.setScene(fileBrowser);
        stage.setTitle("File Browser");
        stage.show();
    }
    
    @FXML private void creditsWindow(ActionEvent e)
    {
       newWindow("creditsFXML.fxml");
    }
    
    public void setScene(Scene scene)
    {
        this.scene = scene;
    }
    
    public void setDireccion(String path) 
    {
        this.primaryPath = path;
        this.Archivos = new GestorArchivos(path);
        Archivos.checkDirectory();
    }
    
    private void newWindow(String xmlfile)
    {
        Stage stage = new Stage();
        root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(xmlfile));
        } catch (IOException ex) {
            Logger.getLogger(UserUIFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void addTreeItem(File parentFile, TreeItem<String> parentTreeItem,String parentName)
    {
        TreeItem<String> root = parentTreeItem;
        String[] filesName = parentFile.list();
        File[] files = parentFile.listFiles();
        int count = 0;
        for (File file : files) {
            if (file.isDirectory()){
                Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon16.png")));
                String folderName = filesName[count];
                TreeItem<String> directory = new TreeItem<>(folderName,folder);
                root.getChildren().add(directory);
                addTreeItem(file,directory,folderName);
                count++;
            }else{
                Node txtFile = new ImageView(new Image(getClass().getResourceAsStream("/img/textIcon16.png")));
                Node pngFile = new ImageView(new Image(getClass().getResourceAsStream("/img/pngIcon16.png")));
                String fileName = filesName[count];
                TreeItem<String> fileRoot;
                if (fileName.matches(".*\\b.txt\\b")){
                    fileRoot = new TreeItem<>(fileName,txtFile);
                    root.getChildren().add(fileRoot);
                }else if (fileName.matches(".*\\b.png\\b")) {
                    fileRoot = new TreeItem<>(fileName, pngFile);
                    root.getChildren().add(fileRoot);
                }
                count++;
            }
        }
        treeView.setRoot(root);
    }
    
    private void updateTreeView(TreeItem<String> selectedItem)
    {
        TreeItem<String> tempSelected = selectedItem;
        String path = "";
        while(!tempSelected.getValue().equals(userName)){
            tempSelected = tempSelected.getParent();
            path = tempSelected.getValue() + "/" + path;
        }
        path = ".//" + path + selectedItem.getValue() + "//";
        File parentFile = new File(path);
        if (parentFile.exists() && parentFile.isDirectory()){
            String[] allFiles = parentFile.list();
            selectedItem.getChildren().clear();
            for (String fileN : allFiles) {
                Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon16.png")));
                Node txtFile = new ImageView(new Image(getClass().getResourceAsStream("/img/textIcon16.png")));
                Node pngFile = new ImageView(new Image(getClass().getResourceAsStream("/img/pngIcon16.png")));
                TreeItem<String> files;
                if (fileN.matches(".*\\b.txt\\b")) {
                    files = new TreeItem<>(fileN,txtFile);
                    selectedItem.getChildren().add(files);
                }else if (fileN.matches(".*\\b.png\\b")){
                    files = new TreeItem<>(fileN,pngFile);
                    selectedItem.getChildren().add(files);
                }else{
                    files = new TreeItem<>(fileN,folder);
                    selectedItem.getChildren().add(files);
                }
            }
        }
        if(selectedItem.getValue().equals(userName)){
            selectedItem.getChildren().clear();
            treeView.setRoot(null);
            addTreeItem(parentDirectory,parentFolder,userName);
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
       
}
