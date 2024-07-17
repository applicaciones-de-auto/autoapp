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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerVehicleInfoFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_Serial oTransVchInfo;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private String pxeModuleName = ""; //Form Title
    private boolean pbisVhclSales = false;
    private int pnEditMode;
    private boolean bBtnVhclAvl = false;
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
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnTransfer, btnClose, btnVhclAvl, btnVhclDesc, btnVhclMnl, btnEngine, btnFrame, btnWareHouse;
    @FXML
    private VBox vboxSales;
    @FXML
    private AnchorPane anchorPurch;
    @FXML
    private GridPane gridPurch;
    @FXML
    private AnchorPane anchorSold;
    @FXML
    private GridPane gridSold;
    @FXML
    private AnchorPane anchorMisc;
    @FXML
    private GridPane gridMisc;
    @FXML
    private TextField txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
            txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25, txtField13;
    @FXML
    private TextArea textArea02, textArea04;
    @FXML
    private ComboBox<String> comboBox18;
    @FXML
    private ComboBox<String> comboBox17;
    @FXML
    private DatePicker datePicker26;
    @FXML
    private TextArea textArea27;
    @FXML
    private TableView<ModelVehicleOwnerHistory> tblViewVhclOwnHsty;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty01, tblViewVhclOwnHsty02, tblViewVhclOwnHsty03, tblViewVhclOwnHsty04, tblViewVhclOwnHsty05;
    @FXML
    private TableView<ModelVehicleWarehouseHistory> tblViewVhclWrhHsty;
    @FXML
    private TableColumn<ModelVehicleWarehouseHistory, String> tblViewVhclWrhHsty01, tblViewVhclWrhHsty02, tblViewVhclWrhHsty03, tblViewVhclWrhHsty04, tblViewVhclWrhHsty05;
    @FXML
    private Label lblSerailID;
    @FXML
    private Label lblSerailIDValue;

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
        comboBox17.setItems(cIsVhclnew);
        comboBox18.setItems(cSoldStats);
    }

    private void initTextFieldPattern() {

    }

    private void getDate(ActionEvent event) {
        /*CLIENT INFORMATION*/
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            oTransVchInfo.setMaster(11, SQLUtil.toDate(datePicker26.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
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
//                            txtField01.setText(oTransVchInfo.getModel().getModel().getOwner());
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
//                            txtField03.setText(oTransVchInfo.getModel().getModel().getCoOwner());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField03.setText("");
                            txtField03.requestFocus();
                            return;
                        }
                        break;
                    case "txtField05":
                        loJSON = oTransVchInfo.searchVehicleMake(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField05.setText(oTransVchInfo.getModel().getModel().getVchlMake());
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            txtField05.setText("");
                            txtField05.requestFocus();
                            return;
                        }
                        break;
                    case "txtField06":
//                        loJSON = oTransVchInfo.searchVehicleTrns(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField06.setText(oTransVchInfo.getModel().getModel().getVchlTrns());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField06.setText("");
//                            txtField06.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField07":
//                        loJSON = oTransVchInfo.searchVehicleModel(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField07.setText(oTransVchInfo.getModel().getModel().getModel());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField07.setText("");
//                            txtField07.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField08":
//                        loJSON = oTransVchInfo.searchVchlColor(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField08.setText(oTransVchInfo.getModel().getModel().getVchlColor());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField08.setText("");
//                            txtField08.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField09":
//                        loJSON = oTransVchInfo.searchType(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField09.setText(oTransVchInfo.getModel().getModel().getVchlType());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField09.setText("");
//                            txtField09.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField10":
//                        loJSON = oTransVchInfo.searchYear(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField10.setText(oTransVchInfo.getModel().getModel().getYear());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField10.setText("");
//                            txtField10.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField24":
//                        loJSON = oTransVchInfo.searchDealer(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField09.setText(oTransVchInfo.getModel().getModel().getDealer());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField09.setText("");
//                            txtField09.requestFocus();
//                            return;
//                        }
                        break;
                    case "txtField25":
//                        loJSON = oTransVchInfo.searchPlcReg(lsValue);
//                        if (!"error".equals(loJSON.get("result"))) {
//                            txtField10.setText(oTransVchInfo.getModel().getModel().getPlcReg());
//                        } else {
//                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                            txtField10.setText("");
//                            txtField10.requestFocus();
//                            return;
//                        }
                        break;
                }
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
        List<TextField> loTxtField = Arrays.asList(txtField11, txtField12, txtField13, txtField15, txtField14
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
//                case 11:/*Plate Number*/
//                    oTransVchInfo.getModel().getModel().setPltNumber(lsValue);
//                    break;
//                case 13:/*Frame Number*/
//                    oTransVchInfo.getModel().getModel().setFrmNumber(lsValue);
//                    break;
//                case 15:/*Key Number*/
//                    oTransVchInfo.getModel().getModel().setKeyNumber(lsValue);
//                    break;
//                case 12:/*CS Number*/
//                    oTransVchInfo.getModel().getModel().setCSNumberx(lsValue);
//                    break;
//                case 14:/*Engine Number */
//                    oTransVchInfo.getModel().getModel().setEngNumber(lsValue);
//                    break;
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
//                case 27:
//                    oTransVchInfo.getModel().getModel().setAddlInfo(lsValue);
//                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void initCmboxFieldAction() {
        //        comboBox17.setOnAction(e -> {
        //            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                if (comboBox17.getSelectionModel().getSelectedIndex() == 0) {
        //                    if (comboBox17.getSelectionModel().getSelectedIndex() >= 0) {
        //                        oTransVchInfo.getModel().getModel().setCategory(String.valueOf((comboBox17.getSelectionModel().getSelectedIndex())));
        //                    }
        //                }
        //        });
        //        comboBox18.setOnAction(e -> {
        //            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
        //                    if (comboBox18.getSelectionModel().getSelectedIndex() >= 0) {
        //                        oTransVchInfo.getModel().getModel().setVchStatus(String.valueOf((comboBox18.getSelectionModel().getSelectedIndex())));
        //                    }
        //                }
        //            }
        //        });
        //        txtField01.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setOwner("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField03.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setCoOwner("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField05.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setMake("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField07.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setModel("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField09.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setType("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField06.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setTrans("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField08.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setColor("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField10.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setYear("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField24.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setDealer("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
        //        txtField25.textProperty()
        //                .addListener((observable, oldValue, newValue) -> {
        //                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
        //                            if (newValue != null) {
        //                                if (newValue.isEmpty()) {
        //                                    oTransVchInfo.getModel().getModel().setPlcReg("");
        //                            }
        //                        }
        //                    }
        //                }
        //                );
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse,
                btnTransfer, btnClose, btnVhclAvl, btnVhclDesc, btnVhclMnl, btnEngine, btnFrame, btnWareHouse);

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
                    if (pbisVhclSales) {
                        loadVehicleInformation();
                        pnEditMode = oTransVchInfo.getEditMode();
                    }
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
                        if (txtField01.getText().isEmpty()) {
                            ShowMessageFX.Warning(null, "Warning", "Please select Vehicle Owner.");
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
//                        loJson = oTransVchInfo.openRecord(oTransVchInfo.getModel().getModel().getClientID());
                    if ("success".equals((String) loJson.get("result"))) {
                        loadVehicleInformation();
                        loadWareHouseHistory();
                        loadOwnerHistory();
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
                JSONObject poJSon;
//                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
//                    if (ShowMessageFX.YesNo(null, "Search Vehicle Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
//                    } else {
//                        return;
//                    }
//                }
//                poJSon = oTransVchInfo.searchRecord("", false);
////                if ("success".equals((String) poJSon.get("result"))) {
////                    loadCustomerInformation();
////                    loadAddress();
////                    loadContact();
////                    loadEmail();
////                    loadSocialMedia();
////                    loadVehicleInfoTable();
////                    loadCoOwnVehicleInfoTable();
//                    pnEditMode = oTransVchInfo.getEditMode();
//                    initFields(pnEditMode);
//                } else {
//                    ShowMessageFX.Warning(null, "Search Vehicle Information Confirmation", (String) poJSon.get("message"));
//                }
//                break;
//            case "btnClose":
//                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
//                    if (poUnload != null) {
//                        poUnload.unloadForm(AnchorMain, oApp, "Customer");
//                    } else {
//                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
//                    }
//                }
                break;
            case "btnTabAdd":
                break;
            case "btnTabRem":
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
                    ShowMessageFX.Warning(null, "Vehicle Type", "Please select `Vehicle Category` value.");
                    return false;
                } else {
//                    oTransVchInfo.getModel().getModel().setUnitType(String.valueOf((comboBox17.getSelectionModel().getSelectedIndex())));
                }
            }
        }
        return true;
    }

    private void loadVehicleInformation() {
        if (pbisVhclSales) {
//            txtField21.setText(oTransVchInfo.getModel().getModel().getDrNumber());
//            if (oTransVchInfo.getModel().getModel().getDateSold() != null && !oTransVchInfo.getModel().getModel().getDateSold().toString().isEmpty()) {
//                datePicker26.setValue(InputTextUtil.strToDate(oTransVchInfo.getModel().getModel().getDateSold()));
//            }
//              txtField23.setText(oTransVchInfo.getModel().getModel().getSoldTo());
        }

//        txtField01.setText(oTransVchInfo.getModel().getModel().getOwnName());
//        txtField03.setText(oTransVchInfo.getModel().getModel().getCoOwnName());
//        textArea02.setText(oTransVchInfo.getModel().getModel().getOwnAdd());
//        textArea04.setText(oTransVchInfo.getModel().getModel().getCoOwnAdd());
//        txtField05.setText(oTransVchInfo.getModel().getModel().getVchlMake());
//        txtField07.setText(oTransVchInfo.getModel().getModel().getVhclModl());
//        txtField09.setText(oTransVchInfo.getModel().getModel().getVhclType());
//        txtField06.setText(oTransVchInfo.getModel().getModel().getVhclTran());
//        txtField08.setText(oTransVchInfo.getModel().getModel().getVhclColr());
//        txtField10.setText(oTransVchInfo.getModel().getModel().getVhclYear());
//        txtField11.setText(oTransVchInfo.getModel().getModel().getFrmNumbr());
//        txtFieldl3.setText(oTransVchInfo.getModel().getModel().getKeyNumbr());
//        txtField15.setText(oTransVchInfo.getModel().getModel().getKeyNumbr());
//        txtField12.setText(oTransVchInfo.getModel().getModel().getCSNumber());
//        txtField14.setText(oTransVchInfo.getModel().getModel().getEngNumbr());
//        txtField16.setText(oTransVchInfo.getModel().getModel().getWrhLocat());
//        txtField19.setText(oTransVchInfo.getModel().getModel().getDtRecved());
//        txtField20.setText(oTransVchInfo.getModel().getModel().getDlvryIns());
//        txtField21.setText(oTransVchInfo.getModel().getModel().getDrNumber());
//        txtField22.setText(oTransVchInfo.getModel().getModel().getDateSold());
//        txtField23.setText(oTransVchInfo.getModel().getModel().getSoldTo());
//        txtField24.setText(oTransVchInfo.getModel().getModel().getSllrDealr());
//        txtField25.setText(oTransVchInfo.getModel().getModel().getPlcRegitr());
//        textArea27.setText(oTransVchInfo.getModel().getModel().getRemarks());
//        if (oTransVchInfo.getModel().getModel().getCvilStat() != null && !oTransVchInfo.getModel().getModel().getVchlCat().trim().isEmpty()) {
//            comboBox17.getSelectionModel().select(Integer.parseInt(oTransVchInfo.getModel().getModel().getVchlCat()));
//        }
//        if (oTransVchInfo.getModel().getModel().getCvilStat() != null && !oTransVchInfo.getModel().getModel().getVhclStat().trim().isEmpty()) {
//            comboBox18.getSelectionModel().select(Integer.parseInt(oTransVchInfo.getModel().getModel().getVhclStat()));
//        }
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
        txtField03.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField09.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        txtField15.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField14.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        txtField20.setDisable(!lbShow);
        txtField19.setDisable(!lbShow);
        txtField23.setDisable(!lbShow);
        txtField22.setDisable(!lbShow);
        txtField21.setDisable(!lbShow);
        txtField24.setDisable(!lbShow);
        txtField25.setDisable(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);

        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
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

    private void loadEngineWindow(Integer fnCodeType, Boolean fbState) {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleEngineFormatEntry.fxml"));

            VehicleEngineFormatEntryController loControl = new VehicleEngineFormatEntryController();
            loControl.setGRider(oApp);
//            loControl.setMakeID((String) oTransVchInfoVehicle.getMaster(23));
//            loControl.setMakeDesc((String) oTransVchInfoVehicle.getMaster(24));
//            loControl.setModelID((String) oTransVchInfoVehicle.getMaster(25));
//            loControl.setModelDesc((String) oTransVchInfoVehicle.getMaster(26));
//            loControl.setCodeType(fnCodeType);
//            loControl.setState(fbState);
//            loControl.setOpenEvent(true);
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

    private void loadFrameWindow(Integer fnCodeType, Boolean fbState) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleFrameFormatEntry.fxml"));
            VehicleFrameFormatEntryController loControl = new VehicleFrameFormatEntryController();
            loControl.setGRider(oApp);
//            loControl.setMakeID((String) oTransVchInfoVehicle.getMaster(23));
//            loControl.setMakeDesc((String) oTransVchInfoVehicle.getMaster(24));
//            loControl.setModelID((String) oTransVchInfoVehicle.getMaster(25));
//            loControl.setModelDesc((String) oTransVchInfoVehicle.getMaster(26));
//            loControl.setCodeType(fnCodeType);
//            loControl.setState(fbState);
//            loControl.setOpenEvent(true);
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
//        loJSON = oTransVchInfo.loadWareHouseList(oTransVchInfo.getModel().().getClientID(), false);
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
