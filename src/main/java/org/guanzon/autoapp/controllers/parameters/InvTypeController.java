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
import org.guanzon.auto.main.parameter.Parts_InventoryType;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InvTypeController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Inventory Type Entry";
    private int pnEditMode;//Modifying fields
    private Parts_InventoryType oTransInventoryType;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnDeactivate, btnClose, btnActive;
    @FXML
    private TextField txtField01, txtField02;
    @FXML
    private CheckBox cboxActivate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransInventoryType = new Parts_InventoryType(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadInventoryTypeFields() {
        txtField01.setText(oTransInventoryType.getModel().getModel().getInvTypCd());
        txtField02.setText(oTransInventoryType.getModel().getModel().getDescript());
        if (oTransInventoryType.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z 0-9]*");
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly));

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        String textFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (textFieldID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        }
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField02);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        JSONObject loJSON = new JSONObject();
        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2:
                    loJSON = oTransInventoryType.getModel().getModel().setDescript(lsValue);
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
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
                oTransInventoryType = new Parts_InventoryType(oApp, false, oApp.getBranchCode());
                loJSON = oTransInventoryType.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadInventoryTypeFields();
                    pnEditMode = oTransInventoryType.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransInventoryType.updateRecord();
                pnEditMode = oTransInventoryType.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Inventory Type Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Inventory Type Information", "Please enter valid inventory type information.");
                        txtField02.setText("");
                        return;
                    }
                    loJSON = oTransInventoryType.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Type Information", (String) loJSON.get("message"));
                        loJSON = oTransInventoryType.openRecord(oTransInventoryType.getModel().getModel().getInvTypCd());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadInventoryTypeFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransInventoryType.getEditMode();
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
                    oTransInventoryType = new Parts_InventoryType(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Inventory Type Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransInventoryType.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadInventoryTypeFields();
                    pnEditMode = oTransInventoryType.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Inventory Type Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransInventoryType.getModel().getModel().getInvTypCd();
                    loJSON = oTransInventoryType.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Inventory Type Information", (String) loJSON.get("message"));
                        return;
                    }
                    loJSON = oTransInventoryType.openRecord(oTransInventoryType.getModel().getModel().getInvTypCd());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadInventoryTypeFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransInventoryType.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransInventoryType.getModel().getModel().getInvTypCd();
                    loJSON = oTransInventoryType.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Inventory Type Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransInventoryType.openRecord(oTransInventoryType.getModel().getModel().getInvTypCd());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadInventoryTypeFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransInventoryType.getEditMode();
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
        cboxActivate.setSelected(false);
        txtField01.clear();
        txtField02.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
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
            if (oTransInventoryType.getModel().getModel().getRecdStat().equals("1")) {
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
