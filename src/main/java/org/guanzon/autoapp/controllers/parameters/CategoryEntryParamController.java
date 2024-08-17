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
import org.guanzon.auto.main.parameter.Parts_InventoryCategory;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class CategoryEntryParamController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Category Entry";
    private int pnEditMode;//Modifying fields
    private Parts_InventoryCategory oTransCategory;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnDeactivate, btnClose, btnActive;
    @FXML
    private TextField txtField01, txtField02, txtField03;
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
        oTransCategory = new Parts_InventoryCategory(oApp, false, oApp.getBranchCode());

        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        InputTextUtil.addTextLimiter(txtField02, 30);
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransCategory.getModel().getModel().setInvTypCd("");
                            }
                        }
                    }
                }
                );
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadCategoryFields() {
        txtField01.setText(oTransCategory.getModel().getModel().getCategrCd());
        txtField02.setText(oTransCategory.getModel().getModel().getDescript());
        txtField03.setText(oTransCategory.getModel().getModel().getInvTypDs());
        if (oTransCategory.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z ]*");
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly));
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
                    case "txtField03":
                        loJSON = oTransCategory.searchInvType(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField03.setText(oTransCategory.getModel().getModel().getInvTypDs());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField03.setText("");
                            txtField03.requestFocus();
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
            } else if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            }
        }
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField02);
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
                case 2:
                    oTransCategory.getModel().getModel().setDescript(lsValue);
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
                oTransCategory = new Parts_InventoryCategory(oApp, false, oApp.getBranchCode());
                loJSON = oTransCategory.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadCategoryFields();
                    pnEditMode = oTransCategory.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransCategory.updateRecord();
                pnEditMode = oTransCategory.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Category Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Category Information", "Please enter valid category information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Category Information", "Please enter value category information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Category Information", "Please enter valid category abbrev information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Category Information", "Please enter value category abbrev information.");
                        txtField03.setText("");
                        return;
                    }
                    loJSON = oTransCategory.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Category Information", (String) loJSON.get("message"));
                        loJSON = oTransCategory.openRecord(oTransCategory.getModel().getModel().getCategrCd());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadCategoryFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransCategory.getEditMode();
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
                    oTransCategory = new Parts_InventoryCategory(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Category Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransCategory.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadCategoryFields();
                    pnEditMode = oTransCategory.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Category Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransCategory.getModel().getModel().getCategrCd();
                    loJSON = oTransCategory.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Category Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Category Information", (String) loJSON.get("message"));
                        return;
                    }
                    loJSON = oTransCategory.openRecord(oTransCategory.getModel().getModel().getCategrCd());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadCategoryFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransCategory.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransCategory.getModel().getModel().getCategrCd();
                    loJSON = oTransCategory.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Category Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Category Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransCategory.openRecord(oTransCategory.getModel().getModel().getCategrCd());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadCategoryFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransCategory.getEditMode();
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
        txtField03.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
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
            //show edit if user clicked save / browse
            if (oTransCategory.getModel().getModel().getRecdStat().equals("1")) {
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
