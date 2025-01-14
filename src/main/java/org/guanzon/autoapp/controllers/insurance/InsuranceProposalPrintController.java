package org.guanzon.autoapp.controllers.insurance;

import java.awt.Component;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
import org.guanzon.auto.main.insurance.InsurancePolicyProposal;
import org.guanzon.autoapp.interfaces.GPrintInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InsuranceProposalPrintController implements Initializable, GPrintInterface {

    private GRider oApp;
    private InsurancePolicyProposal oTransPrint;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "Insurance Proposal Print";
    private boolean running = false;
    private String psTransNox = "";
    Map<String, Object> params = new HashMap<>();
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private final int pnCtr = 0;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnPrint, btnClose;
    @FXML
    private VBox vbProgress;
    @FXML
    private AnchorPane reportPane;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void setTransNo(String fsValue) {
        psTransNox = fsValue;
    }

    public void setObject(InsurancePolicyProposal foValue) {
        oTransPrint = foValue;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        vbProgress.setVisible(true);
        btnPrint.setVisible(false);
        btnPrint.setDisable(true);
        timeline = new Timeline();
        generateReport();
        initButtonsClick();
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnClose, btnPrint);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
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

    @Override
    public void hideReport() {
        poJrViewer = new JRViewer(null);
        reportPane.getChildren().clear();
        poJrViewer.setVisible(false);
        running = false;
        reportPane.setVisible(false);
        timeline.stop();
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
        String lsFormattedAmount = "0.00";
        DecimalFormat loNumFormat = new DecimalFormat("#,##0.00");
        if (fsAmountString != null) {
            if (fsAmountString.equals("0.00") || fsAmountString.isEmpty()) {
                lsFormattedAmount = "";
            } else {
                double amount = Double.parseDouble(fsAmountString);
                lsFormattedAmount = loNumFormat.format(amount);
            }
        }
        return lsFormattedAmount;
    }

    @Override
    public void generateReport() {
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

    @Override
    public boolean loadReport() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransPrint.openTransaction(psTransNox);
        if ("success".equals((String) loJSON.get("result"))) {
            params.put("branchName", oApp.getBranchName());
            params.put("transDate", getValueDateReport("transDate", "dTransact"));
            params.put("customerName", getValueReport("customerName", "sOwnrNmxx"));
            params.put("vehicleDesc", getValueReport("vehicleDesc", "sVhclDesc"));
            params.put("color", getValueReport("color", "sColorDsc"));
            String lsPlateCSNo = "";
            if (oTransPrint.getMasterModel().getMasterModel().getPlateNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getPlateNo() + " / " + oTransPrint.getMasterModel().getMasterModel().getCSNo();
            } else if (oTransPrint.getMasterModel().getMasterModel().getCSNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo();
            }
            params.put("plateCSNo", lsPlateCSNo);
            params.put("frameNo", getValueReport("frameNo", "sFrameNox"));
            params.put("engineNo", getValueReport("engineNo", "sEngineNo"));
            String lsInsBranc = "";
            if (oTransPrint.getMasterModel().getMasterModel().getInsurNme() != null && oTransPrint.getMasterModel().getMasterModel().getBrInsNme() != null) {
                if (!oTransPrint.getMasterModel().getMasterModel().getInsurNme().isEmpty() && !oTransPrint.getMasterModel().getMasterModel().getBrInsNme().isEmpty()) {
                    lsInsBranc = oTransPrint.getMasterModel().getMasterModel().getInsurNme() + " " + oTransPrint.getMasterModel().getMasterModel().getBrInsNme();
                }
            }
            params.put("insCompany", lsInsBranc);
            String lsPolicyType = "";
            switch (oTransPrint.getMasterModel().getMasterModel().getInsTypID()) {
                case "y":
                    lsPolicyType = "TPL";
                    break;
                case "c":
                    lsPolicyType = "COMPREHENSIVE";
                    break;
                case "b":
                    lsPolicyType = "TPL AND COMPREHENSIVE";
                    break;
            }
            String lsTPLAmnt = "";
            String lsTPLPrem = "";
            if (String.valueOf(oTransPrint.getMasterModel().getMasterModel().getInsTypID()).equals("c")) {
                lsTPLAmnt = "N/A";
                lsTPLPrem = "N/A";
            } else {
                lsTPLAmnt = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTPLAmt()));
                lsTPLPrem = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTPLPrem()));
            }
            params.put("policyType", lsPolicyType);
            params.put("enteredBy", System.getProperty("user.name"));
            params.put("enteredDate", getValueDateReport("enteredDate", "dModified"));
            params.put("ownDamageAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getODTCAmt())));
            params.put("aonAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAONCAmt())));
            params.put("tplAmount", lsTPLAmnt);
            params.put("bodilyAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBdyCAmt())));
            params.put("prptyAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPrDCAmt())));
            params.put("pasAccAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPAcCAmt())));
            String lnTax = "";
            if (oTransPrint.getMasterModel().getMasterModel().getTaxRate() != null) {
                lnTax = "(" + "Tax   " + getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTaxRate())) + "% )";
            }
            params.put("taxRate", lnTax);
            params.put("taxAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTaxAmt())));
            params.put("ownDamagePremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getODTCPrem())));
            params.put("aonPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAONCPrem())));
            params.put("tplPremium", lsTPLPrem);
            params.put("bodilyPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBdyCPrem())));
            params.put("prptyDmgPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPrDCPrem())));
            params.put("pasAccPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPAcCPrem())));
            params.put("totalPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTotalAmt())));
            double lnOwdtcPrem = Double.parseDouble(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getODTCPrem()));
            double lnAoncPrem = Double.parseDouble(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAONCPrem()));
            double lnBdyCPrem = Double.parseDouble(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBdyCPrem()));
            double lnPrdcPrem = Double.parseDouble(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPrDCPrem()));
            double lnPacCPrem = Double.parseDouble(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPAcCPrem()));
            double lnTPLPrem = Double.parseDouble(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTPLPrem()));
            double lnBasicPremium = lnOwdtcPrem + lnAoncPrem + lnBdyCPrem + lnPrdcPrem + lnPacCPrem + lnTPLPrem;
            params.put("basicPremium", getValueNumberReport(String.valueOf(lnBasicPremium)));
            String printFileName = null;
            try {
                poJasperPrint = JasperFillManager.fillReport(oApp.getReportPath() + "InsuranceProposal.jasper", params, new JREmptyDataSource());
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

    @Override
    public void showReport() {
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

    @Override
    public void findAndHideButton(Component foComponent, String fsButtonText) {
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
