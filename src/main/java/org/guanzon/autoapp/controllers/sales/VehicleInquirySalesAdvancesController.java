/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VehicleInquirySalesAdvancesController implements Initializable {

    private GRider oApp;
    private final String pxeModuleName = "Inquiry Vehicle Sales Advances";
    ObservableList<String> cSlipType = FXCollections.observableArrayList("RESERVATION", "DEPOSIT", "SAFEGUARD DUTY");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private int pnRow = 0;
    private Inquiry oTransAS;
    private int pnIinqStat;
    private int pnIEditMode;
    private boolean pbState = false;
    @FXML
    private Button btnApply, btnClose;
    @FXML
    private ComboBox<String> comboBox01;
    @FXML
    private TextField txtField02, txtField03, txtField04, txtField06, txtField07, txtField08;
    @FXML
    private TextArea textArea05;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setVSAObject(Inquiry foValue) {
        oTransAS = foValue;
    }

    public void setTableRows(int row) {
        pnRow = row;
    }

    public void setState(boolean flValue) {
        pbState = flValue;
    }

    public void setInqStat(Integer fnValue) {
        pnIinqStat = fnValue;
    }

    public void setEditMode(Integer fnValue) {
        pnIEditMode = fnValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBox01.setItems(cSlipType); //Slipt Type
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new InputTextFormatterUtil(pattern));
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        textArea05.setOnKeyPressed(this::txtArea_KeyPressed);
        txtField04.focusedProperty().addListener(txtField_Focus);
        btnClose.setOnAction(this::handleButtonAction);
        btnApply.setOnAction(this::handleButtonAction);
        initCapitalizationFields();
        loadInquiryReservation();
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField06, txtField07);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        InputTextUtil.setCapsLockBehavior(textArea05);
    }

    /*TRIGGER FOCUS*/
    private void txtField_KeyPressed(KeyEvent event) {
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                case UP:
                    event.consume();
                    CommonUtils.SetPreviousFocus((TextField) event.getSource());
                    break;
                case DOWN:
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                default:
                    break;
            }
        }
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
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransAS.setReservation(pnRow, 5, Double.valueOf(txtField04.getText().replace(",", "")));
                    txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransAS.getReservation(pnRow, "nAmountxx")))));
                    break;
            }
        } else {
            txtField.selectAll();

        }
    };

    /*TRIGGER FOCUS*/
    private void txtArea_KeyPressed(KeyEvent event) {
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                    event.consume();
                    CommonUtils.SetNextFocus((TextArea) event.getSource());
                    break;
                case UP:
                    event.consume();
                    CommonUtils.SetPreviousFocus((TextArea) event.getSource());
                    break;
                case DOWN:
                    event.consume();
                    CommonUtils.SetNextFocus((TextArea) event.getSource());
                    break;
                default:
                    break;
            }
        }
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                if (pbState) {
                    oTransAS.removeReservation(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            case "btnApply":
                if (setSelection()) {
                    if (setToClass()) {
                        CommonUtils.closeStage(btnClose);
                    }
                } else {
                    return;
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private boolean setToClass() {
        if (txtField04.getText().equals("0.00") || txtField04.getText().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value amount.");
            return false;
        }
        switch (txtField06.getText()) {
            case "CANCELLED":
                oTransAS.setReservation(pnRow, 12, "0");
                break;
            case "FOR APPROVAL":
                oTransAS.setReservation(pnRow, 12, "1");
                break;
            case "APPROVED":
                oTransAS.setReservation(pnRow, 12, "2");
                break;
        }

        oTransAS.setReservation(pnRow, 2, SQLUtil.toDate(txtField03.getText(), SQLUtil.FORMAT_SHORT_DATE));
        oTransAS.setReservation(pnRow, 6, textArea05.getText());
        oTransAS.setReservation(pnRow, 11, comboBox01.getSelectionModel().getSelectedIndex());
        return true;
    }

    private void loadInquiryReservation() {
        comboBox01.getSelectionModel().select(Integer.parseInt(oTransAS.getReservation(pnRow, 11).toString())); //VSA Type
        txtField02.setText(oTransAS.getReservation(pnRow, 3).toString());
        if (pbState) { //Add
            txtField03.setText(InputTextUtil.xsDateShort((Date) oApp.getServerDate()));
            txtField06.setText("FOR APPROVAL");
        } else {
            txtField03.setText(InputTextUtil.xsDateShort((Date) oTransAS.getReservation(pnRow, 2)));
            txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransAS.getReservation(pnRow, "nAmountxx")))));
            switch (oTransAS.getReservation(pnRow, 12).toString()) {
                case "0":
                    txtField06.setText("CANCELLED");
                    txtField04.setDisable(!pbState);
                    comboBox01.setDisable(!pbState);
                    textArea05.setDisable(!pbState);
                    btnApply.setDisable(!pbState);
                    break;
                case "1":
                    txtField06.setText("FOR APPROVAL");
                    switch (pnIinqStat) {
                        case 0: //For Follow up
                            txtField04.setDisable(pbState);
                            comboBox01.setDisable(pbState);
                            textArea05.setDisable(pbState);
                            btnApply.setDisable(pbState);
                            break;
                        case 1: //On Process
                        case 3: //VSP
                            if ((pnIEditMode == EditMode.UPDATE)) {
                                txtField04.setDisable(pbState);
                                comboBox01.setDisable(pbState);
                                textArea05.setDisable(pbState);
                                btnApply.setDisable(pbState);
                            } else {
                                txtField04.setDisable(!pbState);
                                comboBox01.setDisable(!pbState);
                                textArea05.setDisable(!pbState);
                                btnApply.setDisable(!pbState);
                            }
                            break;
                        case 2: //Lost Sale
                        case 4: //Sold
                        case 5: //Retired
                        case 6: //Cancelled
                            txtField04.setDisable(!pbState);
                            comboBox01.setDisable(!pbState);
                            textArea05.setDisable(!pbState);
                            btnApply.setDisable(!pbState);
                            break;
                    }
                    break;
                case "2":
                    txtField06.setText("APPROVED");
                    txtField04.setDisable(!pbState);
                    comboBox01.setDisable(!pbState);
                    textArea05.setDisable(!pbState);
                    btnApply.setDisable(!pbState);
                    break;
                default:
                    txtField06.setText("");
                    break;
            }
            txtField07.setText((String) oTransAS.getReservation(pnRow, 13));
            txtField08.setText(InputTextUtil.xsDateShort((Date) oTransAS.getReservation(pnRow, 14)));
            textArea05.setText((String) oTransAS.getReservation(pnRow, 6));
        }
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (comboBox01.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Slip Type` selected.", pxeModuleName, "Please select `Slip Type` value.");
            comboBox01.requestFocus();
            return false;
        }
        return true;
    }
}
