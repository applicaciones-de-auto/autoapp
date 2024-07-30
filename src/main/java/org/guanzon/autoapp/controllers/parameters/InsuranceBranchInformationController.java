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
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.auto.main.parameter.InsuranceBranch;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InsuranceBranchInformationController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private InsuranceBranch oTrans;
    private final String pxeModuleName = "Insurance"; //Form Title
    private int pnEditMode;//Modifying fields
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cInsurType = FXCollections.observableArrayList("NON IN HOUSE", "IN HOUSE (DIRECT)", "SUB-IN HOUSE (ACCREDITED)");
    @FXML
    private GridPane branchFields;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnDeactivate, btnBrowse, btnClose, btnAddInsuranceCompany, btnActive;
    @FXML
    private TextField txtField01_Branch, txtField02_Branch, txtField03_Branch, txtField05_Branch, txtField06_Branch,
            txtField07_Branch, txtField08_Branch, txtField09_Branch, txtField10_Branch, txtField11_Branch,
            txtField12_Branch, txtField13_Branch, txtField14_Branch;
    private ComboBox<String> comboBox04;
    @FXML
    private ComboBox<String> comboBox04_Branch;
    @FXML
    private CheckBox cboxActivate;

    private Stage getStage() {
        return (Stage) txtField05_Branch.getScene().getWindow();
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
//        oTransInsuranceBranch = new InsuranceBranch(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        comboBox04_Branch.setItems(cInsurType);
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initTextFieldPattern() {
        Pattern patt;
        patt = Pattern.compile("[A-Za-z0-9-]*");
        txtField12_Branch.setTextFormatter(new InputTextFormatterUtil(patt)); //sTelNoxxx
        txtField13_Branch.setTextFormatter(new InputTextFormatterUtil(patt)); //sFaxNoxxx
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01_Branch, txtField02_Branch, txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch, txtField14_Branch);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField13_Branch, txtField14_Branch);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    /* Set TextField Value to Master Class */
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
//                case 6:
//                    oTransInsuranceBranch.getModel().getModel().setInsuranceNm(lsValue);
//                    break;
//                case 7:
//                    oTransInsuranceBranch.getModel().getModel().setInsuranceCd(lsValue);
//                    break;
//                case 8:
//                    oTransInsuranceBranch.getModel().getModel().setContactP(lsValue);
//                    break;
//                case 9:
//                    oTransInsuranceBranch.getModel().getModel().setAddress(lsValue);
//                    break;
//                case 13:
//                    oTransInsuranceBranch.getModel().getModel().setTelNo(lsValue);
//                    break;
//                case 14:
//                    oTransInsuranceBranch.getModel().getModel().setFaxNo(lsValue);
//                    break;
            }
        } else {
            loTxtField.selectAll();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02_Branch, txtField03_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField13_Branch, txtField14_Branch);
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
                    case "txtField02_Branch":
//                        loJSON = oTransInsuranceBranch.searchInsuranceName(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField02_Branch.setText(oTransInsuranceBranch.getModel().getModel().getInsuranceName());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField02_Branch.setText("");
//                            txtField02_Branch.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField10_Branch":
//                        loJSON = oTransInsuranceBranch.searchProvinceName(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField10_Branch.setText(oTransInsuranceBranch.getModel().getModel().getProvName());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField10_Branch.setText("");
//                            txtField10_Branch.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField11_Branch":
//                        loJSON = oTransInsuranceBranch.searchTownName(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField11_Branch.setText(oTransInsuranceBranch.getModel().getModel().getTownName());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField11_Branch.setText("");
//                            txtField11_Branch.requestFocus();
//                            return;
//                        }
                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            }
        }
    }

    private void initCmboxFieldAction() {
        txtField02_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
//                                oTransInsuranceBranch.getModel().getModel().setInsuranceID("");
//                                oTransInsuranceBranch.getModel().getModel().setInsuranceName("");
//                                oTransInsuranceBranch.getModel().getModel().setProvName("");
//                                oTransInsuranceBranch.getModel().getModel().setTownID("");
//                                oTransInsuranceBranch.getModel().getModel().setTownName("");
                                clearBranchFields();
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        comboBox04_Branch.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04_Branch.getSelectionModel().getSelectedIndex() >= 0) {
//                    oTransInsuranceBranch.getModel().getModel().setInsuranceType(String.valueOf((comboBox04_Branch.getSelectionModel().getSelectedIndex())));
                    initFields(pnEditMode);
                }
            }
        });

        txtField10_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
//                                oTransInsuranceBranch.getModel().getModel().setProvName("");
//                                oTransInsuranceBranch.getModel().getModel().setTownID("");
                                txtField10_Branch.setText("");
                                initFields(pnEditMode);
                            }
                        }

                    }
                }
                );
        txtField11_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
//                                oTransInsuranceBranch.getModel().getModel().setTownID("");
//                                oTransInsuranceBranch.getModel().getModel().setTownName("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive, btnAddInsuranceCompany);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        try {
            JSONObject loJSON = new JSONObject();
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
//                    clearFields();
//                    oTransInsuranceBranch = new InsuranceBranch(oApp, false, oApp.getBranchCode());
//                    loJSON = oTransInsuranceBranch.newRecord();
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        loadInsurancebranchField();
//                        pnEditMode = oTransInsuranceBranch.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                    }
                    break;
                case "btnEdit":
//                    loJSON = oTransInsuranceBranch.updateRecord();
//                    pnEditMode = oTransInsuranceBranch.getEditMode();
//                    if ("error".equals((String) loJSON.get("result"))) {
//                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
//                    }
//                    break;
                case "btnCancel":
//                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
//                        clearFields();
//                        oTransInsuranceBranch = new InsuranceBranch(oApp, false, oApp.getBranchCode());
//                        pnEditMode = EditMode.UNKNOWN;
//                    }
                    break;
                case "btnSave":
                    //Validate before saving
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (txtField02_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Insurance Name.", "Warning", null);
                            txtField02_Branch.requestFocus();
                            return;
                        }
                        if (!txtField02_Branch.getText().isEmpty()) {
                            if (txtField06_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch Name", "Warning", null);
                                txtField06_Branch.requestFocus();
                                return;
                            }
                            if (txtField07_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch Code", "Warning", null);
                                txtField07_Branch.requestFocus();
                                return;
                            }
                            if (txtField08_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Contact Person", "Warning", null);
                                txtField08_Branch.requestFocus();
                                return;
                            }
                            if (txtField09_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.", "Warning", null);
                                txtField09_Branch.requestFocus();
                                return;
                            }
                            if (txtField10_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value value for Province.", "Warning", null);
                                txtField10_Branch.requestFocus();
                                return;
                            }
                            if (txtField11_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.", "Warning", null);
                                txtField11_Branch.requestFocus();
                                return;
                            }
                            if (txtField13_Branch.getText().length() <= 4) {
                                ShowMessageFX.Warning(getStage(), "Telephone No. cannot be less than 4 digits.", "Warning", null);
                                txtField13_Branch.requestFocus();
                                return;
                            }
                            if (txtField13_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.", "Warning", null);
                                txtField13_Branch.requestFocus();
                                return;
                            }
                            if (txtField14_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Fax No.", "Warning", null);
                                txtField14_Branch.requestFocus();
                                return;
                            }
                        }

                    } else {
                        return;
                    }
//                    loJSON = oTransInsuranceBranch.saveRecord();
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        ShowMessageFX.Information(null, "Insurance Information", (String) loJSON.get("message"));
//                        loJSON = oTransInsuranceBranch.openRecord(oTransInsuranceBranch.getModel().getModel().getInsuranceID());
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            loadInsurancebranchField();
//                            initFields(pnEditMode);
//                            pnEditMode = oTransInsuranceBranch.getEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        return;
//                    }
                    break;
                case "btnAddInsuranceEntry":
                    loadInsuranceEntry();
                    break;
                case "btnDeactivate":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
//                        String fsValue = oTransInsuranceBranch.getModel().getModel().getBrInsuranceID();
//                        loJSON = oTransInsuranceBranch.deactivateRecord(fsValue);
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            ShowMessageFX.Information(null, "Insurance Branch Information", (String) loJSON.get("message"));
//                        } else {
//                            ShowMessageFX.Warning(null, "Insurance Branch Information", (String) loJSON.get("message"));
//                        }
//                        loJSON = oTransInsuranceBranch.openRecord(oTransInsuranceBranch.getModel().getModel().getBrInsuranceID());
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            loadInsurancebranchField();
//                            initFields(pnEditMode);
//                            pnEditMode = oTransInsuranceBranch.getEditMode();
//                        }
//                    }
                    break;
                case "btnActive":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
//                        String fsValue = oTransInsuranceBranch.getModel().getModel().getBrInsuranceID();
//                        loJSON = oTransInsuranceBranch.activateRecord(fsValue);
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            ShowMessageFX.Information(null, "Insurance Branch Information", (String) loJSON.get("message"));
//                        } else {
//                            ShowMessageFX.Warning(null, "Insurance Branch Information", (String) loJSON.get("message"));
//                        }
//                        loJSON = oTransInsuranceBranch.openRecord(oTransInsuranceBranch.getModel().getModel().getBrInsuranceID());
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            loadInsurancebranchField();
//                            initFields(pnEditMode);
//                            pnEditMode = oTransInsuranceBranch.getEditMode();
//                        }
//                    }
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
            }
            initFields(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(InsuranceBranchInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadInsuranceEntry() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("InsuranceCompanyEntry.fxml"));

            InsuranceCompanyEntryController loControl = new InsuranceCompanyEntryController();
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadInsurancebranchField() {
//        txtField01_Branch.setText(oTransInsuranceBranch.getModel().getModel().getInsuranceID());
//        txtField02_Branch.setText(oTransInsuranceBranch.getModel().getModel().getInsuranceName());
//        txtField03_Branch.setText(oTransInsuranceBranch.getModel().getModel().getInsuranceCode());
//        if (oTransInsuranceBranch.getModel().getModel().getInsuranceType() != null && !oTransInsuranceBranch.getModel().getModel().getInsuranceType().trim().isEmpty()) {
//            comboBox04_Branch.getSelectionModel().select(Integer.parseInt(oTransInsuranceBranch.getModel().getModel().getInsuranceType()));
//        }
//        txtField05_Branch.setText(oTransInsuranceBranch.getModel().getModel().getInsuranceBranchID());
//        txtField06_Branch.setText("");
//        txtField07_Branch.setText("");
//        txtField08_Branch.setText("");
//        txtField09_Branch.setText("");
//        txtField10_Branch.setText("");
//        txtField11_Branch.setText("");
//        txtField12_Branch.setText("");
//        txtField13_Branch.setText("");
//        txtField14_Branch.setText("");
//        if (oTransInsuranceBranch.getModel().getModel().getRecdStat().equals("1")) {
//            cboxActivate.setSelected(true);
//        } else {
//            cboxActivate.setSelected(false);
//        }
    }

    private void clearBranchFields() {
        txtField05_Branch.setText("");
        txtField06_Branch.setText("");
        txtField07_Branch.setText("");
        txtField08_Branch.setText("");
        txtField09_Branch.setText("");
        txtField10_Branch.setText("");
        txtField11_Branch.setText("");
        txtField12_Branch.setText("");
        txtField13_Branch.setText("");
        txtField14_Branch.setText("");
    }

    private void clearFields() {
        txtField01_Branch.setText("");
        txtField02_Branch.setText("");
        txtField03_Branch.setText("");
        comboBox04_Branch.setValue("");
        txtField05_Branch.setText("");
        txtField06_Branch.setText("");
        txtField07_Branch.setText("");
        txtField08_Branch.setText("");
        txtField09_Branch.setText("");
        txtField10_Branch.setText("");
        txtField11_Branch.setText("");
        txtField12_Branch.setText("");
        txtField13_Branch.setText("");
        txtField14_Branch.setText("");
        cboxActivate.setSelected(false);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField02_Branch.setDisable(!lbShow);
        txtField03_Branch.setDisable(!lbShow);
        txtField05_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField06_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField07_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField08_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField09_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField10_Branch.setDisable(!(lbShow && !txtField10_Branch.getText().isEmpty()));
        txtField11_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField12_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField13_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnActive.setVisible(false);
        btnActive.setManaged(false);

        if (fnValue == EditMode.READY) {
//            if (oTransInsuranceBranch.getModel().getModel().getRecdStat().equals("1")) {
//                btnEdit.setVisible(true);
//                btnEdit.setManaged(true);
//                btnDeactivate.setVisible(true);
//                btnDeactivate.setManaged(true);
//                btnActive.setVisible(false);
//                btnActive.setManaged(false);
//            } else {
//                btnDeactivate.setVisible(false);
//                btnDeactivate.setManaged(false);
//                btnActive.setVisible(true);
//                btnActive.setManaged(true);
//            }
        }
    }
}
