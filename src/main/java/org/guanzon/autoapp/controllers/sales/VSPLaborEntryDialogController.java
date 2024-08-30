/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
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
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VSPLaborEntryDialogController implements Initializable {

    private GRider oApp;
    private boolean pbLbrDsc;
    private String psLbrDsc;
    private int pnRow = 0;
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Labor";
    private String psJO = "";
    private String psOrigDsc = "";
    private VehicleSalesProposal oTransVSPLabor;
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");

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
        oTransVSPLabor = foValue;
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

        initCapitalizationFields();
        comboBox03.setItems(cChargeType);
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initTextPropertyAction();
        initButtonsClick();
        initFielPattern();
        loadLaborFields();
        initFields();
    }

    private void loadLaborFields() {
        if (!psLbrDsc.isEmpty()) {
            oTransVSPLabor.setVSPLabor(pnRow, "sLaborDsc", psLbrDsc);
        }
        txtField01.setText(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "sLaborCde")));
        txtField02.setText(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "sLaborDsc")));
        if (oTransVSPLabor.getVSPLabor(pnRow, "") != null && !oTransVSPLabor.getVSPLabor(pnRow, "").equals("")) {
            comboBox03.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "sChrgeTyp"))));
            if (oTransVSPLabor.getVSPLabor(pnRow, "").equals(0)) {
                txtField04.setDisable(true);
            } else {
                txtField04.setDisable(true);
            }
        }
        txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborAmt")))));
        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborDsc")))));
        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nNtLabAmt")))));
        if (oTransVSPLabor.getVSPLabor(pnRow, "cAddtlxxx").equals("0")) {
            checkBoxIsAdd.setSelected(false);
            txtField02.setDisable(true);
        } else {
            checkBoxIsAdd.setSelected(true);
        }
        if ((!oTransVSPLabor.getVSPLabor(pnRow, "sTransNox").toString().isEmpty())) {
            txtField02.setDisable(true);
        }
    }

    private void initFielPattern() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new InputTextFormatterUtil(pattern));
        txtField05.setTextFormatter(new InputTextFormatterUtil(pattern));
    }

    private void initCapitalizationFields() {
        InputTextUtil.setCapsLockBehavior(txtField01);
        InputTextUtil.setCapsLockBehavior(txtField02);
    }

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
                    loJSON = oTransVSPLabor.searchLabor("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField02.setText(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "sLaborDsc")));
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
        double lnNetPrice = Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborAmt"))) - Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, " "
                + " , a.nLaborDsc")));
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /* Lost Focus */
            switch (lnIndex) {
                case 1:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTransVSPLabor.setVSPLabor(pnRow, lsValue, lsValue);
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPLabor.setVSPLabor(pnRow, "nLaborAmt", Double.valueOf(lsValue.replace(",", "")));
                    txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborAmt")))));
                    oTransVSPLabor.setVSPLabor(pnRow, "nNtLabAmt", lnNetPrice);
                    txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nNtLabAmt")))));
                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPLabor.setVSPLabor(pnRow, lsValue, Double.valueOf(lsValue.replace(",", "")));
                    txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, lsValue)))));
                    oTransVSPLabor.setVSPLabor(pnRow, "nNtLabAmt", lnNetPrice);
                    txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nNtLabAmt")))));
                    break;
            }
        }
    };

    private void initCmboxFieldAction() {
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTransVSPLabor.setVSPLabor(pnRow, "sChrgeTyp", String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
                initFields();
            }
        }
        );
    }

    private void initTextPropertyAction() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransVSPLabor.setVSPLabor(pnRow, "sLaborDsc", "");
                }
            }
        });
    }

    private void initButtonsClick() {
        btnCloseLabor.setOnAction(this::handleButtonAction);
        btnAddLabor.setOnAction(this::handleButtonAction);
        btnEditLabor.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEditLabor":
            case "btnAddLabor":
                if (isValidEntry()) {
                    CommonUtils.closeStage(btnCloseLabor);
                } else {
                    return;
                }
                break;
            case "btnCloseLabor":
                if (pbState) {
                    if (oTransVSPLabor.getVSPLabor(pnRow, "sLaborCde").toString().isEmpty()) {
                        oTransVSPLabor.removeVSPLabor(pnRow);
                    }
                } else {
                    loJSON = oTransVSPLabor.searchLabor(psOrigDsc, pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                }
                CommonUtils.closeStage(btnCloseLabor);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
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
        if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
            String laborAmount = txtField04.getText().replace(",", ""); // Remove commas from the input string
            try {
                double amount = Double.parseDouble(laborAmount);
                if (amount == 0.00 || amount < 0.00) {
                    ShowMessageFX.Warning(null, "Warning", "Please input Labor Amount");
                    txtField04.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                // Handle the case where laborAmount is not a valid number
                ShowMessageFX.Warning(null, "Warning", "Invalid Labor Amount");
                txtField04.requestFocus();
                return false;
            }

        }
        return true;
    }

    private void initFields() {
        if (comboBox03.getSelectionModel().getSelectedIndex() == 0) {
            txtField04.setDisable(true);
            txtField05.setDisable(true);
            oTransVSPLabor.setVSPLabor(pnRow, "", oTransVSPLabor.getVSPLabor(pnRow, "nLaborDsc"));
            txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborDsc")))));
            double lnNetPrice = Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborAmt"))) - Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLabor(pnRow, "nLaborDsc")));
            oTransVSPLabor.setVSPLabor(pnRow, "nNtLabAmt", lnNetPrice);
        } else {
            txtField04.setDisable(false);
            txtField05.setDisable(!txtField04.getText().isEmpty());
        }
        if (txtField06.getText().equals("0.00")) {
            comboBox03.getSelectionModel().select(0);
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
                txtField04.setDisable(true);
                comboBox03.setDisable(true);
            }
        }
    }

}
