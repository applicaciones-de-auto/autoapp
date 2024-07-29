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
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Bank;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class BankEntryController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Bank Entry";
    private int pnEditMode;//Modifying fields
    private Bank oTransBank;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnBrowse, btnClose;
    ObservableList<String> cBankType = FXCollections.observableArrayList("BANK", "CREDIT UNION", "INSURANCE COMPANY", "INVESTMENT COMPANIES");
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField03;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private Button btnActive;
    @FXML
    private TextField txtField04;
    @FXML
    private ComboBox<String> comboBox02;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField04.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        oTransBank = new Bank(oApp, false, oApp.getBranchCode());

        InputTextUtil.addTextLimiter(txtField03, 10);

        pnEditMode = EditMode.UNKNOWN;

        initTextKeyPressed();
        comboBox02.setItems(cBankType);
        comboBox02.setOnAction(e -> {
            int selectedComboBox02 = comboBox02.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                if (selectedComboBox02 >= 0) {
//                    switch (selectedComboBox02) {
//                        case 0:
//                            oTransBank.getModel().getModel().setBankType("BANK");
//                            break;
//                        case 1:
//                            oTransBank.getModel().getModel().setBankType("CRED");
//                            break;
//                        case 2:
//                            oTransBank.getModel().getModel().setBankType("INSC");
//                            break;
//                        case 3:
//                            oTransBank.getModel().getModel().setBankType("INVC");
//                            break;
//                        default:
//                            break;
//                    }
//                }
                initFields(pnEditMode);
            }
        });
        initTextFieldPattern();

        initCapitalizationFields();

        initTextFieldFocus();

        initButtons();

        clearFields();

        initFields(pnEditMode);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Bank Type", "Please select `Bank Type` value.");
                return false;
            } else {
                switch (comboBox02.getSelectionModel().getSelectedIndex()) {
//                    case 0:
//                        oTransBank.getModel().getModel().setBankType("BANK");
//                        break;
//                    case 1:
//                        oTransBank.getModel().getModel().setBankType("CRED");
//                        break;
//                    case 2:
//                        oTransBank.getModel().getModel().setBankType("INSC");
//                        break;
//                    case 3:
//                        oTransBank.getModel().getModel().setBankType("INVC");
//                        break;
//                    default:
//                        break;
                }
            }
        }
        return true;
    }

    private void initTextFieldPattern() {
        Pattern Bank;
        Bank = Pattern.compile("[A-Za-z 0-9]*");
        txtField03.setTextFormatter(new InputTextFormatterUtil(Bank));
        txtField04.setTextFormatter(new InputTextFormatterUtil(Bank));

    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField04);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
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
                case 3:
                    oTransBank.getModel().getModel().setBankName(lsValue);
                    break;
                case 4:
                    oTransBank.getModel().getModel().setBankCode(lsValue);
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
                oTransBank = new Bank(oApp, false, oApp.getBranchCode());
                loJSON = oTransBank.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadBankField();
                    pnEditMode = oTransBank.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));

                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, " Bank Information Saving....", "Are you sure, do you want to save?")) {
                    if (!setSelection()) {
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, "Bank Information", "Please enter valid bank name information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Bank Information", "Please enter value bank name information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField04.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, " Bank Information", "Please enter valid bank code information.");
                        txtField04.setText("");
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Bank Information", "Please enter value bank code information.");
                        txtField04.setText("");
                        return;
                    }
                    loJSON = oTransBank.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                        loJSON = oTransBank.openRecord(oTransBank.getModel().getModel().getBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadBankField();
                            initFields(pnEditMode);
                            pnEditMode = oTransBank.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, "Bank Information", (String) loJSON.get("message"));
                        return;
                    }
                }
                break;
            case "btnEdit":
                loJSON = oTransBank.updateRecord();
                pnEditMode = oTransBank.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransBank = new Bank(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransBank.getModel().getModel().getBankID();
                    loJSON = oTransBank.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Bank Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransBank.openRecord(oTransBank.getModel().getModel().getBankID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadBankField();
                        initFields(pnEditMode);
                        pnEditMode = oTransBank.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransBank.getModel().getModel().getBankID();
                    loJSON = oTransBank.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Bank Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransBank.openRecord(oTransBank.getModel().getModel().getBankID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadBankField();
                        initFields(pnEditMode);
                        pnEditMode = oTransBank.getEditMode();
                    }
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Bank Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransBank.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadBankField();
                    pnEditMode = oTransBank.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Bank Information", (String) loJSON.get("message"));
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
        txtField01.setDisable(true);
        comboBox02.setDisable(!lbShow);
        txtField03.setDisable(!(lbShow && !comboBox02.getValue().isEmpty()));
        txtField04.setDisable(!(lbShow && !comboBox02.getValue().isEmpty()));
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
            if (oTransBank.getModel().getModel().getRecdStat().equals("1")) {
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

    private void loadBankField() {
        txtField01.setText(oTransBank.getModel().getModel().getBankID());
//        if (oTransBank.getModel().getModel().getBankType() != null && !oTransBank.getModel().getModel().getBankType().trim().isEmpty()) {
//            switch ((String.valueOf(oTransBank.getModel().getModel().getBankType()))) {
//                case "BANK":
//                    comboBox02.setValue("BANK");
//                    break;
//                case "CRED":
//                    comboBox02.setValue("CREDIT UNION");
//                    break;
//                case "INSC":
//                    comboBox02.setValue("INSURANCE COMPANY");
//                    break;
//                case "INVC":
//                    comboBox02.setValue("INVESTMENT COMPANIES");
//                    break;
//                default:
//                    break;
//            }
//        }
        txtField03.setText(oTransBank.getModel().getModel().getBankName());

        txtField04.setText(oTransBank.getModel().getModel().getBankCode());
        if (oTransBank.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void clearFields() {
        txtField01.clear();
        comboBox02.setValue("");
        txtField03.clear();
        txtField04.clear();
        cboxActivate.setSelected(false);
    }
}
