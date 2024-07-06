/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parameters;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
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
public class CategoryEntryParamController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Category Entry Form";
    private int pnEditMode;//Modifying fields
//    private PartsCategory oTrans;
    @FXML
    private Button btnAdd, btnSave, btnEdit, btnCancel, btnBrowse, btnDeactivate, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField07;
    @FXML
    private CheckBox cboxActivate;
    private final List<TextField> poTxtField = Arrays.asList(txtField01, txtField02, txtField07);

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        oTrans = new PartsCategory(oApp, oApp.getBranchCode(), true);
//        oTrans.setWithUI(true);
        initCapitalizationFields();

        initTextFieldFocus();
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
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
                case 7:
//                    oTrans.setMaster(lnIndex, lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();

        }
    };

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
//                    loadCategoryField();
//                    pnEditMode = oTrans.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                }
                break;
            case "btnSave":
//                 if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
//                if (txtField02.getText().trim().equals("")) {
//                    ShowMessageFX.Warning(getStage(), "Please enter a value for category description", "Warning", null);
//                    txtField02.requestFocus();
//                    return;
//                }
//                if (txtField07.getText().trim().equals("")) {
//                    ShowMessageFX.Warning(getStage(), "Please enter a value for inventory type", "Warning", null);
//                    txtField07.requestFocus();
//                    return;
//                }
//                if (oTrans.SaveRecord()) {
//                    if (pnEditMode == EditMode.ADDNEW) {
//                        ShowMessageFX.Information(null, pxeModuleName, "New Category added sucessfully.");
//                    } else {
//                        ShowMessageFX.Information(null, pxeModuleName, "Category updated sucessfully.");
//                    }
//                    if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
//                        loadCategoryField();
//                        pnEditMode = oTrans.getEditMode();
//                    }
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
//                    return;
//                }
//                }
                break;
            case "btnEdit":
//                if (oTrans.UpdateRecord()) {
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
            case "btnDeactivate":
//                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
//                    String lsValue = oTrans.getMaster(1).toString();
//                    boolean lbStatus = false;
//                    if (btnDeactivate.getText().equals("Activate")) {
//                        lbStatus = true;
//                    } else {
//                        lbStatus = false;
//                    }
//                    if (oTrans.UpdateRecordStatus(lsValue, lbStatus)) {
//                        ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                        if (oTrans.OpenRecord(oTrans.getMaster(1).toString())) {
//                            loadCategoryField();
//                            pnEditMode = oTrans.getEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Information(getStage(), oTrans.getMessage(), pxeModuleName, null);
//                        return;
//                    }
//                }
                break;

            case "btnBrowse":
//                    if (oTrans.searchRecord()) {
//                        loadCategoryField();
//                        pnEditMode = EditMode.READY;
//                    }
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

    private void txtField_KeyPressed(KeyEvent event) {
        TextField loTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        switch (event.getCode()) {
            case F3:
            case TAB:
            case ENTER:
                switch (txtFieldID) {
                    case "txtField07":
//                        if (oTrans.searchInvType(txtField07.getText())) {
//                            loadCategoryField();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            txtField07.clear();
//                        }
//                        break;
                    default:
                        break;

                }
                break;
        }
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(loTxtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(loTxtField);
        }

    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(true);
        txtField02.setDisable(!lbShow);
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

    private void loadCategoryField() {
////         txtField01.setText((String) oTrans.getMaster(1));
//            txtField02.setText((String) oTrans.getMaster(2));
//            txtField07.setText((String) oTrans.getMaster(7));
//            if (oTrans.getMaster(4).toString().equals("1")) {
//                cboxActivate.setSelected(true);
//            } else {
//                cboxActivate.setSelected(false);
//            }
    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        cboxActivate.setSelected(false);
    }
}
