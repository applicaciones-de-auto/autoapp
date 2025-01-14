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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_Model;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;

import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleModelController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Vehicle_Model oTrans;
    private final String pxeModuleName = "Vehicle Model";
    private int pnEditMode;

    private String sMakeID = "";
    private String sMakeDesc = "";
    private boolean pbOpenEvent = false;

    ObservableList<String> cUnitType = FXCollections.observableArrayList("COMMERCIAL VEHICLE", "PRIVATE VEHICLE", "LIGHT PRIVATE VEHICLE", "MEDIUM PRIVATE VEHICLE");
    ObservableList<String> cBodyType = FXCollections.observableArrayList("SEDAN", "SUV", "HATCHBACK", "MPV", "MOTORCYCLE", "TRUCK");
//    ObservableList<String> cUnitSize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnBrowse, btnClose, btnActive;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private ComboBox<String> comboBox04, comboBox05, comboBox06;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    public void setMakeID(String fsValue) {
        sMakeID = fsValue;
    }

    public void setMakeDesc(String fsValue) {
        sMakeDesc = fsValue;
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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        oTrans = new Vehicle_Model(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
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
        txtField01.setText(oTrans.getModel().getModel().getModelID());
        txtField02.setText(oTrans.getModel().getModel().getMakeDesc());
        txtField03.setText(oTrans.getModel().getModel().getModelDsc());

        if (oTrans.getModel().getModel().getUnitType() != null && !oTrans.getModel().getModel().getUnitType().trim().isEmpty()) {
            comboBox04.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getUnitType()));
        }
        if (oTrans.getModel().getModel().getBodyType() != null && !oTrans.getModel().getModel().getBodyType().trim().isEmpty()) {
            comboBox05.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getBodyType()));
        }
//        if (oTrans.getModel().getModel().getVhclSize() != null && !oTrans.getModel().getModel().getVhclSize().trim().isEmpty()) {
//            comboBox06.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getVhclSize()));
//        }
        if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z -]*");
        txtField02.setTextFormatter(new TextFormatterUtil(textOnly));
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
                    oTrans.getModel().getModel().setModelDsc(lsValue);
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
                                loJSON = oTrans.searchMake(lsValue, true);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField02.setText(oTrans.getModel().getModel().getMakeDesc());
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
                oTrans = new Vehicle_Model(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    if (pbOpenEvent) {
                        txtField02.setText(sMakeDesc);
                        oTrans.getModel().getModel().setMakeID(sMakeID);
                        oTrans.getModel().getModel().setMakeDesc(sMakeDesc);
                    }
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
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
                if (ShowMessageFX.YesNo(null, "Vehicle Model Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().trim().equals("") || txtField02.getText() == null) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please enter value for make.");
                        txtField02.requestFocus();
                        return;
                    }

                    if (txtField03 == null || txtField03.getText() == null || txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please enter value for model description.");
                        if (txtField03 != null) {
                            txtField03.requestFocus();
                        }
                        return;
                    }
                    if (setSelection()) {
                        loJSON = oTrans.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Vehicle Model Information", (String) loJSON.get("message"));
                            loJSON = oTrans.openRecord(oTrans.getModel().getModel().getModelID());
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
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        clearFields();
                        oTrans = new Vehicle_Model(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getModelID());
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
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Model Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                    ShowMessageFX.Warning(null, "Search Vehicle Model Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getModelID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getModelID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getModelID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Model Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getModelID());
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
        comboBox04.setItems(cUnitType);
        comboBox05.setItems(cBodyType);
//        comboBox06.setItems(cUnitSize);
    }

    @Override
    public void initFieldsAction() {
        comboBox04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getModel().getModel().setUnitType(String.valueOf((comboBox04.getSelectionModel().getSelectedIndex())));
                }
            }
        });
        comboBox05.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox05.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getModel().getModel().setBodyType(String.valueOf((comboBox05.getSelectionModel().getSelectedIndex())));
                }
            }
        });
//        comboBox06.setOnAction(e -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                if (comboBox06.getSelectionModel().getSelectedIndex() >= 0) {
//                    oTrans.getModel().getModel().setVhclSize(String.valueOf((comboBox06.getSelectionModel().getSelectedIndex())));
//                }
//            }
//        });
    }

    @Override
    public void initTextFieldsProperty() {
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setMakeID("");
                            }
                        }
                    }
                }
                );
    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {
        cboxActivate.setSelected(false);
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03);
        CustomCommonUtil.setValue(null, comboBox04, comboBox05);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        if (pbOpenEvent) {
            txtField03.setDisable(true);
        } else {
            txtField03.setDisable(!(fnValue == EditMode.ADDNEW));
        }
        CustomCommonUtil.setDisable(true, txtField01, cboxActivate);
        CustomCommonUtil.setDisable(!lbShow, txtField02, txtField03,
                comboBox04, comboBox05);
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
            txtField02.setDisable(true);
        }
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox04.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Type", "Please select `Vehicle Type` value.");
                return false;
            } else {
                oTrans.getModel().getModel().setUnitType(String.valueOf((comboBox04.getSelectionModel().getSelectedIndex())));
            }
            if (comboBox05.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Body Type", "Please select `Vehicle Body Type` value.");
                return false;
            } else {
                oTrans.getModel().getModel().setBodyType(String.valueOf((comboBox05.getSelectionModel().getSelectedIndex())));
            }
//            if (comboBox06.getSelectionModel().getSelectedIndex() < 0) {
//                ShowMessageFX.Warning(null, "Vehicle  Model Size", "Please select `Vehicle  Model Size` value.");
//                return false;
//            } else {
//                oTrans.getModel().getModel().setVhclSize(String.valueOf((comboBox06.getSelectionModel().getSelectedIndex())));
//            }
        }
        return true;
    }
}
