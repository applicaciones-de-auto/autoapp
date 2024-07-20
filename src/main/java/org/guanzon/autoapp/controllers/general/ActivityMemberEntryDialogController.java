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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityMemberEntryDialogController implements Initializable {

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private Label SelectedCount;
    @FXML
    private TableView<?> tblViewDepart;
    @FXML
    private TableColumn<?, ?> tblDprtmntndex01;
    @FXML
    private TableView<?> tblViewEmployee;
    @FXML
    private TableColumn<?, ?> tblEmplyIndex01;
    @FXML
    private TableColumn<?, ?> tblEmplyIndex02;
    @FXML
    private CheckBox selectAllEmployee;
    @FXML
    private TableColumn<?, ?> tblEmplyIndex03;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
