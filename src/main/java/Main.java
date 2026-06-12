import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/view/user/login-home-form.fxml")));
            stage.setScene(new Scene(root));
            stage.setTitle("ClothingStore - Boutique Manager");

            InputStream icon = getClass().getResourceAsStream("/images/logo.png");
            if (icon != null) {
                stage.getIcons().add(new Image(icon));
            }
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Startup error");
            alert.setHeaderText("ClothingStore could not start");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
