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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_Description;
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
public class VehicleDescriptionController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private final String pxeModuleName = "Vehicle Description"; //Form Title
    private Vehicle_Description oTrans;
    private int pnEditMode;
    private double xOffset, yOffset = 0;
    ObservableList<String> cTransmission = FXCollections.observableArrayList("AUTOMATIC", "MANUAL", "CVT");
    ObservableList<String> cModelsize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnDeactivate, btnActive, btnBrowse, btnClose, btnMake, btnModel, btnType, btnColor;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private ComboBox<String> comboBox07, comboBox08;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Vehicle_Description(oApp, false, oApp.getBranchCode());
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getModel().getModel().getVhclID());
        txtField02.setText(oTrans.getModel().getModel().getMakeDesc());
        txtField03.setText(oTrans.getModel().getModel().getModelDsc());
        txtField04.setText(oTrans.getModel().getModel().getTypeDesc());
        txtField05.setText(oTrans.getModel().getModel().getColorDsc());
        if (oTrans.getModel().getModel().getYearModl() == null) {
            txtField06.setText("0");
        } else {
            txtField06.setText(String.valueOf(oTrans.getModel().getModel().getYearModl()));
        }
        int lnTrnsMn = -1;
        if (oTrans.getModel().getModel().getTransMsn() != null && !oTrans.getModel().getModel().getTransMsn().trim().isEmpty()) {
            switch ((String.valueOf(oTrans.getModel().getModel().getTransMsn()))) {
                case "AT":
                    lnTrnsMn = 0;
                    break;
                case "M":
                    lnTrnsMn = 1;
                    break;
                case "CVT":
                    lnTrnsMn = 2;
                    break;
            }
        }
        comboBox07.getSelectionModel().select(lnTrnsMn);
        if (oTrans.getModel().getModel().getVhclSize() != null && !oTrans.getModel().getModel().getVhclSize().trim().isEmpty()) {
            comboBox08.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getVhclSize()));
        }
        if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern numOnly = Pattern.compile("[0-9]*");
        txtField06.setTextFormatter(new TextFormatterUtil(numOnly));
    }

    @Override
    public void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField06, 4);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField06);
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
                case 6:
                    oTrans.getModel().getModel().setYearModl(Integer.valueOf(lsValue));
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06);
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
                                    return;
                                }
                                break;
                            case "txtField03":
                                loJSON = oTrans.searchModel(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField03.setText(oTrans.getModel().getModel().getModelDsc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField03.setText("");
                                    return;
                                }
                                break;
                            case "txtField04":
                                loJSON = oTrans.searchType(lsValue, true);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField04.setText(oTrans.getModel().getModel().getTypeDesc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField04.setText("");
                                    return;
                                }
                                break;
                            case "txtField05":
                                loJSON = oTrans.searchColor(lsValue, true);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField05.setText(oTrans.getModel().getModel().getColorDsc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField05.setText("");
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
                btnClose, btnDeactivate, btnMake, btnModel, btnType, btnActive, btnColor);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        String lsMakeID = "";
        if (oTrans.getModel().getModel().getMakeID() != null) {
            lsMakeID = oTrans.getModel().getModel().getMakeID();
        }
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new Vehicle_Description(oApp, false, oApp.getBranchCode());
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
                if (ShowMessageFX.YesNo(null, "Vehicle Description Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a value for Make.");
                        txtField02.requestFocus();
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a value for Model.");
                        txtField03.requestFocus();
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a value for Type.");
                        txtField04.requestFocus();
                        return;
                    }
                    if (txtField05.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a value for Color.");
                        txtField05.requestFocus();
                        return;
                    }

                    if (txtField06.getText().trim().equals("") || Integer.parseInt(txtField06.getText()) < 1900) {
                        ShowMessageFX.Warning(null, "Warning", "Please enter a valid value for Year.");
                        txtField06.requestFocus();
                        return;
                    }
                    if (setSelection()) {
                        loJSON = oTrans.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Vehicle Description Information", (String) loJSON.get("message"));
                            loJSON = oTrans.openRecord(oTrans.getModel().getModel().getVhclID());
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
                    clearFields();
                    oTrans = new Vehicle_Description(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Description Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                    ShowMessageFX.Warning(null, "Search Vehicle Description Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getVhclID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getVhclID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getVhclID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getVhclID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnMake":
                loadVehicleMake();
                break;
            case "btnModel":
                try {
                loadVehicleModel(lsMakeID, oTrans.getModel().getModel().getMakeDesc());

            } catch (SQLException ex) {
                Logger.getLogger(VehicleDescriptionController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnType":
                try {
                loadVehicleType(lsMakeID, oTrans.getModel().getModel().getMakeDesc());

            } catch (SQLException ex) {
                Logger.getLogger(VehicleDescriptionController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnColor":
                loadVehicleColor();
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox07.setItems(cTransmission);
        comboBox08.setItems(cModelsize);
    }

    @Override
    public void initFieldsAction() {
        comboBox07.setOnAction(e -> {
            int selectedComboBox07 = comboBox07.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox07 >= 0) {
                    switch (selectedComboBox07) {
                        case 0:
                            oTrans.getModel().getModel().setTransMsn("AT");
                            break;
                        case 1:
                            oTrans.getModel().getModel().setTransMsn("M");
                            break;
                        case 2:
                            oTrans.getModel().getModel().setTransMsn("CVT");
                            break;
                        default:
                            oTrans.getModel().getModel().setTransMsn("");
                            break;
                    }
                }
            }
        });
        comboBox08.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox08.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getModel().getModel().setVhclSize(String.valueOf((comboBox08.getSelectionModel().getSelectedIndex())));
                }
            }
        });
    }

    @Override
    public void initTextFieldsProperty() {
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setMakeID("");
                                oTrans.getModel().getModel().setModelID("");
                                txtField03.setText("");
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
                                oTrans.getModel().getModel().setModelID("");
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
                                oTrans.getModel().getModel().setTypeID("");
                            }
                        }
                    }
                }
                );
        txtField05.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setColorID("");
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
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField04, txtField05);
        txtField06.setText("0");
        CustomCommonUtil.setValue(null, comboBox07, comboBox08);
        cboxActivate.setSelected(false);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(!lbShow, txtField01, cboxActivate);
        CustomCommonUtil.setDisable(!lbShow, txtField02, txtField03, txtField04,
                txtField05, txtField06, comboBox07, comboBox08);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);

        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnEdit, btnDeactivate, btnActive);
        CustomCommonUtil.setManaged(false, btnEdit, btnDeactivate, btnActive);
        if (fnValue == EditMode.READY) {
            if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                CustomCommonUtil.setVisible(false, btnEdit, btnDeactivate);
                CustomCommonUtil.setManaged(false, btnEdit, btnDeactivate);
            } else {
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox07.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Transmission", "Please select `Vehicle Transmission` value.");
                return false;
            } else {
                switch (comboBox07.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        oTrans.getModel().getModel().setTransMsn("AT");
                        break;
                    case 1:
                        oTrans.getModel().getModel().setTransMsn("M");
                        break;
                    case 2:
                        oTrans.getModel().getModel().setTransMsn("CVT");
                        break;
                    default:
                        break;
                }
            }
            if (comboBox08.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Model Size", "Please select `Vehicle Model Size` value.");
                return false;
            } else {
                oTrans.getModel().getModel().setVhclSize(String.valueOf((comboBox08.getSelectionModel().getSelectedIndex())));
            }
        }
        return true;
    }

    private void loadVehicleMake() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleMake.fxml"));

            VehicleMakeController loControl = new VehicleMakeController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);

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

    /*MODEL WINDOW*/
    private void loadVehicleModel(String fsMakeID, String fsMakeDesc) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleModel.fxml"));
            VehicleModelController loControl = new VehicleModelController();
            loControl.setGRider(oApp);
            loControl.setMakeID(fsMakeID);
            loControl.setMakeDesc(fsMakeDesc);

            if (fsMakeID.isEmpty()) {
                loControl.setOpenEvent(false);
            } else {
                loControl.setOpenEvent(true);
            }
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

    private void loadVehicleType(String fsMakeID, String fsMakeDesc) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleType.fxml"));
            VehicleTypeController loControl = new VehicleTypeController();
            loControl.setGRider(oApp);
            loControl.setMakeID(fsMakeID);
            loControl.setMakeDesc(fsMakeDesc);

            if (fsMakeID.isEmpty()) {
                loControl.setOpenEvent(false);
            } else {
                loControl.setOpenEvent(true);
            }
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

    private void loadVehicleColor() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleColor.fxml"));
            VehicleColorController loControl = new VehicleColorController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);

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
