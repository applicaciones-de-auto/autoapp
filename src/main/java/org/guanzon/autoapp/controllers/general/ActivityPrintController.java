/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

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
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.autoapp.models.general.ModelActivityInformation;
import org.guanzon.autoapp.models.general.ModelActivityMember;
import org.guanzon.autoapp.models.general.ModelActivityLocation;
import org.guanzon.autoapp.models.general.ModelActivityVehicle;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityPrintController implements Initializable, ScreenInterface {

    private Activity oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "ActivityPrint";
    private ObservableList<ModelActivityInformation> actMasterData = FXCollections.observableArrayList();
    private List<ModelActivityMember> actMembersData = new ArrayList<ModelActivityMember>();
    private List<ModelActivityLocation> locationData = new ArrayList<ModelActivityLocation>();
    private List<ModelActivityVehicle> actVhclModelData = new ArrayList<ModelActivityVehicle>();
    private String psTransNox;
    private boolean running = false;
    Map<String, Object> params = new HashMap<>();
    private Timeline timeline;
    private Integer timeSeconds = 3;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransPrint = new Activity(oApp, false, oApp.getBranchCode());
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
                    try {
                        loadReport();
                    } catch (SQLException ex) {
                        Logger.getLogger(ActivityPrintController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }));
            timeline.playFromStart();
        }
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

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
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
            fsValue = InputTextUtil.xsDateShort((Date) oTransPrint.getMaster(fsCol));
        }
        return fsValue;
    }

    private static String formatAmount(String fsAmountString) {
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

    private boolean loadReport() throws SQLException {
        int lnCtr;
        JSONObject loJSON = new JSONObject();
        loJSON = oTransPrint.openTransaction(psTransNox);
        if ("success".equals((String) loJSON.get("result"))) {
            params.put("actNo", getValueReport("actNo", "sActNoxxx"));
            params.put("actID", getValueReport("actID", "sActvtyID"));
            params.put("actDateFrom", getValueDateReport("actDateFrom", "dDateFrom"));
            params.put("actDateTo", getValueDateReport("actDateTo", "dDateThru"));
            params.put("actTitle", getValueReport("actTitle", "sActTitle"));
            params.put("actDesc", getValueReport("actDesc", "sActDescx"));
            params.put("actTypDs", getValueReport("actTypeDs", "sActTypDs"));
            params.put("actLogRemrks", getValueReport("actLogRrmks", "sLogRemrk"));
            params.put("actDeprtName", getValueReport("actDeprtName", "sDeptName"));
            params.put("actPersonEnhrge", getValueReport("actPersonEnhrge", "sCompnyNm"));
            params.put("actRemarks", getValueReport("actLogRrmks", "sRemarksx"));
            params.put("actBranchNm", getValueReport("actBranchNm", "sBranchNm"));
            params.put("actBranchCd", getValueReport("actLocation", "sLocation"));
            params.put("actTrgClnt", String.valueOf(oTransPrint.getMaster("nTrgtClnt")));
            params.put("actRcvdBdgt", formatAmount(oTransPrint.getMaster("nRcvdBdgt").toString()));
            params.put("actPropBdgt", formatAmount(oTransPrint.getMaster("nPropBdgt").toString()));
            params.put("actEntryDate", getValueDateReport("actEntryDate", "dEntryDte"));
            params.put("actApprovDte", getValueDateReport("actApprovDte", "dApproved"));
            String lsFrom = InputTextUtil.xsDateShort((Date) oTransPrint.getMaster("dDateFrom"));
            String lsTo = InputTextUtil.xsDateShort((Date) oTransPrint.getMaster("dDateThru"));
            String duration = lsFrom + " - " + lsTo;
            params.put("durationTime", duration);
            //Activity Location
            locationData.clear();
            String sAddress = "";
            for (lnCtr = 0; lnCtr <= oTransPrint.getActLocationList().size() - 1; lnCtr++) {
                sAddress = oTransPrint.getActLocation(lnCtr, "sAddressx").toString().toUpperCase() + " " + oTransPrint.getActLocation(lnCtr, "sBrgyName").toString().toUpperCase() + " " + oTransPrint.getActLocation(lnCtr, "sTownName").toString().toUpperCase() + ", " + oTransPrint.getActLocation(lnCtr, "sProvName").toString().toUpperCase();
                locationData.add(new ModelActivityLocation(
                        String.valueOf(lnCtr + 1), //ROW
                        sAddress,
                        oTransPrint.getActLocation(lnCtr, "sTownIDxx").toString().toUpperCase(),
                        oTransPrint.getActLocation(lnCtr, "sTownName").toString().toUpperCase(),
                        oTransPrint.getActLocation(lnCtr, "sCompnynx").toString().toUpperCase(),
                        oTransPrint.getActLocation(lnCtr, "sZippCode").toString(),
                        oTransPrint.getActLocation(lnCtr, "sProvIDxx").toString(),
                        oTransPrint.getActLocation(lnCtr, "sProvName").toString().toUpperCase(),
                        oTransPrint.getActLocation(lnCtr, "sBrgyIDxx").toString(),
                        oTransPrint.getActLocation(lnCtr, "sBrgyName").toString().toUpperCase()
                ));
            }
            //Activity Members
            actMembersData.clear();
            for (lnCtr = 0; lnCtr <= oTransPrint.getActMemberList().size() - 1; lnCtr++) {
                if (oTransPrint.getActMember(lnCtr, "cOriginal").equals("1")) {
                    actMembersData.add(new ModelActivityMember(
                            String.valueOf(lnCtr + 1), //ROW
                            "",
                            oTransPrint.getActMember(lnCtr, "sDeptName").toString().toUpperCase(),
                            oTransPrint.getActMember(lnCtr, "sEmployID").toString().toUpperCase(),
                            oTransPrint.getActMember(lnCtr, "sCompnyNm").toString().toUpperCase()));
                }
            }
            //Activity Vehicle
            actVhclModelData.clear();
            for (lnCtr = 0; lnCtr <= oTransPrint.getActVehicleList().size() - 1; lnCtr++) {
                actVhclModelData.add(new ModelActivityVehicle(
                        String.valueOf(lnCtr + 1), //ROW
                        oTransPrint.getActVehicle(lnCtr, "sSerialID").toString().toUpperCase(),
                        oTransPrint.getActVehicle(lnCtr, "sCSNoxxxx").toString().toUpperCase(),
                        oTransPrint.getActVehicle(lnCtr, "sDescript").toString().toUpperCase()));
            }

            String lsSourceFileName = "D://GGC_Maven_Systems/reports/autoapp/ActivityReport.jasper";
            JRBeanCollectionDataSource vehicle = new JRBeanCollectionDataSource(actVhclModelData);
            JRBeanCollectionDataSource actlocation = new JRBeanCollectionDataSource(locationData);
            JRBeanCollectionDataSource member = new JRBeanCollectionDataSource(actMembersData);
            System.out.println("vehicle: " + vehicle.getData());
            params.put(
                    "vehicle", vehicle);
            params.put(
                    "actlocation", actlocation);
            params.put(
                    "member", member);
            try {
                poJasperPrint = JasperFillManager.fillReport(lsSourceFileName, params, new JREmptyDataSource());
                if (poJasperPrint != null) {
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
}
