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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_MakeFramePattern;
import org.guanzon.auto.main.parameter.Vehicle_ModelFramePattern;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleFrameFormatController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;

    private Vehicle_ModelFramePattern oTrans;
    private Vehicle_MakeFramePattern oTransMakeFrameFormat;
    private final String pxeModuleName = "Vehicle Frame Format";
    private String psMakeID, psMakeDesc;
    private String psModelID, psModelDesc;
    private int pnEditMode;
    private boolean pbOpenEvent = false;
    ObservableList<String> cFormtType = FXCollections.observableArrayList("MAKE FORMAT", "MODEL FORMAT");
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnClose;
    @FXML
    private ComboBox<String> comboBox01;
    @FXML
    private TextField txtField02, txtField03, txtField04, txtField05;

    public String setMakeID(String fsValue) {
        psMakeID = fsValue;
        return psMakeID;
    }

    public String setMakeDesc(String fsValue) {
        psMakeDesc = fsValue;
        return psMakeDesc;
    }

    public String setModelID(String fsValue) {
        psModelID = fsValue;
        return psModelID;
    }

    public String setModelDesc(String fsValue) {
        psModelDesc = fsValue;
        return psModelDesc;
    }

    public Boolean setOpenEvent(Boolean fbValue) {
        pbOpenEvent = fbValue;
        return pbOpenEvent;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Vehicle_ModelFramePattern(oApp, false, oApp.getBranchCode());
        oTransMakeFrameFormat = new Vehicle_MakeFramePattern(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initPatternFields();
        initLimiterFields();
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
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        switch (comboBox01.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField02.setText(oTransMakeFrameFormat.getModel().getModel().getMakeDesc());
                txtField04.setText(oTransMakeFrameFormat.getModel().getModel().getFrmePtrn());
                CustomCommonUtil.setDisable(true, txtField03, txtField05);
                break;
            case 1:
                txtField02.setText(oTrans.getModel().getModel().getMakeDesc());
                txtField03.setText(oTrans.getModel().getModel().getModelDsc());
                txtField04.setText(oTrans.getModel().getModel().getFrmePtrn());
                if (oTrans.getModel().getModel().getFrmeLen() == null) {
                    txtField05.setText("0");
                } else {
                    txtField05.setText(String.valueOf(oTrans.getModel().getModel().getFrmeLen()));
                }
                break;
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern textOnly, numOnly, frmPat;
        textOnly = Pattern.compile("[A-Za-z -]*");
        frmPat = Pattern.compile("[A-Za-z0-9-]*");
        numOnly = Pattern.compile("[0-9]*");
        txtField02.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField04.setTextFormatter(new TextFormatterUtil(frmPat));
        txtField05.setTextFormatter(new TextFormatterUtil(numOnly));
    }

    @Override
    public void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField04, 3);
        CustomCommonUtil.addTextLimiter(txtField05, 3);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField04, txtField05);
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
                case 4:
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        oTransMakeFrameFormat.getModel().getModel().setFrmePtrn(lsValue);
                    } else {
                        oTrans.getModel().getModel().setFrmePtrn(lsValue);
                    }
                    break;
                case 5:
                    oTrans.getModel().getModel().setFrmeLen(Integer.valueOf(lsValue));
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05);
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
                                if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                                    loJSON = oTransMakeFrameFormat.searchMake(lsValue, true);
                                    if (!"error".equals(loJSON.get("result"))) {
                                        txtField02.setText(oTransMakeFrameFormat.getModel().getModel().getMakeDesc());
                                    } else {
                                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                        txtField02.setText("");
                                        return;
                                    }
                                } else {
                                    loJSON = oTrans.searchMake(lsValue, true);
                                    if (!"error".equals(loJSON.get("result"))) {
                                        txtField02.setText(oTrans.getModel().getModel().getMakeDesc());
                                    } else {
                                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                        txtField02.setText("");
                                        return;
                                    }
                                }
                                break;
                            case "txtField03":
                                if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                                    loJSON = oTrans.searchModel(lsValue);
                                    if (!"error".equals(loJSON.get("result"))) {
                                        txtField03.setText(oTrans.getModel().getModel().getModelDsc());
                                    } else {
                                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                        txtField03.setText("");
                                        return;
                                    }
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
                btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    clearFields();
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        oTransMakeFrameFormat = new Vehicle_MakeFramePattern(oApp, false, oApp.getBranchCode());
                        loJSON = oTransMakeFrameFormat.newRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            if (pbOpenEvent) {
                                oTransMakeFrameFormat.getModel().getModel().setMakeID(psMakeID);
                                oTransMakeFrameFormat.getModel().getModel().setMakeDesc(psMakeDesc);
                            }
                            loadMasterFields();
                            pnEditMode = oTransMakeFrameFormat.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        }
                    } else {
                        oTrans = new Vehicle_ModelFramePattern(oApp, false, oApp.getBranchCode());
                        loJSON = oTrans.newRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            if (pbOpenEvent) {
                                oTrans.getModel().getModel().setMakeID(psMakeID);
                                oTrans.getModel().getModel().setMakeDesc(psMakeDesc);
                                oTrans.getModel().getModel().setModelID(psModelID);
                                oTrans.getModel().getModel().setModelDsc(psModelDesc);
                            }
                            loadMasterFields();
                            pnEditMode = oTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please choose Format Type.");
                }
                break;
            case "btnEdit":
                if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                    loJSON = oTransMakeFrameFormat.updateRecord();
                    pnEditMode = oTransMakeFrameFormat.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                } else {
                    loJSON = oTrans.updateRecord();
                    pnEditMode = oTrans.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                }
                break;
            case "btnSave":
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        if (ShowMessageFX.YesNo(null, "Vehicle Frame Format Information Saving....", "Are you sure, do you want to save?")) {
                            loJSON = oTransMakeFrameFormat.saveRecord();
                            if ("success".equals((String) loJSON.get("result"))) {
                                ShowMessageFX.Information(null, "Vehicle Frame Format Information", (String) loJSON.get("message"));
                                loJSON = oTransMakeFrameFormat.openRecord(oTransMakeFrameFormat.getModel().getModel().getMakeID(), oTransMakeFrameFormat.getModel().getModel().getEntryNo());
                                if ("success".equals((String) loJSON.get("result"))) {
                                    loadMasterFields();
                                    initFields(pnEditMode);
                                    pnEditMode = oTransMakeFrameFormat.getEditMode();
                                }
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
                        }
                    } else {
                        if (ShowMessageFX.YesNo(null, "Vehicle Frame Format Information Saving....", "Are you sure, do you want to save?")) {
                            loJSON = oTrans.saveRecord();
                            if ("success".equals((String) loJSON.get("result"))) {
                                ShowMessageFX.Information(null, "Vehicle FrameFormat Information", (String) loJSON.get("message"));
                                loJSON = oTrans.openRecord(oTrans.getModel().getModel().getModelID(), oTrans.getModel().getModel().getEntryNo());
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
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please choose Format Type.");
                    clearFields();
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTrans = new Vehicle_ModelFramePattern(oApp, false, oApp.getBranchCode());
                    oTransMakeFrameFormat = new Vehicle_MakeFramePattern(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.YesNo(null, "Search Vehicle Frame Format Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }
                    }
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        loJSON = oTransMakeFrameFormat.searchRecord("", false);
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = oTransMakeFrameFormat.getEditMode();
                            initFields(pnEditMode);
                        } else {
                            ShowMessageFX.Warning(null, "Search Vehicle Make Frame Format Information", (String) loJSON.get("message"));
                        }
                    } else {
                        loJSON = oTrans.searchRecord("", false);
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = oTrans.getEditMode();
                            initFields(pnEditMode);
                        } else {
                            ShowMessageFX.Warning(null, "Search Vehicle Model Frame Format Information", (String) loJSON.get("message"));
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please choose Format Type.");
                    clearFields();
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
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
        comboBox01.setItems(cFormtType);
    }

    @Override
    public void initFieldsAction() {
        comboBox01.setOnAction(e -> {
            clearFields();
            pnEditMode = EditMode.UNKNOWN;
            initFields(pnEditMode);
        });
    }

    @Override
    public void initTextFieldsProperty() {
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                                        oTransMakeFrameFormat.getModel().getModel().setMakeID("");
                                    } else {
                                        oTrans.getModel().getModel().setMakeID("");
                                        oTrans.getModel().getModel().setModelDsc("");
                                        txtField03.setText("");
                                    }
                                }
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
                                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                                    oTrans.getModel().getModel().setModelDsc("");
                                }
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
        CustomCommonUtil.setText("", txtField02, txtField03, txtField04);
        txtField05.setText("0");
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true, txtField02, txtField03, txtField04, txtField05);
        if (lbShow) {
            switch (comboBox01.getSelectionModel().getSelectedIndex()) {
                case 0:
                    CustomCommonUtil.setDisable(!lbShow, txtField02, txtField04);
                    CustomCommonUtil.setDisable(true, txtField03, txtField05);
                    break;
                case 1:
                    CustomCommonUtil.setDisable(!lbShow, txtField02, txtField03, txtField04, txtField05);
                    break;
            }
        }
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        if (fnValue == EditMode.UPDATE) {
            if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                CustomCommonUtil.setDisable(true, txtField02, txtField04);
            } else {
                CustomCommonUtil.setDisable(true, txtField02, txtField03, txtField04);
            }
        }
        if (fnValue == EditMode.READY) {
            if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
            }
        }
    }
}
