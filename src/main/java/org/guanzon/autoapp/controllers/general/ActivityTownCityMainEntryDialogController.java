/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityTownCityMainEntryDialogController implements Initializable {

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAddTown;
    @FXML
    private Button btnClose;
    @FXML
    private Label SelectedCount;
    @FXML
    private TableView<?> tblViewTown;
    @FXML
    private TableColumn<?, ?> tblindex01;
    @FXML
    private TableColumn<?, ?> tblindex02;
    @FXML
    private TableColumn<?, ?> tblindex03;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
