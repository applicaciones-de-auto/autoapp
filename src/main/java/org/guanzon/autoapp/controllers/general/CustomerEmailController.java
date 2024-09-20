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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.clients.Client;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class CustomerEmailController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTransEmail;
    private final String pxeCustomerModuleName = "Customer Email";
    private final String pxeRefModuleName = "Referral Agent Email";
    private String psFormStateName;
    private int pnRow = 0;
    private boolean pbState = true;
    private ObservableList<String> cOwnEmAd = FXCollections.observableArrayList("PERSONAL", "OFFICE", "OTHERS");
    @FXML
    private Button btnEdit, btnClose, btnAdd;
    @FXML
    private ComboBox comboBox04EmAd;
    @FXML
    private RadioButton radiobtn06EmaY, radiobtn06EmaN, radiobtn05EmaY, radiobtn05EmaN;
    @FXML
    private ToggleGroup eml_active, eml_prim;
    @FXML
    private TextField txtField03EmAd;
    @FXML
    private Label lblFormTitle;

    public void setObject(Client foObject) {
        oTransEmail = foObject;
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

    public void setFormStateName(String fsValue) {
        psFormStateName = fsValue;
    }

    /**
     * Initializes the controller class.
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        comboBox04EmAd.setItems(cOwnEmAd); // Email Ownership
        //CLIENT Email
        txtField03EmAd.setOnKeyPressed(this::txtField_KeyPressed); // Email Address
        initButtonsClick();
        if (!psFormStateName.equals("Referral Agent Information")) {
            lblFormTitle.setText(pxeCustomerModuleName);
        } else {
            lblFormTitle.setText(pxeRefModuleName);
        }
        initFields();
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

    private void showWarning(String formStateName, String warningTitle, String message) {
        if (formStateName.equals("Referral Agent Information")) {
            ShowMessageFX.Warning(null, "Referral Agent " + warningTitle, message);
        } else {
            ShowMessageFX.Warning(null, "Customer " + warningTitle, message);
        }
    }

    private void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEdit":
            case "btnAdd":
                if (settoClass()) {
                    if (lsButton.equals("btnEdit")) {
                        CommonUtils.closeStage(btnEdit);
                    } else {
                        CommonUtils.closeStage(btnAdd);
                    }
                }
                break;
            case "btnClose":
                if (pbState) {
                    oTransEmail.removeEmail(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                showWarning(psFormStateName, "Information", "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private boolean settoClass() {
        for (int lnCtr = 0; lnCtr <= oTransEmail.getEmailList().size() - 1; lnCtr++) {
            if (String.valueOf(oTransEmail.getEmail(lnCtr, "cPrimaryx")).equals("1") && (lnCtr != pnRow)) {
                if (radiobtn05EmaY.isSelected()) {
                    showWarning(psFormStateName, "Email Warning", "Please note that you cannot add more than 1 primary email.");
                    return false;
                }
            }
            if (oTransEmail.getEmail(lnCtr, "sEmailAdd") != null) {
                if (String.valueOf(oTransEmail.getEmail(lnCtr, "sEmailAdd")).equals(txtField03EmAd.getText()) && (lnCtr != pnRow)) {
                    showWarning(psFormStateName, "Email Warning", "This email address: " + txtField03EmAd.getText() + " already exists.");
                    return false;
                }
            }

        }
        //User cannot set primary that is inactive
        if (radiobtn05EmaY.isSelected() && radiobtn06EmaN.isSelected()) {
            showWarning(psFormStateName, "Email Warning", "Please note that you cannot set primary email that is inactive.");
            return false;
        }
        //Validate Before adding to tables
        if (txtField03EmAd.getText().isEmpty() || txtField03EmAd.getText().trim().equals("")) {
            showWarning(psFormStateName, "Email Warning", "Invalid Email. Insert to table Aborted!");
            return false;
        }
        if (!CommonUtils.isValidEmail(txtField03EmAd.getText())) {
            showWarning(psFormStateName, "Email Warning", "Invalid Email. Insert to table Aborted!");
            return false;
        }
        if (!radiobtn05EmaY.isSelected() && !radiobtn05EmaN.isSelected()) {
            showWarning(psFormStateName, "Email Warning", "Please select Email Type.Insert to table Aborted!");
            return false;
        }
        if (!radiobtn06EmaY.isSelected() && !radiobtn06EmaN.isSelected()) {
            showWarning(psFormStateName, "Email Warning", "Please select Email Status. Insert to table Aborted!");
            return false;
        }
        if (comboBox04EmAd.getValue().equals("")) {
            showWarning(psFormStateName, "Email Warning", "Please select Email Ownership. Insert to table Aborted!");
            return false;
        }
        oTransEmail.setEmail(pnRow, "sEmailAdd", txtField03EmAd.getText());
        oTransEmail.setEmail(pnRow, "cOwnerxxx", comboBox04EmAd.getSelectionModel().getSelectedIndex());
        if (radiobtn05EmaY.isSelected()) {
            oTransEmail.setEmail(pnRow, "cPrimaryx", 1);
        } else {
            oTransEmail.setEmail(pnRow, "cPrimaryx", 0);
        }
        if (radiobtn06EmaY.isSelected()) {
            oTransEmail.setEmail(pnRow, "cRecdStat", 1);
        } else {
            oTransEmail.setEmail(pnRow, "cRecdStat", 0);
        }
        return true;
    }

    private void loadFields() {
        txtField03EmAd.setText((String) oTransEmail.getEmail(pnRow, "sEmailAdd"));
        comboBox04EmAd.getSelectionModel().select(Integer.parseInt(String.valueOf((oTransEmail.getEmail(pnRow, "cOwnerxxx")))));
        if (oTransEmail.getEmail(pnRow, "cRecdStat").toString().equals("1")) {
            radiobtn06EmaY.setSelected(true);
            radiobtn06EmaN.setSelected(false);
        } else {
            radiobtn06EmaY.setSelected(false);
            radiobtn06EmaN.setSelected(true);
        }
        if (oTransEmail.getEmail(pnRow, "cPrimaryx").toString().equals("1")) {
            radiobtn05EmaY.setSelected(true);
            radiobtn05EmaN.setSelected(false);
        } else {
            radiobtn05EmaY.setSelected(false);
            radiobtn05EmaN.setSelected(true);
        }
    }

    private void initFields() {
        if (pbState) {
            int lnSize = oTransEmail.getEmailList().size() - 1;
            if (lnSize == 0) {
                radiobtn05EmaY.setSelected(true);
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
}
