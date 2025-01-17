/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.service.TechnicianLabor;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class TechnicianServiceController implements Initializable, ScreenInterface {

    private GRider oApp;
    private JobOrder oTransTechnician;
    private List<String> existingLaborCode = new ArrayList<>();
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private List<TechnicianLabor> techData = new ArrayList<>();
    private String pxeModuleName = "Technician Service";
    private String psTrans, psOrigDsc = "";
    private int pnRow = 0;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnEdit, btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;

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

    public void setOrigDsc(String fsValue) {
        psOrigDsc = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        initCapitalizationFields();
        initTextKeyPressed();
        initButtonsClick();
        loadTechServiceFields();
        txtField03.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransTechnician.getJOTechModel().getDetailModel(pnRow).setLaborCde("");
                    oTransTechnician.getJOTechModel().getDetailModel(pnRow).setLaborDsc("");
                }
            }
        });
    }

    private void loadTechServiceFields() {
        txtField01.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
        txtField02.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechName());
        txtField03.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getLaborDsc());
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void initButtonsClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnEdit.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                loJSON = oTransTechnician.searchVSPLabor(psOrigDsc, pnRow, true);
                if (!"error".equals((String) loJSON.get("result"))) {
                }
                if (txtField03.getText().trim().isEmpty()) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value in labor description");
                    return;
                }
                CommonUtils.closeStage(btnClose);
                break;
            case "btnEdit":
                if (txtField03.getText().trim().isEmpty()) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value for labor description.");
                } else {
                    CommonUtils.closeStage(btnEdit);
                }
                break;
        }
    }

    private void initTextKeyPressed() {
        txtField03.setOnKeyPressed(event -> txtField_KeyPressed(event));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                case F3:
                    switch (txtFieldID) {
                        case "txtField03":
                            loJSON = oTransTechnician.searchVSPLabor(lsValue, pnRow, false);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField03.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getLaborDsc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
                            break;
                    }
                    event.consume();
                    break;
                default:
                    break;
            }
        }
    }

}
