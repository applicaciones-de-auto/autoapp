/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import java.net.URL;
import java.text.DecimalFormat;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.cashiering.SalesInvoice;
import org.guanzon.autoapp.utils.CustomCommonUtil;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InvcInsertDetailController implements Initializable {

    private GRider oApp;
    private SalesInvoice oTrans;
    private String pxeModuleName = "Transaction Details";
    private String psFormType = "";
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private int pnRow;
    @FXML
    private Button btnUpdate, btnClose;
    @FXML
    private TextField txtField02, txtField03, txtField04, txtField05, txtField06, txtField07, txtField08;
    @FXML
    private TextField txtField01;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(SalesInvoice foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setFormType(String fsValue) {
        psFormType = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initTextFieldsProperty();
        loadMasterFields();
        initFields();
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField02, txtField03, txtField04);
    }

    private void loadMasterFields() {
        txtField01.setText("");
        txtField02.setText(oTrans.getSIDetailModel().getDetailModel(pnRow).getTranType());
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText(CustomCommonUtil.setDecimalFormat(oTrans.getSIDetailModel().getDetailModel(pnRow).getTranAmt()));
        txtField06.setText("0.00");
        txtField07.setText("0.00");
        txtField08.setText(CustomCommonUtil.setDecimalFormat(oTrans.getSIDetailModel().getDetailModel(pnRow).getNetAmt()));
    }

    private void initPatternFields() {
        CustomCommonUtil.inputDecimalOnly(txtField05, txtField06, txtField07);
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06, txtField07);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 2:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    break;
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
                case 6:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
                case 7:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
            }
            loadMasterFields();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06, txtField07);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
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
        btnUpdate.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnUpdate":
                CommonUtils.closeStage(btnUpdate);
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private void initTextFieldsProperty() {
    }

    private void initFields() {
        CustomCommonUtil.setDisable(true, txtField02, txtField03, txtField04, txtField05, txtField06, txtField07);
    }

    private boolean isValidEntry() {
        if (txtField05.getText().equals(0.00) || txtField05.getText().equals("0.00")) {
            ShowMessageFX.Warning(null, "Warning", "Please enter gross Amount.");
            return false;
        }
        return true;
    }

}
