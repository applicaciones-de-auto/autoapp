package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Sales_Executive;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.models.general.SalesExecutiveTrans;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class SalesExecutiveController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Sales_Executive oTrans;
    private final String pxeModuleName = "Sales Executive"; //Form Title
    private int pnEditMode;
    private ObservableList<SalesExecutiveTrans> transData = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd, btnSave, btnBrowse, btnCancel, btnActive, btnDeactivate, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05;
    @FXML
    private TextArea textArea03;
    @FXML
    private CheckBox cboxActivate;
    @FXML
    private TableView<SalesExecutiveTrans> tblTransaction;
    @FXML
    private TableColumn<SalesExecutiveTrans, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;

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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);

        oTrans = new Sales_Executive(oApp, false, oApp.getBranchCode());

        initSalesTransaction();
        initCapitalizationFields();
        initTextKeyPressed();
        initButtonsClick();
        initTextFieldsProperty();
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        CustomCommonUtil.setCapsLockBehavior(textArea03);
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getModel().getModel().getClientID());
        txtField02.setText(oTrans.getModel().getModel().getCompnyNm());
        textArea03.setText(oTrans.getModel().getModel().getAddress());
        txtField04.setText(oTrans.getModel().getModel().getMobileNo());
        txtField05.setText(oTrans.getModel().getModel().getEmailAdd());
        if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
            cboxActivate.setSelected(true);
        } else {
            cboxActivate.setSelected(false);
        }
        return true;
    }

    @Override
    public void initPatternFields() {

    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
    }

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04, txtField05);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        textArea03.setOnKeyPressed(this::textArea_KeyPressed);
    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
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
            if (event.getCode() == KeyCode.F3) {
                switch (txtFieldID) {
                    case "txtField01":
                        loJSON = oTrans.searchEmployee(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadMasterFields();
                            checkExistingSalesExeInformation();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField01.setText("");
                            return;
                        }
                        break;
                    case "txtField02":
                        loJSON = oTrans.searchEmployee(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            loadMasterFields();
                            checkExistingSalesExeInformation();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField02.setText("");
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
            } else if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.TAB) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            } else if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                CommonUtils.SetNextFocus((TextField) event.getSource());
            }
        }
    }

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
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

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnSave, btnBrowse, btnCancel,
                btnClose, btnActive, btnDeactivate);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                transData.clear();
                oTrans = new Sales_Executive(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadSalesTransaction();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Sales Executive Information Saving....", "Are you sure, do you want to save?")) {
                    if (checkExistingSalesExeInformation()) {
                        return;
                    }
                } else {
                    return;
                }

                loJSON = oTrans.saveRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadSalesTransaction();
                        pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    if (loJSON.get("message").toString().contains("Client ID")) {
                        txtField01.requestFocus();
                    } else {
                        txtField02.requestFocus();
                    }
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        clearFields();
                        transData.clear();
                        oTrans = new Sales_Executive(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = EditMode.READY;
                        }
                    }
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Sales Executive Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadSalesTransaction();
                    pnEditMode = EditMode.READY;
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Sales Executive Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getClientID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Sales Executive Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = pnEditMode = EditMode.READY;
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getClientID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Sales Executive Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
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

    @Override
    public void initComboBoxItems() {
    }

    @Override
    public void initFieldsAction() {
    }

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setClientID("");
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
                                oTrans.getModel().getModel().setCompnyNm("");
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

    @Override
    public void clearTables() {
        transData.clear();
    }

    @Override
    public void clearFields() {
        cboxActivate.setSelected(false);
        CustomCommonUtil.setText("", txtField01, txtField02);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW);
        CustomCommonUtil.setDisable(!lbShow, txtField01, txtField02);
        cboxActivate.setDisable(true);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnDeactivate, btnActive);
        CustomCommonUtil.setManaged(false, btnDeactivate, btnActive);
        if (fnValue == EditMode.READY) {
            if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                btnDeactivate.setVisible(true);
                btnDeactivate.setManaged(true);
            } else {
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
    }

    private boolean checkExistingSalesExeInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.validateExistingSE();
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord((String) loJSON.get("sClientID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadSalesTransaction();
                    pnEditMode = EditMode.READY;
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private void loadSalesTransaction() {
        transData.clear();
        String csPlate = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getVSPModelList().size() - 1; lnCtr++) {
            if (oTrans.getModel().getVSPModel(lnCtr).getPlateNo() != null) {
                csPlate = oTrans.getModel().getVSPModel(lnCtr).getCSNo() + "/" + oTrans.getModel().getVSPModel(lnCtr).getPlateNo();
            } else {
                csPlate = oTrans.getModel().getVSPModel(lnCtr).getCSNo();
            }
            transData.add(new SalesExecutiveTrans(
                    String.valueOf(lnCtr + 1), //ROW
                    CustomCommonUtil.xsDateShort(oTrans.getModel().getVSPModel(lnCtr).getDelvryDt()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getVSPNO().toUpperCase()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getBuyCltNm().toUpperCase()),
                    csPlate,
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getVhclFDsc().toUpperCase()),
                    CustomCommonUtil.xsDateShort(oTrans.getModel().getVSPModel(lnCtr).getUDRDate()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getUDRNo().toUpperCase()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getAgentNm())
            ));
            csPlate = "";
        }
        tblTransaction.setItems(transData);
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
    }

}
