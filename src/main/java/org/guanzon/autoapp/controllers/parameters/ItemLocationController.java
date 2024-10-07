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
import org.guanzon.auto.main.parameter.Parts_ItemLocation;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class ItemLocationController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Item Location";
    private int pnEditMode;//Modifying fields
    private Parts_ItemLocation oTransItemLocation;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnDeactivate, btnClose, btnActive;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;

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
        oTransItemLocation = new Parts_ItemLocation(oApp, false, oApp.getBranchCode());

        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initCmboxFieldAction();
        initButtons();
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadItemLocationFields() {
        txtField01.setText(oTransItemLocation.getModel().getModel().getLocatnID());
        txtField02.setText(oTransItemLocation.getModel().getModel().getWHouseNm());
        txtField03.setText(oTransItemLocation.getModel().getModel().getSectnNme());
        txtField04.setText(oTransItemLocation.getModel().getModel().getBinName());
        txtField05.setText(oTransItemLocation.getModel().getModel().getLocatnDs());
        if (oTransItemLocation.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z 0-9]*");
        txtField02.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField04.setTextFormatter(new TextFormatterUtil(textOnly));
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04);
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
                        loJSON = oTransItemLocation.searchWarehouse(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField02.setText(oTransItemLocation.getModel().getModel().getWHouseNm());
                            generateLocation();
                            txtField05.setText(oTransItemLocation.getModel().getModel().getLocatnDs());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            txtField02.requestFocus();
                            return;
                        }
                        break;
                    case "txtField03":
                        loJSON = oTransItemLocation.searchSection(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField03.setText(oTransItemLocation.getModel().getModel().getSectnNme());
                            generateLocation();
                            txtField05.setText(oTransItemLocation.getModel().getModel().getLocatnDs());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField03.setText("");
                            txtField03.requestFocus();
                            return;
                        }
                        break;
                    case "txtField04":
                        loJSON = oTransItemLocation.searchBin(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField04.setText(oTransItemLocation.getModel().getModel().getBinName());
                            generateLocation();
                            txtField05.setText(oTransItemLocation.getModel().getModel().getLocatnDs());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField04.setText("");
                            txtField04.requestFocus();
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

    private void generateLocation() { //autogenerate location
        String lsGen = "";
        String lsWarehouse = oTransItemLocation.getModel().getModel().getWHouseNm();
        String lsSection = oTransItemLocation.getModel().getModel().getSectnNme();
        String lsBin = oTransItemLocation.getModel().getModel().getBinName();
        lsWarehouse = lsWarehouse.isEmpty() ? "" : (lsSection.isEmpty() && lsBin.isEmpty() ? lsWarehouse : (lsSection.isEmpty() && !lsBin.isEmpty() ? lsWarehouse + "-" : lsWarehouse));
        lsSection = lsSection.isEmpty() ? "" : (lsWarehouse.isEmpty() ? lsSection : "-" + lsSection);
        lsBin = lsBin.isEmpty() ? "" : (lsSection.isEmpty() ? lsBin : "-" + lsBin);
        lsGen = lsWarehouse.isEmpty() && lsSection.isEmpty() && lsBin.isEmpty()
                ? ""
                : lsWarehouse + lsSection + lsBin;

        oTransItemLocation.getModel().getModel().setLocatnDs(lsGen);
    }

    private void initCmboxFieldAction() {
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransItemLocation.getModel().getModel().setWHouseID("");
                                oTransItemLocation.getModel().getModel().setWHouseNm("");
                                generateLocation();
                                loadItemLocationFields();
                            }
                        }
                    }
                }
                );
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransItemLocation.getModel().getModel().setSectnID("");
                                oTransItemLocation.getModel().getModel().setSectnNme("");
                                generateLocation();
                                loadItemLocationFields();
                            }
                        }
                    }
                }
                );

        txtField04.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransItemLocation.getModel().getModel().setBinID("");
                                oTransItemLocation.getModel().getModel().setBinName("");
                                generateLocation();
                                loadItemLocationFields();
                            }
                        }
                    }
                }
                );
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
                oTransItemLocation = new Parts_ItemLocation(oApp, false, oApp.getBranchCode());
                loJSON = oTransItemLocation.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadItemLocationFields();
                    pnEditMode = oTransItemLocation.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransItemLocation.updateRecord();
                pnEditMode = oTransItemLocation.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Item Location Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTransItemLocation.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Item Location Information", (String) loJSON.get("message"));
                        loJSON = oTransItemLocation.openRecord(oTransItemLocation.getModel().getModel().getLocatnID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadItemLocationFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransItemLocation.getEditMode();
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
                    oTransItemLocation = new Parts_ItemLocation(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Item Location Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransItemLocation.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadItemLocationFields();
                    pnEditMode = oTransItemLocation.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Item Location Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransItemLocation.getModel().getModel().getLocatnID();
                    loJSON = oTransItemLocation.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Item Location Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Item Location Information", (String) loJSON.get("message"));
                        return;
                    }
                    loJSON = oTransItemLocation.openRecord(oTransItemLocation.getModel().getModel().getLocatnID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadItemLocationFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransItemLocation.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransItemLocation.getModel().getModel().getLocatnID();
                    loJSON = oTransItemLocation.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Item Location Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Item Location Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransItemLocation.openRecord(oTransItemLocation.getModel().getModel().getLocatnID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadItemLocationFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransItemLocation.getEditMode();
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
        txtField04.clear();
        txtField05.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);

        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(true);
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
            if (oTransItemLocation.getModel().getModel().getRecdStat().equals("1")) {
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
