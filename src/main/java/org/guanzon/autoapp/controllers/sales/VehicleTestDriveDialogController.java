/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import org.guanzon.autoapp.models.sales.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.Inquiry;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleTestDriveDialogController implements Initializable {

    private Inquiry oTransTestVhcl;
    private GRider oApp;
    private String pxeModuleName = "Vehicle Test Drive Model";
    ObservableList<ModelInquiryTestDrive> testVhclModelData = FXCollections.observableArrayList();
    private String psTransNox;
    @FXML
    private Button btnAdd, btnClose;

    @FXML
    private TableColumn<ModelInquiryTestDrive, String> tblindex01, tblindex03;
    @FXML
    private TableColumn<ModelInquiryTestDrive, Boolean> tblindex02;
    @FXML
    private TableView<ModelInquiryTestDrive> tblViewInquiryVhcl;

    public void setTransNo(String fsValue) {
        psTransNox = fsValue;
    }

    public void setObject(Inquiry foValue) {
        oTransTestVhcl = foValue;
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        initVehicleTestModelTable();
        loadVhclTestModelTable();
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<ModelInquiryTestDrive> selectedItems = FXCollections.observableArrayList();
                for (ModelInquiryTestDrive item : tblViewInquiryVhcl.getItems()) {
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
                for (ModelInquiryTestDrive item : selectedItems) {
                    String lsModel = item.getTblindex02();
                    boolean isVhclExist = false;
                    try {
                        for (int lnCtr = 0; lnCtr <= oTransTestVhcl.getTestModelCount() - 1; lnCtr++) {
                            if (oTransTestVhcl.getTestModelDetail(lnCtr, "lsModel").toString().equals(lsModel)) {
                                ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add vehicle model, " + lsModel + " already exist.");
                                isVhclExist = true;
                                break;
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(VehicleTestDriveDialogController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (!isVhclExist) {
                        int fnRow;
                        try {
                            fnRow = oTransTestVhcl.getTestModelCount() - 1;
//                            oTransTestVhcl(fnRow, "sDescript", lsModel);
                        } catch (SQLException ex) {
                            Logger.getLogger(VehicleTestDriveDialogController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        addedCount++;
                    }
                }
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added vehicle successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add vehicle");
                }
                CommonUtils.closeStage(btnAdd);
                break;
        }
    }

    private void loadVhclTestModelTable() {
        /*Populate table*/
        testVhclModelData.clear();
        JSONObject loJSON = new JSONObject();
        loJSON = oTransTestVhcl.loadTestModel();
        if ("success".equals((String) loJSON.get("result"))) {
            try {
                for (int lnCtr = 0; lnCtr <= oTransTestVhcl.getTestModelCount(); lnCtr++) {
                    testVhclModelData.add(new ModelInquiryTestDrive(
                            String.valueOf(lnCtr + 1), //ROW
                            oTransTestVhcl.getTestModelDetail(lnCtr, lnCtr).toString()
                    ));
                }
                System.out.println("test count: " + oTransTestVhcl.getTestModelCount());
            } catch (SQLException ex) {
                Logger.getLogger(VehicleTestDriveDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            initVehicleTestModelTable();
        }
    }

    private void initVehicleTestModelTable() {
        tblViewInquiryVhcl.setItems(testVhclModelData);
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));  //Row
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblViewInquiryVhcl.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewInquiryVhcl.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }
}
