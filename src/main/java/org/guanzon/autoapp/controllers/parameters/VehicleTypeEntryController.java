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
import javafx.application.Platform;
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
import org.guanzon.auto.main.parameter.Vehicle_Type;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class VehicleTypeEntryController implements Initializable, ScreenInterface {

    private Vehicle_Type oTransType;
    private GRider oApp;
    private final String pxeModuleName = "Vehicle Type";
    private int pnEditMode;

    private String psMakeID = "";
    private String psMakeDesc = "";
    private String psFormula = "";
    private boolean pbOpenEvent = false;

    ObservableList<String> cTypeFormat = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
//    private Button btnEdit;
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
    private ComboBox<String> comboBox03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private Button btnConcat;
    @FXML
    private TextField txtField07;
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

    public void setMakeID(String fsValue) {
        psMakeID = fsValue;
    }

    public void setMakeDesc(String fsValue) {
        psMakeDesc = fsValue;
    }

    public Boolean setOpenEvent(Boolean fbValue) {
        pbOpenEvent = fbValue;
        return pbOpenEvent;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransType = new Vehicle_Type(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();

        comboBox03.setOnAction(event -> {
            JSONObject loJSON = new JSONObject();
            loJSON = oTransType.loadFormatType();
            String lsDefault = (String) loJSON.get("sDefaultx");
            String lsFormula1 = (String) loJSON.get("sFormula1");
            String lsFormula2 = (String) loJSON.get("sFormula2");
            switch (comboBox03.getSelectionModel().getSelectedIndex()) {
                case 0:
                    psFormula = lsDefault;
                    break;
                case 1:
                    if (!lsFormula1.equals(lsDefault) && !lsFormula1.equals("")) {
                        psFormula = lsFormula1;
                    } else {
                        psFormula = lsFormula2;
                    }
                    break;
                case 2:
                    psFormula = lsFormula2;
                    break;
                default:
                    break;
            }
            initDisableFieldsFormat();
        });

        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initDisableFieldsFormat() {
        if (psFormula.contains("ENGINE_SIZE")) {
            txtField04.setDisable(false);
        } else {
            txtField04.clear();
        }
        if (psFormula.contains("VARIANT_A")) {
            txtField05.setDisable(false);
        } else {
            txtField05.clear();
        }
        if (psFormula.contains("VARIANT_B")) {
            txtField06.setDisable(false);
        } else {
            txtField06.clear();
        }
        if (!psFormula.contains("ENGINE_SIZE")) {
            txtField04.setDisable(true);
        }
        if (!psFormula.contains("VARIANT_A")) {
            txtField05.setDisable(true);
        }
        if (!psFormula.contains("VARIANT_B")) {
            txtField06.setDisable(true);
        }
        if (!psFormula.contains("VARIANT_B") && !psFormula.contains("VARIANT_A")) {
            txtField05.setDisable(true);
            txtField06.setDisable(true);
        }
    }

    private void loadTypeFields() {
        if (pbOpenEvent) {
            txtField02.setText(oTransType.getModel().getModel().getMakeID());
        }
        txtField01.setText(oTransType.getModel().getModel().getTypeID());
        txtField07.setText(oTransType.getModel().getModel().getTypeDesc());
        if (oTransType.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z -]*");
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly));
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04, txtField05, txtField06, txtField07);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        /*TextArea*/
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField04, txtField05, txtField06, txtField07);
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
                        loJSON = oTransType.searchMake(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField02.setText(oTransType.getModel().getModel().getMakeDesc());
                            psMakeID = oTransType.getModel().getModel().getMakeID();
                            psMakeDesc = oTransType.getModel().getModel().getMakeDesc();
                            loadTypeFormat();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            txtField02.requestFocus();
                            psMakeID = "";
                            psMakeDesc = "";
                            comboBox03.setItems(null);
                            return;
                        }
                        initFields(pnEditMode);
                        break;
                    case "txtField04":
                        loJSON = oTransType.searchEngineSize(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField04.setText(oTransType.getModel().getModel().getVhclSize());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField04.setText("");
                            txtField04.requestFocus();
                            return;
                        }
                        break;
                    case "txtField05":
                        loJSON = oTransType.searchVariantType(lsValue, "A");
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField05.setText(oTransType.getModel().getModel().getVarianta());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField05.setText("");
                            txtField05.requestFocus();
                            return;
                        }
                        break;
                    case "txtField06":
                        loJSON = oTransType.searchVariantType(lsValue, "B");
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField06.setText(oTransType.getModel().getModel().getVariantb());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField06.setText("");
                            txtField06.requestFocus();
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

    private void loadTypeFormat() {
        if (oTransType.getModel().getModel().getMakeID() != null) {
            JSONObject loJSON = new JSONObject();
            loJSON = oTransType.loadFormatType();
            cTypeFormat.clear();
            if (!"error".equals((String) loJSON.get("result"))) {
                String lsDefault = (String) loJSON.get("sDefaultx");
                String lsFormula1 = (String) loJSON.get("sFormula1");
                String lsFormula2 = (String) loJSON.get("sFormula2");

                if (lsDefault != null) {
                    cTypeFormat.add(lsDefault);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Default cannot be empty.");
                }

                if (lsFormula1 != null) {
                    if (!lsFormula1.equals(lsDefault) && !lsFormula1.isEmpty()) {
                        cTypeFormat.add(lsFormula1);
                    }
                }

                if (lsFormula2 != null) {
                    if (!lsFormula2.equals(lsDefault) && !lsFormula2.isEmpty()) {
                        cTypeFormat.add(lsFormula2);
                    }
                }

                comboBox03.setItems(cTypeFormat);
                comboBox03.getSelectionModel().select(0);
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
            }
        }
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField07);
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
                case 7:
                    oTransType.getModel().getModel().setTypeDesc(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive, btnConcat);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransType = new Vehicle_Type(oApp, false, oApp.getBranchCode());
                loJSON = oTransType.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    if (pbOpenEvent) {
                        oTransType.getModel().getModel().setMakeID(psMakeID);
                        oTransType.getModel().getModel().setMakeDesc(psMakeDesc);
                        loJSON = oTransType.loadFormatType();
                        if ("success".equals((String) loJSON.get("result"))) {
                            txtField02.setText(psMakeDesc);
                            loadTypeFormat();
                        } else {
                            ShowMessageFX.Warning(getStage(), "Error found while loading vehicle type format.", "Warning", null);
                            return;
                        }
                    } else {
                        psMakeDesc = "";
                        psMakeID = "";
                    }

                    loadTypeFields();
                    pnEditMode = oTransType.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
//            case "btnEdit":
//                loJSON = oTransType.updateRecord();
//                pnEditMode = oTransType.getEditMode();
//                if ("success".equals((String) loJSON.get("result"))) {
//                    if (pbOpenEvent) {
//                        loJSON = oTransType.loadFormatType();
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            txtField02.setText(psMakeDesc);
//                            oTransType.getModel().getModel().setMakeDesc(psMakeID);
//                            oTransType.getModel().getModel().setMakeDesc(psMakeDesc);
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "Error found while loading vehicle type format.", "Warning", null);
//                            return;
//                        }
//                    } else {
//                        psMakeDesc = "";
//                        psMakeID = "";
//                    }
//                    pnEditMode = oTransType.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                    return;
//                }
//                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Type Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTransType.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Type Information", (String) loJSON.get("message"));
                        loJSON = oTransType.openRecord(oTransType.getModel().getModel().getTypeID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            clearFields();
                            loadTypeFields();
                            initFields(pnEditMode);
                            pnEditMode = oTransType.getEditMode();
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
                    oTransType = new Vehicle_Type(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Type Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransType.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    clearFields();
                    loadTypeFields();
                    pnEditMode = oTransType.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Type Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransType.getModel().getModel().getTypeID();
                    loJSON = oTransType.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransType.openRecord(oTransType.getModel().getModel().getTypeID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadTypeFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransType.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransType.getModel().getModel().getTypeID();
                    loJSON = oTransType.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransType.openRecord(oTransType.getModel().getModel().getTypeID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadTypeFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransType.getEditMode();
                    }
                }
                break;
            case "btnConcat":
                String lsType = genType(psFormula);
                if (lsType != null && !lsType.isEmpty()) {
                    txtField07.setText(lsType);
                    oTransType.getModel().getModel().setTypeDesc(lsType);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "No Type Description to be created.");
                }
                break;
            default:
                ShowMessageFX.Warning("Please contact admin to assist about no button available", "Integrated Automotive System", pxeModuleName);
                break;
        }
        initFields(pnEditMode);
    }

    private void clearFields() {
        comboBox03.setValue(null);
        if (!pbOpenEvent) {
            cTypeFormat.clear();
            txtField01.clear();
        }
        cboxActivate.setSelected(false);
        txtField01.clear();
        txtField02.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.clear();
        txtField07.clear();
    }

    private void initFields(int fnValue) {
//        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        boolean lbShow = (fnValue == EditMode.ADDNEW);
        if (pbOpenEvent) {
            txtField01.setDisable(true);
        } else {
            txtField01.setDisable(false);
        }
        if (comboBox03.getValue() == null) {
            txtField04.setDisable(true);
            txtField05.setDisable(true);
            txtField06.setDisable(true);
        }
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        comboBox03.setDisable(!lbShow);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
//        btnEdit.setVisible(false);
//        btnEdit.setManaged(false);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnActive.setVisible(false);
        btnActive.setManaged(false);
        btnConcat.setDisable(!lbShow);

        if (fnValue == EditMode.READY) {
            //show edit if user clicked save / browse
            if (oTransType.getModel().getModel().getRecdStat().equals("1")) {
//                btnEdit.setVisible(true);
//                btnEdit.setManaged(true);
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

    private String genType(String fsValue) {
        if (fsValue == null || fsValue.isEmpty()) {
            return null;
        }

        StringBuilder sFormat = new StringBuilder();
        String[] values = fsValue.split("\\+");

        for (String value : values) {
            String sCode = "";
            switch (value.trim()) {
                case "ENGINE_SIZE":
                    if (txtField04.getText() == null) {
                        if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to skip Engine Size?")) {
                            txtField04.requestFocus();
                            return null;
                        }
                    } else {
                        sCode = txtField04.getText();
                    }
                    break;
                case "VARIANT_A":
                    if (txtField05.getText() == null) {
                        if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to skip Variant Code A?")) {
                            txtField05.requestFocus();
                            return null;
                        }
                    } else {
                        sCode = txtField05.getText();
                    }
                    break;
                case "VARIANT_B":
                    if (txtField06.getText() == null) {
                        if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to skip Variant Code B?")) {
                            txtField06.requestFocus();
                            return null;
                        }
                    } else {
                        sCode = txtField06.getText();
                    }
                    break;
                default:
                    break;
            }

            if (!sCode.isEmpty()) {
                if (sFormat.length() > 0) {
                    sFormat.append(" ");
                }
                sFormat.append(sCode);
            }
        }

        return sFormat.toString();
    }
}
