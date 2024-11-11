package org.guanzon.autoapp.controllers.cashiering;

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
import org.guanzon.auto.main.cashiering.VehicleSalesInvoice;
import org.guanzon.autoapp.interfaces.GPrintInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VSIPrintController implements Initializable, ScreenInterface, GPrintInterface {

    private VehicleSalesInvoice oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "VSI Print";
    private String psTransNox;
    private boolean running = false;
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

    public void setVSIObject(VehicleSalesInvoice foValue) {
        oTransPrint = foValue;
    }

    public void setTransNo(String fsValue) {
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

    private static String getValueNumberReport(String fsAmountString) {
        DecimalFormat loNumFormat = new DecimalFormat("#,##0.00");
        String lsFormattedAmount = "";
        if (fsAmountString.equals("0.00") || fsAmountString.isEmpty()) {
            lsFormattedAmount = "";
        } else {
            double amount = Double.parseDouble(fsAmountString);
            lsFormattedAmount = loNumFormat.format(amount);
        }
        return lsFormattedAmount;
    }

    @Override
    public boolean loadReport() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTransPrint.openTransaction(psTransNox);
        if ("success".equals((String) loJSON.get("result"))) {
            params.put("soldTo", getValueReport("soldTo", "sBuyCltNm"));
            params.put("tinNo", getValueReport("tinNo", "sTaxIDNox"));
            params.put("address", getValueReport("address", "sAddressx"));
            params.put("transDate", getValueDateReport("transDate", "dTransact"));
            params.put("address", getValueReport("address", "sAddressx"));
            params.put("quantity", "One(1)");
            if (oTransPrint.getVSISourceList().size() > 0) {
                String vsiNo = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getReferNo() != null && oTransPrint.getVSISourceModel().getDetailModel().getUDRNo() != null) {
                    vsiNo = "SI # " + getValueReport("vsiNo", "sReferNox") + " / " + " DR # " + oTransPrint.getVSISourceModel().getDetailModel().getUDRNo();
                }
                params.put("vsiNo", vsiNo);
                String lsPaymentMethod = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getPayMode() != null) {
                    switch (oTransPrint.getVSISourceModel().getDetailModel().getPayMode()) {
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
                }
                params.put("terms", lsPaymentMethod);
                String lsBankName = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getPayMode() != null && !oTransPrint.getVSISourceModel().getDetailModel().getPayMode().equals("0")) {
                    if (oTransPrint.getVSISourceModel().getDetailModel().getBankname() != null) {
                        lsBankName = oTransPrint.getVSISourceModel().getDetailModel().getBankname();
                    }
                }
                params.put("bankName", lsBankName);
                String salesAgent = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getSEName() != null) {
                    salesAgent = oTransPrint.getVSISourceModel().getDetailModel().getSEName();
                }
                params.put("salesAgent", CustomCommonUtil.formatName(salesAgent).toUpperCase());
                if (oTransPrint.getVSISourceModel().getDetailModel().getUnitPrce() != null) {
                    params.put("vehiclePrice", getValueNumberReport(String.valueOf(oTransPrint.getVSISourceModel().getDetailModel().getUnitPrce())));
                }
                String lsPlateCSNo = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getPlateNo() != null) {
                    lsPlateCSNo = oTransPrint.getVSISourceModel().getDetailModel().getCSNo() + " / " + oTransPrint.getVSISourceModel().getDetailModel().getPlateNo();
                } else if (oTransPrint.getVSISourceModel().getDetailModel().getCSNo() != null) {
                    lsPlateCSNo = oTransPrint.getVSISourceModel().getDetailModel().getCSNo();
                }
                params.put("plateCSNo", lsPlateCSNo);
                String lsEngineNo = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getEngineNo() != null) {
                    lsEngineNo = oTransPrint.getVSISourceModel().getDetailModel().getEngineNo();
                }
                params.put("engineNo", lsEngineNo);
                String lsFrameNo = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getFrameNo() != null) {
                    lsFrameNo = oTransPrint.getVSISourceModel().getDetailModel().getFrameNo();
                }
                params.put("frameNo", lsFrameNo);
                String lsColor = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getColorDsc() != null) {
                    lsColor = oTransPrint.getVSISourceModel().getDetailModel().getColorDsc();
                }
                params.put("color", lsColor);
                String lsVchlDesc = "";
                if (oTransPrint.getVSISourceModel().getDetailModel().getVhclFDsc() != null) {
                    lsVchlDesc = oTransPrint.getVSISourceModel().getDetailModel().getVhclFDsc();
                }
                params.put("descript", lsVchlDesc);
                if (oTransPrint.getVSISourceModel().getDetailModel().getPromoDsc() != null) {
                    params.put("promoDisc", getValueNumberReport(String.valueOf(oTransPrint.getVSISourceModel().getDetailModel().getPromoDsc())));
                }
                if (oTransPrint.getVSISourceModel().getDetailModel().getFleetDsc() != null) {
                    params.put("fleetDisc", getValueNumberReport(String.valueOf(oTransPrint.getVSISourceModel().getDetailModel().getFleetDsc())));
                }
                if (oTransPrint.getVSISourceModel().getDetailModel().getAddlDsc() != null) {
                    params.put("cashDisc", getValueNumberReport(String.valueOf(oTransPrint.getVSISourceModel().getDetailModel().getAddlDsc())));
                }
                if (oTransPrint.getVSISourceModel().getDetailModel().getTranAmt() != null) {
                    params.put("totalAmount", getValueNumberReport(String.valueOf(oTransPrint.getVSISourceModel().getDetailModel().getTranAmt())));
                }
            }

            params.put("totalAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTranTotl())));
            params.put("vatableSales", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getVatSales())));
            params.put("zeroVatSales", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getZroVATSl())));
            params.put("vatAmount", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getVatAmt())));
            params.put("addVAT", "0.00");
            params.put("vatExempSales", "0.00");
            params.put("totalAmntDue", getValueNumberReport(String.valueOf(oTransPrint.getMasterModel().getMasterModel().getTranTotl())));
            try {
                poJasperPrint = JasperFillManager.fillReport(oApp.getReportPath() + "VehicleSalesInvoice.jasper", params, new JREmptyDataSource());
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
