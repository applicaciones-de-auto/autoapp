/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.Arrays;
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
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                    ObservableList<ModelItemEntryModelYear> selectedModelItemsYear = FXCollections.observableArrayList();
                    for (ModelItemEntryModelYear item : tblVModelList.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedModelItemsYear.add(item);
                        }
                    }

                    if (selectedModelItemsYear.isEmpty()) {
                        ShowMessageFX.Warning(null, pxeModuleName, "No item selected");
                        return;
                    }

                    int removeCount = 0;
                    //Inv Model Year
                    for (ModelItemEntryModelYear item : selectedModelItemsYear) {
                        String lsRow = item.getTblindexModel01();
//                        oTransInventoryModel.removeInvModel_Year(Integer.parseInt(lsRow));
                        removeCount++;
                    }
                    if (removeCount >= 1) {
                        ShowMessageFX.Information(null, pxeModuleName, "Removed Vehicle Model successfully.");
                    } else {
                        ShowMessageFX.Error(null, pxeModuleName, "Failed to removed vehicle model");
                    }
                } else {
                    return;
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
        JSONObject loJSON = new JSONObject();
        modelData.clear();
        loJSON = oTransInventoryModel.loadModel();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 1; lnCtr <= oTransInventoryModel.getInventoryModelYearList().size() - 1; lnCtr++) {
                modelData.add(new ModelItemEntryModelYear(
                        String.valueOf(lnCtr), // ROW
                        String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sMakeIDxx")),
                        String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sMakeDesc")),
                        String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sModelIDx")),
                        String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sModelDsc")),
                        String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "nYearModl")),
                        String.valueOf(lnCtr),
                        String.valueOf(oTransInventoryModel.getInventoryModelYear(lnCtr, "sModelCde"))
                ));

            }
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
