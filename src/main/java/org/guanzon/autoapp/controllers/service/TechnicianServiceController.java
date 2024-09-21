/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.service;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class TechnicianServiceController implements Initializable, ScreenInterface {

    private GRider oApp;
    private JobOrder oTransTechnician;
    private boolean pbState = true;

    @FXML
    private Button btnEdit, btnAdd, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    private String psTrans = "";
    private int pnRow = 0;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(JobOrder foValue) {
        oTransTechnician = foValue;
    }

    public void setTrans(String fsValue) {
        psTrans = fsValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        initCapitalizationFields();
//        comboBox03.setItems(cChargeType);
//        initTextKeyPressed();
//        initTextFieldFocus();
//        initCmboxFieldAction();
//        initTextPropertyAction();
//        initButtonsClick();
//        initFielPattern();
        loadTechServiceFields();
        initFields();
    }

    private void loadTechServiceFields() {
        txtField01.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
        txtField02.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
        txtField03.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
    }

    private void initFields() {
        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }
}
