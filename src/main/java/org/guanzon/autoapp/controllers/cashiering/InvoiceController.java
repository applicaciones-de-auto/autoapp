/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.cashiering.CashierReceivables;
import org.guanzon.auto.main.cashiering.SalesInvoice;
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
    private SalesInvoice oTrans;
    private CashierReceivables oTransCAR;
    private String pxeModuleName = "";
    private UnloadForm poUnload = new UnloadForm();
    private boolean pbIsCAR = false;
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    private String poTransNox = "";
    private String psPayTypex = "";
    private int pnRow;
    ObservableList<String> cSourcexx = FXCollections.observableArrayList("STATE OF ACCOUNT", "CASHIER RECEIVABLES", "OTHERS");
    ObservableList<String> cPayerxxx = FXCollections.observableArrayList("CUSTOMER", "BANK", "INSURANCE", "SUPPLIER", "ASSOCIATE");
    private ObservableList<TransInvoice> transData = FXCollections.observableArrayList();
    private ObservableList<CheckInvoice> checkData = FXCollections.observableArrayList();
    private HashSet<String> psPayMode = new HashSet<>();
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
            tblindexTrans10, tblindexTrans11;
    @FXML
    private TableView<CheckInvoice> tblViewCheck;

    @FXML
    private TableColumn<CheckInvoice, String> tblindexCheck01, tblindexCheck02, tblindexCheck03, tblindexCheck04, tblindexCheck05, tblindexCheck06, tblindexCheck07, tblindexCheck08;
    @FXML
    private TextArea textArea14, textArea16;

    public void setIsCARState(boolean fbValue) {
        pbIsCAR = fbValue;
    }

    public void setCARObject(CashierReceivables foValue) {
        oTransCAR = foValue;
    }

    public void setTransNo(String fsValue) {
        poTransNox = fsValue;
    }

    public void setRow(int fnValue) {
        pnRow = fnValue;
    }

    public void setPayType(String fsValue) {
        psPayTypex = fsValue;
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
        initTransTable();
        initCheckTable();
        Platform.runLater(() -> {
            lblInvoiceTitle.setText(getParentTabTitle());
            if (pbIsCAR) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
                loJSON = oTransCAR.openTransaction(poTransNox);
                if (!"error".equals((String) loJSON.get("result"))) {
                    setCARValueToSI();
                    loadMasterFields();
                    loadTransTable();
                    loadCheckTable();
                    loadPayModeCheckedFields();
                }
            }
        });

        oTrans = new SalesInvoice(oApp, false, oApp.getBranchCode());
        oTransCAR = new CashierReceivables(oApp, false, oApp.getBranchCode());
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
        initTableKeyPressed();
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
        oTrans.computeSIAmount(true);
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getReferNo());
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker02.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getTransactDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        txtField03.setText("0");
        int selectedPayType = -1;
        switch (psPayTypex) {
            case "c":
                selectedPayType = 0;
                break;
            case "b":
                selectedPayType = 1;
                break;
            case "i":
                selectedPayType = 2;
                break;
            case "s":
                selectedPayType = 3;
                break;
            case "a":
                selectedPayType = 4;
                break;

        }
        comboBox04.getSelectionModel().select(selectedPayType);
        txtField05.setText(oTrans.getMasterModel().getMasterModel().getBuyCltNm());
        txtField06.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField07.setText("");
        String lsTinID = "";
        if (oTrans.getMasterModel().getMasterModel().getTaxIDNo() != null) {
            lsTinID = oTrans.getMasterModel().getMasterModel().getTaxIDNo().replaceAll("(.{3})(?=.)", "$1-");
        }
        txtField08.setText(lsTinID);
        txtField09.setText("");
        txtField10.setText("");
//        comboBox11.setValue("");
        txtField12.setText("");
        txtField13.setText("");
        textArea14.setText("");
        txtField15.setText("");
        textArea16.setText("");

        txtField17.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getTranTotl()));
        txtField18.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getWTRate()));
        txtField19.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getVatAmt()));
        txtField20.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getNonVATSl()));
        txtField21.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getZroVATSl()));
        txtField22.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getVatSales()));
        txtField23.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getNetTotal()));
        txtField24.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getCashAmt()));
        txtField25.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getChckAmt()));
        txtField26.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getCardAmt()));
        txtField27.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getOthrAmt()));
        txtField28.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getGiftAmt()));
        txtField29.setText("0.00");
        String lsStatus = "";
        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lsStatus = "Active";
                break;
            case TransactionStatus.STATE_CLOSED:
                lsStatus = "Approved";
                break;
            case TransactionStatus.STATE_CANCELLED:
                lsStatus = "Cancelled";
                break;
            case TransactionStatus.STATE_POSTED:
                lsStatus = "Posted";
                break;
        }
        lblStatus.setText(lsStatus);
        switch (oTrans.getMasterModel().getMasterModel().getPrinted()) {
            case "1":
                lblPrinted.setText("Y");
                break;
            case "0":
                lblPrinted.setText("N");
                break;
            default:
                lblPrinted.setText("");
                break;
        }
        setDoctTypeFromTitleForm();
        return true;
    }

    @Override
    public void initPatternFields() {
        CustomCommonUtil.inputIntegerOnly(txtField03);
    }

    @Override
    public void initLimiterFields() {

    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
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
                case 1:
                    oTrans.getMasterModel().getMasterModel().setReferNo(lsValue);
                    break;
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    break;
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
                case 14:
//                    oTrans.getMasterModel().getMasterModel().set(lsValue);
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
                            switch (comboBox04.getSelectionModel().getSelectedIndex()) {
                                case 0:
                                    loJSON = oTrans.searchCustomer(lsValue);
                                    break;
                                case 1:
                                    loJSON = oTrans.searchBankBranch(lsValue, 0);
                                    break;
                                case 2:
                                    loJSON = oTrans.searchInsurance(lsValue);
                                    break;
                                case 3:
                                    loJSON = oTrans.searchSupplier(lsValue);
                                    break;
                                case 4:
                                    loJSON = oTrans.searchEmployee(lsValue, true);
                                    break;
                            }
                            if (!"error".equals(loJSON.get("result"))) {
                                loadMasterFields();
                            } else {
                                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                                txtField05.setText("");
                                return;
                            }
                            break;
                        case "txtField10":
                            loJSON = oTrans.searchInsurance(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
//                                txtField10.setText(oTrans.getMasterModel().getMasterModel().g);
                            } else {
                                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                                txtField10.setText("");
                                return;
                            }
                            break;
                        case "txtField12":
                            switch (comboBox11.getSelectionModel().getSelectedIndex()) {
                                case 0:
                                    loJSON = oTrans.searchSOA(lsValue);
                                    break;
                                case 1:
                                    loJSON = oTrans.searchCAR(lsValue);
                                    break;
                            }
                            if (!"error".equals(loJSON.get("result"))) {
                                loadTransTable();
                                loadMasterFields();
                            } else {
                                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                                txtField12.setText("");
                                return;
                            }
                            break;
                    }
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
                oTrans = new SalesInvoice(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadTransTable();
                    loadCheckTable();
                    loadPayModeCheckedFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                initFields(pnEditMode);
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        clearFields();
                        clearTables();
                        oTrans = new SalesInvoice(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadTransTable();
                            loadCheckTable();
                            loadPayModeCheckedFields();
                            pnEditMode = oTrans.getEditMode();
                        }
                    }
                    initFields(pnEditMode);
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                String lsDocType = "";
                switch (lblInvoiceTitle.getText()) {
                    case "ACKNOWLEDGEMENT RECEIPT":
                        lsDocType = "7";
                        break;
                    case "BILLING STATEMENT":
                        lsDocType = "6";
                        break;
                    case "COLLECTION RECEIPT":
                        lsDocType = "4";
                        oTrans.getMasterModel().getMasterModel().setDocType("4");
                        break;
                    case "SERVICE INVOICE":
                        lsDocType = "2";
                        break;
                    case "PART SALES INVOICE":
                        lsDocType = "5";
                        break;
                    case "VEHICLE SALES INVOICE":
                        lsDocType = "0";
                        break;
                }
                loJSON = oTrans.searchTransaction("", lsDocType);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadTransTable();
                    loadCheckTable();
                    loadPayModeCheckedFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, "Search Confirmation", (String) loJSON.get("message"));
                }
                initFields(pnEditMode);
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, toTitleCase(pxeModuleName), "Are you sure, do you want to save?")) {
                    Map<CheckBox, String> paymentTypeMap = new HashMap<>();
                    paymentTypeMap.put(checkBoxCard, "CARD");
                    paymentTypeMap.put(checkBoxCheck, "CHECK");
                    paymentTypeMap.put(checkBoxGftCheck, "GIFT CHECK");
                    paymentTypeMap.put(checkBoxOnlnPymntServ, "ONLINE PAYMENT");
                    paymentTypeMap.put(checkBoxCrdInv, "CREDIT MEMO");
                    // Validate each selected checkbox
                    for (Map.Entry<CheckBox, String> entry : paymentTypeMap.entrySet()) {
                        CheckBox checkBox = entry.getKey();
                        String paymentType = entry.getValue();

                        if (checkBox.isSelected()) {
                            boolean hasPaymentDetails = false;

                            for (CheckInvoice item : tblViewCheck.getItems()) {
                                if (paymentType.equals(item.getTblindex02())) {
                                    hasPaymentDetails = true;
                                    break;
                                }
                            }

                            if (!hasPaymentDetails) {
                                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName),
                                        "No payment details, for " + paymentType.toLowerCase() + " payment.");
                                return;
                            }
                        }
                    }

                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadTransTable();
                            loadCheckTable();
                            loadPayModeCheckedFields();
                            pnEditMode = oTrans.getEditMode();
                            initFields(pnEditMode);
                        }
                    } else {
                        ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                        return;
                    }
                }
                break;

            case "btnInvCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure, do you want to cancel this " + pxeModuleName + "?")) {
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadTransTable();
                        loadCheckTable();
                        loadPayModeCheckedFields();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnClose":
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, toTitleCase(pxeModuleName));
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                } else {
                    return;
                }
                break;
            case "btnInstTransDetail":
                if (comboBox11.getSelectionModel().getSelectedIndex() == 2) {
                    oTrans.addSIDetail();
                    loadInsertOtherDetailWindow(oTrans.getSIDetailList().size() - 1);
                }
                break;
            case "btnInsertRemarks":
                break;
            case "btnInsertAdvances":
                if (!tblViewTrans.getItems().isEmpty()) {
                    loadAdvancesDetailWindow(oTrans.getSIAdvancesList().size() - 1);
                }
                break;
            case "btnInsCheckDetail":
                if (psPayMode.isEmpty()) {
                    ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), "Please select paymode first to insert payment details.");
                    return;
                }
                oTrans.addSIPayment();
                loadPaymentDetailWindow(oTrans.getSIPaymentList().size() - 1, false);
                break;
            default:
                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    @Override
    public void initComboBoxItems() {
        comboBox04.setItems(cPayerxxx);
        comboBox11.setItems(cSourcexx);
    }

    @Override
    public void initFieldsAction() {
        datePicker02.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setTransactDte(SQLUtil.toDate(datePicker02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setClientID("");
                    oTrans.getMasterModel().getMasterModel().setBuyCltNm("");
                    oTrans.getMasterModel().getMasterModel().setAddress("");
                    oTrans.getMasterModel().getMasterModel().setTaxIDNo("");
                    txtField05.setText("");
                    txtField06.setText("");
                    txtField07.setText("");
                    txtField08.setText("");
                    txtField09.setText("");
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
                    psPayMode.clear();
                } else {
                    checkBoxNoPymnt.setSelected(false);
                    psPayMode.clear();
                }
                loadPayModeCheckedFields();
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
                    psPayMode.add("CARD");
                } else {
                    checkBoxCard.setSelected(false);
                    psPayMode.remove("CARD");
                }
                initFields(pnEditMode);
            }
        }
        );
        checkBoxOnlnPymntServ.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxOnlnPymntServ.isSelected()) {
                    checkBoxOnlnPymntServ.setSelected(true);
                    psPayMode.add("OP");
                } else {
                    checkBoxOnlnPymntServ.setSelected(false);
                    psPayMode.remove("OP");
                }

                initFields(pnEditMode);
            }
        }
        );
        checkBoxCrdInv.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxCrdInv.isSelected()) {
                    checkBoxCrdInv.setSelected(true);
                    psPayMode.add("CM");
                } else {
                    checkBoxCrdInv.setSelected(false);
                    psPayMode.add("CM");
                }
                initFields(pnEditMode);
            }
        });
        checkBoxCheck.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxCheck.isSelected()) {
                    checkBoxCheck.setSelected(true);
                    psPayMode.add("CHECK");
                } else {
                    checkBoxCheck.setSelected(false);
                    psPayMode.remove("CHECK");
                }

                initFields(pnEditMode);
            }
        });
        checkBoxGftCheck.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (checkBoxGftCheck.isSelected()) {
                    checkBoxGftCheck.setSelected(true);
                    psPayMode.add("GC");
                } else {
                    checkBoxGftCheck.setSelected(false);
                    psPayMode.remove("GC");
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
                    oTrans.getMasterModel().getMasterModel().setClientID("");
                    oTrans.getMasterModel().getMasterModel().setBuyCltNm("");
                    oTrans.getMasterModel().getMasterModel().setAddress("");
                    oTrans.getMasterModel().getMasterModel().setTaxIDNo("");
                    txtField06.setText("");
                    txtField07.setText("");
                    txtField08.setText("");
                    txtField09.setText("");
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
        psPayMode.clear();
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
        CustomCommonUtil.setDisable(true, txtField01, comboBox04, txtField05, txtField10, btnInstTransDetail, txtField12, btnInsertAdvances);
        CustomCommonUtil.setDisable(!lbShow, datePicker02, txtField03,
                checkBoxNoPymnt, checkBoxCheck, checkBoxCard, checkBoxOnlnPymntServ,
                checkBoxCrdInv, checkBoxCheck, checkBoxGftCheck, checkBoxAllwMxPyr,
                comboBox11, textArea14, txtField13, btnInsertRemarks, btnInsCheckDetail);
        if (lbShow) {
            if (!tblViewTrans.getItems().isEmpty()) {
                btnInsertAdvances.setDisable(!lbShow);
            }
            switch (comboBox11.getSelectionModel().getSelectedIndex()) {
                case 0:
                case 1:
                    txtField12.setDisable(!lbShow);
                    break;
                case 2:
                    btnInstTransDetail.setDisable(!lbShow);
                    break;
            }
        }
        if (!pbIsCAR) {
            comboBox04.setDisable(!lbShow);
        }
        if (comboBox04.getValue() != null && !comboBox04.getValue().isEmpty()) {
            txtField05.setDisable(!lbShow);
        }
        if (!txtField05.getText().trim().isEmpty()) {
            switch (comboBox04.getSelectionModel().getSelectedIndex()) {
                case 0:
                case 3:
                case 4:
                    txtField10.setDisable(!lbShow);
                    break;
            }
        }

        if (checkBoxNoPymnt.isSelected()) {
            psPayMode.clear();
            CustomCommonUtil.setSelected(false,
                    checkBoxCash, checkBoxCheck, checkBoxCard, checkBoxOnlnPymntServ,
                    checkBoxCrdInv, checkBoxCheck, checkBoxGftCheck
            );
            CustomCommonUtil.setDisable(true,
                    checkBoxCheck, checkBoxCard, checkBoxOnlnPymntServ,
                    checkBoxCrdInv, checkBoxCheck, checkBoxGftCheck
            );

        }
        if (pnEditMode == EditMode.ADDNEW) {
            txtField01.setDisable(!lbShow);
        }
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

    private void setDoctTypeFromTitleForm() {
        switch (lblInvoiceTitle.getText()) {
            case "ACKNOWLEDGEMENT RECEIPT":
                oTrans.getMasterModel().getMasterModel().setDocType("7");
                break;
            case "BILLING STATEMENT":
                oTrans.getMasterModel().getMasterModel().setDocType("6");
                break;
            case "COLLECTION RECEIPT":
                oTrans.getMasterModel().getMasterModel().setDocType("4");
                break;
            case "SERVICE INVOICE":
                oTrans.getMasterModel().getMasterModel().setDocType("2");
                break;
            case "PART SALES INVOICE":
                oTrans.getMasterModel().getMasterModel().setDocType("5");
                break;
            case "VEHICLE SALES INVOICE":
                oTrans.getMasterModel().getMasterModel().setDocType("0");
                break;
        }
    }

    private void setCARValueToSI() {
        oTrans.getMasterModel().getMasterModel().setClientID(oTransCAR.getMasterModel().getMasterModel().getPayerID());
        oTrans.getMasterModel().getMasterModel().setBuyCltNm(oTransCAR.getMasterModel().getMasterModel().getPayerNme());
        oTrans.getMasterModel().getMasterModel().setAddress(oTransCAR.getMasterModel().getMasterModel().getPayerAdd());
        oTrans.getMasterModel().getMasterModel().setTaxIDNo(oTransCAR.getMasterModel().getMasterModel().getTaxIDNo());
        for (int lnCtr = 0; lnCtr <= oTransCAR.getDetailList().size() - 1; lnCtr++) {
            oTrans.addSIDetail();
            if (oTransCAR.getMasterModel().getMasterModel().getVSPNo() != null) {
                oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setSourceCD("VSP");
            }
            if (oTransCAR.getMasterModel().getMasterModel().getVSANo() != null) {
                oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setSourceCD("VSA");
            }
            if (oTransCAR.getMasterModel().getMasterModel().getInsAppNo() != null) {
                oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setSourceCD("POL");
            }
            oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setTranAmt(oTransCAR.getDetailModel().getDetailModel(lnCtr).getTotalAmt());
            oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setNetAmt(oTransCAR.getDetailModel().getDetailModel(lnCtr).getTotalAmt());
            oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setTranType(oTransCAR.getDetailModel().getDetailModel(lnCtr).getTranType());

            oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setDescript(oTransCAR.getMasterModel().getMasterModel().getSourceCD());
            oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setFormNo(oTransCAR.getMasterModel().getMasterModel().getFormNo());
            oTrans.getSIDetailModel().getDetailModel(oTrans.getSIDetailList().size() - 1).setSourceNo(oTransCAR.getMasterModel().getMasterModel().getTransNo());
        }
        oTrans.computeSIAmount(true);
    }

    private void loadTransTable() {
        transData.clear();
        String lsFormType = "";
        String lsAcctTtle = "";
        String lsRefNoxxx = "";
        String lsPartculr = "";
        String lsGrossAmt = "";
        String lsDiscAmtx = "";
        String lsAdvAmtxx = "";
        String lsTtlAmtxx = "";
        String lsFormNoxx = "";
        String lsCARNoxxx = "";
        for (int lnCntr = 0; lnCntr <= oTrans.getSIDetailList().size() - 1; lnCntr++) {
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getSourceCD() != null) {
                lsFormType = oTrans.getSIDetailModel().getDetailModel(lnCntr).getSourceCD();
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getTranType() != null) {
                lsAcctTtle = oTrans.getSIDetailModel().getDetailModel(lnCntr).getTranType();
            }
            if (lsFormType.isEmpty()) {
                if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getFormNo() != null) {
                    lsRefNoxxx = oTrans.getSIDetailModel().getDetailModel(lnCntr).getFormNo();
                }
            } else {
                if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getFormNo() != null) {
                    lsRefNoxxx = lsFormType + "# " + oTrans.getSIDetailModel().getDetailModel(lnCntr).getFormNo();
                }
            }

            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getDescript() != null) {
                lsPartculr = oTrans.getSIDetailModel().getDetailModel(lnCntr).getDescript();
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getDiscount() != null) {
                lsDiscAmtx = CustomCommonUtil.setDecimalFormat(oTrans.getSIDetailModel().getDetailModel(lnCntr).getDiscount());
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getAdvused() != null) {
                lsAdvAmtxx = CustomCommonUtil.setDecimalFormat(oTrans.getSIDetailModel().getDetailModel(lnCntr).getAdvused());
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getNetAmt() != null) {
                lsTtlAmtxx = CustomCommonUtil.setDecimalFormat(oTrans.getSIDetailModel().getDetailModel(lnCntr).getNetAmt());
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getTranAmt() != null) {
                lsGrossAmt = CustomCommonUtil.setDecimalFormat(oTrans.getSIDetailModel().getDetailModel(lnCntr).getTranAmt());
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getFormNo() != null) {
                lsFormNoxx = oTrans.getSIDetailModel().getDetailModel(lnCntr).getFormNo();
            }
            if (oTrans.getSIDetailModel().getDetailModel(lnCntr).getSourceNo() != null) {
                lsCARNoxxx = oTrans.getSIDetailModel().getDetailModel(lnCntr).getSourceNo();
            }
            transData.add(new TransInvoice(
                    String.valueOf(lnCntr + 1),
                    lsFormType,
                    lsAcctTtle,
                    lsRefNoxxx,
                    lsPartculr,
                    lsGrossAmt,
                    lsDiscAmtx,
                    lsAdvAmtxx,
                    lsTtlAmtxx,
                    lsFormNoxx,
                    lsCARNoxxx
            ));
            lsFormType = "";
            lsAcctTtle = "";
            lsRefNoxxx = "";
            lsPartculr = "";
            lsGrossAmt = "";
            lsDiscAmtx = "";
            lsAdvAmtxx = "";
            lsTtlAmtxx = "";
            lsFormNoxx = "";
            lsCARNoxxx = "";
        }
        tblViewTrans.setItems(transData);
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
        tblindexTrans11.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        tblViewTrans.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewTrans.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        transData.clear();
        tblViewTrans.setItems(transData);
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
        checkData.clear();
        String lsPayMdex = "";
        String lsRefNoxx = "";
        String lsDatexxx = "";
        String lsAmountx = "";
        String lsAllAmtx = "";
        String lsPymtSrc = "";
        String lsRemarks = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getSIPaymentList().size() - 1; lnCtr++) {
            if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayMode() != null) {
                switch (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayMode()) {
                    case "CARD":
                        lsPayMdex = "CARD";
                        if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getCCCardNo() != null) {
                            lsRefNoxx = oTrans.getSIPaymentModel().getDetailModel(lnCtr).getCCCardNo();
                        }
                        if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayAmt() != null) {
                            lsAmountx = CustomCommonUtil.setDecimalFormat(oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayAmt());
                            lsAllAmtx = CustomCommonUtil.setDecimalFormat(oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayAmt());
                        }
                        if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getCCRemarks() != null) {
                            lsRemarks = oTrans.getSIPaymentModel().getDetailModel(lnCtr).getCCRemarks();
                        }
                        break;
                    case "CHECK":
                        lsPayMdex = "CHECK";
                        break;
                    case "GC":
                        lsPayMdex = "GIFT CHECK";
                        if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getGCertNo() != null) {
                            lsRefNoxx = oTrans.getSIPaymentModel().getDetailModel(lnCtr).getGCertNo();
                        }
                        if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayAmt() != null) {
                            lsAmountx = CustomCommonUtil.setDecimalFormat(oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayAmt());
                            lsAllAmtx = CustomCommonUtil.setDecimalFormat(oTrans.getSIPaymentModel().getDetailModel(lnCtr).getPayAmt());
                        }
                        if (oTrans.getSIPaymentModel().getDetailModel(lnCtr).getGCRemrks() != null) {
                            lsRemarks = oTrans.getSIPaymentModel().getDetailModel(lnCtr).getGCRemrks();
                        }
                        break;
                    case "OP":
                        lsPayMdex = "ONLINE PAYMENT";
                        break;
                }
            }
            checkData.add(new CheckInvoice(
                    String.valueOf(lnCtr + 1),
                    lsPayMdex,
                    lsRefNoxx,
                    lsDatexxx,
                    lsAmountx,
                    lsAllAmtx,
                    lsPymtSrc,
                    lsRemarks,
                    ""));
            lsPayMdex = "";
            lsRefNoxx = "";
            lsDatexxx = "";
            lsAmountx = "";
            lsAllAmtx = "";
            lsPymtSrc = "";
            lsRemarks = "";
        }
        tblViewCheck.setItems(checkData);
    }

    private void loadPaymentDetailWindow(int fnRow, boolean fbIsUpdate) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/InvoicePaymentDetails.fxml"));
            InvoicePaymentDetailsController loControl = new InvoicePaymentDetailsController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setPayMode(psPayMode);
            loControl.setIsUpdate(fbIsUpdate);
            loControl.setRow(fnRow);
            fxmlLoader.setController(loControl);

            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadCheckTable();
            loadMasterFields();
            loadPayModeCheckedFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }

    private String getParentTabTitle() {
        Node parent = AnchorMain.getParent();
        Parent tabContentParent = parent.getParent();

        if (tabContentParent instanceof TabPane) {
            TabPane tabPane = (TabPane) tabContentParent;
            Tab tab = findTabByContent(tabPane, AnchorMain);
            if (tab != null) {
                lblInvoiceTitle.setText(tab.getText().toUpperCase());
                pxeModuleName = lblInvoiceTitle.getText().toLowerCase();
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

    //TableView KeyPressed
    private void initTableKeyPressed() {
        tblViewTrans.setOnKeyPressed((KeyEvent event) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this transaction?")) {
                        TransInvoice selectedTrans = getTransSelectedItem();
                        int removeCount = 0;
                        if (selectedTrans != null) {
                            String lsRow = selectedTrans.getTblindex01();
                            int lnRow = Integer.parseInt(lsRow);
                            oTrans.removeSIDetail(lnRow - 1);
                            removeCount++;
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, toTitleCase(pxeModuleName), "Removed transaction successfully");
                        } else {
                            ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), "Removed transaction failed");
                        }
                        loadPayModeCheckedFields();
                        loadMasterFields();
                        loadTransTable();
                    }
                }
            }
        }
        );
        tblViewCheck.setOnKeyPressed((KeyEvent event) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this payment details?")) {
                        CheckInvoice selectedCheck = getCheckSelectedItem();
                        int removeCount = 0;
                        if (selectedCheck != null) {
                            String lsRow = selectedCheck.getTblindex01();
                            int lnRow = Integer.parseInt(lsRow);
                            oTrans.removeSIPayment(lnRow - 1);
                            removeCount++;
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, toTitleCase(pxeModuleName), "Removed payment details successfully");
                        } else {
                            ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), "Removed payment details failed");
                        }
                        loadPayModeCheckedFields();
                        loadCheckTable();
                        loadMasterFields();
                        loadPayModeCheckedFields();
                    }
                }
            }
        });

    }

    private TransInvoice getTransSelectedItem() {
        return tblViewTrans.getSelectionModel().getSelectedItem();
    }

    private CheckInvoice getCheckSelectedItem() {
        return tblViewCheck.getSelectionModel().getSelectedItem();
    }

    private void loadPayModeCheckedFields() {
        for (CheckInvoice item : tblViewCheck.getItems()) {
            String lsPayMode = item.getTblindex02();
            if (lsPayMode.equals("CARD")) {
                checkBoxCard.setSelected(true);
                psPayMode.add("CARD");
            }
            if (lsPayMode.equals("CHECK")) {
                checkBoxCheck.setSelected(true);
                psPayMode.add("CHECK");
            }
            if (lsPayMode.equals("GIFT CHECK")) {
                checkBoxGftCheck.setSelected(true);
                psPayMode.add("GC");
            }
            if (lsPayMode.equals("ONLINE PAYMENT")) {
                checkBoxOnlnPymntServ.setSelected(true);
                psPayMode.add("OP");
            }
            if (lsPayMode.equals("CM")) {
                checkBoxCrdInv.setSelected(true);
                psPayMode.add("CM");
            }
        }
        if (!tblViewTrans.getItems().isEmpty()) {
            checkBoxCash.setSelected(true);
        }
    }

    @FXML
    private void tblCheckOther_Click(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewCheck.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewCheck.getItems().size()) {
                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), "Please select valid payment details.");
                return;
            }

            if (event.getClickCount() == 2) {
                loadPaymentDetailWindow(pnRow, true);
            }
        }
    }

    @FXML
    private void tblTrans_Click(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewTrans.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewTrans.getItems().size()) {
                ShowMessageFX.Warning(null, toTitleCase(pxeModuleName), "Please select valid transaction details.");
                return;
            }
            if (event.getClickCount() == 2) {
                loadInsertDetailWindow(pnRow);
            }

        }
    }

    private void loadAdvancesDetailWindow(int fnRow) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/PRDeductible.fxml"));
            PRDeductibleController loControl = new PRDeductibleController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }

    private void loadInsertDetailWindow(int fnRow) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/InvcInsertDetail.fxml"));
            InvcInsertDetailController loControl = new InvcInsertDetailController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadTransTable();
            loadMasterFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }

    private void loadInsertOtherDetailWindow(int fnRow) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/InvcInsertOtherDetail.fxml"));
            InvcInsertOtherDetailController loControl = new InvcInsertOtherDetailController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadTransTable();
            loadMasterFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }

    public static String toTitleCase(String fsValue) {
        String[] words = fsValue.split("\\s+");
        StringBuilder titleCase = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return titleCase.toString().trim();
    }
}
