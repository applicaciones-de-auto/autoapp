/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.models.sales.VSPReservationInquirers;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VSPRemoveReservationInquiriesController implements Initializable {

    private GRider oApp;
    private String pxeModuleName = "VSP Remove Inquiry Reservations";
    private ObservableList<VSPReservationInquirers> reserveData = FXCollections.observableArrayList();
    private VehicleSalesProposal oTransReserve;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    @FXML
    private Button btnRemove, btnClose;
    @FXML
    private TableColumn<VSPReservationInquirers, Boolean> tblindex02;
    @FXML
    private TableColumn<VSPReservationInquirers, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06;
    @FXML
    private TableView<VSPReservationInquirers> tblViewReservation;
    @FXML
    private CheckBox selectAll;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTransReserve = foValue;
    }

    private Stage getStage() {
        return (Stage) tblViewReservation.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtonClick();
        initReservationTable();
        loadReservationTable();
        if (oTransReserve.getEditMode() == 1) {
            tblindex02.setVisible(false);
        }
    }

    private void initButtonClick() {
        List<Button> buttons = Arrays.asList(btnClose, btnRemove);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnRemove":
                ObservableList<VSPReservationInquirers> selectedItems = FXCollections.observableArrayList();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                    for (VSPReservationInquirers item : tblViewReservation.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedItems.add(item);
                        }
                    }
                    if (selectedItems.isEmpty()) {
                        ShowMessageFX.Warning(null, pxeModuleName, "No selected Items");
                        return;
                    }
                    for (VSPReservationInquirers item : selectedItems) {
                        int lnRow = Integer.parseInt(item.getTblindex01_reservation());
                        loJSON = oTransReserve.removeVSPReservation(lnRow - 1);
                        if ("error".equals((String) loJSON.get("result"))) {
                            ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        }
                    }
                }
                loadReservationTable();
                break;

            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Information(null, pxeModuleName, "Please inform admin for the unregistered " + lsButton + "button.");
                break;
        }
    }

    private void loadReservationTable() {
        reserveData.clear();
        String lsDate = "";
        String lsTransAmount = "";
        String lsTransNo = "";
        String lsTransID = "";
        for (int lnCtr = 0; lnCtr <= oTransReserve.getVSPReservationList().size() - 1; lnCtr++) {
            if (oTransReserve.getVSPReservationModel().getReservation(lnCtr).getSIDate() != null) {
                lsDate = CustomCommonUtil.xsDateShort((Date) oTransReserve.getVSPReservationModel().getReservation(lnCtr).getSIDate());
            }
            if (oTransReserve.getVSPReservationModel().getReservation(lnCtr).getTranAmt() != null) {
                lsTransAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransReserve.getVSPReservationModel().getReservation(lnCtr).getTranAmt())));
            }
            if (oTransReserve.getVSPReservationModel().getReservation(lnCtr).getTransNo() != null) {
                lsTransNo = oTransReserve.getVSPReservationModel().getReservation(lnCtr).getTransNo();
            }
            if (oTransReserve.getVSPReservationModel().getReservation(lnCtr).getTransID() != null) {
                lsTransID = oTransReserve.getVSPReservationModel().getReservation(lnCtr).getTransID();
            }

            reserveData.add(new VSPReservationInquirers(
                    String.valueOf(lnCtr + 1), // ROW
                    String.valueOf(oTransReserve.getVSPReservationModel().getReservation(lnCtr).getSINo()),
                    lsDate,
                    String.valueOf(oTransReserve.getVSPReservationModel().getReservation(lnCtr).getCompnyNm()),
                    lsTransNo,
                    lsTransAmount,
                    lsTransID
            ));
        }
        tblViewReservation.setItems(reserveData);
    }

    private void initReservationTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_reservation"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewReservation.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewReservation.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAll.setSelected(true);
                } else {
                    selectAll.setSelected(false);
                }
            });
        });
        selectAll.setOnAction(event -> {
            boolean newValue = selectAll.isSelected();
            if (!tblViewReservation.getItems().isEmpty()) {
                tblViewReservation.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02_reservation"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex03_reservation"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex04_reservation"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06_reservation"));
        tblViewReservation.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewReservation.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }
}
