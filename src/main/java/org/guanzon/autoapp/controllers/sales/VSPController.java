package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
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
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
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
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.sales.FollowUp;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.FXMLDocumentController;
import org.guanzon.autoapp.controllers.general.VehicleGatePassController;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.sales.Part;
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
public class VSPController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private VehicleSalesProposal oTrans;
    private FollowUp oTransFollow;
    private Inquiry oTransInquiry;
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    public int pnEditMode = -1;//Modifying fields
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
     *
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
        oTransInquiry = new Inquiry(oApp, false, oApp.getBranchCode());
        oTransFollow = new FollowUp(oApp, false, oApp.getBranchCode());

        initLaborTable();
        initAccessoriesTable();
        datePicker09.setDayCellFactory(DateFormatCell);
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
        initTableKeyPressed();
        addRowVSPLabor();
        tblViewLabor.setOnMouseClicked(this::tblLabor_Clicked);
        tblViewAccessories.setOnMouseClicked(this::tblParts_Clicked);

        Platform.runLater(() -> {
            if (pbIsInquiry) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
                loJSON = oTrans.searchInquiry(psInqTransNo, true);
                if (!"error".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    clearFields();
                }
            }
        }
        );
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField04, txtField06, txtField07, txtField08, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23,
                txtField24, txtField25, txtField36, txtField32, txtField62, txtField65, txtField75, txtField81);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea17, textArea26, textArea27, textArea28);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        //Main Interface
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.computeAmount();
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getVSPNO());
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null && !oTrans.getMasterModel().getMasterModel().getTransactDte().toString().isEmpty()) {
            txtField02.setText(CustomCommonUtil.xsDateWMonthName(oTrans.getMasterModel().getMasterModel().getTransactDte()));
        }
        txtField03.setText(oTrans.getMasterModel().getMasterModel().getInqryID());
        if (oTrans.getMasterModel().getMasterModel().getInqryDte() != null && !oTrans.getMasterModel().getMasterModel().getInqryDte().toString().isEmpty()) {
            txtField04.setText(CustomCommonUtil.xsDateWMonthName(oTrans.getMasterModel().getMasterModel().getInqryDte()));
        }
        if (oTrans.getMasterModel().getMasterModel().getSourceCD() != null && !oTrans.getMasterModel().getMasterModel().getSourceCD().trim().isEmpty()) {
            comboBox05.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getSourceCD()));
        }
        txtField06.setText(oTrans.getMasterModel().getMasterModel().getSEName());
        txtField07.setText(oTrans.getMasterModel().getMasterModel().getAgentNm());
        txtField08.setText(oTrans.getMasterModel().getMasterModel().getBranchNm());
        if (oTrans.getMasterModel().getMasterModel().getDelvryDt() != null && !oTrans.getMasterModel().getMasterModel().getDelvryDt().toString().isEmpty()) {
            datePicker09.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getDelvryDt(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        txtField10.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getTranTotl()));
        txtField11.setText(CustomCommonUtil.setDecimalFormat(oTrans.getTotalDiscount()));
        txtField12.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getResrvFee()));
        txtField13.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getAmtPaid()));
        txtField14.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getNetTTotl()));

        String lsInqNmex = "";
        if (oTrans.getMasterModel().getMasterModel().getInqCltNm() != null) {
            lsInqNmex = oTrans.getMasterModel().getMasterModel().getInqCltNm();
        }
        txtField15.setText(lsInqNmex);

        String lsByClNme = "";
        if (oTrans.getMasterModel().getMasterModel().getBuyCltNm() != null) {
            lsByClNme = oTrans.getMasterModel().getMasterModel().getBuyCltNm();
        }
        txtField16.setText(lsByClNme);

        String lsAddrexx = "";
        if (oTrans.getMasterModel().getMasterModel().getAddress() != null) {
            lsAddrexx = oTrans.getMasterModel().getMasterModel().getAddress();
        }
        textArea17.setText(lsAddrexx);

        String lsCoClNme = "";
        if (oTrans.getMasterModel().getMasterModel().getCoCltNm() != null) {
            lsCoClNme = oTrans.getMasterModel().getMasterModel().getCoCltNm();
        }
        txtField18.setText(lsCoClNme);

        txtField18.setText(oTrans.getMasterModel().getMasterModel().getCoCltNm());
        txtField19.setText(oTrans.getMasterModel().getMasterModel().getContctNm());
        if (oTrans.getMasterModel().getMasterModel().getIsVhclNw().equals("0")) {
            brandNewCat.setSelected(true);
        } else if (oTrans.getMasterModel().getMasterModel().getIsVhclNw().equals("1")) {
            preOwnedCat.setSelected(true);
        } else {
            preOwnedCat.setSelected(false);
            brandNewCat.setSelected(false);
        }
        if (oTrans.getMasterModel().getMasterModel().getIsVIP().equals("1")) {
            chckBoxSpecialAccount.setSelected(true);
        } else {
            chckBoxSpecialAccount.setSelected(false);
        }

        String lsCSNoxxx = "";
        if (oTrans.getMasterModel().getMasterModel().getCSNo() != null) {
            lsCSNoxxx = oTrans.getMasterModel().getMasterModel().getCSNo();
        }
        txtField20.setText(lsCSNoxxx);

        String lsPltNoxx = "";
        if (oTrans.getMasterModel().getMasterModel().getPlateNo() != null) {
            lsPltNoxx = oTrans.getMasterModel().getMasterModel().getPlateNo();
        }
        txtField21.setText(lsPltNoxx);

        String lsEngNoxx = "";
        if (oTrans.getMasterModel().getMasterModel().getEngineNo() != null) {
            lsEngNoxx = oTrans.getMasterModel().getMasterModel().getEngineNo();
        }
        txtField22.setText(lsEngNoxx);

        String lsFrmNoxx = "";
        if (oTrans.getMasterModel().getMasterModel().getFrameNo() != null) {
            lsFrmNoxx = oTrans.getMasterModel().getMasterModel().getFrameNo();
        }
        txtField23.setText(lsFrmNoxx);

        txtField24.setText(oTrans.getMasterModel().getMasterModel().getEndPlate());
        txtField25.setText(oTrans.getMasterModel().getMasterModel().getKeyNo());
        textArea26.setText(oTrans.getMasterModel().getMasterModel().getVhclDesc());
        textArea28.setText(oTrans.getMasterModel().getMasterModel().getRemarks());

        //Details Tab Interface
        if (oTrans.getMasterModel().getMasterModel().getPayMode() != null && !oTrans.getMasterModel().getMasterModel().getPayMode().trim().isEmpty()) {
            comboBox29.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getPayMode()));
        }
        txtField30.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getUnitPrce()));
        txtField31.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getDownPaym()));
        String lsBranchBank = "";
        if (oTrans.getMasterModel().getMasterModel().getBankName() != null && oTrans.getMasterModel().getMasterModel().getBrBankNm() != null) {
            lsBranchBank = oTrans.getMasterModel().getMasterModel().getBankName() + " " + oTrans.getMasterModel().getMasterModel().getBrBankNm();
        }
        txtField32.setText(lsBranchBank.trim());
        // Payment Information and Vehicle Charges
        if (!oTrans.getVSPFinanceList().isEmpty()) {
            switch (comboBox29.getSelectionModel().getSelectedIndex()) {
                case 1://FINANCING
                case 2:
                case 3:
                case 4:
                    txtField33.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getDiscount()));
                    txtField34.setText(String.valueOf(oTrans.getVSPFinanceModel().getVSPFinanceModel().getAcctTerm()));
                    txtField35.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getAcctRate()));
                    txtField36.setText("");
                    if (oTrans.getVSPFinanceModel().getVSPFinanceModel().getFinPromo() != null && !oTrans.getVSPFinanceModel().getVSPFinanceModel().getFinPromo().isEmpty()) {
                        comboBox37.getSelectionModel().select(Integer.parseInt(String.valueOf(oTrans.getVSPFinanceModel().getVSPFinanceModel().getFinPromo())));
                    }
                    double ldAprvdNetSrp = Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getUnitPrce())) - Double.parseDouble(String.valueOf(oTrans.getVSPFinanceModel().getVSPFinanceModel().getDiscount()));
                    txtField38.setText(CustomCommonUtil.setDecimalFormat(ldAprvdNetSrp));
                    txtField39.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getNtDwnPmt()));
                    txtField40.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getFinAmt()));
                    txtField41.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getMonAmort()));
                    txtField42.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getRebates()));
                    txtField43.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getGrsMonth()));
                    txtField44.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPFinanceModel().getVSPFinanceModel().getPNValue()));
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
        txtField45.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getFleetDsc()));
        txtField46.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getDue2Sup()));
        txtField47.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getDue2Dlr()));
        txtField48.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getSPFltDsc()));
        txtField49.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getSPFD2Sup()));
        txtField50.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getSPFD2Dlr()));
        txtField51.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getPromoDsc()));
        txtField52.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getPrmD2Sup()));
        txtField53.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getPrmD2Dlr()));
        txtField54.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getAddlDsc()));
        txtField55.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getToLabDsc()));
        txtField56.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getBndleDsc()));
        txtField57.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getToPrtDsc()));
        txtField58.setText("0.00");
        // Other Charges
        txtField59.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getAdvDwPmt()));
        txtField60.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getTPLAmt()));
        if (oTrans.getMasterModel().getMasterModel().getTPLStat() != null && !oTrans.getMasterModel().getMasterModel().getTPLStat().trim().isEmpty()) {
            comboBox61.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getTPLStat()));
        }
        String lsBrTpl = "";
        if (oTrans.getMasterModel().getMasterModel().getTPLInsNm() != null && oTrans.getMasterModel().getMasterModel().getTPLBrIns() != null) {
            lsBrTpl = oTrans.getMasterModel().getMasterModel().getTPLInsNm() + " " + oTrans.getMasterModel().getMasterModel().getTPLBrIns();
        }
        txtField62.setText(lsBrTpl.trim());
        txtField63.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getCompAmt()));
        if (oTrans.getMasterModel().getMasterModel().getCompStat() != null && !oTrans.getMasterModel().getMasterModel().getCompStat().trim().isEmpty()) {
            comboBox64.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getCompStat()));
        }
        String lsBrCom = "";
        if (oTrans.getMasterModel().getMasterModel().getCOMInsNm() != null && oTrans.getMasterModel().getMasterModel().getCOMBrIns() != null) {
            lsBrCom = oTrans.getMasterModel().getMasterModel().getCOMInsNm() + " " + oTrans.getMasterModel().getMasterModel().getCOMBrIns();
        }
        txtField65.setText(lsBrCom.trim());
        if (oTrans.getMasterModel().getMasterModel().getInsurTyp() != null && !oTrans.getMasterModel().getMasterModel().getInsurTyp().trim().isEmpty()) {
            comboBox66.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getInsurTyp()));
        }
        if (comboBox66.getValue() != null) {
            if (oTrans.getMasterModel().getMasterModel().getInsurYr() != null) {
                comboBox67.setValue(String.valueOf(oTrans.getMasterModel().getMasterModel().getInsurYr()));
            }
        }
        txtField68.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getLTOAmt()));
        if (oTrans.getMasterModel().getMasterModel().getLTOStat() != null && !oTrans.getMasterModel().getMasterModel().getLTOStat().trim().isEmpty()) {
            comboBox69.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getLTOStat()));
        }
        txtField70.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getChmoAmt()));
        if (oTrans.getMasterModel().getMasterModel().getChmoStat() != null && !oTrans.getMasterModel().getMasterModel().getChmoStat().trim().isEmpty()) {
            comboBox71.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getChmoStat()));
        }
        txtField72.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getLaborAmt()));
        txtField73.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getAccesAmt()));
        txtField74.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getOthrChrg()));
        txtField75.setText(oTrans.getMasterModel().getMasterModel().getOthrDesc());
        txtField76.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getTranTotl()));
        txtField77.setText(CustomCommonUtil.setDecimalFormat(oTrans.getTotalDiscount()));
        txtField78.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getResrvFee()));
        txtField79.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getAmtPaid()));
        txtField80.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getNetTTotl()));

        String lsJobNO = "";
        if (oTrans.getMasterModel().getMasterModel().getJONo() != null && !oTrans.getMasterModel().getMasterModel().getJONo().isEmpty()) {
            lsJobNO = oTrans.getMasterModel().getMasterModel().getJONo();
        }
        txtField81.setText(lsJobNO);
        txtField82.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getDealrRte()));
        txtField83.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getDealrAmt()));
        txtField84.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getSlsInRte()));
        txtField85.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getMasterModel().getSlsInAmt()));

        String lsVDRNo = "";

        if (oTrans.getMasterModel()
                .getMasterModel().getUDRNo() != null) {
            lsVDRNo = oTrans.getMasterModel().getMasterModel().getUDRNo();
        }

        lblDRNo.setText(lsVDRNo);
        String lsRFNO = "";
        if (oTrans.getMasterModel().getMasterModel().getGatePsNo() != null) {
            lsRFNO = oTrans.getMasterModel().getMasterModel().getGatePsNo();
        }

        lblRFNo.setText(lsRFNO);
        String lsVSI = "";

        if (oTrans.getMasterModel()
                .getMasterModel().getSINo() != null) {
            lsVSI = oTrans.getMasterModel().getMasterModel().getSINo();
        }

        lblSINo.setText(lsVSI);

        String lsPrinted = "";
        if (oTrans.getMasterModel().getMasterModel().getPrinted().equals("1")) {
            lsPrinted = "Y";
        } else {
            lsPrinted = "N";
        }
        lblPrint.setText(lsPrinted);
        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lblVSPStatus.setText("For Approval");
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
        }
        String lsApprvDte = "";
        if (oTrans.getMasterModel().getMasterModel().getApproveDte() != null) {
            if (!CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getApproveDte()).equals("1900-01-01")) {
                lsApprvDte = SQLUtil.dateFormat(oTrans.getMasterModel().getMasterModel().getApproveDte(), SQLUtil.FORMAT_SHORT_DATE);
            }
        }
        lblApproveDate.setText(lsApprvDte);
        return true;
    }

    public void initPatternFields() {
        List<TextField> loTxtField = Arrays.asList(txtField30, txtField31, txtField33, txtField35, txtField39, txtField42,
                txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField59, txtField60, txtField63, txtField68, txtField70, txtField74, txtField82, txtField84);
        Pattern decOnly = Pattern.compile("[0-9,.]*");
        Pattern numOnly = Pattern.compile("[0-9]*");
        loTxtField.forEach(tf -> tf.setTextFormatter(new TextFormatterUtil(decOnly)));
        txtField34.setTextFormatter(new TextFormatterUtil(numOnly));
    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
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
                    oTrans.getVSPFinanceModel().getVSPFinanceModel().setDiscount(new BigDecimal(lnBankDsc));
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
                    oTrans.getVSPFinanceModel().getVSPFinanceModel().setAcctTerm(lnTerm);
                    break;
                case 35: // Rate
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double lnRate = Double.parseDouble(lsValue.replace(",", ""));
                    if (lnRate > 100.00 || lnRate < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        lnRate = 0.00;
                    }
                    oTrans.getVSPFinanceModel().getVSPFinanceModel().setAcctRate(lnRate);
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
                    oTrans.getVSPFinanceModel().getVSPFinanceModel().setNtDwnPmt(new BigDecimal(lnNetDown));
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
                    oTrans.getVSPFinanceModel().getVSPFinanceModel().setRebates(new BigDecimal(lnPrmptPayment));
                    break;
            }
            loadMasterFields();
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
                    oTrans.getMasterModel().getMasterModel().setEndPlate(lsValue);
                    break;
                case 30: // Vehicle Price
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setUnitPrce(new BigDecimal(lsValue.replace(",", "")));
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
                    oTrans.getMasterModel().getMasterModel().setDownPaym(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField31.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setDownPaym(new BigDecimal(txtField31.getText().replace(",", "")));
                    }
                    break;
                case 45: //STD Fleet Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "STD Fleet Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTrans.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField45.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(txtField45.getText().replace(",", "")));
                    }
                    if (Double.parseDouble(lsValue.replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setDue2Dlr(0.00);
                        oTrans.getMasterModel().getMasterModel().setDue2Sup(0.00);
                    }
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
                    oTrans.getMasterModel().getMasterModel().setDue2Sup(lnValueSTDSup);
                    oTrans.getMasterModel().getMasterModel().setDue2Dlr(remainingValueDlrs);
                    if (Double.parseDouble(txtField45.getText().replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setDue2Dlr(0.00);
                        oTrans.getMasterModel().getMasterModel().setDue2Sup(0.00);
                    }
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
                    oTrans.getMasterModel().getMasterModel().setDue2Dlr(lnValueSTDDlr);
                    oTrans.getMasterModel().getMasterModel().setDue2Sup(remainingValueSup);
                    if (Double.parseDouble(txtField45.getText().replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setDue2Dlr(0.00);
                        oTrans.getMasterModel().getMasterModel().setDue2Sup(0.00);
                    }
                    break;
                case 48: //SPL Fleet Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "SPL Fleet Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTrans.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField48.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(Double.parseDouble(txtField48.getText().replace(",", ""))));
                    }
                    if (Double.parseDouble(lsValue.replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setSPFD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setSPFD2Dlr(0.00);
                    }
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
                    oTrans.getMasterModel().getMasterModel().setSPFD2Sup(lnValueSPLSup);
                    oTrans.getMasterModel().getMasterModel().setSPFD2Dlr(remainingValueSDlrs);

                    if (Double.parseDouble(txtField48.getText().replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setSPFD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setSPFD2Dlr(0.00);
                    }
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
                    oTrans.getMasterModel().getMasterModel().setSPFD2Dlr(lnValueSPLDlr);
                    oTrans.getMasterModel().getMasterModel().setSPFD2Sup(remainingValueForsup);
                    if (Double.parseDouble(txtField48.getText().replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setSPFD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setSPFD2Dlr(0.00);
                    }
                    break;
                case 51: //Promo Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Promo Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTrans.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField51.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(Double.parseDouble(txtField51.getText().replace(",", ""))));
                    }
                    if (Double.parseDouble(lsValue.replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setPrmD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setPrmD2Dlr(0.00);
                    }
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
                    oTrans.getMasterModel().getMasterModel().setPrmD2Sup(lnValuePromoSup);
                    oTrans.getMasterModel().getMasterModel().setPrmD2Dlr(remainingValueForDealer);

                    if (Double.parseDouble(txtField51.getText().replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setPrmD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setPrmD2Dlr(0.00);
                    }
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
                    oTrans.getMasterModel().getMasterModel().setPrmD2Dlr(lnValuePromoDlr);
                    oTrans.getMasterModel().getMasterModel().setPrmD2Sup(remainingValueForSupplier);

                    if (Double.parseDouble(txtField51.getText().replace(",", "")) <= 0.0) {
                        oTrans.getMasterModel().getMasterModel().setPrmD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setPrmD2Dlr(0.00);
                    }
                    break;
                case 54:// Cash Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Cash Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTrans.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField54.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(Double.parseDouble(txtField54.getText().replace(",", ""))));
                    }
                    break;
                case 56: //Bundle Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (lnUnitPrice >= 0.00) {
                        if (Double.parseDouble(lsValue.replace(",", "")) > lnUnitPrice) {
                            ShowMessageFX.Warning(null, "Warning", "Bundle Discount cannot be greater than Vehicle Price");
                            lsValue = "0.00";
                        }
                    }
                    oTrans.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(lsValue.replace(",", "")));
                    if (!loadMasterFields()) {
                        txtField56.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(Double.parseDouble(txtField56.getText().replace(",", ""))));
                    }
                    break;
                case 59: //OMA & CMF
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setAdvDwPmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 60: //TPL Insurance
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 63: //Compre Insurance
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setCompAmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 68: //LTO
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setLTOAmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 70: // CHMO/Doc Stamps
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setChmoAmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 74: //Misc Charges
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getMasterModel().getMasterModel().setOthrChrg(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 75://Description
                    oTrans.getMasterModel().getMasterModel().setOthrDesc(lsValue);
                    break;
                case 82:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double diIncent = Double.parseDouble(lsValue.replace(",", ""));
                    if (diIncent > 100.00 || diIncent < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        diIncent = 0.00;
                    }
                    oTrans.getMasterModel().getMasterModel().setDealrRte(diIncent);
                    if (!loadMasterFields()) {
                        txtField82.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setDealrRte(Double.valueOf(txtField82.getText().replace(",", "")));
                    }
                    break;
                case 84:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    double siIncent = Double.parseDouble(lsValue.replace(",", ""));
                    if (siIncent > 100.00 || siIncent < 0.00) {
                        ShowMessageFX.Warning(null, "Warning", "Invalid Amount");
                        siIncent = 0.00;
                    }

                    oTrans.getMasterModel().getMasterModel().setSlsInRte(siIncent);
                    if (!loadMasterFields()) {
                        txtField84.setText("0.00");
                        oTrans.getMasterModel().getMasterModel().setSlsInRte(Double.valueOf(txtField84.getText().replace(",", "")));
                    }
                    break;
            }
            loadMasterFields();
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
                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField15, txtField16, txtField18, txtField20, txtField21, txtField22, txtField23,
                txtField24, txtField32, txtField36, txtField62, txtField65, txtField75);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));

        List<TextField> loTxtFieldAmount = Arrays.asList(txtField30,
                txtField31, txtField33, txtField34, txtField35, txtField82, txtField84,
                txtField38, txtField39, txtField42, txtField45, txtField46, txtField47,
                txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField56, txtField59, txtField60, txtField63, txtField68,
                txtField70, txtField74
        );

        loTxtFieldAmount.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed_Amount(event)));

    }

    private void txtField_KeyPressed_Amount(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                case TAB:
                    switch (txtFieldID) {
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
                        case "txtField15":
                            loJSON = oTrans.searchInquiry(lsValue, false);
                            if (!"error".equals(loJSON.get("result"))) {
                                loadMasterFields();
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField15.setText("");
                                return;
                            }
                            initFields(pnEditMode);
                            break;
                        case "txtField16":
                            loJSON = oTrans.searchClient(lsValue, true);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField16.setText(oTrans.getMasterModel().getMasterModel().getBuyCltNm());
                                textArea17.setText(oTrans.getMasterModel().getMasterModel().getAddress());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));

                                txtField16.setText("");
                                return;
                            }
                            break;
                        case "txtField18":
                            loJSON = oTrans.searchClient(lsValue, false);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField18.setText(oTrans.getMasterModel().getMasterModel().getCoCltNm());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField18.setText("");
                            }
                            break;
                        case "txtField20":
                            loJSON = oTrans.searchAvlVhcl(lsValue, "CS");
                            if (!"error".equals(loJSON.get("result"))) {
                                if (!oTrans.getMasterModel().getMasterModel().getCSNo().isEmpty()) {
                                    txtField20.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
                                }
                                if (!oTrans.getMasterModel().getMasterModel().getPlateNo().isEmpty()) {
                                    txtField21.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
                                }
                                txtField22.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
                                txtField23.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());

                                String lsKeyNo = "";
                                if (oTrans.getMasterModel().getMasterModel().getKeyNo() != null) {
                                    lsKeyNo = oTrans.getMasterModel().getMasterModel().getKeyNo();
                                }
                                txtField25.setText(lsKeyNo);
                                textArea26.setText(oTrans.getMasterModel().getMasterModel().getVhclDesc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField20.setText("");
                            }
                            break;
                        case "txtField21":
                            loJSON = oTrans.searchAvlVhcl(lsValue, "PLT");
                            if (!"error".equals(loJSON.get("result"))) {
                                if (!oTrans.getMasterModel().getMasterModel().getCSNo().isEmpty()) {
                                    txtField20.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
                                }
                                if (!oTrans.getMasterModel().getMasterModel().getPlateNo().isEmpty()) {
                                    txtField21.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
                                }
                                txtField22.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
                                txtField23.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
                                String lsKeyNo = "";
                                if (oTrans.getMasterModel().getMasterModel().getKeyNo() != null) {
                                    lsKeyNo = oTrans.getMasterModel().getMasterModel().getKeyNo();
                                }
                                txtField25.setText(lsKeyNo);
                                textArea26.setText(oTrans.getMasterModel().getMasterModel().getVhclDesc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField21.setText("");
                            }
                            break;
                        case "txtField22":
                            loJSON = oTrans.searchAvlVhcl(lsValue, "ENG");
                            if (!"error".equals(loJSON.get("result"))) {
                                if (!oTrans.getMasterModel().getMasterModel().getCSNo().isEmpty()) {
                                    txtField20.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
                                }
                                if (!oTrans.getMasterModel().getMasterModel().getPlateNo().isEmpty()) {
                                    txtField21.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
                                }
                                txtField22.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
                                txtField23.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
                                String lsKeyNo = "";
                                if (oTrans.getMasterModel().getMasterModel().getKeyNo() != null) {
                                    lsKeyNo = oTrans.getMasterModel().getMasterModel().getKeyNo();
                                }
                                txtField25.setText(lsKeyNo);
                                textArea26.setText(oTrans.getMasterModel().getMasterModel().getVhclDesc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField22.setText("");
                            }
                            break;
                        case "txtField23":
                            loJSON = oTrans.searchAvlVhcl(lsValue, "FRM");
                            if (!"error".equals(loJSON.get("result"))) {
                                if (!oTrans.getMasterModel().getMasterModel().getCSNo().isEmpty()) {
                                    txtField20.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
                                }
                                if (!oTrans.getMasterModel().getMasterModel().getPlateNo().isEmpty()) {
                                    txtField21.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
                                }
                                txtField22.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
                                txtField23.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
                                String lsKeyNo = "";
                                if (oTrans.getMasterModel().getMasterModel().getKeyNo() != null) {
                                    lsKeyNo = oTrans.getMasterModel().getMasterModel().getKeyNo();
                                }
                                txtField25.setText(lsKeyNo);
                                textArea26.setText(oTrans.getMasterModel().getMasterModel().getVhclDesc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField23.setText("");
                            }
                            break;
                        case "txtField32":
                            loJSON = oTrans.searchBankApp(lsValue);
                            if (!"error".equals(loJSON.get("result"))) {
                                String lsBrBank = oTrans.getMasterModel().getMasterModel().getBankName() + " " + oTrans.getMasterModel().getMasterModel().getBrBankNm();
                                txtField32.setText(lsBrBank);
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField32.setText("");
                                return;
                            }
                            break;
                        case "txtField36":
//                    loJSON = oTrans.searchPromo(lsValue);
//                    if (!"error".equals(loJSON.get("result"))) {
////                        txtField36.setText(oTrans.getMasterModel().getMasterModel());
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        txtField36.setText("");
//                        txtField36.requestFocus();
//                        return;
//                    }
                            break;
                        case "txtField62":
                            loJSON = oTrans.searchInsurance(lsValue.trim(), true);
                            if (!"error".equals(loJSON.get("result"))) {

                                String lsBrTpl = oTrans.getMasterModel().getMasterModel().getTPLInsNm() + " " + oTrans.getMasterModel().getMasterModel().getTPLBrIns();
                                txtField62.setText(lsBrTpl);
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
                            initFields(pnEditMode);
                            break;
                        case "txtField65":
                            loJSON = oTrans.searchInsurance(lsValue.trim(), false);
                            if (!"error".equals(loJSON.get("result"))) {
                                String lsBrCom = oTrans.getMasterModel().getMasterModel().getCOMInsNm() + " " + oTrans.getMasterModel().getMasterModel().getCOMBrIns();
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
        List<Button> loButtons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnCancelVSP,
                btnApprove, btnGatePass, btnClose, btnJobOrderAdd, btnAdditionalLabor, btnAddParts,
                btnPaymentHistory, btnAddReservation, btnRemoveReservation);
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
                CustomCommonUtil.switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
                oTrans = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                initFields(pnEditMode);
                break;
            case "btnEdit":
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                initFields(pnEditMode);
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Sales Proposal Information Saving....", "Are you sure, do you want to save?")) {
                    if (comboBox05.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select Inquiry Source.");
                        return;
                    }
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Sales Proposal Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadLaborTable();
                            loadAccessoriesTable();
                            pnEditMode = oTrans.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                initFields(pnEditMode);
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        clearFields();
                        clearTables();
                        CustomCommonUtil.switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
                        oTrans = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadLaborTable();
                            loadAccessoriesTable();
                            pnEditMode = oTrans.getEditMode();
                        }
                    }
                }
                initFields(pnEditMode);
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Sales Proposal Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.READY;
                    } else {
                        pnEditMode = oTrans.getEditMode();
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("", false, false);
                if ("success".equals((String) loJSON.get("result"))) {
                    CustomCommonUtil.switchToTab(tabMain, ImTabPane);
                    loadMasterFields();
                    loadLaborTable();
                    loadAccessoriesTable();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Sales Proposal Information", (String) loJSON.get("message"));
                    pnEditMode = oTrans.getEditMode();
                }
                initFields(pnEditMode);
                break;
            case "btnAdditionalLabor":
                try {
                oTrans.addVSPLabor();
                loadLaborWindowDialog(oTrans.getVSPLaborList().size() - 1, true, "", false);
                loadLaborTable();

            } catch (IOException ex) {
                Logger.getLogger(VSPController.class.getName()).log(Level.SEVERE, null, ex);
            }
            initFields(pnEditMode);
            break;
            case "btnAddParts":
                   try {
                oTrans.addVSPParts();
                loadAccessoriesWindowDialog(oTrans.getVSPPartsList().size() - 1, true);
                loadAccessoriesTable();
            } catch (IOException ex) {
                Logger.getLogger(VSPController.class.getName()).log(Level.SEVERE, null, ex);
            }
            initFields(pnEditMode);
            break;
            case "btnPrint":
            try {
                loadVSPPrint();
            } catch (SQLException ex) {
                Logger.getLogger(VSPController.class.getName()).log(Level.SEVERE, null, ex);
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
                initFields(pnEditMode);
                break;
            case "btnJobOrderAdd":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to create new sales job order record?")) {
                    loadSJOWindow();
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
                }
                initFields(pnEditMode);
                break;
            case "btnApprove":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to approve this vsp?")) {
                    loJSON = oTrans.approveVSP();
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
                    }
                }
                initFields(pnEditMode);
                break;
            default:
                ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                break;
        }
    }

    @Override
    public void initComboBoxItems() {
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

    @Override
    public void initFieldsAction() {
        datePicker09.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setDelvryDt(SQLUtil.toDate(datePicker09.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        chckBoxSpecialAccount.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (chckBoxSpecialAccount.isSelected()) {
                    oTrans.getMasterModel().getMasterModel().setIsVIP("1");
                    chckBoxSpecialAccount.setSelected(true);
                } else {
                    chckBoxSpecialAccount.setSelected(false);
                    oTrans.getMasterModel().getMasterModel().setIsVIP("0");
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox29.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                try {
                    String originalPay = String.valueOf(oTrans.getMasterModel().getMasterModel().getPayMode());
                    int originalIndex = comboBox29.getSelectionModel().getSelectedIndex();
                    if (originalIndex >= 0 && originalIndex < comboBox29.getItems().size()) {
                        switch (originalIndex) {
                            case 0:
                                if (oTrans.getVSPFinanceList().size() - 1 == 0) {
                                    if (ShowMessageFX.YesNo(null, pxeModuleName,
                                            "Are you sure you want to replace payment mode to cash?\n"
                                            + "if YES: from Downpayment to Promissory Note Amt will be cleared\n"
                                            + "if NO: it will remain the values.")) {
                                        oTrans.removeVSPFinance(0);
                                        oTrans.getMasterModel().getMasterModel().setDownPaym(new BigDecimal(0.00));
                                        oTrans.getMasterModel().getMasterModel().setChmoStat("0");
                                        comboBox71.getSelectionModel().select(0);
                                        oTrans.getMasterModel().getMasterModel().setChmoAmt(new BigDecimal(0.00));
                                        txtField70.setText("0.00");
                                        oTrans.getMasterModel().getMasterModel().setDealrRte(0.00);
                                        oTrans.getMasterModel().getMasterModel().setSlsInRte(0.00);
                                        clearFinanceFields();
                                    } else {
                                        Platform.runLater(() -> {
                                            if (!String.valueOf(originalPay).equals("")) {
                                                comboBox29.getSelectionModel().select(Integer.parseInt(originalPay));
                                            }
                                            // Restore previous item
                                        });
                                        return;
                                    }
                                }
                                break;
                            default:
                                if (oTrans.getVSPFinanceList().size() - 1 < 0) {
                                    oTrans.addVSPFinance();
                                }
                                break;
                        }
                        oTrans.getMasterModel().getMasterModel().setPayMode(String.valueOf(comboBox29.getSelectionModel().getSelectedIndex()));
                        initFields(pnEditMode);
                        loadMasterFields();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        );

        comboBox37.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox37.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getVSPFinanceModel().getVSPFinanceModel().setFinPromo(String.valueOf(comboBox37.getSelectionModel().getSelectedIndex()));
                    switch (comboBox37.getSelectionModel().getSelectedIndex()) {
                        case 0: // NONE
                        case 1: // ALL-IN HOUSE (Regular)
                            oTrans.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(0.00));
                            txtField56.setText("0.00");
                            break;
                        case 2: // ALL-IN PROMO
                            oTrans.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(0.00));
                            txtField54.setText("0.00");
                            break;
                    }
                    initFields(pnEditMode);
                }
            }
        }
        );
        comboBox61.setOnAction(event
                -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setTPLStat(String.valueOf(comboBox61.getSelectionModel().getSelectedIndex()));
                if (comboBox61.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setInsTplCd("");
                    oTrans.getMasterModel().getMasterModel().setTPLBrIns("");
                    oTrans.getMasterModel().getMasterModel().setTPLInsNm("");
                    txtField62.setText("");
                    oTrans.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(0.00));
                    txtField60.setText("0.00");
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox64.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox64.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setInsCode("");
                    oTrans.getMasterModel().getMasterModel().setCOMBrIns("");
                    oTrans.getMasterModel().getMasterModel().setCOMInsNm("");
                    oTrans.getMasterModel().getMasterModel().setCompAmt(new BigDecimal(0.00));
                    oTrans.getMasterModel().getMasterModel().setInsurTyp("");
                    oTrans.getMasterModel().getMasterModel().setInsurYr(0);
                    txtField63.setText("0.00");
                    txtField65.setText("");
                    comboBox66.setValue(null);
                    comboBox67.setValue(null);
                    if (!comboBox67.getItems().contains("0")) {
                        comboBox67.getItems().add(0, "0");
                    }
                    switch (comboBox64.getSelectionModel().getSelectedIndex()) {
                        case 0:
                        case 2:
                        case 4:
                            oTrans.getMasterModel().getMasterModel().setInsurTyp("0");
                            comboBox66.getSelectionModel().select(0);
                            comboBox67.setValue("0");
                            break;
                        case 1:
                            oTrans.getMasterModel().getMasterModel().setInsurTyp("1");
                            comboBox66.getSelectionModel().select(1);
                            comboBox67.getItems().remove("0");
                            break;
                        case 3:
                            comboBox67.setValue(null);
                            break;
                    }
                    oTrans.getMasterModel().getMasterModel().setCompStat(String.valueOf(comboBox64.getSelectionModel().getSelectedIndex()));
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox66.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                int selectedIndex = comboBox66.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    if (!comboBox67.getItems().contains("0")) {
                        comboBox67.getItems().add(0, "0");
                    }
                    if (selectedIndex == 0) {
                        comboBox67.setValue("0");
                    } else if (selectedIndex >= 1 && selectedIndex <= 4) {
                        comboBox67.getItems().remove("0");
                        comboBox67.setValue(null);
                        oTrans.getMasterModel().getMasterModel().setInsurYr(null);
                        comboBox67.getSelectionModel().clearSelection();
                    }
                    oTrans.getMasterModel().getMasterModel().setInsurTyp(String.valueOf(comboBox66.getSelectionModel().getSelectedIndex()));
                }
                initFields(pnEditMode);
            }
        }
        );

        comboBox67.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox67.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setInsurYr(Integer.valueOf(comboBox67.getValue()));
                }
                if (comboBox64.getSelectionModel().getSelectedIndex() == 1 || comboBox66.getSelectionModel().getSelectedIndex() != 0) {
                    if (oTrans.getMasterModel().getMasterModel().getInsurYr() == 0 || comboBox67.getValue().equals("0")) {
                        comboBox67.setValue(null);
                    }
                } else {
                    comboBox67.setValue(String.valueOf(oTrans.getMasterModel().getMasterModel().getInsurYr()));
                }
            }
        }
        );
        comboBox69.setOnAction(e
                -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setLTOStat(String.valueOf(comboBox69.getSelectionModel().getSelectedIndex()));
                if (comboBox69.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setLTOAmt(new BigDecimal(0.00));
                    txtField68.setText("0.00");
                }
                initFields(pnEditMode);
            }
        }
        );
        comboBox71.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setChmoStat(String.valueOf(comboBox71.getSelectionModel().getSelectedIndex()));
                if (comboBox71.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setChmoAmt(new BigDecimal(0.00));
                    txtField70.setText("0.00");
                }
                initFields(pnEditMode);
            }
        }
        );
    }

    private void clearFinanceFields() {
        CustomCommonUtil.setText("0.00", txtField31, txtField33, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42,
                txtField43, txtField44, txtField82, txtField83, txtField84, txtField85
        );

        txtField32.setText("");
        txtField34.setText("0");
        txtField36.setText("");
        comboBox37.setValue(null);
    }

    private boolean isInquiryEmpty() {
        oTrans.getMasterModel().getMasterModel().setClientID("");
        oTrans.getMasterModel().getMasterModel().setInqCltID("");
        oTrans.getMasterModel().getMasterModel().setInqCltNm("");
        oTrans.getMasterModel().getMasterModel().setCoCltID("");
        oTrans.getMasterModel().getMasterModel().setIsVhclNw("");
        oTrans.getMasterModel().getMasterModel().setCSNo("");
        oTrans.getMasterModel().getMasterModel().setVhclDesc("");
        oTrans.getMasterModel().getMasterModel().setVhclFDsc("");
        oTrans.getMasterModel().getMasterModel().setEndPlate("");
        oTrans.getMasterModel().getMasterModel().setKeyNo("");
        oTrans.getMasterModel().getMasterModel().setPayMode("");
        oTrans.getMasterModel().getMasterModel().setBnkAppCD("");
        oTrans.getMasterModel().getMasterModel().setBankName("");
        oTrans.getMasterModel().getMasterModel().setBrBankNm("");
        if (!oTrans.getVSPFinanceList().isEmpty()) {
            oTrans.getVSPFinanceModel().getVSPFinanceModel().setBankID("");
            oTrans.getVSPFinanceModel().getVSPFinanceModel().setBankname("");
        }
        if (oTrans.getVSPFinanceList().size() - 1 == 0) {
            oTrans.removeVSPFinance(0);
        }
        oTrans.getMasterModel().getMasterModel().setUnitPrce(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setDownPaym(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setDealrRte(0.00);
        oTrans.getMasterModel().getMasterModel().setDealrAmt(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setSlsInRte(0.00);
        oTrans.getMasterModel().getMasterModel().setSlsInAmt(new BigDecimal(0.00));

        oTrans.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setAddlDsc(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setBndleDsc(new BigDecimal(0.00));

        oTrans.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setTPLStat("");
        oTrans.getMasterModel().getMasterModel().setTPLBrIns("");
        oTrans.getMasterModel().getMasterModel().setTPLInsNm("");

        oTrans.getMasterModel().getMasterModel().setCompAmt(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setCompStat("");

        oTrans.getMasterModel().getMasterModel().setCOMInsNm("");
        oTrans.getMasterModel().getMasterModel().setCOMBrIns("");

        oTrans.getMasterModel().getMasterModel().setInsurTyp("");

        oTrans.getMasterModel().getMasterModel().setLTOAmt(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setLTOStat("");

        oTrans.getMasterModel().getMasterModel().setOthrChrg(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setOthrDesc("");

        oTrans.getMasterModel().getMasterModel().setChmoAmt(new BigDecimal(0.00));
        oTrans.getMasterModel().getMasterModel().setChmoStat("");
        return true;
    }

    @Override
    public void initTextFieldsProperty() {
        txtField15.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        if (isInquiryEmpty()) {
                            clearInquiryFields();
                            clearFinanceFields();
                        }
                    }
                }
                initFields(pnEditMode);
            }
        });
        lblRFNo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                lblRFNo.setOnMouseEntered(event -> lblRFNo.setCursor(Cursor.HAND));
                lblRFNo.setOnMouseExited(event -> lblRFNo.setCursor(Cursor.DEFAULT));
            } else {
                lblRFNo.setOnMouseEntered(null);
                lblRFNo.setOnMouseExited(null);
                lblRFNo.setCursor(Cursor.DEFAULT);
            }
        });
        txtField16.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getMasterModel().getMasterModel().setBuyCltNm("");
                    }
                }
            }
        });
        txtField18.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getMasterModel().getMasterModel().setCoCltID("");
                        oTrans.getMasterModel().getMasterModel().setCoCltNm("");
                    }
                }
            }
        });
        txtField20.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        setVchlClass(txtField21, txtField22, txtField23,
                                txtField25);
                    }
                }
            }
        }
        );
        txtField21.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        setVchlClass(txtField20, txtField22, txtField23,
                                txtField25);
                    }
                }
            }
        }
        );
        txtField22.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        setVchlClass(txtField20, txtField21, txtField23,
                                txtField25);
                    }
                }
            }
        }
        );
        txtField23.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        setVchlClass(txtField20, txtField21, txtField22,
                                txtField25);
                    }
                }
            }
        }
        );
        txtField32.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getMasterModel().getMasterModel().setBnkAppCD("");
                        oTrans.getMasterModel().getMasterModel().setBankName("");
                        if (!oTrans.getVSPFinanceList().isEmpty()) {
                            oTrans.getVSPFinanceModel().getVSPFinanceModel().setBankID("");
                            oTrans.getVSPFinanceModel().getVSPFinanceModel().setBankname("");
                            oTrans.getVSPFinanceModel().getVSPFinanceModel().setDiscount(new BigDecimal(0.00));
                        }
                        txtField33.setText("0.00");
                    }
                }
            }
            initFields(pnEditMode);
        }
        );
        txtField45.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty() || newValue.equals("0.00") || newValue.equals("0.0") || newValue.equals("0")) {
                        oTrans.getMasterModel().getMasterModel().setFleetDsc(new BigDecimal(0.00));
                        oTrans.getMasterModel().getMasterModel().setDue2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setDue2Dlr(0.00);
                        CustomCommonUtil.setText("0.00", txtField45, txtField46, txtField47);
                        initFields(pnEditMode);
                    }
                }
            }
        }
        );
        txtField48.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty() || newValue.equals("0.00") || newValue.equals("0.0") || newValue.equals("0")) {
                        oTrans.getMasterModel().getMasterModel().setSPFltDsc(new BigDecimal(0.00));
                        oTrans.getMasterModel().getMasterModel().setSPFD2Sup(0.00);
                        oTrans.getMasterModel().getMasterModel().setSPFD2Dlr(0.00);
                        CustomCommonUtil.setText("0.00", txtField48, txtField49, txtField50);
                        initFields(pnEditMode);
                    }
                }
            }
        }
        );
        txtField51.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty() || newValue.equals("0.00") || newValue.equals("0.0") || newValue.equals("0")) {
                                oTrans.getMasterModel().getMasterModel().setPromoDsc(new BigDecimal(0.00));
                                oTrans.getMasterModel().getMasterModel().setPrmD2Sup(0.00);
                                oTrans.getMasterModel().getMasterModel().setPrmD2Dlr(0.00);
                                CustomCommonUtil.setText("0.00", txtField51, txtField52, txtField53);
                                initFields(pnEditMode);
                            }
                        }
                    }
                }
                );
        txtField62.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setInsTplCd("");
                                oTrans.getMasterModel().getMasterModel().setTPLBrIns("");
                                oTrans.getMasterModel().getMasterModel().setTPLInsNm("");
                            }
                        }
                    }
                }
                );
        txtField65.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                        if (newValue != null) {
                            if (newValue.isEmpty()) {
                                oTrans.getMasterModel().getMasterModel().setInsCode("");
                                oTrans.getMasterModel().getMasterModel().setCOMInsNm("");
                                oTrans.getMasterModel().getMasterModel().setCOMBrIns("");
                            }
                        }
                    }
                }
                );

    }

    private void setVchlClass(TextField... txtFields) {
        oTrans.getMasterModel().getMasterModel().setSerialID("");
        oTrans.getMasterModel().getMasterModel().setCSNo("");
        oTrans.getMasterModel().getMasterModel().setPlateNo("");
        oTrans.getMasterModel().getMasterModel().setEngineNo("");
        oTrans.getMasterModel().getMasterModel().setFrameNo("");
        oTrans.getMasterModel().getMasterModel().setVhclDesc("");
        oTrans.getMasterModel().getMasterModel().setVhclFDsc("");
        oTrans.getMasterModel().getMasterModel().setKeyNo("");
        oTrans.getMasterModel().getMasterModel().setEndPlate("");
        CustomCommonUtil.setText("", txtFields);
        textArea26.setText("");
    }

    @Override
    public void clearTables() {
        laborData.clear();
        accessoriesData.clear();
    }

    @SuppressWarnings("unchecked")
    private void clearInquiryFields() {
        CustomCommonUtil.setText("", txtField03, txtField04, txtField06, txtField07, txtField08,
                txtField10, txtField11, txtField12, txtField13, txtField14, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField32, txtField36,
                txtField62, txtField65, txtField75, txtField81);

        CustomCommonUtil.setText("", textArea17, textArea26);
        CustomCommonUtil.setText("0.00", txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                txtField77, txtField78, txtField79, txtField80, txtField82, txtField83, txtField84, txtField85);
        txtField34.setText("0");
        List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
        loComboBox.forEach(cmB -> cmB.setValue(null));
        datePicker09.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        brandNewCat.setSelected(false);
        preOwnedCat.setSelected(false);
        CustomCommonUtil.setSelected(false, chckBoxSpecialAccount, chckBoxRustProof,
                chckBoxPermaShine, chckBoxUndercoat, chckBoxTint);
        tgUnitCategory.selectToggle(null);

        CustomCommonUtil.setText("", lblDRNo, lblSINo, lblVSPStatus, lblPrint,
                lblRFNo, lblApproveDate);
        CustomCommonUtil.switchToTab(tabMain, ImTabPane);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
                txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField32, txtField36,
                txtField62, txtField65, txtField75, txtField81);

        CustomCommonUtil.setText("", textArea17, textArea26);
        CustomCommonUtil.setText("0.00", txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                txtField77, txtField78, txtField79, txtField80, txtField82, txtField83, txtField84, txtField85);
        txtField34.setText("0");
        List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
        loComboBox.forEach(cmB -> cmB.setValue(null));
        datePicker09.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        brandNewCat.setSelected(false);
        preOwnedCat.setSelected(false);
        CustomCommonUtil.setSelected(false, chckBoxSpecialAccount, chckBoxRustProof,
                chckBoxPermaShine, chckBoxUndercoat, chckBoxTint);
        tgUnitCategory.selectToggle(null);

        CustomCommonUtil.setText("", lblDRNo, lblSINo, lblVSPStatus, lblPrint,
                lblRFNo, lblApproveDate);
        CustomCommonUtil.switchToTab(tabMain, ImTabPane);
    }

    private void loadVSPPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPPrint.fxml"));
            VSPPrintController loControl = new VSPPrintController();
            loControl.setGRider(oApp);
            loControl.setVSObject(oTrans);
            loControl.setOldPrint(oTrans.getMasterModel().getMasterModel().getPrinted());
            loControl.setTransNo(oTrans.getMasterModel().getMasterModel().getTransNo());
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
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadMasterFields();
                loadLaborTable();
                loadAccessoriesTable();
                pnEditMode = oTrans.getEditMode();
                initFields(pnEditMode);
            }
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
            oTrans.addVSPLabor();
            loJSON = oTrans.searchLabor(fsDescript, oTrans.getVSPLaborList().size() - 1, false);
            if (!"error".equals((String) loJSON.get("result"))) {
                loadLaborWindowDialog(oTrans.getVSPLaborList().size() - 1, true, fsDescript, fbWithLabor);
            } else {
                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                oTrans.removeVSPLabor(oTrans.getVSPLaborList().size() - 1);
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
                    loadLaborWindowDialog(pnRow, false, oTrans.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc(), true);

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
            loControl.setObject(oTrans);
            loControl.setWithLabor(fbWtLabor);
            loControl.setLbrDesc(fsLbrDesc);
            if (!oTrans.getVSPLaborModel().getVSPLabor(fnRow).getLaborDsc().isEmpty()) {
                loControl.setOrigDsc(String.valueOf(oTrans.getVSPLaborModel().getVSPLabor(fnRow).getLaborDsc()));
            }
            loControl.setState(fbIsAdd);
            if (oTrans.getVSPLaborModel().getVSPLabor(fnRow).getDSNo() != null) {
                loControl.setJO(oTrans.getVSPLaborModel().getVSPLabor(fnRow).getDSNo());
            }
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
            loadLaborTable();
            loadAccessoriesTable();
            loadMasterFields();
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
        for (int lnCtr = 0; lnCtr <= oTrans.getVSPLaborList().size() - 1; lnCtr++) {
            if (oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt() != null) {
                lsGrsAmount = CustomCommonUtil.setDecimalFormat(oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt());
            }
            if (oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount() != null) {
                lsDiscAmount = CustomCommonUtil.setDecimalFormat(oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount());
            }
            if (oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt() != null) {
                lsNetAmount = CustomCommonUtil.setDecimalFormat(oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt());
            }
            if (oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getAddtl().equals("1")) {
                lbAdditional = true;
            }
            if (oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo() != null) {
                lsJoNoxx = oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo();
            }
            laborData.add(new Labor(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getTransNo()),
                    String.valueOf(oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborCde()),
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    String.valueOf(oTrans.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDsc()),
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
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPAccessories.fxml"));
            VSPAccessoriesController loControl = new VSPAccessoriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setState(fbIsAdd);
            fxmlLoader.setController(loControl);
            loControl.setRequest(false);
            loControl.setRow(fnRow);
            if (oTrans.getVSPPartsModel().getVSPParts(fnRow).getStockID() != null) {
                loControl.setOrigDsc(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(fnRow).getStockID()));
            }
            if (oTrans.getVSPPartsModel().getVSPParts(fnRow).getStockID() != null) {
                loControl.setStockID(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(fnRow).getStockID()));
            }
            if (oTrans.getVSPPartsModel().getVSPParts(fnRow).getDSNo() != null) {
                loControl.setJO(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(fnRow).getDSNo()));
            }
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
            loadAccessoriesTable();
            loadLaborTable();
            loadMasterFields();
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
        for (int lnCtr = 0; lnCtr <= oTrans.getVSPPartsList().size() - 1; lnCtr++) {
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice() != null) {
                lsGrsAmount = CustomCommonUtil.setDecimalFormat(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice());
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                lsQuantity = String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null && oTrans.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                BigDecimal lsGrsAmt = new BigDecimal(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice()));
                int lsQuan = oTrans.getVSPPartsModel().getVSPParts(lnCtr).getQuantity();
                lsTotalAmount = CustomCommonUtil.setDecimalFormat(String.valueOf(lsGrsAmt.doubleValue() * lsQuan));
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount() != null) {
                lsDiscAmount = CustomCommonUtil.setDecimalFormat(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount());
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt() != null) {
                lsNetAmount = CustomCommonUtil.setDecimalFormat(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt());
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc() != null) {
                lsPartsDesc = String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc());
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getBarCode() != null) {
                lsBarCode = String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getBarCode());
            }
            if (oTrans.getVSPPartsModel().getVSPParts(lnCtr).getDSNo() != null) {
                lsJoNoxx = oTrans.getVSPPartsModel().getVSPParts(lnCtr).getDSNo();
            }
            accessoriesData.add(new Part(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getTransNo()),
                    String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getStockID()),
                    String.valueOf(oTrans.getVSPPartsModel().getVSPParts(lnCtr).getDescript()),
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
            setDisable(fsEmpty || fsItem.isBefore(datePicker09.getValue().minusDays(0)));
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
                                oTrans.removeVSPLabor(lnRow - 1);
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
                        loadMasterFields();
                        loadAccessoriesTable();
                        loadLaborTable();
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
                                oTrans.removeVSPParts(lnRow - 1);
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
                        loadMasterFields();
                        loadAccessoriesTable();
                        loadLaborTable();
                    }
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
            loControl.setObject(oTrans);
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
            loadMasterFields();
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
            loControl.setObject(oTrans);
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
            loadMasterFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }

    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        CustomCommonUtil.setDisable(true,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, comboBox37,
                txtField39, txtField42, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField52, txtField53,
                txtField54, txtField56, txtField59, txtField60, txtField62,
                txtField63, txtField65, txtField68, txtField70, txtField72, txtField73, txtField74, txtField75, btnJobOrderAdd,
                txtField51, txtField53, txtField56, comboBox71, txtField82, txtField84);
        CustomCommonUtil.setDisable(!lbShow,
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
        CustomCommonUtil.setDisable(!(lbShow && !txtField15.getText().isEmpty()),
                txtField16, txtField18,
                txtField20, txtField21, txtField22, txtField23, txtField24,
                chckBoxSpecialAccount, comboBox29, txtField30, btnAddReservation, btnRemoveReservation);
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            //Payment Mode
            if (comboBox29.getSelectionModel().getSelectedIndex() >= 0) {
                double ldVhclSRP = Double.parseDouble(txtField30.getText().replace(",", ""));
                switch (comboBox29.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        comboBox71.getSelectionModel().select(0);
                        CustomCommonUtil.setDisable(true, txtField31, txtField59, comboBox71);
                        CustomCommonUtil.setDisable(!lbShow, txtField51, txtField54, txtField56);
                        break;
                    case 1: //BANK PURCHASE ORDER
                        CustomCommonUtil.setDisable(!lbShow, txtField31, txtField32);
                        if (ldVhclSRP > 0.00 || ldVhclSRP > 0.0) {
                            CustomCommonUtil.setDisable(!(lbShow && !txtField32.getText().isEmpty()),
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
                            CustomCommonUtil.setDisable(true, txtField34, txtField35);
                        }
                        break;
                    case 2: //BANK FINANCING
                    case 3: // Company
                    case 4:
                        txtField31.setDisable(!lbShow);
                        txtField32.setDisable(!lbShow);
                        if (ldVhclSRP > 0.00 || ldVhclSRP > 0.0) {
                            CustomCommonUtil.setDisable(!(lbShow && !txtField32.getText().isEmpty()),
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
                    CustomCommonUtil.setDisable(!lbShow, txtField51, txtField54);
                    txtField56.setDisable(true);  // Disable Bundle Disc
                    break;
                case 2: // ALL-IN PROMO
                    CustomCommonUtil.setDisable(!lbShow, txtField51, txtField56);
                    txtField54.setDisable(true);  // Disable Cash Disc
                    break;
            }
            if (chckBoxSpecialAccount.isSelected()) {
                CustomCommonUtil.setDisable(!lbShow, txtField45, txtField48);
            }
            double ldSTDFleet = Double.parseDouble(txtField45.getText().replace(",", ""));
            double ldSPLFleet = Double.parseDouble(txtField48.getText().replace(",", ""));
            double ldPromoDsc = Double.parseDouble(txtField51.getText().replace(",", ""));
            if (ldSTDFleet > 0.00 || ldSTDFleet > 0.0) {
                CustomCommonUtil.setDisable(!lbShow, txtField46, txtField47);

            }
            if (ldSPLFleet > 0.00 || ldSPLFleet > 0.0) {
                CustomCommonUtil.setDisable(!lbShow, txtField49, txtField50);
            }
            if (ldPromoDsc > 0.00 || ldPromoDsc > 0.0) {
                CustomCommonUtil.setDisable(!lbShow, txtField52, txtField53);
            }
            //TPL insurance
            switch (comboBox61.getSelectionModel().getSelectedIndex()) {
                case 1: //FOC
                    txtField60.setDisable(true);
                    txtField62.setDisable(!lbShow);
                    break;
                case 3:
                    CustomCommonUtil.setDisable(!lbShow, txtField60, txtField62);
                    break;
                default:
                    CustomCommonUtil.setDisable(true, txtField60, txtField62);
                    break;
            }
            //Compre Insurance
            switch (comboBox64.getSelectionModel().getSelectedIndex()) {
                case 1: //FOC
                    CustomCommonUtil.setDisable(!lbShow, txtField65, comboBox67);
                    CustomCommonUtil.setDisable(true, txtField63, comboBox66);
                    break;
                case 3:
                    CustomCommonUtil.setDisable(!lbShow, txtField63, txtField65, comboBox66, comboBox67);
                    break;
                default: //NONE
                    CustomCommonUtil.setDisable(true, txtField63, txtField65, comboBox66, comboBox67);
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
//            if (!((String) oTrans.getMaster("sSerialID")).isEmpty()) {
//                if (!lblVSPStatus.getText().equals("Cancelled")) {
//                    if (oTrans.getVSPLaborCount() != 0 || oTrans.getVSPPartsCount() != 0) {
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
//            }+
        }
        if (fnValue == EditMode.UPDATE) {
            if (!oTrans.getMasterModel().getMasterModel().getSerialID().isEmpty()) {
                CustomCommonUtil.setDisable(true, txtField20, txtField21, txtField22, txtField23);
            }

            txtField15.setDisable(true);
            if (oTrans.getMasterModel().getMasterModel().getTPLType() != null) {
                if (!oTrans.getMasterModel().getMasterModel().getTPLType().isEmpty()) {
                    CustomCommonUtil.setDisable(true, comboBox61, txtField62);

                }
            }
            if (oTrans.getMasterModel().getMasterModel().getCOMType() != null) {
                if (!oTrans.getMasterModel().getMasterModel().getCOMType().isEmpty()) {
                    CustomCommonUtil.setDisable(true, comboBox64, txtField65);

                }
            }
            if (oTrans.getMasterModel().getMasterModel().getBOTType() != null) {
                if (!oTrans.getMasterModel().getMasterModel().getBOTType().isEmpty()) {
                    CustomCommonUtil.setDisable(true, comboBox61, txtField62,
                            comboBox64, txtField65);
                }
            }
        }

        if (!tblViewLabor.getItems()
                .isEmpty() || !tblViewAccessories.getItems().isEmpty()) {
            btnJobOrderAdd.setDisable(fnValue == EditMode.ADDNEW || oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CANCELLED));
        }

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnCancelVSP, btnGatePass, btnApprove);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnCancelVSP, btnGatePass, btnApprove);
        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        if (fnValue == EditMode.READY) {
            if (!oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CANCELLED)) {
                if (oTrans.getMasterModel().getMasterModel().getUDRNo() != null && !oTrans.getMasterModel().getMasterModel().getUDRNo().isEmpty()) {
                    btnGatePass.setManaged(true);
                    btnGatePass.setVisible(true);
                }

                if (oTrans.getMasterModel().getMasterModel().getGatePsNo() != null && !oTrans.getMasterModel().getMasterModel().getGatePsNo().isEmpty()) {
                    btnGatePass.setManaged(false);
                    btnGatePass.setVisible(false);
                }
                CustomCommonUtil.setVisible(true, btnEdit, btnCancelVSP);
                CustomCommonUtil.setManaged(true, btnEdit, btnCancelVSP);
                if (oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_OPEN)) {
                    btnApprove.setVisible(true);
                    btnApprove.setManaged(true);
                }
            }
            btnRemoveReservation.setDisable(false);
            tabAddOns.setDisable(false);
            tabDetails.setDisable(false);
            if (oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CLOSED)
                    || oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_POSTED)) {
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
            }
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
            loControl.setVSPObject(oTrans);
            loControl.setTransNo(oTrans.getMasterModel().getMasterModel().getInqTran());
            loControl.setsVSPNox(oTrans.getMasterModel().getMasterModel().getTransNo());
            loControl.setBranCD(oTrans.getMasterModel().getMasterModel().getBranchCD());
            loControl.setClientName(oTrans.getMasterModel().getMasterModel().getInqCltNm());
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadMasterFields();
                loadLaborTable();
                loadAccessoriesTable();
                pnEditMode = oTrans.getEditMode();
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
            if (oTrans.getMasterModel().getMasterModel().getTransNo() != null) {
                loControl.setVSPTrans(oTrans.getMasterModel().getMasterModel().getTransNo());
            }

            fxmlLoader.setController(loControl);
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;
                Tab existingTab = tabpane.getTabs().stream().
                        filter(tabPane -> tabPane.getText().equals(lsFormName))
                        .findFirst().orElse(null);
                if (existingTab != null) {
                    if (ShowMessageFX.YesNo(null, pxeModuleName,
                            "Sales Job Order is already open.\n"
                            + "Do you want to switch to the existing tab and create a new Sales Job Order?")) {
                        tabpane.getSelectionModel().select(existingTab);
                        Parent parent = fxmlLoader.load();
                        existingTab.setContent(parent);
                        closeSalesJobTab(existingTab, loControl, lsFormName);
                        return;
                    } else {
                        return;
                    }
                }
                Parent parent = fxmlLoader.load();
                // Create a new tab and set its content
                Tab loNewTab = new Tab(lsFormName, parent);
                loNewTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
                loNewTab.setContextMenu(fdController.createContextMenu(tabpane, loNewTab, oApp));
                tabpane.getTabs().add(loNewTab);
                tabpane.getSelectionModel().select(loNewTab);
                closeSalesJobTab(loNewTab, loControl, lsFormName);
            }
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
        }
    }

    private void closeSalesJobTab(Tab foTab, SalesJobOrderController foController, String fsFormName) {
        foTab.setOnCloseRequest(event -> {
            if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this tab?")) {
                if (poUnload == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    return;
                }
                // Ensure loControl.AnchorMain is initialized before calling unload
                if (foController.AnchorMain == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "AnchorMain is not initialized properly. Cannot unload form.");
                    return;
                }
                try {
                    poUnload.unloadForm(foController.AnchorMain, oApp, fsFormName);
                } catch (Exception e) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Error unloading form: " + e.getMessage());
                }
            } else {
                event.consume(); // Prevent tab close
            }
        }
        );
    }

    private void loadGatePassWindow(boolean fbIsClicked) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/VehicleGatePass.fxml"));
            VehicleGatePassController loControl = new VehicleGatePassController();
            loControl.setGRider(oApp);

            loControl.setVSPTransNo(oTrans.getMasterModel().getMasterModel().getTransNo());
            loControl.setVGPTransNo(oTrans.getMasterModel().getMasterModel().getGatePsNo());
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
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
            if ("success".equals((String) loJSON.get("result"))) {
                loadMasterFields();
                loadLaborTable();
                loadAccessoriesTable();
                pnEditMode = oTrans.getEditMode();
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
