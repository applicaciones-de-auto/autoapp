package org.guanzon.autoapp.controllers.insurance;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.insurance.InsurancePolicyApplication;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InsuranceApplicationController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private InsurancePolicyApplication oTransInsApplication;
    private String pxeModuleName = "Insurance Proposal";
    private UnloadForm poUnload = new UnloadForm();
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cAppType = FXCollections.observableArrayList("NEW", "RENEW");
    ObservableList<String> cNewBusType = FXCollections.observableArrayList("YES", "NO");
    ObservableList<String> cPolType = FXCollections.observableArrayList("TPL", "COMPREHENSIVE", "TPL AND COMPREHENSIVE");
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cVhclTyp = FXCollections.observableArrayList("COMMERCIAL", "PRIVATE");
    ObservableList<String> cBodyType = FXCollections.observableArrayList("SEDAN", "SUV", "HATCHBACK", "MPV", "MOTORCYCLE", "TRUCK");
    ObservableList<String> cVhclSize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");
    ObservableList<String> cActsNtr = FXCollections.observableArrayList("CHARGE", "FREE", "NOT APPLICABLE");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
            txtField19, txtField20, txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
            txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
            txtField39, txtField40, txtField41, txtField42, txtField43, txtField45;
    @FXML
    private DatePicker datePicker05, datePicker06, datePicker11;
    @FXML
    private TextArea textArea14, textArea44, textArea46;

    @FXML
    private ComboBox<String> comboBox03, comboBox04, comboBox09, comboBox12, comboBox18, comboBox21, comboBox22, comboBox28;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnBrowse, btnInsAppCancel, btnCancel, btnPrint, btnClose, btnPayment;

    @FXML
    private Label lblPolicyNo, lblPrintDate, lblStatus;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransInsApplication = new InsurancePolicyApplication(oApp, false, oApp.getBranchCode());
        datePicker05.setDayCellFactory(DateFrom);
        initCapitalizationFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02,
                txtField07, txtField08, txtField10, txtField13,
                txtField15, txtField16, txtField17, txtField19, txtField20);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTextArea = Arrays.asList(textArea14, textArea44, textArea46);
        loTextArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField17, txtField41);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea44);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
    }
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 17:
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    int lnAutCapacity = Integer.parseInt(lsValue);
//                    oTransInsApplication.getMasterModel().getMasterModel().setAuhorize(lnAutCapacity);
//                    txtField17.setText( oTransInsApplication.getMasterModel().getMasterModel().getAuhorize();
                    break;
                case 41:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnDiscount = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnDiscount < 0) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00"; // Reset to 0.00 for invalid amount
                    }
////                    oTransInsApplication.getMasterModel().getMasterModel().setAuhorize(lnAutCapacity);
////                    txtField17.setText( oTransInsApplication.getMasterModel().getMasterModel().getAuhorize();
//                    oTransInsApplication.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(lsValue.replace(",", "")));
//                    if (!loadMasterFields()) {
//                        txtField41.setText("0.00");
//                        oTransInsApplication.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(txtField41.getText().replace(",", "")));
//                    }
//                    txtField41.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getODTCAmt()))));
                    break;
            }
            loadMasterFields();
        } else {
            txtField.selectAll();

        }
    };
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        TextArea loTextArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextArea.getId().substring(8, 10));
        String lsValue = loTextArea.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 44:
                    break;

            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField10, txtField41, txtField08);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        List<TextArea> loTxtArea = Arrays.asList(textArea44);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
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
                case "txtField01":
                    loJSON = oTransInsApplication.searchProposal(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField08":
                    loJSON = oTransInsApplication.searchInsuranceCoordinator(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {;
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    break;
                case "txtField10":
                    loJSON = oTransInsApplication.searchbank(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
            initFields(pnEditMode);
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
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

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnInsAppCancel, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransInsApplication = new InsurancePolicyApplication(oApp, false, oApp.getBranchCode());
                loJSON = oTransInsApplication.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTransInsApplication.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransInsApplication.updateTransaction();
                pnEditMode = oTransInsApplication.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                LocalDate loDateFrom = datePicker05.getValue();
                LocalDate loDateTo = datePicker06.getValue();
                if (ShowMessageFX.YesNo(null, "Insurance Application Information Saving....", "Are you sure, do you want to save?")) {
                    if (loDateFrom != null && loDateTo != null && loDateFrom.isAfter(loDateTo)) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a valid date from.");
                        return;
                    }

                    if (loDateFrom != null && loDateTo != null && loDateTo.isBefore(loDateFrom)) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a valid date to.");
                        return;
                    }

                    loJSON = oTransInsApplication.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Application Information", (String) loJSON.get("message"));
                        loJSON = oTransInsApplication.openTransaction(oTransInsApplication.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = oTransInsApplication.getEditMode();
                            initFields(pnEditMode);
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                } else {
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransInsApplication = new InsurancePolicyApplication(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Insurance Application Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransInsApplication.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTransInsApplication.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Insurance Application Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Insurance Application");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnPrint":
                try {
                loadInsProposalPrint();
            } catch (SQLException ex) {
                Logger.getLogger(InsuranceApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnInsAppCancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this Insurance Proposal?")) {
                    loJSON = oTransInsApplication.cancelTransaction(oTransInsApplication.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransInsApplication.openTransaction(oTransInsApplication.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = oTransInsApplication.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox03.setItems(cAppType);
        comboBox04.setItems(cPolType);
        comboBox09.setItems(cModeOfPayment);
        comboBox12.setItems(cNewBusType);
        comboBox18.setItems(cVhclTyp);
        comboBox21.setItems(cVhclSize);
        comboBox22.setItems(cBodyType);
        comboBox28.setItems(cActsNtr);
    }

    @Override
    public void initFieldsAction() {
        comboBox09.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox09.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransInsApplication.getMasterModel().getMasterModel().setVhclNew(String.valueOf(comboBox22.getSelectionModel().getSelectedIndex()));
                    initFields(pnEditMode);
                }
            }

        });
        comboBox21.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox21.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransInsApplication.getMasterModel().getMasterModel().setVhclNew(String.valueOf(comboBox22.getSelectionModel().getSelectedIndex()));
                }
            }
        });
        comboBox22.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox22.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransInsApplication.getMasterModel().getMasterModel().setVhclNew(String.valueOf(comboBox22.getSelectionModel().getSelectedIndex()));
                }
            }
        });
        datePicker05.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransInsApplication.getMasterModel().getMasterModel().setValidFrmDte(SQLUtil.toDate(datePicker05.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        datePicker06.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransInsApplication.getMasterModel().getMasterModel().setValidTruDte(SQLUtil.toDate(datePicker06.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
    }
    private Callback<DatePicker, DateCell> DateFrom = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate minDate = CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()));
            setDisable(fbEmpty || foItem.isBefore(minDate));
        }
    };

    private Callback<DatePicker, DateCell> DateTo = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate minDate = datePicker06.getValue();
            setDisable(fbEmpty || foItem.isBefore(minDate));
        }
    };

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        clearPolicyPropsInformation();
                        clearPolicyPropsFields();
                    }
                    initFields(pnEditMode);
                }
            }
        });
        txtField10.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransInsApplication.getMasterModel().getMasterModel().setBrBankID("");
                        oTransInsApplication.getMasterModel().getMasterModel().setBankName("");
                        oTransInsApplication.getMasterModel().getMasterModel().setBrBankNm("");
                    }
                }
            }
        });
        datePicker05.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (pnEditMode == EditMode.ADDNEW) {
                    datePicker06.setDayCellFactory(DateTo);
                    datePicker06.setValue(newValue.plusDays(1));
                }
            }
        });
    }

    @Override
    public void clearTables() {

    }

    private void clearPolicyPropsInformation() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (txtField02.getText().trim().isEmpty()) {
                /*POLICY INFORMATION*/
                oTransInsApplication.getMasterModel().getMasterModel().setPropslNo("");
                oTransInsApplication.getMasterModel().getMasterModel().setPropslDt(null);
                oTransInsApplication.getMasterModel().getMasterModel().setInsTypID("");
                oTransInsApplication.getMasterModel().getMasterModel().setIsNew("");
                oTransInsApplication.getMasterModel().getMasterModel().setBrInsID("");
                oTransInsApplication.getMasterModel().getMasterModel().setInsurNme("");
                oTransInsApplication.getMasterModel().getMasterModel().setBrInsNme("");
                oTransInsApplication.getMasterModel().getMasterModel().setFinType("");
                oTransInsApplication.getMasterModel().getMasterModel().setBrBankID("");
                oTransInsApplication.getMasterModel().getMasterModel().setBankName("");
                oTransInsApplication.getMasterModel().getMasterModel().setBrBankNm("");
                /*Customer INFORMATION*/
                oTransInsApplication.getMasterModel().getMasterModel().setClientID("");
                oTransInsApplication.getMasterModel().getMasterModel().setOwnrNm("");
                oTransInsApplication.getMasterModel().getMasterModel().setAddress("");
                oTransInsApplication.getMasterModel().getMasterModel().setPlateNo("");
                oTransInsApplication.getMasterModel().getMasterModel().setCSNo("");
                oTransInsApplication.getMasterModel().getMasterModel().setEngineNo("");
                oTransInsApplication.getMasterModel().getMasterModel().setFrameNo("");
                oTransInsApplication.getMasterModel().getMasterModel().setVhclFDsc("");
                /*POLICY INFORMATION*/
                oTransInsApplication.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setODTCRate(0.00);
                oTransInsApplication.getMasterModel().getMasterModel().setODTCPrem(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setAONCRate(0.00);
                oTransInsApplication.getMasterModel().getMasterModel().setAONCPayM("");
                oTransInsApplication.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setPAcCPrem(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(0.00));
                oTransInsApplication.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal(0.00));
            }
        }
    }

    private void clearPolicyPropsFields() {
        CustomCommonUtil.setText("0.00", txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        CustomCommonUtil.setText("", txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
                txtField19, txtField20, txtField43, txtField45);
        CustomCommonUtil.setText("", textArea14);
        List<DatePicker> loDatePicker = Arrays.asList(
                datePicker05, datePicker06, datePicker11);
        loDatePicker.forEach(dp -> dp.setValue(LocalDate.of(1900, Month.JANUARY, 1)));
        CustomCommonUtil.setValue(null, comboBox03, comboBox04, comboBox09, comboBox12, comboBox18, comboBox21, comboBox22, comboBox28);
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("0.00", txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        CustomCommonUtil.setText("", txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
                txtField19, txtField20, txtField43, txtField45);
        CustomCommonUtil.setText("", textArea14, textArea44, textArea46);
        List<DatePicker> loDatePicker = Arrays.asList(
                datePicker05, datePicker06, datePicker11);
        loDatePicker.forEach(dp -> dp.setValue(LocalDate.of(1900, Month.JANUARY, 1)));
        CustomCommonUtil.setValue(null, comboBox03, comboBox04, comboBox09, comboBox12, comboBox18, comboBox21, comboBox22, comboBox28);
        CustomCommonUtil.setText("", lblPolicyNo, lblPrintDate, lblStatus);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        CustomCommonUtil.setDisable(true, datePicker05, datePicker06,
                comboBox09, txtField10,
                txtField17, comboBox21,
                comboBox22, txtField41);
        CustomCommonUtil.setDisable(!lbShow, txtField01, textArea44);
        CustomCommonUtil.setDisable(!(lbShow && !txtField01.getText().isEmpty()),
                datePicker05, datePicker06, txtField08);

        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnInsAppCancel, btnPayment);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnInsAppCancel, btnPayment);
        if (lbShow) {
            if (oTransInsApplication.getMasterModel().getMasterModel().getTotalAmt() != null) {
                if (!oTransInsApplication.getMasterModel().getMasterModel().getTotalAmt().equals("0.00")) {
                    txtField41.setDisable(!lbShow);
                }
            }
            if (oTransInsApplication.getMasterModel().getMasterModel().getVSPTrnNo().isEmpty()) {
                comboBox09.setDisable(!lbShow);
            }
            switch (comboBox09.getSelectionModel().getSelectedIndex()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    if (oTransInsApplication.getMasterModel().getMasterModel().getVSPTrnNo() != null) {
                        if (!oTransInsApplication.getMasterModel().getMasterModel().getVSPTrnNo().isEmpty()) {
                            txtField10.setDisable(!lbShow);
                        }
                    }
                    break;
            }
        }
        if (fnValue == EditMode.READY) {
            if (!lblStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnPrint, btnInsAppCancel);
                CustomCommonUtil.setManaged(true, btnEdit, btnPrint, btnInsAppCancel);
            }
            if (oTransInsApplication.getMasterModel().getMasterModel().getTransNo() != null
                    && !oTransInsApplication.getMasterModel().getMasterModel().getTransNo().isEmpty()) {
                btnPayment.setVisible(true);
                btnPayment.setManaged(true);
            }
        }

        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
    }

    @Override
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransInsApplication.computeAmount();
        /* POLICY INFORMATION */
        txtField01.setText(oTransInsApplication.getMasterModel().getMasterModel().getPropslNo());
        txtField02.setText(oTransInsApplication.getMasterModel().getMasterModel().getTransNo());
        int lnAppType = -1;
        if (oTransInsApplication.getMasterModel().getMasterModel().getIsNew() != null) {
            switch (oTransInsApplication.getMasterModel().getMasterModel().getIsNew()) {
                case "y":
                    lnAppType = 0;
                    break;
                case "n":
                    lnAppType = 1;
                    break;
            }
        }
        comboBox03.getSelectionModel().select(lnAppType);
        int policeType = -1;
        if (oTransInsApplication.getMasterModel().getMasterModel().getInsTypID() != null) {
            switch (oTransInsApplication.getMasterModel().getMasterModel().getInsTypID()) {
                case "y":
                    policeType = 0;
                    break;
                case "c":
                    policeType = 1;
                    break;
                case "b":
                    policeType = 2;
                    break;
            }
        }
        comboBox04.getSelectionModel().select(policeType);
        if (oTransInsApplication.getMasterModel().getMasterModel().getValidFrmDte() != null) {
            datePicker05.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransInsApplication.getMasterModel().getMasterModel().getValidFrmDte())));
        }
        if (oTransInsApplication.getMasterModel().getMasterModel().getValidTruDte() != null) {
            datePicker06.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransInsApplication.getMasterModel().getMasterModel().getValidTruDte())));
        }
        String lsInsBranc = "";
        if (oTransInsApplication.getMasterModel().getMasterModel().getInsurNme() != null && oTransInsApplication.getMasterModel().getMasterModel().getBrInsNme() != null) {
            if (!oTransInsApplication.getMasterModel().getMasterModel().getInsurNme().isEmpty() && !oTransInsApplication.getMasterModel().getMasterModel().getBrInsNme().isEmpty()) {
                lsInsBranc = oTransInsApplication.getMasterModel().getMasterModel().getInsurNme() + " " + oTransInsApplication.getMasterModel().getMasterModel().getBrInsNme();
            }
        }
        txtField07.setText(lsInsBranc);
        txtField08.setText(oTransInsApplication.getMasterModel().getMasterModel().getEmpName());
        if (oTransInsApplication.getMasterModel().getMasterModel().getFinType() != null && !oTransInsApplication.getMasterModel().getMasterModel().getFinType().trim().isEmpty()) {
            comboBox09.getSelectionModel().select(Integer.parseInt(oTransInsApplication.getMasterModel().getMasterModel().getFinType()));
        }
        String lsBrBank = "";
        if (oTransInsApplication.getMasterModel().getMasterModel().getBankName() != null && oTransInsApplication.getMasterModel().getMasterModel().getBrBankNm() != null) {
            if (!oTransInsApplication.getMasterModel().getMasterModel().getBankName().isEmpty() && !oTransInsApplication.getMasterModel().getMasterModel().getBrBankNm().isEmpty()) {
                lsBrBank = oTransInsApplication.getMasterModel().getMasterModel().getBankName() + " " + oTransInsApplication.getMasterModel().getMasterModel().getBrBankNm();
            }
        }
        txtField10.setText(lsBrBank);
        if (oTransInsApplication.getMasterModel().getMasterModel().getPropslDt() != null) {
            datePicker11.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransInsApplication.getMasterModel().getMasterModel().getPropslDt())));
        }
        int lnNewBus = -1;
        if (oTransInsApplication.getMasterModel().getMasterModel().getIsNew() != null) {
            switch (oTransInsApplication.getMasterModel().getMasterModel().getIsNew()) {
                case "y":
                    lnNewBus = 0;
                    break;
                case "n":
                    lnNewBus = 1;
                    break;
            }
        }
        comboBox12.getSelectionModel().select(lnNewBus);
        txtField13.setText(oTransInsApplication.getMasterModel().getMasterModel().getOwnrNm());
        txtField13.setText(oTransInsApplication.getMasterModel().getMasterModel().getOwnrNm());
        textArea14.setText(oTransInsApplication.getMasterModel().getMasterModel().getAddress());
        txtField15.setText(oTransInsApplication.getMasterModel().getMasterModel().getCSNo());
        txtField16.setText(oTransInsApplication.getMasterModel().getMasterModel().getPlateNo());
        txtField19.setText(oTransInsApplication.getMasterModel().getMasterModel().getEngineNo());
        txtField20.setText(oTransInsApplication.getMasterModel().getMasterModel().getFrameNo());
        txtField16.setText(oTransInsApplication.getMasterModel().getMasterModel().getVhclFDsc());
        txtField17.setText(String.valueOf("0"));
        if (oTransInsApplication.getMasterModel().getMasterModel().getVhclNew() != null && !oTransInsApplication.getMasterModel().getMasterModel().getVhclNew().trim().isEmpty()) {
            comboBox18.getSelectionModel().select(Integer.parseInt(oTransInsApplication.getMasterModel().getMasterModel().getVhclNew()));
        }
        if (oTransInsApplication.getMasterModel().getMasterModel().getVhclNew() != null && !oTransInsApplication.getMasterModel().getMasterModel().getVhclNew().trim().isEmpty()) {
            comboBox22.getSelectionModel().select(Integer.parseInt(oTransInsApplication.getMasterModel().getMasterModel().getVhclNew()));
        }

        if (oTransInsApplication.getMasterModel().getMasterModel().getVhclNew() != null && !oTransInsApplication.getMasterModel().getMasterModel().getVhclNew().trim().isEmpty()) {
            comboBox22.getSelectionModel().select(Integer.parseInt(oTransInsApplication.getMasterModel().getMasterModel().getVhclNew()));
        }

        txtField23.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getODTCAmt()))));
        txtField24.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getODTCRate()))));
        txtField26.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getAONCAmt()))));
        txtField27.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getAONCRate()))));
        txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getBdyCAmt()))));
        txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getPrDCAmt()))));
        txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getPAcCAmt()))));
        txtField36.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getTPLAmt()))));
        int actNtr = -1;
        if (oTransInsApplication.getMasterModel().getMasterModel().getAONCPayM() != null) {
            switch (oTransInsApplication.getMasterModel().getMasterModel().getAONCPayM()) {
                case "cha":
                    actNtr = 0;
                    break;
                case "foc":
                    actNtr = 1;
                    break;
                case "na":
                    actNtr = 2;
                    break;
            }
        }
        comboBox28.getSelectionModel().select(actNtr);
        txtField25.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getODTCPrem()))));
        txtField29.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getAONCPrem()))));
        txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getBdyCPrem()))));
        txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getPrDCPrem()))));
        txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getPAcCPrem()))));
        txtField37.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getTPLPrem()))));
        txtField38.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getTaxRate()))));
        txtField39.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getTaxAmt()))));
        txtField40.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsApplication.getMasterModel().getMasterModel().getTotalAmt()))));

        txtField43.setText("");
        textArea44.setText("");
        txtField45.setText("");
        textArea46.setText("");
        if (oTransInsApplication.getMasterModel().getMasterModel().getTranStat() != null) {
            switch (oTransInsApplication.getMasterModel().getMasterModel().getTranStat()) {
                case TransactionStatus.STATE_OPEN:
                    lblStatus.setText("Active");
                    break;
                case TransactionStatus.STATE_CLOSED:
                    lblStatus.setText("Approved");
                    break;
                case TransactionStatus.STATE_CANCELLED:
                    lblStatus.setText("Cancelled");
                    break;
                case TransactionStatus.STATE_POSTED:
                    lblStatus.setText("Posted");
                    break;
                default:
                    lblStatus.setText("");
                    break;
            }
        }
        lblPolicyNo.setText("");
        lblPrintDate.setText("");
        return true;
    }

    private void loadInsProposalPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/insurance/InsuranceApplicationPrint.fxml"));
            InsuranceApplicationPrintController loControl = new InsuranceApplicationPrintController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransInsApplication);
            loControl.setTransNo(oTransInsApplication.getMasterModel().getMasterModel().getTransNo());
            fxmlLoader.setController(loControl);
            //load the main interface
            Parent parent = fxmlLoader.load();
            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }
}
