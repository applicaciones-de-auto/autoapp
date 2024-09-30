/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.sales.FollowUp;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.FXMLDocumentController;
import org.guanzon.autoapp.controllers.general.VehicleGatePassController;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.sales.Part;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VSPController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposal oTransVSP;
    private FollowUp oTransFollow;
    private Inquiry oTransInquiry;
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    public int pnEditMode = -1;//Modifying fields
    private int pnCtr = 0;
    private String psInqTransNo = "";
    private int pnRow = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean pbIsInquiry = false;
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO");
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cFinPromoType = FXCollections.observableArrayList("NONE", "ALL-IN IN HOUSE", "ALL-IN PROMO");
    ObservableList<String> cTplType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o DEALER", "C/o BANK");
    ObservableList<String> cCompType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o DEALER", "C/o BANK ");
    ObservableList<String> cCompYearType1 = FXCollections.observableArrayList("NONE", "FREE", "REGULAR RATE", "DISCOUNTED RATE", "FROM PROMO DISC");
    ObservableList<String> cCompYearType2 = FXCollections.observableArrayList("1", "2", "3", "4");
    ObservableList<String> cLTOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE");
    ObservableList<String> cHMOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE", "C/o BANK");
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private ObservableList<Part> accessoriesData = FXCollections.observableArrayList();
    @FXML
    AnchorPane AnchorMain;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Tab tabMain, tabDetails, tabAddOns;
    @FXML
    private Label lblDRNo, lblSINo, lblVSPStatus, lblPrint, lblRFNo, lblApproveDate;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnCancelVSP,
            btnApprove, btnGatePass, btnClose, btnJobOrderAdd, btnAdditionalLabor, btnAddParts,
            btnPaymentHistory, btnAddReservation, btnRemoveReservation;
    //Main TextField Tab
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
            txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField18,
            txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25;
    //Detail & AddOns TextField Tab
    @FXML
    private TextField txtField30;
    @FXML
    private TextField txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField38,
            txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
            txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
            txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60, txtField62,
            txtField63, txtField65, txtField68, txtField70, txtField72, txtField73, txtField74, txtField75, txtField76,
            txtField77, txtField78, txtField79, txtField80, txtField81, txtField82, txtField83, txtField84, txtField85;
    @FXML
    private DatePicker datePicker09;
    @FXML
    private ComboBox<String> comboBox05, comboBox29, comboBox37, comboBox61,
            comboBox64, comboBox66, comboBox67, comboBox69, comboBox71;
    @FXML
    private TextArea textArea17, textArea26, textArea27, textArea28;
    @FXML
    private ToggleGroup tgUnitCategory;
    @FXML
    private RadioButton brandNewCat, preOwnedCat;
    @FXML
    private TableView<Labor> tblViewLabor;
    @FXML
    private TableColumn<Labor, String> tblindex01_labor, tblindex02_labor, tblindex03_labor, tblindex04_labor,
            tblindex05_labor, tblindex06_labor, tblindex07_labor, tblindex08_labor, tblindex09_labor;
    @FXML
    private CheckBox chckBoxSpecialAccount, chckBoxRustProof, chckBoxPermaShine, chckBoxUndercoat, chckBoxTint;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableColumn<Part, String> tblindex01_part, tblindex02_part, tblindex03_part, tblindex04_part,
            tblindex05_part, tblindex06_part, tblindex07_part, tblindex08_part, tblindex09_part, tblindex10_part,
            tblindex11_part;

    private FXMLDocumentController fdController = new FXMLDocumentController();

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setIsInquiryState(boolean fbValue) {
        pbIsInquiry = fbValue;
    }

    public void setInquiryTrans(String fsValue) {
        psInqTransNo = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransVSP = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
        oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
        oTransFollow = new FollowUp(oApp, false, oApp.getBranchCode());
        initLaborTable();
        initAccessoriesTable();
        datePicker09.setDayCellFactory(DateFormatCell);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initComboBoxItems();
        initCmboxFieldAction();
        initTableKeyPressed();
        initPatternFields();
        initDatePropertyAction();
        initTextPropertyAction();
        initButtonClick();
        clearVSPFields();
        clearTables();
        addRowVSPLabor();
        tblViewLabor.setOnMouseClicked(this::tblLabor_Clicked);
        tblViewAccessories.setOnMouseClicked(this::tblParts_Clicked);

        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);

        Platform.runLater(() -> {
            if (pbIsInquiry) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
                loJSON = oTransVSP.searchInquiry(psInqTransNo, true);
                if (!"error".equals((String) loJSON.get("result"))) {
                    loadVSPFields();
                    pnEditMode = oTransVSP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    clearFields();
                }
            }
        }
        );
        lblRFNo.setOnMouseEntered(event -> lblRFNo.setCursor(Cursor.HAND));
        // Set default cursor when not hovering
        lblRFNo.setOnMouseExited(event -> lblRFNo.setCursor(Cursor.DEFAULT));

    }

    private void initPatternFields() {
        List<TextField> loTxtField = Arrays.asList(txtField30, txtField31, txtField33, txtField35, txtField39, txtField42,
                txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField59, txtField60, txtField63, txtField68, txtField70, txtField74, txtField82, txtField84);
        Pattern decOnly = Pattern.compile("[0-9,.]*");
        Pattern numOnly = Pattern.compile("[0-9]*");
        loTxtField.forEach(tf -> tf.setTextFormatter(new TextFormatterUtil(decOnly)));
        txtField34.setTextFormatter(new TextFormatterUtil(numOnly));
    }

    private boolean loadVSPFields() {

        //Main Interface
        JSONObject loJSON = new JSONObject();
        loJSON = oTransVSP.computeAmount();
//        if (!"error".equals((String) loJSON.get("result"))) {
        txtField01.setText(oTransVSP.getMasterModel().getMasterModel().getVSPNO());
        if (oTransVSP.getMasterModel().getMasterModel().getTransactDte() != null && !oTransVSP.getMasterModel().getMasterModel().getTransactDte().toString().isEmpty()) {
            txtField02.setText(CustomCommonUtil.xsDateWMonthName(oTransVSP.getMasterModel().getMasterModel().getTransactDte()));
        }
        txtField03.setText(oTransVSP.getMasterModel().getMasterModel().getInqryID());
        if (oTransVSP.getMasterModel().getMasterModel().getInqryDte() != null && !oTransVSP.getMasterModel().getMasterModel().getInqryDte().toString().isEmpty()) {
            txtField04.setText(CustomCommonUtil.xsDateWMonthName(oTransVSP.getMasterModel().getMasterModel().getInqryDte()));
        }
        if (oTransVSP.getMasterModel().getMasterModel().getSourceCD() != null && !oTransVSP.getMasterModel().getMasterModel().getSourceCD().trim().isEmpty()) {
            comboBox05.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getSourceCD()));
        }
        txtField06.setText(oTransVSP.getMasterModel().getMasterModel().getSEName());
        txtField07.setText(oTransVSP.getMasterModel().getMasterModel().getAgentNm());
        txtField08.setText(oTransVSP.getMasterModel().getMasterModel().getBranchNm());
        if (oTransVSP.getMasterModel().getMasterModel().getDelvryDt() != null && !oTransVSP.getMasterModel().getMasterModel().getDelvryDt().toString().isEmpty()) {
            datePicker09.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransVSP.getMasterModel().getMasterModel().getDelvryDt())));
        }
        txtField10.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTranTotl()))));
        txtField11.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getTotalDiscount()))));
        txtField12.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getResrvFee()))));
        txtField13.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAmtPaid()))));
        txtField14.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetTTotl()))));
        txtField15.setText(oTransVSP.getMasterModel().getMasterModel().getInqCltNm());
        txtField16.setText(oTransVSP.getMasterModel().getMasterModel().getBuyCltNm());
        textArea17.setText(oTransVSP.getMasterModel().getMasterModel().getAddress());
        txtField18.setText(oTransVSP.getMasterModel().getMasterModel().getCoCltNm());
        txtField19.setText(oTransVSP.getMasterModel().getMasterModel().getContctNm());
        if (oTransVSP.getMasterModel().getMasterModel().getIsVhclNw().equals("0")) {
            brandNewCat.setSelected(true);
        } else if (oTransVSP.getMasterModel().getMasterModel().getIsVhclNw().equals("1")) {
            preOwnedCat.setSelected(true);
        } else {
            preOwnedCat.setSelected(false);
            brandNewCat.setSelected(false);
        }
        if (oTransVSP.getMasterModel().getMasterModel().getIsVIP().equals("1")) {
            chckBoxSpecialAccount.setSelected(true);
        } else {
            chckBoxSpecialAccount.setSelected(false);
        }
        txtField20.setText(oTransVSP.getMasterModel().getMasterModel().getCSNo());
        txtField21.setText(oTransVSP.getMasterModel().getMasterModel().getPlateNo());
        txtField22.setText(oTransVSP.getMasterModel().getMasterModel().getEngineNo());
        txtField23.setText(oTransVSP.getMasterModel().getMasterModel().getFrameNo());
        txtField24.setText(oTransVSP.getMasterModel().getMasterModel().getEndPlate());
        txtField25.setText(oTransVSP.getMasterModel().getMasterModel().getKeyNo());
        textArea26.setText(oTransVSP.getMasterModel().getMasterModel().getVhclDesc());
        textArea28.setText(oTransVSP.getMasterModel().getMasterModel().getRemarks());

        //Details Tab Interface
        if (oTransVSP.getMasterModel().getMasterModel().getPayMode() != null && !oTransVSP.getMasterModel().getMasterModel().getPayMode().trim().isEmpty()) {
            comboBox29.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getPayMode()));
        }
        txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getUnitPrce()))));
        txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
        String lsBranchBank = "";
        if (oTransVSP.getMasterModel().getMasterModel().getBankName() != null && oTransVSP.getMasterModel().getMasterModel().getBrBankNm() != null) {
            if (!oTransVSP.getMasterModel().getMasterModel().getBankName().isEmpty() && !oTransVSP.getMasterModel().getMasterModel().getBrBankNm().isEmpty()) {
                lsBranchBank = oTransVSP.getMasterModel().getMasterModel().getBankName() + " " + oTransVSP.getMasterModel().getMasterModel().getBrBankNm();
            }
        }
        txtField32.setText(lsBranchBank);
        // Payment Information and Vehicle Charges
        if (!oTransVSP.getVSPFinanceList().isEmpty()) {
            switch (comboBox29.getSelectionModel().getSelectedIndex()) {
                case 1://FINANCING
                case 2:
                case 3:
                case 4:
                    txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getDiscount()))));
                    txtField34.setText(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getAcctTerm()));
                    txtField35.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getAcctRate()))));
                    System.out.println("test acc rate: " + poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getAcctRate()))));
                    txtField36.setText("");
                    if (oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getFinPromo() != null && !oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getFinPromo().isEmpty()) {
                        comboBox37.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getFinPromo())));
                    }
                    double ldAprvdNetSrp = Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getUnitPrce())) - Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getDiscount()));
                    txtField38.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(ldAprvdNetSrp))));
                    txtField39.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getNtDwnPmt()))));
                    txtField40.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getFinAmt()))));
                    txtField41.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getMonAmort()))));
                    txtField42.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getRebates()))));
                    txtField43.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getGrsMonth()))));
                    txtField44.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPFinanceModel().getVSPFinanceModel().getPNValue()))));
                    break;
                default:
                    List<TextField> loTextField = Arrays.asList(
                            txtField33, txtField34, txtField35, txtField38, txtField39, txtField40, txtField41, txtField42,
                            txtField43, txtField44);
                    loTextField.forEach(tf -> tf.setText("0.00"));
                    txtField36.setText("0.00");
                    comboBox37.setValue(null);
                    break;
            }
        } else {
            clearFinanceFields();
        }
        // Discount
        txtField45.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getFleetDsc()))));
        txtField46.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDue2Sup()))));
        txtField47.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDue2Dlr()))));
        txtField48.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFltDsc()))));
        txtField49.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFD2Sup()))));
        txtField50.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFD2Dlr()))));
        txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPromoDsc()))));
        txtField52.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPrmD2Sup()))));
        txtField53.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPrmD2Dlr()))));
        txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAddlDsc()))));
        txtField55.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getToLabDsc()))));
        txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getBndleDsc()))));
        txtField57.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getToPrtDsc()))));
        txtField58.setText("0.00");
        // Other Charges
        txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAdvDwPmt()))));
        txtField60.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTPLAmt()))));
        if (oTransVSP.getMasterModel().getMasterModel().getTPLStat() != null && !oTransVSP.getMasterModel().getMasterModel().getTPLStat().trim().isEmpty()) {
            comboBox61.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getTPLStat()));
        }
        String lsBrTpl = "";
        if (oTransVSP.getMasterModel().getMasterModel().getTPLInsNm() != null && oTransVSP.getMasterModel().getMasterModel().getTPLBrIns() != null) {
            if (!oTransVSP.getMasterModel().getMasterModel().getTPLInsNm().isEmpty() && !oTransVSP.getMasterModel().getMasterModel().getTPLBrIns().isEmpty()) {
                lsBrTpl = oTransVSP.getMasterModel().getMasterModel().getTPLInsNm() + " " + oTransVSP.getMasterModel().getMasterModel().getTPLBrIns();
            }
        }
        txtField62.setText(lsBrTpl);
        txtField63.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getCompAmt()))));
        if (oTransVSP.getMasterModel().getMasterModel().getCompStat() != null && !oTransVSP.getMasterModel().getMasterModel().getCompStat().trim().isEmpty()) {
            comboBox64.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getCompStat()));
        }
        String lsBrCom = "";
        if (oTransVSP.getMasterModel().getMasterModel().getCOMInsNm() != null && oTransVSP.getMasterModel().getMasterModel().getCOMBrIns() != null) {
            if (!oTransVSP.getMasterModel().getMasterModel().getCOMInsNm().isEmpty() && !oTransVSP.getMasterModel().getMasterModel().getCOMBrIns().isEmpty()) {
                lsBrCom = oTransVSP.getMasterModel().getMasterModel().getCOMInsNm() + " " + oTransVSP.getMasterModel().getMasterModel().getCOMBrIns();
            }
        }
        txtField65.setText(lsBrCom);
        if (oTransVSP.getMasterModel().getMasterModel().getInsurTyp() != null && !oTransVSP.getMasterModel().getMasterModel().getInsurTyp().trim().isEmpty()) {
            comboBox66.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getInsurTyp()));
        }
        if (oTransVSP.getMasterModel().getMasterModel().getInsurYr() != null) {
            int lnYear = 0;
            switch (String.valueOf(oTransVSP.getMasterModel().getMasterModel().getInsurYr())) {
                case "0":
                    lnYear = 0;
                    break;
                case "1":
                    lnYear = 1;
                    break;
                case "2":
                    lnYear = 2;
                    break;
                case "3":
                    lnYear = 3;
                    break;
                case "4":
                    lnYear = 4;
                    break;
            }
            comboBox67.setValue(String.valueOf(lnYear));
        }
        txtField68.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getLTOAmt()))));
        if (oTransVSP.getMasterModel().getMasterModel().getLTOStat() != null && !oTransVSP.getMasterModel().getMasterModel().getLTOStat().trim().isEmpty()) {
            comboBox69.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getLTOStat()));
        }
        txtField70.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getChmoAmt()))));
        if (oTransVSP.getMasterModel().getMasterModel().getChmoStat() != null && !oTransVSP.getMasterModel().getMasterModel().getChmoStat().trim().isEmpty()) {
            comboBox71.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getChmoStat()));
        }
        txtField72.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getLaborAmt()))));
        txtField73.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAccesAmt()))));
        txtField74.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getOthrChrg()))));
        txtField75.setText(oTransVSP.getMasterModel().getMasterModel().getOthrDesc());
        txtField76.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTranTotl()))));
        txtField77.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getTotalDiscount()))));
        txtField78.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getResrvFee()))));
        txtField79.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAmtPaid()))));
        txtField80.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetTTotl()))));

        String lsJobNO = "";
        if (oTransVSP.getMasterModel().getMasterModel().getJONo() != null && !oTransVSP.getMasterModel().getMasterModel().getJONo().isEmpty()) {
            lsJobNO = oTransVSP.getMasterModel().getMasterModel().getJONo();
        }
        txtField81.setText(lsJobNO);
        txtField82.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDealrRte()))));
        txtField83.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDealrAmt()))));
        txtField84.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSlsInRte()))));
        txtField85.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSlsInAmt()))));

        String lsVDRNo = "";

        if (oTransVSP.getMasterModel()
                .getMasterModel().getUDRNo() != null) {
            lsVDRNo = oTransVSP.getMasterModel().getMasterModel().getUDRNo();
        }

        lblDRNo.setText(lsVDRNo);
        String lsRFNO = "";
        if (oTransVSP.getMasterModel().getMasterModel().getGatePsNo() != null) {
            lsRFNO = oTransVSP.getMasterModel().getMasterModel().getGatePsNo();
        }

        lblRFNo.setText(lsRFNO);
        String lsVSI = "";

        if (oTransVSP.getMasterModel()
                .getMasterModel().getSINo() != null) {
            lsVSI = oTransVSP.getMasterModel().getMasterModel().getSINo();
        }

        lblSINo.setText(lsVSI);

        lblPrint.setText(oTransVSP.getMasterModel().getMasterModel().getPrinted());
        switch (oTransVSP.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lblVSPStatus.setText("Active");
                break;
            case TransactionStatus.STATE_CLOSED:
                lblVSPStatus.setText("Approved");
                break;
            case TransactionStatus.STATE_CANCELLED:
                lblVSPStatus.setText("Cancelled");
                break;
            case TransactionStatus.STATE_POSTED:
                lblVSPStatus.setText("Posted");
                break;
            default:
                lblVSPStatus.setText("");
                break;
        } //            if (oTransVSP.getMasterModel().getMasterModel().getApprovedDte() != null && !oTransVSP.getMasterModel().getMasterModel().getApprovedDte().toString().isEmpty()) {
        //                lblApproveDate.setText(CustomCommonUtil.xsDateShort(oTransVSP.getMasterModel().getMasterModel().getApprovedDte()));
        //            }
        //    }
        //        else {
        //            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
        //        return false;
        //    }

        return true;
    }

    private void loadVSPPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPPrint.fxml"));
            VSPPrintController loControl = new VSPPrintController();
            loControl.setGRider(oApp);
            loControl.setVSObject(oTransVSP);
            loControl.setTransNox(oTransVSP.getMasterModel().getMasterModel().getTransNo());
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

    private void addRowVSPLabor() {
        chckBoxRustProof.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (chckBoxRustProof.isSelected()) {
                    initLaborDescript("RUSTPROOF", true);
                    chckBoxRustProof.setSelected(false);
                }
            }
        }
        );
        chckBoxPermaShine.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (chckBoxPermaShine.isSelected()) {
                    initLaborDescript("PERMASHINE", true);
                    chckBoxPermaShine.setSelected(false);
                }
            }
        });
        chckBoxUndercoat.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (chckBoxUndercoat.isSelected()) {
                    initLaborDescript("UNDERCOAT", true);
                    chckBoxUndercoat.setSelected(false);
                }
            }
        });
        chckBoxTint.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (chckBoxTint.isSelected()) {
                    initLaborDescript("TINT", true);
                    chckBoxTint.setSelected(false);
                }
            }
        });
    }

    private void initLaborDescript(String fsDescript, boolean fbWithLabor) {
        JSONObject loJSON = new JSONObject();
        try {
            oTransVSP.addVSPLabor();
            loJSON = oTransVSP.searchLabor(fsDescript, oTransVSP.getVSPLaborList().size() - 1, false);
            if (!"error".equals((String) loJSON.get("result"))) {
                loadLaborWindowDialog(oTransVSP.getVSPLaborList().size() - 1, true, fsDescript, fbWithLabor);
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                oTransVSP.removeVSPLabor(oTransVSP.getVSPLaborList().size() - 1);
                return;

            }
        } catch (IOException ex) {
            Logger.getLogger(VSPController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tblLabor_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewLabor.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewLabor.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid labor information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadLaborWindowDialog(pnRow, false, oTransVSP.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc(), true);

                } catch (IOException ex) {
                    Logger.getLogger(VSPController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void loadLaborWindowDialog(Integer fnRow, boolean fbIsAdd, String fsLbrDesc, boolean fbWtLabor) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPLabor.fxml"));
            VSPLaborController loControl = new VSPLaborController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVSP);
            loControl.setWithLabor(fbWtLabor);
            loControl.setLbrDesc(fsLbrDesc);
            if (!oTransVSP.getVSPLaborModel().getVSPLabor(fnRow).getLaborDsc().isEmpty()) {
                loControl.setOrigDsc(String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(fnRow).getLaborDsc()));
            }
            loControl.setState(fbIsAdd);
            loControl.setJO(oTransVSP.getVSPLaborModel().getVSPLabor(fnRow).getDSNo());
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
            loadVSPFields();

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);

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
        for (int lnCtr = 0; lnCtr <= oTransVSP.getVSPLaborList().size() - 1; lnCtr++) {
            if (oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt())));
            }
            if (oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount() != null) {
                lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount())));
            }
            if (oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt() != null) {
                lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt())));
            }
            if (oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getAddtl().equals("1")) {
                lbAdditional = true;
            }
            if (oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo() != null) {
                lsJoNoxx = oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo();
            }
            laborData.add(new Labor(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getTransNo()),
                    String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getLaborCde()),
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    String.valueOf(oTransVSP.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDsc()),
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
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewLabor.setEditable(true);
        } else {
            tblViewLabor.setEditable(false);
        }

        tblindex01_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex03_labor"));
        tblindex03_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_labor"));
        tblindex06_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex06_labor"));
        tblindex07_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex08_labor.setCellValueFactory(new PropertyValueFactory<>("addOrNot"));
        tblindex09_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex10_labor"));

        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void loadAccessoriesWindowDialog(Integer fnRow, boolean fbIsAdd) throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPAccessories.fxml"));
            VSPAccessoriesController loControl = new VSPAccessoriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVSP);
            loControl.setState(fbIsAdd);
            fxmlLoader.setController(loControl);
            loControl.setRequest(false);
            loControl.setRow(fnRow);
            if (!oTransVSP.getVSPPartsModel().getVSPParts(fnRow).getDescript().isEmpty()) {
                loControl.setOrigDsc(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(fnRow).getDescript()));
            }
            loControl.setStockID(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(fnRow).getStockID()));
            loControl.setJO(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(fnRow).getDSNo()));
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
            loadVSPFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    private void tblParts_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewAccessories.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewAccessories.getItems().size()) {
                ShowMessageFX.Warning(getStage(), "Please select valid accessories information.", "Warning", null);
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadAccessoriesWindowDialog(pnRow, false);

                } catch (IOException ex) {
                    Logger.getLogger(VSPController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
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
        for (int lnCtr = 0; lnCtr <= oTransVSP.getVSPPartsList().size() - 1; lnCtr++) {
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice())));
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                lsQuantity = String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null && oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                BigDecimal lsGrsAmt = new BigDecimal(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice()));
                int lsQuan = Integer.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
                lsTotalAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lsGrsAmt.doubleValue() * lsQuan)));
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount() != null) {
                lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount())));
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt() != null) {
                lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt())));
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc() != null) {
                lsPartsDesc = String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc());
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getBarCode() != null) {
                lsBarCode = String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getBarCode());
            }
            if (oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getDSNo() != null) {
                lsJoNoxx = oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getDSNo();
            }
            accessoriesData.add(new Part(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getTransNo()),
                    String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getStockID()),
                    String.valueOf(oTransVSP.getVSPPartsModel().getVSPParts(lnCtr).getDescript()),
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
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewAccessories.setEditable(true);
        } else {
            tblViewAccessories.setEditable(false);
        }
        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("tblindex13_part"));
        tblindex07_part.setCellValueFactory(new PropertyValueFactory<>("tblindex07_part"));
        tblindex08_part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_part"));
        tblindex09_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex10_part.setCellValueFactory(new PropertyValueFactory<>(""));
        tblindex11_part.setCellValueFactory(new PropertyValueFactory<>("tblindex11_part"));

        tblViewAccessories.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }
    private Callback<DatePicker, DateCell> DateFormatCell = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate fsItem, boolean fsEmpty) {
            super.updateItem(fsItem, fsEmpty);
            LocalDate loMinDate = CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort((Date) oApp.getServerDate())).minusDays(0);
            setDisable(fsEmpty || fsItem.isBefore(loMinDate));
        }
    };

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField04, txtField06, txtField07, txtField08, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23,
                txtField24, txtField25, txtField36, txtField32, txtField62, txtField65, txtField75, txtField81);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea17, textArea26, textArea27, textArea28);

        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField15, txtField16, txtField18,
                txtField20, txtField21, txtField22, txtField23, txtField24, txtField30,
                txtField31, txtField32, txtField33, txtField34, txtField35, txtField36,
                txtField39, txtField42, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField56, txtField58, txtField59, txtField60, txtField62,
                txtField63, txtField65, txtField68, txtField70, txtField74, txtField75,
                txtField81, txtField82, txtField84);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField15":
                    loJSON = oTransVSP.searchInquiry(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadVSPFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField15.setText("");
                        txtField15.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField16":
                    loJSON = oTransVSP.searchClient(lsValue, true);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField16.setText(oTransVSP.getMasterModel().getMasterModel().getBuyCltNm());
                        textArea17.setText(oTransVSP.getMasterModel().getMasterModel().getAddress());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField16.setText("");
                        txtField16.requestFocus();
                        return;
                    }
                    break;
                case "txtField18":
                    loJSON = oTransVSP.searchClient(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField18.setText(oTransVSP.getMasterModel().getMasterModel().getCoCltNm());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField18.setText("");
                        txtField18.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField20":
                    loJSON = oTransVSP.searchAvlVhcl(lsValue, "CS");
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField20.setText(oTransVSP.getMasterModel().getMasterModel().getCSNo());
                        txtField21.setText(oTransVSP.getMasterModel().getMasterModel().getPlateNo());
                        txtField22.setText(oTransVSP.getMasterModel().getMasterModel().getEngineNo());
                        txtField23.setText(oTransVSP.getMasterModel().getMasterModel().getFrameNo());
                        String lsKeyNo = "";
                        if (oTransVSP.getMasterModel().getMasterModel().getKeyNo() != null) {
                            lsKeyNo = oTransVSP.getMasterModel().getMasterModel().getKeyNo();
                        }
                        txtField25.setText(lsKeyNo);
                        textArea26.setText(oTransVSP.getMasterModel().getMasterModel().getVhclDesc());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField20.setText("");
                        txtField20.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField21":
                    loJSON = oTransVSP.searchAvlVhcl(lsValue, "PLT");
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField20.setText(oTransVSP.getMasterModel().getMasterModel().getCSNo());
                        txtField21.setText(oTransVSP.getMasterModel().getMasterModel().getPlateNo());
                        txtField22.setText(oTransVSP.getMasterModel().getMasterModel().getEngineNo());
                        txtField23.setText(oTransVSP.getMasterModel().getMasterModel().getFrameNo());
                        String lsKeyNo = "";
                        if (oTransVSP.getMasterModel().getMasterModel().getKeyNo() != null) {
                            lsKeyNo = oTransVSP.getMasterModel().getMasterModel().getKeyNo();
                        }
                        txtField25.setText(lsKeyNo);
                        textArea26.setText(oTransVSP.getMasterModel().getMasterModel().getVhclDesc());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField21.setText("");
                        txtField21.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField22":
                    loJSON = oTransVSP.searchAvlVhcl(lsValue, "ENG");
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField20.setText(oTransVSP.getMasterModel().getMasterModel().getCSNo());
                        txtField21.setText(oTransVSP.getMasterModel().getMasterModel().getPlateNo());
                        txtField22.setText(oTransVSP.getMasterModel().getMasterModel().getEngineNo());
                        txtField23.setText(oTransVSP.getMasterModel().getMasterModel().getFrameNo());
                        String lsKeyNo = "";
                        if (oTransVSP.getMasterModel().getMasterModel().getKeyNo() != null) {
                            lsKeyNo = oTransVSP.getMasterModel().getMasterModel().getKeyNo();
                        }
                        txtField25.setText(lsKeyNo);
                        textArea26.setText(oTransVSP.getMasterModel().getMasterModel().getVhclDesc());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField22.setText("");
                        txtField22.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField23":
                    loJSON = oTransVSP.searchAvlVhcl(lsValue, "FRM");
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField20.setText(oTransVSP.getMasterModel().getMasterModel().getCSNo());
                        txtField21.setText(oTransVSP.getMasterModel().getMasterModel().getPlateNo());
                        txtField22.setText(oTransVSP.getMasterModel().getMasterModel().getEngineNo());
                        txtField23.setText(oTransVSP.getMasterModel().getMasterModel().getFrameNo());
                        String lsKeyNo = "";
                        if (oTransVSP.getMasterModel().getMasterModel().getKeyNo() != null) {
                            lsKeyNo = oTransVSP.getMasterModel().getMasterModel().getKeyNo();
                        }
                        txtField25.setText(lsKeyNo);
                        textArea26.setText(oTransVSP.getMasterModel().getMasterModel().getVhclDesc());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField23.setText("");
                        txtField23.requestFocus();
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField32":
                    loJSON = oTransVSP.searchBankApp(lsValue.trim());
                    if (!"error".equals(loJSON.get("result"))) {
                        String lsBrBank = oTransVSP.getMasterModel().getMasterModel().getBankName() + " " + oTransVSP.getMasterModel().getMasterModel().getBrBankNm();
                        txtField32.setText(lsBrBank);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField36":
//                    loJSON = oTransVSP.searchPromo(lsValue);
//                    if (!"error".equals(loJSON.get("result"))) {
////                        txtField36.setText(oTransVSP.getMasterModel().getMasterModel());
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        txtField36.setText("");
//                        txtField36.requestFocus();
//                        return;
//                    }
                    break;
                case "txtField62":
                    loJSON = oTransVSP.searchInsurance(lsValue.trim(), true);
                    if (!"error".equals(loJSON.get("result"))) {

                        String lsBrTpl = oTransVSP.getMasterModel().getMasterModel().getTPLInsNm() + " " + oTransVSP.getMasterModel().getMasterModel().getTPLBrIns();
                        txtField62.setText(lsBrTpl);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
                case "txtField65":
                    loJSON = oTransVSP.searchInsurance(lsValue.trim(), false);
                    if (!"error".equals(loJSON.get("result"))) {
                        String lsBrCom = oTransVSP.getMasterModel().getMasterModel().getCOMInsNm() + " " + oTransVSP.getMasterModel().getMasterModel().getCOMBrIns();
                        txtField65.setText(lsBrCom);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    initFields(pnEditMode);
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
            initFields(pnEditMode);
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        }
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField24, txtField30, txtField31, txtField45, txtField46, txtField47, txtField48,
                txtField49, txtField50, txtField51, txtField52, txtField53, txtField54,
                txtField56, txtField58, txtField59, txtField60, txtField63, txtField68, txtField70,
                txtField74, txtField75, txtField82, txtField84
        );
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextField> loTxtFieldFinacne = Arrays.asList(txtField33, txtField34, txtField35, txtField39, txtField42);
        loTxtFieldFinacne.forEach(tf -> tf.focusedProperty().addListener(txtFieldFinance_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea27, textArea28);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
    }

    final ChangeListener<? super Boolean> txtFieldFinance_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        double lnUnitPrice = Double.parseDouble(txtField30.getText().replace(",", ""));
        if (lsValue == null) {
            return;
        }
//        if (!oTransVSP.getVSPFinanceList().isEmpty()) {
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 33: // Bank Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnBankDsc = Double.parseDouble(txtField33.getText().replace(",", ""));

                    if (lnBankDsc > lnUnitPrice) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Bank Discount cannot be greater than Vehicle Price");
                        lnBankDsc = 0.00;
                        return;
                    }
                    oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setDiscount(new BigDecimal(lnBankDsc));
                    txtField33.setText(poGetDecimalFormat.format(lnBankDsc));
                    break;
                case 34: // Terms
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    int lnTerm = Integer.parseInt(txtField34.getText());

                    if (lnTerm > lnUnitPrice) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Terms cannot be greater than Vehicle Price");
                        lnTerm = 0;
                    }
                    oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setAcctTerm(lnTerm);
                    txtField34.setText(String.valueOf(lnTerm));
                    break;
                case 35: // Rate
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnRate = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnRate > lnUnitPrice) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Rate cannot be greater than Vehicle Price");
                        lnRate = 0.00;
                    }
                    oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setAcctRate(lnRate);
                    txtField35.setText(poGetDecimalFormat.format(lnRate));
//                    System.out.println("test rates: " + poGetDecimalFormat.format(String.valuOf(oTransVSP.getMasterModel().getMasterModel().get)) );
                    break;
                case 39: //Net DownPayment
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnNetDown = Double.parseDouble(txtField39.getText().replace(",", ""));

                    if (lnNetDown > lnUnitPrice) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Net DownPayment cannot be greater than Vehicle Price");
                        lnNetDown = 0.00;
                    }
                    oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setNtDwnPmt(new BigDecimal(lnNetDown));
                    txtField39.setText(poGetDecimalFormat.format(lnNetDown));
                    break;
                case 42: // Prompt Payment Disc.
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnPrmptPayment = Double.parseDouble(txtField42.getText().replace(",", ""));

                    if (lnPrmptPayment > lnUnitPrice) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Prompt Payment Disc cannot be greater than Vehicle Price");
                        lnPrmptPayment = 0.00;
                    }
                    oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setRebates(new BigDecimal(lnPrmptPayment));
                    txtField42.setText(poGetDecimalFormat.format(lnPrmptPayment));
                    break;
            }
            loadVSPFields();
        } else {
            loTxtField.selectAll();
        }
    };
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        double lnUnitPrice = Double.parseDouble(txtField30.getText().replace(",", ""));
        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 24: // Vehicle Price
                    oTransVSP.getMasterModel().getMasterModel().setEndPlate(lsValue);
                    break;
                case 30: // Vehicle Price
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setUnitPrce(new BigDecimal(lsValue.replace(",", "")));
                    txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getUnitPrce()))));
                    break;
                case 31: // DownPayment
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double dwnPaymnt = Double.parseDouble(lsValue.replace(",", ""));
                    if (lnUnitPrice >= 0.00) {
                        if (dwnPaymnt > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "DownPayment cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setDownPaym(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField31.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setDownPaym(new BigDecimal(Double.valueOf(txtField31.getText().replace(",", ""))));
                    }
                    txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 45: //STD Fleet Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) >= lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "STD Fleet Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField45.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(Double.valueOf(txtField45.getText().replace(",", ""))));
                    }
                    txtField45.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getFleetDsc()))));
                    break;
                case 46: // STD Fleet Discount Plant
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    double lnValueSTDSup = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueSTDSup > 100.00 || lnValueSTDSup < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnValueSTDSup = 0.00;
                    }

                    double remainingValueDlrs = 100.00 - lnValueSTDSup;
                    oTransVSP.getMasterModel().getMasterModel().setDue2Sup(lnValueSTDSup);
                    txtField46.setText(poGetDecimalFormat.format(lnValueSTDSup));
                    oTransVSP.getMasterModel().getMasterModel().setDue2Dlr(remainingValueDlrs);
                    txtField47.setText(poGetDecimalFormat.format(remainingValueDlrs));
                    break;
                case 47:// STD Fleet Discount Dealer
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    double lnValueSTDDlr = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueSTDDlr > 100.00 || lnValueSTDDlr < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnValueSTDDlr = 0.00;
                    }

                    double remainingValueSup = 100.00 - lnValueSTDDlr;
                    oTransVSP.getMasterModel().getMasterModel().setDue2Dlr(lnValueSTDDlr);
                    txtField47.setText(poGetDecimalFormat.format(lnValueSTDDlr));
                    oTransVSP.getMasterModel().getMasterModel().setDue2Sup(remainingValueSup);
                    txtField46.setText(poGetDecimalFormat.format(remainingValueSup));
                    break;
                case 48: //SPL Fleet Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) >= lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "SPL Fleet Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField48.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(Double.valueOf(txtField48.getText().replace(",", ""))));
                    }
                    txtField48.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFltDsc()))));
                    break;
                case 49://SPL Fleet Discount Plant
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    double lnValueSPLSup = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueSPLSup > 100.00 || lnValueSPLSup < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnValueSPLSup = 0.00;
                    }

                    double remainingValueSDlrs = 100.00 - lnValueSPLSup;
                    oTransVSP.getMasterModel().getMasterModel().setSPFD2Sup(lnValueSPLSup);
                    txtField49.setText(poGetDecimalFormat.format(lnValueSPLSup));
                    oTransVSP.getMasterModel().getMasterModel().setSPFD2Dlr(remainingValueSDlrs);
                    txtField50.setText(poGetDecimalFormat.format(remainingValueSDlrs));
                    break;
                case 50://SPL Fleet Discount Dealer
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    double lnValueSPLDlr = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValueSPLDlr > 100.00 || lnValueSPLDlr < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnValueSPLDlr = 0.00;
                    }

                    double remainingValueForsup = 100.00 - lnValueSPLDlr;
                    oTransVSP.getMasterModel().getMasterModel().setSPFD2Dlr(lnValueSPLDlr);
                    txtField50.setText(poGetDecimalFormat.format(lnValueSPLDlr));
                    oTransVSP.getMasterModel().getMasterModel().setSPFD2Sup(remainingValueForsup);
                    txtField49.setText(poGetDecimalFormat.format(remainingValueForsup));
                    break;
                case 51: //Promo Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) >= lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Promo Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField51.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(Double.valueOf(txtField51.getText().replace(",", ""))));
                    }
                    txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPromoDsc()))));
                    break;
                case 52: // Promo Discount Plant
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    double lnValuePromoSup = Double.parseDouble(lsValue.replace(",", ""));

                    if (lnValuePromoSup > 100.00 || lnValuePromoSup < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnValuePromoSup = 0.00; // Reset to 0.00 for invalid amount
                    }

                    double remainingValueForDealer = 100.00 - lnValuePromoSup;
                    oTransVSP.getMasterModel().getMasterModel().setPrmD2Sup(lnValuePromoSup);
                    txtField52.setText(poGetDecimalFormat.format(lnValuePromoSup));
                    oTransVSP.getMasterModel().getMasterModel().setPrmD2Dlr(remainingValueForDealer);
                    txtField53.setText(poGetDecimalFormat.format(remainingValueForDealer));
                    break;
                case 53: // Promo Discount Dealer
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }

                    double lnValuePromoDlr = Double.parseDouble(lsValue.replace(",", ""));
                    if (lnValuePromoDlr > 100.00 || lnValuePromoDlr < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnValuePromoDlr = 0.00; // Reset to 0.00 for invalid amount
                    }
                    double remainingValueForSupplier = 100.00 - lnValuePromoDlr;
                    oTransVSP.getMasterModel().getMasterModel().setPrmD2Dlr(lnValuePromoDlr);
                    txtField53.setText(poGetDecimalFormat.format(lnValuePromoDlr));
                    oTransVSP.getMasterModel().getMasterModel().setPrmD2Sup(remainingValueForSupplier);
                    txtField52.setText(poGetDecimalFormat.format(remainingValueForSupplier));
                    break;
                case 54:// Cash Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) >= lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Cash Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField54.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(Double.valueOf(txtField48.getText().replace(",", ""))));
                    }
                    txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAddlDsc()))));
                    break;
                case 56: //Bundle Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) >= lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Bundle Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField56.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(Double.valueOf(txtField56.getText().replace(",", ""))));
                    }
                    txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getBndleDsc()))));
                    break;
                case 58: //Insurance Discount
//                    if (lsValue.isEmpty()) {
//                        lsValue = "0.00";
//                    }
//                    oTransVSP.getMasterModel().getMasterModel().setInsurAmt(BigDecimal.valueOf(Double.valueOf(lsValue.replace(",", "")))
//                    );
//                    txtField58.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getInsurAmt()))));
//                    break;
                    break;
                case 59: //OMA & CMF
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setAdvDwPmt(new BigDecimal(Double.valueOf(lsValue.replace(",", "")))
                    );
                    txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAdvDwPmt()))));
                    break;
                case 60: //TPL Insurance
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(Double.valueOf(lsValue.replace(",", "")))
                    );
                    txtField60.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTPLAmt()))));
                    break;
                case 63: //Compre Insurance
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setCompAmt(new BigDecimal(Double.valueOf(lsValue.replace(",", "")))
                    );
                    txtField63.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getCompAmt()))));
                    break;
                case 68: //LTO
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setLTOAmt(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    txtField68.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getLTOAmt()))));
                    break;
                case 70: // CHMO/Doc Stamps
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setChmoAmt(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    txtField70.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getChmoAmt()))));
                    break;
                case 74: //Misc Charges
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setOthrChrg(new BigDecimal(Double.valueOf(lsValue.replace(",", ""))));
                    txtField74.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getOthrChrg()))));
                    break;
                case 75://Description
                    oTransVSP.getMasterModel().getMasterModel().setOthrDesc(lsValue);
                    break;
                case 82: // DownPayment
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double diIncent = Double.parseDouble(lsValue.replace(",", ""));
                    if (lnUnitPrice >= 0.00) {
                        if (diIncent > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Dealer Incentives Rate cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setDealrRte((Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField82.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setDealrRte(Double.valueOf(txtField82.getText().replace(",", "")));
                    }
                    txtField82.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDealrRte()))));
                    break;
                case 84: // DownPayment
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double siIncent = Double.parseDouble(lsValue.replace(",", ""));
                    if (lnUnitPrice >= 0.00) {
                        if (siIncent > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Sales Executive Incentives Rate cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTransVSP.getMasterModel().getMasterModel().setSlsInRte((Double.valueOf(lsValue.replace(",", ""))));
                    if (!loadVSPFields()) {
                        txtField84.setText("0.00");
                        oTransVSP.getMasterModel().getMasterModel().setSlsInRte(Double.valueOf(txtField84.getText().replace(",", "")));
                    }
                    txtField84.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSlsInRte()))));
                    break;
            }
            loadVSPFields();
        } else {
            loTxtField.selectAll();
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
                case 28:
                    oTransVSP.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };
    //TableView KeyPressed

    private void initTableKeyPressed() {
        tblViewLabor.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this labor?")) {
                        Labor selectedVSPLabor = getLaborSelectedItem();
                        int removeCount = 0;
                        if (selectedVSPLabor != null) {
                            String lsRow = selectedVSPLabor.getTblindex01_labor();
                            String lsLaborID = selectedVSPLabor.getTblindex03_labor();
                            String lsJO = selectedVSPLabor.getTblindex10_labor();
                            int lnRow = Integer.parseInt(lsRow);
                            if (lsJO.isEmpty()) {
                                oTransVSP.removeVSPLabor(lnRow - 1);
                                removeCount++;
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, "Labor " + lsLaborID + " already job order.");
                            }
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Removed labor successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Removed labor failed");
                        }
                        loadVSPFields();
                        loadLaborTable();
                    } else {
                        return;
                    }
                }
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
                            String lsParts = selectedVSPPart.getTblindex09_part();
                            String lsJO = selectedVSPPart.getTblindex11_part();
                            int lnRow = Integer.parseInt(lsRow);
                            if (lsJO.isEmpty()) {
                                oTransVSP.removeVSPParts(lnRow - 1);
                                removeCount++;
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, "Accessories " + lsParts + " already job order");
                            }
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Removed accessories successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Removed accessories failed");
                        }
                        loadVSPFields();
                        loadAccessoriesTable();
                    }
                } else {
                    return;
                }
            }
        });
    }

    private Labor getLaborSelectedItem() {
        return tblViewLabor.getSelectionModel().getSelectedItem();
    }

    private Part getPartSelectedItem() {
        return tblViewAccessories.getSelectionModel().getSelectedItem();
    }

    private void initComboBoxItems() {
        comboBox05.setItems(cInquiryType);
        comboBox29.setItems(cModeOfPayment);
        comboBox37.setItems(cFinPromoType);
        comboBox61.setItems(cTplType);
        comboBox64.setItems(cCompType);
        comboBox66.setItems(cCompYearType1);
        comboBox67.setItems(cCompYearType2);
        comboBox69.setItems(cLTOType);
        comboBox71.setItems(cHMOType);
    }

    private void initCmboxFieldAction() {

        chckBoxSpecialAccount.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (chckBoxSpecialAccount.isSelected()) {
                    oTransVSP.getMasterModel().getMasterModel().setIsVIP("1");
                    chckBoxSpecialAccount.setSelected(true);
                } else {
                    chckBoxSpecialAccount.setSelected(false);
                    oTransVSP.getMasterModel().getMasterModel().setIsVIP("0");
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox29.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                int originalPay = Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getPayMode());
                int originalIndex = comboBox29.getSelectionModel().getSelectedIndex();
                if (originalIndex >= 0 && originalIndex < comboBox29.getItems().size()) {
                    switch (originalIndex) {
                        case 0:
                            if (oTransVSP.getVSPFinanceList().size() - 1 == 0) {
                                if (ShowMessageFX.YesNo(null, pxeModuleName,
                                        "Are you sure you want to replace payment mode to cash?\n"
                                        + "if YES: from Downpayment to Promissory Note Amt will be cleared\n"
                                        + "if NO: it will remain the values.")) {
                                    oTransVSP.removeVSPFinance(0);
                                    oTransVSP.getMasterModel().getMasterModel().setDownPaym(new BigDecimal("0.00"));
                                    oTransVSP.getMasterModel().getMasterModel().setChmoStat("0");
                                    comboBox71.getSelectionModel().select(0);
                                    oTransVSP.getMasterModel().getMasterModel().setChmoAmt(new BigDecimal("0.00"));
                                    txtField70.setText("0.00");
                                    oTransVSP.getMasterModel().getMasterModel().setDealrRte(0.00);
                                    oTransVSP.getMasterModel().getMasterModel().setSlsInRte(0.00);
                                    clearFinanceFields();
                                } else {
                                    Platform.runLater(() -> {
                                        comboBox29.getSelectionModel().select(originalPay); // Restore previous item
                                    });
                                    return;
                                }
                            }
                            break;
                        default:
                            if (oTransVSP.getVSPFinanceList().size() - 1 < 0) {
                                oTransVSP.addVSPFinance();
                            }
                            break;
                    }
                    oTransVSP.getMasterModel().getMasterModel().setPayMode(String.valueOf(comboBox29.getSelectionModel().getSelectedIndex()));
                    initFields(pnEditMode);
                    loadVSPFields();
                }
            }
        }
        );

        comboBox37.setOnAction(event
                -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox37.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setFinPromo(String.valueOf(comboBox37.getSelectionModel().getSelectedIndex()));
                    switch (comboBox37.getSelectionModel().getSelectedIndex()) {
                        case 0: // NONE
                        case 1: // ALL-IN HOUSE (Regular)
                            oTransVSP.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(0.00));
                            txtField56.setText("0.00");
                            break;
                        case 2: // ALL-IN PROMO
                            oTransVSP.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(0.00));
                            txtField54.setText("0.00");
                            break;
                    }
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox61.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVSP.getMasterModel().getMasterModel().setTPLStat(String.valueOf(comboBox61.getSelectionModel().getSelectedIndex()));
                if (comboBox61.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVSP.getMasterModel().getMasterModel().setInsTplCd("");
                    oTransVSP.getMasterModel().getMasterModel().setTPLBrIns("");
                    oTransVSP.getMasterModel().getMasterModel().setTPLInsNm("");
                    txtField62.setText("");
                    oTransVSP.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(0.00));
                    txtField60.setText("0.00");
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox64.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox64.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVSP.getMasterModel().getMasterModel().setInsCode("");
                    oTransVSP.getMasterModel().getMasterModel().setCOMBrIns("");
                    oTransVSP.getMasterModel().getMasterModel().setCOMInsNm("");
                    oTransVSP.getMasterModel().getMasterModel().setCompAmt(new BigDecimal(0.00));
                    oTransVSP.getMasterModel().getMasterModel().setInsurTyp("");
                    oTransVSP.getMasterModel().getMasterModel().setInsurYr(0);
                    txtField63.setText("0.00");
                    txtField65.setText("");
                    comboBox66.setValue(null);
                    comboBox67.setValue(null);
                }
                if (comboBox64.getSelectionModel().getSelectedIndex() >= 0) {

                    if (!comboBox67.getItems().contains("0")) {
                        comboBox67.getItems().add(0, "0");
                    }

                    switch (comboBox64.getSelectionModel().getSelectedIndex()) {
                        case 0:
                        case 2:
                        case 4:
                            oTransVSP.getMasterModel().getMasterModel().setInsurTyp("0");
                            comboBox66.getSelectionModel().select(0);
                            comboBox67.setValue("0");
                            oTransVSP.getMasterModel().getMasterModel().setInsurYr(0);
                            break;
                        case 1:
                            oTransVSP.getMasterModel().getMasterModel().setInsurTyp("1");
                            comboBox66.getSelectionModel().select(1);
                            comboBox67.getItems().remove("0");
                            break;
                        case 3:
//                            comboBox66.getItems().remove("NONE");
//                            comboBox67.getItems().remove("0"); // Remove "0" when selecting indices 1-4
                            break;
                    }
                    oTransVSP.getMasterModel().getMasterModel().setCompStat(String.valueOf(comboBox64.getSelectionModel().getSelectedIndex()));
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox66.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                int selectedIndex = comboBox66.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    // Ensure "0" is in the comboBox67 items list before setting the value
                    if (!comboBox67.getItems().contains("0")) {
                        comboBox67.getItems().add(0, "0"); // Add "0" at the start of the list if it's not there
                    }
                    if (selectedIndex == 0) {
                        comboBox67.setValue("0");
                        oTransVSP.getMasterModel().getMasterModel().setInsurYr(0);
                        comboBox67.getSelectionModel().select("0");
                    } else if (selectedIndex >= 1 && selectedIndex <= 4) {
                        comboBox67.getItems().remove("0"); // Remove "0" when selecting indices 1-4
                        comboBox67.setValue(null);
                        oTransVSP.getMasterModel().getMasterModel().setInsurYr(null); // Use null to set the insurance year to null
                        comboBox67.getSelectionModel().clearSelection();
                    }
                    oTransVSP.getMasterModel().getMasterModel().setInsurTyp(String.valueOf(comboBox66.getSelectionModel().getSelectedIndex()));
                    initFields(pnEditMode);
                }
            }
        }
        );

        comboBox67.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox67.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVSP.getMasterModel().getMasterModel().setInsurYr(Integer.valueOf(comboBox67.getValue()));
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox69.setOnAction(e
                -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVSP.getMasterModel().getMasterModel().setLTOStat(String.valueOf(comboBox69.getSelectionModel().getSelectedIndex()));
                if (comboBox69.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVSP.getMasterModel().getMasterModel().setLTOAmt(new BigDecimal(0.00));
                    txtField68.setText("0.00");
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox71.setOnAction(e
                -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVSP.getMasterModel().getMasterModel().setChmoStat(String.valueOf(comboBox71.getSelectionModel().getSelectedIndex()));
                if (comboBox71.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVSP.getMasterModel().getMasterModel().setCompAmt(new BigDecimal(0.00));
                    txtField70.setText("0.00");
                }
                initFields(pnEditMode);
            }
        }
        );
    }

    private void clearFinanceFields() {
        List<TextField> loTxtField = Arrays.asList(txtField31, txtField33, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42,
                txtField43, txtField44, txtField82, txtField83, txtField84, txtField85);
        loTxtField.forEach(tf -> tf.setText("0.00"));
        txtField32.setText("");
        txtField34.setText("0");
        txtField36.setText("");
        comboBox37.setValue(null);
    }

    private void initDatePropertyAction() {
        datePicker09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVSP.getMasterModel().getMasterModel().setDelvryDt(SQLUtil.toDate(datePicker09.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
    }

    private void initTextPropertyAction() {
        txtField15.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setClientID("");
                        oTransVSP.getMasterModel().getMasterModel().setCoCltID("");
                        oTransVSP.getMasterModel().getMasterModel().setIsVhclNw("");
                        oTransVSP.getMasterModel().getMasterModel().setCSNo("");
                        oTransVSP.getMasterModel().getMasterModel().setPlateNo("");
                        oTransVSP.getMasterModel().getMasterModel().setEngineNo("");
                        oTransVSP.getMasterModel().getMasterModel().setFrameNo("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclDesc("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclFDsc("");
                        oTransVSP.getMasterModel().getMasterModel().setKeyNo("");
                        clearFields();
                        clearFinanceFields();
                    }
                }
                initFields(pnEditMode);
            }
        });
        txtField16.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setBuyCltNm("");
                    }
                }
            }
        });
        txtField18.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setCoCltID("");
                        oTransVSP.getMasterModel().getMasterModel().setCoCltNm("");
                    }
                }
            }
        });
        txtField20.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setCSNo("");
                        oTransVSP.getMasterModel().getMasterModel().setPlateNo("");
                        oTransVSP.getMasterModel().getMasterModel().setEngineNo("");
                        oTransVSP.getMasterModel().getMasterModel().setFrameNo("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclDesc("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclFDsc("");
                        oTransVSP.getMasterModel().getMasterModel().setKeyNo("");
                        txtField21.setText("");
                        txtField22.setText("");
                        txtField23.setText("");
                        txtField25.setText("");
                        textArea26.setText("");
                    }
                }
            }
        });
        txtField21.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setCSNo("");
                        oTransVSP.getMasterModel().getMasterModel().setPlateNo("");
                        oTransVSP.getMasterModel().getMasterModel().setEngineNo("");
                        oTransVSP.getMasterModel().getMasterModel().setFrameNo("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclDesc("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclFDsc("");
                        oTransVSP.getMasterModel().getMasterModel().setKeyNo("");
                        txtField20.setText("");
                        txtField22.setText("");
                        txtField23.setText("");
                        txtField25.setText("");
                        textArea26.setText("");
                    }
                }
            }
        });
        txtField22.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setCSNo("");
                        oTransVSP.getMasterModel().getMasterModel().setPlateNo("");
                        oTransVSP.getMasterModel().getMasterModel().setEngineNo("");
                        oTransVSP.getMasterModel().getMasterModel().setFrameNo("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclDesc("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclFDsc("");
                        oTransVSP.getMasterModel().getMasterModel().setKeyNo("");
                        txtField20.setText("");
                        txtField21.setText("");
                        txtField23.setText("");
                        txtField25.setText("");
                        textArea26.setText("");
                    }
                }
            }
        });
        txtField23.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setCSNo("");
                        oTransVSP.getMasterModel().getMasterModel().setPlateNo("");
                        oTransVSP.getMasterModel().getMasterModel().setEngineNo("");
                        oTransVSP.getMasterModel().getMasterModel().setFrameNo("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclDesc("");
                        oTransVSP.getMasterModel().getMasterModel().setVhclFDsc("");
                        oTransVSP.getMasterModel().getMasterModel().setKeyNo("");
                        txtField20.setText("");
                        txtField21.setText("");
                        txtField22.setText("");
                        txtField25.setText("");
                        textArea26.setText("");
                    }
                }
            }
        });
        txtField32.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setBnkAppCD("");
                        oTransVSP.getMasterModel().getMasterModel().setBankName("");
                        if (oTransVSP.getVSPFinanceList().size() > 0) {
                            oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setBankID("");
                            oTransVSP.getVSPFinanceModel().getVSPFinanceModel().setBankname("");
                        }

                        txtField33.setText("0.00");
                    }
                }
            }
            initFields(pnEditMode);
        });
        txtField45.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty() || newValue.equals("0.00") || newValue.equals("0.0") || newValue.equals("0")) {
                        oTransVSP.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(0.00));
                        oTransVSP.getMasterModel().getMasterModel().setDue2Sup(0.00);
                        oTransVSP.getMasterModel().getMasterModel().setDue2Dlr(0.00);
                        txtField45.setText("0.00");
                        txtField46.setText("0.00");
                        txtField47.setText("0.00");
                        initFields(pnEditMode);
                    }
                }
            }
        });
        txtField48.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty() || newValue.equals("0.00") || newValue.equals("0.0") || newValue.equals("0")) {
                        oTransVSP.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(0.00));
                        oTransVSP.getMasterModel().getMasterModel().setSPFD2Sup(0.00);
                        oTransVSP.getMasterModel().getMasterModel().setSPFD2Dlr(0.00);
                        txtField48.setText("0.00");
                        txtField49.setText("0.00");
                        txtField50.setText("0.00");
                        initFields(pnEditMode);
                    }
                }
            }
        });
        txtField51.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty() || newValue.equals("0.00") || newValue.equals("0.0") || newValue.equals("0")) {
                        oTransVSP.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(0.00));
                        oTransVSP.getMasterModel().getMasterModel().setPrmD2Sup(0.00);
                        oTransVSP.getMasterModel().getMasterModel().setPrmD2Dlr(0.00);
                        txtField51.setText("0.00");
                        txtField52.setText("0.00");
                        txtField53.setText("0.00");
                        initFields(pnEditMode);
                    }
                }
            }
        });
        txtField62.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setInsTplCd("");
                        oTransVSP.getMasterModel().getMasterModel().setTPLBrIns("");
                        oTransVSP.getMasterModel().getMasterModel().setTPLInsNm("");
                    }
                }
            }
        });
        txtField65.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVSP.getMasterModel().getMasterModel().setInsCode("");
                        oTransVSP.getMasterModel().getMasterModel().setCOMInsNm("");
                        oTransVSP.getMasterModel().getMasterModel().setCOMBrIns("");
                    }
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    private void clearFields() {
        List<TextField> loTxtFieldString = Arrays.asList(txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
                txtField10, txtField11, txtField12, txtField13, txtField14, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField32, txtField36,
                txtField62, txtField65, txtField75, txtField81);
        loTxtFieldString.forEach(tf -> tf.setText(""));

        List<TextArea> loTxtAreaString = Arrays.asList(textArea17, textArea26);
        loTxtAreaString.forEach(ta -> ta.setText(""));
        List<TextField> loTxtFieldDouble = Arrays.asList(txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                txtField77, txtField78, txtField79, txtField80, txtField82, txtField83, txtField84, txtField85);
        loTxtFieldDouble.forEach(tf -> tf.setText("0.00"));
        txtField34.setText("0");
        List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
        loComboBox.forEach(cmB -> cmB.setValue(null));
        datePicker09.setValue(LocalDate.of(1999, Month.JANUARY, 1));
        brandNewCat.setSelected(false);
        preOwnedCat.setSelected(false);
        chckBoxSpecialAccount.setSelected(false);
        chckBoxRustProof.setSelected(false);
        chckBoxPermaShine.setSelected(false);
        chckBoxUndercoat.setSelected(false);
        chckBoxTint.setSelected(false);
        tgUnitCategory.selectToggle(null);
        lblDRNo.setText("");
        lblSINo.setText("");
        lblVSPStatus.setText("");
        lblPrint.setText("");
        lblRFNo.setText("");
        lblApproveDate.setText("");
        switchToTab(tabMain, ImTabPane);
    }

    private void initButtonClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnCancelVSP,
                btnApprove, btnGatePass, btnClose, btnJobOrderAdd, btnAdditionalLabor, btnAddParts,
                btnPaymentHistory, btnAddReservation, btnRemoveReservation);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearVSPFields();
                clearTables();
                switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
                oTransVSP = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                loJSON = oTransVSP.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVSPFields();
                    pnEditMode = oTransVSP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransVSP.updateTransaction();
                pnEditMode = oTransVSP.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Sales Proposal Information Saving....", "Are you sure, do you want to save?")) {
                    if (comboBox05.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select Inquiry Source.");
                        return;
                    }
                    loJSON = oTransVSP.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Sales Proposal Information", (String) loJSON.get("message"));
                        loJSON = oTransVSP.openTransaction(oTransVSP.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadVSPFields();
                            loadLaborTable();
                            loadAccessoriesTable();
                            pnEditMode = oTransVSP.getEditMode();
                            initFields(pnEditMode);
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearVSPFields();
                    clearTables();
                    switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
                    oTransVSP = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Sales Proposal Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        pnEditMode = oTransVSP.getEditMode();
                        return;
                    }
                }
                loJSON = oTransVSP.searchTransaction("", false, false);
                if ("success".equals((String) loJSON.get("result"))) {
                    switchToTab(tabMain, ImTabPane);
                    loadVSPFields();
                    loadLaborTable();
                    loadAccessoriesTable();
                    pnEditMode = oTransVSP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Sales Proposal Information", (String) loJSON.get("message"));
                }
                break;
            case "btnAdditionalLabor":
                try {
                oTransVSP.addVSPLabor();
                loadLaborWindowDialog(oTransVSP.getVSPLaborList().size() - 1, true, "", false);
                loadLaborTable();

            } catch (IOException ex) {
                Logger.getLogger(VSPController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnAddParts":
                    try {
                oTransVSP.addVSPParts();
                loadAccessoriesWindowDialog(oTransVSP.getVSPPartsList().size() - 1, true);
                loadAccessoriesTable();

            } catch (IOException ex) {
                Logger.getLogger(VSPController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnPrint":
                try {
                loadVSPPrint();

            } catch (SQLException ex) {
                Logger.getLogger(VSPController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Vehicle Sales Proposal");
                    } else {
                        ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                    }
                }
                break;
            case "btnCancelVSP":
                loJSON = oTransFollow.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    try {
                        loadLostSaleWindow(false);

                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleInquiryController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ShowMessageFX.Warning(null, "Integrated Automotive System", (String) loJSON.get("message"));
                }
                break;
            case "btnJobOrderAdd":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to create new sales job order record?")) {
                    loadSJOWindow();
                } else {
                    return;
                }
                break;
            case "btnAddReservation":
                try {
                loadAddReservationWindowDialog();

            } catch (IOException ex) {
                Logger.getLogger(VSPController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnRemoveReservation":
                try {
                loadRemoveReservationWindowDialog();

            } catch (IOException ex) {
                Logger.getLogger(VSPController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnGatePass":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want create gatepass")) {
                    loadGatePassWindow(false);
                } else {
                    return;
                }
                break;
            default:
                ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                break;
        }
        initFields(pnEditMode);
    }

    private void loadAddReservationWindowDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPAddReservationInquiries.fxml"));
            VSPAddReservationInquiriesController loControl = new VSPAddReservationInquiriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVSP);
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
            loadVSPFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    private void loadRemoveReservationWindowDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPRemoveReservationInquiries.fxml"));
            VSPRemoveReservationInquiriesController loControl = new VSPRemoveReservationInquiriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVSP);
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
            loadVSPFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }

    }

    @SuppressWarnings("unchecked")
    private void clearVSPFields() {
        List<TextField> loTxtFieldString = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
                txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField32, txtField36,
                txtField62, txtField65, txtField75, txtField81);
        loTxtFieldString.forEach(tf -> tf.setText(""));

        List<TextArea> loTxtAreaString = Arrays.asList(textArea28);

        loTxtAreaString.forEach(ta -> ta.setText(""));
        List<TextField> loTxtFieldDouble = Arrays.asList(txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                txtField77, txtField78, txtField79, txtField80, txtField82, txtField83, txtField84, txtField85);
        loTxtFieldDouble.forEach(tf -> tf.setText("0.00"));
        txtField34.setText("0");
        List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
        loComboBox.forEach(cmB -> cmB.setValue(null));
        datePicker09.setValue(LocalDate.of(1999, Month.JANUARY, 1));
        brandNewCat.setSelected(false);
        preOwnedCat.setSelected(false);
        chckBoxSpecialAccount.setSelected(false);
        chckBoxRustProof.setSelected(false);
        chckBoxPermaShine.setSelected(false);
        chckBoxUndercoat.setSelected(false);
        chckBoxTint.setSelected(false);
        tgUnitCategory.selectToggle(null);
        lblDRNo.setText("");
        lblSINo.setText("");
        lblVSPStatus.setText("");
        lblPrint.setText("");
        lblRFNo.setText("");
        lblApproveDate.setText("");
        switchToTab(tabMain, ImTabPane);
    }

    private void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    private void clearTables() {
        laborData.clear();
        accessoriesData.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        setDisable(true,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, comboBox37,
                txtField39, txtField42, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField56, txtField59, txtField60, txtField62,
                txtField63, txtField65, txtField68, txtField70, txtField72, txtField73, txtField74, txtField75, btnJobOrderAdd,
                txtField51, txtField53, txtField56, comboBox71, txtField82, txtField84);
        setDisable(!lbShow,
                txtField15, textArea28,
                //discount
                comboBox61, comboBox64, comboBox66, comboBox67, comboBox69,
                //add ons
                txtField74, chckBoxRustProof, chckBoxPermaShine, chckBoxUndercoat, chckBoxTint,
                btnAdditionalLabor, btnAddParts, datePicker09
        );
        tabAddOns.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        tabDetails.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        //depends if empty or not
        setDisable(!(lbShow && !txtField15.getText().isEmpty()),
                txtField16, txtField18,
                txtField20, txtField21, txtField22, txtField23, txtField24,
                chckBoxSpecialAccount, comboBox29, txtField30, btnAddReservation, btnRemoveReservation);

        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            //Payment Mode
            if (comboBox29.getSelectionModel().getSelectedIndex() >= 0) {
                double ldVhclSRP = Double.parseDouble(txtField30.getText().replace(",", ""));
                switch (comboBox29.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        setDisable(true, txtField31, txtField59, comboBox71);
                        setDisable(!lbShow, txtField51, txtField54, txtField56);
                        break;
                    case 1: //BANK PURCHASE ORDER
                        txtField31.setDisable(!lbShow);
                        txtField32.setDisable(!lbShow);
                        if (ldVhclSRP > 0.00 || ldVhclSRP > 0.0) {
                            setDisable(!(lbShow && !txtField32.getText().isEmpty()),
                                    txtField33,
                                    //                                                                        txtField36,
                                    comboBox37,
                                    txtField39,
                                    txtField42,
                                    txtField59,
                                    comboBox71,
                                    txtField82,
                                    txtField84
                            );
                            setDisable(true, txtField34, txtField35);
                        }
                        break;
                    case 2: //BANK FINANCING
                    case 3: // Company
                    case 4:
                        txtField31.setDisable(!lbShow);
                        txtField32.setDisable(!lbShow);
                        if (ldVhclSRP > 0.00 || ldVhclSRP > 0.0) {
                            setDisable(!(lbShow && !txtField32.getText().isEmpty()),
                                    txtField33,
                                    txtField34,
                                    txtField35,
                                    //                                    txtField36,
                                    comboBox37,
                                    txtField39,
                                    txtField42,
                                    txtField59,
                                    comboBox71,
                                    txtField82,
                                    txtField84
                            );
                        }
                        break;
                }
            }
            //Finance Promo
            switch (comboBox37.getSelectionModel().getSelectedIndex()) {
                case 0: // NONE
                case 1: // ALL-IN HOUSE (Regular)
                    txtField51.setDisable(false); // Enable Promo Disc
                    txtField54.setDisable(false); // Enable Cash Disc
                    txtField56.setDisable(true);  // Disable Bundle Disc
                    break;
                case 2: // ALL-IN PROMO
                    txtField51.setDisable(false); // Enable Promo Disc
                    txtField54.setDisable(true);  // Disable Cash Disc
                    txtField56.setDisable(false); // Enable Bundle Disc
                    break;
            }
            if (chckBoxSpecialAccount.isSelected()) {
                txtField45.setDisable(!lbShow);
                txtField48.setDisable(!lbShow);
            }
            double ldSTDFleet = Double.parseDouble(txtField45.getText().replace(",", ""));
            double ldSPLFleet = Double.parseDouble(txtField48.getText().replace(",", ""));
            double ldPromoDsc = Double.parseDouble(txtField51.getText().replace(",", ""));
            if (ldSTDFleet > 0.00 || ldSTDFleet > 0.0) {
                txtField46.setDisable(!lbShow);
                txtField47.setDisable(!lbShow);
            }
            if (ldSPLFleet > 0.00 || ldSPLFleet > 0.0) {
                txtField49.setDisable(!lbShow);
                txtField50.setDisable(!lbShow);
            }
            if (ldPromoDsc > 0.00 || ldPromoDsc > 0.0) {
                txtField52.setDisable(!lbShow);
                txtField53.setDisable(!lbShow);
            }
            //TPL insurance
            switch (comboBox61.getSelectionModel().getSelectedIndex()) {
                case 1: //FOC
                    txtField60.setDisable(true);
                    txtField62.setDisable(!lbShow);
                    break;
                case 3:
                    txtField60.setDisable(!lbShow);
                    txtField62.setDisable(!lbShow);
                    break;
                default:
                    txtField60.setDisable(true);
                    txtField62.setDisable(true);
                    break;
            }
            //Compre Insurance
            switch (comboBox64.getSelectionModel().getSelectedIndex()) {
                case 1: //FOC
                    txtField63.setDisable(true);
                    txtField65.setDisable(!lbShow);
                    comboBox66.setDisable(true);
                    comboBox67.setDisable(!lbShow);
                    break;
                case 3:
                    txtField63.setDisable(!lbShow);
                    txtField65.setDisable(!lbShow);
                    comboBox66.setDisable(!lbShow);
                    comboBox67.setDisable(!lbShow);
                    break;
                default: //NONE
                    txtField63.setDisable(true);
                    txtField65.setDisable(true);
                    comboBox66.setDisable(true);
                    comboBox67.setDisable(true);
                    break;
            }
            //Compre Ins Type
            switch (comboBox66.getSelectionModel().getSelectedIndex()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    comboBox67.setDisable(!lbShow);
                    break;
                default:
                    comboBox67.setDisable(true);
                    break;
            }
            switch (comboBox69.getSelectionModel().getSelectedIndex()) {
                case 2: //
                    txtField68.setDisable(!lbShow);
                    break;
                default:
                    txtField68.setDisable(true);
                    break;
            }
            switch (comboBox71.getSelectionModel().getSelectedIndex()) {
                case 2:
                case 3:
                    txtField70.setDisable(!lbShow);
                    break;
                default:
                    txtField70.setDisable(true);
                    break;
            }
            double ldMSChrgs = Double.parseDouble(txtField74.getText().replace(",", ""));
            if (ldMSChrgs > 0.00 || ldMSChrgs > 0.0) {
                txtField75.setDisable(!lbShow);
            }
//            if (!((String) oTransVSP.getMaster("sSerialID")).isEmpty()) {
//                if (!lblVSPStatus.getText().equals("Cancelled")) {
//                    if (oTransVSP.getVSPLaborCount() != 0 || oTransVSP.getVSPPartsCount() != 0) {
//                        boolean isLaborApproved = tblViewLabor.getItems().stream().anyMatch(item -> !item.getTblindex14_Labor().isEmpty());
//                        boolean isPartsApproved = tblViewAccessories.getItems().stream().anyMatch(item -> !item.getTblindex20_Part().isEmpty());
//
//                        btnJobOrderAdd.setDisable(!(isLaborApproved || isPartsApproved));
//                    } else {
//                        btnJobOrderAdd.setDisable(true);
//                    }
//
//                }
//            } else {
//                btnJobOrderAdd.setDisable(true);
//            }
        }
        if (fnValue == EditMode.UPDATE) {
            if (oTransVSP.getMasterModel().getMasterModel().getSerialID() != null) {
                if (!oTransVSP.getMasterModel().getMasterModel().getSerialID().isEmpty()) {
                    setDisable(true, txtField20, txtField21, txtField22, txtField23);
                }
            }

            setDisable(true, txtField15);
            if (oTransVSP.getMasterModel().getMasterModel().getTPLType() != null) {
                if (!oTransVSP.getMasterModel().getMasterModel().getTPLType().isEmpty()) {
                    comboBox61.setDisable(true);
                    txtField62.setDisable(true);

                }
            }
            if (oTransVSP.getMasterModel().getMasterModel().getCOMType() != null) {
                if (!oTransVSP.getMasterModel().getMasterModel().getCOMType().isEmpty()) {
                    comboBox64.setDisable(true);
                    txtField65.setDisable(true);

                }
            }
            if (oTransVSP.getMasterModel().getMasterModel().getBOTType() != null) {
                if (!oTransVSP.getMasterModel().getMasterModel().getBOTType().isEmpty()) {
                    comboBox61.setDisable(true);
                    txtField62.setDisable(true);
                    comboBox64.setDisable(true);
                    txtField65.setDisable(true);
                }
            }
        }
        if (!tblViewLabor.getItems().isEmpty() || !tblViewAccessories.getItems().isEmpty()) {
            btnJobOrderAdd.setDisable(fnValue == EditMode.ADDNEW || lblVSPStatus.getText().equals("Cancelled"));
        }
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnApprove.setManaged(false);
        btnApprove.setVisible(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancelVSP.setManaged(false);
        btnCancelVSP.setVisible(false);
        btnGatePass.setManaged(false);
        btnGatePass.setVisible(false);
        if (fnValue == EditMode.READY) {
            if (!lblVSPStatus.getText().equals("Cancelled")) {
                if (oTransVSP.getMasterModel().getMasterModel().getUDRNo() != null && !oTransVSP.getMasterModel().getMasterModel().getUDRNo().isEmpty()) {
                    btnGatePass.setManaged(true);
                    btnGatePass.setVisible(true);
                }
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnCancelVSP.setVisible(true);
                btnCancelVSP.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                tabAddOns.setDisable(false);
                tabDetails.setDisable(false);
                if (oTransVSP.getMasterModel().getMasterModel().getGatePsNo() != null) {
                    if (!oTransVSP.getMasterModel().getMasterModel().getGatePsNo().isEmpty()) {
                        btnGatePass.setManaged(false);
                        btnGatePass.setVisible(false);
                    }
                }
            }
            btnRemoveReservation.setDisable(false);
        }

    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    private void loadLostSaleWindow(boolean fbIsInquiry) throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VehicleInquiryLostSale.fxml"));
            VehicleInquiryLostSaleController loControl = new VehicleInquiryLostSaleController();
            loControl.setGRider(oApp);
            loControl.setFollowUpObject(oTransFollow);
            loControl.setVSPObject(oTransVSP);
            loControl.setTransNo(oTransVSP.getMasterModel().getMasterModel().getInqTran());
            loControl.setsVSPNox(oTransVSP.getMasterModel().getMasterModel().getTransNo());
            loControl.setBranCD(oTransVSP.getMasterModel().getMasterModel().getBranchCD());
            loControl.setClientName(oTransVSP.getMasterModel().getMasterModel().getInqCltNm());
            loControl.setStateForm(fbIsInquiry);
            loControl.setInquiryObject(oTransInquiry);
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            JSONObject loJSON = new JSONObject();
            loJSON = oTransVSP.openTransaction(oTransVSP.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadVSPFields();
                loadLaborTable();
                loadAccessoriesTable();
                pnEditMode = oTransVSP.getEditMode();
                initFields(pnEditMode);
            }
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadSJOWindow() {
        try {
            String lsFormName = "Sales Job Order";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/SalesJobOrder.fxml"));
            SalesJobOrderController loControl = new SalesJobOrderController();
            loControl.setGRider(oApp);
            loControl.setIsVSPState(true);
            if (oTransVSP.getMasterModel().getMasterModel().getTransNo() != null) {
                loControl.setVSPTrans(oTransVSP.getMasterModel().getMasterModel().getTransNo());
            }
            fxmlLoader.setController(loControl);
            Parent parent = fxmlLoader.load();
            AnchorPane otherAnchorPane = loControl.AnchorMain;

            // Get the parent of the TabContent node
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            // If the parent is a TabPane, you can work with it directly
            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;

                for (Tab tab : tabpane.getTabs()) {
                    if (tab.getText().equals(lsFormName)) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "You have opened Vehicle Sales Proposal.\n"
                                + "Are you sure you want to convert this vsp for a new sales job order record?")) {
                            tabpane.getSelectionModel().select(tab);
                            poUnload.unloadForm(AnchorMain, oApp, lsFormName);
                            loadSJOWindow();
                        } else {
                            return;
                        }
                        return;
                    }
                }
                Tab loNewTab = new Tab(lsFormName, parent);
                loNewTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
                loNewTab.setContextMenu(fdController.createContextMenu(tabpane, loNewTab, oApp));
                tabpane.getTabs().add(loNewTab);
                tabpane.getSelectionModel().select(loNewTab);
                loNewTab.setOnCloseRequest(event -> {
                    if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                        if (poUnload != null) {
                            poUnload.unloadForm(otherAnchorPane, oApp, lsFormName);
                        } else {
                            ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                        }
                    } else {
                        // Cancel the close request
                        event.consume();
                    }

                });

            }

        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void loadGatePassWindow(boolean fbIsClicked) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/VehicleGatePass.fxml"));
            VehicleGatePassController loControl = new VehicleGatePassController();
            loControl.setGRider(oApp);
            loControl.setVSPTransNo(oTransVSP.getMasterModel().getMasterModel().getTransNo());
            loControl.setVGPTransNo(oTransVSP.getMasterModel().getMasterModel().getGatePsNo());
            loControl.setIsVSPState(true);
            loControl.setOpenEvent(true);
            loControl.setIsClicked(fbIsClicked);
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            JSONObject loJSON = new JSONObject();
            loJSON = oTransVSP.openTransaction(oTransVSP.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadVSPFields();
                loadLaborTable();
                loadAccessoriesTable();
                pnEditMode = oTransVSP.getEditMode();
                initFields(pnEditMode);
            }
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    @FXML
    private void lblGPClicked(MouseEvent event) {
        if (!lblRFNo.getText().trim().isEmpty()) {
            if (event.getClickCount() == 1) {
                loadGatePassWindow(true);
            }
        }
    }
}
