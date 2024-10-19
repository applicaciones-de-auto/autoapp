package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.autoapp.controllers.parameters.ActivitySourceTypeController;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.models.general.ActivityMember;
import org.guanzon.autoapp.models.general.ActivityLocation;
import org.guanzon.autoapp.models.general.ActivityVehicle;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityInformationController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Activity oTrans;
    private ObservableList<ActivityLocation> locationData = FXCollections.observableArrayList();
    private ObservableList<ActivityMember> actMembersData = FXCollections.observableArrayList();
    private ObservableList<ActivityVehicle> actVhclModelData = FXCollections.observableArrayList();
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Activity Information"; //Form Title
    private int pnEditMode;//Modifying fields
    private double xOffset = 0;
    private double yOffset = 0;
    private int pnRow = -1;
    private int iTabIndex = 0; //Set tab index
    private int lnCtr;
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnPrint, btnActivityHistory, btnActCancel, btnClose, btnAddSource, btnAddRowTasks, btnRemoveTasks, btnAddRowBudget, btnRemoveBudget, btnTabAdd, btnTabRem;
    @FXML
    private Label lblApprovedDate, lblCancelStatus, lblActivityID, lblActivityIDValue;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private DatePicker dateFrom03, dateTo04;
    @FXML
    private TextField txtField02, txtField06, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16;
    @FXML
    private ComboBox<String> comboBox05;
    @FXML
    private TextArea textArea07, textArea08, textArea09, textArea10;
    @FXML
    private TabPane tabPCustCont;
    @FXML
    private Tab tabLocationInfo, tabMembersInfo, tabVehicleInfo, tabActivityInfo, tabDetails;
    @FXML
    private TableView<ActivityLocation> tblLocationAddress;
    @FXML
    private TableColumn<ActivityLocation, String> locationAddres01, locationAddres02, locationAddres03, locationAddres04;
    @FXML
    private TableView<ActivityMember> tblViewActivityMembers;
    @FXML
    private TableColumn<ActivityMember, String> tblMembersIndex01, tblMembersIndex02, tblMembersIndex03;
    @FXML
    private TableView<ActivityVehicle> tblViewVhclModels;
    @FXML
    private TableColumn<ActivityVehicle, String> tblVhclIndex01, tblVhclIndex02, tblVhclIndex03;

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
        oTrans = new Activity(oApp, false, oApp.getBranchCode());

        initActLocationTable();
        initActMembersTable();
        initActivityVehicleTable();
        dateFrom03.setDayCellFactory(DateFrom);
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
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField06, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea08, textArea09, textArea10);
        loTxtArea.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        if (oTrans.getModel().getModel().getActvtyID() != null) {
            lblActivityID.setText("ACTIVITY ID: ");
            lblActivityIDValue.setText(oTrans.getModel().getModel().getActvtyID());
        } else {
            lblActivityIDValue.setText("");
            lblActivityID.setText("");
        }
        txtField02.setText(oTrans.getModel().getModel().getActNo());
        if (oTrans.getModel().getModel().getDateFrom() != null && !oTrans.getModel().getModel().getDateFrom().toString().isEmpty()) {
            dateFrom03.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getModel().getModel().getDateFrom(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        if (oTrans.getModel().getModel().getDateThru() != null && !oTrans.getModel().getModel().getDateThru().toString().isEmpty()) {
            dateTo04.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getModel().getModel().getDateThru(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        if (oTrans.getModel().getModel().getEventTyp() != null && !oTrans.getModel().getModel().getEventTyp().trim().isEmpty()) {
            switch (String.valueOf(oTrans.getModel().getModel().getEventTyp())) {
                case "eve":
                    comboBox05.setValue("EVENT");
                    break;
                case "sal":
                    comboBox05.setValue("SALES CALL");
                    break;
                case "pro":
                    comboBox05.setValue("PROMO");
                    break;
            }
        }
        txtField06.setText(oTrans.getModel().getModel().getActTypDs().trim());
        textArea07.setText(oTrans.getModel().getModel().getActTitle());
        textArea08.setText(oTrans.getModel().getModel().getActDesc());
        textArea09.setText(oTrans.getModel().getModel().getLogRemrk());
        textArea10.setText(oTrans.getModel().getModel().getRemarks());
        txtField11.setText(oTrans.getModel().getModel().getDeptName());
        txtField12.setText(oTrans.getModel().getModel().getEmpInCharge());
        txtField13.setText(oTrans.getModel().getModel().getBranchNm());
        txtField14.setText(oTrans.getModel().getModel().getLocation());
        txtField15.setText(String.valueOf(oTrans.getModel().getModel().getTrgtClnt()));
        txtField16.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getModel().getModel().getRcvdBdgt()))));
        String lsApprvDate = "";
        if (oTrans.getModel().getModel().getApproveDte() != null) {
            lsApprvDate = CustomCommonUtil.xsDateShort(oTrans.getModel().getModel().getApproveDte());//dApproved
        }
        lblApprovedDate.setText(lsApprvDate);
        switch (oTrans.getModel().getModel().getTranStat()) {
            case "0":
                lblCancelStatus.setText("Deactivated");
            case "1":
                lblCancelStatus.setText("For Approval");
                break;
            case "2":
                lblCancelStatus.setText("Cancelled");
                break;
            case "3":
                lblCancelStatus.setText("Approved");
                break;
            default:
                lblCancelStatus.setText("");
                break;
        }

        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern numOnly = Pattern.compile("[0-9]*");
        Pattern decOnly = Pattern.compile("[0-9,.]*");
        txtField15.setTextFormatter(new TextFormatterUtil(numOnly));  //nTrgtClnt
        txtField16.setTextFormatter(new TextFormatterUtil(decOnly));  //nRcvdBdgt
    }

    @Override
    public void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField15, 4);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField15, txtField16);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea08, textArea09, textArea10);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
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
                case 15:/*No of Target Client*/
                    oTrans.getModel().getModel().setTrgtClnt(Integer.valueOf(lsValue));
                    break;
                case 16:/*Total Event Budget*/
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getModel().getModel().setRcvdBdgt(Double.valueOf(lsValue.replace(",", "")));
                    txtField16.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getModel().getModel().getRcvdBdgt()))));
                    break;
            }
        } else {
            txtField.selectAll();

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
                case 7:
                    oTrans.getModel().getModel().setActTitle(lsValue);
                    checkExistingActivityInformation();
                    break;
                case 8:
                    oTrans.getModel().getModel().setActDesc(lsValue);
                    break;
                case 9:
                    oTrans.getModel().getModel().setLogRemrk(lsValue);
                    break;
                case 10:
                    oTrans.getModel().getModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField06, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea08, textArea09, textArea10);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));

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
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case TAB:
                    case ENTER:
                    case F3:
                        switch (txtFieldID) {
                            case "txtField06":
                                loJSON = oTrans.searchEventType(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField06.setText(oTrans.getModel().getModel().getActTypDs());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField06.setText("");
                                    return;
                                }
                                break;
                            case "txtField11":
                                loJSON = oTrans.searchDepartment(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField11.setText(oTrans.getModel().getModel().getDeptName());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField11.setText("");
                                    return;
                                }
                                break;
                            case "txtField12":
                                loJSON = oTrans.searchEmployee(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField12.setText(oTrans.getModel().getModel().getEmpInCharge());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField12.setText("");
                                    return;
                                }
                                break;
                            case "txtField13":
                                loJSON = oTrans.searchBranch(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField13.setText(oTrans.getModel().getModel().getBranchNm());
                                    txtField14.setText(oTrans.getModel().getModel().getLocation());
                                    checkExistingActivityInformation();
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField13.setText("");
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnPrint, btnActivityHistory,
                btnActCancel, btnClose, btnAddSource, btnAddRowTasks, btnRemoveTasks, btnAddRowBudget, btnRemoveBudget, btnTabAdd, btnTabRem);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        try {
            JSONObject loJSON = new JSONObject();
            String lsButton = ((Button) event.getSource()).getId();
            iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
            switch (lsButton) {
                case "btnAdd":
                    clearFields();
                    clearTables();
                    oTrans = new Activity(oApp, false, oApp.getBranchCode());
                    loJSON = oTrans.newRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    break;
                case "btnEdit":
                    loJSON = oTrans.updateRecord();
                    pnEditMode = oTrans.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                    break;
                case "btnSave":
                    LocalDate loDateFrom = dateFrom03.getValue();
                    LocalDate loDateTo = dateTo04.getValue();
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to save?")) {
                        if (checkExistingActivityInformation()) {
                            return;
                        }
                        if (loDateFrom != null && loDateTo != null && loDateFrom.isAfter(loDateTo)) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid date from.", "Warning", null);
                            return;
                        }

                        if (loDateFrom != null && loDateTo != null && loDateTo.isBefore(loDateFrom)) {
                            ShowMessageFX.Warning(getStage(), "Please enter a valid date to.", "Warning", null);
                            return;
                        }

                        if (comboBox05.getSelectionModel().isEmpty()) {
                            ShowMessageFX.Warning(getStage(), "Please choose a value for Active Type", "Warning", null);
                            return;
                        }
                        if (textArea07.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Title.", "Warning", null);
                            textArea07.requestFocus();
                            return;
                        }
                        if (textArea08.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Activity Description", "Warning", null);
                            textArea08.requestFocus();
                            return;
                        }
                        if (txtField11.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Department in charge.", "Warning", null);
                            txtField11.requestFocus();
                            return;
                        }
                        if (txtField12.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Person in charge.", "Warning", null);
                            txtField12.requestFocus();
                            return;
                        }
                        if (txtField13.getText().trim().equals("")) {
                            ShowMessageFX.Warning(getStage(), "Please enter a value for Branch in charge", "Warning", null);
                            txtField13.requestFocus();
                            return;
                        }
                        if (!setSelection()) {
                            return;
                        }
                        loJSON = oTrans.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Activity Information", (String) loJSON.get("message"));
                            loJSON = oTrans.openRecord(oTrans.getModel().getModel().getActvtyID());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loadActivityLocationTable();
                                loadActMembersTable();
                                loadActivityVehicleTable();
                                loadMasterFields();
                                initFields(pnEditMode);
                                pnEditMode = oTrans.getEditMode();
                            }
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    }
                    break;
                case "btnCancel":
                    if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                        clearFields();
                        clearTables();
                        oTrans = new Activity(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                case "btnBrowse":
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.YesNo(null, "Search Activity Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }
                    }
                    loJSON = oTrans.searchRecord("", false);
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadActivityLocationTable();
                        loadActMembersTable();
                        loadActivityVehicleTable();
                        loadMasterFields();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(null, "Search Activity Information Confirmation", (String) loJSON.get("message"));
                    }
                    break;
                case "btnClose":
                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                        if (poUnload != null) {
                            poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                        }
                    }
                    break;
                case "btnAddSource":
                    loadActTypeAddSourceDialog();
                    break;
                case "btnTabAdd":
                    switch (iTabIndex) {
                        case 0: // Address
                            oTrans.addActLocation();
                            loadActLocationDialog(oTrans.getActLocationList().size() - 1, true);
                            loadActivityLocationTable();
                            break;
                        case 1: //Members
                            if (oTrans.getModel().getModel().getEmployID() == null) {
                                ShowMessageFX.Warning(null, "Warning", "Please enter value in Person in charge.");
                                return;
                            } else {
                                if (oTrans.getModel().getModel().getEmployID().isEmpty()) {
                                    ShowMessageFX.Warning(null, "Warning", "Please enter value in Person in charge.");
                                    return;
                                }
                            }
                            loadActivityMemberDialog();
                            loadActMembersTable();
                            break;
                        case 2: //Vehicle
                            loadActivityVehicleDialog();
                            loadActivityVehicleTable();
                            break;
                    }
                    break;
                case "btnTabRem":
                    if (pnRow < 0) {
                        ShowMessageFX.Warning(null, "Warning", "No selected item");
                        return;
                    }
                    switch (iTabIndex) {
                        case 0:
                            if (ShowMessageFX.YesNo(null, "Activity Location Confirmation", "Are you sure you want to remove this Activity Location?")) {
                            } else {
                                return;
                            }
                            oTrans.removeActLocation(pnRow);
                            pnRow = 0;
                            loadActivityLocationTable();
                            break;
                        case 1:
                            oTrans.removeActMember(pnRow);
                            pnRow = 0;
                            loadActMembersTable();
                            break;
                        case 2:
                            if (ShowMessageFX.YesNo(null, "Activity Vehicle Confirmation", "Are you sure you want to remove this  Activity Vehicle?")) {
                            } else {
                                return;
                            }
                            oTrans.removeActVehicle(pnRow);
                            pnRow = 0;
                            loadActivityVehicleTable();
                            break;
                    }
                    btnTabRem.setVisible(false);
                    break;
                case "btnPrint":
                    loadActivityPrint();
                    break;
                case "btnActCancel":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to cancel this activity?") == true) {
                        String fsValue = oTrans.getModel().getModel().getActvtyID();
                        loJSON = oTrans.cancelRecord(fsValue);
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Activity Information", (String) loJSON.get("message"));
                        } else {
                            ShowMessageFX.Warning(null, "Activity Information", (String) loJSON.get("message"));
                        }
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getActvtyID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadActivityLocationTable();
                            loadActMembersTable();
                            loadActivityVehicleTable();
                            loadMasterFields();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    }
                    break;
                default:
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                    break;
            }
            initFields(pnEditMode);
        } catch (IOException ex) {
            Logger.getLogger(ActivityInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initComboBoxItems() {
        comboBox05.setItems(cType);
    }

    @Override
    public void initFieldsAction() {
        dateFrom03.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (pnEditMode == EditMode.ADDNEW) {
                    dateTo04.setDayCellFactory(DateTo);
                    dateTo04.setValue(newValue.plusDays(1));
                }
            }
        });
        dateFrom03.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getModel().getModel().setDateFrom(SQLUtil.toDate(dateFrom03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
                checkExistingActivityInformation();
            }
        });
        dateTo04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getModel().getModel().setDateThru(SQLUtil.toDate(dateTo04.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
                checkExistingActivityInformation();
            }
        });
        comboBox05.setOnAction(e -> {
            int selectedComboBox05 = comboBox05.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox05 >= 0) {
                    switch (selectedComboBox05) {
                        case 0:
                            oTrans.getModel().getModel().setEventTyp("eve");
                            oTrans.getModel().getModel().setActTypDs("");
                            oTrans.getModel().getModel().setActSrce("");
                            txtField06.setText("");
                            break;
                        case 1:
                            oTrans.getModel().getModel().setEventTyp("sal");
                            oTrans.getModel().getModel().setActTypDs("");
                            oTrans.getModel().getModel().setActSrce("");
                            txtField06.setText("");
                            break;
                        case 2:
                            oTrans.getModel().getModel().setEventTyp("pro");
                            oTrans.getModel().getModel().setActTypDs("");
                            oTrans.getModel().getModel().setActSrce("");
                            txtField06.setText("");
                            break;
                    }
                    checkExistingActivityInformation();
                }
                initFields(pnEditMode);
            }
        }
        );
    }

    @Override
    public void initTextFieldsProperty() {
        txtField06.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setActSrce("");
                                oTrans.getModel().getModel().setActTypDs("");
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
                                oTrans.getModel().getModel().setDeptID("");
                                oTrans.getModel().getModel().setEmployID("");
                                txtField12.setText("");
                                initFields(pnEditMode);
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
                                oTrans.getModel().getModel().setEmployID("");
                                initFields(pnEditMode);
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
                                oTrans.getModel().getModel().setBranchNm("");
                                oTrans.getModel().getModel().setLocation("");
                                txtField14.setText("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        tabPCustCont.getSelectionModel()
                .selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) -> {
                    pnRow = 0;
                    btnTabRem.setVisible(false);
                }
                );
    }

    @Override
    public void clearTables() {
        locationData.clear();
        actMembersData.clear();
        actVhclModelData.clear();
    }

    @Override
    public void clearFields() {
        lblActivityID.setText("");
        lblActivityIDValue.setText("");
        CustomCommonUtil.setText("", txtField02, txtField06,
                txtField11, txtField12, txtField13, txtField14);
        CustomCommonUtil.setText("", textArea07, textArea08,
                textArea09, textArea10);
        CustomCommonUtil.setText("", lblCancelStatus, lblApprovedDate);
        dateFrom03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        dateTo04.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        comboBox05.setValue("");
        txtField15.setText("0");
        txtField16.setText("0.00");
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        CustomCommonUtil.setDisable(!lbShow, dateFrom03, dateTo04, comboBox05, textArea07, textArea08,
                textArea09, textArea10, txtField11, txtField13, txtField15, txtField16);
        txtField06.setDisable(!(lbShow && !comboBox05.getValue().isEmpty()));
        txtField12.setDisable(!(lbShow && !txtField11.getText().isEmpty()));

        CustomCommonUtil.setVisible(false, btnActCancel, btnTabRem, btnEdit, btnPrint);
        CustomCommonUtil.setManaged(false, btnActCancel, btnTabRem, btnEdit, btnPrint);
        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel, btnTabAdd);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel, btnTabAdd);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnTabAdd.setDisable(!lbShow);
//        btnActivityHistory.setVisible(false);
//        btnActivityHistory.setManaged(false);
        if (fnValue == EditMode.READY) {
            if (lblCancelStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(false, btnActCancel, btnEdit, btnPrint);
                CustomCommonUtil.setManaged(false, btnActCancel, btnEdit, btnPrint);
//                btnActivityHistory.setVisible(true);
//                btnActivityHistory.setManaged(true);
            } else {
                CustomCommonUtil.setVisible(true, btnActCancel, btnEdit, btnPrint);
                CustomCommonUtil.setManaged(true, btnActCancel, btnEdit, btnPrint);
//                btnActivityHistory.setVisible(true);
//                btnActivityHistory.setManaged(true);
            }
        }

    }

    private boolean checkExistingActivityInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.validateExistingRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord((String) loJSON.get("sActvtyID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadActivityLocationTable();
                    loadActMembersTable();
                    loadActivityVehicleTable();
                    loadMasterFields();
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

    private Callback<DatePicker, DateCell> DateFrom = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            switch (pnEditMode) {
                case EditMode.ADDNEW:
                    LocalDate minDate = CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate()));
                    setDisable(fbEmpty || foItem.isBefore(minDate));
                    break;
                case EditMode.UPDATE:
                    setDisable(fbEmpty || foItem.isBefore(dateFrom03.getValue()));
                    break;
            }
        }
    };

    private Callback<DatePicker, DateCell> DateTo = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate minDate = dateFrom03.getValue();
            setDisable(fbEmpty || foItem.isBefore(minDate));
        }
    };

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox05.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Activity Source Type", "Please select `Activity Source Type` value.");
                return false;
            } else {
                switch (String.valueOf(oTrans.getModel().getModel().getEventTyp())) {
                    case "eve":
                        comboBox05.setValue("EVENT");
                        break;
                    case "sal":
                        comboBox05.setValue("SALES CALL");
                        break;
                    case "pro":
                        comboBox05.setValue("PROMO");
                        break;
                }
            }
        }
        return true;
    }

    private void loadActivityPrint() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityPrint.fxml"));

            ActivityPrintController loControl = new ActivityPrintController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setTransNo(oTrans.getModel().getModel().getActvtyID());
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

    private void loadActTypeAddSourceDialog() throws IOException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/ActivitySourceType.fxml"));
            ActivitySourceTypeController loControl = new ActivitySourceTypeController();
            loControl.setGRider(oApp);
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

    private void loadActLocationDialog(Integer fnRow, boolean isAdd) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityLocation.fxml"));
            ActivityLocationController loControl = new ActivityLocationController();
            loControl.setGRider(oApp);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
            loControl.setObject(oTrans);
            loControl.setProvID(String.valueOf(oTrans.getActLocation(0, "sProvIDxx").toString()));
            loControl.setProvName(String.valueOf(oTrans.getActLocation(0, "sProvName")));
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

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadActivityLocationTable();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void initActLocationTable() {
        locationAddres01.setCellValueFactory(new PropertyValueFactory<>("tblindexLocation01"));
        locationAddres02.setCellValueFactory(new PropertyValueFactory<>("tblindexLocation02"));
        locationAddres03.setCellValueFactory(new PropertyValueFactory<>("tblindexLocation06"));
        locationAddres04.setCellValueFactory(new PropertyValueFactory<>("tblindexLocation05"));
        tblLocationAddress.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblLocationAddress.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        locationData.clear();
        tblLocationAddress.setItems(locationData);
    }

    private void loadActivityLocationTable() {
        locationData.clear();
        String sAddress = "";
        for (lnCtr = 0; lnCtr <= oTrans.getActLocationList().size() - 1; lnCtr++) {
            sAddress = oTrans.getActLocation(lnCtr, "sAddressx").toString().toUpperCase() + " " + oTrans.getActLocation(lnCtr, "sBrgyName").toString().toUpperCase() + " " + oTrans.getActLocation(lnCtr, "sTownName").toString().toUpperCase() + ", " + oTrans.getActLocation(lnCtr, "sProvName").toString().toUpperCase();
            locationData.add(new ActivityLocation(
                    String.valueOf(lnCtr + 1), //ROW
                    sAddress,
                    oTrans.getActLocation(lnCtr, "sTownIDxx").toString().toUpperCase(),
                    oTrans.getActLocation(lnCtr, "sTownName").toString().toUpperCase(),
                    oTrans.getActLocation(lnCtr, "sCompnynx").toString().toUpperCase(),
                    oTrans.getActLocation(lnCtr, "sZippCode").toString(),
                    oTrans.getActLocation(lnCtr, "sProvIDxx").toString(),
                    oTrans.getActLocation(lnCtr, "sProvName").toString().toUpperCase(),
                    oTrans.getActLocation(lnCtr, "sBrgyIDxx").toString(),
                    oTrans.getActLocation(lnCtr, "sBrgyName").toString().toUpperCase()
            ));
        }
    }

    @FXML
    private void tblActLocation_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblLocationAddress.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblLocationAddress.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid location information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                loadActLocationDialog(pnRow, false);
                loadActivityLocationTable();
            }
            if (event.getClickCount() == 1) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            }
        }
    }

    private void loadActivityMemberDialog() throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityMember.fxml"));
            ActivityMemberController loControl = new ActivityMemberController();
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
            loadActMembersTable();

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadActMembersTable() {
        actMembersData.clear();
        int lnRow = 1;
        for (lnCtr = 0; lnCtr <= oTrans.getActMemberList().size() - 1; lnCtr++) {
            if (oTrans.getActMember(lnCtr, "cOriginal").equals("1")) {
                actMembersData.add(new ActivityMember(
                        String.valueOf(lnRow), //ROW
                        String.valueOf(lnCtr + 1),
                        String.valueOf(oTrans.getActMember(lnCtr, "sDeptName")).toUpperCase(),
                        String.valueOf(oTrans.getActMember(lnCtr, "sEmployID")).toUpperCase(),
                        String.valueOf(oTrans.getActMember(lnCtr, "sCompnyNm")).toUpperCase()
                ));
                lnRow++;
            }
        }
    }

    private void initActMembersTable() {
        tblMembersIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindexMem01"));
        tblMembersIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindexMem05"));
        tblMembersIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindexMem03"));
        tblViewActivityMembers.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewActivityMembers.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        actMembersData.clear();
        tblViewActivityMembers.setItems(actMembersData);
    }

    @FXML
    private void tblActMembers_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            int lnSel = tblViewActivityMembers.getSelectionModel().getSelectedIndex();
            pnRow = Integer.parseInt(actMembersData.get(lnSel).getTblindexMem02());
            if (lnSel < 0 || lnSel >= tblViewActivityMembers.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid members information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 1) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            }
        }
    }

    private void loadActivityVehicleDialog() throws IOException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityVehicle.fxml"));
            ActivityVehicleController loControl = new ActivityVehicleController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadActivityVehicleTable();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadActivityVehicleTable() {
        actVhclModelData.clear();
        if (oTrans.getActVehicleList() != null) {
            for (lnCtr = 0; lnCtr <= oTrans.getActVehicleList().size() - 1; lnCtr++) {
                actVhclModelData.add(new ActivityVehicle(
                        String.valueOf(lnCtr + 1), //ROW
                        String.valueOf(oTrans.getActVehicle(lnCtr, "sSerialID")).toUpperCase(),
                        String.valueOf(oTrans.getActVehicle(lnCtr, "sCSNoxxxx")).toUpperCase(),
                        String.valueOf(oTrans.getActVehicle(lnCtr, "sDescript")).toUpperCase()
                ));
            }
        }
    }

    private void initActivityVehicleTable() {
        tblVhclIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblVhclIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblVhclIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblViewVhclModels.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclModels.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        actVhclModelData.clear();
        tblViewVhclModels.setItems(actVhclModelData);
    }

    @FXML
    private void tblActVehicle_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {

            pnRow = tblViewVhclModels.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewVhclModels.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid vehicle information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 1) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            }
        }
    }

}
