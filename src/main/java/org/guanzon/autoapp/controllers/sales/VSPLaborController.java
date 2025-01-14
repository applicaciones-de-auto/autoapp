/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VSPLaborController implements Initializable {

    private GRider oApp;
    private boolean pbLbrDsc;
    private String psLbrDsc = "";
    private int pnRow = 0;
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Labor";
    private String psJO = "";
    private String psOrigDsc = "";
    private VehicleSalesProposal oTrans;
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAddLabor, btnEditLabor, btnCloseLabor;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06;
    @FXML
    private ComboBox<String> comboBox03;
    @FXML
    private CheckBox checkBoxIsAdd;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setOrigDsc(String fsValue) {
        psOrigDsc = fsValue;
    }

    public void setJO(String fsValue) {
        psJO = fsValue;
    }

    public void setLbrDesc(String fsValue) {
        psLbrDsc = fsValue;
    }

    public void setWithLabor(boolean fbValue) {
        pbLbrDsc = fbValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        loadMasterFields();
        initFields();
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01);
        CustomCommonUtil.setCapsLockBehavior(txtField02);
    }

    private void loadMasterFields() {
        if (!psLbrDsc.isEmpty()) {
            oTrans.getVSPLaborModel().getVSPLabor(pnRow).setLaborDsc(psLbrDsc);
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborCde() != null) {
            txtField01.setText(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborCde());
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc() != null) {
            txtField02.setText(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc());
        }
        int lnCrgType = -1;
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp() != null) {
            switch (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp()) {
                case "0":
                    lnCrgType = 0;
                    break;
                case "1":
                    lnCrgType = 1;
                    break;
            }
            comboBox03.getSelectionModel().select(lnCrgType);
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt() != null) {
            txtField04.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt()));
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount() != null) {
            txtField05.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()));
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt() != null) {
            txtField06.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt()));
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getAddtl() != null) {
            if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getAddtl().equals("0")) {
                checkBoxIsAdd.setSelected(false);
                txtField02.setDisable(true);
            } else {
                checkBoxIsAdd.setSelected(true);
            }
        }
        if (oTrans.getVSPLaborModel().getVSPLabor(pnRow).getEntryNo() > 0) {
            txtField02.setDisable(true);
        }
    }

    private boolean setToClass() {
        oTrans.getVSPLaborModel().getVSPLabor(pnRow).setLaborAmt(new BigDecimal(txtField04.getText().replace(",", "")));
        oTrans.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(txtField05.getText().replace(",", "")));
        oTrans.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(txtField06.getText().replace(",", "")));
        oTrans.getVSPLaborModel().getVSPLabor(pnRow).setChrgeTyp(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
        return true;
    }

    private void initPatternFields() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(pattern));
        txtField05.setTextFormatter(new TextFormatterUtil(pattern));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        double lnLaborAmtx = Double.parseDouble(txtField04.getText().replace(",", ""));
        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    txtField04.setText(CustomCommonUtil.setDecimalFormat(lsValue.replace(",", "")));
                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (Double.parseDouble(lsValue.replace(",", "")) > lnLaborAmtx) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Discount cannot be greater than Labor Amount.");
                        lsValue = "0.00";
                    }
                    txtField05.setText(CustomCommonUtil.setDecimalFormat(lsValue.replace(",", "")));
                    break;
            }
            computeTotalLaborFromType();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField02":
                    loJSON = oTrans.searchLabor("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField01.setText(String.valueOf(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborCde()));
                        txtField02.setText(String.valueOf(oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc()));
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                        txtField02.clear();
                        return;
                    }
                    break;
            }
            initFields();
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initButtonsClick() {
        btnCloseLabor.setOnAction(this::handleButtonAction);
        btnAddLabor.setOnAction(this::handleButtonAction);
        btnEditLabor.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEditLabor":
            case "btnAddLabor":
                if (isValidEntry()) {
                    if (setToClass()) {
                        if (lsButton.equals("btnAddLabor")) {
                            CommonUtils.closeStage(btnAddLabor);
                        } else {
                            CommonUtils.closeStage(btnEditLabor);
                        }
                    }
                }
                break;
            case "btnCloseLabor":
                JSONObject loJSON = new JSONObject();
                if (pbState) {
                    oTrans.removeVSPLabor(pnRow);
                } else {
                    loJSON = oTrans.searchLabor(psOrigDsc, pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                    }
                }
                CommonUtils.closeStage(btnCloseLabor);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private void initComboBoxItems() {
        comboBox03.setItems(cChargeType);
    }

    private void initFieldsAction() {
        if (pbState) {
            if (pbLbrDsc) {
                oTrans.getVSPLaborModel().getVSPLabor(pnRow).setAddtl("0");
            } else {
                oTrans.getVSPLaborModel().getVSPLabor(pnRow).setAddtl("1");
            }
        }
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
                    txtField05.setText("0.00");
                }
                computeTotalLaborFromType();
                initFields();
            }
        }
        );
    }

    private void computeTotalLaborFromType() {
        double lnLaborAmnt = Double.parseDouble(txtField04.getText().replace(",", ""));
        double lnLaborDscx = Double.parseDouble(txtField05.getText().replace(",", ""));
        double lnTtlAmntFr = lnLaborAmnt - lnLaborAmnt;
        double lnTtlAmntCh = lnLaborAmnt - lnLaborDscx;
        switch (comboBox03.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField05.setText(CustomCommonUtil.setDecimalFormat(lnLaborAmnt));
                txtField06.setText(CustomCommonUtil.setDecimalFormat(lnTtlAmntFr));
                break;
            case 1:
                txtField06.setText(CustomCommonUtil.setDecimalFormat(lnTtlAmntCh));
                break;
            default:
                CustomCommonUtil.setText("0.00", txtField04, txtField05, txtField06);
        }
    }

    private void initTextFieldsProperty() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTrans.getVSPLaborModel().getVSPLabor(pnRow).setLaborCde("");
                    oTrans.getVSPLaborModel().getVSPLabor(pnRow).setLaborDsc("");
                }
            }
        });
    }

    private void initFields() {
        switch (comboBox03.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField04.setDisable(false);
                txtField05.setDisable(true);
                break;
            case 1:
                txtField04.setDisable(false);
                txtField05.setDisable(txtField04.getText().isEmpty());
                break;
            default:
                CustomCommonUtil.setDisable(true, txtField04, txtField05);
                break;
        }
        if (pbState) {
            btnAddLabor.setVisible(true);
            btnAddLabor.setManaged(true);
            btnEditLabor.setVisible(false);
            btnEditLabor.setManaged(false);
            if (pbLbrDsc) {
                txtField02.setDisable(true);
            }
        } else {
            btnAddLabor.setVisible(false);
            btnAddLabor.setManaged(false);
            btnEditLabor.setVisible(true);
            btnEditLabor.setManaged(true);
            if (!psJO.isEmpty()) {
                CustomCommonUtil.setDisable(true, txtField04, comboBox03);
            }
        }
    }

    private boolean isValidEntry() {
        if (txtField02.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, "Warning", "Please input Labor Description");
            txtField02.requestFocus();
            return false;
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (txtField04.getText().equals("0.00")) {
            ShowMessageFX.Warning(null, "Warning", "Please enter amount.");
            return false;
        }

        if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
            if (txtField06.getText().equals("0.00")) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid Charge Type.");
                return false;
            }
            if (Double.parseDouble(txtField06.getText().replace(",", "")) < 0.00) {
                ShowMessageFX.Warning(null, "Warning", "Please enter valid net price amount.");
                return false;
            }
        }
        return true;
    }

}
