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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.models.general.ModelActivityTown;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityTownCityMainEntryDialogController implements Initializable, ScreenInterface {

    private GRider oApp;
    private String psProvID;
//    private Activity oTransActTown;
    private final String pxeModuleName = "Activity Town Entry"; //Form Title
    private ObservableList<ModelActivityTown> townCitydata = FXCollections.observableArrayList();
    @FXML
    private Button btnClose;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAddTown;

    @FXML
    private TableView<ModelActivityTown> tblViewTown;
    @FXML
    private TableColumn<ModelActivityTown, String> tblindex01;
    @FXML
    private TableColumn<ModelActivityTown, String> tblindex02;
    @FXML
    private TableColumn<ModelActivityTown, String> tblindex03;

//    public void setObject(Activity foValue) {
//        oTransActTown = foValue;
//    }
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setProv(String fsValue) {
        psProvID = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnClose.setOnAction(this::cmdButton_Click);
        btnAddTown.setOnAction(this::cmdButton_Click);
        initTownTable();
        loadTownTable();

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAddTown":
                // Collect selected items
                ObservableList<ModelActivityTown> selectedItems = FXCollections.observableArrayList();
                for (ModelActivityTown item : tblViewTown.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                // Check if there are no selected items
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                    break;
                }
                // Confirm the addition of selected items
                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                    break;
                }
                int addedCount = 0;
                for (ModelActivityTown item : selectedItems) {
                    String townId = item.getTblindexCity02();
                    String townName = item.getTblindexCity03();
                    boolean isTownExist = false;
//                    for (int i = 0; i < oTransActTown.getActTownList().size(); i++) {
//                        if (oTransActTown.getActTown(i, "sTownName").toString().equals(townName)) {
//                            ShowMessageFX.Warning(null, pxeModuleName, "Skipping, Failed to add town, " + townName + " already exists.");
//                            isTownExist = true;
//                            break;
//                        }
//                    }
                    if (!isTownExist) {
                        addedCount++;
//                        oTransActTown.addActTown(townId, townName);
                    }
                }

                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added town(s) successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add town(s).");
                }

                CommonUtils.closeStage(btnAddTown);
                break;
        }
    }

    private void loadTownTable() {
//        townCitydata.clear();
//        if (oTransActTown.loadTown(psProvID, true)) {
//            for (int lnCtr = 0; lnCtr <= oTransActTown.getTownList().size() - 1; lnCtr++) {
//                System.out.println(oTransActTown.getTown(lnCtr, "sTownName").toString());
//                townCitydata.add(new ModelActivityTown(
//                        String.valueOf(lnCtr + 1), //ROW
//                        oTransActTown.getTown(lnCtr, "sTownIDxx").toString(),
//                        oTransActTown.getTown(lnCtr, "sTownName").toString()
//                ));
//            }
//            tblViewTown.setItems(townCitydata);
//        }
    }

    private void initTownTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindexCity01"));  //Row
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindexCity03"));
        tblViewTown.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewTown.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        townCitydata.clear();
        tblViewTown.setItems(townCitydata);
    }
}
