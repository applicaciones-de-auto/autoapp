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
import org.guanzon.auto.main.parameter.Vehicle_Model;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class VehicleModelEntryController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_Model oTransModel;
    private final String pxeModuleName = "Vehicle Model";
    private int pnEditMode;

    private String sMakeID = "";
    private String sMakeDesc = "";
    private boolean pbOpenEvent = false;

    ObservableList<String> cUnitType = FXCollections.observableArrayList("COMMERCIAL VEHICLE", "PRIVATE VEHICLE", "LIGHT PRIVATE VEHICLE", "MEDIUM PRIVATE VEHICLE");
    ObservableList<String> cBodyType = FXCollections.observableArrayList("SEDAN", "SUV", "HATCHBACK", "MPV", "MOTORCYCLE", "TRUCK");
    ObservableList<String> cUnitSize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnBrowse, btnClose;
    @FXML
    private TextField txtField03;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField01;
    @FXML
    private ComboBox<String> comboBox05;
    @FXML
    private ComboBox<String> comboBox06;
    @FXML
    private ComboBox<String> comboBox04;
    @FXML
    private Button btnActive;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    public void setMakeID(String fsValue) {
        sMakeID = fsValue;
    }

    public void setMakeDesc(String fsValue) {
        sMakeDesc = fsValue;
    }

    public Boolean setOpenEvent(Boolean fbValue) {
        pbOpenEvent = fbValue;
        return pbOpenEvent;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransModel = new Vehicle_Model(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        comboBox04.setItems(cUnitType);
        comboBox05.setItems(cBodyType);
        comboBox06.setItems(cUnitSize);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadModelFields() {
        txtField01.setText(oTransModel.getModel().getModel().getModelID());
        txtField02.setText(oTransModel.getModel().getModel().getMakeDesc());
        txtField03.setText(oTransModel.getModel().getModel().getModelDsc());

        if (oTransModel.getModel().getModel().getUnitType() != null && !oTransModel.getModel().getModel().getUnitType().trim().isEmpty()) {
            comboBox04.getSelectionModel().select(Integer.parseInt(oTransModel.getModel().getModel().getUnitType()));
        }
        if (oTransModel.getModel().getModel().getBodyType() != null && !oTransModel.getModel().getModel().getBodyType().trim().isEmpty()) {
            comboBox05.getSelectionModel().select(Integer.parseInt(oTransModel.getModel().getModel().getBodyType()));
        }
        if (oTransModel.getModel().getModel().getVhclSize() != null && !oTransModel.getModel().getModel().getVhclSize().trim().isEmpty()) {
            comboBox06.getSelectionModel().select(Integer.parseInt(oTransModel.getModel().getModel().getVhclSize()));
        }
        if (oTransModel.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z -]*");
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new InputTextFormatterUtil(textOnly));
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            TextField lsTxtField = (TextField) event.getSource();
            String txtFieldID = ((TextField) event.getSource()).getId();
            String lsValue = "";
            if (lsTxtField.getText() == null) {
                lsValue = "";
            } else {
                lsValue = lsTxtField.getText();
            }
            JSONObject loJSON = new JSONObject();
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField02":
                        loJSON = oTransModel.searchMake(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField02.setText(oTransModel.getModel().getModel().getMakeDesc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            txtField02.requestFocus();
                            return;
                        }
                        break;
                }

                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            }
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
                    oTransModel.getModel().getModel().setModelDsc(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initCmboxFieldAction() {
        comboBox04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransModel.getModel().getModel().setUnitType(String.valueOf((comboBox04.getSelectionModel().getSelectedIndex())));
                }
            }
        });
        comboBox05.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox05.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransModel.getModel().getModel().setBodyType(String.valueOf((comboBox05.getSelectionModel().getSelectedIndex())));
                }
            }
        });
        comboBox06.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox06.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransModel.getModel().getModel().setVhclSize(String.valueOf((comboBox06.getSelectionModel().getSelectedIndex())));
                }
            }
        });
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransModel.getModel().getModel().setMakeID("");
                            }
                        }
                    }
                }
                );
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox04.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Type", "Please select `Vehicle Type` value.");
                return false;
            } else {
                oTransModel.getModel().getModel().setUnitType(String.valueOf((comboBox04.getSelectionModel().getSelectedIndex())));
            }
            if (comboBox05.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Body Type", "Please select `Vehicle Body Type` value.");
                return false;
            } else {
                oTransModel.getModel().getModel().setBodyType(String.valueOf((comboBox05.getSelectionModel().getSelectedIndex())));
            }
            if (comboBox06.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle  Model Size", "Please select `Vehicle  Model Size` value.");
                return false;
            } else {
                oTransModel.getModel().getModel().setVhclSize(String.valueOf((comboBox06.getSelectionModel().getSelectedIndex())));
            }
        }
        return true;
    }

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
                oTransModel = new Vehicle_Model(oApp, false, oApp.getBranchCode());
                loJSON = oTransModel.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    if (pbOpenEvent) {
                        txtField02.setText(sMakeDesc);
                        oTransModel.getModel().getModel().setMakeID(sMakeID);
                        oTransModel.getModel().getModel().setMakeDesc(sMakeDesc);
                    }
                    loadModelFields();
                    pnEditMode = oTransModel.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnEdit":
                loJSON = oTransModel.updateRecord();
                pnEditMode = oTransModel.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Model Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please enter value for make.");
                        txtField02.requestFocus();
                        return;
                    }

                    if (txtField03 == null || txtField03.getText() == null || txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please enter value for model description.");
                        if (txtField03 != null) {
                            txtField03.requestFocus();
                        }
                        return;
                    }
                    if (setSelection()) {
                        loJSON = oTransModel.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Vehicle Model Information", (String) loJSON.get("message"));
                            loJSON = oTransModel.openRecord(oTransModel.getModel().getModel().getModelID());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loadModelFields();
                                initFields(pnEditMode);
                                pnEditMode = oTransModel.getEditMode();
                            }
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransModel = new Vehicle_Model(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Model Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransModel.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadModelFields();
                    pnEditMode = oTransModel.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Model Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransModel.getModel().getModel().getModelID();
                    loJSON = oTransModel.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransModel.openRecord(oTransModel.getModel().getModel().getModelID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadModelFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransModel.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransModel.getModel().getModel().getModelID();
                    loJSON = oTransModel.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransModel.openRecord(oTransModel.getModel().getModel().getModelID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadModelFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransModel.getEditMode();
                    }
                }
                break;
            default:
                ShowMessageFX.Warning("Please contact admin to assist about no button available", "Integrated Automotive System", pxeModuleName);
                break;
        }
        initFields(pnEditMode);
    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        comboBox04.setValue(null);
        comboBox05.setValue(null);
        comboBox06.setValue(null);
        cboxActivate.setSelected(false);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        if (pbOpenEvent) {
            txtField03.setDisable(true);
        } else {
            txtField03.setDisable(!(fnValue == EditMode.ADDNEW));
        }
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        comboBox04.setDisable(!lbShow);
        comboBox05.setDisable(!lbShow);
        comboBox06.setDisable(!lbShow);
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
            //show edit if user clicked save / browse
            if (oTransModel.getModel().getModel().getRecdStat().equals("1")) {
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
        if (fnValue == EditMode.UPDATE) {
            txtField02.setDisable(true);
        }
    }
}
