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
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Address_Province;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ProvinceController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private final String pxeModuleName = "Province";
    private int pnEditMode;//Modifying fields
    private Address_Province oTrans;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnClose, btnSave, btnEdit, btnCancel, btnDeactivate, btnActive, btnBrowse;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private CheckBox cboxActivate;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        oTrans = new Address_Province(oApp, false, oApp.getBranchCode());

        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initTextFieldsProperty();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        if (oTrans.getModel().getModel().getProvID() != null) {
            txtField01.setText(oTrans.getModel().getModel().getProvID());
        }

        String lsRegionName = "";
        if (oTrans.getModel().getModel().getRegionNm() != null) {
            lsRegionName = oTrans.getModel().getModel().getRegionNm();
        }
        txtField02.setText(lsRegionName);

        if (oTrans.getModel().getModel().getProvName() != null) {
            txtField03.setText(oTrans.getModel().getModel().getProvName());
        }
        if (oTrans.getModel().getModel().getRecdStat() != null) {
            if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                cboxActivate.setSelected(true);
            } else {
                cboxActivate.setSelected(false);
            }
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z 0-9]*");
        txtField03.setTextFormatter(new TextFormatterUtil(textOnly));
    }

    @Override
    public void initLimiterFields() {

    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField03);
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
                case 3:
                    oTrans.getModel().getModel().setProvName(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
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
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case TAB:
                    case ENTER:
                    case F3:
                        switch (txtFieldID) {
                            case "txtField02":
                                loJSON = oTrans.searchRegion(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField02.setText(oTrans.getModel().getModel().getRegionNm());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField02.setText("");
                                    return;
                                }
                                break;
                        }
                        initFields(pnEditMode);
                        event.consume();
                        CommonUtils.SetNextFocus((TextField) event.getSource());
                        break;
                    case UP:
                        event.consume();
                        CommonUtils.SetPreviousFocus((TextField) event.getSource());
                        break;
                    case DOWN:
                        event.consume();
                        CommonUtils.SetNextFocus((TextField) event.getSource());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new Address_Province(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateRecord();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Province Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Region Information", "Please enter value Region name.");
                        txtField02.requestFocus();
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Province Information", "Please enter valid Province name.");
                        txtField03.setText("");
                        return;
                    }

                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Province Information", "Please enter value Province name.");
                        txtField03.requestFocus();
                        return;
                    }
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Province Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getProvID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        clearFields();
                        oTrans = new Address_Province(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getProvID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    }
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Province Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Province Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getProvID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Province Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Province Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getProvID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getProvID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Province Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Province Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getProvID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            default:
                ShowMessageFX.Warning("Please contact admin to assist about no button available", "Integrated Automotive System", pxeModuleName);
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {

    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void initTextFieldsProperty() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getModel().getModel().setRegionID("");
                        oTrans.getModel().getModel().setRegionNm("");
                    }
                }
            }
        });
    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {
        cboxActivate.setSelected(false);
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(!lbShow, txtField02, txtField03);
        txtField03.setDisable(!(lbShow && !txtField02.getText().isEmpty()));
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnEdit, btnDeactivate, btnActive);
        CustomCommonUtil.setManaged(false, btnEdit, btnDeactivate, btnActive);
        if (fnValue == EditMode.READY) {
            if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnDeactivate);
                CustomCommonUtil.setManaged(true, btnEdit, btnDeactivate);
            } else {
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
    }

}
