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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.clients.Client;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class CustomerContactFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTransMobile;
    private final String pxeModuleName = "Customer Mobile";
    private int pnRow = 0;
    private boolean pbState = true;
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    ObservableList<String> cOwnCont = FXCollections.observableArrayList("PERSONAL", "OFFICE", "OTHERS");
    ObservableList<String> cTypCont = FXCollections.observableArrayList("MOBILE", "TELEPHONE", "FAX");
    @FXML
    private ComboBox comboBox05Cont, comboBox04Cont;
    @FXML
    private TextField txtField03Cont;
    @FXML
    private ToggleGroup con_active, con_prim;
    @FXML
    private RadioButton radiobtn14CntY, radiobtn14CntN, radiobtn11CntY, radiobtn11CntN;
    @FXML
    private TextArea textArea13Cont;

    public void setObject(Client foObject) {
        oTransMobile = foObject;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
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
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {

        Pattern loPattern = Pattern.compile("[0-9]*");
        txtField03Cont.setTextFormatter(new InputTextFormatterUtil(loPattern)); //Mobile No
        initComboBoxItems();

        // Set the action handler for the combo box
        comboBox04Cont.setOnAction(event -> {
            // Clear the text field when an action is performed on the combo box
            if (txtField03Cont != null) {
                txtField03Cont.clear();
            }
            Integer selectedIndex = comboBox04Cont.getSelectionModel().getSelectedIndex();

            // Set the text limiter based on the selected index
            if (selectedIndex != null) {
                switch (selectedIndex) {
                    case 0:
                        if (txtField03Cont != null) {
                            InputTextUtil.addTextLimiter(txtField03Cont, 11); // CONTACT NO
                        }
                        break;
                    case 1:
                    case 2:
                        if (txtField03Cont != null) {
                            InputTextUtil.addTextLimiter(txtField03Cont, 10); // TELE & FAX NO
                        }
                        break;
                }
            }
        });

        initCapitalizationFields();
        initTextKeyPressed();
        initButtons();
        if (pbState) {
            int lnSize = oTransMobile.getMobileList().size() - 1;
            if (lnSize == 0) {
                radiobtn11CntY.setSelected(true);
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

    @SuppressWarnings("unchecked")
    private void initComboBoxItems() {
        comboBox05Cont.setItems(cOwnCont); // Contact Ownership
        comboBox04Cont.setItems(cTypCont); // Mobile Type
    }

    private void initCapitalizationFields() {
        InputTextUtil.setCapsLockBehavior(txtField03Cont);
        InputTextUtil.setCapsLockBehavior(textArea13Cont);
    }

    private void initTextKeyPressed() {
        txtField03Cont.setOnKeyPressed(this::txtField_KeyPressed);  //Mobile Number
        textArea13Cont.setOnKeyPressed(this::txtArea_KeyPressed); // Contact Remarks
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEdit":
            case "btnAdd":
                if (settoClass()) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnClose":
                if (pbState) {
                    oTransMobile.removeMobile(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;

        }
    }

    private boolean settoClass() {
        for (int lnCtr = 0; lnCtr <= oTransMobile.getMobileList().size() - 1; lnCtr++) {
            if (oTransMobile.getMobile(lnCtr, "cPrimaryx").toString().equals("1") && (lnCtr != pnRow)) {
                if (radiobtn11CntY.isSelected()) {
                    ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Please note that you cannot add more than 1 primary contact number.");
                    return false;
                }
            }
        }
        if (radiobtn11CntY.isSelected() && radiobtn14CntN.isSelected()) {
            ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Please note that you cannot set primary contact that is inactive.");
            return false;
        }
        if (comboBox04Cont.getSelectionModel().getSelectedIndex() == 0) {
            if (CommonUtils.classifyNetwork(txtField03Cont.getText()).isEmpty()) {
                ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Prefix not registered " + txtField03Cont.getText());
                return false;
            }
        }
        //Validate Before adding to tables
        if (txtField03Cont.getText().isEmpty() || txtField03Cont.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Invalid Mobile. Insert to table Aborted!");
            return false;
        }
        if (!radiobtn11CntY.isSelected() && !radiobtn11CntN.isSelected()) {
            ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Please select Mobile Type. Insert to table Aborted!");
            return false;
        }
        if (!radiobtn14CntY.isSelected() && !radiobtn14CntN.isSelected()) {
            ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Please select Mobile Status. Insert to table Aborted!");
            return false;
        }
        if (comboBox05Cont.getValue().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Please select Contact Ownership. Insert to table Aborted!");
            return false;
        }
        if (comboBox04Cont.getValue().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Customer Mobile Warning", "Please select Mobile Type. Insert to table Aborted!");
            return false;
        }
        oTransMobile.setMobile(pnRow, 3, txtField03Cont.getText());
        oTransMobile.setMobile(pnRow, 4, comboBox04Cont.getSelectionModel().getSelectedIndex());
        oTransMobile.setMobile(pnRow, 5, comboBox05Cont.getSelectionModel().getSelectedIndex());
        oTransMobile.setMobile(pnRow, 13, textArea13Cont.getText());
        if (radiobtn11CntY.isSelected()) {
            oTransMobile.setMobile(pnRow, 11, 1);
        } else {
            oTransMobile.setMobile(pnRow, 11, 0);
        }
        if (radiobtn14CntY.isSelected()) {
            oTransMobile.setMobile(pnRow, 14, 1);
        } else {
            oTransMobile.setMobile(pnRow, 14, 0);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void loadFields() {
        txtField03Cont.setText((String) oTransMobile.getMobile(pnRow, "sMobileNo"));
        textArea13Cont.setText((String) oTransMobile.getMobile(pnRow, "sRemarksx"));
        comboBox05Cont.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransMobile.getMobile(pnRow, "cOwnerxxx"))));
        comboBox04Cont.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransMobile.getMobile(pnRow, "cMobileTp"))));
        if (oTransMobile.getMobile(pnRow, "cRecdStat").toString().equals("1")) {
            radiobtn14CntY.setSelected(true);
            radiobtn14CntN.setSelected(false);
        } else {
            radiobtn14CntY.setSelected(false);
            radiobtn14CntN.setSelected(true);
        }
        if (oTransMobile.getMobile(pnRow, "cPrimaryx").toString().equals("1")) {
            radiobtn11CntY.setSelected(true);
            radiobtn11CntN.setSelected(false);
        } else {
            radiobtn11CntY.setSelected(false);
            radiobtn11CntN.setSelected(true);
        }
    }

}
