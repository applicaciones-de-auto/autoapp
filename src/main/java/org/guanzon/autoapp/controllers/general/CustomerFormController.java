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
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Client;
import org.guanzon.auto.main.clients.Vehicle_Serial;
import org.guanzon.autoapp.models.general.ModelCustomerAddress;
import org.guanzon.autoapp.models.general.ModelCustomerEmail;
import org.guanzon.autoapp.models.general.ModelCustomerMobile;
import org.guanzon.autoapp.models.general.ModelCustomerSocialMedia;
import org.guanzon.autoapp.models.general.ModelCustomerVehicleInfo;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;
import org.guanzon.autoapp.utils.InputTextFormatterUtil;

/**
 * FXML Controller class
 *
 * @author Auto Group Programmers
 */
public class CustomerFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTrans;
    private Vehicle_Serial oTransVehicle;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Customer"; //Form Title
    private final String pModuleName = "Customer Information"; //for dialog
    private int pnEditMode;//Modifying fields for Customer Entry
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    private int iTabIndex = 0; //Set tab index

    /* ------------------DATA TABLES----------------------- */
    private ObservableList<ModelCustomerAddress> addressdata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerMobile> contactdata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerEmail> emaildata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerSocialMedia> socialmediadata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerVehicleInfo> vhclinfodata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerVehicleInfo> coownvhclinfodata = FXCollections.observableArrayList();

    /* ------------------COMBO BOX ITEMS/VALUE----------------------- */
    ObservableList<String> cCvlStat = FXCollections.observableArrayList("SINGLE", "MARRIED", "DIVORCED", "SEPARATED", "WIDOWED");
    ObservableList<String> cGender = FXCollections.observableArrayList("MALE", "FEMALE");
    ObservableList<String> cCusttype = FXCollections.observableArrayList("CLIENT", "COMPANY");
    ObservableList<String> cTitle = FXCollections.observableArrayList("MR.", "MISS", "MRS.");

    /* ------------------ANCHOR PANE----------------------- */
    @FXML
    private AnchorPane AnchorMain;
    /* ------------------TABPANE----------------------------*/
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private TabPane tabPCustCont;
    @FXML
    private Tab tabAddrInf, tabContNo, tabEmail, tabSocMed;
    /* ------------------TABS------------------------------ */
    @FXML
    private Tab tabCustomer, tabVhclInfo;
    /* ------------------TABLE VIEW------------------------ */
    @FXML
    private TableView<ModelCustomerAddress> tblAddress;
    @FXML
    private TableView<ModelCustomerEmail> tblEmail;
    @FXML
    private TableView<ModelCustomerSocialMedia> tblSocMed;
    @FXML
    private TableView<ModelCustomerMobile> tblContact;
    @FXML
    private TableView<ModelCustomerVehicleInfo> tblViewVhclInfo, tblViewCoVhclInfo;
    /* -------------------TABLE COLUMN--------------------- */
    @FXML
    private TableColumn< ModelCustomerAddress, String> addrindex01, addrindex02,
            addrindex03, addrindex04, addrindex05, addrindex06, addrindex07,
            addrindex08, addrindex09, addrindex10;

    @FXML
    private TableColumn< ModelCustomerMobile, String> contindex01, contindex02, contindex03,
            contindex04, contindex05, contindex06, contindex07;

    @FXML
    private TableColumn<ModelCustomerEmail, String> emadindex01, emadindex02, emadindex03,
            emadindex04, emadindex05;

    @FXML
    private TableColumn< ModelCustomerSocialMedia, String> socmindex01, socmindex02, socmindex03, socmindex04;
    @FXML
    private TableColumn< ModelCustomerVehicleInfo, String> tblVhcllist01, tblVhcllist02, tblVhcllist03, tblVhcllist04, tblVhcllist05, tblVhcllist06;
    @FXML
    private TableColumn< ModelCustomerVehicleInfo, String> tblCoVhcllist01, tblCoVhcllist02, tblCoVhcllist03, tblCoVhcllist04,
            tblCoVhcllist06, tblCoVhcllist05;

    /* -------------------TEXTFIELDS----------------------- */
    @FXML
    private TextField txtField02, txtField03, txtField04, txtField05, txtField06, txtField16, txtField10,
            txtField25, txtField12, txtField14, txtField13;
    /* ---------------------TEXTAREA----------------------- */
    @FXML
    private TextArea textArea15;
    /* ---------------------BUTTONS------------------------ */
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnClose;
    @FXML
    private Button btnTabAdd, btnTabRem;
    /* ---------------------COMBOBOX---------------------- */
    @FXML
    private ComboBox<String> comboBox07, comboBox08, comboBox09, comboBox18;
    /* --------------------DATEPICKER--------------------- */
    @FXML
    private DatePicker txtField11;
    /* --------------------LABEL-------------------------- */
    @FXML
    private Label lblType;
    @FXML
    private Label lblTypeValue;
    @FXML
    private TableColumn<?, ?> tblCoVhcllist07;

    /**
     * Initializes the controller class.
     *
     * @param foValue
     *
     */
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtField02.getScene().getWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the Client_Master transaction
        oTrans = new Client(oApp, false, oApp.getBranchCode());
        oTransVehicle = new Vehicle_Serial(oApp, false, oApp.getBranchCode());

        initAddress();
        initContact();
        initEmail();
        initSocialMedia();
        initVehicleInfo();
        initCoVehicleInfo();
        initComboBoxValue();
        initTextFieldPattern();
        txtField11.setOnAction(this::getDate);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        clearTables();
        InputTextUtil.addTextLimiter(txtField13, 15); // tin
        InputTextUtil.addTextLimiter(txtField14, 15); // lto
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initComboBoxValue() {
        comboBox07.setItems(cTitle);
        comboBox08.setItems(cGender);
        comboBox09.setItems(cCvlStat);
        comboBox18.setItems(cCusttype);
    }

    private void initTextFieldPattern() {
        Pattern TinOnly, textOnly, suffOnly, ltoOnly;
        TinOnly = Pattern.compile("[0-9-]*");
        textOnly = Pattern.compile("[A-Za-z ]*");
        suffOnly = Pattern.compile("[A-Za-z .]*");
        ltoOnly = Pattern.compile("[A-Za-z0-9-]*");
        txtField13.setTextFormatter(new InputTextFormatterUtil(TinOnly));
        txtField10.setTextFormatter(new InputTextFormatterUtil(textOnly)); //citezenship
        txtField02.setTextFormatter(new InputTextFormatterUtil(textOnly)); //lastname
        txtField03.setTextFormatter(new InputTextFormatterUtil(textOnly)); //firstname
        txtField04.setTextFormatter(new InputTextFormatterUtil(textOnly)); //middlename
        txtField05.setTextFormatter(new InputTextFormatterUtil(textOnly)); // maidenname
        txtField06.setTextFormatter(new InputTextFormatterUtil(suffOnly));
        txtField14.setTextFormatter(new InputTextFormatterUtil(ltoOnly));

    }

    private void getDate(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        /*CLIENT INFORMATION*/
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                oTrans.setMaster(11, SQLUtil.toDate(txtField11.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
                checkExistingCustomerInformation(true);
            }
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06,
                txtField16, txtField10, txtField25, txtField12, txtField14, txtField13);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
        /*TextArea*/
        InputTextUtil.setCapsLockBehavior(textArea15);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06,
                txtField16, txtField10, txtField25, txtField12, txtField14, txtField13);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        textArea15.setOnKeyPressed(this::textArea_KeyPressed);
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
                    case "txtField10":
                        loJSON = oTrans.searchCitizenShip(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField10.setText(oTrans.getModel().getModel().getCntryNme());
                        } else {
                            ShowMessageFX.Warning(null, pModuleName, (String) loJSON.get("message"));
                            txtField10.setText("");
                            txtField10.requestFocus();
                            return;
                        }
                        break;
                    case "txtField25":
                        loJSON = oTrans.searchSpouse(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField25.setText(oTrans.getModel().getModel().getSpouseNm());
                        } else {
                            ShowMessageFX.Warning(null, pModuleName, (String) loJSON.get("message"));
                            txtField25.setText("");
                            txtField25.requestFocus();
                            return;
                        }
                        break;
                    case "txtField12":
                        loJSON = oTrans.searchBirthPlc(lsValue);
                        if (!"error".equals(loJSON.get("result"))) {
                            txtField12.setText(oTrans.getModel().getModel().getTownName());
                        } else {
                            ShowMessageFX.Warning(null, pModuleName, (String) loJSON.get("message"));
                            txtField12.setText("");
                            txtField12.requestFocus();
                            return;
                        }
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
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField03, txtField04, txtField05, txtField06,
                txtField16, txtField10, txtField25, txtField12, txtField14, txtField13);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));

        textArea15.focusedProperty().addListener(txtArea_Focus);

    }

    private boolean checkExistingCustomerInformation(boolean fbIsClient) {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.validateExistingClientInfo(fbIsClient);
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord((String) loJSON.get("sClientID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadCustomerInformation();
                    loadAddress();
                    loadContact();
                    loadEmail();
                    loadSocialMedia();
                    loadVehicleInfoTable();
                    loadCoOwnVehicleInfoTable();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pModuleName, (String) loJSON.get("message"));
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
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
                case 16:/*company name*/
                    oTrans.getModel().getModel().setCompnyNm(lsValue);
                    checkExistingCustomerInformation(false);
                    break;
                case 2:/*last name*/
                    oTrans.getModel().getModel().setLastName(lsValue);
                    checkExistingCustomerInformation(true);
                    break;
                case 3:/*frist name*/
                    oTrans.getModel().getModel().setFirstName(lsValue);
                    checkExistingCustomerInformation(true);
                    break;
                case 4:/*middle name*/
                    oTrans.getModel().getModel().setMiddleName(lsValue);
                    break;
                case 5:/*maiden name*/
                    oTrans.getModel().getModel().setMaidenName(lsValue);
                    break;
                case 6:/*spouse */
                    oTrans.getModel().getModel().setSuffixName(lsValue);
                    break;
                case 14:
                    oTrans.getModel().getModel().setLTOID(lsValue);
                    break;
                case 13:
                    oTrans.getModel().getModel().setTaxIDNo(lsValue);
                    checkExistingCustomerInformation(false);
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
                case 15:
                    oTrans.getModel().getModel().setAddlInfo(lsValue);
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
        comboBox18.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getModel().getModel().setClientTp(String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
                comboChange();
                if (!oTrans.getModel().getModel().getClientTp().equals("0")) {
                    oTrans.getModel().getModel().setLastName("");
                    oTrans.getModel().getModel().setFirstName("");
                    oTrans.getModel().getModel().setMiddleName("");
                    oTrans.getModel().getModel().setMaidenName("");
                    oTrans.getModel().getModel().setSuffixName("");
                    oTrans.getModel().getModel().setTitle(null);
                    oTrans.getModel().getModel().setGender(null);
                    oTrans.getModel().getModel().setCvilStat(null);
                    oTrans.getModel().getModel().setCitizen("");
                    oTrans.getModel().getModel().setCntryNme("");
                    oTrans.getModel().getModel().setSpouseID("");
                    oTrans.getModel().getModel().setSpouseNm("");
                    oTrans.getModel().getModel().setTownName("");
                    oTrans.getModel().getModel().setBirthPlc("");
                    oTrans.getModel().getModel().setBirthDte(convertLocalDateToDate(LocalDate.of(1900, Month.JANUARY, 1)));
                    txtField25.setDisable(true);
                } else {
                    oTrans.getModel().getModel().setCompnyNm("");
                    txtField25.setDisable(true);
                }
                loadCustomerInformation();
            }
        });

        comboBox08.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox08.getSelectionModel().getSelectedIndex() >= 0) {
                        oTrans.getModel().getModel().setGender(String.valueOf((comboBox08.getSelectionModel().getSelectedIndex())));
                    }
                }
                txtField25.setText("");
                oTrans.getModel().getModel().setSpouseID("");
                oTrans.getModel().getModel().setSpouseNm("");

                if (comboBox09.getValue() != null) {
                    if (comboBox09.getSelectionModel().getSelectedIndex() == 1) {
                        if (comboBox08.getValue() != null) {
                            txtField25.setDisable(false);
                        }
                    } else {
                        txtField25.setDisable(true);
                    }
                } else {
                    txtField25.setDisable(true);
                }
            }
        });
        comboBox07.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox07.getSelectionModel().getSelectedIndex() >= 0) {
                        oTrans.getModel().getModel().setTitle(String.valueOf((comboBox07.getSelectionModel().getSelectedIndex())));
                    }
                }
            }
        });
        comboBox09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (!comboBox09.getValue().isEmpty()) {
                        if (comboBox09.getSelectionModel().getSelectedIndex() == 1) {
                            if (comboBox08.getValue() != null) {
                                txtField25.setDisable(false);
                            }
                        } else {
                            oTrans.getModel().getModel().setSpouseID("");
                            oTrans.getModel().getModel().setSpouseNm("");
                            txtField25.setText("");
                            txtField25.setDisable(true);
                        }
                    } else {
                        txtField25.setDisable(true);
                    }
                    if (comboBox09.getSelectionModel().getSelectedIndex() >= 0) {
                        oTrans.getModel().getModel().setCvilStat(String.valueOf((comboBox09.getSelectionModel().getSelectedIndex())));
                    }
                }
            }
        });
        txtField12.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            oTrans.getModel().getModel().setBirthPlc("");
                        }
                    }
                }
            }
        });

        txtField25.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            oTrans.getModel().getModel().setSpouseID("");
                            oTrans.getModel().getModel().setSpouseNm("");
                        }
                    }
                }
            }
        });

        txtField10.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getModel().getModel().setCitizen("");
                    }
                }
            }
        });

        //Tab Process
        tabPCustCont.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) -> {
            pnRow = 0;
            btnTabRem.setVisible(false);
        });
    }

    private void comboChange() {
        switch (comboBox18.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField16.clear(); //company name
                break;
            case 1:
                txtField02.clear(); //last name
                txtField03.clear(); //first name
                txtField04.clear(); //mid name
                txtField06.clear(); //suffix
                txtField05.clear(); //maiden
                txtField12.clear(); // birth plce
                txtField10.clear(); //citizenship
                txtField14.clear(); //LTO NO
                txtField25.clear(); // Spouse
                comboBox07.setValue("");
                comboBox08.setValue("");
                comboBox09.setValue("");
                break;
        }
        cmdCLIENTType(true);
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel,
                btnClose, btnTabAdd, btnTabRem);

        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTrans = new Client(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadCustomerInformation();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pModuleName, (String) loJSON.get("message"));
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
                if (ShowMessageFX.YesNo(null, "Customer Information Saving....", "Are you sure, do you want to save?")) {
                    LocalDate selectedDate = txtField11.getValue();
                    LocalDate currentDate = LocalDate.now();
                    Period age = Period.between(selectedDate, currentDate);
                    //check fields before saving
                    if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                        if (checkExistingCustomerInformation(true)) {
                            return;
                        }
                        if (txtField02.getText().matches("[^a-zA-Z].*")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter valid last name information.");
                            txtField02.setText("");
                            return;
                        }
                        if (txtField02.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter value last name information.");
                            txtField02.setText("");
                            return;
                        }
                        if (txtField03.getText().matches("[^a-zA-Z].*")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter valid first name information.");
                            txtField03.setText("");
                            return;
                        }
                        if (txtField03.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter value first name information.");
                            txtField03.setText("");
                            return;
                        }
                        if (txtField04.getText().matches("[^a-zA-Z].*")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter valid middle name information.");
                            txtField04.setText("");
                            return;
                        }
                        if (txtField05.getText().matches("[^a-zA-Z].*")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter valid maiden name information.");
                            txtField05.setText("");
                            return;
                        }
                        if (txtField06.getText().matches("[^a-zA-Z].*")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter valid suffix information.");
                            txtField06.setText("");
                            return;
                        }
                        if (age.getYears() < 18) {
                            ShowMessageFX.Warning(getStage(), null, pModuleName, "Less than 18 years old is not allowed.");
                            txtField11.requestFocus();
                            return;
                        }
                        if (age.getYears() >= 100) {
                            ShowMessageFX.Warning(getStage(), null, pModuleName, "Greater than 100 years old is not allowed.");
                            txtField11.requestFocus();
                            return;
                        }
                        if (comboBox09.getSelectionModel().getSelectedIndex() == 1 && comboBox07.getSelectionModel().getSelectedIndex() == 1) {
                            ShowMessageFX.Warning(getStage(), null, pModuleName, "Please choose valid Title and Civil Status.");
                            return;
                        }
                        if (comboBox08.getSelectionModel().getSelectedIndex() == 0 && comboBox07.getSelectionModel().getSelectedIndex() >= 1) {
                            ShowMessageFX.Warning(getStage(), null, pModuleName, "Please choose valid Title and Gender.");
                            return;
                        }
                        if (comboBox08.getSelectionModel().getSelectedIndex() == 1 && comboBox07.getSelectionModel().getSelectedIndex() < 1) {
                            ShowMessageFX.Warning(getStage(), null, pModuleName, "Please choose valid Title and Gender.");
                            return;
                        }
                    } else {
                        if (checkExistingCustomerInformation(false)) {
                            return;
                        }
                        if (txtField16.getText().matches("[^a-zA-Z].*")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter valid company name information.");
                            txtField16.setText("");
                            return;
                        }
                        if (txtField16.getText().trim().equals("")) {
                            ShowMessageFX.Warning(null, pModuleName, "Please enter value company name information.");
                            txtField16.setText("");
                            return;
                        }
                    }
                } else {
                    return;
                }

                if (setSelection()) {
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Customer Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadCustomerInformation();
                            loadAddress();
                            loadContact();
                            loadEmail();
                            loadSocialMedia();
                            loadVehicleInfoTable();
                            loadCoOwnVehicleInfoTable();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pModuleName, (String) loJSON.get("message"));
                        return;
                    }
                } else {
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTrans = new Client(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Customer Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadCustomerInformation();
                    loadAddress();
                    loadContact();
                    loadEmail();
                    loadSocialMedia();
                    loadVehicleInfoTable();
                    loadCoOwnVehicleInfoTable();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Customer Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Customer");
                    } else {
                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                    }
                }
                break;
            case "btnTabAdd":
                switch (iTabIndex) {
                    case 0: // Address
                        oTrans.addAddress();
                        loadAddressForm(oTrans.getAddressList().size() - 1, true);
                        loadAddress();
                        break;
                    case 1: //Mobile
                        oTrans.addMobile();
                        loadContactForm(oTrans.getMobileList().size() - 1, true);
                        loadContact();
                        break;
                    case 2: //Email
                        oTrans.addEmail();
                        loadEmailForm(oTrans.getEmailList().size() - 1, true);
                        loadEmail();
                        break;
                    case 3: // Social Media
                        oTrans.addSocialMed();
                        loadSocialMediaForm(oTrans.getSocialMediaList().size() - 1, true);
                        loadSocialMedia();
                        break;
                }
                break;
            case "btnTabRem":
                if (pnRow < 0) {
                    ShowMessageFX.Warning(null, "Warning", "No selected item");
                    return;
                }
                switch (iTabIndex) {
                    case 0: // Address
                        if (ShowMessageFX.YesNo(null, "Customer Address Confirmation", "Are you sure you want to remove this Client Address?")) {
                        } else {
                            return;
                        }
                        oTrans.removeAddress(pnRow);
                        pnRow = 0;
                        loadAddress();
                        break;
                    case 1://Mobile
                        if (ShowMessageFX.YesNo(null, "Customer Mobile Confirmation", "Are you sure you want to remove this Client Mobile?")) {
                        } else {
                            return;
                        }
                        oTrans.removeMobile(pnRow);
                        pnRow = 0;
                        loadContact();
                        break;
                    case 2://Email
                        if (ShowMessageFX.YesNo(null, "Customer Email Confirmation", "Are you sure you want to remove this  Client Email?")) {
                        } else {
                            return;
                        }
                        oTrans.removeEmail(pnRow);
                        pnRow = 0;
                        loadEmail();
                        break;
                    case 3://Social Media
                        if (ShowMessageFX.YesNo(null, "Customer Social Media Confirmation", "Are you sure you want to remove this  Client Social Media?")) {
                        } else {
                            return;
                        }
                        oTrans.removeSocialMed(pnRow);
                        pnRow = 0;
                        loadSocialMedia();
                        break;
                }
                btnTabRem.setVisible(false);
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
            if (comboBox18.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Customer Client Type", "Please select `Client Type` value.");
                clearFields();
                clearTables();
                return false;
            } else {
                oTrans.getModel().getModel().setClientTp(String.valueOf(comboBox18.getSelectionModel().getSelectedIndex()));
                if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
                    if (comboBox07.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, "Customer Title", "Please select `Title` value.");
                        comboBox07.requestFocus();
                        return false;
                    } else {
                        oTrans.setMaster(7, String.valueOf(comboBox07.getSelectionModel().getSelectedIndex()));
                    }
                    if (comboBox08.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, "Customer Gender", "Please select `Gender` value.");
                        comboBox08.requestFocus();
                        return false;
                    } else {
                        oTrans.getModel().getModel().setGender(String.valueOf(comboBox08.getSelectionModel().getSelectedIndex()));
                    }
                    if (comboBox09.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, "Customer Civil Status", "Please select `Civil Status` value.");
                        comboBox09.requestFocus();
                        return false;
                    } else {
                        oTrans.getModel().getModel().setCvilStat(String.valueOf(comboBox09.getSelectionModel().getSelectedIndex()));
                    }
                }
            }
        }
        return true;
    }

    private void clearTables() {
        addressdata.clear();
        contactdata.clear();
        emaildata.clear();
        socialmediadata.clear();
        vhclinfodata.clear();
        coownvhclinfodata.clear();
    }

    private void loadCustomerInformation() {
        comboBox18.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getClientTp()));
        txtField13.setText(oTrans.getModel().getModel().getTaxIDNo());
        txtField14.setText(oTrans.getModel().getModel().getLTOID());
        textArea15.setText(oTrans.getModel().getModel().getAddlInfo());

        if (comboBox18.getSelectionModel().getSelectedIndex() == 0) {
            lblType.setText("CLIENT ID : ");
            if (oTrans.getModel().getModel().getClientID() != null) {
                lblTypeValue.setText(oTrans.getModel().getModel().getClientID());
            } else {
                lblTypeValue.setText("");
            }
            txtField16.setText("");
            txtField02.setText(oTrans.getModel().getModel().getLastName());
            txtField03.setText(oTrans.getModel().getModel().getFirstName());
            txtField04.setText(oTrans.getModel().getModel().getMiddleName());
            txtField05.setText(oTrans.getModel().getModel().getMaidenName());
            txtField06.setText(oTrans.getModel().getModel().getSuffixName());
            if (oTrans.getModel().getModel().getTitle() != null && !oTrans.getModel().getModel().getTitle().trim().isEmpty()) {
                comboBox07.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getTitle()));
            }
            if (oTrans.getModel().getModel().getGender() != null && !oTrans.getModel().getModel().getGender().trim().isEmpty()) {
                comboBox08.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getGender()));
            }
            if (oTrans.getModel().getModel().getCvilStat() != null && !oTrans.getModel().getModel().getCvilStat().trim().isEmpty()) {
                comboBox09.getSelectionModel().select(Integer.parseInt(oTrans.getModel().getModel().getCvilStat()));
            }
            if (oTrans.getModel().getModel().getBirthDte() != null && !oTrans.getModel().getModel().getBirthDte().toString().isEmpty()) {
                txtField11.setValue(InputTextUtil.strToDate(oTrans.getMaster(11).toString()));
            }
            txtField10.setText(oTrans.getModel().getModel().getCntryNme());
            txtField12.setText(oTrans.getModel().getModel().getTownName());
        } else {
            lblType.setText("COMPANY ID : ");
            if (oTrans.getModel().getModel().getClientID() != null) {
                lblTypeValue.setText(oTrans.getModel().getModel().getClientID());
            } else {
                lblTypeValue.setText("");
            }
            txtField02.setText("");
            txtField03.setText("");
            txtField04.setText("");
            txtField05.setText("");
            txtField06.setText("");
            comboBox07.setValue(null);
            comboBox08.setValue(null);
            comboBox09.setValue(null);
            txtField11.setValue(LocalDate.of(1900, Month.JANUARY, 1));
            txtField10.setText("");
            txtField25.setText("");
            txtField25.setDisable(true);
            txtField12.setText("");
            txtField16.setText(oTrans.getModel().getModel().getCompnyNm());
        }

    }

    /*Enabling / Disabling Fields*/
    private void initFields(int fnValue) {
        pnRow = 0;
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        /*CLIENT VEHICLE INFO TAB*/
        tabVhclInfo.setDisable(fnValue == EditMode.ADDNEW);

        /*CLIENT Master*/
        comboBox18.setDisable(!lbShow); //CLIENT type
        txtField14.setDisable(!lbShow); //LTO NO
        txtField13.setDisable(!lbShow); //TIN NO
        textArea15.setDisable(!lbShow); //Remarks
        txtField02.setDisable(true); //last name
        txtField03.setDisable(true); //first name
        txtField04.setDisable(true); //mid name
        txtField06.setDisable(true); //suffix
        txtField05.setDisable(true); //maiden name
        txtField12.setDisable(true); //birth plce
        txtField10.setDisable(true); //citizenship
        txtField11.setDisable(true); //bdate
        comboBox08.setDisable(true); //Gender
        comboBox09.setDisable(true); //Civil Stat
        comboBox07.setDisable(true); //Title
        txtField25.setDisable(true); //Spouse
        txtField16.setDisable(true); //company name
        cmdCLIENTType(lbShow);

        if (lbShow) {
            if (comboBox09.getValue() != null) {
                if (!comboBox09.getValue().isEmpty()) {
                    if (comboBox09.getSelectionModel().getSelectedIndex() == 1) {
                        if (comboBox08.getValue() != null) {
                            txtField25.setDisable(false);
                        }
                    } else {
                        txtField25.setDisable(true);
                        txtField25.setText("");
                        oTrans.getModel().getModel().setSpouseID("");
                        oTrans.getModel().getModel().setSpouseNm("");
                    }
                } else {
                    txtField25.setDisable(true);
                }
            } else {
                txtField25.setDisable(true);
            }
        }

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnTabAdd.setVisible(lbShow);
        btnTabRem.setVisible(false);

        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }

        if (fnValue == EditMode.UPDATE || fnValue == EditMode.READY) {
            comboBox18.setDisable(true);
        }
    }

    private void cmdCLIENTType(boolean bValue) {
        if (bValue) {
            boolean bCust = true;
            switch (comboBox18.getSelectionModel().getSelectedIndex()) {
                case 0:
                    bCust = true;
                    break;
                case 1:
                    bCust = false;
                    break;
            }

            txtField16.setDisable(bCust); //company name
            txtField02.setDisable(!bCust); //last name
            txtField03.setDisable(!bCust); //first name
            txtField04.setDisable(!bCust); //mid name
            txtField06.setDisable(!bCust); //suffix
            txtField05.setDisable(!bCust); //maiden name
            txtField12.setDisable(!bCust); // birth plce
            txtField10.setDisable(!bCust); //citizenship
            txtField11.setDisable(!bCust); //bdate
            comboBox08.setDisable(!bCust); //Gender
            comboBox09.setDisable(!bCust); //Civil Stat
            comboBox07.setDisable(!bCust); //Title
        }
    }

    private void clearFields() {
        pnRow = 0;
        comboBox18.setValue(null);
        comboBox08.setValue(null);
        comboBox09.setValue(null);
        comboBox07.setValue(null);
        txtField11.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        lblType.setText("");
        lblTypeValue.setText("");
        txtField16.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.clear();
        txtField10.clear();
        txtField25.clear();
        txtField12.clear();
        txtField14.clear();
        txtField13.clear();
        textArea15.clear();
    }

    @FXML
    private void tblAddress_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblAddress.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblAddress.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid address information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                loadAddressForm(pnRow, false);
                loadAddress();
            }
            if (oTrans.getAddress(pnRow, "sEntryByx") == null || ((String) oTrans.getAddress(pnRow, "sEntryByx")).isEmpty()) {
                btnTabRem.setVisible(true);
            } else {
                btnTabRem.setVisible(false);
            }
        }
    }

    private void loadAddressForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow < 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerAddressForm.fxml"));
            CustomerAddressFormController loControl = new CustomerAddressFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
            loControl.setOrigProv((String) oTrans.getAddress(fnRow, 25));
            loControl.setOrigTown((String) oTrans.getAddress(fnRow, 16));
            loControl.setOrigBrgy((String) oTrans.getAddress(fnRow, 18));
            loControl.setFormStateName("Customer Information");
            loControl.setClientID(oTrans.getModel().getModel().getClientID());
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
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    /*populate Address Table*/
    private void initAddress() {
        addrindex01.setCellValueFactory(new PropertyValueFactory<>("addrindex01"));
        addrindex02.setCellValueFactory(new PropertyValueFactory<>("addrindex02"));
        addrindex03.setCellValueFactory(new PropertyValueFactory<>("addrindex03"));
        addrindex04.setCellValueFactory(new PropertyValueFactory<>("addrindex04"));
        addrindex05.setCellValueFactory(new PropertyValueFactory<>("addrindex05"));
        addrindex06.setCellValueFactory(new PropertyValueFactory<>("addrindex06"));
        addrindex07.setCellValueFactory(new PropertyValueFactory<>("addrindex07"));
        addrindex08.setCellValueFactory(new PropertyValueFactory<>("addrindex08"));
        addrindex09.setCellValueFactory(new PropertyValueFactory<>("addrindex09"));
        addrindex10.setCellValueFactory(new PropertyValueFactory<>("addrindex10"));
        tblAddress.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAddress.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        addressdata.clear();
        tblAddress.setItems(addressdata);
    }

    private void loadAddress() {
        String sAddress, sStatus, sPrimary, sCurrent, sProvincial, sOffice;
        addressdata.clear();
        /*Set Values to Class Address Master*/
        for (lnCtr = 0; lnCtr <= oTrans.getAddressList().size() - 1; lnCtr++) {
            sAddress = oTrans.getAddress(lnCtr, "sAddressx").toString() + " " + oTrans.getAddress(lnCtr, "sBrgyName").toString() + " " + oTrans.getAddress(lnCtr, "sTownName").toString() + ", " + oTrans.getAddress(lnCtr, "sProvName").toString();
            if (oTrans.getAddress(lnCtr, "cPrimaryx").toString().equals("1")) {
                sPrimary = "Y";
            } else {
                sPrimary = "N";
            }
            if (oTrans.getAddress(lnCtr, "cCurrentx").toString().equals("1")) {
                sCurrent = "Y";
            } else {
                sCurrent = "N";
            }
            if (oTrans.getAddress(lnCtr, "cProvince").toString().equals("1")) {
                sProvincial = "Y";
            } else {
                sProvincial = "N";
            }
            if (oTrans.getAddress(lnCtr, "cOfficexx").toString().equals("1")) {
                sOffice = "Y";
            } else {
                sOffice = "N";
            }
            if (oTrans.getAddress(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACTIVE";
            }
            if (sAddress != null) {
                addressdata.add(new ModelCustomerAddress(
                        String.valueOf(lnCtr + 1), //ROW
                        sPrimary,
                        oTrans.getAddress(lnCtr, "sHouseNox").toString(),
                        sAddress.trim().toUpperCase(),
                        oTrans.getAddress(lnCtr, "sZippCode").toString(),
                        sCurrent,
                        sProvincial,
                        sOffice,
                        sStatus,
                        oTrans.getAddress(lnCtr, "sRemarksx").toString()
                ));
            }
        }

    }

    @FXML
    private void tblContact_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblContact.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblContact.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid contact no. information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                loadContactForm(pnRow, false);
                loadContact();
            }
            if (oTrans.getMobile(pnRow, "sEntryByx") == null || oTrans.getMobile(pnRow, "sEntryByx").toString().isEmpty()) {
                btnTabRem.setVisible(true);
            } else {
                btnTabRem.setVisible(false);
            }
        }
    }

    private void loadContactForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow < 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerContactForm.fxml"));
            CustomerContactFormController loControl = new CustomerContactFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
            fxmlLoader.setController(loControl);
            loControl.setFormStateName("Customer Information");
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
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private void loadContact() {
        String sOwnership, sPrimary, sType, sStatus;
        contactdata.clear();
        /*Set Values to Class Mobile Master*/
        for (lnCtr = 0; lnCtr <= oTrans.getMobileList().size() - 1; lnCtr++) {
            switch (oTrans.getMobile(lnCtr, "cOwnerxxx").toString()) {
                case "0":
                    sOwnership = "PERSONAL";
                    break;
                case "1":
                    sOwnership = "OFFICE";
                    break;
                default:
                    sOwnership = "OTHERS";
                    break;
            }
            switch (oTrans.getMobile(lnCtr, "cMobileTp").toString()) {
                case "0":
                    sType = "MOBILE";
                    break;
                case "1":
                    sType = "TELEPHONE";
                    break;
                default:
                    sType = "FAX";
                    break;
            }
            if (oTrans.getMobile(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACITVE";
            }

            if (oTrans.getMobile(lnCtr, "cPrimaryx").toString().equals("1")) {
                sPrimary = "Y";
            } else {
                sPrimary = "N";
            }

            if (oTrans.getMobile(lnCtr, "sMobileNo") != null
                    || !oTrans.getMobile(lnCtr, "sMobileNo").toString().isEmpty()) {
                contactdata.add(new ModelCustomerMobile(
                        String.valueOf(lnCtr + 1), //ROW
                        sPrimary,
                        sOwnership, //OWNERSHIP
                        sType,
                        oTrans.getMobile(lnCtr, "sMobileNo").toString(), //NUMBER
                        sStatus,
                        oTrans.getMobile(lnCtr, "sRemarksx").toString()
                ));
            }
        }
    }

    /*populate Contact Table*/
    private void initContact() {
        contindex01.setCellValueFactory(new PropertyValueFactory<>("contindex01"));
        contindex02.setCellValueFactory(new PropertyValueFactory<>("contindex02"));
        contindex03.setCellValueFactory(new PropertyValueFactory<>("contindex03"));
        contindex04.setCellValueFactory(new PropertyValueFactory<>("contindex04"));
        contindex05.setCellValueFactory(new PropertyValueFactory<>("contindex05"));
        contindex06.setCellValueFactory(new PropertyValueFactory<>("contindex06"));
        contindex07.setCellValueFactory(new PropertyValueFactory<>("contindex07"));
        tblContact.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblContact.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        contactdata.clear();
        tblContact.setItems(contactdata);

    }

    @FXML
    private void tblEmail_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblEmail.getSelectionModel().getSelectedIndex();

            // Check if no row is selected
            if (pnRow < 0) {
                ShowMessageFX.Warning(getStage(), "Please select valid email address information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                loadEmailForm(pnRow, false);
                loadEmail();
            }
            // Check if the email entry is empty or null
            if (oTrans.getEmail(pnRow, "sEntryByx") == null || oTrans.getEmail(pnRow, "sEntryByx").toString().isEmpty()) {
                btnTabRem.setVisible(true);
            } else {
                btnTabRem.setVisible(false);
            }
        }
    }

    private void loadEmailForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow < 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerEmailForm.fxml"));
            CustomerEmailFormController loControl = new CustomerEmailFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            loControl.setFormStateName("Customer Information");
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
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private void loadEmail() {
        String sOwnership, sPrimary, sStatus;
        emaildata.clear();
        /*Set Values to Class Mobile Master*/
        for (lnCtr = 0; lnCtr <= oTrans.getEmailList().size() - 1; lnCtr++) {
            switch (oTrans.getEmail(lnCtr, "cOwnerxxx").toString()) {
                case "0":
                    sOwnership = "PERSONAL";
                    break;
                case "1":
                    sOwnership = "OFFICE";
                    break;
                default:
                    sOwnership = "OTHERS";
                    break;
            }
            if (oTrans.getEmail(lnCtr, "cPrimaryx").toString().equals("1")) {
                sPrimary = "Y";
            } else {
                sPrimary = "N";
            }
            if (oTrans.getEmail(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACTIVE";
            }
            emaildata.add(new ModelCustomerEmail(
                    String.valueOf(lnCtr + 1), //ROW
                    sPrimary,
                    sOwnership, //OWNERSHIP
                    oTrans.getEmail(lnCtr, "sEmailAdd").toString(), //EMAIL
                    sStatus
            ));
        }
    }

    /*populate Email Table*/
    private void initEmail() {
        emadindex01.setCellValueFactory(new PropertyValueFactory<>("emadindex01"));
        emadindex02.setCellValueFactory(new PropertyValueFactory<>("emadindex02"));
        emadindex03.setCellValueFactory(new PropertyValueFactory<>("emadindex03"));
        emadindex04.setCellValueFactory(new PropertyValueFactory<>("emadindex04"));
        emadindex05.setCellValueFactory(new PropertyValueFactory<>("emadindex05"));
        tblEmail.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblEmail.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        emaildata.clear();
        tblEmail.setItems(emaildata);
    }

    @FXML
    private void tblSocMed_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblSocMed.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblSocMed.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid social media information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                loadSocialMediaForm(pnRow, false);
                loadSocialMedia();
            }
            if (oTrans.getSocialMed(pnRow, "sEntryByx") == null || oTrans.getSocialMed(pnRow, "sEntryByx").toString().isEmpty()) {
                btnTabRem.setVisible(true);
            } else {
                btnTabRem.setVisible(false);
            }
        }
    }

    private void loadSocialMediaForm(Integer fnRow, boolean isAdd) {
        try {
            if (fnRow < 0) {
                ShowMessageFX.Warning(getStage(), "Invalid Table Row to Set. ", "Warning", null);
                return;
            }
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerSocialMediaForm.fxml"));
            CustomerSocialMediaFormController loControl = new CustomerSocialMediaFormController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            loControl.setFormStateName("Customer Information");
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
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private void loadSocialMedia() {
        String sSocType, sStatus = "";
        socialmediadata.clear();
        /*Set Values to Class Mobile Master*/
        for (lnCtr = 0; lnCtr <= oTrans.getSocialMediaList().size() - 1; lnCtr++) {
            switch (oTrans.getSocialMed(lnCtr, "cSocialTp").toString()) {
                case "0":
                    sSocType = "FACEBOOK";
                    break;
                case "1":
                    sSocType = "WHATSAPP";
                    break;
                case "2":
                    sSocType = "INSTAGRAM";
                    break;
                case "3":
                    sSocType = "TIKTOK";
                    break;
                case "4":
                    sSocType = "TWITTER";
                    break;
                default:
                    sSocType = "OTHERS";
                    break;
            }

            if (oTrans.getSocialMed(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACTIVE";
            }
            if (!oTrans.getSocialMed(lnCtr, "sAccountx").toString().trim().equals("")
                    || !oTrans.getSocialMed(lnCtr, "sAccountx").toString().trim().isEmpty()) {
                socialmediadata.add(new ModelCustomerSocialMedia(
                        String.valueOf(lnCtr + 1), //ROW
                        sSocType, //Social Type
                        oTrans.getSocialMed(lnCtr, "sAccountx").toString(), //Acount
                        sStatus
                ));
            }
        }
    }

    /*populate Social Media Table*/
    private void initSocialMedia() {
        socmindex01.setCellValueFactory(new PropertyValueFactory<>("socmindex01"));
        socmindex02.setCellValueFactory(new PropertyValueFactory<>("socmindex02"));
        socmindex03.setCellValueFactory(new PropertyValueFactory<>("socmindex03"));
        socmindex04.setCellValueFactory(new PropertyValueFactory<>("socmindex04"));

        tblSocMed.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblSocMed.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        socialmediadata.clear();
        tblSocMed.setItems(socialmediadata);
    }

    @FXML
    private void tblViewVhclInfo_Clicked(MouseEvent event
    ) {
    }

    private String getVehicleInfo(String fsValue, Integer rowCounter, String fsCol) {
        fsValue = "";
        if (oTransVehicle.getVehicleSerial(rowCounter, fsCol) != null) {
            fsValue = String.valueOf(oTransVehicle.getVehicleSerial(rowCounter, fsCol));
        }
        return fsValue;
    }

    private void loadVehicleInfoTable() {
        JSONObject loJSON;
        vhclinfodata.clear();
        loJSON = oTransVehicle.LoadVehicleList(oTrans.getModel().getModel().getClientID(), true);
        if ("success".equals((String) loJSON.get("result"))) {
            for (lnCtr = 0; lnCtr <= oTransVehicle.getVehicleSerialList().size() - 1; lnCtr++) {
                vhclinfodata.add(new ModelCustomerVehicleInfo(
                        String.valueOf(lnCtr + 1), //ROW
                        getVehicleInfo("fsCSNo", lnCtr, "sCSNoxxxx"),
                        getVehicleInfo("fsPlateNo", lnCtr, "sPlateNox"),
                        getVehicleInfo("fsDescript", lnCtr, "sDescript"),
                        getVehicleInfo("fsCompany", lnCtr, "sCOwnerNm"),
                        getVehicleInfo("fsDealerNm", lnCtr, "sDealerNm"),
                        "",
                        "",
                        ""
                ));
            }
        }
    }

    /*populate vheicle information Table*/
    private void initVehicleInfo() {
        tblVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblVhcllist06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));

        tblViewVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewVhclInfo.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewVhclInfo.setItems(vhclinfodata);
    }

    private void loadCoOwnVehicleInfoTable() {
        JSONObject loJSON;
        coownvhclinfodata.clear();
        loJSON = oTransVehicle.LoadVehicleList(oTrans.getModel().getModel().getClientID(), false);
        /*Set Values to table from vehicle info table*/
        if ("success".equals((String) loJSON.get("result"))) {
            for (lnCtr = 0; lnCtr <= oTransVehicle.getVehicleSerialList().size() - 1; lnCtr++) {
                coownvhclinfodata.add(new ModelCustomerVehicleInfo(
                        String.valueOf(lnCtr + 1), //ROW
                        getVehicleInfo("fsCSNo", lnCtr, "sCSNoxxxx"),
                        getVehicleInfo("fsPlateNo", lnCtr, "sPlateNox"),
                        getVehicleInfo("fsDescript", lnCtr, "sDescript"),
                        getVehicleInfo("fsCompany", lnCtr, "sOwnerNmx"),
                        getVehicleInfo("fsDealerNm", lnCtr, "sDealerNm"),
                        "",
                        "",
                        ""
                ));
            }
        }
    }

    /*populate vheicle information Table*/
    private void initCoVehicleInfo() {
        tblCoVhcllist01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblCoVhcllist02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblCoVhcllist03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblCoVhcllist04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblCoVhcllist05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblCoVhcllist06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));

        tblViewCoVhclInfo.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewCoVhclInfo.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblViewCoVhclInfo.setItems(coownvhclinfodata);
    }
}
