package edu.icet.demo.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Swaps the scene of the current stage to the given FXML view.
 */
public final class Navigation {

    private Navigation() {
    }

    public static void navigate(Node sourceNode, String fxmlResourcePath) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(Navigation.class.getResource(fxmlResourcePath),
                            "View not found: " + fxmlResourcePath));
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            throw new IllegalStateException("Could not open the view: " + fxmlResourcePath, e);
        }
    }
}
