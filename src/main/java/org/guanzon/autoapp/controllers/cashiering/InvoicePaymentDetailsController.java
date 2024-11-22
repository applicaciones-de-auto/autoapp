/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.cashiering.SalesInvoice;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InvoicePaymentDetailsController implements Initializable {

    private GRider oApp;
    private SalesInvoice oTrans;
    private String pxeModuleName = "Invoice Payment Details";
    ObservableList<String> cPayerxxx = FXCollections.observableArrayList("CARD", "CHECK", "GIFT CHECK", "ONLINE PAYMENT");
    private HashSet<String> psPayMode = new HashSet<>();
    private int pnRow;
    private boolean pbIsUpdate = false;
    @FXML
    private Button btnAdd, btnUpdate, btnClose;
    @FXML
    private TextField txtField01_Card, txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card;
    @FXML
    private TextArea textArea07_Card;
    @FXML
    private DatePicker datePicker03_Check;
    @FXML
    private TextField txtField01_Check, txtField02_Check, txtField04_Check, txtField05_Check, txtField06_Check;
    @FXML
    private TextArea textArea07_Check;
    @FXML
    private TextField txtField01_Gift, txtField03_Gift, txtField04_Gift;
    @FXML
    private TextArea textArea05_Gift;
    @FXML
    private CheckBox checkBox02_Gift;
    @FXML
    private TextField txtField02_Online, txtField03_Online, txtField04_Online;
    @FXML
    private TextArea textArea05_Online;
    @FXML
    private ComboBox<String> comboBox01_Online;
    @FXML
    private ComboBox<String> comboBoxPayMde;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private GridPane gridCard, gridCheck, gridGift, gridOnline;
    @FXML
    private Label lblNoFields;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(SalesInvoice foValue) {
        oTrans = foValue;
    }

    public void setPayMode(HashSet<String> fsValue) {
        psPayMode = fsValue;
    }

    public void setRow(int fnValue) {
        pnRow = fnValue;
    }

    public void setIsUpdate(boolean fbValue) {
        pbIsUpdate = fbValue;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxPayMde.setItems(cPayerxxx);
        if (!pbIsUpdate) {
            if (psPayMode.size() == 1) {
                for (String paymode : psPayMode) {
                    switch (paymode) {
                        case "CARD":
                            comboBoxPayMde.getSelectionModel().select(0);
                            oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("CARD");
                            break;
                        case "CHECK":
                            comboBoxPayMde.getSelectionModel().select(1);
                            oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("CHECK");
                            break;
                        case "GC":
                            comboBoxPayMde.getSelectionModel().select(2);
                            oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("GC");
                            break;
                        case "OP":
                            comboBoxPayMde.getSelectionModel().select(3);
                            oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("OP");
                            break;
                    }
                    initFields();
                }
            }
        }
        initCapitalizationFields();
        initPatternFields();
        initLimiterFields();
        initFieldsAction();
        initButtonsClick();
        initTextFieldsProperty();
        initTextFieldFocus();
        initTextKeyPressed();
        loadMasterFields();
        initFields();
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField03_Card, txtField04_Card, txtField05_Card,
                txtField04_Check, txtField05_Check, txtField06_Check,
                txtField04_Gift,
                txtField03_Online, txtField04_Online);
        CustomCommonUtil.setCapsLockBehavior(textArea07_Card, textArea07_Check, textArea05_Online, textArea05_Gift);

    }

    private void initPatternFields() {
        CustomCommonUtil.inputIntegerOnly(txtField02_Card);
    }

    private void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField02_Card, 16);
        CustomCommonUtil.addTextLimiter(txtField03_Card, 10);
        CustomCommonUtil.addTextLimiter(txtField04_Card, 10);
    }

    private void loadMasterFields() {
        if (!pbIsUpdate) {
            if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getPayMode() != null) {
                switch (oTrans.getSIPaymentModel().getDetailModel(pnRow).getPayMode()) {
                    case "CARD":
                        comboBoxPayMde.getSelectionModel().select(0);
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("CARD");
                        break;
                    case "CHECK":
                        comboBoxPayMde.getSelectionModel().select(1);
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("CHECK");
                        break;
                    case "GC":
                        comboBoxPayMde.getSelectionModel().select(2);
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("GC");
                        break;
                    case "OP":
                        comboBoxPayMde.getSelectionModel().select(3);
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("OP");
                        break;
                }
            }
        } else {
            switch (oTrans.getSIPaymentModel().getDetailModel(pnRow).getPayMode()) {
                case "CARD":
                    comboBoxPayMde.getSelectionModel().select(0);
                    break;
                case "CHECK":
                    comboBoxPayMde.getSelectionModel().select(1);
                    break;
                case "GC":
                    comboBoxPayMde.getSelectionModel().select(2);
                    break;
                case "OP":
                    comboBoxPayMde.getSelectionModel().select(3);
                    break;
            }
        }
        switch (comboBoxPayMde.getSelectionModel().getSelectedIndex()) {
            case 0:
                loadCardFields();
                break;
            case 1:
                loadCheckFields();
                break;
            case 2:
                loadGiftFields();
                break;
            case 3:
                loadOnlineFields();
                break;
        }

    }

    private void loadCardFields() {
        if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCBankName() != null) {
            txtField01_Card.setText(oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCBankName());
        }
        if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCCardNo() != null) {
            txtField02_Card.setText(oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCCardNo());
        }
        if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCApprovNo() != null) {
            txtField03_Card.setText(oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCApprovNo());
        }
        if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCTraceNo() != null) {
            txtField04_Card.setText(oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCTraceNo());
        }
        if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getPayAmt() != null) {
            txtField05_Card.setText(CustomCommonUtil.setDecimalFormat(oTrans.getSIPaymentModel().getDetailModel(pnRow).getPayAmt()));
        }
        if (oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCRemarks() != null) {
            textArea07_Card.setText(oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCRemarks());
        }
    }

    private void loadCheckFields() {
        txtField01_Check.setText("");
        txtField02_Check.setText("");
        datePicker03_Check.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        txtField04_Check.setText("");
        txtField05_Check.setText("");
        txtField06_Check.setText("");
        textArea07_Check.setText("");
    }

    private void loadGiftFields() {
        txtField01_Gift.setText("");
        txtField03_Gift.setText("");
        txtField04_Gift.setText("");
        checkBox02_Gift.setSelected(false);
        textArea05_Gift.setText("");
    }

    private void loadOnlineFields() {
        comboBox01_Online.setValue("");
        txtField02_Online.setText("");
        txtField03_Online.setText("");
        txtField04_Online.setText("");
        textArea05_Online.setText("");
    }

    private void initFieldsAction() {
        comboBoxPayMde.setOnAction(e -> {
            final String[] lsSelectedValue = {""};
            if (comboBoxPayMde.getSelectionModel().getSelectedIndex() >= 0) {
                switch (comboBoxPayMde.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        lsSelectedValue[0] = "CARD";
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("CARD");
                        break;
                    case 1:
                        lsSelectedValue[0] = "CHECK";
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("CHECK");
                        break;
                    case 2:
                        lsSelectedValue[0] = "GC";
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("GC");
                        break;
                    case 3:
                        lsSelectedValue[0] = "OP";
                        oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("OP");
                        break;
                }
                boolean isValid = psPayMode.stream().anyMatch(payMode -> payMode.equals(lsSelectedValue[0]));
                if (!isValid) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Invalid Payment Details");
                    oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayMode("");
                    Platform.runLater(() -> {
                        comboBoxPayMde.getSelectionModel().select(-1);
                        initFields();
                    });
                } else {
                    initFields();
                }
            }
        });
    }

    private void initTextFieldsProperty() {
        txtField01_Card.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isEmpty()) {
                oTrans.getSIPaymentModel().getDetailModel(pnRow).setCCBankID("");
                oTrans.getSIPaymentModel().getDetailModel(pnRow).setCCBankName("");
            }
        }
        );
        txtField01_Check.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isEmpty()) {
            }
        }
        );
        txtField03_Gift.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isEmpty()) {
            }
        }
        );
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtFieldCard = Arrays.asList(txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card);
        loTxtFieldCard.forEach(tf -> tf.focusedProperty().addListener(txtFieldCard_Focus));

        List<TextField> loTxtFieldCheck = Arrays.asList(txtField04_Check, txtField05_Check, txtField06_Check);
        loTxtFieldCheck.forEach(tf -> tf.focusedProperty().addListener(txtFieldCheck_Focus));

        List<TextField> loTxtFieldGift = Arrays.asList(txtField01_Gift, txtField03_Gift, txtField04_Gift);
        loTxtFieldGift.forEach(tf -> tf.focusedProperty().addListener(txtFieldGift_Focus));

        List<TextField> loTxtFieldOnline = Arrays.asList(txtField02_Online, txtField03_Online, txtField04_Online);
        loTxtFieldOnline.forEach(tf -> tf.focusedProperty().addListener(txtFieldOnline_Focus));

        List<TextArea> loTxtAreaCardOnline = Arrays.asList(textArea05_Online, textArea07_Card);
        loTxtAreaCardOnline.forEach(tf -> tf.focusedProperty().addListener(txtAreaCardOnline_Focus));

        List<TextArea> loTxtAreaGiftCheck = Arrays.asList(textArea05_Gift, textArea07_Check);
        loTxtAreaGiftCheck.forEach(tf -> tf.focusedProperty().addListener(txtAreaGiftCheck_Focus));
    }
    final ChangeListener<? super Boolean> txtFieldCard_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 2:
                    oTrans.getSIPaymentModel().getDetailModel(pnRow).setCCCardNo(lsValue);
                    break;
                case 3:
                    oTrans.getSIPaymentModel().getDetailModel(pnRow).setCCApprovNo(lsValue);
                    break;
                case 4:
                    oTrans.getSIPaymentModel().getDetailModel(pnRow).setCCTraceNo(lsValue);
                    break;
                case 5:
                    if (lsValue.equals("0.00") || lsValue.equals("0") || lsValue.equals("0.0") || lsValue.isEmpty()) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Invalid Amount");
                        lsValue = "0.00";
                    }
                    oTrans.getSIPaymentModel().getDetailModel(pnRow).setPayAmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 6:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
            }
            loadCardFields();
        }
    };
    final ChangeListener<? super Boolean> txtFieldCheck_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 4:

                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
                case 6:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
            }
            loadCheckFields();
        }
    };
    final ChangeListener<? super Boolean> txtFieldGift_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 1:
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
            }
            loadGiftFields();
        }
    };
    final ChangeListener<? super Boolean> txtFieldOnline_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    break;
            }
            loadOnlineFields();
        }
    };
    final ChangeListener<? super Boolean> txtAreaCardOnline_Focus = (o, ov, nv) -> {
        TextArea loTextArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextArea.getId().substring(8, 10));
        String lsValue = loTextArea.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 5:
                    break;
                case 7:
                    oTrans.getSIPaymentModel().getDetailModel(pnRow).setCCRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };
    final ChangeListener<? super Boolean> txtAreaGiftCheck_Focus = (o, ov, nv) -> {
        TextArea loTextArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextArea.getId().substring(8, 10));
        String lsValue = loTextArea.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 5:
//                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
                case 7:
//                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01_Card, txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card,
                txtField01_Check, txtField02_Check, txtField04_Check, txtField05_Check, txtField06_Check,
                txtField01_Gift, txtField03_Gift, txtField04_Gift,
                txtField02_Online, txtField03_Online, txtField04_Online);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        txtField01_Gift.setOnKeyPressed(this::txtField_Gift_KeyPressed);
        List<TextArea> loTxtArea = Arrays.asList(textArea07_Card, textArea07_Check, textArea05_Online, textArea05_Gift);
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
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField01_Card":
                    loJSON = oTrans.searchPaymentBank(lsValue, pnRow);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField01_Card.setText(oTrans.getSIPaymentModel().getDetailModel(pnRow).getCCBankName());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField01_Card.setText("");
                        return;
                    }
                    break;
                case "txtField02_Card":
                    break;
                case "txtField01_Check":
                    break;
                case "txtField02_Check":
                    break;
                case "txtField01_Gift":
                    break;
                case "txtField03_Gift":
                    break;
                case "txtField02_Online":
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void txtField_Gift_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField01_Gift":
//                    loJSON = oTrans.searchGiftNumber(lsValue, false, pnRow);
//                    if (!"error".equals(loJSON.get("result"))) {
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        txtField01_Gift.setText("");
//                        return;
//                    }
                    break;
                case "txtField03_Gift":
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
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

    private void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnUpdate, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
            case "btnUpdate":
                if (isValidEntry()) {
                    if (lsButton.equals("btnAdd")) {
                        CommonUtils.closeStage(btnAdd);
                    } else {
                        CommonUtils.closeStage(btnUpdate);
                    }
                }
                break;
            case "btnClose":
                if (!pbIsUpdate) {
                    oTrans.removeSIPayment(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private void setGridPaneToTrue(GridPane foGridPane) {
        foGridPane.setVisible(true);
        lblNoFields.setVisible(false);
        lblNoFields.setManaged(false);
    }

    private void clearCardFields() {
        CustomCommonUtil.setText("", txtField01_Card, txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card);
        textArea07_Card.setText("");
    }

    private void clearCheckFields() {
        CustomCommonUtil.setText("", txtField01_Check, txtField02_Check, txtField04_Check, txtField05_Check, txtField06_Check);
        datePicker03_Check.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        textArea07_Check.setText("");
    }

    private void clearGiftFields() {
        CustomCommonUtil.setText("", txtField01_Gift, txtField03_Gift, txtField04_Gift);
        checkBox02_Gift.setSelected(false);
        textArea05_Gift.setText("");
    }

    private void clearOnlineFields() {
        comboBox01_Online.setValue(null);
        CustomCommonUtil.setText("", txtField02_Online, txtField03_Online, txtField04_Online);
        textArea05_Online.setText("");
    }

    private void initFields() {
        CustomCommonUtil.setVisible(false, gridCard, gridCheck, gridGift, gridOnline, btnAdd, btnUpdate);
        CustomCommonUtil.setManaged(false, btnAdd, btnUpdate);
        comboBoxPayMde.setDisable(false);
        if (pbIsUpdate) {
            btnUpdate.setVisible(true);
            btnUpdate.setManaged(true);
            comboBoxPayMde.setDisable(true);
        } else {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
        }
        if (psPayMode.size() == 1) {
            comboBoxPayMde.setDisable(true);
        }
        switch (comboBoxPayMde.getSelectionModel().getSelectedIndex()) {
            case 0:
                clearCheckFields();
                clearGiftFields();
                clearOnlineFields();
                setGridPaneToTrue(gridCard);
                loadCardFields();
                break;
            case 1:
                clearCardFields();
                clearGiftFields();
                clearOnlineFields();
                setGridPaneToTrue(gridCheck);
                loadCheckFields();
                break;
            case 2:
                clearCardFields();
                clearCheckFields();
                clearOnlineFields();
                setGridPaneToTrue(gridGift);
                loadGiftFields();
                break;
            case 3:
                clearCardFields();
                clearGiftFields();
                clearCheckFields();
                setGridPaneToTrue(gridOnline);
                loadOnlineFields();
                break;
            default:
                clearCardFields();
                clearGiftFields();
                clearCheckFields();
                clearOnlineFields();
                lblNoFields.setVisible(true);
                lblNoFields.setManaged(true);
                break;
        }
    }

    private boolean isValidEntry() {
        if (comboBoxPayMde.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select paymode.");
            return false;
        }
        boolean isValid = false;
        switch (comboBoxPayMde.getSelectionModel().getSelectedIndex()) {
            case 0:
                isValid = validateCard();
                break;
            case 1:
                isValid = validateCheck();
                break;
            case 2:
                isValid = validateGift();
                break;
            case 3:
                isValid = validateOnline();
                break;
        }
        return isValid;
    }

    private boolean validateCard() {
        if (txtField01_Card.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter bank name.");
            return false;
        }
        if (txtField02_Card.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter bank card no.");
            return false;
        }
        if (txtField02_Card.getText().length() < 16) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid card no.");
            return false;
        }
        if (txtField03_Card.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter approval code.");
            return false;
        }
        if (txtField04_Card.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter trace no.");
            return false;
        }
        if (txtField05_Card.getText().equals("0.00") || txtField05_Card.getText().equals("0.0")) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid amount.");
            return false;
        }
        return true;
    }

    private boolean validateCheck() {
        if (txtField01_Check.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter bank name.");
            return false;
        }
        if (txtField02_Check.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter check no.");
            return false;
        }
        if (datePicker03_Check.getValue().equals("1900-01-01")) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select valid check date.");
            return false;
        }
        if (txtField05_Check.getText().equals("0.00") || txtField05_Check.getText().equals("0.0")) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid amount.");
            return false;
        }
        return true;
    }

    private boolean validateGift() {
        if (txtField01_Gift.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter gift check number.");
            return false;
        }
        if (checkBox02_Gift.isSelected()) {
            if (txtField03_Gift.getText().trim().isEmpty()) {
                ShowMessageFX.Warning(null, pxeModuleName, "Please enter subsidized to.");
                return false;
            }
        }
        if (txtField04_Gift.getText().equals("0.00") || txtField04_Gift.getText().equals("0.0")) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid amount.");
            return false;
        }
        return true;
    }

    private boolean validateOnline() {
        if (comboBox01_Online.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select payment source.");
            return false;
        }
        if (txtField02_Online.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter referrence no.");
            return false;
        }
        if (txtField03_Online.getText().equals("0.00") || txtField03_Online.getText().equals("0.0")) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid amount.");
            return false;
        }
        return true;
    }
}
