/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parameters;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_Make;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleMakeEntryController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_Make oTransMake;
    private final String pxeModuleName = "Vehicle Make";
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
    private Button btnDeactivate;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private Button btnActive;

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
        oTransMake = new Vehicle_Make(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadMakeFields() {
        txtField01.setText(oTransMake.getModel().getModel().getMakeID());
        txtField02.setText(oTransMake.getModel().getModel().getMakeDesc());
        if (oTransMake.getModel().getModel().getRecdStat().equals("1")) {
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
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
                    oTransMake.getModel().getModel().setMakeDesc(lsValue);
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
        JSONObject poJson;
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransMake = new Vehicle_Make(oApp, false, oApp.getBranchCode());
                poJson = oTransMake.newRecord();
                if ("success".equals((String) poJson.get("result"))) {
                    loadMakeFields();
                    pnEditMode = oTransMake.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) poJson.get("message"));
                }
                break;
            case "btnEdit":
                poJson = oTransMake.updateRecord();
                pnEditMode = oTransMake.getEditMode();
                if ("error".equals((String) poJson.get("result"))) {
                    ShowMessageFX.Warning((String) poJson.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Make Information Saving....", "Are you sure, do you want to save?")) {
                    poJson = oTransMake.saveRecord();
                    if ("success".equals((String) poJson.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Make Information", (String) poJson.get("message"));
                        poJson = oTransMake.openRecord(oTransMake.getModel().getModel().getMakeID());
                        if ("success".equals((String) poJson.get("result"))) {
                            loadMakeFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransMake.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) poJson.get("message"));
                        return;
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransMake = new Vehicle_Make(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                JSONObject poJSon;
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Make Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                poJSon = oTransMake.searchRecord("", false);
                if ("success".equals((String) poJSon.get("result"))) {
                    loadMakeFields();
                    pnEditMode = oTransMake.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Make Information", (String) poJSon.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransMake.getModel().getModel().getMakeID();
                    poJSon = oTransMake.deactivateRecord(fsValue);
                    if ("success".equals((String) poJSon.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Make Information", (String) poJSon.get("message"));
                        poJson = oTransMake.openRecord(oTransMake.getModel().getModel().getMakeID());
                        if ("success".equals((String) poJson.get("result"))) {
                            loadMakeFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransMake.getEditMode();
                        }
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransMake.getModel().getModel().getMakeID();
                    poJSon = oTransMake.activateRecord(fsValue);
                    if ("success".equals((String) poJSon.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Make Information", (String) poJSon.get("message"));
                        poJson = oTransMake.openRecord(oTransMake.getModel().getModel().getMakeID());
                        if ("success".equals((String) poJson.get("result"))) {
                            loadMakeFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransMake.getEditMode();
                        }
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
            if (oTransMake.getModel().getModel().getRecdStat().equals("1")) {
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
