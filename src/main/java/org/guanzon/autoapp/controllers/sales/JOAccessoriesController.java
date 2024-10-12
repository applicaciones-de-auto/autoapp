/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.math.BigDecimal;
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
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class JOAccessoriesController implements Initializable {

    private GRider oApp;
    private int pnRow = 0;
    private String psStockID = "";
    private String psJO = "";
    private boolean pbState = true;
    private boolean pbIsVSPJo = true;
    private final String pxeModuleName = "Sales JO Accessories";
    private boolean pbRequest = false;
    private String psOrigDsc = "";
    private JobOrder oTrans;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06;
    @FXML
    private ComboBox<String> comboBox03;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setIsVSPJo(boolean fbValue) {
        pbIsVSPJo = fbValue;
    }

    public void setObject(JobOrder foValue) {
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
        loadMasterFields();
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        initFields();
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01);
        CustomCommonUtil.setCapsLockBehavior(txtField02);
    }

    private void loadMasterFields() {
        if (oTrans.getJOPartsModel().getJOParts(pnRow).getBarCode() != null) {
            txtField01.setText(String.valueOf(oTrans.getJOPartsModel().getJOParts(pnRow).getBarCode()));
        }
        txtField02.setText(String.valueOf(oTrans.getJOPartsModel().getJOParts(pnRow).getDescript()));
        if (oTrans.getJOPartsModel().getJOParts(pnRow).getPayChrge() != null && !oTrans.getJOPartsModel().getJOParts(pnRow).getPayChrge().isEmpty()) {
            comboBox03.getSelectionModel().select(Integer.parseInt(String.valueOf(oTrans.getJOPartsModel().getJOParts(pnRow).getPayChrge())));
        }
        //compute depends if charge or free
        double lnAccesAmnt = Double.parseDouble(String.valueOf(oTrans.getJOPartsModel().getJOParts(pnRow).getUnitPrce()));;
        int lnAccQuan = Integer.parseInt(String.valueOf(oTrans.getJOPartsModel().getJOParts(pnRow).getQtyEstmt()));
        double lnTotalAmnt = lnAccesAmnt * lnAccQuan;
        txtField04.setText(poGetDecimalFormat.format(lnAccesAmnt));
        txtField05.setText(String.valueOf(lnAccQuan));
        txtField06.setText(poGetDecimalFormat.format(lnTotalAmnt));
        if (oTrans.getJOPartsModel().getJOParts(pnRow).getEntryNo() > 0) {
            txtField02.setDisable(true);
        }
    }

    private void initPatternFields() {
        Pattern patternInt = Pattern.compile("[0-9]*");
        txtField05.setTextFormatter(new TextFormatterUtil(patternInt));
        Pattern patternDec = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(patternDec));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        JSONObject loJSON = new JSONObject();
        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 2:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTrans.getJOPartsModel().getJOParts(pnRow).setDescript(lsValue);
                    break;
                case 4:

                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    oTrans.getJOPartsModel().getJOParts(pnRow).setUnitPrce(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 5:
                    String lsOrigQuan = "0";
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    if (!pbRequest) {
                        lsOrigQuan = String.valueOf(oTrans.getJOPartsModel().getJOParts(pnRow).getQtyEstmt());
                    }
                    loJSON = oTrans.checkVSPJOParts(oTrans.getJOPartsModel().getJOParts(pnRow).getStockID(), Integer.parseInt(lsValue), pnRow, pbState);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        oTrans.getJOPartsModel().getJOParts(pnRow).setQtyEstmt(Integer.parseInt(lsValue));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        oTrans.getJOPartsModel().getJOParts(pnRow).setQtyEstmt(Integer.parseInt(lsOrigQuan));
                    }
                    break;
            }
            loadMasterFields();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
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
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        btnEdit.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
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
                    oTrans.removeJOParts(pnRow);
                }
                CommonUtils.closeStage(btnClose);
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
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTrans.getJOPartsModel().getJOParts(pnRow).setPayChrge(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
                loadMasterFields();
                initFields();
            }
        }
        );
    }

    private void initTextFieldsProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTrans.getJOPartsModel().getJOParts(pnRow).setStockID("");
                    oTrans.getJOPartsModel().getJOParts(pnRow).setBarCode("");
                }
            }
        });
    }

    private void initFields() {
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
            if (!txtField01.getText().isEmpty()) {
                txtField02.setDisable(true);
            }
        }
        if (pbRequest) {
            txtField01.setDisable(false);
            CustomCommonUtil.setDisable(true, txtField02, comboBox03, txtField04, txtField05);
        } else {
            txtField01.setDisable(true);
            CustomCommonUtil.setDisable(false, txtField02, comboBox03, txtField04, txtField05);
            switch (comboBox03.getSelectionModel().getSelectedIndex()) {
                case 0:
                case 1:
                    txtField04.setDisable(false);
                    txtField05.setDisable(txtField04.getText().isEmpty());
                    break;
                default:
                    CustomCommonUtil.setDisable(true, txtField04, txtField05);
                    break;
            }
        }
        if (pbIsVSPJo) {
            CustomCommonUtil.setDisable(false, txtField05);
            CustomCommonUtil.setDisable(true, txtField01, txtField02, comboBox03, txtField04);
        }
    }

    private boolean isValidEntry() {
        if (txtField02.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, "Warning", "Please input Accessories Description");
            txtField02.requestFocus();
            return false;
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (!txtField04.getText().equals(0.00) || !txtField04.getText().equals("0.00")) {
            if (txtField05.getText().equals(0) || txtField05.getText().equals("0")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter quantity higher than zero.");
                return false;
            }
        }
        if (!txtField05.getText().equals(0) || !txtField05.getText().equals("0")) {
            if (txtField04.getText().equals(0.00) || txtField04.getText().equals("0.00")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter amount.");
                return false;
            }
        }
        return true;
    }
}
