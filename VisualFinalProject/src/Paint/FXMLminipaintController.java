/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import FileBrowser.OpenFileBrowserFXMLController;
import FileBrowser.SavePNGFileBrowserFXMLController;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author sbpaco
 */
public class FXMLminipaintController implements Initializable {
    
    //>>>>>>>>>>>>>>>>>>>>>>>FXML Variables<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @FXML
    private RadioButton strokeRB, fillRB, strokeFillRB;
    @FXML
    private ColorPicker colorPick, FillcolorPick;
    @FXML
    private Canvas TheCanvas, canvasGo;
    @FXML
    private Button rectButton, lineButton, ovlButton, pencButton, eraserButton, clearButton;
    @FXML
    private Slider sizeSlider;
    
    //>>>>>>>>>>>>>>>>>>>>>>>Other variables<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private GraphicsContext gcB,gcF;
    private boolean drawline = false,drawoval = false,drawrectangle = false,erase = false,freedesign = true;
    double startX, startY, lastX,lastY,oldX,oldY;
    double hg;
    ManejoImagenes imagenNueva;
    Image currentImage;
    Image originalImage;
    ControlDibujo controlDibujo = new ControlDibujo();
    private Scene scene;
    private Stage stage;
    private Parent root;
    private File recoveredFile = null;
    //////////////////////////////////////////////////////////////////////////////
    
    @FXML
    private void onMousePressedListener(MouseEvent e){
        this.startX = e.getX();
        this.startY = e.getY();
        this.oldX = e.getX();
        this.oldY = e.getY();
    }
    
    @FXML
    private void onMouseDraggedListener(MouseEvent e){
        this.lastX = e.getX();
        this.lastY = e.getY();
        
        if(drawrectangle)
            drawRectEffect();
        if(drawoval)
            drawOvalEffect();
        if(drawline)
            drawLineEffect();
        if(freedesign)
            freeDrawing();
        if(erase)
            eraser();
    }
    
    @FXML
    private void onMouseReleaseListener(MouseEvent e){
        if(drawrectangle)
            drawRect();
        if(drawoval)
            drawOval();
        if(drawline)
            drawLine();
    /////////////////////////////////////////////////
        currentImage = currentStateCanvas();
        this.controlDibujo.setCurrentImage(currentImage);
        System.out.println(currentImage.toString());
        
        if (controlDibujo.getSizeArrayImages() == 0) {
            this.controlDibujo.setCurrentImage(originalImage);
            System.out.println("Guardando canvas vacio:" + originalImage.toString());
        }
    }
    
    @FXML
    private void onMouseExitedListener(MouseEvent event)
    {
        System.out.println("No puedes dibujar fuera del canvas");
    }
    
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>Draw methods<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    
    private void drawOval()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());
        
        if(fillRB.isSelected()){
            gcB.setFill(FillcolorPick.getValue());
            gcB.fillOval(startX, startY, wh, hg);
        }
        if(strokeRB.isSelected()){
            gcB.setStroke(colorPick.getValue());
            gcB.strokeOval(startX, startY, wh, hg);
        }
        if(strokeFillRB.isSelected()){
            gcB.setFill(FillcolorPick.getValue());
            gcB.setStroke(colorPick.getValue());
            gcB.fillOval(startX, startY, wh, hg);
            gcB.strokeOval(startX, startY, wh, hg);
        }
    }
    
    private void drawRect()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());
        
        if(fillRB.isSelected()){
            gcB.setFill(FillcolorPick.getValue());
            gcB.fillRect(startX, startY, wh, hg);
        }
        if(strokeRB.isSelected()){
            gcB.setStroke(colorPick.getValue());
            gcB.strokeRect(startX, startY, wh, hg);
        }
        if(strokeFillRB.isSelected()){
            gcB.setFill(FillcolorPick.getValue());
            gcB.setStroke(colorPick.getValue());
            gcB.fillRect(startX, startY, wh, hg);
            gcB.strokeRect(startX, startY, wh, hg);
        }
    }
 
    private void drawLine()
    {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcB.strokeLine(startX, startY, lastX, lastY);
    }
    
    private void freeDrawing()
    {
        gcB.setLineWidth(sizeSlider.getValue());
        gcF.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcF.setStroke(colorPick.getValue());
        gcF.strokeLine(oldX, oldY, lastX, lastY);
        gcB.strokeLine(oldX, oldY, lastX, lastY);
        oldX = lastX;
        oldY = lastY;
    }
    
    private void eraser()
    {
        gcB.clearRect(lastX, lastY, sizeSlider.getValue(), sizeSlider.getValue());
        gcF.clearRect(lastX, lastY, sizeSlider.getValue(), sizeSlider.getValue());
    }
    
    //////////////////////////////////////////////////////////////////////
    //>>>>>>>>>>>>>>>>>>>>>>>>>>Draw effects methods<<<<<<<<<<<<<<<<<<<<<<<
    
    private void drawOvalEffect()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());
        
        if(fillRB.isSelected()){
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setFill(FillcolorPick.getValue());
            gcF.fillOval(startX, startY, wh, hg);
        }
        if(strokeRB.isSelected()){
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setStroke(colorPick.getValue());
            gcF.strokeOval(startX, startY, wh, hg );
        }
        if(strokeFillRB.isSelected()){
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setFill(FillcolorPick.getValue());
            gcF.setStroke(colorPick.getValue());
            gcF.fillOval(startX, startY, wh, hg);
            gcF.strokeOval(startX, startY, wh, hg );
        }
       }
    
    private void drawRectEffect()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());
        
        if(fillRB.isSelected()){
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setFill(FillcolorPick.getValue());
            gcF.fillRect(startX, startY, wh, hg);
        }
        if(strokeRB.isSelected()){
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setStroke(colorPick.getValue());
            gcF.strokeRect(startX, startY, wh, hg );
        }
        if(strokeFillRB.isSelected()){
            gcF.clearRect(0, 0, canvasGo.getWidth(), canvasGo.getHeight());
            gcF.setFill(FillcolorPick.getValue());
            gcF.setStroke(colorPick.getValue());
            gcF.fillRect(startX, startY, wh, hg);
            gcF.strokeRect(startX, startY, wh, hg );
        }
    }
    
    private void drawLineEffect()
    {
        gcF.setLineWidth(sizeSlider.getValue());
        gcF.setStroke(colorPick.getValue());
        gcF.clearRect(0, 0, canvasGo.getWidth() , canvasGo.getHeight());
        gcF.strokeLine(startX, startY, lastX, lastY);
    }
    ///////////////////////////////////////////////////////////////////////
    
    @FXML 
    private void clearCanvas(ActionEvent e)
    {
        gcB.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0, 0, TheCanvas.getWidth(), TheCanvas.getHeight());
    }
        
    
    //>>>>>>>>>>>>>>>>>>>>>Buttons control<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @FXML
    private void setOvalAsCurrentShape(ActionEvent e)
    {
        drawline = false;
        drawoval = true;
        drawrectangle = false;
        freedesign = false;
        erase = false;
    }
    
     @FXML
    private void setLineAsCurrentShape(ActionEvent e)
    {
        drawline = true;
        drawoval = false;
        drawrectangle = false;
        freedesign = false;
        erase = false;
    }
     @FXML
    private void setRectangleAsCurrentShape(ActionEvent e)
    {
        drawline = false;
        drawoval = false;
        freedesign = false;
        erase=false;
        drawrectangle = true;
    }
    
    @FXML
    private void setErase(ActionEvent e)
    {
        drawline = false;
        drawoval = false;
        drawrectangle = false;    
        erase = true;
        freedesign= false;
    }
    
    @FXML
    private void setFreeDesign(ActionEvent e)
    {
        drawline = false;
        drawoval = false;
        drawrectangle = false;    
        erase = false;
        freedesign = true;
    }
    
    //////////////////////////////////////////////////////////////////
    //>>>>>>>>>>>>>>>>>>>>Save and open images<<<<<<<<<<<<<<<<<<<<<<<
    
    @FXML
    private void openImage(ActionEvent event){
        stage = new Stage();
        FXMLLoader myloader = new FXMLLoader(
        getClass().getResource("/FileBrowser/OpenFileBrowserFXML.fxml"));
        try {
            root = (Parent) myloader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLminipaintController.class.getName()).log(Level.SEVERE,null,ex);
        }
        OpenFileBrowserFXMLController myController = myloader.getController();
        Scene fileBrowser = new Scene(root);
        myController.setStage(stage);
        myController.setScene(fileBrowser);
        stage.setScene(fileBrowser);
        stage.setTitle("Open File");
        stage.showAndWait();
        Image imagenAbrir;
        imagenAbrir =  myController.getReadedImage();
        gcB.clearRect(0,0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0,0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcB.drawImage(imagenAbrir, 0, 0);
        try {
            this.recoveredFile =  new File (myController.getFilePath());
        } catch (Exception e) {
            System.err.println("Nothing recovered");
        }
        
        
    }
    
    @FXML
    public void saveAsNewImage(){
        //this.imagenNueva.saveAsImage(this.TheCanvas.getGraphicsContext2D());
        stage = new Stage();
        FXMLLoader myloader = new FXMLLoader(
        getClass().getResource("/FileBrowser/SavePNGFileBrowserFXML.fxml"));
        try {
            root = (Parent) myloader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLminipaintController.class.getName()).log(Level.SEVERE,null,ex);
        }
        SavePNGFileBrowserFXMLController myController = myloader.getController();
        Scene fileBrowser = new Scene(root);
        myController.setStage(stage);
        myController.setScene(fileBrowser);
        myController.setImageToSave(this.TheCanvas.getGraphicsContext2D());
        stage.setScene(fileBrowser);
        stage.setTitle("Save As");
        stage.show();
    }
    
    @FXML
    private void save(ActionEvent event){
        //this.imagenNueva.saveImage(this.TheCanvas.getGraphicsContext2D());
        SavePNGFileBrowserFXMLController savePNG = new SavePNGFileBrowserFXMLController();
        savePNG.setFile(this.recoveredFile);
        savePNG.setImageToSave(this.TheCanvas.getGraphicsContext2D());
        savePNG.justSaveIt();
    }
    
    public void setDirection(String path){
        this.imagenNueva = new ManejoImagenes(path);
    }
    
    //////////////////////////////////////////////////////////////////
    //>>>>>>>>>>>>>>>>>>>>>>>>Undo and Redo<<<<<<<<<<<<<<<<<<<<<<<<<<
    
    private Image currentStateCanvas(){
        Image currentState;
        WritableImage img = new WritableImage((int)TheCanvas.getWidth() , (int) TheCanvas.getHeight());
        currentState = TheCanvas.snapshot(null, img);
        return currentState;
    }
    
    @FXML
    private void undoCanvas(){
        Image img;
        img = this.controlDibujo.getUndo();
        
        gcB.clearRect(0,0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0,0, TheCanvas.getWidth(), TheCanvas.getHeight());
        
        gcB.drawImage(img,0,0);
    }
    
    @FXML
    private void redoCanvas(){
        Image img;
        img = this.controlDibujo.getRedo();
        
        gcB.clearRect(0,0, TheCanvas.getWidth(), TheCanvas.getHeight());
        gcF.clearRect(0,0, TheCanvas.getWidth(), TheCanvas.getHeight());
        
        gcB.drawImage(img,0,0);
    }

    //////////////////////////////////////////////////////////////////
    
    @FXML
    private void creditosAlumno(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Universidad Politécnica de Chiapas");
        alert.setHeaderText("Creditos");
        alert.setContentText("Materia: Programación Visual\nAlumno: Alumno UPChiapas\nMatrícula: XXXXXX");
        alert.showAndWait();
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    
    //////////////////////////////////////////////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gcB = TheCanvas.getGraphicsContext2D();
        gcF = canvasGo.getGraphicsContext2D();
        
        sizeSlider.setMin(1);
        sizeSlider.setMax(50);
        
        //////////////////////////////////
        Image imageRect = new Image(getClass().getResourceAsStream("Stop-32.png"));
        ImageView icR = new ImageView(imageRect);
        icR.setFitWidth(20);
        icR.setFitHeight(20);
        rectButton.setGraphic(icR);  
        
        Image imageLinea = new Image(getClass().getResourceAsStream("Ruler-32.png"));
        ImageView icLin = new ImageView(imageLinea);
        icLin.setFitWidth(20);
        icLin.setFitHeight(20);
        lineButton.setGraphic(icLin);
        
        Image imageOvalo = new Image(getClass().getResourceAsStream("Chart-32.png"));
        ImageView icOval = new ImageView(imageOvalo);
        icOval.setFitWidth(20);
        icOval.setFitHeight(20);
        ovlButton.setGraphic(icOval);
        
        Image imageLapiz = new Image(getClass().getResourceAsStream("Pencil-32.png"));
        ImageView icLapiz = new ImageView(imageLapiz);
        icLapiz.setFitWidth(20);
        icLapiz.setFitHeight(20);
        pencButton.setGraphic(icLapiz);
        
        Image imageBorrador = new Image(getClass().getResourceAsStream("Eraser-32.png"));
        ImageView icBorr = new ImageView(imageBorrador);
        icBorr.setFitHeight(20);
        icBorr.setFitWidth(20);
        eraserButton.setGraphic(icBorr);
        
        Image imageLimpiar = new Image(getClass().getResourceAsStream("Clear-32.png"));
        ImageView icLim = new ImageView(imageLimpiar);
        icLim.setFitHeight(20);
        icLim.setFitWidth(20);
        clearButton.setGraphic(icLim);
        
        if(controlDibujo.getSizeArrayImages() == 0){
            originalImage = currentStateCanvas();
            this.controlDibujo.setCurrentImage(originalImage);
            System.out.println("Guardando canvas vacio: " + originalImage.toString());
        }
    
    }    
    
}
