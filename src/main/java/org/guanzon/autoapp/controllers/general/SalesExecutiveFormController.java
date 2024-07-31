/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Sales_Executive;
import org.guanzon.autoapp.models.general.ModelSalesExecutiveTrans;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class SalesExecutiveFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Sales_Executive oTransSalesExe;
    private final String pxeModuleName = "Sales Executive Information"; //Form Title
    private int pnEditMode;
    private int lnCtr;
    private ObservableList<ModelSalesExecutiveTrans> transData = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd, btnSave, btnBrowse, btnCancel, btnActive, btnDeactivate, btnClose;
    @FXML
    private Label lblNameInfo;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05;
    @FXML
    private TextArea textArea03;
    @FXML
    private Label lblNameInfo1;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TableView<ModelSalesExecutiveTrans> tblTransaction;
    @FXML
    private TableColumn<ModelSalesExecutiveTrans, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09;

    /**
     * Initializes the controller class.
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransSalesExe = new Sales_Executive(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initTextKeyPressed();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        transData.clear();
        initSalesTransaction();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initCmboxFieldAction() {
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransSalesExe.getModel().getModel().setClientID("");
                                txtField02.setText("");
                                textArea03.setText("");
                                txtField04.setText("");
                                txtField05.setText("");
                                transData.clear();
                            }
                        }
                    }
                }
                );
        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransSalesExe.getModel().getModel().setCompnyNm("");
                                txtField01.setText("");
                                textArea03.setText("");
                                txtField04.setText("");
                                txtField05.setText("");
                                transData.clear();
                            }
                        }
                    }
                }
                );
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnBrowse, btnCancel,
                btnClose, btnActive, btnDeactivate);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void txtFieldNullHandleException(TextField txtField, String fsColName) {
        try {
            if (txtField.getText().trim().equals("")) {
                ShowMessageFX.Warning(null, pxeModuleName, "Please enter value " + fsColName + " information.");
                txtField.setText("");
                return;
            }
        } catch (NullPointerException e) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value " + fsColName + " information.");
            return;
        }
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                transData.clear();
                oTransSalesExe = new Sales_Executive(oApp, false, oApp.getBranchCode());
                loJSON = oTransSalesExe.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadSalesExeField();
                    loadSalesTransaction();
                    pnEditMode = oTransSalesExe.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Sales Executive Information Saving....", "Are you sure, do you want to save?")) {
                    txtFieldNullHandleException(txtField01, "Employee ID");
                    txtFieldNullHandleException(txtField02, "Employee Name");
                } else {
                    return;
                }
                loJSON = oTransSalesExe.saveRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    loJSON = oTransSalesExe.openRecord(oTransSalesExe.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadSalesExeField();
                        loadSalesTransaction();
                        pnEditMode = EditMode.READY;
                        initFields(pnEditMode);

                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;

            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    transData.clear();
                    oTransSalesExe = new Sales_Executive(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Sales Executive Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransSalesExe.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadSalesExeField();
                    loadSalesTransaction();
                    pnEditMode = EditMode.READY;
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Sales Executive Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransSalesExe.getModel().getModel().getClientID();
                    loJSON = oTransSalesExe.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Sales Executive Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransSalesExe.openRecord(oTransSalesExe.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadSalesExeField();
                        pnEditMode = pnEditMode = EditMode.READY;;
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransSalesExe.getModel().getModel().getClientID();
                    loJSON = oTransSalesExe.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Sales Executive Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransSalesExe.openRecord(oTransSalesExe.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadSalesExeField();
                        pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        InputTextUtil.setCapsLockBehavior(textArea03);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04, txtField05);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        textArea03.setOnKeyPressed(this::textArea_KeyPressed);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            TextField lsTxtField = (TextField) event.getSource();
            String txtFieldID = ((TextField) event.getSource()).getId();
            String lsValue = "";
            if (lsTxtField.getText() == null) {
                lsValue = "";
            } else {
                lsValue = lsTxtField.getText();
            }
            JSONObject loJSON = new JSONObject();
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField01":
                        loJSON = oTransSalesExe.searchEmployee(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadSalesExeField();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField01.setText("");
                            txtField01.requestFocus();
                            return;
                        }
                        break;
                    case "txtField02":
                        loJSON = oTransSalesExe.searchEmployee(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadSalesExeField();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
                            txtField02.requestFocus();
                            return;
                        }
                        break;

                }
                initFields(pnEditMode);
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            }
        }
    }

    private void textArea_KeyPressed(KeyEvent event) {
        String textAreaID = ((TextArea) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (textAreaID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void loadSalesExeField() {
        txtField01.setText(oTransSalesExe.getModel().getModel().getClientID());
        txtField02.setText(oTransSalesExe.getModel().getModel().getCompnyNm());
        textArea03.setText(oTransSalesExe.getModel().getModel().getAddress());
        txtField04.setText(oTransSalesExe.getModel().getModel().getMobileNo());
        txtField05.setText(oTransSalesExe.getModel().getModel().getEmailAdd());
        if (oTransSalesExe.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
    }

    private String getValue(String fsValue, Integer loRow, String fsCol) {
        try {
            fsValue = "";
            if (oTransSalesExe.getVSPTransDetail(loRow, fsCol) != null) {
                fsValue = String.valueOf(oTransSalesExe.getVSPTransDetail(loRow, fsCol)).toUpperCase();

            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesExecutiveFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fsValue;
    }

    private String getValueDate(String fsValue, Integer loRow, String fsCol) {
        try {
            fsValue = "";
            if (oTransSalesExe.getVSPTransDetail(loRow, fsCol) != null) {
                fsValue = InputTextUtil.xsDateShort((Date) oTransSalesExe.getVSPTransDetail(loRow, fsCol));

            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesExecutiveFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fsValue;
    }

    private void loadSalesTransaction() {
        transData.clear();
        JSONObject loJSON = new JSONObject();
        try {
            loJSON = oTransSalesExe.loadTransaction();
            if ("success".equals((String) loJSON.get("result"))) {
                for (lnCtr = 0; lnCtr <= oTransSalesExe.getVSPTransCount() - 1; lnCtr++) {
                    String csPlate = String.valueOf(oTransSalesExe.getVSPTransDetail(lnCtr, "sCSNoxxxx")).toUpperCase() + "/" + String.valueOf(oTransSalesExe.getVSPTransDetail(lnCtr, "sPlateNox")).toUpperCase();
                    transData.add(new ModelSalesExecutiveTrans(
                            String.valueOf(lnCtr + 1), //ROW
                            getValueDate("ldVSPDate", lnCtr, ""),
                            getValue("lsVSPNo", lnCtr, "sVSPNOxxx"),
                            getValue("lsCustomName", lnCtr, "sBuyCltNm"),
                            csPlate,
                            getValue("lsCSPlateNo", lnCtr, "sDescript"),
                            getValueDate("ldDrDate", lnCtr, "dUDRDatex"),
                            getValue("lsDrNo", lnCtr, "sUDRNoxxx"),
                            getValue("lsSalesExe", lnCtr, "sSaleExNm  ")
                    ));

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesExecutiveFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*populate Social Media Table*/
    private void initSalesTransaction() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));

        tblTransaction.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblTransaction.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        transData.clear();
        tblTransaction.setItems(transData);
    }

    private void clearFields() {
        cboxActivate.setSelected(false);
        txtField01.clear();
        txtField02.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW);
        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnDeactivate.setVisible(false);
        btnDeactivate.setManaged(false);
        btnActive.setVisible(false);
        btnActive.setManaged(false);
        if (fnValue == EditMode.READY) {
            if (oTransSalesExe.getModel().getModel().getRecdStat().equals("1")) {
                btnDeactivate.setVisible(true);
                btnDeactivate.setManaged(true);
                btnActive.setVisible(false);
                btnActive.setManaged(false);
            } else {
                btnDeactivate.setVisible(false);
                btnDeactivate.setManaged(false);
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
    }
}
