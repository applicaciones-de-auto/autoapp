/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.models.general.ModelActivityApproval;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ActivityApprovalController implements Initializable, ScreenInterface {

    private GRider oApp;
//    private Activity oTrans;
    private int poCtr = 0;
    private final String pxeModuleName = "Activity Approval"; //Form Title
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    ObservableList<String> cFilter = FXCollections.observableArrayList("Activity No.", "Activity Date", "Activity Title", "Activity Type",
            "Person in Charge", "Department");
    ObservableList<String> cType = FXCollections.observableArrayList("EVENT", "PROMO", "SALES");

    private ObservableList<ModelActivityApproval> actApprovalData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved, btnClose, btnActNo, btnActTitle, btnPerson, btnDepart, btnDate, btnType;
    @FXML
    private ComboBox<String> comboFilter, comboType;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Label lFrom, lTo, lbTotalBudget;
    @FXML
    private DatePicker fromDate, toDate;
    @FXML
    private Button btnRefresh;
    @FXML
    private TableView<ModelActivityApproval> tblViewActApproval;
    @FXML
    private TableColumn<ModelActivityApproval, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09;
    @FXML
    private TableColumn<ModelActivityApproval, Boolean> tblindexselect;
    @FXML
    private CheckBox selectAllCheckBox;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        initActApprovalTable();
//        initOtherUtils();
        initCombo();
//        loadActApprovalTable();
//        initButtons();
    }

    private void initOtherUtils() {
        btnActNo.setVisible(false);
        btnActNo.setManaged(false);
        btnActTitle.setVisible(false);
        btnActTitle.setManaged(false);
        btnDate.setVisible(false);
        btnDate.setManaged(false);
        btnPerson.setVisible(false);
        btnPerson.setManaged(false);
        btnDepart.setVisible(false);
        btnDepart.setManaged(false);
        btnType.setVisible(false);
        btnType.setManaged(false);
        txtFieldSearch.setVisible(false);
        txtFieldSearch.setManaged(false);
        lFrom.setManaged(false);
        lFrom.setVisible(false);
        fromDate.setVisible(false);
        fromDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
        fromDate.setManaged(false);
        lTo.setVisible(false);
        lTo.setManaged(false);
        toDate.setVisible(false);
        toDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
        toDate.setManaged(false);
        comboType.setVisible(false);
        comboType.setManaged(false);
        comboFilter.setItems(cFilter);
        comboType.setItems(cType);
    }

    public void initCombo() {
        comboFilter.setOnAction(e -> {
            String selectedFilter = comboFilter.getSelectionModel().getSelectedItem();
            // Hide all controls first
            txtFieldSearch.setVisible(false);
            txtFieldSearch.setManaged(false);
            btnActNo.setVisible(false);
            btnActNo.setManaged(false);
            btnActTitle.setVisible(false);
            btnActTitle.setManaged(false);
            btnDate.setVisible(false);
            btnDate.setManaged(false);
            btnPerson.setVisible(false);
            btnPerson.setManaged(false);
            btnDepart.setVisible(false);
            btnDepart.setManaged(false);
            btnType.setVisible(false);
            btnType.setManaged(false);
            txtFieldSearch.setVisible(false);
            txtFieldSearch.setManaged(false);
            lFrom.setManaged(false);
            lFrom.setVisible(false);
            fromDate.setVisible(false);
            fromDate.setManaged(false);
            lTo.setVisible(false);
            lTo.setManaged(false);
            toDate.setVisible(false);
            toDate.setManaged(false);
            comboType.setVisible(false);
            comboType.setManaged(false);

            // Show relevant controls based on selected filter
            switch (selectedFilter) {
                case "Activity No.":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnActNo.setVisible(true);
                    btnActNo.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);

                    break;
                case "Activity Date":
                    btnDate.setVisible(true);
                    btnDate.setManaged(true);
                    lFrom.setVisible(true);
                    lFrom.setManaged(true);

                    fromDate.setVisible(true);
                    fromDate.setManaged(true);
                    lTo.setVisible(true);
                    lTo.setManaged(true);
                    toDate.setVisible(true);
                    toDate.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Activity Type":
                    comboType.setVisible(true);
                    comboType.setManaged(true);
                    btnType.setVisible(true);
                    btnType.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Activity Title":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnActTitle.setVisible(true);
                    btnActTitle.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;
                case "Person in Charge":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnPerson.setVisible(true);
                    btnPerson.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);

                    break;
                case "Department":
                    txtFieldSearch.setText("");
                    txtFieldSearch.setVisible(true);
                    txtFieldSearch.setManaged(true);
                    btnDepart.setVisible(true);
                    btnDepart.setManaged(true);
                    tblViewActApproval.setItems(actApprovalData);
                    break;

            }
        });
    }

    private void initButtons() {
        List<Button> buttons = Arrays.asList(btnApproved, btnClose, btnActNo, btnActTitle, btnPerson, btnDepart, btnDate, btnType);

        buttons.forEach(e -> e.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJson = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose": //close tab
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Activity Approval");
                    } else {
                        ShowMessageFX.Warning(null, "Please notify the system administrator to configure the null value at the close button.", "Warning", pxeModuleName);
                    }
                }
                break;
            case "btnActNo":
                filterTable(txtFieldSearch.getText(), "ActNo");
                break;
            case "btnActTitle":
                filterTable(txtFieldSearch.getText(), "ActTitle");
                break;
            case "btnPerson":
                filterTable(txtFieldSearch.getText(), "Person");
                break;
            case "btnDepart":
                filterTable(txtFieldSearch.getText(), "Depart");
                break;
            case "btnType": //btn filter for comboBox
                String lsSelectedType = comboType.getValue();
                if (lsSelectedType == null) {
                    // No type selected, show all data
                    tblViewActApproval.setItems(actApprovalData);
                } else {
                    // Filter data based on selected type
                    ObservableList<ModelActivityApproval> filteredCombo = FXCollections.observableArrayList();
                    for (ModelActivityApproval actData : actApprovalData) {
                        if (actData.getTblindex11().equals(lsSelectedType)) {
                            filteredCombo.add(actData);
                        }
                    }
                    tblViewActApproval.setItems(filteredCombo);
                    if (filteredCombo.isEmpty()) {
                        ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                    }
                }
                break;
            case "btnDate": //btn filter for Activity Date
                LocalDate loFilterFromDate = fromDate.getValue();
                LocalDate loFilterToDate = toDate.getValue();
                ObservableList<ModelActivityApproval> filteredDate = FXCollections.observableArrayList();
                for (ModelActivityApproval actData : actApprovalData) {
                    LocalDate actDateFrom = LocalDate.parse(actData.getTblindex06());
                    LocalDate actDateTo = LocalDate.parse(actData.getTblindex07());

                    if (loFilterFromDate == null || actDateFrom.isAfter(loFilterFromDate.minusDays(1))) {
                        if (loFilterToDate == null || actDateTo.isBefore(loFilterToDate.plusDays(1))) {
                            filteredDate.add(actData);
                        }
                    }
                }
                tblViewActApproval.setItems(filteredDate);
                LocalDate loDateFrom = fromDate.getValue();
                LocalDate loDateTo = toDate.getValue();
                if (loDateFrom != null && loDateTo != null && loDateTo.isBefore(loDateFrom)) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please enter a valid date.");
                    fromDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
                    toDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
                    loadActApprovalTable();
                    return;
                }
                if (loDateFrom != null && loDateTo != null && loDateFrom.isAfter(loDateTo)) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please enter a valid date.");
                    loadActApprovalTable();
                    fromDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
                    toDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
                    loadActApprovalTable();
                    return;
                }
                if (filteredDate.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                }
                break;
            case "btnApproved": //btn for approval
                ObservableList<ModelActivityApproval> selectedItems = FXCollections.observableArrayList();
                for (ModelActivityApproval item : tblViewActApproval.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }
                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to approve.");
                    return;
                }

                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {
                    return;
                }
                int approvedCount = 0;
//                for (ModelActivityApproval item : selectedItems) {
//                    String fsTransNox = item.getTblindex01(); // Assuming there is a method to retrieve the transaction number
//                    loJSON = oTrans.ApproveActivity(fsTransNox);
//                    if ("success".equals((String) loJSON.get("result"))) {
//                        approvedCount++;
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, "Failed to approve activity.");
//                        loadActApprovalTable();
//                        selectAllCheckBox.setSelected(false);
//                    }
//                }
                if (approvedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, "Activity approved successfully.");
                }
                loadActApprovalTable();
                selectAllCheckBox.setSelected(false);
                tblViewActApproval.getItems().removeAll(selectedItems);
                btnRefresh.fire();
                break;
            case "btnRefresh": //btn for refresh
                comboType.getSelectionModel()
                        .clearSelection();
                txtFieldSearch.clear();

                fromDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
                toDate.setValue(InputTextUtil.strToDate(oApp.getServerDate().toString()));
                loadActApprovalTable();

                selectAllCheckBox.setSelected(
                        false);

                break;
        }
    }

    private void filterTable(String searchText, String fieldType) {
        String filterText = searchText.trim().toLowerCase();
        FilteredList<ModelActivityApproval> filteredList = new FilteredList<>(actApprovalData);
        filteredList.setPredicate(clients -> {
            if (filterText.isEmpty()) {
                return true;
            } else {
                String fieldValue;
                switch (fieldType) {
                    case "ActNo":
                        fieldValue = clients.getTblindex01().toLowerCase();
                        break;
                    case "ActTitle":
                        fieldValue = clients.getTblindex02().toLowerCase();
                        break;
                    case "Person":
                        fieldValue = clients.getTblindex05().toLowerCase();
                        break;
                    case "Depart":
                        fieldValue = clients.getTblindex06().toLowerCase();
                        break;
                    default:
                        return false;
                }
                return fieldValue.contains(filterText);
            }
        });
        tblViewActApproval.setItems(filteredList);
        if (filteredList.isEmpty()) {
            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
        }
    }

    private void loadActApprovalTable() {
        actApprovalData.clear();
        double lnTotalBudg = 0;
//        if (oTrans.loadActForApproval()) {
//            for (poCtr = 0; poCtr <= oTrans.getItemList().size() - 1; poCtr++) {
//                // Iterate over the data and count the approved item
//                String lsAmountString = oTrans.getDetail(poCtr, "nPropBdgt").toString();
//                // Convert the amount to a decimal value
//                double lnAmount = Double.parseDouble(lsAmountString);
//                // Format the decimal value with decimal separators
//                DecimalFormat loDecimalFormat = new DecimalFormat("#,##0.0");
//                String lsFormattedAmount = loDecimalFormat.format(lnAmount);
//                String res = oTrans.getDetail(poCtr, "sEventTyp").toString();
//                if (res.equals("sal")) {
//                    res = "SALES";
//                }
//                if (res.equals("pro")) {
//                    res = "PROMO";
//                }
//                if (res.equals("eve")) {
//                    res = "EVENT";
//                }
//
//                actApprovalData.add(new ModelActivityApproval(
//                        String.valueOf(poCtr),
//                        oTrans.getDetail(poCtr, "cTranStat").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "sActvtyID").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "sActTitle").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "sActDescx").toString().toUpperCase(),
//                        res.toUpperCase(),
//                        oTrans.getDetail(poCtr, "dDateFrom").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "dDateThru").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "sLocation").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "sCompnynx").toString().toUpperCase(),
//                        oTrans.getDetail(poCtr, "sActSrcex").toString().toUpperCase()
//                ));
//                lnTotalBudg = lnTotalBudg + Double.parseDouble(oTrans.getDetail(poCtr, "nPropBdgt").toString());
//            }
//            lbTotalBudget.setText((CommonUtils.NumberFormat((Number) lnTotalBudg, "₱ " + "#,##0.00")));
//            tblViewActApproval.setItems(actApprovalData);
//        }

    }

    private void initActApprovalTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblRow"));  //Row
        // Set up listener for "Select All" checkbox
        tblindexselect.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblViewActApproval.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblViewActApproval.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean lbNewValue = selectAllCheckBox.isSelected();
            tblViewActApproval.getItems().forEach(item -> item.getSelect().setSelected(lbNewValue));
        });
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(cellData -> {
            // Get the data for the current row
            ModelActivityApproval rowData = cellData.getValue();

            // Get the values you want to concatenate
            String value1 = rowData.getTblindex03(); //from date
            String value2 = rowData.getTblindex10(); //to date

            // Concatenate the values
            String concatenatedValue = value1 + " - " + value2;

            // Create a new ObservableValue containing the concatenated value
            ObservableValue<String> observableValue = new SimpleStringProperty(concatenatedValue);

            return observableValue;
        });
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindex09.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
    }
}
