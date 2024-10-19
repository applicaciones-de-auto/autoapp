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
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleInquirySalesAdvancesController implements Initializable {

    private GRider oApp;
    private final String pxeModuleName = "Vehicle Inquiry Sales Advances";
    ObservableList<String> cSlipType = FXCollections.observableArrayList("RESERVATION", "DEPOSIT", "SAFEGUARD DUTY");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private int pnRow = 0;
    private Inquiry oTrans;
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
        oTrans = foValue;
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
        initCapitalizationFields();
        initPatternFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtonsClick();
        initComboBoxItems();
        initCapitalizationFields();
        loadMasterFields();
        initFields();
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField06, txtField07);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        CustomCommonUtil.setCapsLockBehavior(textArea05);
    }

    private void loadMasterFields() {
        comboBox01.getSelectionModel().select(Integer.parseInt(oTrans.getReservation(pnRow, "cResrvTyp").toString())); //VSA Type
        txtField02.setText(oTrans.getReservation(pnRow, "sReferNox").toString());
        if (pbState) { //Add
            txtField03.setText(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()));
            txtField06.setText("FOR APPROVAL");
        } else {
            txtField03.setText(CustomCommonUtil.xsDateShort((Date) oTrans.getReservation(pnRow, "dTransact")));
            txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getReservation(pnRow, "nAmountxx")))));
            switch (oTrans.getReservation(pnRow, "cTranStat").toString()) {
                case TransactionStatus.STATE_OPEN:
                    txtField06.setText("FOR APPROVAL");
                    switch (pnIinqStat) {
                        case 0: //For Follow up
                            CustomCommonUtil.setDisable(pbState, txtField04, comboBox01,
                                    textArea05, btnApply);
                            break;
                        case 1: //On Process
                        case 3: //VSP
                            if ((pnIEditMode == EditMode.UPDATE)) {
                                CustomCommonUtil.setDisable(pbState, txtField04, comboBox01,
                                        textArea05, btnApply);
                            } else {
                                CustomCommonUtil.setDisable(!pbState, txtField04, comboBox01,
                                        textArea05, btnApply);
                            }
                            break;
                        case 2: //Lost Sale
                        case 4: //Sold
                        case 5: //Cancelled
                            CustomCommonUtil.setDisable(!pbState, txtField04, comboBox01,
                                    textArea05, btnApply);
                            break;
                    }
                    break;
                case TransactionStatus.STATE_CLOSED:
                    txtField06.setText("APPROVED");
                    CustomCommonUtil.setDisable(!pbState, txtField04, comboBox01,
                            textArea05, btnApply);
                    break;
                case TransactionStatus.STATE_CANCELLED:
                    txtField06.setText("CANCELLED");
                    CustomCommonUtil.setDisable(!pbState, txtField04, comboBox01,
                            textArea05, btnApply);
                    break;
                case TransactionStatus.STATE_POSTED:
                    txtField06.setText("POSTED");
                    break;
            }
        }

        txtField07.setText((String) oTrans.getReservation(pnRow, "sApprover"));
        txtField08.setText(CustomCommonUtil.xsDateShort((Date) oTrans.getReservation(pnRow, "dApprovex")));
        textArea05.setText((String) oTrans.getReservation(pnRow, "sRemarksx"));
    }

    public void initPatternFields() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(pattern));
    }

    private void initTextKeyPressed() {
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        textArea05.setOnKeyPressed(this::txtArea_KeyPressed);
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

    private void initTextFieldFocus() {
        txtField04.focusedProperty().addListener(txtField_Focus);
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
                    try {
                        oTrans.setReservation(pnRow, "nAmountxx", Double.valueOf(txtField04.getText().replace(",", "")));
                        txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getReservation(pnRow, "nAmountxx")))));
                    } catch (NumberFormatException e) {
                        System.out.print(e);
                    }
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

    private void initButtonsClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnApply.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                if (pbState) {
                    oTrans.removeReservation(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            case "btnApply":
                if (setSelection()) {
                    if (setToClass()) {
                        CommonUtils.closeStage(btnApply);
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

    private void initComboBoxItems() {
        comboBox01.setItems(cSlipType);
    }

    private void initFields() {
        CustomCommonUtil.setDisable(true, comboBox01, txtField02, txtField03, txtField04, textArea05);
        if (!oTrans.getReservation(pnRow, "cTranStat").equals("2")) {
            CustomCommonUtil.setDisable(false, comboBox01, txtField04, textArea05);
        }
    }

    private boolean setToClass() {
        if (txtField04.getText().matches("[^0-9].*") || txtField04.getText().matches(".*\\.$")) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter a valid amount.");
            return false;
        }
        if (txtField04.getText().equals("0.00") || txtField04.getText().isEmpty()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value amount.");
            return false;
        }
        switch (txtField06.getText()) {
            case "CANCELLED":
                oTrans.setReservation(pnRow, "cTranStat", TransactionStatus.STATE_CANCELLED);
                break;
            case "FOR APPROVAL":
                oTrans.setReservation(pnRow, "cTranStat", TransactionStatus.STATE_OPEN);
                break;
            case "APPROVED":
                oTrans.setReservation(pnRow, "cTranStat", TransactionStatus.STATE_CLOSED);
                break;
            case "POSTED":
                oTrans.setReservation(pnRow, "cTranStat", TransactionStatus.STATE_POSTED);
                break;
        }

        oTrans.setReservation(pnRow, "dTransact", SQLUtil.toDate(txtField03.getText(), SQLUtil.FORMAT_SHORT_DATE));
        oTrans.setReservation(pnRow, "sRemarksx", textArea05.getText());
        oTrans.setReservation(pnRow, "cResrvTyp", String.valueOf(comboBox01.getSelectionModel().getSelectedIndex()));
        return true;
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
