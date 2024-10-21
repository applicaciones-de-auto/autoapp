package org.guanzon.autoapp.controllers.sales;

import java.awt.Component;
import java.net.URL;
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
import org.guanzon.auto.main.sales.VehicleDeliveryReceipt;
import org.guanzon.autoapp.interfaces.GPrintInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VDRPrintController implements Initializable, ScreenInterface, GPrintInterface {

    private VehicleDeliveryReceipt oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint;
    private JRViewer poJrViewer;
    private final String pxeModuleName = "Vehicle Delivery Receipt Print";
    private boolean running = false;
    private String psTransNox = "";
    Map<String, Object> params = new HashMap<>();
    private Timeline timeline;
    private Integer timeSeconds = 3;
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

    public void setObject(VehicleDeliveryReceipt foValue) {
        oTransPrint = foValue;
    }

    @Override
    public void setTransNo(String fsValue) {
        psTransNox = fsValue;
    }

    /**
     * /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbProgress.setVisible(true);
        btnPrint.setVisible(false);
        btnPrint.setDisable(true);
        timeline = new Timeline();
        initButtonsClick();
        generateReport();
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
                try {
                if (JasperPrintManager.printReport(poJasperPrint, true)) {
                    oTransPrint.getMasterModel().getMasterModel().setPrinted("1");
                    loJSON = oTransPrint.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, pxeModuleName, "Printed Successfully");
                        CommonUtils.closeStage(btnClose);
                    }
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
            params.put("branchName", oApp.getBranchName());
            params.put("branchAddress", oApp.getAddress());
            params.put("vdrNo", getValueReport("vdrNo", "sReferNox"));
            params.put("vdrDate", getValueDateReport("vdrDate", "dTransact"));
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
                params.put("terms", lsPaymentMethod);
            }
            String lsBuyColNm = "";
            if (oTransPrint.getMasterModel().getMasterModel().getBuyCltNm() != null && oTransPrint.getMasterModel().getMasterModel().getCoCltNm() != null) {
                lsBuyColNm = oTransPrint.getMasterModel().getMasterModel().getBuyCltNm() + " / " + oTransPrint.getMasterModel().getMasterModel().getCoCltNm();
            } else if (oTransPrint.getMasterModel().getMasterModel().getBuyCltNm() != null) {
                lsBuyColNm = oTransPrint.getMasterModel().getMasterModel().getBuyCltNm();
            }
            params.put("buycoBuyer", lsBuyColNm);
            params.put("address", getValueReport("address", "sAddressx"));
            params.put("quantity", "One(1)");
            params.put("vehicleDesc", getValueReport("vehicleDesc", "sVhclDesc"));
            params.put("remarks", getValueReport("remarks", "sRemarksx"));
            String lsPlateCSNo = "";
            if (oTransPrint.getMasterModel().getMasterModel().getCSNo() != null && oTransPrint.getMasterModel().getMasterModel().getPlateNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo() + " / " + oTransPrint.getMasterModel().getMasterModel().getPlateNo();
            } else if (oTransPrint.getMasterModel().getMasterModel().getCSNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo();
            }
            params.put("csPlateNo", lsPlateCSNo);
            params.put("frameNo", getValueReport("frameNo", "sFrameNox"));
            params.put("engineNo", getValueReport("engineNo", "sEngineNo"));
            params.put("color", getValueReport("color", "sColorDsc"));
            String sourceFileName = "D://GGC_Maven_Systems/reports/autoapp/vdr.jasper";
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
