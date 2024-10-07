package org.guanzon.autoapp.controllers.insurance;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.insurance.InsurancePolicy;
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
public class InsurancePolicyController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private InsurancePolicy oTrans;
    private String pxeModuleName = "Insurance Policy";
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cAppType = FXCollections.observableArrayList("NEW", "RENEW");
    ObservableList<String> cNewBusType = FXCollections.observableArrayList("YES", "NO");
    ObservableList<String> cPolType = FXCollections.observableArrayList("TPL", "COMPREHENSIVE", "TPL AND COMPREHENSIVE");
    ObservableList<String> cVhclTyp = FXCollections.observableArrayList("COMMERCIAL", "PRIVATE");
    ObservableList<String> cBodyType = FXCollections.observableArrayList("SEDAN", "SUV", "HATCHBACK", "MPV", "MOTORCYCLE", "TRUCK");
    ObservableList<String> cVhclSize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnClose, btnPrintCOC, btnInsPolCancel, btnPayHistory, btnCustomer, btnInsComp;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
            txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField30, txtField31,
            txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38, txtField39, txtField40, txtField41,
            txtField42, txtField43, txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51,
            txtField52, txtField53, txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60;
    @FXML
    private DatePicker datePicker04, datePicker05, datePicker12, datePicker13;
    @FXML
    private ComboBox<String> comboBox06, comboBox09, comboBox14, comboBox25, comboBox28, comboBox29, comboBox44;
    @FXML
    private TextArea textArea16, textArea18, textArea20;
    @FXML
    private Label lblInsPolicyStatus;
    @FXML
    private Tab mainTab;
    @FXML
    private Tab PremAddTab;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InsurancePolicy(oApp, false, oApp.getBranchCode());
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41,
                txtField42
        );
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        CustomCommonUtil.setCapsLockBehavior(textArea16);
        CustomCommonUtil.setCapsLockBehavior(textArea18);
        CustomCommonUtil.setCapsLockBehavior(textArea20);
    }

    @Override
    public void initPatternFields() {

    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41,
                txtField42);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea16);
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
                case 22:
                    break;
                case 23:
                    break;
            }
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
                case 16:
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41,
                txtField42);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        List<TextArea> loTxtArea = Arrays.asList(textArea16);
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
                    loJSON = oTrans.searchPolicyApplication(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnClose, btnPrintCOC, btnInsPolCancel,
                btnPayHistory, btnCustomer, btnInsComp);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new InsurancePolicy(oApp, false, oApp.getBranchCode());
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
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Insurance Policy Information Saving....", "Are you sure, do you want to save?")) {
                } else {
                    return;
                }
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Insurance Policy Information", (String) loJSON.get("message"));
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTrans = new InsurancePolicy(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Insurance Policy Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                    ShowMessageFX.Warning(null, "Search Insurance Policy Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Insurance Policy");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnPrint":
                break;
            case "btnPrintCOC":
                break;
            case "btnInsPolCancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this Insurance Policy?")) {
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Policy Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Policy Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnPayHistory":
                break;
            case "btnCustomer":
                break;
            case "btnInsComp":
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox06.setItems(cPolType);
        comboBox09.setItems(cAppType);
        comboBox14.setItems(cNewBusType);
        comboBox25.setItems(cVhclTyp);
        comboBox28.setItems(cVhclSize);
        comboBox29.setItems(cBodyType);
    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        clearPolicyAppInformation();
                        clearPolicyAppFields();
                    }
                    initFields(pnEditMode);
                }
            }
        });
    }

    @Override
    public void clearTables() {

    }

    private void clearPolicyAppInformation() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (txtField02.getText().trim().isEmpty()) {
                /*POLICY INFORMATION*/
                oTrans.getMasterModel().getMasterModel().setApplicNo("");
                oTrans.getMasterModel().getMasterModel().setApplicDte(null);
                oTrans.getMasterModel().getMasterModel().setInsTypID("");
                oTrans.getMasterModel().getMasterModel().setIsNew("");
                oTrans.getMasterModel().getMasterModel().setBrInsID("");
                oTrans.getMasterModel().getMasterModel().setInsurNme("");
                oTrans.getMasterModel().getMasterModel().setBrInsNme("");
                /*Customer INFORMATION*/
                oTrans.getMasterModel().getMasterModel().setClientID("");
                oTrans.getMasterModel().getMasterModel().setOwnrNm("");
                oTrans.getMasterModel().getMasterModel().setAddress("");
                oTrans.getMasterModel().getMasterModel().setPlateNo("");
                oTrans.getMasterModel().getMasterModel().setCSNo("");
                oTrans.getMasterModel().getMasterModel().setEngineNo("");
                oTrans.getMasterModel().getMasterModel().setFrameNo("");
                oTrans.getMasterModel().getMasterModel().setVhclFDsc("");
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

    private void clearPolicyAppFields() {
        CustomCommonUtil.setText("0.00", txtField30, txtField31,
                txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38, txtField39, txtField40,
                txtField43, txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51,
                txtField52, txtField53, txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60);
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41, txtField42);
        CustomCommonUtil.setText("", textArea20);
        List<DatePicker> loDatePicker = Arrays.asList(
                datePicker05, datePicker13);
        loDatePicker.forEach(dp -> dp.setValue(LocalDate.of(1900, Month.JANUARY, 1)));
        CustomCommonUtil.setValue(null, comboBox06, comboBox09, comboBox14, comboBox25, comboBox28, comboBox29);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clearFields() {
        CustomCommonUtil.setText("0.00", txtField30, txtField31,
                txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38, txtField39, txtField40,
                txtField43, txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51,
                txtField52, txtField53, txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60);
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41, txtField42);
        CustomCommonUtil.setText("", textArea16, textArea18, textArea20);
        CustomCommonUtil.setValue(null, comboBox06, comboBox09, comboBox14, comboBox25, comboBox28, comboBox29);
        List<DatePicker> loDatePicker = Arrays.asList(datePicker04, datePicker05, datePicker12, datePicker13);
        loDatePicker.forEach(dp -> dp.setValue(LocalDate.of(1900, Month.JANUARY, 1)));
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true,
                txtField30, txtField31,
                txtField32, txtField33,
                txtField34, txtField35,
                txtField36, txtField37,
                txtField38, txtField39,
                txtField40, txtField41,
                txtField42, txtField43,
                txtField30, txtField31,
                txtField32, txtField33,
                txtField34, txtField35,
                txtField36, txtField37,
                txtField38, txtField39,
                txtField40, txtField41,
                txtField42, txtField43
        );
        CustomCommonUtil.setDisable(!lbShow, txtField01);
        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnPrintCOC, btnInsPolCancel, btnPayHistory);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnPrintCOC, btnInsPolCancel, btnPayHistory);

        if (fnValue == EditMode.READY) {
            if (!lblInsPolicyStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnPrint, btnPrintCOC, btnInsPolCancel);
                CustomCommonUtil.setManaged(true, btnEdit, btnPrint, btnPrintCOC, btnInsPolCancel);
            }
            if (oTrans.getMasterModel().getMasterModel().getTransNo() != null
                    && !oTrans.getMasterModel().getMasterModel().getTransNo().isEmpty()) {
                btnPayHistory.setVisible(true);
                btnPayHistory.setManaged(true);
            }
        }

        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
    }

    @Override
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.computeAmount();
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getApplicNo());
        txtField02.setText(oTrans.getMasterModel().getMasterModel().getPolicyNo());
        txtField03.setText("");
        if (oTrans.getMasterModel().getMasterModel().getApplicDte() != null) {
            datePicker04.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getApplicDte())));
        }
        if (oTrans.getMasterModel().getMasterModel().getValidFrmDte() != null) {
            datePicker05.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getValidFrmDte())));
        }

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
        comboBox06.getSelectionModel().select(lnAppType);
        String lsInsBranc = "";
        if (oTrans.getMasterModel().getMasterModel().getInsurNme() != null && oTrans.getMasterModel().getMasterModel().getBrInsNme() != null) {
            if (!oTrans.getMasterModel().getMasterModel().getInsurNme().isEmpty() && !oTrans.getMasterModel().getMasterModel().getBrInsNme().isEmpty()) {
                lsInsBranc = oTrans.getMasterModel().getMasterModel().getInsurNme() + " " + oTrans.getMasterModel().getMasterModel().getBrInsNme();
            }
        }
        txtField07.setText(lsInsBranc);
        txtField08.setText(oTrans.getMasterModel().getMasterModel().getEmpName());
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
        comboBox09.getSelectionModel().select(policeType);
        txtField10.setText("");
        txtField11.setText("");
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker12.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTransactDte())));
        }
        if (oTrans.getMasterModel().getMasterModel().getValidTruDte() != null) {
            datePicker13.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getValidTruDte())));
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
        comboBox14.getSelectionModel().select(lnNewBus);
        txtField15.setText("");
        textArea16.setText("");
        txtField17.setText("");
        textArea18.setText("");
        txtField19.setText(oTrans.getMasterModel().getMasterModel().getOwnrNm());
        textArea20.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField21.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
        txtField22.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
        txtField23.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
        txtField24.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
        if (oTrans.getMasterModel().getMasterModel().getUnitType() != null && !oTrans.getMasterModel().getMasterModel().getUnitType().trim().isEmpty()) {
            comboBox25.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getUnitType()));
        }
        txtField26.setText("");
        txtField27.setText("");
        if (oTrans.getMasterModel().getMasterModel().getVhclSize() != null && !oTrans.getMasterModel().getMasterModel().getVhclSize().trim().isEmpty()) {
            comboBox28.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getVhclSize()));
        }

        if (oTrans.getMasterModel().getMasterModel().getBodyType() != null && !oTrans.getMasterModel().getMasterModel().getBodyType().trim().isEmpty()) {
            comboBox29.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getBodyType()));
        }
        txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCAmt()))));
        txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCRate()))));
        txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCAmt()))));
        txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCRate()))));
        txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCAmt()))));
        txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCAmt()))));
        txtField36.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCAmt()))));
        txtField37.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLAmt()))));
        txtField38.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDocRate()))));
        txtField39.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getVATRate()))));
        txtField40.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getLGUTaxRt()))));
        txtField41.setText("0.00");
        txtField42.setText("0.00");
        txtField43.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCPrem()))));
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
        comboBox44.getSelectionModel().select(actNtr);
        txtField45.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCPrem()))));
        txtField46.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCPrem()))));
        txtField47.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCPrem()))));
        txtField48.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCPrem()))));
        txtField49.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLPrem()))));
        txtField50.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getGrossAmt()))));
        txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDocAmt()))));
        txtField52.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getVATAmt()))));
        txtField53.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getLGUTaxAm()))));
        txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAuthFee()))));
        txtField55.setText("0.00");
        txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDiscAmt()))));
        txtField57.setText("");
        txtField58.setText("0.00");
        txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getCommissn()))));
        txtField60.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPayAmt()))));
        if (oTrans.getMasterModel().getMasterModel().getTranStat() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
                case TransactionStatus.STATE_OPEN:
                    lblInsPolicyStatus.setText("For Approval");
                    break;
                case TransactionStatus.STATE_CLOSED:
                    lblInsPolicyStatus.setText("Approved");
                    break;
                case TransactionStatus.STATE_CANCELLED:
                    lblInsPolicyStatus.setText("Cancelled");
                    break;
                case TransactionStatus.STATE_POSTED:
                    lblInsPolicyStatus.setText("Posted");
                    break;
                default:
                    lblInsPolicyStatus.setText("");
                    break;
            }
        }
        return true;
    }
}
