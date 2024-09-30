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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_ModelEnginePattern;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class VehicleEngineFormatController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_ModelEnginePattern oTransEngineFormat;
    private final String pxeModuleName = "Vehicle Engine Format";
    private String psMakeID, psMakeDesc;
    private String psModelID, psModelDesc;
    private int pnEditMode;
    private boolean pbOpenEvent = false;
    private boolean pbState;
    private int pnEditModeValue;
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

    public String setMakeID(String fsValue) {
        psMakeID = fsValue;
        return psMakeID;
    }

    public String setMakeDesc(String fsValue) {
        psMakeDesc = fsValue;
        return psMakeDesc;
    }

    public String setModelID(String fsValue) {
        psModelID = fsValue;
        return psModelID;
    }

    public String setModelDesc(String fsValue) {
        psModelDesc = fsValue;
        return psModelDesc;
    }

    public Boolean setOpenEvent(Boolean fbValue) {
        pbOpenEvent = fbValue;
        return pbOpenEvent;
    }

    public Boolean setState(Boolean fbState) {
        pbState = fbState;
        return pbState;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        oTransEngineFormat = new Vehicle_ModelEnginePattern(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransEngineFormat.getModel().getModel().setMakeID("");
                                oTransEngineFormat.getModel().getModel().setModelID("");
                                txtField02.setText("");
                            }
                        }
                    }
                }
                );
        CustomCommonUtil.addTextLimiter(txtField03, 3);
        CustomCommonUtil.addTextLimiter(txtField04, 4);
        initFields(pnEditMode);

    }

    private void loadEngineFormatFields() {
        txtField01.setText(oTransEngineFormat.getModel().getModel().getMakeDesc());
        txtField02.setText(oTransEngineFormat.getModel().getModel().getModelDsc());
        txtField03.setText(oTransEngineFormat.getModel().getModel().getEngnPtrn());
        if (oTransEngineFormat.getModel().getModel().getEngnLen() == null) {
            txtField04.setText("0");
        } else {
            txtField04.setText(String.valueOf(oTransEngineFormat.getModel().getModel().getEngnLen()));
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly, numOnly, engPat;
        textOnly = Pattern.compile("[A-Za-z -]*");
        engPat = Pattern.compile("[A-Za-z0-9-]*");
        numOnly = Pattern.compile("[0-9]*");
        txtField02.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new TextFormatterUtil(engPat));
        txtField04.setTextFormatter(new TextFormatterUtil(numOnly));

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04);
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
                    case "txtField01":
                        loJSON = oTransEngineFormat.searchMake(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField01.setText(oTransEngineFormat.getModel().getModel().getMakeDesc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField01.setText("");
                            txtField01.requestFocus();
                            return;
                        }
                        break;
                    case "txtField02":
                        loJSON = oTransEngineFormat.searchModel(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField02.setText(oTransEngineFormat.getModel().getModel().getModelDsc());
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
            } else if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            }
        }

    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
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
                    oTransEngineFormat.getModel().getModel().setEngnPtrn(lsValue);
                    break;
                case 4:
                    oTransEngineFormat.getModel().getModel().setEngnLen(Integer.valueOf(lsValue));
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
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransEngineFormat = new Vehicle_ModelEnginePattern(oApp, false, oApp.getBranchCode());
                loJSON = oTransEngineFormat.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    if (pbOpenEvent) {
                        oTransEngineFormat.getModel().getModel().setMakeID(psMakeID);
                        oTransEngineFormat.getModel().getModel().setMakeDesc(psMakeDesc);
                        oTransEngineFormat.getModel().getModel().setModelID(psModelID);
                        oTransEngineFormat.getModel().getModel().setModelDsc(psModelDesc);
                    }
                    loadEngineFormatFields();
                    pnEditMode = oTransEngineFormat.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransEngineFormat.updateRecord();
                pnEditMode = oTransEngineFormat.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle EngineFormat Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTransEngineFormat.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle EngineFormat Information", (String) loJSON.get("message"));
                        loJSON = oTransEngineFormat.openRecord(oTransEngineFormat.getModel().getModel().getModelID(), oTransEngineFormat.getModel().getModel().getEntryNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadEngineFormatFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransEngineFormat.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransEngineFormat = new Vehicle_ModelEnginePattern(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle EngineFormat Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransEngineFormat.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadEngineFormatFields();
                    pnEditMode = oTransEngineFormat.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle EngineFormat Information", (String) loJSON.get("message"));
                }
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
        txtField01.setDisable(!lbShow);
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
        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
            txtField02.setDisable(true);
            txtField03.setDisable(true);
        }
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }
}
