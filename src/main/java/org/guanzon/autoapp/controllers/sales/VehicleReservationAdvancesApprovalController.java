package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.sales.InquiryVehicleSalesAdvances;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleReservationAdvancesApprovalController implements Initializable, ScreenInterface, GApprovalInterface {

    private GRider oApp;
    private Inquiry oTrans;
    private final String pxeModuleName = "Vehicle Sales Advances Approval";
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.0");
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("SLIP NO",
            "TYPE",
            "SLIP DATE",
            "CUSTOMER NAME");
    private ObservableList<InquiryVehicleSalesAdvances> poReservApprovalData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnRefresh, btnFilter, btnClose;
    @FXML
    private TableView<InquiryVehicleSalesAdvances> tblVhclApproval;
    @FXML
    private TableColumn<InquiryVehicleSalesAdvances, String> tblindex01;
    @FXML
    private TableColumn<InquiryVehicleSalesAdvances, String> tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08;
    @FXML
    private TableColumn<InquiryVehicleSalesAdvances, Boolean> tblindex02;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private ComboBox<String> comboBoxFilter;
    @FXML
    private Label lblFrom, lblTo;
    @FXML
    private DatePicker datePickerFrom, datePickerTo;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Inquiry(oApp, false, oApp.getBranchCode());
        comboBoxFilter.setItems(cComboFilter);
        CustomCommonUtil.setCapsLockBehavior(txtFieldSearch);
        initLoadTable();
        loadTable();
        initButtonsClick();
        initFields();
    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnApproved, btnClose, btnFilter, btnRefresh);
        buttons.forEach(e -> e.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }

                break;
            case "btnFilter":
                String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
                FilteredList<InquiryVehicleSalesAdvances> filteredData = new FilteredList<>(poReservApprovalData, p -> true);
                switch (lsSelectedFilter) {
                    case "SLIP DATE":
                        if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null) {
                            LocalDate fromDate = datePickerFrom.getValue();
                            LocalDate toDate = datePickerTo.getValue();
                            filteredData.setPredicate(item -> {
                                LocalDate itemDate;
                                itemDate = CustomCommonUtil.strToDate(item.getTblindex04());
                                return (itemDate != null && !itemDate.isBefore(fromDate) && !itemDate.isAfter(toDate));
                            });
                        } else {
                            ShowMessageFX.Information(null, "Filter Error", "Please select both a 'From' and 'To' date.");
                            selectAllCheckBox.setSelected(false);
                        }
                        break;
                    case "SLIP NO":
                        String lsSlipNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsSlipNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex02().toLowerCase().contains(lsSlipNoSearch)
                            );
                        }
                        break;
                    case "TYPE":
                        String lsTypeSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsTypeSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex03().toLowerCase().contains(lsTypeSearch));
                        }
                        break;
                    case "CUSTOMER NAME":
                        String lsCustomSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsCustomSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex05().toLowerCase().contains(lsCustomSearch));
                        }
                        break;
                }
                int rowNumber = 1;
                for (InquiryVehicleSalesAdvances item : filteredData) {
                    item.setTblindex01(String.valueOf(rowNumber)); // Reset the row number for filtered items
                    rowNumber++;
                }
                tblVhclApproval.setItems(filteredData);
                if (filteredData.isEmpty()) {
                    ShowMessageFX.Information(null, "No Records", "No records found for the selected criteria.");
                    selectAllCheckBox.setSelected(false);
                }
                break;
            case "btnApproved":
                ObservableList<InquiryVehicleSalesAdvances> selectedItems = FXCollections.observableArrayList();

                for (InquiryVehicleSalesAdvances item : tblVhclApproval.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                        System.out.println("Selected size: " + selectedItems.size());
                    }
                }

                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected.");
                    return;
                }

                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {
                    return;
                }

                int lnApprovedCount = 0;
                String lsMessage = "";

                for (InquiryVehicleSalesAdvances selectedItem : selectedItems) {
                    String lsSlipNo = selectedItem.getTblindex02();

                    InquiryVehicleSalesAdvances originalItem = poReservApprovalData.stream()
                            .filter(item -> item.getTblindex02().equals(lsSlipNo))
                            .findFirst()
                            .orElse(null);

                    if (originalItem != null) {
                        int originalIndex = poReservApprovalData.indexOf(originalItem);
                        JSONObject loJSON = oTrans.approveReservation(originalIndex);

                        if (!"error".equals((String) loJSON.get("result"))) {
                            lsMessage = "Approved successfully.";
                            lnApprovedCount++;
                        } else {
                            lsMessage = "Approve failed, Please try again or contact support.";
                        }
                    }
                }

                if (lnApprovedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, lsMessage);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, lsMessage);
                }
                loadTable();
                selectAllCheckBox.setSelected(false);
                tblVhclApproval.getItems().removeAll(selectedItems);
                break;
            case "btnRefresh":
                tblVhclApproval.refresh();
                datePickerFrom.setValue(null);
                datePickerTo.setValue(null);
                txtFieldSearch.setText("");
                selectAllCheckBox.setSelected(false);
                loadTable();
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    @Override
    public void loadTable() {
        poReservApprovalData.clear();
        String lsSlipNoxx = "";
        String lsSlipType = "";
        String lsSlipDate = "";
        String lsCustName = "";
        String lsAmountxx = "";
        String lsStatusxx = "";
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.loadReservationForApproval();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTrans.getReservationList().size() - 1; lnCtr++) {
                if (oTrans.getReservationModel().getDetailModel(lnCtr).getReferNo() != null) {
                    lsSlipNoxx = String.valueOf(oTrans.getReservationModel().getDetailModel(lnCtr).getReferNo());
                }
                if (oTrans.getReservationModel().getDetailModel(lnCtr).getResrvTyp() != null) {
                    switch (oTrans.getReservationModel().getDetailModel(lnCtr).getResrvTyp()) {
                        case "0":
                            lsSlipType = "RESERVATION";
                            break;
                        case "1":
                            lsSlipType = "DEPOSIT";
                            break;
                        case "2":
                            lsSlipType = "SAFEGUARD DUTY";
                            break;
                    }
                }
                if (oTrans.getReservationModel().getDetailModel(lnCtr).getTransactDte() != null) {
                    lsSlipDate = CustomCommonUtil.xsDateShort(oTrans.getReservationModel().getDetailModel(lnCtr).getTransactDte());
                }
                if (oTrans.getReservationModel().getDetailModel(lnCtr).getCompnyNm() != null) {
                    lsCustName = String.valueOf(oTrans.getReservationModel().getDetailModel(lnCtr).getCompnyNm());
                }
                if (oTrans.getReservationModel().getDetailModel(lnCtr).getAmount() != null) {
                    lsAmountxx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getReservationModel().getDetailModel(lnCtr).getAmount())));
                }
                if (oTrans.getReservationModel().getDetailModel(lnCtr).getTranStat() != null) {
                    switch (oTrans.getReservationModel().getDetailModel(lnCtr).getTranStat()) {
                        case TransactionStatus.STATE_OPEN:
                            lsStatusxx = "For Approval";
                            break;
                        case TransactionStatus.STATE_CLOSED:
                            lsStatusxx = "Approved";
                            break;
                        case TransactionStatus.STATE_CANCELLED:
                            lsStatusxx = "Cancelled";
                            break;
                        case TransactionStatus.STATE_POSTED:
                            lsStatusxx = "Posted";
                            break;
                    }
                }
                poReservApprovalData.add(new InquiryVehicleSalesAdvances(
                        String.valueOf(lnCtr + 1),
                        lsSlipNoxx.toUpperCase(),
                        lsSlipType.toUpperCase(),
                        lsSlipDate.toUpperCase(),
                        lsCustName.toUpperCase(),
                        lsAmountxx,
                        lsStatusxx.toUpperCase(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""));

            }
            lsSlipNoxx = "";
            lsSlipType = "";
            lsSlipDate = "";
            lsCustName = "";
            lsAmountxx = "";
            lsStatusxx = "";
            tblVhclApproval.setItems(poReservApprovalData);
        }
    }

    @Override
    public void initLoadTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));

        tblVhclApproval.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblVhclApproval.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean lbNewValue = selectAllCheckBox.isSelected();
            tblVhclApproval.getItems().forEach(item -> item.getSelect().setSelected(lbNewValue));
        });
        tblVhclApproval.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVhclApproval.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    @Override
    public void initFields() {
        CustomCommonUtil.setVisible(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnFilter);
        CustomCommonUtil.setManaged(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnFilter);
        comboBoxFilter.setOnAction(e -> {
            CustomCommonUtil.setVisible(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnFilter);
            CustomCommonUtil.setManaged(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnFilter);
            String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
            switch (lsSelectedFilter) {
                case "SLIP DATE":
                    CustomCommonUtil.setVisible(true, datePickerFrom, datePickerTo, btnFilter);
                    CustomCommonUtil.setManaged(true, datePickerFrom, datePickerTo, btnFilter);
                    txtFieldSearch.setText("");
                    tblVhclApproval.setItems(poReservApprovalData);
                    break;
                case "SLIP NO":
                case "TYPE":
                case "CUSTOMER NAME":
                    txtFieldSearch.setText("");
                    CustomCommonUtil.setVisible(true, txtFieldSearch, btnFilter);
                    CustomCommonUtil.setManaged(true, txtFieldSearch, btnFilter);
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblVhclApproval.setItems(poReservApprovalData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });

    }
}
