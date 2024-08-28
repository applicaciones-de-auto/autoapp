/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.text.DecimalFormat;
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
 * @author User
 */
public class VSPAccessoriesDialogController implements Initializable {

    private GRider oApp;
    private int pnRow = 0;
    private String psStockID = "";
    private String psJO = "";
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Accessories";
    private boolean pbRequest = false;
    private String psOrigDsc = "";
    private VehicleSalesProposal oTransVSPAccessories;
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField05, txtField06, txtField07;
    @FXML
    private ComboBox<String> comboBox04;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTransVSPAccessories = foValue;
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

    public void setStockID(String fsValue) {
        psStockID = fsValue;
    }

    public void setRequest(boolean fsValue) {
        pbRequest = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initCapitalizationFields();
        comboBox04.setItems(cChargeType);
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initTextPropertyAction();
        initButtonsClick();
        initFielPattern();
        loadPartsFields();
        initFields();
    }

    private void loadPartsFields() {
        txtField01.setText(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "")));
        txtField02.setText(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "")));
        if (oTransVSPAccessories.getVSPParts(pnRow, "") != null && !oTransVSPAccessories.getVSPParts(pnRow, "").equals("")) {
            comboBox04.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, ""))));
            if (oTransVSPAccessories.getVSPParts(pnRow, "").equals(0)) {
                txtField05.setDisable(true);
            } else {
                txtField05.setDisable(true);
            }
        }
        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "nNtDwnPmt")))));
        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "nNtDwnPmt")))));
        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "nNtDwnPmt")))));
//        if ((!oTransVSPAccessories.getVSPParts(pnRow, "sTransNox").toString().isEmpty())) {
//            txtField02.setDisable(true);
//        }
    }

    private void initFielPattern() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField05.setTextFormatter(new InputTextFormatterUtil(pattern));
        txtField06.setTextFormatter(new InputTextFormatterUtil(pattern));
    }

    private void initCapitalizationFields() {
        InputTextUtil.setCapsLockBehavior(txtField01);
        InputTextUtil.setCapsLockBehavior(txtField02);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField03, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField02":
                    loJSON = oTransVSPAccessories.searchParts("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        oTransVSPAccessories.getVSPParts(pnRow, "");
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                        txtField02.clear();
                        txtField02.requestFocus();
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
                txtField01, txtField02, txtField03, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        double lnNetPrice = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, ""))) - Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "")));
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /* Lost Focus */
            switch (lnIndex) {
                case 2:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTransVSPAccessories.setVSPParts(pnRow, lsValue, lsValue);
                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPAccessories.setVSPParts(pnRow, lsValue, Double.valueOf(lsValue.replace(",", "")));
                    txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "")))));
                    txtField07.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lnNetPrice))));
                    break;
                case 6:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPAccessories.setVSPParts(pnRow, lsValue, Double.valueOf(lsValue.replace(",", "")));
                    txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, lsValue)))));
                    txtField07.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lnNetPrice))));
                    break;
            }
        }
    };

    private void initCmboxFieldAction() {
        comboBox04.setOnAction(event -> {
            if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
                oTransVSPAccessories.setVSPParts(pnRow, "", String.valueOf(comboBox04.getSelectionModel().getSelectedIndex()));
                initFields();
            }
        }
        );
    }

    private void initTextPropertyAction() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransVSPAccessories.setVSPParts(pnRow, "", "");
                }
            }
        });
    }

    private void initButtonsClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        btnEdit.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEdit":
            case "btnAdd":
                if (isValidEntry()) {
                    CommonUtils.closeStage(btnClose);
                } else {
                    return;
                }
                break;
            case "btnClose":
                if (pbState) {
                    if (oTransVSPAccessories.getVSPParts(pnRow, "").toString().isEmpty()) {
                        oTransVSPAccessories.removeVSPParts(pnRow);
                    }
                } else {
                    loJSON = oTransVSPAccessories.searchParts(psOrigDsc, pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private boolean isValidEntry() {
        if (txtField02.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, "Warning", "Please input Accessories Description");
            txtField02.requestFocus();
            return false;
        }
        if (comboBox04.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (comboBox04.getSelectionModel().getSelectedIndex() == 1) {
            String laborAmount = txtField05.getText().replace(",", ""); // Remove commas from the input string
            try {
                double amount = Double.parseDouble(laborAmount);
                if (amount == 0.00 || amount < 0.00) {
                    ShowMessageFX.Warning(null, "Warning", "Please input Labor Amount");
                    txtField05.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                // Handle the case where laborAmount is not a valid number
                ShowMessageFX.Warning(null, "Warning", "Invalid Labor Amount");
                txtField05.requestFocus();
                return false;
            }

        }
        return true;
    }

    private void initFields() {
        if (comboBox04.getSelectionModel().getSelectedIndex() == 0) {
            txtField05.setDisable(true);
            txtField06.setDisable(true);
            oTransVSPAccessories.setVSPParts(pnRow, "", oTransVSPAccessories.getVSPParts(pnRow, ""));
            txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "")))));
            double lnNetPrice = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, ""))) - Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPParts(pnRow, "")));
            oTransVSPAccessories.setVSPParts(pnRow, "", lnNetPrice);
        } else {
            txtField05.setDisable(false);
            txtField06.setDisable(!txtField05.getText().isEmpty());
        }
        if (txtField06.getText().equals("0.00")) {
            comboBox04.getSelectionModel().select(0);
        }

        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);

            if (pbRequest) {
                txtField02.setDisable(true);
                txtField03.setDisable(true);
                comboBox04.setDisable(true);
                txtField05.setDisable(true);
                txtField06.setDisable(true);
            } else {
                txtField02.setDisable(false);
                txtField03.setDisable(false);
                comboBox04.setDisable(false);
                txtField05.setDisable(false);
                txtField06.setDisable(false);

            }
            if (!psJO.isEmpty()) {
                txtField02.setDisable(true);
                txtField05.setDisable(true);
                comboBox04.setDisable(true);
                txtField06.setDisable(true);
            } else {
                if (pbRequest) {
                    if (!oTransVSPAccessories.getVSPParts(pnRow, "sApproved").toString().isEmpty()) {
                        txtField01.setDisable(false);
                    } else {
                        txtField01.setDisable(true);
                    }
                }

            }
            if (!txtField01.getText().isEmpty()) {
                txtField02.setDisable(true);
            }
        }
    }

}
