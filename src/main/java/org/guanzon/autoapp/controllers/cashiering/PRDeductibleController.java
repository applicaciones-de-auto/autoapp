/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.autoapp.models.cashiering.Deductibles;
import org.guanzon.autoapp.models.cashiering.ManuallyDeductibles;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PRDeductibleController implements Initializable {

    private String pxeModuleName = "Deductibles";
    private ObservableList<ManuallyDeductibles> manuallyData = FXCollections.observableArrayList();
    private ObservableList<Deductibles> deductData = FXCollections.observableArrayList();
    @FXML
    private Button btnAddDeductibles, btnRefresh, btnApplyReceipts, btnClose;
    @FXML
    private TextField txtField01, txtField03;
    @FXML
    private CheckBox selectAllBox;
    @FXML
    private ComboBox<String> comboBox02;
    @FXML
    private TableView<ManuallyDeductibles> tblViewManually;
    @FXML
    private TableColumn<Deductibles, String> tblindex01_Manually, tblindex03_Manually, tblindex04_Manually, tblindex05_Manually, tblindex06_Manually;
    @FXML
    private TableColumn<Deductibles, Boolean> tblindex02_Manually;
    @FXML
    private TableView<Deductibles> tblViewDeductibles;
    @FXML
    private TableColumn<Deductibles, String> tblindex01_Deductibles, tblindex02_Deductibles, tblindex03_Deductibles, tblindex04_Deductibles;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initManuallyTable();
        initDeductiblesTable();
        initButtonsClick();
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
    }

    private void loadManuallyTable() {
        manuallyData.clear();
        tblViewManually.setItems(manuallyData);
    }

    private void initManuallyTable() {
        tblindex01_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_Manually.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex04_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex05_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex06_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblViewManually.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewManually.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllBox.setSelected(true);
                } else {
                    selectAllBox.setSelected(false);
                }
            });
        });
        selectAllBox.setOnAction(event -> {
            boolean newValue = selectAllBox.isSelected();
            tblViewManually.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblViewManually.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewManually.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        manuallyData.clear();
        tblViewManually.setItems(manuallyData);
    }

    private void loadDeductiblesTable() {
        deductData.clear();
        tblViewDeductibles.setItems(deductData);
    }

    private void initDeductiblesTable() {
        tblindex01_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblViewDeductibles.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewDeductibles.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        deductData.clear();
        tblViewDeductibles.setItems(deductData);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField03":
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAddDeductibles, btnRefresh, btnApplyReceipts, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAddDeductibles":
                CommonUtils.closeStage(btnAddDeductibles);
                break;
            case "btnRefresh":
                CommonUtils.closeStage(btnRefresh);
                break;
            case "btnApplyReceipts":
                CommonUtils.closeStage(btnApplyReceipts);
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
}
