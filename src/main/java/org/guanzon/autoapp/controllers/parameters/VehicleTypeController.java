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
import org.guanzon.auto.main.parameter.Vehicle_Type;
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
public class VehicleTypeController implements Initializable, ScreenInterface, GRecordInterface {

    private Vehicle_Type oTrans;
    private GRider oApp;
    private final String pxeModuleName = "Vehicle Type";
    private int pnEditMode;
    private String psMakeID = "";
    private String psMakeDesc = "";
    private String psFormula = "";
    private boolean pbOpenEvent = false;

    ObservableList<String> cTypeFormat = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnSave, btnCancel, btnDeactivate, btnBrowse, btnClose, btnConcat, btnActive;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06, txtField07;
    @FXML
    private ComboBox<String> comboBox03;
    @FXML
    private CheckBox cboxActivate;

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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        oTrans = new Vehicle_Type(oApp, false, oApp.getBranchCode());

        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initFieldsAction();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04, txtField05, txtField06, txtField07);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        if (pbOpenEvent) {
            txtField02.setText(oTrans.getModel().getModel().getMakeID());
        }
        txtField01.setText(oTrans.getModel().getModel().getTypeID());
        txtField07.setText(oTrans.getModel().getModel().getTypeDesc());
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
    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
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
                    oTrans.getModel().getModel().setTypeDesc(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField04, txtField05, txtField06, txtField07);
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
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField02":
                        loJSON = oTrans.searchMake(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField02.setText(oTrans.getModel().getModel().getMakeDesc());
                            psMakeID = oTrans.getModel().getModel().getMakeID();
                            psMakeDesc = oTrans.getModel().getModel().getMakeDesc();
                            loadTypeFormat();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            psMakeID = "";
                            psMakeDesc = "";
                            comboBox03.setItems(null);
                            return;
                        }
                        initFields(pnEditMode);
                        break;
                    case "txtField04":
                        loJSON = oTrans.searchEngineSize(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField04.setText(oTrans.getModel().getModel().getVhclSize());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField04.setText("");
                            return;
                        }
                        break;
                    case "txtField05":
                        loJSON = oTrans.searchVariantType(lsValue, "A");
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField05.setText(oTrans.getModel().getModel().getVarianta());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField05.setText("");
                            return;
                        }
                        break;
                    case "txtField06":
                        loJSON = oTrans.searchVariantType(lsValue, "B");
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField06.setText(oTrans.getModel().getModel().getVariantb());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField06.setText("");
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

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive, btnConcat);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new Vehicle_Type(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    if (pbOpenEvent) {
                        oTrans.getModel().getModel().setMakeID(psMakeID);
                        oTrans.getModel().getModel().setMakeDesc(psMakeDesc);
                        loJSON = oTrans.loadFormatType();
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

                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Type Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Type Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTypeID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            clearFields();
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
                        oTrans = new Vehicle_Type(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTypeID());
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
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Type Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    clearFields();
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Type Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getTypeID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTypeID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getTypeID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Type Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTypeID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnConcat":
                String lsType = genType(psFormula);
                if (lsType != null && !lsType.isEmpty()) {
                    txtField07.setText(lsType);
                    oTrans.getModel().getModel().setTypeDesc(lsType);
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

    @Override
    public void initComboBoxItems() {

    }

    @Override
    public void initFieldsAction() {
        comboBox03.setOnAction(event -> {
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.loadFormatType();
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
    }

    @Override
    public void initTextFieldsProperty() {

    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {
        comboBox03.setValue(null);
        if (!pbOpenEvent) {
            cTypeFormat.clear();
            txtField02.setText("");
        }
        cboxActivate.setSelected(false);
        CustomCommonUtil.setText("", txtField01, txtField02,
                txtField04, txtField05, txtField06, txtField07);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW);
        if (pbOpenEvent) {
            txtField02.setDisable(true);
        } else {
            txtField02.setDisable(false);
        }
        cboxActivate.setDisable(true);
        CustomCommonUtil.setDisable(!lbShow, txtField02, comboBox03, btnConcat);
        if (comboBox03.getValue() == null) {
            CustomCommonUtil.setDisable(true, txtField04, txtField05, txtField06);
        }
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnDeactivate, btnActive);
        CustomCommonUtil.setManaged(false, btnDeactivate, btnActive);
        if (fnValue == EditMode.READY) {
            if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                CustomCommonUtil.setVisible(true, btnDeactivate);
                CustomCommonUtil.setManaged(true, btnDeactivate);
            } else {
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
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

    private void loadTypeFormat() {
        if (oTrans.getModel().getModel().getMakeID() != null) {
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.loadFormatType();
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
