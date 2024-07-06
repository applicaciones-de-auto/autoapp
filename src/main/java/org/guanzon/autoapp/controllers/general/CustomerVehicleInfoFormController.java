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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerVehicleInfoFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Customer Vehicle Information"; //Form Title
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnTransfer;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField35V;
    @FXML
    private TextArea textArea37V;
    @FXML
    private TextField txtField36V;
    @FXML
    private TextArea textArea38V;
    @FXML
    private Button btnVhclAvl;
    @FXML
    private Button btnVhclDesc;
    @FXML
    private TextField txtField24V;
    @FXML
    private TextField txtField26V;
    @FXML
    private TextField txtField28V;
    @FXML
    private TextField txtField31V;
    @FXML
    private TextField txtField30V;
    @FXML
    private TextField txtField32V;
    @FXML
    private TextField txtField20V;
    @FXML
    private TextField txtField03V;
    @FXML
    private TextField txtField11V;
    @FXML
    private TextField txtField08V;
    @FXML
    private TextField txtField04V;
    @FXML
    private Button btnEngFrm;
    @FXML
    private Button btnVhclMnl;
    @FXML
    private TextField txtField13;
    @FXML
    private VBox vboxSales;
    @FXML
    private AnchorPane anchorPurch;
    @FXML
    private GridPane gridPurch;
    @FXML
    private ComboBox<?> comboBox14;
    @FXML
    private ComboBox<?> comboBox15;
    @FXML
    private AnchorPane anchorSold;
    @FXML
    private GridPane gridSold;
    @FXML
    private TextField txtField42V;
    @FXML
    private TextField txtField41V;
    @FXML
    private TextField txtField40V;
    @FXML
    private AnchorPane anchorMisc;
    @FXML
    private GridPane gridMisc;
    @FXML
    private TextField txtField09V;
    @FXML
    private DatePicker txtField21V;
    @FXML
    private TextField txtField22V;
    @FXML
    private TextArea textArea34V;
    @FXML
    private TableView<?> tblViewVhclHsty;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty01;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty02;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty03;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty04;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty05;
    @FXML
    private TableView<?> tblViewVhclHsty1;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty011;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty021;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty031;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty041;
    @FXML
    private TableColumn<?, ?> tblVhcllHsty051;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

}
