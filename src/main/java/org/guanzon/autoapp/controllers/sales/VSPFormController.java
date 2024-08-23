/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
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
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.models.sales.ModelVSPLabor;
import org.guanzon.autoapp.models.sales.ModelVSPPart;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposal oTransVSP;
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    public int pnEditMode;//Modifying fields
    private int pnCtr = 0;
    private String psTransNo = "";
    private int pnRow = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO");
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cFinPromoType = FXCollections.observableArrayList("NONE", "ALL-IN IN HOUSE", "ALL-IN PROMO");
    ObservableList<String> cTplType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o DEALER", "C/o BANK");
    ObservableList<String> cCompType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o DEALER", "C/o BANK ");
    ObservableList<String> cCompYearType1 = FXCollections.observableArrayList("NONE", "FREE", "REGULAR RATE", "DISCOUNTED RATE", "FROM PROMO DISC");
    ObservableList<String> cCompYearType2 = FXCollections.observableArrayList("0", "1", "2", "3", "4");
    ObservableList<String> cLTOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE");
    ObservableList<String> cHMOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE", "C/o BANK");
    private ObservableList<ModelVSPLabor> laborData = FXCollections.observableArrayList();
    private ObservableList<ModelVSPPart> partData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
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
    private TextField txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField38,
            txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
            txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
            txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60, txtField62,
            txtField63, txtField65, txtField68, txtField70, txtField72, txtField73, txtField74, txtField75, txtField76,
            txtField77, txtField78, txtField79, txtField80, txtField81;
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
    private TableView<ModelVSPLabor> tblViewLabor;
    @FXML
    private TableColumn<ModelVSPLabor, String> tblindex01_labor, tblindex02_labor, tblindex03_labor, tblindex04_labor,
            tblindex05_labor, tblindex06_labor, tblindex07_labor, tblindex08_labor, tblindex09_labor;
    @FXML
    private CheckBox chckBoxSpecialAccount, chckBoxRustProof, chckBoxPermaShine, chckBoxUndercoat, chckBoxTint;
    @FXML
    private TableView<ModelVSPPart> tblViewParts;
    @FXML
    private TableColumn<ModelVSPPart, String> tblindex01_part, tblindex02_part, tblindex03_part, tblindex04_part,
            tblindex05_part, tblindex06_part, tblindex07_part, tblindex08_part, tblindex09_part, tblindex10_part,
            tblindex11_part;

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransVSP = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());

        initLaborTable();
        initPartsTable();
        datePicker09.setDayCellFactory(DateFormatCell);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initComboBoxItems();
        initCmboxFieldAction();
        initDatePropertyAction();
        initTextPropertyAction();
        initButtonsClick();
        clearVSPFields(false);
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadVSPFields() {
        //Main Interface
        JSONObject loJSON = new JSONObject();
        loJSON = oTransVSP.computeAmount();
        if ("success".equals((String) loJSON.get("result"))) {
            txtField01.setText(oTransVSP.getMasterModel().getMasterModel().getVSPNO());
            if (oTransVSP.getMasterModel().getMasterModel().getTransactDte() != null && !oTransVSP.getMasterModel().getMasterModel().getTransactDte().toString().isEmpty()) {
                txtField02.setText(InputTextUtil.xsDateShort(oTransVSP.getMasterModel().getMasterModel().getTransactDte()));
            }
            txtField03.setText(oTransVSP.getMasterModel().getMasterModel().getInqryID());
            if (oTransVSP.getMasterModel().getMasterModel().getInqryDte() != null && !oTransVSP.getMasterModel().getMasterModel().getInqryDte().toString().isEmpty()) {
                txtField04.setText(InputTextUtil.xsDateShort(oTransVSP.getMasterModel().getMasterModel().getInqryDte()));
            }
            if (oTransVSP.getMasterModel().getMasterModel().getSourceCD() != null && !oTransVSP.getMasterModel().getMasterModel().getSourceCD().trim().isEmpty()) {
                comboBox05.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getSourceCD()));
            }
            txtField06.setText(oTransVSP.getMasterModel().getMasterModel().getSEName());
            txtField07.setText(oTransVSP.getMasterModel().getMasterModel().getAgentNm());
            txtField08.setText(oTransVSP.getMasterModel().getMasterModel().getBranchNm());
//        if (oTransVSP.getMasterModel().getMasterModel().GET() != null && !oTransVSP.getMasterModel().getMasterModel().getTargetDt().toString().isEmpty()) {
//            datePicker09.setValue(InputTextUtil.strToDate(InputTextUtil.xsDateShort(oTransVSP.getMasterModel().getMasterModel().getTargetDt())));
//        }
            txtField10.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTranTotl()))));
//        txtField11.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().get()))));
            txtField12.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getRsvAmtTl()))));
            txtField13.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
            txtField14.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetTTotl()))));
            txtField15.setText(oTransVSP.getMasterModel().getMasterModel().getInqCltNm());
            txtField16.setText(oTransVSP.getMasterModel().getMasterModel().getInqCltNm());
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
//        if (oTransVSP.getMasterModel().getMasterModel().getSpecial().equals("1")) {
//            chckBoxSpecialAccount.setSelected(true);
//        } else {
//            chckBoxSpecialAccount.setSelected(false);
//        }
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
            txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAdvDwPmt()))));
            txtField32.setText(oTransVSP.getMasterModel().getMasterModel().getBankName());
//        txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getBankDsc()))));
//        txtField34.setText(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTerms()));
//        txtField35.setText(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getRate()));
//        txtField36.setText(oTransVSP.getMasterModel().getMasterModel().getSalesPromo());
//        if (oTransVSP.getMasterModel().getMasterModel().getFinancePromo() != null && !oTransVSP.getMasterModel().getMasterModel().getFinancePromo().trim().isEmpty()) {
//            comboBox37.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getFinancePromo()));
//        }
            // Payment Information and Vehicle Charges
//        double ldAprvdNetSrp = Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getApproveSRP() - String.valueOf(oTransVSP.getMasterModel().getMasterModel().getApproveSRP()));
//        txtField38.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(ldAprvdNetSrp))));
//        txtField39.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().netD()))));
//        txtField40.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAmountFinanc()))));
//        txtField41.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetMont()))));
//        txtField42.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetMont()))));
//        txtField43.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetMont()))));
//        txtField44.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetMont()))));
//        // Discount
//        txtField45.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getFleetDsc()))));
//        txtField46.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFD2Dlr()))));
//        txtField47.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFD2Sup()))));
            txtField48.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFltDsc()))));
            txtField49.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFD2Sup()))));
            txtField50.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getSPFD2Dlr()))));
            txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPromoDsc()))));
            txtField52.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPrmD2Sup()))));
            txtField53.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getPrmD2Dlr()))));
//        txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel()()))));
//        txtField55.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetMont()))));
            txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getBndleDsc()))));
//        txtField57.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().()))));
//        txtField58.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getI()))));
            // Other Charges
//        txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel()()))));
            txtField60.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTPLAmt()))));
            if (oTransVSP.getMasterModel().getMasterModel().getTPLStat() != null && !oTransVSP.getMasterModel().getMasterModel().getTPLStat().trim().isEmpty()) {
                comboBox61.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getTPLStat()));
            }
            txtField62.setText(oTransVSP.getMasterModel().getMasterModel().getTPLInsNm());
            txtField63.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getCompAmt()))));
            if (oTransVSP.getMasterModel().getMasterModel().getCompStat() != null && !oTransVSP.getMasterModel().getMasterModel().getCompStat().trim().isEmpty()) {
                comboBox64.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getCompStat()));
            }
            txtField65.setText(oTransVSP.getMasterModel().getMasterModel().getCOMInsNm());
//        if (oTransVSP.getMasterModel().getMasterModel().get() != null && !oTransVSP.getMasterModel().getMasterModel().getFinancePromo().trim().isEmpty()) {
//            comboBox66.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getFinancePromo()));
//        }
//        if (oTransVSP.getMasterModel().getMasterModel().getFinancePromo() != null && !oTransVSP.getMasterModel().getMasterModel().getFinancePromo().trim().isEmpty()) {
//            comboBox67.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getFinancePromo()));
//        }
            txtField68.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getLTOAmt()))));
            if (oTransVSP.getMasterModel().getMasterModel().getLTOStat() != null && !oTransVSP.getMasterModel().getMasterModel().getLTOStat().trim().isEmpty()) {
                comboBox69.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getLTOStat()));
            }
//        txtField70.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getOthrDesc()))));
//        if (oTransVSP.getMasterModel().getMasterModel().getFinancePromo() != null && !oTransVSP.getMasterModel().getMasterModel().getFinancePromo().trim().isEmpty()) {
//            comboBox71.getSelectionModel().select(Integer.parseInt(oTransVSP.getMasterModel().getMasterModel().getFinancePromo()));
//        }
            txtField72.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getLaborAmt()))));
            txtField73.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getAccesAmt()))));
            txtField74.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getOthrChrg()))));
            txtField75.setText(oTransVSP.getMasterModel().getMasterModel().getOthrDesc());

            txtField76.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getTranTotl()))));
//        txtField77.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().get()))));
            txtField78.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getRsvAmtTl()))));
            txtField79.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
            txtField80.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getNetTTotl()))));

//        txtField81.setText(oTransVSP.getMasterModel().getMasterModel()());
//        lblDRNo.setText(oTransVSP.getMasterModel().getMasterModel().getNetMont());
//        lblRFNo.setText(oTransVSP.getMasterModel().getMasterModel().getNetMont());
//        lblSINo.setText(oTransVSP.getMasterModel().getMasterModel().getNetMont());
            lblPrint.setText(oTransVSP.getMasterModel().getMasterModel().getPrinted());
            lblVSPStatus.setText(oTransVSP.getMasterModel().getMasterModel().getTranStat());
            if (oTransVSP.getMasterModel().getMasterModel().getTransactDte() != null && !oTransVSP.getMasterModel().getMasterModel().getTransactDte().toString().isEmpty()) {
                lblApproveDate.setText(InputTextUtil.xsDateShort(oTransVSP.getMasterModel().getMasterModel().getApprovedDte()));
            }
        } else {
            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
            return;
        }
    }

    private void loadVSPPrint(String sTransno) throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPFormPrint.fxml"));
////            VSPFormPrintController loControl = new VSPFormPrintController();
//            loControl.setGRider(oApp);
//            loControl.setTransNox(sTransno);
//            fxmlLoader.setController(loControl);
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
            if (chckBoxRustProof.isSelected()) {
                initLaborDescript("RUSTPROOF", true);
                chckBoxRustProof.setSelected(false);
            }
        });
        chckBoxPermaShine.setOnAction(event -> {
            if (chckBoxPermaShine.isSelected()) {
                initLaborDescript("PERMASHINE", true);
                chckBoxPermaShine.setSelected(false);
            }
        });
        chckBoxUndercoat.setOnAction(event -> {
            if (chckBoxUndercoat.isSelected()) {
                initLaborDescript("UNDERCOAT", true);
                chckBoxUndercoat.setSelected(false);
            }
        });
        chckBoxTint.setOnAction(event -> {
            if (chckBoxTint.isSelected()) {
                initLaborDescript("TINT", true);
                chckBoxTint.setSelected(false);
            }
        });
    }

    private void initLaborDescript(String laborDescript, boolean withLabor) {
//        if (oTransVSP.addVSPLabor(laborDescript, withLabor)) {
//            loadLaborAdditionalDialog(oTransVSP.getVSPLaborCount(), true);
//        } else {
//            ShowMessageFX.Warning(getStage(), oTransVSP.getMessage(), "Warning", "Error while adding labor.");
//        }
    }

    private void tblLabor_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewLabor.getSelectionModel().getSelectedIndex() + 1;
            if (pnRow == 0) {
                return;
            }
            if (event.getClickCount() == 2) {
//                loadLaborWindowDialog(pnRow, false);
            }
        }
    }

    private void loadLaborWindowDialog(Integer fnRow, boolean fbIsAdd) throws IOException {

        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPLaborEntryDialog.fxml"));
//            VSPLaborEntryDialogController loControl = new VSPLaborEntryDialogController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransVSP);
//            loControl.setOrigDsc((String) oTrans.getVSPLaborDetail(fnRow, 7));
//            loControl.setState(fbIsAdd);
//            loControl.setJO((String) oTrans.getVSPLaborDetail(fnRow, 11));
//            fxmlLoader.setController(loControl);
//            loControl.setRow(fnRow);
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

    }

    private void initLaborTable() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewLabor.setEditable(true);
        } else {
            tblViewLabor.setEditable(false);
        }

        tblindex01_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex02_labor"));
        tblindex03_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex03_labor"));
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_labor"));
        tblindex06_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex06_labor"));
        tblindex07_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex08_labor.setCellValueFactory(new PropertyValueFactory<>("addOrNot"));
        tblindex09_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));

        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void loadPartsWindowDialog(Integer fnRow, boolean fbIsAdd) throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPPartsDialog.fxml"));
//            VSPPartsDialogController loControl = new VSPPartsDialogController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransVSP);
//            loControl.setState(fbIsAdd);
//            fxmlLoader.setController(loControl);
//            loControl.setRequest(false);
//            loControl.setRow(fnRow);
//            loControl.setOrigDsc((String) oTrans.getVSPPartsDetail(fnRow, 9));
//            loControl.setStockID((String) oTrans.getVSPPartsDetail(fnRow, 3));
//            loControl.setJO((String) oTrans.getVSPPartsDetail(fnRow, 11));
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
            loadPartsTable();
            loadVSPFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    private void tblParts_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewParts.getSelectionModel().getSelectedIndex();
            if (pnRow == 0) {
                return;
            }
            if (event.getClickCount() == 2) {
//                loadPartsAdditionalDialog(pnRow, false);
                loadPartsTable();
            }
        }
    }

    private void loadPartsTable() {

    }

    private void initPartsTable() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewParts.setEditable(true);
        } else {
            tblViewParts.setEditable(false);
        }

        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("tblindex02_part"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex03_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex07_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex08_part.setCellValueFactory(new PropertyValueFactory<>("tblindex07_part"));
        tblindex09_part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_part"));
        tblindex10_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex11_part.setCellValueFactory(new PropertyValueFactory<>("tblindex10_part"));

        tblViewParts.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewParts.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }
    private Callback<DatePicker, DateCell> DateFormatCell = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate fsItem, boolean fsEmpty) {
            super.updateItem(fsItem, fsEmpty);
            LocalDate loMinDate = InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())).minusDays(0);
            setDisable(fsEmpty || fsItem.isBefore(loMinDate));
        }
    };

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField04, txtField06, txtField07, txtField08, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23,
                txtField24, txtField25, txtField36, txtField32, txtField62, txtField65, txtField81);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea17, textArea26, textArea27, textArea28);

        loTxtArea.forEach(ta -> InputTextUtil.setCapsLockBehavior(ta));
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField15, txtField16, txtField18,
                txtField20, txtField21, txtField22, txtField23, txtField24,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36,
                txtField39, txtField42, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60, txtField62,
                txtField63, txtField65, txtField68, txtField70, txtField74, txtField75,
                txtField81);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea27, textArea28);
        loTxtArea.forEach(ta -> ta.setOnKeyPressed(event -> textArea_KeyPressed(event)));
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
                    loJSON = oTransVSP.searchInquiry(lsValue);
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
                    break;
                case "txtField20":
//                    loJSON = oTransVSP.searchClient(lsValue, true);
//                    if (!"error".equals(loJSON.get("result"))) {
//                    txtField20.setText(oTransVSP.getMasterModel().getMasterModel().getCSNo());
//                    txtField21.setText(oTransVSP.getMasterModel().getMasterModel().getPlateNo());
//                    txtField22.setText(oTransVSP.getMasterModel().getMasterModel().getEngineNo());
//                    txtField23.setText(oTransVSP.getMasterModel().getMasterModel().getFrameNo());
//                    txtField25.setText(oTransVSP.getMasterModel().getMasterModel().getKeyNo());
//                    textArea26.setText(oTransVSP.getMasterModel().getMasterModel().getVhclDesc());
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        txtField20.setText("");
//                        txtField20.requestFocus();
//                        return;
//                    }
//                    break;
                    break;
                case "txtField32":
                    loJSON = oTransVSP.searchBankApp(lsValue);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField32.setText(oTransVSP.getMasterModel().getMasterModel().getBankName());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField32.setText("");
                        txtField32.requestFocus();
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
                    loJSON = oTransVSP.searchInsurance(lsValue, true);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField62.setText(oTransVSP.getMasterModel().getMasterModel().getTPLInsNm());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField62.setText("");
                        txtField62.requestFocus();
                        return;
                    }
                    break;
                case "txtField65":
                    loJSON = oTransVSP.searchInsurance(lsValue, false);
                    if (!"error".equals(loJSON.get("result"))) {
                        txtField65.setText(oTransVSP.getMasterModel().getMasterModel().getCOMInsNm());
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField65.setText("");
                        txtField65.requestFocus();
                        return;
                    }
                    break;
            }
            initFields(pnEditMode);
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
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
        List<TextField> loTxtField = Arrays.asList(txtField30, txtField31, txtField33, txtField34, txtField35, txtField39, txtField42,
                txtField45, txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53, txtField54,
                txtField55, txtField56, txtField57, txtField58, txtField59, txtField60, txtField63, txtField68, txtField70,
                txtField74, txtField75
        );
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
        List<TextArea> loTxtArea = Arrays.asList(textArea27, textArea28);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
    }

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 30: // Vehicle Price
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setUnitPrce(Double.valueOf(lsValue.replace(",", "")));
                    txtField30.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getUnitPrce()))));
                    break;
                case 31: // DownPayment
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSP.getMasterModel().getMasterModel().setDownPaym(Double.valueOf(lsValue.replace(",", "")));
                    txtField31.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 33: // Bank Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField33.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 34: // Terms
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().setT(Double.valueOf(lsValue.replace(",", "")));
//                    txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 35: // Rate
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().setT(Double.valueOf(lsValue.replace(",", "")));
//                    txtField34.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 39: //Net DownPayment
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField39.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 42: // Prompt Payment Disc.
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField42.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 45: //STD Fleet Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField45.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 46: // STD Fleet Discount Plant
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField46.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 47:// STD Fleet Discount Dealer
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField47.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 48: //SPL Fleet Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField48.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 49://SPL Fleet Discount Plant
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField49.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 50://SPL Fleet Discount Dealer
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField50.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 51: //Promo Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField51.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 52://Promo Discount Plant
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField52.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));

                    break;
                case 53://Promo Discount Dealer
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField503setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));

                    break;
                case 54:// Cash Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField54.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));

                    break;
                case 55: //Labor Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField55.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 56: //Bundle Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField56.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 57: //Accessories Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField57.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 58: //Insurance Discount
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField58.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 59: //OMA & CMF
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField59.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 60: //TPL Insurance
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField60.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 63: //Compre Insurance
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField63.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 68: //LTO
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField68.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 70: // CHMO/Doc Stamps
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField70.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));

                    break;
                case 74: //Misc Charges
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
//                    txtField74.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSP.getMasterModel().getMasterModel().getDownPaym()))));
                    break;
                case 75://Description
//                    oTransVSP.getMasterModel().getMasterModel().set(Double.valueOf(lsValue.replace(",", "")));
                    break;

            }
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
//                case 27:
                case 28:
                    oTransVSP.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

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
        if (!comboBox66.getSelectionModel().isSelected(0)) {
            comboBox67.getItems().remove("0");
        } else {
            comboBox67.getItems().add("0");
        }

    }

    private void initDatePropertyAction() {

    }

    private void initTextPropertyAction() {
//        txtField15.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                if (newValue != null) {
//                    if (newValue.isEmpty()) {
//                        oTransVSP.getMasterModel().getMasterModel().setClientID("");
//                        oTransVSP.getMasterModel().getMasterModel().setCoCltID("");
//                        oTransVSP.getMasterModel().getMasterModel().setCSNo("");
//                        oTransVSP.getMasterModel().getMasterModel().setPlateNo("");
//                        oTransVSP.getMasterModel().getMasterModel().setEngineNo("");
//                        oTransVSP.getMasterModel().getMasterModel().setFrameNo("");
////                        txtField16.setText("");
////                        textArea17.setText("");
////                        txtField18.setText("");
////                        txtField19.setText("");
////                        brandNewCat.setSelected(false);
////                        preOwnedCat.setSelected(false);
////                        txtField20.setText("");
////                        txtField21.setText("");
////                        txtField22.setText("");
////                        txtField23.setText("");
////                        txtField24.setText("");
////                        txtField25.setText("");
////                        textArea26.setText("");
//                        clearVSPFields(true);
//                    }
//                }
//                initFields(pnEditMode);
//            }
//        });

    }

    private void initButtonsClick() {
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
                clearVSPFields(false);
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
                    if (setSelection()) {
                        loJSON = oTransVSP.saveTransaction();
                        if ("success".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Information(null, "Vehicle Sales Proposal Information", (String) loJSON.get("message"));
                            loJSON = oTransVSP.openTransaction(oTransVSP.getMasterModel().getMasterModel().getTransNo());
                            if ("success".equals((String) loJSON.get("result"))) {
                                loadVSPFields();
                                loadLaborTable();
                                loadPartsTable();
                                pnEditMode = oTransVSP.getEditMode();
                                initFields(pnEditMode);
                            }
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        }
                    }
                } else {
                    return;
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearVSPFields(false);
                    clearTables();
                    switchToTab(tabMain, ImTabPane);// Load fields, clear them, and set edit mode
                    oTransVSP = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Sales Proposal Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransVSP.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVSPFields();
                    loadLaborTable();
                    loadPartsTable();
                    pnEditMode = oTransVSP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Sales Proposal Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnAdditionalLabor":
//                    if (oTrans.addVSPLabor("", false)) {
//                        loadLaborAdditionalDialog(oTrans.getVSPLaborCount(), true);
//                    }
                break;
            case "btnAddParts":
//                    if (oTrans.AddVSPParts()) {
//                        loadPartsAdditionalDialog(oTrans.getVSPPartsCount(), true);
//                    }
                break;
            case "btnPrint":
//                    String lsrowdata = oTrans.getMaster(1).toString();
//                    loadVSPPrint(lsrowdata);
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
//                    if (!cancelVSP()) {
//                        return;
//                    }
                break;
            case "btnJobOrderAdd":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to create new sales job order record?") == true) {
                } else {
                    return;
                }
//                    loadJobOrderWindow();
                break;
            case "btnAddReservation":
                try {
                loadAddReservationWindowDialog();

            } catch (IOException ex) {
                Logger.getLogger(VSPFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnRemoveReservation":
                try {
                loadRemoveReservationWindowDialog();

            } catch (IOException ex) {
                Logger.getLogger(VSPFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            break;
            default:
                ShowMessageFX.Warning(getStage(), "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                break;
        }
        initFields(pnEditMode);

    }

    private boolean setSelection() {
        return true;
    }

    private void loadAddReservationWindowDialog() throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPAddReservationInquiries.fxml"));
//           VSPAddReservationInquiriesController loControl = new VSPAddReservationInquiriesController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransVSP);
//            fxmlLoader.setController(loControl);
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
//           VSPRemoveReservationInquiriesController loControl = new VSPRemoveReservationInquiriesController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransVSP);
//            fxmlLoader.setController(loControl);
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
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    @SuppressWarnings("unchecked")
    private void clearVSPFields(boolean fbIsInqrClrd) {
        if (fbIsInqrClrd) {
            List<TextField> loTxtFieldString = Arrays.asList(txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
                    txtField10, txtField11, txtField12, txtField13, txtField14, txtField16, txtField18,
                    txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField32, txtField36,
                    txtField62, txtField65, txtField75, txtField81);
            loTxtFieldString.forEach(tf -> tf.setText(""));

            List<TextField> loTxtFieldDouble = Arrays.asList(txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                    txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                    txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                    txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                    txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                    txtField77, txtField78, txtField79, txtField80);
            loTxtFieldDouble.forEach(tf -> tf.setText("0.00"));

            List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                    comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
            loComboBox.forEach(tf -> tf.setValue(null));
            datePicker09.setValue(LocalDate.of(1999, Month.JANUARY, 1));
            brandNewCat.setSelected(false);
            preOwnedCat.setSelected(false);
            chckBoxSpecialAccount.setSelected(false);
            chckBoxRustProof.setSelected(false);
            chckBoxPermaShine.setSelected(false);
            chckBoxUndercoat.setSelected(false);
            chckBoxTint.setSelected(false);
            tgUnitCategory.selectToggle(null);
            switchToTab(tabMain, ImTabPane);
        } else {
            List<TextField> loTxtFieldString = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
                    txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField18,
                    txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField32, txtField36,
                    txtField62, txtField65, txtField75, txtField81);
            loTxtFieldString.forEach(tf -> tf.setText(""));

            List<TextArea> loTxtAreaString = Arrays.asList();

            loTxtAreaString.forEach(ta -> ta.setText(""));

            List<TextField> loTxtFieldDouble = Arrays.asList(txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                    txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                    txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                    txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                    txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                    txtField77, txtField78, txtField79, txtField80);
            loTxtFieldDouble.forEach(tf -> tf.setText("0.00"));

            List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                    comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
            loComboBox.forEach(tf -> tf.setValue(null));
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

    }

    private void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    private void clearTables() {
        laborData.clear();
        partData.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        setDisable(true,
                txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, comboBox37,
                txtField39, txtField42, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60, txtField62,
                txtField63, txtField65, txtField68, txtField70, txtField72, txtField73, txtField74, txtField75, btnJobOrderAdd);
        setDisable(!lbShow,
                txtField15, textArea28,
                //discount
                txtField51, txtField54, txtField56, txtField58,
                //comboBox
                comboBox37, comboBox61, comboBox64, comboBox66, comboBox67, comboBox69, comboBox71,
                //add ons
                chckBoxRustProof, chckBoxPermaShine, chckBoxUndercoat, chckBoxTint,
                btnAdditionalLabor, btnAddParts, datePicker09
        );

//        tabAddOns.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
//        tabDetails.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        //depends if empty or not
        setDisable(!(lbShow && !txtField15.getText().isEmpty()),
                txtField16, txtField18, txtField19, txtField20, txtField21, txtField22, txtField23, txtField24,
                chckBoxSpecialAccount, comboBox29, txtField30, btnAddReservation, btnRemoveReservation);

        if (lbShow) {
            //Payment Mode
            if (comboBox29.getSelectionModel().getSelectedIndex() >= 0) {
                double ldVhclSRP = Double.parseDouble(txtField30.getText());
                switch (comboBox29.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        txtField31.setDisable(true);
                        break;
                    case 1: //BANK PURCHASE ORDER
                        if (ldVhclSRP > 0.00 || ldVhclSRP > 0.0) {
                            setDisable(!lbShow,
                                    txtField31,
                                    txtField32,
                                    txtField36,
                                    comboBox37,
                                    txtField39,
                                    txtField42,
                                    txtField59
                            );
                            txtField33.setDisable(!(lbShow && !txtField32.getText().isEmpty()));
                            txtField34.setDisable(true);
                            txtField35.setDisable(true);
                        }
                        break;
                    case 2: //BANK FINANCING
                    case 3: // Company
                    case 4:
                        if (ldVhclSRP > 0.00 || ldVhclSRP > 0.0) {
                            setDisable(!lbShow,
                                    txtField31,
                                    txtField32,
                                    txtField34,
                                    txtField35,
                                    txtField36,
                                    comboBox37,
                                    txtField39,
                                    txtField42,
                                    txtField59
                            );
                            txtField33.setDisable(!(lbShow && !txtField32.getText().isEmpty()));
                        }
                        break;
                }
            }
            //Finance Promo
            switch (comboBox37.getSelectionModel().getSelectedIndex()) {
                case 0://NONE
                case 1://ALL-IN HOUSE
                    txtField56.setDisable(true);
                    loadVSPFields();
                    break;
                case 2://ALL-IN PROMO
                    txtField54.setDisable(true);
                    loadVSPFields();
                    break;
            }
            if (chckBoxSpecialAccount.isSelected()) {
                txtField45.setDisable(!lbShow);
                txtField48.setDisable(!lbShow);
            }
            double ldSTDFleet = Double.parseDouble(txtField45.getText());
            double ldSPLFleet = Double.parseDouble(txtField48.getText());
            double ldPromoDsc = Double.parseDouble(txtField51.getText());
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
                    loadVSPFields();
                    break;
            }
            //Compre Insurance
            switch (comboBox64.getSelectionModel().getSelectedIndex()) {
                case 1: //FOC
                    txtField63.setDisable(true);
                    txtField65.setDisable(!lbShow);
                    comboBox66.setDisable(true);
                    comboBox67.setDisable(!lbShow);
                    loadVSPFields();
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
                    loadVSPFields();
                    break;
            }
            //Compre Ins Type
            switch (comboBox66.getSelectionModel().getSelectedIndex()) {
                case 1:
                case 2:
                case 3:
                    comboBox67.setDisable(!lbShow);
                    break;
                default:
                    comboBox67.setDisable(true);
                    break;
            }

            switch (comboBox69.getSelectionModel().getSelectedIndex()) {
                case 2: //
                    txtField18.setDisable(!lbShow);
                    break;
                default:
                    txtField18.setDisable(true);
                    loadVSPFields();
                    break;
            }
            switch (comboBox71.getSelectionModel().getSelectedIndex()) {
                case 3:
                case 4:
                    txtField19.setDisable(!lbShow);
                    break;
                default:
                    txtField19.setDisable(true);
                    loadVSPFields();
                    break;
            }
            double ldMSChrgs = Double.parseDouble(txtField74.getText());
            if (ldMSChrgs > 0.00 || ldMSChrgs > 0.0) {
                txtField75.setDisable(!lbShow);
            }
//            if (!((String) oTransVSP.getMaster("sSerialID")).isEmpty()) {
//                if (!lblVSPStatus.getText().equals("Cancelled")) {
//                    if (oTransVSP.getVSPLaborCount() != 0 || oTransVSP.getVSPPartsCount() != 0) {
//                        boolean isLaborApproved = tblViewLabor.getItems().stream().anyMatch(item -> !item.getTblindex14_Labor().isEmpty());
//                        boolean isPartsApproved = tblViewParts.getItems().stream().anyMatch(item -> !item.getTblindex20_Part().isEmpty());
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
            txtField15.setDisable(true);
        }
        btnJobOrderAdd.setDisable(fnValue == EditMode.ADDNEW || lblVSPStatus.getText().equals("Cancelled"));
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
            if (lblVSPStatus.getText().equals("Cancelled")) {
                btnCancelVSP.setVisible(false);
                btnCancelVSP.setManaged(false);
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                tabAddOns.setDisable(false);
                tabDetails.setDisable(false);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
            } else {
                btnCancelVSP.setVisible(true);
                btnCancelVSP.setManaged(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                tabAddOns.setDisable(false);
                tabDetails.setDisable(false);
            }
        }
    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

}
