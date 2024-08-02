/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import org.guanzon.autoapp.controllers.components.ViewPhotoDialogController;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
import org.guanzon.auto.main.clients.Sales_Agent;
import org.guanzon.autoapp.models.general.ModelCustomerAddress;
import org.guanzon.autoapp.models.general.ModelCustomerEmail;
import org.guanzon.autoapp.models.general.ModelCustomerMobile;
import org.guanzon.autoapp.models.general.ModelCustomerSocialMedia;
import org.guanzon.autoapp.models.general.ModelRefAgentRequirements;
import org.guanzon.autoapp.models.general.ModelRefAgentTrans;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ReferralAgentFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Client oTransClient;
    private Sales_Agent oTransRef;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Referral Agent Information"; //Form Title
    private int pnEditMode;
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    private int iTabIndex = 0; //Set tab index
    private String psFileName = "";
    private String psFileUrl = "";
    private String imgIdentfier = "";

    private ObservableList<ModelCustomerAddress> addressdata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerMobile> contactdata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerEmail> emaildata = FXCollections.observableArrayList();
    private ObservableList<ModelCustomerSocialMedia> socialmediadata = FXCollections.observableArrayList();
    private ObservableList<ModelRefAgentTrans> transData = FXCollections.observableArrayList();
    private ObservableList<ModelRefAgentRequirements> agentReqData = FXCollections.observableArrayList();
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
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField12;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private DatePicker datePicker09;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> comboBox06, comboBox07, comboBox08, comboBox10, comboBox11;
    @FXML
    private TabPane tabPCustCont;
    @FXML
    private Tab tabAddrInf;
    @FXML
    private TableView<ModelCustomerAddress> tblAddress;
    @FXML
    private TableColumn<ModelCustomerAddress, String> addrindex01, addrindex02, addrindex03, addrindex04, addrindex05, addrindex06,
            addrindex07, addrindex08, addrindex09, addrindex10;
    @FXML
    private Tab tabContNo;
    @FXML
    private TableView<ModelCustomerMobile> tblContact;
    @FXML
    private TableColumn<ModelCustomerMobile, String> contindex01, contindex02, contindex03, contindex04, contindex05, contindex06, contindex07;
    @FXML
    private Tab tabEmail;
    @FXML
    private TableView<ModelCustomerEmail> tblEmail;
    @FXML
    private TableColumn<ModelCustomerEmail, String> emadindex01, emadindex02, emadindex03, emadindex04, emadindex05;
    @FXML
    private Tab tabSocMed;
    @FXML
    private TableView<ModelCustomerSocialMedia> tblSocMed;
    @FXML
    private TableColumn<ModelCustomerSocialMedia, String> socmindex01, socmindex02, socmindex03, socmindex04;
    @FXML
    private TableView<ModelRefAgentRequirements> tblRequirementsInfo;
    @FXML
    private TableColumn<ModelRefAgentRequirements, Boolean> rqrmIndex01;
    @FXML
    private TableColumn<ModelRefAgentRequirements, String> rqrmIndex02;
    @FXML
    private TableColumn<ModelRefAgentRequirements, String> rqrmIndex03;
    @FXML
    private TableColumn<ModelRefAgentRequirements, String> rqrmIndex04;
    @FXML
    private TableView<ModelRefAgentTrans> tblTransaction;
    @FXML
    private TableColumn<ModelRefAgentTrans, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09;
    @FXML
    private Label lblStatus;
    @FXML
    private Tab tabReferral;

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
        initComboValueItems();
        oTransClient = new Client(oApp, false, oApp.getBranchCode());
        oTransRef = new Sales_Agent(oApp, false, oApp.getBranchCode());
        initAddress();
        initContact();
        initEmail();
        initSocialMedia();
        initAgentRequirements();
        initAgentTransaction();
        initTextFieldPattern();
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initButtons();
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initTextFieldPattern() {
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField12);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField12);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
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
                        loJSON = oTransClient.searchClient(lsValue, true);
                        if (!"error".equals(loJSON.get("result"))) {
                            oTransRef = new Sales_Agent(oApp, false, oApp.getBranchCode());
                            loJSON = oTransRef.newRecord();
                            if (!"error".equals(loJSON.get("result"))) {
                                oTransRef.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
                                loadReferralAgentInformation();
                                loadAddress();
                                loadContact();
                                loadEmail();
                                loadSocialMedia();
                                loadAgentTrans();
                                loadAgentRequirements();
                                initFields(pnEditMode);
                                pnEditMode = oTransClient.getEditMode();
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }

                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                        break;
                    case "txtField02":
                        loJSON = oTransClient.searchClient(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            oTransRef = new Sales_Agent(oApp, false, oApp.getBranchCode());
                            loJSON = oTransRef.newRecord();
                            if (!"error".equals(loJSON.get("result"))) {
                                oTransRef.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
                                loadReferralAgentInformation();
                                loadAddress();
                                loadContact();
                                loadEmail();
                                loadSocialMedia();
                                loadAgentTrans();
                                loadAgentRequirements();
                                initFields(pnEditMode);
                                pnEditMode = oTransClient.getEditMode();
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }

                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                            return;
                        }
                        break;
                    case "txtField03":
                        loJSON = oTransClient.searchClient(lsValue, false);
                        if (!"error".equals(loJSON.get("result"))) {
                            oTransRef = new Sales_Agent(oApp, false, oApp.getBranchCode());
                            loJSON = oTransRef.newRecord();
                            if (!"error".equals(loJSON.get("result"))) {
                                oTransRef.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
                                loadReferralAgentInformation();
                                loadAddress();
                                loadContact();
                                loadEmail();
                                loadSocialMedia();
                                loadAgentTrans();
                                loadAgentRequirements();
                                initFields(pnEditMode);
                                pnEditMode = oTransClient.getEditMode();
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }

                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
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

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField12);
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
                case 1:/*agent id*/
                    oTransClient.getModel().getModel().setClientID(lsValue);
                    break;
                case 2:/*last name*/
                    oTransClient.getModel().getModel().setLastName(lsValue);
                    break;
                case 3:/*frist name*/
                    oTransClient.getModel().getModel().setFirstName(lsValue);
                    break;
                case 4:/*middle name*/
                    oTransClient.getModel().getModel().setMiddleName(lsValue);
                    break;
                case 5:/*Suffix Name*/
                    oTransClient.getModel().getModel().setSuffixName(lsValue);
                    break;
                case 12:/*Profession */
//                    oTransClient.getModel().getModel().setProfession(lsValue);
                    break;
            }
        } else {
            txtField.selectAll();

        }
    };

    private void clearFields() {
        Image loImageError = new Image("file:D:/Integrated Automotive System/autoapp/src/main/resources/org/guanzon/autoapp/images/defaultimage.png");
        imageView.setImage(loImageError);
        psFileUrl = "";
        psFileName = "";
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        comboBox06.setValue("");
        comboBox07.setValue("");
        comboBox08.setValue("");
        datePicker09.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        comboBox10.setValue("");
        comboBox11.setValue("");
        txtField12.setText("");
    }

    private void clearTables() {
        addressdata.clear();
        contactdata.clear();
        emaildata.clear();
        socialmediadata.clear();
        transData.clear();
        agentReqData.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        btnUploadImage.setDisable(true);
        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        comboBox06.setDisable(!lbShow);
        comboBox07.setDisable(!lbShow);
        comboBox08.setDisable(!lbShow);
        datePicker09.setDisable(!lbShow);
        comboBox10.setDisable(!lbShow);
        comboBox11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
//        btnUploadImage.setDisable(!lbShow);

//        if (oTransClient.getModel().getModel().getImage() != null) {
//            btnRemoveImage.setDisable(false);
//        }
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);

        btnTabAdd.setVisible(lbShow);
        btnTabRem.setVisible(false);

        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnDisapprove.setVisible(false);
        btnDisapprove.setManaged(false);
        btnApprove.setVisible(false);
        btnApprove.setManaged(false);
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
        if (fnValue == EditMode.READY) {
            if (oTransRef.getModel().getModel().getRecdStat().equals("2")) {
                btnDisapprove.setVisible(false);
                btnDisapprove.setManaged(false);
                btnApprove.setVisible(true);
                btnApprove.setManaged(true);
            } else if (oTransRef.getModel().getModel().getRecdStat().equals("1")) {
                btnDisapprove.setVisible(true);
                btnDisapprove.setManaged(true);
                btnApprove.setVisible(false);
                btnApprove.setManaged(false);
            } else {
                btnDisapprove.setVisible(true);
                btnDisapprove.setManaged(true);
                btnApprove.setVisible(true);
                btnApprove.setManaged(true);
            }
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    private void initCmboxFieldAction() {
        datePicker09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransClient.setMaster(11, SQLUtil.toDate(datePicker09.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        comboBox06.setOnAction(e -> {
            int selectedComboBox06 = comboBox10.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox06 >= 0) {
                    oTransClient.getModel().getModel().setTitle(String.valueOf(selectedComboBox06));
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox07.setOnAction(e -> {
            int selectedComboBox07 = comboBox08.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox07 >= 0) {
                    oTransClient.getModel().getModel().setGender(String.valueOf(selectedComboBox07));
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox08.setOnAction(e -> {
            int selectedComboBox08 = comboBox08.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox08 >= 0) {
                    oTransClient.getModel().getModel().setCvilStat(String.valueOf(selectedComboBox08));
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox10.setOnAction(e -> {
            int selectedComboBox10 = comboBox10.getSelectionModel().getSelectedIndex();
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (selectedComboBox10 >= 0) {
                    switch (selectedComboBox10) {
                        case 0:
                            oTransRef.getModel().getModel().setAgentTyp("reg");
                            break;
                        case 1:
                            oTransRef.getModel().getModel().setAgentTyp("bnk");
                            break;
                        case 2:
                            oTransRef.getModel().getModel().setAgentTyp("emp");
                            break;
                        case 3:
                            oTransRef.getModel().getModel().setAgentTyp("stk");
                            break;
                        default:
                            break;
                    }
                }
                initFields(pnEditMode);
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

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnAdd, btnEdit, btnSave, btnBrowse, btnCancel, btnApprove, btnDisapprove, btnClose, btnUploadImage, btnRemoveImage, btnTabAdd, btnTabRem);

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
                oTransClient = new Client(oApp, false, oApp.getBranchCode());
                loJSON = oTransClient.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    oTransRef = new Sales_Agent(oApp, false, oApp.getBranchCode());
                    loJSON = oTransRef.newRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        pnEditMode = oTransClient.getEditMode();
                        loadReferralAgentInformation();
                        loadAgentTrans();
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
                    loJSON = oTransRef.updateRecord();
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
                    //check fields before saving
//                    if (checkExistingReferalAgentInformation(true)) {
//                        return;
//                    }
                } else {
                    return;
                }
                if (setSelection()) {
                    loJSON = oTransClient.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        oTransRef.getModel().getModel().setClientID(oTransClient.getModel().getModel().getClientID());
                        loJSON = oTransRef.saveRecord();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                            loJSON = oTransClient.openRecord(oTransClient.getModel().getModel().getClientID());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loJSON = oTransRef.openRecord(oTransClient.getModel().getModel().getClientID());
                                if ("success".equals((String) loJSON.get("result"))) {
                                    loadReferralAgentInformation();
                                    loadAddress();
                                    loadContact();
                                    loadEmail();
                                    loadSocialMedia();
                                    loadAgentTrans();
                                    loadAgentRequirements();
                                    initFields(pnEditMode);
                                    pnEditMode = oTransClient.getEditMode();
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
                    clearFields();
                    clearTables();
                    oTransClient = new Client(oApp, false, oApp.getBranchCode());
                    oTransRef = new Sales_Agent(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransRef.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    oTransClient.openRecord(oTransRef.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadReferralAgentInformation();
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
                break;
            case "btnDisapprove":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransRef.getModel().getModel().getClientID();
                    loJSON = oTransRef.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Sales Executive Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransClient.openRecord(oTransRef.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadReferralAgentInformation();
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
                break;
            case "btnApprove":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTransRef.getModel().getModel().getClientID();
                    loJSON = oTransRef.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Executive Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Sales Executive Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransClient.openRecord(oTransRef.getModel().getModel().getClientID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadReferralAgentInformation();
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
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean setSelection() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            if (comboBox10.getSelectionModel().getSelectedIndex() < 0) {
                ShowMessageFX.Warning(null, "Referral Type", "Please select `Referral Type` value.");
                return false;
            } else {
                switch (String.valueOf(oTransRef.getModel().getModel().getAgentTyp())) {
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

    private void loadReferralAgentInformation() {
        txtField01.setText(oTransRef.getModel().getModel().getClientID());
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
            datePicker09.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransClient.getModel().getModel().getBirthDte())));
        }
        if (oTransClient.getModel().getModel().getCvilStat() != null && !oTransClient.getModel().getModel().getCvilStat().trim().isEmpty()) {
            comboBox08.getSelectionModel().select(Integer.parseInt(oTransClient.getModel().getModel().getCvilStat()));
        }
        if (oTransRef.getModel().getModel().getRecdStat() != null) {
            switch (oTransRef.getModel().getModel().getRecdStat()) {
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
        if (oTransRef.getModel().getModel().getAgentTyp() != null && !oTransRef.getModel().getModel().getAgentTyp().trim().isEmpty()) {
            switch (String.valueOf(oTransRef.getModel().getModel().getAgentTyp())) {
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
//        if (oTransRef.getModel().getModel().getEducAttain!= null && !oTransRefferal.getModel().getModel().getgetEducAttain().trim().isEmpty()) {
//            comboBox11.getSelectionModel().select(Integer.parseInt(oTransRefferal.getModel().getModel().getgetEducAttain()));
//        }

//        txtField12.setText(oTransRef.getModel().getModel().getProfession());
    }

    @FXML
    private void ImageViewClicked(MouseEvent event) {
//        if (!oTransRef.getModel().getModel().getImage().isEmpty()) {
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
            ViewPhotoDialogController loControl = new ViewPhotoDialogController();
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
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    private void initComboValueItems() {
        comboBox06.setItems(cTitle);
        comboBox07.setItems(cGender);
        comboBox08.setItems(cCvlStat);
        comboBox10.setItems(cReferTyp);
        comboBox11.setItems(cEducAttain);
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
        for (lnCtr = 0; lnCtr <= oTransClient.getAddressList().size() - 1; lnCtr++) {
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
                addressdata.add(new ModelCustomerAddress(
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
        for (lnCtr = 0; lnCtr <= oTransClient.getMobileList().size() - 1; lnCtr++) {
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

            if (oTransClient.getMobile(lnCtr, "sMobileNo") != null
                    || !oTransClient.getMobile(lnCtr, "sMobileNo").toString().isEmpty()) {
                contactdata.add(new ModelCustomerMobile(
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
        for (lnCtr = 0; lnCtr <= oTransClient.getEmailList().size() - 1; lnCtr++) {
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
            emaildata.add(new ModelCustomerEmail(
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
        for (lnCtr = 0; lnCtr <= oTransClient.getSocialMediaList().size() - 1; lnCtr++) {
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
                socialmediadata.add(new ModelCustomerSocialMedia(
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

//    private String getValue(String fsValue, Integer loRow, String fsCol) {
//        try {
//            fsValue = "";
//            if (oTransRef.getVSPTransDetail(loRow, fsCol) != null) {
//                fsValue = String.valueOf(oTransRef.getVSPTransDetail(loRow, fsCol)).toUpperCase();
//
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(SalesExecutiveFormController.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//        return fsValue;
//    }
//    private String getValueDate(String fsValue, Integer loRow, String fsCol) {
//        try {
//            fsValue = "";
//            if (oTransRef.getVSPTransDetail(loRow, fsCol) != null) {
//                fsValue = InputTextUtil.xsDateShort((Date) oTransRef.getVSPTransDetail(loRow, fsCol));
//
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(SalesExecutiveFormController.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//        return fsValue;
//    }
    private void loadAgentTrans() {
        transData.clear();
//        JSONObject loJSON = new JSONObject();
//        try {
//            loJSON = oTransRef.loadTransaction();
//            if ("success".equals((String) loJSON.get("result"))) {
//                for (lnCtr = 0; lnCtr <= oTransRef.getVSPTransCount() - 1; lnCtr++) {
//                    String csPlate = String.valueOf(oTransRef.getVSPTransDetail(lnCtr, "sCSNoxxxx")).toUpperCase() + "/" + String.valueOf(oTransRef.getVSPTransDetail(lnCtr, "sPlateNox")).toUpperCase();
//                    transData.add(new ModelRefAgentTrans(
//                            String.valueOf(lnCtr + 1), //ROW
//                            getValueDate("ldVSPDate", lnCtr, ""),
//                            getValue("lsVSPNo", lnCtr, "sVSPNOxxx"),
//                            getValue("lsCustomName", lnCtr, "sBuyCltNm"),
//                            csPlate,
//                            getValue("lsCSPlateNo", lnCtr, "sDescript"),
//                            getValueDate("ldDrDate", lnCtr, "dUDRDatex"),
//                            getValue("lsDrNo", lnCtr, "sUDRNoxxx"),
//                            getValue("lsSalesExe", lnCtr, "sSaleExNm  ")
//                    ));
//
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(SalesExecutiveFormController.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
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
        transData.clear();
        tblTransaction.setItems(transData);
    }

}
