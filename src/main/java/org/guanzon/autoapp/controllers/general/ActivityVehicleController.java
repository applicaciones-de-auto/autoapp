/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
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
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.autoapp.models.general.ActivityVehicle;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityVehicleController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Activity Vehicle";
    private Activity oTransActVehicle;
    ObservableList<ActivityVehicle> actVhclModelData = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<ActivityVehicle, String> tblindexVehicle01;
    @FXML
    private TableColumn<ActivityVehicle, Boolean> tblindexVehicle02;
    @FXML
    private TableColumn<ActivityVehicle, String> tblindexVehicle03;
    @FXML
    private TableView<ActivityVehicle> tblViewActVhcl;
    @FXML
    private TableColumn<ActivityVehicle, String> tblindexVehicle04;

    public void setObject(Activity foValue) {
        oTransActVehicle = foValue;
    }

    @Override
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
        initVehicleTable();
        loadActVhclModelTable();

    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<ActivityVehicle> selectedItems = FXCollections.observableArrayList();
                for (ActivityVehicle item : tblViewActVhcl.getItems()) {
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
                for (ActivityVehicle item : selectedItems) {
                    String lsSerialID = item.getTblindex02();
                    String lsDescript = item.getTblindex04();
                    String lsCSNoxxxx = item.getTblindex03();// Assuming there is a method to retrieve the transaction number

                    boolean isVhclExist = false;
                    for (int lnCtr = 0; lnCtr <= oTransActVehicle.getActVehicleList().size() - 1; lnCtr++) {
                        if (oTransActVehicle.getActVehicle(lnCtr, "sCSNoxxxx").toString().equals(lsCSNoxxxx)) {
                            ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add vehicle model, " + lsCSNoxxxx + " already exist.");
                            isVhclExist = true;
                            break;
                        }
                    }
                    if (!isVhclExist) {
                        oTransActVehicle.addActVehicle();
                        int fnRow = oTransActVehicle.getActVehicleList().size() - 1;
                        oTransActVehicle.setActVehicle(fnRow, "sSerialID", lsSerialID);
                        oTransActVehicle.setActVehicle(fnRow, "sDescript", lsDescript);
                        oTransActVehicle.setActVehicle(fnRow, "sCSNoxxxx", lsCSNoxxxx);
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

    private void loadActVhclModelTable() {
        /*Populate table*/
        actVhclModelData.clear();
        JSONObject loJSON = new JSONObject();
        loJSON = oTransActVehicle.loadVehicle();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTransActVehicle.getVehicleList().size() - 1; lnCtr++) {

                actVhclModelData.add(new ActivityVehicle(
                        String.valueOf(lnCtr + 1), //ROW
                        oTransActVehicle.getSerialID(lnCtr, lnCtr).toString(),
                        oTransActVehicle.getVehicleCSNo(lnCtr, lnCtr).toString(),
                        oTransActVehicle.getVehicleDesc(lnCtr, lnCtr).toString()
                ));
            }
            initVehicleTable();
        }
    }

    private void initVehicleTable() {
        tblViewActVhcl.setItems(actVhclModelData);
        tblindexVehicle01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));  //Row
        tblindexVehicle02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActVhcl.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewActVhcl.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean lbNewValue = selectAllCheckBox.isSelected();
            tblViewActVhcl.getItems().forEach(item -> item.getSelect().setSelected(lbNewValue));
        });
        tblindexVehicle03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindexVehicle04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblViewActVhcl.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewActVhcl.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

}
