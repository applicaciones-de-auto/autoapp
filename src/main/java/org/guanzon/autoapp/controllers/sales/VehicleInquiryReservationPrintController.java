/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.controllers.general.ActivityPrintController;
import org.guanzon.autoapp.models.general.ModelActivityInformation;
import org.guanzon.autoapp.models.general.ModelActivityLocation;
import org.guanzon.autoapp.models.general.ModelActivityMember;
import org.guanzon.autoapp.models.general.ModelActivityVehicle;
import org.guanzon.autoapp.models.sales.ModelInquiryVehicleSalesAdvances;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquiryReservationPrintController implements Initializable, ScreenInterface {

    private Inquiry oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "Reservation Print";
    private Integer[] pnRows;
    private boolean running = false;
    private ObservableList<ModelInquiryVehicleSalesAdvances> vhlApprovalPrintData = FXCollections.observableArrayList();
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

    public void setVSObject(Inquiry foValue) {
        oTransPrint = foValue;
    }

    public void setRows(Integer[] fnValue) {
        pnRows = fnValue;
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

        btnClose.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
    }

    private void cmdButton_Click(ActionEvent event) {
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

    private String getValueReport(Integer fnRow, String fsValue, String fsCol) {
        fsValue = "";
        if (oTransPrint.getReservation(fnRow, fsCol) != null) {
            fsValue = oTransPrint.getReservation(fnRow, fsCol).toString().toUpperCase();
        }
        return fsValue;
    }

    private String getValueDateReport(Integer fnRow, String fsValue, String fsCol) {
        fsValue = "";
        if (oTransPrint.getReservation(fnRow, fsCol) != null) {
            fsValue = InputTextUtil.xsDateShort((Date) oTransPrint.getReservation(fnRow, fsCol));
        }
        return fsValue;
    }

    private boolean loadReport() {
        JSONObject loJSON = new JSONObject();
        Map<String, Object> params = new HashMap<>();
        params.put("sCompnyNm", "Guanzon Group of Companies");
        params.put("sBranchNm", oApp.getBranchName());
        params.put("sAddressx", oApp.getAddress());
        String lsPlateCSNo = "";
        String lsDescript = "";
        if (String.valueOf(oTransPrint.getMasterModel().getMasterModel().getPlateNo()) != null) {
            lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getPlateNo();
        } else {
            lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo();
        }
        if (String.valueOf(oTransPrint.getMasterModel().getMasterModel().getDescript()) != null) {
            lsDescript = oTransPrint.getMasterModel().getMasterModel().getDescript();
        }
        params.put("sDescript", lsPlateCSNo + " " + lsDescript);
        vhlApprovalPrintData.clear();
        loJSON = oTransPrint.loadReservationList();
        if ("success".equals((String) loJSON.get("result"))) {
            for (pnCtr = 0; pnCtr <= pnRows.length - 1; pnCtr++) {
                Integer lnCtr = pnRows[pnCtr];
                //Iterate over the data and count the approved item
                String amountString = oTransPrint.getReservation(lnCtr, "nAmountxx").toString();
                //Convert the amount to a decimal value
                double amount = Double.parseDouble(amountString);
                //Format the decimal value with decimal separators
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                String formattedAmount = decimalFormat.format(amount);
                String lsResType = "";
                switch (String.valueOf(oTransPrint.getReservation(lnCtr, "cResrvTyp"))) {
                    case "0":
                        lsResType = "RESERVATION";
                        break;
                    case "1":
                        lsResType = "DEPOSIT";
                        break;
                    case "2":
                        lsResType = "SAFEGUARD DUTY";
                        break;
                }
                String lsInqStat = "";
                if (String.valueOf(oTransPrint.getReservation(lnCtr, "cTranStat")) != null) {
                    switch (String.valueOf(oTransPrint.getReservation(lnCtr, "cTranStat"))) {
                        case "0":
                            lsInqStat = "CANCELLED";
                            break;
                        case "1":
                            lsInqStat = "FOR APPROVAL";
                            break;
                        case "2":
                            lsInqStat = "APPROVED";
                            break;
                    }
                }
                vhlApprovalPrintData.add(new ModelInquiryVehicleSalesAdvances(
                        String.valueOf(lnCtr),
                        getValueDateReport(lnCtr, "lsSlipDate", "dTransact"),
                        lsResType,
                        getValueReport(lnCtr, "lsRefNox", "sReferNox"),
                        formattedAmount,
                        lsInqStat,
                        getValueReport(lnCtr, "lsRemarks", "sRemarksx"),
                        "",
                        getValueDateReport(lnCtr, "lsApprovDate", "dApproved"),
                        getValueReport(lnCtr, "lsRefNo", "sReferNox"),
                        getValueReport(lnCtr, "lsCompanyName", "sCompnyNm"),
                        "",
                        ""));

            }
        }
        String sourceFileName = "D://GGC_Maven_Systems/reports/autoapp/reserve.jasper";
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(vhlApprovalPrintData);
        try {
            poJasperPrint = JasperFillManager.fillReport(sourceFileName, params, beanColDataSource);
            printFileName = poJasperPrint.toString();
            if (printFileName != null) {
                showReport();
            }
        } catch (JRException ex) {
            running = false;
            vbProgress.setVisible(false);
            timeline.stop();
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
