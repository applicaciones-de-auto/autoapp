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
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.models.sales.Part;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class JOVSPAccessoriesController implements Initializable, ScreenInterface {

    private VehicleSalesProposal oTransAccessories;
    private GRider oApp;
    private ObservableList<Part> accessoriesData = FXCollections.observableArrayList();
    private final String pxeModuleName = "Job Order VSP Part";
    private String psTrans = "";
    private int pnRow = 0;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableColumn<Part, String> tblindex01_part, tblindex03_part, tblindex04_part, tblindex05_part, tblindex06_part,
            tblindex07_part, tblindex08_part, tblindex09_part, tblindex10_part, tblindex11_part;
    @FXML
    private TableColumn<Part, Boolean> tblindex02_part;
    @FXML
    private CheckBox selectAll;

    public void setObject(VehicleSalesProposal foValue) {
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
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        initAccessoriesTable();
        loadAccessoriesTable();
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<Part> selectedItems = FXCollections.observableArrayList();
                for (Part item : tblViewAccessories.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                    return;
                }
                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                    return;
                }
                int addedCount = 0;
                for (Part item : selectedItems) {
                    int lsRow = Integer.parseInt(item.getTblindex01_part());// Assuming there is a method to retrieve the transaction number
                    String lsPartID = item.getTblindex03_part();
                    String lsBarCde = item.getTblindex09_part();
                    String lsDesc = item.getTblindex04_part();

                    boolean isPartExist = false;
                    for (int lnCtr = 0; lnCtr <= oTransAccessories.getVSPPartsList().size() - 1; lnCtr++) {
                        if (oTransAccessories.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc().equals(lsDesc)) {
                            ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add accessories, " + lsDesc + " already exist.");
                            isPartExist = true;
                            break;
                        }
                    }
                    if (!isPartExist) {
                        oTransAccessories.addVSPParts();
//                        int fnRow = oTransPart.getActVehicleList().size() - 1;
//                        oTransPart.setActVehicle(fnRow, "sSerialID", lsSerialID);
//                        oTransPart.setActVehicle(fnRow, "sDescript", lsDescript);
//                        oTransPart.setActVehicle(fnRow, "sCSNoxxxx", lsCSNoxxxx);
//                        addedCount++;
                    }
                }
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added accessories successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add accessories");
                }
                CommonUtils.closeStage(btnAdd);
                break;
        }
    }

    private void loadAccessoriesTable() {
        accessoriesData.clear();
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsQuantity = "";
        String lsDiscAmount = "";
        String lsTotalAmount = "";
        String lsNetAmount = "";
        String lsPartsDesc = "";
        String lsBarCode = "";
        String lsJoNoxx = "";
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
                    "",
                    lsJoNoxx,
                    lsPartsDesc,
                    lsTotalAmount,
                    lbChargeType
            ));
            lbChargeType = false;
            lsGrsAmount = "";
            lsQuantity = "";
            lsDiscAmount = "";
            lsTotalAmount = "";
            lsNetAmount = "";
            lsPartsDesc = "";
            lsBarCode = "";
            lsJoNoxx = "";
        }
        tblViewAccessories.setItems(accessoriesData);
    }

    private void initAccessoriesTable() {
        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex07_part.setCellValueFactory(new PropertyValueFactory<>("tblindex13_part"));
        tblindex08_part.setCellValueFactory(new PropertyValueFactory<>("tblindex07_part"));
        tblindex09_part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_part"));
        tblindex10_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex11_part.setCellValueFactory(new PropertyValueFactory<>("tblindex11_part"));

        tblViewAccessories.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }
}
