/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.event.EventHandler;
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
import javafx.scene.control.TextInputControl;
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
import org.guanzon.auto.main.sales.BankApplication;
import org.guanzon.auto.main.sales.FollowUp;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.models.sales.ModelInquiryFollowUp;
import org.guanzon.autoapp.models.sales.ModelInquiryPromoOffered;
import org.guanzon.autoapp.models.sales.ModelInquiryRequirements;
import org.guanzon.autoapp.models.sales.ModelInquiryVehiclePriority;
import org.guanzon.autoapp.models.sales.ModelInquiryVehicleSalesAdvances;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryBankApplications;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VehicleInquiryFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Inquiry oTransInquiry;
    private BankApplication oTransBank;
    private FollowUp oTransFollow;
    private String pxeModuleName = "Vehicle Inquiry";
    private int pnEditMode;//Modifying fields for Customer Entry
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    UnloadForm poUnload = new UnloadForm(); //Object for closing form
    private int iTabIndex = 0; //Set tab index
    private int tabPinEditMode;
    //    /* ------------------DATA TABLES----------------------- */
    private ObservableList<ModelInquiryVehiclePriority> priorityunitdata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryPromoOffered> promosoffereddata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryRequirements> inqrequirementsdata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryVehicleSalesAdvances> inqvsadata = FXCollections.observableArrayList();
    private ObservableList<ModelVehicleInquiryBankApplications> bankappdata = FXCollections.observableArrayList();
    private ObservableList<ModelInquiryFollowUp> followupdata = FXCollections.observableArrayList();

    //    /* ------------------COMBO BOX ITEMS/VALUE----------------------- */
    private ObservableList<String> cInquirySourceType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO"); //Inquiry Type values
    private ObservableList<String> cInqStatus = FXCollections.observableArrayList("FOR FOLLOW-UP", "ON PROCESS", "LOST SALE", "WITH VSP", "SOLD", "CANCELLED"); //Inquiry Type Values
    private ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    private ObservableList<String> cCustomerType = FXCollections.observableArrayList("BUSINESS", "EMPLOYED", "OFW", "SEAMAN", "ANY"); // Customer Type Values
    private ObservableList<String> cTargetVehcl = FXCollections.observableArrayList("BRANDNEW", "PRE-OWNED");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit, btnSave, btnConvertSales, btnLostSale, btnProcess, btnCancel, btnClose, btnBrowse;
    @FXML
    private Label lblInqStatus;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tabCustomerInquiry;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
            txtField11, txtField12, txtField13, txtField14, txtField16, txtField18, txtField20, txtField27, txtField29, txtField30;
    @FXML
    private ToggleGroup hotCategoryToggleGroup;
    @FXML
    private TableView<ModelInquiryVehiclePriority> tblPriorityUnit;
    @FXML
    private TableColumn<ModelInquiryVehiclePriority, String> trgvIndex01, trgvIndex02;
    @FXML
    private TableColumn<ModelInquiryVehiclePriority, Button> trgvIndex03, trgvIndex04;
    @FXML
    private Button btnTargetVhclRemove, btnTargetVhclAdd;
    @FXML
    private Button btnPromoRemove, btnPromoAdd;
    @FXML
    private TableView<ModelInquiryPromoOffered> tblPromosOffered;
    @FXML
    private TableColumn<ModelInquiryPromoOffered, String> prmoIndex01, prmoIndex02, prmoIndex03, prmoIndex04;
    @FXML
    private Tab tabInquiryProcess;
    @FXML
    private TableView tblRequirementsInfo;
    @FXML
    private TableColumn rqrmIndex01;
    @FXML
    private TableColumn rqrmIndex02, rqrmIndex03, rqrmIndex04;
    @FXML
    private Button btnASadd;
    @FXML
    private Button btnASremove;
    @FXML
    private Button btnASprint, btnASCancel;
    @FXML
    private TableView<ModelInquiryVehicleSalesAdvances> tblAdvanceSlip;
    @FXML
    private TableColumn<ModelInquiryVehicleSalesAdvances, Boolean> vsasCheck01;
    @FXML
    private TableColumn<ModelInquiryVehicleSalesAdvances, String> vsasIndex01, vsasIndex02, vsasIndex03, vsasIndex04, vsasIndex05, vsasIndex06, vsasIndex07;
    @FXML
    private Button btnSndMngerApprov;
    @FXML
    private Tab tabBankHistory;
    @FXML
    private Button btnBankAppNew;
    @FXML
    private TableView<ModelVehicleInquiryBankApplications> tblBankApplication;
    @FXML
    private TableColumn<ModelVehicleInquiryBankApplications, String> bankIndex01, bankIndex02, bankIndex03, bankIndex04, bankIndex05, bankIndex06, bankIndex07, bankIndex08;
    @FXML
    private Tab tabFollowingHistory;
    @FXML
    private Button btnFollowUp;
    @FXML
    private TableView<ModelInquiryFollowUp> tblFollowHistory;
    @FXML
    private TableColumn<ModelInquiryFollowUp, String> flwpIndex01, flwpIndex02, flwpIndex03, flwpIndex04, flwpIndex05, flwpIndex06, flwpIndex07, flwpIndex08;
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
        oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
        oTransBank = new BankApplication(oApp, false, oApp.getBranchCode());
        oTransFollow = new FollowUp(oApp, false, oApp.getBranchCode());

        //initialized tables
        initVehiclePriority();
        initPromoOffered();
        initInquiryRequirements();
        initAdvancesSlip();
        initBankApplications();
        initFollowHistory();
        initComboBoxValue();
        datePicker22.setDayCellFactory(targetDate);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearCustomerFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
        initBtnProcess(pnEditMode);
    }
    private Callback<DatePicker, DateCell> targetDate = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate loMinDate = datePicker22.getValue();

            setDisable(fbEmpty || foItem.isBefore(loMinDate));
        }
    };

    private void initComboBoxValue() {
        comboBox10.setItems(cInquirySourceType);
        comboBox21.setItems(cTargetVehcl);
        comboBox25.setItems(cModeOfPayment);
        comboBox26.setItems(cCustomerType);
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField16, txtField18, txtField20, txtField27, txtField29, txtField30);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea04, textArea23, textArea24, textArea28);

        loTxtArea.forEach(ta -> InputTextUtil.setCapsLockBehavior(ta));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField16, txtField18, txtField20, txtField27, txtField29, txtField30);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea04, textArea23, textArea24, textArea28);
        loTxtArea.forEach(ta -> ta.setOnKeyPressed(event -> textArea_KeyPressed(event)));
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
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField03":
                    loJSON = oTransInquiry.searchClient(lsValue, true);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField03.setText(oTransInquiry.getMasterModel().getMasterModel().getClientNm());
                        textArea04.setText(oTransInquiry.getMasterModel().getMasterModel().getAddress());
                        txtField06.setText(oTransInquiry.getMasterModel().getMasterModel().getMobileNo());
                        txtField07.setText(oTransInquiry.getMasterModel().getMasterModel().getEmailAdd());
                        txtField08.setText(oTransInquiry.getMasterModel().getMasterModel().getAccount());
                        if (oTransInquiry.getMasterModel().getMasterModel().getClientTp().equals("0")) {
                            oTransInquiry.getMasterModel().getMasterModel().setContctID("");
                            oTransInquiry.getMasterModel().getMasterModel().setContctNm("");
                            txtField05.setText("");
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField03.setText("");
                        txtField03.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField05":
                    loJSON = oTransInquiry.searchClient(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField05.setText(oTransInquiry.getMasterModel().getMasterModel().getContctNm());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField05.setText("");
                        txtField05.requestFocus();
                        return;
                    }
                    break;
                case "txtField09":
                    loJSON = oTransInquiry.searchSalesExecutive(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField09.setText(oTransInquiry.getMasterModel().getMasterModel().getSalesExe());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField09.setText("");
                        txtField09.requestFocus();
                        return;
                    }
                    break;
                case "txtField11":
                    loJSON = oTransInquiry.searchOnlinePlatform(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField11.setText(oTransInquiry.getMasterModel().getMasterModel().getPlatform());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField11.setText("");
                        txtField11.requestFocus();
                        return;
                    }
                    break;
                case "txtField12":
                    loJSON = oTransInquiry.searchActivity(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField12.setText(oTransInquiry.getMasterModel().getMasterModel().getActTitle());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField12.setText("");
                        txtField12.requestFocus();
                        return;
                    }
                    break;
                case "txtField13":
                    loJSON = oTransInquiry.searchReferralAgent(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField13.setText(oTransInquiry.getMasterModel().getMasterModel().getSalesAgn());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField13.setText("");
                        txtField13.requestFocus();
                        return;
                    }
                    break;
                case "txtField14":
                    loJSON = oTransInquiry.searchBranch(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField14.setText(oTransInquiry.getMasterModel().getMasterModel().getBranchNm());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField14.setText("");
                        txtField14.requestFocus();
                        return;
                    }
                    break;
                case "txtField27":
                    loJSON = oTransInquiry.searchAvlVhcl(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        String lsPlateCs = "";
                        if (!oTransInquiry.getMasterModel().getMasterModel().getPlateNo().isEmpty()) {
                            lsPlateCs = String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getPlateNo());
                        } else {
                            lsPlateCs = String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getCSNo());
                        }
                        txtField27.setText(lsPlateCs);
                        textArea28.setText(String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getDescript()));
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
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode() == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
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

    private void initTextFieldFocus() {
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
                    oTransInquiry.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initCmboxFieldAction() {
        datePicker22.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransInquiry.setMaster(12, SQLUtil.toDate(datePicker22.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        txtField02.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransInquiry.setMaster(3, SQLUtil.toDate(txtField02.getText().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox10.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox10.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransInquiry.getMasterModel().getMasterModel().setSourceCD((String.valueOf((comboBox10.getSelectionModel().getSelectedIndex()))));
                }
                switch (comboBox10.getSelectionModel().getSelectedIndex()) {
                    case 1:
                        oTransInquiry.getMasterModel().getMasterModel().setActvtyID("");
                        oTransInquiry.getMasterModel().getMasterModel().setAgentID("");
                        txtField12.setText("");//Activity ID
                        txtField13.setText("");//Agent ID
                        break;
                    case 3:
                        oTransInquiry.getMasterModel().getMasterModel().setActvtyID("");
                        oTransInquiry.getMasterModel().getMasterModel().setPlatform("");
                        txtField11.setText("");//Online Store
                        txtField12.setText("");//Activity ID
                        break;
                    case 4:
                    case 5:
                        oTransInquiry.getMasterModel().getMasterModel().setAgentID("");
                        oTransInquiry.getMasterModel().getMasterModel().setPlatform("");
                        txtField13.setText("");//Agent ID
                        txtField11.setText("");//Online Store
                        txtField12.setText("");//Online Store
                        break;
                    default:
                        oTransInquiry.getMasterModel().getMasterModel().setAgentID("");
                        oTransInquiry.getMasterModel().getMasterModel().setPlatform("");
                        oTransInquiry.getMasterModel().getMasterModel().setActvtyID("");
                        txtField12.setText("");//Activity ID
                        txtField13.setText("");//Agent ID
                        txtField11.setText("");//Online Store
                        break;
                }
                initFields(pnEditMode);
            }
        });
        comboBox21.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox21.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransInquiry.getMasterModel().getMasterModel().setIsVhclNw(String.valueOf((comboBox21.getSelectionModel().getSelectedIndex())));
                }
                initFields(pnEditMode);
            }
        });
        comboBox25.setOnAction(e -> {
            if (comboBox25.getSelectionModel().getSelectedIndex() >= 0) {
                oTransInquiry.getMasterModel().getMasterModel().setPayMode(String.valueOf((comboBox25.getSelectionModel().getSelectedIndex())));
                if (oTransInquiry.getMasterModel().getMasterModel().getTranStat().equals("0") || pnEditMode == EditMode.UPDATE) {
                    switch (comboBox25.getSelectionModel().getSelectedIndex()) {
                        case 0:
                        case 1:
                        case 3:
                            comboBox26.setValue("ANY");
                            oTransInquiry.getMasterModel().getMasterModel().setCustGrp("4");
                            comboBox26.getSelectionModel().select(Integer.parseInt(oTransInquiry.getMasterModel().getMasterModel().getCustGrp()));
                            loadInquiryRequirements();
                            break;
                        case 2:
                        case 4:
                            comboBox26.getItems().remove("ANY");
                            comboBox26.setValue("");
                            oTransInquiry.getMasterModel().getMasterModel().setCustGrp("");
                            comboBox26.setValue(oTransInquiry.getMasterModel().getMasterModel().getCustGrp());
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
                oTransInquiry.getMasterModel().getMasterModel().setCustGrp(String.valueOf((comboBox26.getSelectionModel().getSelectedIndex())));
                initFields(pnEditMode);
                initBtnProcess(pnEditMode);
                loadInquiryRequirements();
            }
        });
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransInquiry.getMasterModel().getMasterModel().setClientID("");
                                if (oTransInquiry.getMasterModel().getMasterModel().getClientTp().equals("0")) {
                                    oTransInquiry.getMasterModel().getMasterModel().setContctID("");
                                    oTransInquiry.getMasterModel().getMasterModel().setContctNm("");
                                    txtField05.setText("");
                                }
                                textArea04.setText("");
                                txtField06.setText("");
                                txtField07.setText("");
                                txtField08.setText("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setContctID("");
                                oTransInquiry.getMasterModel().getMasterModel().setContctNm("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setSalesExe("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setPlatform("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setActvtyID("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setSalesAgn("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setBranchCd("");
                                oTransInquiry.getMasterModel().getMasterModel().setBranchNm("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setTestModl("");
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
                                oTransInquiry.getMasterModel().getMasterModel().setCSNo("");
                                oTransInquiry.getMasterModel().getMasterModel().setPlateNo("");
                                oTransInquiry.getMasterModel().getMasterModel().setVhclID("");
                                textArea28.setText("");
                            }
                        }
                    }
                }
                );

        tabPaneMain.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) -> {
            pnRow = 0;
            if (pnEditMode == EditMode.UPDATE) {
                tabPinEditMode = iTabIndex;
            }
            initFields(pnEditMode);
        }
        );
    }

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")

    private boolean setSelection() {
        if (rdbtnHtA19.isSelected()) {
            oTransInquiry.getMasterModel().getMasterModel().setIntrstLv("a");
        } else if (rdbtnHtB19.isSelected()) {
            oTransInquiry.getMasterModel().getMasterModel().setIntrstLv("b");
        } else if (rdbtnHtC19.isSelected()) {
            oTransInquiry.getMasterModel().getMasterModel().setIntrstLv("c");
        }
        if (comboBox10.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select `Inquiry Type` value.");
            return false;
        } else {
            oTransInquiry.getMasterModel().getMasterModel().setSourceCD(String.valueOf(comboBox10.getSelectionModel().getSelectedIndex()));
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
            case 2:
                if (txtField11.getText().equals("") || txtField11.getText() == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select `Online Store` value.");
                    txtField11.requestFocus();
                    return false;
                }
                break;
            default:
                break;
        }
        if (comboBox21.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select `Vehicle Category` value.");
            return false;
        } else {
            oTransInquiry.getMasterModel().getMasterModel().setIsVhclNw(String.valueOf(comboBox21.getSelectionModel().getSelectedIndex()));
        }

        return true;
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnConvertSales, btnLostSale, btnProcess, btnCancel,
                btnClose, btnTestDriveModel,
                btnTargetVhclRemove, btnTargetVhclAdd,
                btnPromoAdd, btnPromoRemove,
                btnASadd, btnASremove, btnASprint, btnASCancel,
                btnBankAppNew,
                btnFollowUp);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        iTabIndex = tabPaneMain.getSelectionModel().getSelectedIndex();
        switch (lsButton) {
            case "btnAdd":
                clearCustomerFields();
                clearTables();
                switchToTab(tabCustomerInquiry, tabPaneMain);// Load fields, clear them, and set edit mode
                oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
                loJSON = oTransInquiry.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadCustomerInquiryInformation();
                    pnEditMode = oTransInquiry.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
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
                loJSON = oTransInquiry.updateTransaction();
                pnEditMode = oTransInquiry.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Inquiry Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.READY;
                    } else {
                        pnEditMode = oTransInquiry.getEditMode();
                        return;
                    }
                }
                loJSON = oTransInquiry.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    clearCustomerFields();
                    clearTables();
                    loadCustomerInquiryInformation();
                    loadVehiclePriority();
                    loadPromoOffered();
                    loadInquiryRequirements();
                    loadAdvancesSlip();
                    loadBankApplications();
                    loadFollowHistory();
                    pnEditMode = oTransInquiry.getEditMode();
                    initFields(pnEditMode);
                    initBtnProcess(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Inquiry Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Inquiry Information Saving....", "Are you sure, do you want to save?")) {
                    switch (iTabIndex) {
                        case 0:
                        case 1:
                            for (int lnCtr = 0; lnCtr <= oTransInquiry.getVehiclePriorityList().size() - 1; lnCtr++) {
                                if (oTransInquiry.getVehiclePriority(lnCtr, "nPriority").equals(1)) {
                                    oTransInquiry.getMasterModel().getMasterModel().setVhclID(oTransInquiry.getVehiclePriority(lnCtr, "sVhclIDxx").toString());
                                    break;
                                }
                            }
                            if (setSelection()) {
                                loJSON = oTransInquiry.saveTransaction();
                                if ("success".equals((String) loJSON.get("result"))) {
                                    ShowMessageFX.Information(null, "Vehicle Inquiry Information", (String) loJSON.get("message"));
                                    loJSON = oTransInquiry.openTransaction(oTransInquiry.getMasterModel().getMasterModel().getTransNo());
                                    if ("success".equals((String) loJSON.get("result"))) {
                                        loadCustomerInquiryInformation();
                                        loadVehiclePriority();
                                        loadPromoOffered();
                                        loadInquiryRequirements();
                                        loadAdvancesSlip();
                                        loadBankApplications();
                                        loadFollowHistory();
                                        initFields(pnEditMode);
                                        pnEditMode = oTransInquiry.getEditMode();
                                    }
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
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
                    clearCustomerFields();
                    clearTables();
                    switchToTab(tabCustomerInquiry, tabPaneMain);// Load fields, clear them, and set edit mode
                    oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnTargetVhclAdd":
                loJSON = oTransInquiry.searchVehicle();
                if (!"error".equals((String) loJSON.get("result"))) {
                    boolean isExist = false;
                    for (lnCtr = 0; lnCtr <= oTransInquiry.getVehiclePriorityList().size() - 1; lnCtr++) {
                        if (String.valueOf(oTransInquiry.getVehiclePriority(lnCtr, "sVhclIDxx")).equals((String) loJSON.get("sVhclIDxx"))) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        oTransInquiry.addVehiclePriority();
                        oTransInquiry.setVehiclePriority(oTransInquiry.getVehiclePriorityList().size() - 1, "nPriority", oTransInquiry.getVehiclePriorityList().size());
                        oTransInquiry.setVehiclePriority(oTransInquiry.getVehiclePriorityList().size() - 1, "sVhclIDxx", (String) loJSON.get("sVhclIDxx"));
                        oTransInquiry.setVehiclePriority(oTransInquiry.getVehiclePriorityList().size() - 1, "sDescript", (String) loJSON.get("sDescript"));
                        ShowMessageFX.Information(null, pxeModuleName, "Vehicle Priority added successfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Vehicle is already exist.");
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                loadVehiclePriority();
                break;
            case "btnTargetVhclRemove":
                if (ShowMessageFX.YesNo(null, "Vehicle Confirmation", "Are you sure you want to remove this Vehicle Information?")) {
                } else {
                    return;
                }
                oTransInquiry.removeVehiclePriority(pnRow);
                pnRow = 0;
                loadVehiclePriority();
                break;
            case "btnPromoAdd":
                loJSON = oTransInquiry.searchPromo();
                if (!"error".equals((String) loJSON.get("result"))) {
                    boolean isExist = false;
                    for (lnCtr = 0; lnCtr <= oTransInquiry.getPromoList().size() - 1; lnCtr++) {
                        if (String.valueOf(oTransInquiry.getPromo(lnCtr, "sPromoIDx")).equals((String) loJSON.get("sActvtyID"))) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        oTransInquiry.addPromo();
                        oTransInquiry.setPromo(oTransInquiry.getPromoList().size() - 1, "sPromoIDx", (String) loJSON.get("sActvtyID"));
                        oTransInquiry.setPromo(oTransInquiry.getPromoList().size() - 1, "sActNoxxx", (String) loJSON.get("sActNoxxx"));
                        oTransInquiry.setPromo(oTransInquiry.getPromoList().size() - 1, "sActTitle", (String) loJSON.get("sActTitle"));
                        oTransInquiry.setPromo(oTransInquiry.getPromoList().size() - 1, "dDateFrom", SQLUtil.toDate((String) loJSON.get("dDateFrom"), SQLUtil.FORMAT_SHORT_DATE));
                        oTransInquiry.setPromo(oTransInquiry.getPromoList().size() - 1, "dDateThru", SQLUtil.toDate((String) loJSON.get("dDateThru"), SQLUtil.FORMAT_SHORT_DATE));
                        ShowMessageFX.Information(null, pxeModuleName, "Promo added successfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Promo is already exist.");
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                loadPromoOffered();
                break;
            case "btnPromoRemove":
                if (ShowMessageFX.YesNo(null, "Promo Confirmation", "Are you sure you want to remove this Promo?")) {
                } else {
                    return;
                }
                oTransInquiry.removePromo(pnRow);
                pnRow = 0;
                loadPromoOffered();
                break;
            case "btnTestDriveModel":
                laodTestModelWindow();
                break;
            case "btnProcess":
                if (ShowMessageFX.YesNo(null, "Vehicle Inquiry Information Saving....", "Are you sure, do you want to prcoess?")) {
                } else {
                    return;
                }
                oTransInquiry.getMasterModel().getMasterModel().setTranStat("1");
                loJSON = oTransInquiry.saveTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                    loJSON = oTransInquiry.openTransaction(oTransInquiry.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadCustomerInquiryInformation();
                        loadVehiclePriority();
                        loadPromoOffered();
                        loadInquiryRequirements();
                        loadAdvancesSlip();
                        loadBankApplications();
                        loadFollowHistory();
                        initFields(pnEditMode);
                        pnEditMode = oTransInquiry.getEditMode();
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnASadd":
                try {
                oTransInquiry.addReservation();
                loadVehicleSalesAdvancesWindow(oTransInquiry.getReservationList().size() - 1, true, pnEditMode);
                loadAdvancesSlip();
                initFields(pnEditMode);
            } catch (SQLException ex) {
                Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnASremove":
            case "btnASCancel":
            case "btnASprint":
                int lnRow = 0;
                int pnCtr = 1;
                ObservableList<ModelInquiryVehicleSalesAdvances> selectedItems = FXCollections.observableArrayList();
                for (ModelInquiryVehicleSalesAdvances item : tblAdvanceSlip.getItems()) {
                    if (item.getSelect().isSelected()) {
                        if ("btnASremove".equals(lsButton) && pnCtr > 1) {
                            ShowMessageFX.Information(getStage(), "Please select atleast 1 slip to be removed.", pxeModuleName, null);
                            return;
                        }
                        if ("btnASCancel".equals(lsButton) && pnCtr > 1) {
                            ShowMessageFX.Information(getStage(), "Please select atleast 1 slip to be cancelled.", pxeModuleName, null);
                            return;
                        }
                        switch (oTransInquiry.getReservation(lnRow, 12).toString()) {
                            case "1":
                                if ("btnASprint".equals(lsButton)) {
                                    ShowMessageFX.Information(getStage(), "Slip No. " + oTransInquiry.getReservation(lnRow, 3).toString() + " is not yet approved. Printing Aborted.", pxeModuleName, null);
                                    return;
                                } else {
                                    selectedItems.add(item);
                                }
                                break;
                            case "2":
                                switch (lsButton) {
                                    case "btnASCancel":
                                        ShowMessageFX.Information(getStage(), "You are not allowed to Cancel Slip No. " + oTransInquiry.getReservation(lnRow, 3).toString(), pxeModuleName, null);
                                        return;
                                    case "btnASremove":
                                        ShowMessageFX.Information(getStage(), "You are not allowed to Remove Slip No. " + oTransInquiry.getReservation(lnRow, 3).toString(), pxeModuleName, null);
                                        return;
                                    case "btnASprint":
                                        selectedItems.add(item);
                                }
                                break;
                            case "0":
                                ShowMessageFX.Information(getStage(), "Slip No. " + oTransInquiry.getReservation(lnRow, 3).toString() + " is already Cancelled.", pxeModuleName, null);
                                return;
                        }
                        pnCtr++;
                    }
                    lnRow++;
                }

                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(getStage(), "No items selected!", pxeModuleName, null);
                } else {
                    switch (lsButton) {
                        case "btnASCancel":
                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel?")) {
                                for (ModelInquiryVehicleSalesAdvances item : selectedItems) {
                                    lnRow = Integer.valueOf(item.getTblindex01()) - 1; // Assuming there is a method to retrieve the transaction number
                                    if (lnRow >= 0) {
                                        loJSON = oTransInquiry.cancelReservation(lnRow);
                                        if ("success".equals((String) loJSON.get("result"))) {
                                            oTransInquiry.loadReservationList();
                                            loadAdvancesSlip();
                                        } else {
                                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                        }
                                    }
                                }
                                ShowMessageFX.Information(getStage(), "Reservation cancelled successfully.", pxeModuleName, null);
                            }
                            break;
                        case "btnASremove":
                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                                for (ModelInquiryVehicleSalesAdvances item : selectedItems) {
                                    lnRow = Integer.valueOf(item.getTblindex01()) - 1; // Assuming there is a method to retrieve the transaction number
                                    if (lnRow >= 0) {
                                        if (String.valueOf(oTransInquiry.getReservation(lnRow, 15)).trim().isEmpty()) {
                                            oTransInquiry.removeReservation(lnRow);
                                        } else {
                                            ShowMessageFX.Information(getStage(), "Reservation No. " + String.valueOf(oTransInquiry.getReservation(lnRow, 3)) + " is already saved cannot be removed.\nReservation needs to be cancelled.", pxeModuleName, null);
                                        }
                                    }
                                }
                            } else {
                                return;
                            }
                            break;
                        case "btnASprint":
                            // Determine the size of the array
                            int numberOfItems = selectedItems.size();

                            // Initialize the array with the determined size
                            Integer[] lnSelRow = new Integer[numberOfItems];

                            // Iterate through the selected items and populate the array
                            int lnCtr = 0;
                            for (ModelInquiryVehicleSalesAdvances item : selectedItems) {
                                lnRow = Integer.valueOf(item.getTblindex01()) - 1; // Assuming getTblindex01() returns a valid integer
                                lnSelRow[lnCtr] = lnRow;
                                lnCtr++;
                            }

                            try {
                                loadVehicleSalesAdvancesPrint(lnSelRow);
                            } catch (SQLException ex) {
                                Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
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
                        loadBankApplicationWindow(pnRow, Integer.parseInt(String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getPayMode())), pnEditMode, true, "");
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
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
                        Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", (String) loJSON.get("message"));
                }
                break;
            case "btnLostSale":
                loJSON = oTransFollow.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    try {
                        loadLostSaleWindow();
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", (String) loJSON.get("message"));
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
        initBtnProcess(pnEditMode);
    }

    /*INQUIRY PROCESS: PRINT VEHICLE SALES ADVANCES*/
    private void loadVehicleSalesAdvancesPrint(Integer[] fnRows) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryReservationPrint.fxml"));
            VehicleInquiryReservationPrintController loControl = new VehicleInquiryReservationPrintController();
            loControl.setGRider(oApp);
            loControl.setVSObject(oTransInquiry);
            loControl.setRows(fnRows);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    private void laodTestModelWindow() {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleTestDriveDialog.fxml"));
            VehicleTestDriveDialogController loControl = new VehicleTestDriveDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransInquiry);
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
            txtField20.setText(oTransInquiry.getMasterModel().getMasterModel().getTestModl());
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadCustomerInquiryInformation() {
        txtField01.setText(oTransInquiry.getMasterModel().getMasterModel().getInqryID());
        if (oTransInquiry.getMasterModel().getMasterModel().getTransactDte() != null && !oTransInquiry.getMasterModel().getMasterModel().getTransactDte().toString().isEmpty()) {
            txtField02.setText(InputTextUtil.xsDateShort(oTransInquiry.getMasterModel().getMasterModel().getTransactDte()));
        }
        txtField03.setText(oTransInquiry.getMasterModel().getMasterModel().getClientNm());
        textArea04.setText(oTransInquiry.getMasterModel().getMasterModel().getAddress());
        txtField05.setText(oTransInquiry.getMasterModel().getMasterModel().getContctNm());
        txtField06.setText(oTransInquiry.getMasterModel().getMasterModel().getMobileNo());
        txtField07.setText(oTransInquiry.getMasterModel().getMasterModel().getEmailAdd());
        txtField08.setText(oTransInquiry.getMasterModel().getMasterModel().getAccount());
        txtField09.setText(oTransInquiry.getMasterModel().getMasterModel().getSalesExe());
        if (oTransInquiry.getMasterModel().getMasterModel().getSourceCD() != null && !oTransInquiry.getMasterModel().getMasterModel().getSourceCD().trim().isEmpty()) {
            comboBox10.getSelectionModel().select(Integer.parseInt(oTransInquiry.getMasterModel().getMasterModel().getSourceCD()));
        }
        txtField11.setText(oTransInquiry.getMasterModel().getMasterModel().getPlatform());
        txtField12.setText(oTransInquiry.getMasterModel().getMasterModel().getActTitle());
        txtField13.setText(oTransInquiry.getMasterModel().getMasterModel().getSalesAgn());
        txtField14.setText(oTransInquiry.getMasterModel().getMasterModel().getBranchNm());
        txtField18.setText(poGetDecimalFormat.format(Double.parseDouble("0.00")));
        switch (oTransInquiry.getMasterModel().getMasterModel().getIntrstLv()) {
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

        txtField20.setText(oTransInquiry.getMasterModel().getMasterModel().getTestModl());
        if (oTransInquiry.getMasterModel().getMasterModel().getIsVhclNw() != null && !oTransInquiry.getMasterModel().getMasterModel().getIsVhclNw().trim().isEmpty()) {
            comboBox21.getSelectionModel().select(Integer.parseInt(oTransInquiry.getMasterModel().getMasterModel().getIsVhclNw()));
        }
        if (oTransInquiry.getMasterModel().getMasterModel().getTargetDt() != null && !oTransInquiry.getMasterModel().getMasterModel().getTargetDt().toString().isEmpty()) {
            datePicker22.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransInquiry.getMasterModel().getMasterModel().getTargetDt())));
        }
        textArea23.setText(oTransInquiry.getMasterModel().getMasterModel().getRemarks());
        textArea24.setText("");

        String inqStats = "";
        if (!oTransInquiry.getMaster("cTranStat").equals("")) {
            switch (String.valueOf(oTransInquiry.getMaster("cTranStat"))) {
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
                    inqStats = "RETIRED";
                    break;
                case "6":
                    inqStats = "CANCELLED";
                    break;
            }
            lblInqStatus.setText(inqStats);
        }
        if (oTransInquiry.getMasterModel().getMasterModel().getPayMode() != null && !oTransInquiry.getMasterModel().getMasterModel().getPayMode().trim().isEmpty()) {
            String sModeOfPayment = "";
            switch (oTransInquiry.getMasterModel().getMasterModel().getPayMode()) {
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
        if (oTransInquiry.getMasterModel().getMasterModel().getCustGrp() != null && !oTransInquiry.getMasterModel().getMasterModel().getCustGrp().trim().isEmpty()) {
            String sCustomerType = "";
            switch (oTransInquiry.getMasterModel().getMasterModel().getCustGrp()) {
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
        if (String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getPlateNo()) != null) {
            lsPlateCSNo = oTransInquiry.getMasterModel().getMasterModel().getPlateNo();
        } else {
            lsPlateCSNo = oTransInquiry.getMasterModel().getMasterModel().getCSNo();
        }
        txtField27.setText(lsPlateCSNo);
        textArea28.setText(oTransInquiry.getMasterModel().getMasterModel().getDescript());
        txtField29.setText("");
        txtField29.setText("");
        txtField30.setText("");
    }

    private void initFields(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField03.setDisable(!lbShow);
        if (oTransInquiry.getMasterModel().getMasterModel().getClientTp().equals("1")) {
            txtField05.setDisable(!lbShow);
        } else {
            txtField05.setDisable(true);

        }
        txtField09.setDisable(!lbShow);
        comboBox10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        rdbtnHtA19.setDisable(!lbShow);
        rdbtnHtB19.setDisable(!lbShow);
        rdbtnHtC19.setDisable(!lbShow);
        comboBox21.setDisable(!lbShow);
        datePicker22.setDisable(!lbShow);
        textArea23.setDisable(!lbShow);
        textArea24.setDisable(!lbShow);
        txtField14.setDisable(true);
        if (fnValue == EditMode.ADDNEW) {
            if (oApp.isMainOffice()) {
                txtField14.setDisable(!lbShow); // Branch Name
                txtField14.setEditable(true); // Branch Name
            } else {
                txtField14.setDisable(true); // Branch Name
                txtField14.setEditable(false); // Branch Name
            }
        }

        switch (comboBox10.getSelectionModel().getSelectedIndex()) {
            case 1:
                txtField11.setDisable(!lbShow);//Online Store
                txtField12.setDisable(true); //Activity ID
                txtField13.setDisable(true); //Agent ID
                break;
            case 3:
                txtField13.setDisable(!lbShow);//Agent ID
                txtField12.setDisable(true); //Activity ID
                txtField11.setDisable(true); //Online Store
                break;
            case 4:
            case 5:
                txtField12.setDisable(!lbShow);//Activity ID
                txtField13.setDisable(true); //Agent ID
                txtField11.setDisable(true); //Online Store
                break;
            default:
                txtField12.setDisable(true); //Activity ID
                txtField13.setDisable(true); //Agent ID
                txtField11.setDisable(true); //Online Store
                break;
        }

        //Inquiry button
        btnTargetVhclAdd.setVisible(lbShow);
        btnPromoAdd.setVisible(lbShow);
        btnPromoRemove.setDisable(true);
        btnTargetVhclRemove.setDisable(true);
        btnTargetVhclRemove.setVisible(lbShow);
        btnPromoRemove.setVisible(lbShow);
        //Inquiry General Button
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnConvertSales.setVisible(false);
        btnConvertSales.setManaged(false);
        btnLostSale.setVisible(false);
        btnLostSale.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        //Bank Application
        btnBankAppNew.setVisible(false);
        //For Follow up
        btnFollowUp.setVisible(false);
        btnProcess.setVisible(false);
        btnProcess.setManaged(false);
        boolean lbTab = (fnValue == EditMode.READY);
        tabInquiryProcess.setDisable(!lbTab);
        tabBankHistory.setDisable(!lbTab);
        tabFollowingHistory.setDisable(!lbTab);
        btnTestDriveModel.setDisable(!lbShow);
//        if (fnValue == EditMode.UPDATE) {
//            switch (String.valueOf(oTransInquiry.getMaster("cTranStat"))) {
//                case "2":
//                case "4":
//                case "5":
//                    if (tabPaneMain.getSelectionModel().getSelectedIndex() == 0) {
//                        trgvIndex03.setVisible(false);
//                        trgvIndex04.setVisible(false);
//                    }
//                    break;
//                default:
//                    if (tabPaneMain.getSelectionModel().getSelectedIndex() == 0) {
//                        trgvIndex03.setVisible(true);
//                        trgvIndex04.setVisible(true);
//                    }
//                    break;
//
//            }
//        }
        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
            trgvIndex03.setVisible(false);
            trgvIndex04.setVisible(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            btnLostSale.setVisible(true);
            btnLostSale.setManaged(true);
            switch (String.valueOf(oTransInquiry.getMaster("cTranStat"))) {
                case "0"://For Follow up
                    if (tabPaneMain.getSelectionModel().getSelectedIndex() == 1) {
                        btnFollowUp.setVisible(true);
                        btnSave.setVisible(false);
                        btnSave.setManaged(false);
                        btnProcess.setVisible(true);
                        btnProcess.setManaged(true);
                    } else {
                        btnProcess.setVisible(false);
                        btnProcess.setManaged(false);
                    }
                    break;
                case "1": //On process
                    if (comboBox25.getSelectionModel().getSelectedIndex() >= 0) {
//                        btnConvertSales.setVisible(true);
//                        btnConvertSales.setManaged(true);
                    }
                    if (comboBox25.getSelectionModel().getSelectedIndex() > 0) {
                        //Bank Application
                        btnBankAppNew.setVisible(true);
                    }
                    //For Follow up
                    btnFollowUp.setVisible(true);
                    break;
                case "3": //VSP
                    //Bank Application
                    btnBankAppNew.setVisible(true);
                    //For Follow up
                    btnFollowUp.setVisible(true);
                    break;
                case "2": //Lost Sale
                case "4": //Sold
                case "5": //Cancelled
                    btnEdit.setVisible(false);
                    btnEdit.setManaged(false);
                    btnLostSale.setVisible(false);
                    btnLostSale.setManaged(false);
                    break;
            }
        }
        if (fnValue == EditMode.READY) {
            switch (tabPaneMain.getSelectionModel().getSelectedIndex()) {
                case 0:
                    switch (String.valueOf(oTransInquiry.getMaster("cTranStat"))) {
                        case "0":
                        case "1":
                        case "3":
                            btnEdit.setVisible(true);
                            btnEdit.setManaged(true);
                            break;
                        default:
                            btnEdit.setVisible(false);
                            btnEdit.setManaged(false);
                            break;
                    }
                    btnProcess.setVisible(false);
                    btnProcess.setManaged(false);
                    break;
                case 1:
                    switch (String.valueOf(oTransInquiry.getMaster("cTranStat"))) {
                        case "1":
                        case "3":
                            btnEdit.setVisible(true);
                            btnEdit.setManaged(true);
                            break;
                        case "0":
                        default:
                            btnEdit.setVisible(false);
                            btnEdit.setManaged(false);
                            break;
                    }
                    initBtnProcess(pnEditMode);
                    break;
                case 2:
                case 3:
                    btnEdit.setVisible(false);
                    btnEdit.setManaged(false);
                    break;
            }
        }
        if (fnValue == EditMode.UPDATE) {
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
            txtField03.setDisable(true);
        }
    }

    private void initInquiryProcessFieldsFalse() {
        // Set visibility for trgvIndex fields
        trgvIndex03.setVisible(true);
        trgvIndex04.setVisible(true);

        // Disable common fields
        setDisable(false, txtField09, comboBox10, txtField11, txtField12, txtField13, txtField14, comboBox21, datePicker22,
                rdbtnHtA19, rdbtnHtB19, rdbtnHtC19, btnTargetVhclAdd, btnTargetVhclRemove, btnPromoAdd, btnPromoRemove, btnTestDriveModel);
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (oTransInquiry.getMasterModel().getMasterModel().getClientTp().equals(1)) {
                txtField05.setDisable(false);
            } else {
                txtField05.setDisable(true);
            }
        }

        // Special conditions
        if (pnEditMode == EditMode.UPDATE) {
            txtField03.setDisable(true);
        }
        txtField14.setEditable(false); // Branch Name
    }

    private void initInquiryProcessFieldsTrue() {
        trgvIndex03.setVisible(true);
        trgvIndex04.setVisible(true);

        setDisable(true, txtField05, txtField09, comboBox10, txtField11, txtField12, txtField13, txtField14, comboBox21, datePicker22,
                rdbtnHtA19, rdbtnHtB19, rdbtnHtC19, btnTargetVhclAdd, btnTargetVhclRemove, btnPromoAdd, btnPromoRemove, btnTestDriveModel);
        txtField14.setEditable(false); // Branch Name
    }

    private void initCustomerInquiryFieldsTrue() {
        rqrmIndex01.setVisible(false);
        vsasCheck01.setVisible(false);
        setVisible(true, btnFollowUp, btnBankAppNew);
        setDisable(true, comboBox25, comboBox26, btnASadd,
                btnASremove, btnASprint, btnASCancel,
                txtField27, btnSndMngerApprov);
        setVisible(false, btnASadd, btnASremove, btnASCancel);

        // Special condition
        if (pnEditMode == EditMode.READY) {
            btnASprint.setDisable(false);
        }
    }

    private void initCustomerInquiryFieldsFalse() {
        // Set visibility and disable properties for fields
        rqrmIndex01.setVisible(true);
        vsasCheck01.setVisible(true);
        setVisible(false, btnFollowUp, btnBankAppNew);
        setDisable(false, comboBox25, comboBox26, btnASadd, btnASremove, btnASprint, btnASCancel, txtField27, btnSndMngerApprov);
        setVisible(true, btnASadd, btnASCancel);

        btnASremove.setVisible(oTransInquiry.getReservationList().size() > 0);

        if (pnEditMode == EditMode.READY) {
            btnASprint.setDisable(true);
        }
    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    private void setVisible(boolean visible, Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(visible);
        }
    }


    /*INQUIRY PROCESS*/
    private void initBtnProcess(int fnValue) {
        pnRow = 0;
        /* NOTE:
            lbShow (FALSE)= invisible
            !lbShow (TRUE)= visible
         */

        boolean lbShow = (fnValue == EditMode.UPDATE);
        if (oTransInquiry.getMaster(
                "cTranStat") != null) {
            if (oTransInquiry.getMaster("cTranStat").equals("0")) {
                rqrmIndex01.setVisible(true);
            } else {
                rqrmIndex01.setVisible(lbShow);
            }
        }

        /*INQUIRY PROCESS*/
        //Requirements
        comboBox25.setDisable(!lbShow);
        txtField27.setDisable(!lbShow);
        btnASCancel.setVisible(lbShow);
        btnSndMngerApprov.setDisable(!lbShow);
        switch (comboBox25.getSelectionModel().getSelectedIndex()) {
            case 0: //CASH
            case 1: // Bank Purchase order
            case 3: // Company Purchase Order
                comboBox26.setDisable(true);
                break;
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
        btnASadd.setVisible(lbShow);
        btnASremove.setVisible(lbShow);
        btnASprint.setVisible(lbShow);
        btnProcess.setVisible(false);
        btnProcess.setManaged(false);

        if (fnValue == EditMode.READY || (lbShow)) {
            btnASprint.setVisible(true);

            //Enable Button / textfield based on Inquiry Status
            switch (String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getTranStat())) { // cTranStat
                case "0"://For Follow up
                    rqrmIndex01.setVisible(true);
                    //Requirements
                    comboBox25.setDisable(false);
                    txtField27.setDisable(false);
                    btnSndMngerApprov.setDisable(false);
                    switch (comboBox25.getSelectionModel().getSelectedIndex()) {
                        case 0:
                        case 1:
                        case 3:
                            comboBox26.setDisable(true);
                            break;
                        case 2:
                        case 4:
                            comboBox26.setDisable(false);
                            break;
                    }
                    //Reservation
                    btnASadd.setVisible(true);
                    btnASremove.setVisible(true);
                    btnASCancel.setVisible(true);
                    btnASprint.setVisible(true);
                    if (tabPaneMain.getSelectionModel().getSelectedIndex() == 1) {
                        btnProcess.setVisible(true);
                        btnProcess.setManaged(true);
                    } else {
                        btnProcess.setVisible(false);
                        btnProcess.setManaged(false);
                    }
                    break;
//                    case "1": //On process
//                    case "3": //VSP
//                         //Requirements
//                         cmbInqpr01.setDisable(false);
//                         cmbInqpr02.setDisable(false);
//                         //Approved by
//                         txtField21.setDisable(false);
//                         //Reservation
//                         btnASadd.setVisible(true);
//                         btnASremove.setVisible(true);
//                         btnAScancel.setVisible(true);
//                         btnASprintview.setVisible(true);
//                         btnASprint.setVisible(true);
//
//                         //General button
//                         btnProcess.setVisible(true);
//                         break;
                case "2": //Lost Sale
                case "4": //Sold
                case "5": //Cancelled
                    //Requirements
                    comboBox25.setDisable(true);
                    comboBox26.setDisable(true);
                    //Reservation
                    btnASadd.setVisible(false);
                    btnASremove.setVisible(false);
                    btnASCancel.setVisible(false);
                    btnASprint.setVisible(false);
                    //General button
                    btnProcess.setVisible(false);
                    btnProcess.setManaged(false);
                    break;
            }

        }

    }

    private void clearCustomerFields() {
        pnRow = 0;
        lblInqStatus.setText("");
        txtField18.setText("0.00");

        clearTextFields(txtField01, txtField02, txtField03, txtField05, txtField06, txtField07, txtField08, txtField09,
                txtField11, txtField12, txtField13, txtField14, txtField16, txtField20,
                txtField27, txtField29, txtField30);

        clearTextFields(textArea04, textArea23, textArea24, textArea28);

        resetComboBoxes(comboBox10, comboBox21, comboBox25, comboBox26);

        resetRadioButtons(rdbtnHtA19, rdbtnHtB19, rdbtnHtC19);

        datePicker22.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())));
    }

    private void clearTextFields(TextInputControl... fields) {
        for (TextInputControl field : fields) {
            field.setText("");
        }
    }

    private void resetComboBoxes(ComboBox<?>... comboBoxes) {
        for (ComboBox<?> comboBox : comboBoxes) {
            comboBox.setValue(null);
        }
    }

    private void resetRadioButtons(RadioButton... radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.setSelected(false);
        }
    }

    private void clearTables() {
        priorityunitdata.clear();
        promosoffereddata.clear();
        inqrequirementsdata.clear();
        inqvsadata.clear();
        bankappdata.clear();
        followupdata.clear();
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
        for (lnCtr = 0; lnCtr <= oTransInquiry.getVehiclePriorityList().size() - 1; lnCtr++) {
            priorityunitdata.add(new ModelInquiryVehiclePriority(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransInquiry.getVehiclePriority(lnCtr, "nPriority")),
                    String.valueOf(oTransInquiry.getVehiclePriority(lnCtr, "sDescript")).toUpperCase(),
                    String.valueOf(oTransInquiry.getVehiclePriority(lnCtr, "sVhclIDxx")).toUpperCase()
            ));
        }
    }

    private void initVehiclePriority() {
        trgvIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01")); // nPriority
        trgvIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex03")); // Description

        trgvIndex03.setCellFactory(param -> new TableCell<ModelInquiryVehiclePriority, Button>() {
            Button upBtn = new Button();

            {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("ARROW_UP");
                icon.setSize("12px");
                icon.setStyleClass("button-icon");
                upBtn.setGraphic(icon);

                upBtn.setOnAction(event -> {
                    ModelInquiryVehiclePriority model = getTableView().getItems().get(getIndex());
                    ObservableList<ModelInquiryVehiclePriority> items = tblPriorityUnit.getItems();
                    int currentIndex = getIndex();

                    if (currentIndex > 0) {
                        // Swap items
                        ModelInquiryVehiclePriority prevModel = items.get(currentIndex - 1);
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

        trgvIndex04.setCellFactory(param -> new TableCell<ModelInquiryVehiclePriority, Button>() {
            Button downBtn = new Button();

            {
                FontAwesomeIconView icon = new FontAwesomeIconView();
                icon.setGlyphName("ARROW_DOWN");
                icon.setSize("12px");
                icon.setStyleClass("button-icon");
                downBtn.setGraphic(icon);

                downBtn.setOnAction(event -> {
                    ModelInquiryVehiclePriority model = getTableView().getItems().get(getIndex());
                    ObservableList<ModelInquiryVehiclePriority> items = tblPriorityUnit.getItems();
                    int currentIndex = getIndex();

                    if (currentIndex < items.size() - 1) {
                        // Swap items
                        ModelInquiryVehiclePriority nextModel = items.get(currentIndex + 1);
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

        tblPriorityUnit.setItems(priorityunitdata);
    }

    private void updateModel(ObservableList<ModelInquiryVehiclePriority> items) {
        // Iterate over the entire list
        for (int pnCtr = 0; pnCtr < items.size(); pnCtr++) {
            ModelInquiryVehiclePriority unit = items.get(pnCtr);
            try {
                // Update the fields with new row positions
                int lnPrio = pnCtr + 1; // Row number starts from 1
                String lsDescript = unit.getTblindex03();
                String lsVhclID = unit.getTblindex04();

                // Update the data model
                oTransInquiry.setVehiclePriority(pnCtr, "nPriority", lnPrio);
                oTransInquiry.setVehiclePriority(pnCtr, "sDescript", lsDescript);
                oTransInquiry.setVehiclePriority(pnCtr, "sVhclIDxx", lsVhclID);

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
                btnPromoRemove.setDisable(false);
            }
        }
    }

    private void loadPromoOffered() {
        promosoffereddata.clear();
        for (lnCtr = 0; lnCtr <= oTransInquiry.getPromoList().size() - 1; lnCtr++) {
            promosoffereddata.add(new ModelInquiryPromoOffered(
                    String.valueOf(lnCtr + 1), //ROW
                    InputTextUtil.xsDateShort((Date) oTransInquiry.getPromo(lnCtr, "dDateFrom")),
                    InputTextUtil.xsDateShort((Date) oTransInquiry.getPromo(lnCtr, "dDateThru")),
                    String.valueOf(oTransInquiry.getPromo(lnCtr, "sActTitle")).toUpperCase(),
                    String.valueOf(oTransInquiry.getPromo(lnCtr, "sPromoIDx")).toUpperCase()
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
        JSONObject loJSON = oTransInquiry.loadRequirements();
        boolean lbCheck = false;
        String lsReceive = "";
        String lsReceiveDte = "";
        if (!"error".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr < oTransInquiry.getRequirementList().size(); lnCtr++) { // Changed loop condition
                lbCheck = String.valueOf(oTransInquiry.getRequirement(lnCtr, "cSubmittd")).equals("1");
                if (lbCheck) {
                    lsReceive = String.valueOf(oTransInquiry.getRequirement(lnCtr, "sCompnyNm"));
                    lsReceiveDte = InputTextUtil.xsDateShort((Date) oTransInquiry.getRequirement(lnCtr, "dReceived"));
                } else {
                    lsReceive = "";
                    lsReceiveDte = "";
                }
                inqrequirementsdata.add(new ModelInquiryRequirements(
                        lbCheck,
                        String.valueOf(oTransInquiry.getRequirement(lnCtr, "sDescript")),
                        lsReceive.toUpperCase(),
                        lsReceiveDte,
                        String.valueOf(oTransInquiry.getRequirement(lnCtr, "sRqrmtCde"))
                ));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void initInquiryRequirements() {
        tblRequirementsInfo.setEditable(true);
        tblRequirementsInfo.setSelectionModel(null);
        rqrmIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        rqrmIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        rqrmIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        rqrmIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));

        rqrmIndex01.setCellFactory(CheckBoxTableCell.forTableColumn((Integer index) -> {
            ModelInquiryRequirements requirement = inqrequirementsdata.get(index);
            BooleanProperty selected = requirement.selectedProperty();
            loadInquiryRequirements();
            selected.addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    JSONObject loJSON = oTransInquiry.searchEmployee(requirement.getTblindex05(), requirement.getTblindex02());
                    if (!"error".equals(loJSON.get("result"))) {
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                    }
                } else {
                    oTransInquiry.removeEmployee(requirement.getTblindex05());
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
        if (pnEditMode == EditMode.UPDATE) {
            pnRow = tblAdvanceSlip.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblAdvanceSlip.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid reservation information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadVehicleSalesAdvancesWindow(pnRow, false, pnEditMode);
                    loadAdvancesSlip();

                } catch (SQLException ex) {
                    Logger.getLogger(VehicleInquiryFormController.class
                            .getName()).log(Level.SEVERE, null, ex);
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
        for (int lnCtr = 0; lnCtr <= oTransInquiry.getReservationList().size() - 1; lnCtr++) {
            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "dTransact")) != null) {
                lsResDte = InputTextUtil.xsDateShort((Date) oTransInquiry.getReservation(lnCtr, "dTransact"));
            }
            switch (String.valueOf(oTransInquiry.getReservation(lnCtr, "cResrvTyp"))) {
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

            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "nAmountxx")) != null) {
                lsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransInquiry.getReservation(lnCtr, "nAmountxx"))));
            }
            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "sApproved")) != null) {
                lsApprovedBy = String.valueOf(oTransInquiry.getReservation(lnCtr, "sApproved"));
            }
            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "dApproved")) != null) {
                lsApprovedDte = InputTextUtil.xsDateShort((Date) oTransInquiry.getReservation(lnCtr, "dApproved"));
            }
            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "cTranStat")) != null) {
                switch (String.valueOf(oTransInquiry.getReservation(lnCtr, "cTranStat"))) {
                    case "0":
                        lsInqStat = "CANCELLED";
                        break;
                    case "1":
                        lsInqStat = "FOR APPROVAL";
                        break;
                    case "2":
                        lsInqStat = "APPROVED";
                        break;
                }
            }
            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "sReferNox")) != null) {
                lsRsNox = String.valueOf(oTransInquiry.getReservation(lnCtr, "sReferNox"));
            }
            if (String.valueOf(oTransInquiry.getReservation(lnCtr, "sTransNox")) != null) {
                lsSlipNo = String.valueOf(oTransInquiry.getReservation(lnCtr, "sTransNox"));
            }
            inqvsadata.add(new ModelInquiryVehicleSalesAdvances(
                    String.valueOf(lnCtr + 1),
                    lsResDte,
                    String.valueOf(oTransInquiry.getReservation(lnCtr, "cResrvTyp")),
                    lsResType,
                    lsAmount,
                    lsInqStat,
                    String.valueOf(oTransInquiry.getReservation(lnCtr, "sRemarksx")),
                    lsApprovedBy,
                    lsApprovedDte,
                    lsRsNox,
                    String.valueOf(oTransInquiry.getReservation(lnCtr, "sCompnyNm")),
                    "",
                    ""));
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
            loControl.setVSAObject(oTransInquiry);
            loControl.setTableRows(fnRow);
            loControl.setState(isAdd);
            loControl.setInqStat(Integer.parseInt(oTransInquiry.getMasterModel().getMasterModel().getTranStat()));
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
            loControl.setSource(oTransInquiry.getMasterModel().getMasterModel().getTransNo());
            loControl.setState(isAdd);
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
                    ModelVehicleInquiryBankApplications selectedItem = tblBankApplication.getItems().get(pnRow);
                    String lsTransNox = selectedItem.getTblindex02();
                    loadBankApplicationWindow(pnRow, Integer.parseInt(String.valueOf(oTransInquiry.getMasterModel().getMasterModel().getPayMode())), pnEditMode, false, lsTransNox);
                    loadBankApplications();
                } catch (SQLException ex) {
                    Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
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
            loJSON = oTransInquiry.loadBankApplicationList();
            if ("success".equals((String) loJSON.get("result"))) {
                for (int lnCtr = 1; lnCtr <= oTransInquiry.getBankApplicationCount(); lnCtr++) {
                    if (!String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sCancelld")).isEmpty()) {
                        lsCancelledDt = InputTextUtil.xsDateShort((Date) oTransInquiry.getBankApplicationDetail(lnCtr, "dCancelld"));
                        lsStatus = "CANCELLED";
                    } else {
                        if (oTransInquiry.getBankApplicationDetail(lnCtr, "cTranStat") != null) {
                            switch (String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "cTranStat"))) {
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
                    if (String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "cPayModex")) != null) {
                        switch (String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "cPayModex"))) {
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
                        if (oTransInquiry.getBankApplicationDetail(lnCtr, "sBankType") != null) {
                            switch ((String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sBankType")))) {
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
                        Logger.getLogger(VehicleInquiryFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    bankappdata.add(new ModelVehicleInquiryBankApplications(
                            String.valueOf(lnCtr),
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sTransNox")),
                            lsBanktype,
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sBankIDxx")),
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sBankName")),
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sBrBankNm")),
                            lsPaymode,
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "dAppliedx")),
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "sApplicNo")),
                            lsCancelledDt,
                            lsStatus,
                            String.valueOf(oTransInquiry.getBankApplicationDetail(lnCtr, "dApproved"))
                    ));
                    lsCancelledDt = "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehicleInquiryFormController.class
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
                    ModelInquiryFollowUp selectedItem = tblFollowHistory.getItems().get(pnRow);
                    String lsTransNox = selectedItem.getTblindex01();
                    loadFollowUpWindow(pnRow, false, lsTransNox);
                    loadFollowHistory();
                } catch (SQLException ex) {
                    Logger.getLogger(VehicleInquiryFormController.class
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
            loControl.setSource(oTransInquiry.getMasterModel().getMasterModel().getTransNo());
            loControl.setRefNo(fsRefNox);
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
        loJSON = oTransInquiry.loadFollowUpList();
        if ("success".equals((String) loJSON.get("result"))) {
            try {
                for (int lnCtr = 1; lnCtr <= oTransInquiry.getFollowUpCount(); lnCtr++) {
                    if (oTransInquiry.getFollowUpDetail(lnCtr, "dFollowUp") != null) {
                        lsFollowUpDate = InputTextUtil.xsDateShort((Date) oTransInquiry.getFollowUpDetail(lnCtr, "dFollowUp"));
                    }
                    if (oTransInquiry.getFollowUpDetail(lnCtr, "dTransact") != null) {
                        lsTransact = InputTextUtil.xsDateShort((Date) oTransInquiry.getFollowUpDetail(lnCtr, "dTransact"));
                    }
                    if (oTransInquiry.getFollowUpDetail(lnCtr, "sMethodCd") != null) {
                        lsMethod = String.valueOf(oTransInquiry.getFollowUpDetail(lnCtr, "sMethodCd"));
                    }
                    if (oTransInquiry.getFollowUpDetail(lnCtr, "tFollowUp") != null) {
                        lsFollowTime = String.valueOf(oTransInquiry.getFollowUpDetail(lnCtr, "tFollowUp"));
                    }
                    if (oTransInquiry.getFollowUpDetail(lnCtr, "sMethodCd") != null) {
                        if (String.valueOf(oTransInquiry.getFollowUpDetail(lnCtr, "sMethodCd")).equals("UPDATE")) {
                            lsPlatForm = "";
                            lsFollowUpDate = "";
                            lsFollowTime = "";
                        } else {
                            if (oTransInquiry.getFollowUpDetail(lnCtr, "sPlatform") != null) {
                                lsPlatForm = String.valueOf(oTransInquiry.getFollowUpDetail(lnCtr, "sPlatform"));
                            }
                        }
                    }
                    followupdata.add(new ModelInquiryFollowUp(
                            String.valueOf(lnCtr),
                            String.valueOf(oTransInquiry.getFollowUpDetail(lnCtr, "sReferNox")),
                            lsTransact,
                            lsFollowUpDate,
                            lsFollowTime,
                            lsMethod,
                            lsPlatForm,
                            String.valueOf(oTransInquiry.getFollowUpDetail(lnCtr, "sRemarksx"))));
                }
            } catch (SQLException ex) {
                Logger.getLogger(VehicleInquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void loadLostSaleWindow() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryLostSaleForm.fxml"));
            VehicleInquiryLostSaleFormController loControl = new VehicleInquiryLostSaleFormController();
            loControl.setGRider(oApp);
            loControl.setFollowUpObject(oTransFollow);
            loControl.setInquiryObject(oTransInquiry);
            loControl.setTransNo(oTransInquiry.getMasterModel().getMasterModel().getTransNo());
            loControl.setsVSPNox("");
            loControl.setState(true); //If true set tag to lost sale automatically else allow user to edit.
            loControl.setClientName(oTransInquiry.getMasterModel().getMasterModel().getClientNm());
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
            loJSON = oTransInquiry.openTransaction(oTransInquiry.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadCustomerInquiryInformation();
                loadVehiclePriority();
                loadPromoOffered();
                loadInquiryRequirements();
                loadAdvancesSlip();
                loadBankApplications();
                loadFollowHistory();
                pnEditMode = oTransInquiry.getEditMode();
                initFields(pnEditMode);
            }
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
}
