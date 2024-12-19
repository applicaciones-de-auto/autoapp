/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
public class UpdateDeductiblesController implements Initializable {

    private GRider oApp;
    private SalesInvoice oTrans;
    private String pxeModuleName = "Update Deductibles";
    private int pnRow;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnUpdate, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(SalesInvoice foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnValue) {
        pnRow = fnValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMasterFields();
        initButtonsClick();
        initCapitalizationFields();
        initTextFieldFocus();
        initTextKeyPressed();
    }

    private void loadMasterFields() {
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01, txtField02, txtField03);
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(
                txtField03, txtField04);
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
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
            }
            loadMasterFields();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnUpdate, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    public void handleButtonAction(ActionEvent event) {
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
}
