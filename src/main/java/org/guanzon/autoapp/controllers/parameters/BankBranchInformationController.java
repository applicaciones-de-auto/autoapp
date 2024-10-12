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
public class BankBranchInformationController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private BankBranch oTrans;
    private final String pxeModuleName = "Bank Branch"; //Form Title
    private int pnEditMode;//Modifying field
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cBankType = FXCollections.observableArrayList("BANK", "CREDIT UNION", "INSURANCE COMPANY", "INVESTMENT COMPANIES");

    @FXML
    private GridPane branchFields;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnDeactivate, btnBrowse, btnClose, btnAddBankEntry, btnActive;
    @FXML
    private TextField txtField01_Branch, txtField02_Branch, txtField04_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
            txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private ComboBox<String> comboBox03_Branch;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) comboBox03_Branch.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new BankBranch(oApp, false, oApp.getBranchCode());

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
        List<TextField> loTxtField = Arrays.asList(txtField01_Branch, txtField02_Branch, txtField04_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01_Branch.setText(oTrans.getModel().getModel().getBankID());
        txtField02_Branch.setText(oTrans.getModel().getModel().getBankName());
        if (oTrans.getModel().getModel().getBankType() != null && !oTrans.getModel().getModel().getBankType().trim().isEmpty()) {
            switch ((String.valueOf(oTrans.getModel().getModel().getBankType()))) {
                case "bank":
                    comboBox03_Branch.setValue("BANK");
                    break;
                case "cred":
                    comboBox03_Branch.setValue("CREDIT UNION");
                    break;
                case "insc":
                    comboBox03_Branch.setValue("INSURANCE COMPANY");
                    break;
                case "invc":
                    comboBox03_Branch.setValue("INVESTMENT COMPANIES");
                    break;
                default:
                    break;
            }
        }
        txtField04_Branch.setText(oTrans.getModel().getModel().getBrBankID());
        txtField05_Branch.setText(oTrans.getModel().getModel().getBrBankNm());
        txtField06_Branch.setText(oTrans.getModel().getModel().getBrBankCd());
        txtField07_Branch.setText(oTrans.getModel().getModel().getContactP());
        txtField08_Branch.setText(oTrans.getModel().getModel().getProvName());
        txtField09_Branch.setText(oTrans.getModel().getModel().getTownName());
        txtField10_Branch.setText(oTrans.getModel().getModel().getZippCode());
        txtField11_Branch.setText(oTrans.getModel().getModel().getAddress());
        txtField12_Branch.setText(oTrans.getModel().getModel().getTelNo());
        txtField13_Branch.setText(oTrans.getModel().getModel().getFaxNo());
        if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern patt;
        patt = Pattern.compile("[0-9-,]*");
        txtField12_Branch.setTextFormatter(new TextFormatterUtil(patt)); //sTelNoxxx
        txtField13_Branch.setTextFormatter(new TextFormatterUtil(patt)); //sFaxNoxxx
    }

    @Override
    public void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField05_Branch, 30);
        CustomCommonUtil.addTextLimiter(txtField06_Branch, 10);
        CustomCommonUtil.addTextLimiter(txtField12_Branch, 30);
        CustomCommonUtil.addTextLimiter(txtField13_Branch, 15);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch);
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
                case 5:
                    oTrans.getModel().getModel().setBrBankNm(lsValue);
                    break;
                case 6:
                    oTrans.getModel().getModel().setBrBankCd(lsValue);
                    break;
                case 7:
                    oTrans.getModel().getModel().setContactP(lsValue);
                    break;
                case 11:
                    oTrans.getModel().getModel().setAddress(lsValue);
                    break;
                case 12:
                    oTrans.getModel().getModel().setTelNo(lsValue);
                    break;
                case 13:
                    oTrans.getModel().getModel().setFaxNo(lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01_Branch, txtField02_Branch, txtField04_Branch, txtField05_Branch, txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch, txtField12_Branch, txtField13_Branch);
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
                    case "txtField02_Branch":
                        loJSON = oTrans.searchBank(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField01_Branch.setText(oTrans.getModel().getModel().getBankID());
                            txtField02_Branch.setText(oTrans.getModel().getModel().getBankName());
                            switch ((String.valueOf(oTrans.getModel().getModel().getBankType()))) {
                                case "bank":
                                    comboBox03_Branch.setValue("BANK");
                                    break;
                                case "cred":
                                    comboBox03_Branch.setValue("CREDIT UNION");
                                    break;
                                case "insc":
                                    comboBox03_Branch.setValue("INSURANCE COMPANY");
                                    break;
                                case "invc":
                                    comboBox03_Branch.setValue("INVESTMENT COMPANIES");
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02_Branch.setText("");
                            txtField02_Branch.requestFocus();
                            return;
                        }
                        break;
                    case "txtField08_Branch":
                        loJSON = oTrans.searchProvince(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField08_Branch.setText(oTrans.getModel().getModel().getProvName());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField08_Branch.setText("");
                            txtField08_Branch.requestFocus();
                            return;
                        }
                        break;
                    case "txtField09_Branch":
                        loJSON = oTrans.searchTown(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField09_Branch.setText(oTrans.getModel().getModel().getTownName());
                            txtField10_Branch.setText(oTrans.getModel().getModel().getZippCode());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField09_Branch.setText("");
                            txtField09_Branch.requestFocus();
                            return;
                        }
                        break;
                }
                initFields(pnEditMode);
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate, btnActive, btnAddBankEntry);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        try {
            JSONObject loJSON = new JSONObject();
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
                    clearFields();
                    oTrans = new BankBranch(oApp, false, oApp.getBranchCode());
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
                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        clearFields();
                        oTrans = new BankBranch(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnBrowse":
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.YesNo(null, "Search Bank Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
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
                        ShowMessageFX.Warning(null, "Search Bank Branch Information", (String) loJSON.get("message"));
                    }
                    break;
                case "btnClose":
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                        CommonUtils.closeStage(btnClose);
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
                            if (txtField05_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch Name", "Warning", null);
                                txtField05_Branch.requestFocus();
                                return;
                            }
                            if (txtField06_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch Code", "Warning", null);
                                txtField06_Branch.requestFocus();
                                return;
                            }
                            if (txtField07_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Contact Person", "Warning", null);
                                txtField07_Branch.requestFocus();
                                return;
                            }

                            if (txtField08_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.", "Warning", null);
                                txtField08_Branch.requestFocus();
                                return;
                            }
                            if (txtField09_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality", "Warning", null);
                                txtField09_Branch.requestFocus();
                                return;
                            }
                            if (txtField11_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.", "Warning", null);
                                txtField11_Branch.requestFocus();
                                return;
                            }
                            if (txtField12_Branch.getText().length() <= 4) {
                                ShowMessageFX.Warning(getStage(), "Telephone No. cannot be less than 4 digits.", "Warning", null);
                                txtField12_Branch.requestFocus();
                                return;
                            }
                            if (txtField12_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.", "Warning", null);
                                txtField12_Branch.requestFocus();
                                return;
                            }
                            if (txtField13_Branch.getText().trim().equals("")) {
                                ShowMessageFX.Warning(getStage(), "Please enter a value for Fax No.", "Warning", null);
                                txtField13_Branch.requestFocus();
                                return;
                            }
                        }

                    } else {
                        return;
                    }
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Bank Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBrBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
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
                        String fsValue = oTrans.getModel().getModel().getBrBankID();
                        loJSON = oTrans.deactivateRecord(fsValue);
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Bank Branch Information", (String) loJSON.get("message"));
                        } else {
                            ShowMessageFX.Warning(null, "Bank Branch Information", (String) loJSON.get("message"));
                        }
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBrBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    }
                    break;
                case "btnActive":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                        String fsValue = oTrans.getModel().getModel().getBrBankID();
                        loJSON = oTrans.activateRecord(fsValue);
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Bank Branch Information", (String) loJSON.get("message"));
                        } else {
                            ShowMessageFX.Warning(null, "Bank Branch Information", (String) loJSON.get("message"));
                        }
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getBrBankID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    }
                    break;

            }
            initFields(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(BankBranchInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initComboBoxItems() {
        comboBox03_Branch.setItems(cBankType);
    }

    @Override
    public void initFieldsAction() {
    }

    @Override
    public void initTextFieldsProperty() {
        txtField02_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setBankID("");
                                oTrans.getModel().getModel().setBankName("");
                                oTrans.getModel().getModel().setProvName("");
                                oTrans.getModel().getModel().setTownID("");
                                oTrans.getModel().getModel().setTownName("");
                                txtField01_Branch.setText("");
                                comboBox03_Branch.setValue("");
                                clearBranchFields();

                            }
                        }
                        initFields(pnEditMode);
                    }
                }
                );
        txtField08_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setProvName("");
                                oTrans.getModel().getModel().setTownID("");
                                txtField09_Branch.setText("");
                                txtField10_Branch.setText("");
                                txtField11_Branch.setText("");
                                initFields(pnEditMode);
                            }
                        }

                    }
                }
                );
        txtField09_Branch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setTownID("");
                                oTrans.getModel().getModel().setTownName("");
                                txtField10_Branch.setText("");
                                txtField11_Branch.setText("");
                                initFields(pnEditMode);
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
        CustomCommonUtil.setText("", txtField01_Branch, txtField02_Branch,
                txtField04_Branch, txtField05_Branch,
                txtField06_Branch, txtField07_Branch,
                txtField08_Branch, txtField09_Branch,
                txtField10_Branch, txtField11_Branch,
                txtField12_Branch, txtField13_Branch);
        comboBox03_Branch.setValue("");
        cboxActivate.setSelected(false);
    }

    private void clearBranchFields() {
        CustomCommonUtil.setText("", txtField05_Branch,
                txtField06_Branch, txtField07_Branch, txtField08_Branch,
                txtField09_Branch, txtField10_Branch, txtField11_Branch,
                txtField12_Branch, txtField13_Branch);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField02_Branch.setDisable(!lbShow);
        CustomCommonUtil.setDisable(!(lbShow && !txtField02_Branch.getText().isEmpty()),
                txtField05_Branch, txtField06_Branch, txtField07_Branch,
                txtField08_Branch, txtField12_Branch, txtField13_Branch);
        txtField09_Branch.setDisable(!(lbShow && !txtField08_Branch.getText().isEmpty()));
        txtField11_Branch.setDisable(!(lbShow && !txtField09_Branch.getText().isEmpty()));
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnEdit, btnDeactivate, btnActive);
        CustomCommonUtil.setManaged(false, btnEdit, btnDeactivate, btnActive);
        if (fnValue == EditMode.UPDATE) {
            CustomCommonUtil.setDisable(true, txtField02_Branch, txtField05_Branch,
                    txtField06_Branch, txtField08_Branch, txtField09_Branch, txtField11_Branch);
        }
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

    private void loadBankEntry() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/Bank.fxml"));

            BankController loControl = new BankController();
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

}
