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
import org.guanzon.auto.main.parameter.Bank;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class BankController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private final String pxeModuleName = "Bank";
    private int pnEditMode;//Modifying fields
    private Bank oTrans;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnActive, btnBrowse, btnClose;
    ObservableList<String> cBankType = FXCollections.observableArrayList("BANK", "CREDIT UNION", "INSURANCE COMPANY", "INVESTMENT COMPANIES");
    @FXML
    private TextField txtField01, txtField03, txtField04;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private ComboBox<String> comboBox02;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField04.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);

        oTrans = new Bank(oApp, false, oApp.getBranchCode());

        initCapitalizationFields();
        initPatternFields();
        initLimiterFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField04);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {

        txtField01.setText(oTrans.getModel().getModel().getBankID());
        if (oTrans.getModel().getModel().getBankType() != null && !oTrans.getModel().getModel().getBankType().trim().isEmpty()) {
            switch ((String.valueOf(oTrans.getModel().getModel().getBankType()))) {
                case "bank":
                    comboBox02.setValue("BANK");
                    break;
                case "cred":
                    comboBox02.setValue("CREDIT UNION");
                    break;
                case "insc":
                    comboBox02.setValue("INSURANCE COMPANY");
                    break;
                case "invc":
                    comboBox02.setValue("INVESTMENT COMPANIES");
                    break;
                default:
                    break;
            }
        }
        txtField03.setText(oTrans.getModel().getModel().getBankName());
        txtField04.setText(oTrans.getModel().getModel().getBankCode());
        if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern Bank;
        Bank = Pattern.compile("[A-Za-z 0-9]*");
        txtField03.setTextFormatter(new TextFormatterUtil(Bank));
        txtField04.setTextFormatter(new TextFormatterUtil(Bank));

    }

    @Override
    public void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField03, 30);
        CustomCommonUtil.addTextLimiter(txtField04, 10);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText().toUpperCase();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /* Lost Focus */
            switch (lnIndex) {
                case 3:
                    oTrans.getModel().getModel().setBankName(lsValue);
                    break;
                case 4:
                    oTrans.getModel().getModel().setBankCode(lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();

        }
    };

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Bank Type", "Please select `Bank Type` value.");
                return false;
            } else {
                switch (comboBox02.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        oTrans.getModel().getModel().setBankType("bank");
                        break;
                    case 1:
                        oTrans.getModel().getModel().setBankType("cred");
                        break;
                    case 2:
                        oTrans.getModel().getModel().setBankType("insc");
                        break;
                    case 3:
                        oTrans.getModel().getModel().setBankType("invc");
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
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

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnActive, btnBrowse, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new Bank(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));

                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, " Bank Information Saving....", "Are you sure, do you want to save?")) {
                    if (!setSelection()) {
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, "Bank Information", "Please enter valid bank name information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Bank Information", "Please enter value bank name information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField04.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, " Bank Information", "Please enter valid bank code information.");
                        txtField04.setText("");
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Bank Information", "Please enter value bank code information.");
                        txtField04.setText("");
                        return;
                    }
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, "Bank Information", (String) loJSON.get("message"));
                        return;
                    }
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateRecord();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        clearFields();
                        oTrans = new Bank(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBankID());
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
                    if (ShowMessageFX.YesNo(null, "Search Bank Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                    ShowMessageFX.Warning(null, "Search Bank Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getBankID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Bank Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBankID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getBankID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Bank Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBankID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox02.setItems(cBankType);
    }

    @Override
    public void initFieldsAction() {
        comboBox02.setOnAction(e -> {
            int selectedComboBox02 = comboBox02.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox02 >= 0) {
                    switch (selectedComboBox02) {
                        case 0:
                            oTrans.getModel().getModel().setBankType("bank");
                            break;
                        case 1:
                            oTrans.getModel().getModel().setBankType("cred");
                            break;
                        case 2:
                            oTrans.getModel().getModel().setBankType("insc");
                            break;
                        case 3:
                            oTrans.getModel().getModel().setBankType("invc");
                            break;
                        default:
                            break;
                    }
                }
                initFields(pnEditMode);
            }
        });
    }

    @Override
    public void initTextFieldsProperty() {
    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {
        comboBox02.setValue("");
        cboxActivate.setSelected(false);
        CustomCommonUtil.setText("", txtField01, txtField03, txtField04);

    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true, txtField01, cboxActivate);
        comboBox02.setDisable(!lbShow);
        CustomCommonUtil.setDisable(!(lbShow && !comboBox02.getValue().isEmpty()), txtField03, txtField04);
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
