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
public class InsuranceApplicationController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private String pxeModuleName = "Insurance Application";
    private int pnEditMode = -1;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TextField txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
            txtField19, txtField20, txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
            txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
            txtField39, txtField40, txtField41, txtField42, txtField43, txtField45;
    @FXML
    private DatePicker datePicker05, datePicker06, datePicker11;
    @FXML
    private TextArea textArea14, textArea44, textArea46;

    @FXML
    private ComboBox<String> comboBox03, comboBox04, comboBox09, comboBox12, comboBox18, comboBox21, comboBox22, comboBox28;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnBrowse, btnInsAppCancel, btnCancel, btnPrint, btnClose, btnPayment;

    @FXML
    private Label lblPolicyNo, lblPrintDate, lblStatus;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
                txtField19, txtField20);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTextArea = Arrays.asList(textArea14, textArea44, textArea46);
        loTextArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList();
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea44);
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
                case 43:
                    break;

            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
                txtField19, txtField20, txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        List<TextArea> loTxtArea = Arrays.asList(textArea44);
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
//                    loJSON = oTransInsProposal.searchInquiry(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField10":
//                    loJSON = oTransInsProposal.searchClient(lsValue, true);
                    if (!"error".equals(loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnInsAppCancel, btnClose);
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
            case "btnIPCancel":
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox09.setValue(null);
        comboBox12.setItems(null);
        comboBox18.setItems(null);
        comboBox21.setItems(null);
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
    public void clearFields() {
        List<TextField> loTxtField = Arrays.asList(
                txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42);
        loTxtField.forEach(tf -> tf.setText("0.00"));
        List<TextField> loStringTxtField = Arrays.asList(
                txtField01, txtField02, txtField07, txtField08, txtField10, txtField13, txtField15, txtField16, txtField17,
                txtField19, txtField20, txtField23, txtField24, txtField25, txtField26, txtField27, txtField29,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField37, txtField38,
                txtField39, txtField40, txtField41, txtField42, txtField43, txtField45);
        loStringTxtField.forEach(tf -> tf.setText(""));
        List<TextArea> loTextArea = Arrays.asList(
                textArea14, textArea44, textArea46);
        loTextArea.forEach(ta -> ta.setText(""));
        List<DatePicker> loDatePicker = Arrays.asList(
                datePicker05, datePicker06, datePicker11);
        loDatePicker.forEach(dp -> dp.setValue(LocalDate.of(1900, Month.JANUARY, 1)));
        List<ComboBox> loComboBox = Arrays.asList(
                comboBox03, comboBox04, comboBox09, comboBox12, comboBox18, comboBox21, comboBox22, comboBox28);
        loComboBox.forEach(cb -> cb.setValue(null));
        List<Label> loLabel = Arrays.asList(
                lblPolicyNo, lblPrintDate, lblStatus);
        loLabel.forEach(lb -> lb.setText(""));
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
        btnInsAppCancel.setVisible(false);
        btnInsAppCancel.setManaged(false);

        if (fnValue == EditMode.READY) {
            if (!lblStatus.getText().equals("Cancelled")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                btnInsAppCancel.setVisible(true);
                btnInsAppCancel.setManaged(true);
            }
        }
    }

    @Override
    public void loadMasterFields() {

    }

}
