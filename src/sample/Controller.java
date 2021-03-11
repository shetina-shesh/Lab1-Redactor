package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Controller {

    @FXML
    public Canvas ImageCanvas;
    public StackPane Spane;
    public GraphicsContext gc;
    public TextArea TextAreaSend;
    public Image image;
    public ImageView Iview;
    public int count = 1;

    @FXML
    public void initialize() {
        Spane.setStyle("-fx-background-color:#999999;");
        gc = ImageCanvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        ImageCanvas.setOnMouseDragged(event -> gc.fillRect(event.getX(), event.getY(), 5, 5));
    }

    @FXML
    public Image OpenFile(ActionEvent actionEvent) {
        Spane.getChildren().clear();
        gc.clearRect(0, 0, ImageCanvas.getWidth(), ImageCanvas.getHeight());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", "*.png"));
        File loadImageFile = fileChooser.showOpenDialog(new Stage());
        image = new Image(loadImageFile.toURI().toString());
        Iview = new ImageView(image);
        Iview.setFitHeight(500);
        Iview.setFitWidth(600);
        Spane.getChildren().addAll(ImageCanvas, Iview);
        ImageCanvas.toFront();
        return image;
    }

    @FXML
    public void SendFile(ActionEvent actionEvent) {
        WritableImage wi = new WritableImage(600, 500);
        Spane.snapshot(null, wi);
        File outputFile = new File("S:/Dosug/Заявка №" + count + ".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(wi, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter("S:/Dosug/Подробное описание заявки №" + count + ".txt"));
            bf.write(TextAreaSend.getText());
            bf.flush();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Заявка отправлена. Спасибо!"));
        Scene dialogScene = new Scene(dialogVbox, 200, 50);
        dialog.setScene(dialogScene);
        dialog.show();
        Spane.getChildren().clear();
        gc.clearRect(0, 0, ImageCanvas.getWidth(), ImageCanvas.getHeight());
        TextAreaSend.setText("");
        count++;
    }
}
