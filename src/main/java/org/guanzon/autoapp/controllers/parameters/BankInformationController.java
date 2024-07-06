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
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BankInformationController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private BankInformation oTrans;
    private final String pxeModuleName = "Bank"; //Form Title
    private int pnEditMode;//Modifying fields
    private String psOldTransNo = "";
    private String psTransNo = "";
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private GridPane branchFields;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnDeactivate, btnBrowse, btnClose, btnAddBankEntry;
    @FXML
    private TextField txtField04_Branch, txtField18_Branch, txtField19_Branch, txtField01_Branch, txtField02_Branch,
            txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField14_Branch, txtField16_Branch, txtField08_Branch,
            txtField09_Branch, txtField10_Branch;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private CheckBox checkBox12;
    private List<TextField> poTxtField = Arrays.asList(txtField04_Branch, txtField18_Branch, txtField19_Branch, txtField01_Branch, txtField02_Branch,
            txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField14_Branch, txtField16_Branch, txtField08_Branch,
            txtField09_Branch, txtField10_Branch
    );
    private List<TextField> poTxtFieldDisable = Arrays.asList(txtField02_Branch, txtField03_Branch, txtField05_Branch, txtField06_Branch, txtField14_Branch,
            txtField16_Branch, txtField08_Branch, txtField09_Branch, txtField10_Branch);

    private Stage getStage() {
        return (Stage) txtField04_Branch.getScene().getWindow();
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
//        oTrans = new BankInformation(oApp, oApp.getBranchCode(), false);
//        oTrans.setWithUI(true);
        //initialize set capslock
        initSetCapsLockField();

        /*Set Focus to set Value to Class*/
        initTxtFieldFocus();

        //initilize text keypressed
        initTxtFieldKeyPressed();

        //Button Click Event
        initButtonClick();

        /*Clear Fields*/
        clearFields();

        initTextFormatter();

        initActionsFieldProperty();

        pnEditMode = EditMode.UNKNOWN;

        initFields(pnEditMode);

        Platform.runLater(() -> {
//            if (oTrans.loadState()) {
//                pnEditMode = oTrans.getEditMode();
//                loadBankField();
//                initFields(pnEditMode);
//            } else {
//                if (oTrans.getMessage().isEmpty()) {
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
//            }
        });
    }

    private void initSetComboBoxtoBankBranch() {
        handleComboBoxSelectionBankMaster(comboBox, 4);
    }

    private void handleComboBoxSelectionBankMaster(ComboBox<String> foComboBox, int fnFieldNumber) {
        if (pnEditMode == EditMode.UPDATE || pnEditMode == EditMode.ADDNEW) {
            foComboBox.setOnAction(e -> {
                int lnSelectedType = foComboBox.getSelectionModel().getSelectedIndex(); // Retrieve the selected type
                if (lnSelectedType >= 0) {
//                    oTrans.setBranch(fnFieldNumber, String.valueOf(lnSelectedType));
                }
                initFields(pnEditMode);
            }
            );
        }
    }

    private void initSetCapsLockField() {
        poTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTxtFieldFocus() {
        poTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
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
                case 18:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 14:
                case 16:
                case 8:
                case 9:
                case 10:
//                    oTrans.setBranch(lnIndex, lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();
        }
    };

    //storing values on bankentrydata
    private void initTxtFieldKeyPressed() {
        poTxtField.forEach(tf -> tf.setOnKeyPressed(this::txtField_KeyPressed));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField loTxtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lnIndex) {
                case 18:
//                    if (oTrans.SearchRecord(loTxtField.getText(), false)) {
//                        loadBankField();
//                        initFields(pnEditMode);
//                    } else {
//                        txtField18_Branch.clear();
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField18_Branch.requestFocus();
//                        return;
//                    }
                    break;
                case 16: // sTownNamexx
//                    if (oTrans.searchTown(loTxtField.getText(), false)) {
//                        loadBankField();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField16_Branch.requestFocus();
//                        return;
//                    }
                    break;
                case 14: // sProvName
//                    if (oTrans.searchProvince(loTxtField.getText(), false)) {
//                        txtField16_Branch.setDisable(false);
//                        txtField08_Branch.setDisable(false);
//                        loadBankField();
//                        txtField16_Branch.clear();
//                        txtField08_Branch.clear();
//                    } else {
//                        txtField14_Branch.clear();
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField14_Branch.requestFocus();
//                        return;
//                    }
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initButtonClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
//                    if (oTrans.NewBranch()) {
//                        clearFields();
//                        loadBankField();
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                    }
                    break;
                case "btnEdit":
//                    if (oTrans.UpdateBranch()) {
//                        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                            if (txtField16_Branch.getText().isEmpty()) {
//                                ShowMessageFX.Warning(getStage(), "Province is empty.", "Warning", null);
//                                break;
//                            }
//                        }
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                    }
                    break;
                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(getStage(), "Are you sure you want to cancel?", pxeModuleName, null) == true) {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnSave":
                    //Validate before saving
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                        if (txtField18_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Bank Name.", "Warning", null);
                            txtField18_Branch.requestFocus();
                            return;
                        }

                        if (txtField02_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid value for Branch Name", "Warning", null);
                            txtField02_Branch.requestFocus();
                            return;
                        }
                        if (txtField06_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Contact Person", "Warning", null);
                            txtField06_Branch.requestFocus();
                            return;
                        }
                        if (txtField06_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.", "Warning", null);
                            txtField06_Branch.requestFocus();
                            return;
                        }
                        if (txtField14_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.", "Warning", null);
                            txtField14_Branch.requestFocus();
                            return;
                        }
                        if (txtField16_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.", "Warning", null);
                            txtField16_Branch.requestFocus();
                            return;
                        }
                        if (txtField09_Branch.getText().length() <= 4) {
                            ShowMessageFX.Warning(getStage(), "Telephone No. cannot be less than 4 digits.", "Warning", null);
                            txtField09_Branch.requestFocus();
                            return;
                        }
                        if (txtField09_Branch.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.", "Warning", null);
                            txtField09_Branch.requestFocus();
                            return;
                        }
//                    if (oTrans.SaveRecord()) {
//                        if (pnEditMode == EditMode.ADDNEW) {
//                            ShowMessageFX.Information(null, pxeModuleName, "New Bank Branch added sucessfully.");
//                        } else {
//                            ShowMessageFX.Information(null, pxeModuleName, "Bank Branch updated sucessfully.");
//                        }
//                        if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
//                            loadBankEntry();
//                            pnEditMode = oTrans.getMasterEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                        return;
//                    }
                    }
                    break;
                case "btnAddBankEntry":
                    loadBankEntry();
                    break;
                case "btnDeactivate":
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to change status?")) {
                        boolean lbStatus = false;
                        if (btnDeactivate.getText().equals("Activate")) {
                            lbStatus = true;
                        } else {
                            lbStatus = false;
                        }
//                        if (oTrans.UpdateBranchStatus(lbStatus)) {
//                            ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                            if (oTrans.OpenBranch(oTrans.getBranch(1).toString())) {
//                                loadBankField();
//                                pnEditMode = oTrans.getEditMode();
//                            }
//                        } else {
//                            ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                            return;
//                        }
                    }
                    break;
                case "btnClose":
                    CommonUtils.closeStage(btnClose);
                    break;
            }
            initFields(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(BankInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        txtField18_Branch.setDisable(!lbShow);

        txtField02_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));
        txtField03_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));
        txtField05_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));
        txtField06_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));
        txtField14_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));
        txtField16_Branch.setDisable(!(lbShow && !txtField16_Branch.getText().isEmpty()));
        txtField08_Branch.setDisable(!(lbShow && !txtField16_Branch.getText().isEmpty()));
        txtField09_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));
        txtField10_Branch.setDisable(!(lbShow && !txtField18_Branch.getText().isEmpty()));

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        //if lbShow = false hide btn
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);

        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }

        if (fnValue == EditMode.READY) {
            //show edit if user clicked save / browse
//            if (oTrans.getBranch(11).toString().equals("1")) {
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

    private void clearBranchFields() {
//        oTrans.setBranch(1, "");
//        oTrans.setBranch(2, "");
//        oTrans.setBranch(3, "");
//        oTrans.setBranch(4, "");
//        oTrans.setBranch(5, "");
//        oTrans.setBranch(6, "");
//        oTrans.setBranch(8, "");
//        oTrans.setBranch(9, "");
//        oTrans.setBranch(10, "");
//        oTrans.setBranch(14, "");
//        oTrans.setBranch(16, "");
//        oTrans.setBranch(17, "");
//        oTrans.setBranch(18, "");
//        oTrans.setBranch(19, "");

    }

    private void clearFields() {
        checkBox12.setSelected(false);
        poTxtField.forEach(tf -> tf.setText(""));
    }

    private void initActionsFieldProperty() {
        txtField14_Branch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                txtField16_Branch.clear();
                txtField16_Branch.setDisable(true);
                txtField08_Branch.clear();
                txtField08_Branch.setDisable(true);
            }
        });
        txtField18_Branch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue.isEmpty()) {
                    disableBranchTrue();
                    clearBranchFields();
                    poTxtFieldDisable.forEach(tf -> tf.setText(""));
                } else {
                    disableBranchFalse();
                }
            }
        });
    }

    private void initTextFormatter() {
        Pattern loNumberOnlyPattern = Pattern.compile("[0-9,+-]*");
        txtField09_Branch.setTextFormatter(new InputTextFormatterUtil(loNumberOnlyPattern)); //sTelNoxxx
        txtField10_Branch.setTextFormatter(new InputTextFormatterUtil(loNumberOnlyPattern)); //sFaxNoxxx
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

    private void disableBranchTrue() {
        poTxtFieldDisable.forEach(disable -> disable.setDisable(true));
    }

    private void disableBranchFalse() {
        poTxtFieldDisable.forEach(disable -> disable.setDisable(false));
    }

    private void loadBankField() {
//        txtField04_Branch.setText((String) oTrans.getBranch("sBankIDxx"));
//        txtField18_Branch.setText((String) oTrans.getBranch("sBankName"));
//        txtField19_Branch.setText((String) oTrans.getBranch("sBankCode"));
//        txtField01_Branch.setText((String) oTrans.getBranch("sBrBankID"));
//        txtField02_Branch.setText((String) oTrans.getBranch("sBrBankNm"));
//        txtField03_Branch.setText((String) oTrans.getBranch("sBrBankCd"));
//        txtField05_Branch.setText((String) oTrans.getBranch("sContactP"));
//        txtField06_Branch.setText((String) oTrans.getBranch("sAddressx"));
//        txtField14_Branch.setText((String) oTrans.getBranch("sProvName"));
//        txtField16_Branch.setText((String) oTrans.getBranch("sTownName"));
//        txtField08_Branch.setText((String) oTrans.getBranch("sZippCode"));
//        txtField09_Branch.setText((String) oTrans.getBranch("sTelNoxxx"));
//        txtField10_Branch.setText((String) oTrans.getBranch("sFaxNoxxx"));
//        if (oTrans.getBranch(11).toString().equals("1")) {
//            checkBox12.setSelected(true);
//        } else {
//            checkBox12.setSelected(false);
//        }
    }
}
