/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.sales.BankApplication;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VehicleInquiryBankApplicationController implements Initializable {

    private GRider oApp;
    private BankApplication oTransBankApp;
    private Inquiry oTransInquiry;
    private int pnRow = 0;
    private String psOApplieddate = "";
    private int pnInqPayMode;
    private int pnEditMode;
    private boolean pbState = false;
    private String psSourceNo = "";
    private String psTransNox = "";
    private String psBranchCd = "";
    private final String pxeModuleName = "Inquiry Bank Application";

    ObservableList<String> cBankPaymode = FXCollections.observableArrayList("BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cBankStatus = FXCollections.observableArrayList("ON-GOING", "DECLINE", "APPROVED"); //Bank Application Status Values
    ObservableList<String> cBankType = FXCollections.observableArrayList("BANK", "CREDIT UNION", "INSURANCE COMPANY", "INVESTMENT COMPANIES");
    @FXML
    private Button btnSave, btnClose, btnBACancel;
    @FXML
    private TextArea textArea10;
    @FXML
    private ComboBox<String> comboBox03, comboBox06, comboBox07;
    @FXML
    private DatePicker datePicker08, datePicker09;
    @FXML
    private TextField txtField02, txtField01, txtField04, txtField05;
    @FXML
    private Button btnEdit;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(BankApplication foValue) {
        oTransBankApp = foValue;
    }

    public void setEditMode(Integer fiValue) {
        pnEditMode = fiValue;
    }

    public void setTableRows(int row) {
        pnRow = row;
    }

    public void setState(boolean flValue) {
        pbState = flValue;
    }

    public void setInqPaymentMode(Integer fiValue) {
        pnInqPayMode = fiValue;
    }

    public void setSource(String fsValue) {
        psSourceNo = fsValue;
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    public void setBranCD(String fsValue) {
        psBranchCd = fsValue;
    }

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComvoValue();
        initCmboxFieldAction();
        datePicker08.setDayCellFactory(callApplied);
        datePicker09.setDayCellFactory(callApprove);
        initTextKeyPressed();
        initTextFieldFocus();
        initButtonClick();
        loadBankApplication();
        initCapitalizationFields();
        btnBACancel.setVisible(!pbState);
        btnBACancel.setManaged(!pbState);
        initFields(pnEditMode);
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField01, txtField04, txtField05);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        CustomCommonUtil.setCapsLockBehavior(textArea10);
    }

    private void initComvoValue() {
        comboBox03.setItems(cBankType);
        comboBox06.setItems(cBankPaymode);
        comboBox07.setItems(cBankStatus);
    }

    private void initButtonClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        btnEdit.setOnAction(this::handleButtonAction);
        btnBACancel.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnEdit":
                loJSON = oTransBankApp.updateTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    pnEditMode = oTransBankApp.getEditMode();
                }
                break;
            case "btnBACancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to cancel this Bank Application?")) {
                    loJSON = oTransBankApp.cancelTransaction(psTransNox);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                        CommonUtils.closeStage(btnClose);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                } else {
                    return;
                }
                break;
            case "btnSave":
                LocalDate loApplied = datePicker08.getValue();
                LocalDate loApprove = datePicker09.getValue();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                    if (comboBox07.getSelectionModel().getSelectedIndex() == 2) {
                        if (loApprove.isBefore(loApplied)) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Invalid Approve Date.");
                            return;
                        }
                    }
                    if (setSelection()) {
                        oTransBankApp.getMasterModel().setTargetBranchCd(psBranchCd);
                        oTransBankApp.getMasterModel().getMasterModel().setSourceNo(psSourceNo);
                        loJSON = oTransBankApp.saveTransaction();
                        if ("success".equals((String) loJSON.get("result"))) {
                            if (pbState) {
                                ShowMessageFX.Information(null, pxeModuleName, "Bank Application added sucessfully.");
                            } else {
                                ShowMessageFX.Information(null, pxeModuleName, "Bank Application save sucessfully.");
                            }
                            CommonUtils.closeStage(btnClose);
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initFields(pnEditMode);
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (comboBox06.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Payment Mode` selected.", pxeModuleName, "Please select `Payment Mode` value.");
            comboBox06.requestFocus();
            return false;
        } else {
            if (comboBox06.getSelectionModel().getSelectedIndex() + 1 != pnInqPayMode) {
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) && (comboBox07.getSelectionModel().getSelectedIndex() != 1)) {
                    ShowMessageFX.Warning("Invalid `Payment Mode` selected.", pxeModuleName, "Payment Mode selected is not the same with Inquiry Payment Mode.");
                    return false;
                }
            }
            int lnModePymn = 1;
            switch (comboBox06.getValue()) {
                case "BANK PURCHASE ORDER":
                    lnModePymn = 1;
                    break;
                case "BANK FINANCING":
                    lnModePymn = 2;
                    break;
                case "COMPANY PURCHASE ORDER":
                    lnModePymn = 3;
                    break;
                case "COMPANY FINANCING":
                    lnModePymn = 4;
                    break;
            }
            oTransBankApp.getMasterModel().getMasterModel().setPayMode(String.valueOf(lnModePymn));
        }
        if (comboBox07.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Application Status` selected.", pxeModuleName, "Please select `Application Status` value.");
            comboBox07.requestFocus();
            return false;
        } else {
            oTransBankApp.getMasterModel().getMasterModel().setTranStat(String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
        }
        return true;
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField01, txtField04, txtField05);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea10);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        JSONObject loJSON = new JSONObject();
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField02":
                    loJSON = oTransBankApp.searchBank(lsValue, true);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField02.setText(oTransBankApp.getMasterModel().getMasterModel().getBankName());
                        if (oTransBankApp.getMasterModel().getMasterModel().getBankType() != null && !oTransBankApp.getMasterModel().getMasterModel().getBankType().trim().isEmpty()) {
                            switch ((String.valueOf(oTransBankApp.getMasterModel().getMasterModel().getBankType()))) {
                                case "bank":
                                    comboBox03.setValue("BANK");
                                    break;
                                case "cred":
                                    comboBox03.setValue("CREDIT UNION");
                                    break;
                                case "insc":
                                    comboBox03.setValue("INSURANCE COMPANY");
                                    break;
                                case "invc":
                                    comboBox03.setValue("INVESTMENT COMPANIES");
                                    break;
                                default:
                                    break;
                            }
                        }
                        txtField04.setText(oTransBankApp.getMasterModel().getMasterModel().getBrBankNm());
                        txtField05.setText(oTransBankApp.getMasterModel().getMasterModel().getAddressx());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField02.setText("");
                        txtField02.requestFocus();
                        return;
                    }
                    break;
            }
            initFields(pnEditMode);
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.DOWN) {
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
        } else if (event.getCode() == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        }
    }

    public void loadBankApplication() {
        JSONObject loJSON = new JSONObject();
        if (pbState) {
            txtField01.setText(oTransBankApp.getMasterModel().getMasterModel().getApplicNo());
            txtField02.setText(oTransBankApp.getMasterModel().getMasterModel().getBankName());
            if (oTransBankApp.getMasterModel().getMasterModel().getBankType() != null && !oTransBankApp.getMasterModel().getMasterModel().getBankType().trim().isEmpty()) {
                switch ((String.valueOf(oTransBankApp.getMasterModel().getMasterModel().getBankType()))) {
                    case "bank":
                        comboBox03.setValue("BANK");
                        break;
                    case "cred":
                        comboBox03.setValue("CREDIT UNION");
                        break;
                    case "insc":
                        comboBox03.setValue("INSURANCE COMPANY");
                        break;
                    case "invc":
                        comboBox03.setValue("INVESTMENT COMPANIES");
                        break;
                    default:
                        break;
                }
            }
            txtField04.setText(oTransBankApp.getMasterModel().getMasterModel().getBrBankNm());
            txtField05.setText(oTransBankApp.getMasterModel().getMasterModel().getAddressx());

            comboBox06.getSelectionModel().select(pnInqPayMode - 1); //Payment Mode
            if (!oTransBankApp.getMasterModel().getMasterModel().getCancelld().isEmpty()) {
                comboBox07.setValue("CANCELLED");
                initFields(pnEditMode);
            } else {
                if (String.valueOf(oTransBankApp.getMasterModel().getMasterModel().getTranStat()) != null) {
                    comboBox07.getSelectionModel().select(Integer.parseInt(oTransBankApp.getMasterModel().getMasterModel().getTranStat())); //Bank Application Status
                }
            }
            if (oTransBankApp.getMasterModel().getMasterModel().getAppliedDte() != null) {
                datePicker08.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransBankApp.getMasterModel().getMasterModel().getAppliedDte())));
            }

            if (oTransBankApp.getMasterModel().getMasterModel().getApprovedDte() != null) {
                datePicker09.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransBankApp.getMasterModel().getMasterModel().getApprovedDte())));
            }
            textArea10.setText(oTransBankApp.getMasterModel().getMasterModel().getRemarks()); //Remarks
        } else {
            loJSON = oTransBankApp.openTransaction(psTransNox);
            if ("success".equals((String) loJSON.get("result"))) {
                txtField01.setText(oTransBankApp.getMasterModel().getMasterModel().getApplicNo());
                txtField02.setText(oTransBankApp.getMasterModel().getMasterModel().getBankName());
                if (oTransBankApp.getMasterModel().getMasterModel().getBankType() != null && !oTransBankApp.getMasterModel().getMasterModel().getBankType().trim().isEmpty()) {
                    switch ((String.valueOf(oTransBankApp.getMasterModel().getMasterModel().getBankType()))) {
                        case "bank":
                            comboBox03.setValue("BANK");
                            break;
                        case "cred":
                            comboBox03.setValue("CREDIT UNION");
                            break;
                        case "insc":
                            comboBox03.setValue("INSURANCE COMPANY");
                            break;
                        case "invc":
                            comboBox03.setValue("INVESTMENT COMPANIES");
                            break;
                        default:
                            break;
                    }
                }
                txtField04.setText(oTransBankApp.getMasterModel().getMasterModel().getBrBankNm());
                txtField05.setText(oTransBankApp.getMasterModel().getMasterModel().getAddressx());
                if (oTransBankApp.getMasterModel().getMasterModel().getPayMode() != null && !oTransBankApp.getMasterModel().getMasterModel().getPayMode().trim().isEmpty()) {
                    String sModeOfPayment = "";
                    switch (Integer.parseInt(oTransBankApp.getMasterModel().getMasterModel().getPayMode())) {
                        case 1:
                            sModeOfPayment = "BANK PURCHASE ORDER";
                            break;
                        case 2:
                            sModeOfPayment = "BANK FINANCING";
                            break;
                        case 3:
                            sModeOfPayment = "COMPANY PURCHASE ORDER";
                            break;
                        case 4:
                            sModeOfPayment = "COMPANY FINANCING";
                            break;

                    }
                    comboBox06.setValue(sModeOfPayment);
                }
                if (!oTransBankApp.getMasterModel().getMasterModel().getCancelld().isEmpty()) {
                    comboBox07.setValue("CANCELLED");
                    initFields(pnEditMode);
                } else {
                    if (String.valueOf(oTransBankApp.getMasterModel().getMasterModel().getTranStat()) != null) {
                        comboBox07.getSelectionModel().select(Integer.parseInt(oTransBankApp.getMasterModel().getMasterModel().getTranStat())); //Bank Application Status
                    }
                }
                if (oTransBankApp.getMasterModel().getMasterModel().getAppliedDte() != null) {
                    datePicker08.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransBankApp.getMasterModel().getMasterModel().getAppliedDte())));
                }

                if (oTransBankApp.getMasterModel().getMasterModel().getApprovedDte() != null) {
                    datePicker09.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransBankApp.getMasterModel().getMasterModel().getApprovedDte())));
                }
                textArea10.setText(oTransBankApp.getMasterModel().getMasterModel().getRemarks()); //Remarks
                if (psOApplieddate.isEmpty()) {
                    psOApplieddate = CustomCommonUtil.xsDateShort(oTransBankApp.getMasterModel().getMasterModel().getAppliedDte());
                }
            }
        }

    }

    private Callback<DatePicker, DateCell> callApplied = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);

            LocalDate loToday = LocalDate.now();
            LocalDate loMinDate;

            if (pbState) {
                loMinDate = loToday.minusDays(7);
            } else {
                if (pnEditMode == EditMode.UPDATE) {
                    LocalDate loAppliedDate = CustomCommonUtil.strToDate(psOApplieddate);
                    loMinDate = loAppliedDate.minusDays(7);
                } else {
                    loMinDate = loToday.minusDays(7); // Default case
                }
            }

            setDisable(fbEmpty || foItem.isBefore(loMinDate) || foItem.isAfter(loToday));
        }
    };

    private Callback<DatePicker, DateCell> callApprove = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);

            LocalDate loToday = LocalDate.now();
            LocalDate loMinDate = loToday.minusDays(7);

            setDisable(fbEmpty || foItem.isBefore(loMinDate) || foItem.isAfter(loToday));
        }
    };

    private void initCmboxFieldAction() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransBankApp.getMasterModel().getMasterModel().setBankID("");
                    oTransBankApp.getMasterModel().getMasterModel().setBankName("");
                    comboBox03.setValue("");
                    txtField04.setText("");
                    txtField05.setText("");
                }
            }
        }
        );
        datePicker08.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                datePicker08.setDayCellFactory(callApplied);
                datePicker09.setDayCellFactory(callApprove);
                if (comboBox07.getSelectionModel().getSelectedIndex() == 2) {
                    datePicker09.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((oApp.getServerDate()))));
                } else {
                    datePicker09.setValue(LocalDate.of(1900, Month.JANUARY, 1));
                }
            }
        }
        );
        datePicker08.setOnAction(e -> {
            oTransBankApp.setMaster("dAppliedx", SQLUtil.toDate(datePicker08.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }
        );
        datePicker09.setOnAction(e -> {
            oTransBankApp.setMaster("dApproved", SQLUtil.toDate(datePicker09.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));

        }
        );
        comboBox06.setOnAction(event -> {
            if (comboBox06.getSelectionModel().getSelectedIndex() == 2) {
                datePicker08.setDisable(false);
                datePicker08.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate())));
            }
        }
        );
        comboBox07.setOnAction(e -> {
            if (comboBox07.getSelectionModel().getSelectedIndex() >= 0) {
                if (comboBox07.getSelectionModel().getSelectedIndex() == 2) {
                    datePicker09.setDisable(false);
                    datePicker09.setDayCellFactory(callApprove);
                    datePicker09.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((oApp.getServerDate()))));
                } else {
                    datePicker09.setDisable(true);
                    datePicker09.setValue(LocalDate.of(1900, Month.JANUARY, 1));
                }
                oTransBankApp.getMasterModel().getMasterModel().setTranStat(String.valueOf((comboBox07.getSelectionModel().getSelectedIndex())));
            }
            initFields(pnEditMode);
        }
        );
    }

    private void initTextFieldFocus() {
        txtField01.focusedProperty().addListener(txtField_Focus);
        textArea10.focusedProperty().addListener(txtArea_Focus);
    }

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTextField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextField.getId().substring(8, 10));
        String lsValue = loTextField.getText();

        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    oTransBankApp.getMasterModel().getMasterModel().setApplicNo(lsValue);
                    break;
            }
        } else {
            loTextField.selectAll();
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
                case 10:
                    oTransBankApp.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.UPDATE);
        txtField01.setDisable(true); //Bank Name
        txtField02.setDisable(true);
        comboBox07.setDisable(true);
        datePicker08.setDisable(true); //Approved Date
        datePicker09.setDisable(true); //Approved Date
        textArea10.setDisable(true);

        if (pbState) {
            txtField01.setDisable(false); //Bank Name
            txtField02.setDisable(false);
            comboBox07.setDisable(false);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
            btnSave.setVisible(true);
            btnSave.setManaged(true);
            datePicker08.setDisable(false); //Approved Date
            if (comboBox07.getSelectionModel().getSelectedIndex() == 2) {
                datePicker09.setDisable(false); //Approved Date
            }
            textArea10.setDisable(false);
        } else {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnSave.setVisible(false);
            btnSave.setManaged(false);
            if (comboBox07.getSelectionModel().getSelectedIndex() == 2) {
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                btnBACancel.setVisible(false);
                btnBACancel.setManaged(false);
            }
            if (comboBox07.getValue().equals("DECLINE")) {
                btnSave.setVisible(false);
                btnSave.setManaged(false);
                btnBACancel.setVisible(false);
                btnBACancel.setManaged(false);
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
            }
        }
        if (fnValue == EditMode.UPDATE) {
            comboBox07.setDisable(false);
            datePicker08.setDisable(true); //Approved Date
            if (comboBox07.getSelectionModel().getSelectedIndex() == 2) {
                datePicker09.setDisable(false); //Approved Date
            }
            if ((comboBox07.getSelectionModel().getSelectedIndex() == 1) || (comboBox07.getSelectionModel().getSelectedIndex() == 2)) {
                datePicker08.setDisable(true); //Applied Date
            }
            btnSave.setVisible(true);
            btnSave.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
            btnBACancel.setVisible(false);
            btnBACancel.setManaged(false);
            textArea10.setDisable(false);
        }
        if (comboBox07.getValue().equals("CANCELLED")) {
            btnSave.setVisible(false);
            btnSave.setManaged(false);
            btnBACancel.setVisible(false);
            btnBACancel.setManaged(false);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        }
    }

}
