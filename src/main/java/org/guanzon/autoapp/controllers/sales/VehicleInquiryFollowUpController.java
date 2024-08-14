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
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquiryFollowUpController implements Initializable {

    private GRider oApp;
    private FollowUp oTransFollow;
    private Inquiry oTransInquiry;
    private int pnRow = 0;
    private String psTransNo = "";
    private int pnEditMode;
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
    private TextField txtField01, txtField04, txtField07;
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
                oTransFollow.getMasterModel().getMasterModel().setMethodCd(String.valueOf(comboBox02.getValue()));
            }
            initFields();
        }
        );
        datePicker03.setOnAction(e -> {
            oTransFollow.setMaster(8, SQLUtil.toDate(datePicker03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }
        );
//        Pattern time = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]*");
//        txtField07.setTextFormatter(new InputTextFormatterUtil(time));
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
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save?")) {
                } else {
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
            ShowMessageFX.Warning("No `Payment Mode` selected.", pxeModuleName, "Please select `Payment Mode` value.");
            comboBox02.requestFocus();
            return false;
        } else {
            oTransFollow.getMasterModel().getMasterModel().setMethodCd(String.valueOf(comboBox02.getValue()));
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
        JSONObject loJSON = new JSONObject();
        if (pbState) {
            txtField01.setText(InputTextUtil.xsDateShort(oTransFollow.getMasterModel().getMasterModel().getTransactDte()));
            if (oTransFollow.getMasterModel().getMasterModel().getMethodCd() != null) {
                comboBox02.getSelectionModel().select(Integer.parseInt(oTransFollow.getMasterModel().getMasterModel().getMethodCd()));
            }
            if (oTransFollow.getMasterModel().getMasterModel().getFollowUpDte() != null) {
                datePicker03.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransFollow.getMasterModel().getMasterModel().getFollowUpDte())));
            }
            txtField04.setText(oTransFollow.getMasterModel().getMasterModel().getPlatform());
            txtField07.setText(String.valueOf(oTransFollow.getMasterModel().getMasterModel().getFollowUpTme()));
            textArea05.setText(oTransFollow.getMasterModel().getMasterModel().getMessage());
            textArea06.setText(oTransFollow.getMasterModel().getMasterModel().getRemarks());
        } else {
            loJSON = oTransFollow.openTransaction(psRefNox);
            if ("success".equals((String) loJSON.get("result"))) {
                txtField01.setText(InputTextUtil.xsDateShort(oTransFollow.getMasterModel().getMasterModel().getTransactDte())
                );
                if (oTransFollow.getMasterModel().getMasterModel().getMethodCd() != null) {
                    comboBox02.getSelectionModel().select(Integer.parseInt(oTransFollow.getMasterModel().getMasterModel().getMethodCd()));
                }
                if (oTransFollow.getMasterModel().getMasterModel().getFollowUpDte() != null) {
                    datePicker03.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransFollow.getMasterModel().getMasterModel().getFollowUpDte())));
                }
                txtField04.setText(oTransFollow.getMasterModel().getMasterModel().getPlatform());
                txtField07.setText(String.valueOf(oTransFollow.getMasterModel().getMasterModel().getFollowUpTme()));
                textArea05.setText(oTransFollow.getMasterModel().getMasterModel().getMessage());
                textArea06.setText(oTransFollow.getMasterModel().getMasterModel().getRemarks());
            }
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
