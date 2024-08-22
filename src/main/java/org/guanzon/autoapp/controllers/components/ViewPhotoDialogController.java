/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ViewPhotoDialogController implements Initializable {

    private GRider oApp;
    private String psValidPhoto;
    private String psValidPhotoURL;
    private String psFormTitle;
    @FXML
    private Label lbFormTitle;
    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane searchBar;
    @FXML
    private Label lblReference;
    @FXML
    private ImageView imgPhoto;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setPicName(String fsValue) {
        psValidPhoto = fsValue;
    }

    public void setPicUrl(String fsValue) {
        psValidPhotoURL = fsValue;
    }

    public void setFormTitle(String fsValue) {
        psFormTitle = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPhoto();
        btnClose.setOnAction(this::handleButtonAction);

    }

    private void loadPhoto() {
        lbFormTitle.setText(psFormTitle);
        lblReference.setText(psValidPhoto);
        if (!psValidPhotoURL.isEmpty()) {
            imgPhoto.setImage(new Image(psValidPhotoURL));
        } else {
            Image loImage = new Image("file:D:/Integrated Automotive System/autoapp/src/main/resources/org/guanzon/autoapp/images/no-image-available.png");
            imgPhoto.setImage(loImage);
        }

    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
        }
    }

}
