package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.controllers.service.TechnicianServiceController;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.sales.Part;
import org.guanzon.autoapp.models.service.TechnicianLabor;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class SalesJobOrderController implements Initializable, ScreenInterface, GTransactionInterface {

    private JobOrder oTrans;
    private GRider oApp;
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    private String psVSPTransNo = "";
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private int pnRow = -1;
    private boolean pbIsVSP = false;
    private final String pxeModuleName = "Sales Job Order"; //Form Title
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private ObservableList<Part> accessoriesData = FXCollections.observableArrayList();
    private ObservableList<TechnicianLabor> techData = FXCollections.observableArrayList();

    @FXML
    AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle, lblJobOrderStatus, lblCompleteStatus;

    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCancelJobOrder, btnClose, btnAddLabor, btnAddAccessories, btnDone, btnAddTechnician;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Tab tabMain, laborTab, accessoriesTab, technicianTab, accessoriesMatTab, issuanceTab;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06, txtField07, txtField09, txtField10, txtField11, txtField12, txtField13;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private TextArea textArea14, textArea08, textArea15;
    @FXML
    private TableView<Labor> tblViewLabor;
    @FXML
    private TableColumn<Labor, String> tblindex01_labor;
    @FXML
    private TableColumn<Labor, String> tblindex02_labor, tblindex03_labor, tblindex04_labor, tblindex05_labor;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableView<TechnicianLabor> tblViewTechnician;
    @FXML
    private TableColumn<TechnicianLabor, String> tblindex01_tech, tblindex02_tech, tblindex03_tech;
    @FXML
    private TableView<?> tblViewPaintings;
    @FXML
    private TableView<?> tblViewPaintings1;
    @FXML
    private TableColumn<Part, String> tblindex01_part, tblindex02_part, tblindex03_part, tblindex04_part, tblindex05_part, tblindex06_part;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setIsVSPState(boolean fbValue) {
        pbIsVSP = fbValue;
    }

    public void setVSPTrans(String fsValue) {
        psVSPTransNo = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new JobOrder(oApp, false, oApp.getBranchCode());
        initLaborTable();
        initTechnician();
        initAccessoriesTable();

        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

        Platform.runLater(() -> {
            if (pbIsVSP) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
                loJSON = oTrans.searchVSP(psVSPTransNo, true);
                if (!"error".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    clearFields();
                }
            }
        });
        initTableKeyPressed();
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField07,
                txtField09, txtField10, txtField11, txtField12, txtField13);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea08, textArea14, textArea15);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.computeAmount();
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getVSPNo());
        txtField02.setText(oTrans.getMasterModel().getMasterModel().getDSNo());
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker03.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTransactDte())));
        }
        txtField04.setText(poGetDecimalFormat.format(oTrans.getMasterModel().getMasterModel().getLaborAmt()));
        txtField05.setText(poGetDecimalFormat.format(oTrans.getMasterModel().getMasterModel().getPartsAmt()));
        txtField06.setText(poGetDecimalFormat.format(oTrans.getMasterModel().getMasterModel().getTranAmt()));
        txtField07.setText(oTrans.getMasterModel().getMasterModel().getOwnrNm());
        textArea08.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField09.setText(oTrans.getMasterModel().getMasterModel().getCoBuyrNm());
        txtField10.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
        txtField11.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
        txtField12.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
        txtField13.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
        textArea14.setText(oTrans.getMasterModel().getMasterModel().getVhclDesc());
        textArea15.setText(oTrans.getMasterModel().getMasterModel().getRemarks());
        String lsStatus = "";
        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lsStatus = "Active";
                break;
            case TransactionStatus.STATE_CLOSED:
                lsStatus = "Completed";
                break;
            case TransactionStatus.STATE_CANCELLED:
                lsStatus = "Cancelled";
                break;
            case TransactionStatus.STATE_POSTED:
                lsStatus = "Posted";
                break;
        }
        lblJobOrderStatus.setText(lsStatus);
        String lsDateCompleted = "";
        if (oTrans.getMasterModel().getMasterModel().getCompleteDte() != null) {
            if (!CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getCompleteDte()).equals("1900-01-01")) {
                lsDateCompleted = CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getCompleteDte());
            }
        }
        lblCompleteStatus.setText(lsDateCompleted);
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
        textArea15.focusedProperty().addListener(txtArea_Focus);
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
                case 15:
                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    public void initTextKeyPressed() {
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
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
                        case "txtField01":
                            loJSON = oTrans.searchVSP(lsValue.trim(), false);
                            if (!"error".equals(loJSON.get("result"))) {
                                loadMasterFields();
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
                            break;
                    }
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    initFields(pnEditMode);
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

    }

    @Override
    public void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnAddTechnician, btnCancelJobOrder, btnClose, btnDone, btnAddLabor, btnAddAccessories);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                CustomCommonUtil.switchToTab(tabMain, ImTabPane);
                oTrans = new JobOrder(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    oTrans.getMasterModel().getMasterModel().setJobType("0");
                    oTrans.getMasterModel().getMasterModel().setPaySrce("3");
                    oTrans.getMasterModel().getMasterModel().setWorkCtgy("2");
                    oTrans.getMasterModel().getMasterModel().setLaborTyp("");
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
//            case "btnSave":
//                if (ShowMessageFX.YesNo(null, "Sales Job Order Information Saving....", "Are you sure, do you want to save?")) {
//                    String lsJOLaborCode = "";
//                    for (int lnCtr = 0; lnCtr <= oTrans.getVSPLaborList().size() - 1; lnCtr++) {
//                        lsJOLaborCode = oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborCde();
//                    }
//                    String lsTechLaborCode = "";
//                    for (int lnCtr = 0; lnCtr <= oTrans.getJOTechList().size() - 1; lnCtr++) {
//                        lsTechLaborCode = oTrans.getJOTechModel().getDetailModel(lnCtr).getLaborCde();
//                    }
//
//                    loJSON = oTrans.saveTransaction();
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        ShowMessageFX.Information(null, "Sales Job Order Information", (String) loJSON.get("message"));
//                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            CustomCommonUtil.switchToTab(tabMain, ImTabPane);
//                            loadMasterFields();
//                            loadLaborTable();
//                            loadAccessoriesTable();
//                            pnEditMode = oTrans.getEditMode();
//                            initFields(pnEditMode);
//                        }
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                    }
//                }
//                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Sales Job Order Information Saving....", "Are you sure, do you want to save?")) {

                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Job Order Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            CustomCommonUtil.switchToTab(tabMain, ImTabPane);
                            loadMasterFields();
                            loadLaborTable();
                            loadAccessoriesTable();
                            loadTechnician();
                            pnEditMode = oTrans.getEditMode();
                            initFields(pnEditMode);
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    CustomCommonUtil.switchToTab(tabMain, ImTabPane);
                    oTrans = new JobOrder(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Sales Job Order Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    CustomCommonUtil.switchToTab(tabMain, ImTabPane);
                    loadMasterFields();
                    loadLaborTable();
                    loadAccessoriesTable();
                    loadTechnician();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Sales Job Order Information", (String) loJSON.get("message"));
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
            case "btnPrint":
//                loadSJOPrint();
                break;
            case "btnCancelJobOrder":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this SJO?")) {
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadLaborTable();
                        loadAccessoriesTable();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnDone":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to complete this SJO?")) {
                    oTrans.getMasterModel().getMasterModel().setTranStat(TransactionStatus.STATE_CLOSED);
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, "SJO completed successfully.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadLaborTable();
                        loadAccessoriesTable();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;

            case "btnAddLabor":
                try {
                loadLaborWindowDialog();
            } catch (IOException ex) {
                Logger.getLogger(SalesJobOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnAddAccessories":
                try {
                loadPartsWindowDialog();
            } catch (IOException ex) {
                Logger.getLogger(SalesJobOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnAddTechnician":
                loJSON = oTrans.searchTechnician();
                if (!"error".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadTechnician();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
    }

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getMasterModel().getMasterModel().setVSPNo("");
                        clearFieldsNoVS();
                    }
                }
                initFields(pnEditMode);
            }
        });
    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void clearTables() {
        laborData.clear();
        accessoriesData.clear();
        techData.clear();
    }

    private void clearFieldsNoVS() {
        CustomCommonUtil.setText("", txtField02, txtField07, txtField09,
                txtField10, txtField11, txtField12, txtField13);
        datePicker03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        CustomCommonUtil.setText("0.00", txtField04, txtField05, txtField06);
        CustomCommonUtil.setText("", textArea14, textArea15);
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField02, txtField07,
                txtField10, txtField11, txtField12, txtField13);
        datePicker03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        CustomCommonUtil.setText("0.00", txtField04, txtField05, txtField06);
        CustomCommonUtil.setText("", textArea08, textArea14, textArea15);
        CustomCommonUtil.setText("", lblJobOrderStatus, lblCompleteStatus);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        CustomCommonUtil.setDisable(!lbShow, txtField01, textArea15);

        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnCancelJobOrder, btnDone);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnCancelJobOrder, btnDone);
        laborTab.setDisable(txtField01.getText().isEmpty());
        accessoriesTab.setDisable(txtField01.getText().isEmpty());
        technicianTab.setDisable(txtField01.getText().isEmpty());
        accessoriesMatTab.setDisable(true);
        issuanceTab.setDisable(true);
        btnAddLabor.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        btnAddAccessories.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        btnAddTechnician.setDisable(true);
        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
        if (fnValue == EditMode.READY) {
            if (!oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CANCELLED)) {
                CustomCommonUtil.setVisible(true, btnEdit, btnCancelJobOrder, btnDone);
                CustomCommonUtil.setManaged(true, btnEdit, btnCancelJobOrder, btnDone);
            }
            if (oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CLOSED)) {
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                CustomCommonUtil.setVisible(false, btnEdit, btnDone);
                CustomCommonUtil.setManaged(false, btnEdit, btnDone);
            }
        }
        if (!tblViewLabor.getItems().isEmpty()) {
            btnAddTechnician.setDisable(!lbShow);
        }
    }

    private void loadLaborTable() {
        laborData.clear();
        boolean lbAdditional = false;
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsDiscAmount = "";
        String lsNetAmount = "";
        String lsJoNoxx = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getJOLaborList().size() - 1; lnCtr++) {
            if (oTrans.getJOLaborModel().getDetailModel(lnCtr).getUnitPrce() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getJOLaborModel().getDetailModel(lnCtr).getUnitPrce())));
            }

            if (oTrans.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
                lbChargeType = true;
            }
            laborData.add(new Labor(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTrans.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
                    String.valueOf(oTrans.getJOLaborModel().getDetailModel(lnCtr).getLaborCde()),
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    String.valueOf(oTrans.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc()),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    lsJoNoxx,
                    lbAdditional,
                    lbChargeType
            ));
            lbAdditional = false;
            lbChargeType = false;
            lsGrsAmount = "";
            lsDiscAmount = "";
            lsNetAmount = "";
            lsJoNoxx = "";
        }
        tblViewLabor.setItems(laborData);
    }

    private void initLaborTable() {
        tblindex01_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex03_labor"));
        tblindex03_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void loadLaborWindowDialog() throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/JOVSPLabor.fxml"));
//
            JOVSPLaborController loControl = new JOVSPLaborController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setTrans(oTrans.getMasterModel().getMasterModel().getTransNo());
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
            loadLaborTable();
            loadMasterFields();
            initFields(pnEditMode);
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private void loadAccessoriesTable() {
        accessoriesData.clear();
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsQuantity = "";
        String lsDiscAmount = "";
        String lsTotalAmount = "";
        String lsNetAmount = "";
        String lsPartsDesc = "";
        String lsBarCode = "";
        String lsJoNoxx = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getJOPartsList().size() - 1; lnCtr++) {
            if (oTrans.getJOPartsModel().getJOParts(lnCtr).getDescript() != null) {
                lsPartsDesc = String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getDescript());
            }
            if (oTrans.getJOPartsModel().getJOParts(lnCtr).getUnitPrce() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getUnitPrce())));
            }
            if (oTrans.getJOPartsModel().getJOParts(lnCtr).getQtyEstmt() != null) {
                lsQuantity = String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getQtyEstmt());
            }
            if (oTrans.getJOPartsModel().getJOParts(lnCtr).getPayChrge().equals("0")) {
                lbChargeType = true;
            }
            if (oTrans.getJOPartsModel().getJOParts(lnCtr).getBarCode() != null) {
                lsBarCode = String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getBarCode());
            }
            accessoriesData.add(new Part(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getTransNo()),
                    String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getStockID()),
                    String.valueOf(oTrans.getJOPartsModel().getJOParts(lnCtr).getDescript()),
                    lsQuantity,
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    lsBarCode,
                    "",
                    lsJoNoxx,
                    lsPartsDesc,
                    lsTotalAmount,
                    lbChargeType
            ));
            lbChargeType = false;
            lsGrsAmount = "";
            lsQuantity = "";
            lsDiscAmount = "";
            lsTotalAmount = "";
            lsNetAmount = "";
            lsPartsDesc = "";
            lsBarCode = "";
            lsJoNoxx = "";
        }
        tblViewAccessories.setItems(accessoriesData);
    }

    private void initAccessoriesTable() {
        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblViewAccessories.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void loadPartsWindowDialog() throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/JOVSPAccessories.fxml"));

            JOVSPAccessoriesController loControl = new JOVSPAccessoriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setTrans(oTrans.getMasterModel().getMasterModel().getTransNo());
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
            loadAccessoriesTable();
            loadMasterFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private Labor getLaborSelectedItem() {
        return tblViewLabor.getSelectionModel().getSelectedItem();
    }

    private Part getPartSelectedItem() {
        return tblViewAccessories.getSelectionModel().getSelectedItem();
    }

    private TechnicianLabor getTechSelectedItem() {
        return tblViewTechnician.getSelectionModel().getSelectedItem();
    }

    private void initTableKeyPressed() {
        tblViewLabor.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    Labor selectedVSPLabor = getLaborSelectedItem();
                    if (selectedVSPLabor != null) {
                        // Get the labor code to be deleted
                        String laborCodeToRemove = selectedVSPLabor.getTblindex03_labor();
                        String laborDesc = selectedVSPLabor.getTblindex07_labor();
                        // Validate if the labor code is assigned to any technician
                        boolean isAssignedToTechnician = false;
                        for (int lnCtr = 0; lnCtr <= oTrans.getJOTechList().size() - 1; lnCtr++) {
                            String technicianLaborCode = oTrans.getJOTechModel().getDetailModel(lnCtr).getLaborCde();
                            if (technicianLaborCode.equals(laborCodeToRemove)) {
                                isAssignedToTechnician = true;
                                break;
                            }
                        }

                        // If labor code is assigned to a technician, show a warning and do not proceed with removal
                        if (isAssignedToTechnician) {
                            ShowMessageFX.Warning(null, pxeModuleName,
                                    "Cannot remove labor description: " + laborDesc + " because it is assigned to a technician.");
                            return; // Exit method to prevent deletion
                        }

                        // Confirm removal from the user
                        if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this labor?")) {
                            String lsRow = selectedVSPLabor.getTblindex01_labor();
                            int lnRow = Integer.parseInt(lsRow);

                            // Remove the labor from oTrans
                            oTrans.removeJOLabor(lnRow - 1);
                            ShowMessageFX.Information(null, pxeModuleName, "Removed labor successfully");

                            // Refresh the fields and tables
                            loadMasterFields();
                            loadTechnician();
                            loadAccessoriesTable();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "No labor selected to remove.");
                    }
                }
                initFields(pnEditMode);
            }
        });

        tblViewAccessories.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this accessories?")) {
                        Part selectedVSPPart = getPartSelectedItem();
                        int removeCount = 0;
                        if (selectedVSPPart != null) {
                            String lsRow = selectedVSPPart.getTblindex01_part();
                            int lnRow = Integer.parseInt(lsRow);
                            oTrans.removeJOParts(lnRow - 1);
                            removeCount++;
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Removed accessories successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Removed accessories failed");
                        }
                        loadMasterFields();
                        loadTechnician();
                        loadAccessoriesTable();
                    }
                } else {
                }
            }
        });
        tblViewTechnician.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this technician?")) {
                        TechnicianLabor selectedTech = getTechSelectedItem();
                        int removeCount = 0;
                        if (selectedTech != null) {
                            String lsRow = selectedTech.getTblindex01_tech();
                            int lnRow = Integer.parseInt(lsRow);
                            oTrans.removeJOTech(lnRow - 1);
                            removeCount++;
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Removed technician successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Removed technician failed");
                        }
                        loadMasterFields();
                        loadTechnician();
                        loadAccessoriesTable();
                    }
                } else {
                }

            }
        });
    }

    @FXML
    private void tblViewLabor_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewLabor.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewLabor.getItems().size()) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid labor information.");
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadLaborWindowDialog(pnRow, true, false);

                } catch (IOException ex) {
                    Logger.getLogger(SalesJobOrderController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void tblViewAccessories_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewAccessories.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewAccessories.getItems().size()) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid accessories information.");
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadAccessoriesWindowDialog(pnRow, true, false);

                } catch (IOException ex) {
                    Logger.getLogger(SalesJobOrderController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void loadLaborWindowDialog(Integer fnRow, boolean fbIsVSPJo, boolean fbIsAdd) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/JOLabor.fxml"));
            JOLaborController loControl = new JOLaborController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setIsVSPJo(fbIsVSPJo);
            if (!oTrans.getJOLaborModel().getDetailModel(fnRow).getLaborDsc().isEmpty()) {
                loControl.setOrigDsc(String.valueOf(oTrans.getJOLaborModel().getDetailModel(fnRow).getLaborDsc()));
            }
            loControl.setState(fbIsAdd);
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
            loadLaborTable();
            loadMasterFields();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }

    private void loadAccessoriesWindowDialog(Integer fnRow, boolean fbIsVSPJO, boolean fbIsAdd) throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/JOAccessories.fxml"));

            JOAccessoriesController loControl = new JOAccessoriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setState(fbIsAdd);
            loControl.setIsVSPJo(fbIsVSPJO);

            loControl.setRequest(false);
            loControl.setRow(fnRow);
            if (oTrans.getJOPartsModel().getJOParts(fnRow).getDescript() != null) {
                loControl.setOrigDsc(String.valueOf(oTrans.getJOPartsModel().getJOParts(fnRow).getDescript()));
            }
            loControl.setStockID(String.valueOf(oTrans.getJOPartsModel().getJOParts(fnRow).getStockID()));
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
            loadAccessoriesTable();
            loadMasterFields();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }

    private void initTechnician() {
        tblindex01_tech.setCellValueFactory(new PropertyValueFactory<>("tblindex01_tech"));
        tblindex02_tech.setCellValueFactory(new PropertyValueFactory<>("tblindex03_tech"));
        tblindex03_tech.setCellValueFactory(new PropertyValueFactory<>("tblindex05_tech"));
        tblViewTechnician.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void loadTechnician() {
        techData.clear();
        String lsTechName = "";
        String lsTechID = "";
        String lsLaborCde = "";
        String lsLaborDesc = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getJOTechList().size() - 1; lnCtr++) {
            if (oTrans.getJOTechModel().getDetailModel(lnCtr).getTechID() != null) {
                lsTechID = oTrans.getJOTechModel().getDetailModel(lnCtr).getTechID();
            }
            if (oTrans.getJOTechModel().getDetailModel(lnCtr).getTechName() != null) {
                lsTechName = oTrans.getJOTechModel().getDetailModel(lnCtr).getTechName();
            }
            if (oTrans.getJOTechModel().getDetailModel(lnCtr).getLaborCde() != null) {
                lsLaborCde = oTrans.getJOTechModel().getDetailModel(lnCtr).getLaborCde();
            }
            if (oTrans.getJOTechModel().getDetailModel(lnCtr).getLaborDsc() != null) {
                lsLaborDesc = oTrans.getJOTechModel().getDetailModel(lnCtr).getLaborDsc();
            }
            techData.add(new TechnicianLabor(
                    String.valueOf(lnCtr + 1),
                    lsTechID.toUpperCase(),
                    lsTechName.toUpperCase(),
                    lsLaborCde.toUpperCase(),
                    lsLaborDesc.toUpperCase()));
            lsTechName = "";
            lsTechID = "";
            lsLaborCde = "";
            lsLaborDesc = "";
        }
        tblViewTechnician.setItems(techData);
    }

    private void loadTechnicianServiceWindowDialog(int fnRow) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/service/TechnicianService.fxml"));
            TechnicianServiceController loControl = new TechnicianServiceController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            loControl.setTrans(oTrans.getMasterModel().getMasterModel().getTransNo());
            loControl.setOrigDsc(oTrans.getJOTechModel().getDetailModel(fnRow).getLaborCde());
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
            loadTechnician();
            loadMasterFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    @FXML
    private void tblViewTechnicianClick(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewTechnician.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewTechnician.getItems().size()) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid technician information.");
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadTechnicianServiceWindowDialog(pnRow);

                } catch (IOException ex) {
                    Logger.getLogger(SalesJobOrderController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
