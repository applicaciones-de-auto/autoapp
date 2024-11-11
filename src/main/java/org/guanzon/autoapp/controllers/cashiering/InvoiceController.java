/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.cashiering.CheckInvoice;
import org.guanzon.autoapp.models.cashiering.TransInvoice;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InvoiceController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
//    private CAR oTrans;
    private String pxeModuleName = "";
    private UnloadForm poUnload = new UnloadForm();
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private boolean pbIsCAR = false;
    private String lsClientSource = "";
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cSourcexx = FXCollections.observableArrayList("NEW", "RENEW");
    ObservableList<String> cPayerxxx = FXCollections.observableArrayList("CUSTOMER", "SUPPLIER");
    private ObservableList<TransInvoice> transData = FXCollections.observableArrayList();
    private ObservableList<CheckInvoice> checkData = FXCollections.observableArrayList();

    @FXML
    AnchorPane AnchorMain;
    @FXML
    private Label lblInvoiceTitle, lblStatus, lblPrinted;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnInvCancel, btnClose;

    @FXML
    private TextField txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10, txtField12,
            txtField13, txtField15, txtField17, txtField18, txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25,
            txtField26, txtField27, txtField28, txtField29;
    @FXML
    private DatePicker datePicker02;
    @FXML
    private CheckBox checkBoxCash, checkBoxCard, checkBoxOnlnPymntServ, checkBoxCrdInv, checkBoxNoPymnt, checkBoxCheck,
            checkBoxGftCheck, checkBoxAllwMxPyr;
    @FXML
    private ComboBox<String> comboBox04, comboBox11;
    @FXML
    private Button btnInstTransDetail, btnInsertRemarks, btnInsertAdvances, btnInsCheckDetail;
    @FXML
    private TableView<TransInvoice> tblViewTrans;
    @FXML
    private TableColumn<TransInvoice, String> tblindexTrans01, tblindexTrans02, tblindexTrans03, tblindexTrans04, tblindexTrans05, tblindexTrans06, tblindexTrans07, tblindexTrans08, tblindexTrans09,
            tblindexTrans10;
    @FXML
    private TableView<CheckInvoice> tblViewCheck;

    @FXML
    private TableColumn<CheckInvoice, String> tblindexCheck01, tblindexCheck02, tblindexCheck03, tblindexCheck04, tblindexCheck05, tblindexCheck06, tblindexCheck07, tblindexCheck08;
    @FXML
    private TextArea textArea14, textArea16;

    public void setIsCARState(boolean fbValue) {
        pbIsCAR = fbValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            if (pbIsCAR) {
                btnAdd.fire();
            }
            lblInvoiceTitle.setText(getParentTabTitle());
        });

//        oTrans = new Invoice(oApp, false, oApp.getBranchCode());
        initTransTable();
        initCheckTable();
        initCapitalizationFields();
        initPatternFields();
        initLimiterFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initFieldsAction();
        initComboBoxItems();
        initTextFieldsProperty();
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10, txtField12);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea14, textArea16);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText("");
        datePicker02.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        txtField03.setText("");
        comboBox04.setValue("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        comboBox11.setValue("");
        txtField12.setText("");
        txtField13.setText("");
        textArea14.setText("");
        txtField15.setText("");
        textArea16.setText("");
        txtField17.setText("");
        txtField18.setText("");
        txtField19.setText("");
        txtField20.setText("");
        txtField21.setText("");
        txtField22.setText("");
        txtField23.setText("");
        txtField24.setText("");
        txtField25.setText("");
        txtField26.setText("");
        txtField27.setText("");
        txtField28.setText("");
        txtField29.setText("");
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10, txtField12,
                txtField13, txtField15, txtField17, txtField18, txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25,
                txtField26, txtField27, txtField28
        );
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea14, textArea16);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));

    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        if (lsValue == null) {
            return;
        }

        if (!nv) {
            switch (lnIndex) {
            }
            loadMasterFields();
        } else {
            loTxtField.selectAll();
        }
    };
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        TextArea loTextArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextArea.getId().substring(8, 10));
        String lsValue = loTextArea.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 12:
//                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField10, txtField12,
                txtField13, txtField15, txtField17, txtField18, txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25,
                txtField26, txtField27, txtField28, txtField29);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea14, textArea16);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));

    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
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
                case TAB:
                case ENTER:
                case F3:
                    switch (txtFieldID) {
                        case "txtField05":
//                            loJSON = oTrans.searchCustomer(lsValue);
//                            if (!"error".equals(loJSON.get("result"))) {
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                txtField05.setText("");
//                                return;
//                            }
                            break;
                        case "txtField10":
//                            loJSON = oTrans.searchInsurance(lsValue);
//                            if (!"error".equals(loJSON.get("result"))) {
//                                txtField10.setText(oTrans.getModel().getModel().getDeptName());
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                txtField10.setText("");
//                                return;
//                            }
                            break;
                        case "txtField12":
//                            loJSON = oTrans.searchCntrl(lsValue);
//                            if (!"error".equals(loJSON.get("result"))) {
//                                txtField12.setText(oTrans.getModel().getModel().getEmpInCharge());
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                txtField12.setText("");
//                                return;
//                            }
                            break;
                    }
                    initFields(pnEditMode);
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                case UP:
                    event.consume();
                    CommonUtils.SetPreviousFocus((TextField) event.getSource());
                    break;
                case DOWN:
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                default:
                    break;
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
        List<Button> loButtons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnInvCancel, btnClose,
                btnInstTransDetail, btnInsertRemarks, btnInsertAdvances, btnInsCheckDetail);
        loButtons.forEach(btn -> btn.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
//                oTrans = new SalesInvoice(oApp, false, oApp.getBranchCode());
//                loJSON = oTrans.newTransaction();
//                if ("success".equals((String) loJSON.get("result"))) {
//                    loadMasterFields();
//                    pnEditMode = oTrans.getEditMode();
//                    initFields(pnEditMode);
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                }
                break;
            case "btnEdit":
//                loJSON = oTrans.updateTransaction();
//                pnEditMode = oTrans.getEditMode();
//                if ("error".equals((String) loJSON.get("result"))) {
//                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
//                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
//                    CustomCommonUtil.switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
//                    oTrans = new SalesInvoice(oApp, false, oApp.getBranchCode());
//                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search " + pxeModuleName + "Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
//                loJSON = oTrans.searchTransaction("");
//                if ("success".equals((String) loJSON.get("result"))) {
//                    loadMasterFields();
//                    initFields(pnEditMode);
//                    pnEditMode = oTrans.getEditMode();
//                } else {
//                    ShowMessageFX.Warning(null, "Search " + pxeModuleName + "Confirmation", (String) loJSON.get("message"));
//                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to save?")) {
//                    loJSON = oTrans.saveTransaction();
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        ShowMessageFX.Information(null, pxeModuleName + " Information", (String) loJSON.get("message"));
//                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            loadMasterFields();
//                            initFields(pnEditMode);
//                            pnEditMode = oTrans.getEditMode();
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        return;
//                    }
                }
                break;
            case "btnInvCancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this VSI?")) {
//                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                    }
//                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        loadMasterFields();
//                        pnEditMode = oTrans.getEditMode();
//                        initFields(pnEditMode);
//                    }
                }
                break;
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                } else {
                    return;
                }
                break;
            case "btnInstTransDetail":
            case "btnInsertRemarks ":
            case "btnInsertAdvances ":
            case "btnInsCheckDetail":
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    @Override
    public void initComboBoxItems() {
        comboBox04.setItems(cSourcexx);
        comboBox11.setItems(cPayerxxx);
    }

    @Override
    public void initFieldsAction() {
        datePicker02.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                oTrans.getModel().getModel().setDateThru(SQLUtil.toDate(datePicker02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
                }
                initFields(pnEditMode);
            }
        });
        comboBox11.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox11.getSelectionModel().getSelectedIndex() >= 0) {
                }
                initFields(pnEditMode);
            }
        });
        checkBoxNoPymnt.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxNoPymnt.isSelected()) {
                    checkBoxNoPymnt.setSelected(true);
                } else {
                    checkBoxNoPymnt.setSelected(false);
                }
                initFields(pnEditMode);
            }
        }
        );
        checkBoxCash.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxCash.isSelected()) {
                    checkBoxCash.setSelected(true);
                } else {
                    checkBoxCash.setSelected(false);
                }
                initFields(pnEditMode);
            }
        }
        );
        checkBoxCard.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxCard.isSelected()) {
                    checkBoxCard.setSelected(true);
                } else {
                    checkBoxCard.setSelected(false);
                }
                initFields(pnEditMode);
            }
        }
        );
        checkBoxOnlnPymntServ.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxOnlnPymntServ.isSelected()) {
                    checkBoxOnlnPymntServ.setSelected(true);
                } else {
                    checkBoxOnlnPymntServ.setSelected(false);
                }
                initFields(pnEditMode);
            }
        }
        );
        checkBoxCrdInv.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxCrdInv.isSelected()) {
                    checkBoxCrdInv.setSelected(true);
                } else {
                    checkBoxCrdInv.setSelected(false);
                }
                initFields(pnEditMode);
            }
        });
        checkBoxCheck.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxCheck.isSelected()) {
                    checkBoxCheck.setSelected(true);
                } else {
                    checkBoxCheck.setSelected(false);
                }
                initFields(pnEditMode);
            }
        });
        checkBoxGftCheck.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxGftCheck.isSelected()) {
                    checkBoxGftCheck.setSelected(true);
                } else {
                    checkBoxGftCheck.setSelected(false);
                }
                initFields(pnEditMode);
            }
        });
    }

    @Override
    public void initTextFieldsProperty() {
        txtField05.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null && newValue.isEmpty()) {
                    initFields(pnEditMode);
                }
            }
        }
        );
        txtField10.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null && newValue.isEmpty()) {
                    initFields(pnEditMode);
                }
            }
        });
        txtField12.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null && newValue.isEmpty()) {
                    initFields(pnEditMode);
                }
            }
        });
    }

    @Override

    public void clearTables() {
        transData.clear();
        checkData.clear();
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10, txtField12,
                txtField13, txtField15);
        CustomCommonUtil.setText("0.00", txtField17, txtField18, txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25,
                txtField26, txtField27, txtField28, txtField29);
        CustomCommonUtil.setText("", lblPrinted, lblStatus);
        txtField03.setText("0");
        datePicker02.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        CustomCommonUtil.setText("", textArea14, textArea16);
        CustomCommonUtil.setValue(null, comboBox04, comboBox11);
        CustomCommonUtil.setSelected(false, checkBoxCash, checkBoxCard, checkBoxOnlnPymntServ, checkBoxCrdInv, checkBoxCheck,
                checkBoxGftCheck, checkBoxNoPymnt, checkBoxAllwMxPyr);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        CustomCommonUtil.setDisable(!lbShow, txtField03,
                checkBoxNoPymnt, checkBoxCash, checkBoxCheck, checkBoxCard, checkBoxOnlnPymntServ,
                checkBoxCrdInv, checkBoxCheck, checkBoxGftCheck,
                comboBox11, textArea14, txtField12, txtField13, btnInstTransDetail, btnInsertRemarks, btnInsertAdvances, btnInsCheckDetail);
        txtField05.setDisable(!(lbShow && !comboBox04.getValue().isEmpty()));
        txtField10.setDisable(!(lbShow && !txtField05.getText().isEmpty()));

        CustomCommonUtil.setVisible(false, btnInvCancel, btnEdit, btnPrint);
        CustomCommonUtil.setManaged(false, btnInvCancel, btnEdit, btnPrint);
        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        if (fnValue == EditMode.READY) {
            if (lblStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(false, btnInvCancel, btnEdit, btnPrint);
                CustomCommonUtil.setManaged(false, btnInvCancel, btnEdit, btnPrint);
            } else {
                CustomCommonUtil.setVisible(true, btnInvCancel, btnEdit, btnPrint);
                CustomCommonUtil.setManaged(true, btnInvCancel, btnEdit, btnPrint);
            }
        }
    }

    private void initTransTable() {
        tblindexTrans01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindexTrans02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindexTrans03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindexTrans04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindexTrans05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindexTrans06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindexTrans07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindexTrans08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindexTrans09.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        tblindexTrans10.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        tblViewTrans.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewTrans.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        transData.clear();
        tblViewTrans.setItems(transData);
    }

    private void loadTransTable() {

    }

    private void initCheckTable() {
        tblindexCheck01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindexCheck02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindexCheck03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindexCheck04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindexCheck05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindexCheck06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindexCheck07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindexCheck08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblViewCheck.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewCheck.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        checkData.clear();
        tblViewCheck.setItems(checkData);
    }

    private void loadCheckTable() {

    }

    private String getParentTabTitle() {
        Node parent = AnchorMain.getParent();
        Parent tabContentParent = parent.getParent();

        if (tabContentParent instanceof TabPane) {
            TabPane tabPane = (TabPane) tabContentParent;
            Tab tab = findTabByContent(tabPane, AnchorMain);
            if (tab != null) {
                pxeModuleName = tab.getText();
                return tab.getText().toUpperCase();
            }
        }

        return null; // No parent Tab found
    }

    private Tab findTabByContent(TabPane tabPane, Node content) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getContent() == content) {
                return tab;
            }
        }
        return null;
    }
}
