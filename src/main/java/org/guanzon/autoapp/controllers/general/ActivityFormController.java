/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import org.guanzon.autoapp.controllers.parameters.ActivitySourceTypeEntryController;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.models.general.ModelActivityMember;
import org.guanzon.autoapp.models.general.ModelActivityTown;
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
//    private Activity oTransActivityActivity;
    private ObservableList<ModelActivityMember> actMembersData = FXCollections.observableArrayList();
    private ObservableList<ModelActivityTown> townCitydata = FXCollections.observableArrayList();
    private ObservableList<ModelActivityVehicle> actVhclModelData = FXCollections.observableArrayList();
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    CancelForm cancelform = new CancelForm(); //Object for closing form
    private final String pxeModuleName = "Activity Information"; //Form Title
    private int pnEditMode;//Modifying fields
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "SALES CALL", "PROMO");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnPrint, btnActivityHistory, btnActCancel, btnClose, btnAddSource, btnActivityMembersSearch, btnActivityMemRemove,
            btnVhclModelsSearch, btnVhlModelRemove, btnAddRowTasks, btnRemoveTasks, btnAddRowBudget, btnRemoveBudget, btnCityRemove, btnCitySearch;
    @FXML
    private Label lblApprovedBy;
    @FXML
    private Label lblApprovedDate;
    @FXML
    private Label lblCancelStatus;
    @FXML
    private Label lblActivityID;
    @FXML
    private Label lblActivityIDValue;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab MainTab;
    @FXML
    private DatePicker dateFrom02, dateTo03;
    @FXML
    private ComboBox<String> comboBox04;
    @FXML
    private TextField txtField01, txtField05, txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16;
    @FXML
    private TextArea textArea06, textArea07, textArea08, textArea09, textArea17, textArea18;
    @FXML
    private TableView<ModelActivityTown> tblViewCity;
    @FXML
    private TableColumn<ModelActivityTown, String> tblCityIndex01, tblCityIndex02, tblCityIndex03;
    @FXML
    private CheckBox selectAllCity;
    @FXML
    private TableView<ModelActivityMember> tblViewActivityMembers;
    @FXML
    private TableColumn<ModelActivityMember, String> tblMembersIndex01, tblMembersIndex02, tblMembersIndex03, tblMembersIndex04;
    @FXML
    private CheckBox selectAllCheckBoxEmployee;
    @FXML
    private TableView<ModelActivityVehicle> tblViewVhclModels;
    @FXML
    private TableColumn<ModelActivityVehicle, String> tblVhclIndex01, tblVhclIndex02, tblVhclIndex03;
    @FXML
    private CheckBox selectAllVchlMode;
    @FXML
    private Tab DetailsTab;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the Client_Master transaction
////        oTransActivityActivity = new Activity(oApp, false, oApp.getBranchCode());
////
////        initTownTable();
////        initMembersTable();
////        initActivityVehicleTable();
//        initTableProperties();
//        initTextFieldPattern();
//        dateFrom02.setOnAction(this::getDateFrom);
//        dateTo03.setOnAction(this::getDateTo);
//        comboBox04.setItems(cType);
//        initCapitalizationFields();
//        initTextKeyPressed();
//        initTextFieldFocus();
//        initCmboxFieldAction();
//        initButtons();
//        clearFields();
//        clearTables();
//        InputTextUtil.addTextLimiter(txtField14, 4);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initTableProperties() {
        tblViewVhclModels.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclModels.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewActivityMembers.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewActivityMembers.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewCity.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewCity.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void initTextFieldPattern() {
        Pattern numOnly = Pattern.compile("[0-9]*");
        Pattern decOnly = Pattern.compile("[0-9,]*");
        Pattern loStreetFormat = Pattern.compile("[A-Za-z0-9,. ]*");
        txtField12.setTextFormatter(new InputTextFormatterUtil(numOnly));  //nTrgtClnt
        txtField11.setTextFormatter(new InputTextFormatterUtil(decOnly));  //nRcvdBdgt
        textArea08.setTextFormatter(new InputTextFormatterUtil(loStreetFormat)); //street
    }

    /*Set Date Value to Master Class*/
    private void getDateFrom(ActionEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//            oTransActivityActivity.setMaster(6, SQLUtil.toDate(dateFrom02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }
    }

    private void getDateTo(ActionEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//            oTransActivityActivity.setMaster(7, SQLUtil.toDate(dateTo03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField05, txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea06, textArea07, textArea08, textArea09, textArea17, textArea18);
        loTxtArea.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField05, txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea06, textArea07, textArea08, textArea09, textArea17, textArea18);
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
                    case "txtField01":
//                        loJSON = oTransActivityActivity.searchSource(lsValue, true, false);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField01.setText(oTransActivityActivity.getModel().getModel().getSource());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField01.setText("");
//                            txtField01.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField10":
//                        loJSON = oTransActivityActivity.searchDepart(lsValue, false, false);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField10.setText(oTransActivityActivity.getModel().getModel().getDepartNm());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField10.setText("");
//                            txtField10.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField11":
//                        loJSON = oTransActivityActivity.searchEmplyNme(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField11.setText(oTransActivityActivity.getModel().getModel().getMakeDesc());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField11.setText("");
//                            txtField11.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField12":
//                        loJSON = oTransActivityActivity.searchBrnchNm(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField12.setText(oTransActivityActivity.getModel().getModel().getBrnchNme());
//                            txtField13.setText(oTransActivityActivity.getModel().getModel().getBrnchCde());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField12.setText("");
//                            txtField13.setText("");
//                            txtField12.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField16":
//                        loJSON = oTransActivityActivity.searchProvNme(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField16.setText(oTransActivityActivity.getModel().getModel().getProvNme());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField16.setText("");
//                            txtField16.requestFocus();
//                            return;
//                        }
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
        List<TextField> loTxtField = Arrays.asList(txtField14, txtField15, txtField16);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea06, textArea07, textArea08, textArea09, textArea17, textArea18);
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
                case 14:/*No of Target Client*/
//                    oTransActivityActivity.getModel().getModel().setTrgClients(Integer.valueOf(lsValue));
                    break;
                case 15:/*Total Event Budget*/
//                    oTransActivityActivity.getModel().getModel().setTtlBudget(Double.valueOf(lsValue));
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
                case 6:
//                    oTransActivityActivity.getModel().getModel().setActTitle(lsValue);
                    break;
                case 7:
//                    oTransActivityActivity.getModel().getModel().setActDsc(lsValue);
                    break;
                case 8:
//                    oTransActivityActivity.getModel().getModel().setActLgRrmks(lsValue);
                    break;
                case 9:
//                    oTransActivityActivity.getModel().getModel().setActRemarks(lsValue);
                    break;
                case 17:
//                    oTransActivityActivity.getModel().getModel().setStreet(lsValue);
                    break;
                case 18:
//                    oTransActivityActivity.getModel().getModel().setEstablishment(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initCmboxFieldAction() {
        comboBox04.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox04.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
//                        oTransActivityActivity.getModel().getModel().setSoldStat(String.valueOf((comboBox04.getSelectionModel().getSelectedIndex())));
                        initFields(pnEditMode);
                    }
                }
            }
        });
        txtField05.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
//                                oTransActivityActivity.getModel().getModel().setSourceID("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField10.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
//                                oTransActivityActivity.getModel().getModel().setDeparmtentInCharge("");
//                                oTransActivityActivity.getModel().getModel().setEmpID("");
                                txtField11.setText("");
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
//                                oTransActivityActivity.getModel().getModel().setEmpID("");
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
//                                oTransActivityActivity.getModel().getModel().setBranchID("");
                                txtField13.setText("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnPrint, btnActivityHistory, btnActCancel, btnClose, btnAddSource, btnActivityMembersSearch, btnActivityMemRemove,
                btnVhclModelsSearch, btnVhlModelRemove, btnAddRowTasks, btnRemoveTasks, btnAddRowBudget, btnRemoveBudget, btnCityRemove, btnCitySearch);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        try {
            JSONObject loJson = new JSONObject();
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnAdd":
//                clearFields();
//                clearTables();
//                oTransActivityActivity = new Activity(oApp, false, oApp.getBranchCode());
//                loJson = oTransActivityActivity.newRecord();
//                if ("success".equals((String) loJson.get("result"))) {
//                    loadActivityInformation();
//                    pnEditMode = oTransActivityActivity.getEditMode();
//                    initFields(pnEditMode);
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJson.get("message"));
//                }
                    break;
                case "btnEdit":
//                loJson = oTransActivityActivity.updateRecord();
//                pnEditMode = oTransActivityActivity.getEditMode();
//                if ("error".equals((String) loJson.get("result"))) {
//                    ShowMessageFX.Warning((String) loJson.get("message"), "Warning", null);
//                }
                    break;
                case "btnSave":
                    if (ShowMessageFX.YesNo(null, "Activity Information Saving....", "Are you sure, do you want to save?")) {
//                    if (setSelection()) {
//                        loJson = oTransActivity.saveRecord();
//                        if ("success".equals((String) loJson.get("result"))) {
//                            ShowMessageFX.Information(null, "Activity Information", (String) loJson.get("message"));
//                            loJson = oTransActivity.openRecord(oTransActivity.getModel().getModel().getSerialID());
//                            if ("success".equals((String) loJson.get("result"))) {
//                                loadActMembersTable();
//                                loadActivityInformation();
//                                initFields(pnEditMode);
////                                pnEditMode = oTransActivity.getEditMode();
//                            }
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJson.get("message"));
//                            return;
//                        }
//                    }
                    }
                    break;
                case "btnCancel":
//                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
//                    clearFields();
//                    clearTables();
//                    oTransActivityActivity = new Activity(oApp, false, oApp.getBranchCode());
//                    pnEditMode = EditMode.UNKNOWN;
//                }
                    break;
                case "btnBrowse":
                    if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                        if (ShowMessageFX.YesNo(null, "Search Activity Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        } else {
                            return;
                        }
                    }
                    if ("success".equals((String) loJson.get("result"))) {
                        loadActivityInformation();
                        loadActMembersTable();
//                    pnEditMode = oTransActivity.getEditMode();
                        initFields(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(null, "Search Activity Information Confirmation", (String) loJson.get("message"));
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
                case "btnCitySearch":
                    loadTownDialog();
                    break;
                case "btnActivityMembersSearch":
                    loadActivityMemberDialog();
                    break;
                case "btnVhclModelsSearch ":
                    loadActivityVehicleDialog();
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

    private void loadActivityInformation() {
//        if (oTransActivity.getModel().getModel().getActID != null) {
//            lblActivityID.setText("ACTIVITY ID: ");
//            lblActivityIDValue.setText(oTransActivity.getModel().getModel().getActID);
//
//        } else {
//            lblActivityIDValue.setText("");
//            lblActivityID.setText("");
//
//        }
//        if (oTransActivity.getModel().getModel().getActType() != null && !oTransActivity.getModel().getModel().getActType().trim().isEmpty()) {
//            comboBox04.getSelectionModel().select(Integer.parseInt(oTransActivity.getModel().getModel().getActType()));
//        }
//        if (oTransActivity.getModel().getModel().getActFrom() != null && !oTransActivity.getModel().getModel().getActFrom().toString().isEmpty()) {
//            dateFrom03.setValue(InputTextUtil.strToDate(oTransActivity.getMaster(11).toString()));
//        }
//
//        if (oTransActivity.getModel().getModel().getActTo() != null && !oTransActivity.getModel().getModel().getActTo().toString().isEmpty()) {
//            dateFrom04.setValue(InputTextUtil.strToDate(oTransActivity.getMaster(11).toString()));
//        }
//        if (oTransActivity.getModel().getModel().getApprvlStat()) {
//            lblCancelStatus.setText("Cancelled");
//        } else {
//            lblCancelStatus.setText("");
//        }

//        if (oTransActivity.getModel().getModel().getApprovBy) {
//            lblApprovedBy.setText("Approved");
//            if (oTransActivity.getModel().getModel().getDtApproved() != null && !oTransActivity.getModel().getModel().getDtApproved().toString().isEmpty()) {
//                lblApprovedDate.setText(InputTextUtil.strToDate(oTransActivity.getMaster(11).toString()));//dApproved
//            }
//        } else {
//            lblApprovedBy.setText("");
//            lblApprovedDate.setText("");//dApproved
//        }
//        txtField05.setText(oTransActivity.getModel().getModel().getSourceNm());
//        textArea06.setText(oTransActivity.getModel().getModel().getActTitle());
//        textArea07.setText(oTransActivity.getModel().getModel().getActDesc());
//        textArea08.setText(oTransActivity.getModel().getModel().getActLogRmrks());
//        textArea09.setText(oTransActivity.getModel().getModel().getActRmrks());
//        txtField10.setText(oTransActivity.getModel().getModel().getDprtMent());
//        txtField11.setText(oTransActivity.getModel().getModel().getEmpyCrg());
//        txtField12.setText(oTransActivity.getModel().getModel().getBrnchNme());
//        txtField13.setText(oTransActivity.getModel().getModel().getBrnchCode());
//        txtField14.setText(Integer.parseInt(String.valueOf(oTransActivity.getModel().getModel().getNoTrgClnt())));
//        txtField15.setText(oTransActivity.getModel().getModel().getTotalBudget());
//        txtField16.setText(oTransActivity.getModel().getModel().getProvince());
//        if (oTransActivity.getTownList().size() == 1) {
//            textArea17.setText(oTransActivity.getModel().getModel().getStreet());
//        }
//        textArea18.setText(oTransActivity.getModel().getModel().getActRmrks());
    }

    private void loadActivityPrint(String sTransno) throws SQLException {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ActivityPrint.fxml"));

            ActivityPrintController loControl = new ActivityPrintController();
//            loControl.setGRider(oApp);
//            loControl.setTransNox(oTransActivity.getModel().getModel().getActID());

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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/ActivityTypeAddSource.fxml"));

            ActivitySourceTypeEntryController loControl = new ActivitySourceTypeEntryController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTrans);
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

    private void loadTownDialog() {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/ActivityTownCityMainEntryDialog.fxml"));

            ActivityTownCityMainEntryDialogController loControl = new ActivityTownCityMainEntryDialogController();
            loControl.setGRider(oApp);
//            loControl.setObject(oTransActivity);
//            loControl.setProv(oTransActivity.getModel().getModel().getProvNm());
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
            loadTownTable();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadTownTable() {
//        townCitydata.clear();
//        for (int lnCtr = 1; lnCtr <= oTransActivity.getActTownCount(); lnCtr++) {
//            townCitydata.add(new ModelActivityTown(
//                    String.valueOf(lnCtr), //ROW
//                    oTransActivity.getActTown(lnCtr, "sTownIDxx").toString(),
//                    oTransActivity.getActTown(lnCtr, "sTownName").toString()));
//
//        }
//        tblViewCity.setItems(townCitydata);
        initTownTable();
    }

    private void initTownTable() {
        tblCityIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindexCity01"));
        tblCityIndex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewCity.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewCity.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCity.setSelected(true);
                } else {
                    selectAllCity.setSelected(false);
                }
            });
        });
        selectAllCity.setOnAction(event -> {
            boolean lbNewValue = selectAllCity.isSelected();
            if (!tblViewCity.getItems().isEmpty()) {
                tblViewCity.getItems().forEach(item -> item.getSelect().setSelected(lbNewValue));
            }
        }
        );
        tblCityIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindexCity03"));
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
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransActivity);
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

    //Activity Member
    private void loadActMembersTable() {
//        actMembersData.clear();
//        for (int lnCtr = 1; lnCtr <= oTransActivity.getActMemberCount(); lnCtr++) {
//            if (oTransActivity.getActMember(lnCtr, "cOriginal").equals("1")) {
//                actMembersData.add(new ModelActivityMember(
//                        String.valueOf(lnCtr), //ROW
//                        oTransActivity.getActMember(lnCtr, "sDeptName").toString(),
//                        "",
//                        oTransActivity.getActMember(lnCtr, "sCompnyNm").toString(), // Fifth column (Department)
//                        oTransActivity.getActMember(lnCtr, "sEmployID").toString()
//                ));
//            }
//        }
//        tblViewActivityMembers.setItems(actMembersData);
//        initActMembersTable();
    }

    private void initActMembersTable() {
        tblMembersIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindexMem01"));
        tblMembersIndex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActivityMembers.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewActivityMembers.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBoxEmployee.setSelected(true);
                } else {
                    selectAllCheckBoxEmployee.setSelected(false);
                }
            });
        });
        selectAllCheckBoxEmployee.setOnAction(event -> {
            boolean newValue = selectAllCheckBoxEmployee.isSelected();
            if (!tblViewActivityMembers.getItems().isEmpty()) {
                tblViewActivityMembers.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblMembersIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindexMem04"));
        tblMembersIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindexMem02"));
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
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransActivity);
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
//        /*Populate table*/
//        actVhclModelData.clear();
//        for (int lnCtr = 1; lnCtr <= oTransActivity.getActVehicleCount(); lnCtr++) {
//            actVhclModelData.add(new ModelActivityVehicle(
//                    String.valueOf(lnCtr), //ROW
//                    oTransActivity.getActVehicle(lnCtr, "sSerialID").toString(),
//                    oTransActivity.getActVehicle(lnCtr, "sDescript").toString(),
//                    oTransActivity.getActVehicle(lnCtr, "sCSNoxxxx").toString()
//            ));
//        }
//        tblViewVhclModels.setItems(actVhclModelData);
//        initActivityVehicleTable();
    }

    private void initActivityVehicleTable() {
        tblVhclIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindexVchl01"));  //Row
        tblVhclIndex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewVhclModels.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewVhclModels.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllVchlMode.setSelected(true);
                } else {
                    selectAllVchlMode.setSelected(false);
                }
            });
        });
        selectAllVchlMode.setOnAction(event -> {
            boolean newValue = selectAllVchlMode.isSelected();
            if (!tblViewVhclModels.getItems().isEmpty()) {
                tblViewVhclModels.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        }
        );
        tblVhclIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindexVchl03"));
    }

    private void clearTables() {
        townCitydata.clear();
        actMembersData.clear();
        actVhclModelData.clear();
    }

    private void clearFields() {
        lblActivityID.setText("");
        lblActivityIDValue.setText("");
        dateFrom02.setValue(InputTextUtil.strToDate(CommonUtils.xsDateMedium((Date) oApp.getServerDate())));
        dateTo03.setValue(InputTextUtil.strToDate(CommonUtils.xsDateMedium((Date) oApp.getServerDate())));
        comboBox04.setValue("");
        txtField05.setText("");
        textArea06.setText("");
        textArea07.setText("");
        textArea08.setText("");
        textArea09.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        txtField14.setText("0");
        txtField15.setText("0.00");
        txtField16.setText("");
        textArea17.setText("");
        textArea18.setText("");
        lblCancelStatus.setText("");
        lblApprovedDate.setText("");
        lblApprovedBy.setText("");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        lblActivityID.setDisable(!lbShow);
        lblActivityIDValue.setDisable(!lbShow);
        dateFrom02.setDisable(!lbShow);
        dateTo03.setDisable(!lbShow);
        comboBox04.setDisable(!lbShow);
        txtField05.setDisable(!(lbShow && !comboBox04.getValue().isEmpty()));
        textArea06.setDisable(!lbShow);
        textArea07.setDisable(!lbShow);
        textArea08.setDisable(!lbShow);
        textArea09.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!(lbShow && !txtField10.getText().isEmpty()));
        txtField12.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        txtField15.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        textArea17.setDisable(!lbShow);
        textArea18.setDisable(!lbShow);
        // Button
        btnCitySearch.setDisable(!(lbShow && !txtField16.getText().isEmpty()));
        btnCityRemove.setDisable(!(lbShow && !txtField16.getText().isEmpty()));
        if (tblViewCity != null && tblViewCity.getItems() != null) {
            boolean lbIsTblViewCityEmpty = tblViewCity.getItems().isEmpty() || tblViewCity.getItems().size() >= 2;
            if (lbIsTblViewCityEmpty) {
                textArea17.setDisable(!lbShow);
                textArea17.setText("");
            } else {
                textArea17.setDisable(false);
            }
            textArea17.setDisable(!(lbShow && !tblViewCity.getItems().isEmpty()) || (lbShow && tblViewCity.getItems().size() >= 2));
        }
        btnActCancel.setVisible(false);
        btnActCancel.setManaged(false);
        btnActivityMembersSearch.setDisable(!lbShow);
        btnActivityMemRemove.setDisable(!lbShow);
        btnVhclModelsSearch.setDisable(!lbShow);
        btnVhlModelRemove.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
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
        tblCityIndex02.setVisible(!lbShow);
        selectAllCity.setVisible(!lbShow);
        tblMembersIndex02.setVisible(!lbShow);
        selectAllCheckBoxEmployee.setVisible(!lbShow);
        tblVhclIndex02.setVisible(!lbShow);
        selectAllVchlMode.setVisible(!lbShow);

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
