/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.models.general.ModelActivityVehicle;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityVehicleDialogController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Activity Vehicle Entry";
//    private Activity oTransActVehicle;
    ObservableList<ModelActivityVehicle> actVhclModelData = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<ModelActivityVehicle> tblViewActVchl;
    @FXML
    private TableColumn<ModelActivityVehicle, String> tblindex01, tblindex03;
    @FXML
    private TableColumn<ModelActivityVehicle, Boolean> tblindex02;
    @FXML
    private CheckBox selectAllCheckBox;

//    public void setObject(Activity foValue) {
//        oTransActVehicle = foValue;
//    }
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
                ObservableList<ModelActivityVehicle> selectedItems = FXCollections.observableArrayList();
                for (ModelActivityVehicle item : tblViewActVchl.getItems()) {
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
//                for (ModelActivityVehicle item : selectedItems) {
//                    String lsSerialID = item.getTblIndexVhcl02();
//                    String lsDescript = item.getTblIndexVchl03();
//                    String lsCSNoxxxx = item.getTblIndexVchl04();// Assuming there is a method to retrieve the transaction number
//                    boolean isVhclExist = false;
//                    for (int lnCtr = 0; lnCtr <= oTransActVehicle.getActVehicleList().size() - 1; lnCtr++) {
//                        if (oTransActVehicle.getActVehicle(lnCtr, "sDescript").toString().equals(lsDescript)) {
//                            ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add vehicle model, " + lsDescript + " already exist.");
//                            isVhclExist = true;
//                            break;
//                        }
//                    }
//                    if (!isVhclExist) {
//                        loJSON = oTransActVehicle.addActVehicle(lsSerialID, lsDescript, lsCSNoxxxx);
//                        if ("success".equals((String) loJSON.get("result"))) {
//                            addedCount++;
//                        } else {
//                            ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
//                        }
//                    }
//                    }
//                }
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
//        if (oTransActVehicle.loadActVehicle("", false)) {
//            for (int lnCtr = 1; lnCtr <= oTransActVehicle.getVehicleCount(); lnCtr++) {
//                actVhclModelData.add(new ModelActivityVehicle(
//                        String.valueOf(lnCtr + 1), //ROW
//                        oTransActVehicle.getVehicle(lnCtr, "sSerialID").toString(),
//                        oTransActVehicle.getVehicle(lnCtr, "sDescript").toString(),
//                        oTransActVehicle.getVehicle(lnCtr, "sCSNoxxxx").toString()
//                ));
//            }
//            tblViewActVchl.setItems(actVhclModelData);
//        }
    }

    private void initVehicleTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindexVhcl01"));  //Row
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActVchl.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewActVchl.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean lbNewValue = selectAllCheckBox.isSelected();
            tblViewActVchl.getItems().forEach(item -> item.getSelect().setSelected(lbNewValue));
        });
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindexVhcl03"));
        tblViewActVchl.setItems(actVhclModelData);
        tblViewActVchl.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewActVchl.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

}
