/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquiryLostSaleFormController implements Initializable {

    private GRider oApp;
    private boolean pbLoaded = false;
//    private InquiryFollowUp oTransFollowUp;
//    private VehicleSalesProposalMaster oTransVSP;

    private String psSourceNo;
    private String psVSPNox;
    private String psClient;
    private int pnItag;
    private int pnIGdsCt;
    private boolean pbState = false;

    private final String pxeModuleName = "Inquiry Lost Sale Remarks Form";
    ObservableList<String> cTag = FXCollections.observableArrayList("CHANGE OF UNIT", "LOST SALE");
    ObservableList<String> cReasons = FXCollections.observableArrayList("BOUGHT FROM COMPETITOR BRAND", "BOUGHT FROM OTHER DEALER", "BOUGHT SECOND HAND VEHICLE", "LACK OF REQUIREMENTS", "NO BUDGET");
    ObservableList<String> cGdsCat = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED");

    @FXML
    private Button btnDlost;
    @FXML
    private Button btnTlost;
    @FXML
    private ComboBox<String> comboBox01, comboBox02, comboBox03;
    @FXML
    private TextField txtField04, txtField05;
    @FXML
    private TextArea textArea06;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

//    public void setFollowUpObject(InquiryFollowUp foValue) {
//        oTransFollowUp = foValue;
//    }
//
//    public void setVSPObject(VehicleSalesProposalMaster foValue) {
//        oTransVSP = foValue;
//    }
    public void setState(boolean fsValue) {
        pbState = fsValue;
    }

    public void setsSourceNo(String fsValue) {
        psSourceNo = fsValue;
    }

    public void setsVSPNox(String fsValue) {
        psVSPNox = fsValue;
    }

    public void setClientName(String fsValue) {
        psClient = fsValue;
    }

    private Stage getStage() {
        return (Stage) btnTlost.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Pattern pattern;
        pattern = Pattern.compile("^[a-zA-Z0-9 ]*");
        textArea06.setTextFormatter(new InputTextFormatterUtil(pattern));

//        lblClientName.setText(psClient); //Client Name
        comboBox01.setItems(cTag);
        comboBox02.setItems(cReasons);
        comboBox03.setItems(cGdsCat);
        comboBox02.setDisable(true);
        comboBox03.setDisable(true);
        txtField04.setDisable(true);
        txtField05.setDisable(true);

        if (pbState) {
            comboBox01.getSelectionModel().select(1); //Tag
            comboBox01.setDisable(pbState);
            comboBox02.setDisable(false);
        }

        btnTlost.setOnAction(this::handleButtonAction);
        btnDlost.setOnAction(this::handleButtonAction);

//        if (oTransFollowUp.NewRecord()) {
//        } else {
//            ShowMessageFX.Warning(null, pxeModuleName, oTransFollowUp.getMessage());
//        }
        initCmboxFieldAction();
        initTextKeyPressed();
        initTextFieldFocus();
        initCapitalizationFields();
    }

    private void initCmboxFieldAction() {
        comboBox01.setOnAction(event -> {
            comboBox02.setValue("");
            comboBox03.setValue("");
            txtField04.setText("");
            txtField05.setText("");
            System.out.println("comboBox01.getSelectionModel().getSelectedIndex() >>> " + comboBox01.getSelectionModel().getSelectedIndex());
            switch (comboBox01.getSelectionModel().getSelectedIndex()) {
                case 0:
                    comboBox02.setDisable(true);
                    comboBox03.setDisable(true);
                    txtField04.setDisable(true);
                    txtField05.setDisable(true);
                    break;
                case 1:
                    comboBox02.setDisable(false);
                    break;
            }

        });

        comboBox02.setOnAction(event -> {
            comboBox03.setValue("");
            txtField04.setText("");
            txtField05.setText("");
            switch (comboBox02.getSelectionModel().getSelectedIndex()) {
                case 0:
                case 1:
                    comboBox03.setDisable(false);
                    txtField04.setDisable(false);
                    txtField05.setDisable(false);
                    break;
                case 2:
                    comboBox03.getSelectionModel().select(1);
                    comboBox03.setDisable(true);
                    txtField04.setDisable(false);
                    txtField05.setDisable(false);
                    break;
                case 3:
                case 4:
                    comboBox03.setDisable(true);
                    txtField04.setDisable(true);
                    txtField05.setDisable(true);
                    break;
            }

        });
    }

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
                case 4:/*No of Target Client*/
//                    oTransLost.getModel().getModel().setTrgtClnt(Integer.valueOf(lsValue));
                    break;
                case 5:/*Total Event Budget*/
//                    oTransLost.getModel().getModel().setTrgtClnt(Integer.valueOf(lsValue));
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
//                    oTransLost.getModel().getModel().setActTitle(lsValue);
                    break;

            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField04, txtField05);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea06);
        loTxtArea.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        boolean lbisLostSale = true;
        String sLostSale = "";
        switch (lsButton) {
            case "btnTlost":
                if (!setSelection()) {
                    return;
                }

                if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                    lbisLostSale = false;
                    sLostSale = " Cancel this Transaction";
                } else {
                    lbisLostSale = true;
                    sLostSale = " tag this inquiry as " + comboBox01.getValue().toString();
                    if (!pbState) {
                        sLostSale = sLostSale + " and Cancel VSP";
                    }
                }

                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to" + sLostSale + " ?")) {
                } else {
                    return;
                }

                if (textArea06.getText().length() < 20) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter at least 20 characters.");
                    textArea06.requestFocus();
                    return;
                }

//                oTransFollowUp.setTransNox(psSourceNo);
//                oTransFollowUp.setVSPNox(psVSPNox);
//                oTransFollowUp.setisFollowUp(false);
//
//                if (!pbState) {
//                    if (!oTransVSP.cancelVSP()) {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTransVSP.getMessage());
//                        return;
//                    }
//                }
//
//                if (oTransFollowUp.SaveRecord()) {
//                    if (oTransFollowUp.LostSale(lbisLostSale, pbState)) {
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, oTransFollowUp.getMessage());
//                        return;
//                    }
//                    ShowMessageFX.Information(null, pxeModuleName, oTransFollowUp.getMessage());
//                } else {
//                    //ShowMessageFX.Warning(null, pxeModuleName, "Failed to Save Inquiry Lost Sales Remarks Form.");
//                    ShowMessageFX.Warning(null, pxeModuleName, oTransFollowUp.getMessage());
//                    return;
//                }
                CommonUtils.closeStage(btnTlost);
                break;
            case "btnDlost":
                CommonUtils.closeStage(btnDlost);
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (comboBox01.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning("No `Tag` selected.", pxeModuleName, "Please select `Tag` value.");
            comboBox01.requestFocus();
            return false;
        } else {
            //oTransFollowUp.setFollowUp(4,comboBox01.getValue().toString());
            if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
                if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                    ShowMessageFX.Warning("No `Reason` selected.", pxeModuleName, "Please select `Reason` value.");
                    comboBox02.requestFocus();
                    return false;
                } else {
//                    oTransFollowUp.setFollowUp(13, comboBox02.getValue().toString());
                }
            }
        }
        if (comboBox01.getSelectionModel().getSelectedIndex() == 1) {
            if ((comboBox02.getSelectionModel().getSelectedIndex() != 4) && (comboBox02.getSelectionModel().getSelectedIndex() != 3)) {
                if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
                    ShowMessageFX.Warning("No `Goods Category` selected.", pxeModuleName, "Please select `Goods Category` value.");
                    comboBox03.requestFocus();
                    return false;
                } else {
//                    oTransFollowUp.setFollowUp(10, comboBox03.getValue().toString());
                }
            }
        }
        return true;
    }

}
