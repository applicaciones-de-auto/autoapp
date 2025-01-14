package org.guanzon.autoapp.controllers.general;

import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.clients.Vehicle_Gatepass;
import org.guanzon.autoapp.interfaces.GPrintInterface;
import org.guanzon.autoapp.models.general.JobDone;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VGPPrintController implements Initializable, ScreenInterface, GPrintInterface {

    private Vehicle_Gatepass oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "VGP Print";
    private String psOldPrintValue = "";
    private List<JobDone> vgpData = new ArrayList<JobDone>();
    private String psTransNox;
    private String psAccessor;
    private boolean state;
    private boolean running = false;
    Map<String, Object> params = new HashMap<>();
    private Timeline timeline;
    private Integer timeSeconds = 3;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
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

    public void setOldPrint(String fsValue) {
        psOldPrintValue = fsValue;
    }

    /**
     * Initializes the controller class.
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
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnPrint":
                loJSON = oTransPrint.savePrint(true);
                if (!"error".equals((String) loJSON.get("result"))) {
                    try {
                        if (JasperPrintManager.printReport(poJasperPrint, true)) {
                            loJSON = oTransPrint.savePrint(false);
                            if ("success".equals((String) loJSON.get("result"))) {
                                ShowMessageFX.Information(null, pxeModuleName, "Printed succesfully.");
                                CommonUtils.closeStage(btnClose);
                            }
                        } else {
                            handlePrintFailure(oTransPrint);
                        }
                    } catch (JRException ex) {
                        handlePrintFailure(oTransPrint);
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Print Aborted : " + (String) loJSON.get("message"));
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private void handlePrintFailure(Vehicle_Gatepass foValue) {
        JSONObject loJSON = new JSONObject();
        foValue.getMasterModel().getMasterModel().setPrinted(psOldPrintValue);
        loJSON = foValue.saveTransaction();
        if ("success".equals((String) loJSON.get("result"))) {
            ShowMessageFX.Error(null, pxeModuleName, "Print Aborted");
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

    @Override
    public boolean loadReport() {
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
            String lsItem = "";
            String lsDescID = "";
            String lsDesc = "";
            for (int lnCtr = 0; lnCtr <= oTransPrint.getVGPItemList().size() - 1; lnCtr++) {
                switch (String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getItemType())) {
                    case "l":
                        lsItem = "LABOR";
                        lsDescID = String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getItemCode());
                        lsDesc = String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getLaborDsc());
                        break;
                    case "p":
                        lsItem = "PARTS";
                        lsDescID = String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getItemCode());
                        lsDesc = String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getStockDsc());
                        break;
                    case "d":
                        lsDescID = String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getItemCode());
                        lsDesc = String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getDefltDsc());
                        break;
                }
                vgpData.add(new JobDone(
                        String.valueOf(lnCtr + 1),
                        lsItem,
                        lsDescID,
                        lsDesc,
                        String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getQuantity()), // Assuming 1 as a placeholder
                        "",
                        String.valueOf(oTransPrint.getVGPItemModel().getDetailModel(lnCtr).getReleased()),
                        ""
                ));
                lsItem = "";
                lsDescID = "";
                lsDesc = "";
            }
            JRBeanCollectionDataSource jobDoneData = new JRBeanCollectionDataSource(vgpData);
            System.out.println("vehicle: " + jobDoneData.getData());
            params.put(
                    "jobDoneData", jobDoneData);
            try {
                poJasperPrint = JasperFillManager.fillReport(oApp.getReportPath() + "VehicleGatePass.jasper", params, new JREmptyDataSource());
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
