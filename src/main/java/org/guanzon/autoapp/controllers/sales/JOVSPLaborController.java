/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class JOVSPLaborController implements Initializable, ScreenInterface {

    private JobOrder oTransLabor;
    private GRider oApp;
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private final String pxeModuleName = "Job Order VSP Labor";
    private String psTrans = "";
    private int pnRow = 0;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<Labor> tblViewLabor;
    @FXML
    private TableColumn<Labor, String> tblindex01_labor, tblindex03_labor, tblindex04_labor, tblindex05_labor,
            tblindex06_labor, tblindex07_labor, tblindex08_labor, tblindex09_labor;
    @FXML
    private TableColumn<Labor, Boolean> tblindex02_labor;
    @FXML
    private CheckBox selectAll;

    public void setObject(JobOrder foValue) {
        oTransLabor = foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setTrans(String fsValue) {
        psTrans = fsValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        initLaborTable();
        loadLaborTable();
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<Labor> selectedItems = FXCollections.observableArrayList();

                // Collect selected items from the table
                for (Labor item : tblViewLabor.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }

                // Check if no items are selected
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                    return;
                }

                // Confirmation before adding labor
                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                    return;
                }

                int addedCount = 0;

                // Iterate through selected items
                for (Labor item : selectedItems) {
                    String lsLaborID = item.getTblindex03_labor();
                    String lsLaborDesc = item.getTblindex07_labor();
                    String lsAmnt = item.getTblindex04_labor();
                    String lsChargeType = item.getTblindex08_labor();
                    String lsJO = item.getTblindex10_labor();

                    // Skip adding if labor already has a job order
                    if (!lsJO.isEmpty()) {
                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add labor, " + lsLaborDesc + " already has a job order.");
                        continue; // Skip this item and move to the next
                    }

                    // Check if the labor description already exists
                    boolean isLaborExist = false;
                    for (int lnCtr = 0; lnCtr <= oTransLabor.getJOLaborList().size() - 1; lnCtr++) {
                        if (oTransLabor.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc().equals(lsLaborDesc)) {
                            ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add labor, " + lsLaborDesc + " already exists.");
                            isLaborExist = true;
                            break;
                        }
                    }

                    // Add labor if it doesn't already exist
                    if (!isLaborExist) {
                        oTransLabor.addJOLabor();
                        int lnRow = oTransLabor.getJOLaborList().size() - 1;
                        oTransLabor.getJOLaborModel().getDetailModel(lnRow).setLaborCde(lsLaborID);
                        oTransLabor.getJOLaborModel().getDetailModel(lnRow).setLaborDsc(lsLaborDesc);
                        oTransLabor.getJOLaborModel().getDetailModel(lnRow).setUnitPrce(new BigDecimal(lsAmnt.replace(",", "")));
                        oTransLabor.getJOLaborModel().getDetailModel(lnRow).setPayChrge(lsChargeType);
                        addedCount++;
                    }
                }

                // Show result messages based on the number of added items
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added labor successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add labor");
                }
                CommonUtils.closeStage(btnAdd);
                break;

        }
    }

    private void loadLaborTable() {
        laborData.clear();
        boolean lbAdditional = false;
        boolean lbChargeType = false;
        String lsChargeType = "";
        String lsGrsAmount = "";
        String lsDiscAmount = "";
        String lsNetAmount = "";
        String lsJoNoxx = "";
        JSONObject loJSON = new JSONObject();
        loJSON = oTransLabor.loadVSPLabor();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTransLabor.getVSPLaborList().size() - 1; lnCtr++) {
                if (oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt() != null) {
                    lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt())));
                }
                if (oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount() != null) {
                    lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount())));
                }
                if (oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt() != null) {
                    lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt())));
                }
                if (oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getChrgeTyp().equals("0")) {
                    lbChargeType = true;
                    lsChargeType = "0";
                } else {
                    lsChargeType = "1";
                }
                if (oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getAddtl().equals("1")) {
                    lbAdditional = true;
                }
                if (oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo() != null) {
                    lsJoNoxx = oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo();
                }
                laborData.add(new Labor(
                        String.valueOf(lnCtr + 1),
                        String.valueOf(oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getTransNo()),
                        String.valueOf(oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getLaborCde()),
                        lsGrsAmount,
                        lsDiscAmount,
                        lsNetAmount,
                        String.valueOf(oTransLabor.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDsc()),
                        "",
                        "",
                        "",
                        "",
                        lsChargeType,
                        "",
                        lsJoNoxx,
                        lbAdditional,
                        lbChargeType
                ));
                lbAdditional = false;
                lbChargeType = false;
                lsGrsAmount = "";
                lsDiscAmount = "";
                lsNetAmount = "";
                lsJoNoxx = "";
                lsChargeType = "";
            }
        }
        tblViewLabor.setItems(laborData);
    }

    private void initLaborTable() {
        tblindex01_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02_labor.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex03_labor"));
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_labor"));
        tblindex06_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_labor"));
        tblindex07_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex06_labor"));
        tblindex08_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex09_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex10_labor"));
        tblViewLabor.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewLabor.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAll.setSelected(true);
                } else {
                    selectAll.setSelected(false);
                }
            });
        });
        selectAll.setOnAction(event -> {
            boolean newValue = selectAll.isSelected();
            tblViewLabor.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }
}
