/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ItemEntryExpandModelTableController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TableView<?> tblVModelList;
    @FXML
    private CheckBox selectModelAll;
    @FXML
    private Button btnRemove;
    @FXML
    private TableColumn<?, ?> tblindexModel01;
    @FXML
    private TableColumn<?, ?> tblindexModel02;
    @FXML
    private TableColumn<?, ?> tblindexModel03;
    @FXML
    private TableColumn<?, ?> tblindexModel04;
    @FXML
    private TableColumn<?, ?> tblindexModel05;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
