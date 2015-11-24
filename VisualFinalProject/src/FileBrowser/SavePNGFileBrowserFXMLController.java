/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileBrowser;

import Paint.FXMLminipaintController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import visualfinalproject.LogginController;

/**
 * FXML Controller class
 *
 * @author Karlos
 */
public class SavePNGFileBrowserFXMLController implements Initializable {

    @FXML private TreeView<String> treeView;
    @FXML private TilePane tilePane;
    @FXML private ChoiceBox formatChoice;
    @FXML private TextField fileNameField;
    private Scene scene;
    private TreeItem<String> selectedItem;
    private TreeItem<String> parentFolder;
    private String parentDirectoryName;
    private File parentDirectory;
    private static File newfile;
    private static Canvas canvas;
    private LogginController logginAuxiliar;
    private Stage stage;
    
    @FXML
    private void close(){
        this.stage.close();
    }
    
    @FXML
    private void saveImage(){
        String fileN = fileNameField.getText();
        String fileName = "";
        try {
            fileName = fileN + formatChoice.getValue().toString();
        } catch (Exception e) {
            fileName = fileN + ".png";
        }
        try {
            if (selectedItem.getValue() != null) {
                String temp = "";
                TreeItem<String> root = selectedItem;
                while(!root.getValue().equals(parentDirectoryName)){
                    root = root.getParent();
                    temp = root.getValue() +"/"+ temp;
                }
                String principalPath = ".//" + temp + selectedItem.getValue() + "//";
                File tempFile = new File(principalPath);
                String path = "";
                if (tempFile.isDirectory()) {
                    path = ".//" + temp + selectedItem.getValue() + "/" + fileName + "//";    
                }else{
                    path = ".//" + temp + fileName + "//";
                }
                newfile = new File(path);
                int w = (int) canvas.getWidth();
                int h = (int) canvas.getHeight();
                WritableImage image = new WritableImage(w, h);
                canvas.snapshot(null, image);
                try {
                    boolean succed = ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", newfile);
                    System.out.println(succed);
                } catch (IOException ex) {
                    Logger.getLogger(SavePNGFileBrowserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }             
            }
        }catch (RuntimeException e){
            String newDirectoryPath = ".//"+parentDirectoryName+"/"+fileName+"//";
            newfile = new File(newDirectoryPath);
            int w = (int) canvas.getWidth();
            int h = (int) canvas.getHeight();
            WritableImage image = new WritableImage(w, h);
            canvas.snapshot(null, image);
            try {
                boolean succed = ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", newfile);
                System.out.println(succed);
            } catch (IOException ex) {
                Logger.getLogger(SavePNGFileBrowserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }    
        }
        this.stage.close();
    }
    
    @FXML
    private void deleteFile()
    {
        TreeItem<String> selected = selectedItem;
        String temp = "";
        while(!selected.getValue().equals(parentDirectoryName)){
            selected = selected.getParent();
            temp = selected.getValue() + "/" + temp;
        }
        temp = ".//" + temp + selectedItem.getValue() + "//";
        File file = new File(temp);
        boolean succed = file.delete();
        System.out.println(succed);
    }
    
    @FXML
    private void newDirectory()
    {
        TextInputDialog  newDialog = new TextInputDialog();
        newDialog.setTitle("New Directory");
        newDialog.setContentText("Enter new directory name:");
        newDialog.setGraphic(null);
        newDialog.setHeaderText(null);
        Optional<String> result = newDialog.showAndWait();
        if (result.isPresent()){
            String directoryName = result.get();
            try {
                if (selectedItem.getValue() != null) {
                    String temp = "";
                    TreeItem<String> root = selectedItem;
                    while(!root.getValue().equals(parentDirectoryName)){
                        root = root.getParent();
                        temp = root.getValue() +"/"+ temp;
                    }
                    String path = ".//" + temp + selectedItem.getValue() + "/" + directoryName + "//";
                    File file = new File(path);
                    boolean succed = file.mkdir();
                }
            }catch (RuntimeException e){
                String newDirectoryPath = ".//"+parentDirectoryName+"/"+directoryName+"//";
                File newDirectory = new File(newDirectoryPath);
                newDirectory.mkdir();
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatChoice.getItems().add(".png");
        tilePane.setPadding(new Insets(10,10,10,10));
        tilePane.setVgap(10);
        tilePane.setHgap(10);
        logginAuxiliar = new LogginController();
        this.parentDirectoryName = logginAuxiliar.getUser();
        Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon16.png")));
        parentDirectory = new File(".//"+ parentDirectoryName +"//");
        parentFolder = new TreeItem<>(parentDirectoryName,folder);
        addTreeItem(parentDirectory,parentFolder,parentDirectoryName);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedItem = (TreeItem<String>) newValue;
                updateTreeView(selectedItem);
                setTilePane(selectedItem.getChildren(),selectedItem.getValue());
            }
        });
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
        while(!tempSelected.getValue().equals(parentDirectoryName)){
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
        if(selectedItem.getValue().equals(parentDirectoryName)){
            selectedItem.getChildren().clear();
            treeView.setRoot(null);
            addTreeItem(parentDirectory,parentFolder,parentDirectoryName);
        }
    }
    
    private void setTilePane(ObservableList<TreeItem<String>> children,String path)
    {
        tilePane.getChildren().clear();
        tilePane.setPrefColumns(4);
        tilePane.setPrefRows(4);
        System.out.println(path);
        System.out.println(children.size());
        if (children.size() > 0) {
            for (int count = 0; count < children.size(); count++) {
                Button button = new Button();
                button.setId(path);
                String test = children.get(count).getValue();
                Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon96.png")));
                Node txtFile = new ImageView(new Image(getClass().getResourceAsStream("/img/textIcon96.png")));
                Node pngFile = new ImageView(new Image(getClass().getResourceAsStream("/img/pngIcon96.png")));

                button.setGraphic(folder);
                if (test.matches(".*\\b.txt\\b")) {
                    button.setGraphic(txtFile);
                }
                if (test.matches(".*\\b.png\\b")) {
                    button.setGraphic(pngFile);
                }
                button.setText(children.get(count).getValue());
                button.setTextAlignment(TextAlignment.CENTER);
                button.setOnAction((e) -> {
                });
                tilePane.setOrientation(Orientation.HORIZONTAL);
                tilePane.getChildren().addAll(button);
            }
        }else{
            Node folder = new ImageView(new Image(getClass().getResourceAsStream("/img/folderIcon96.png")));
            Node txtFile = new ImageView(new Image(getClass().getResourceAsStream("/img/textIcon96.png")));
            Node pngFile = new ImageView(new Image(getClass().getResourceAsStream("/img/pngIcon96.png")));
            Button button = new Button();
            button.setId(path);
            button.setGraphic(folder);
            if (path.matches(".*\\b.txt\\b")){
                button.setGraphic(txtFile);
            }
            if (path.matches(".*\\b.png\\b")){
                button.setGraphic(pngFile);
            }
            button.setText(path);
            button.setTextAlignment(TextAlignment.CENTER);
            button.setOnAction((e) ->{

            });
            tilePane.setOrientation(Orientation.HORIZONTAL);
            tilePane.getChildren().add(button);
        }
    }
    
    public void setScene(Scene scene)
    {
        this.scene = scene;
    }  
    
    public void setImageToSave(GraphicsContext gc){
        canvas = gc.getCanvas();
    }
    
    public void setFile(File file){
        newfile = file;
    }
    
    public void justSaveIt(){
        if (newfile != null) {
            int wd = (int) canvas.getWidth();
            int hg = (int) canvas.getHeight();
            WritableImage image = new WritableImage(wd, hg);
            canvas.snapshot(null, image);
                try {
                    boolean succed = ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", newfile);
                    System.out.println(succed);
                } catch (IOException ex) {
                    Logger.getLogger(SavePNGFileBrowserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }           
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Can't save");
            alert.setHeaderText("Looks like you are trying to save something");
            alert.setContentText("The File doesn't exist right now, try to save it with Save As button ;)");
            alert.showAndWait();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
