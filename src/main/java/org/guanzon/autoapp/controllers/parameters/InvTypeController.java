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
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Parts_InventoryType;
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
public class InvTypeController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Parts_InventoryType oTrans;
    private final String pxeModuleName = "Inventory Type";
    private int pnEditMode;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnDeactivate, btnClose, btnActive;
    @FXML
    private TextField txtField01, txtField02;
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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        oTrans = new Parts_InventoryType(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getModel().getModel().getInvTypCd());
        txtField02.setText(oTrans.getModel().getModel().getDescript());
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
        textOnly = Pattern.compile("[A-Za-z 0-9]*");
        txtField02.setTextFormatter(new TextFormatterUtil(textOnly));
    }

    @Override
    public void initLimiterFields() {
    }

    public void initTextFieldFocus() {
        txtField02.focusedProperty().addListener(txtField_Focus);
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
                case 2:
                    loJSON = oTrans.getModel().getModel().setDescript(lsValue);
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    public void initTextKeyPressed() {
        txtField02.setOnKeyPressed(event -> txtField_KeyPressed(event));
    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
        String textFieldID = ((TextField) event.getSource()).getId();
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case TAB:
                case ENTER:
                case F3:
                    switch (textFieldID) {
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
                oTrans = new Parts_InventoryType(oApp, false, oApp.getBranchCode());
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
                if (ShowMessageFX.YesNo(null, "Inventory Type Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid inventory type information.");
                        txtField02.setText("");
                        return;
                    }
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Type Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getInvTypCd());
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
                        oTrans = new Parts_InventoryType(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getInvTypCd());
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
                    if (ShowMessageFX.YesNo(null, "Search Inventory Type Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                    ShowMessageFX.Warning(null, "Search Inventory Type Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getInvTypCd();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Inventory Type Information", (String) loJSON.get("message"));
                        return;
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getInvTypCd());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getInvTypCd();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Type Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Inventory Type Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getInvTypCd());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please contact admin to assist about no button available");
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

    }

    @Override
    public void clearTables() {

    }

    public void clearFields() {
        cboxActivate.setSelected(false);
        CustomCommonUtil.setText("", txtField01, txtField02);
    }

    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true, txtField01, cboxActivate);
        txtField02.setDisable(!lbShow);
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
