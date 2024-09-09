/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class SalesJobOrderController implements Initializable, ScreenInterface {

    private GRider oApp;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle, lblJobOrderStatus;
    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCancelJobOrder, btnClose, btnDone;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Tab tabMain;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06, txtField07, txtField09, txtField10, txtField12, txtField13;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private TextArea textArea08, textArea14, textArea09;
    @FXML
    private Tab laborTab;
    @FXML
    private TableView<?> tblViewLabor;
    @FXML
    private TableColumn<?, ?> tblindex01_labor;
    @FXML
    private TableColumn<?, ?> tblindex02_labor;
    @FXML
    private TableColumn<?, ?> tblindex03_labor;
    @FXML
    private TableColumn<?, ?> tblindex03_Labor;
    @FXML
    private TableColumn<?, ?> tblindex06_Labor;
    @FXML
    private Button btnAddLabor;
    @FXML
    private Tab accessoriesTab;
    @FXML
    private Button btnAddAccessories;
    @FXML
    private TableView<?> tblViewAccessories;
    @FXML
    private TableColumn<?, ?> tblindex01_accer;
    @FXML
    private TableColumn<?, ?> tblindex02_accer;
    @FXML
    private TableColumn<?, ?> tblindex03_accer;
    @FXML
    private TableColumn<?, ?> tblindex04_accer;
    @FXML
    private TableColumn<?, ?> tblindex05_accer;
    @FXML
    private TableColumn<?, ?> tblindex06_accer;
    @FXML
    private TableColumn<?, ?> tblindex07_accer;
    @FXML
    private TableColumn<?, ?> tblindex08_accer;
    @FXML
    private Tab technicianTab;
    @FXML
    private TableView<?> tblViewTechnician;
    @FXML
    private TableColumn<?, ?> tblindex01_tech;
    @FXML
    private TableColumn<?, ?> tblindex02_tech;
    @FXML
    private TableColumn<?, ?> tblindex03_tech;
    @FXML
    private Button btnAddLabor1;
    @FXML
    private Tab accessoriesMatTab;
    @FXML
    private TableView<?> tblViewPaintings;
    @FXML
    private Tab issuanceTab;
    @FXML
    private TableView<?> tblViewPaintings1;
    @FXML
    private TextField txtField11;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
