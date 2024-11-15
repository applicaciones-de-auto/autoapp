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
        initButtonsClick();
        if (lsPayMode.size() == 1) {
            for (String paymode : lsPayMode) {
                switch (paymode) {
                    case "Card":
                        comboBoxPayMde.getSelectionModel().select(0);
                        initFields();
                        break;
                    case "Check":
                        comboBoxPayMde.getSelectionModel().select(1);
                        initFields();
                        break;
                    case "Gift":
                        comboBoxPayMde.getSelectionModel().select(2);
                        initFields();
                        break;
                    case "Online":
                        comboBoxPayMde.getSelectionModel().select(3);
                        initFields();
                        break;
                }
            }
        }
        comboBoxPayMde.setOnAction(e -> {
            if (comboBoxPayMde.getSelectionModel().getSelectedIndex() >= 0) {
                initFields();
            }
        });
    }

    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnUpdate, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                break;
            case "btnUpdate":
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
        lblNoFields.setVisible(true);
        lblNoFields.setManaged(true);
        CustomCommonUtil.setVisible(false, gridCard, gridCheck, gridGift, gridOnline);
        switch (comboBoxPayMde.getSelectionModel().getSelectedIndex()) {
            case 0:
                clearCheckFields();
                clearGiftFields();
                clearOnlineFields();
                setGridPaneToTrue(gridCard);
                break;
            case 1:
                clearCardFields();
                clearGiftFields();
                clearOnlineFields();
                setGridPaneToTrue(gridCheck);
                break;
            case 2:
                clearCardFields();
                clearCheckFields();
                clearOnlineFields();
                setGridPaneToTrue(gridGift);
                break;
            case 3:
                clearCardFields();
                clearGiftFields();
                clearCheckFields();
                setGridPaneToTrue(gridOnline);
                break;
        }
    }
}
