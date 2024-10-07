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
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.autoapp.models.general.ActivityMember;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityMemberController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Activity oTransActMembers;
    private final String pxeModuleName = "Activity Member"; //Form Title
    private ObservableList<ActivityMember> employeeData = FXCollections.observableArrayList();
    private ObservableList<ActivityMember> departData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableView<ActivityMember> tblViewDepart, tblViewEmployee;
    @FXML
    private TableColumn<ActivityMember, String> tblDprtmntndex01;
    @FXML
    private TableColumn<ActivityMember, String> tblEmplyIndex01, tblEmplyIndex03;
    @FXML
    private TableColumn<ActivityMember, Boolean> tblEmplyIndex02;
    @FXML
    private CheckBox selectAllEmployee;

    public void setObject(Activity foValue) {
        oTransActMembers = foValue;
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
        loadDepartTable();
        initDepartTable();
        initEmployeeTable();
        tblViewDepart.setOnMouseClicked(event -> {
            ActivityMember selectedDepartment = tblViewDepart.getSelectionModel().getSelectedItem();
            if (selectedDepartment != null) {
                String departmentID = selectedDepartment.getTblindexMem02();
                loadEmployeeTable(departmentID);
            }
        });
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<ActivityMember> selectedItems = FXCollections.observableArrayList();
                for (ActivityMember item : tblViewEmployee.getItems()) {
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
                for (ActivityMember item : selectedItems) {
                    String lsEmployID = item.getTblindexMem04();
                    String lsEmpName = item.getTblindexMem05();
                    String lsDeptName = item.getTblindexMem03();
                    boolean isEmpExist = false;
                    if (oTransActMembers.getModel().getModel().getEmployID().equals(lsEmployID)) {
                        ShowMessageFX.Error(null, pxeModuleName, "Person in charge: " + lsEmpName + " cannot be one of the member.");
                        isEmpExist = true;
                        addedCount++;
                    }
                    for (int lnCtr = 0; lnCtr <= oTransActMembers.getActMemberList().size() - 1; lnCtr++) {
                        if (oTransActMembers.getActMember(lnCtr, "sEmployID").toString().equals(lsEmployID)) {
                            if (oTransActMembers.getActMember(lnCtr, "cOriginal").toString().equals("1")) {
                                ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add Employee, " + lsEmpName + " already exist.");
                            } else {
                                oTransActMembers.setActMember(lnCtr, "cOriginal", "1");
                                addedCount++;
                            }
                            isEmpExist = true;
                            break;
                        }
                    }
                    if (!isEmpExist) {
                        oTransActMembers.addActMember();
                        int fnRow = oTransActMembers.getActMemberList().size() - 1;
                        oTransActMembers.setActMember(fnRow, "sEmployID", lsEmployID);
                        oTransActMembers.setActMember(fnRow, "sCompnyNm", lsEmpName);
                        oTransActMembers.setActMember(fnRow, "sDeptName", lsDeptName);
                        addedCount++;
                    }
                }
                if (addedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Added Employee successfully.");
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Failed to add employee");
                }
                CommonUtils.closeStage(btnAdd);
                break;
        }
    }
    //storing values on bankentrydata

    private void loadEmployeeTable(String departmentID) {
        employeeData.clear();
        JSONObject loJSON = new JSONObject();
        loJSON = oTransActMembers.loadEmployee(departmentID);
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTransActMembers.getEmployeeList().size() - 1; lnCtr++) {
                employeeData.add(new ActivityMember(
                        String.valueOf(lnCtr + 1), // ROW
                        "",
                        oTransActMembers.getEmpDeptNm(lnCtr, lnCtr).toString(),
                        oTransActMembers.getEmployeeID(lnCtr, lnCtr).toString(),
                        oTransActMembers.getEmployeeNm(lnCtr, lnCtr).toString()
                ));
            }
            tblViewEmployee.setItems(employeeData);
            initEmployeeTable();
        }
    }

    private void initEmployeeTable() {
        tblEmplyIndex01.setCellValueFactory(new PropertyValueFactory<>("tblindexMem01"));
        tblEmplyIndex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewEmployee.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewEmployee.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllEmployee.setSelected(true);
                } else {
                    selectAllEmployee.setSelected(false);
                }
            });
        });
        selectAllEmployee.setOnAction(event -> {
            boolean newValue = selectAllEmployee.isSelected();
            tblViewEmployee.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblEmplyIndex03.setCellValueFactory(new PropertyValueFactory<>("tblindexMem05"));
        tblViewEmployee.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewEmployee.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void loadDepartTable() {
        departData.clear();
        JSONObject loJSON = new JSONObject();
        loJSON = oTransActMembers.loadDepartment();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 1; lnCtr <= oTransActMembers.getDepartmentList().size() - 1; lnCtr++) {
                departData.add(new ActivityMember(
                        String.valueOf(lnCtr), //ROW
                        oTransActMembers.getDepartmentID(lnCtr, lnCtr).toString(),
                        oTransActMembers.getDepartmentNm(lnCtr, lnCtr).toString(),
                        "",
                        ""
                ));
            }
        }
        tblViewDepart.setItems(departData);
    }

    private void initDepartTable() {
        tblDprtmntndex01.setCellValueFactory(new PropertyValueFactory<>("tblindexMem03"));
        tblViewDepart.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewDepart.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

}
