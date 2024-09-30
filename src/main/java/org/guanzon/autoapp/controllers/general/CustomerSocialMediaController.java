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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.clients.Client;
import org.guanzon.autoapp.interfaces.ScreenInterface;
//
///**
// * FXML Controller class
// *
// * @author Auto Group Programmers
// */

public class CustomerSocialMediaController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTransSocMed;
    private String psFormStateName;
    private final String pxeCustomerModuleName = "Customer Social Media";
    private final String pxeRefModuleName = "Referral Agent Social Media";
    private int pnRow = 0;
    private boolean pbState = true;
    private ObservableList<String> cSocType = FXCollections.observableArrayList("FACEBOOK", "WHATSAPP", "INSTAGRAM", "TIKTOK", "TWITTER");
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private RadioButton radiobtn05SocN, radiobtn05SocY;
    @FXML
    private ToggleGroup soc_active;
    @FXML
    private TextField txtField03Socm;
    @FXML
    private ComboBox comboBox04Socm;
    @FXML
    private Label lblFormTitle;

    public void setObject(Client foObject) {
        oTransSocMed = foObject;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setFormStateName(String fsValue) {
        psFormStateName = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtField03Socm.setOnKeyPressed(this::txtField_KeyPressed); // SocMed Account
        comboBox04Socm.setItems(cSocType); // SocMed Type
        if (!psFormStateName.equals("Referral Agent Information")) {
            lblFormTitle.setText(pxeCustomerModuleName);
        } else {
            lblFormTitle.setText(pxeRefModuleName);
        }
        initButtonsClick();
        initFields();
    }

    private void showWarning(String formStateName, String warningTitle, String message) {
        if (formStateName.equals("Referral Agent Information")) {
            ShowMessageFX.Warning(null, "Referral Agent " + warningTitle, message);
        } else {
            ShowMessageFX.Warning(null, "Customer " + warningTitle, message);
        }
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
                    oTransSocMed.removeSocialMed(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                showWarning(psFormStateName, "Information", "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private boolean settoClass() {
        for (int lnCtr = 0; lnCtr <= oTransSocMed.getSocialMediaList().size() - 1; lnCtr++) {
            if (oTransSocMed.getSocialMed(lnCtr, "cSocialTp") != null) {
                if (oTransSocMed.getSocialMed(lnCtr, "cSocialTp") != null && oTransSocMed.getSocialMed(lnCtr, "sAccountx") != null) {
                    if (String.valueOf(oTransSocMed.getSocialMed(lnCtr, "cSocialTp")).equals(String.valueOf(comboBox04Socm.getSelectionModel().getSelectedIndex()))
                            && String.valueOf(oTransSocMed.getSocialMed(lnCtr, "sAccountx")).equals(txtField03Socm.getText())) {
                        if (lnCtr != pnRow) {
                            showWarning(psFormStateName, "Social Media Warning", "This social media account: " + txtField03Socm.getText() + " already exists.");
                            return false;
                        }
                    }
                }
            }
        }
        if (txtField03Socm.getText().isEmpty() || txtField03Socm.getText().trim().equals("")) {
            showWarning(psFormStateName, "Social Media Warning", "Invalid Account. Insert to table Aborted!");
            return false;
        }
        if (!radiobtn05SocY.isSelected() && !radiobtn05SocN.isSelected()) {
            showWarning(psFormStateName, "Social Media Warning", "Please select Account Type.Insert to table Aborted!");
            return false;
        }
        if (comboBox04Socm.getValue().equals("") || comboBox04Socm.getValue() == null) {
            showWarning(psFormStateName, "Social Media Warning", "Please select Social Media Type. Insert to table Aborted!");
            return false;
        }
        oTransSocMed.setSocialMed(pnRow,
                "sAccountx", txtField03Socm.getText());
        oTransSocMed.setSocialMed(pnRow, "cSocialTp", comboBox04Socm.getSelectionModel().getSelectedIndex());
        if (radiobtn05SocY.isSelected()) {
            oTransSocMed.setSocialMed(pnRow, "cRecdStat", 1);
        } else {
            oTransSocMed.setSocialMed(pnRow, "cRecdStat", 0);
        }
        return true;
    }

    private void loadSocialFields() {
        txtField03Socm.setText((String) oTransSocMed.getSocialMed(pnRow, "sAccountx"));
        comboBox04Socm.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransSocMed.getSocialMed(pnRow, "cSocialTp"))));
        if (oTransSocMed.getSocialMed(pnRow, "cRecdStat").toString().equals("1")) {
            radiobtn05SocY.setSelected(true);
            radiobtn05SocN.setSelected(false);
        } else {
            radiobtn05SocY.setSelected(false);
            radiobtn05SocN.setSelected(true);
        }
    }

    private void initFields() {
        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            loadSocialFields();
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

}
