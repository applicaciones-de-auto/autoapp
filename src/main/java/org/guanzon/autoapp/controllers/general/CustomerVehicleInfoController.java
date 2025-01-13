package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Vehicle_Serial;
import org.guanzon.autoapp.controllers.parameters.VehicleDescriptionController;
import org.guanzon.autoapp.controllers.parameters.VehicleEngineFormatController;
import org.guanzon.autoapp.controllers.parameters.VehicleFrameFormatController;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.models.general.VehicleOwnerHistory;
import org.guanzon.autoapp.models.general.VehicleWarehouseHistory;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class CustomerVehicleInfoController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Vehicle_Serial oTrans;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private String pxeModuleName = ""; //Form Title
    private boolean pbisVhclSales = false;
    private int pnEditMode;
    private int pnRow = -1;
    private int lnCtr;
    private double xOffset = 0;
    private double yOffset = 0;
    private ObservableList<VehicleOwnerHistory> vhclOwnerHistoryData = FXCollections.observableArrayList();
    private ObservableList<VehicleWarehouseHistory> vhclWrhHistoryData = FXCollections.observableArrayList();
    ObservableList<String> cIsVhclStat = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED");
    ObservableList<String> cSoldStats = FXCollections.observableArrayList("NON SALES CUSTOMER", "AVAILABLE FOR SALE", "VSP", "SOLD");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle, lblSerailID, lblSerailIDValue;
    @FXML
    private HBox vBoxPurchasedSold;
    @FXML
    private AnchorPane anchorPurch, gridPurch, gridSold, anchorMisc, gridMisc;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnTransfer, btnClose, btnVhclAvl, btnVhclDesc, btnLocation, btnFrame, btnEngine;
    @FXML
    private TextArea textArea02, textArea04, textArea27;
    @FXML
    private TextField txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
            txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25, txtField13;
    @FXML
    private ComboBox<String> comboBox17, comboBox18;
    @FXML
    private DatePicker datePicker26;
    @FXML
    private TableView<VehicleOwnerHistory> tblViewVhclOwnHsty;
    @FXML
    private TableColumn<VehicleOwnerHistory, String> tblViewVhclOwnHsty01, tblViewVhclOwnHsty02, tblViewVhclOwnHsty03, tblViewVhclOwnHsty04, tblViewVhclOwnHsty05;
    @FXML
    private TableView<VehicleWarehouseHistory> tblViewVhclWrhHsty;
    @FXML
    private TableColumn<VehicleWarehouseHistory, String> tblViewVhclWrhHsty01, tblViewVhclWrhHsty02, tblViewVhclWrhHsty03, tblViewVhclWrhHsty04, tblViewVhclWrhHsty05;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Vehicle_Serial(oApp, false, oApp.getBranchCode());
        initOwnHistory();
        initWareHouseHistory();
        Platform.runLater(() -> {
            if (getParentTabTitle().contains("SALES")) {
                pbisVhclSales = true;
                initFields(pnEditMode);
            } else {
                pbisVhclSales = false;
            }
        });
        initCapitalizationFields();
        initPatternFields();
        initLimiterFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
                txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25, txtField13);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        CustomCommonUtil.setCapsLockBehavior(textArea02);
        CustomCommonUtil.setCapsLockBehavior(textArea04);
        CustomCommonUtil.setCapsLockBehavior(textArea27);
    }

    @Override
    public boolean loadMasterFields() {
        lblSerailID.setText("SERIAL ID: ");
        lblSerailIDValue.setText(oTrans.getModel().getModel().getSerialID());
        txtField01.setText(oTrans.getModel().getModel().getOwnerNmx());
        txtField03.setText(oTrans.getModel().getModel().getCOwnerNm());
        textArea02.setText(oTrans.getModel().getModel().getOwnerAdd());
        textArea04.setText(oTrans.getModel().getModel().getCOwnerAd());
        txtField05.setText(oTrans.getModel().getModel().getMakeDesc());
        txtField07.setText(oTrans.getModel().getModel().getModelDsc());
        txtField09.setText(oTrans.getModel().getModel().getTypeDesc());
        txtField06.setText(oTrans.getModel().getModel().getTransMsn());
        txtField08.setText(oTrans.getModel().getModel().getColorDsc());
        if (oTrans.getModel().getModel().getYearModl() == null) {
            txtField10.setText("");
        } else {
            txtField10.setText(String.valueOf(oTrans.getModel().getModel().getYearModl()));
        }
        if (oTrans.getModel().getModel().getPlateNo() != null) {
            txtField11.setText(oTrans.getModel().getModel().getPlateNo());
        } else {
            txtField11.setText("");
        }
        txtField13.setText(oTrans.getModel().getModel().getFrameNo());
        txtField15.setText(oTrans.getModel().getModel().getKeyNo());
        if (oTrans.getModel().getModel().getCSNo() != null) {
            txtField12.setText(oTrans.getModel().getModel().getCSNo());
        } else {
            txtField12.setText("");
        }
        txtField14.setText(oTrans.getModel().getModel().getEngineNo());
        txtField16.setText(oTrans.getModel().getModel().getLocation());
//        txtField19.setText(oTrans.getModel().getModel().);
//        txtField20.setText(oTrans.getModel().getModel().getDlvryIns());
        String lsDRNo = "";
        if (oTrans.getModel().getModel().getUdrNo() != null && !String.valueOf(oTrans.getModel().getModel().getUdrNo()).isEmpty()) {
            lsDRNo = String.valueOf(oTrans.getModel().getModel().getUdrNo());

        }
        txtField21.setText(lsDRNo);
        if (oTrans.getModel().getModel().getUdrDate() != null && !String.valueOf(oTrans.getModel().getModel().getUdrDate()).isEmpty()) {
            txtField22.setText(CustomCommonUtil.xsDateShort(oTrans.getModel().getModel().getUdrDate()));
        }
        txtField23.setText(oTrans.getModel().getModel().getSoldTo());
        txtField24.setText(oTrans.getModel().getModel().getDealerNm());
        txtField25.setText(oTrans.getModel().getModel().getPlaceReg());
        textArea27.setText(oTrans.getModel().getModel().getRemarks());
        int lnVhclNew = -1;
        if (oTrans.getModel().getModel().getVhclNew() != null && !oTrans.getModel().getModel().getVhclNew().trim().isEmpty()) {
            switch (oTrans.getModel().getModel().getVhclNew()) {
                case "0":
                    lnVhclNew = 0;
                    break;
                case "1":
                    lnVhclNew = 1;
                    break;
            }
        }
        comboBox17.getSelectionModel().select(lnVhclNew);
        if (oTrans.getModel().getModel().getSoldStat() != null && !oTrans.getModel().getModel().getSoldStat().trim().isEmpty()) {
            comboBox18.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getSoldStat()));
        }
        // Your code
        if (oTrans.getModel().getModel().getRegisterDte() != null && !oTrans.getModel().getModel().getRegisterDte().toString().isEmpty()) {
            datePicker26.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getModel().getModel().getRegisterDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern removeSymbols;
        removeSymbols = Pattern.compile("[A-Za-z0-9]*");
        List<TextField> loTxtField = Arrays.asList(txtField11, txtField12, txtField15);
        loTxtField.forEach(tf -> tf.setTextFormatter(new TextFormatterUtil(removeSymbols)));
    }

    @Override
    public void initLimiterFields() {
        CustomCommonUtil.addTextLimiter(txtField15, 12);
        CustomCommonUtil.addTextLimiter(txtField11, 10);
        CustomCommonUtil.addTextLimiter(txtField12, 12);
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField11, txtField12, txtField13, txtField15, txtField14, txtField24);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        textArea27.focusedProperty().addListener(txtArea_Focus);
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
                case 11:/*Plate Number*/
                    oTrans.getModel().getModel().setPlateNo(lsValue);
                    checkExistingVchlInformation();
                    break;
                case 13:/*Frame Number*/
                    oTrans.getModel().getModel().setFrameNo(lsValue);
                    checkExistingVchlInformation();
                    break;
                case 15:/*Key Number*/
                    oTrans.getModel().getModel().setKeyNo(lsValue);
                    break;
                case 12:/*CS Number*/
                    oTrans.getModel().getModel().setCSNo(lsValue);
                    checkExistingVchlInformation();
                    break;
                case 14:/*Engine Number */
                    oTrans.getModel().getModel().setEngineNo(lsValue);
                    checkExistingVchlInformation();
                    break;
                case 24:/*Dealer*/
                    oTrans.getModel().getModel().setDealerNm(lsValue);
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
                case 27:
                    oTrans.getModel().getModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
                txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25, txtField13);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        textArea27.setOnKeyPressed(this::textArea_KeyPressed);
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
                            case "txtField01":
                                loJSON = oTrans.searchOwner(lsValue, true, false);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField01.setText(oTrans.getModel().getModel().getOwnerNmx());
                                    textArea02.setText(oTrans.getModel().getModel().getOwnerAdd());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField01.setText("");
                                    return;
                                }
                                break;
                            case "txtField03":
                                loJSON = oTrans.searchOwner(lsValue, false, false);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField03.setText(oTrans.getModel().getModel().getCOwnerNm());
                                    textArea04.setText(oTrans.getModel().getModel().getCOwnerAd());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField03.setText("");
                                    return;
                                }
                                break;
                            case "txtField05":
                                loJSON = oTrans.searchMake(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField05.setText(oTrans.getModel().getModel().getMakeDesc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField05.setText("");
                                    return;
                                }
                                break;
                            case "txtField06":
                                if (txtField05.getText().trim().equals("")) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                                    txtField05.requestFocus();
                                    return;
                                }
                                loJSON = oTrans.searchTransMsn(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField06.setText(oTrans.getModel().getModel().getTransMsn());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField06.setText("");
                                    return;
                                }
                                break;
                            case "txtField07":
                                if (txtField05.getText().trim().equals("")) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                                    txtField05.requestFocus();
                                    return;
                                }
                                loJSON = oTrans.searchModel(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField07.setText(oTrans.getModel().getModel().getModelDsc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField07.setText("");
                                    return;
                                }
                                break;
                            case "txtField08":
                                if (txtField05.getText().trim().equals("")) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                                    txtField05.requestFocus();
                                    return;
                                }
                                loJSON = oTrans.searchColor(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField08.setText(oTrans.getModel().getModel().getColorDsc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField08.setText("");
                                    return;
                                }
                                break;
                            case "txtField09":
                                if (txtField05.getText().trim().equals("")) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                                    txtField05.requestFocus();
                                    return;
                                }
                                loJSON = oTrans.searchType(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField09.setText(oTrans.getModel().getModel().getTypeDesc());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField09.setText("");
                                    return;
                                }
                                break;
                            case "txtField10":
                                if (txtField05.getText().trim().equals("")) {
                                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                                    txtField05.requestFocus();
                                    return;
                                }
                                loJSON = oTrans.searchYearModel(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField10.setText(String.valueOf(oTrans.getModel().getModel().getYearModl()));
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField10.setText("");
                                    return;
                                }
                                break;
                            case "txtField24":
                                loJSON = oTrans.searchDealer(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField24.setText(oTrans.getModel().getModel().getDealerNm());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    return;
                                }
                                break;
                            case "txtField25":
                                loJSON = oTrans.searchRegsplace(lsValue);
                                if (!"error".equals(loJSON.get("result"))) {
                                    txtField25.setText(oTrans.getModel().getModel().getPlaceReg());
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField25.setText("");
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
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse,
                btnTransfer, btnClose, btnVhclAvl, btnVhclDesc, btnEngine, btnFrame);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJson = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTrans = new Vehicle_Serial(oApp, false, oApp.getBranchCode());
                loJson = oTrans.newRecord();
                if ("success".equals((String) loJson.get("result"))) {
                    if (!pbisVhclSales) {
                        comboBox18.getSelectionModel().select(0);
                        oTrans.getModel().getModel().setIsDemo("0");
                    } else {
                        comboBox18.getSelectionModel().select(1);
                        oTrans.getModel().getModel().setIsDemo("");
                    }
                    oTrans.getModel().getModel().setSoldStat(String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
                    loadMasterFields();
                    txtField10.setText("");
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJson.get("message"));
                }
                break;
            case "btnEdit":
                loJson = oTrans.updateRecord();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJson.get("result"))) {
                    ShowMessageFX.Warning((String) loJson.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Information Saving....", "Are you sure, do you want to save?")) {
                    if (!pbisVhclSales) {
                        if (txtField01.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, "Warning", "Please select Vehicle Owner.");
                            return;
                        }
                        if (txtField24.getText().matches("[^a-zA-Z0-9].*")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid dealer name information.");
                            txtField24.setText("");
                            return;
                        }
                    }
                    if (checkExistingVchlInformation()) {
                        return;
                    }
                } else {
                    return;
                }

                if (!setSelection()) {
                    return;
                }
                loJson = oTrans.saveRecord();
                if ("success".equals((String) loJson.get("result"))) {
                    ShowMessageFX.Information(null, "Vehicle Information", (String) loJson.get("message"));
                    loJson = oTrans.openRecord(oTrans.getModel().getModel().getSerialID());
                    if ("success".equals((String) loJson.get("result"))) {
                        loadMasterFields();
//                        loadWareHouseHistory();
//                        loadOwnerHistory();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJson.get("message"));
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTrans = new Vehicle_Serial(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }

                if (pbisVhclSales) {
                    loJson = oTrans.searchRecord("", false, true);
                } else {
                    loJson = oTrans.searchRecord("", false, false);
                }

                if ("success".equals((String) loJson.get("result"))) {
                    loadMasterFields();
//                    loadOwnerHistory();
//                    loadWareHouseHistory();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Information", (String) loJson.get("message"));
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
            case "btnVhclDesc":
                loadVehicleDescriptionWindow();
                break;
            case "btnFrame":
            case "btnEngine":
                if (txtField05.getText().trim().equals("")) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value of make.");
                    txtField05.requestFocus();
                    return;
                }
                if (txtField07.getText().trim().equals("")) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value of model.");
                    txtField07.requestFocus();
                    return;
                }
                if (lsButton.contains("btnEngine")) {
                    loadEngineWindow();
                } else {
                    loadFrameWindow();
                }
                break;
            case "btnWareHouse":
                break;
            case "btnVhclAvl":
                loJson = oTrans.searchAvailableVhcl();
                if ("success".equals((String) loJson.get("result"))) {
                    loadAvailableVehicle();
                } else {
                    ShowMessageFX.Warning(null, "Search Available Vehicle", (String) loJson.get("message"));
                }
                break;
            case "btnVhclMnl":
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox17.setItems(cIsVhclStat);
        comboBox18.setItems(cSoldStats);
    }

    @Override
    public void initFieldsAction() {
        comboBox17.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox17.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox17.getSelectionModel().getSelectedIndex() >= 0) {
                        oTrans.getModel().getModel().setVhclNew(String.valueOf((comboBox17.getSelectionModel().getSelectedIndex())));
                    }
                }
            }
        });
        comboBox18.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
                        oTrans.getModel().getModel().setSoldStat(String.valueOf((comboBox18.getSelectionModel().getSelectedIndex())));
                    }
                }
            }
        });
        datePicker26.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getModel().getModel().setRegisterDte(SQLUtil.toDate(datePicker26.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
    }

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setOwnerNmx("");
                                oTrans.getModel().getModel().setOwnerAdd("");
                                textArea02.setText("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setCOwnerNm("");
                                oTrans.getModel().getModel().setCOwnerAd("");
                                textArea04.setText("");
                                initFields(pnEditMode);
                            }
                        }

                    }
                }
                );
        txtField05.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setMakeID("");
                                oTrans.getModel().getModel().setModelID("");
                                oTrans.getModel().getModel().setTransMsn("");
                                oTrans.getModel().getModel().setTypeID("");
                                oTrans.getModel().getModel().setYearModl(0);
                                oTrans.getModel().getModel().setColorID("");
                                CustomCommonUtil.setText("", txtField06, txtField07, txtField08, txtField09, txtField10);
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField07.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setModelID("");
                                oTrans.getModel().getModel().setTransMsn("");
                                oTrans.getModel().getModel().setTypeID("");
                                oTrans.getModel().getModel().setYearModl(0);
                                oTrans.getModel().getModel().setColorID("");
                                CustomCommonUtil.setText("", txtField06, txtField08, txtField09, txtField10);
                                initFields(pnEditMode);
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
                                oTrans.getModel().getModel().setTypeID("");
                                oTrans.getModel().getModel().setTransMsn("");
                                oTrans.getModel().getModel().setYearModl(0);
                                oTrans.getModel().getModel().setColorID("");
                                CustomCommonUtil.setText("", txtField06, txtField08, txtField10);
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField06.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setTransMsn("");
                                oTrans.getModel().getModel().setYearModl(0);
                                oTrans.getModel().getModel().setColorID("");
                                CustomCommonUtil.setText("", txtField08, txtField10);
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField08.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setYearModl(0);
                                oTrans.getModel().getModel().setColorID("");
                                txtField10.setText("");
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
                                oTrans.getModel().getModel().setYearModl(0);
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
                                oTrans.getModel().getModel().setPlaceReg("");
                                datePicker26.setValue(LocalDate.of(1900, Month.JANUARY, 1));
                                txtField25.setText("");
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField24.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setDealerNm("");
                            }
                        }
                    }
                }
                );
        txtField25.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getModel().getModel().setPlaceReg("");
                            }
                        }
                    }
                }
                );
    }

    @Override
    public void clearTables() {
        vhclOwnerHistoryData.clear();
        vhclWrhHistoryData.clear();
    }

    @Override
    public void clearFields() {
        pnRow = 0;
        CustomCommonUtil.setText("", lblSerailID, lblSerailIDValue, lblSerailID);
        CustomCommonUtil.setText("", txtField01, txtField03, txtField05,
                txtField06, txtField07, txtField08, txtField09, txtField10,
                txtField11, txtField12, txtField13, txtField14, txtField15,
                txtField16, txtField19, txtField20, txtField21, txtField22, txtField23,
                txtField24, txtField25);
        CustomCommonUtil.setValue(null, comboBox17, comboBox18);
        CustomCommonUtil.setText("", textArea02, textArea04, textArea27);
        datePicker26.setValue(LocalDate.of(1900, Month.JANUARY, 1));
    }

    @Override
    public void initFields(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true, txtField01, txtField03, comboBox17);
        CustomCommonUtil.setVisible(false, anchorPurch, gridPurch, vBoxPurchasedSold, gridSold);
        CustomCommonUtil.setManaged(false, anchorPurch, gridPurch, vBoxPurchasedSold, gridSold);
        CustomCommonUtil.setVisible(false, anchorMisc, gridMisc);
        CustomCommonUtil.setManaged(false, anchorMisc, gridMisc);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        if (!pbisVhclSales) {
            txtField01.setDisable(!lbShow);
            txtField03.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
            CustomCommonUtil.setVisible(true, anchorMisc, gridMisc);
            CustomCommonUtil.setManaged(true, anchorMisc, gridMisc);
        } else {
            CustomCommonUtil.setVisible(true, anchorPurch, gridPurch, vBoxPurchasedSold, gridSold);
            CustomCommonUtil.setManaged(true, anchorPurch, gridPurch, vBoxPurchasedSold, gridSold);
        }
        CustomCommonUtil.setDisable(!lbShow, txtField05, txtField11, txtField12, txtField15,
                txtField16, txtField24, textArea27);
        txtField07.setDisable(!(lbShow && !txtField05.getText().isEmpty()));
        txtField09.setDisable(!(lbShow && !txtField07.getText().isEmpty()));
        txtField06.setDisable(!(lbShow && !txtField09.getText().isEmpty()));
        txtField08.setDisable(!(lbShow && !txtField06.getText().isEmpty()));
        txtField10.setDisable(!(lbShow && !txtField08.getText().isEmpty()));
        CustomCommonUtil.setDisable(!(lbShow && !txtField05.getText().isEmpty()) && !(lbShow && !txtField07.getText().isEmpty()),
                txtField13, txtField14, btnFrame, btnEngine);
//        txtField20.setDisable(!lbShow);
//        txtField19.setDisable(!lbShow);
//        txtField23.setDisable(!lbShow);
//        txtField22.setDisable(!lbShow);
//        txtField21.setDisable(!lbShow);
        CustomCommonUtil.setDisable(!(lbShow && !txtField11.getText().isEmpty()), txtField25, datePicker26);
        CustomCommonUtil.setVisible(false, btnVhclAvl, btnEdit);
        CustomCommonUtil.setManaged(false, btnVhclAvl, btnEdit);
        if (pnEditMode == EditMode.ADDNEW) {
            if (!pbisVhclSales) {
                btnVhclAvl.setVisible(true);
                btnVhclAvl.setManaged(true);
            } else {
                comboBox17.setDisable(!lbShow);
            }
        }
        CustomCommonUtil.setVisible(false, btnVhclDesc, btnTransfer);
        CustomCommonUtil.setManaged(false, btnVhclDesc, btnTransfer);
        if (fnValue == EditMode.UPDATE) {
            CustomCommonUtil.setDisable(true, txtField01, comboBox17);
        }
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }

        comboBox18.setVisible(true);
        comboBox18.setManaged(true);
    }

    private boolean checkExistingVchlInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.validateExistingRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord((String) loJSON.get("sSerialID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    //                        loadWareHouseHistory();
//                        loadOwnerHistory();
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

    private String getParentTabTitle() {
        Node loParent = AnchorMain.getParent();
        Parent tabContentParent = loParent.getParent();
        if (tabContentParent instanceof TabPane) {
            TabPane loTabPane = (TabPane) tabContentParent;
            Tab loTab = findTabByContent(loTabPane, AnchorMain);
            if (loTab != null) {
                pxeModuleName = loTab.getText();
                return loTab.getText().toUpperCase();
            }
        }
        return null;
    }

    private Tab findTabByContent(TabPane foTabPane, Node foContent) {
        for (Tab tab : foTabPane.getTabs()) {
            if (tab.getContent() == foContent) {
                return tab;
            }
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (pbisVhclSales) {
                if (comboBox17.getSelectionModel().getSelectedIndex() < 0) {
                    if (comboBox18.getSelectionModel().getSelectedIndex() != 0) {
                        ShowMessageFX.Warning(null, "Vehicle Category", "Please select `Vehicle Category` value.");
                        return false;
                    }
                } else {
                    oTrans.getModel().getModel().setVhclNew(String.valueOf((comboBox17.getSelectionModel().getSelectedIndex())));
                }
            }
        }
        return true;
    }

    private void loadAvailableVehicle() {
        txtField05.setText(oTrans.getModel().getModel().getMakeDesc());
        txtField07.setText(oTrans.getModel().getModel().getModelDsc());
        txtField09.setText(oTrans.getModel().getModel().getTypeDesc());
        txtField06.setText(oTrans.getModel().getModel().getTransMsn());
        txtField08.setText(oTrans.getModel().getModel().getColorDsc());
        if (oTrans.getModel().getModel().getYearModl() == null) {
            txtField10.setText("");
        } else {
            txtField10.setText(String.valueOf(oTrans.getModel().getModel().getYearModl()));
        }
        if (oTrans.getModel().getModel().getPlateNo() != null) {
            txtField11.setText(oTrans.getModel().getModel().getPlateNo());
        } else {
            txtField11.setText("");
        }

        txtField13.setText(oTrans.getModel().getModel().getFrameNo());
        txtField15.setText(oTrans.getModel().getModel().getKeyNo());
        if (oTrans.getModel().getModel().getCSNo() != null) {
            txtField12.setText(oTrans.getModel().getModel().getCSNo());
        } else {
            txtField12.setText("");
        }
        txtField14.setText(oTrans.getModel().getModel().getEngineNo());
        txtField16.setText(oTrans.getModel().getModel().getLocation());
    }

    private void loadVehicleDescriptionWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleDescription.fxml"));
            VehicleDescriptionController loControl = new VehicleDescriptionController();
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", pxeModuleName);
            System.exit(1);
        }
    }

    private void loadEngineWindow() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleEngineFormat.fxml"));
            VehicleEngineFormatController loControl = new VehicleEngineFormatController();
            loControl.setGRider(oApp);
            loControl.setMakeID(oTrans.getModel().getModel().getMakeID());
            loControl.setMakeDesc(oTrans.getModel().getModel().getMakeDesc());
            loControl.setModelID(oTrans.getModel().getModel().getModelID());
            loControl.setModelDesc(oTrans.getModel().getModel().getModelDsc());
            loControl.setOpenEvent(true);
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", pxeModuleName);
            System.exit(1);
        }
    }

    private void loadFrameWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleFrameFormat.fxml"));
            VehicleFrameFormatController loControl = new VehicleFrameFormatController();
            loControl.setGRider(oApp);
            loControl.setMakeID(oTrans.getModel().getModel().getMakeID());
            loControl.setMakeDesc(oTrans.getModel().getModel().getMakeDesc());
            loControl.setModelID(oTrans.getModel().getModel().getModelID());
            loControl.setModelDesc(oTrans.getModel().getModel().getModelDsc());
            loControl.setOpenEvent(true);
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", pxeModuleName);
            System.exit(1);
        }
    }

    private void loadOwnerHistory() {
        JSONObject loJSON = new JSONObject();
        vhclOwnerHistoryData.clear();
//        loJSON = oTrans.load(oTrans.getModel().().getClientID(), false);
        /*Set Values to table from vehicle info table*/
//        if ("success".equals((String) loJSON.get("result"))) {
        for (lnCtr = 0; lnCtr <= oTrans.getVehicleSerialList().size() - 1; lnCtr++) {
            vhclOwnerHistoryData.add(new VehicleOwnerHistory(
                    String.valueOf(lnCtr + 1), //ROW
                    oTrans.getVehicleSerial(lnCtr, 8).toString(),
                    oTrans.getVehicleSerial(lnCtr, 20).toString(),
                    oTrans.getVehicleSerial(lnCtr, 33).toString(),
                    oTrans.getVehicleSerial(lnCtr, 9).toString()
            ));
        }
//        }
    }

    /*populate vheicle information Table*/
    private void initOwnHistory() {
        tblViewVhclOwnHsty01.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclOwnHsty01"));
        tblViewVhclOwnHsty02.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclOwnHsty02"));
        tblViewVhclOwnHsty03.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclOwnHsty03"));
        tblViewVhclOwnHsty04.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclOwnHsty04"));
        tblViewVhclOwnHsty05.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclOwnHsty05"));

        tblViewVhclOwnHsty.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclOwnHsty.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewVhclOwnHsty.setItems(vhclOwnerHistoryData);
    }

    private void loadWareHouseHistory() {
        JSONObject loJSON = new JSONObject();
        vhclWrhHistoryData.clear();
//        loJSON = oTrans.loadWareHouseList(oTrans.getModel().().getClientID(), false);
        /*Set Values to table from vehicle info table*/
//        if ("success".equals((String) loJSON.get("result"))) {
        for (lnCtr = 0; lnCtr <= oTrans.getVehicleSerialList().size() - 1; lnCtr++) {
            vhclWrhHistoryData.add(new VehicleWarehouseHistory(
                    String.valueOf(lnCtr + 1), //ROW
                    oTrans.getVehicleSerial(lnCtr, 8).toString(),
                    oTrans.getVehicleSerial(lnCtr, 20).toString(),
                    oTrans.getVehicleSerial(lnCtr, 33).toString(),
                    oTrans.getVehicleSerial(lnCtr, 9).toString()
            ));
        }
//        }
    }

    /*populate vheicle information Table*/
    private void initWareHouseHistory() {
        tblViewVhclWrhHsty01.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclWrhHsty01"));
        tblViewVhclWrhHsty02.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclWrhHsty02"));
        tblViewVhclWrhHsty03.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclWrhHsty03"));
        tblViewVhclWrhHsty04.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclWrhHsty04"));
        tblViewVhclWrhHsty05.setCellValueFactory(new PropertyValueFactory<>("tblViewVhclWrhHsty05"));

        tblViewVhclWrhHsty.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclOwnHsty.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        tblViewVhclWrhHsty.setItems(vhclWrhHistoryData);
    }

}
