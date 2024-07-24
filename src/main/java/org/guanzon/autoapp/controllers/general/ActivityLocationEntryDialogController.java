/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityLocationEntryDialogController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Activity oTransLocation;
    private final String pxeModuleName = "Activity Location";
    private int pnRow = 0;
    private boolean pbState = true;
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05;
    @FXML
    private TextArea textArea06;

    public void setObject(Activity foObject) {
        oTransLocation = foObject;
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
    public void initialize(URL url, ResourceBundle rb) {
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        textArea06.setOnKeyPressed(this::txtArea_KeyPressed);

        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        InputTextUtil.setCapsLockBehavior(textArea06);
        initButtons();
        textProperty();
        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            loadActLocationFields();
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    private void loadActLocationFields() {
        txtField01.setText((String) oTransLocation.getActLocation(pnRow, "sAddressx"));
        txtField02.setText((String) oTransLocation.getActLocation(pnRow, "sProvName"));
        txtField03.setText((String) oTransLocation.getActLocation(pnRow, "sTownName"));
        txtField04.setText((String) oTransLocation.getActLocation(pnRow, "sBrgyName"));
        txtField05.setText((String) oTransLocation.getActLocation(pnRow, "sZippCode"));
        textArea06.setText((String) oTransLocation.getActLocation(pnRow, "sCompnynx"));

    }

    private void textProperty() {
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            oTransLocation.setActLocation(pnRow, "sProvIDxx", "");
                            oTransLocation.setActLocation(pnRow, "sTownIDxx", "");
                            oTransLocation.setActLocation(pnRow, "sBrgyIDxx", "");
                            txtField03.setText("");
                            txtField04.setText("");
                            txtField05.setText("");
                            initFields();
                        }
                    }
                }
                );
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            oTransLocation.setActLocation(pnRow, "sTownIDxx", "");
                            oTransLocation.setActLocation(pnRow, "sBrgyIDxx", "");
                            txtField04.setText("");
                            txtField05.setText("");
                            initFields();
                        }
                    }
                }
                );
        txtField04.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            oTransLocation.setActLocation(pnRow, "sBrgyIDxx", "");
                            initFields();
                        }
                    }
                }
                );
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField02":  //Search by Province Address
                    loJSON = oTransLocation.searchProvince(lsTxtField.getText(), pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField02.setText((String) oTransLocation.getActLocation(pnRow, "sProvName"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField02.setText("");
                        txtField02.focusedProperty();
                        return;
                    }
                    break;
                case "txtField03":  //Search by Town Address
                    loJSON = oTransLocation.searchTown(lsTxtField.getText(), pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField03.setText((String) oTransLocation.getActLocation(pnRow, "sTownName"));
                        txtField05.setText((String) oTransLocation.getActLocation(pnRow, "sZippCode"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField03.setText("");
                        txtField03.focusedProperty();
                        return;
                    }
                    break;
                case "txtField04":  //Search by Brgy Address
                    loJSON = oTransLocation.searchBarangay(lsTxtField.getText(), pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField04.setText((String) oTransLocation.getActLocation(pnRow, "sBrgyName"));
                    } else {
                        txtField04.setText("");
                        txtField04.focusedProperty();
                        return;
                    }
                    break;
            }
            initFields();
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void txtArea_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextArea) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
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
                    oTransLocation.removeActLocation(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private boolean settoClass() {
        //Validate Before adding to tables
        if (txtField01.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Street Name", "Please enter value of Street Name.");
            return false;
        }
        if (txtField02.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Province Name", "Please enter value of Province Name.");
            return false;
        }
        if (txtField03.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Town Name", "Please enter value of Town Name.");
            return false;
        }
        if (txtField04.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Barangay Name", "Please enter value of Barangay Name.");
            return false;
        }
        if (textArea06.getText().trim().equals("")) {
            ShowMessageFX.Warning(getStage(), null, "Establishment", "Please enter value of Establishment.");
            return false;
        }
        oTransLocation.setActLocation(pnRow, 3, txtField01.getText());
        oTransLocation.setActLocation(pnRow, 10, txtField02.getText());
        oTransLocation.setActLocation(pnRow, 8, txtField03.getText());
        oTransLocation.setActLocation(pnRow, 4, txtField04.getText());
        oTransLocation.setActLocation(pnRow, 7, txtField05.getText());
        oTransLocation.setActLocation(pnRow, 6, textArea06.getText());
        return true;
    }

    private void initFields() {
        txtField03.setDisable(txtField02.getText().isEmpty());
        txtField04.setDisable(txtField03.getText().isEmpty());
    }
}
