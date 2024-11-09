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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import org.guanzon.auto.main.insurance.InsurancePolicy;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
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
    ObservableList<String> cActsNtr = FXCollections.observableArrayList("CHARGE", "FREE", "NOT APPLICABLE");
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
    @FXML
    private TabPane tabPane;

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
        datePicker12.setDayCellFactory(DateFrom);
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
    private Callback<DatePicker, DateCell> DateFrom = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            switch (pnEditMode) {
                case EditMode.ADDNEW:
                    LocalDate minDate = CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()));
                    setDisable(fbEmpty || foItem.isBefore(minDate));
                    break;
                case EditMode.UPDATE:
                    setDisable(fbEmpty || foItem.isBefore(datePicker12.getValue()));
                    break;
            }
        }
    };

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
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.computeAmount();
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getReferNo());
        txtField02.setText(oTrans.getMasterModel().getMasterModel().getPolicyNo());
        txtField03.setText(oTrans.getMasterModel().getMasterModel().getORNo());
        if (oTrans.getMasterModel().getMasterModel().getApplicDte() != null) {
            datePicker04.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getApplicDte(), SQLUtil.FORMAT_SHORT_DATE)));

        }
        if (oTrans.getMasterModel().getMasterModel().getValidFrmDte() != null) {
            datePicker05.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getValidFrmDte(), SQLUtil.FORMAT_SHORT_DATE)));
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
        txtField10.setText(oTrans.getMasterModel().getMasterModel().getCOCNo());
        txtField11.setText(oTrans.getMasterModel().getMasterModel().getMVFileNo());
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker12.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getTransactDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        if (oTrans.getMasterModel().getMasterModel().getValidTruDte() != null) {
            datePicker13.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getValidTruDte(), SQLUtil.FORMAT_SHORT_DATE)));
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
        textArea16.setText(oTrans.getMasterModel().getMasterModel().getRemarks());
        txtField17.setText("");
        textArea18.setText("");
        txtField19.setText(oTrans.getMasterModel().getMasterModel().getOwnrNm());
        textArea20.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField21.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
        txtField22.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
        txtField23.setText(oTrans.getMasterModel().getMasterModel().getVhclFDsc());
        txtField24.setText("0");
        if (oTrans.getMasterModel().getMasterModel().getUnitType() != null && !oTrans.getMasterModel().getMasterModel().getUnitType().trim().isEmpty()) {
            comboBox25.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getUnitType()));
        }
        txtField26.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
        txtField27.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
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
        double lnOwdtcPrem = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCPrem()));
        double lnAoncPrem = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCPrem()));
        double lnBdyCPrem = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCPrem()));
        double lnPrdcPrem = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCPrem()));
        double lnPacCPrem = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCPrem()));
        double lnTPLPrem = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLPrem()));
        double lnBasicPremium = lnOwdtcPrem + lnAoncPrem + lnBdyCPrem + lnPrdcPrem + lnPacCPrem + lnTPLPrem;
        txtField50.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lnBasicPremium))));
        txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDocAmt()))));
        txtField52.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getVATAmt()))));
        txtField53.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getLGUTaxAm()))));
        txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAuthFee()))));
        txtField55.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getGrossAmt()))));
        txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDiscAmt()))));
        txtField57.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getNetTotal()))));
        txtField58.setText("0.00");
        txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getCommissn()))));
        txtField60.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPayAmt()))));
        if (oTrans.getMasterModel().getMasterModel().getTranStat() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
                case TransactionStatus.STATE_OPEN:
                    lblInsPolicyStatus.setText("Active");
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

    @Override
    public void initPatternFields() {
        List<TextField> loTxtField = Arrays.asList(txtField30, txtField31,
                txtField32, txtField34, txtField35, txtField36,
                txtField46, txtField47, txtField48, txtField37, txtField49);
        Pattern decOnly = Pattern.compile("[0-9,.]*");
        loTxtField.forEach(tf -> tf.setTextFormatter(new TextFormatterUtil(decOnly)));
    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField10, txtField11, txtField26, txtField27, txtField30, txtField31,
                txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField51, txtField52, txtField53, txtField41,
                txtField42, txtField46, txtField47, txtField48, txtField49,
                txtField54, txtField56, txtField59);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea16);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
    }
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
                case 2:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTrans.getMasterModel().getMasterModel().setPolicyNo(lsValue);
                    break;
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTrans.getMasterModel().getMasterModel().setORNo(lsValue);
                    break;
                case 10:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTrans.getMasterModel().getMasterModel().setCOCNo(lsValue);
                    break;
                case 11:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTrans.getMasterModel().getMasterModel().setMVFileNo(lsValue);
                    break;
                case 30:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOAD = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOAD < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00"; // Reset to 0.00 for invalid amount
                    }
                    oTrans.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField30.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(txtField30.getText().replace(",", "")));
                    }
                    txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCAmt()))));
                    break;
                case 31:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOADRate = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOADRate > 100.00 || lnValueOADRate < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setODTCRate(Double.parseDouble(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField31.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setODTCRate(Double.parseDouble(txtField31.getText().replace(",", "")));
                    }
                    txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getODTCRate()))));
                    break;
                case 32:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueAON = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueAON < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField32.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(txtField32.getText().replace(",", "")));
                    }
                    txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCAmt()))));
                    break;
                case 33:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOANRate = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOANRate > 100.00 || lnValueOANRate < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setAONCRate(Double.parseDouble(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField33.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setAONCRate(Double.parseDouble(txtField33.getText().replace(",", "")));
                    }
                    txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAONCRate()))));
                    break;
                case 34:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueBdyInj = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueBdyInj < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField34.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal(txtField34.getText().replace(",", "")));
                    }
                    txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCAmt()))));
                    break;
                case 35:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePrplDmg = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePrplDmg < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField35.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal(txtField35.getText().replace(",", "")));
                    }
                    txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCAmt()))));
                    break;
                case 36:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePassACC = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePassACC < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField36.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal(txtField36.getText().replace(",", "")));
                    }
                    txtField36.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCAmt()))));
                    break;
                case 37:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueTPL = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueTPL < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField37.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(txtField37.getText().replace(",", "")));
                    }
                    txtField37.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLAmt()))));
                    break;
                case 46:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueBodyInj = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueBodyInj < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField46.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal(txtField46.getText().replace(",", "")));
                    }
                    txtField46.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getBdyCPrem()))));
                    break;
                case 47:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePrpDmgPrm = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePrpDmgPrm < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField47.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal(txtField47.getText().replace(",", "")));
                    }
                    txtField47.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPrDCPrem()))));
                    break;
                case 48:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePaAccPrm = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePaAccPrm < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setPAcCPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField48.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setPAcCPrem(new BigDecimal(txtField48.getText().replace(",", "")));
                    }
                    txtField48.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getPAcCPrem()))));
                    break;
                case 49:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueTPLPRM = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueTPLPRM < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField49.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal(txtField49.getText().replace(",", "")));
                    }
                    txtField49.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTPLPrem()))));
                    break;
                case 51:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueDoc = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueDoc < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setDocAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField51.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setDocAmt(new BigDecimal(txtField51.getText().replace(",", "")));
                    }
                    txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDocAmt()))));
                    break;
                case 52:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueTax = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueTax < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setVATAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField52.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setVATAmt(new BigDecimal(txtField52.getText().replace(",", "")));
                    }
                    txtField52.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getVATAmt()))));
                    break;
                case 53:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueLGU = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueLGU < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setLGUTaxAm(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField53.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setLGUTaxAm(new BigDecimal(txtField53.getText().replace(",", "")));
                    }
                    txtField53.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getLGUTaxAm()))));
                    break;
                case 54:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueAuthFee = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueAuthFee < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    double lnValueGross = Double.parseDouble(txtField50.getText().replace(",", ""));
                    if (lnValueAuthFee > lnValueGross) {
                        ShowMessageFX.Warning(null, "Warning", "Authentication Fee cannot be greater than Gross Total Premium");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setAuthFee(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField54.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setAuthFee(new BigDecimal(txtField54.getText().replace(",", "")));
                    }
                    txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getAuthFee()))));
                    break;
                case 56:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueDisc = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueDisc < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    if (lnValueDisc > Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getNetTotal()))) {
                        ShowMessageFX.Warning(null, "Warning", "Discount cannot be greater than Total Net Premium");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setDiscAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField56.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setDiscAmt(new BigDecimal(txtField56.getText().replace(",", "")));
                    }
                    txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDiscAmt()))));
                    break;
                case 59:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueCom = Double.parseDouble(lsValue.replace(",", ""));
                    if (lnValueCom < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    if (lnValueCom > Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getNetTotal()))) {
                        ShowMessageFX.Warning(null, "Warning", "Commission cannot be greater than Total Amount Due");
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setCommissn(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField59.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setCommissn(new BigDecimal(txtField59.getText().replace(",", "")));
                    }
                    txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getCommissn()))));
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
                case 16:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField10, txtField11, txtField26, txtField27, txtField30, txtField31,
                txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField51, txtField52, txtField53, txtField41,
                txtField42, txtField46, txtField47, txtField48, txtField49,
                txtField54, txtField56, txtField59);
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
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                case F3:
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
                    break;
                case UP:
                    event.consume();
                    CommonUtils.SetPreviousFocus((TextField) event.getSource());
                    break;
                case DOWN:
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                default:
                    break;
            }
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
                CustomCommonUtil.switchToTab(mainTab, tabPane);
                oTrans = new InsurancePolicy(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                initFields(pnEditMode);
                break;
            case "btnEdit":
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                initFields(pnEditMode);
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Insurance Policy Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Policy Information", (String) loJSON.get("message"));
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
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    CustomCommonUtil.switchToTab(mainTab, tabPane);
                    oTrans = new InsurancePolicy(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                initFields(pnEditMode);
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Insurance Policy Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.READY;
                    } else {
                        pnEditMode = oTrans.getEditMode();
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    CustomCommonUtil.switchToTab(mainTab, tabPane);
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, "Search Insurance Policy Information Confirmation", (String) loJSON.get("message"));
                    pnEditMode = oTrans.getEditMode();
                }
                initFields(pnEditMode);
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
                ShowMessageFX.Warning(null, pxeModuleName, "Button payment history is underdevelopment");
                break;
            case "btnCustomer":
                break;
            case "btnInsComp":
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
    }

    @Override
    public void initComboBoxItems() {
        comboBox06.setItems(cPolType);
        comboBox09.setItems(cAppType);
        comboBox14.setItems(cNewBusType);
        comboBox25.setItems(cVhclTyp);
        comboBox28.setItems(cVhclSize);
        comboBox29.setItems(cBodyType);
        comboBox44.setItems(cActsNtr);
    }

    @Override
    public void initFieldsAction() {
        comboBox44.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox44.getSelectionModel().getSelectedIndex() >= 0) {
                    String lsActsType = "";
                    switch (comboBox44.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            lsActsType = "cha";
                            break;
                        case 1:
                            lsActsType = "foc";
                            oTrans.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal(0.00));
                            oTrans.getMasterModel().getMasterModel().setAONCRate(0.00);
                            CustomCommonUtil.setText("0.00", txtField33, txtField45);
                            break;
                        case 2:
                            lsActsType = "na";
                            oTrans.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(0.00));
                            oTrans.getMasterModel().getMasterModel().setAONCRate(0.00);
                            oTrans.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal(0.00));
                            CustomCommonUtil.setText("0.00", txtField32, txtField33, txtField45);
                            break;
                    }
                    oTrans.getMasterModel().getMasterModel().setAONCPayM(lsActsType);
                    initFields(pnEditMode);
                }
            }
        }
        );

    }

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue.isEmpty()) {
                    clearPolicyAppInformation();
                    clearPolicyAppFields();
                }
                initFields(pnEditMode);
            }
        });
    }

    @Override
    public void clearTables() {

    }

    private void clearPolicyAppInformation() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (txtField01.getText().trim().isEmpty()) {
                /*POLICY INFORMATION*/
                oTrans.getMasterModel().getMasterModel().setReferNo("");
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
        CustomCommonUtil.switchToTab(mainTab, tabPane);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true,
                txtField03, txtField10, txtField11,
                txtField30, txtField31,
                txtField32, txtField33,
                txtField34, txtField35,
                txtField36, txtField37,
                txtField51, txtField52,
                txtField53, txtField41,
                txtField42, txtField46,
                txtField47, txtField48,
                txtField49, txtField50,
                txtField54, txtField56,
                txtField58, txtField59,
                txtField60, comboBox44
        );
        CustomCommonUtil.setDisable(!lbShow, txtField01, txtField02, datePicker12,
                txtField03, txtField10, txtField11,
                textArea16,
                txtField51, txtField52, txtField53,
                txtField54, txtField56, txtField59
        //                txtField41, txtField42
        );
        PremAddTab.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        if (lbShow) {
            switch (oTrans.getMasterModel().getMasterModel().getInsTypID()) {
                case "y":
                    CustomCommonUtil.setDisable(!lbShow, txtField37, txtField49);
                    break;
                case "c":
                    CustomCommonUtil.setDisable(!lbShow, txtField30, txtField31,
                            txtField32, comboBox44, txtField34,
                            txtField35, txtField36, txtField46,
                            txtField47, txtField48);
                    break;
                case "b":
                    CustomCommonUtil.setDisable(!lbShow, txtField30, txtField31,
                            txtField32, comboBox44, txtField34, txtField35,
                            txtField36, txtField46, txtField47, txtField48,
                            txtField37, txtField49);
                    break;
            }
            switch (comboBox44.getSelectionModel().getSelectedIndex()) {
                case 0:
                    CustomCommonUtil.setDisable(!lbShow, txtField32, txtField33);
                    break;
                case 1:
                    CustomCommonUtil.setDisable(!lbShow, txtField32);
                    break;
                case 2:
                    CustomCommonUtil.setDisable(true, txtField32, txtField33);
                    break;
            }
        }
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
            PremAddTab.setDisable(false);
        }

        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
            txtField02.setDisable(true);
            datePicker12.setDisable(true);
        }
    }

    private void loadInsProposalPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/insurance/InsurancePolicyPrint.fxml"));
//            InsurancePolicyPrintController loControl = new InsurancePolicyPrintController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTrans);
//            loControl.setTransNo(oTrans.getMasterModel().getMasterModel().getTransNo());
//            fxmlLoader.setController(loControl);
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
