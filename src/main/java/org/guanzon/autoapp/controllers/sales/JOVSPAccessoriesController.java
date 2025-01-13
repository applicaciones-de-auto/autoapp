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
import org.guanzon.autoapp.models.sales.Part;
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
public class JOVSPAccessoriesController implements Initializable, ScreenInterface {

    private JobOrder oTransAccessories;
    private GRider oApp;
    private ObservableList<Part> accessoriesData = FXCollections.observableArrayList();
    private final String pxeModuleName = "Job Order VSP Part";
    private String psTrans = "";
    private int pnRow = 0;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableColumn<Part, String> tblindex01_part, tblindex03_part, tblindex04_part, tblindex05_part, tblindex06_part,
            tblindex07_part, tblindex08_part, tblindex09_part, tblindex10_part, tblindex11_part, tblindex12_part;
    @FXML
    private TableColumn<Part, Boolean> tblindex02_part;
    @FXML
    private CheckBox selectAll;

    public void setObject(JobOrder foValue) {
        oTransAccessories = foValue;
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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        initAccessoriesTable();
        loadAccessoriesTable();
        initButtonsClick();
    }

    private void loadAccessoriesTable() {
        accessoriesData.clear();
        boolean lbChargeType = false;
        String lsChargeType = "";
        String lsGrsAmount = "";
        String lsQuantity = "";
        String lsDiscAmount = "";
        String lsTotalAmount = "";
        String lsNetAmount = "";
        String lsPartsDesc = "";
        String lsBarCode = "";
        String lsJoNoxx = "";
        JSONObject loJSON = new JSONObject();
        loJSON = oTransAccessories.loadVSPParts();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTransAccessories.getVSPPartsList().size() - 1; lnCtr++) {
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice() != null) {
                    lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice())));
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                    lsQuantity = String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null && oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                    BigDecimal lsGrsAmt = new BigDecimal(String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice()));
                    int lsQuan = Integer.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
                    lsTotalAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lsGrsAmt.doubleValue() * lsQuan)));
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount() != null) {
                    lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount())));
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt() != null) {
                    lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt())));
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getChrgeTyp().equals("0")) {
                    lbChargeType = true;
                    lsChargeType = "0";
                } else {
                    lsChargeType = "1";
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc() != null) {
                    lsPartsDesc = String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc());
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getBarCode() != null) {
                    lsBarCode = String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getBarCode());
                }
                if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getDSNo() != null) {
                    lsJoNoxx = oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getDSNo();
                }
                accessoriesData.add(new Part(
                        String.valueOf(lnCtr + 1),
                        String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getTransNo()),
                        String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getStockID()),
                        String.valueOf(oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getDescript()),
                        lsQuantity,
                        lsGrsAmount,
                        lsDiscAmount,
                        lsNetAmount,
                        lsBarCode,
                        lsChargeType,
                        lsJoNoxx,
                        lsPartsDesc,
                        lsTotalAmount,
                        lbChargeType
                ));
                lbChargeType = false;
                lsChargeType = "";
                lsGrsAmount = "";
                lsQuantity = "";
                lsDiscAmount = "";
                lsTotalAmount = "";
                lsNetAmount = "";
                lsPartsDesc = "";
                lsBarCode = "";
                lsJoNoxx = "";
            }
        }
        tblViewAccessories.setItems(accessoriesData);
    }

    private void initAccessoriesTable() {
        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex12_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex07_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex08_part.setCellValueFactory(new PropertyValueFactory<>("tblindex13_part"));
        tblindex09_part.setCellValueFactory(new PropertyValueFactory<>("tblindex07_part"));
        tblindex10_part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_part"));
        tblindex11_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex12_part.setCellValueFactory(new PropertyValueFactory<>("tblindex11_part"));

        tblViewAccessories.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewAccessories.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAll.setSelected(true);
                } else {
                    selectAll.setSelected(false);
                }
            });
        });
        selectAll.setOnAction(event -> {
            boolean newValue = selectAll.isSelected();
            tblViewAccessories.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblViewAccessories.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void initButtonsClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<Part> selectedItems = FXCollections.observableArrayList();

                // Collect selected items from the table
                for (Part item : tblViewAccessories.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }

                // Check if no items are selected
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                    return;
                }

                // Confirmation before adding accessories
                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                    return;
                }

                int addedCount = 0;

                // Iterate through selected items
                for (Part item : selectedItems) {
                    String lsStockID = item.getTblindex03_part();
                    String lsAccDesc = item.getTblindex04_part();
                    String lsBarCde = item.getTblindex09_part();
                    String lsAmnt = item.getTblindex06_part();
                    String lsQuan = item.getTblindex05_part();
                    String lsChrgTyp = item.getTblindex10_part();
                    String lsJO = item.getTblindex11_part();

                    // Skip if Stock ID is missing
                    if (lsStockID.equals("")) {
                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add accessories, " + lsBarCde + " has no Stock ID available.");
                        continue; // Skip to the next selected item
                    }
                    oTransAccessories.addJOParts();
                    int lnRow = oTransAccessories.getJOPartsList().size() - 1;
                    loJSON = oTransAccessories.checkVSPJOParts(lsStockID, 0, lnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        oTransAccessories.getJOPartsModel().getDetailModel(lnRow).setStockID(lsStockID);
                        oTransAccessories.getJOPartsModel().getDetailModel(lnRow).setBarCode(lsBarCde);
                        oTransAccessories.getJOPartsModel().getDetailModel(lnRow).setDescript(lsAccDesc);
                        oTransAccessories.getJOPartsModel().getDetailModel(lnRow).setPayChrge(lsChrgTyp);
//                        oTransAccessories.getJOPartsModel().getDetailModel(lnRow).setQtyEstmt(Integer.valueOf(lsQuan));
                        oTransAccessories.getJOPartsModel().getDetailModel(lnRow).setUnitPrce(new BigDecimal(lsAmnt.replace(",", "")));
                        addedCount++;
                    } else {
                        oTransAccessories.removeJOParts(lnRow);
                        ShowMessageFX.Error(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                // Show result messages based on the number of added items
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added accessories successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add accessories");
                }

                CommonUtils.closeStage(btnAdd);
                break;

        }
    }

}
