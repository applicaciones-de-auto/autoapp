/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.FollowUp;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleInquiryLostSaleController implements Initializable {

    private GRider oApp;
    private FollowUp oTransLost;
    private Inquiry oTransInquiry;
    private VehicleSalesProposal oTransVSP;
    private String psSourceNo;
    private String psVSPNox;
    private String psClient;
    private boolean pbIsInquiry = false;
    private String psBranchCd = "";
    private final String pxeModuleName = "Inquiry Lost Sale";
    //if change unit is only VSP will remove
    //if lost of sale both vsp and inquiry
    ObservableList<String> cTag = FXCollections.observableArrayList("CHANGE OF UNIT", "LOST SALE");
    ObservableList<String> cReasons = FXCollections.observableArrayList("BOUGHT FROM COMPETITOR VEHICLE",
            "BOUGHT SECOND HAND VEHICLE",
            "BOUGHT FROM OTHER DEALER",
            "NO BUDGET ",
            "LACK OF REQUIREMENTS",
            "NO AVAILABLE UNIT",
            "NOT PRIORITY YET ",
            "NOT APPROVED BY BANK  ");
    ObservableList<String> cGdsCat = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED");

    @FXML
    private Button btnDlost, btnTlost;
    @FXML
    private ComboBox<String> comboBox01, comboBox02, comboBox03;
    @FXML
    private TextField txtField04, txtField05;
    @FXML
    private TextArea textArea06;
    @FXML
    private Label lblClientName;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setFollowUpObject(FollowUp foValue) {
        oTransLost = foValue;
    }

    public void setInquiryObject(Inquiry foValue) {
        oTransInquiry = foValue;
    }

    public void setVSPObject(VehicleSalesProposal foValue) {
        oTransVSP = foValue;
    }

    public void setStateForm(boolean fsValue) {
        pbIsInquiry = fsValue;
    }

    public void setTransNo(String fsValue) {
        psSourceNo = fsValue;
    }

    public void setsVSPNox(String fsValue) {
        psVSPNox = fsValue;
    }

    public void setClientName(String fsValue) {
        psClient = fsValue;
    }

    public void setBranCD(String fsValue) {
        psBranchCd = fsValue;
    }

    private Stage getStage() {
        return (Stage) btnTlost.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblClientName.setText(psClient);
        comboBox02.setDisable(true);
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        if (pbIsInquiry) {
            comboBox01.getSelectionModel().select(1);
        }
        initFields();
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField04, txtField05);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea06);
        loTxtArea.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void initPatternFields() {
        Pattern pattern;
        pattern = Pattern.compile("^[a-zA-Z0-9 .,]*");
        textArea06.setTextFormatter(new TextFormatterUtil(pattern));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField04, txtField05);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea06);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
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
                    oTransLost.getMasterModel().getMasterModel().setMkeCmptr(lsValue);
                    break;
                case 5:
                    oTransLost.getMasterModel().getMasterModel().setMkeCmptr(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();

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
                case 6:
                    String lsTag = "";
                    switch (comboBox01.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            lsTag = "CHANGE OF UNIT : ";
                            break;
                        case 1:
                            lsTag = "LOST SALE : ";
                            break;
                    }
                    oTransLost.getMasterModel().getMasterModel().setRemarks(lsTag + " " + lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField04, txtField05);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        textArea06.setOnKeyPressed(this::txtArea_KeyPressed);
    }

    //Search using F3
    private void txtField_KeyPressed(KeyEvent event) {
        TextField loTxtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
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
        } else if (event.getCode() == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        }
    }

    public void initButtonsClick() {
        btnTlost.setOnAction(this::handleButtonAction);
        btnDlost.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        String sLostSale = "";
        switch (lsButton) {
            case "btnTlost":
                if (!setSelection()) {
                    return;
                }
                if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                    sLostSale = " Cancel this Transaction";
                } else {
                    sLostSale = " tag this inquiry as " + comboBox01.getValue();
                    if (!pbIsInquiry) {
                        sLostSale += " and Cancel VSP";
                    }
                }

                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to" + sLostSale + " ?")) {
                } else {
                    return;
                }
                if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                    if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select Reason.");
                        return;
                    }
                }
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    if (comboBox02.getSelectionModel().getSelectedIndex() >= 0) {
                        switch (comboBox02.getSelectionModel().getSelectedIndex()) {
                            case 0:
                            case 2:
                                if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please select Goods Category.");
                                    return;
                                }
                                if (txtField04.getText().trim().isEmpty()) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please select Car Make Competitor.");
                                    return;
                                }
                                if (txtField05.getText().trim().isEmpty()) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please select Car Dealer Competitor.");
                                    return;
                                }
                                break;
                            case 1:
                                if (txtField04.getText().trim().isEmpty()) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please select Car Make Competitor.");
                                    return;
                                }
                                if (txtField05.getText().trim().isEmpty()) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please select Car Dealer Competitor.");
                                    return;
                                }
                                break;
                        }
                    }
                }

                if (textArea06.getText().length() < 20) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter at least 20 characters.");
                    textArea06.requestFocus();
                    return;
                }
                if (!pbIsInquiry) {
                    loJSON = oTransVSP.cancelTransaction(oTransVSP.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
//                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                }

                if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                    loJSON = oTransInquiry.lostSale(psSourceNo);
                } else {
                    loJSON = oTransInquiry.openTransaction(psSourceNo);
                    oTransInquiry.getMasterModel().getMasterModel().setTranStat("1");
                    loJSON = oTransInquiry.saveTransaction();
                }

                if ("success".equals((String) loJSON.get("result"))) {
                    oTransLost.getMasterModel().setTargetBranchCd(psBranchCd);
                    oTransLost.getMasterModel().getMasterModel().setTransNo(psSourceNo);
                    oTransLost.getMasterModel().getMasterModel().setEmployID(oApp.getUserID());
                    oTransLost.getMasterModel().getMasterModel().setMethodCd("UPDATE");
                    oTransLost.getMasterModel().getMasterModel().setSclMedia("");
                    oTransLost.getMasterModel().getMasterModel().setPlatform("");
                    loJSON = oTransLost.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                        CommonUtils.closeStage(btnDlost);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;

            case "btnDlost":
                CommonUtils.closeStage(btnDlost);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private void initComboBoxItems() {
        comboBox01.setItems(cTag);
        comboBox02.setItems(cReasons);
        comboBox03.setItems(cGdsCat);
    }

    private void initFieldsAction() {
        comboBox01.setOnAction(event -> {
            int selectedIndex = comboBox01.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                clearFields();
                initFields();
            }
        });
        comboBox02.setOnAction(event -> {
            int selectedIndex = comboBox02.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                String[] reasons = {"cmptr", "2ndhd", "odlr", "nobgt", "lackr", "noavl", "nonp", "nonap"};
                String lsReason = reasons[selectedIndex];
                oTransLost.getMasterModel().getMasterModel().setMkeCmptr("");
                oTransLost.getMasterModel().getMasterModel().setDlrCmptr("");
                CustomCommonUtil.setText("", txtField04, txtField05);
                comboBox03.setValue("");
                if (selectedIndex != 1) {
                    comboBox03.setValue("");
                } else {
                    comboBox03.getSelectionModel().select(1);
                }
                if (selectedIndex == 0 || selectedIndex >= 3) {
                    oTransLost.getMasterModel().getMasterModel().setGdsCmptr("");
                }
                oTransLost.getMasterModel().getMasterModel().setRspnseCd(lsReason);
                initFields();
            }
        });
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTransLost.getMasterModel().getMasterModel().setGdsCmptr(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
            }
        });
    }

    private void initTextFieldsProperty() {
        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransLost.getMasterModel().getMasterModel().setMkeCmptr("");
                }
            }
        }
        );
        txtField05.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransLost.getMasterModel().getMasterModel().setDlrCmptr("");
                }
            }
        }
        );
    }

    private void clearFields() {
        CustomCommonUtil.setText("", txtField04, txtField05);
        CustomCommonUtil.setValue("", comboBox02, comboBox03);
        textArea06.setText("");
    }

    private void initFields() {
        comboBox02.setDisable(false);
        CustomCommonUtil.setDisable(true, comboBox01, comboBox03, txtField04,
                txtField05);
        if (!pbIsInquiry) {
            comboBox01.setDisable(false);
            comboBox02.setDisable(true);
            switch (comboBox01.getSelectionModel().getSelectedIndex()) {
                case 0:
                    CustomCommonUtil.setDisable(true, comboBox02, comboBox03, txtField04,
                            txtField05);
                    textArea06.setDisable(false);
                    break;
                case 1:
                    CustomCommonUtil.setDisable(false, comboBox02, comboBox03, txtField04,
                            txtField05);
                    break;
            }
        }
        switch (comboBox02.getSelectionModel().getSelectedIndex()) {
            case 0:
            case 2:
                CustomCommonUtil.setDisable(comboBox02.getValue().equals(""), comboBox03, txtField04, txtField05);
                break;
            case 1:
                comboBox03.setDisable(true);
                CustomCommonUtil.setDisable(comboBox02.getValue().equals(""), txtField04, txtField05);
                break;
            default:
                CustomCommonUtil.setDisable(true, comboBox03, txtField04, txtField05);
                break;
        }
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (comboBox01.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Tag` selected.", pxeModuleName, "Please select `Tag` value.");
            return false;
        } else {
            if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                    ShowMessageFX.Warning("No `Reason` selected.", pxeModuleName, "Please select `Reason` value.");
                    return false;
                } else {
                    int selectedIndex = comboBox02.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        String[] reasons = {"cmptr", "2ndhd", "odlr", "nobgt", "lackr", "noavl", "nonp", "nonap"};
                        String lsReason = reasons[selectedIndex];
                        oTransLost.getMasterModel().getMasterModel().setRspnseCd(lsReason);
                    }
                }
            }

        }
        return true;
    }

}
