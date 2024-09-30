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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.clients.Vehicle_Gatepass;
import org.guanzon.autoapp.models.general.JobDone;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VGPPrintController implements Initializable, ScreenInterface {

    private Vehicle_Gatepass oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "VGP Print";
    private List<JobDone> vgpData = new ArrayList<JobDone>();
    private String psTransNox;
    private String psAccessor;
    private boolean state;
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

    public void setObject(Vehicle_Gatepass foValue) {
        oTransPrint = foValue;
    }

    public void setTransNo(String fsValue) {
        psTransNox = fsValue;
    }

    public boolean setState() {
        return state;
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
        btnClose.setOnAction(this::handleButtonAction);
        btnPrint.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
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
                        Logger.getLogger(VGPPrintController.class.getName()).log(Level.SEVERE, null, ex);
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
            fsValue = CustomCommonUtil.xsDateShort((Date) oTransPrint.getMaster(fsCol));
        }
        return fsValue;
    }

    private boolean loadReport() throws SQLException {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransPrint.openTransaction(psTransNox);
        if ("success".equals((String) loJSON.get("result"))) {
            params.put("gatepassno", getValueReport("gatepassno", "sTransNox"));
            params.put("date", getValueDateReport("date", "dTransact"));
            params.put("customer_name", oTransPrint.getVSPModel().getMasterModel().getBuyCltNm());
            params.put("address", oTransPrint.getVSPModel().getMasterModel().getAddress());
            String lsPlateCSNo = "";
            if (oTransPrint.getVSPModel().getMasterModel().getPlateNo() != null) {
                lsPlateCSNo = oTransPrint.getVSPModel().getMasterModel().getCSNo() + " / " + oTransPrint.getVSPModel().getMasterModel().getPlateNo();
            } else if (oTransPrint.getVSPModel().getMasterModel().getCSNo() != null) {
                lsPlateCSNo = oTransPrint.getVSPModel().getMasterModel().getCSNo();
            }
            params.put("csplateno", lsPlateCSNo);
            params.put("frameno", oTransPrint.getVSPModel().getMasterModel().getFrameNo());
            params.put("engineno", oTransPrint.getVSPModel().getMasterModel().getEngineNo());
            params.put("vehicledescription", oTransPrint.getVSPModel().getMasterModel().getVhclFDsc());
            params.put("Purpose", "");
            params.put("refeno", getValueReport("refeno", "sSourceNo"));
            vgpData.clear();
            // First, add labor data to the jobDoneData list
            String lsLaborJONo = "";
            int lnRow = 1;

            // Iterate over labor data
            for (int lnLaborRow = 0; lnLaborRow <= oTransPrint.getVSPLaborList().size() - 1; lnLaborRow++) {
                lsLaborJONo = oTransPrint.getVSPLaborModel().getDetailModel(lnLaborRow).getDSNo();

                // Add only if DSNo is not null
                if (lsLaborJONo != null && !lsLaborJONo.isEmpty()) {
                    vgpData.add(new JobDone(
                            String.valueOf(lnRow),
                            "LABOR",
                            String.valueOf(oTransPrint.getVSPLaborModel().getDetailModel(lnLaborRow).getLaborCde()),
                            String.valueOf(oTransPrint.getVSPLaborModel().getDetailModel(lnLaborRow).getLaborDsc()),
                            "1", // Assuming 1 as a placeholder
                            lsLaborJONo,
                            "",
                            String.valueOf(oTransPrint.getVSPLaborModel().getDetailModel(lnLaborRow).getRemarks()))
                    );
                    lnRow++;
                }
            }

            String lsPartsJONo = "";

            // Iterate over parts data
            for (int lnPartsRow = 0; lnPartsRow <= oTransPrint.getVSPPartsList().size() - 1; lnPartsRow++) {
                lsPartsJONo = oTransPrint.getVSPPartsModel().getDetailModel(lnPartsRow).getDSNo();

                // Add only if DSNo is not null
                if (lsPartsJONo != null && !lsPartsJONo.isEmpty()) {
                    vgpData.add(new JobDone(
                            String.valueOf(lnRow),
                            "PARTS",
                            String.valueOf(oTransPrint.getVSPPartsModel().getDetailModel(lnPartsRow).getBarCode()),
                            String.valueOf(oTransPrint.getVSPPartsModel().getDetailModel(lnPartsRow).getPartDesc()),
                            String.valueOf(oTransPrint.getVSPPartsModel().getDetailModel(lnPartsRow).getQuantity()),
                            lsPartsJONo,
                            "",
                            "")
                    );
                    lnRow++;
                }
            }

            String lsSourceFileName = "D://GGC_Maven_Systems/reports/autoapp/VehicleGatePass.jasper";
            JRBeanCollectionDataSource jobDoneData = new JRBeanCollectionDataSource(vgpData);
            System.out.println("vehicle: " + jobDoneData.getData());
            params.put(
                    "jobDoneData", jobDoneData);
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
