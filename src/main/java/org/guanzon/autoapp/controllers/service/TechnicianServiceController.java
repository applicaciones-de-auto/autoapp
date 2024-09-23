/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.service;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class TechnicianServiceController implements Initializable, ScreenInterface {

    private GRider oApp;
    private JobOrder oTransTechnician;
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private String pxeModuleName = "Technician Service";
    @FXML
    private Button btnEdit, btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    private String psTrans = "";
    private int pnRow = 0;
    @FXML
    private TableView<Labor> tblViewLabor;
    @FXML
    private TableColumn<Labor, String> tblindex01;
    @FXML
    private TableColumn<Labor, Boolean> tblindex02;
    @FXML
    private TableColumn<Labor, String> tblindex03;
    @FXML
    private CheckBox selectAll;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(JobOrder foValue) {
        oTransTechnician = foValue;
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
        btnEdit.setOnAction(this::handleButtonAction);
        initLaborTable();
        loadTechServiceFields();
        loadLabor();
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnEdit":
                ObservableList<Labor> selectedItems = FXCollections.observableArrayList();
                for (Labor item : tblViewLabor.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to update.");
                    return;
                }

                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to update?")) {
                    return;
                }

                int lnTech = 0;
                for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOTechList().size() - 1; lnCtr++) {
                    if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getTechID().equals(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID())) {
                        lnTech++;

//                        if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getLaborCde() == null) {
//                            lnTech++;
//                        } else {
//                            if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getLaborCde().trim().isEmpty()) {
//                                lnTech++;
//                            }
//                        }
                    }
                }

                while (selectedItems.size() > lnTech) {
                    oTransTechnician.addJOTech();
                    oTransTechnician.getJOTechModel().getDetailModel(oTransTechnician.getJOTechList().size() - 1).setTechName(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechName());
                    oTransTechnician.getJOTechModel().getDetailModel(oTransTechnician.getJOTechList().size() - 1).setTechID(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
                    lnTech++;
                }

                int addedCount = 0;
                for (Labor item : selectedItems) {
                    String lsLabrCde = item.getTblindex03_labor();
                    String lsLabrDesc = item.getTblindex07_labor();

                    for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOTechList().size() - 1; lnCtr++) {
                        if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getTechID().equals(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID())) {
                            oTransTechnician.getJOTechModel().getDetailModel(lnCtr).setLaborCde(lsLabrCde);
                            oTransTechnician.getJOTechModel().getDetailModel(lnCtr).setLaborDsc(lsLabrDesc);
                            addedCount++;
                        }
                    }
                }
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Update Tech with Labor successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to update Tech with Labor");
                }
                CommonUtils.closeStage(btnEdit);
                break;
        }
    }

    private void loadTechServiceFields() {
        txtField01.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
        txtField02.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechName());
    }

    private void loadLabor() {
        laborData.clear();
        boolean lbAdditional = false;
        boolean lbChargeType = false;
        for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOLaborList().size() - 1; lnCtr++) {
            if (oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
                lbChargeType = true;
            }
            laborData.add(new Labor(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborCde()),
                    "",
                    "",
                    "",
                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc()),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    lbAdditional,
                    lbChargeType
            ));
            lbAdditional = false;
            lbChargeType = false;
        }
        tblViewLabor.setItems(laborData);
    }

    private void initLaborTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
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

    }

}
