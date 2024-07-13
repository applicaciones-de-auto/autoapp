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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_ModelEnginePattern;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleEngineFormatEntryController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_ModelEnginePattern oTransEngineFormat;
    private final String pxeModuleName = "Vehicle Engine Format";
    private int pnEditMode;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        oTransEngineFormat = new Vehicle_ModelEnginePattern(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    private void loadEngineFormatFields() {
//        txtField01.setText(oTransEngineFormat.getModel().getModel().getVehicleMake());
//        txtField02.setText(oTransEngineFormat.getModel().getModel().getVehicleModel());
//        txtField03.setText(oTransEngineFormat.getModel().getModel().getVehicleEngineFormatPatt());
//        txtField04.setText(oTransEngineFormat.getModel().getModel().getVehicleEngineFormatLength));
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z ]*");
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new InputTextFormatterUtil(textOnly));

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        /*TextArea*/
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
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
//                    oTransEngineFormatEngineFormat.getModel().getModel().setVehicleEngineFormatDesc(lsValue);
                    break;
                case 4:
//                    oTransEngineFormatEngineFormat.getModel().getModel().setVehicleEngineFormatCode(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject poJson;
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
//                clearFields();
//                oTransEngineFormat = new Vehicle_EngineFormat(oApp, false, oApp.getBranchCode());
//                poJson = oTransEngineFormat.newRecord();
//                if ("success".equals((String) poJson.get("result"))) {
//                    loadEngineFormatFields();
////                    pnEditMode = oTransEngineFormat.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, (String) poJson.get("message"));
//                }
                break;
            case "btnEdit":
//                poJson = oTransEngineFormat.updateRecord();
//                pnEditMode = oTransEngineFormat.getEditMode();
//                if ("error".equals((String) poJson.get("result"))) {
//                    ShowMessageFX.Warning((String) poJson.get("message"), "Warning", null);
//                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle EngineFormat Information Saving....", "Are you sure, do you want to save?")) {
//                    poJson = oTransEngineFormat.saveRecord();
//                    if ("success".equals((String) poJson.get("result"))) {
//                        ShowMessageFX.Information(null, "Vehicle EngineFormat Information", (String) poJson.get("message"));
//                        poJson = oTransEngineFormat.openRecord(oTransEngineFormat.getModel().getModel().getClientID());
//                        if ("success".equals((String) poJson.get("result"))) {
//                            loadEngineFormatFields();
//                            initFields(pnEditMode);
//                            pnEditMode = oTransEngineFormat.getEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) poJson.get("message"));
//                        return;
//                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
//                    oTranEngineFormat = new Vehicle_EngineFormat(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                JSONObject poJSon;
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle EngineFormat Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
//                poJSon = oTransEngineFormat.searchRecord("", false);
//                if ("success".equals((String) poJSon.get("result"))) {
//                    loadEngineFormatFields();
////                    pnEditMode = oTransEngineFormatEngineFormat.getEditMode();
//                    initFields(pnEditMode);
//                } else {
//                    ShowMessageFX.Warning(null, "Search Vehicle EngineFormat Information", (String) poJSon.get("message"));
//                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
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
        txtField04.setText("0");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
    }
}