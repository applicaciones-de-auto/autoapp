/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Service_Mechanic;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoApp Programmers
 */
public class TechnicianController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Service_Mechanic oTransTechnician;
    private String pxeModuleName = "Technician";
    private int pnEditMode = -1;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnDeactivate, btnActive, btnClose;
    @FXML
    private TextField txtField01, txtField02;
    @FXML
    private CheckBox cboxActivate, cboxBRP, cboxMecha;
    @FXML
    private TextArea textArea03;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransTechnician = new Service_Mechanic(oApp, false, oApp.getBranchCode());
        initFieldAction();
        initButtons();
        initTextKeyPressed();
        initCapitalizationFields();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initFieldAction() {
        cboxMecha.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (cboxMecha.isSelected()) {
                    oTransTechnician.getModel().getModel().setTchSkill("y");
                } else {
                    oTransTechnician.getModel().getModel().setTchSkill("n");
                }
            }
        });
        cboxBRP.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (cboxBRP.isSelected()) {
                    oTransTechnician.getModel().getModel().setBrpSkill("y");
                } else {
                    oTransTechnician.getModel().getModel().setBrpSkill("n");
                }
            }
        });
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransTechnician.getModel().getModel().setClientID("");
                                txtField02.setText("");
                                textArea03.setText("");
                            }
                        }
                    }
                }
                );
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransTechnician.getModel().getModel().setCompnyNm("");
                                txtField01.setText("");
                                textArea03.setText("");
                            }
                        }
                    }
                }
                );
    }

    private void loadTechFields() {
        txtField01.setText(oTransTechnician.getModel().getModel().getClientID());
        txtField02.setText(oTransTechnician.getModel().getModel().getCompnyNm());
        textArea03.setText(oTransTechnician.getModel().getModel().getAddress());
        if (oTransTechnician.getModel()
                .getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        if (oTransTechnician.getModel().getModel().getTchSkill() != null) {
            if (oTransTechnician.getModel().getModel().getTchSkill().equals("y")) {
                cboxMecha.setSelected(true);
            } else {
                cboxMecha.setSelected(false);
            }
        }

        if (oTransTechnician.getModel().getModel().getBrpSkill() != null) {
            if (oTransTechnician.getModel().getModel().getBrpSkill().equals("y")) {
                cboxBRP.setSelected(true);
            } else {
                cboxBRP.setSelected(false);
            }
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        CustomCommonUtil.setCapsLockBehavior(textArea03);
    }

    private boolean checkExistingTechInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransTechnician.validateExistingSM();
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTransTechnician.openRecord((String) loJSON.get("sClientID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadTechFields();
                    pnEditMode = EditMode.READY;
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
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
            if (event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField01":
                        loJSON = oTransTechnician.searchEmployee(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadTechFields();
                            checkExistingTechInformation();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField01.setText("");
                            txtField01.requestFocus();
                            return;
                        }
                        break;
                    case "txtField02":
                        loJSON = oTransTechnician.searchEmployee(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadTechFields();
                            checkExistingTechInformation();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            txtField02.requestFocus();
                            return;
                        }
                        break;

                }
                initFields(pnEditMode);
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.TAB) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            }
        }
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnBrowse, btnCancel,
                btnClose, btnActive, btnDeactivate, btnEdit);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransTechnician = new Service_Mechanic(oApp, false, oApp.getBranchCode());
                loJSON = oTransTechnician.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadTechFields();
                    pnEditMode = oTransTechnician.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Technician Information Saving....", "Are you sure, do you want to save?")) {
                } else {
                    return;
                }

                loJSON = oTransTechnician.saveRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Technician Information", (String) loJSON.get("message"));
                    loJSON = oTransTechnician.openRecord(oTransTechnician.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadTechFields();
                        pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    if (loJSON.get("message").toString().contains("Client ID")) {
                        txtField01.requestFocus();
                    } else {
                        txtField02.requestFocus();
                    }
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransTechnician = new Service_Mechanic(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Technician Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransTechnician.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadTechFields();
                    pnEditMode = EditMode.READY;
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Technician Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnEdit":
                loJSON = oTransTechnician.updateRecord();
                pnEditMode = oTransTechnician.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransTechnician.getModel().getModel().getClientID();
                    loJSON = oTransTechnician.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Technician Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Technician Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransTechnician.openRecord(oTransTechnician.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadTechFields();
                        pnEditMode = pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransTechnician.getModel().getModel().getClientID();
                    loJSON = oTransTechnician.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Technician Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Technician Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransTechnician.openRecord(oTransTechnician.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadTechFields();
                        pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        textArea03.setText("");
        cboxActivate.setSelected(false);
        cboxMecha.setSelected(false);
        cboxBRP.setSelected(false);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        cboxMecha.setDisable(!lbShow);
        cboxBRP.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnActive.setVisible(false);
        btnActive.setManaged(false);
        if (fnValue == EditMode.READY) {
            if (oTransTechnician.getModel().getModel().getRecdStat().equals("1")) {
                btnDeactivate.setVisible(true);
                btnDeactivate.setManaged(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
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
            txtField01.setDisable(true);
            txtField02.setDisable(true);
        }
    }
}
