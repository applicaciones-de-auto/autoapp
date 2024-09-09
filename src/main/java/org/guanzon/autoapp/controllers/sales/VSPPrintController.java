/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.awt.Component;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.AbstractButton;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.swing.JRViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VSPPrintController implements Initializable, ScreenInterface {

    private VehicleSalesProposal oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "Vehicle SAles Proposal Print";
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private boolean running = false;
    private String psTransNox = "";
    Map<String, Object> params = new HashMap<>();
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private int pnCtr = 0;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnClose;
    @FXML
    private VBox vbProgress;
    @FXML
    private AnchorPane reportPane;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    public void setVSObject(VehicleSalesProposal foValue) {
        oTransPrint = foValue;
    }

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbProgress.setVisible(true);
        btnPrint.setVisible(false);
        btnPrint.setDisable(true);
        timeline = new Timeline();
        generateReport();

        btnClose.setOnAction(this::handleButtonClick);
        btnPrint.setOnAction(this::handleButtonClick);
    }

    private void handleButtonClick(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnPrint":
                try {
                if (JasperPrintManager.printReport(poJasperPrint, true)) {
                    ShowMessageFX.Information(null, pxeModuleName, "Printed Successfully");
                    CommonUtils.closeStage(btnClose);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
                }
            } catch (JRException ex) {
                ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted");
            }
            break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private void hideReport() {
        poJrViewer = new JRViewer(null);
        reportPane.getChildren().clear();
        poJrViewer.setVisible(false);
        running = false;
        reportPane.setVisible(false);
        timeline.stop();
    }

    private void generateReport() {
        hideReport();
        if (!running) {
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), (ActionEvent event1) -> {
                timeSeconds--;
                if (timeSeconds <= 0) {
                    timeSeconds = 0;
                }
                if (timeSeconds == 0) {
                    loadReport();
                }
            }));
            timeline.playFromStart();
        }
    }

    private String getValueReport(String fsValue, String fsCol) {
        fsValue = "";
        if (oTransPrint.getMaster(fsCol) != null) {
            fsValue = oTransPrint.getMaster(fsCol).toString().toUpperCase();
        }
        return fsValue;
    }

    private String getValueDateReport(String fsValue, String fsCol) {
        fsValue = "";
        if (oTransPrint.getMaster(fsCol) != null) {
            fsValue = CustomCommonUtil.xsDateWMonthName((Date) oTransPrint.getMaster(fsCol));
        }
        return fsValue;
    }

    private static String getValueNumberReport(String fsAmountString) {
        DecimalFormat loNumFormat = new DecimalFormat("#,##0.00");
        String lsFormattedAmount = "";
        if (fsAmountString.contains("0.00") || fsAmountString.isEmpty()) {
            lsFormattedAmount = "";
        } else {
            double amount = Double.parseDouble(fsAmountString);
            lsFormattedAmount = loNumFormat.format(amount);
        }
        return lsFormattedAmount;
    }

    private boolean loadReport() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransPrint.openTransaction(psTransNox);
        if ("success".equals((String) loJSON.get("result"))) {
            params.put("branchName", oApp.getBranchName());
            params.put("branchAddress", oApp.getAddress());
            params.put("vspNo", getValueReport("vspNo", "sVSPNOxxx"));
            params.put("vspDate", getValueDateReport("vspDate", "dDelvryDt"));
            params.put("model", getValueReport("model", "sVhclDesc"));
            params.put("customerName", getValueReport("customerName", ""));
            params.put("birthDate", getValueReport("birthDate", ""));
            params.put("tinNo", getValueReport("tinNo", ""));
            params.put("homeAddress", getValueReport("homeAddress", ""));
            params.put("officeAddress", getValueReport("officeAddress", ""));
            params.put("contactNo", getValueReport("contactNo", ""));
            params.put("officeTelNo", getValueReport("officeTelNo", ""));
            params.put("emailAddress", getValueReport("emailAddress", ""));

            String lsPlateCSNo = "";
            if (oTransPrint.getMasterModel().getMasterModel().getCSNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo();
            } else if (oTransPrint.getMasterModel().getMasterModel().getPlateNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getPlateNo();
            }
            params.put("csplateNo", lsPlateCSNo);
            params.put("keyNo", getValueReport("keyNo", "sKeyNoxxx"));
            params.put("engineNo", getValueReport("engineNo", "sEngineNo"));
            params.put("frameNo", getValueReport("frameNo", "sFrameNox"));
            params.put("vspDate", getValueReport("vspDate", "dDelvryDt"));
            if (oTransPrint.getMasterModel().getMasterModel().getPayMode() != null) {
                String lsPaymentMethod = "";
                switch (oTransPrint.getMasterModel().getMasterModel().getPayMode()) {
                    case "0":
                        lsPaymentMethod = "CASH";
                        break;
                    case "1":
                        lsPaymentMethod = "BANK PURCHASE ORDER";
                        break;
                    case "2":
                        lsPaymentMethod = "BANK FINANCING";
                        break;
                    case "3":
                        lsPaymentMethod = "COMPANY PURCHASE ORDER";
                        break;
                    case "4":
                        lsPaymentMethod = "COMPANY FINANCING";
                        break;
                }
                params.put("paymentMethod", lsPaymentMethod);
            }
            params.put("terms", "0");
            params.put("rate", "0.00");
            params.put("amountFinanced", "0.00");
            params.put("netMonthInstall", "0.00");
            params.put("promptPaymentDiscount", "0.00");
            params.put("grsMontInst", "0.00");
            params.put("promiNtAmnt", "0.00");
            params.put("bankName", "");
            if (oTransPrint.getVSPFinanceList().size() > 0) {
                String lsBank = "";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getAcctTerm() != null) {
                    lsBank = String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBankName()) + "" + String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBrBankNm());
                }
                params.put("bankName", lsBank);
                String lnTerms = "0";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getAcctTerm() != null) {
                    lnTerms = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getAcctTerm()));
                }
                params.put("terms", lnTerms);
                String lnRate = "0.00";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getAcctRate() != null) {
                    lnTerms = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getAcctRate()));
                }
                params.put("rate", lnRate);
                String lnAmtFince = "0.00";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getFinAmt() != null) {
                    lnAmtFince = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getFinAmt()));
                }
                params.put("amountFinanced", lnAmtFince);
                String lnNetMonth = "0.00";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getMonAmort() != null) {
                    lnAmtFince = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getMonAmort()));
                }
                params.put("netMonthInstall", lnNetMonth);
                String lnPromptPaymentDiscount = "0.00";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getRebates() != null) {
                    lnPromptPaymentDiscount = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getMonAmort()));
                }
                params.put("promptPaymentDiscount", lnPromptPaymentDiscount);
                String lnGrsMonthIns = "0.00";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getGrsMonth() != null) {
                    lnGrsMonthIns = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getGrsMonth()));
                }
                params.put("grsMontInst", lnGrsMonthIns);
                String lnPromiAmnt = "0.00";
                if (oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getPNValue() != null) {
                    lnPromiAmnt = poGetDecimalFormat.format(String.valueOf(oTransPrint.getVSPFinanceModel().getVSPFinanceModel().getPNValue()));
                }
                params.put("promiNtAmnt", lnPromiAmnt);
            }
            params.put("vehicleSRP", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getUnitPrce())));
            params.put("downPayment", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getDownPaym())));
            String lsTLInsuranceTPL = "";
            String lsTPLInsuranceAmount = "0.00";
            switch (oTransPrint.getMasterModel().getMasterModel().getTPLStat()) {
                case "1":
                    lsTLInsuranceTPL = oTransPrint.getMasterModel().getMasterModel().getTPLInsNm() + "" + oTransPrint.getMasterModel().getMasterModel().getTPLInsNm();
                    lsTPLInsuranceAmount = "FREE";
                    break;
                case "3":
                    lsTPLInsuranceAmount = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTPLAmt()));
                    lsTLInsuranceTPL = oTransPrint.getMasterModel().getMasterModel().getTPLInsNm() + "" + oTransPrint.getMasterModel().getMasterModel().getTPLBrIns();
                    break;
            }
            params.put("insuranceTPL", lsTLInsuranceTPL);
            params.put("insTPLAmnt", lsTPLInsuranceAmount);
            String lsInsuranceCompre = "";
            String lsInsuranceCompreAmount = "";
            switch (oTransPrint.getMasterModel().getMasterModel().getCompStat()) {
                case "1":
                    lsInsuranceCompre = oTransPrint.getMasterModel().getMasterModel().getCOMInsNm() + "" + oTransPrint.getMasterModel().getMasterModel().getCOMBrIns();
                    lsInsuranceCompreAmount = "FREE";
                    break;
                case "3":
                    lsInsuranceCompreAmount = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getCompAmt()));
                    lsInsuranceCompre = oTransPrint.getMasterModel().getMasterModel().getCOMInsNm() + "" + oTransPrint.getMasterModel().getMasterModel().getCOMBrIns();
                    break;
            }
            params.put("insuranceCompre", lsInsuranceCompre);
            params.put("insCmprAmnt", lsInsuranceCompreAmount);
            String lsLtoAmount = "0.00";
            switch (oTransPrint.getMasterModel().getMasterModel().getLTOStat()) {
                case "0":
                    lsLtoAmount = "0.00";
                    break;
                case "1":
                    lsLtoAmount = "FREE";
                    break;
                case "2":
                    lsLtoAmount = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getLTOAmt()));
                    break;
            }
            params.put("ltoAmount", lsLtoAmount);
            String lsChmoDocStamps = "";
            switch (oTransPrint.getMasterModel().getMasterModel().getChmoStat()) {
                case "0":
                    lsChmoDocStamps = "0.00";
                    break;
                case "1":
                    lsChmoDocStamps = "FREE";
                    break;
                case "2":
                case "3":
                    lsChmoDocStamps = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getChmoAmt()));
                    break;
            }
            params.put("chmoDocStamps", lsChmoDocStamps);
            String lsRustAmount = "";
            String lsUnderAmount = "";
            String lsPermaAmount = "";
            String lsTintAmount = "";
            double lsAdditionalAmount = 0.00;
            String lsAdditionalAmountDisplay = "0.00";
            for (int lnCtr = 0; lnCtr <= oTransPrint.getVSPLaborList().size() - 1; lnCtr++) {
                String lsCurrentLaborDsc = "";
                String lsCurrentAmount = "";
                String lsCurrentStatus = "";
                if (oTransPrint.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDsc() != null) {
                    lsCurrentLaborDsc = String.valueOf(oTransPrint.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDsc());
                }
                if (oTransPrint.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt() != null) {
                    lsCurrentAmount = String.valueOf(oTransPrint.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt());
                }
                if (oTransPrint.getVSPLaborModel().getVSPLabor(lnCtr).getChrgeTyp() != null) {
                    lsCurrentStatus = String.valueOf(oTransPrint.getVSPLaborModel().getVSPLabor(lnCtr).getChrgeTyp());
                }
                if (lsCurrentLaborDsc.equalsIgnoreCase("RUSTPROOF")) {
                    lsRustAmount = getValueNumberReport(lsCurrentAmount);
                } else if (lsCurrentLaborDsc.equalsIgnoreCase("UNDERCOAT")) {
                    lsUnderAmount = getValueNumberReport(lsCurrentAmount);
                } else if (lsCurrentLaborDsc.equalsIgnoreCase("PERMASHINE") && lsCurrentStatus.equals("0")) {
                    lsPermaAmount = getValueNumberReport(lsCurrentAmount);
                } else if (lsCurrentLaborDsc.equalsIgnoreCase("TINT") && lsCurrentStatus.equals("0")) {
                    lsTintAmount = getValueNumberReport(lsCurrentAmount);
                } else if (lsCurrentStatus.equals("1")) {
                    double lnAmount = Double.parseDouble(lsCurrentAmount);
                    lsAdditionalAmount += lnAmount;
                    lsAdditionalAmountDisplay = getValueNumberReport(String.valueOf(lsAdditionalAmount));
                } else {
                    System.out.println("INVALID!");
                }

            }
            //stats and type not yet done
            params.put("rustProof", lsRustAmount);
            params.put("underCoat", lsUnderAmount);
            params.put("permaGloss", lsPermaAmount);
            params.put("tint", lsTintAmount);
            params.put("additionalLabor", lsAdditionalAmountDisplay);
            params.put("accessories", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAccesAmt())));
            String lsInqTypDisplay = "";
            switch (oTransPrint.getMasterModel().getMasterModel().getInsurTyp()) {
                case "0":
                    lsInqTypDisplay = "WALK-IN";
                    break;
                case "1":
                    lsInqTypDisplay = "WEB INQUIRY";
                    break;
                case "2":
                    lsInqTypDisplay = "PHONE-IN";
                    break;
                case "3":
                    lsInqTypDisplay = "REFERRAL";
                    break;
                case "4":
                    lsInqTypDisplay = "SALES CALL";
                    break;
                case "5":
                    lsInqTypDisplay = "EVENT";
                    break;
                case "6":
                    lsInqTypDisplay = "SERVICE";
                    break;
                case "7":
                    lsInqTypDisplay = "OFFICE ACCOUNT";
                    break;
                case "8":
                    lsInqTypDisplay = "CAREMITTANCE";
                    break;
                case "9":
                    lsInqTypDisplay = "DATABASE";
                    break;
                case "10":
                    lsInqTypDisplay = "UIO";
                    break;
            }
            params.put("inqType", lsInqTypDisplay);
            params.put("pRomo", "Promo disc:");
            params.put("promoAmnt", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPromoDsc())));
            params.put("caSh", "Cash disc:");
            params.put("cashAmnt", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAddlDsc())));
            params.put("bundle", "Bundle disc:");
            params.put("bundleAmnt", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBndleDsc())));
            params.put("stdFleet", "STD Fleet disc:");
            params.put("stdFleetAmnt", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getFleetDsc())));
            params.put("splFleet", "SPL Fleet disc:");
            params.put("splFleetAmnt", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getSPFltDsc())));
            params.put("totalAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTranTotl())));
            params.put("totalDsc", getValueNumberReport(String.valueOf(oTransPrint.getTotalDiscount())));
            params.put("reservationFee", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getResrvFee())));
            params.put("dnPymntAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAmtPaid())));
            params.put("netAmountDueTotal", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getNetTTotl())));
            params.put("remarksNote", getValueReport("remarksNote", "sRemarksx"));
            params.put("salesExecutive", getValueReport("salesExecutive", "sSENamexx"));
            params.put("branchUserName", oApp.getClientName());
            params.put("transNo", getValueReport("transNo", "sTransNox"));

            String sourceFileName = "D://GGC_Maven_Systems/reports/autoapp/vsp.jasper";
            String printFileName = null;
            try {
                poJasperPrint = JasperFillManager.fillReport(sourceFileName, params, new JREmptyDataSource());
                printFileName = poJasperPrint.toString();
                if (printFileName != null) {
                    showReport();
                }
            } catch (JRException ex) {
                running = false;
                vbProgress.setVisible(false);
                timeline.stop();

            }
        }

        return false;
    }

    private void showReport() {
        vbProgress.setVisible(false);
        btnPrint.setVisible(true);
        btnPrint.setDisable(false);
        poJrViewer = new JRViewer(poJasperPrint);
        poJrViewer.setZoomRatio(0.75f);
        findAndHideButton(poJrViewer, "Print");
        findAndHideButton(poJrViewer, "Save");
        // Find and hide the buttons
        SwingNode swingNode = new SwingNode();
        poJrViewer.setOpaque(true);
        poJrViewer.setVisible(true);
        poJrViewer.setFitPageZoomRatio();
        swingNode.setContent(poJrViewer);
        swingNode.setVisible(true);
        reportPane.setTopAnchor(swingNode, 0.0);
        reportPane.setBottomAnchor(swingNode, 0.0);
        reportPane.setLeftAnchor(swingNode, 0.0);
        reportPane.setRightAnchor(swingNode, 0.0);
        reportPane.getChildren().add(swingNode);
        running = true;
        reportPane.setVisible(true);
        timeline.stop();
    }

    private void findAndHideButton(Component foComponent, String fsButtonText) {
        if (foComponent instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) foComponent;
            if (button.getToolTipText() != null) {
                if (button.getToolTipText().equals(fsButtonText)) {
                    button.setVisible(false);
                    return;
                }
            }
        }

        if (foComponent instanceof java.awt.Container) {
            java.awt.Container container = (java.awt.Container) foComponent;
            Component[] loComponents = container.getComponents();
            for (Component childComponent : loComponents) {
                findAndHideButton(childComponent, fsButtonText);
            }
        }
    }

}
