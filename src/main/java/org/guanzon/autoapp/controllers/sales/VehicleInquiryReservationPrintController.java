package org.guanzon.autoapp.controllers.sales;

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
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.interfaces.GPrintInterface;
import org.guanzon.autoapp.models.sales.InquiryVehicleSalesAdvances;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleInquiryReservationPrintController implements Initializable, ScreenInterface, GPrintInterface {

    private Inquiry oTransPrint;
    private GRider oApp;
    private JasperPrint poJasperPrint; //Jasper Libraries
    private JRViewer poJrViewer;
    private final String pxeModuleName = "Vehicle Inquiry Reservation Print";
    private Integer[] pnRows;
    private boolean running = false;
    private ObservableList<InquiryVehicleSalesAdvances> vhlApprovalPrintData = FXCollections.observableArrayList();
    private Timeline timeline;
    private Integer timeSeconds = 3;
    private int pnCtr = 0;
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
                for (pnCtr = 0; pnCtr <= pnRows.length - 1; pnCtr++) {
                    Integer lnCtr = pnRows[pnCtr];
                    loJSON = oTransPrint.saveReservationPrint(lnCtr, true);
                }
                if (!"error".equals((String) loJSON.get("result"))) {
                    try {
                        if (JasperPrintManager.printReport(poJasperPrint, true)) {
                            for (pnCtr = 0; pnCtr <= pnRows.length - 1; pnCtr++) {
                                Integer lnCtr = pnRows[pnCtr];
                                loJSON = oTransPrint.saveReservationPrint(lnCtr, false);
                            }
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

    private void handlePrintFailure(Inquiry foValue) {
        JSONObject loJSON = new JSONObject();
        String psOldPrint = "";
        for (pnCtr = 0; pnCtr <= pnRows.length - 1; pnCtr++) {
            Integer lnCtr = pnRows[pnCtr];
            psOldPrint = String.valueOf(foValue.getReservationModel().getDetailModel(lnCtr).getPrinted());
            foValue.getReservationModel().getDetailModel(lnCtr).setPrinted(Integer.parseInt(psOldPrint));
        }
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
            fsValue = CustomCommonUtil.xsDateShort((Date) oTransPrint.getReservation(fnRow, fsCol));
        }
        return fsValue;
    }

    @Override
    public boolean loadReport() {
        JSONObject loJSON = new JSONObject();
        Map<String, Object> params = new HashMap<>();
        params.put("sCompnyNm", "Guanzon Group of Companies");
        params.put("sBranchNm", oApp.getBranchName());
        params.put("sAddressx", oApp.getAddress());
        String lsPlateCSNo = "";
        String lsDescript = "";
        if (oTransPrint.getMasterModel().getMasterModel().getPlateNo() != null) {
            lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getPlateNo();
        } else {
            if (oTransPrint.getMasterModel().getMasterModel().getCSNo() != null) {
                lsPlateCSNo = oTransPrint.getMasterModel().getMasterModel().getCSNo();
            }
        }
        if (oTransPrint.getMasterModel().getMasterModel().getDescript() != null) {
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
                if (oTransPrint.getReservation(lnCtr, "cTranStat") != null) {
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
                vhlApprovalPrintData.add(new InquiryVehicleSalesAdvances(
                        String.valueOf(lnCtr),
                        getValueDateReport(lnCtr, "lsSlipDate", "dTransact"),
                        lsResType,
                        getValueReport(lnCtr, "lsRefNox", "sReferNox"),
                        formattedAmount,
                        lsInqStat,
                        getValueReport(lnCtr, "lsRemarks", "sRemarksx"),
                        "",
                        getValueDateReport(lnCtr, "lsApprovDate", "dApprovex"),
                        getValueReport(lnCtr, "lsRefNo", "sReferNox"),
                        getValueReport(lnCtr, "lsCompanyName", "sCompnyNm"),
                        "",
                        ""));

            }
        }
        String printFileName = null;
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(vhlApprovalPrintData);
        try {

            poJasperPrint = JasperFillManager.fillReport(oApp.getReportPath() + "InquiryReservation.jasper", params, beanColDataSource);
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
