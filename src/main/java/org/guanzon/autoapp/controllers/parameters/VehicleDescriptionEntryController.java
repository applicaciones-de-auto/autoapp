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
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleDescriptionEntryController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Vehicle Description"; //Form Title
//    private Vehicle oTransVehicleDescription;
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
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private ComboBox<String> comboBox07;
    @FXML
    private Button btnMake;
    @FXML
    private Button btnType;
    @FXML
    private Button btnColor;
    @FXML
    private Button btnDeactivate;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private ComboBox<String> comboBox06;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private Button btnModel;

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
//         / oTransVehicleDescription = new Vehicle_VehicleDescription(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        comboBox06.setItems(cTransmission);
        comboBox07.setItems(cModelsize);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadVehicleDescriptionFields() {
//        txtField01.setText(oTransVehicleDescription.getModel().getModel().getVehicleVehicleDescriptionID());
//        txtField02.setText(oTransVehicleDescription.getModel().getModel().getVehicleVehicleDescriptionDescription());
//        txtField03.setText(oTransVehicleDescription.getModel().getModel().getVehicleVehicleTYPE());
//        txtField04.setText(oTransVehicleDescription.getModel().getModel().getVehicleVehicleColor());
//        txtField05.setText(oTransVehicleDescription.getModel().getModel().getVehicleVehicleYear());
//        if (oTransVehicleDescription.getModel().getModel().getCvilStat() != null && !oTransVehicleDescription.getModel().getModel().getCvilStat().trim().isEmpty()) {
//            comboBox06.getSelectionModel().select(Integer.parseInt(oTransVehicleDescription.getModel().getModel().getCvilStat()));
//        }
//        if (oTrans.getModel().getModel().getCvilStat() != null && !oTransVehicleDescription.getModel().getModel().getCvilStat().trim().isEmpty()) {
//            comboBox07.getSelectionModel().select(Integer.parseInt(oTransVehicleDescription.getModel().getModel().getCvilStat()));
//        }
//        if (oTransVehicleDescription.getMaster(3).toString().equals("1")) {
//            cboxActivate.setSelected(true);
//        } else {
//            cboxActivate.setSelected(false);
//        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly;
        textOnly = Pattern.compile("[A-Za-z ]*");
        txtField01.setTextFormatter(new InputTextFormatterUtil(textOnly));
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new InputTextFormatterUtil(textOnly));

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05);
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
            JSONObject loJSON;
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField01":
//                        loJSON = oTransColor.searchVhclMake(lsValue);
//                        if ("success".equals(loJSON.get("result"))) {
//                            txtField01.setText(oTransColor.getModel().getModel().getVhclMake());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField01.setText("");
//                            txtField01.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField02":
//                        loJSON = oTransColor.searchVhclModel(lsValue);
//                        if ("success".equals(loJSON.get("result"))) {
//                            txtField02.setText(oTransColor.getModel().getModel().getVhclMake());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField02.setText("");
//                            txtField02.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField03":
//                        loJSON = oTransColor.searchVhclType(lsValue);
//                        if ("success".equals(loJSON.get("result"))) {
//                            txtField03.setText(oTransColor.getModel().getModel().getVhclMake());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField03.setText("");
//                            txtField03.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField04":
//                        loJSON = oTransColor.searchVhclColor(lsValue);
//                        if ("success".equals(loJSON.get("result"))) {
//                            txtField04.setText(oTransColor.getModel().getModel().getVhclMake());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField04.setText("");
//                            txtField04.requestFocus();
//                            return;
//                        }
                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextArea) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextArea) event.getSource());
            }
        }

    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField05);
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
                case 5:
//                    oTransVehicleDescriptionVehicleDescription.getModel().getModel().setVehicleVehicleYear(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initCmboxFieldAction() {
        comboBox06.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox06.getSelectionModel().getSelectedIndex() >= 0) {
//                    oTransColor.getModel().getModel().setVehicleTrabs(String.valueOf((cmboBox05.getSelectionVehicleDescription().getSelectedIndex())));
                }
            }
        });
        comboBox07.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox07.getSelectionModel().getSelectedIndex() >= 0) {
//                    oTransColor.getModel().getModel().setModelSize(String.valueOf((cmboBox06.getSelectionVehicleDescription().getSelectedIndex())));
                }
            }
        });

        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
//                                oTransColor.getModel().getModel().setVchlMake("");
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
//                                oTransColor.getModel().getModel().setVchlModel("");
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
//                                oTransColor.getModel().getModel().setVchlType("");
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
//                                oTransColor.getModel().getModel().setVchlColor("");
                            }
                        }
                    }
                }
                );
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox06.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning("No `Transmission` selected.", "Vehicle Transmission", "Please select `Vehicle Transmission` value.");
                return false;
            } else {
//            oTransColor.getModel().getModel().setVehicleTyp(String.valueOf((cmboBox05.getSelectionVehicleDescription().getSelectedIndex())));
            }
            if (comboBox07.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning("No `Vehicle Model Size` selected.", "Vehicle Model Size", "Please select `Vehicle Model Size` value.");
                return false;
            } else {
//             oTransColor.getModel().getModel().setBodyTyp(String.valueOf((cmboBox06.getSelectionVehicleDescription().getSelectedIndex())));
            }
        }
        return true;
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnMake, btnModel, btnType, btnColor);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject poJson;
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
//                clearFields();
//                oTransVehicleDescription = new Vehicle_VehicleDescription(oApp, false, oApp.getBranchCode());
//                poJson = oTransVehicleDescription.newRecord();
//                if ("success".equals((String) poJson.get("result"))) {
//                    loadVehicleDescriptionFields();
////                    pnEditMode = oTransVehicleDescription.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, (String) poJson.get("message"));
//                }
                break;
            case "btnEdit":
//                poJson = oTransVehicleDescription.updateRecord();
//                pnEditMode = oTransVehicleDescription.getEditMode();
//                if ("error".equals((String) poJson.get("result"))) {
//                    ShowMessageFX.Warning((String) poJson.get("message"), "Warning", null);
//                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Description Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField01.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Make.", "Warning", null);
                        txtField01.requestFocus();
                        return;
                    }
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Model.", "Warning", null);
                        txtField02.requestFocus();
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Type.", "Warning", null);
                        txtField03.requestFocus();
                        return;
                    }
                    if (txtField04.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Color.", "Warning", null);
                        txtField04.requestFocus();
                        return;
                    }

                    if (txtField05.getText().trim().equals("") || Integer.parseInt(txtField05.getText()) < 1900) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Year.", "Warning", null);
                        txtField05.requestFocus();
                        return;
                    }
                    if (setSelection()) {
//                        poJson = oTransVehicleDescription.saveRecord();
//                    if ("success".equals((String) poJson.get("result"))) {
//                        ShowMessageFX.Information(null, "Vehicle Description Information", (String) poJson.get("message"));
//                        poJson = oTransVehicleDescription.openRecord(oTransVehicleDescription.getModel().getModel().getClientID());
//                        if ("success".equals((String) poJson.get("result"))) {
//                            loadVehicleDescriptionFields();
//                            initFields(pnEditMode);
//                            pnEditMode = oTransVehicleDescription.getEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) poJson.get("message"));
//                        return;
//                    }
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
//                    oTranVehicleDescription = new Vehicle_VehicleDescription(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                JSONObject poJSon;
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Description Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
//                poJSon = oTransVehicleDescription.searchRecord("", false);
//                if ("success".equals((String) poJSon.get("result"))) {
//                    loadVehicleDescriptionFields();
////                    pnEditMode = oTransVehicleDescriptionVehicleDescription.getEditMode();
//                    initFields(pnEditMode);
//                } else {
//                    ShowMessageFX.Warning(null, "Search Vehicle Description Information", (String) poJSon.get("message"));
//                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
//                    String fsValue = oTransVehicleDescription.getModel().getModel().getVehicleVehicleDescriptionID();
                    boolean fbStatus = false;
                    if (btnDeactivate.getText().equals("Activate")) {
                        fbStatus = true;
                    } else {
                        fbStatus = false;
                    }
//                    poJSon = oTransVehicleDescription.UpdateRecordStatus(fsValue, fbStatus)
//                    if ("success".equals((String) poJSon.get("result"))) {
////                        ShowMessageFX.Information(null, "Vehicle Description Information", (String) poJson.get("message"));
//                        poJson = oTransVehicleDescription.openRecord(oTransVehicleDescription.getModel().getModel().getVehicleVehicleDescriptionID());
////                        if ("success".equals((String) poJson.get("result"))) {
////                            loadVehicleDescriptionFields();
////                            pnEditMode = oTransVehicleDescription.getEditMode();
////                        }
//                    }
                }
                break;
            case "btnMake":
                loadVehicleMake();
                break;
            case "btnModel":
            case "btnType":
//                if (oTrans.getMaster(3).toString().equals("") || oTrans.getMaster(3).toString().isEmpty() || oTrans.getMaster(3).toString() == null) {
//                    ShowMessageFX.Warning(getStage(), "Kindly ensure that the Vehicle Make is selected.", "Warning", "");
//                    txtField03.requestFocus();
//                    return;
//                }
//                if (lsButton.equals("btnModel")) {
//                    loadVehicleModel(oTrans.getMaster(3).toString(), oTrans.getMaster(15).toString());
//                } else if (lsButton.equals("btnType")) {
//                    loadVehicleType(oTrans.getMaster(3).toString(), oTrans.getMaster(15).toString());
//                }
                break;
            case "btnColor":
                loadVehicleColor();
                break;
            default:
                ShowMessageFX.Warning("Please contact admin to assist about no button available", "Integrated Automotive System", pxeModuleName);
                break;
        }
        initFields(pnEditMode);
    }

    private void loadVehicleMake() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleMakeEntry.fxml"));

            VehicleMakeEntryController loControl = new VehicleMakeEntryController();
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    /*MODEL WINDOW*/
    private void loadVehicleModel(String sSourceID, String sSourceDesc) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleModelEntry.fxml"));
            VehicleModelEntryController loControl = new VehicleModelEntryController();
            loControl.setGRider(oApp);
            loControl.setMakeID(sSourceID);
            loControl.setMakeDesc(sSourceDesc);
            loControl.setOpenEvent(true);
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    /*TYPE WINDOW*/
    private void loadVehicleType(String sSourceID, String sSourceDesc) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleTypeForm.fxml"));
            VehicleTypeEntryController loControl = new VehicleTypeEntryController();
            loControl.setGRider(oApp);
            loControl.setMakeID(sSourceID);
            loControl.setMakeDesc(sSourceDesc);
            loControl.setOpenEvent(true);
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadVehicleColor() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleColorEntry.fxml"));

            VehicleColorEntryController loControl = new VehicleColorEntryController();
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
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.setText("0");
        comboBox06.setValue(null);
        comboBox07.setValue(null);
        cboxActivate.setSelected(false);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        comboBox06.setDisable(!lbShow);
        comboBox07.setDisable(!lbShow);
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
        if (fnValue == EditMode.READY) {
            //show edit if user clicked save / browse
//            if (oTransVehicleDescription.getMaster(3).toString().equals("1")) {
//                btnDeactivate.setText("Deactivate");
//                btnDeactivate.setVisible(true);
//                btnDeactivate.setManaged(true);
//                btnEdit.setVisible(true);
//                btnEdit.setManaged(true);
//            } else {
//                btnDeactivate.setText("Activate");
//                btnDeactivate.setVisible(true);
//                btnDeactivate.setManaged(true);
//            }
        }
    }
}
