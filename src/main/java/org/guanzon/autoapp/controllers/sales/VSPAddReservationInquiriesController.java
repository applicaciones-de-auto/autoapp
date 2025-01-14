/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.models.sales.VSPReservationInquirers;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;

import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VSPAddReservationInquiriesController implements Initializable {

    private GRider oApp;
    private String pxeModuleName = "VSP Add Reservation Inquirers";
    private ObservableList<VSPReservationInquirers> reserveData = FXCollections.observableArrayList();
    ObservableList<String> cFilter = FXCollections.observableArrayList("RECEIPT NO", "RECEIPT DATE", "CUSTOMER NAME");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private VehicleSalesProposal oTransReserve;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAdd, btnClose, btnFilter, btnRefresh;
    @FXML
    private TableColumn<VSPReservationInquirers, Boolean> tblindex02;
    @FXML
    private TableColumn<VSPReservationInquirers, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06;
    @FXML
    private TableView<VSPReservationInquirers> tblViewReservation;
    @FXML
    private CheckBox selectAll;
    @FXML
    private TextField txtField01, txtField03;
    @FXML
    private DatePicker datePicker02;
    @FXML
    private ComboBox<String> comboBoxFilter;
    @FXML
    private HBox hboxPain;

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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        initCapitalizationFields();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        initReservationTable();
        loadReservationTable();

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void loadReservationTable() {
        JSONObject loJSON = new JSONObject();
        reserveData.clear();
        String lsDate = "";
        String lsTransAmount = "";
        String lsTransNo = "";
        String lsTransID = "";
        String lsSITransNo = "";
        loJSON = oTransReserve.loadOTHReservationList();
        if (!"error".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTransReserve.getOTHReservationList().size() - 1; lnCtr++) {
                if (oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSIDate() != null) {
                    lsDate = CustomCommonUtil.xsDateShort(oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSIDate());
                }
                if (oTransReserve.getOTHReservationModel().getReservation(lnCtr).getTranAmt() != null) {
                    lsTransAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransReserve.getOTHReservationModel().getReservation(lnCtr).getTranAmt())));
                }
                if (oTransReserve.getOTHReservationModel().getReservation(lnCtr).getTransNo() != null) {
                    lsTransNo = oTransReserve.getOTHReservationModel().getReservation(lnCtr).getTransNo();
                }
                if (oTransReserve.getOTHReservationModel().getReservation(lnCtr).getTransID() != null) {
                    lsTransID = oTransReserve.getOTHReservationModel().getReservation(lnCtr).getTransID();
                }
                if (oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSITranNo() != null) {
                    lsSITransNo = oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSITranNo();
                }
                reserveData.add(new VSPReservationInquirers(
                        String.valueOf(lnCtr + 1), // ROW
                        String.valueOf(oTransReserve.getOTHReservationModel().getReservation(lnCtr).getSINo()),
                        lsDate,
                        String.valueOf(oTransReserve.getOTHReservationModel().getReservation(lnCtr).getCompnyNm()),
                        lsTransNo,
                        lsTransAmount,
                        lsTransID,
                        lsSITransNo
                ));
                lsDate = "";
                lsTransAmount = "";
                lsTransNo = "";
                lsTransID = "";
                lsSITransNo = "";
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
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06_reservation"));

        tblViewReservation.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewReservation.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
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

    private void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnClose, btnAdd, btnFilter, btnRefresh);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnAdd":
                ObservableList<VSPReservationInquirers> selectedItems = FXCollections.observableArrayList();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                    for (VSPReservationInquirers item : tblViewReservation.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedItems.add(item);
                        }
                    }
                    if (selectedItems.isEmpty()) {
                        ShowMessageFX.Warning(null, pxeModuleName, "No selected Items");
                        selectAll.setSelected(false);
                        return;
                    }
                    for (VSPReservationInquirers item : selectedItems) {
                        String lsSITranNo = item.getTblindex08_reservation();
                        String lsTransNo = item.getTblindex05_reservation();
                        String lsTransID = item.getTblindex07_reservation();
                        loJSON = oTransReserve.addToVSPReservation(lsTransNo, lsTransID, lsSITranNo);
                    }
                    if ("error".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Information(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                    selectAll.setSelected(false);
                }
                loadReservationTable();
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnFilter":
                LocalDate selectedDate = datePicker02.getValue();

                // Filter the data based on the selected date
                ObservableList<VSPReservationInquirers> filteredData = FXCollections.observableArrayList();

                if (selectedDate == null) {
                    // If no date is selected, reset the table to show all data
                    tblViewReservation.setItems(FXCollections.observableArrayList(reserveData));
                } else {
                    // Filter data
                    for (VSPReservationInquirers reserveDataItem : reserveData) {
                        try {
                            // Parse the reservation date from the model
                            LocalDate reservationDate = LocalDate.parse(reserveDataItem.getTblindex03_reservation(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                            // Check if the reservation date matches the selected date
                            if (reservationDate.isEqual(selectedDate)) {
                                filteredData.add(reserveDataItem);
                            }
                        } catch (DateTimeParseException e) {
                            // Handle parsing exceptions if necessary
                            System.err.println("Error parsing reservation date: " + e.getMessage());
                        }
                    }

                    // Set the filtered items in the table view
                    tblViewReservation.setItems(filteredData);

                    // Show a message if no records were found
                    if (filteredData.isEmpty()) {
                        ShowMessageFX.Information(null, "ModuleName", "No record found!");
                    }
                }
                break;
            case "btnRefresh":
                tblViewReservation.refresh();
                CustomCommonUtil.setText("", txtField01, txtField03);
                datePicker02.setValue(null);
                tblViewReservation.setItems(reserveData);
                break;
            default:
                ShowMessageFX.Information(null, pxeModuleName, "Please inform admin for the unregistered " + lsButton + "button.");
                break;
        }
    }

    private void initComboBoxItems() {
        comboBoxFilter.setItems(cFilter);
    }

    private void initFieldsAction() {
        txtField01.setDisable(true);
        comboBoxFilter.setOnAction(e -> {
            String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
            CustomCommonUtil.setVisible(false, txtField01, txtField03, hboxPain);
            CustomCommonUtil.setManaged(false, txtField01, txtField03, hboxPain);
            switch (lsSelectedFilter) {
                case "RECEIPT NO":
                    txtField01.setDisable(false);
                    txtField01.setText("");
                    txtField01.setVisible(true);
                    txtField01.setManaged(true);
                    tblViewReservation.setItems(reserveData);
                    break;
                case "RECEIPT DATE":
                    datePicker02.setValue(null);
                    hboxPain.setVisible(true);
                    tblViewReservation.setItems(reserveData);
                    break;
                case "CUSTOMER NAME":
                    txtField03.setText("");
                    txtField03.setVisible(true);
                    txtField03.setManaged(true);
                    tblViewReservation.setItems(reserveData);
                    break;
            }
        });
    }

    private void initTextFieldsProperty() {
        txtField01.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            loadReservationTable();
                            tblViewReservation.setItems(reserveData);
                            tblViewReservation.refresh();
                        }
                    }
                }
                );
        txtField03.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (newValue.isEmpty()) {
                            loadReservationTable();
                            tblViewReservation.setItems(reserveData);
                            tblViewReservation.refresh();
                        }
                    }
                }
                );
    }

}
