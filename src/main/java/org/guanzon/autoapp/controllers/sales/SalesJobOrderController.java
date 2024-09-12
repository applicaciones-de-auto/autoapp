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
import org.guanzon.autoapp.models.general.Technician;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.sales.Part;
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
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCancelJobOrder, btnClose, btnDone, btnAddLabor, btnAddAccessories;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Tab tabMain, laborTab, accessoriesTab, technicianTab, accessoriesMatTab, issuanceTab;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06, txtField07, txtField09, txtField10, txtField11, txtField12, txtField13;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private TextArea textArea08, textArea14, textArea09;
    @FXML
    private TableView<Labor> tblViewLabor;
    @FXML
    private TableColumn<Labor, String> tblindex01_labor, tblindex02_labor, tblindex03_labor, tblindex04_labor, tblindex05_Labor;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableColumn<Part, String> tblindex01_accer, tblindex02_accer, tblindex03_accer, tblindex04_accer, tblindex05_accer, tblindex06_accer,
            tblindex07_accer, tblindex08_accer;
    @FXML
    private TableView<Technician> tblViewTechnician;
    @FXML
    private TableColumn<Technician, String> tblindex01_tech, tblindex02_tech, tblindex03_tech;
    @FXML
    private TableView<?> tblViewPaintings;
    @FXML
    private TableView<?> tblViewPaintings1;
    @FXML
    private Button btnAddTechnician;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
