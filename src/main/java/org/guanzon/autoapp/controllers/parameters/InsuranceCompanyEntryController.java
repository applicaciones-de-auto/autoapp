/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parameters;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Insurance;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InsuranceCompanyEntryController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Insurance Entry";
    private int pnEditMode;//Modifying fields
    private Insurance oTransInsurance;
    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnDeactivate, btnBrowse, btnClose, btnActive, btnSave;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private CheckBox cboxActivate;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        oTransInsurance = new Insurance(oApp, false, oApp.getBranchCode());

        InputTextUtil.addTextLimiter(txtField03, 10);

        initTextKeyPressed();

        initTextFieldPattern();

        initCapitalizationFields();

        initTextFieldFocus();

        initButtons();

        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initTextFieldPattern() {
        Pattern Insurance;
        Insurance = Pattern.compile("[A-Za-z 0-9]*");
        txtField02.setTextFormatter(new InputTextFormatterUtil(Insurance));
        txtField03.setTextFormatter(new InputTextFormatterUtil(Insurance));

    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText().toUpperCase();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /* Lost Focus */
            switch (lnIndex) {
                case 2:
                    oTransInsurance.getModel().getModel().setInsurNme(lsValue);
                    break;
                case 3:
                    oTransInsurance.getModel().getModel().setInsurCde(lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();

        }
    };

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnActive, btnBrowse, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransInsurance = new Insurance(oApp, false, oApp.getBranchCode());
                loJSON = oTransInsurance.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadInsuranceField();
                    pnEditMode = oTransInsurance.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, " Insurance Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, "Insurance Information", "Please enter valid insurance company name information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Insurance Information", "Please enter value insurance company name information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, " Insurance Information", "Please enter valid insurance company code information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Insurance Information", "Please enter value insurance company code information.");
                        txtField03.setText("");
                        return;
                    }
                    loJSON = oTransInsurance.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Information", (String) loJSON.get("message"));
                        loJSON = oTransInsurance.openRecord(oTransInsurance.getModel().getModel().getInsurID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadInsuranceField();
                            initFields(pnEditMode);
                            pnEditMode = oTransInsurance.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Information", (String) loJSON.get("message"));
                        return;
                    }
                }
                break;
            case "btnEdit":
                loJSON = oTransInsurance.updateRecord();
                pnEditMode = oTransInsurance.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransInsurance = new Insurance(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransInsurance.getModel().getModel().getInsurID();
                    loJSON = oTransInsurance.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransInsurance.openRecord(oTransInsurance.getModel().getModel().getInsurID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadInsuranceField();
                        initFields(pnEditMode);
                        pnEditMode = oTransInsurance.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransInsurance.getModel().getModel().getInsurID();
                    loJSON = oTransInsurance.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Insurance Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Insurance Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransInsurance.openRecord(oTransInsurance.getModel().getModel().getInsurID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadInsuranceField();
                        initFields(pnEditMode);
                        pnEditMode = oTransInsurance.getEditMode();
                    }
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Insurance Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransInsurance.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadInsuranceField();
                    pnEditMode = oTransInsurance.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Insurance Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initFields(pnEditMode);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnActive.setVisible(false);
        btnActive.setManaged(false);
        if (fnValue == EditMode.READY) {
            if (oTransInsurance.getModel().getModel().getRecdStat().equals("1")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnDeactivate.setVisible(true);
                btnDeactivate.setManaged(true);
                btnActive.setVisible(false);
                btnActive.setManaged(false);
            } else {
                btnDeactivate.setVisible(false);
                btnDeactivate.setManaged(false);
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
    }

    private void loadInsuranceField() {
        txtField01.setText(oTransInsurance.getModel().getModel().getInsurID());
        txtField02.setText(oTransInsurance.getModel().getModel().getInsurNme());
        txtField03.setText(oTransInsurance.getModel().getModel().getInsurCde());
        if (oTransInsurance.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        cboxActivate.setSelected(false);
    }
}
