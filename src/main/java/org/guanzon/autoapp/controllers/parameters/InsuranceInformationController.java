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
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InsuranceInformationController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private InsuranceInformation oTrans;
    private final String pxeModuleName = "Insurance"; //Form Title
    private int pnEditMode;//Modifying fields
    private String psOldTransNo = "";
    private String psTransNo = "";
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cInsurType = FXCollections.observableArrayList("NON IN HOUSE", "IN HOUSE (DIRECT)", "SUB-IN HOUSE (ACCREDITED)");
    @FXML
    private GridPane branchFields;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnDeactivate, btnBrowse, btnClose, btnAddInsuranceCompany;
    @FXML
    private TextField txtField05_Branch, txtField15_Branch, txtField20_Branch, txtField01_Branch, txtField02_Branch,
            txtField03_Branch, txtField06_Branch, txtField07_Branch, txtField16_Branch, txtField18_Branch,
            txtField09_Branch, txtField10_Branch, txtField11_Branch;
    @FXML
    private ComboBox<String> comboBox04;
    @FXML
    private CheckBox checkBox12;
    List<TextField> poTxtField = Arrays.asList(txtField05_Branch, txtField15_Branch, txtField20_Branch, txtField01_Branch, txtField02_Branch,
            txtField03_Branch, txtField06_Branch, txtField07_Branch, txtField16_Branch, txtField18_Branch,
            txtField09_Branch, txtField10_Branch, txtField11_Branch);
    List<TextField> poTxtFieldDisable = Arrays.asList(txtField05_Branch, txtField15_Branch, txtField20_Branch, txtField01_Branch, txtField02_Branch,
            txtField03_Branch, txtField06_Branch, txtField07_Branch, txtField16_Branch, txtField18_Branch,
            txtField09_Branch, txtField10_Branch, txtField11_Branch);

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
        // TODO
//        oTrans = new InsuranceInformation(oApp, oApp.getBranchCode(), false);
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

        initButtonClick();

        initSetComboBoxtoInsuranceBranch();

        /*Clear Fields*/
        clearFields();

        comboBox04.setItems(cInsurType);

        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

        Platform.runLater(() -> {
//            if (oTrans.loadState()) {
//                pnEditMode = oTrans.getEditMode();
//                loadInsuranceField();
//                initFields(pnEditMode);
//            } else {
//                if (oTrans.getMessage().isEmpty()) {
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
//            }
        });
    }

    private void disableBranchTrue() {
        poTxtFieldDisable.forEach(disable -> disable.setDisable(true));
    }

    private void disableBranchFalse() {
        poTxtFieldDisable.forEach(disable -> disable.setDisable(false));
    }

    private void initTextFormatter() {
        Pattern loNumberOnlyPattern = Pattern.compile("[0-9,+-]*");
        txtField10_Branch.setTextFormatter(new InputTextFormatterUtil(loNumberOnlyPattern)); //sTelNoxxx
        txtField11_Branch.setTextFormatter(new InputTextFormatterUtil(loNumberOnlyPattern)); //sFaxNoxxx
    }

    private void initActionsFieldProperty() {
        txtField16_Branch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                txtField18_Branch.clear();
                txtField18_Branch.setDisable(true);
                txtField09_Branch.clear();
                txtField09_Branch.setDisable(true);
            }
        });
        txtField15_Branch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue.isEmpty()) {
                    disableBranchTrue();
                    clearBranchFields();
                    comboBox04.setValue(null);
                    poTxtField.forEach(tf -> tf.setText(""));
                } else {
                    disableBranchFalse();
                }
            }
        });
    }

    private void initSetCapsLockField() {
        poTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initSetComboBoxtoInsuranceBranch() {
        handleComboBoxSelectionInsuranceMaster(comboBox04, 4);
    }

    private void handleComboBoxSelectionInsuranceMaster(ComboBox<String> foComboBox, int fnFieldNumber) {
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
    }    //storing values on bankentrydata

    private void initTxtFieldKeyPressed() {
        poTxtField.forEach(tf -> tf.setOnKeyPressed(this::txtField_KeyPressed));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField loTxtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lnIndex) {
                case 15:
//                    if (oTrans.SearchRecord(loTxtField.getText(), false)) {
//                        loadInsuranceField();
//                        initFields(pnEditMode);
//                    } else {
//                        txtField15_Branch.clear();
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField15_Branch.requestFocus();
//                        return;
//                    }
                    break;
                case 18: // sTownNamexx
//                    if (oTrans.searchTown(loTxtField.getText(), false)) {
//                        loadInsuranceField();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField18_Branch.requestFocus();
//                        return;
//                    }
                    break;
                case 16: // sProvName
//                    if (oTrans.searchProvince(loTxtField.getText(), false)) {
//                        txtField18_Branch.setDisable(false);
//                        txtField09_Branch.setDisable(false);
//                        loadInsuranceField();
//                        txtField18_Branch.clear();
//                        txtField09_Branch.clear();
//                    } else {
//                        txtField16_Branch.clear();
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        txtField16_Branch.requestFocus();
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
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 9:
                case 10:
                case 11:
                case 16:
                case 15:
                case 18:
                case 20:
//                    oTrans.setBranch(lnIndex, lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();
        }
    };

    private void initButtonClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnDeactivate);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
//                if (oTrans.NewBranch()) {
//                    clearFields();
//                    loadInsuranceField();
//                    pnEditMode = oTrans.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
                break;
            case "btnEdit":
//                if (oTrans.UpdateBranch()) {
//                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                        if (txtField16_Branch.getText().isEmpty()) {
//                            ShowMessageFX.Warning(getStage(), "Province is empty.", "Warning", null);
//                            break;
//                        }
//                    }
//                    pnEditMode = oTrans.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
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
                    if (txtField15_Branch.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Insurance Name.", "Warning", null);
                        txtField15_Branch.requestFocus();
                        return;
                    }
                    if (comboBox04.getSelectionModel().isEmpty()) {
                        ShowMessageFX.Warning(getStage(), "Please choose a value for Company Type", "Warning", null);
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
                    if (txtField07_Branch.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for HouseNo/Street/Barangay.", "Warning", null);
                        txtField07_Branch.requestFocus();
                        return;
                    }
                    if (txtField16_Branch.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a valid value for Province.", "Warning", null);
                        txtField16_Branch.requestFocus();
                        return;
                    }
                    if (txtField18_Branch.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Municipality.", "Warning", null);
                        txtField18_Branch.requestFocus();
                        return;
                    }
                    if (txtField10_Branch.getText().length() <= 4) {
                        ShowMessageFX.Warning(getStage(), "Telephone No. cannot be less than 4 digits.", "Warning", null);
                        txtField10_Branch.requestFocus();
                        return;
                    }
                    if (txtField10_Branch.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Telephone No.", "Warning", null);
                        txtField10_Branch.requestFocus();
                        return;
                    }
                    //Proceed Saving
//                    if (oTrans.SaveBranch()) {
//                        ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
//                        getSelectedItem((String) oTrans.getBranch(1));
//                        pnEditMode = oTrans.getEditMode();
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving Insurance Company Information");
//                    }
                }
                break;
            case "btnAddInsuranceCompany":
                loadInsuranceEntry();
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    boolean lbStatus = false;
                    if (btnDeactivate.getText().equals("Activate")) {
                        lbStatus = true;
                    } else {
                        lbStatus = false;
                    }
//                    if (oTrans.UpdateInsBranchStatus(lbStatus)) {
//                        ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                        if (oTrans.OpenBranch(oTrans.getBranch(1).toString())) {
//                            loadInsuranceField();
//                            pnEditMode = oTrans.getEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                        return;
//                    }
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
        }
        initFields(pnEditMode);
    }

    private void loadInsuranceEntry() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("InsuranceEntry.fxml"));

            InsuranceEntryController loControl = new InsuranceEntryController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();
            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
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

    private void clearBranchFields() {
//        oTrans.setBranch(1, "");
//        oTrans.setBranch(2, "");
//        oTrans.setBranch(3, "");
//        oTrans.setBranch(4, "");
//        oTrans.setBranch(5, "");
//        oTrans.setBranch(6, "");
//        oTrans.setBranch(7, "");
//        oTrans.setBranch(8, "");
//        oTrans.setBranch(9, "");
//        oTrans.setBranch(10, "");
//        oTrans.setBranch(11, "");
//        oTrans.setBranch(13, "");
//        oTrans.setBranch(15, "");
//        oTrans.setBranch(16, "");
//        oTrans.setBranch(17, "");
//        oTrans.setBranch(18, "");
//        oTrans.setBranch(19, "");
//        oTrans.setBranch(20, "");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        txtField15_Branch.setDisable(!lbShow);
        comboBox04.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField06_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField02_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField03_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField10_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField11_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField07_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));
        txtField16_Branch.setDisable(!(lbShow && !txtField15_Branch.getText().isEmpty()));

        txtField18_Branch.setDisable(!(lbShow && !txtField16_Branch.getText().isEmpty()));
        txtField09_Branch.setDisable(!(lbShow && !txtField16_Branch.getText().isEmpty()));
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
//            if (oTrans.getBranch(12).toString().equals("1")) {
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

    private void clearFields() {
        checkBox12.setSelected(false);
        poTxtField.forEach(tf -> tf.setText(""));
    }

    private void loadInsuranceField() {
//        String lsSelectedItem04 = oTrans.getBranch("sCompnyTp").toString();
//        switch (lsSelectedItem04) {
//            case "0":
//                lsSelectedItem04 = "NON IN HOUSE";
//                break;
//            case "1":
//                lsSelectedItem04 = "IN HOUSE (DIRECT)";
//                break;
//            case "2":
//                lsSelectedItem04 = "SUB-IN HOUSE (ACCREDITED)";
//                break;
//
//        }
//        comboBox04.setValue(lsSelectedItem04);
//        txtField05_Branch.setText((String) oTrans.getBranch("sInsurIDx"));
//        txtField15_Branch.setText((String) oTrans.getBranch("sInsurNme"));
//        txtField20_Branch.setText((String) oTrans.getBranch("sInsurCde"));
//        txtField01_Branch.setText((String) oTrans.getBranch("sBrInsIDx"));
//        txtField02_Branch.setText((String) oTrans.getBranch("sBrInsNme"));
//        txtField03_Branch.setText((String) oTrans.getBranch("sBrInsCde"));
//        txtField06_Branch.setText((String) oTrans.getBranch("sContactP"));
//        txtField09_Branch.setText((String) oTrans.getBranch("sZippCode"));
//        txtField10_Branch.setText((String) oTrans.getBranch("sTelNoxxx"));
//        txtField11_Branch.setText((String) oTrans.getBranch("sFaxNoxxx"));
//        txtField07_Branch.setText((String) oTrans.getBranch("sAddressx"));
//        txtField16_Branch.setText((String) oTrans.getBranch("sProvName"));
//        txtField18_Branch.setText((String) oTrans.getBranch("sTownName"));
//        if (oTrans.getBranch(12).toString().equals("1")) {
//            checkBox12.setSelected(true);
//        } else {
//            checkBox12.setSelected(false);
//        }
    }

}
