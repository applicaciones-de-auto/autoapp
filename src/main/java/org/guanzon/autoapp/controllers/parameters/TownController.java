/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parameters;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Address_Town;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class TownController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private final String pxeModuleName = "Town";
    private double xOffset, yOffset = 0;
    private int pnEditMode;//Modifying fields
    private Address_Town oTrans;
    @FXML
    private Button btnAdd, btnClose, btnSave, btnEdit, btnCancel, btnDeactivate, btnActive, btnBrowse, btnProvince;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04;
    @FXML
    private CheckBox cboxActivate;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Address_Town(oApp, false, oApp.getBranchCode());

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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getModel().getModel().getTownID());
        txtField02.setText(oTrans.getModel().getModel().getProvName());
        txtField03.setText(oTrans.getModel().getModel().getTownName());
        txtField04.setText(oTrans.getModel().getModel().getZippCode());
        if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern textOnly, numOnly;
        textOnly = Pattern.compile("[A-Za-z 0-9]*");
        txtField03.setTextFormatter(new TextFormatterUtil(textOnly));
        numOnly = Pattern.compile("[0-9]*");
        txtField04.setTextFormatter(new TextFormatterUtil(numOnly));
    }

    @Override
    public void initLimiterFields() {

    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField03, txtField04);
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
                    oTrans.getModel().getModel().setTownName(lsValue);
                    break;
                case 4:
                    oTrans.getModel().getModel().setZippCode(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04);
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
                                loJSON = oTrans.searchProvince(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField02.setText(oTrans.getModel().getModel().getProvName());
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
                btnClose, btnDeactivate, btnActive, btnProvince);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new Address_Town(oApp, false, oApp.getBranchCode());
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
                if (ShowMessageFX.YesNo(null, "Town Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Province Information", "Please enter value Province name.");
                        txtField02.requestFocus();
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Town Information", "Please enter valid Town name.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Town Information", "Please enter value Town name.");
                        txtField03.requestFocus();
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Zip Code Information", "Please enter value Zip code.");
                        txtField04.requestFocus();
                        return;
                    }
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Town Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTownID());
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
                    clearFields();
                    oTrans = new Address_Town(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Town Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                    ShowMessageFX.Warning(null, "Search Town Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getTownID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Town Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Town Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTownID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getTownID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Town Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Town Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getTownID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnProvince":
                try {
                loadProvinceWindow();
            } catch (SQLException ex) {
                Logger.getLogger(TownController.class.getName()).log(Level.SEVERE, null, ex);
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
                if (newValue != null && newValue.isEmpty()) {
                    oTrans.getModel().getModel().setProvID("");
                    oTrans.getModel().getModel().setProvName("");
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
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField04);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(!lbShow, txtField02);
        txtField03.setDisable(!(lbShow && !txtField02.getText().isEmpty()));
        txtField04.setDisable(!(lbShow && !txtField03.getText().isEmpty()));
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

    private void loadProvinceWindow() throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/Province.fxml"));
            ProvinceController loControl = new ProvinceController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

}
