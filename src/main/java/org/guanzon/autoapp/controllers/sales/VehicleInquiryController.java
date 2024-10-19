package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.sales.BankApplication;
import org.guanzon.auto.main.sales.FollowUp;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.FXMLDocumentController;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.models.sales.InquiryFollowUp;
import org.guanzon.autoapp.models.sales.InquiryPromoOffered;
import org.guanzon.autoapp.models.sales.InquiryRequirements;
import org.guanzon.autoapp.models.sales.InquiryVehiclePriority;
import org.guanzon.autoapp.models.sales.InquiryVehicleSalesAdvances;
import org.guanzon.autoapp.models.sales.InquiryVehicleBankApplications;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleInquiryController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private Inquiry oTrans;
    private BankApplication oTransBank;
    private FollowUp oTransFollow;
    private String pxeModuleName = "Vehicle Inquiry";
    private int pnEditMode;
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    UnloadForm poUnload = new UnloadForm(); //Object for closing form
    private int iTabIndex = 0; //Set tab index
    private int tabPinEditMode;
    //    /* ------------------DATA TABLES----------------------- */
    private ObservableList<InquiryVehiclePriority> priorityunitdata = FXCollections.observableArrayList();
    private ObservableList<InquiryPromoOffered> promosoffereddata = FXCollections.observableArrayList();
    private ObservableList<InquiryRequirements> inqrequirementsdata = FXCollections.observableArrayList();
    private ObservableList<InquiryVehicleSalesAdvances> inqvsadata = FXCollections.observableArrayList();
    private ObservableList<InquiryVehicleBankApplications> bankappdata = FXCollections.observableArrayList();
    private ObservableList<InquiryFollowUp> followupdata = FXCollections.observableArrayList();

    //    /* ------------------COMBO BOX ITEMS/VALUE----------------------- */
    private ObservableList<String> cInquirySourceType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO"); //Inquiry Type values
    private ObservableList<String> cInqStatus = FXCollections.observableArrayList("FOR FOLLOW-UP", "ON PROCESS", "LOST SALE", "WITH VSP", "SOLD", "CANCELLED"); //Inquiry Type Values
    private ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    private ObservableList<String> cCustomerType = FXCollections.observableArrayList("BUSINESS", "EMPLOYED", "OFW", "SEAMAN", "ANY"); // Customer Type Values
    private ObservableList<String> cTargetVehcl = FXCollections.observableArrayList("BRANDNEW", "PRE-OWNED");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnConvertSales, btnLostSale, btnProcess, btnCancel, btnClose, btnBrowse;
    @FXML
    private Label lblInqStatus;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tabCustomerInquiry;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
            txtField11, txtField12, txtField13, txtField14, txtField16, txtField18, txtField20, txtField27, txtField29, txtField30;
    @FXML
    private ToggleGroup hotCategoryToggleGroup;
    @FXML
    private TableView<InquiryVehiclePriority> tblPriorityUnit;
    @FXML
    private TableColumn<InquiryVehiclePriority, String> trgvIndex01, trgvIndex02;
    @FXML
    private TableColumn<InquiryVehiclePriority, Button> trgvIndex03, trgvIndex04;
    @FXML
    private Button btnTargetVhclRemove, btnTargetVhclAdd;
    @FXML
    private Button btnPromoRemove, btnPromoAdd;
    @FXML
    private TableView<InquiryPromoOffered> tblPromosOffered;
    @FXML
    private TableColumn<InquiryPromoOffered, String> prmoIndex01, prmoIndex02, prmoIndex03, prmoIndex04;
    @FXML
    private Tab tabInquiryProcess;
    @FXML
    private TableView tblRequirementsInfo;
    @FXML
    private TableColumn rqrmIndex01;
    @FXML
    private TableColumn rqrmIndex02, rqrmIndex03, rqrmIndex04, rqrmIndex05;
    @FXML
    private Button btnASadd;
    @FXML
    private Button btnASremove;
    @FXML
    private Button btnASprint, btnASCancel;
    @FXML
    private TableView<InquiryVehicleSalesAdvances> tblAdvanceSlip;
    @FXML
    private TableColumn<InquiryVehicleSalesAdvances, Boolean> vsasCheck01;
    @FXML
    private TableColumn<InquiryVehicleSalesAdvances, String> vsasIndex01, vsasIndex02, vsasIndex03, vsasIndex04, vsasIndex05, vsasIndex06, vsasIndex07;
    @FXML
    private Button btnSndMngerApprov;
    @FXML
    private Tab tabBankHistory;
    @FXML
    private Button btnBankAppNew;
    @FXML
    private TableView<InquiryVehicleBankApplications> tblBankApplication;
    @FXML
    private TableColumn<InquiryVehicleBankApplications, String> bankIndex01, bankIndex02, bankIndex03, bankIndex04, bankIndex05, bankIndex06, bankIndex07, bankIndex08;
    @FXML
    private Tab tabFollowingHistory;
    @FXML
    private Button btnFollowUp;
    @FXML
    private TableView<InquiryFollowUp> tblFollowHistory;
    @FXML
    private TableColumn<InquiryFollowUp, String> flwpIndex01, flwpIndex02, flwpIndex03, flwpIndex04, flwpIndex05, flwpIndex06, flwpIndex07, flwpIndex08;
    @FXML
    private ComboBox<String> comboBox10, comboBox21, comboBox25, comboBox26;
    @FXML
    private RadioButton rdbtnHtA19, rdbtnHtB19, rdbtnHtC19;
    @FXML
    private DatePicker datePicker22;
    @FXML
    private TextArea textArea04, textArea23, textArea24, textArea28;
    @FXML
    private Button btnTestDriveModel;
    @FXML
    private TableView<?> tblViewPymntConv;
    @FXML
    private TableColumn<?, ?> tblindexPaymentConv01;
    @FXML
    private TableColumn<?, ?> tblindexPaymentConv02;

    private FXMLDocumentController fdController = new FXMLDocumentController();

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Inquiry(oApp, false, oApp.getBranchCode());
        oTransBank = new BankApplication(oApp, false, oApp.getBranchCode());
        oTransFollow = new FollowUp(oApp, false, oApp.getBranchCode());

        //initialized tables
        initVehiclePriority();
        initPromoOffered();
        initInquiryRequirements();
        initAdvancesSlip();
        initBankApplications();
        initFollowHistory();

        datePicker22.setDayCellFactory(targetDate);

        initCapitalizationFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField16, txtField18, txtField20, txtField27, txtField29, txtField30);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea04, textArea23, textArea24, textArea28);

        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getInqryID());
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null && !oTrans.getMasterModel().getMasterModel().getTransactDte().toString().isEmpty()) {
            txtField02.setText(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTransactDte()));
        }
        txtField03.setText(oTrans.getMasterModel().getMasterModel().getClientNm());
        textArea04.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField05.setText(oTrans.getMasterModel().getMasterModel().getContctNm());
        txtField06.setText(oTrans.getMasterModel().getMasterModel().getMobileNo());
        txtField07.setText(oTrans.getMasterModel().getMasterModel().getEmailAdd());
        txtField08.setText(oTrans.getMasterModel().getMasterModel().getAccount());
        txtField09.setText(oTrans.getMasterModel().getMasterModel().getSalesExe());
        if (oTrans.getMasterModel().getMasterModel().getSourceCD() != null && !oTrans.getMasterModel().getMasterModel().getSourceCD().trim().isEmpty()) {
            comboBox10.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getSourceCD()));
        }
        txtField11.setText(oTrans.getMasterModel().getMasterModel().getPlatform());
        txtField12.setText(oTrans.getMasterModel().getMasterModel().getActTitle());
        txtField13.setText(oTrans.getMasterModel().getMasterModel().getSalesAgn());
        txtField14.setText(oTrans.getMasterModel().getMasterModel().getBranchNm());
        txtField18.setText(poGetDecimalFormat.format(Double.parseDouble("0.00")));
        switch (oTrans.getMasterModel().getMasterModel().getIntrstLv()) {
            case "a":
                rdbtnHtA19.setSelected(true);
                break;
            case "b":
                rdbtnHtB19.setSelected(true);
                break;
            case "c":
                rdbtnHtC19.setSelected(true);
                break;
            default:
                rdbtnHtA19.setSelected(false);
                rdbtnHtB19.setSelected(false);
                rdbtnHtC19.setSelected(false);
                break;
        }

        txtField20.setText(oTrans.getMasterModel().getMasterModel().getTestModl());
        if (oTrans.getMasterModel().getMasterModel().getIsVhclNw() != null && !oTrans.getMasterModel().getMasterModel().getIsVhclNw().trim().isEmpty()) {
            comboBox21.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getIsVhclNw()));
        }
        if (oTrans.getMasterModel().getMasterModel().getTargetDt() != null && !oTrans.getMasterModel().getMasterModel().getTargetDt().toString().isEmpty()) {
            datePicker22.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTargetDt())));
        }
        textArea23.setText(oTrans.getMasterModel().getMasterModel().getRemarks());
        textArea24.setText("");

        String inqStats = "";
        if (oTrans.getMasterModel().getMasterModel().getTranStat() != null) {
            switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
                case "0":
                    inqStats = "FOR FOLLOW-UP";
                    break;
                case "1":
                    inqStats = "ON PROCESS";
                    break;
                case "2":
                    inqStats = "LOST SALE";
                    break;
                case "3":
                    inqStats = "WITH VSP";
                    break;
                case "4":
                    inqStats = "SOLD";
                    break;
                case "5":
                    inqStats = "CANCELLED";
                    break;
                case "6":
                    inqStats = "FOR APPROVAL";
                    break;
            }
            lblInqStatus.setText(inqStats);
        }
        if (oTrans.getMasterModel().getMasterModel().getPayMode() != null && !oTrans.getMasterModel().getMasterModel().getPayMode().trim().isEmpty()) {
            String sModeOfPayment = "";
            switch (oTrans.getMasterModel().getMasterModel().getPayMode()) {
                case "0":
                    sModeOfPayment = "CASH";
                    break;
                case "1":
                    sModeOfPayment = "BANK PURCHASE ORDER";
                    break;
                case "2":
                    sModeOfPayment = "BANK FINANCING";
                    break;
                case "3":
                    sModeOfPayment = "COMPANY PURCHASE ORDER";
                    break;
                case "4":
                    sModeOfPayment = "COMPANY FINANCING";
                    break;

            }
            comboBox25.setValue(sModeOfPayment);
        }
        if (oTrans.getMasterModel().getMasterModel().getCustGrp() != null && !oTrans.getMasterModel().getMasterModel().getCustGrp().trim().isEmpty()) {
            String sCustomerType = "";
            switch (oTrans.getMasterModel().getMasterModel().getCustGrp()) {
                case "0":
                    sCustomerType = "BUSINESS";
                    break;
                case "1":
                    sCustomerType = "EMPLOYED";
                    break;
                case "2":
                    sCustomerType = "OFW";
                    break;
                case "3":
                    sCustomerType = "SEAMAN";
                    break;
                case "4":
                    sCustomerType = "ANY";
                    break;

            }
            comboBox26.setValue(sCustomerType);
        }
        String lsPlateCSNo = "";
        if (oTrans.getMasterModel().getMasterModel().getPlateNo() != null) {
            lsPlateCSNo = oTrans.getMasterModel().getMasterModel().getPlateNo();
        } else {
            lsPlateCSNo = oTrans.getMasterModel().getMasterModel().getCSNo();
            System.out.println("cs no: " + oTrans.getMasterModel().getMasterModel().getPlateNo());
        }
        txtField27.setText(lsPlateCSNo);
        textArea28.setText(oTrans.getMasterModel().getMasterModel().getDescript());
        txtField29.setText("");
        txtField29.setText("");
        txtField30.setText("");
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
        List<TextArea> loTxtArea = Arrays.asList(textArea23);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
    }

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
                case 23:
                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField16, txtField18, txtField20, txtField27, txtField29, txtField30);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea04, textArea23, textArea24, textArea28);
        loTxtArea.forEach(ta -> ta.setOnKeyPressed(event -> textArea_KeyPressed(event)));
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
                case ENTER:
                case F3:
                    switch (txtFieldID) {
                        case "txtField03":
                            loJSON = oTrans.searchClient(lsValue, true);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField03.setText(oTrans.getMasterModel().getMasterModel().getClientNm());
                                textArea04.setText(oTrans.getMasterModel().getMasterModel().getAddress());
                                txtField06.setText(oTrans.getMasterModel().getMasterModel().getMobileNo());
                                txtField07.setText(oTrans.getMasterModel().getMasterModel().getEmailAdd());
                                txtField08.setText(oTrans.getMasterModel().getMasterModel().getAccount());
                                if (oTrans.getMasterModel().getMasterModel().getClientTp().equals("0")) {
                                    oTrans.getMasterModel().getMasterModel().setContctID("");
                                    oTrans.getMasterModel().getMasterModel().setContctNm("");
                                    txtField05.setText("");
                                }
                                checkExistingInquiryInformation(true);
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField03.setText("");
                                txtField03.requestFocus();
                                return;
                            }
                            initFields(pnEditMode);
                            break;
                        case "txtField05":
                            loJSON = oTrans.searchClient(lsValue, false);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField05.setText(oTrans.getMasterModel().getMasterModel().getContctNm());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField05.setText("");
                                txtField05.requestFocus();
                                return;
                            }
                            break;
                        case "txtField09":
                            loJSON = oTrans.searchSalesExecutive(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField09.setText(oTrans.getMasterModel().getMasterModel().getSalesExe());
                                checkExistingInquiryInformation(false);
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField09.setText("");
                                txtField09.requestFocus();
                                return;
                            }
                            break;
                        case "txtField11":
                            loJSON = oTrans.searchOnlinePlatform(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField11.setText(oTrans.getMasterModel().getMasterModel().getPlatform());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField11.setText("");
                                txtField11.requestFocus();
                                return;
                            }
                            break;
                        case "txtField12":
                            loJSON = oTrans.searchActivity(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField12.setText(oTrans.getMasterModel().getMasterModel().getActTitle());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField12.setText("");
                                txtField12.requestFocus();
                                return;
                            }
                            break;
                        case "txtField13":
                            loJSON = oTrans.searchReferralAgent(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField13.setText(oTrans.getMasterModel().getMasterModel().getSalesAgn());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField13.setText("");
                                txtField13.requestFocus();
                                return;
                            }
                            break;
                        case "txtField14":
                            loJSON = oTrans.searchBranch(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField14.setText(oTrans.getMasterModel().getMasterModel().getBranchNm());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField14.setText("");
                                return;
                            }
                            break;
                        case "txtField27":
                            loJSON = oTrans.searchAvlVhcl(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                String lsPlateCs = "";
                                if (!oTrans.getMasterModel().getMasterModel().getPlateNo().isEmpty()) {
                                    lsPlateCs = String.valueOf(oTrans.getMasterModel().getMasterModel().getPlateNo());
                                } else {
                                    lsPlateCs = String.valueOf(oTrans.getMasterModel().getMasterModel().getCSNo());
                                }
                                txtField27.setText(lsPlateCs);
                                textArea28.setText(String.valueOf(oTrans.getMasterModel().getMasterModel().getDescript()));
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField27.setText("");
                                txtField27.requestFocus();
                                return;
                            }
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnConvertSales, btnLostSale, btnProcess, btnCancel,
                btnClose, btnTestDriveModel,
                btnTargetVhclRemove, btnTargetVhclAdd,
                btnPromoAdd, btnPromoRemove, btnSndMngerApprov,
                btnASadd, btnASremove, btnASprint, btnASCancel,
                btnBankAppNew,
                btnFollowUp);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        iTabIndex = tabPaneMain.getSelectionModel().getSelectedIndex();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                CustomCommonUtil.switchToTab(tabCustomerInquiry, tabPaneMain);// Load fields, clear them, and set edit mode
                oTrans = new Inquiry(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    switch (tabPaneMain.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            tabPinEditMode = 0;
                            break;
                        case 1:
                            tabPinEditMode = 1;
                            break;
                    }
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                    initBtnProcess(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnEdit":
                switch (iTabIndex) {
                    case 0:
                        tabPinEditMode = 0;
                        break;
                    case 1:
                        tabPinEditMode = 1;
                        break;
                }
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Inquiry Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.READY;
                    } else {
                        pnEditMode = oTrans.getEditMode();
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    clearFields();
                    clearTables();
                    loadMasterFields();
                    loadVehiclePriority();
                    loadPromoOffered();
                    loadInquiryRequirements();
                    loadAdvancesSlip();
                    loadBankApplications();
                    loadFollowHistory();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                    initBtnProcess(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Inquiry Information Confirmation", (String) loJSON.get("message"));
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                    initBtnProcess(pnEditMode);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Inquiry Information Saving....", "Are you sure, do you want to save?")) {
                    switch (iTabIndex) {
                        case 0:
                        case 1:
                            for (int lnCtr = 0; lnCtr <= oTrans.getVehiclePriorityList().size() - 1; lnCtr++) {
                                if (oTrans.getVehiclePriority(lnCtr, "nPriority").equals(1)) {
                                    oTrans.getMasterModel().getMasterModel().setVhclID(oTrans.getVehiclePriority(lnCtr, "sVhclIDxx").toString());
                                    break;
                                }
                            }
                            if (setSelection()) {
                                loJSON = oTrans.saveTransaction();
                                if ("success".equals((String) loJSON.get("result"))) {
                                    ShowMessageFX.Information(null, "Vehicle Inquiry Information", (String) loJSON.get("message"));
                                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                                    if ("success".equals((String) loJSON.get("result"))) {
                                        loadMasterFields();
                                        loadVehiclePriority();
                                        loadPromoOffered();
                                        loadInquiryRequirements();
                                        loadAdvancesSlip();
                                        loadBankApplications();
                                        loadFollowHistory();
                                        initFields(pnEditMode);
                                        pnEditMode = oTrans.getEditMode();
                                    }
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    return;
                                }
                            }
                            break;
                    }
                } else {
                    return;
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Vehicle Inquiry");
                    } else {
                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    CustomCommonUtil.switchToTab(tabCustomerInquiry, tabPaneMain);// Load fields, clear them, and set edit mode
                    oTrans = new Inquiry(oApp, false, oApp.getBranchCode());
                    tabPinEditMode = 0;
                    pnEditMode = EditMode.UNKNOWN;
                    initFields(pnEditMode);
                }
                break;
            case "btnTargetVhclAdd":
                loJSON = oTrans.searchVehicle();
                if (!"error".equals((String) loJSON.get("result"))) {
                    boolean isExist = false;
                    for (lnCtr = 0; lnCtr <= oTrans.getVehiclePriorityList().size() - 1; lnCtr++) {
                        if (String.valueOf(oTrans.getVehiclePriority(lnCtr, "sVhclIDxx")).equals((String) loJSON.get("sVhclIDxx"))) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        oTrans.addVehiclePriority();
                        oTrans.setVehiclePriority(oTrans.getVehiclePriorityList().size() - 1, "nPriority", oTrans.getVehiclePriorityList().size());
                        oTrans.setVehiclePriority(oTrans.getVehiclePriorityList().size() - 1, "sVhclIDxx", (String) loJSON.get("sVhclIDxx"));
                        oTrans.setVehiclePriority(oTrans.getVehiclePriorityList().size() - 1, "sDescript", (String) loJSON.get("sDescript"));
                        ShowMessageFX.Information(null, pxeModuleName, "Vehicle Priority added successfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Vehicle is already exist.");
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                loadVehiclePriority();
                break;
            case "btnTargetVhclRemove":
                if (ShowMessageFX.YesNo(null, "Vehicle Confirmation", "Are you sure you want to remove this Vehicle Information?")) {
                } else {
                    return;
                }
                oTrans.removeVehiclePriority(pnRow);
                pnRow = 0;
                loadVehiclePriority();
                break;
            case "btnPromoAdd":
                loJSON = oTrans.searchPromo();
                if (!"error".equals((String) loJSON.get("result"))) {
                    boolean isExist = false;
                    for (lnCtr = 0; lnCtr <= oTrans.getPromoList().size() - 1; lnCtr++) {
                        if (String.valueOf(oTrans.getPromo(lnCtr, "sPromoIDx")).equals((String) loJSON.get("sActvtyID"))) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        oTrans.addPromo();
                        oTrans.setPromo(oTrans.getPromoList().size() - 1, "sPromoIDx", (String) loJSON.get("sActvtyID"));
                        oTrans.setPromo(oTrans.getPromoList().size() - 1, "sActNoxxx", (String) loJSON.get("sActNoxxx"));
                        oTrans.setPromo(oTrans.getPromoList().size() - 1, "sActTitle", (String) loJSON.get("sActTitle"));
                        oTrans.setPromo(oTrans.getPromoList().size() - 1, "dDateFrom", SQLUtil.toDate((String) loJSON.get("dDateFrom"), SQLUtil.FORMAT_SHORT_DATE));
                        oTrans.setPromo(oTrans.getPromoList().size() - 1, "dDateThru", SQLUtil.toDate((String) loJSON.get("dDateThru"), SQLUtil.FORMAT_SHORT_DATE));
                        ShowMessageFX.Information(null, pxeModuleName, "Promo added successfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Promo is already exist.");
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                loadPromoOffered();
                break;
            case "btnPromoRemove":
                if (ShowMessageFX.YesNo(null, "Promo Confirmation", "Are you sure you want to remove this Promo?")) {
                } else {
                    return;
                }
                oTrans.removePromo(pnRow);
                pnRow = 0;
                loadPromoOffered();
                break;
            case "btnTestDriveModel":
                laodTestModelWindow();
                break;
            case "btnProcess":
                if (ShowMessageFX.YesNo(null, "Vehicle Inquiry Information Saving....", "Are you sure, do you want to process?")) {
                    if (oTrans.getMasterModel().getMasterModel().getTranStat().equals("0")) {
                        oTrans.getMasterModel().getMasterModel().setTranStat("1");
                    }
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadVehiclePriority();
                            loadPromoOffered();
                            loadInquiryRequirements();
                            loadAdvancesSlip();
                            loadBankApplications();
                            loadFollowHistory();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        oTrans.getMasterModel().getMasterModel().setTranStat("0");
                        return;
                    }
                } else {
                    return;
                }

                break;
            case "btnASadd":
                try {
                oTrans.addReservation();
                loadVehicleSalesAdvancesWindow(oTrans.getReservationList().size() - 1, true, pnEditMode);
                loadAdvancesSlip();
                initFields(pnEditMode);
            } catch (SQLException ex) {
                Logger.getLogger(VehicleInquiryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnASremove":
            case "btnASCancel":
            case "btnASprint":
                int lnRow = 0;
                int pnCtr = 1;
                ObservableList<InquiryVehicleSalesAdvances> selectedItems = FXCollections.observableArrayList();

                for (InquiryVehicleSalesAdvances item : tblAdvanceSlip.getItems()) {
                    if (item.getSelect().isSelected()) {
//                        selectedItems.add(item);
                        if ("btnASremove".equals(lsButton) && pnCtr > 1) {
                            ShowMessageFX.Warning(getStage(), "Please select atleast 1 slip to be removed.", pxeModuleName, null);
                            return;
                        }
                        if ("btnASCancel".equals(lsButton) && pnCtr > 1) {
                            ShowMessageFX.Warning(getStage(), "Please select atleast 1 slip to be cancelled.", pxeModuleName, null);
                            return;
                        }
                        switch (oTrans.getReservation(lnRow, "cTranStat").toString()) {
                            case "1":
                                if ("btnASprint".equals(lsButton)) {
                                    ShowMessageFX.Warning(getStage(), "Slip No. " + oTrans.getReservation(lnRow, 3).toString() + " is not yet approved. Printing Aborted.", pxeModuleName, null);
                                    return;
                                } else {
                                    selectedItems.add(item);
                                }
                                break;

                        }
                        pnCtr++;
                    }
                    lnRow++;
                }

                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Warning(getStage(), "No items selected!", pxeModuleName, null);
                } else {
                    switch (lsButton) {
                        case "btnASCancel":
                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel?")) {
                                boolean lbIsCancelled = false;
                                for (InquiryVehicleSalesAdvances item : selectedItems) {
                                    lnRow = Integer.valueOf(item.getTblindex01()) - 1; // Assuming there is a method to retrieve the transaction number
                                    if (lnRow >= 0) {
                                        loJSON = oTrans.cancelReservation(lnRow);
                                        if ("success".equals((String) loJSON.get("result"))) {
                                            lbIsCancelled = true;
                                            oTrans.loadReservationList();
                                            loadAdvancesSlip();
                                        } else {
                                            lbIsCancelled = false;
                                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                        }
                                    }
                                }
                                if (lbIsCancelled) {
                                    ShowMessageFX.Information(getStage(), "Reservation cancelled successfully.", pxeModuleName, null);
                                }
                            }
                            break;
                        case "btnASremove":
                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                                for (InquiryVehicleSalesAdvances item : selectedItems) {
                                    lnRow = Integer.parseInt(item.getTblindex01()) - 1; // Assuming there is a method to retrieve the transaction number
                                    if (lnRow >= 0) {
                                        if (String.valueOf(oTrans.getReservation(lnRow, "sEntryByx")).trim().isEmpty()) {
                                            oTrans.removeReservation(lnRow);
                                            System.out.println("status: " + oTrans.getMasterModel().getMasterModel().getTranStat());
                                        } else {
                                            ShowMessageFX.Warning(null, pxeModuleName, "Reservation No. " + String.valueOf(oTrans.getReservation(lnRow, "sReferNox")) + " is already saved.\n\nCancel reservation instead.");
                                        }
                                    }
                                }
                            } else {
                                return;
                            }
                            break;
                        case "btnASprint":
                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to print?")) {
                                // Determine the size of the array
                                int numberOfItems = selectedItems.size();

                                // Initialize the array with the determined size
                                Integer[] lnSelRow = new Integer[numberOfItems];

                                // Iterate through the selected items and populate the array
                                int lnCtr = 0;
                                for (InquiryVehicleSalesAdvances item : selectedItems) {
                                    lnRow = Integer.valueOf(item.getTblindex01()) - 1; // Assuming getTblindex01() returns a valid integer
                                    lnSelRow[lnCtr] = lnRow;
                                    lnCtr++;
                                }

                                try {
                                    loadVehicleSalesAdvancesPrint(lnSelRow);
                                } catch (SQLException ex) {
                                    Logger.getLogger(VehicleInquiryController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                return;
                            }
                            break;
                        default:
                            break;
                    }
                    loadAdvancesSlip();
                }
                break;
            case "btnBankAppNew":
                loJSON = oTransBank.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    try {
                        loadBankApplicationWindow(pnRow, Integer.parseInt(String.valueOf(oTrans.getMasterModel().getMasterModel().getPayMode())), pnEditMode, true, "");
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", (String) loJSON.get("message"));

                }
                break;
            case "btnFollowUp":
                loJSON = oTransFollow.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    try {
                        loadFollowUpWindow(pnRow, true, "");
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", (String) loJSON.get("message"));
                }
                break;
            case "btnLostSale":
                loJSON = oTransFollow.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    try {
                        loadLostSaleWindow(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", (String) loJSON.get("message"));
                }
                break;
            case "btnConvertSales":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to convert this inquiry for a new vsp record?")) {
                    loadVSPWindow();
                } else {
                    return;
                }

                break;
            case "btnSndMngerApprov":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to send this to manager for approval?")) {
                    oTrans.getMasterModel().getMasterModel().setTranStat("6");
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Inquiry Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadVehiclePriority();
                        loadPromoOffered();
                        loadInquiryRequirements();
                        loadAdvancesSlip();
                        loadBankApplications();
                        loadFollowHistory();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    }
                } else {
                    return;
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
        initBtnProcess(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox10.setItems(cInquirySourceType);
        comboBox21.setItems(cTargetVehcl);
        comboBox25.setItems(cModeOfPayment);
        comboBox26.setItems(cCustomerType);
    }

    @Override
    public void initFieldsAction() {
        datePicker22.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.setMaster(12, SQLUtil.toDate(datePicker22.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        txtField02.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.setMaster(3, SQLUtil.toDate(txtField02.getText().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox10.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox10.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setSourceCD((String.valueOf((comboBox10.getSelectionModel().getSelectedIndex()))));
                }
                switch (comboBox10.getSelectionModel().getSelectedIndex()) {
                    case 1:
                        oTrans.getMasterModel().getMasterModel().setActvtyID("");
                        oTrans.getMasterModel().getMasterModel().setAgentID("");
                        CustomCommonUtil.setText("", txtField12, txtField13);
                        break;
                    case 3:
                        oTrans.getMasterModel().getMasterModel().setActvtyID("");
                        oTrans.getMasterModel().getMasterModel().setPlatform("");
                        CustomCommonUtil.setText("", txtField11, txtField12);
                        break;
                    case 4:
                    case 5:
                        oTrans.getMasterModel().getMasterModel().setAgentID("");
                        oTrans.getMasterModel().getMasterModel().setPlatform("");
                        CustomCommonUtil.setText("", txtField11, txtField12, txtField13);
                        break;
                    default:
                        oTrans.getMasterModel().getMasterModel().setAgentID("");
                        oTrans.getMasterModel().getMasterModel().setPlatform("");
                        oTrans.getMasterModel().getMasterModel().setActvtyID("");
                        CustomCommonUtil.setText("", txtField11, txtField12, txtField13);
                        break;
                }
                initFields(pnEditMode);
            }
        });
        comboBox21.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox21.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setIsVhclNw(String.valueOf((comboBox21.getSelectionModel().getSelectedIndex())));
                }
                initFields(pnEditMode);
            }
        });
        comboBox25.setOnAction(e -> {
            if (comboBox25.getSelectionModel().getSelectedIndex() >= 0) {
                oTrans.getMasterModel().getMasterModel().setPayMode(String.valueOf((comboBox25.getSelectionModel().getSelectedIndex())));
                if (oTrans.getMasterModel().getMasterModel().getTranStat().equals("0") || pnEditMode == EditMode.UPDATE) {
                    switch (comboBox25.getSelectionModel().getSelectedIndex()) {
                        case 0:
                        case 1:
                        case 3:
                            comboBox26.setValue("ANY");
                            oTrans.getMasterModel().getMasterModel().setCustGrp("4");
                            comboBox26.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getCustGrp()));
                            loadInquiryRequirements();
                            break;
                        case 2:
                        case 4:
                            comboBox26.getItems().remove("ANY");
                            comboBox26.setValue("");
                            oTrans.getMasterModel().getMasterModel().setCustGrp("");
                            comboBox26.setValue(oTrans.getMasterModel().getMasterModel().getCustGrp());
                            inqrequirementsdata.clear();
                            break;
                    }
                }
                initFields(pnEditMode);
                initBtnProcess(pnEditMode);
            }
        });
        comboBox26.setOnAction(e -> {
            if (comboBox26.getSelectionModel().getSelectedIndex() >= 0) {
                oTrans.getMasterModel().getMasterModel().setCustGrp(String.valueOf((comboBox26.getSelectionModel().getSelectedIndex())));
                initFields(pnEditMode);
                initBtnProcess(pnEditMode);
                loadInquiryRequirements();
            }
        });
        tabPaneMain.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) -> {
            pnRow = 0;
            if (pnEditMode == EditMode.UPDATE) {
                tabPinEditMode = iTabIndex;
            }
            initFields(pnEditMode);
        }
        );
    }

    @Override
    public void initTextFieldsProperty() {
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setClientID("");
                                if (oTrans.getMasterModel().getMasterModel().getClientTp().equals("0")) {
                                    oTrans.getMasterModel().getMasterModel().setContctID("");
                                    oTrans.getMasterModel().getMasterModel().setContctNm("");
                                    txtField05.setText("");
                                }
                                textArea04.setText("");
                                CustomCommonUtil.setText("", txtField06, txtField07, txtField08);
                            }
                        }
                    }
                    initFields(pnEditMode);
                }
                );
        txtField05.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setContctID("");
                                oTrans.getMasterModel().getMasterModel().setContctNm("");
                            }
                        }
                    }
                }
                );
        txtField09.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setSalesExe("");
                            }
                        }
                    }
                }
                );
        txtField11.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setPlatform("");
                            }
                        }
                    }
                }
                );
        txtField12.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setActvtyID("");
                            }
                        }
                    }
                }
                );
        txtField13.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setSalesAgn("");
                            }
                        }
                    }
                }
                );
        txtField14.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setBranchCd("");
                                oTrans.getMasterModel().getMasterModel().setBranchNm("");
                            }
                        }
                    }
                }
                );
        txtField20.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setTestModl("");
                            }
                        }
                    }
                }
                );
        txtField27.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setCSNo("");
                                oTrans.getMasterModel().getMasterModel().setPlateNo("");
                                oTrans.getMasterModel().getMasterModel().setVhclID("");
                                textArea28.setText("");
                            }
                        }
                    }
                }
                );
    }

    @Override
    public void clearTables() {
        priorityunitdata.clear();
        promosoffereddata.clear();
        inqrequirementsdata.clear();
        inqvsadata.clear();
        bankappdata.clear();
        followupdata.clear();
    }

    @Override
    public void clearFields() {
        pnRow = 0;
        lblInqStatus.setText("");
        txtField18.setText("0.00");
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField16, txtField20,
                txtField27, txtField29, txtField30);
        CustomCommonUtil.setText("", textArea04, textArea23, textArea24, textArea28);
        CustomCommonUtil.setValue(null, comboBox10, comboBox21, comboBox25, comboBox26);
        CustomCommonUtil.setSelected(false, rdbtnHtA19, rdbtnHtB19, rdbtnHtC19);
        datePicker22.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate())));
    }

    @Override
    public void initFields(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        //Inquiry button
        CustomCommonUtil.setVisible(lbShow, btnTargetVhclAdd, btnPromoAdd, btnTargetVhclRemove);
        CustomCommonUtil.setManaged(lbShow, btnTargetVhclAdd, btnPromoAdd, btnTargetVhclRemove);
        //Inquiry General Button
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnEdit, btnConvertSales, btnFollowUp, btnLostSale);
        CustomCommonUtil.setManaged(false, btnEdit, btnConvertSales, btnFollowUp, btnLostSale);
        btnASprint.setDisable(false);
        //Bank Application
        CustomCommonUtil.setVisible(false, btnBankAppNew, btnProcess);
        CustomCommonUtil.setManaged(false, btnBankAppNew, btnProcess);

        CustomCommonUtil.setDisable(true, txtField05, txtField14, txtField11, txtField12, txtField13,
                btnPromoRemove, btnTargetVhclRemove, btnSndMngerApprov);
        CustomCommonUtil.setDisable(!lbShow, txtField03, txtField09, comboBox10, txtField11,
                txtField12, txtField13, rdbtnHtA19, rdbtnHtB19, rdbtnHtC19,
                comboBox21, datePicker22, textArea23, textArea24);
        if (fnValue == EditMode.ADDNEW) {
            trgvIndex03.setVisible(true);
            trgvIndex04.setVisible(true);
        }
        switch (comboBox10.getSelectionModel().getSelectedIndex()) {
            case 1:
                txtField11.setDisable(!lbShow);//Online Store
                CustomCommonUtil.setDisable(true, txtField12, txtField13);
                break;
            case 3:
                txtField13.setDisable(!lbShow);//Agent ID
                CustomCommonUtil.setDisable(true, txtField11, txtField12);
                break;
            case 4:
            case 5:
                txtField12.setDisable(!lbShow);//Activity ID
                CustomCommonUtil.setDisable(true, txtField11, txtField13);
                break;
        }

        boolean lbTab = (fnValue == EditMode.READY);
        tabInquiryProcess.setDisable(!lbTab);
        tabBankHistory.setDisable(!lbTab);
        tabFollowingHistory.setDisable(!lbTab);
        if (fnValue == EditMode.READY) {
            trgvIndex03.setVisible(false);
            trgvIndex04.setVisible(false);
            CustomCommonUtil.setVisible(true, btnLostSale);
            CustomCommonUtil.setManaged(true, btnLostSale);
            switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
                case "0"://For Follow up
                    if (tabPaneMain.getSelectionModel().getSelectedIndex() == 1) {
                        btnSave.setVisible(false);
                        btnSave.setManaged(false);
                        btnProcess.setVisible(true);
                        btnProcess.setManaged(true);
                    }
                    btnFollowUp.setVisible(true);
                    break;
                case "1": //On process
                    if (comboBox25.getSelectionModel().getSelectedIndex() >= 0) {
                        btnConvertSales.setVisible(true);
                        btnConvertSales.setManaged(true);
                    }
                    if (comboBox25.getSelectionModel().getSelectedIndex() > 0) {
                        //Bank Application
                        btnBankAppNew.setVisible(true);
                        btnBankAppNew.setManaged(true);
                    }
                    //For Follow up
                    btnFollowUp.setVisible(true);
                    break;
                case "3": //VSP
                    //Bank Application
                    CustomCommonUtil.setVisible(true, btnBankAppNew, btnFollowUp);
                    CustomCommonUtil.setManaged(true, btnBankAppNew, btnFollowUp);
                    break;

            }
            switch (tabPaneMain.getSelectionModel().getSelectedIndex()) {
                case 0:
                case 1:
                    switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
                        case "1":
                        case "6":
                        case "3":
                            btnEdit.setVisible(true);
                            btnEdit.setManaged(true);
                            break;
                    }
                    initBtnProcess(pnEditMode);
                    break;
            }
        }

        if (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE) {
            if (tabPinEditMode == 1) {
                initCustomerInquiryFieldsFalse();
                initInquiryProcessFieldsTrue();
            } else {
                initCustomerInquiryFieldsTrue();
                initInquiryProcessFieldsFalse();
            }

        }
        //disable fields according to the tab index
        if (fnValue == EditMode.UPDATE) {
            switch (tabPaneMain.getSelectionModel().getSelectedIndex()) {
                default:
                    tabCustomerInquiry.setDisable(false);
                    tabInquiryProcess.setDisable(false);
                    tabBankHistory.setDisable(false);
                    tabFollowingHistory.setDisable(false);
                    break;
            }
            if (oTrans.getMasterModel().getMasterModel().getTranStat().equals("3")) {
                comboBox21.setDisable(true);
            }
            txtField03.setDisable(true);
            txtField09.setDisable(true);
        }
        if (fnValue == EditMode.ADDNEW) {
            if (oApp.isMainOffice()) {
                txtField14.setDisable(!lbShow); // Branch Name
            } else {
                txtField14.setDisable(true); // Branch Name
            }
        }

        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case "0":
            case "1":
            case "3":
            case "6":
                if (fnValue == EditMode.READY) {
                    if (tblAdvanceSlip.getItems().size() > 0) {
                        vsasCheck01.setVisible(true);
                    }
                }
                if (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE) {
                    if (tabPinEditMode == 1) {
                        initCustomerInquiryFieldsFalse();
                        initInquiryProcessFieldsTrue();
                    } else {
                        initCustomerInquiryFieldsTrue();
                        initInquiryProcessFieldsFalse();
                    }
                }
                break;
            case "2":
            case "4":
            case "5":
                vsasCheck01.setVisible(false);
                break;
        }

        if (oTrans.getMasterModel()
                .getMasterModel().getTranStat().equals("0")) {
            btnSndMngerApprov.setDisable(false);
        }
    }

    private void initInquiryProcessFieldsFalse() {
        // Set visibility for trgvIndex fields
        trgvIndex03.setVisible(true);
        trgvIndex04.setVisible(true);
        CustomCommonUtil.setDisable(false, comboBox10, txtField14, comboBox21, datePicker22,
                rdbtnHtA19, rdbtnHtB19, rdbtnHtC19, btnTargetVhclAdd, btnPromoAdd, btnTestDriveModel, textArea23);
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (oTrans.getMasterModel().getMasterModel().getClientTp().equals("1")) {
                txtField05.setDisable(false);
            } else {
                txtField05.setDisable(true);
            }
        }

        // Special conditions
        if (pnEditMode == EditMode.UPDATE) {
            txtField03.setDisable(true);
        }
    }

    private void initInquiryProcessFieldsTrue() {
        trgvIndex03.setVisible(false);
        trgvIndex04.setVisible(false);
        CustomCommonUtil.setDisable(true, txtField05, txtField09, comboBox10, txtField11, txtField12, txtField13, txtField14, comboBox21, datePicker22,
                rdbtnHtA19, rdbtnHtB19, rdbtnHtC19, btnTargetVhclAdd, btnPromoAdd, btnPromoRemove, btnTestDriveModel, textArea23);
        txtField14.setEditable(false); // Branch Name

    }

    private void initCustomerInquiryFieldsTrue() {
        rqrmIndex01.setVisible(false);
        vsasCheck01.setVisible(false);
        CustomCommonUtil.setVisible(true, btnFollowUp, btnBankAppNew);
        CustomCommonUtil.setManaged(true, btnFollowUp, btnBankAppNew);
        CustomCommonUtil.setDisable(true, btnASremove, btnASCancel, btnASprint, comboBox25, comboBox26, btnASadd,
                txtField27, btnSndMngerApprov);
        if (pnEditMode == EditMode.READY) {
            if (tblAdvanceSlip.getItems().size() > 0) {
                btnASprint.setDisable(false);
            }
        }
    }

    private void initCustomerInquiryFieldsFalse() {
        // Set visibility and disable properties for fields
        rqrmIndex01.setVisible(true);
        vsasCheck01.setVisible(true);
        CustomCommonUtil.setVisible(false, btnFollowUp, btnBankAppNew);
        CustomCommonUtil.setManaged(false, btnFollowUp, btnBankAppNew);
        CustomCommonUtil.setDisable(false, btnASCancel, btnASremove, btnASprint,
                comboBox25, comboBox26, btnASadd, txtField27, btnASadd);
        if (oTrans.getMasterModel().getMasterModel().getTranStat().equals("0")) {
            btnSndMngerApprov.setDisable(false);
        }
        tblAdvanceSlip.setDisable(false);
        if (tblAdvanceSlip.getItems().size() < 0) {
            btnASCancel.setDisable(true);
            btnASremove.setDisable(true);
            if (pnEditMode == EditMode.READY) {
                btnASprint.setDisable(true);
            }
        }

    }

    /*INQUIRY PROCESS*/
    private void initBtnProcess(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true, comboBox25, comboBox26, btnSndMngerApprov,
                btnASprint, btnASremove, btnASCancel);
        CustomCommonUtil.setDisable(!lbShow, comboBox25, txtField27, btnASadd);
        btnProcess.setVisible(false);
        btnProcess.setManaged(false);
        if (oTrans.getMasterModel().getMasterModel().getTranStat() != null) {
            if (oTrans.getMasterModel().getMasterModel().getTranStat().equals("0")) {
                btnSndMngerApprov.setDisable(false);
                rqrmIndex01.setVisible(true);
            } else {
                rqrmIndex01.setVisible(lbShow);
            }
        }
        switch (comboBox25.getSelectionModel().getSelectedIndex()) {
            case 2: //Bank Financing
            case 4: //Company Financing
                comboBox26.setDisable(!lbShow);
                break;
        } //        if (cmbInqpr01.getSelectionModel().getSelectedIndex() == 1){
        //            cmbInqpr02.setDisable(true);
        //        } else {
        //            cmbInqpr02.setDisable(!lbShow);
        //        }
        //Reservation
        if (tblAdvanceSlip.getItems().size() > 0) {
            CustomCommonUtil.setDisable(false, btnASCancel, btnASprint);
        }
        if (fnValue == EditMode.READY || (lbShow)) {
            btnASprint.setVisible(true);
            switch (String.valueOf(oTrans.getMasterModel().getMasterModel().getTranStat())) {
                case "0"://For Follow up
                    rqrmIndex01.setVisible(true);
                    CustomCommonUtil.setDisable(false, comboBox25, txtField27);
                    switch (comboBox25.getSelectionModel().getSelectedIndex()) {
                        case 2:
                        case 4:
                            comboBox26.setDisable(false);
                            break;
                    }
                    btnASadd.setDisable(false);
                    if (tblAdvanceSlip.getItems().size() > 0) {
                        CustomCommonUtil.setDisable(false, btnASprint, btnASremove, btnASCancel);
                    }
                    if (tabPaneMain.getSelectionModel().getSelectedIndex() == 1) {
                        btnProcess.setVisible(true);
                        btnProcess.setManaged(true);
                    }
                    break;
            }

        }

    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (rdbtnHtA19.isSelected()) {
            oTrans.getMasterModel().getMasterModel().setIntrstLv("a");
        } else if (rdbtnHtB19.isSelected()) {
            oTrans.getMasterModel().getMasterModel().setIntrstLv("b");
        } else if (rdbtnHtC19.isSelected()) {
            oTrans.getMasterModel().getMasterModel().setIntrstLv("c");
        }
        if (comboBox10.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select `Inquiry Type` value.");
            return false;
        } else {
            oTrans.getMasterModel().getMasterModel().setSourceCD(String.valueOf(comboBox10.getSelectionModel().getSelectedIndex()));
        }
        switch (comboBox10.getSelectionModel().getSelectedIndex()) {
            case 3:
                if (txtField13.getText().equals("") || txtField13.getText() == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select `Refferal Agent` value.");
                    txtField13.requestFocus();
                    return false;
                }
                break;
            case 4:
            case 5:
                if (txtField12.getText().equals("") || txtField12.getText() == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select `Event` value.");
                    txtField12.requestFocus();
                    return false;
                }
                break;
            case 1:
                if (txtField11.getText().equals("") || txtField11.getText() == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select `Online Store` value.");
                    txtField11.requestFocus();
                    return false;
                }
                break;
        }
        if (comboBox21.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select `Vehicle Category` value.");
            return false;
        } else {
            oTrans.getMasterModel().getMasterModel().setIsVhclNw(String.valueOf(comboBox21.getSelectionModel().getSelectedIndex()));
        }

        return true;
    }

    private boolean checkExistingInquiryInformation(boolean fbIsClient) {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.checkExistingTransaction(fbIsClient);
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openTransaction((String) loJSON.get("sTransNox"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadVehiclePriority();
                    loadPromoOffered();
                    loadInquiryRequirements();
                    loadAdvancesSlip();
                    loadBankApplications();
                    loadFollowHistory();
                    initFields(pnEditMode);
                    pnEditMode = oTrans.getEditMode();
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private void loadVSPWindow() {
        try {
            String lsFormName = "Vehicle Sales Proposal";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSP.fxml"));
            VSPController loControl = new VSPController();
            loControl.setGRider(oApp);
            loControl.setIsInquiryState(true);
            if (oTrans.getMasterModel().getMasterModel().getTransNo() != null) {
                loControl.setInquiryTrans(oTrans.getMasterModel().getMasterModel().getTransNo());
            }
            fxmlLoader.setController(loControl);
            Parent parent = fxmlLoader.load();
            AnchorPane otherAnchorPane = loControl.AnchorMain;

            // Get the parent of the TabContent node
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            // If the parent is a TabPane, you can work with it directly
            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;

                for (Tab tab : tabpane.getTabs()) {
                    if (tab.getText().equals(lsFormName)) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "You have opened Vehicle Sales Proposal Form. Are you sure you want to convert this inquiry for a new vsp record?")) {
                            tabpane.getSelectionModel().select(tab);
                            poUnload.unloadForm(AnchorMain, oApp, lsFormName);
                            loadVSPWindow();
                        } else {
                            return;
                        }
                        return;
                    }
                }
                Tab loNewTab = new Tab(lsFormName, parent);
                loNewTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
                loNewTab.setContextMenu(fdController.createContextMenu(tabpane, loNewTab, oApp));
                tabpane.getTabs().add(loNewTab);
                tabpane.getSelectionModel().select(loNewTab);
                loNewTab.setOnCloseRequest(event -> {
                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?")) {
                        if (poUnload != null) {
                            poUnload.unloadForm(otherAnchorPane, oApp, lsFormName);
                        } else {
                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                    } else {
                        // Cancel the close request
                        event.consume();
                    }

                });

            }

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    /*INQUIRY PROCESS: PRINT VEHICLE SALES ADVANCES*/
    private void loadVehicleSalesAdvancesPrint(Integer[] fnRows) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryReservationPrint.fxml"));
            VehicleInquiryReservationPrintController loControl = new VehicleInquiryReservationPrintController();
            loControl.setGRider(oApp);
            loControl.setVSObject(oTrans);
            loControl.setRows(fnRows);
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void laodTestModelWindow() {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleTestDrive.fxml"));
            VehicleTestDriveController loControl = new VehicleTestDriveController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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
            txtField20.setText(oTrans.getMasterModel().getMasterModel().getTestModl());
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    @FXML
    private void tblVehiclePrio_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            int lnSel = tblPriorityUnit.getSelectionModel().getSelectedIndex();
            if (lnSel < 0 || lnSel >= tblPriorityUnit.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid vehicle information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 1) {
                if (pnEditMode == EditMode.ADDNEW) {
                    btnTargetVhclRemove.setDisable(false);
                } else {
                    btnTargetVhclRemove.setDisable(true);
                }
            }
        }
    }

    private void loadVehiclePriority() {
        priorityunitdata.clear();
        for (lnCtr = 0; lnCtr <= oTrans.getVehiclePriorityList().size() - 1; lnCtr++) {
            priorityunitdata.add(new InquiryVehiclePriority(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTrans.getVehiclePriority(lnCtr, "nPriority")),
                    String.valueOf(oTrans.getVehiclePriority(lnCtr, "sDescript")).toUpperCase(),
                    String.valueOf(oTrans.getVehiclePriority(lnCtr, "sVhclIDxx")).toUpperCase()
            ));
        }
    }

    private void initVehiclePriority() {
        trgvIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01")); // nPriority
        trgvIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex03")); // Description

        trgvIndex03.setCellFactory(param -> new TableCell<InquiryVehiclePriority, Button>() {
            Button upBtn = new Button();

            {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("ARROW_UP");
                icon.setSize("12px");
                icon.setStyleClass("button-icon");
                upBtn.setGraphic(icon);

                upBtn.setOnAction(event -> {
                    InquiryVehiclePriority model = getTableView().getItems().get(getIndex());
                    ObservableList<InquiryVehiclePriority> items = tblPriorityUnit.getItems();
                    int currentIndex = getIndex();

                    if (currentIndex > 0) {
                        // Swap items
                        InquiryVehiclePriority prevModel = items.get(currentIndex - 1);
                        items.set(currentIndex - 1, model);
                        items.set(currentIndex, prevModel);

                        // Update data model with new positions
                        updateModel(items);

                        // Select new position
                        getTableView().getSelectionModel().select(currentIndex - 1);
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : upBtn);
            }
        });

        trgvIndex04.setCellFactory(param -> new TableCell<InquiryVehiclePriority, Button>() {
            Button downBtn = new Button();

            {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("ARROW_DOWN");
                icon.setSize("12px");
                icon.setStyleClass("button-icon");
                downBtn.setGraphic(icon);

                downBtn.setOnAction(event -> {
                    InquiryVehiclePriority model = getTableView().getItems().get(getIndex());
                    ObservableList<InquiryVehiclePriority> items = tblPriorityUnit.getItems();
                    int currentIndex = getIndex();

                    if (currentIndex < items.size() - 1) {
                        // Swap items
                        InquiryVehiclePriority nextModel = items.get(currentIndex + 1);
                        items.set(currentIndex + 1, model);
                        items.set(currentIndex, nextModel);

                        // Update data model with new positions
                        updateModel(items);

                        // Select new position
                        getTableView().getSelectionModel().select(currentIndex + 1);
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : downBtn);
            }
        });

        tblPriorityUnit.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblPriorityUnit.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblPriorityUnit.setItems(priorityunitdata);
    }

    private void updateModel(ObservableList<InquiryVehiclePriority> items) {
        // Iterate over the entire list
        for (int pnCtr = 0; pnCtr < items.size(); pnCtr++) {
            InquiryVehiclePriority unit = items.get(pnCtr);
            try {
                // Update the fields with new row positions
                int lnPrio = pnCtr + 1; // Row number starts from 1
                String lsDescript = unit.getTblindex03();
                String lsVhclID = unit.getTblindex04();

                // Update the data model
                oTrans.setVehiclePriority(pnCtr, "nPriority", lnPrio);
                oTrans.setVehiclePriority(pnCtr, "sDescript", lsDescript);
                oTrans.setVehiclePriority(pnCtr, "sVhclIDxx", lsVhclID);

            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error updating index: " + pnCtr + " - " + e.getMessage());
            }
        }
        // Refresh table outside the loop
        loadVehiclePriority();
        tblPriorityUnit.refresh();
    }

    @FXML
    private void tblPromo_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            int lnSel = tblPromosOffered.getSelectionModel().getSelectedIndex();
            if (lnSel < 0 || lnSel >= tblPromosOffered.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid promo information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 1) {
                if (tblPromosOffered.getItems().size() > 0) {
                    if (tabPinEditMode == 0) {
                        btnPromoRemove.setDisable(false);
                    }
                }
            }
        }
    }

    private void loadPromoOffered() {
        promosoffereddata.clear();
        for (lnCtr = 0; lnCtr <= oTrans.getPromoList().size() - 1; lnCtr++) {
            promosoffereddata.add(new InquiryPromoOffered(
                    String.valueOf(lnCtr + 1), //ROW
                    CustomCommonUtil.xsDateShort((Date) oTrans.getPromo(lnCtr, "dDateFrom")),
                    CustomCommonUtil.xsDateShort((Date) oTrans.getPromo(lnCtr, "dDateThru")),
                    String.valueOf(oTrans.getPromo(lnCtr, "sActTitle")).toUpperCase(),
                    String.valueOf(oTrans.getPromo(lnCtr, "sPromoIDx")).toUpperCase()
            ));
        }
    }

    private void initPromoOffered() {
        prmoIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        prmoIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        prmoIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        prmoIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        tblPromosOffered.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblPromosOffered.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblPromosOffered.setItems(promosoffereddata);
    }

    private void loadInquiryRequirements() {
        inqrequirementsdata.clear();
        JSONObject loJSON = oTrans.loadRequirements();
        boolean lbCheck = false;
        String lsReceive = "";
        String lsReceiveDte = "";
        String lsRequired = "";
        if (!"error".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr < oTrans.getRequirementList().size(); lnCtr++) { // Changed loop condition
                lbCheck = String.valueOf(oTrans.getRequirement(lnCtr, "cSubmittd")).equals("1");
                if (lbCheck) {
                    lsReceive = String.valueOf(oTrans.getRequirement(lnCtr, "sCompnyNm"));
                    lsReceiveDte = CustomCommonUtil.xsDateShort((Date) oTrans.getRequirement(lnCtr, "dReceived"));
                } else {
                    lsReceive = "";
                    lsReceiveDte = "";
                }
                if (oTrans.getRequirement(lnCtr, "cRequired").equals("0")) {
                    lsRequired = "N";
                } else {
                    lsRequired = "Y";
                }
                inqrequirementsdata.add(new InquiryRequirements(
                        lbCheck,
                        String.valueOf(oTrans.getRequirement(lnCtr, "sDescript")),
                        lsReceive.toUpperCase(),
                        lsReceiveDte,
                        String.valueOf(oTrans.getRequirement(lnCtr, "sRqrmtCde")),
                        lsRequired
                ));
                lbCheck = false;
                lsReceive = "";
                lsReceiveDte = "";
                lsRequired = "";
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void initInquiryRequirements() {
        tblRequirementsInfo.setEditable(true);
        tblRequirementsInfo.setSelectionModel(null);
        rqrmIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        rqrmIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        rqrmIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        rqrmIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        rqrmIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        rqrmIndex01.setCellFactory(CheckBoxTableCell.forTableColumn((Integer index) -> {
            InquiryRequirements requirement = inqrequirementsdata.get(index);
            BooleanProperty selected = requirement.selectedProperty();
            loadInquiryRequirements();
            selected.addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    JSONObject loJSON = oTrans.searchEmployee(requirement.getTblindex05(), requirement.getTblindex02(), requirement.getTblindex06());
                    if (!"error".equals(loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                    }
                } else {
                    oTrans.removeEmployee(requirement.getTblindex05());
                }
            });

            return selected;
        }));

        // Prevent column reordering
        tblRequirementsInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequirementsInfo.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblRequirementsInfo.setItems(inqrequirementsdata);
        tblRequirementsInfo.refresh();
        loadInquiryRequirements();
    }

    @FXML
    private void tblAdvanceSlip_Clicked(MouseEvent event) {
        pnRow = tblAdvanceSlip.getSelectionModel().getSelectedIndex();
        if (pnRow < 0 || pnRow >= tblAdvanceSlip.getItems().size()) {
            ShowMessageFX.Warning(getStage(), "Please select valid reservation information.", "Warning", null);
            return;
        }
        if (pnEditMode == EditMode.UPDATE) {
            if (tabPinEditMode == 1) {
                if (event.getClickCount() == 2) {
                    try {
                        loadVehicleSalesAdvancesWindow(pnRow, false, pnEditMode);
                        loadAdvancesSlip();

                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } else {
            if (oTrans.getMasterModel().getMasterModel().getTranStat().equals("0")) {
                if (tabPaneMain.getSelectionModel().getSelectedIndex() == 1) {
                    if (event.getClickCount() == 2) {
                        try {
                            loadVehicleSalesAdvancesWindow(pnRow, false, pnEditMode);
                            loadAdvancesSlip();

                        } catch (SQLException ex) {
                            Logger.getLogger(VehicleInquiryController.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }

    }

    private void loadAdvancesSlip() {
        inqvsadata.clear();
        String lsResDte = "";
        String lsResType = "";
        String lsAmount = "";
        String lsInqStat = "";
        String lsApprovedBy = "";
        String lsApprovedDte = "";
        String lsRsNox = "";
        String lsSlipNo = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getReservationList().size() - 1; lnCtr++) {
            if (String.valueOf(oTrans.getReservation(lnCtr, "dTransact")) != null) {
                lsResDte = CustomCommonUtil.xsDateShort((Date) oTrans.getReservation(lnCtr, "dTransact"));
            }
            switch (String.valueOf(oTrans.getReservation(lnCtr, "cResrvTyp"))) {
                case "0":
                    lsResType = "RESERVATION";
                    break;
                case "1":
                    lsResType = "DEPOSIT";
                    break;
                case "2":
                    lsResType = "SAFEGUARD DUTY";
                    break;
            }

            if (String.valueOf(oTrans.getReservation(lnCtr, "nAmountxx")) != null) {
                lsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getReservation(lnCtr, "nAmountxx"))));
            }
            if (String.valueOf(oTrans.getReservation(lnCtr, "sApprover")) != null) {
                lsApprovedBy = String.valueOf(oTrans.getReservation(lnCtr, "sApprover"));
            }
            if (String.valueOf(oTrans.getReservation(lnCtr, "dApprovex")) != null) {
                lsApprovedDte = CustomCommonUtil.xsDateShort((Date) oTrans.getReservation(lnCtr, "dApprovex"));
            }
            if (String.valueOf(oTrans.getReservation(lnCtr, "cTranStat")) != null) {
                switch (String.valueOf(oTrans.getReservation(lnCtr, "cTranStat"))) {
                    case TransactionStatus.STATE_OPEN:
                        lsInqStat = "For Approval";
                        break;
                    case TransactionStatus.STATE_CLOSED:
                        lsInqStat = "Approved";
                        break;
                    case TransactionStatus.STATE_CANCELLED:
                        lsInqStat = "Cancelled";
                        break;
                    case TransactionStatus.STATE_POSTED:
                        lsInqStat = "Posted";
                        break;
                }
            }
            if (String.valueOf(oTrans.getReservation(lnCtr, "sReferNox")) != null) {
                lsRsNox = String.valueOf(oTrans.getReservation(lnCtr, "sReferNox"));
            }
            if (String.valueOf(oTrans.getReservation(lnCtr, "sTransNox")) != null) {
                lsSlipNo = String.valueOf(oTrans.getReservation(lnCtr, "sTransNox"));
            }
            inqvsadata.add(new InquiryVehicleSalesAdvances(
                    String.valueOf(lnCtr + 1),
                    lsResDte,
                    String.valueOf(oTrans.getReservation(lnCtr, "cResrvTyp")),
                    lsResType,
                    lsAmount,
                    lsInqStat.toUpperCase(),
                    String.valueOf(oTrans.getReservation(lnCtr, "sRemarksx")),
                    lsApprovedBy,
                    lsApprovedDte,
                    lsRsNox,
                    String.valueOf(oTrans.getReservation(lnCtr, "sCompnyNm")),
                    "",
                    ""));
            lsResDte = "";
            lsResType = "";
            lsAmount = "";
            lsInqStat = "";
            lsApprovedBy = "";
            lsApprovedDte = "";
            lsRsNox = "";
            lsSlipNo = "";
        }

    }

    private void initAdvancesSlip() {
        vsasCheck01.setCellValueFactory(new PropertyValueFactory<>("select"));
        vsasIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        vsasIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        vsasIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        vsasIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        vsasIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        vsasIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        vsasIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblAdvanceSlip.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAdvanceSlip.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblAdvanceSlip.setItems(inqvsadata);
    }

    /*INQUIRY PROCESS: OPEN VEHICLE SALES ADVANCES*/
    private void loadVehicleSalesAdvancesWindow(Integer fnRow, boolean isAdd, Integer fEditMode) throws SQLException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquirySalesAdvances.fxml"));

            VehicleInquirySalesAdvancesController loControl = new VehicleInquirySalesAdvancesController();
            loControl.setGRider(oApp);
            loControl.setVSAObject(oTrans);
            loControl.setTableRows(fnRow);
            loControl.setState(isAdd);
            loControl.setInqStat(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getTranStat()));
            loControl.setEditMode(fEditMode);
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    /*INQUIRY BANK APPLICATION*/
    private void loadBankApplicationWindow(Integer fnRow, Integer fnPaymentMode, Integer fnEditmode, boolean isAdd, String fsTransNox) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryBankApplication.fxml"));
            VehicleInquiryBankApplicationController loControl = new VehicleInquiryBankApplicationController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransBank);
            loControl.setSource(oTrans.getMasterModel().getMasterModel().getTransNo());
            loControl.setState(isAdd);
            loControl.setBranCD(oTrans.getMasterModel().getMasterModel().getBranchCd());
            loControl.setEditMode(fnEditmode);
            loControl.setInqPaymentMode(fnPaymentMode);
            loControl.setTableRows(fnRow);
            loControl.setTransNox(fsTransNox);
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadBankApplications();

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    @FXML
    private void tblBankAppli_Clicked(MouseEvent event) {
        pnRow = tblBankApplication.getSelectionModel().getSelectedIndex();
        if (pnRow < 0 || pnRow >= tblBankApplication.getItems().size()) {
            ShowMessageFX.Warning(getStage(), "Please select valid bank application information.", "Warning", null);
            return;
        }
        if (pnEditMode == EditMode.READY) {
            if (event.getClickCount() == 2) {
                try {
                    InquiryVehicleBankApplications selectedItem = tblBankApplication.getItems().get(pnRow);
                    String lsTransNox = selectedItem.getTblindex02();
                    loadBankApplicationWindow(pnRow, Integer.parseInt(String.valueOf(oTrans.getMasterModel().getMasterModel().getPayMode())), pnEditMode, false, lsTransNox);
                    loadBankApplications();

                } catch (SQLException ex) {
                    Logger.getLogger(VehicleInquiryController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void loadBankApplications() {
        try {
            bankappdata.clear();
            String lsStatus = "";
            String lsPaymode = "";
            String lsBanktype = "";
            String lsCancelledDt = "";
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.loadBankApplicationList();
            if ("success".equals((String) loJSON.get("result"))) {
                for (int lnCtr = 1; lnCtr <= oTrans.getBankApplicationCount(); lnCtr++) {
                    if (!String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sCancelld")).isEmpty()) {
                        lsCancelledDt = CustomCommonUtil.xsDateShort((Date) oTrans.getBankApplicationDetail(lnCtr, "dCancelld"));
                        lsStatus = "CANCELLED";
                    } else {
                        if (oTrans.getBankApplicationDetail(lnCtr, "cTranStat") != null) {
                            switch (String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "cTranStat"))) {
                                case "0":
                                    lsStatus = "ON-GOING";
                                    break;
                                case "1":
                                    lsStatus = "DECLINE";
                                    break;
                                case "2":
                                    lsStatus = "APPROVED";
                                    break;
                            }
                        }
                    }
                    if (String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "cPayModex")) != null) {
                        switch (String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "cPayModex"))) {
                            case "1":
                                lsPaymode = "BANK PURCHASE ORDER";
                                break;
                            case "2":
                                lsPaymode = "BANK FINANCING";
                                break;
                            case "3":
                                lsPaymode = "COMPANY PURCHASE ORDER";
                                break;
                            case "4":
                                lsPaymode = "COMPANY FINANCING";
                                break;
                        }
                    }
                    try {
                        if (oTrans.getBankApplicationDetail(lnCtr, "sBankType") != null) {
                            switch ((String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sBankType")))) {
                                case "bank":
                                    lsBanktype = "BANK";
                                    break;
                                case "cred":
                                    lsBanktype = "CREDIT UNION";
                                    break;
                                case "insc":
                                    lsBanktype = "INSURANCE COMPANY";
                                    break;
                                case "invc":
                                    lsBanktype = "INVESTMENT COMPANIES";
                                    break;
                                default:
                                    break;

                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    bankappdata.add(new InquiryVehicleBankApplications(
                            String.valueOf(lnCtr),
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sTransNox")),
                            lsBanktype,
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sBankIDxx")),
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sBankName")),
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sBrBankNm")),
                            lsPaymode,
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "dAppliedx")),
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "sApplicNo")),
                            lsCancelledDt,
                            lsStatus,
                            String.valueOf(oTrans.getBankApplicationDetail(lnCtr, "dApproved"))
                    ));

                    lsStatus = "";
                    lsPaymode = "";
                    lsBanktype = "";
                    lsCancelledDt = "";

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleInquiryController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initBankApplications() {
        bankIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        bankIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        bankIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        bankIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        bankIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        bankIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        bankIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        bankIndex08.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        tblBankApplication.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblBankApplication.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblBankApplication.setItems(bankappdata);
    }

    @FXML
    private void tblFollowUp_Clicked(MouseEvent event) {
        pnRow = tblFollowHistory.getSelectionModel().getSelectedIndex();
        if (pnRow < 0 || pnRow >= tblFollowHistory.getItems().size()) {
            ShowMessageFX.Warning(getStage(), "Please select valid follow up information.", "Warning", null);
            return;
        }
        if (pnEditMode == EditMode.READY) {
            if (event.getClickCount() == 2) {
                try {
                    InquiryFollowUp selectedItem = tblFollowHistory.getItems().get(pnRow);
                    String lsTransNox = selectedItem.getTblindex01();
                    loadFollowUpWindow(pnRow, false, lsTransNox);
                    loadFollowHistory();

                } catch (SQLException ex) {
                    Logger.getLogger(VehicleInquiryController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /*INQUIRY FOR FOLLOW-UP*/
    private void loadFollowUpWindow(Integer fnRow, boolean isAdd, String fsRefNox) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryFollowUp.fxml"));

            VehicleInquiryFollowUpController loControl = new VehicleInquiryFollowUpController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransFollow);
            loControl.setSource(oTrans.getMasterModel().getMasterModel().getTransNo());
            loControl.setRefNo(fsRefNox);
            loControl.setBranCD(oTrans.getMasterModel().getMasterModel().getBranchCd());
            loControl.setTableRows(fnRow);
            loControl.setState(isAdd);
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadFollowHistory();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadFollowHistory() {
        followupdata.clear();
        JSONObject loJSON = new JSONObject();
        String lsFollowUpDate = "";
        String lsTransact = "";
        String lsMethod = "";
        String lsPlatForm = "";
        String lsFollowTime = "";
        loJSON = oTrans.loadFollowUpList();
        if ("success".equals((String) loJSON.get("result"))) {
            try {
                for (int lnCtr = 1; lnCtr <= oTrans.getFollowUpCount(); lnCtr++) {
                    if (oTrans.getFollowUpDetail(lnCtr, "dFollowUp") != null) {
                        lsFollowUpDate = CustomCommonUtil.xsDateShort((Date) oTrans.getFollowUpDetail(lnCtr, "dFollowUp"));
                    }
                    if (oTrans.getFollowUpDetail(lnCtr, "dTransact") != null) {
                        lsTransact = CustomCommonUtil.xsDateShort((Date) oTrans.getFollowUpDetail(lnCtr, "dTransact"));
                    }
                    if (oTrans.getFollowUpDetail(lnCtr, "sMethodCd") != null) {
                        lsMethod = String.valueOf(oTrans.getFollowUpDetail(lnCtr, "sMethodCd"));
                    }
                    if (oTrans.getFollowUpDetail(lnCtr, "tFollowUp") != null) {
                        lsFollowTime = String.valueOf(oTrans.getFollowUpDetail(lnCtr, "tFollowUp"));
                    }
                    if (oTrans.getFollowUpDetail(lnCtr, "sMethodCd") != null) {
                        if (String.valueOf(oTrans.getFollowUpDetail(lnCtr, "sMethodCd")).equals("UPDATE")) {
                            lsPlatForm = "";
                            lsFollowUpDate = "";
                            lsFollowTime = "";
                        } else {
                            if (oTrans.getFollowUpDetail(lnCtr, "sPlatform") != null) {
                                lsPlatForm = String.valueOf(oTrans.getFollowUpDetail(lnCtr, "sPlatform"));
                            }
                        }
                    }
                    followupdata.add(new InquiryFollowUp(
                            String.valueOf(lnCtr),
                            String.valueOf(oTrans.getFollowUpDetail(lnCtr, "sReferNox")),
                            lsTransact,
                            lsFollowUpDate,
                            lsFollowTime,
                            lsMethod,
                            lsPlatForm,
                            String.valueOf(oTrans.getFollowUpDetail(lnCtr, "sRemarksx"))));

                    lsFollowUpDate = "";
                    lsTransact = "";
                    lsMethod = "";
                    lsPlatForm = "";
                    lsFollowTime = "";

                }
            } catch (SQLException ex) {
                Logger.getLogger(VehicleInquiryController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initFollowHistory() {
        flwpIndex01.setCellValueFactory(new PropertyValueFactory<>("tblrowxx01"));
        flwpIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        flwpIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        flwpIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        flwpIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        flwpIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        flwpIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        flwpIndex08.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblFollowHistory.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblFollowHistory.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblFollowHistory.setItems(followupdata);
    }

    private void loadLostSaleWindow(boolean fbIsInquiry) throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryLostSale.fxml"));
            VehicleInquiryLostSaleController loControl = new VehicleInquiryLostSaleController();
            loControl.setGRider(oApp);
            loControl.setFollowUpObject(oTransFollow);
            loControl.setInquiryObject(oTrans);
            loControl.setTransNo(oTrans.getMasterModel().getMasterModel().getTransNo());
            loControl.setsVSPNox("");
            loControl.setBranCD(oTrans.getMasterModel().getMasterModel().getBranchCd());
            loControl.setStateForm(fbIsInquiry);
            loControl.setClientName(oTrans.getMasterModel().getMasterModel().getClientNm());
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadMasterFields();
                loadVehiclePriority();
                loadPromoOffered();
                loadInquiryRequirements();
                loadAdvancesSlip();
                loadBankApplications();
                loadFollowHistory();
                pnEditMode = oTrans.getEditMode();
                initFields(pnEditMode);
            }
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    private Callback<DatePicker, DateCell> targetDate = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            switch (pnEditMode) {
                case EditMode.ADDNEW:
                    LocalDate minDate = CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()));
                    setDisable(fbEmpty || foItem.isBefore(minDate));
                    break;
                case EditMode.UPDATE:
                    setDisable(fbEmpty || foItem.isBefore(datePicker22.getValue()));
                    break;
            }
        }
    };

}
