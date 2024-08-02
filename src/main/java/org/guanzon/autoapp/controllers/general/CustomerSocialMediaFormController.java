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
//
///**
// * FXML Controller class
// *
// * @author Auto Group Programmers
// */

public class CustomerSocialMediaFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTransSocMed;
    private final String pxeCustomerModuleName = "Customer Social Media";
    private final String pxeRefModuleName = "Referral Agent Social Media";
    private String psFormStateName;
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //CLIENT Social Media
        txtField03Socm.setOnKeyPressed(this::txtField_KeyPressed); // SocMed Account
        comboBox04Socm.setItems(cSocType); // SocMed Type
        initButtons();
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
        //Validate Before adding to tables
        if (txtField03Socm.getText().isEmpty() || txtField03Socm.getText().trim().equals("")) {
            showWarning(psFormStateName, "Mobile Warning", "Invalid Account. Insert to table Aborted!");
            return false;
        }
        if (!radiobtn05SocY.isSelected() && !radiobtn05SocN.isSelected()) {
            showWarning(psFormStateName, "Mobile Warning", "Please select Account Type.Insert to table Aborted!");
            return false;
        }
        if (comboBox04Socm.getValue().equals("") || comboBox04Socm.getValue() == null) {
            showWarning(psFormStateName, "Mobile Warning", "Please select Social Media Type. Insert to table Aborted!");
            return false;
        }
        oTransSocMed.setSocialMed(pnRow, 3, txtField03Socm.getText());
        oTransSocMed.setSocialMed(pnRow, 4, comboBox04Socm.getSelectionModel().getSelectedIndex());
        if (radiobtn05SocY.isSelected()) {
            oTransSocMed.setSocialMed(pnRow, 5, 1);
        } else {
            oTransSocMed.setSocialMed(pnRow, 5, 0);
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

}
