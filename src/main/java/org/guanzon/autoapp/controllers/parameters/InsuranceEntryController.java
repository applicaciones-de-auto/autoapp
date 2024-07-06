/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parameters;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InsuranceEntryController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Insurance Company Entry";
    private int pnEditMode;//Modifying fields
//    private InsuranceInformation oTrans;
    private GRider oApp;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnBrowse, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private CheckBox cboxActivate;
    private final List<TextField> poTxtField = Arrays.asList(txtField01, txtField02, txtField03);

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
        // TODO        oTrans = new InsuranceInformation(oApp, oApp.getBranchCode(), true);
//        oTrans.setWithUI(true);
        CommonUtils.addTextLimiter(txtField03, 10);

        initCapitalizationFields();

        initTextFieldFocus();

        initButtons();

        pnEditMode = EditMode.UNKNOWN;

        initFields(pnEditMode);
    }

    private void initCapitalizationFields() {
        poTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextFieldFocus() {
        poTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
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
                case 2:
                case 3:
//                    oTrans.setMaster(lnIndex, lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();

        }
    };

    private void loadInsuranceField() {
//        txtField01.setText((String) oTrans.getMaster(1));
//        txtField02.setText((String) oTrans.getMaster(2));
//        txtField03.setText((String) oTrans.getMaster(3));
//        if (oTrans.getMaster(4).toString().equals("1")) {
//            cboxActivate.setSelected(true);
//        } else {
//            cboxActivate.setSelected(false);
//        }

    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnEdit, btnCancel, btnDeactivate, btnBrowse, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
//                if (oTrans.NewRecord()) {
//                    clearFields();
//                    loadInsuranceField();
//                    pnEditMode = oTrans.getMasterEditMode();
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
                break;
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Insurance Company Name", "Warning", null);
                        txtField02.requestFocus();
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(getStage(), "Please enter a value for Insurance Company Code", "Warning", null);
                        txtField03.requestFocus();
                        return;
                    }
//                    if (oTrans.SaveRecord()) {
//                        if (pnEditMode == EditMode.ADDNEW) {
//                            ShowMessageFX.Information(null, pxeModuleName, "New Insurance Company added sucessfully.");
//                        } else {
//                            ShowMessageFX.Information(null, pxeModuleName, "Insurance Company updated sucessfully.");
//                        }
//                        if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
//                            loadInsuranceField();
//                            pnEditMode = oTrans.getMasterEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                        return;
//                    }
                }
                break;
            case "btnEdit":
//                if (oTrans.UpdateRecord()) {
//                    pnEditMode = oTrans.getMasterEditMode();
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
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    boolean lbStatus = false;
                    if (btnDeactivate.getText().equals("Activate")) {
                        lbStatus = true;
                    } else {
                        lbStatus = false;
                    }
//                    if (oTrans.UpdateInsuranceStatus(lbStatus)) {
//                        ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                        if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
//                            loadInsuranceField();
//                            pnEditMode = oTrans.getMasterEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                        return;
//                    }
                }
                break;
            case "btnSearch":
//                if (oTrans.SearchRecord("", true)) {
//                    loadInsuranceField();
//                    pnEditMode = EditMode.READY;
//                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initFields(pnEditMode);
    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        cboxActivate.setSelected(false);
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
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
//            if (oTrans.getMaster(4).toString().equals("1")) {
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
