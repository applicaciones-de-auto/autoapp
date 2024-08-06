/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquirySalesAdvancesController implements Initializable {

    private GRider oApp;
    private final String pxeModuleName = "Inquiry Vehicle Sales Advances";
    ObservableList<String> cSlipType = FXCollections.observableArrayList("RESERVATION", "DEPOSIT", "SAFEGUARD DUTY");
    public int pnTbl_Row = 0;
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

//    public void setVSAObject(InquiryProcess foValue) {
//        oTransProcess = foValue;
//    }
    public void setTableRows(int row) {
        pnTbl_Row = row;
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

        Pattern pattern = Pattern.compile("[0-9,.]$");
        txtField04.setTextFormatter(new InputTextFormatterUtil(pattern));
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        textArea05.setOnKeyPressed(this::txtArea_KeyPressed);
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

    //Search using F3
    private void txtField_KeyPressed(KeyEvent event) {
        TextField loTxtField = (TextField) event.getSource();
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(loTxtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(loTxtField);
        }
    }

    /*TRIGGER FOCUS*/
    private void txtArea_KeyPressed(KeyEvent event) {
        if (event.getCode() == ENTER || event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnApply":
                if (setSelection()) {
//                    if (pbState) {
//                        oTransProcess.addReserve();
//                    }
//                    // Create a DecimalFormat with two decimal places
//                    DecimalFormat decimalFormat = new DecimalFormat("###0.00");
//                    oTransProcess.setInqRsv(pnTbl_Row, 2, SQLUtil.toDate(txtField02.getText(), SQLUtil.FORMAT_SHORT_DATE));
//                    oTransProcess.setInqRsv(pnTbl_Row, 5, decimalFormat.format(Double.parseDouble(txtField05.getText().replace(",", ""))));
//                    oTransProcess.setInqRsv(pnTbl_Row, 6, textArea06.getText());
//                    oTransProcess.setInqRsv(pnTbl_Row, 12, comboBox01.getSelectionModel().getSelectedIndex());
                } else {
                    return;
                }
                CommonUtils.closeStage(btnApply);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private void loadInquiryReservation() {
//        comboBox01.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqRsv(pnTbl_Row, 12).toString())); //VSA Type
//        if (pbState) { //Add
//            txtField03.setText(InputTextUtil.xsDateShort((Date) oApp.getServerDate()));
//            txtField06.setText("FOR APPROVAL");
//        } else {
//            txtField03.setText(CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(pnTbl_Row, 2)));
//            txtField02.setText(oTransProcess.getInqRsv(pnTbl_Row, 3).toString());
//            DecimalFormat loDecimalFormat = new DecimalFormat("#,##0.00");
//            String lsFormattedAmount = loDecimalFormat.format(Double.parseDouble(String.valueOf(oTransProcess.getInqRsv(pnTbl_Row, 5))));
//            txtField04.setText(lsFormattedAmount);
//            switch (oTransProcess.getInqRsv(pnTbl_Row, 13).toString()) {
//                case "0":
//                    txtField06.setText("FOR APPROVAL");
//                    switch (pnIinqStat) {
//                        case 0: //For Follow up
//                            txtField04.setDisable(pbState);
//                            comboBox01.setDisable(pbState);
//                            textArea06.setDisable(pbState);
//                            btnApply.setDisable(pbState);
//                            break;
//                        case 1: //On Process
//                        case 3: //VSP
//                            if ((pnIEditMode == EditMode.UPDATE)) {
//                                txtField04.setDisable(pbState);
//                                comboBox01.setDisable(pbState);
//                                textArea06.setDisable(pbState);
//                                btnApply.setDisable(pbState);
//                            } else {
//                                txtField05.setDisable(!pbState);
//                                comboBox01.setDisable(!pbState);
//                                textArea06.setDisable(!pbState);
//                                btnApply.setDisable(!pbState);
//                            }
//                            break;
//                        case 2: //Lost Sale
//                        case 4: //Sold
//                        case 5: //Retired
//                        case 6: //Cancelled
//                            txtField04.setDisable(!pbState);
//                            comboBox01.setDisable(!pbState);
//                            textArea06.setDisable(!pbState);
//                            btnApply.setDisable(!pbState);
//                            break;
//                    }
//                    break;
//                case "1":
//                    txtField06.setText("APPROVED");
//                    txtField04.setDisable(!pbState);
//                    comboBox01.setDisable(!pbState);
//                    textArea06.setDisable(!pbState);
//                    btnApply.setDisable(!pbState);
//                    break;
//                case "2":
//                    txtField06.setText("CANCELLED");
//                    txtField04.setDisable(!pbState);
//                    comboBox01.setDisable(!pbState);
//                    textArea06.setDisable(!pbState);
//                    btnApply.setDisable(!pbState);
//                    break;
//                default:
//                    txtField06.setText("");
//                    break;
//            }
//            txtField07.setText((String) oTransProcess.getInqRsv(pnTbl_Row, 23));
//            txtField08.setText(CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(pnTbl_Row, 15)));
//            textArea06.setText((String) oTransProcess.getInqRsv(pnTbl_Row, 6));
//        }
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
