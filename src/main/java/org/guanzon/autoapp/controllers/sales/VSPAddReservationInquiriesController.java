/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.models.sales.VSPReservationInquirers;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VSPAddReservationInquiriesController implements Initializable {

    private GRider oApp;
    private String pxeModuleName = "VSP Add Reservation Inquirers";
    private ObservableList<VSPReservationInquirers> reserveData = FXCollections.observableArrayList();
    ObservableList<String> cFilter = FXCollections.observableArrayList("RECEIPT NO", "RECEIPT DATE", "CUSTOMER NAME");
    private VehicleSalesProposal oTransReserve;
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableColumn<VSPReservationInquirers, Boolean> tblindex02;
    @FXML
    private TableColumn<VSPReservationInquirers, String> tblindex01, tblindex03, tblindex04, tblindex05;
    @FXML
    private TableView<VSPReservationInquirers> tblViewReservation;
    @FXML
    private CheckBox selectAll;
    @FXML
    private TextField txtField01;
    @FXML
    private DatePicker datePicker02;
    @FXML
    private TextField txtField03;
    @FXML
    private ComboBox<String> comboBoxFilter;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTransReserve = foValue;
    }

    private Stage getStage() {
        return (Stage) tblViewReservation.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtonClick();
        initReservationTable();
        loadReservationTable();
        initTextKeyPressed();
        dateValueProperty();
        initFieldTextProperty();
        initCapitalizationFields();
        initComboFields();
        comboBoxFilter.setItems(cFilter);

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
    }

    private void dateValueProperty() {
        FilteredList<VSPReservationInquirers> filteredTxtFieldReserveDate = new FilteredList<>(reserveData);

        datePicker02.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                tblViewReservation.setItems(filteredTxtFieldReserveDate);
                return;
            }

            ObservableList<VSPReservationInquirers> filteredDate = filteredTxtFieldReserveDate.stream()
                    .filter(reserveData -> {
                        LocalDate reserveDate = LocalDate.parse(reserveData.getTblindex03_reservation());
                        return newValue.equals(reserveDate);
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            tblViewReservation.setItems(filteredDate);

            if (filteredDate.isEmpty()) {
                ShowMessageFX.Information(null, "ModuleName", "No record found!");
            }
        });
    }

    private void initFieldTextProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    loadReservationTable();
                    tblViewReservation.setItems(reserveData);
                    tblViewReservation.refresh();
                }
            }
        });
        txtField03.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    loadReservationTable();
                    tblViewReservation.setItems(reserveData);
                    tblViewReservation.refresh();
                }
            }
        });
    }

    private void initButtonClick() {
        List<Button> buttons = Arrays.asList(btnClose, btnAdd);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                ObservableList<VSPReservationInquirers> selectedItems = FXCollections.observableArrayList();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                    for (VSPReservationInquirers item : tblViewReservation.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedItems.add(item);
                        }
                    }
                    int addCount = 0;
                    for (VSPReservationInquirers item : selectedItems) {
                        String lsRecptNo = item.getTblindex02_reservation();
                        oTransReserve.addToVSPReservation(lsRecptNo);
                        addCount++;
                    }
                    if (addCount >= 1) {
                        ShowMessageFX.Information(null, pxeModuleName, "Added reservation Successfully");
                        selectAll.setSelected(false);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Failed to add reservation.");
                        return;
                    }
                }
                loadReservationTable();
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Information(null, pxeModuleName, "Please inform admin for the unregistered " + lsButton + "button.");
                break;
        }
    }

    private void loadReservationTable() {
        JSONObject loJSON = new JSONObject();
        reserveData.clear();
        String lsDate = "";
        loJSON = oTransReserve.loadOTHReservationList();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTransReserve.getOTHReservationList().size() - 1; lnCtr++) {
                if (oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSIDate() != null) {
                    lsDate = InputTextUtil.xsDateShort((Date) oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSIDate());
                }
                reserveData.add(new VSPReservationInquirers(
                        String.valueOf(lnCtr + 1), // ROW
                        String.valueOf(oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSINo()),
                        lsDate,
                        String.valueOf(oTransReserve.getOTHReservationModel().getReservation(lnCtr).getCompnyNm()),
                        ""
                ));
            }
        }
        tblViewReservation.setItems(reserveData);
    }

    private void initReservationTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_reservation"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewReservation.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewReservation.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAll.setSelected(true);
                } else {
                    selectAll.setSelected(false);
                }
            });
        });
        selectAll.setOnAction(event -> {
            boolean newValue = selectAll.isSelected();
            if (!tblViewReservation.getItems().isEmpty()) {
                tblViewReservation.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02_reservation"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex03_reservation"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex04_reservation"));
        tblViewReservation.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewReservation.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void initComboFields() {
        txtField01.setDisable(true);
        comboBoxFilter.setOnAction(e -> {
            String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
            txtField01.setVisible(false);
            txtField01.setManaged(false);
            datePicker02.setVisible(false);
            datePicker02.setManaged(false);
            txtField03.setVisible(false);
            txtField03.setManaged(false);
            switch (lsSelectedFilter) {
                case "RECEIPT NO":
                    txtField01.setText("");
                    txtField01.setDisable(false);
                    txtField01.setVisible(true);
                    txtField01.setManaged(true);
                    datePicker02.setVisible(false);
                    datePicker02.setManaged(false);
                    txtField03.setVisible(false);
                    txtField03.setManaged(false);
                    tblViewReservation.setItems(reserveData);
                    break;
                case "RECEIPT DATE":
                    datePicker02.setValue(LocalDate.now());
                    datePicker02.setVisible(true);
                    datePicker02.setManaged(true);
                    txtField01.setVisible(false);
                    txtField01.setManaged(false);
                    txtField03.setVisible(false);
                    txtField03.setManaged(false);
                    tblViewReservation.setItems(reserveData);
                    break;
                case "CUSTOMER NAME":
                    txtField03.setText("");
                    txtField03.setVisible(true);
                    txtField03.setManaged(true);
                    datePicker02.setVisible(false);
                    datePicker02.setManaged(false);
                    txtField01.setVisible(false);
                    txtField01.setManaged(false);
                    tblViewReservation.setItems(reserveData);
                    break;
            }
        });
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        switch (event.getCode()) {
            case ENTER:
                switch (txtFieldID) {
                    case "txtField01":
                        String lsFilterReserve = txtField01.getText().trim().toLowerCase();
                        FilteredList<VSPReservationInquirers> filteredTxtFieldReserve = new FilteredList<>(reserveData);
                        filteredTxtFieldReserve.setPredicate(clients -> {
                            if (lsFilterReserve.isEmpty()) {
                                return true;
                            } else {
                                String receiptNo = clients.getTblindex02_reservation().toLowerCase();
                                return receiptNo.contains(lsFilterReserve);
                            }
                        });
                        tblViewReservation.setItems(filteredTxtFieldReserve);
                        if (filteredTxtFieldReserve.isEmpty()) {
                            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                        }
                        break;
                    case "txtField03":
                        String lsFilterCustomer = txtField03.getText().trim().toLowerCase();
                        FilteredList<VSPReservationInquirers> filteredTxtFieldCustomer = new FilteredList<>(reserveData);
                        filteredTxtFieldCustomer.setPredicate(clients -> {
                            if (lsFilterCustomer.isEmpty()) {
                                return true;
                            } else {
                                String reserveName = clients.getTblindex04_reservation().toLowerCase();
                                return reserveName.contains(lsFilterCustomer);
                            }
                        });
                        tblViewReservation.setItems(filteredTxtFieldCustomer);
                        if (filteredTxtFieldCustomer.isEmpty()) {
                            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                        }
                        break;
                }
                break;
        }
    }
}
