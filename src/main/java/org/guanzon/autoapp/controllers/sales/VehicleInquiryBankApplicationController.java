/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquiryBankApplicationController implements Initializable {

    private GRider oApp;
    private BankApplication oTransBankApp;

    private String psTransNo = "";
    private String psOApplieddate = "";
    private int pnInqPayMode;
    private int pnEditMode;
    private final String pxeModuleName = "Inquiry Bank Application";

    ObservableList<String> cBankPaymode = FXCollections.observableArrayList("BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cBankStatus = FXCollections.observableArrayList("ON-GOING", "DECLINE", "APPROVED"); //Bank Application Status Values

    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField01, txtField02;
    @FXML
    private DatePicker datePicker05, datePicker06;
    @FXML
    private ComboBox<String> comboBox03, comboBox04;
    @FXML
    private TextArea textArea07;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

//    public void setObject(InquiryBankApplication foValue) {
//        oTransBankApp = foValue;
//    }
    public void setEditMode(Integer fiValue) {
        pnEditMode = fiValue;
    }

    public void setsTransNo(String fsValue) {
        psTransNo = fsValue;
    }

    public void setInqPaymentMode(Integer fiValue) {
        pnInqPayMode = fiValue;
    }

    private Stage getStage() {
        return (Stage) datePicker05.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBox03.setItems(cBankPaymode);
        comboBox04.setItems(cBankStatus);

        initCmboxFieldAction();
        datePicker05.setDayCellFactory(callApplied);
        datePicker06.setDayCellFactory(callApprove);

        initTextKeyPressed();
        initTextFieldFocus();

        btnClose.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
        loadBankApplication();
        initFields(pnEditMode);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnSave":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                } else {
                    return;
                }
                if (setSelection()) {
                    // oTransBankApp.setPayMode(pnInqPayMode);
                    //oTransBankApp.setTransNox((String) oTransBankApp.getBankApp( 5));
//                    if (oTransBankApp.SaveRecord()) {
//                        ShowMessageFX.Information(null, pxeModuleName, "Bank Application save sucessfully.");
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTransBankApp.getMessage());
//                        return;
//                    }
                } else {
                    return;
                }
                CommonUtils.closeStage(btnSave);
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
        if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Payment Mode` selected.", pxeModuleName, "Please select `Payment Mode` value.");
            comboBox03.requestFocus();
            return false;
        } else {
            if (comboBox03.getSelectionModel().getSelectedIndex() != pnInqPayMode) {
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) && (comboBox04.getSelectionModel().getSelectedIndex() != 1)) {
                    ShowMessageFX.Warning("Invalid `Payment Mode` selected.", pxeModuleName, "Payment Mode selected is not the same with Inquiry Payment Mode.");
                    return false;
                }
            }
//            oTransBankApp.setBankApp(4, comboBox03.getSelectionModel().getSelectedIndex());
        }
        if (comboBox04.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Application Status` selected.", pxeModuleName, "Please select `Application Status` value.");
            comboBox04.requestFocus();
            return false;
        } else {
//                oTransBankApp.setBankApp(9, comboBox04.getSelectionModel().getSelectedIndex());
        }

        return true;
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea07);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));

    }

    private void txtField_KeyPressed(KeyEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
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
//                    case "txtField01":
//                        loJSON = oTransBankApp.searchEventType(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField01.setText(oTransBankApp.getModel().getModel().getBankName());
//                            txtField02.setText(oTransBankApp.getModel().getModel().getBankAddress());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField01.setText("");
//                            txtField01.requestFocus();
//                            return;
//                        }
//                        break;
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
//        txtField01.setText(oTransBankApp.getBankApp(16).toString()); //Bank Name
//        txtField02.setText(oTransBankApp.getBankApp(18).toString()); //Bank Address
//        txtField02.setValue(strToDate(CommonUtils.xsDateShort((Date) oTransBankApp.getBankApp(2))));
//        if (pnEditMode == EditMode.ADDNEW) {
//            comboBox03.getSelectionModel().select(pnInqPayMode); //Payment Mode
//        } else {
//            comboBox03.getSelectionModel().select(Integer.parseInt(oTransBankApp.getBankApp(4).toString())); //Payment Mode
//        }
//        if (Integer.parseInt(oTransBankApp.getBankApp(9).toString()) == 3) {
//            comboBox04.setValue("Cancelled");
//            pnEditMode = EditMode.UNKNOWN;
//        } else {
//            comboBox04.getSelectionModel().select(Integer.parseInt(oTransBankApp.getBankApp(9).toString())); //Bank Application Status
//        }
//        if (oTransBankApp.getModel().getModel().getDateFrom() != null && !oTransBankApp.getModel().getModel().getDateFrom().toString().isEmpty()) {
//            datePicker05.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransBankApp.getModel().getModel().getDateFrom())));
//        }
//        if (oTransBankApp.getModel().getModel().getDateThru() != null && !oTransBankApp.getModel().getModel().getDateThru().toString().isEmpty()) {
//            datePicker06.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransBankApp.getModel().getModel().getDateThru())));
//        }
//
//        textArea07.setText(oTransBankApp.getBankApp(8).toString()); //Remarks
//
//        //Get the original applied date
//        if (pnEditMode == EditMode.UPDATE) {
//            if (psOApplieddate.isEmpty()) {
//                psOApplieddate = oTransBankApp.getBankApp(2).toString();
//            }
//        }

    }

    private Callback<DatePicker, DateCell> callApplied = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate foItem, boolean fbEmpty) {
                    super.updateItem(foItem, fbEmpty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate loToday = LocalDate.now();
                    if (pnEditMode == EditMode.ADDNEW) {
                        LocalDate loMinDate = LocalDate.now().minusDays(7);
                        setDisable(fbEmpty || foItem.isBefore(loMinDate) || foItem.compareTo(loToday) > 0);
                    } else if (pnEditMode == EditMode.UPDATE) {
                        LocalDate loMinDate = InputTextUtil.strToDate(psOApplieddate).minusDays(7);
                        setDisable(fbEmpty || foItem.isBefore(loMinDate) || foItem.compareTo(InputTextUtil.strToDate(psOApplieddate)) > 0);
                    }
                }
            };
        }
    };

    private Callback<DatePicker, DateCell> callApprove = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate foItem, boolean fbEmpty) {
                    super.updateItem(foItem, fbEmpty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate loToday = LocalDate.now();
                    LocalDate loMinDate = LocalDate.now().minusDays(7);
                    setDisable(fbEmpty || foItem.isBefore(loMinDate) || foItem.compareTo(loToday) > 0);
                }
            };
        }
    };

    private void initCmboxFieldAction() {
        datePicker05.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                oTransInquiry.setMaster(7, SQLUtil.toDate(datePicker05.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        datePicker06.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                oTransInquiry.setMaster(7, SQLUtil.toDate(datePicker06.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox03.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
//                    oTransInquiry.getModel().getModel().setTitle(String.valueOf((comboBox21.getSelectionModel().getSelectedIndex())));
                }
                initFields(pnEditMode);
            }
        });
        comboBox04.setOnAction(event -> {
            if (comboBox04.getSelectionModel().getSelectedIndex() == 2) {
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    datePicker06.setDisable(false);
                    datePicker06.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())));
                }
            }
        });
        comboBox04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
//                    oTransInquiry.getModel().getModel().setTitle(String.valueOf((comboBox21.getSelectionModel().getSelectedIndex())));
                }
                initFields(pnEditMode);
            }
        });
    }

    private void initTextFieldFocus() {
        textArea07.focusedProperty().addListener(txtArea_Focus);
    }
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
                case 7:
//                    oTransBank.getModel().getModel().setAddlInfo(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(fnValue != EditMode.ADDNEW); //Bank Name
        comboBox03.setDisable(!lbShow); //Payment Mode
        comboBox04.setDisable(!lbShow); //Application Status
        datePicker05.setDisable(!lbShow); //Applied Date
        datePicker06.setDisable(true); //Approved Date
        textArea07.setDisable(!lbShow); //Remarks
        btnSave.setDisable(!lbShow);

        if (fnValue == EditMode.UPDATE) {
            if ((comboBox04.getSelectionModel().getSelectedIndex() == 1) || (comboBox04.getSelectionModel().getSelectedIndex() == 2)) {
                comboBox03.setDisable(true); //Payment Mode
                comboBox04.setDisable(true); //Application Status
                datePicker05.setDisable(true); //Applied Date
            }
        }

    }

}
