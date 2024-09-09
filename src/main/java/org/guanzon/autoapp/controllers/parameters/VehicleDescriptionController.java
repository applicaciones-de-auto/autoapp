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
import javafx.scene.input.KeyCode;
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
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class VehicleDescriptionController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Vehicle Description"; //Form Title
    private Vehicle_Description oTransVehicleDescription;
    private int pnEditMode;
    ObservableList<String> cTransmission = FXCollections.observableArrayList("AUTOMATIC", "MANUAL", "CVT");
    ObservableList<String> cModelsize = FXCollections.observableArrayList("BANTAM", "SMALL", "MEDIUM", "LARGE");
    private double xOffset, yOffset = 0;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDeactivate;
    @FXML
    private Button btnActive;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private ComboBox<String> comboBox07;
    @FXML
    private ComboBox<String> comboBox08;
    @FXML
    private Button btnMake;
    @FXML
    private Button btnModel;
    @FXML
    private Button btnType;
    @FXML
    private Button btnColor;
    @FXML
    private TextField txtField01;

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
        oTransVehicleDescription = new Vehicle_Description(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();

        Pattern numOnly = Pattern.compile("[0-9]*");
        txtField06.setTextFormatter(new TextFormatterUtil(numOnly));
        CustomCommonUtil.addTextLimiter(txtField06, 4);
        comboBox07.setItems(cTransmission);
        comboBox08.setItems(cModelsize);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

    }

    private void loadVehicleDescriptionFields() {
        txtField01.setText(oTransVehicleDescription.getModel().getModel().getVhclID());
        txtField02.setText(oTransVehicleDescription.getModel().getModel().getMakeDesc());
        txtField03.setText(oTransVehicleDescription.getModel().getModel().getModelDsc());
        txtField04.setText(oTransVehicleDescription.getModel().getModel().getTypeDesc());
        txtField05.setText(oTransVehicleDescription.getModel().getModel().getColorDsc());
        if (oTransVehicleDescription.getModel().getModel().getYearModl() == null) {
            txtField06.setText("0");
        } else {
            txtField06.setText(String.valueOf(oTransVehicleDescription.getModel().getModel().getYearModl()));
        }
        if (oTransVehicleDescription.getModel().getModel().getTransMsn() != null && !oTransVehicleDescription.getModel().getModel().getTransMsn().trim().isEmpty()) {
            switch ((String.valueOf(oTransVehicleDescription.getModel().getModel().getTransMsn()))) {
                case "AT":
                    comboBox07.setValue("AUTOMATIC");
                    break;
                case "M":
                    comboBox07.setValue("MANUAL");
                    break;
                case "CVT":
                    comboBox07.setValue("CVT");
                    break;
                default:
                    break;
            }
        }
        if (oTransVehicleDescription.getModel().getModel().getVhclSize() != null && !oTransVehicleDescription.getModel().getModel().getVhclSize().trim().isEmpty()) {
            comboBox08.getSelectionModel().select(Integer.parseInt(oTransVehicleDescription.getModel().getModel().getVhclSize()));
        }
        if (oTransVehicleDescription.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06);
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
                        loJSON = oTransVehicleDescription.searchMake(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField02.setText(oTransVehicleDescription.getModel().getModel().getMakeDesc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            txtField02.requestFocus();
                            return;
                        }
                        break;
                    case "txtField03":
                        loJSON = oTransVehicleDescription.searchModel(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField03.setText(oTransVehicleDescription.getModel().getModel().getModelDsc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField03.setText("");
                            txtField03.requestFocus();
                            return;
                        }
                        break;
                    case "txtField04":
                        loJSON = oTransVehicleDescription.searchType(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField04.setText(oTransVehicleDescription.getModel().getModel().getTypeDesc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField04.setText("");
                            txtField04.requestFocus();
                            return;
                        }
                        break;
                    case "txtField05":
                        loJSON = oTransVehicleDescription.searchColor(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField05.setText(oTransVehicleDescription.getModel().getModel().getColorDsc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField05.setText("");
                            txtField05.requestFocus();
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

    private void initTextFieldFocus() {
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
                    oTransVehicleDescription.getModel().getModel().setYearModl(Integer.valueOf(lsValue));
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initCmboxFieldAction() {
        comboBox07.setOnAction(e -> {
            int selectedComboBox07 = comboBox07.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox07 >= 0) {
                    switch (selectedComboBox07) {
                        case 0:
                            oTransVehicleDescription.getModel().getModel().setTransMsn("AT");
                            break;
                        case 1:
                            oTransVehicleDescription.getModel().getModel().setTransMsn("M");
                            break;
                        case 2:
                            oTransVehicleDescription.getModel().getModel().setTransMsn("CVT");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        comboBox08.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox08.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVehicleDescription.getModel().getModel().setVhclSize(String.valueOf((comboBox08.getSelectionModel().getSelectedIndex())));
                }
            }
        });

        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransVehicleDescription.getModel().getModel().setMakeID("");
                                oTransVehicleDescription.getModel().getModel().setModelID("");
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
                                oTransVehicleDescription.getModel().getModel().setModelID("");
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
                                oTransVehicleDescription.getModel().getModel().setTypeID("");
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
                                oTransVehicleDescription.getModel().getModel().setColorID("");
                            }
                        }
                    }
                }
                );
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
                        oTransVehicleDescription.getModel().getModel().setTransMsn("AT");
                        break;
                    case 1:
                        oTransVehicleDescription.getModel().getModel().setTransMsn("M");
                        break;
                    case 2:
                        oTransVehicleDescription.getModel().getModel().setTransMsn("CVT");
                        break;
                    default:
                        break;
                }
            }
            if (comboBox08.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Vehicle Model Size", "Please select `Vehicle Model Size` value.");
                return false;
            } else {
                oTransVehicleDescription.getModel().getModel().setVhclSize(String.valueOf((comboBox08.getSelectionModel().getSelectedIndex())));
            }
        }
        return true;
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnMake, btnModel, btnType, btnActive, btnColor);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();

        String lsMakeID = "";
        if (oTransVehicleDescription.getModel().getModel().getMakeID() != null) {
            lsMakeID = oTransVehicleDescription.getModel().getModel().getMakeID();
        }
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransVehicleDescription = new Vehicle_Description(oApp, false, oApp.getBranchCode());
                loJSON = oTransVehicleDescription.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVehicleDescriptionFields();
                    pnEditMode = oTransVehicleDescription.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransVehicleDescription.updateRecord();
                pnEditMode = oTransVehicleDescription.getEditMode();
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
                        loJSON = oTransVehicleDescription.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Vehicle Description Information", (String) loJSON.get("message"));
                            loJSON = oTransVehicleDescription.openRecord(oTransVehicleDescription.getModel().getModel().getVhclID());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loadVehicleDescriptionFields();
                                initFields(pnEditMode);
                                pnEditMode = oTransVehicleDescription.getEditMode();
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
                    oTransVehicleDescription = new Vehicle_Description(oApp, false, oApp.getBranchCode());
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
                loJSON = oTransVehicleDescription.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVehicleDescriptionFields();
                    pnEditMode = oTransVehicleDescription.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Description Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransVehicleDescription.getModel().getModel().getVhclID();
                    loJSON = oTransVehicleDescription.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransVehicleDescription.openRecord(oTransVehicleDescription.getModel().getModel().getVhclID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadVehicleDescriptionFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransVehicleDescription.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransVehicleDescription.getModel().getModel().getVhclID();
                    loJSON = oTransVehicleDescription.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Vehicle Description Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransVehicleDescription.openRecord(oTransVehicleDescription.getModel().getModel().getVhclID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadVehicleDescriptionFields();
                        initFields(pnEditMode);
                        pnEditMode = oTransVehicleDescription.getEditMode();
                    }
                }
                break;
            case "btnMake":
                loadVehicleMake();
                break;
            case "btnModel":
                try {
                loadVehicleModel(lsMakeID, oTransVehicleDescription.getModel().getModel().getMakeDesc());

            } catch (SQLException ex) {
                Logger.getLogger(VehicleDescriptionController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnType":
                try {
                loadVehicleType(lsMakeID, oTransVehicleDescription.getModel().getModel().getMakeDesc());

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

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.setText("0");
        comboBox07.setValue(null);
        comboBox08.setValue(null);
        cboxActivate.setSelected(false);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        comboBox07.setDisable(!lbShow);
        comboBox08.setDisable(!lbShow);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnActive.setVisible(false);
        btnActive.setManaged(false);

        if (fnValue == EditMode.READY) {
            //show edit if user clicked save / browse
            if (oTransVehicleDescription.getModel().getModel().getRecdStat().equals("1")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
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
}
