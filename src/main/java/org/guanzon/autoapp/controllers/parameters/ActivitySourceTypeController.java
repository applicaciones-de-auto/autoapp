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
import org.guanzon.auto.main.parameter.Activity_Source;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivitySourceTypeController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Activity_Source oTransActivity;
    private final String pxeModuleName = "Activity Source";
    private int pnEditMode;
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDeactivate;
    @FXML
    private Button btnActive;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TextField txtField03;
    @FXML
    private ComboBox<String> comboBox02;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField03.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransActivity = new Activity_Source(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        initComboBoxAction();
        comboBox02.setItems(cType);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initComboBoxAction() {
        comboBox02.setOnAction(e
                -> {
            int selectedComboBox02 = comboBox02.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox02 >= 0) {
                    switch (selectedComboBox02) {
                        case 0:
                            oTransActivity.getModel().getModel().setEventTyp("eve");
                            break;
                        case 1:
                            oTransActivity.getModel().getModel().setEventTyp("sal");
                            break;
                        case 2:
                            oTransActivity.getModel().getModel().setEventTyp("pro");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        );
    }

    private void loadActivitySource() {
        txtField01.setText(oTransActivity.getModel().getModel().getActTypID());
        if (oTransActivity.getModel().getModel().getEventTyp() != null && !oTransActivity.getModel().getModel().getEventTyp().trim().isEmpty()) {
            switch (String.valueOf(oTransActivity.getModel().getModel().getEventTyp())) {
                case "eve":
                    comboBox02.setValue("EVENT");
                    break;
                case "sal":
                    comboBox02.setValue("SALES CALL");
                    break;
                case "pro":
                    comboBox02.setValue("PROMO");
                    break;
            }
        }
        txtField03.setText(oTransActivity.getModel().getModel().getActTypDs());
        if (oTransActivity.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initTextFieldPattern() {
        Pattern actPat;
        actPat = Pattern.compile("[A-Za-z0-9 ,/'.]*");
        txtField03.setTextFormatter(new InputTextFormatterUtil(actPat));
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
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

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField03);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
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
                case 3:
                    oTransActivity.getModel().getModel().setActTypDs(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransActivity = new Activity_Source(oApp, false, oApp.getBranchCode());
                loJSON = oTransActivity.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadActivitySource();
                    pnEditMode = oTransActivity.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransActivity.updateRecord();
                pnEditMode = oTransActivity.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Activity Source Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField03.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Activity Source Information", "Please enter valid make information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Activity Source Information", "Please enter value make information.");
                        txtField03.setText("");
                        return;
                    }
                    if (!setSelection()) {
                        return;
                    }
                    loJSON = oTransActivity.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Activity Source Information", (String) loJSON.get("message"));
                        loJSON = oTransActivity.openRecord(oTransActivity.getModel().getModel().getActTypID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadActivitySource();
                            initFields(pnEditMode);
                            pnEditMode = oTransActivity.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, "Activity Source Information", (String) loJSON.get("message"));
                        return;
                    }
                }
                break;

            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransActivity = new Activity_Source(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                JSONObject poJSon;
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search  Activity Source Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                poJSon = oTransActivity.searchRecord("", false);
                if ("success".equals((String) poJSon.get("result"))) {
                    loadActivitySource();
                    pnEditMode = oTransActivity.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search  Activity Source Information", (String) poJSon.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransActivity.getModel().getModel().getActTypID();
                    poJSon = oTransActivity.deactivateRecord(fsValue);
                    if ("success".equals((String) poJSon.get("result"))) {
                        ShowMessageFX.Information(null, " Activity Source Information", (String) poJSon.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, " Activity Source Information", (String) poJSon.get("message"));
                    }
                    loJSON = oTransActivity.openRecord(oTransActivity.getModel().getModel().getActTypID()
                    );
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadActivitySource();
                        initFields(pnEditMode);
                        pnEditMode = oTransActivity.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransActivity.getModel().getModel().getActTypID();
                    poJSon = oTransActivity.activateRecord(fsValue);
                    if ("success".equals((String) poJSon.get("result"))) {
                        ShowMessageFX.Information(null, " Activity Source Information", (String) poJSon.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, " Activity Source Information", (String) poJSon.get("message"));
                    }
                    loJSON = oTransActivity.openRecord(oTransActivity.getModel().getModel().getActTypID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadActivitySource();
                        initFields(pnEditMode);
                        pnEditMode = oTransActivity.getEditMode();
                    }
                }
                break;
            default:
                ShowMessageFX.Warning("Please contact admin to assist about no button available", "Integrated Automotive System", pxeModuleName);
                break;
        }
        initFields(pnEditMode);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Activity Source Type", "Please select `Activity Source Type` value.");
                return false;
            } else {
                switch (String.valueOf(oTransActivity.getModel().getModel().getEventTyp())) {
                    case "eve":
                        comboBox02.setValue("EVENT");
                        break;
                    case "sal":
                        comboBox02.setValue("SALES CALL");
                        break;
                    case "pro":
                        comboBox02.setValue("PROMO");
                        break;
                }
            }
        }
        return true;
    }

    private void clearFields() {
        cboxActivate.setSelected(false);
        txtField01.clear();
        comboBox02.setValue("");
        txtField03.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        comboBox02.setDisable(!lbShow);
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
            if (oTransActivity.getModel().getModel().getRecdStat().equals("1")) {
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
}
