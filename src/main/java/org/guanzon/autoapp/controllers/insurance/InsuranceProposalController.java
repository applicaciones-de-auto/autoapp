package org.guanzon.autoapp.controllers.insurance;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    private boolean pbIsOrig = false;
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cClntSrc = FXCollections.observableArrayList("GENERAL CUSTOMER", "VSP");
    ObservableList<String> cAppType = FXCollections.observableArrayList("NEW", "RENEW");
    ObservableList<String> cVhclTyp = FXCollections.observableArrayList("COMMERCIAL", "PRIVATE");
    ObservableList<String> cPolType = FXCollections.observableArrayList("TPL", "COMPREHENSIVE", "TPL AND COMPREHENSIVE");
    ObservableList<String> cActsNtr = FXCollections.observableArrayList("CHARGE", "FREE", "NOT APPLICABLE");
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
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransInsProposal.computeAmount();
        if (oTransInsProposal.getMasterModel().getMasterModel().getVSPTranNo() != null && !oTransInsProposal.getMasterModel().getMasterModel().getVSPTranNo().isEmpty()) {
            comboBox01.getSelectionModel().select(1);
        } else {
            comboBox01.getSelectionModel().select(0);
        }
        txtField02.setText(oTransInsProposal.getMasterModel().getMasterModel().getOwnrNm());
        textArea03.setText(oTransInsProposal.getMasterModel().getMasterModel().getAddress());
        txtField04.setText(oTransInsProposal.getMasterModel().getMasterModel().getCSNo());
        txtField05.setText(oTransInsProposal.getMasterModel().getMasterModel().getPlateNo());
        txtField06.setText(oTransInsProposal.getMasterModel().getMasterModel().getEngineNo());
        txtField07.setText(oTransInsProposal.getMasterModel().getMasterModel().getFrameNo());
        txtField08.setText(oTransInsProposal.getMasterModel().getMasterModel().getVhclFDsc());

        txtField09.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getUnitPrce()))));
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
        if (oTransInsProposal.getMasterModel().getMasterModel().getIsNew() != null) {
            switch (oTransInsProposal.getMasterModel().getMasterModel().getIsNew()) {
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
        if (oTransInsProposal.getMasterModel().getMasterModel().getInsTypID() != null) {
            switch (oTransInsProposal.getMasterModel().getMasterModel().getInsTypID()) {
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
            switch (oTransInsProposal.getMasterModel().getMasterModel().getAONCPayM()) {
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
        comboBox27.getSelectionModel().select(actNtr);
        txtField28.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCPrem()))));
        txtField29.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCPrem()))));
        txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getBdyCPrem()))));
        txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPrDCPrem()))));
        txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPAcCPrem()))));
        txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTPLPrem()))));

        double lnOwdtcPrem = Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCPrem()));
        double lnAoncPrem = Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCPrem()));
        double lnBdyCPrem = Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getBdyCPrem()));
        double lnPrdcPrem = Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPrDCPrem()));
        double lnPacCPrem = Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPAcCPrem()));
        double lnTPLPrem = Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTPLPrem()));
        double lnBasicPremium = lnOwdtcPrem + lnAoncPrem + lnBdyCPrem + lnPrdcPrem + lnPacCPrem + lnTPLPrem;
        txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lnBasicPremium))));
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
//        if (oTransInsProposal.getMasterModel().getMasterModel().getTransNo() != null) {
//            lblIPNo.setText("Insurance Proposal No: ");
//            lsIPNoValue = oTransInsProposal.getMasterModel().getMasterModel().getTransNo();
//        }
//        lblIPNoValue.setText(lsIPNoValue);
        return true;
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField19, txtField20, txtField21,
                txtField22, txtField23, txtField24, txtField25, txtField26, txtField28,
                txtField29, txtField30, txtField31, txtField32, txtField33, txtField35);
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
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOAD = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOAD < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00"; // Reset to 0.00 for invalid amount
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField19.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal(txtField19.getText().replace(",", "")));
                    }
                    txtField19.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCAmt()))));
                    break;
                case 20:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOADRate = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOADRate > 100.00 || lnValueOADRate < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setODTCRate(Double.parseDouble(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField20.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setODTCRate(Double.parseDouble(txtField20.getText().replace(",", "")));
                    }
                    txtField20.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getODTCRate()))));
                    break;
                case 21:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOAN = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOAN < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField21.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal(txtField21.getText().replace(",", "")));
                    }
                    txtField21.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCAmt()))));
                    break;
                case 22:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueOANRate = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueOANRate > 100.00 || lnValueOANRate < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setAONCRate(Double.parseDouble(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField22.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setAONCRate(Double.parseDouble(txtField22.getText().replace(",", "")));
                    }
                    txtField22.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getAONCRate()))));
                    break;
                case 23:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueBdyInj = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueBdyInj < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField23.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal(txtField23.getText().replace(",", "")));
                    }
                    txtField23.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getBdyCAmt()))));
                    break;
                case 24:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePrplDmg = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePrplDmg < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField24.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal(txtField24.getText().replace(",", "")));
                    }
                    txtField24.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPrDCAmt()))));
                    break;
                case 25:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePassACC = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePassACC < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField25.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal(txtField25.getText().replace(",", "")));
                    }
                    txtField25.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPAcCAmt()))));
                    break;
                case 26:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueTPL = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueTPL < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField26.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(txtField26.getText().replace(",", "")));
                    }
                    txtField26.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTPLAmt()))));
                    break;
                case 30:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueBodyInj = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueBodyInj < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField30.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal(txtField30.getText().replace(",", "")));
                    }
                    txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getBdyCPrem()))));
                    break;
                case 31:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePrpDmgPrm = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePrpDmgPrm < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField31.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal(txtField31.getText().replace(",", "")));
                    }
                    txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPrDCPrem()))));
                    break;
                case 32:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValuePaAccPrm = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePaAccPrm < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setPAcCPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField32.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setPAcCPrem(new BigDecimal(txtField32.getText().replace(",", "")));
                    }
                    txtField32.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getPAcCPrem()))));
                    break;
                case 33:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueTPLPRM = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueTPLPRM < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField33.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal(txtField33.getText().replace(",", "")));
                    }
                    txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTPLPrem()))));
                    break;
                case 35:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnValueTax = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueTax > 100.00 || lnValueTax < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setTaxRate(Double.parseDouble(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField35.setText("0.00");
                        oTransInsProposal.getMasterModel().getMasterModel().setTaxRate(Double.parseDouble(txtField35.getText().replace(",", "")));
                    }
                    txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInsProposal.getMasterModel().getMasterModel().getTaxRate()))));
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
                txtField22, txtField23, txtField24, txtField25, txtField26, txtField28, txtField29, txtField30, txtField31, txtField32, txtField33, txtField35
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
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        loJSON = oTransInsProposal.searchGeneralClient(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadMasterFields();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    } else {
                        loJSON = oTransInsProposal.searchVSP(lsValue, false, String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
                        if (!"error".equals(loJSON.get("result"))) {
                            loadMasterFields();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    }
                    break;
                case "txtField14":
//                    loJSON = oTransInsProposal.searchInsurance(lsValue);
//                    if (!"error".equals(loJSON.get("result"))) {
//                        loadMasterFields();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        return;
//                    }
                    ShowMessageFX.Warning(null, pxeModuleName, "THIS SEARCH TEXTFIELD IS UNDER DEVELOPMENT");
                    break;
                case "txtField16":
                    loJSON = oTransInsProposal.searchInsurance(lsValue.trim());
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
                    loadMasterFields();
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
                try {
                loadInsProposalPrint();
            } catch (SQLException ex) {
                Logger.getLogger(InsuranceProposalController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                    oTransInsProposal.getMasterModel().getMasterModel().setClientID("");
                    oTransInsProposal.getMasterModel().getMasterModel().setAddress("");
                    oTransInsProposal.getMasterModel().getMasterModel().setPlateNo("");
                    oTransInsProposal.getMasterModel().getMasterModel().setCSNo("");
                    oTransInsProposal.getMasterModel().getMasterModel().setEngineNo("");
                    oTransInsProposal.getMasterModel().getMasterModel().setFrameNo("");
                    oTransInsProposal.getMasterModel().getMasterModel().setVhclFDsc("");
                    txtField02.setText("");
                    comboBox17.setValue(null);
                    comboBox18.setValue(null);
                    oTransInsProposal.getMasterModel().getMasterModel().setAONCPayM("");
                    comboBox27.setValue(null);
                    clearVSPFields();
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox18.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                String originalPolicy = oTransInsProposal.getMasterModel().getMasterModel().getInsTypID();
                String lsPolType = "";
                String lsValuePolType = "";
                if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                        if (!originalPolicy.isEmpty()) {
                            if (!pbIsOrig) {
                                if (ShowMessageFX.YesNo(null, pxeModuleName,
                                        "Are you sure you want to replace policy type?\n"
                                        + "if YES: it will clear all values related fields\n"
                                        + "if NO: it will remain the values.")) {
                                    oTransInsProposal.getMasterModel().getMasterModel().setClientID("");
                                    oTransInsProposal.getMasterModel().getMasterModel().setAddress("");
                                    oTransInsProposal.getMasterModel().getMasterModel().setPlateNo("");
                                    oTransInsProposal.getMasterModel().getMasterModel().setCSNo("");
                                    oTransInsProposal.getMasterModel().getMasterModel().setEngineNo("");
                                    oTransInsProposal.getMasterModel().getMasterModel().setFrameNo("");
                                    oTransInsProposal.getMasterModel().getMasterModel().setVhclFDsc("");
                                    txtField02.setText("");
                                    comboBox17.setValue(null);
                                    oTransInsProposal.getMasterModel().getMasterModel().setAONCPayM("");
                                    comboBox27.setValue(null);
                                    clearVSPFields();
                                    lsValuePolType = setPoliceType(lsPolType, comboBox18.getSelectionModel().getSelectedIndex());
                                    pbIsOrig = false;
                                } else {
                                    Platform.runLater(() -> {
                                        int orig = -1;
                                        pbIsOrig = true;
                                        switch (originalPolicy) {
                                            case "y":
                                                orig = 0;
                                                break;
                                            case "n":
                                                orig = 1;
                                                break;
                                            case "b":
                                                orig = 2;
                                                break;
                                        }
                                        comboBox18.getSelectionModel().select(orig);
                                    });
                                    return;
                                }
                            }
                        } else {
                            lsValuePolType = setPoliceType(lsPolType, comboBox18.getSelectionModel().getSelectedIndex());
                        }
                    } else {
                        lsValuePolType = setPoliceType(lsPolType, comboBox18.getSelectionModel().getSelectedIndex());
                    }
                    if (!lsValuePolType.isEmpty()) {
                        oTransInsProposal.getMasterModel().getMasterModel().setInsTypID(lsValuePolType);
                    }
                    initFields(pnEditMode);
                    int lnPolicyType = -1;
                    switch (oTransInsProposal.getMasterModel().getMasterModel().getInsTypID()) {
                        case "y":
                            lnPolicyType = 0;
                            break;
                        case "n":
                            lnPolicyType = 1;
                            break;
                        case "b":
                            lnPolicyType = 2;
                            break;
                    }
                    comboBox18.getSelectionModel().select(lnPolicyType);
                }
            }
        }
        );
        comboBox17.setOnAction(event
                -> {
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
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setIsNew(lsAppType);
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox27.setOnAction(event
                -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox27.getSelectionModel().getSelectedIndex() >= 0) {
                    String lsActsType = "";
                    switch (comboBox27.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            lsActsType = "cha";
                            break;
                        case 1:
                            lsActsType = "foc";
                            oTransInsProposal.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal(0.00));
                            txtField29.setText("0.00");
                            oTransInsProposal.getMasterModel().getMasterModel().setAONCRate(0.00);
                            txtField22.setText("0.00");
                            break;
                        case 2:
                            lsActsType = "na";
                            break;
                    }
                    oTransInsProposal.getMasterModel().getMasterModel().setAONCPayM(lsActsType);
                    initFields(pnEditMode);
                }
            }
        }
        );
    }

    private String setPoliceType(String fsValue, int fnSelectedIndex) {
        switch (fnSelectedIndex) {
            case 0:
                fsValue = "y";
                oTransInsProposal.getMasterModel().getMasterModel().setAONCPayM("na");
                int actNtr = -1;
                if (oTransInsProposal.getMasterModel().getMasterModel().getAONCPayM() != null) {
                    switch (oTransInsProposal.getMasterModel().getMasterModel().getAONCPayM()) {
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
                comboBox27.getSelectionModel().select(actNtr);
                CustomCommonUtil.setText("0.00", txtField19, txtField20,
                        txtField28, txtField21,
                        txtField22, txtField29,
                        txtField23, txtField30,
                        txtField24, txtField31,
                        txtField25, txtField32);
                break;
            case 1:
                fsValue = "n";
                oTransInsProposal.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal("0.00"));
                oTransInsProposal.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal("0.00"));
                CustomCommonUtil.setText("0.00", txtField26, txtField33);
                oTransInsProposal.getMasterModel().getMasterModel().setAONCPayM("");
                comboBox27.setValue(null);
                break;
            case 2:
                fsValue = "b";
                oTransInsProposal.getMasterModel().getMasterModel().setAONCPayM("");
                comboBox27.setValue(null);
                break;
        }
        return fsValue;
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
        CustomCommonUtil.setText("", txtField04, txtField05, txtField06,
                txtField07, txtField08, txtField12,
                txtField14, txtField16);
        textArea03.setText("");
        datePicker11.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oApp.getServerDate())));
        comboBox10.setValue(null);
    }

    @Override
    public void clearTables() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void clearFields() {
        CustomCommonUtil.setText("0.00", txtField09,
                txtField19, txtField20,
                txtField21, txtField22,
                txtField23, txtField24,
                txtField25, txtField26,
                txtField28, txtField29,
                txtField30, txtField31,
                txtField32, txtField33,
                txtField34, txtField35,
                txtField36, txtField37);
        CustomCommonUtil.setText("", txtField02,
                txtField04, txtField05,
                txtField06, txtField07,
                txtField08, txtField12,
                txtField14, txtField16);

        CustomCommonUtil.setValue(null,
                comboBox01, comboBox10,
                comboBox17, comboBox18,
                comboBox27);
        CustomCommonUtil.setText("", lblApprovalStatus, lblApprovalNo, lblIPStatus, lblIPNo, lblIPNoValue, lblIPStatus);
        datePicker11.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oApp.getServerDate())));
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true,
                txtField02, comboBox10,
                comboBox18,
                txtField16, comboBox17,
                txtField19, txtField20, txtField28,
                txtField21, txtField22, comboBox27, txtField29,
                txtField23, txtField30,
                txtField24, txtField31,
                txtField25, txtField32,
                txtField26, txtField33
        );
        CustomCommonUtil.setDisable(!lbShow, comboBox01, txtField35);
        if (lbShow) {
            if (lsClientSource == "1") {
                if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
                    CustomCommonUtil.setDisable(!lbShow, txtField02, comboBox17, comboBox10);
                }
            } else {
                if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
                    CustomCommonUtil.setDisable(!lbShow, txtField02, comboBox10, txtField16, comboBox17);
                }
            }
            switch (comboBox18.getSelectionModel().getSelectedIndex()) {
                case 0:
                    CustomCommonUtil.setDisable(!lbShow, txtField26, txtField33);
                    break;
                case 1:
                    CustomCommonUtil.setDisable(!lbShow, txtField19, txtField20,
                            comboBox27, txtField23, txtField30,
                            txtField24, txtField31,
                            txtField25, txtField32);
                    break;
                case 2:
                    CustomCommonUtil.setDisable(!lbShow, txtField19, txtField20,
                            comboBox27, txtField23, txtField30,
                            txtField24, txtField31,
                            txtField25, txtField32,
                            txtField26, txtField33);
                    break;
            }
            switch (comboBox27.getSelectionModel().getSelectedIndex()) {
                case 0:
                    CustomCommonUtil.setDisable(!lbShow, txtField21, txtField22);
                    break;
                case 1:
                    CustomCommonUtil.setDisable(!lbShow, txtField21);
                    break;
            }

        }

        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnIPCancel);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnIPCancel);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        comboBox18.setDisable(!(lbShow && !comboBox01.getValue().isEmpty()));
        if (fnValue == EditMode.UPDATE) {
            CustomCommonUtil.setDisable(true, comboBox01);
        }
        if (fnValue == EditMode.READY) {
            if (!lblIPStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnPrint, btnIPCancel);
                CustomCommonUtil.setManaged(true, btnEdit, btnPrint, btnIPCancel);
            }
        }
    }

    private void loadInsProposalPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/insurance/InsuranceProposalPrint.fxml"));
            InsuranceProposalPrintController loControl = new InsuranceProposalPrintController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransInsProposal);
            loControl.setTransNo(oTransInsProposal.getMasterModel().getMasterModel().getTransNo());
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
