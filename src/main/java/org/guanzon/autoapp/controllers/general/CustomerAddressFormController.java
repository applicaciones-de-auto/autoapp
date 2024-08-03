///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package org.guanzon.autoapp.controllers.general;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.auto.main.clients.Client;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 10-23-2023
 */
public class CustomerAddressFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTransAddress;
    private final String pxeCustomerModuleName = "Customer Address";
    private final String pxeRefModuleName = "Referral Agent Address";
    private int pnRow = 0;
    private boolean pbState = true;
    private String psClientID, psOrigProv, psOrigTown, psOrigBrgy, psFormStateName = "";
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private TextField txtField03Addr, txtField04Addr, txtField05Addr, txtField06Addr, txtField07Addr, txtField23Addr;
    @FXML
    private RadioButton radiobtn18AddY;
    @FXML
    private ToggleGroup add_active;
    @FXML
    private RadioButton radiobtn18AddN;
    @FXML
    private CheckBox checkBox14Addr, checkBox17Addr, checkBox12Addr, checkBox13Addr;
    @FXML
    private TextArea textArea11Addr;
    @FXML
    private Label lblFormTitle;

    public void setObject(Client foObject) {
        oTransAddress = foObject;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setOrigProv(String fsValue) {
        psOrigProv = fsValue;
    }

    public void setOrigTown(String fsValue) {
        psOrigTown = fsValue;
    }

    public void setOrigBrgy(String fsValue) {
        psOrigBrgy = fsValue;
    }

    public void setClientID(String fsValue) {
        psClientID = fsValue;
    }

    public void setFormStateName(String fsValue) {
        psFormStateName = fsValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputTextUtil.addTextLimiter(txtField03Addr, 14); //HOUSE NO
        Pattern loPattern = Pattern.compile("[0-9]*");
        txtField03Addr.setTextFormatter(new InputTextFormatterUtil(loPattern)); //House No
        txtField07Addr.setTextFormatter(new InputTextFormatterUtil(loPattern)); //Zip code
        if (!psFormStateName.equals("Referral Agent Information")) {
            lblFormTitle.setText(pxeCustomerModuleName);
        } else {
            lblFormTitle.setText(pxeRefModuleName);
        }
        initCapitalizationFields();
        initTextKeyPressed();
        initButtons();
        initCmboxFieldAction();
        initTextFieldListener();

        if (pbState) {
            int lnSize = oTransAddress.getAddressList().size() - 1;
            if (lnSize == 0) {
                checkBox14Addr.setSelected(true);
            }
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            loadFields();
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    private void showWarning(String formStateName, String warningTitle, String message) {
        if (formStateName.equals("Referral Agent Information")) {
            ShowMessageFX.Warning(null, "Referral Agent " + warningTitle, message);
        } else {
            ShowMessageFX.Warning(null, "Customer " + warningTitle, message);
        }
    }

    private void initCapitalizationFields() {
        List<TextField> poTxtFieldList = Arrays.asList(txtField03Addr, txtField04Addr, txtField05Addr, txtField06Addr, txtField07Addr, txtField23Addr);
        poTxtFieldList.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        InputTextUtil.setCapsLockBehavior(textArea11Addr);
    }

    private void initTextKeyPressed() {
        txtField03Addr.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04Addr.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05Addr.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06Addr.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07Addr.setOnKeyPressed(this::txtField_KeyPressed);
        txtField23Addr.setOnKeyPressed(this::txtField_KeyPressed);
        textArea11Addr.setOnKeyPressed(this::txtArea_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField23Addr":  //Search by Province Address
                    loJSON = oTransAddress.searchProvince(lsTxtField.getText(), pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField23Addr.setText((String) oTransAddress.getAddress(pnRow, "sProvName"));
                        txtField05Addr.setDisable(false);
                    } else {
                        showWarning(psFormStateName, "Address Warning", (String) loJSON.get("message"));
                        txtField23Addr.clear(); // Province
                        txtField05Addr.setDisable(true);
                        txtField23Addr.focusedProperty();
                        return;
                    }
                    txtField06Addr.clear(); // Brgy
                    txtField05Addr.clear(); // Town
                    txtField07Addr.clear(); //Zip code
                    break;
                case "txtField05Addr":  //Search by Town Address
                    loJSON = oTransAddress.searchTown(lsTxtField.getText(), pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField05Addr.setText((String) oTransAddress.getAddress(pnRow, "sTownName"));
                        txtField07Addr.setText((String) oTransAddress.getAddress(pnRow, "sZippCode"));
                        txtField06Addr.setDisable(false);
                    } else {
                        showWarning(psFormStateName, "Address Warning", (String) loJSON.get("message"));
                        txtField05Addr.clear(); // Town
                        txtField07Addr.clear(); //Zip code
                        txtField06Addr.setDisable(true);
                        txtField05Addr.focusedProperty();
                        return;
                    }
                    txtField06Addr.clear(); // Brgy
                    break;
                case "txtField06Addr":  //Search by Brgy Address
                    loJSON = oTransAddress.searchBarangay(lsTxtField.getText(), pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField06Addr.setText((String) oTransAddress.getAddress(pnRow, "sBrgyName"));
                    } else {
                        txtField06Addr.clear(); // Brgy
                        txtField06Addr.focusedProperty();
                        return;
                    }
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
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

    private void initButtons() {
        btnAdd.setOnAction(this::handleButtonAction);
        btnEdit.setOnAction(this::handleButtonAction);
        btnClose.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnEdit":
            case "btnAdd":
                String lsConcatAddresses = "";
                String lsAddress = txtField03Addr.getText() + txtField04Addr.getText() + txtField06Addr.getText() + txtField05Addr.getText() + txtField23Addr.getText();
                lsAddress = lsAddress.replace(" ", "");
                //checking if it has similarities address.
                for (int lnCtr = 0; lnCtr <= oTransAddress.getAddressList().size() - 1; lnCtr++) {
                    lsConcatAddresses = (String) oTransAddress.getAddress(lnCtr, "sHouseNox") + (String) oTransAddress.getAddress(lnCtr, "sAddressx") + (String) oTransAddress.getAddress(lnCtr, "sBrgyName") + (String) oTransAddress.getAddress(lnCtr, "sTownName") + (String) oTransAddress.getAddress(lnCtr, "sProvName");
                    lsConcatAddresses = lsConcatAddresses.replace(" ", "").toUpperCase();
                    if (lsConcatAddresses.equals(lsAddress)) {
                        if (lnCtr != pnRow) {
                            lnCtr += 1;
                            showWarning(psFormStateName, "Information", "Address Information already exist in referral agent address at row " + lnCtr + ".  ");
                            return;
                        }
                    }
                }
                String lsAddrsID = String.valueOf((String) oTransAddress.getAddress(pnRow, "sAddrssID"));
                if ((String) oTransAddress.getAddress(pnRow, "sAddrssID") != null) {
                    loJSON = oTransAddress.checkClientAddress(lsAddrsID, lsAddress, psClientID, pnRow);
                    if ("confirm".equals((String) loJSON.get("result"))) {
                        if (psFormStateName.equals("Referral Agent Information")) {
                            if (!ShowMessageFX.YesNo(null, "Referral Agent Confirmation", (String) loJSON.get("message"))) {
                                return;
                            }
                        } else {
                            if (!ShowMessageFX.YesNo(null, "Customer Address Confirmation", (String) loJSON.get("message"))) {
                                return;
                            }
                        }
                    }
                } else {
                    loJSON = oTransAddress.checkClientAddress(lsAddress, pnRow, true);
                    if ("confirm".equals((String) loJSON.get("result"))) {
                        if (psFormStateName.equals("Referral Agent Information")) {
                            if (!ShowMessageFX.YesNo(null, "Referral Agent Confirmation", (String) loJSON.get("message"))) {
                                return;
                            }
                        } else {
                            if (!ShowMessageFX.YesNo(null, "Customer Address Confirmation", (String) loJSON.get("message"))) {
                                return;
                            }
                        }
                    }
                }
                if (settoClass()) {
                    if ((String) oTransAddress.getAddress(pnRow, "sAddrssID") != null) {
                        oTransAddress.updateAddresses(pnRow);
                    } else {
                        oTransAddress.checkClientAddress(lsAddress, pnRow, false);
                    }
                    CommonUtils.closeStage(btnClose);
                } else {
                    return;
                }
                break;
            case "btnClose":
                JSONObject lsJSON;
                if (pbState) {
                    oTransAddress.removeAddress(pnRow);
                } else {
                    lsJSON = oTransAddress.searchProvince(psOrigProv, pnRow, true);
                    if ("success".equals((String) lsJSON.get("result"))) {
                        lsJSON = oTransAddress.searchTown(psOrigTown, pnRow, true);
                        if ("success".equals((String) lsJSON.get("result"))) {
                            lsJSON = oTransAddress.searchBarangay(psOrigBrgy, pnRow, true);
                            if ("success".equals((String) lsJSON.get("result"))) {
                            }
                        }
                    }
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                showWarning(psFormStateName, "Address Warning", "Button with name " + lsButton + " not registered.");
                break;

        }
    }

    //Set CheckBox Action
    private void initCmboxFieldAction() {
        checkBox17Addr.setOnAction(this::comboBoxAction);
        checkBox13Addr.setOnAction(this::comboBoxAction);
        checkBox12Addr.setOnAction(this::comboBoxAction);
    }

    //Set CheckBox Action
    private void comboBoxAction(ActionEvent event) {
        String lsCbSel = ((CheckBox) event.getSource()).getId();
        switch (lsCbSel) {
            /*client_address*/
            case "checkBox17Addr": // Current
                if (checkBox17Addr.isSelected()) {
                    checkBox12Addr.setSelected(false);
                }
                break;
            case "checkBox13Addr": // Provincial
                if (checkBox13Addr.isSelected()) {
                    checkBox12Addr.setSelected(false);
                }
                break;
            case "checkBox12Addr": // Office
                if (checkBox12Addr.isSelected()) {
                    checkBox17Addr.setSelected(false);
                    checkBox13Addr.setSelected(false);
                }
                break;
        }
    }

    private void initTextFieldListener() {
        txtField23Addr.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oTransAddress != null && pnRow >= 0) {
                if (newValue.isEmpty() || newValue == null) {
                    oTransAddress.setAddress(pnRow, 25, ""); //province id
                    oTransAddress.setAddress(pnRow, 24, ""); //province
                    oTransAddress.setAddress(pnRow, 16, ""); //town id
                    oTransAddress.setAddress(pnRow, 23, ""); //town
                    oTransAddress.setAddress(pnRow, 17, ""); //zip
                    oTransAddress.setAddress(pnRow, 6, ""); //brgy id
                    oTransAddress.setAddress(pnRow, 22, ""); //brgy
                    txtField23Addr.clear(); // Province
                    txtField05Addr.clear(); // Town
                    txtField07Addr.clear(); //Zip code
                    txtField06Addr.clear(); // Brgy
                    txtField05Addr.setDisable(true);// Town
                    txtField06Addr.setDisable(true);// Brgy
                }
            } else {
                System.err.println("oTransAddress is null or pnRow is invalid.");
            }
        }
        );

        txtField05Addr.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oTransAddress != null && pnRow >= 0) {
                if (newValue.isEmpty() || newValue == null) {
                    oTransAddress.setAddress(pnRow, 16, ""); //town id
                    oTransAddress.setAddress(pnRow, 23, ""); //town
                    oTransAddress.setAddress(pnRow, 17, ""); //zip
                    oTransAddress.setAddress(pnRow, 6, ""); //brgy id
                    oTransAddress.setAddress(pnRow, 22, ""); //brgy
                    txtField05Addr.clear(); // Town
                    txtField07Addr.clear(); //Zip code
                    txtField06Addr.clear(); // Brgy
                    txtField06Addr.setDisable(true);// Brgy

                } else {
                    System.err.println("oTransAddress is null or pnRow is invalid.");
                }
            }
        }
        );

        txtField06Addr.textProperty().addListener((observable, oldValue, newValue) -> {
            // Ensure oTransAddress and pnRow are not null
            if (oTransAddress != null && pnRow >= 0) {
                // Check if the new value is empty or null
                if (newValue == null || newValue.isEmpty()) {
                    oTransAddress.setAddress(pnRow, 6, ""); // brgy id
                    oTransAddress.setAddress(pnRow, 22, ""); // brgy
                }
            } else {
                System.err.println("oTransAddress is null or pnRow is invalid.");
            }
        });

    }

    private boolean settoClass() {
        for (int lnCtr = 0; lnCtr <= oTransAddress.getAddressList().size() - 1; lnCtr++) {
            if (oTransAddress.getAddress(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
                if (checkBox14Addr.isSelected()) {
                    showWarning(psFormStateName, "Address Warning", "Please note that you cannot add more than 1 primary address.");
                    return false;
                }
            }
        }
        if (checkBox14Addr.isSelected() && radiobtn18AddN.isSelected()) {
            showWarning(psFormStateName, "Address Warning", "Please note that you cannot set primary address that is inactive.");
            return false;
        }
        //Validate Before adding to tables
        if ((txtField07Addr.getText().isEmpty() || txtField05Addr.getText().isEmpty() || txtField06Addr.getText().isEmpty())
                || txtField07Addr.getText().trim().equals("") || txtField05Addr.getText().trim().equals("") || txtField06Addr.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), "Invalid Address. Insert to table Aborted!", "Warning", null);
            return false;

        }
        if (!radiobtn18AddY.isSelected() && !radiobtn18AddN.isSelected()) {
            showWarning(psFormStateName, "Address Warning", "Please select Address Status. Insert to table Aborted!");
            return false;
        }
        if (!checkBox12Addr.isSelected() && !checkBox13Addr.isSelected()
                && !checkBox14Addr.isSelected() && !checkBox17Addr.isSelected()) {
            showWarning(psFormStateName, "Address Warning", "Please select Address Type. Insert to table Aborted!");
            return false;
        }
        String sHouseNox = "";
        if (!txtField03Addr.getText().isEmpty()) {
            sHouseNox = txtField03Addr.getText();
        }
        if (((String) oTransAddress.getAddress(pnRow, "sTownIDxx")).isEmpty()) {
            showWarning(psFormStateName, "Address Warning", "Please select Town. Insert to table Aborted!");
            return false;
        }
        if (((String) oTransAddress.getAddress(pnRow, "sBrgyIDxx")).isEmpty()) {
            showWarning(psFormStateName, "Address Warning", "Please select Barangay. Insert to table Aborted!");
            return false;

        }
        oTransAddress.setAddress(pnRow, 14, sHouseNox);
        oTransAddress.setAddress(pnRow, 15, txtField04Addr.getText());
        oTransAddress.setAddress(pnRow, 17, txtField07Addr.getText());
        oTransAddress.setAddress(pnRow, 21, textArea11Addr.getText());
        if (checkBox12Addr.isSelected()) {
            oTransAddress.setAddress(pnRow, 3, 1);
        } else {
            oTransAddress.setAddress(pnRow, 3, 0);
        }
        if (checkBox13Addr.isSelected()) {
            oTransAddress.setAddress(pnRow, 4, 1);
        } else {
            oTransAddress.setAddress(pnRow, 4, 0);
        }
        if (checkBox14Addr.isSelected()) {
            oTransAddress.setAddress(pnRow, 5, 1);
        } else {
            oTransAddress.setAddress(pnRow, 5, 0);
        }
        if (checkBox17Addr.isSelected()) {
            oTransAddress.setAddress(pnRow, 8, 1);
        } else {
            oTransAddress.setAddress(pnRow, 8, 0);
        }
        if (radiobtn18AddY.isSelected()) {
            oTransAddress.setAddress(pnRow, 9, 1);
        } else {
            oTransAddress.setAddress(pnRow, 9, 0);
        }

        return true;
    }

    private void loadFields() {
        txtField03Addr.setText((String) oTransAddress.getAddress(pnRow, "sHouseNox"));
        txtField04Addr.setText((String) oTransAddress.getAddress(pnRow, "sAddressx"));
        txtField23Addr.setText((String) oTransAddress.getAddress(pnRow, "sProvName"));
        txtField05Addr.setText((String) oTransAddress.getAddress(pnRow, "sTownName"));
        txtField06Addr.setText((String) oTransAddress.getAddress(pnRow, "sBrgyName"));
        txtField07Addr.setText((String) oTransAddress.getAddress(pnRow, "sZippCode"));
        textArea11Addr.setText((String) oTransAddress.getAddress(pnRow, "sRemarksx"));
        if (!((String) oTransAddress.getAddress(pnRow, "sProvIDxx")).isEmpty()) {
            txtField05Addr.setDisable(false);
        } else {
            txtField05Addr.setDisable(true);
        }
        if (!((String) oTransAddress.getAddress(pnRow, "sTownIDxx")).isEmpty()) {
            txtField06Addr.setDisable(false);
        } else {
            txtField06Addr.setDisable(true);
        }
        if (oTransAddress.getAddress(pnRow, "cRecdStat").toString().equals("1")) {
            radiobtn18AddY.setSelected(true);
            radiobtn18AddN.setSelected(false);
        } else {
            radiobtn18AddY.setSelected(false);
            radiobtn18AddN.setSelected(true);
        }
        if (oTransAddress.getAddress(pnRow, "cOfficexx").toString().equals("1")) {
            checkBox12Addr.setSelected(true);
        } else {
            checkBox12Addr.setSelected(false);
        }
        if (oTransAddress.getAddress(pnRow, "cProvince").toString().equals("1")) {
            checkBox13Addr.setSelected(true);
        } else {
            checkBox13Addr.setSelected(false);
        }
        if (oTransAddress.getAddress(pnRow, "cPrimaryx").toString().equals("1")) {
            checkBox14Addr.setSelected(true);
        } else {
            checkBox14Addr.setSelected(false);
        }
        if (oTransAddress.getAddress(pnRow, "cCurrentx").toString().equals("1")) {
            checkBox17Addr.setSelected(true);
        } else {
            checkBox17Addr.setSelected(false);
        }
        if ((String) oTransAddress.getAddress(pnRow, 1) != null) {
            txtField23Addr.setDisable(true);
            txtField05Addr.setDisable(true);
            txtField06Addr.setDisable(true);
        }
    }

}
