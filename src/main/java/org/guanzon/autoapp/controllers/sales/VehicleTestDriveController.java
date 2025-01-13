/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import org.guanzon.autoapp.models.sales.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.Inquiry;
import org.json.simple.JSONObject;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleTestDriveController implements Initializable {

    private Inquiry oTransTestVhcl;
    private GRider oApp;
    private String pxeModuleName = "Vehicle Test Drive Model";
    ObservableList<InquiryTestDrive> testVhclModelData = FXCollections.observableArrayList();
    private List<String> existingModelDescriptions = new ArrayList<>();
    private Set<String> selectedModelIDs = new HashSet<>();
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableColumn<InquiryTestDrive, String> tblindex01, tblindex03;
    @FXML
    private TableColumn<InquiryTestDrive, Boolean> tblindex02;
    @FXML
    private TableView<InquiryTestDrive> tblViewInquiryVhcl;
    @FXML
    private TableColumn<InquiryTestDrive, String> tblindex04;

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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        loadVhclTestModelTable();
        initVehicleTestModelTable();
        initButtonsClick();

    }

    private void loadVhclTestModelTable() {
        testVhclModelData.clear();

        String existingModels = oTransTestVhcl.getMasterModel().getMasterModel().getTestModl();
        existingModelDescriptions = Arrays.stream(existingModels.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        JSONObject loJSON = oTransTestVhcl.loadTestModel();
        if (!"error".equals(loJSON.get("result"))) {
            try {
                for (int lnCtr = 1; lnCtr <= oTransTestVhcl.getTestModelCount(); lnCtr++) {
                    // Retrieve data for each model
                    String makeID = oTransTestVhcl.getTestModelDetail(lnCtr, "sMakeIDxx").toString();
                    String makeDesc = oTransTestVhcl.getTestModelDetail(lnCtr, "sMakeDesc").toString();
                    String modelID = oTransTestVhcl.getTestModelDetail(lnCtr, "sModelIDx").toString();
                    String modelDesc = oTransTestVhcl.getTestModelDetail(lnCtr, "sModelDsc").toString();

                    InquiryTestDrive item = new InquiryTestDrive(
                            String.valueOf(lnCtr),
                            makeID,
                            makeDesc,
                            modelID,
                            modelDesc
                    );

                    if (existingModelDescriptions.contains(modelDesc)) {
                        item.getSelect().setSelected(true);
                    }

                    testVhclModelData.add(item);
                }
            } catch (SQLException ex) {
                Logger.getLogger(VehicleTestDriveController.class.getName()).log(Level.SEVERE, null, ex);
            }
            initVehicleTestModelTable();
        }
    }

    private void initVehicleTestModelTable() {
        tblViewInquiryVhcl.setItems(testVhclModelData);
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblViewInquiryVhcl.setRowFactory(tv -> {
            TableRow<InquiryTestDrive> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    InquiryTestDrive rowData = row.getItem();
                    String modelDesc = rowData.getTblindex05();

                    if (rowData.getSelect().isSelected()) {
                        existingModelDescriptions.add(modelDesc);
                    } else {
                        existingModelDescriptions.remove(modelDesc);
                    }
                }
            });
            return row;
        });
        tblViewInquiryVhcl.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewInquiryVhcl.lookup("TableHeaderRow");
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
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<InquiryTestDrive> selectedItems = FXCollections.observableArrayList();
                // Collect selected items from the TableView
                for (InquiryTestDrive item : tblViewInquiryVhcl.getItems()) {
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
                StringBuilder fsModels = new StringBuilder();
                for (InquiryTestDrive item : selectedItems) {
                    try {
                        String lsModelDesc = item.getTblindex05();

                        if (fsModels.indexOf(lsModelDesc) == -1) {
                            if (fsModels.length() > 0) {
                                fsModels.append(", ");
                            }
                            fsModels.append(lsModelDesc);
                        }
                        addedCount++;
                    } catch (Exception ex) {
                        Logger.getLogger(VehicleTestDriveController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                oTransTestVhcl.getMasterModel().getMasterModel().setTestModl(fsModels.toString());
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added test vehicle(s) successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add vehicle(s).");
                }
                CommonUtils.closeStage(btnAdd);
                break;

        }
    }

}
