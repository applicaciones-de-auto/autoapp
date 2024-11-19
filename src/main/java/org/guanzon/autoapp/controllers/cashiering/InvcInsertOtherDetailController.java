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
public class InvcInsertOtherDetailController implements Initializable {

    private GRider oApp;
    private SalesInvoice oTrans;
    private int pnRow;
    private String pxeModuleName = "Other Transaction Details";
    ObservableList<String> cFormType = FXCollections.observableArrayList("SOA", "CAR", "OTHERS", "OTHERS DESCRIPTION");
    @FXML
    private Button btnInsertOther, btnClose;
    @FXML
    private ComboBox<String> comboBox01;
    @FXML
    private TextField txtField02, txtField03;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(SalesInvoice foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initCapitalizationFields();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        loadMasterFields();
        comboBox01.setDisable(true);
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField02, txtField03);
    }

    private void loadMasterFields() {
        txtField02.setText("");
        txtField03.setText("");
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField02":
//                            loJSON = oTrans.searchAcctTitle(lsValue);
//                            if (!"error".equals(loJSON.get("result"))) {
//                                txtField02.setText(oTrans.getModel().getModel().getAcctTitle());
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                txtField02.setText("");
//                                return;
//                            }
                    break;
                case "txtField03":
//                            loJSON = oTrans.searchParticular(lsValue);
//                            if (!"error".equals(loJSON.get("result"))) {
//                                txtField03.setText(oTrans.getModel().getModel().getParticular());
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                txtField03.setText("");
//                                return;
//                            }
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initButtonsClick() {
        btnInsertOther.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnInsertOther":
                CommonUtils.closeStage(btnInsertOther);
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private void initComboBoxItems() {
        comboBox01.setItems(cFormType);
    }

    private void initFieldsAction() {
        comboBox01.setOnAction(event -> {
            if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
//                oTrans.getSIDetailModel().getDetailModel(pnRow).set
            }
        }
        );
    }

    private void initTextFieldsProperty() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isEmpty()) {
//                    oTrans.getMasterModel().getMasterModel().setBuyCltNm("");
            }
        }
        );
        txtField03.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isEmpty()) {
//                    oTrans.getMasterModel().getMasterModel().setBuyCltNm("");
            }
        }
        );
    }
}
