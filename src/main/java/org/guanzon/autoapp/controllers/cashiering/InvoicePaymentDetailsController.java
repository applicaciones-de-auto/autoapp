/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InvoicePaymentDetailsController implements Initializable {

    private GRider oApp;
    private SalesInvoice oTransPayment;
    private String pxeModuleName = "Invoice Payment Details";
    ObservableList<String> cPayerxxx = FXCollections.observableArrayList("CARD", "CHECK", "GIFT CHECK", "ONLINE PAYMENT");
    private List<String> lsPayMode = new ArrayList<>();
    private int pnRow;
    private boolean poIsUpdate = false;
    @FXML
    private Button btnAdd, btnUpdate, btnClose;
    @FXML
    private TextField txtField01_Card, txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card, txtField06_Card;
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
        oTransPayment = foValue;
    }

    public void setPayMode(List<String> fsValue) {
        lsPayMode = fsValue;
    }

    public void setRow(int fnValue) {
        pnRow = fnValue;
    }

    public void setIsUpdate(boolean fbValue) {
        poIsUpdate = fbValue;
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
        initFields();
        if (lsPayMode.size() == 1) {
            for (String paymode : lsPayMode) {
                switch (paymode) {
                    case "CARD":
                        comboBoxPayMde.getSelectionModel().select(0);
                        initFields();
                        break;
                    case "CHECK":
                        comboBoxPayMde.getSelectionModel().select(1);
                        initFields();
                        break;
                    case "GC":
                        comboBoxPayMde.getSelectionModel().select(2);
                        initFields();
                        break;
                    case "OP":
                        comboBoxPayMde.getSelectionModel().select(3);
                        initFields();
                        break;
                }
            }
        }
        comboBoxPayMde.setOnAction(e -> {
            final String[] lsSelectedValue = {""};
            if (comboBoxPayMde.getSelectionModel().getSelectedIndex() >= 0) {
                switch (comboBoxPayMde.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        lsSelectedValue[0] = "CARD";
                        break;
                    case 1:
                        lsSelectedValue[0] = "CHECK";
                        break;
                    case 2:
                        lsSelectedValue[0] = "GC";
                        break;
                    case 3:
                        lsSelectedValue[0] = "OP";
                        break;
                }
                boolean isValid = lsPayMode.stream().anyMatch(payMode -> payMode.equals(lsSelectedValue[0]));
                if (!isValid) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Invalid Payment Details");
                    Platform.runLater(() -> {
                        comboBoxPayMde.getSelectionModel().select(-1);
                        initFields();
                    });
                } else {
                    initFields();
                }
            }
        });
        initButtonsClick();
        initCapitalizationFields();
        initTextFieldFocus();
        initTextKeyPressed();

    }

    private void loadCardFields() {
        txtField01_Card.setText("");
        txtField02_Card.setText("");
        txtField03_Card.setText("");
        txtField04_Card.setText("");
        txtField05_Card.setText("");
        txtField06_Card.setText("");
        textArea07_Card.setText("");
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

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField03_Card, txtField04_Card, txtField05_Card, txtField06_Card,
                txtField04_Check, txtField05_Check, txtField06_Check,
                txtField04_Gift,
                txtField03_Online, txtField04_Online);
        CustomCommonUtil.setCapsLockBehavior(textArea07_Card, textArea07_Check, textArea05_Online, textArea05_Gift);

    }

    private void initTextFieldFocus() {
        List<TextField> loTxtFieldCard = Arrays.asList(txtField03_Card, txtField04_Card, txtField05_Card, txtField06_Card);
        loTxtFieldCard.forEach(tf -> tf.focusedProperty().addListener(txtFieldCard_Focus));

        List<TextField> loTxtFieldCheck = Arrays.asList(txtField04_Check, txtField05_Check, txtField06_Check);
        loTxtFieldCheck.forEach(tf -> tf.focusedProperty().addListener(txtFieldCheck_Focus));

        List<TextField> loTxtFieldGift = Arrays.asList(txtField01_Gift, txtField03_Gift, txtField04_Gift);
        loTxtFieldGift.forEach(tf -> tf.focusedProperty().addListener(txtFieldGift_Focus));

        List<TextField> loTxtFieldOnline = Arrays.asList(txtField02_Online, txtField03_Online, txtField04_Online);
        loTxtFieldOnline.forEach(tf -> tf.focusedProperty().addListener(txtFieldOnline_Focus));

        List<TextArea> loTxtAreaCardCheck = Arrays.asList(textArea07_Card, textArea07_Check);
        loTxtAreaCardCheck.forEach(tf -> tf.focusedProperty().addListener(txtAreaCardCheck_Focus));

        List<TextArea> loTxtAreaGiftOnline = Arrays.asList(textArea05_Online, textArea05_Gift);
        loTxtAreaGiftOnline.forEach(tf -> tf.focusedProperty().addListener(txtAreaGiftOnline_Focus));
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
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
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
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
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
    final ChangeListener<? super Boolean> txtAreaCardCheck_Focus = (o, ov, nv) -> {
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
    final ChangeListener<? super Boolean> txtAreaGiftOnline_Focus = (o, ov, nv) -> {
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
        List<TextField> loTxtField = Arrays.asList(txtField01_Card, txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card, txtField06_Card,
                txtField01_Check, txtField02_Check, txtField04_Check, txtField05_Check, txtField06_Check,
                txtField01_Gift, txtField03_Gift, txtField04_Gift,
                txtField02_Online, txtField03_Online, txtField04_Online);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

        List<TextArea> loTxtArea = Arrays.asList(textArea07_Card, textArea07_Check, textArea05_Online, textArea05_Gift);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField01_Card":
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

    public void textArea_KeyPressed(KeyEvent event) {
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

    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnUpdate, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                CommonUtils.closeStage(btnAdd);
                break;
            case "btnUpdate":
                CommonUtils.closeStage(btnUpdate);
                break;
            case "btnClose":
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
        CustomCommonUtil.setText("", txtField01_Card, txtField02_Card, txtField03_Card, txtField04_Card, txtField05_Card, txtField06_Card);
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
        if (poIsUpdate) {
            btnUpdate.setVisible(true);
            btnUpdate.setManaged(true);
            comboBoxPayMde.setDisable(true);
        } else {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
        }
        if (lsPayMode.size() == 1) {
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
}
