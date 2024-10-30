/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InvoiceController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
//    private CAR oTrans;
    private String pxeModuleName = "";
    private UnloadForm poUnload = new UnloadForm();
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private String lsClientSource = "";
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cSourcexx = FXCollections.observableArrayList("NEW", "RENEW");
    ObservableList<String> cPayerxxx = FXCollections.observableArrayList("CUSTOMER", "SUPPLIER");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblInvoiceTitle, lblStatus, lblPrinted;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnInvCancel, btnClose;
    @FXML
    private TextField txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10, txtField11, txtField12,
            txtField13, txtField15, txtField16, txtField17, txtField18, txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25,
            txtField26, txtField27;
    @FXML
    private DatePicker datePicker02;
    @FXML
    private CheckBox checkBoxCash, checkBoxCard, checkBoxOnlnPymntServ, checkBoxCrdInv, checkBoxNoPymnt, checkBoxCheck,
            checkBoxGftCheck, checkBoxAllwMxPyr;
    @FXML
    private ComboBox<String> comboBox04, comboBox11;
    @FXML
    private Button btnInstTransDetail, btnInsertRemarks, btnInsertAdvances, btnInsCheckDetail;
    @FXML
    private TableView<?> tblViewTrans;
    @FXML
    private TableColumn<?, ?> tblindexTrans01;
    @FXML
    private TableColumn<?, ?> tblindexTrans02, tblindexTrans03, tblindexTrans04, tblindexTrans05, tblindexTrans06, tblindexTrans07, tblindexTrans08, tblindexTrans09,
            tblindexTrans10;
    @FXML
    private TableView<?> tblViewCheck;
    @FXML
    private TableColumn<?, ?> tblindexCheck01;
    @FXML
    private TableColumn<?, ?> tblindexCheck02, tblindexCheck03, tblindexCheck04, tblindexCheck05, tblindexCheck06, tblindexCheck07, tblindexCheck08;
    @FXML
    private TextArea textArea12, textArea14;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            lblInvoiceTitle.setText(getParentTabTitle());
        });
        initButtonsClick();
        initFields(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {

    }

    @Override
    public void initCapitalizationFields() {

    }

    @Override
    public boolean loadMasterFields() {

        return true;
    }

    @Override
    public void initPatternFields() {

    }

    @Override
    public void initLimiterFields() {

    }

    @Override
    public void initTextFieldFocus() {

    }

    @Override
    public void initTextKeyPressed() {

    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {

    }

    @Override
    public void textArea_KeyPressed(KeyEvent event) {

    }

    @Override
    public void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnClose);
        loButtons.forEach(btn -> btn.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                    break;
                } else {
                    return;
                }
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    @Override
    public void initComboBoxItems() {

    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void initTextFieldsProperty() {

    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {

    }

    @Override
    public void initFields(int fnValue) {
//        String lsInvCancel = "";
//        switch (pxeModuleName) {
//            case "Acknowledgement Receipt":
//                lsInvCancel = "ARCancel";
//                break;
//            case "Billing Statement":
//                lsInvCancel = "BSCancel";
//                break;
//            case "Collection Receipt":
//                lsInvCancel = "CRCancel";
//                break;
//            case "Official Receipt":
//                lsInvCancel = "ORCancel";
//                break;
//            case "Parts Sales Invoice":
//                lsInvCancel = "PSICancel";
//                break;
//        }
//        btnInvCancel.setText(lsInvCancel);
    }

    private String getParentTabTitle() {
        Node parent = AnchorMain.getParent();
        Parent tabContentParent = parent.getParent();

        if (tabContentParent instanceof TabPane) {
            TabPane tabPane = (TabPane) tabContentParent;
            Tab tab = findTabByContent(tabPane, AnchorMain);
            if (tab != null) {
                pxeModuleName = tab.getText();
                return tab.getText().toUpperCase();
            }
        }

        return null; // No parent Tab found
    }

    private Tab findTabByContent(TabPane tabPane, Node content) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getContent() == content) {
                return tab;
            }
        }
        return null;
    }
}
