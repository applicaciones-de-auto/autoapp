/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.models.sales.ModelInquiryBankApplications;
import org.guanzon.autoapp.models.sales.ModelInquiryFollowUp;
import org.guanzon.autoapp.models.sales.ModelInquiryPromoOffered;
import org.guanzon.autoapp.models.sales.ModelInquiryRequirements;
import org.guanzon.autoapp.models.sales.ModelInquiryVehiclePriority;
import org.guanzon.autoapp.models.sales.ModelInquiryVehicleSalesAdvances;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquiryFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Inquiry oTransInquiry;
    private String pxeModuleName = "Vehicle Inquiry";
    private int pnEditMode;//Modifying fields for Customer Entry
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    UnloadForm poUnload = new UnloadForm(); //Object for closing form
    //    /* ------------------DATA TABLES----------------------- */
    private ObservableList<ModelInquiryVehiclePriority> priorityunitdata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryPromoOffered> promosoffereddata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryRequirements> inqrequirementsdata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryVehicleSalesAdvances> inqvsadata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryBankApplications> bankappdata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryFollowUp> followupdata = FXCollections.observableArrayList();

    //    /* ------------------COMBO BOX ITEMS/VALUE----------------------- */
    private ObservableList<String> cInquirySourceType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO"); //Inquiry Type values
    private ObservableList<String> cInqStatus = FXCollections.observableArrayList("FOR FOLLOW-UP", "ON PROCESS", "LOST SALE", "WITH VSP", "SOLD", "RETIRED", "CANCELLED"); //Inquiry Type Values
    private ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    private ObservableList<String> cCustomerType = FXCollections.observableArrayList("BUSINESS", "EMPLOYED", "OFW", "SEAMAN", "ANY"); // Customer Type Values
    private ObservableList<String> cTargetVehcl = FXCollections.observableArrayList("BRANDNEW", "PRE-OWNED");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnApply, btnConvertSales, btnLostSale, btnProcess, btnModify, btnCancel, btnClose;
    @FXML
    private Label lblInqStatus;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tabCustomerInquiry;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
            txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField17, txtField18, txtField20, txtField27, txtField29, txtField30;
    @FXML
    private ToggleGroup hotCategoryToggleGroup;
    @FXML
    private TableView<ModelInquiryVehiclePriority> tblPriorityUnit;
    @FXML
    private TableColumn<ModelInquiryVehiclePriority, String> trgvIndex01, trgvIndex02;
    @FXML
    private TableColumn<ModelInquiryVehiclePriority, Button> trgvIndex03, trgvIndex04;
    @FXML
    private Button btnTargetVhclRemove, btnTargetVhclAdd;
    @FXML
    private Button btnPromoRemove, btnPromoAdd;
    @FXML
    private TableView<ModelInquiryPromoOffered> tblPromosOffered;
    @FXML
    private TableColumn<ModelInquiryPromoOffered, String> prmoIndex01, prmoIndex02, prmoIndex03, prmoIndex04;
    @FXML
    private Tab tabInquiryProcess;
    @FXML
    private TableView<ModelInquiryRequirements> tblRequirementsInfo;
    @FXML
    private TableColumn<ModelInquiryRequirements, Boolean> rqrmIndex01;
    @FXML
    private TableColumn<ModelInquiryRequirements, String> rqrmIndex02, rqrmIndex03, rqrmIndex04;
    @FXML
    private Button btnASadd, btnASremove, btnASprint, btnAScancel;
    @FXML
    private TableView<ModelInquiryVehicleSalesAdvances> tblAdvanceSlip;
    @FXML
    private TableColumn<ModelInquiryVehicleSalesAdvances, Boolean> vsasCheck01;
    @FXML
    private TableColumn<ModelInquiryVehicleSalesAdvances, String> vsasIndex01, vsasIndex02, vsasIndex03, vsasIndex04, vsasIndex05, vsasIndex06, vsasIndex07;
    @FXML
    private Button btnSndMngerApprov;
    @FXML
    private Tab tabBankHistory;
    @FXML
    private Button btnBankAppNew, btnBankAppView, btnBankAppUpdate, btnBankAppCancel;
    @FXML
    private TableView<ModelInquiryBankApplications> tblBankApplication;
    @FXML
    private TableColumn<ModelInquiryBankApplications, Boolean> bankCheck01;
    @FXML
    private TableColumn<ModelInquiryBankApplications, String> bankIndex01, bankIndex02, bankIndex03, bankIndex04, bankIndex05, bankIndex06;
    @FXML
    private Tab tabFollowingHistory;
    @FXML
    private Button btnFollowUp;
    @FXML
    private TableView<ModelInquiryFollowUp> tblFollowHistory;
    @FXML
    private TableColumn<ModelInquiryFollowUp, String> flwpIndex01, flwpIndex02, flwpIndex03, flwpIndex04, flwpIndex05, flwpIndex06;
    @FXML
    private ComboBox<String> comboBox10, comboBox21, comboBox25, comboBox26;
    @FXML
    private RadioButton rdbtnHtA19, rdbtnHtB19, rdbtnHtC19;
    @FXML
    private DatePicker datePicker22;
    @FXML
    private TextArea textArea04, textArea23, textArea24, textArea28;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
        initVehiclePriority();
        initPromoOffered();
        initInquiryRequirements();
        initAdvancesSlip();
        initBankApplications();
        initFollowHistory();
        initComboBoxValue();
//        initTextFieldPattern();
//        txtField11.setOnAction(this::getDate);
        initCapitalizationFields();
        initTextKeyPressed();
//        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initComboBoxValue() {
        comboBox10.setItems(cInquirySourceType);
        comboBox21.setItems(cTargetVehcl);
        comboBox25.setItems(cModeOfPayment);
        comboBox26.setItems(cCustomerType);
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField17, txtField18, txtField20, txtField27, txtField29, txtField30);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea04, textArea23, textArea24, textArea28);

        loTxtArea.forEach(ta -> InputTextUtil.setCapsLockBehavior(ta));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField17, txtField18, txtField20, txtField27, txtField29, txtField30);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea04, textArea23, textArea24, textArea28);
        loTxtArea.forEach(ta -> ta.setOnKeyPressed(event -> textArea_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            TextField lsTxtField = (TextField) event.getSource();
            String txtFieldID = ((TextField) event.getSource()).getId();
            String lsValue = "";
            if (lsTxtField.getText() == null) {
                lsValue = "";
            } else {
                lsValue = lsTxtField.getText();
            }
            JSONObject loJSON = new JSONObject();
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField03":
                        loJSON = oTransInquiry.searchClient(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField03.setText(String.valueOf(oTransInquiry.getMaster("sClientNm")));
                            textArea04.setText(String.valueOf(oTransInquiry.getMaster("sAddressx")));
                            txtField06.setText(String.valueOf(oTransInquiry.getMaster("sMobileNo")));
                            txtField07.setText(String.valueOf(oTransInquiry.getMaster("sEmailAdd")));
                            txtField08.setText(String.valueOf(oTransInquiry.getMaster("sAccountx")));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField03.setText("");
                            txtField03.requestFocus();
                            return;
                        }
                        break;
                    case "txtField05":
                        loJSON = oTransInquiry.searchClient(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField05.setText(String.valueOf(oTransInquiry.getMaster("sContctNm")));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField05.setText("");
                            txtField05.requestFocus();
                            return;
                        }
                        break;
                    case "txtField09":
                        loJSON = oTransInquiry.searchSalesExecutive(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField09.setText(String.valueOf(oTransInquiry.getMaster("sSalesExe")));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField09.setText("");
                            txtField09.requestFocus();
                            return;
                        }
                        break;
                    case "txtField11":
                        loJSON = oTransInquiry.searchOnlinePlatform(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField11.setText(String.valueOf(oTransInquiry.getMaster("sSalesExe")));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField11.setText("");
                            txtField11.requestFocus();
                            return;
                        }
                        break;
                    case "txtField12":
                        loJSON = oTransInquiry.searchActivity(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField12.setText(String.valueOf(oTransInquiry.getMaster("sActTitle")));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField12.setText("");
                            txtField12.requestFocus();
                            return;
                        }
                        break;
                    case "txtField13":
                        loJSON = oTransInquiry.searchReferralAgent(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField13.setText(String.valueOf(oTransInquiry.getMaster("sSalesAgn")));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField13.setText("");
                            txtField13.requestFocus();
                            return;
                        }
                        break;
//                    case "txtField14":
//                        loJSON = oTransInquiry.searhBranch(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField14.setText(String.valueOf(oTransInquiry.getMaster("sSalesAgn")));
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField14.setText("");
//                            txtField14.requestFocus();
//                            return;
//                        }
//                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            }

        }
    }

    private void textArea_KeyPressed(KeyEvent event) {
        String textAreaID = ((TextArea) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (textAreaID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void initCmboxFieldAction() {

    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnApply, btnConvertSales, btnLostSale, btnProcess, btnModify, btnCancel,
                btnClose);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
                loJSON = oTransInquiry.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVehicleInquiryInformation();
                    pnEditMode = oTransInquiry.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransInquiry.updateTransaction();
                pnEditMode = oTransInquiry.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Customer Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransInquiry.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVehicleInquiryInformation();
                    loadVehiclePriority();
                    loadPromoOffered();
                    loadInquiryRequirements();
                    loadAdvancesSlip();
                    loadBankApplications();
                    loadFollowHistory();
                    pnEditMode = oTransInquiry.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Inquiry Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Customer Information Saving....", "Are you sure, do you want to save?")) {
                } else {
                    return;
                }

                loJSON = oTransInquiry.saveTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Customer Information", (String) loJSON.get("message"));
                    loJSON = oTransInquiry.openTransaction("");
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadVehicleInquiryInformation();
                        loadVehiclePriority();
                        loadPromoOffered();
                        loadInquiryRequirements();
                        loadAdvancesSlip();
                        loadBankApplications();
                        loadFollowHistory();
                        initFields(pnEditMode);
                        pnEditMode = oTransInquiry.getEditMode();
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Customer");
                    } else {
                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    private void loadVehicleInquiryInformation() {

    }

    private void initFields(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField03.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField09.setDisable(!lbShow);
        comboBox10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        rdbtnHtA19.setDisable(!lbShow);
        rdbtnHtB19.setDisable(!lbShow);
        rdbtnHtC19.setDisable(!lbShow);
        txtField20.setDisable(!lbShow);
        comboBox21.setDisable(!lbShow);
        datePicker22.setDisable(!lbShow);
        textArea23.setDisable(!lbShow);
        textArea24.setDisable(!lbShow);
        comboBox25.setDisable(!lbShow);
        comboBox26.setDisable(!lbShow);
        txtField27.setDisable(!lbShow);

        if (fnValue == EditMode.ADDNEW) {
            if (oApp.isMainOffice()) {
                txtField14.setDisable(false); // Branch Name
            } else {
                txtField14.setDisable(true); // Branch Name
            }
        }
        switch (comboBox10.getSelectionModel().getSelectedIndex()) {
            case 1:
                txtField11.setDisable(!lbShow);//Online Store
                txtField12.setText("");//Activity ID
                txtField13.setText("");//Agent ID
                break;
            case 3:
                txtField13.setDisable(!lbShow);//Agent ID
                txtField11.setText("");//Online Store
                txtField12.setText("");//Activity ID
                break;
            case 4:
            case 5:
                txtField12.setDisable(!lbShow);//Activity ID
                txtField13.setText("");//Agent ID
                txtField11.setText("");//Online Store
                break;
            default:
                txtField12.setDisable(true); //Activity ID
                txtField13.setDisable(true); //Agent ID
                txtField11.setDisable(true); //Online Store
        }

        //Inquiry button
        btnTargetVhclAdd.setVisible(lbShow);
        btnTargetVhclRemove.setVisible(lbShow);
        //Inquiry General Button
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnConvertSales.setVisible(false);
        btnConvertSales.setManaged(false);
        btnLostSale.setVisible(false);
        btnLostSale.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        //Bank Application
        btnBankAppNew.setVisible(false);
        btnBankAppUpdate.setVisible(false);
        btnBankAppCancel.setVisible(false);
        btnBankAppView.setVisible(false);
        bankCheck01.setVisible(false);
        //For Follow up
        btnFollowUp.setVisible(false);

        boolean lbTab = (fnValue == EditMode.READY);
        //tabCustomerInquiry.setDisable(!lbTab);
        tabInquiryProcess.setDisable(!lbTab);
        tabBankHistory.setDisable(!lbTab);
        tabFollowingHistory.setDisable(!lbTab);

        lblInqStatus.setText("");
//        if (comboBox24.getSelectionModel().getSelectedIndex() >= 0) {
//            lblInqStatus.setText(comboBox24.getValue().toString());
//        }
//        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
//            btnEdit.setVisible(true);
//            btnEdit.setManaged(true);
//            //Enable Button / textfield based on Inquiry Status
//            switch (comboBox24.getSelectionModel().getSelectedIndex()) { // cTranStat
//                case 0://For Follow up
//                    btnLostSale.setVisible(true);
//                    btnLostSale.setManaged(true);
//                    btnFollowUp.setVisible(true);
//                    break;
//                case 1: //On process
//                    btnLostSale.setVisible(true);
//                    btnLostSale.setManaged(true);
//                    if (cmbInqpr01.getSelectionModel().getSelectedIndex() >= 0) {
//                        btnConvertSales.setVisible(true);
//                        btnConvertSales.setManaged(true);
//                    }
//                    if (cmbInqpr01.getSelectionModel().getSelectedIndex() > 0) {
//                        //Bank Application
//                        btnBankAppNew.setVisible(true);
//                        btnBankAppUpdate.setVisible(true);
//                        btnBankAppCancel.setVisible(true);
//                        btnBankAppView.setVisible(true);
//                        bankCheck01.setVisible(true);
//                    }
//                    //For Follow up
//                    btnFollowUp.setVisible(true);
//                    break;
//                case 3: //VSP
//                    //Bank Application
//                    btnBankAppNew.setVisible(true);
//                    btnBankAppUpdate.setVisible(true);
//                    btnBankAppCancel.setVisible(true);
//                    btnBankAppView.setVisible(true);
//                    bankCheck01.setVisible(true);
//                    //For Follow up
//                    btnFollowUp.setVisible(true);
//                    break;
//                case 2: //Lost Sale
//                    btnPrintRefund.setVisible(true);
//                    btnPrintRefund.setManaged(true);
//                    btnEdit.setVisible(false);
//                    btnEdit.setManaged(false);
//                    break;
//                case 5: //Retired
//                    tabInquiryProcess.setDisable(true);
//                    tabBankHistory.setDisable(true);
//                    btnFollowUp.setVisible(true);
//                    btnEdit.setVisible(false);
//                    btnEdit.setManaged(false);
//                    break;
//                case 4: //Sold
//                case 6: //Cancelled
//                    btnEdit.setVisible(false);
//                    btnEdit.setManaged(false);
//                    break;
//            }
//        }
//
//        if (pnSelectedIndex == 1) {
//            btnAdd.setVisible(false);
//            btnAdd.setManaged(false);
//            btnEdit.setVisible(false);
//            btnEdit.setManaged(false);
//            btnSave.setVisible(false);
//            btnSave.setManaged(false);
//            btnClear.setVisible(false);
//            btnClear.setManaged(false);
//            btnCancel.setVisible(false);
//            btnCancel.setManaged(false);
//        } else {
//            btnProcess.setVisible(false);
//            btnProcess.setManaged(false);
//            btnModify.setVisible(false);
//            btnModify.setManaged(false);
//            btnApply.setVisible(false);
//            btnApply.setManaged(false);
//        }
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        textArea04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        comboBox10.setValue("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        txtField14.setText("");
        txtField15.setText("");
        txtField16.setText("");
        txtField17.setText("0.00");
        txtField18.setText("0.00");
        rdbtnHtA19.setSelected(false);
        rdbtnHtB19.setSelected(false);
        rdbtnHtC19.setSelected(false);
        txtField20.setText("");
        comboBox21.setValue("");
        datePicker22.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())));
        textArea23.setText("");
        textArea24.setText("");
        comboBox25.setValue("");
        comboBox26.setValue("");
        txtField27.setText("");
        textArea28.setText("");
        txtField29.setText("");
        txtField30.setText("");
    }

    private void clearTables() {
        priorityunitdata.clear();
        promosoffereddata.clear();
        inqrequirementsdata.clear();
        inqvsadata.clear();
        bankappdata.clear();
        followupdata.clear();
    }

    private void loadVehiclePriority() {

    }

    private void initVehiclePriority() {
        trgvIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        trgvIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        trgvIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        trgvIndex03.setCellFactory(param -> new TableCell<ModelInquiryVehiclePriority, Button>() {
            Button upBtn = new Button();

            {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("ARROW_UP");
                icon.setSize("12px");
                icon.setStyleClass("button-icon");
                upBtn.setGraphic(icon);

                upBtn.setOnAction(event -> {
                    ModelInquiryVehiclePriority model = getTableView().getItems().get(getIndex());
                    ObservableList<ModelInquiryVehiclePriority> items = tblPriorityUnit.getItems();
                    int currentIndex = getIndex();

                    if (currentIndex > 0) {

                        ModelInquiryVehiclePriority prevModel = items.get(currentIndex - 1);
                        items.set(currentIndex - 1, model);
                        items.set(currentIndex, prevModel);
                        getTableView().getSelectionModel().select(currentIndex - 1);
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(upBtn);
                }
            }
        });

        trgvIndex03.setCellFactory(param -> new TableCell<ModelInquiryVehiclePriority, Button>() {
            Button downBtn = new Button();

            {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("ARROW_DOWN");
                icon.setSize("12px");
                icon.setStyleClass("button-icon");
                downBtn.setGraphic(icon);

                downBtn.setOnAction(event -> {
                    ModelInquiryVehiclePriority model = getTableView().getItems().get(getIndex());
                    ObservableList<ModelInquiryVehiclePriority> items = tblPriorityUnit.getItems();
                    int currentIndex = getIndex();

                    if (currentIndex < items.size() - 1) {

                        ModelInquiryVehiclePriority nextModel = items.get(currentIndex + 1);
                        items.set(currentIndex + 1, model);
                        items.set(currentIndex, nextModel);

                        getTableView().getSelectionModel().select(currentIndex + 1);
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(downBtn);
                }
            }
        });

        tblPriorityUnit.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblPriorityUnit.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblPriorityUnit.setItems(priorityunitdata);
    }

    private void loadPromoOffered() {

    }

    private void initPromoOffered() {
        prmoIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        prmoIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        prmoIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        prmoIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        tblPromosOffered.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblPromosOffered.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblPromosOffered.setItems(promosoffereddata);
    }

    private void loadInquiryRequirements() {

    }

    private void initInquiryRequirements() {
        rqrmIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        rqrmIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        rqrmIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        rqrmIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        tblRequirementsInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequirementsInfo.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblRequirementsInfo.setItems(inqrequirementsdata);
    }

    private void loadAdvancesSlip() {

    }

    private void initAdvancesSlip() {
        vsasIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        vsasIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        vsasIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        vsasIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        vsasIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        vsasIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));

        tblAdvanceSlip.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAdvanceSlip.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblAdvanceSlip.setItems(inqvsadata);
    }

    private void loadBankApplications() {

    }

    private void initBankApplications() {
        bankIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        bankCheck01.setCellValueFactory(new PropertyValueFactory<>("tblcheck01"));
        bankIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        bankIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        bankIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        bankIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        bankIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex12"));

        tblBankApplication.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblBankApplication.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblBankApplication.setItems(bankappdata);
    }

    private void loadFollowHistory() {

    }

    private void initFollowHistory() {
        flwpIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        flwpIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        flwpIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        flwpIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        flwpIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        flwpIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));

        tblFollowHistory.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblFollowHistory.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblFollowHistory.setItems(followupdata);
    }
}
