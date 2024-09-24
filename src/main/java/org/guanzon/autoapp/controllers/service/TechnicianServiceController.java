/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.service;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.service.TechnicianLabor;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class TechnicianServiceController implements Initializable, ScreenInterface {

    private GRider oApp;
    private JobOrder oTransTechnician;
    private List<String> existingLaborCode = new ArrayList<>();
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private List<TechnicianLabor> techData = new ArrayList<>();
    private String pxeModuleName = "Technician Service";
    private String psTrans = "";
    private int pnRow = 0;
    @FXML
    private Button btnEdit, btnClose;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;

//    private TableView<Labor> tblViewLabor;
//    private TableColumn<Labor, String> tblindex01;
//    private TableColumn<Labor, Boolean> tblindex02;
//    private TableColumn<Labor, String> tblindex03;
//    private CheckBox selectAll;
    @FXML
    private TextField txtField03;

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
        txtField03.setOnKeyPressed(event -> txtField_KeyPressed(event));
//        initLaborTable();
        initCapitalizationFields();
        loadTechServiceFields();
//        loadLabor();
//        loadLabor();
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnEdit":
                if (txtField03.getText().trim().isEmpty()) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please enter value for labor description.");
                    return;
                } else {
                    CommonUtils.closeStage(btnEdit);
                }
//                ObservableList<Labor> selectedItems = FXCollections.observableArrayList();
//                for (Labor item : tblViewLabor.getItems()) {
//                    if (item.getSelect().isSelected()) {
//                        selectedItems.add(item);
//                    }
//                }
//                if (selectedItems.isEmpty()) {
//                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to update.");
//                    return;
//                }
//
//                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to update?")) {
//                    return;
//                }
//
//                int lnTech = 0;
//                for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOTechList().size() - 1; lnCtr++) {
//                    //check tech id
//                    if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getTechID().equals(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID())) {
//                        lnTech++;
//                    }
//                }
//
//                while (selectedItems.size() > lnTech) {
//                    //add technician into the row table
//                    oTransTechnician.addJOTech();
//                    oTransTechnician.getJOTechModel().getDetailModel(oTransTechnician.getJOTechList().size() - 1).setTechID(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
//                    oTransTechnician.getJOTechModel().getDetailModel(oTransTechnician.getJOTechList().size() - 1).setTechName(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechName());
//                    lnTech++;
//                }
//
//                int addedCount = 0;
//                for (Labor item : selectedItems) {
//                    String lsLabrCde = item.getTblindex03_labor();
//                    String lsLabrDesc = item.getTblindex07_labor();
//                    for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOTechList().size() - 1; lnCtr++) {
//                        if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getTechID().equals(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID())) {
//                            if (oTransTechnician.getJOTechModel().getDetailModel(lnCtr).getLaborCde().isEmpty()) {
//                                oTransTechnician.getJOTechModel().getDetailModel(lnCtr).setLaborCde(lsLabrCde);
//                                oTransTechnician.getJOTechModel().getDetailModel(lnCtr).setLaborDsc(lsLabrDesc);
//                                addedCount++;
//                                break;
//                            }
//                        }
//                    }
//                }
//                if (addedCount > 0) {
//                    ShowMessageFX.Information(null, pxeModuleName, "Update Tech with Labor successfully.");
//                } else {
//                    ShowMessageFX.Error(null, pxeModuleName, "Failed to update Tech with Labor");
//                }
//                loJSON = oTransTechnician.sear
                break;
        }
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                case F3:
                    switch (txtFieldID) {
                        case "txtField03":
                            loJSON = oTransTechnician.searchVSPLabor(lsValue, pnRow);
                            if (!"error".equals(loJSON.get("result"))) {
                                txtField03.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getLaborDsc());
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                txtField03.setText("");
                                return;
                            }
                            break;
                    }
                    event.consume();
                    break;
                default:
                    break;
            }
        }
    }

    private void loadTechServiceFields() {
        txtField01.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechID());
        txtField02.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getTechName());
        txtField03.setText(oTransTechnician.getJOTechModel().getDetailModel(pnRow).getLaborDsc());
    }

    //    private void loadLabor() {
    //        laborData.clear();
    //        boolean lbAdditional = false;
    //        boolean lbChargeType = false;
    //        for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOLaborList().size() - 1; lnCtr++) {
    //            if (oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
    //                lbChargeType = true;
    //            }
    //            String lsLaborCode = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborCde());
    //            laborData.add(new Labor(
    //                    String.valueOf(lnCtr + 1),
    //                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
    //                    lsLaborCode,
    //                    "",
    //                    "",
    //                    "",
    //                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc()),
    //                    "",
    //                    "",
    //                    "",
    //                    "",
    //                    "",
    //                    "",
    //                    "",
    //                    lbAdditional,
    //                    lbChargeType
    //            ));
    //            lbAdditional = false;
    //            lbChargeType = false;
    //        }
    //        tblViewLabor.setItems(laborData);
    //    }
//    private void loadLabor() {
//        laborData.clear();
//        boolean lbAdditional = false;
//        boolean lbChargeType = false;
//
//        existingLaborCode.clear();
//        //add laborcode from existing laborcode arraylist
//        for (int lnRow = 0; lnRow < oTransTechnician.getJOTechList().size(); lnRow++) {
//            existingLaborCode.add(oTransTechnician.getJOTechModel().getDetailModel(lnRow).getTechID());
//            existingLaborCode.add(oTransTechnician.getJOTechModel().getDetailModel(lnRow).getLaborCde());
//        }
//        for (int lnCtr = 0; lnCtr < oTransTechnician.getJOLaborList().size(); lnCtr++) {
//            if (oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
//                lbChargeType = true;
//            }
//            String lsLaborCode = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborCde());
//            String laborDesc = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc());
//
//            // Add to laborData
//            laborData.add(new Labor(
//                    String.valueOf(lnCtr + 1),
//                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
//                    lsLaborCode,
//                    "",
//                    "",
//                    "",
//                    laborDesc,
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    lbAdditional,
//                    lbChargeType
//            ));
//            lbAdditional = false;
//            lbChargeType = false;
//        }
//        tblViewLabor.setItems(laborData);
//    }
//
//    private void initLaborTable() {
//        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
//        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
//        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
//        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        tblViewLabor.getItems().forEach(item -> {
//            CheckBox selectCheckBox = item.getSelect();
//            selectCheckBox.setOnAction(event -> {
//                if (tblViewLabor.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
//                    selectAll.setSelected(true);
//                } else {
//                    selectAll.setSelected(false);
//                }
//            });
//        });
//        selectAll.setOnAction(event -> {
//            boolean newValue = selectAll.isSelected();
//            tblViewLabor.getItems().forEach(item -> item.getSelect().setSelected(newValue));
//        });
//    }
//    private void loadLabor() {
//
//        existingLaborCode.clear();
//        //add laborcode from existing laborcode arraylist
//        for (int lnRow = 0; lnRow <= oTransTechnician.getJOTechList().size() - 1; lnRow++) {
//            existingLaborCode.add(oTransTechnician.getJOTechModel().getDetailModel(lnRow).getTechID());
//            existingLaborCode.add(oTransTechnician.getJOTechModel().getDetailModel(lnRow).getLaborCde());
//        }
//        laborData.clear();
//
//        boolean lbAdditional = false;
//        boolean lbChargeType = false;
//
//        for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOLaborList().size() - 1; lnCtr++) {
//            if (oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
//                lbChargeType = true;
//            }
//            String lsLaborCode = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborCde());
//            String laborDesc = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc());
//            Labor item = new Labor(String.valueOf(lnCtr + 1),
//                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
//                    lsLaborCode,
//                    "",
//                    "",
//                    "",
//                    laborDesc,
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    lbAdditional,
//                    lbChargeType);
//            lbAdditional = false;
//            lbChargeType = false;
//            String lsTechID = "";
//            for (int lnRow = 0; lnRow <= oTransTechnician.getJOTechList().size() - 1; lnRow++) {
//                lsTechID = oTransTechnician.getJOTechModel().getDetailModel(lnRow).getTechID();
//            }
//            if (existingLaborCode.equals(lsTechID)) {
//                if (existingLaborCode.contains(lsLaborCode)) {
//                    item.getSelect().setSelected(true);
//                }
//            }
//
//            laborData.add(item);
//        }
//        initLaborTable();
//    }
//    private void loadLabor() {
//        existingLaborCode.clear();
//        //add laborcode from existing laborcode arraylist
//        for (int lnRow = 0; lnRow <= oTransTechnician.getJOTechList().size() - 1; lnRow++) {
//            String techID = oTransTechnician.getJOTechModel().getDetailModel(lnRow).getTechID();
//            String laborCd = oTransTechnician.getJOTechModel().getDetailModel(lnRow).getLaborCde();
//            existingLaborCode.add(techID + " : " + laborCd);
//            System.out.println("tech and labor: " + existingLaborCode);
//        }
//        laborData.clear();
//        boolean lbAdditional = false;
//        boolean lbChargeType = false;
//        for (int lnCtr = 0; lnCtr <= oTransTechnician.getJOLaborList().size() - 1; lnCtr++) {
//            if (oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
//                lbChargeType = true;
//            }
//            String lsLaborCode = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborCde());
//            String laborDesc = String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc());
//            Labor item = new Labor(String.valueOf(lnCtr + 1),
//                    String.valueOf(oTransTechnician.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
//                    lsLaborCode,
//                    "",
//                    "",
//                    "",
//                    laborDesc,
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    lbAdditional,
//                    lbChargeType);
//            lbAdditional = false;
//            lbChargeType = false;
//            String lsTechID = "";
//            for (int lnRow = 0; lnRow <= oTransTechnician.getJOTechList().size() - 1; lnRow++) {
//                lsTechID = oTransTechnician.getJOTechModel().getDetailModel(lnRow).getTechID();
//            }
//            if (existingLaborCode.contains(lsTechID + " : " + lsLaborCode)) {
//                item.getSelect().setSelected(true);
//            } else {
//                item.getSelect().setSelected(false);
//            }
//            laborData.add(item);
//        }
//        initLaborTable();
//    }
//
//    private void initLaborTable() {
//        tblViewLabor.setItems(laborData);
//        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
//        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
//        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
//        tblViewLabor.setRowFactory(tv -> {
//            TableRow<Labor> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (!row.isEmpty()) {
//                    Labor rowData = row.getItem();
//                    String laborCde = rowData.getTblindex03_labor();
//
//                    if (rowData.getSelect().isSelected()) {
//                        existingLaborCode.add(laborCde);
//                    } else {
//                        existingLaborCode.remove(laborCde);
//                    }
//                }
//            });
//            return row;
//        });
//        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
//            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
//            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                header.setReordering(false);
//            });
//        });
//        tblViewLabor.getItems().forEach(item -> {
//            CheckBox selectCheckBox = item.getSelect();
//            selectCheckBox.setOnAction(event -> {
//                if (tblViewLabor.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
//                    selectAll.setSelected(true);
//                } else {
//                    selectAll.setSelected(false);
//                }
//            });
//        });
//        selectAll.setOnAction(event -> {
//            boolean newValue = selectAll.isSelected();
//            tblViewLabor.getItems().forEach(item -> item.getSelect().setSelected(newValue));
//        });
//    }
}
