package org.guanzon.autoapp;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.interfaces.ScreenInterface;

/**
 * FXMLMenuParameterForm is a class that represents a form window for displaying
 * parameters using JavaFX. This form can be used to display modal dialogs with
 * a drop shadow effect.
 *
 * @author Arsiela Date Created: 06-21-2023
 */
public class FXMLMenuParameterForm {

    // Variables to track the window movement
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Opens the parameter window.
     *
     * @param fsiController The controller implementing the ScreenInterface
     * interface.
     * @param oApp The GRider object.
     * @param fsFxml The path to the FXML file for the parameter form.
     * @param fxmlPathDirectory The directory where the FXML file is located.
     */
    public void FXMLMenuParameterForm(ScreenInterface fsiController, GRider oApp, String fsFxml, String fxmlPathDirectory) {
        try {
            // Create a new stage for the dialog
            Stage stage = new Stage();

            // Initialize the ScreenInterface and set the GRider
            ScreenInterface fxObj = fsiController;
            fxObj.setGRider(oApp);

            // Construct the correct path to the FXML file
            String fxmlPath = fxmlPathDirectory + fsFxml;
            java.net.URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.out.println("Resource not found: " + fxmlPath);
                throw new IOException("FXML resource not found: " + fxmlPath);
            }

            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(resource);
            fxmlLoader.setController(fxObj);
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            // Set the parent as the scene
            Scene scene = new Scene(parent);

            // Configure the stage for the dialog
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");

            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
}
