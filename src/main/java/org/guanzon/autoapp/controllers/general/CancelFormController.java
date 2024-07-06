/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;

/**
 * FXML Controller class
 *
 * @author Arsiela Date Created: 05-20-2023
 */
public class CancelFormController implements Initializable {

    private GRider oApp;
////    private MasterCallback oListener;
//    private CancellationMaster oTrans;
    private boolean pbState;
    private String psSourceNox;
    private String psTransNo;
    private String psSourceCD;

    private final String pxeModuleName = "Cancellation / Deactivation Remarks";
    @FXML
    private Button btnCancel, btnDCancel;
    @FXML
    private Label lblFormNo;
    @FXML
    private TextArea textArea01;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setsSourceNox(String fsValue) {
        psSourceNox = fsValue;
    }

    public void setsSourceCD(String fsValue) {
        psSourceCD = fsValue;
    }

    public void setTransNo(String fsValue) {
        psTransNo = fsValue;
    }

    public boolean setState() {
        return pbState;
    }

    private Stage getStage() {
        return (Stage) btnCancel.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

//        oTrans = new CancellationMaster(oApp, oApp.getBranchCode(), true); //Initialize ClientMaster
////        oTrans.setCallback(oListener);
//        oTrans.setWithUI(true);
        lblFormNo.setText(psTransNo);
        InputTextUtil.setCapsLockBehavior(textArea01);

        Pattern loPattern = Pattern.compile("^[a-zA-Z0-9 ]*");
        textArea01.setTextFormatter(new InputTextFormatterUtil(loPattern));

        btnCancel.setOnAction(this::handleButtonAction);
        btnDCancel.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel/deactivate?")) {
                } else {
                    return;
                }
                if (textArea01.getText().length() < 20) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter at least 20 characters.");
                    textArea01.requestFocus();
                    return;
                }
//                if (oTrans.CancelForm(psTransNo, textArea01.getText(), psSourceCD, psSourceNox)) {
//                    pbState = true;
//                } else {
//                    return;
//                }
                CommonUtils.closeStage(btnCancel);
                break;
            case "btnDCancel":
                pbState = false;
                CommonUtils.closeStage(btnDCancel);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
}
