/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.insurance;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InsurancePolicyController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private final String pxeModuleName = "Insurance Policy";
    private int pnEditMode = -1;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCose, btnPrintCOC, btnInsPolCancel, btnPayHistory, btnCustomer, btnInsComp;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
            txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField30, txtField31,
            xtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38, txtField39, txtField40, txtField41,
            txtField42, txtField43, txtField44, txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51,
            txtField52, txtField53, txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60;
    @FXML
    private DatePicker datePicker04, datePicker05, datePicker12, datePicker13;
    @FXML
    private ComboBox<String> comboBox06, comboBox09, comboBox14, comboBox25, comboBox28, comboBox29;
    @FXML
    private TextArea textArea16, textArea18, textArea20;
    @FXML
    private Label lblInsPolicyStatus;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCapitalizationFields();
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41,
                txtField42
        );
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        CustomCommonUtil.setCapsLockBehavior(textArea16);
        CustomCommonUtil.setCapsLockBehavior(textArea18);
        CustomCommonUtil.setCapsLockBehavior(textArea20);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41,
                txtField42);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea16);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
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
                case 22:
                    break;
                case 23:
                    break;
                case 24:
                    break;
                case 25:
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    break;
                case 29:
                    break;
                case 30:
                    break;
                case 31:
                    break;
                case 32:
                    break;
                case 33:
                    break;
                case 34:
                    break;
                case 35:
                    break;
                case 36:
                    break;
                case 37:
                    break;
            }
        } else {
            txtField.selectAll();

        }
    };
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        TextArea loTextArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextArea.getId().substring(8, 10));
        String lsValue = loTextArea.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 16:
                    break;

            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41,
                txtField42);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        List<TextArea> loTxtArea = Arrays.asList(textArea16);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField01":
//                    loJSON = oTransInsProposal.searchApplicationNo(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
            initFields(pnEditMode);
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        }
    }

    private void textArea_KeyPressed(KeyEvent event) {
        String textAreaID = ((TextArea) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (textAreaID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCose, btnPrintCOC, btnInsPolCancel,
                btnPayHistory, btnCustomer, btnInsComp);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                break;
            case "btnEdit":
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Insurance Application Information Saving....", "Are you sure, do you want to save?")) {
                } else {
                    return;
                }
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Insurance Application Information", (String) loJSON.get("message"));
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
//                    oTransInsApplication = new Client(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Insurance Application Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
//                loJSON = oTransInsApplication.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
//                    pnEditMode = oTransInsApplication.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Insurance Application Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Insurance Application");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnPrint":
                break;
            case "btnPrintCOC":
                break;
            case "btnInsPolCancel":
                break;
            case "btnPayHistory":
                break;
            case "btnCustomer":
                break;
            case "btnInsComp":
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox06.setItems(null);
        comboBox09.setItems(null);
        comboBox14.setItems(null);
        comboBox25.setItems(null);
        comboBox28.setItems(null);
        comboBox29.setItems(null);

    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void initTextFieldsProperty() {

    }

    @Override
    public void clearTables() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void clearFields() {
        List<TextField> loNumberTxtField = Arrays.asList(txtField30, txtField31,
                xtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38, txtField39, txtField40,
                txtField43, txtField44, txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51,
                txtField52, txtField53, txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60);
        loNumberTxtField.forEach(tf -> tf.setText("0.00"));

        List<TextField> loStringTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField07, txtField08, txtField10, txtField11, txtField19,
                txtField21, txtField22, txtField23, txtField24, txtField15, txtField17, txtField26, txtField27, txtField41, txtField42);
        loStringTxtField.forEach(tf -> tf.setText(""));

        List<TextArea> loTextAtea = Arrays.asList(textArea16, textArea18, textArea20);
        loTextAtea.forEach(tA -> tA.setText(""));

        List<ComboBox> loComboBox = Arrays.asList(comboBox06, comboBox09, comboBox14, comboBox25, comboBox28, comboBox29);
        loComboBox.forEach(cb -> cb.setValue(null));

        List<DatePicker> loDatePicker = Arrays.asList(datePicker04, datePicker05, datePicker12, datePicker13);
        loDatePicker.forEach(dp -> dp.setValue(LocalDate.of(1900, Month.JANUARY, 1)));
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnPrint.setDisable(true);
        btnInsPolCancel.setVisible(false);
        btnInsPolCancel.setManaged(false);

        if (fnValue == EditMode.READY) {
            if (!lblInsPolicyStatus.getText().equals("Cancelled")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                btnInsPolCancel.setVisible(true);
                btnInsPolCancel.setManaged(true);
            }
        }
    }

    @Override
    public boolean loadMasterFields() {

        return false;

    }

}
