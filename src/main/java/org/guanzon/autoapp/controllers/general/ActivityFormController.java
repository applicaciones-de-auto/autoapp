/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
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
import org.guanzon.autoapp.controllers.parameters.ActivitySourceTypeEntryController;
import org.guanzon.autoapp.models.general.ModelActivityMember;
import org.guanzon.autoapp.models.general.ModelActivityLocation;
import org.guanzon.autoapp.models.general.ModelActivityVehicle;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Activity oTransActivity;
    private ObservableList<ModelActivityLocation> locationData = FXCollections.observableArrayList();
    private ObservableList<ModelActivityMember> actMembersData = FXCollections.observableArrayList();
    private ObservableList<ModelActivityVehicle> actVhclModelData = FXCollections.observableArrayList();
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
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
    private Label lblApprovedBy, lblApprovedDate, lblCancelStatus, lblActivityID, lblActivityIDValue;
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
    private Tab tabLocationInfo, tabMembersInfo, tabVehicleInfo;
    @FXML
    private TableView<ModelActivityLocation> tblLocationAddress;
    @FXML
    private TableColumn<ModelActivityLocation, String> locationAddres01, locationAddres02, locationAddres03, locationAddres04;
    @FXML
    private TableView<ModelActivityMember> tblViewActivityMembers;
    @FXML
    private TableColumn<ModelActivityMember, String> tblMembersIndex01, tblMembersIndex02, tblMembersIndex03;
    @FXML
    private TableView<ModelActivityVehicle> tblViewVhclModels;
    @FXML
    private TableColumn<ModelActivityVehicle, String> tblVhclIndex01, tblVhclIndex02, tblVhclIndex03;
    @FXML
    private Tab tabActivityInfo;
    @FXML
    private Tab tabDetails;

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
        oTransActivity = new Activity(oApp, false, oApp.getBranchCode());

        initActLocationTable();
        initActMembersTable();
        initActivityVehicleTable();
        initTextFieldPattern();
        dateFrom03.setDayCellFactory(DateFrom);
        comboBox05.setItems(cType);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        clearTables();
        InputTextUtil.addTextLimiter(txtField15, 4);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initTextFieldPattern() {
        Pattern numOnly = Pattern.compile("[0-9]*");
        Pattern decOnly = Pattern.compile("[0-9,.]*");
        txtField15.setTextFormatter(new InputTextFormatterUtil(numOnly));  //nTrgtClnt
        txtField16.setTextFormatter(new InputTextFormatterUtil(decOnly));  //nRcvdBdgt
    }

    private Callback<DatePicker, DateCell> DateFrom = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate minDate = InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())).minusDays(7);
            setDisable(fbEmpty || foItem.isBefore(minDate));
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

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField06, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea08, textArea09, textArea10);
        loTxtArea.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField06, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea08, textArea09, textArea10);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));

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
                    case "txtField06":
                        loJSON = oTransActivity.searchEventType(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField06.setText(oTransActivity.getMasterModel().getModel().getActTypDs());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField06.setText("");
                            txtField06.requestFocus();
                            return;
                        }
                        break;
                    case "txtField11":
                        loJSON = oTransActivity.searchDepartment(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField11.setText(oTransActivity.getMasterModel().getModel().getDeptName());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField11.setText("");
                            txtField11.requestFocus();
                            return;
                        }
                        break;
                    case "txtField12":
                        loJSON = oTransActivity.searchEmployee(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField12.setText(oTransActivity.getMasterModel().getModel().getEmpInCharge());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField12.setText("");
                            txtField12.requestFocus();
                            return;
                        }
                        break;
                    case "txtField13":
                        loJSON = oTransActivity.searchBranch(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField13.setText(oTransActivity.getMasterModel().getModel().getBranchNm());
                            txtField14.setText(oTransActivity.getMasterModel().getModel().getLocation());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField13.setText("");
                            txtField13.requestFocus();
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

    private void initTextFieldFocus() {
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
                    oTransActivity.getMasterModel().getModel().setTrgtClnt(Integer.valueOf(lsValue));
                    break;
                case 16:/*Total Event Budget*/
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransActivity.getMasterModel().getModel().setRcvdBdgt(Double.valueOf(lsValue.replace(",", "")));
                    txtField16.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransActivity.getMasterModel().getModel().getRcvdBdgt()))));
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
                    oTransActivity.getMasterModel().getModel().setActTitle(lsValue);
                    break;
                case 8:
                    oTransActivity.getMasterModel().getModel().setActDesc(lsValue);
                    break;
                case 9:
                    oTransActivity.getMasterModel().getModel().setLogRemrk(lsValue);
                    break;
                case 10:
                    oTransActivity.getMasterModel().getModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initCmboxFieldAction() {
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
                oTransActivity.setMaster(7, SQLUtil.toDate(dateFrom03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));

            }
        });
        dateTo04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransActivity.setMaster(8, SQLUtil.toDate(dateTo04.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox05.setOnAction(e -> {
            int selectedComboBox02 = comboBox05.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox02 >= 0) {
                    switch (selectedComboBox02) {
                        case 0:
                            oTransActivity.getMasterModel().getModel().setEventTyp("eve");
                            break;
                        case 1:
                            oTransActivity.getMasterModel().getModel().setEventTyp("sal");
                            break;
                        case 2:
                            oTransActivity.getMasterModel().getModel().setEventTyp("pro");
                            break;
                        default:
                            break;
                    }
                }
                initFields(pnEditMode);
            }
        }
        );
        txtField06.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransActivity.getMasterModel().getModel().setActTypDs("");
                                initFields(pnEditMode);
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
                                oTransActivity.getMasterModel().getModel().setDeptID("");
                                oTransActivity.getMasterModel().getModel().setEmployID("");
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
                                oTransActivity.getMasterModel().getModel().setEmployID("");
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
                                oTransActivity.getMasterModel().getModel().setBranchNm("");
                                oTransActivity.getMasterModel().getModel().setLocation("");
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox05.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Activity Source Type", "Please select `Activity Source Type` value.");
                return false;
            } else {
                switch (String.valueOf(oTransActivity.getMasterModel().getModel().getEventTyp())) {
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

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnPrint, btnActivityHistory,
                btnActCancel, btnClose, btnAddSource, btnAddRowTasks, btnRemoveTasks, btnAddRowBudget, btnRemoveBudget, btnTabAdd, btnTabRem);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        try {
            JSONObject loJSON = new JSONObject();
            String lsButton = ((Button) event.getSource()).getId();
            iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
            switch (lsButton) {
                case "btnAdd":
                    clearFields();
                    clearTables();
                    oTransActivity = new Activity(oApp, false, oApp.getBranchCode());
                    loJSON = oTransActivity.newTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadActivityInformation();
                        pnEditMode = oTransActivity.getEditMode();
                        initFields(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    break;
                case "btnEdit":
                    loJSON = oTransActivity.updateTransaction();
                    pnEditMode = oTransActivity.getEditMode();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                    break;
                case "btnSave":
                    LocalDate loDateFrom = dateFrom03.getValue();
                    LocalDate loDateTo = dateTo04.getValue();
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to save?")) {
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
                        loJSON = oTransActivity.saveTransaction();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Activity Information", (String) loJSON.get("message"));
                            loJSON = oTransActivity.openTransaction(oTransActivity.getMasterModel().getModel().getActvtyID());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loadActivityLocationTable();
                                loadActMembersTable();
                                loadActivityVehicleTable();
                                loadActivityInformation();
                                initFields(pnEditMode);
                                pnEditMode = oTransActivity.getEditMode();
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
                        oTransActivity = new Activity(oApp, false, oApp.getBranchCode());
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
                    loJSON = oTransActivity.searchTransaction("", false);
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadActivityLocationTable();
                        loadActMembersTable();
                        loadActivityVehicleTable();
                        loadActivityInformation();
                        pnEditMode = oTransActivity.getEditMode();
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
                            oTransActivity.addActLocation();
                            loadActLocationDialog(oTransActivity.getActLocationList().size() - 1, true);
                            loadActivityLocationTable();
                            break;
                        case 1: //Members
                            if (oTransActivity.getMasterModel().getModel().getEmployID() == null) {
                                ShowMessageFX.Warning(null, "Warning", "Please enter value in Person in charge.");
                                return;
                            } else {
                                if (oTransActivity.getMasterModel().getModel().getEmployID().isEmpty()) {
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
                            oTransActivity.removeActLocation(pnRow);
                            pnRow = 0;
                            loadActivityLocationTable();
                            break;
                        case 1:
                            oTransActivity.removeActMember(pnRow);
                            pnRow = 0;
                            loadActMembersTable();
                            break;
                        case 2:
                            if (ShowMessageFX.YesNo(null, "Activity Vehicle Confirmation", "Are you sure you want to remove this  Activity Vehicle?")) {
                            } else {
                                return;
                            }
                            oTransActivity.removeActVehicle(pnRow);
                            pnRow = 0;
                            loadActivityVehicleTable();
                            break;
                    }
                    btnTabRem.setVisible(false);
                    break;
                case "btnPrint":
                    String lsTransNox = oTransActivity.getMasterModel().getModel().getActvtyID();
                     {
                        try {
                            loadActivityPrint(lsTransNox);
                        } catch (SQLException ex) {
                            Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                default:
                    ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                    break;
            }
            initFields(pnEditMode);
        } catch (IOException ex) {
            Logger.getLogger(ActivityFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadActivityPrint(String sTransNo) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityPrint.fxml"));

            ActivityPrintController loControl = new ActivityPrintController();
            loControl.setGRider(oApp);
            loControl.setTransNox(sTransNo);
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

    private void loadActTypeAddSourceDialog() throws IOException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/ActivitySourceTypeEntry.fxml"));

            ActivitySourceTypeEntryController loControl = new ActivitySourceTypeEntryController();
            loControl.setGRider(oApp);
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
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadActivityInformation() {
        if (oTransActivity.getMasterModel().getModel().getActvtyID() != null) {
            lblActivityID.setText("ACTIVITY ID: ");
            lblActivityIDValue.setText(oTransActivity.getMasterModel().getModel().getActvtyID());
        } else {
            lblActivityIDValue.setText("");
            lblActivityID.setText("");
        }
        txtField02.setText(oTransActivity.getMasterModel().getModel().getActNo());
        if (oTransActivity.getMasterModel().getModel().getDateFrom() != null && !oTransActivity.getMasterModel().getModel().getDateFrom().toString().isEmpty()) {
            dateFrom03.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransActivity.getMasterModel().getModel().getDateFrom())));
        }
        if (oTransActivity.getMasterModel().getModel().getDateThru() != null && !oTransActivity.getMasterModel().getModel().getDateThru().toString().isEmpty()) {
            dateTo04.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransActivity.getMasterModel().getModel().getDateThru())));
        }
        if (oTransActivity.getMasterModel().getModel().getEventTyp() != null && !oTransActivity.getMasterModel().getModel().getEventTyp().trim().isEmpty()) {
            switch (String.valueOf(oTransActivity.getMasterModel().getModel().getEventTyp())) {
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
        txtField06.setText(oTransActivity.getMasterModel().getModel().getActTypDs());
        textArea07.setText(oTransActivity.getMasterModel().getModel().getActTitle());
        textArea08.setText(oTransActivity.getMasterModel().getModel().getActDesc());
        textArea09.setText(oTransActivity.getMasterModel().getModel().getLogRemrk());
        textArea10.setText(oTransActivity.getMasterModel().getModel().getRemarks());
        txtField11.setText(oTransActivity.getMasterModel().getModel().getDeptName());
        txtField12.setText(oTransActivity.getMasterModel().getModel().getEmpInCharge());
        txtField13.setText(oTransActivity.getMasterModel().getModel().getBranchNm());
        txtField14.setText(oTransActivity.getMasterModel().getModel().getLocation());
        txtField15.setText(String.valueOf(oTransActivity.getMasterModel().getModel().getTrgtClnt()));
        txtField16.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransActivity.getMasterModel().getModel().getRcvdBdgt()))));

//        if (oTransActivity.getMasterModel().getModel().getApproved() != null) {
//            lblApprovedBy.setText(oTransActivity.getMasterModel().getModel().getApproved());
//            if (oTransActivity.getMasterModel().getModel().getApprovedDte() != null && !oTransActivity.getMasterModel().getModel().getApprovedDte().toString().isEmpty()) {
//                lblApprovedDate.setText(oTransActivity.getMaster(23).toString());//dApproved
//            }
//        } else {
//            lblApprovedBy.setText("");
//            lblApprovedDate.setText("");//dApproved
//        }
        if (oTransActivity.getMasterModel().getModel().getTranStat().equals("2")) {
            lblCancelStatus.setText("Cancelled");
        } else if (oTransActivity.getMasterModel().getModel().getTranStat().equals("1")) {
            lblCancelStatus.setText("Active");
        } else {
            lblCancelStatus.setText("");
        }
    }

    private void loadActLocationDialog(Integer fnRow, boolean isAdd) {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityLocationEntryDialog.fxml"));
            ActivityLocationEntryDialogController loControl = new ActivityLocationEntryDialogController();
            loControl.setGRider(oApp);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
            loControl.setObject(oTransActivity);
            loControl.setProvID(String.valueOf(oTransActivity.getActLocation(0, "sProvIDxx").toString()));
            loControl.setProvName(String.valueOf(oTransActivity.getActLocation(0, "sProvName")));
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
        for (lnCtr = 0; lnCtr <= oTransActivity.getActLocationList().size() - 1; lnCtr++) {
            sAddress = oTransActivity.getActLocation(lnCtr, "sAddressx").toString().toUpperCase() + " " + oTransActivity.getActLocation(lnCtr, "sBrgyName").toString().toUpperCase() + " " + oTransActivity.getActLocation(lnCtr, "sTownName").toString().toUpperCase() + ", " + oTransActivity.getActLocation(lnCtr, "sProvName").toString().toUpperCase();
            locationData.add(new ModelActivityLocation(
                    String.valueOf(lnCtr + 1), //ROW
                    sAddress,
                    oTransActivity.getActLocation(lnCtr, "sTownIDxx").toString().toUpperCase(),
                    oTransActivity.getActLocation(lnCtr, "sTownName").toString().toUpperCase(),
                    oTransActivity.getActLocation(lnCtr, "sCompnynx").toString().toUpperCase(),
                    oTransActivity.getActLocation(lnCtr, "sZippCode").toString(),
                    oTransActivity.getActLocation(lnCtr, "sProvIDxx").toString(),
                    oTransActivity.getActLocation(lnCtr, "sProvName").toString().toUpperCase(),
                    oTransActivity.getActLocation(lnCtr, "sBrgyIDxx").toString(),
                    oTransActivity.getActLocation(lnCtr, "sBrgyName").toString().toUpperCase()
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
            }
        }
    }
    //Activity Members Entry Dialog

    private void loadActivityMemberDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityMemberEntryDialog.fxml"));
            ActivityMemberEntryDialogController loControl = new ActivityMemberEntryDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransActivity);
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
        for (lnCtr = 0; lnCtr <= oTransActivity.getActMemberList().size() - 1; lnCtr++) {
            if (oTransActivity.getActMember(lnCtr, "cOriginal").equals("1")) {
                actMembersData.add(new ModelActivityMember(
                        String.valueOf(lnRow), //ROW
                        String.valueOf(lnCtr + 1),
                        String.valueOf(oTransActivity.getActMember(lnCtr, "sDeptName")).toUpperCase(),
                        String.valueOf(oTransActivity.getActMember(lnCtr, "sEmployID")).toUpperCase(),
                        String.valueOf(oTransActivity.getActMember(lnCtr, "sCompnyNm")).toUpperCase()
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
            }
        }
    }

    //Activity Vehicle Entry Dialog
    private void loadActivityVehicleDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityVehicleDialog.fxml"));
            ActivityVehicleDialogController loControl = new ActivityVehicleDialogController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransActivity);
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
            loadActivityVehicleTable();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadActivityVehicleTable() {
        actVhclModelData.clear();
        if (oTransActivity.getActVehicleList() != null) {
            for (lnCtr = 0; lnCtr <= oTransActivity.getActVehicleList().size() - 1; lnCtr++) {
                actVhclModelData.add(new ModelActivityVehicle(
                        String.valueOf(lnCtr + 1), //ROW
                        String.valueOf(oTransActivity.getActVehicle(lnCtr, "sSerialID")).toUpperCase(),
                        String.valueOf(oTransActivity.getActVehicle(lnCtr, "sCSNoxxxx")).toUpperCase(),
                        String.valueOf(oTransActivity.getActVehicle(lnCtr, "sDescript")).toString().toUpperCase()
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
            }
        }
    }

    private void clearTables() {
        locationData.clear();
        actMembersData.clear();
        actVhclModelData.clear();
    }

    private void clearFields() {
        lblActivityID.setText("");
        lblActivityIDValue.setText("");
        dateFrom03.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())));
        dateTo04.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())));
        comboBox05.setValue("");
        txtField06.setText("");
        textArea07.setText("");
        textArea08.setText("");
        textArea09.setText("");
        textArea10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        txtField14.setText("");
        txtField15.setText("0");
        txtField16.setText("0.00");
        lblCancelStatus.setText("");
        lblApprovedDate.setText("");
        lblApprovedBy.setText("");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        dateFrom03.setDisable(!lbShow);
        dateTo04.setDisable(!lbShow);
        comboBox05.setDisable(!lbShow);
        txtField06.setDisable(!(lbShow && !comboBox05.getValue().isEmpty()));
        textArea07.setDisable(!lbShow);
        textArea08.setDisable(!lbShow);
        textArea09.setDisable(!lbShow);
        textArea10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!(lbShow && !txtField11.getText().isEmpty()));
        txtField13.setDisable(!lbShow);
        txtField15.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        btnActCancel.setVisible(false);
        btnActCancel.setManaged(false);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnTabAdd.setVisible(lbShow);
        btnTabAdd.setDisable(!lbShow);
        btnTabRem.setVisible(false);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnActivityHistory.setVisible(false);
        btnActivityHistory.setManaged(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);

        if (fnValue == EditMode.READY) {
            if (lblCancelStatus.getText().equals("Cancelled")) {
                btnActCancel.setVisible(false);
                btnActCancel.setManaged(false);
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
                btnActivityHistory.setVisible(true);
                btnActivityHistory.setManaged(true);
            } else {
                btnActCancel.setVisible(true);
                btnActCancel.setManaged(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                btnActivityHistory.setVisible(true);
                btnActivityHistory.setManaged(true);
            }
        }

    }
}
