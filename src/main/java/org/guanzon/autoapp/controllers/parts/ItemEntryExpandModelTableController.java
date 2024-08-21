/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.guanzon.auto.main.parts.InventoryInformation;
import org.guanzon.autoapp.models.parts.ModelItemEntryModelYear;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class ItemEntryExpandModelTableController implements Initializable {

    private GRider oApp;
    private InventoryInformation oTransInventoryModel;
    private final String pxeModuleName = "Item Expand Model"; //Form Title
    private ObservableList<ModelItemEntryModelYear> modelData = FXCollections.observableArrayList();
    @FXML
    private Button btnClose, btnRemove;
    @FXML
    private TableView<ModelItemEntryModelYear> tblVModelList;
    @FXML
    private CheckBox selectModelAll;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModel01;
    @FXML
    private TableColumn<ModelItemEntryModelYear, Boolean> tblindexModel02;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModel03;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModel04;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModel05;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) tblVModelList.getScene().getWindow();
    }

    public void setObject(InventoryInformation foValue) {
        oTransInventoryModel = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initModelYearTable();
        loadModelYearTable();
        initButtonClick();

    }

    private void initButtonClick() {
        List<Button> buttons = Arrays.asList(btnClose, btnRemove);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnRemove":
                ObservableList<ModelItemEntryModelYear> selectedItems = FXCollections.observableArrayList();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                    for (ModelItemEntryModelYear item : tblVModelList.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedItems.add(item);
                        }
                    }

                    // Maps to hold the model codes and their corresponding years
                    Map<String, List<Integer>> modelYearMap = new HashMap<>();

                    // Loop through selected items and populate the map
                    for (ModelItemEntryModelYear item : selectedItems) {
                        String lsModelCode = item.getTblindexModel08();
                        int lnYear = item.getTblindexModel06().isEmpty() ? 0 : Integer.parseInt(item.getTblindexModel06());

                        // Add the year to the corresponding model code in the map
                        modelYearMap.computeIfAbsent(lsModelCode, k -> new ArrayList<>()).add(lnYear);
                    }

                    // Debug: Print out the modelYearMap to ensure it contains the correct data
                    for (Map.Entry<String, List<Integer>> entry : modelYearMap.entrySet()) {
                        System.out.println("Model Code: " + entry.getKey() + ", Years: " + entry.getValue());
                    }

                    // Now loop through the map and remove the models
                    int removeCount = 0;
                    for (Map.Entry<String, List<Integer>> entry : modelYearMap.entrySet()) {
                        String modelCode = entry.getKey();
                        Integer[] lnModelYears = entry.getValue().toArray(new Integer[0]);

                        // Debug: Check the content of lnModelYears before passing it to removeInvModel_Year
                        System.out.println("Removing Model Code: " + modelCode + " with Years: " + Arrays.toString(lnModelYears));

                        try {
                            // Call the removeModel method
                            oTransInventoryModel.removeInvModel_Year(modelCode, lnModelYears);
                            removeCount++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.err.println("Error removing model: " + modelCode + ", Years: " + Arrays.toString(lnModelYears));
                            e.printStackTrace();
                        }
                    }
                    if (removeCount >= 1) {
                        ShowMessageFX.Information(null, pxeModuleName, "Removed Model Successfully");
                        selectModelAll.setSelected(false);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Failed to remove model.");
                        return;
                    }
                    // Reload the table after removal
                }
                loadModelYearTable();
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Information(null, pxeModuleName, "Please inform admin for the unregistered " + lsButton + "button.");
                break;
        }
    }

    private void loadModelYearTable() {
        modelData.clear();
        String lsYearModl = "";
        for (int lnCtr = 0; lnCtr <= oTransInventoryModel.getInventoryModelYearList().size() - 1; lnCtr++) {
            if (!oTransInventoryModel.getInventoryModelYear(lnCtr, "nYearModl").equals(0)) {
                lsYearModl = String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "nYearModl"));
            }
            modelData.add(new ModelItemEntryModelYear(
                    String.valueOf(lnCtr + 1), // ROW
                    "",
                    String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sMakeDesc")),
                    "",
                    String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sModelDsc")),
                    lsYearModl,
                    String.valueOf(lnCtr),
                    String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sModelCde"))
            ));
            lsYearModl = "";
        }
        tblVModelList.setItems(modelData);
    }

    private void initModelYearTable() {
        tblindexModel01.setCellValueFactory(new PropertyValueFactory<>("tblindexModel01"));
        tblindexModel02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblVModelList.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblVModelList.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectModelAll.setSelected(true);
                } else {
                    selectModelAll.setSelected(false);
                }
            });
        });
        selectModelAll.setOnAction(event -> {
            boolean newValue = selectModelAll.isSelected();
            if (!tblVModelList.getItems().isEmpty()) {
                tblVModelList.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblindexModel03.setCellValueFactory(new PropertyValueFactory<>("tblindexModel03"));
        tblindexModel04.setCellValueFactory(new PropertyValueFactory<>("tblindexModel05"));
        tblindexModel05.setCellValueFactory(new PropertyValueFactory<>("tblindexModel06"));
        tblVModelList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVModelList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }
}
