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
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class TechnicianController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Service_Mechanic oTrans;
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
        oTrans = new Service_Mechanic(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initTextKeyPressed();
        initButtonsClick();
        initFieldsAction();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        CustomCommonUtil.setCapsLockBehavior(textArea03);
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getModel().getModel().getClientID());
        txtField02.setText(oTrans.getModel().getModel().getCompnyNm());
        textArea03.setText(oTrans.getModel().getModel().getAddress());
        if (oTrans.getModel()
                .getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        if (oTrans.getModel().getModel().getTchSkill() != null) {
            if (oTrans.getModel().getModel().getTchSkill().equals("y")) {
                cboxMecha.setSelected(true);
            } else {
                cboxMecha.setSelected(false);
            }
        }

        if (oTrans.getModel().getModel().getBrpSkill() != null) {
            if (oTrans.getModel().getModel().getBrpSkill().equals("y")) {
                cboxBRP.setSelected(true);
            } else {
                cboxBRP.setSelected(false);
            }
        }
        return true;
    }

    @Override
    public void initPatternFields() {

    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
    }

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
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
            if (event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField01":
                        loJSON = oTrans.searchEmployee(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadMasterFields();
                            checkExistingTechInformation();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField01.setText("");
                            return;
                        }
                        break;
                    case "txtField02":
                        loJSON = oTrans.searchEmployee(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadMasterFields();
                            checkExistingTechInformation();
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

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnBrowse, btnCancel,
                btnClose, btnActive, btnDeactivate, btnEdit);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new Service_Mechanic(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Technician Information Saving....", "Are you sure, do you want to save?")) {
                } else {
                    return;
                }

                loJSON = oTrans.saveRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Technician Information", (String) loJSON.get("message"));
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
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
                    oTrans = new Service_Mechanic(oApp, false, oApp.getBranchCode());
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
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = EditMode.READY;
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Technician Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateRecord();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getClientID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Technician Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Technician Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getClientID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Technician Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Technician Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
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

    @Override
    public void initComboBoxItems() {

    }

    @Override
    public void initFieldsAction() {
        cboxMecha.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (cboxMecha.isSelected()) {
                    oTrans.getModel().getModel().setTchSkill("y");
                } else {
                    oTrans.getModel().getModel().setTchSkill("n");
                }
            }
        });
        cboxBRP.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (cboxBRP.isSelected()) {
                    oTrans.getModel().getModel().setBrpSkill("y");
                } else {
                    oTrans.getModel().getModel().setBrpSkill("n");
                }
            }
        });
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setClientID("");
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
                                oTrans.getModel().getModel().setCompnyNm("");
                                txtField01.setText("");
                                textArea03.setText("");
                            }
                        }
                    }
                }
                );
    }

    @Override
    public void initTextFieldsProperty() {
    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField02);
        textArea03.setText("");
        CustomCommonUtil.setSelected(false, cboxActivate, cboxMecha, cboxBRP);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(!lbShow, txtField01, txtField02, cboxMecha,
                cboxBRP);
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
        if (fnValue == EditMode.UPDATE) {
            CustomCommonUtil.setDisable(true, txtField01, txtField02);
        }
    }

    private boolean checkExistingTechInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.validateExistingSM();
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord((String) loJSON.get("sClientID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
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
}
