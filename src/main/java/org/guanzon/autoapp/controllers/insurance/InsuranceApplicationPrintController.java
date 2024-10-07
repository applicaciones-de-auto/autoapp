/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.insurance;

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
import org.guanzon.auto.main.insurance.InsurancePolicyApplication;
import org.guanzon.autoapp.interfaces.GPrintInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InsuranceApplicationPrintController implements Initializable, GPrintInterface {

    private GRider oApp;
    private InsurancePolicyApplication oTransPrint;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "Insurance Application Print";
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private boolean running = false;
    private String psTransNox = "";
    Map<String, Object> params = new HashMap<>();
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private final int pnCtr = 0;
    @FXML
    private AnchorPane AnchorMain;
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

    public void setObject(InsurancePolicyApplication foValue) {
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
        vbProgress.setVisible(true);
        btnPrint.setVisible(false);
        btnPrint.setDisable(true);
        timeline = new Timeline();
        generateReport();

        btnClose.setOnAction(this::handleButtonAction);
        btnPrint.setOnAction(this::handleButtonAction);
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
        String lsFormattedAmount = "";
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
            params.put("controlNo", "");
            params.put("customerName", getValueReport("customerName", "sOwnrNmxx"));
            params.put("address", getValueReport("customerName", "sAddressx"));
            params.put("vehicleDesc", getValueReport("vehicleDesc", "sVhclDesc"));
            params.put("color", getValueReport("color", "sColorDsc"));
            params.put("chassisNo", getValueReport("chassisNo", "sFrameNox"));
            params.put("motorNo", getValueReport("motorNo", "sEngineNo"));
            String lsPlateCSNo = "";
            if (oTransPrint.getMasterModel().getMasterModel().getPlateNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getPlateNo() + " / " + oTransPrint.getMasterModel().getMasterModel().getCSNo();
            } else if (oTransPrint.getMasterModel().getMasterModel().getCSNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo();
            }
            params.put("plateCSNo", lsPlateCSNo);
            String lsInsBranc = "";
            if (oTransPrint.getMasterModel().getMasterModel().getInsurNme() != null && oTransPrint.getMasterModel().getMasterModel().getBrInsNme() != null) {
                if (!oTransPrint.getMasterModel().getMasterModel().getInsurNme().isEmpty() && !oTransPrint.getMasterModel().getMasterModel().getBrInsNme().isEmpty()) {
                    lsInsBranc = oTransPrint.getMasterModel().getMasterModel().getInsurNme() + " " + oTransPrint.getMasterModel().getMasterModel().getBrInsNme();
                }
            }
            params.put("insuranceCompany", lsInsBranc);
            String lsInsDate = "";
            if (oTransPrint.getMasterModel().getMasterModel().getValidFrmDte() != null && oTransPrint.getMasterModel().getMasterModel().getValidTruDte() != null) {
                lsInsDate = getValueDateReport("", "dValidFrm") + " - " + getValueDateReport("", "dValidTru");

            }
            params.put("inceptionDate", lsInsDate);
            String lsPolicyType = "";
            switch (oTransPrint.getMasterModel().getMasterModel().getInsTypID()) {
                case "y":
                    lsPolicyType = "TPL";
                    break;
                case "n":
                    lsPolicyType = "COMPREHENSIVE";
                    break;
                case "b":
                    lsPolicyType = "TPL AND COMPREHENSIVE";
                    break;
            }
            params.put("policyType", lsPolicyType);
            String lsBank = "";
            if (oTransPrint.getMasterModel().getMasterModel().getBankName() != null && oTransPrint.getMasterModel().getMasterModel().getBrBankNm() != null) {
                if (!oTransPrint.getMasterModel().getMasterModel().getBankName().isEmpty() && !oTransPrint.getMasterModel().getMasterModel().getBrBankNm().isEmpty()) {
                    lsBank = oTransPrint.getMasterModel().getMasterModel().getBankName() + " " + oTransPrint.getMasterModel().getMasterModel().getBrBankNm();
                }
            }
            params.put("mortgage", lsBank);
            String lsTPLAmnt = "";
            String lsTPLPrem = "";
            if (oTransPrint.getMasterModel().getMasterModel().getAONCPayM() != null) {
                if (oTransPrint.getMasterModel().getMasterModel().getAONCPayM().equals("na")) {
                    lsTPLAmnt = "N/A";
                    lsTPLPrem = "N/A";
                } else {
                    lsTPLAmnt = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTPLAmt()));
                    lsTPLPrem = getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTPLPrem()));
                }
            }
            params.put("ownDmgAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getODTCAmt())));
            params.put("aonAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBdyCAmt())));
            params.put("tplAmount", lsTPLAmnt);
            params.put("bodilyAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBdyCAmt())));
            params.put("prprtyAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPrDCAmt())));
            params.put("pasAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPAcCAmt())));
            params.put("taxRate", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTaxRate())));
            params.put("ownPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getODTCPrem())));
            params.put("aonPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getAONCPrem())));
            params.put("tplPremium", lsTPLPrem);
            params.put("bodilyPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getBdyCPrem())));
            params.put("prprtyPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPrDCPrem())));
            params.put("passPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPAcCPrem())));
            params.put("totalPremium", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTotalAmt())));
            params.put("basicPremium", "");
            params.put("remarks", "");
            params.put("preparedBy", "");
            params.put("taxAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTaxAmt())));
            params.put("notedBy", "");
            String sourceFileName = "D://GGC_Maven_Systems/reports/autoapp/insapplication.jasper";
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
