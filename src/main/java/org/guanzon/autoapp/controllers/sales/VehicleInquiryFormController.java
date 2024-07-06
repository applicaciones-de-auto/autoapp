///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
// */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
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
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.FXMLDocumentController;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryBankApplications;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryFollowUp;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryList;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryPriorityUnit;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryPromoOffered;
import org.guanzon.autoapp.models.sales.ModelVehicleInquiryRequirements;
import org.guanzon.autoapp.models.sales.ModelVehicleInquirySalesAdvances;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
//
///**
// * FXML Controller class
// *
// * @author Arsiela
// *
// */

public class VehicleInquiryFormController implements Initializable, ScreenInterface {
//
//    private InquiryMaster oTrans;
//    private InquiryProcess oTransProcess;
//    private InquiryBankApplication oTransBankApp;
//    private InquiryFollowUp oTransFollowUp;

    private GRider oApp;
    private UnloadForm poUnload = new UnloadForm(); //Object for closing form
    private FXMLDocumentController fdController = new FXMLDocumentController();
//    CancelForm cancelform = new CancelForm(); //Object for closing form
    private double xOffset, yOffset = 0;
    private final String pxeModuleName = "Vehicle Inquiry"; //Form Title
    private int pnEditMode = EditMode.UNKNOWN;
    ;//Modifying fields
    private int pnRow, pnOldRow, pnSelectedTblRowIndex = -1;
    private int pnSelectedIndex = 0;
    private int pnCtr = 0;
    private int lnRow = 0;
    private int pnPageCounter;
    private String psOldTransNo, psSourceNox, psClientID, psInqStat, psValue = "";
    private int pnIinqPayMode = 0;

//    /* ------------------DATA TABLES----------------------- */
//    private ObservableList<ModelVehicleInquiryPriorityUnit> priorityunitdata = FXCollections.observableArrayList();
//    private ObservableList<ModelVehicleInquiryPromoOffered> promosoffereddata = FXCollections.observableArrayList();
//    private ObservableList<ModelVehicleInquiryRequirements> inqrequirementsdata = FXCollections.observableArrayList();
//    private ObservableList<ModelVehicleInquirySalesAdvances> inqvsadata = FXCollections.observableArrayList();
//    private ObservableList<ModelVehicleInquiryBankApplications> bankappdata = FXCollections.observableArrayList();
//    private ObservableList<ModelVehicleInquiryFollowUp> followupdata = FXCollections.observableArrayList();
//    private FilteredList<ModelVehicleInquiryList> filteredData;
//
//    /* ------------------COMBO BOX ITEMS/VALUE----------------------- */
//    private ObservableList<String> cInquiryType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO"); //Inquiry Type values
//    private ObservableList<String> cInqStatus = FXCollections.observableArrayList("FOR FOLLOW-UP", "ON PROCESS", "LOST SALE", "WITH VSP", "SOLD", "RETIRED", "CANCELLED"); //Inquiry Type Values
//    private ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
//    private ObservableList<String> cCustomerType = FXCollections.observableArrayList("BUSINESS", "EMPLOYED", "OFW", "SEAMAN", "ANY"); // Customer Type Values
//    /* ------------------ANCHOR PANE----------------------- */
//    @FXML
//    private AnchorPane AnchorMain;
//    /* ------------------TABPANE----------------------------*/
//    @FXML
//    private TabPane tabPaneMain;
//    /* ------------------TABS------------------------------ */
//    @FXML
//    private Tab tabCustomerInquiry, tabInquiryProcess, tabBankHistory, tabFollowingHistory; // Customer Inquiry Tab
//    /* ------------------TABLE VIEW------------------------ */
//    @FXML
//    private TableView tblInquiry, tblPriorityUnit, tblPromosOffered, tblRequirementsInfo, tblAdvanceSlip,
//            tblBankApplication, tblFollowHistory;
//
//    /* -------------------TABLE COLUMN--------------------- */
//    @FXML
//    private TableColumn listIndex01, listIndex02, listIndex03, listIndex04, listIndex05, listIndex06;
//    @FXML
//    private TableColumn trgvIndex01, trgvIndex02;
//    @FXML
//    private TableColumn prmoIndex01, prmoIndex02, prmoIndex03, prmoIndex04;
//    @FXML
//    private TableColumn rqrmIndex01, rqrmIndex02, rqrmIndex03, rqrmIndex04;
//    @FXML
//    private TableColumn vsasCheck01, vsasIndex01, vsasIndex02, vsasIndex03, vsasIndex04, vsasIndex05, vsasIndex06, vsasIndex07;
//    @FXML
//    private TableColumn bankCheck01, bankIndex01, bankIndex02, bankIndex03, bankIndex04, bankIndex05, bankIndex07;
//    @FXML
//    private TableColumn flwpIndex01, flwpIndex02, flwpIndex03, flwpIndex04, flwpIndex05, flwpIndex06;
//    /* -------------------TEXTFIELDS----------------------- */
    @FXML
    private TextField textSeek01;
//    @FXML
//    private TextField txtField02, txtField03, txtField04, txtField07, txtField29, txtField30, txtField31, txtField32,
//            txtField09, txtField18, txtField17, txtField13, txtField15, txtField14, txtField41; //Branch Name
//    @FXML
//    private TextField txtRsvcs06, txtRsvpn06, txtRsvmd06;
//    @FXML
//    private TextField txtField21, txtPymtc01; //Approved By
//    /* ---------------------TEXTAREA----------------------- */
//    @FXML
//    private TextArea textArea33, textArea08;
//    /* ---------------------BUTTONS------------------------ */
//    @FXML
//    private Button btnClose, btnAdd, btnEdit, btnSave, btnClear, btnConvertSales, btnPrintRefund, btnLostSale, btnCancel,
//            btnTargetVhclAdd, btnTargetVhclRemove, btnTargetVehicleUp, btnTargetVehicleDown, btnPromosAdd, btnPromosRemove;
//    @FXML
//    private Button btnFollowUp; // FollowUp
//    @FXML
//    private Button btnASadd, btnASremove, btnASprint, btnAScancel, btnPymtcon, btnProcess, btnModify, btnApply;
//    @FXML
//    private Button btnBankAppView, btnBankAppNew, btnBankAppUpdate, btnBankAppCancel;
//
//    /* ---------------------COMBOBOX---------------------- */
//    @FXML
//    private ComboBox cmbType012; //Inquiry Type
//    @FXML
//    private ComboBox comboBox24; //Inquiry status
//    @FXML
//    private ComboBox cmbInqpr01, cmbInqpr02;
//    /* --------------------TOGGLE GROUPS------------------ */
//    @FXML
//    private ToggleGroup targetVehicle;
//    @FXML
//    private ToggleGroup hotCategory;
//
//    /* --------------------RADIO BUTTONS------------------ */
//    @FXML
//    private RadioButton rdbtnHtA11, rdbtnHtB11, rdbtnHtC11, rdbtnNew05, rdbtnPro05;
//    /* --------------------DATEPICKER--------------------- */
//    @FXML
//    private DatePicker dateSeek01, dateSeek02;
//    @FXML
//    private DatePicker txtField10; //Target Release Date
//    /* --------------------LABEL-------------------------- */
//    @FXML
//    private Label lblInqStatus, lblContactPrsn;
//    private final List<TextField> poTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField07, txtField29, txtField30, txtField31, txtField32,
//            txtField09, txtField18, txtField17, txtField13, txtField15, txtField14, txtField41, txtRsvcs06, txtRsvpn06, txtRsvmd06, txtField21, txtPymtc01);
//

    private Stage getStage() {
        return (Stage) textSeek01.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
//
//    /**
//     * Initializes the controller class.
//     *
//     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

////        oTrans = new InquiryMaster(oApp, oApp.getBranchCode(), false); //Initialize ClientMaster
////        oTrans.setWithUI(true);
//        initTargetVehicle();
//        initPromosOffered();
//
//        txtField41.setVisible(false);
//        lblContactPrsn.setVisible(false);
//        /*CUSTOMER INQUIRY*/
//        comboBox24.setItems(cInqStatus); //Inquiry Status
//        cmbType012.setItems(cInquiryType); //Inquiry Type
//        //("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO"); //Inquiry Type values
//        txtField07.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                if (newValue.isEmpty()) {
//                    txtField29.clear();
//                    textArea33.clear();
//                    txtField30.clear();
//                    txtField31.clear();
//                    txtField32.clear();
////                    oTrans.setMaster(7, "");
////                    oTrans.setMaster(29, "");
////                    oTrans.setMaster(33, "");
////                    oTrans.setMaster(30, "");
////                    oTrans.setMaster(31, "");
////                    oTrans.setMaster(32, "");
//                    txtField41.setVisible(false);
//                    lblContactPrsn.setVisible(false);
////                    oTrans.setMaster(40, "");
////                    oTrans.setMaster(41, "");
//                }
//            }
//        }
//        );
//
//        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (pnEditMode == EditMode.ADDNEW) {
//                if (newValue != null && !newValue.isEmpty()) {
//                } else {
////                    oTrans.setMaster("sEmployID", "");
////                    oTrans.setMaster("sSalesExe", "");
//                }
//            }
//        }
//        );
//
//        cmbType012.setOnAction(event -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//            } else {
//                return;
//            }
//            txtField13.setDisable(true); //online store
//            txtField09.setDisable(true); //ref agent
//            txtField15.setDisable(true); //event
//            switch (cmbType012.getSelectionModel().getSelectedIndex()) {
//                case 0:
//                case 2:
//                case 6:
//                case 7:
//                case 8:
//                case 9:
//                case 10:
//                    txtField13.clear();
//                    txtField09.clear();
//                    txtField15.clear();
////                    oTrans.setMaster(13, "");
////                    oTrans.setMaster(36, "");
////                    oTrans.setMaster(9, "");
////                    oTrans.setMaster(35, "");
////                    oTrans.setMaster(15, "");
////                    oTrans.setMaster(37, "");
//                    break;
//                case 1: //WEB INQUIRY
//                    txtField13.setDisable(false);
//                    txtField15.clear();
////                    txtField09.clear();
////                    oTrans.setMaster(15, "");
////                    oTrans.setMaster(37, "");
////                    oTrans.setMaster(9, "");
////                    oTrans.setMaster(35, "");
//                    break;
//                case 3: //REFERRAL
//                    txtField09.setDisable(false);
//                    txtField15.clear();
//                    txtField13.clear();
////
////                    oTrans.setMaster(15, "");
////                    oTrans.setMaster(37, "");
////                    oTrans.setMaster(13, "");
////                    oTrans.setMaster(36, "");
//                    break;
//                case 4: //SALES CALL
//                case 5: //EVENT
//                    txtField15.setDisable(false);
//                    txtField15.clear();
//                    txtField09.clear();
//                    txtField13.clear();
//
////                    oTrans.setMaster(15, "");
////                    oTrans.setMaster(37, "");
////                    oTrans.setMaster(9, "");
////                    oTrans.setMaster(35, "");
////                    oTrans.setMaster(13, "");
////                    oTrans.setMaster(36, "");
//                    break;
//            }
////            oTrans.setMaster(12, String.valueOf(cmbType012.getSelectionModel().getSelectedIndex()));
//        }
//        );
//
//        //txtField04.focusedProperty().addListener(txtField_Focus);  // Sales Executive
//        //txtField07.focusedProperty().addListener(txtField_Focus);  //Customer ID
//        //txtField29.focusedProperty().addListener(txtField_Focus);  //Company ID
//        //txtField09.focusedProperty().addListener(txtField_Focus);  //Agent ID
//        textArea08.focusedProperty().addListener(txtArea_Focus);  //Remarks
//        //txtField15.focusedProperty().addListener(txtField_Focus);  //Activity ID
//        //txtField14.focusedProperty().addListener(txtField_Focus);  //Test Model
//        txtField10.setOnAction(
//                this::getDate);
//        txtField10.setDayCellFactory(callB);
//
//        /*INQUIRY PROCESS*/
////        oTransProcess = new InquiryProcess(oApp, oApp.getBranchCode(), false);
////
////        oTransProcess.setCallback(oListener);
////        oTransProcess.setWithUI(true);
////        initInquiryAdvances();
//        tblRequirementsInfo.widthProperty()
//                .addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//                    TableHeaderRow header = (TableHeaderRow) tblRequirementsInfo.lookup("TableHeaderRow");
//                    header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                        header.setReordering(false);
//                    });
//                }
//                );
//
////        /*INQUIRY BANK APPLICATION*/
////        oTransBankApp = new InquiryBankApplication(oApp, oApp.getBranchCode(), true);
////        oTransBankApp.setCallback(oListener);
////        oTransBankApp.setWithUI(true);
//        initBankApplication();
//        cmbInqpr01.setItems(cModeOfPayment); //Mode of Payment
//        cmbInqpr02.setItems(cCustomerType); //Customer Type
//        //Mode of Payment
//        cmbInqpr01.setOnAction(event -> {
//            cmbInqpr02.setValue(null);
//            cmbInqpr02.setItems(cCustomerType);
//            switch (cmbInqpr01.getSelectionModel().getSelectedIndex()) {
//                case 0:
//                case 1:
//                case 3:
//                    cmbInqpr02.getSelectionModel().select(4); //Set to Any
//                    cmbInqpr02.setDisable(true);
//                    break;
//                case 2:
//                case 4:
//                    ObservableList<String> cCustType = FXCollections.observableArrayList("BUSINESS", "EMPLOYED", "OFW", "SEAMAN"); // Customer Type Values
//                    cmbInqpr02.setItems(cCustType);
//                    cmbInqpr02.setDisable(false);
//                    break;
//            }
//        }
//        );
////        //Customer Type
////        cmbInqpr02.setOnAction(event -> {
////            loadInquiryRequirements();
////            if (oTransProcess.getEditMode() == EditMode.ADDNEW || oTransProcess.getEditMode() == EditMode.UPDATE) {
////                if (oTransProcess.getInqReqCount() > 0) {
////                    oTransProcess.setInqReq(1, "cPayModex", cmbInqpr01.getSelectionModel().getSelectedIndex());
////                    oTransProcess.setInqReq(1, "cCustGrpx", cmbInqpr02.getSelectionModel().getSelectedIndex());
////                }
////            }
////        }
////        );
//
////        tblAdvanceSlip.setRowFactory(tv -> {
////            TableRow<InquiryTableVehicleSalesAdvances> row = new TableRow<>();
////            row.setOnMouseClicked(event -> {
////                if (event.getClickCount() == 2 && !row.isEmpty()) {
////                    int nStat = comboBox24.getSelectionModel().getSelectedIndex();
////                    loadVehicleSalesAdvancesWindow(row.getIndex() + 1, false, nStat, oTransProcess.getEditMode());
////                }
////            });
////            return row;
////        }
////        );
////        /*INQUIRY FOLLOW-UP*/
////        oTransFollowUp = new InquiryFollowUp(oApp, oApp.getBranchCode(), true);
////        oTransFollowUp.setWithUI(true);
//        initFollowUp();
//
////        tblFollowHistory.setRowFactory(tv
////                -> {
////            TableRow<InquiryTableVehicleSalesAdvances> row = new TableRow<>();
////            row.setOnMouseClicked(event -> {
////                if (event.getClickCount() == 2 && !row.isEmpty()) {
////                    loadFollowUpWindow(oTransFollowUp.getDetail(row.getIndex() + 1, 1).toString(), true);
////                }
////            });
////            return row;
////        }
////        );
////        btnFollowUp.setOnAction(this::cmdButton_Click);
//
//        /*Clear Fields*/
//        clearFields();
//        pnEditMode = EditMode.UNKNOWN;
//        initButton(pnEditMode);
//        tabPaneMain.getSelectionModel()
//                .selectedItemProperty().addListener(new ChangeListener<Tab>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab,
//                            Tab loNewTab
//                    ) {
//                        pnSelectedIndex = tabPaneMain.getSelectionModel().getSelectedIndex();
////                        initBtnProcess(oTransProcess.getEditMode());
//                        initButton(pnEditMode);
//                    }
//                }
//                );
//
////        Platform.runLater(() -> {
////            if (oTrans.loadState()) {
////                pnEditMode = oTrans.getEditMode();
////                loadCustomerInquiry();
////                loadTargetVehicle();
////                loadPromosOfferred();
////                if (pnEditMode == EditMode.UPDATE || pnEditMode == EditMode.READY) {
////                    loadInquiryProcess((String) oTrans.getMaster(1));
////                }
////
////                initBtnProcess(pnEditMode);
////                initButton(pnEditMode);
////            } else {
////                if (oTrans.getMessage().isEmpty()) {
////                } else {
////                    ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
////                }
////            }
////        }
////        );
//        initCapitalizationFields();
//
//        initTextKeyPressed();
//
//        initTextFieldFocus();
//
//        initCmboxFieldAction();
//
//        initButtons();
//    }
//
//    private void initButtons() {
//        List<Button> buttons = Arrays.asList(btnClose, btnAdd, btnEdit, btnSave, btnClear, btnConvertSales, btnPrintRefund, btnLostSale, btnCancel,
//                btnTargetVhclAdd, btnTargetVhclRemove, btnTargetVehicleUp, btnTargetVehicleDown, btnPromosAdd, btnPromosRemove);
//
//        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
//    }
//    //Method/Function for general buttons
//
//    private void handleButtonAction(ActionEvent event) {
//        try {
//            String lsButton = ((Button) event.getSource()).getId();
//            switch (lsButton) {
//                /*CUSTOMER INQUIRY: PRIORITY UNIT*/
//                case "btnTargetVhclAdd":
//                    lnRow = priorityunitdata.size();
//                    if (lnRow == 0) {
//                        lnRow = 1;
//                    } else {
//                        lnRow++;
//                    }
////                    oTrans.addVhclPrty();
////                    if (oTrans.searchVhclPrty(lnRow, "", false)) {
////                    } else {
////                        oTrans.removeTargetVehicle(lnRow);
////                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
////                    }
//                    loadTargetVehicle();
//                    break;
//
//                case "btnTargetVhclRemove":
//                    pnSelectedTblRowIndex = tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1;
////                    if (pnSelectedTblRowIndex >= 1) {
////                        if ((oTrans.getVhclPrty(pnSelectedTblRowIndex, 1).toString() != null) && (!oTrans.getVhclPrty(pnSelectedTblRowIndex, 1).toString().trim().equals(""))) {
////                        } else {
////                            oTrans.removeTargetVehicle(pnSelectedTblRowIndex);
////                            loadTargetVehicle();
////                        }
////                    }
//                    pnSelectedTblRowIndex = 0;
//                    break;
//                case "btnTargetVehicleDown":
//                    if ((pnSelectedTblRowIndex < tblPriorityUnit.getItems().size() - 1) && pnSelectedTblRowIndex != tblPriorityUnit.getItems().size()) {
//                        Collections.swap(tblPriorityUnit.getItems(), pnSelectedTblRowIndex, pnSelectedTblRowIndex + 1);
//                        tblPriorityUnit.getSelectionModel().select(pnSelectedTblRowIndex + 1);
//                        pnCtr = 1;
////                        for (InquiryTablePriorityUnit unit : priorityunitdata) {
////                            oTrans.setVhclPrty(pnCtr, "nPriority", unit.getTblindex01());
////                            oTrans.setVhclPrty(pnCtr, "sDescript", unit.getTblindex02());
////                            pnCtr++;
////                        }
//                        loadTargetVehicle();
//                        pnSelectedTblRowIndex++;
//                    }
//                    break;
//                case "btnTargetVehicleUp":
//                    if (pnSelectedTblRowIndex > 0) {
//                        Collections.swap(tblPriorityUnit.getItems(), pnSelectedTblRowIndex, pnSelectedTblRowIndex - 1);
//                        tblPriorityUnit.getSelectionModel().select(pnSelectedTblRowIndex - 1);
//                        pnCtr = 1;
////                        for (InquiryTablePriorityUnit unit : priorityunitdata) {
////                            oTrans.setVhclPrty(pnCtr, "nPriority", unit.getTblindex01());
////                            oTrans.setVhclPrty(pnCtr, "sDescript", unit.getTblindex02());
////                            pnCtr++;
////                        }
//                        loadTargetVehicle();
//                        pnSelectedTblRowIndex--;
//                    }
//                    break;
//                /*CUSTOMER INDQUIRY: PROMO OFFERED*/
//                case "btnPromosAdd":
//                    lnRow = promosoffereddata.size();
//                    if (lnRow == 0) {
//                        lnRow = 1;
//                    } else {
//                        lnRow++;
//                    }
////                    oTrans.addPromo();
////                    if (oTrans.searchActivity(lnRow, "pro", false)) {
////                    } else {
////                        oTrans.removeInqPromo(lnRow);
////                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
////                    }
//                    loadPromosOfferred();
//                    break;
//                case "btnPromosRemove":
//                    pnSelectedTblRowIndex = tblPromosOffered.getSelectionModel().getSelectedIndex() + 1;
////                    if (pnSelectedTblRowIndex >= 1) {
////                        if ((oTrans.getInqPromo(pnSelectedTblRowIndex, 1).toString() != null) && (!oTrans.getInqPromo(pnSelectedTblRowIndex, 1).toString().trim().equals(""))) {
////                        } else {
////                            oTrans.removeInqPromo(pnSelectedTblRowIndex);
////                            loadPromosOfferred();
////                        }
////                    }
//                    pnSelectedTblRowIndex = 0;
//                    break;
//                /*CUSTOMER INQUIRY: GENERAL BUTTON*/
//                case "btnAdd":
//                    //pnEditMode  = EditMode.ADDNEW;
//                    tabPaneMain.getSelectionModel().select(tabCustomerInquiry);
//                    /*Clear Fields*/
////                    if (oTrans.NewRecord()) {
////                        clearFields();
////                        clearClassFields();
////                        oTrans.searchBranch(oApp.getBranchCode(), true);
////                        loadCustomerInquiry();
////                        loadTargetVehicle();
////                        loadPromosOfferred();
////                        textSeek01.clear(); // Client Search
////                        psSourceNox = "";
////                        pnEditMode = oTrans.getEditMode();
////                    } else {
////                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
////                    }
//                    break;
//                case "btnEdit":
//                    tabPaneMain.getSelectionModel().select(tabCustomerInquiry);
////                    if (oTrans.UpdateRecord()) {
////                        pnEditMode = oTrans.getEditMode();
////                    } else {
////                        ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
////                    }
//                    break;
//                case "btnSave":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
//                        if (setSelection()) {
////                            if (oTrans.SaveRecord()) {
////                                ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
////                                loadInquiryListTable();
////                                getSelectedItem((String) oTrans.getMaster(1));
////                                pnEditMode = oTrans.getEditMode();
////                                initBtnProcess(pnEditMode);
////                            } else {
////                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "Error while saving " + pxeModuleName);
////                            }
//                        }
//                        break;
//                    } else {
//                        return;
//                    }
//                case "btnClear":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to clear fields?") == true) {
//                    } else {
//                        return;
//                    }
//                    clearClassFields();
//                    loadCustomerInquiry();
//                    loadTargetVehicle();
//                    loadPromosOfferred();
//                    break;
//                case "btnConvertSales":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to convert this inquiry for a new vsp record?") == true) {
//                    } else {
//                        return;
//                    }
//                    loadVSPWindow();
//                    break;
//                case "btnPrintRefund":
////                    loadPrintRefund((String) oTrans.getMaster(1));
//                    break;
//                case "btnLostSale":
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to tag this inquiry as LOST SALE?") == true) {
//                        loadLostSaleWindow();
//
////                        getSelectedItem((String) oTrans.getMaster(1));
////                        pnEditMode = oTrans.getEditMode();
////                        initBtnProcess(pnEditMode);
//                        break;
//                    } else {
//                        return;
//                    }
//                /*INQUIRY PROCESS: RESERVATION*/
//                case "btnASadd":
//                    //Open window for inquiry reservation
//                    lnRow = inqvsadata.size();
//                    if (lnRow == 0) {
//                        lnRow = 1;
//                    } else {
//                        lnRow++;
//                    }
////                    loadVehicleSalesAdvancesWindow(lnRow, true, comboBox24.getSelectionModel().getSelectedIndex(), oTransProcess.getEditMode());
//                    break;
//                case "btnASremove":
//                case "btnAScancel":
//                case "btnASprint":
//                    lnRow = 1;
//                    pnCtr = 1;
//                    ObservableList<InquiryTableVehicleSalesAdvances> selectedItems = FXCollections.observableArrayList();
//                    for (InquiryTableVehicleSalesAdvances item : inqvsadata) {
//                        if (item.isTblcheck01()) {
//
//                            if ("btnASremove".equals(lsButton) && pnCtr > 1) {
//                                ShowMessageFX.Information(getStage(), "Please select atleast 1 slip to be removed.", pxeModuleName, null);
//                                return;
//                            }
//
//                            if ("btnAScancel".equals(lsButton) && pnCtr > 1) {
//                                ShowMessageFX.Information(getStage(), "Please select atleast 1 slip to be cancelled.", pxeModuleName, null);
//                                return;
//                            }
//                            switch (oTransProcess.getInqRsv(lnRow, 13).toString()) {
//                                case "0":
//                                    if ("btnASprint".equals(lsButton)) {
//                                        ShowMessageFX.Information(getStage(), "Slip No. " + oTransProcess.getInqRsv(lnRow, 3).toString() + " is not yet approved. Printing Aborted.", pxeModuleName, null);
//                                        return;
//                                    } else {
//                                        selectedItems.add(item);
//                                    }
//                                    break;
//                                case "1":
//                                    switch (lsButton) {
//                                        case "btnAScancel":
//                                            ShowMessageFX.Information(getStage(), "You are not allowed to Cancel Slip No. " + oTransProcess.getInqRsv(lnRow, 3).toString(), pxeModuleName, null);
//                                            return;
//                                        case "btnASremove":
//                                            ShowMessageFX.Information(getStage(), "You are not allowed to Remove Slip No. " + oTransProcess.getInqRsv(lnRow, 3).toString(), pxeModuleName, null);
//                                            return;
//                                        case "btnASprint":
//                                            selectedItems.add(item);
//                                    }
//                                    break;
//                                case "2":
//                                    ShowMessageFX.Information(getStage(), "Slip No. " + oTransProcess.getInqRsv(lnRow, 3).toString() + " is already Cancelled.", pxeModuleName, null);
//                                    return;
//                            }
//                            pnCtr++;
//                        }
//                        lnRow++;
//                    }
//
//                    if (selectedItems.isEmpty()) {
//                        ShowMessageFX.Information(getStage(), "No items selected!", pxeModuleName, null);
//                    } else {
//                        switch (lsButton) {
//                            case "btnAScancel":
//                                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel?")) {
//                                    for (InquiryTableVehicleSalesAdvances item : selectedItems) {
//                                        String lsRow = item.getTblindex01(); // Assuming there is a method to retrieve the transaction number
//                                        if (cancelform.loadCancelWindow(oApp, (String) oTransProcess.getInqRsv(Integer.parseInt(lsRow), 1), (String) oTransProcess.getInqRsv(Integer.parseInt(lsRow), 3), "VSA")) {
//                                            if (oTransProcess.CancelReservation(Integer.parseInt(lsRow))) {
//                                                //Retrieve Reservation
//                                                String[] lsSourceNo = {psSourceNox}; //Use array cause class is mandatory array to call even I only need 1
//                                                oTransProcess.loadReservation(lsSourceNo, true);
//                                            } else {
//                                                ShowMessageFX.Information(getStage(), "Failed to cancel reservation.", pxeModuleName, null);
//                                                return;
//                                            }
//                                        } else {
//                                            return;
//                                        }
//
//                                    }
//                                    ShowMessageFX.Information(getStage(), "Reservation cancelled successfully.", pxeModuleName, null);
//                                }
//                                break;
//                            case "btnASremove":
//                                for (InquiryTableVehicleSalesAdvances item : selectedItems) {
//                                    String lsRow = item.getTblindex01(); // Assuming there is a method to retrieve the transaction number
//                                    if (Integer.parseInt(lsRow) >= 1) {
//                                        if (oTransProcess.removeInqRes(Integer.parseInt(lsRow))) {
//                                            break;
//                                        } else {
//                                            ShowMessageFX.Information(getStage(), oTransProcess.getMessage(), pxeModuleName, null);
//                                        }
//                                    }
//                                }
//                                break;
//                            case "btnASprint":
//                                lnRow = 0;
//                                String[] lsrowdata = new String[pnCtr];
//                                for (InquiryTableVehicleSalesAdvances item : selectedItems) {
//                                    String sRow = item.getTblindex01(); // Assuming there is a method to retrieve the transaction number
//                                    String sTrans = item.getTblindex10();
//                                    if (Integer.parseInt(sRow) >= 1) {
//                                        lsrowdata[lnRow] = sTrans;
//                                    }
//                                    lnRow++;
//                                }
//                                loadVehicleSalesAdvancesPrint(lsrowdata);
//                                break;
//                            default:
//                                break;
//                        }
//                        loadInquiryAdvances();
//                    }
//                    break;
//                /*INQUIRY PROCESS: GENERAL BUTTON*/
//                case "btnProcess":
//                case "btnApply":
//                    if ("btnProcess".equals(lsButton)) {
//                        if (oTransProcess.NewRecord()) {
//                        } else {
//                            return;
//                        }
//                    }
//                    if (cmbInqpr01.getSelectionModel().getSelectedIndex() >= 0) {
//                        if (cmbInqpr02.getSelectionModel().getSelectedIndex() < 0) {
//                            ShowMessageFX.Warning(getStage(), "Please select Customer Type.", "Warning", "Error while saving " + pxeModuleName);
//                            return;
//                        }
//                        if (!validatePayMode()) {
//                            return;
//                        }
//                    }
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to save?") == true) {
//                        oTransProcess.setClientID((String) oTrans.getMaster(7)); //psClientID
//                        oTransProcess.setTransNox((String) oTrans.getMaster(1)); //psSourceNox
//                        if (oTransProcess.SaveRecord()) {
//                            ShowMessageFX.Information(getStage(), "Transaction save successfully.", pxeModuleName, null);
//                            loadInquiryListTable();
//                            pagination.setPageFactory(this::createPage);
//                            getSelectedItem((String) oTrans.getMaster(1));
//                            pnEditMode = oTransProcess.getEditMode();
//                            initBtnProcess(pnEditMode);
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTransProcess.getMessage(), "Warning", "Error while saving " + pxeModuleName);
//                            return;
//                        }
//                        break;
//                    } else {
//                        return;
//                    }
//                case "btnModify":
//                    if (oTransProcess.UpdateRecord()) {
//                        loadInquiryRequirements();
//                        initInquiryRequirements(); //added by rsie 10-07-2023
//                    } else {
//                        return;
//                    }
//                    initBtnProcess(oTransProcess.getEditMode());
//                    break;
//                case "btnPymtcon":
//                    break;
//                /*INQUIRY BANK APPLICATION*/
//                case "btnBankAppNew":
//
//                    if (oTransBankApp.NewRecord()) {
//                        oTransBankApp.setTransNox((String) oTrans.getMaster(1)); //psSourceNox
//                        //Open window
//                        loadBankApplicationWindow("", pnIinqPayMode, oTransBankApp.getEditMode());
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(), "Warning", null);
//                    }
//                    break;
//                case "btnBankAppUpdate":
//                case "btnBankAppCancel":
//                case "btnBankAppView":
//                    pnCtr = 1;
//                    ObservableList<InquiryTableBankApplications> selBankItems = FXCollections.observableArrayList();
//                    for (InquiryTableBankApplications item : bankappdata) {
//                        if (item.isTblcheck01()) {
//                            if (("btnBankAppView".equals(lsButton) && pnCtr > 1)
//                                    || "btnBankAppUpdate".equals(lsButton) && pnCtr > 1) {
//                                ShowMessageFX.Warning(getStage(), "Please select atleast 1 slip to be view / updated.", pxeModuleName, null);
//                                return;
//                            }
//                            selBankItems.add(item);
//                            pnCtr++;
//                        }
//                    }
//
//                    if (selBankItems.isEmpty()) {
//                        ShowMessageFX.Information(getStage(), "No items selected!", pxeModuleName, null);
//                    } else {
//                        if ("btnBankAppCancel".equals(lsButton)) {
//                            if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to cancel?")) {
//                            } else {
//                                return;
//                            }
//                        }
//                        for (InquiryTableBankApplications item : selBankItems) {
//                            String lsTransNo = item.getTblindex10();
//                            //String sSourceNo = item.getTblindex13();
//                            switch (lsButton) {
//                                case "btnBankAppCancel":
//                                    String lsTransStat = (String) oTransBankApp.getBankAppDet(Integer.parseInt(item.getTblindex01()), 9);
//                                    switch (lsTransStat) {
//                                        case "1":
//                                            ShowMessageFX.Warning(getStage(), "Selected Bank Application has already been declined.", pxeModuleName, null);
//                                            return;
//                                        case "2":
//                                            ShowMessageFX.Warning(getStage(), "Approved bank applications cannot be cancelled.", pxeModuleName, null);
//                                            return;
//                                        case "3":
//                                            ShowMessageFX.Warning(getStage(), "Selected Bank Application has already been cancelled.", pxeModuleName, null);
//                                            return;
//                                        default:
//                                            break;
//                                    }
//                                    if (oTransBankApp.CancelBankApp(lsTransNo)) {
//                                    } else {
//                                        //ShowMessageFX.Warning(null, pxeModuleName, "Failed to cancel Bank Application.");
//                                        ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(), pxeModuleName, null);
//                                        return;
//                                    }
//                                    break;
//
//                                case "btnBankAppUpdate":
//                                    if (oTransBankApp.loadBankApplication(lsTransNo, false)) {
//                                        if (Integer.parseInt(oTransBankApp.getBankApp(9).toString()) == 3) {
//                                            ShowMessageFX.Warning(getStage(), "You are not allowed to Update cancelled Bank Application.", "Warning", null);
//                                            return;
//                                        }
//                                        if (oTransBankApp.UpdateRecord()) {
//                                            //Open window
//                                            loadBankApplicationWindow(lsTransNo, pnIinqPayMode, oTransBankApp.getEditMode());
//                                        } else {
//                                            ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(), "Warning", null);
//                                        }
//                                    } else {
//                                        ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(), "Warning", null);
//                                    }
//
//                                    break;
//                                case "btnBankAppView":
//                                    if (oTransBankApp.loadBankApplication(lsTransNo, false)) {
//                                        //Open window
//                                        loadBankApplicationWindow(lsTransNo, pnIinqPayMode, oTransBankApp.getEditMode());
//                                    } else {
//                                        ShowMessageFX.Warning(getStage(), oTransBankApp.getMessage(), "Warning", null);
//                                    }
//                                    break;
//                                default:
//                                    break;
//                            }
//                            lsTransNo = "";
//                        }
//
//                        if ("btnBankAppCancel".equals(lsButton)) {
//                            ShowMessageFX.Information(getStage(), oTransBankApp.getMessage(), pxeModuleName, null);
//                        }
//                        oTransBankApp.loadBankApplication(psSourceNox, true);
//                        loadBankApplication();
//                    }
//                    break;
//
//                /*INQUIRY FOR FOLLOW UP*/
//                case "btnFollowUp":
//                    if (oTransFollowUp.NewRecord()) {
//                        //Open window
//                        loadFollowUpWindow("", false);
//                    } else {
//                        ShowMessageFX.Warning(getStage(), oTransFollowUp.getMessage(), "Warning", null);
//                    }
//                    break;
//                case "btnCancel":
//                    if (ShowMessageFX.OkayCancel(getStage(), null, pxeModuleName, "Are you sure, do you want to cancel?") == true) {
//                        clearClassFields();
//                        clearFields();
//                        if (!psSourceNox.equals("") && psSourceNox != null) {
//                            oTrans.OpenRecord(psSourceNox);
//                            loadCustomerInquiry();
//                            loadTargetVehicle();
//                            loadPromosOfferred();
//                            pnEditMode = oTrans.getEditMode();
//                        } else {
//                            pnEditMode = EditMode.UNKNOWN;
//                        }
//                        break;
//                    } else {
//                        return;
//                    }
//                case "btnClose": //close tab
//                    if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
//                        if (poUnload != null) {
//                            poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
//                        } else {
//                            ShowMessageFX.Warning(getStage(), "Notify System Admin to Configure Null value at close button.", pxeModuleName, null);
//                        }
//                        break;
//                    } else {
//                        return;
//                    }
//                default:
//                    ShowMessageFX.Warning(getStage(), "Button with name " + lsButton + " not registered.", pxeModuleName, null);
//                    return;
//            }
//            if (pnSelectedIndex != 1) {
//                initButton(pnEditMode);
//
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(VehicleInquiryFormController.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    private void initCapitalizationFields() {
//        poTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
//        /*TextArea*/
//        InputTextUtil.setCapsLockBehavior(textArea33);
//        InputTextUtil.setCapsLockBehavior(textArea08);
//    }
//
//    private void initTextKeyPressed() {
//        poTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
//        /*TextArea*/
//        textArea33.setOnKeyPressed(this::textArea_KeyPressed);
//        textArea08.setOnKeyPressed(this::textArea_KeyPressed);
//    }
//    //Search using F3
//
//    private void txtField_KeyPressed(KeyEvent event) {
//        TextField loTxtField = (TextField) event.getSource();
//        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
//
//        switch (event.getCode()) {
//            case F3:
//            case TAB:
//            case ENTER:
//                switch (lnIndex) {
//                    case 2: //Branch Name
//                        if (oTrans.searchBranch(txtField02.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//                    case 7: //Customer
//                        if (oTrans.searchCustomer(txtField07.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//                    case 4: //Sales Executive
//                        if (oTrans.searchSalesExec(txtField04.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//
//                    case 9: //Agent
//                        if (oTrans.searchSalesAgent(txtField09.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//
//                    case 15: //Event / Activity
//                        String sActType = "";
//                        if (cmbType012.getSelectionModel().getSelectedIndex() == 4) {
//                            sActType = "sal";
//                        } else if (cmbType012.getSelectionModel().getSelectedIndex() == 5) {
//                            sActType = "eve";
//                        }
//
//                        if (oTrans.searchActivity(1, sActType, true)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//                    case 13: //Web Inquiry
//                        if (oTrans.searchPlatform(txtField14.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//                    case 14: //Model
//                        if (oTrans.searchVhclPrty(0, txtField14.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                        }
//                        break;
//                    case 41:
//                        if (oTrans.searchContactPerson(txtField41.getText(), false)) {
//                            loadCustomerInquiry();
//                        } else {
//                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
//                            return;
//                        }
//                        break;
//                }
//                break;
//        }
//
//        switch (event.getCode()) {
//            case ENTER:
//            case DOWN:
//                CommonUtils.SetNextFocus(loTxtField);
//                break;
//            case UP:
//                CommonUtils.SetPreviousFocus(loTxtField);
//        }
//
//    }
//
//    private void textArea_KeyPressed(KeyEvent event) {
//        TextArea textArea = (TextArea) event.getSource();
//        String textAreaID = ((TextArea) event.getSource()).getId();
////        poJSON = new JSONObject();
//        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
//            switch (textAreaID) {
//            }
//            event.consume();
//            CommonUtils.SetNextFocus((TextArea) event.getSource());
//        } else if (event.getCode() == KeyCode.UP) {
//            event.consume();
//            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
//        }
//    }
//
//    private void initTextFieldFocus() {
//        poTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
//        textArea33.focusedProperty().addListener(txtArea_Focus);
//        textArea33.focusedProperty().addListener(txtArea_Focus);
//    }
//
//    //TODO
//    private boolean validatePayMode() {
//        String lsPayMode = "";
//        String lsTrnStat = "";
//        String lsRecStat = "";
//        int lnPayMode = cmbInqpr01.getSelectionModel().getSelectedIndex() - 1;
//        for (int pnCtr = 1; pnCtr <= oTransBankApp.getBankAppCount(); pnCtr++) {
//            lsPayMode = String.valueOf(oTransBankApp.getBankAppDet(pnCtr, 4));
//            lsTrnStat = String.valueOf(oTransBankApp.getBankAppDet(pnCtr, 9));
//            //lsRecStat = String.valueOf(oTransBankApp.getBankAppDet(pnCtr, 10));
//            //if (lnPayMode >= 0){
//            //if(lsRecStat.equals("1")){ //Active
//            if (lsTrnStat.equals("0") || lsTrnStat.equals("2")) { //on-going or approved
//                if ((lnPayMode != Integer.valueOf(lsPayMode))) {
////                                    cmbInqpr01.getSelectionModel().select(Integer.valueOf(lsPayMode));
////                                    cmbInqpr02.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqReq(oTransProcess.getInqReqCount(), "cCustGrpx").toString()));
//                    ShowMessageFX.Warning(getStage(), "Invalid Payment Mode: Please cancel on-going or approved bank application with different payment mode.", "Warning", null);
//                    return false;
//
//                }
//            }
//            //}
//            //}
//        }
//        return true;
//    }
//
//    /*OPEN WINDOW FOR VEHICLE SALES PROPOSAL*/
//    private void loadVSPWindow() {
//        String lsFormName = "Vehicle Sales Proposal";
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("VSPForm.fxml"));
////            VSPFormController loControl = new VSPFormController();
////            loControl.setGRider(oApp);
////            fxmlLoader.setController(loControl);
////            Parent parent = fxmlLoader.load();
////            loControl.setAddMode((String) oTrans.getMaster(1));
////            AnchorPane otherAnchorPane = loControl.AnchorMain;
//// Get the parent of the TabContent node
//        Node tabContent = AnchorMain.getParent();
//        Parent tabContentParent = tabContent.getParent();
//// If the parent is a TabPane, you can work with it directly
//        if (tabContentParent instanceof TabPane) {
//            TabPane tabpane = (TabPane) tabContentParent;
//
//            for (Tab tab : tabpane.getTabs()) {
//                if (tab.getText().equals(lsFormName)) {
//                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "You have opened Vehicle Sales Proposal Form. Are you sure you want to convert this inquiry for a new vsp record?") == true) {
//                    } else {
//                        return;
//                    }
//                    tabpane.getSelectionModel().select(tab);
//                    poUnload.unloadForm(AnchorMain, oApp, lsFormName);
//                    loadVSPWindow();
//                    return;
//                }
//            }
//
//            Tab loNewTab = new Tab(lsFormName, parent);
//            loNewTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
//            loNewTab.setContextMenu(fdController.createContextMenu(tabpane, loNewTab, oApp));
//            tabpane.getTabs().add(loNewTab);
//            tabpane.getSelectionModel().select(loNewTab);
//            loNewTab.setOnCloseRequest(event -> {
//                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
//                    if (poUnload != null) {
//                        poUnload.unloadForm(otherAnchorPane, oApp, lsFormName);
//                    } else {
//                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
//                    }
//                } else {
//                    // Cancel the close request
//                    event.consume();
//                }
//
//            });
//
//            List<String> tabName = new ArrayList<>();
//            tabName = TabsStateManager.loadCurrentTab();
//            tabName.remove(lsFormName);
//            tabName.add(lsFormName);
//            // Save the list of tab IDs to the JSON file
//            TabsStateManager.saveCurrentTab(tabName);
//
//        }
//    }
//
//    /*INQUIRY PRINT REFUND*/
//    private void loadPrintRefund(String fsValue) throws SQLException {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("InquiryRefundPrint.fxml"));
//
//            InquiryRefundPrintController loControl = new InquiryRefundPrintController();
//            loControl.setGRider(oApp);
//            loControl.setTransNox(fsValue);
//            loControl.setObject(oTrans);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//        } catch (IOException e) {
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*INQUIRY PROCESS: OPEN VEHICLE SALES ADVANCES*/
//    private void loadVehicleSalesAdvancesWindow(int fnRow, boolean fstate, Integer fnStat, Integer fEditMode) throws SQLException {
//        /**
//         * if state = true : ADD else if state = false : UPDATE *
//         */
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("InquiryVehicleSalesAdvancesForm.fxml"));
//
//            InquiryVehicleSalesAdvancesFormController loControl = new InquiryVehicleSalesAdvancesFormController();
//            loControl.setGRider(oApp);
//            loControl.setVSAObject(oTransProcess);
//            loControl.setTableRows(fnRow);
//            loControl.setState(fstate);
//            loControl.setInqStat(fnStat);
//            loControl.setEditMode(fEditMode);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//            loadInquiryAdvances();
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*INQUIRY PROCESS: PRINT VEHICLE SALES ADVANCES*/
//    private void loadVehicleSalesAdvancesPrint(String[] fsTransno) throws SQLException {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("ReservationPrint.fxml"));
//
//            ReservationPrintController loControl = new ReservationPrintController();
//            loControl.setGRider(oApp);
//            //loControl.setVSAObject(oTransProcess);
//            loControl.setTransNox(fsTransno);
//
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//            loadInquiryAdvances();
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*INQUIRY BANK APPLICATION*/
//    private void loadBankApplicationWindow(String fsTransnox, Integer fnPaymentMode, Integer fniEditmode) throws SQLException {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("InquiryBankApplicationForm.fxml"));
//
//            InquiryBankApplicationFormController loControl = new InquiryBankApplicationFormController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransBankApp);
//            loControl.setEditMode(fniEditmode);
//            loControl.setInqPaymentMode(fnPaymentMode - 1);
//            loControl.setsTransNo(fsTransnox);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//            oTransBankApp.loadBankApplication((String) oTrans.getMaster(1), true);
//            loadBankApplication();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*INQUIRY FOR FOLLOW-UP*/
//    private void loadFollowUpWindow(String fsTransno, Boolean fbEntmode) throws SQLException {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("InquiryFollowUpForm.fxml"));
//
//            InquiryFollowUpFormController loControl = new InquiryFollowUpFormController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransFollowUp);
//            loControl.setsTransNo(fsTransno);
//            loControl.setsSourceNo(psSourceNox);
//            loControl.setState(fbEntmode);
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//            oTransFollowUp.loadFollowUp(psSourceNox, true);
//            loadFollowUp();
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    /*INQUIRY FOR LOST SALE*/
//    private void loadLostSaleWindow() throws SQLException {
//        try {
//            Stage stage = new Stage();
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("InquiryLostSaleForm.fxml"));
//
//            InquiryLostSaleFormController loControl = new InquiryLostSaleFormController();
//            loControl.setGRider(oApp);
//            loControl.setFollowUpObject(oTransFollowUp);
//            loControl.setsSourceNo(psSourceNox);
//            loControl.setsVSPNox("");
//            loControl.setState(true); //If true set tag to lost sale automatically else allow user to edit.
//            loControl.setClientName(oTrans.getMaster("sCompnyNm").toString()); //Set Client Name
//            fxmlLoader.setController(loControl);
//
//            //load the main interface
//            Parent parent = fxmlLoader.load();
//
//            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    xOffset = event.getSceneX();
//                    yOffset = event.getSceneY();
//                }
//            });
//
//            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    stage.setX(event.getScreenX() - xOffset);
//                    stage.setY(event.getScreenY() - yOffset);
//                }
//            });
//
//            //set the main interface as the scene
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("");
//            stage.showAndWait();
//
//            oTrans.OpenRecord(psSourceNox);
//            oTransFollowUp.loadFollowUp(psSourceNox, true);
//            loadFollowUp();
//
//            initButton(oTrans.getEditMode());
//        } catch (IOException e) {
//            e.printStackTrace();
//            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
//            System.exit(1);
//        }
//    }
//
//    private void loadInquiryProcess(String TransNo) {
//        try {
//            //Retrieve Requirements
//            oTransProcess.loadRequirements(TransNo);
//            if (oTransProcess.getInqReqCount() > 0) {
//                cmbInqpr01.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqReq(oTransProcess.getInqReqCount(), "cPayModex").toString())); //Inquiry Payment mode
//                cmbInqpr02.getSelectionModel().select(Integer.parseInt(oTransProcess.getInqReq(oTransProcess.getInqReqCount(), "cCustGrpx").toString())); //Inquiry Customer Type
//                pnIinqPayMode = Integer.parseInt(oTransProcess.getInqReq(oTransProcess.getInqReqCount(), "cPayModex").toString());
//            } else {
//                cmbInqpr01.setValue(null);
//                cmbInqpr02.setValue(null);
//            }
//            //Load Table Requirements
//            loadInquiryRequirements();
//            //Retrieve Reservation
//            String[] lsSourceNo = {TransNo};
//            oTransProcess.loadReservation(lsSourceNo, true);
//            //Load Table Reservation
//            loadInquiryAdvances();
//
//            //Load Table Bank Application
//            oTransBankApp.loadBankApplication(TransNo, true);
//            loadBankApplication();
//
//            //Load Table Follow Up History
//            oTransFollowUp.loadFollowUp(TransNo, true);
//            loadFollowUp();
//
//            psClientID = (String) oTrans.getMaster(7);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(VehicleInquiryFormController.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    //Load Customer Inquiry Data
//    public void loadCustomerInquiry() {
//        // Get the current event handler
//        EventHandler<ActionEvent> eventHandler = cmbType012.getOnAction();
//        // Remove the event handler to prevent it from triggering
//        cmbType012.setOnAction(null);
//        // Set the value without triggering the event
//        cmbType012.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(12).toString())); //Inquiry Type
//        // Add the event handler back
//        cmbType012.setOnAction(eventHandler);
//        txtField03.setText(CommonUtils.xsDateMedium((Date) oTrans.getMaster(3)));  //Inquiry Date
//        txtField07.setText((String) oTrans.getMaster(29)); //Custmer Name ***
//        if (((String) oTrans.getMaster(39)).equals("1")) {
//            txtField29.setText((String) oTrans.getMaster(29)); //Company Name
//        } else {
//            txtField29.setText("");
//        }
//        txtField04.setText((String) oTrans.getMaster(34)); //Sales Executive ID //Employee ID
//        txtField30.setText((String) oTrans.getMaster(30)); //Contact No
//        txtField32.setText((String) oTrans.getMaster(32)); //Email Address
//        textArea33.setText((String) oTrans.getMaster(33)); //Client Address
//        txtField31.setText((String) oTrans.getMaster(31)); //Social Media
//        txtField13.setText((String) oTrans.getMaster(36)); //Web Inquiry
//        txtField10.setValue(strToDate(CommonUtils.xsDateShort((Date) oTrans.getMaster(10)))); //Target Release Date
//        txtField09.setText((String) oTrans.getMaster(35)); //Agent ID
//        txtField15.setText((String) oTrans.getMaster(37)); //Activity ID
//        txtField02.setText((String) oTrans.getMaster(38)); //Branch Name
//        txtField41.setText((String) oTrans.getMaster(41)); //Contact Name
//        comboBox24.getSelectionModel().select(Integer.parseInt(oTrans.getMaster(24).toString())); //Inquiry Status
//        switch (oTrans.getMaster(5).toString()) {
//            case "0":
//                rdbtnNew05.setSelected(true);
//                break;
//            case "1":
//                rdbtnPro05.setSelected(true);
//                break;
//            default:
//                rdbtnNew05.setSelected(false);
//                rdbtnPro05.setSelected(false);
//                break;
//        }
//        switch (oTrans.getMaster(11).toString().toLowerCase()) {
//            case "a":
//                rdbtnHtA11.setSelected(true);
//                break;
//            case "b":
//                rdbtnHtB11.setSelected(true);
//                break;
//            case "c":
//                rdbtnHtC11.setSelected(true);
//                break;
//            default:
//                rdbtnHtA11.setSelected(false);
//                rdbtnHtB11.setSelected(false);
//                rdbtnHtC11.setSelected(false);
//                break;
//        }
//        textArea08.setText((String) oTrans.getMaster(8)); //Remarks
//        txtField17.setText(oTrans.getMaster(17).toString()); //Slip No.
//        txtField18.setText(oTrans.getMaster(18).toString()); //Rsv Amount
//        txtField14.setText(oTrans.getMaster(14).toString());
//        if (((String) oTrans.getMaster(39)).equals("1")) {
//            txtField41.setText((String) oTrans.getMaster(41)); //Contact Name
//            txtField41.setVisible(true);
//            lblContactPrsn.setVisible(true);
//        } else {
//            txtField41.setVisible(false);
//            lblContactPrsn.setVisible(false);
//        }
//    }
//
//    /*CUSTOMER INQUIRY: PRIORITY UNIT*/
//    //Load Customer Inquiry Priority Unit
//    public void loadTargetVehicle() {
//        /*Populate table*/
//        priorityunitdata.clear();
//        for (pnCtr = 1; pnCtr <= oTrans.getVhclPrtyCount(); pnCtr++) {
//            //Update priority row count
//            oTrans.setVhclPrty(pnCtr, "nPriority", pnCtr); //Handle Encoded Value
//            //Add Priority unit to table display
//            priorityunitdata.add(new InquiryTablePriorityUnit(
//                    oTrans.getVhclPrty(pnCtr, "nPriority").toString(), //Priority Unit
//                    oTrans.getVhclPrty(pnCtr, "sDescript").toString() // Vehicle Description
//            ));
//        }
//
//    }
//
//    // Load Customer Inquiry Target Vehicle Data
//    public void initTargetVehicle() {
//        tblPriorityUnit.setEditable(true);
//        trgvIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        trgvIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//
//        tblPriorityUnit.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblPriorityUnit.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblPriorityUnit.setItems(priorityunitdata);
////          boolean lbShow = (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE);
////          if (lbShow) {
////               tblPriorityUnit.setEditable(true);
////          } else {
////               tblPriorityUnit.setEditable(false);
////          }
//
////          trgvIndex02.setCellFactory(TextFieldTableCell.forTableColumn()); // make the cells editable
////          // Set the event handler to store the edited value
////          trgvIndex02.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InquiryTablePriorityUnit, String>>() {
////               @Override
////               public void handle(TableColumn.CellEditEvent<InquiryTablePriorityUnit, String> event) {
////                    // Code to handle edit event
////                    InquiryTablePriorityUnit detail = event.getRowValue();
////                    detail.setTblindex02(event.getNewValue());
////                    sValue = event.getNewValue();
////               }
////          });
////          trgvIndex02.setEditable(true);// make the column editable
////
////          tblPriorityUnit.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
////               TablePosition<?, ?> focusedCell = tblPriorityUnit.getFocusModel().getFocusedCell();
////               //String columnId = focusedCell.getTableColumn().getId();
////               switch (event.getCode()){
////                    case F3:
////                    case ENTER:
////                         // Check if the focused cell is editable
////                         if (tblPriorityUnit.getItems().size() > 0) {
////                              if (focusedCell.getTableColumn().isEditable()) {
////                                   int lnIndex = Integer.parseInt(focusedCell.getTableColumn().getId().substring(9,11));
////                                   switch (lnIndex){
////                                        case 2: //Vehicle Description
////                                             try {
////                                             if ((oTrans.getVhclPrty(tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1, 1).toString() != null)
////                                             &&  (!oTrans.getVhclPrty(tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1, 1).toString().trim().equals(""))){
////                                             } else {
////                                                  if (lbShow) {
////                                                       if (oTrans.searchVhclPrty(tblPriorityUnit.getSelectionModel().getSelectedIndex() + 1,"",false)){
////                                                            loadTargetVehicle();
////                                                       } else {
////                                                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
////                                                       }
////                                                  }
////                                             }
////
////                                             } catch (SQLException ex) {
////                                                  Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
////                                             }
////                                   break;
////                                   }
////                              }
////                         }
////                    break;
////               }
////          });
//    }
//
//    @FXML
//    private void tblPriorityUnit_Clicked(MouseEvent event) {
//        if (event.getClickCount() > 0) {
//            pnSelectedTblRowIndex = tblPriorityUnit.getSelectionModel().getSelectedIndex();
//
////               //ShowMessageFX.Information(null, pxeModuleName, pnSelectedTblRowIndex + "");
////               tblPriorityUnit.setOnKeyReleased((KeyEvent t)-> {
////                    try {
////                         KeyCode key = t.getCode();
////                         switch (key){
////                               case DOWN:
////                                   if (pnSelectedTblRowIndex > 0) {
//////                                       tblPriorityUnit.getSelectionModel().select(pnSelectedIndex - 1);
//////                                       tblPriorityUnit.scrollTo(pnSelectedIndex - 1);
//////                                        oTrans.setVehiclePriority(pnSelectedTblRowIndex, false);
////                                   }
////                                   event.consume();
////                                   break;
////                               case UP:
////                                    if (pnSelectedTblRowIndex < tblPriorityUnit.getItems().size() - 1) {
////                                        //tblPriorityUnit.getSelectionModel().select(pnSelectedIndex + 1);
////                                        //tblPriorityUnit.scrollTo(pnSelectedIndex + 1);
//// //                                       oTrans.setVehiclePriority(pnSelectedTblRowIndex, true);
////                                   }
////                                   event.consume();
////                                   break;
////                         }
////                    } catch (SQLException ex) {
////                         Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
////                    } catch (org.json.simple.parser.ParseException ex) {
////                         Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////               });
//        }
//
//    }
//
//    /*CUSTOMER INQUIRY: PROMOS OFFERED*/
//    //Load Customer Inquiry Promo Offered
//    public void loadPromosOfferred() {
//        /*Populate table*/
//        promosoffereddata.clear();
//        for (pnCtr = 1; pnCtr <= oTrans.getInqPromoCount(); pnCtr++) {
//            promosoffereddata.add(new InquiryTablePromoOffered(
//                    String.valueOf(pnCtr),
//                    //oTrans.getInqPromo(pnCtr, "sTransNox").toString(), //ROW
//                    CommonUtils.xsDateShort((Date) oTrans.getInqPromo(pnCtr, "dDateFrom")), //Start Date
//                    CommonUtils.xsDateShort((Date) oTrans.getInqPromo(pnCtr, "dDateThru")), //End Date
//                    oTrans.getInqPromo(pnCtr, "sActTitle").toString() // Promo Offered
//            ));
//        }
//
//    }
//
//    //Load Customer Inquiry PromosOffered
//    public void initPromosOffered() {
//        tblPromosOffered.setEditable(true);
//        prmoIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        prmoIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        prmoIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        prmoIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//
//        tblPromosOffered.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblPromosOffered.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblPromosOffered.setItems(promosoffereddata);
////        prmoIndex04.setCellFactory(TextFieldTableCell.forTableColumn()); // make the cells editable
//
////        // Set the event handler to store the edited value
////        prmoIndex04.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InquiryTablePromoOffered, String>>() {
////            @Override
////            public void handle(TableColumn.CellEditEvent<InquiryTablePromoOffered, String> event) {
////                // Code to handle edit event
////                InquiryTablePromoOffered detail = event.getRowValue();
////                detail.setTblindex04(event.getNewValue());
////                sValue = event.getNewValue();
////            }
////        });
////        prmoIndex04.setEditable(true);// make the column editable
////        //unitIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
////
////        tblPromosOffered.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
////            TablePosition<?, ?> focusedCell = tblPromosOffered.getFocusModel().getFocusedCell();
////            //String columnId = focusedCell.getTableColumn().getId();
////            int lnIndex = Integer.parseInt(focusedCell.getTableColumn().getId().substring(9, 11));
////            switch (event.getCode()) {
////                case F3:
////                case ENTER:
////                    // Check if the focused cell is editable
////                    if (focusedCell.getTableColumn().isEditable()) {
////                        switch (lnIndex) {
////                            case 2: //Vehicle Description
////                                // Code to execute when F3 is pressed on an editable column
////                                ShowMessageFX.Warning(getStage(), (tblPromosOffered.getSelectionModel().getSelectedIndex() + 1) + "", "Warning", null);
////                                //System.out.println("F3 was pressed on an editable column");
////                                // System.out.println(tblPriorityUnit.getSelectionModel().getSelectedIndex());
//////                                         try {
//////                                             if (oTrans.searchInqPromo(tblPromosOffered.getSelectionModel().getSelectedIndex() + 1,sValue,false)){
//////                                                 loadPromosOfferred();
//////                                             } else
//////                                                  ShowMessageFX.Warning(getStage(), oTrans.getMessage(),"Warning", null);
//////                                         } catch (SQLException ex) {
//////                                              Logger.getLogger(InquiryFormController.class.getName()).log(Level.SEVERE, null, ex);
//////                                         }
////
////                                break;
////
////                        }
////                    }
////                    break;
////            }
////        });
//    }
//
//    /*INQUIRY: PROCESS*/
//    // Load Inquiry Process Requirements
//    public void loadInquiryRequirements() {
//
//        /*Populate table*/
//        inqrequirementsdata.clear();
//        boolean lbSubmitted = false;
//        int lnCnt;
//        String lsRecby, lsRecdate;
//        lsRecby = "";
//        lsRecdate = "";
//        if (oTransProcess.loadRequirementsSource(String.valueOf(cmbInqpr02.getSelectionModel().getSelectedIndex()), String.valueOf(cmbInqpr01.getSelectionModel().getSelectedIndex()))) {
//            //System.out.println("oTransProcess.getInqReqCount() " + oTransProcess.getInqReqCount());
//            for (pnCtr = 1; pnCtr <= oTransProcess.getInqReqSrcCount(); pnCtr++) {
//                //Display selected Item
//                for (lnCnt = 1; lnCnt <= oTransProcess.getInqReqCount(); lnCnt++) {
//                    if (oTransProcess.getInqReq(lnCnt, "sRqrmtCde").toString().equals(oTransProcess.getInqReqSrc(pnCtr, "sRqrmtCde").toString())) {
//                        if (oTransProcess.getInqReq(lnCnt, "cSubmittd").toString().equals("1")) {
//                            lbSubmitted = true;
//                            lsRecby = oTransProcess.getInqReq(lnCnt, "sCompnyNm").toString();
//                            lsRecdate = CommonUtils.xsDateShort((Date) oTransProcess.getInqReq(lnCnt, "dReceived"));
//                        } else {
//                            lbSubmitted = false;
//                            lsRecby = "";
//                            lsRecdate = "";
//                        }
//                        break;
//                    }
//                }
//
//                //Add Display for Observable List Table View
//                inqrequirementsdata.add(new InquiryTableRequirements(
//                        lbSubmitted //Check box
//                        ,
//                         oTransProcess.getInqReqSrc(pnCtr, "sDescript").toString().trim().toUpperCase() //Requirements Description
//                        ,
//                         lsRecby.toUpperCase() //Received By
//                        ,
//                         lsRecdate //Received Date
//                ));
//
//                //Clear Variables
//                lbSubmitted = false;
//                lsRecby = "";
//                lsRecdate = "";
//            }
//
//        }
//
//        //initInquiryRequirements();
//    }
//
//    public void initInquiryRequirements() {
//        boolean lbShow = false;
//        switch (comboBox24.getSelectionModel().getSelectedIndex()) {
//            case 0: //For Follow up
//                lbShow = oTransProcess.getEditMode() == EditMode.READY;
//                break;
//            case 1: //On Process
//            case 3: //VSP
//                lbShow = oTransProcess.getEditMode() == EditMode.UPDATE;
//                break;
//            case 2: //Lost Sale
//            case 4: //Sold
//            case 5: //Retired
//            case 6: //Cancelled
//                lbShow = false;
//                break;
//        }
//
//        boolean lbMode = lbShow;
//        tblRequirementsInfo.setEditable(true);
//        //tblRequirementsInfo.getSelectionModel().setCellSelectionEnabled(true);
//        tblRequirementsInfo.setSelectionModel(null);
//        rqrmIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        rqrmIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        rqrmIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        rqrmIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//
//        rqrmIndex01.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(Integer index) {
//                InquiryTableRequirements requirement = inqrequirementsdata.get(index);
//                BooleanProperty selected = requirement.selectedProperty();
//
//                loadInquiryRequirements();
//                selected.addListener((obs, oldValue, newValue) -> {
//                    if (newValue) {
//                        if (lbMode) {
//                            boolean bExist = false;
//                            int lnRow = 0;
//
//                            for (lnRow = 1; lnRow <= oTransProcess.getInqReqCount(); lnRow++) {
//                                if (oTransProcess.getInqReq(lnRow, "sRqrmtCde").toString().equals(oTransProcess.getInqReqSrc(index + 1, "sRqrmtCde").toString())) {
//                                    bExist = true;
//                                    break;
//                                }
//                            }
//
//                            if (!bExist) {
//                                oTransProcess.addRequirements();
//                                lnRow = oTransProcess.getInqReqCount();
//                            }
//                            //oTransProcess.getInqReqCount()
//                            if (oTransProcess.searchSalesExec(lnRow, "", false)) {
//                                oTransProcess.setInqReq(lnRow, "cSubmittd", 1);
//                                oTransProcess.setInqReq(lnRow, "sRqrmtCde", oTransProcess.getInqReqSrc(index + 1, "sRqrmtCde").toString());
//                                oTransProcess.setInqReq(lnRow, "dReceived", SQLUtil.toDate(CommonUtils.xsDateShort((Date) oApp.getServerDate()), SQLUtil.FORMAT_SHORT_DATE));
//                                oTransProcess.setInqReq(1, "cPayModex", cmbInqpr01.getSelectionModel().getSelectedIndex());
//                                oTransProcess.setInqReq(1, "cCustGrpx", cmbInqpr02.getSelectionModel().getSelectedIndex());
//                            } else {
//                                oTransProcess.removeInqReq(oTransProcess.getInqReqSrc(index + 1, "sRqrmtCde").toString());
//                                ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", "No selected Employee!");
//                            }
//                        }
//                    } else {
//                        if (lbMode) {
//                            oTransProcess.removeInqReq(oTransProcess.getInqReqSrc(index + 1, "sRqrmtCde").toString());
//
//                        }
//                    }
//                });
//
//                return selected;
//            }
//        }));
//
//        tblRequirementsInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblRequirementsInfo.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblRequirementsInfo.setItems(inqrequirementsdata);
//
//    }
//
//    // Load Inquiry Process Advance Slip
//    public void loadInquiryAdvances() {
//        /*Populate table*/
//        inqvsadata.clear();
//        double lnAmount = 0.00;
//        String sVsaType, sVsaStat, formattedAmount;
//        for (pnCtr = 1; pnCtr <= oTransProcess.getReserveCount(); pnCtr++) {
//            switch (oTransProcess.getInqRsv(pnCtr, 12).toString()) {
//                case "0":
//                    sVsaType = "RESERVATION";
//                    break;
//                case "1":
//                    sVsaType = "DEPOSIT";
//                    break;
//                case "2":
//                    sVsaType = "SAFEGUARD DUTY";
//                    break;
//                default:
//                    sVsaType = "";
//                    break;
//            }
//
//            switch (oTransProcess.getInqRsv(pnCtr, 13).toString()) {
//                case "0":
//                    sVsaStat = "FOR APPROVAL";
//                    break;
//                case "1":
//                    sVsaStat = "APPROVED";
//                    break;
//                case "2":
//                    sVsaStat = "CANCELLED";
//                    break;
//                default:
//                    sVsaStat = "";
//                    break;
//            }
//
//            // amount= Double.parseDouble(String.format("%.2f", oTransProcess.getInqRsv(pnCtr, 5)));
//            // Format the decimal value with decimal separators
//            //TODO
//            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
//            //DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###.######");
//            //DecimalFormat decimalFormat = new DecimalFormat("###############.######");
//            formattedAmount = decimalFormat.format(Double.parseDouble(String.valueOf(oTransProcess.getInqRsv(pnCtr, 5))));
//            inqvsadata.add(new InquiryTableVehicleSalesAdvances(
//                    false,
//                    String.valueOf(pnCtr) //Row
//                    ,
//                     CommonUtils.xsDateShort((Date) oTransProcess.getInqRsv(pnCtr, 2)) //Date
//                    ,
//                     sVsaType //Type
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 3) //VSA No
//                    ,
//                     formattedAmount //Amount
//                    ,
//                     sVsaStat //Status
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 6) // Remarks
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 13) // Approved By
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 14) // Approved Date
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 1) // sTransNox
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 20) // Client Name
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 22) // SE Name
//                    ,
//                     (String) oTransProcess.getInqRsv(pnCtr, 21) // Unit Description
//
//            ));
//
//        }
//        initInquiryAdvances();
//
//    }
//
//    public void initInquiryAdvances() {
//        boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE);
//        tblAdvanceSlip.setEditable(true);
//        //tblAdvanceSlip.getSelectionModel().setCellSelectionEnabled(true);
//        tblAdvanceSlip.setSelectionModel(null);
//        vsasIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        vsasIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        vsasIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        vsasIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//        vsasIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
//        vsasIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
//        vsasIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
//
//        vsasCheck01.setCellValueFactory(new PropertyValueFactory<>("tblcheck01"));
//        vsasCheck01.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(Integer index) {
//                InquiryTableVehicleSalesAdvances advances = inqvsadata.get(index);
//                BooleanProperty selected = advances.selectedProperty();
//                selected.addListener((obs, oldValue, newValue) -> {
//                    if (newValue) {
//                        if (lbShow) {
//                            advances.setTblcheck01(newValue);
//                        }
//                    } else {
//                        if (lbShow) {
//                            advances.setTblcheck01(newValue);
//                        }
//                    }
//                });
//                return selected;
//            }
//        }));
//
//        tblAdvanceSlip.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblAdvanceSlip.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblAdvanceSlip.setItems(inqvsadata);
//
//    }
//
//    //Load Bank Application Data
//    public void loadBankApplication() {
//        /*Populate table*/
//        bankappdata.clear();
//        for (pnCtr = 1; pnCtr <= oTransBankApp.getBankAppCount(); pnCtr++) {
//            String sPayMode, sBankAppStat;
//            switch ((String) oTransBankApp.getBankAppDet(pnCtr, 4)) {
//                case "0":
//                    sPayMode = "BANK PURCHASE ORDER";
//                    break;
//                case "1":
//                    sPayMode = "BANK FINANCING";
//                    break;
//                case "2":
//                    sPayMode = "COMPANY PURCHASE ORDER";
//                    break;
//                case "3":
//                    sPayMode = "COMPANY FINANCING";
//                    break;
//                default:
//                    sPayMode = "";
//                    break;
//            }
//
//            if (null == (String) oTransBankApp.getBankAppDet(pnCtr, 9)) {
//                sBankAppStat = "";
//            } else {
//                switch ((String) oTransBankApp.getBankAppDet(pnCtr, 9)) {
//                    case "0":
//                        sBankAppStat = "ON-GOING";
//                        break;
//                    case "1":
//                        sBankAppStat = "DECLINE";
//                        break;
//                    case "2":
//                        sBankAppStat = "APPROVED";
//                        break;
//                    case "3":
//                        sBankAppStat = "CANCELLED";
//                        break;
//                    default:
//                        sBankAppStat = "";
//                        break;
//                }
//            }
//
//            bankappdata.add(new InquiryTableBankApplications(
//                    false,
//                    String.valueOf(pnCtr) //Row
//                    ,
//                     (String) oTransBankApp.getBankAppDet(pnCtr, 16) //Bank Name
//                    ,
//                     (String) oTransBankApp.getBankAppDet(pnCtr, 18) //Bank Branch
//                    ,
//                     sPayMode //Payment Mode
//                    ,
//                     (String) oTransBankApp.getBankAppDet(pnCtr, 19) //Bank Address
//                    ,
//                     (String) oTransBankApp.getBankAppDet(pnCtr, 8) //Remarks
//                    ,
//                     CommonUtils.xsDateShort((Date) oTransBankApp.getBankAppDet(pnCtr, 2)) //Applied Date
//                    ,
//                     sBankAppStat // Application Status
//                    ,
//                     CommonUtils.xsDateShort((Date) oTransBankApp.getBankAppDet(pnCtr, 3)) //Approval Date
//                    ,
//                     (String) oTransBankApp.getBankAppDet(pnCtr, 1) //sTransNox
//                    ,
//                     (String) oTransBankApp.getBankAppDet(pnCtr, 14) //Cancelled By
//                    ,
//                     CommonUtils.xsDateShort((Date) oTransBankApp.getBankAppDet(pnCtr, 15))//Cancelled Date
//            //,(String) oTransBankApp.getBankAppDet(pnCtr, 6) //sSourceNo
//            ));
//        }
//        initBankApplication();
//    }
//
//    //Load Bank Application Data in table
//    public void initBankApplication() {
//        boolean lbShow = (pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE);
//        tblBankApplication.setEditable(true);
//        tblBankApplication.setSelectionModel(null);
//        bankIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
//        bankIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        bankIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        bankIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//        bankIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex08")); //status
//        //bankIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex11")); //cancelled by
//        bankIndex07.setCellValueFactory(new PropertyValueFactory<>("tblindex12")); //cancelled date
//
//        bankCheck01.setCellValueFactory(new PropertyValueFactory<>("tblcheck01"));
//        bankCheck01.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(Integer index) {
//                InquiryTableBankApplications select = bankappdata.get(index);
//                BooleanProperty selected = select.selectedProperty();
//                selected.addListener((obs, oldValue, newValue) -> {
//                    if (newValue) {
//                        if (lbShow) {
//                            select.setTblcheck01(newValue);
//                        }
//                    } else {
//                        if (lbShow) {
//                            select.setTblcheck01(newValue);
//                        }
//                    }
//                });
//                return selected;
//            }
//        }));
//
//        tblBankApplication.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblBankApplication.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblBankApplication.setItems(bankappdata);
//
//    }
//
//    //Load FollowUp Data
//    public void loadFollowUp() {
//        /*Populate table*/
//        followupdata.clear();
//        for (pnCtr = 1; pnCtr <= oTransFollowUp.getFollowUpCount(); pnCtr++) {
//            followupdata.add(new InquiryTableFollowUp(
//                    String.valueOf(pnCtr) //Row
//                    ,
//                     (String) oTransFollowUp.getDetail(pnCtr, 1) //sTransNo
//                    ,
//                     CommonUtils.xsDateShort((Date) oTransFollowUp.getDetail(pnCtr, 3)) //Follow up Date
//                    ,
//                     CommonUtils.xsDateShort((Date) oTransFollowUp.getDetail(pnCtr, 8)) //Next Follow up Date
//                    ,
//                     (String) oTransFollowUp.getDetail(pnCtr, 6) //Medium
//                    ,
//                     (String) oTransFollowUp.getDetail(pnCtr, 16) //Platform
//                    ,
//                     (String) oTransFollowUp.getDetail(pnCtr, 4) //Remarks
//
//            ));
//        }
//        initFollowUp();
//    }
//
//    //Load FollowUp Data
//    public void initFollowUp() {
//        tblFollowHistory.setEditable(true);
//        flwpIndex01.setCellValueFactory(new PropertyValueFactory<>("tblrowxx01"));
//        flwpIndex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
//        flwpIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
//        flwpIndex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
//        flwpIndex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
//        flwpIndex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
//
//        tblFollowHistory.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblFollowHistory.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//
//        tblFollowHistory.setItems(followupdata);
//    }
//
//    /*Set TextField Value to Master Class*/
//    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
//        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
//        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
//        String lsValue = loTxtField.getText();
//        if (lsValue == null) {
//            return;
//        }
//        if (!nv) {
//            /*Lost Focus*/
//            switch (lnIndex) {
//                case 2: //
//                case 3: //
//                //case 4: //
//                //case 7: //
//                //case 9: //
//                case 12: //
//                case 13: //
//                case 14: //
//                case 17: //
//                case 18: //
//                case 21: //
//                case 24: //
//                case 29: //
//                case 30: //
//                case 31: //
//                case 32: //
//                case 33: //
//                    oTrans.setMaster(lnIndex, lsValue); //Handle Encoded Value
//                    break;
//                case 4: //
//                    oTrans.setMaster(34, lsValue); //Handle Encoded Value
//                    break;
//                case 9: //
//                    oTrans.setMaster(35, lsValue); //Handle Encoded Value
//                    break;
//                case 7: //
//                    oTrans.setMaster(29, lsValue); //Handle Encoded Value
//                    break;
//                case 15: //
//                    oTrans.setMaster(37, lsValue); //Handle Encoded Value
//                    break;
//
//            }
//        } else {
//            loTxtField.selectAll();
//
//        }
//    };
//
//    /*Set TextArea to Master Class*/
//    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
//        TextArea loTxtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
//        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
//        String lsValue = loTxtField.getText();
//
//        if (lsValue == null) {
//            return;
//        }
//        if (!nv) {
//            /*Lost Focus*/
//            switch (lnIndex) {
//                case 8:
//                    oTrans.setMaster(lnIndex, lsValue);
//                    break;
//            }
//        } else {
//            loTxtField.selectAll();
//        }
//    };
//
//    /*Set ComboBox Value to Master Class*/
//    @SuppressWarnings("ResultOfMethodCallIgnored")
//    private boolean setSelection() {
//        if (rdbtnHtA11.isSelected()) {
//            oTrans.setMaster(11, "a");
//        } else if (rdbtnHtB11.isSelected()) {
//            oTrans.setMaster(11, "b");
//        } else if (rdbtnHtC11.isSelected()) {
//            oTrans.setMaster(11, "c");
//        }
//        if (!rdbtnNew05.isSelected() && !rdbtnPro05.isSelected()) {
//            //ShowMessageFX.Warning("No `Vehicle Category` selected.", pxeModuleName, "Please select `Vehicle Category` value.");
//            ShowMessageFX.Warning(getStage(), "Please select `Vehicle Category` value.", pxeModuleName, null);
//            return false;
//        } else {
//            if (rdbtnNew05.isSelected()) {
//                oTrans.setMaster(5, 0);
//            } else if (rdbtnPro05.isSelected()) {
//                oTrans.setMaster(5, 1);
//            }
//        }
//        if (cmbType012.getSelectionModel().getSelectedIndex() < 0) {
//            ShowMessageFX.Warning(getStage(), "Please select `Inquiry Type` value.", pxeModuleName, null);
//            cmbType012.requestFocus();
//            return false;
//        } else {
//            oTrans.setMaster(12, String.valueOf(cmbType012.getSelectionModel().getSelectedIndex()));
//        }
//        //            if (cmbType012.getSelectionModel().getSelectedIndex() == 1){
////                if (cmbOnstr13.getSelectionModel().getSelectedIndex() < 0){
////                    ShowMessageFX.Warning("No `Online Store` selected.", pxeModuleName, "Please select `Online Store` value.");
////                    cmbOnstr13.requestFocus();
////                    return false;
////                }else
////                   oTrans.setMaster(13, String.valueOf(cmbOnstr13.getSelectionModel().getSelectedIndex()));
////            }
//        if (cmbType012.getSelectionModel().getSelectedIndex() == 3) {
//            if (txtField09.getText().equals("") || txtField09.getText() == null) {
//                //ShowMessageFX.Warning("No `Refferal Agent` selected.", pxeModuleName, "Please select `Refferal Agent` value.");
//                ShowMessageFX.Warning(getStage(), "Please select `Refferal Agent` value.", pxeModuleName, null);
//                txtField09.requestFocus();
//                return false;
//            }
//        } else if (cmbType012.getSelectionModel().getSelectedIndex() == 4 || cmbType012.getSelectionModel().getSelectedIndex() == 5) {
//            if (txtField15.getText().equals("") || txtField15.getText() == null) {
//                //ShowMessageFX.Warning("No `Event` selected.", pxeModuleName, "Please select `Event` value.");
//                ShowMessageFX.Warning(getStage(), "Please select `Event` value.", pxeModuleName, null);
//                txtField15.requestFocus();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /*Convert Date to String*/
//    private LocalDate strToDate(String fsVal) {
//        DateTimeFormatter loDate_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate loLocalDate = LocalDate.parse(fsVal, loDate_formatter);
//        return loLocalDate;
//    }
//
//    /*Set Date Value to Master Class*/
//    public void getDate(ActionEvent event) {
//        oTrans.setMaster(10, SQLUtil.toDate(txtField10.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
//    }
//
//    private Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
//        @Override
//        public DateCell call(final DatePicker param) {
//            return new DateCell() {
//                @Override
//                public void updateItem(LocalDate foItem, boolean fbEmpty) {
//                    super.updateItem(foItem, fbEmpty); //To change body of generated methods, choose Tools | Templates.
//                    LocalDate today = LocalDate.now();
//                    setDisable(fbEmpty || foItem.compareTo(today) < 0);
//                }
//
//            };
//        }
//
//    };
//
//    /*Enabling / Disabling Fields*/
// /*INQUIRY ENTRY MAIN*/
//    private void initButton(int fnValue) {
//        pnRow = 0;
//        /* NOTE:
//             lbShow (FALSE)=F invisible
//             !lbShow (TRUE)= visible
//         */
//        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
//
//        /*Inquiry Entry*/
//        txtField04.setDisable(!lbShow); // Sales Executive
//        if (fnValue == EditMode.ADDNEW) {
//            /*Enable / Disable Selecting Branch
//            * if branch == main office >> Enable selecting Branch
//            * else not main office >> Disable selecting Branch
//             */
//            if (oApp.isMainOffice()) {
//                txtField02.setDisable(false); // Branch Name
//            } else {
//                txtField02.setDisable(true); // Branch Name
//            }
//        } else {
//            txtField02.setDisable(true); // Branch Name
//        }
//
//        txtField07.setDisable(true); //Customer ID
//        txtField29.setDisable(true); //Company ID
//        textArea08.setDisable(!lbShow); //Remarks
//        cmbType012.setDisable(!lbShow); //Inquiry Type
//        txtField14.setDisable(!lbShow); //Test Model
//        textArea33.setDisable(true); //Client Address
//        txtField10.setDisable(!lbShow); //Target Delivery
//        txtField41.setDisable(!lbShow); // Contact person
//        //Radio Button Toggle Group
//        rdbtnHtA11.setDisable(!lbShow);
//        rdbtnHtB11.setDisable(!lbShow);
//        rdbtnHtC11.setDisable(!lbShow);
//        rdbtnNew05.setDisable(!lbShow);
//        rdbtnPro05.setDisable(!lbShow);
//        //Enable or Disable fields for online store, agent, activity based of selected inquiry type
//        switch (cmbType012.getSelectionModel().getSelectedIndex()) {
//            case 1:
////                cmbOnstr13.setDisable(!lbShow);
//                txtField13.setDisable(!lbShow);
//                txtField15.setText("");
//                txtField09.setText("");
//                break;
//            case 3:
//                txtField09.setDisable(!lbShow);
//                txtField15.setText("");
//                txtField13.setText("");
//                //cmbOnstr13.setValue(null);
//                break;
//            case 4:
//            case 5:
//                txtField15.setDisable(!lbShow);
//                txtField09.setText("");
//                txtField13.setText("");
//                //cmbOnstr13.setValue(null);
//                break;
//            default:
//                txtField15.setDisable(true); //Activity ID
//                txtField09.setDisable(true); //Agent ID
//                txtField13.setDisable(true); //Online Store
//            //cmbOnstr13.setDisable(true); //Online Store
//        }
//        //Inquiry button
//        btnTargetVhclAdd.setVisible(lbShow);
//        btnTargetVhclRemove.setVisible(lbShow);
//        btnTargetVehicleUp.setVisible(lbShow);
//        btnTargetVehicleDown.setVisible(lbShow);
//        btnPromosAdd.setVisible(lbShow);
//        btnPromosRemove.setVisible(lbShow);
//        //Inquiry General Button
//        btnAdd.setVisible(!lbShow);
//        btnAdd.setManaged(!lbShow);
//        btnSave.setVisible(lbShow);
//        btnSave.setManaged(lbShow);
//        btnEdit.setVisible(false);
//        btnEdit.setManaged(false);
//        btnConvertSales.setVisible(false);
//        btnConvertSales.setManaged(false);
//        btnPrintRefund.setVisible(false);
//        btnPrintRefund.setManaged(false);
//        btnLostSale.setVisible(false);
//        btnLostSale.setManaged(false);
//        btnClear.setVisible(false);
//        btnClear.setManaged(false);
//        btnCancel.setVisible(lbShow);
//        btnCancel.setManaged(lbShow);
//        //Bank Application
//        btnBankAppNew.setVisible(false);
//        btnBankAppUpdate.setVisible(false);
//        btnBankAppCancel.setVisible(false);
//        btnBankAppView.setVisible(false);
//        bankCheck01.setVisible(false);
//        //For Follow up
//        btnFollowUp.setVisible(false);
//
//        boolean lbTab = (fnValue == EditMode.READY);
//        //tabCustomerInquiry.setDisable(!lbTab);
//        tabInquiryProcess.setDisable(!lbTab);
//        tabBankHistory.setDisable(!lbTab);
//        tabFollowingHistory.setDisable(!lbTab);
//
//        if (fnValue == EditMode.ADDNEW) {
//            btnClear.setVisible(lbShow);
//            btnClear.setManaged(lbShow);
//            txtField07.setDisable(!lbShow); //Customer ID
//            txtField29.setDisable(!lbShow); //Company ID
//        }
//        lblInqStatus.setText("");
//        if (comboBox24.getSelectionModel().getSelectedIndex() >= 0) {
//            lblInqStatus.setText(comboBox24.getValue().toString());
//        }
//        if (fnValue == EditMode.READY) { //show edit if user clicked save / browse
//            btnEdit.setVisible(true);
//            btnEdit.setManaged(true);
//            //Enable Button / textfield based on Inquiry Status
//            switch (comboBox24.getSelectionModel().getSelectedIndex()) { // cTranStat
//                case 0://For Follow up
//                    btnLostSale.setVisible(true);
//                    btnLostSale.setManaged(true);
//                    btnFollowUp.setVisible(true);
//                    break;
//                case 1: //On process
//                    btnLostSale.setVisible(true);
//                    btnLostSale.setManaged(true);
//                    if (cmbInqpr01.getSelectionModel().getSelectedIndex() >= 0) {
//                        btnConvertSales.setVisible(true);
//                        btnConvertSales.setManaged(true);
//                    }
//                    if (cmbInqpr01.getSelectionModel().getSelectedIndex() > 0) {
//                        //Bank Application
//                        btnBankAppNew.setVisible(true);
//                        btnBankAppUpdate.setVisible(true);
//                        btnBankAppCancel.setVisible(true);
//                        btnBankAppView.setVisible(true);
//                        bankCheck01.setVisible(true);
//                    }
//                    //For Follow up
//                    btnFollowUp.setVisible(true);
//                    break;
//                case 3: //VSP
//                    //Bank Application
//                    btnBankAppNew.setVisible(true);
//                    btnBankAppUpdate.setVisible(true);
//                    btnBankAppCancel.setVisible(true);
//                    btnBankAppView.setVisible(true);
//                    bankCheck01.setVisible(true);
//                    //For Follow up
//                    btnFollowUp.setVisible(true);
//                    break;
//                case 2: //Lost Sale
//                    btnPrintRefund.setVisible(true);
//                    btnPrintRefund.setManaged(true);
//                    btnEdit.setVisible(false);
//                    btnEdit.setManaged(false);
//                    break;
//                case 5: //Retired
//                    tabInquiryProcess.setDisable(true);
//                    tabBankHistory.setDisable(true);
//                    btnFollowUp.setVisible(true);
//                    btnEdit.setVisible(false);
//                    btnEdit.setManaged(false);
//                    break;
//                case 4: //Sold
//                case 6: //Cancelled
//                    btnEdit.setVisible(false);
//                    btnEdit.setManaged(false);
//                    break;
//            }
//        }
//
//        if (pnSelectedIndex == 1) {
//            btnAdd.setVisible(false);
//            btnAdd.setManaged(false);
//            btnEdit.setVisible(false);
//            btnEdit.setManaged(false);
//            btnSave.setVisible(false);
//            btnSave.setManaged(false);
//            btnClear.setVisible(false);
//            btnClear.setManaged(false);
//            btnCancel.setVisible(false);
//            btnCancel.setManaged(false);
//        } else {
//            btnProcess.setVisible(false);
//            btnProcess.setManaged(false);
//            btnModify.setVisible(false);
//            btnModify.setManaged(false);
//            btnApply.setVisible(false);
//            btnApply.setManaged(false);
//        }
//
//    }
//
//    /*INQUIRY PROCESS*/
//    private void initBtnProcess(int fnValue) {
//        pnRow = 0;
//        /* NOTE:
//            lbShow (FALSE)= invisible
//            !lbShow (TRUE)= visible
//         */
//        boolean lbShow = (fnValue == EditMode.UPDATE);
//
//        if (comboBox24.getSelectionModel().getSelectedIndex() == 0) {
//            rqrmIndex01.setVisible(true);
//        } else {
//            rqrmIndex01.setVisible(lbShow);
//        }
//        /*INQUIRY PROCESS*/
//        //Requirements
//        cmbInqpr01.setDisable(!lbShow);
//        switch (cmbInqpr01.getSelectionModel().getSelectedIndex()) {
//            case 0: //CASH
//            case 1: // Bank Purchase order
//            case 3: // Company Purchase Order
//                cmbInqpr02.setDisable(true);
//                break;
//            case 2: //Bank Financing
//            case 4: //Company Financing
//                cmbInqpr02.setDisable(!lbShow);
//                break;
//        }
////        if (cmbInqpr01.getSelectionModel().getSelectedIndex() == 1){
////            cmbInqpr02.setDisable(true);
////        } else {
////            cmbInqpr02.setDisable(!lbShow);
////        }
//        //Approved by
//        txtField21.setDisable(!lbShow);
//        //Reservation
//        btnASadd.setVisible(lbShow);
//        btnASremove.setVisible(lbShow);
//        btnAScancel.setVisible(lbShow);
//        btnASprint.setVisible(lbShow);
//        //General button
//        btnProcess.setVisible(false);
//        btnProcess.setManaged(false);
//        btnModify.setVisible(false);
//        btnModify.setManaged(false);
//        btnApply.setVisible(lbShow);
//        btnApply.setManaged(lbShow);
//
//        if (fnValue == EditMode.READY || (lbShow)) {
//            btnAScancel.setVisible(true);
//            btnASprint.setVisible(true);
//
//            btnModify.setVisible(fnValue == EditMode.READY);
//            btnModify.setManaged(fnValue == EditMode.READY);
//
//            //Enable Button / textfield based on Inquiry Status
//            switch (comboBox24.getSelectionModel().getSelectedIndex()) { // cTranStat
//                case 0://For Follow up
//
//                    rqrmIndex01.setVisible(true);
//                    //Requirements
//                    cmbInqpr01.setDisable(false);
//                    cmbInqpr02.setDisable(false);
//                    //Approved by
//                    txtField21.setDisable(false);
//                    //Reservation
//                    btnASadd.setVisible(true);
//                    btnASremove.setVisible(true);
//                    btnAScancel.setVisible(true);
//                    btnASprint.setVisible(true);
//
//                    //General button
//                    btnProcess.setVisible(true);
//                    btnProcess.setManaged(true);
//                    btnModify.setVisible(false);
//                    btnModify.setManaged(false);
//                    break;
////                    case 1: //On process
////                    case 3: //VSP
////                         //Requirements
////                         cmbInqpr01.setDisable(false);
////                         cmbInqpr02.setDisable(false);
////                         //Approved by
////                         txtField21.setDisable(false);
////                         //Reservation
////                         btnASadd.setVisible(true);
////                         btnASremove.setVisible(true);
////                         btnAScancel.setVisible(true);
////                         btnASprintview.setVisible(true);
////                         btnASprint.setVisible(true);
////
////                         //General button
////                         btnProcess.setVisible(true);
////                         btnModify.setVisible(false);
////                         btnModify.setManaged(false);
////                         btnApply.setVisible(false);
////                         btnApply.setManaged(false);
////                         break;
//                case 5: //Retired
//                case 2: //Lost Sale
//                case 4: //Sold
//                case 6: //Cancelled
//                    //Requirements
//                    cmbInqpr01.setDisable(true);
//                    cmbInqpr02.setDisable(true);
//                    //Approved by
//                    txtField21.setDisable(true);
//                    //Reservation
//                    btnASadd.setVisible(false);
//                    btnASremove.setVisible(false);
//                    btnAScancel.setVisible(false);
//                    btnASprint.setVisible(false);
//
//                    //General button
//                    btnProcess.setVisible(false);
//                    btnModify.setVisible(false);
//                    btnModify.setManaged(false);
//                    btnApply.setVisible(false);
//                    btnApply.setManaged(false);
//                    break;
//            }
//
//        }
//
//    }
//
//    /*Clear Class Value*/
//    private void clearClassFields() {
//        //Class Master
//        for (pnCtr = 1; pnCtr <= 38; pnCtr++) {
//            switch (pnCtr) {
//                case 2: //
//                //case 4: //
//                //case 7: //
//                //case 9: //
//                case 14: //
//                case 21: //
//                case 29: //
//                case 30: //
//                case 31: //
//                case 32: //
//                case 33: //
//                    oTrans.setMaster(pnCtr, ""); //Handle Encoded Value
//                    break;
//                case 4: //
//                    oTrans.setMaster(34, ""); //Handle Encoded Value
//                    break;
//                case 9: //
//                    oTrans.setMaster(35, ""); //Handle Encoded Value
//                    break;
//                case 7: //
//                    oTrans.setMaster(29, ""); //Handle Encoded Value
//                    break;
//                case 13:
//                    oTrans.setMaster(36, "");
//                    break;
//                case 15:
//                    oTrans.setMaster(37, ""); //Handle Encoded Value
//                    break;
//                case 38:
//                    oTrans.setMaster(38, ""); //Handle Encoded Value
//                    break;
////                    case 39:
////                        oTrans.setMaster(39, ""); //Handle Encoded Value
////                        break;
//                case 11:
//                    oTrans.setMaster(pnCtr, "a"); //Handle Encoded Value
//                    break;
//                case 10:
//                    //oTrans.setMaster(10,SQLUtil.toDate("1/1/1900", SQLUtil.FORMAT_SHORT_DATE));
//                    //oTrans.setMaster(pnCtr, LocalDate.of(1900, Month.JANUARY, 1));
//                    oTrans.setMaster(pnCtr, oApp.getServerDate());
//                    break;
//                case 5:
//                case 12:
//                    oTrans.setMaster(pnCtr, "0");
//                    break;
//            }
//        }
//        //Class Priority Unit
//        do {
//            oTrans.removeTargetVehicle(oTrans.getVhclPrtyCount());
//        } while (oTrans.getVhclPrtyCount() != 0);
//        //Class Promo Offered
//        do {
//            oTrans.removeInqPromo(oTrans.getInqPromoCount());
//        } while (oTrans.getInqPromoCount() != 0);
//
//        /*Clear Red Color for required fileds*/
//        txtField04.getStyleClass().remove("required-field");
//        txtField07.getStyleClass().remove("required-field");
//    }
//
//    //Method for clearing Fields
//    public void clearFields() {
//        pnRow = 0;
//        pnSelectedTblRowIndex = 0;
//        /*Inquiry*/
//        txtField02.clear(); //Branch Code
//        txtField03.clear();//Inqiury Date
//        txtField04.clear(); // Sales Executive
//        txtField07.clear(); //Customer ID
//        txtField29.clear(); //Company ID
//        txtField30.clear(); //Contact Number
//        txtField31.clear(); //Social Media
//        txtField32.clear(); //Email
//        textArea33.clear(); //Client Address
//        txtField09.clear(); //Agent ID
//        textArea08.clear(); //Remarks
//        txtField41.clear(); //contact person
//        comboBox24.setValue(null);  //Inquiry Status
//        txtField18.clear(); //Reserve Amount
//        txtField17.clear(); //Reserved
////        cmbOnstr13.setValue(null);
//        txtField13.clear();//Online Store
//        cmbType012.setValue(null); //Inquiry Type
//        txtField15.clear(); //Activity ID
//        txtField14.clear(); //Test Model
//        txtField41.setVisible(false);
//        lblContactPrsn.setVisible(false);
//        hotCategory.selectToggle(null);//Radio Button Toggle Group
//        targetVehicle.selectToggle(null);//Radio Button Toggle Group
//        promosoffereddata.clear();
//        priorityunitdata.clear();
//
//        /*Inquiry Process*/
//        txtField21.clear(); //Approved By
//    }
    }
}
