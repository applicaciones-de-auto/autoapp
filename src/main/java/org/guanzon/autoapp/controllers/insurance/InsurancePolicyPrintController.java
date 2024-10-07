/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.insurance;

import java.awt.Component;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.interfaces.GPrintInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InsurancePolicyPrintController implements Initializable, GPrintInterface {

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private VBox vbProgress;
    @FXML
    private AnchorPane reportPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void setGRider(GRider foValue) {

    }

    @Override
    public void setTransNo(String fsValue) {
    }

    @Override
    public void handleButtonAction(ActionEvent event) {

    }

    @Override
    public void hideReport() {

    }

    @Override
    public void generateReport() {

    }

    @Override
    public void loadReport() {

    }

    @Override
    public void showReport() {

    }

    @Override
    public void findAndHideButton(Component foComponent, String fsButtonText) {

    }

}
