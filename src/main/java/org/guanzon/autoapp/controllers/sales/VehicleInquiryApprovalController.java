package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
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
import org.guanzon.auto.main.sales.Inquiry;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.sales.InquiryApproval;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleInquiryApprovalController implements Initializable, ScreenInterface, GApprovalInterface {

    private GRider oApp;
    private Inquiry oTrans;
    private final String pxeModuleName = "Vehicle Inquiry Approval"; //Form Title
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("INQUIRY ID", "INQUIRY DATE", "CUSTOMER NAME", "SALES EXECUTIVE",
            "BRANCH", "INQUIRY STATUS");
    private ObservableList<InquiryApproval> poInqApprovalData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnRefresh, btnFilter, btnClose;
    @FXML
    private TableView<InquiryApproval> tblVhclApproval;
    @FXML
    private TableColumn<InquiryApproval, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08;
    @FXML
    private TableColumn<InquiryApproval, Boolean> tblindex02;
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
                FilteredList<InquiryApproval> filteredData = new FilteredList<>(poInqApprovalData, p -> true);
                switch (lsSelectedFilter) {
                    case "INQUIRY DATE":
                        if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null) {
                            LocalDate fromDate = datePickerFrom.getValue();
                            LocalDate toDate = datePickerTo.getValue();
                            filteredData.setPredicate(item -> {
                                LocalDate itemDate;
                                itemDate = CustomCommonUtil.strToDate(item.getTblindex03());
                                return (itemDate != null && !itemDate.isBefore(fromDate) && !itemDate.isAfter(toDate));
                            });

                            if (filteredData.isEmpty()) {
                                ShowMessageFX.Information(null, "No Records", "No records found for the selected date range.");
                            }
                        } else {
                            ShowMessageFX.Information(null, "Filter Error", "Please select both a 'From' and 'To' date.");
                            selectAllCheckBox.setSelected(false);
                        }
                        break;
                    case "INQUIRY ID":
                        String lsInquiryIDSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsInquiryIDSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex02().toLowerCase().contains(lsInquiryIDSearch)
                            );
                        }
                        break;

                    case "CUSTOMER NAME":
                        String lsCustomNameSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsCustomNameSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex04().toLowerCase().contains(lsCustomNameSearch));
                        }
                        break;

                    case "SALES EXECUTIVE":
                        String lsSalesExeSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsSalesExeSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex05().toLowerCase().contains(lsSalesExeSearch));
                        }
                        break;
                    case "BRANCH":
                        String lsBranchSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsBranchSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex06().toLowerCase().contains(lsBranchSearch));
                        }
                        break;
                    case "INQUIRY STATUS":
                        String lsInqStatusSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsInqStatusSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex07().toLowerCase().contains(lsInqStatusSearch));
                        }
                        break;
                }
                int rowNumber = 1;
                for (InquiryApproval item : filteredData) {
                    item.setTblindex01(String.valueOf(rowNumber));
                    rowNumber++;
                }
                tblVhclApproval.setItems(filteredData);
                if (filteredData.isEmpty()) {
                    ShowMessageFX.Information(null, "No Records", "No records found for the selected criteria.");
                    selectAllCheckBox.setSelected(false);
                }
                break;
            case "btnApproved":
                ObservableList<InquiryApproval> selectedItems = FXCollections.observableArrayList();

                for (InquiryApproval item : tblVhclApproval.getItems()) {
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

                for (InquiryApproval selectedItem : selectedItems) {
                    String inquiryID = selectedItem.getTblindex02();
                    InquiryApproval originalItem = poInqApprovalData.stream()
                            .filter(item -> item.getTblindex02().equals(inquiryID))
                            .findFirst()
                            .orElse(null);
                    if (originalItem != null) {
                        int originalIndex = poInqApprovalData.indexOf(originalItem);
                        JSONObject loJSON = oTrans.approveInquiry(originalIndex);

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
        poInqApprovalData.clear();
        String lsInquiryID = "";
        String lsDate = "";
        String lsCustomerName = "";
        String lsSalesExe = "";
        String lsBranch = "";
        String lsStatus = "";
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.loadInquiryForApproval();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTrans.getInquiryList().size() - 1; lnCtr++) {
                if (oTrans.getInquiryModel().getDetailModel(lnCtr).getInqryID() != null) {
                    lsInquiryID = String.valueOf(oTrans.getInquiryModel().getDetailModel(lnCtr).getInqryID());
                }
                if (oTrans.getInquiryModel().getDetailModel(lnCtr).getTransactDte() != null) {
                    lsDate = CustomCommonUtil.xsDateShort(oTrans.getInquiryModel().getDetailModel(lnCtr).getTransactDte());
                }
                if (oTrans.getInquiryModel().getDetailModel(lnCtr).getClientNm() != null) {
                    lsCustomerName = String.valueOf(oTrans.getInquiryModel().getDetailModel(lnCtr).getClientNm());
                }
                if (oTrans.getInquiryModel().getDetailModel(lnCtr).getSalesExe() != null) {
                    lsSalesExe = String.valueOf(oTrans.getInquiryModel().getDetailModel(lnCtr).getSalesExe());
                }
                if (oTrans.getInquiryModel().getDetailModel(lnCtr).getBranchNm() != null) {
                    lsBranch = String.valueOf(oTrans.getInquiryModel().getDetailModel(lnCtr).getBranchNm());
                }
                if (oTrans.getInquiryModel().getDetailModel(lnCtr).getTranStat() != null) {
                    switch (oTrans.getInquiryModel().getDetailModel(lnCtr).getTranStat()) {
                        case "0":
                            lsStatus = "FOR FOLLOW-UP";
                            break;
                        case "1":
                            lsStatus = "ON PROCESS";
                            break;
                        case "2":
                            lsStatus = "LOST SALE";
                            break;
                        case "3":
                            lsStatus = "WITH VSP";
                            break;
                        case "4":
                            lsStatus = "SOLD";
                            break;
                        case "5":
                            lsStatus = "CANCELLED";
                            break;
                    }
                }

                poInqApprovalData.add(new InquiryApproval(
                        String.valueOf(lnCtr + 1),
                        lsInquiryID.toUpperCase(),
                        lsDate.toUpperCase(),
                        lsCustomerName.toUpperCase(),
                        lsSalesExe.toUpperCase(),
                        lsBranch.toUpperCase(),
                        lsStatus.toUpperCase(),
                        "",
                        "",
                        "",
                        "",
                        ""));
            }
            lsInquiryID = "";
            lsDate = "";
            lsCustomerName = "";
            lsSalesExe = "";
            lsBranch = "";
            lsStatus = "";
            tblVhclApproval.setItems(poInqApprovalData);
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
                case "INQUIRY DATE":
                    CustomCommonUtil.setVisible(true, datePickerFrom, datePickerTo, btnFilter);
                    CustomCommonUtil.setManaged(true, datePickerFrom, datePickerTo, btnFilter);
                    txtFieldSearch.setText("");
                    tblVhclApproval.setItems(poInqApprovalData);
                    break;
                case "INQUIRY ID":
                case "CUSTOMER NAME":
                case "SALES EXECUTIVE":
                case "BRANCH":
                case "INQUIRY STATUS":
                    txtFieldSearch.setText("");
                    CustomCommonUtil.setVisible(true, txtFieldSearch, btnFilter);
                    CustomCommonUtil.setManaged(true, txtFieldSearch, btnFilter);
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblVhclApproval.setItems(poInqApprovalData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });

    }
}
