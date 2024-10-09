package org.guanzon.autoapp.controllers.insurance;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InsuranceApplicationController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private InsurancePolicyApplication oTrans;
    private String pxeModuleName = "Insurance Application";
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
            txtField39, txtField40, txtField41, txtField42, txtField43, txtField45, txtField47;
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
        oTrans = new InsurancePolicyApplication(oApp, false, oApp.getBranchCode());
        datePicker05.setDayCellFactory(DateFrom);
        initCapitalizationFields();
        initPatternFields();
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
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.computeAmount();
        /* POLICY INFORMATION */
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getPropslNo());
        txtField02.setText(oTrans.getMasterModel().getMasterModel().getTransNo());
        int lnAppType = -1;
        if (oTrans.getMasterModel().getMasterModel().getIsNew() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getIsNew()) {
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
        if (oTrans.getMasterModel().getMasterModel().getInsTypID() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getInsTypID()) {
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
        if (oTrans.getMasterModel().getMasterModel().getValidFrmDte() != null) {
            datePicker05.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getValidFrmDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        if (oTrans.getMasterModel().getMasterModel().getValidTruDte() != null) {
            datePicker06.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getValidTruDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        String lsInsBranc = "";
        if (oTrans.getMasterModel().getMasterModel().getInsurNme() != null && oTrans.getMasterModel().getMasterModel().getBrInsNme() != null) {
            lsInsBranc = oTrans.getMasterModel().getMasterModel().getInsurNme() + " " + oTrans.getMasterModel().getMasterModel().getBrInsNme();
        }
        txtField07.setText(lsInsBranc.trim());
        txtField08.setText(oTrans.getMasterModel().getMasterModel().getEmpName());
        if (oTrans.getMasterModel().getMasterModel().getFinType() != null && !oTrans.getMasterModel().getMasterModel().getFinType().trim().isEmpty()) {
            comboBox09.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getFinType()));
        }
        String lsBrBank = "";
        if (oTrans.getMasterModel().getMasterModel().getBankName() != null && oTrans.getMasterModel().getMasterModel().getBrBankNm() != null) {
            lsBrBank = oTrans.getMasterModel().getMasterModel().getBankName() + " " + oTrans.getMasterModel().getMasterModel().getBrBankNm();
        }
        txtField10.setText(lsBrBank.trim());
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null && !String.valueOf(oTrans.getMasterModel().getMasterModel().getTransactDte()).isEmpty()) {
            datePicker11.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getTransactDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        int lnNewBus = -1;
        if (oTrans.getMasterModel().getMasterModel().getIsNew() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getIsNew()) {
                case "y":
                    lnNewBus = 0;
                    break;
                case "n":
                    lnNewBus = 1;
                    break;
            }
        }
        comboBox12.getSelectionModel().select(lnNewBus);
        txtField13.setText(oTrans.getMasterModel().getMasterModel().getOwnrNm());
        textArea14.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField15.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
        txtField16.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
        txtField47.setText(oTrans.getMasterModel().getMasterModel().getVhclFDsc());
        txtField17.setText(String.valueOf("0"));
        if (oTrans.getMasterModel().getMasterModel().getUnitType() != null && !oTrans.getMasterModel().getMasterModel().getUnitType().trim().isEmpty()) {
            comboBox18.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getUnitType()));
        }
        txtField19.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
        txtField20.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());

        if (oTrans.getMasterModel().getMasterModel().getVhclSize() != null && !oTrans.getMasterModel().getMasterModel().getVhclSize().trim().isEmpty()) {
            comboBox21.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getVhclSize()));
        }

        if (oTrans.getMasterModel().getMasterModel().getBodyType() != null && !oTrans.getMasterModel().getMasterModel().getBodyType().trim().isEmpty()) {
            comboBox22.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getBodyType()));
        }

        txtField23.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCAmt()))));
        txtField24.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCRate()))));
        txtField26.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCAmt()))));
        txtField27.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCRate()))));
        txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCAmt()))));
        txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCAmt()))));
        txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCAmt()))));
        txtField36.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLAmt()))));
        int actNtr = -1;
        if (oTrans.getMasterModel().getMasterModel().getAONCPayM() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getAONCPayM()) {
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
        txtField25.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCPrem()))));
        txtField29.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCPrem()))));
        txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCPrem()))));
        txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCPrem()))));
        txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCPrem()))));
        txtField37.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLPrem()))));
        txtField38.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTaxRate()))));
        txtField39.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTaxAmt()))));
        txtField40.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTotalAmt()))));

        txtField43.setText("");
        textArea44.setText("");
        txtField45.setText("");
        textArea46.setText("");
        if (oTrans.getMasterModel().getMasterModel().getTranStat() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
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
        String lsPolicyNo = "";
        if (oTrans.getMasterModel().getMasterModel().getPolicyNo() != null && !oTrans.getMasterModel().getMasterModel().getPolicyNo().isEmpty()) {
            lsPolicyNo = oTrans.getMasterModel().getMasterModel().getPolicyNo();
        }
        lblPolicyNo.setText(lsPolicyNo);
        lblPrintDate.setText("");
        return true;
    }

    @Override
    public void initPatternFields() {
        List<TextField> loTxtField = Arrays.asList(txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        Pattern loDecOnly = Pattern.compile("[0-9,.]*");
        Pattern loNumOnly = Pattern.compile("[0-9]*");
        loTxtField.forEach(tf -> tf.setTextFormatter(new TextFormatterUtil(loDecOnly)));
        txtField17.setTextFormatter(new TextFormatterUtil(loNumOnly));
    }

    @Override
    public void initLimiterFields() {
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
//                    oTrans.getMasterModel().getMasterModel().setAuhorize(lnAutCapacity);
//                    txtField17.setText( oTrans.getMasterModel().getMasterModel().getAuhorize();
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
////                    oTrans.getMasterModel().getMasterModel().setAuhorize(lnAutCapacity);
////                    txtField17.setText( oTrans.getMasterModel().getMasterModel().getAuhorize();
//                    oTrans.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(lsValue.replace(",", "")));
//                    if (!loadMasterFields()) {
//                        txtField41.setText("0.00");
//                        oTrans.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(txtField41.getText().replace(",", "")));
//                    }
//                    txtField41.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCAmt()))));
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

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
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
                    loJSON = oTrans.searchProposal(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        System.out.println("bank name: " + oTrans.getMasterModel().getMasterModel().getBankName());
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField08":
                    loJSON = oTrans.searchInsuranceCoordinator(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {;
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    break;
                case "txtField10":
                    loJSON = oTrans.searchbank(lsValue.trim());
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

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPayment, btnPrint, btnInsAppCancel, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new InsurancePolicyApplication(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
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

                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Application Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = oTrans.getEditMode();
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
                    oTrans = new InsurancePolicyApplication(oApp, false, oApp.getBranchCode());
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
                loJSON = oTrans.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
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
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnPayment":
                ShowMessageFX.Warning(null, pxeModuleName, "Button payment history is underdevelopment");
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
        comboBox09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox09.getSelectionModel().getSelectedIndex() >= 0) {
                    if (oTrans.getMasterModel().getMasterModel().getVSPTrnNo().isEmpty()) {
                        switch (comboBox09.getSelectionModel().getSelectedIndex()) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                oTrans.getMasterModel().getMasterModel().setBrBankID("");
                                oTrans.getMasterModel().getMasterModel().setBrBankNm("");
                                oTrans.getMasterModel().getMasterModel().setBankName("");
                                txtField10.setText("");
                                break;
                        }
                    }
                    oTrans.getMasterModel().getMasterModel().setFinType(String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
                    initFields(pnEditMode);
                }
            }
        });
        datePicker05.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setValidFrmDte(SQLUtil.toDate(datePicker05.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        datePicker06.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setValidTruDte(SQLUtil.toDate(datePicker06.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
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
                        oTrans.getMasterModel().getMasterModel().setBrBankID("");
                        oTrans.getMasterModel().getMasterModel().setBankName("");
                        oTrans.getMasterModel().getMasterModel().setBrBankNm("");
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
                oTrans.getMasterModel().getMasterModel().setPropslNo("");
                oTrans.getMasterModel().getMasterModel().setPropslDt(null);
                oTrans.getMasterModel().getMasterModel().setInsTypID("");
                oTrans.getMasterModel().getMasterModel().setIsNew("");
                oTrans.getMasterModel().getMasterModel().setBrInsID("");
                oTrans.getMasterModel().getMasterModel().setInsurNme("");
                oTrans.getMasterModel().getMasterModel().setBrInsNme("");
                oTrans.getMasterModel().getMasterModel().setFinType("");
                oTrans.getMasterModel().getMasterModel().setBrBankID("");
                oTrans.getMasterModel().getMasterModel().setBankName("");
                oTrans.getMasterModel().getMasterModel().setBrBankNm("");
                /*Customer INFORMATION*/
                oTrans.getMasterModel().getMasterModel().setClientID("");
                oTrans.getMasterModel().getMasterModel().setOwnrNm("");
                oTrans.getMasterModel().getMasterModel().setAddress("");
                oTrans.getMasterModel().getMasterModel().setPlateNo("");
                oTrans.getMasterModel().getMasterModel().setCSNo("");
                oTrans.getMasterModel().getMasterModel().setEngineNo("");
                oTrans.getMasterModel().getMasterModel().setFrameNo("");
                oTrans.getMasterModel().getMasterModel().setVhclFDsc("");
                oTrans.getMasterModel().getMasterModel().setUnitType("");
                oTrans.getMasterModel().getMasterModel().setVhclSize("");
                oTrans.getMasterModel().getMasterModel().setBodyType("");

                /*POLICY INFORMATION*/
                oTrans.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setODTCRate(0.00);
                oTrans.getMasterModel().getMasterModel().setODTCPrem(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setAONCRate(0.00);
                oTrans.getMasterModel().getMasterModel().setAONCPayM("");
                oTrans.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setPAcCPrem(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(0.00));
                oTrans.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal(0.00));
            }
        }
    }

    private void clearPolicyPropsFields() {
        CustomCommonUtil.setText("0.00", txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        CustomCommonUtil.setText("", txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
                txtField19, txtField20, txtField43, txtField45, txtField47);
        CustomCommonUtil.setText("", textArea14);
        List<DatePicker> loDatePicker = Arrays.asList(
                datePicker05, datePicker06, datePicker11);
        loDatePicker.forEach(dp -> dp.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()))));
        CustomCommonUtil.setValue(null, comboBox03, comboBox04, comboBox09, comboBox12, comboBox18, comboBox21, comboBox22, comboBox28);
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("0.00", txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        txtField17.setText("0");
        CustomCommonUtil.setText("", txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16,
                txtField19, txtField20, txtField43, txtField45, txtField47);
        CustomCommonUtil.setText("", textArea14, textArea44, textArea46);
        List<DatePicker> loDatePicker = Arrays.asList(
                datePicker05, datePicker06, datePicker11);
        loDatePicker.forEach(dp -> dp.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()))));
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
//            if (oTrans.getMasterModel().getMasterModel().getTotalAmt() != null) {
//                if (!oTrans.getMasterModel().getMasterModel().getTotalAmt().equals("0.00")) {
//                    txtField41.setDisable(!lbShow);
//                }
//            }
            if (oTrans.getMasterModel().getMasterModel().getVSPTrnNo().isEmpty()) {
                comboBox09.setDisable(!lbShow);
                switch (comboBox09.getSelectionModel().getSelectedIndex()) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        txtField10.setDisable(!lbShow);
                        break;
                }
            }
        }
        if (fnValue == EditMode.READY) {
            if (!lblStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnPrint, btnInsAppCancel);
                CustomCommonUtil.setManaged(true, btnEdit, btnPrint, btnInsAppCancel);
            }
            if (oTrans.getMasterModel().getMasterModel().getTransNo() != null
                    && !oTrans.getMasterModel().getMasterModel().getTransNo().isEmpty()) {
                btnPayment.setVisible(true);
                btnPayment.setManaged(true);
            }
        }

        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
    }

    private void loadInsProposalPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/insurance/InsuranceApplicationPrint.fxml"));
            InsuranceApplicationPrintController loControl = new InsuranceApplicationPrintController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setTransNo(oTrans.getMasterModel().getMasterModel().getTransNo());
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
