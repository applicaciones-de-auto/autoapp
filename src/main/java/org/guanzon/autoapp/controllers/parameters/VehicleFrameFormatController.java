/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parameters;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parameter.Vehicle_MakeFramePattern;
import org.guanzon.auto.main.parameter.Vehicle_ModelFramePattern;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class VehicleFrameFormatController implements Initializable, ScreenInterface {

    private GRider oApp;

    private Vehicle_ModelFramePattern oTransModelFrameFormat;
    private Vehicle_MakeFramePattern oTransMakeFrameFormat;
    private final String pxeModuleName = "Vehicle Frame Format";
    private String psMakeID, psMakeDesc;
    private String psModelID, psModelDesc;
    private int pnEditMode;
    private boolean pbOpenEvent = false;
    ObservableList<String> cFormtType = FXCollections.observableArrayList("MODEL FORMAT", "MAKE FORMAT");
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private ComboBox<String> comboBox01;
    @FXML
    private TextField txtField05;

    public String setMakeID(String fsValue) {
        psMakeID = fsValue;
        return psMakeID;
    }

    public String setMakeDesc(String fsValue) {
        psMakeDesc = fsValue;
        return psMakeDesc;
    }

    public String setModelID(String fsValue) {
        psModelID = fsValue;
        return psModelID;
    }

    public String setModelDesc(String fsValue) {
        psModelDesc = fsValue;
        return psModelDesc;
    }

    public Boolean setOpenEvent(Boolean fbValue) {
        pbOpenEvent = fbValue;
        return pbOpenEvent;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        oTransModelFrameFormat = new Vehicle_ModelFramePattern(oApp, false, oApp.getBranchCode());
        oTransMakeFrameFormat = new Vehicle_MakeFramePattern(oApp, false, oApp.getBranchCode());
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initButtons();
        clearFields();
        comboBox01.setItems(cFormtType);
        comboBox01.setOnAction(e -> {
            clearFields();
            pnEditMode = EditMode.UNKNOWN;
            initFields(pnEditMode);
        });

        CustomCommonUtil.addTextLimiter(txtField04, 3);
        CustomCommonUtil.addTextLimiter(txtField05, 3);

        txtField02.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                                    oTransModelFrameFormat.getModel().getModel().setMakeID("");
                                    oTransModelFrameFormat.getModel().getModel().setModelDsc("");
                                    txtField03.setText("");
                                } else {
                                    oTransMakeFrameFormat.getModel().getModel().setMakeID("");

                                }
                            }
                        }
                    }
                }
                );
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                                    oTransModelFrameFormat.getModel().getModel().setModelDsc("");
                                }
                            }
                        }
                    }
                }
                );
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadFrameFormatFields() {
        switch (comboBox01.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField02.setText(oTransModelFrameFormat.getModel().getModel().getMakeDesc());
                txtField03.setText(oTransModelFrameFormat.getModel().getModel().getModelDsc());
                txtField04.setText(oTransModelFrameFormat.getModel().getModel().getFrmePtrn());
                if (oTransModelFrameFormat.getModel().getModel().getFrmeLen() == null) {
                    txtField05.setText("0");
                } else {
                    txtField05.setText(String.valueOf(oTransModelFrameFormat.getModel().getModel().getFrmeLen()));
                }
                break;
            case 1:
                txtField02.setText(oTransMakeFrameFormat.getModel().getModel().getMakeDesc());
                txtField04.setText(oTransMakeFrameFormat.getModel().getModel().getFrmePtrn());
                txtField03.setDisable(true);
                txtField05.setDisable(true);
            default:
                break;
        }
    }

    private void initTextFieldPattern() {
        Pattern textOnly, numOnly, frmPat;
        textOnly = Pattern.compile("[A-Za-z -]*");
        frmPat = Pattern.compile("[A-Za-z0-9-]*");
        numOnly = Pattern.compile("[0-9]*");
        txtField02.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField03.setTextFormatter(new TextFormatterUtil(textOnly));
        txtField04.setTextFormatter(new TextFormatterUtil(frmPat));
        txtField05.setTextFormatter(new TextFormatterUtil(numOnly));

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

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
                    case "txtField02":
                        if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                            loJSON = oTransModelFrameFormat.searchMake(lsValue, true);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField02.setText(oTransModelFrameFormat.getModel().getModel().getMakeDesc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField02.setText("");
                                txtField02.requestFocus();
                                return;
                            }
                        } else {
                            loJSON = oTransMakeFrameFormat.searchMake(lsValue, true);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField02.setText(oTransMakeFrameFormat.getModel().getModel().getMakeDesc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField02.setText("");
                                txtField02.requestFocus();
                                return;
                            }
                        }
                        break;
                    case "txtField03":
                        if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                            loJSON = oTransModelFrameFormat.searchModel(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField03.setText(oTransModelFrameFormat.getModel().getModel().getModelDsc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField03.setText("");
                                txtField03.requestFocus();
                                return;
                            }
                        }
                        break;
                }
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();
                CommonUtils.SetPreviousFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            }
        }

    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField04, txtField05);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 4:
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        oTransModelFrameFormat.getModel().getModel().setFrmePtrn(lsValue);
                    } else {
                        oTransMakeFrameFormat.getModel().getModel().setFrmePtrn(lsValue);
                    }
                    break;
                case 5:
                    oTransModelFrameFormat.getModel().getModel().setFrmeLen(Integer.valueOf(lsValue));
                    break;
            }
        } else {
            txtField.selectAll();
        }
    };

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    clearFields();
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        oTransModelFrameFormat = new Vehicle_ModelFramePattern(oApp, false, oApp.getBranchCode());
                        loJSON = oTransModelFrameFormat.newRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            if (pbOpenEvent) {
                                oTransModelFrameFormat.getModel().getModel().setMakeID(psMakeID);
                                oTransModelFrameFormat.getModel().getModel().setMakeDesc(psMakeDesc);
                                oTransModelFrameFormat.getModel().getModel().setModelID(psModelID);
                                oTransModelFrameFormat.getModel().getModel().setModelDsc(psModelDesc);
                            }
                            loadFrameFormatFields();
                            pnEditMode = oTransModelFrameFormat.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        }
                    } else {
                        oTransMakeFrameFormat = new Vehicle_MakeFramePattern(oApp, false, oApp.getBranchCode());
                        loJSON = oTransMakeFrameFormat.newRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            if (pbOpenEvent) {
                                oTransMakeFrameFormat.getModel().getModel().setMakeID(psMakeID);
                                oTransMakeFrameFormat.getModel().getModel().setMakeDesc(psMakeDesc);
                            }
                            loadFrameFormatFields();
                            pnEditMode = oTransMakeFrameFormat.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please choose Format Type.");
                }
                break;
            case "btnEdit":
                if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                    loJSON = oTransModelFrameFormat.updateRecord();
                    pnEditMode = oTransModelFrameFormat.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                } else {
                    loJSON = oTransMakeFrameFormat.updateRecord();
                    pnEditMode = oTransMakeFrameFormat.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                }
                break;
            case "btnSave":
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        if (ShowMessageFX.YesNo(null, "Vehicle FrameFormat Information Saving....", "Are you sure, do you want to save?")) {
                            loJSON = oTransModelFrameFormat.saveRecord();
                            if ("success".equals((String) loJSON.get("result"))) {
                                ShowMessageFX.Information(null, "Vehicle FrameFormat Information", (String) loJSON.get("message"));
                                loJSON = oTransModelFrameFormat.openRecord(oTransModelFrameFormat.getModel().getModel().getModelID(), oTransModelFrameFormat.getModel().getModel().getEntryNo());
                                if ("success".equals((String) loJSON.get("result"))) {
                                    loadFrameFormatFields();
                                    initFields(pnEditMode);
                                    pnEditMode = oTransModelFrameFormat.getEditMode();
                                }
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
                        }
                    } else {
                        if (ShowMessageFX.YesNo(null, "Vehicle FrameFormat Information Saving....", "Are you sure, do you want to save?")) {
                            loJSON = oTransMakeFrameFormat.saveRecord();
                            if ("success".equals((String) loJSON.get("result"))) {
                                ShowMessageFX.Information(null, "Vehicle FrameFormat Information", (String) loJSON.get("message"));
                                loJSON = oTransMakeFrameFormat.openRecord(oTransMakeFrameFormat.getModel().getModel().getMakeID(), oTransMakeFrameFormat.getModel().getModel().getEntryNo());
                                if ("success".equals((String) loJSON.get("result"))) {
                                    loadFrameFormatFields();
                                    initFields(pnEditMode);
                                    pnEditMode = oTransMakeFrameFormat.getEditMode();
                                }
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please choose Format Type.");
                    clearFields();
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    oTransModelFrameFormat = new Vehicle_ModelFramePattern(oApp, false, oApp.getBranchCode());
                    oTransMakeFrameFormat = new Vehicle_MakeFramePattern(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (comboBox01.getSelectionModel().getSelectedIndex() >= 0) {
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.YesNo(null, "Search Vehicle Frame Format Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }
                    }
                    if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                        loJSON = oTransModelFrameFormat.searchRecord("", false);
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadFrameFormatFields();
                            pnEditMode = oTransModelFrameFormat.getEditMode();
                            initFields(pnEditMode);
                        } else {
                            ShowMessageFX.Warning(null, "Search Vehicle Model Frame Format Information", (String) loJSON.get("message"));
                        }
                    } else {
                        loJSON = oTransMakeFrameFormat.searchRecord("", false);
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadFrameFormatFields();
                            pnEditMode = oTransMakeFrameFormat.getEditMode();
                            initFields(pnEditMode);
                        } else {
                            ShowMessageFX.Warning(null, "Search Vehicle Make Frame Format Information", (String) loJSON.get("message"));
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please choose Format Type.");
                    clearFields();
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning("Please contact admin to assist about no button available", "Integrated Automotive System", pxeModuleName);
                break;
        }
        initFields(pnEditMode);

    }

    private void clearFields() {
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.setText("0");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField02.setDisable(true);
        txtField03.setDisable(true);
        txtField04.setDisable(true);
        txtField05.setDisable(true);
        if (lbShow) {
            switch (comboBox01.getSelectionModel().getSelectedIndex()) {
                case 0:
                    txtField02.setDisable(!lbShow);
                    txtField03.setDisable(!lbShow);
                    txtField04.setDisable(!lbShow);
                    txtField05.setDisable(!lbShow);
                    break;
                case 1:
                    txtField02.setDisable(!lbShow);
                    txtField03.setDisable(true);
                    txtField04.setDisable(!lbShow);
                    txtField05.setDisable(true);
                    break;
            }
        }
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        if (fnValue == EditMode.UPDATE) {
            if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                txtField02.setDisable(true);
                txtField03.setDisable(true);
                txtField04.setDisable(true);
            } else {
                txtField02.setDisable(true);
                txtField04.setDisable(true);
            }
        }
        if (fnValue == EditMode.READY) {
            if (comboBox01.getSelectionModel().getSelectedIndex() == 0) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
            } else {
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
            }
        }
    }
}
