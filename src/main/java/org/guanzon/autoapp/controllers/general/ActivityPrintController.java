/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.awt.Component;
import java.net.URL;
import java.sql.SQLException;
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
import org.guanzon.autoapp.models.general.ModelActivityInformation;
import org.guanzon.autoapp.models.general.ModelActivityMember;
import org.guanzon.autoapp.models.general.ModelActivityLocation;
import org.guanzon.autoapp.models.general.ModelActivityVehicle;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityPrintController implements Initializable, ScreenInterface {

//    private Activity oTransAct;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "ActivityPrint";
    private ObservableList<ModelActivityInformation> actMasterData = FXCollections.observableArrayList();
    private List<ModelActivityMember> actMembersData = new ArrayList<ModelActivityMember>();
    private List<ModelActivityLocation> townCitydata = new ArrayList<ModelActivityLocation>();
    private List<ModelActivityVehicle> actVhclModelData = new ArrayList<ModelActivityVehicle>();
    private String psTransNox;
    private boolean running = false;
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
        //       oTrans= new Activity(oApp, false, oApp.getBranchCode());
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

    public void setTransNox(String fsValue) {
        psTransNox = fsValue;
    }

    private boolean loadReport() throws SQLException {
        int lnCtr;
        Map<String, Object> params = new HashMap<>();
        actMasterData.clear();
//        if (oTransAct.OpenRecord(psTransNox)) {
//            for (lnCtr = 1; lnCtr <= oTransAct.getItemCount(); lnCtr++) {
//                String dApproved = " ";
//                if (!((String) oTransAct.getDetail(lnCtr, "sApproved")).isEmpty()) {
//                    dApproved = CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dApproved"));
//                }
//                String lsEntry = CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dEntryDte"));
//                if (lsEntry.isEmpty()) {
//                    lsEntry = " ";
//                } else {
//                    lsEntry = CommonUtils.xsDateShort((Date) oTrans.getDetail(lnCtr, "dEntryDte"));
//                }
//                String lsFrom = CommonUtils.xsDateLong((Date) oTrans.getDetail(lnCtr, "dDateFrom"));
//                String lsTo = CommonUtils.xsDateLong((Date) oTrans.getDetail(lnCtr, "dDateThru"));
//                String lsLogRemark = "";
//                if (!oTransAct.getDetail(lnCtr, "sLogRemrk").toString().isEmpty()) {
//                    lsLogRemark = oTransAct.getDetail(lnCtr, "sLogRemrk").toString().toUpperCase();
//                }
//                String duration = lsFrom + " - " + lsTo;
//                actMasterData.add(new ModelActivityInformation(
//                        oTransAct.getDetail(lnCtr, "sActvtyID").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sActTitle").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sActDescx").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sActTypDs").toString().toUpperCase(),
//                        duration,
//                        "",
//                        oTransAct.getDetail(lnCtr, "sLocation").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sCompnynx").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "nPropBdgt").toString(),
//                        oTransAct.getDetail(lnCtr, "nTrgtClnt").toString(),
//                        lsLogRemark,
//                        oTransAct.getDetail(lnCtr, "sRemarksx").toString().toUpperCase(),
//                        lsEntry,
//                        dApproved,
//                        oTransAct.getDetail(lnCtr, "sDeptName").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sCompnyNm").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sBranchNm").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sProvName").toString().toUpperCase(),
//                        oTransAct.getDetail(lnCtr, "sActNoxxx").toString().toUpperCase()
//                ));
//            }
//            townCitydata.clear();
//            String lsTown = "";
//            for (lnCtr = 0; lnCtr <= oTransAct.getActTownList().size() - 1; lnCtr++) {
//                if (lsTown.isEmpty()) {
//                    lsTown = oTransAct.getActTown(lnCtr, "sTownName").toString();
//                } else {
//                    lsTown = lsTown + " , " + oTransAct.getActTown(lnCtr, "sTownName").toString();
//                }
//            }
//            townCitydata.add(new ModelActivityLocation(
//                    "", //ROW
//                    "",
//                    lsTown.toUpperCase()
//            ));
//            actMembersData.clear();
//            for (lnCtr = 0; lnCtr <= oTransAct.getActMemberList().size() - 1; lnCtr++) {
//                if (oTransAct.getActMember(lnCtr, "cOriginal").equals("1")) {
//                    actMembersData.add(new ModelActivityMember(
//                            String.valueOf(lnCtr + 1), //ROW
//                            "",
//                            oTransAct.getActMember(lnCtr, "sDeptName").toString().toUpperCase(),
//                            oTransAct.getActMember(lnCtr, "sEmployID").toString().toUpperCase(),
//                            oTransAct.getActMember(lnCtr, "sCompnyNm").toString().toUpperCase()));
//                }
//            }
//            actVhclModelData.clear();
//            for (lnCtr = 0; lnCtr <= oTransAct.getActVehicleList().size() - 1; lnCtr++) {
//                actVhclModelData.add(new ModelActivityVehicle(
//                        String.valueOf(lnCtr + 1), //ROW
//                        oTransAct.getActVehicle(lnCtr, "sSerialID").toString().toUpperCase(),
//                        oTransAct.getActVehicle(lnCtr, "sDescript").toString().toUpperCase(),
//                        oTransAct.getActVehicle(lnCtr, "sCSNoxxxx").toString().toUpperCase()));
//            }
//        }
        String lsSourceFileName = "D://GGC_Java_Systems/reports/autoapp/ActivityPrint.jasper";
        String lsPrintFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(actMasterData);
        JRBeanCollectionDataSource vehicle = new JRBeanCollectionDataSource(actVhclModelData);
        JRBeanCollectionDataSource town = new JRBeanCollectionDataSource(townCitydata);
        JRBeanCollectionDataSource member = new JRBeanCollectionDataSource(actMembersData);
        params.put("vehicle", vehicle);
        params.put("town", town);
        params.put("member", member);
        try {
            poJasperPrint = JasperFillManager.fillReport(lsSourceFileName, params, beanColDataSource);
            lsPrintFileName = poJasperPrint.toString();
            if (lsPrintFileName != null) {
                showReport();
            }
        } catch (JRException ex) {
            running = false;
            vbProgress.setVisible(false);
            timeline.stop();
        }
        return false;

    }
}
