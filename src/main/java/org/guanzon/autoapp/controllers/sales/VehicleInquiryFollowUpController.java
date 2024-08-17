/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Arrays;
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
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.auto.main.sales.FollowUp;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.auto.model.sales.Model_Inquiry_FollowUp;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VehicleInquiryFollowUpController implements Initializable {

    private GRider oApp;
    private FollowUp oTransFollow;
    private Inquiry oTransInquiry;
    private int pnRow = 0;
    private boolean pbState = false;
    private String psSourceNo = "";
    private String psRefNox = "";
    private final String pxeModuleName = "Inquiry Follow Up";
    ObservableList<String> cMedium = FXCollections.observableArrayList("TEXT", "CALL", "SOCIAL MEDIA", "EMAIL", "VIBER");
    @FXML
    private Button btnSave, btnClose;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private ComboBox<String> comboBox02;
    @FXML
    private TextField txtField01, txtField04, txtField07, txtFieldRef;
    @FXML
    private TextArea textArea05, textArea06;

    /**
     * Initializes the controller class.
     */
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(FollowUp foValue) {
        oTransFollow = foValue;
    }

    public void setTableRows(int row) {
        pnRow = row;
    }

    public void setState(boolean flValue) {
        pbState = flValue;
    }

    public void setSource(String fsValue) {
        psSourceNo = fsValue;
    }

    public void setRefNo(String fsValue) {
        psRefNox = fsValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboBox02.setItems(cMedium);
        datePicker03.setDayCellFactory(callDate);

        comboBox02.setOnAction(event -> {
            if (comboBox02.getSelectionModel().getSelectedIndex() >= 0) {
                txtField04.setText("");
                oTransFollow.getMasterModel().getMasterModel().setPlatform("");
                String lsMethod = "";
                switch (comboBox02.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        lsMethod = "TEXT";
                        break;
                    case 1:
                        lsMethod = "CALL";
                        break;
                    case 2:
                        lsMethod = "SOCIAL MEDIA";
                        break;
                    case 3:
                        lsMethod = "EMAIL";
                        break;
                    case 4:
                        lsMethod = "VIBER";
                        break;
                }
                oTransFollow.getMasterModel().getMasterModel().setMethodCd(lsMethod);
            }
            initFields();
        }
        );
        datePicker03.setOnAction(e -> {
            oTransFollow.setMaster(8, SQLUtil.toDate(datePicker03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }
        );
        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransFollow.getMasterModel().getMasterModel().setSclMedia("");
                    oTransFollow.getMasterModel().getMasterModel().setPlatform("");
                }
            }
        }
        );
        initCapitalizationFields();
        initTextKeyPressed();
        initTextAreaFocus();
        initButtonClick();
        loadFollowUpField();
        initFields();
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        InputTextUtil.setCapsLockBehavior(textArea05);
        InputTextUtil.setCapsLockBehavior(textArea06);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField04);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea05, textArea06);
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
                case "txtField04":
                    loJSON = oTransFollow.searchOnlinePlatform(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField04.setText(oTransFollow.getMasterModel().getMasterModel().getPlatform());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField04.setText("");
                        txtField04.requestFocus();
                        return;
                    }
                    break;
            }
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

    private void initTextAreaFocus() {
        textArea05.focusedProperty().addListener(txtArea_Focus);
        textArea06.focusedProperty().addListener(txtArea_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
    }

    private void initButtonClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnSave.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnSave":
                LocalDate followUp = datePicker03.getValue();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                    if (followUp == null) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter value in next follow-up date.");
                        return;
                    }
                    if (followUp.equals(txtField01.getText())) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Follow Up date cannot be the same with next follow up date.");
                        return;
                    }
                    if (txtField07.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter value in follow-up time.");
                        return;
                    } else {
                        if (txtField07.getText().equals(Time.valueOf("00:00:00")) || txtField07.getText().length() < 8 || txtField07.getText().length() > 8) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid time in follow-up time.");
                            return;
                        }
                    }
                    int lnHr = Integer.parseInt(txtField07.getText().substring(0, 2));
                    String lnCol1 = txtField07.getText().substring(2, 3);
                    int lnMin = Integer.parseInt(txtField07.getText().substring(3, 5));
                    String lnCol2 = txtField07.getText().substring(5, 6);
                    int lnSs = Integer.parseInt(txtField07.getText().substring(6, 8));

                    String timePattern = "^\\d{2}:\\d{2}:\\d{2}$";
                    String timeInput = txtField07.getText();
                    if (!lnCol1.equals(":") || !lnCol2.equals(":")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Invalid Next Follow Up Time.");
                        return;
                    }

                    if (lnHr < 8 || lnHr > 17) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Invalid Next Follow Up Time working hours.");
                        return;
                    }

                    if (lnMin < 0 || lnMin > 59) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Invalid Next Follow Up Time minutes.");
                        return;
                    }

                    if (lnSs < 0 || lnSs > 59) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Invalid Next Follow Up Time seconds.");
                        return;
                    }

                    if (lnHr == 17) {
                        if (lnMin > 30) {

                            ShowMessageFX.Warning(null, pxeModuleName, "Invalid Next Follow Up Time working hours.");
                            return;
                        }
                    }
                    // Check if the input matches the time pattern
                    if (!timeInput.matches(timePattern)) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter a valid time format: hh:mm:ss (e.g., 00:00:00).");
                        return;
                    }
                    if (textArea05.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter value in follow up about.");
                        return;
                    }
                    if (textArea06.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter value in response/remarks.");
                        return;
                    }
                    if (textArea05.getText().trim().equals((textArea06.getText()))) {
                        ShowMessageFX.Warning(null, pxeModuleName, "cannot be the same with remarks.");
                        return;
                    }
                    if (textArea06.getText().trim().equals((textArea05.getText()))) {
                        ShowMessageFX.Warning(null, pxeModuleName, "cannot be the same with follow up about.");
                        return;
                    }
                    if (setSelection()) {
                        oTransFollow.getMasterModel().getMasterModel().setEmployID(oApp.getUserID());
                        oTransFollow.getMasterModel().getMasterModel().setTransNo(psSourceNo);
                        loJSON = oTransFollow.saveTransaction();
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
                } else {
                    return;
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
        initFields();
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Method Code` selected.", pxeModuleName, "Please select `Method Code` value.");
            comboBox02.requestFocus();
            return false;
        } else {
            String lsMethod = "";
            switch (comboBox02.getSelectionModel().getSelectedIndex()) {
                case 0:
                    lsMethod = "TEXT";
                    break;
                case 1:
                    lsMethod = "CALL";
                    break;
                case 2:
                    lsMethod = "SOCIAL MEDIA";
                    break;
                case 3:
                    lsMethod = "EMAIL";
                    break;
                case 4:
                    lsMethod = "VIBER";
                    break;
            }
            oTransFollow.getMasterModel().getMasterModel().setMethodCd(lsMethod);
        }
        return true;
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
                case 7:
                    oTransFollow.getMasterModel().getMasterModel().setFollowUpTme(Time.valueOf(lsValue));
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
                case 5:
                    oTransFollow.getMasterModel().getMasterModel().setMessage(lsValue);
                    break;
                case 6:
                    oTransFollow.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private Callback<DatePicker, DateCell> callDate = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate fbItem, boolean fbEmpty) {
                    super.updateItem(fbItem, fbEmpty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    setDisable(fbEmpty || fbItem.compareTo(today) < 0);
                }
            };
        }
    };

    private void loadFollowUpField() {
        JSONObject loJSON = pbState ? null : oTransFollow.openTransaction(psRefNox);

        if (pbState || "success".equals(loJSON.get("result"))) {
            Model_Inquiry_FollowUp master = oTransFollow.getMasterModel().getMasterModel();

            txtFieldRef.setText(master.getReferNo());
            txtField01.setText(InputTextUtil.xsDateShort(master.getTransactDte()));

            if (master.getMethodCd() != null) {
                String lnMethod = "";
                switch (master.getMethodCd()) {
                    case "TEXT":
                        lnMethod = "0";
                        break;
                    case "CALL":
                        lnMethod = "1";
                        break;
                    case "SOCIAL MEDIA":
                        lnMethod = "2";
                        break;
                    case "EMAIL":
                        lnMethod = "3";
                        break;
                    case "VIBER":
                        lnMethod = "4";
                        break;
                    default:
                        lnMethod = "-1";
                        break;
                }
                comboBox02.getSelectionModel().select(Integer.parseInt(lnMethod));
            }

            if (master.getFollowUpDte() != null) {
                datePicker03.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(master.getFollowUpDte())));
            }

            txtField04.setText(master.getPlatform());
            txtField07.setText(String.valueOf(master.getFollowUpTme()));
            textArea05.setText(master.getMessage());
            textArea06.setText(master.getRemarks());

        }
    }

    private void initFields() {
        if (pbState) {
            datePicker03.setDisable(false);
            txtField07.setDisable(false);
            comboBox02.setDisable(false);
            textArea05.setDisable(false);
            textArea06.setDisable(false);
            btnSave.setVisible(true);
            btnSave.setManaged(true);
            if (comboBox02.getSelectionModel().getSelectedIndex() == 2) {
                txtField04.setDisable(false);
            } else {
                txtField04.setDisable(true);
            }
        } else {
            datePicker03.setDisable(true);
            txtField07.setDisable(true);
            btnSave.setVisible(false);
            btnSave.setManaged(false);
            txtField04.setDisable(true);
            comboBox02.setDisable(true);
            textArea05.setDisable(true);
            textArea06.setDisable(true);
        }
    }
}
