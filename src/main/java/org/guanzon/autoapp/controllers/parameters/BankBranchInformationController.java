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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.BankBranch;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class BankBranchInformationController implements Initializable, ScreenInterface {

    private GRider oApp;
    private BankBranch oTransBankBranch;
    private final String pxeModuleName = "Bank Branch"; //Form Title
    private int pnEditMode;//Modifying field
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private GridPane branchFields;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnDeactivate, btnBrowse, btnClose, btnAddBankEntry, btnActive;
    @FXML
    private TextField txtField01_Branch;
    @FXML
    private TextField txtField02_Branch, txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
            txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch, txtField14_Branch;
    ObservableList<String> cBankType = FXCollections.observableArrayList("BANK", "CREDIT UNION", "INSURANCE COMPANY", "INVESTMENT COMPANIES");
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private ComboBox<String> comboBox04_Branch;

    private Stage getStage() {
        return (Stage) comboBox04_Branch.getScene().getWindow();
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
        // TODO
        oTransBankBranch = new BankBranch(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        comboBox04_Branch.setItems(cBankType);
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
        List<TextField> loTxtField = Arrays.asList(txtField01_Branch, txtField02_Branch, txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch, txtField14_Branch);
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
                case 6:
                    oTransBankBranch.getModel().getModel().setBrBankNm(lsValue);
                    break;
                case 7:
                    oTransBankBranch.getModel().getModel().setBrBankCd(lsValue);
                    break;
                case 8:
                    oTransBankBranch.getModel().getModel().setContactP(lsValue);
                    break;
                case 9:
                    oTransBankBranch.getModel().getModel().setAddress(lsValue);
                    break;
                case 13:
                    oTransBankBranch.getModel().getModel().setTelNo(lsValue);
                    break;
                case 14:
                    oTransBankBranch.getModel().getModel().setFaxNo(lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01_Branch, txtField02_Branch, txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch, txtField14_Branch);
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
//                        loJSON = oTransBankBranch.searchBankName(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField02_Branch.setText(oTransBankBranch.getModel().getModel().getBankName());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField02_Branch.setText("");
//                            txtField02_Branch.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField10_Branch":
//                        loJSON = oTransBankBranch.searchProvinceName(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField10_Branch.setText(oTransBankBranch.getModel().getModel().getProvName());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField10_Branch.setText("");
//                            txtField10_Branch.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField11_Branch":
//                        loJSON = oTransBankBranch.searchTownName(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField11_Branch.setText(oTransBankBranch.getModel().getModel().getTownName());
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
                                oTransBankBranch.getModel().getModel().setBankID("");
                                oTransBankBranch.getModel().getModel().setBankName("");
                                oTransBankBranch.getModel().getModel().setProvName("");
                                oTransBankBranch.getModel().getModel().setTownID("");
                                oTransBankBranch.getModel().getModel().setTownName("");
                                clearBranchFields();
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField10_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransBankBranch.getModel().getModel().setProvName("");
                                oTransBankBranch.getModel().getModel().setTownID("");
                                txtField11_Branch.setText("");
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
                                oTransBankBranch.getModel().getModel().setTownID("");
                                oTransBankBranch.getModel().getModel().setTownName("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive, btnAddBankEntry);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        try {
            JSONObject loJSON = new JSONObject();
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
                    clearFields();
                    oTransBankBranch = new BankBranch(oApp, false, oApp.getBranchCode());
                    loJSON = oTransBankBranch.newRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadBankbranchField();
                        pnEditMode = oTransBankBranch.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    break;
                case "btnEdit":
                    loJSON = oTransBankBranch.updateRecord();
                    pnEditMode = oTransBankBranch.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                    break;

                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        clearFields();
                        oTransBankBranch = new BankBranch(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnSave":
                    //Validate before saving
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (txtField02_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Bank Name.", "Warning", null);
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
                                ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.", "Warning", null);
                                txtField10_Branch.requestFocus();
                                return;
                            }
                            if (txtField11_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality", "Warning", null);
                                txtField11_Branch.requestFocus();
                                return;
                            }
                            if (txtField12_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for ZipCode No.", "Warning", null);
                                txtField12_Branch.requestFocus();
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
                    loJSON = oTransBankBranch.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                        loJSON = oTransBankBranch.openRecord(oTransBankBranch.getModel().getModel().getBrBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadBankbranchField();
                            initFields(pnEditMode);
                            pnEditMode = oTransBankBranch.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    break;
                case "btnAddBankEntry":
                    loadBankEntry();
                    break;
                case "btnDeactivate":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                        String fsValue = oTransBankBranch.getModel().getModel().getBrBankID();
                        loJSON = oTransBankBranch.deactivateRecord(fsValue);
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Bank Branch Information", (String) loJSON.get("message"));
                        } else {
                            ShowMessageFX.Warning(null, "Bank Branch Information", (String) loJSON.get("message"));
                        }
                        loJSON = oTransBankBranch.openRecord(oTransBankBranch.getModel().getModel().getBrBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadBankbranchField();
                            initFields(pnEditMode);
                            pnEditMode = oTransBankBranch.getEditMode();
                        }
                    }
                    break;
                case "btnActive":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                        String fsValue = oTransBankBranch.getModel().getModel().getBrBankID();
                        loJSON = oTransBankBranch.activateRecord(fsValue);
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Bank Branch Information", (String) loJSON.get("message"));
                        } else {
                            ShowMessageFX.Warning(null, "Bank Branch Information", (String) loJSON.get("message"));
                        }
                        loJSON = oTransBankBranch.openRecord(oTransBankBranch.getModel().getModel().getBrBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadBankbranchField();
                            initFields(pnEditMode);
                            pnEditMode = oTransBankBranch.getEditMode();
                        }
                    }
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
            }
            initFields(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(BankBranchInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadBankEntry() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("BankEntry.fxml"));

            BankEntryController loControl = new BankEntryController();
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

    private void loadBankbranchField() {
        txtField01_Branch.setText(oTransBankBranch.getModel().getModel().getBankID());
        txtField02_Branch.setText(oTransBankBranch.getModel().getModel().getBankName());
//        txtField03_Branch.setText(oTransBankBranch.getModel().getModel().getBankCode());
        //        if (oTransBankBranch.getModel().getModel().getBankType() != null && !oTransBank.getModel().getModel().getBankType().trim().isEmpty()) {
//            switch ((String.valueOf(oTransBank.getModel().getModel().getBankType()))) {
//                case "BANK":
//                    comboBox03_Branch.setValue("BANK");
//                    break;
//                case "CRED":
//                    comboBox03_Branch.setValue("CREDIT UNION");
//                    break;
//                case "INSC":
//                    comboBox03_Branch.setValue("INSURANCE COMPANY");
//                    break;
//                case "INVC":
//                    comboBox03_Branch.setValue("INVESTMENT COMPANIES");
//                    break;
//                default:
//                    break;
//            }
//        }
        txtField05_Branch.setText("");
        txtField06_Branch.setText("");
        txtField07_Branch.setText("");
        txtField08_Branch.setText("");
        txtField09_Branch.setText("");
        txtField10_Branch.setText("");
        txtField11_Branch.setText("");
        txtField12_Branch.setText("");
        txtField13_Branch.setText("");
        if (oTransBankBranch.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
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
        txtField10_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField11_Branch.setDisable(!(lbShow && !txtField10_Branch.getText().isEmpty()));
        txtField12_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField13_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
        txtField14_Branch.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()));
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
            if (oTransBankBranch.getModel().getModel().getRecdStat().equals("1")) {
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
