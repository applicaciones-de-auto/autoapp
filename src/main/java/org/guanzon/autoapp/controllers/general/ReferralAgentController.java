package org.guanzon.autoapp.controllers.general;

import org.guanzon.autoapp.controllers.components.ViewPhotoController;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Client;
import org.guanzon.auto.main.clients.Sales_Agent;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.models.general.CustomerAddress;
import org.guanzon.autoapp.models.general.CustomerEmail;
import org.guanzon.autoapp.models.general.CustomerMobile;
import org.guanzon.autoapp.models.general.CustomerSocialMedia;
import org.guanzon.autoapp.models.general.RefAgentRequirements;
import org.guanzon.autoapp.models.general.RefAgentTrans;
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
public class ReferralAgentController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Client oTransClient;
    private Sales_Agent oTrans;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Referral Agent"; //Form Title
    private int pnEditMode;
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int iTabIndex = 0; //Set tab index
    private String psFileName = "";
    private String psFileUrl = "";
    private String imgIdentfier = "";
    private boolean pbIsBrowse = false;
    private ObservableList<CustomerAddress> addressdata = FXCollections.observableArrayList();
    private ObservableList<CustomerMobile> contactdata = FXCollections.observableArrayList();
    private ObservableList<CustomerEmail> emaildata = FXCollections.observableArrayList();
    private ObservableList<CustomerSocialMedia> socialmediadata = FXCollections.observableArrayList();
    private ObservableList<RefAgentTrans> transData = FXCollections.observableArrayList();
    private ObservableList<RefAgentRequirements> agentReqData = FXCollections.observableArrayList();
    ObservableList<String> cCvlStat = FXCollections.observableArrayList("SINGLE", "MARRIED", "DIVORCED", "SEPARATED", "WIDOWED");
    ObservableList<String> cGender = FXCollections.observableArrayList("MALE", "FEMALE");
    ObservableList<String> cReferTyp = FXCollections.observableArrayList("AGENT", "BANK", "EMPLOYEE", "STOCKHOLDER");
    ObservableList<String> cTitle = FXCollections.observableArrayList("MR.", "MISS", "MRS.");
    ObservableList<String> cEducAttain = FXCollections.observableArrayList("UNDERGRADUATE", "HIGH SCHOOL GRADUATE", "COLLEGE GRADUATE");

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnApprove, btnDisapprove, btnClose, btnUploadImage, btnRemoveImage, btnTabAdd, btnTabRem;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField12, txtField13, txtField14;
    @FXML
    private TabPane tabPaneMain, tabPCustCont;
    @FXML
    private DatePicker datePicker09;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> comboBox06, comboBox07, comboBox08, comboBox10, comboBox11;
    @FXML
    private Tab tabAddrInf, tabContNo, tabEmail, tabSocMed, tabReferral;
    @FXML
    private TableView<CustomerAddress> tblAddress;
    @FXML
    private TableColumn<CustomerAddress, String> addrindex01, addrindex02, addrindex03, addrindex04, addrindex05, addrindex06,
            addrindex07, addrindex08, addrindex09, addrindex10;
    @FXML
    private TableView<CustomerMobile> tblContact;
    @FXML
    private TableColumn<CustomerMobile, String> contindex01, contindex02, contindex03, contindex04, contindex05, contindex06, contindex07;
    @FXML
    private TableView<CustomerEmail> tblEmail;
    @FXML
    private TableColumn<CustomerEmail, String> emadindex01, emadindex02, emadindex03, emadindex04, emadindex05;
    @FXML
    private TableView<CustomerSocialMedia> tblSocMed;
    @FXML
    private TableColumn<CustomerSocialMedia, String> socmindex01, socmindex02, socmindex03, socmindex04;
    @FXML
    private TableView<RefAgentRequirements> tblRequirementsInfo;
    @FXML
    private TableColumn<RefAgentRequirements, Boolean> rqrmIndex01;
    @FXML
    private TableColumn<RefAgentRequirements, String> rqrmIndex02;
    @FXML
    private TableColumn<RefAgentRequirements, String> rqrmIndex03;
    @FXML
    private TableColumn<RefAgentRequirements, String> rqrmIndex04;
    @FXML
    private TableView<RefAgentTrans> tblTransaction;
    @FXML
    private TableColumn<RefAgentTrans, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09;
    @FXML
    private Label lblStatus;

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
        oTransClient = new Client(oApp, false, oApp.getBranchCode());
        oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
        initAddress();
        initContact();
        initEmail();
        initSocialMedia();
        initAgentRequirements();
        initAgentTransaction();

        initCapitalizationFields();
        initPatternFields();
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField12, txtField13, txtField14);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        String lsRefAgnt = "";
        if (oTrans.getModel().getModel().getClientID() != null) {
            lsRefAgnt = oTrans.getModel().getModel().getClientID();
        }
        txtField01.setText(lsRefAgnt);

        txtField02.setText(oTransClient.getModel().getModel().getLastName());
        txtField03.setText(oTransClient.getModel().getModel().getFirstName());
        txtField04.setText(oTransClient.getModel().getModel().getMiddleName());
        txtField05.setText(oTransClient.getModel().getModel().getSuffixName());
        if (oTransClient.getModel().getModel().getTitle() != null && !oTransClient.getModel().getModel().getTitle().trim().isEmpty()) {
            comboBox06.getSelectionModel().select(Integer.parseInt(oTransClient.getModel().getModel().getTitle()));
        }
        if (oTransClient.getModel().getModel().getGender() != null && !oTransClient.getModel().getModel().getGender().trim().isEmpty()) {
            comboBox07.getSelectionModel().select(Integer.parseInt(oTransClient.getModel().getModel().getGender()));
        }
        if (oTransClient.getModel().getModel().getCvilStat() != null && !oTransClient.getModel().getModel().getCvilStat().trim().isEmpty()) {
            comboBox08.getSelectionModel().select(Integer.parseInt(oTransClient.getModel().getModel().getCvilStat()));
        }
        if (oTransClient.getModel().getModel().getBirthDte() != null && !oTransClient.getModel().getModel().getBirthDte().toString().isEmpty()) {
            datePicker09.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransClient.getModel().getModel().getBirthDte())));
        }
        if (oTrans.getModel().getModel().getRecdStat() != null) {
            switch (oTrans.getModel().getModel().getRecdStat()) {
                case "1":
                    lblStatus.setText("Approved");
                    break;
                case "2":
                    lblStatus.setText("Disapproved");
                    break;
                default:
                    lblStatus.setText("Inactive");
                    break;
            }
        }
        if (oTrans.getModel().getModel().getAgentTyp() != null && !oTrans.getModel().getModel().getAgentTyp().trim().isEmpty()) {
            switch (String.valueOf(oTrans.getModel().getModel().getAgentTyp())) {
                case "reg":
                    comboBox10.setValue("AGENT");
                    break;
                case "bnk":
                    comboBox10.setValue("BANK");
                    break;
                case "emp":
                    comboBox10.setValue("EMPLOYEE");
                    break;
                case "stk":
                    comboBox10.setValue("STOCKHOLDER");
                    break;
                default:
                    comboBox10.setValue("");
                    break;
            }
        }
//        if (oTrans.getModel().getModel().getEducAttain!= null && !oTransferal.getModel().getModel().getgetEducAttain().trim().isEmpty()) {
//            comboBox11.getSelectionModel().select(Integer.parseInt(oTransferal.getModel().getModel().getgetEducAttain()));
//        }
        comboBox11.setValue("");
        txtField12.setText(oTrans.getModel().getModel().getProfessn());
        txtField13.setText(oTrans.getModel().getModel().getCompany());
        txtField14.setText(oTrans.getModel().getModel().getPosition());
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern textOnly, suffOnly;
        textOnly = Pattern.compile("[A-Za-z ]*");
        suffOnly = Pattern.compile("[A-Za-z .]*");
        List<TextField> loTextField = Arrays.asList(txtField02, txtField03, txtField04);
        loTextField.forEach(tf -> tf.setTextFormatter(new TextFormatterUtil(textOnly)));
        txtField05.setTextFormatter(new TextFormatterUtil(suffOnly));
    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField12, txtField13, txtField14);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
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
                case 2:/*last name*/
                    oTransClient.getModel().getModel().setLastName(lsValue);
                    if (pnEditMode == EditMode.ADDNEW) {
                        checkExistingReferralAgentInformation();
                    }
                    break;
                case 3:/*first name*/
                    oTransClient.getModel().getModel().setFirstName(lsValue);
                    if (pnEditMode == EditMode.ADDNEW) {
                        checkExistingReferralAgentInformation();
                    }
                    break;
                case 4:/*middle name*/
                    oTransClient.getModel().getModel().setMiddleName(lsValue);
                    break;
                case 5:/*Suffix Name*/
                    oTransClient.getModel().getModel().setSuffixName(lsValue);
                    break;
                case 12:/*Profession */
                    oTrans.getModel().getModel().setProfessn(lsValue);
                    break;
                case 13:/*Company */
                    oTrans.getModel().getModel().setCompany(lsValue);
                    break;
                case 14:/*Position */
                    oTrans.getModel().getModel().setPosition(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();

        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField12, txtField13, txtField14);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
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
                    case F3:
                        switch (txtFieldID) {
                            case "txtField01":
                                if (pnEditMode == EditMode.ADDNEW) {
                                    loJSON = oTransClient.searchClient(lsValue, true);
                                    if (!"error".equals(loJSON.get("result"))) {
                                        oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
                                        loJSON = oTrans.newRecord();
                                        if (!"error".equals(loJSON.get("result"))) {
                                            oTrans.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
                                            loadMasterFields();
                                            loadAddress();
                                            loadContact();
                                            loadEmail();
                                            loadSocialMedia();
                                            loadAgentTrans();
                                            loadAgentRequirements();
                                            pnEditMode = oTrans.getEditMode();
                                            initRefFields(pnEditMode);
                                        } else {
                                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                            return;
                                        }
                                    } else {
                                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                        return;
                                    }
                                }
                                break;
//                    case "txtField02":
//                        if (pnEditMode == EditMode.ADDNEW) {
//                            loJSON = oTransClient.searchClient(lsValue, false);
//                            if (!"error".equals(loJSON.get("result"))) {
//                                oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
//                                loJSON = oTrans.newRecord();
//                                if (!"error".equals(loJSON.get("result"))) {
//                                    oTrans.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
//                                    loadMasterFields();
//                                    loadAddress();
//                                    loadContact();
//                                    loadEmail();
//                                    loadSocialMedia();
//                                    loadAgentTrans();
//                                    loadAgentRequirements();
//                                    pnEditMode = oTransClient.getEditMode();
//                                } else {
//                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                    return;
//                                }
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                return;
//                            }
//                        }
//                        break;
//                    case "txtField03":
//                        if (pnEditMode == EditMode.ADDNEW) {
//                            loJSON = oTransClient.searchClient(lsValue, false);
//                            if (!"error".equals(loJSON.get("result"))) {
//                                oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
//                                loJSON = oTrans.newRecord();
//                                if (!"error".equals(loJSON.get("result"))) {
//                                    oTrans.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
//                                    loadMasterFields();
//                                    loadAddress();
//                                    loadContact();
//                                    loadEmail();
//                                    loadSocialMedia();
//                                    loadAgentTrans();
//                                    loadAgentRequirements();
//                                    pnEditMode = oTransClient.getEditMode();
//                                } else {
//                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                    return;
//                                }
//                            } else {
//                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                                return;
//                            }
//                        }
//                        break;
                        }
                        event.consume();
                        CommonUtils.SetNextFocus((TextField) event.getSource());
                        break;
                    case UP:
                        event.consume();
                        CommonUtils.SetPreviousFocus((TextField) event.getSource());
                        break;
                    case ENTER:
                        event.consume();
                        CommonUtils.SetNextFocus((TextField) event.getSource());
                        break;
                    case TAB:
                        event.consume();
                        CommonUtils.SetNextFocus((TextField) event.getSource());
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
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnApprove, btnDisapprove, btnClose, btnUploadImage, btnRemoveImage, btnTabAdd, btnTabRem);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        iTabIndex = tabPCustCont.getSelectionModel().getSelectedIndex();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTransClient = new Client(oApp, false, oApp.getBranchCode());
                loJSON = oTransClient.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
                    loJSON = oTrans.newRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        pnEditMode = oTransClient.getEditMode();
                        loadMasterFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return;
                }
                break;
            case "btnEdit":
                loJSON = oTransClient.updateRecord();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                } else {
                    loJSON = oTrans.updateRecord();
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                    }
                }
                pnEditMode = oTransClient.getEditMode();
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to save?")) {
                    LocalDate selectedDate = datePicker09.getValue();
                    LocalDate currentDate = LocalDate.now();
                    Period age = Period.between(selectedDate, currentDate);
//                    if (checkExistingReferralAgentInformation()) {
//                        return;
//                    }
                    if (txtField01.getText().matches("[^a-zA-Z0-9].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid agent id information.");
                        txtField01.setText("");
                        return;
                    }
                    if (txtField02.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid last name information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter value last name information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField03.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid first name information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField03.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter value first name information.");
                        txtField03.setText("");
                        return;
                    }
                    if (txtField04.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid middle name information.");
                        txtField04.setText("");
                        return;
                    }
                    if (txtField05.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid maiden name information.");
                        txtField05.setText("");
                        return;
                    }
                    if (txtField05.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid suffix information.");
                        txtField05.setText("");
                        return;
                    }
                    if (age.getYears() < 18) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Less than 18 years old is not allowed.");
                        return;
                    }
                    if (age.getYears() >= 100) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Greater than 100 years old is not allowed.");
                        return;
                    }
                    if (comboBox08.getSelectionModel().getSelectedIndex() == 1 && comboBox06.getSelectionModel().getSelectedIndex() == 1) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please choose valid Title and Civil Status.");
                        return;
                    }
                    if (comboBox07.getSelectionModel().getSelectedIndex() == 0 && comboBox06.getSelectionModel().getSelectedIndex() >= 1) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please choose valid Title and Gender.");
                        return;
                    }
                    if (comboBox07.getSelectionModel().getSelectedIndex() == 1 && comboBox06.getSelectionModel().getSelectedIndex() < 1) {
                        ShowMessageFX.Warning(getStage(), null, pxeModuleName, "Please choose valid Title and Gender.");
                        return;
                    }
                } else {
                    return;
                }
                if (setSelection()) {
                    String sClientID = String.valueOf(oTransClient.getModel().getModel().getClientID());
                    loJSON = oTransClient.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        oTrans.getModel().getModel().setClientID(sClientID);
                        loJSON = oTrans.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                            loJSON = oTransClient.openRecord(sClientID);
                            if ("success".equals((String) loJSON.get("result"))) {
                                loJSON = oTrans.openRecord(sClientID);
                                if ("success".equals((String) loJSON.get("result"))) {
                                    loadMasterFields();
                                    loadAddress();
                                    loadContact();
                                    loadEmail();
                                    loadSocialMedia();
                                    loadAgentTrans();
                                    loadAgentRequirements();
                                    pnEditMode = oTransClient.getEditMode();
                                    initFields(pnEditMode);
                                }
                            }
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                } else {
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        oTransClient = new Client(oApp, false, oApp.getBranchCode());
                        oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                        clearFields();
                        clearTables();
                    } else {
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loJSON = oTransClient.openRecord(oTrans.getModel().getModel().getClientID());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loadMasterFields();
                                loadAddress();
                                loadContact();
                                loadEmail();
                                loadSocialMedia();
                                loadAgentTrans();
                                loadAgentRequirements();
                                pnEditMode = oTransClient.getEditMode();
                                initFields(pnEditMode);
                            }
                        }
                    }

                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pbIsBrowse = true;
                        if (pnEditMode == EditMode.ADDNEW) {
                            pnEditMode = EditMode.ADDNEW;
                        }

                        if (pnEditMode == EditMode.UPDATE) {
                            pnEditMode = EditMode.UPDATE;
                        }

                    } else {
                        pnEditMode = oTransClient.getEditMode();
                        return;
                    }
                }
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    oTransClient.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadAddress();
                        loadContact();
                        loadEmail();
                        loadSocialMedia();
                        loadAgentTrans();
                        loadAgentRequirements();
                        pnEditMode = oTransClient.getEditMode();
                        initFields(pnEditMode);
                    }
                } else {
                    pbIsBrowse = false;
                    ShowMessageFX.Warning(null, "Search Referral Agent Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Referral Agent Information");
                    } else {
                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                    }
                }
                break;
            case "btnTabAdd":
                switch (iTabIndex) {
                    case 0: // Address
                        oTransClient.addAddress();
                        loadAddressForm(oTransClient.getAddressList().size() - 1, true);
                        loadAddress();
                        break;
                    case 1: //Mobile
                        oTransClient.addMobile();
                        loadContactForm(oTransClient.getMobileList().size() - 1, true);
                        loadContact();
                        break;
                    case 2: //Email
                        oTransClient.addEmail();
                        loadEmailForm(oTransClient.getEmailList().size() - 1, true);
                        loadEmail();
                        break;
                    case 3: // Social Media
                        oTransClient.addSocialMed();
                        loadSocialMediaForm(oTransClient.getSocialMediaList().size() - 1, true);
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
                        if (ShowMessageFX.YesNo(null, "Referral Agent Address Confirmation", "Are you sure you want to remove this Referral Agent Address?")) {
                        } else {
                            return;
                        }
                        oTransClient.removeAddress(pnRow);
                        pnRow = 0;
                        loadAddress();
                        break;
                    case 1://Mobile
                        if (ShowMessageFX.YesNo(null, "Referral Agent Mobile Confirmation", "Are you sure you want to remove this Referral Agent Mobile?")) {
                        } else {
                            return;
                        }
                        oTransClient.removeMobile(pnRow);
                        pnRow = 0;
                        loadContact();
                        break;
                    case 2://Email
                        if (ShowMessageFX.YesNo(null, "Referral Agent Email Confirmation", "Are you sure you want to remove this Referral Agent Email?")) {
                        } else {
                            return;
                        }
                        oTransClient.removeEmail(pnRow);
                        pnRow = 0;
                        loadEmail();
                        break;
                    case 3://Social Media
                        if (ShowMessageFX.YesNo(null, "Referral Agent Social Media Confirmation", "Are you sure you want to remove this Referral Agent Social Media?")) {
                        } else {
                            return;
                        }
                        oTransClient.removeSocialMed(pnRow);
                        pnRow = 0;
                        loadSocialMedia();
                        break;
                }
                btnTabRem.setVisible(false);
                btnTabRem.setManaged(false);
                break;
            case "btnDisapprove":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to cancel this activity?") == true) {
                    String fsValue = oTrans.getModel().getModel().getClientID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }

                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loJSON = oTransClient.openRecord(oTrans.getModel().getModel().getClientID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadAddress();
                            loadContact();
                            loadEmail();
                            loadSocialMedia();
                            loadAgentTrans();
                            loadAgentRequirements();
                            pnEditMode = oTransClient.getEditMode();
                            initFields(pnEditMode);
                        }
                    }
                }
                break;
            case "btnApprove":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to change status?")) {
                    String fsValue = oTrans.getModel().getModel().getClientID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }

                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loJSON = oTransClient.openRecord(oTrans.getModel().getModel().getClientID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadAddress();
                            loadContact();
                            loadEmail();
                            loadSocialMedia();
                            loadAgentTrans();
                            loadAgentRequirements();
                            pnEditMode = oTransClient.getEditMode();
                            initFields(pnEditMode);
                        }
                    }
                }
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {
        comboBox06.setItems(cTitle);
        comboBox07.setItems(cGender);
        comboBox08.setItems(cCvlStat);
        comboBox10.setItems(cReferTyp);
        comboBox11.setItems(cEducAttain);
    }

    @Override
    public void initFieldsAction() {
        datePicker09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransClient.setMaster(11, SQLUtil.toDate(datePicker09.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
                if (!pbIsBrowse) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        checkExistingReferralAgentInformation();
                    }
                }
            }
        });
        comboBox06.setOnAction(e -> {
            int selectedComboBox06 = comboBox06.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox06 >= 0) {
                    oTransClient.getModel().getModel().setTitle(String.valueOf(selectedComboBox06));
                }
            }
        }
        );
        comboBox07.setOnAction(e -> {
            int selectedComboBox07 = comboBox07.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox07 >= 0) {
                    oTransClient.getModel().getModel().setGender(String.valueOf(selectedComboBox07));
                }
            }
        }
        );
        comboBox08.setOnAction(e -> {
            int selectedComboBox08 = comboBox08.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox08 >= 0) {
                    oTransClient.getModel().getModel().setCvilStat(String.valueOf(selectedComboBox08));
                }
            }
        }
        );
        comboBox10.setOnAction(e -> {
            int selectedComboBox10 = comboBox10.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox10 >= 0) {
                    switch (selectedComboBox10) {
                        case 0:
                            oTrans.getModel().getModel().setAgentTyp("reg");
                            break;
                        case 1:
                            oTrans.getModel().getModel().setAgentTyp("bnk");
                            break;
                        case 2:
                            oTrans.getModel().getModel().setAgentTyp("emp");
                            break;
                        case 3:
                            oTrans.getModel().getModel().setAgentTyp("stk");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        );
        tabPCustCont.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) -> {
            pnRow = 0;
            btnTabRem.setVisible(false);
            btnTabRem.setManaged(false);
        }
        );
    }

    @Override
    public void initTextFieldsProperty() {
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTransClient.getModel().getModel().setClientID("");
                                oTrans.getModel().getModel().setClientID("");
                            }
                        }
                    }
                }
                );
    }

    @Override
    public void clearTables() {
        addressdata.clear();
        contactdata.clear();
        emaildata.clear();
        socialmediadata.clear();
        transData.clear();
        agentReqData.clear();
    }

    @Override
    public void clearFields() {
        Image loImageError = new Image("file:D:/Integrated Automotive System/autoapp/src/main/resources/org/guanzon/autoapp/images/defaultimage.png");
        imageView.setImage(loImageError);
        psFileUrl = "";
        psFileName = "";
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03,
                txtField04, txtField05, txtField12, txtField13, txtField14);

        CustomCommonUtil.setValue(null, comboBox06, comboBox07, comboBox08,
                comboBox10, comboBox11);
        datePicker09.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        lblStatus.setText("");
    }

    @Override
    public void initFields(int fnValue
    ) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnUploadImage.setDisable(true);
        CustomCommonUtil.setDisable(!lbShow, txtField01, txtField02, txtField03,
                txtField04, txtField05, comboBox06, comboBox07, comboBox08,
                datePicker09, comboBox10, comboBox11, txtField12, txtField13,
                txtField14);
//        btnUploadImage.setDisable(!lbShow);
//        if (oTransClient.getModel().getModel().getImage() != null) {
//            btnRemoveImage.setDisable(false);
//        }
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);

        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave, btnTabAdd);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave, btnTabAdd);
        CustomCommonUtil.setVisible(false, btnEdit, btnTabRem, btnDisapprove, btnApprove);
        CustomCommonUtil.setManaged(false, btnEdit, btnTabRem, btnDisapprove, btnApprove);
        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
        if (fnValue == EditMode.READY) {
            if (oTrans.getModel().getModel().getRecdStat().equals("2")) {
                btnDisapprove.setVisible(false);
                btnDisapprove.setManaged(false);
//                btnApprove.setVisible(true);
//                btnApprove.setManaged(true);
            } else if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                btnDisapprove.setVisible(true);
                btnDisapprove.setManaged(true);
            } else {
                CustomCommonUtil.setVisible(true, btnEdit, btnDisapprove, btnApprove);
                CustomCommonUtil.setManaged(true, btnEdit, btnDisapprove, btnApprove);
            }
        }
    }

    private void initRefFields(int fnValue) {
        if (fnValue == EditMode.ADDNEW) {
            CustomCommonUtil.setDisable(true, txtField01, txtField02, txtField03,
                    txtField04, txtField05, comboBox06, comboBox07, comboBox08,
                    datePicker09);
        }

    }

    private boolean checkExistingReferralAgentInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransClient.validateExistingClientInfo(true);
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTransClient.openRecord((String) loJSON.get("sClientID"));
                if ("success".equals((String) loJSON.get("result"))) {
                    oTrans = new Sales_Agent(oApp, false, oApp.getBranchCode());
                    loJSON = oTrans.newRecord();
                    if (!"error".equals(loJSON.get("result"))) {
                        oTrans.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
                        loadMasterFields();
                        loadAddress();
                        loadContact();
                        loadEmail();
                        loadSocialMedia();
                        loadAgentTrans();
                        loadAgentRequirements();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return false;
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return false;
                }
            } else {
                return false;
            }
        }

        loJSON = oTrans.validateExistingRA();
        String sClientID = (String) loJSON.get("sClientID");
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord(sClientID);
                if ("success".equals((String) loJSON.get("result"))) {
                    loJSON = oTransClient.openRecord(sClientID);
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadAddress();
                        loadContact();
                        loadEmail();
                        loadSocialMedia();
                        loadAgentTrans();
                        loadAgentRequirements();
                        pnEditMode = oTransClient.getEditMode();
                        initFields(pnEditMode);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return false;
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    return false;
                }
            } else {

                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox10.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Referral Type", "Please select `Referral Type` value.");
                return false;
            } else {
                switch (String.valueOf(oTrans.getModel().getModel().getAgentTyp())) {
                    case "reg":
                        comboBox10.setValue("AGENT");
                        break;
                    case "bnk":
                        comboBox10.setValue("BANK");
                        break;
                    case "emp":
                        comboBox10.setValue("EMPLOYEE");
                        break;
                    case "stk":
                        comboBox10.setValue("STOCKHOLDER");
                        break;
                }
            }
        }
        return true;
    }

    @FXML
    private void ImageViewClicked(MouseEvent event) {
//        if (!oTrans.getModel().getModel().getImage().isEmpty()) {
        loadPhotoWindow();
//        } else {
//            psFileUrl = "";
//        }
    }

    /*OPEN PHOTO WINDOW*/
    private void loadPhotoWindow() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/components/ViewPhotoDialog.fxml"));
            ViewPhotoController loControl = new ViewPhotoController();
            loControl.setGRider(oApp);
            loControl.setFormTitle("Profile Picture");
            loControl.setPicName(psFileName);
//            if (oTransClient.getModel().getModel().getImageUrl() != null) {
//                psFileUrl = oTransClient.getModel().getModel().getImageUrl();
            loControl.setPicUrl("file:D:/Integrated Automotive System/autoapp/src/main/resources/org/guanzon/autoapp/images/defaultimage.png");
//            }

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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

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
            if (oTransClient.getAddress(pnRow, "sEntryByx") == null || ((String) oTransClient.getAddress(pnRow, "sEntryByx")).isEmpty()) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            } else {
                btnTabRem.setVisible(false);
                btnTabRem.setManaged(false);
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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerAddress.fxml"));
            CustomerAddressController loControl = new CustomerAddressController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransClient);
            loControl.setRow(fnRow);
            loControl.setState(isAdd);
            loControl.setOrigProv((String) oTransClient.getAddress(fnRow, 25));
            loControl.setOrigTown((String) oTransClient.getAddress(fnRow, 16));
            loControl.setOrigBrgy((String) oTransClient.getAddress(fnRow, 18));
            loControl.setFormStateName("Referral Agent Information");
            loControl.setClientID(oTransClient.getModel().getModel().getClientID());
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
        for (int lnCtr = 0; lnCtr <= oTransClient.getAddressList().size() - 1; lnCtr++) {
            sAddress = oTransClient.getAddress(lnCtr, "sAddressx").toString() + " " + oTransClient.getAddress(lnCtr, "sBrgyName").toString() + " " + oTransClient.getAddress(lnCtr, "sTownName").toString() + ", " + oTransClient.getAddress(lnCtr, "sProvName").toString();
            if (oTransClient.getAddress(lnCtr, "cPrimaryx").toString().equals("1")) {
                sPrimary = "Y";
            } else {
                sPrimary = "N";
            }
            if (oTransClient.getAddress(lnCtr, "cCurrentx").toString().equals("1")) {
                sCurrent = "Y";
            } else {
                sCurrent = "N";
            }
            if (oTransClient.getAddress(lnCtr, "cProvince").toString().equals("1")) {
                sProvincial = "Y";
            } else {
                sProvincial = "N";
            }
            if (oTransClient.getAddress(lnCtr, "cOfficexx").toString().equals("1")) {
                sOffice = "Y";
            } else {
                sOffice = "N";
            }
            if (oTransClient.getAddress(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACTIVE";
            }
            if (sAddress != null) {
                addressdata.add(new CustomerAddress(
                        String.valueOf(lnCtr + 1), //ROW
                        sPrimary,
                        oTransClient.getAddress(lnCtr, "sHouseNox").toString(),
                        sAddress.trim().toUpperCase(),
                        oTransClient.getAddress(lnCtr, "sZippCode").toString(),
                        sCurrent,
                        sProvincial,
                        sOffice,
                        sStatus,
                        oTransClient.getAddress(lnCtr, "sRemarksx").toString()
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
            if (oTransClient.getMobile(pnRow, "sEntryByx") == null || oTransClient.getMobile(pnRow, "sEntryByx").toString().isEmpty()) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            } else {
                btnTabRem.setVisible(false);
                btnTabRem.setManaged(false);
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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerContact.fxml"));
            CustomerContactController loControl = new CustomerContactController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransClient);
            loControl.setFormStateName("Referral Agent Information");
            loControl.setRow(fnRow);
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
            scene.setFill(Color.TRANSPARENT);
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
        for (int lnCtr = 0; lnCtr <= oTransClient.getMobileList().size() - 1; lnCtr++) {
            switch (oTransClient.getMobile(lnCtr, "cOwnerxxx").toString()) {
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
            switch (oTransClient.getMobile(lnCtr, "cMobileTp").toString()) {
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
            if (oTransClient.getMobile(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACITVE";
            }

            if (oTransClient.getMobile(lnCtr, "cPrimaryx").toString().equals("1")) {
                sPrimary = "Y";
            } else {
                sPrimary = "N";
            }

            if (String.valueOf(oTransClient.getMobile(lnCtr, "sMobileNo")) != null
                    || !String.valueOf(oTransClient.getMobile(lnCtr, "sMobileNo")).isEmpty()) {
                contactdata.add(new CustomerMobile(
                        String.valueOf(lnCtr + 1), //ROW
                        sPrimary,
                        sOwnership, //OWNERSHIP
                        sType,
                        oTransClient.getMobile(lnCtr, "sMobileNo").toString(), //NUMBER
                        sStatus,
                        oTransClient.getMobile(lnCtr, "sRemarksx").toString()
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
            if (oTransClient.getEmail(pnRow, "sEntryByx") == null || oTransClient.getEmail(pnRow, "sEntryByx").toString().isEmpty()) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            } else {
                btnTabRem.setVisible(false);
                btnTabRem.setManaged(false);
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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerEmail.fxml"));
            CustomerEmailController loControl = new CustomerEmailController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransClient);
            loControl.setFormStateName("Referral Agent Information");
            loControl.setRow(fnRow);
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
            scene.setFill(Color.TRANSPARENT);
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
        for (int lnCtr = 0; lnCtr <= oTransClient.getEmailList().size() - 1; lnCtr++) {
            switch (oTransClient.getEmail(lnCtr, "cOwnerxxx").toString()) {
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
            if (oTransClient.getEmail(lnCtr, "cPrimaryx").toString().equals("1")) {
                sPrimary = "Y";
            } else {
                sPrimary = "N";
            }
            if (oTransClient.getEmail(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACTIVE";
            }
            emaildata.add(new CustomerEmail(
                    String.valueOf(lnCtr + 1), //ROW
                    sPrimary,
                    sOwnership, //OWNERSHIP
                    oTransClient.getEmail(lnCtr, "sEmailAdd").toString(), //EMAIL
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
            if (oTransClient.getSocialMed(pnRow, "sEntryByx") == null || oTransClient.getSocialMed(pnRow, "sEntryByx").toString().isEmpty()) {
                btnTabRem.setVisible(true);
                btnTabRem.setManaged(true);
            } else {
                btnTabRem.setVisible(false);
                btnTabRem.setManaged(false);
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
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/CustomerSocialMedia.fxml"));
            CustomerSocialMediaController loControl = new CustomerSocialMediaController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransClient);
            loControl.setFormStateName("Referral Agent Information");
            loControl.setRow(fnRow);
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
            scene.setFill(Color.TRANSPARENT);
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
        for (int lnCtr = 0; lnCtr <= oTransClient.getSocialMediaList().size() - 1; lnCtr++) {
            switch (oTransClient.getSocialMed(lnCtr, "cSocialTp").toString()) {
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

            if (oTransClient.getSocialMed(lnCtr, "cRecdStat").toString().equals("1")) {
                sStatus = "ACTIVE";
            } else {
                sStatus = "INACTIVE";
            }
            if (!oTransClient.getSocialMed(lnCtr, "sAccountx").toString().trim().equals("")
                    || !oTransClient.getSocialMed(lnCtr, "sAccountx").toString().trim().isEmpty()) {
                socialmediadata.add(new CustomerSocialMedia(
                        String.valueOf(lnCtr + 1), //ROW
                        sSocType, //Social Type
                        oTransClient.getSocialMed(lnCtr, "sAccountx").toString(), //Acount
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

    private void loadAgentRequirements() {

    }

    private void initAgentRequirements() {

    }

    private void loadAgentTrans() {
        transData.clear();
        String csPlate = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getVSPModelList().size() - 1; lnCtr++) {
            if (oTrans.getModel().getVSPModel(lnCtr).getPlateNo() != null) {
                csPlate = oTrans.getModel().getVSPModel(lnCtr).getCSNo() + "/" + oTrans.getModel().getVSPModel(lnCtr).getPlateNo();
            } else {
                csPlate = oTrans.getModel().getVSPModel(lnCtr).getCSNo();
            }
            transData.add(new RefAgentTrans(
                    String.valueOf(lnCtr + 1), //ROW
                    CustomCommonUtil.xsDateShort(oTrans.getModel().getVSPModel(lnCtr).getDelvryDt()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getVSPNO().toUpperCase()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getBuyCltNm().toUpperCase()),
                    csPlate,
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getVhclFDsc().toUpperCase()),
                    CustomCommonUtil.xsDateShort(oTrans.getModel().getVSPModel(lnCtr).getUDRDate()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getUDRNo().toUpperCase()),
                    String.valueOf(oTrans.getModel().getVSPModel(lnCtr).getSEName())));
            csPlate = "";
        }
        tblTransaction.setItems(transData);
    }

    private void initAgentTransaction() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));

        tblTransaction.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblTransaction.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

}
