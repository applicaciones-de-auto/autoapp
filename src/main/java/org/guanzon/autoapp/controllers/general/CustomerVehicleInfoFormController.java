/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
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
import org.guanzon.autoapp.controllers.parameters.VehicleDescriptionEntryController;
import org.guanzon.autoapp.controllers.parameters.VehicleEngineFormatEntryController;
import org.guanzon.autoapp.controllers.parameters.VehicleFrameFormatEntryController;
import org.guanzon.autoapp.models.general.ModelVehicleOwnerHistory;
import org.guanzon.autoapp.models.general.ModelVehicleWarehouseHistory;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class CustomerVehicleInfoFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_Serial oTransVchInfo;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private String pxeModuleName = ""; //Form Title
    private boolean pbisVhclSales = false;
    private int pnEditMode;
    private int pnRow = -1;
    private int lnCtr;
    private double xOffset = 0;
    private double yOffset = 0;
    private ObservableList<ModelVehicleOwnerHistory> vhclOwnerHistoryData = FXCollections.observableArrayList();
    private ObservableList<ModelVehicleWarehouseHistory> vhclWrhHistoryData = FXCollections.observableArrayList();
    ObservableList<String> cSoldStats = FXCollections.observableArrayList("NON SALES CUSTOMER", "AVAILABLE FOR SALE", "VSP", "SOLD");
    ObservableList<String> cIsVhclnew = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Label lblSerailID;
    @FXML
    private Label lblSerailIDValue;
    @FXML
    private AnchorPane anchorPurch;
    @FXML
    private HBox vBoxPurchasedSold;
    @FXML
    private AnchorPane gridPurch;
    @FXML
    private AnchorPane gridSold;
    @FXML
    private AnchorPane anchorMisc;
    @FXML
    private AnchorPane gridMisc;
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
    private TableView<ModelVehicleOwnerHistory> tblViewVhclOwnHsty;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty01;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty02;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty03;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty04;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty05;
    @FXML
    private TableView<ModelVehicleWarehouseHistory> tblViewVhclWrhHsty;
    @FXML
    private TableColumn<ModelVehicleWarehouseHistory, String> tblViewVhclWrhHsty01;
    @FXML
    private TableColumn<ModelVehicleWarehouseHistory, String> tblViewVhclWrhHsty02;
    @FXML
    private TableColumn<ModelVehicleWarehouseHistory, String> tblViewVhclWrhHsty03;
    @FXML
    private TableColumn<ModelVehicleWarehouseHistory, String> tblViewVhclWrhHsty04, tblViewVhclWrhHsty05;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransVchInfo = new Vehicle_Serial(oApp, false, oApp.getBranchCode());

        initOwnHistory();
        initWareHouseHistory();
        initComboBoxValue();
        initTextFieldPattern();
        datePicker26.setOnAction(this::getDate);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        clearTables();
        InputTextUtil.addTextLimiter(txtField15, 12);
        InputTextUtil.addTextLimiter(txtField11, 10);
        InputTextUtil.addTextLimiter(txtField13, 19);
        Platform.runLater(() -> {
            if (getParentTabTitle().contains("SALES")) {
                pbisVhclSales = true;
                initFields(pnEditMode);
            } else {
                pbisVhclSales = false;
            }
        });
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

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

    private void initComboBoxValue() {
        comboBox17.setItems(cSoldStats);
        comboBox18.setItems(cIsVhclnew);
    }

    private void initTextFieldPattern() {
        Pattern removeSymbols, withSpace;
        removeSymbols = Pattern.compile("[A-Za-z0-9]*");
        withSpace = Pattern.compile("[A-Za-z0-9 ]*");
        txtField11.setTextFormatter(new InputTextFormatterUtil(removeSymbols)); //PlateNo
        txtField12.setTextFormatter(new InputTextFormatterUtil(removeSymbols)); //CsNo
        txtField15.setTextFormatter(new InputTextFormatterUtil(removeSymbols)); //keyno
        txtField24.setTextFormatter(new InputTextFormatterUtil(withSpace)); //register
    }

    private void getDate(ActionEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            oTransVchInfo.setMaster(22, SQLUtil.toDate(datePicker26.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
                txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25, txtField13);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));

        /*TextArea*/
        InputTextUtil.setCapsLockBehavior(textArea02);
        InputTextUtil.setCapsLockBehavior(textArea04);
        InputTextUtil.setCapsLockBehavior(textArea27);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
                txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25, txtField13);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        textArea27.setOnKeyPressed(this::textArea_KeyPressed);
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
                        loJSON = oTransVchInfo.searchOwner(lsValue, true, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField01.setText(oTransVchInfo.getModel().getModel().getOwnerNmx());
                            textArea02.setText(oTransVchInfo.getModel().getModel().getOwnerAdd());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField01.setText("");
                            txtField01.requestFocus();
                            return;
                        }
                        break;
                    case "txtField03":
                        loJSON = oTransVchInfo.searchOwner(lsValue, false, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField03.setText(oTransVchInfo.getModel().getModel().getCOwnerNm());
                            textArea04.setText(oTransVchInfo.getModel().getModel().getCOwnerAd());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField03.setText("");
                            txtField03.requestFocus();
                            return;
                        }
                        break;
                    case "txtField05":
                        loJSON = oTransVchInfo.searchMake(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField05.setText(oTransVchInfo.getModel().getModel().getMakeDesc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField05.setText("");
                            txtField05.requestFocus();
                            return;
                        }
                        break;
                    case "txtField06":
                        if (txtField05.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                            txtField05.requestFocus();
                            return;
                        }
                        loJSON = oTransVchInfo.searchTransMsn(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField06.setText(oTransVchInfo.getModel().getModel().getTransMsn());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField06.setText("");
                            txtField06.requestFocus();
                            return;
                        }
                        break;
                    case "txtField07":
                        if (txtField05.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                            txtField05.requestFocus();
                            return;
                        }
                        loJSON = oTransVchInfo.searchModel(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField07.setText(oTransVchInfo.getModel().getModel().getModelDsc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField07.setText("");
                            txtField07.requestFocus();
                            return;
                        }
                        break;
                    case "txtField08":
                        if (txtField05.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                            txtField05.requestFocus();
                            return;
                        }
                        loJSON = oTransVchInfo.searchColor(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField08.setText(oTransVchInfo.getModel().getModel().getColorDsc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField08.setText("");
                            txtField08.requestFocus();
                            return;
                        }
                        break;
                    case "txtField09":
                        if (txtField05.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                            txtField05.requestFocus();
                            return;
                        }
                        loJSON = oTransVchInfo.searchType(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField09.setText(oTransVchInfo.getModel().getModel().getTypeDesc());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField09.setText("");
                            txtField09.requestFocus();
                            return;
                        }
                        break;
                    case "txtField10":
                        if (txtField05.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter value make.");
                            txtField05.requestFocus();
                            return;
                        }
                        loJSON = oTransVchInfo.searchYearModel(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField10.setText(String.valueOf(oTransVchInfo.getModel().getModel().getYearModl()));
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField10.setText("");
                            txtField10.requestFocus();
                            return;
                        }
                        break;
                    case "txtField24":
                        loJSON = oTransVchInfo.searchDealer(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField24.setText(oTransVchInfo.getModel().getModel().getDealerNm());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                        break;
                    case "txtField25":
                        loJSON = oTransVchInfo.searchRegsplace(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField25.setText(oTransVchInfo.getModel().getModel().getPlaceReg());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField25.setText("");
                            txtField25.requestFocus();
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
        List<TextField> loTxtField = Arrays.asList(txtField11, txtField12, txtField13, txtField15, txtField14, txtField24
        );
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
                    oTransVchInfo.getModel().getModel().setPlateNo(lsValue);
                    break;
                case 13:/*Frame Number*/
                    oTransVchInfo.getModel().getModel().setFrameNo(lsValue);
                    break;
                case 15:/*Key Number*/
                    oTransVchInfo.getModel().getModel().setKeyNo(lsValue);
                    break;
                case 12:/*CS Number*/
                    oTransVchInfo.getModel().getModel().setCSNo(lsValue);
                    break;
                case 14:/*Engine Number */
                    oTransVchInfo.getModel().getModel().setEngineNo(lsValue);
                    break;
                case 24:/*Dealer*/
                    oTransVchInfo.getModel().getModel().setDealerNm(lsValue);
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
                    oTransVchInfo.getModel().getModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void initCmboxFieldAction() {
        comboBox17.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox17.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox17.getSelectionModel().getSelectedIndex() >= 0) {
                        oTransVchInfo.getModel().getModel().setSoldStat(String.valueOf((comboBox17.getSelectionModel().getSelectedIndex())));
                    }
                }
            }
        });
        comboBox18.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
                        oTransVchInfo.getModel().getModel().setVhclStat(String.valueOf((comboBox18.getSelectionModel().getSelectedIndex())));
                    }
                }
            }
        });
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransVchInfo.getModel().getModel().setOwnerNmx("");
                                oTransVchInfo.getModel().getModel().setOwnerAdd("");
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
                                oTransVchInfo.getModel().getModel().setCOwnerNm("");
                                oTransVchInfo.getModel().getModel().setCOwnerAd("");
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
                                oTransVchInfo.getModel().getModel().setMakeID("");
                                oTransVchInfo.getModel().getModel().setModelID("");
                                oTransVchInfo.getModel().getModel().setTransMsn("");
                                oTransVchInfo.getModel().getModel().setTypeID("");
                                oTransVchInfo.getModel().getModel().setYearModl(0);
                                oTransVchInfo.getModel().getModel().setColorID("");
                                txtField06.setText("");
                                txtField07.setText("");
                                txtField08.setText("");
                                txtField09.setText("");
                                txtField10.setText("");
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
                                oTransVchInfo.getModel().getModel().setModelID("");
                                oTransVchInfo.getModel().getModel().setTransMsn("");
                                oTransVchInfo.getModel().getModel().setTypeID("");
                                oTransVchInfo.getModel().getModel().setYearModl(0);
                                oTransVchInfo.getModel().getModel().setColorID("");
                                txtField06.setText("");
                                txtField08.setText("");
                                txtField09.setText("");
                                txtField10.setText("");
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
                                oTransVchInfo.getModel().getModel().setTypeID("");
                                oTransVchInfo.getModel().getModel().setTransMsn("");
                                oTransVchInfo.getModel().getModel().setYearModl(0);
                                oTransVchInfo.getModel().getModel().setColorID("");
                                txtField06.setText("");
                                txtField08.setText("");
                                txtField10.setText("");
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
                                oTransVchInfo.getModel().getModel().setTransMsn("");
                                oTransVchInfo.getModel().getModel().setYearModl(0);
                                oTransVchInfo.getModel().getModel().setColorID("");
                                txtField08.setText("");
                                txtField10.setText("");
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
                                oTransVchInfo.getModel().getModel().setYearModl(0);
                                oTransVchInfo.getModel().getModel().setColorID("");
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
                                oTransVchInfo.getModel().getModel().setYearModl(0);
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
                                oTransVchInfo.getModel().getModel().setPlaceReg("");
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
                                oTransVchInfo.getModel().getModel().setDealerNm("");
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
                                oTransVchInfo.getModel().getModel().setPlaceReg("");
                            }
                        }
                    }
                }
                );
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse,
                btnTransfer, btnClose, btnVhclAvl, btnVhclDesc, btnEngine, btnFrame);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJson = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTransVchInfo = new Vehicle_Serial(oApp, false, oApp.getBranchCode());
                loJson = oTransVchInfo.newRecord();
                if ("success".equals((String) loJson.get("result"))) {
                    if (!pbisVhclSales) {
                        comboBox17.getSelectionModel().select(0);
                        oTransVchInfo.getModel().getModel().setIsDemo("0");
                    } else {
                        comboBox17.getSelectionModel().select(1);
                        oTransVchInfo.getModel().getModel().setIsDemo("");
                    }
                    oTransVchInfo.getModel().getModel().setSoldStat(String.valueOf(comboBox17.getSelectionModel().getSelectedIndex()));
                    loadVehicleInformation();
                    txtField10.setText("");
                    pnEditMode = oTransVchInfo.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJson.get("message"));
                }
                break;
            case "btnEdit":
                loJson = oTransVchInfo.updateRecord();
                pnEditMode = oTransVchInfo.getEditMode();
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
                } else {
                    return;
                }

                if (!setSelection()) {
                    return;
                }
                loJson = oTransVchInfo.saveRecord();
                if ("success".equals((String) loJson.get("result"))) {
                    ShowMessageFX.Information(null, "Vehicle Information", (String) loJson.get("message"));
                    loJson = oTransVchInfo.openRecord(oTransVchInfo.getModel().getModel().getSerialID());
                    if ("success".equals((String) loJson.get("result"))) {
                        loadVehicleInformation();
//                        loadWareHouseHistory();
//                        loadOwnerHistory();
                        initFields(pnEditMode);
                        pnEditMode = oTransVchInfo.getEditMode();
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
                    oTransVchInfo = new Vehicle_Serial(oApp, false, oApp.getBranchCode());
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
                    loJson = oTransVchInfo.searchRecord("", false, true);
                } else {
                    loJson = oTransVchInfo.searchRecord("", false, false);
                }
                if ("success".equals((String) loJson.get("result"))) {
                    loadVehicleInformation();
//                    loadOwnerHistory();
//                    loadWareHouseHistory();
                    pnEditMode = oTransVchInfo.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Information Confirmation", (String) loJson.get("message"));
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
                loJson = oTransVchInfo.searchAvailableVhcl();
                if ("success".equals((String) loJson.get("result"))) {
                    loadAvailableVehicle();
                } else {
                    ShowMessageFX.Warning(null, "Search Available Vehicle Confirmation", (String) loJson.get("message"));
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

    /*Set ComboBox Value to Master Class*/
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (pbisVhclSales) {
                if (comboBox17.getSelectionModel().getSelectedIndex() < 0) {
                    ShowMessageFX.Warning(null, "Vehicle Category", "Please select `Vehicle Category` value.");
                    return false;
                } else {
                    oTransVchInfo.getModel().getModel().setSoldStat(String.valueOf((comboBox17.getSelectionModel().getSelectedIndex())));
                }
            }
        }
        return true;
    }

    private void loadAvailableVehicle() {
        txtField05.setText(oTransVchInfo.getModel().getModel().getMakeDesc());
        txtField07.setText(oTransVchInfo.getModel().getModel().getModelDsc());
        txtField09.setText(oTransVchInfo.getModel().getModel().getTypeDesc());
        txtField06.setText(oTransVchInfo.getModel().getModel().getTransMsn());
        txtField08.setText(oTransVchInfo.getModel().getModel().getColorDsc());
        if (oTransVchInfo.getModel().getModel().getYearModl() == null) {
            txtField10.setText("");
        } else {
            txtField10.setText(String.valueOf(oTransVchInfo.getModel().getModel().getYearModl()));
        }
        if (oTransVchInfo.getModel().getModel().getPlateNo() != null) {
            txtField11.setText(oTransVchInfo.getModel().getModel().getPlateNo());
        } else {
            txtField11.setText("");
        }

        txtField13.setText(oTransVchInfo.getModel().getModel().getFrameNo());
        txtField15.setText(oTransVchInfo.getModel().getModel().getKeyNo());
        if (oTransVchInfo.getModel().getModel().getCSNo() != null) {
            txtField12.setText(oTransVchInfo.getModel().getModel().getCSNo());
        } else {
            txtField12.setText("");
        }
        txtField14.setText(oTransVchInfo.getModel().getModel().getEngineNo());
        txtField16.setText(oTransVchInfo.getModel().getModel().getLocation());
    }

    private void loadVehicleInformation() {
        lblSerailID.setText("SERIAL ID: ");
        lblSerailIDValue.setText(oTransVchInfo.getModel().getModel().getSerialID());
        txtField01.setText(oTransVchInfo.getModel().getModel().getOwnerNmx());
        txtField03.setText(oTransVchInfo.getModel().getModel().getCOwnerNm());
        textArea02.setText(oTransVchInfo.getModel().getModel().getOwnerAdd());
        textArea04.setText(oTransVchInfo.getModel().getModel().getCOwnerAd());

        txtField05.setText(oTransVchInfo.getModel().getModel().getMakeDesc());
        txtField07.setText(oTransVchInfo.getModel().getModel().getModelDsc());
        txtField09.setText(oTransVchInfo.getModel().getModel().getTypeDesc());
        txtField06.setText(oTransVchInfo.getModel().getModel().getTransMsn());
        txtField08.setText(oTransVchInfo.getModel().getModel().getColorDsc());
        if (oTransVchInfo.getModel().getModel().getYearModl() == null) {
            txtField10.setText("");
        } else {
            txtField10.setText(String.valueOf(oTransVchInfo.getModel().getModel().getYearModl()));
        }
        if (oTransVchInfo.getModel().getModel().getPlateNo() != null) {
            txtField11.setText(oTransVchInfo.getModel().getModel().getPlateNo());
        } else {
            txtField11.setText("");
        }

        txtField13.setText(oTransVchInfo.getModel().getModel().getFrameNo());
        txtField15.setText(oTransVchInfo.getModel().getModel().getKeyNo());
        if (oTransVchInfo.getModel().getModel().getCSNo() != null) {
            txtField12.setText(oTransVchInfo.getModel().getModel().getCSNo());
        } else {
            txtField12.setText("");
        }
        txtField14.setText(oTransVchInfo.getModel().getModel().getEngineNo());
        txtField16.setText(oTransVchInfo.getModel().getModel().getLocation());
//        txtField19.setText(oTransVchInfo.getModel().getModel().getRecvDate);
//        txtField20.setText(oTransVchInfo.getModel().getModel().getDlvryIns());
//        txtField21.setText(oTransVchInfo.getModel().getModel().getDrNumber());
//        txtField22.setText(oTransVchInfo.getModel().getModel().get);
        txtField23.setText(oTransVchInfo.getModel().getModel().getSoldTo());
        txtField24.setText(oTransVchInfo.getModel().getModel().getDealerNm());
        txtField25.setText(oTransVchInfo.getModel().getModel().getPlaceReg());
        textArea27.setText(oTransVchInfo.getModel().getModel().getRemarks());
        if (oTransVchInfo.getModel().getModel().getSoldStat() != null && !oTransVchInfo.getModel().getModel().getSoldStat().trim().isEmpty()) {
            comboBox17.getSelectionModel().select(Integer.parseInt(oTransVchInfo.getModel().getModel().getSoldStat()));
        }
        if (oTransVchInfo.getModel().getModel().getVhclStat() != null && !oTransVchInfo.getModel().getModel().getVhclStat().trim().isEmpty()) {
//            comboBox18.getSelectionModel().select(Integer.parseInt(oTransVchInfo.getModel().getModel().getVhclStat()));
        }
        // Your code
        if (oTransVchInfo.getModel().getModel().getRegisterDte() != null && !oTransVchInfo.getModel().getModel().getRegisterDte().toString().isEmpty()) {
            datePicker26.setValue(InputTextUtil.strToDate(oTransVchInfo.getMaster(22).toString()));
        }

    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void clearFields() {
        pnRow = 0;
        lblSerailID.setText("");
        lblSerailIDValue.setText("");
        lblSerailID.setText("");
        txtField01.setText("");
        txtField03.setText("");
        txtField05.setText("");
        txtField07.setText("");
        txtField09.setText("");
        txtField06.setText("");
        txtField08.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField15.setText("");
        txtField12.setText("");
        txtField14.setText("");
        txtField16.setText("");
        comboBox17.setValue(null);
        comboBox18.setValue(null);
        datePicker26.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        txtField20.setText("");
        txtField19.setText("");
        txtField23.setText("");
        txtField22.setText("");
        txtField21.setText("");
        txtField24.setText("");
        txtField25.setText("");
        txtField13.setText("");
        textArea02.setText("");
        textArea04.setText("");
        textArea27.setText("");
    }

    private void clearTables() {
        vhclOwnerHistoryData.clear();
        vhclWrhHistoryData.clear();
    }

    /*Enabling / Disabling Fields*/
    private void initFields(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        txtField01.setDisable(!lbShow);
        txtField03.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        txtField05.setDisable(!lbShow);
        txtField07.setDisable(!(lbShow && !txtField05.getText().isEmpty()));
        txtField09.setDisable(!(lbShow && !txtField07.getText().isEmpty()));
        txtField06.setDisable(!(lbShow && !txtField09.getText().isEmpty()));
        txtField08.setDisable(!(lbShow && !txtField06.getText().isEmpty()));
        txtField10.setDisable(!(lbShow && !txtField08.getText().isEmpty()));
        txtField11.setDisable(!lbShow);
        txtField13.setDisable(!(lbShow && !txtField05.getText().isEmpty()) && !(lbShow && !txtField07.getText().isEmpty()));
        txtField15.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField14.setDisable(!(lbShow && !txtField05.getText().isEmpty()) && !(lbShow && !txtField07.getText().isEmpty()));
        txtField16.setDisable(!lbShow);
//        txtField20.setDisable(!lbShow);
//        txtField19.setDisable(!lbShow);
//        txtField23.setDisable(!lbShow);
//        txtField22.setDisable(!lbShow);
//        txtField21.setDisable(!lbShow);
        txtField24.setDisable(!lbShow);
        txtField25.setDisable(!(lbShow && !txtField11.getText().isEmpty()));
        textArea27.setDisable(!lbShow);
        datePicker26.setDisable(!(lbShow && !txtField11.getText().isEmpty()));
        btnFrame.setDisable(!(lbShow && !txtField05.getText().isEmpty()) && !(lbShow && !txtField07.getText().isEmpty()));
        btnEngine.setDisable(!(lbShow && !txtField05.getText().isEmpty()) && !(lbShow && !txtField07.getText().isEmpty()));

        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnVhclAvl.setVisible(false);
        if (pnEditMode == EditMode.ADDNEW) {
            if (!pbisVhclSales) {
                btnVhclAvl.setVisible(true);
            } else {
                comboBox17.setDisable(false);
            }
        }

        btnVhclDesc.setVisible(true);
        btnTransfer.setVisible(false);
        btnTransfer.setManaged(false);

        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);

        }

        if (pbisVhclSales) {
            anchorMisc.setVisible(false);
            anchorMisc.setManaged(false);
            gridMisc.setVisible(false);
            gridMisc.setManaged(false);
            anchorPurch.setVisible(true);
            anchorPurch.setManaged(true);
            gridPurch.setVisible(true);
            gridPurch.setManaged(true);
            vBoxPurchasedSold.setVisible(true);
            vBoxPurchasedSold.setManaged(true);
            gridSold.setVisible(true);
            gridSold.setManaged(true);
            txtField01.setDisable(true);
            txtField03.setDisable(true);
        } else {
            anchorMisc.setVisible(true);
            anchorMisc.setManaged(true);
            gridMisc.setVisible(true);
            gridMisc.setManaged(true);
            anchorPurch.setVisible(false);
            anchorPurch.setManaged(false);
            gridPurch.setVisible(false);
            gridPurch.setManaged(false);
            vBoxPurchasedSold.setVisible(false);
            vBoxPurchasedSold.setManaged(false);
            gridSold.setVisible(false);
            gridSold.setManaged(false);
        }
        comboBox18.setVisible(true);
        comboBox18.setManaged(true);
    }

    private void loadVehicleDescriptionWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleDescriptionEntry.fxml"));
            VehicleDescriptionEntryController loControl = new VehicleDescriptionEntryController();
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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleEngineFormatEntry.fxml"));
            VehicleEngineFormatEntryController loControl = new VehicleEngineFormatEntryController();
            loControl.setGRider(oApp);
            loControl.setMakeID(oTransVchInfo.getModel().getModel().getMakeID());
            loControl.setMakeDesc(oTransVchInfo.getModel().getModel().getMakeDesc());
            loControl.setModelID(oTransVchInfo.getModel().getModel().getModelID());
            loControl.setModelDesc(oTransVchInfo.getModel().getModel().getModelDsc());
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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleFrameFormatEntry.fxml"));
            VehicleFrameFormatEntryController loControl = new VehicleFrameFormatEntryController();
            loControl.setGRider(oApp);
            loControl.setMakeID(oTransVchInfo.getModel().getModel().getMakeID());
            loControl.setMakeDesc(oTransVchInfo.getModel().getModel().getMakeDesc());
            loControl.setModelID(oTransVchInfo.getModel().getModel().getModelID());
            loControl.setModelDesc(oTransVchInfo.getModel().getModel().getModelDsc());
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
//        loJSON = oTransVchInfo.load(oTransVchInfo.getModel().().getClientID(), false);
        /*Set Values to table from vehicle info table*/
//        if ("success".equals((String) loJSON.get("result"))) {
        for (lnCtr = 0; lnCtr <= oTransVchInfo.getVehicleSerialList().size() - 1; lnCtr++) {
            vhclOwnerHistoryData.add(new ModelVehicleOwnerHistory(
                    String.valueOf(lnCtr + 1), //ROW
                    oTransVchInfo.getVehicleSerial(lnCtr, 8).toString(),
                    oTransVchInfo.getVehicleSerial(lnCtr, 20).toString(),
                    oTransVchInfo.getVehicleSerial(lnCtr, 33).toString(),
                    oTransVchInfo.getVehicleSerial(lnCtr, 9).toString()
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
//        loJSON = oTransVchInfo.loadWareHouseList(oTransVchInfo.getModel().().getClientID(), false);
        /*Set Values to table from vehicle info table*/
//        if ("success".equals((String) loJSON.get("result"))) {
        for (lnCtr = 0; lnCtr <= oTransVchInfo.getVehicleSerialList().size() - 1; lnCtr++) {
            vhclWrhHistoryData.add(new ModelVehicleWarehouseHistory(
                    String.valueOf(lnCtr + 1), //ROW
                    oTransVchInfo.getVehicleSerial(lnCtr, 8).toString(),
                    oTransVchInfo.getVehicleSerial(lnCtr, 20).toString(),
                    oTransVchInfo.getVehicleSerial(lnCtr, 33).toString(),
                    oTransVchInfo.getVehicleSerial(lnCtr, 9).toString()
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
