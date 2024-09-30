package org.guanzon.autoapp.controllers.insurance;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.insurance.InsurancePolicyProposal;
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
public class InsuranceProposalController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private InsurancePolicyProposal oTransInsProposal;
    private String pxeModuleName = "Insurance Proposal";
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private String lsClientSource = "";
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cClntSrc = FXCollections.observableArrayList("GENERAL CUSTOMER", "VSP");
    ObservableList<String> cAppType = FXCollections.observableArrayList("NEW", "RENEW");
    ObservableList<String> cVhclTyp = FXCollections.observableArrayList("COMMERCIAL", "PRIVATE");
    ObservableList<String> cPolType = FXCollections.observableArrayList("TPL", "COMPREHENSIVE", "TPL AND COMPREHENSIVE");
    ObservableList<String> cActsNtr = FXCollections.observableArrayList("CHARGE", "FREE");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnIPCancel, btnClose;
    @FXML
    private Label lblApprovalStatus, lblApprovalNo, lblIPStatus, lblIPNo, lblIPNoValue;
    @FXML
    private TextArea textArea03, textArea13, textArea15;
    @FXML
    private TextField txtField02, txtField04, txtField05, txtField06, txtField07, txtField08, txtField09, txtField12, txtField14, txtField16, txtField19, txtField20, txtField21,
            txtField22, txtField23, txtField24, txtField25, txtField26, txtField28, txtField29, txtField30, txtField31, txtField32, txtField33, txtField34,
            txtField35, txtField36, txtField37;
    @FXML
    private ComboBox<String> comboBox01, comboBox10, comboBox18, comboBox17, comboBox27;
    @FXML
    private DatePicker datePicker11;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransInsProposal = new InsurancePolicyProposal(oApp, false, oApp.getBranchCode());
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
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField04, txtField05, txtField06, txtField07,
                txtField08, txtField12, txtField14, txtField16
        );
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        CustomCommonUtil.setCapsLockBehavior(textArea03);
        CustomCommonUtil.setCapsLockBehavior(textArea13);
        CustomCommonUtil.setCapsLockBehavior(textArea15);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField19, txtField20, txtField21,
                txtField22, txtField23, txtField24, txtField25, txtField26, txtField28,
                txtField29, txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36
        );
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea13);
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
                case 19:
                    break;
                case 20:
                    break;
                case 21:
                    break;
                case 22:
                    break;
                case 23:
                    break;
                case 24:
                    break;
                case 25:
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    break;
                case 29:
                    break;
                case 30:
                    break;
                case 31:
                    break;
                case 32:
                    break;
                case 33:
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
                case 13:
                    break;

            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField02, txtField04, txtField05, txtField06, txtField07, txtField08, txtField09, txtField12, txtField14, txtField16, txtField19, txtField20, txtField21,
                txtField22, txtField23, txtField24, txtField25, txtField26, txtField28, txtField29, txtField30, txtField31, txtField32, txtField33, txtField34
        );
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        List<TextArea> loTxtArea = Arrays.asList(textArea03, textArea13, textArea15);
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
                case "txtField02":
                    loJSON = oTransInsProposal.searchVSP(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    break;
                case "txtField16":
//                    loJSON = oTransInsProposal.searchInsurance(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnIPCancel, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTransInsProposal = new InsurancePolicyProposal(oApp, false, oApp.getBranchCode());
                loJSON = oTransInsProposal.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTransInsProposal.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransInsProposal.updateTransaction();
                pnEditMode = oTransInsProposal.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Insurance Proposal Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTransInsProposal.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                        loJSON = oTransInsProposal.openTransaction(oTransInsProposal.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = oTransInsProposal.getEditMode();
                            initFields(pnEditMode);
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransInsProposal = new InsurancePolicyProposal(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Insurance Proposal Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransInsProposal.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    pnEditMode = oTransInsProposal.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Insurance Proposal Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Insurance Proposal");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnPrint":
                break;
            case "btnIPCancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this Insurance Proposal?")) {
                    loJSON = oTransInsProposal.cancelTransaction(oTransInsProposal.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Proposal Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransInsProposal.openTransaction(oTransInsProposal.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = oTransInsProposal.getEditMode();
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
        comboBox01.setItems(cClntSrc);
        comboBox10.setItems(cVhclTyp);
        comboBox17.setItems(cAppType);
        comboBox18.setItems(cPolType);
        comboBox27.setItems(cActsNtr);
    }

    @Override
    public void initFieldsAction() {
        comboBox01.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    switch (comboBox01.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            lsClientSource = "0";
                            break;
                        case 1:
                            lsClientSource = "1";
                            break;
                    }
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox17.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox17.getSelectionModel().getSelectedIndex() >= 0) {
                    String lsAppType = "";
                    switch (comboBox17.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            lsAppType = "y";
                            break;
                        case 1:
                            lsAppType = "n";
                            break;
                        case 2:
                            lsAppType = "b";
                            break;
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setInsTypID(lsAppType);
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox18.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
                    String lsPolType = "";
                    switch (comboBox18.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            lsPolType = "y";
                            break;
                        case 1:
                            lsPolType = "n";
                            break;
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setIsNew(lsPolType);
                    initFields(pnEditMode);
                }
            }
        }
        );
    }

    @Override
    public void initTextFieldsProperty() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransInsProposal.getMasterModel().getMasterModel().setClientID("");
                        oTransInsProposal.getMasterModel().getMasterModel().setAddress("");
                        oTransInsProposal.getMasterModel().getMasterModel().setPlateNo("");
                        oTransInsProposal.getMasterModel().getMasterModel().setCSNo("");
                        oTransInsProposal.getMasterModel().getMasterModel().setEngineNo("");
                        oTransInsProposal.getMasterModel().getMasterModel().setFrameNo("");
                        oTransInsProposal.getMasterModel().getMasterModel().setVhclFDsc("");
                        clearVSPFields();
                        initFields(pnEditMode);
                    }
                }
            }
        });
        txtField16.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransInsProposal.getMasterModel().getMasterModel().setInsurNme("");
                        initFields(pnEditMode);
                    }
                }
            }
        });

    }

    public void clearVSPFields() {
        txtField09.setText("0.00");
        List<TextField> loStringTxtField = Arrays.asList(
                txtField04, txtField05, txtField06, txtField07, txtField08, txtField12, txtField14, txtField16);
        loStringTxtField.forEach(tf -> tf.setText(""));
        textArea03.setText("");
        datePicker11.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        comboBox10.setValue(null);
    }

    @Override
    public void clearTables() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void clearFields() {
        List<TextField> loTxtField = Arrays.asList(
                txtField09, txtField19, txtField20, txtField21,
                txtField22, txtField23, txtField24, txtField25, txtField26, txtField28, txtField29, txtField30, txtField31, txtField32, txtField33, txtField34,
                txtField35, txtField36, txtField37);
        loTxtField.forEach(tf -> tf.setText("0.00"));
        List<TextField> loStringTxtField = Arrays.asList(
                txtField02, txtField04, txtField05, txtField06, txtField07, txtField08, txtField12, txtField14, txtField16);
        loStringTxtField.forEach(tf -> tf.setText(""));
        datePicker11.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        List<ComboBox> loComboBox = Arrays.asList(
                comboBox01, comboBox10, comboBox17, comboBox18, comboBox27);
        loComboBox.forEach(cb -> cb.setValue(null));
        List<Label> loLabel = Arrays.asList(
                lblApprovalStatus, lblApprovalNo, lblIPStatus, lblIPNo, lblIPNoValue, lblIPStatus);
        loLabel.forEach(tf -> tf.setText(""));
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(!lbShow, comboBox01, txtField02, txtField14, txtField16, comboBox17, comboBox18);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnIPCancel);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnIPCancel);
        if (fnValue == EditMode.READY) {
            if (!lblIPStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnPrint, btnIPCancel);
                CustomCommonUtil.setManaged(true, btnEdit, btnPrint, btnIPCancel);
            }
        }
    }

    @Override
    public void loadMasterFields() {
        comboBox01.getSelectionModel().select(1);
        txtField02.setText(oTransInsProposal.getMasterModel().getMasterModel().getOwnrNm());
        textArea03.setText(oTransInsProposal.getMasterModel().getMasterModel().getAddress());
        txtField04.setText(oTransInsProposal.getMasterModel().getMasterModel().getCSNo());
        txtField05.setText(oTransInsProposal.getMasterModel().getMasterModel().getPlateNo());
        txtField06.setText(oTransInsProposal.getMasterModel().getMasterModel().getEngineNo());
        txtField07.setText(oTransInsProposal.getMasterModel().getMasterModel().getFrameNo());
        txtField08.setText(oTransInsProposal.getMasterModel().getMasterModel().getVhclFDsc());

        txtField09.setText("0.00");
        if (oTransInsProposal.getMasterModel().getMasterModel().getVhclNew() != null) {
            comboBox10.getSelectionModel().select(oTransInsProposal.getMasterModel().getMasterModel().getVhclNew());
        }
        if (oTransInsProposal.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker11.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransInsProposal.getMasterModel().getMasterModel().getTransactDte())));
        }
        txtField12.setText("");
        textArea13.setText(oTransInsProposal.getMasterModel().getMasterModel().getRemarks());
        txtField14.setText("");
        textArea15.setText("");
        txtField16.setText(oTransInsProposal.getMasterModel().getMasterModel().getBrInsNme());
        int appType = -1;
        if (oTransInsProposal.getMasterModel().getMasterModel().getInsTypID() != null) {
            switch (oTransInsProposal.getMasterModel().getMasterModel().getInsTypID()) {
                case "y":
                    appType = 0;
                    break;
                case "n":
                    appType = 1;
                    break;
            }
        }
        comboBox17.getSelectionModel().select(appType);
        int policeType = -1;
        if (oTransInsProposal.getMasterModel().getMasterModel().getIsNew() != null) {
            switch (oTransInsProposal.getMasterModel().getMasterModel().getIsNew()) {
                case "y":
                    policeType = 0;
                    break;
                case "n":
                    policeType = 1;
                    break;
                case "b":
                    policeType = 2;
                    break;
            }
        }
        comboBox18.getSelectionModel().select(policeType);
        txtField19.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCAmt()))));
        txtField20.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCRate()))));
        txtField21.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCAmt()))));
        txtField22.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCRate()))));
        txtField23.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getBdyCAmt()))));
        txtField24.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPrDCAmt()))));
        txtField25.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPAcCAmt()))));
        txtField26.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTPLAmt()))));
        int actNtr = -1;
        if (oTransInsProposal.getMasterModel().getMasterModel().getAONCPayM() != null) {
            switch (oTransInsProposal.getMasterModel().getMasterModel().getIsNew()) {
                case "cha":
                    actNtr = 0;
                    break;
                case "foc":
                    actNtr = 1;
                    break;
            }
        }
        comboBox27.getSelectionModel().select(actNtr);
        txtField28.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCPrem()))));
        txtField29.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCPrem()))));
        txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getBdyCPrem()))));
        txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPrDCPrem()))));
        txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPacCPrem()))));
        txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTPLPrem()))));
        txtField34.setText("0.00");
        txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTaxRate()))));
        txtField36.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTaxAmt()))));
        txtField37.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTotalAmt()))));
        String lsApStatus = "";
        String lsAppNo = "";
        String lsIPStatus = "";
        String lsIPNoValue = "";
        lblApprovalStatus.setText("");
        lblApprovalNo.setText("");
        lblIPStatus.setText("");
        if (oTransInsProposal.getMasterModel().getMasterModel().getTransNo() != null) {
            lblIPNo.setText("Insurance Proposal No: ");
            lsIPNoValue = oTransInsProposal.getMasterModel().getMasterModel().getTransNo();
        }
        lblIPNoValue.setText(lsIPNoValue);
    }

}
