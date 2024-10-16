/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.insurance;

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
import org.guanzon.auto.main.insurance.InsurancePolicyProposal;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.models.insurance.InsuranceProposalApproval;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class InsuranceProposalApprovalController implements Initializable, ScreenInterface, GApprovalInterface {

    private GRider oApp;
    private final String pxeModuleName = "Insurance Proposal Approval";
    private InsurancePolicyProposal oTrans;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.0");
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private ObservableList<InsuranceProposalApproval> poInsProposalData = FXCollections.observableArrayList();
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("INSURANCE PROPOSAL DATE", "PROPOSAL NO", "INSURANCE COMPANY", "CUSTOMER NAME", "PLATE/CSNO");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose, btnApprove, btnDisapprove, btnFilter, btnRefresh;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableView<InsuranceProposalApproval> tblInsProposApproval;
    @FXML
    private TableColumn<InsuranceProposalApproval, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09, tblindex10;
    @FXML
    private TableColumn<InsuranceProposalApproval, Boolean> tblindex02;
    @FXML
    private ComboBox<String> comboBoxFilter;
    @FXML
    private DatePicker datePickerFrom, datePickerTo;
    @FXML
    private Label lblTo, lblFrom;
    @FXML
    private TextField txtFieldSearch;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InsurancePolicyProposal(oApp, false, oApp.getBranchCode());
        comboBoxFilter.setItems(cComboFilter);
        CustomCommonUtil.setCapsLockBehavior(txtFieldSearch);
        initLoadTable();
        loadTable();
        initButtonsClick();
        initFields();
    }

    @Override
    public void loadTable() {
        poInsProposalData.clear();
        String lsInsDatex = "";
        String lsInsProNo = "";
        String lsPolTypex = "";
        String lsAppTypex = "";
        String lsInsCompy = "";
        String lsAmountxx = "";
        String lsCustName = "";
        String lsPltCSNox = "";
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.loadProposalForApproval();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTrans.getProposalList().size() - 1; lnCtr++) {

                if (oTrans.getProposalModel().getDetailModel(lnCtr).getTransactDte() != null) {
                    lsInsDatex = CustomCommonUtil.xsDateShort(oTrans.getProposalModel().getDetailModel(lnCtr).getTransactDte());
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getTransNo() != null) {
                    lsInsProNo = String.valueOf(oTrans.getProposalModel().getDetailModel(lnCtr).getTransNo());
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getInsTypID() != null) {
                    switch (oTrans.getProposalModel().getDetailModel(lnCtr).getInsTypID()) {
                        case "y":
                            lsPolTypex = "TPL";
                            break;
                        case "c":
                            lsPolTypex = "COMPREHENSIVE";
                            break;
                        case "b":
                            lsPolTypex = "TPL AND COMPREHENSIVE";
                            break;
                    }
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getIsNew() != null) {
                    switch (oTrans.getProposalModel().getDetailModel(lnCtr).getIsNew()) {
                        case "y":
                            lsAppTypex = "NEW";
                            break;
                        case "n":
                            lsAppTypex = "RENEW";
                            break;
                    }
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getInsurNme() != null && oTrans.getProposalModel().getDetailModel(lnCtr).getBrInsNme() != null) {
                    lsInsCompy = oTrans.getProposalModel().getDetailModel(lnCtr).getInsurNme() + " " + oTrans.getProposalModel().getDetailModel(lnCtr).getBrInsNme();
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getTotalAmt() != null) {
                    lsAmountxx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getProposalModel().getDetailModel(lnCtr).getTotalAmt())));
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getOwnrNm() != null) {
                    lsCustName = String.valueOf(oTrans.getProposalModel().getDetailModel(lnCtr).getOwnrNm());
                }
                if (oTrans.getProposalModel().getDetailModel(lnCtr).getPlateNo() != null && oTrans.getProposalModel().getDetailModel(lnCtr).getCSNo() != null) {
                    lsPltCSNox = String.valueOf(oTrans.getProposalModel().getDetailModel(lnCtr).getPlateNo()) + "/" + String.valueOf(oTrans.getProposalModel().getDetailModel(lnCtr).getCSNo());
                } else {
                    lsPltCSNox = String.valueOf(oTrans.getProposalModel().getDetailModel(lnCtr).getCSNo());
                }
                poInsProposalData.add(new InsuranceProposalApproval(
                        String.valueOf(lnCtr + 1),
                        lsInsDatex,
                        lsInsProNo.toUpperCase(),
                        lsPolTypex.toUpperCase(),
                        lsAppTypex.toUpperCase(),
                        lsInsCompy.trim().toUpperCase(),
                        lsAmountxx,
                        lsCustName.toUpperCase(),
                        lsPltCSNox.toUpperCase(),
                        "",
                        ""));
                tblInsProposApproval.setItems(poInsProposalData);
            }
            lsInsDatex = "";
            lsInsProNo = "";
            lsPolTypex = "";
            lsAppTypex = "";
            lsInsCompy = "";
            lsAmountxx = "";
            lsCustName = "";
            lsPltCSNox = "";
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
        tblindex09.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindex10.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        tblInsProposApproval.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblInsProposApproval.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblInsProposApproval.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblInsProposApproval.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblInsProposApproval.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnClose, btnApprove, btnDisapprove, btnFilter, btnRefresh);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnPrint":
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, "Follow Up List");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }

                break;
            case "btnFilter":
                String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
                FilteredList<InsuranceProposalApproval> filteredData = new FilteredList<>(poInsProposalData, p -> true); // Start with all items
                switch (lsSelectedFilter) {
                    case "INSURANCE PROPOSAL DATE":
                        if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null) {
                            LocalDate fromDate = datePickerFrom.getValue();
                            LocalDate toDate = datePickerTo.getValue();

                            filteredData.setPredicate(item -> {
                                LocalDate itemDate;
                                itemDate = CustomCommonUtil.strToDate(item.getTblindex02());
                                return (itemDate != null && !itemDate.isBefore(fromDate) && !itemDate.isAfter(toDate));
                            });
                        } else {
                            ShowMessageFX.Information(null, "Filter Error", "Please select both a 'From' and 'To' date.");
                            selectAllCheckBox.setSelected(false);
                        }
                        break;
                    case "PROPOSAL NO":
                        String policyNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (policyNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex03().toLowerCase().contains(policyNoSearch)
                            );
                        }
                        break;
                    case "INSURANCE COMPANY":
                        String companySearch = txtFieldSearch.getText().toLowerCase();
                        if (companySearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex06().toLowerCase().contains(companySearch));
                        }
                        break;
                    case "CUSTOMER NAME":
                        String customerNameSearch = txtFieldSearch.getText().toLowerCase();
                        if (customerNameSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex08().toLowerCase().contains(customerNameSearch));
                        }
                        break;
                    case "PLATE/CSNO":
                        String plateNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (plateNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex09().toLowerCase().contains(plateNoSearch));
                        }
                        break;

                }

                int rowNumber = 1;
                for (InsuranceProposalApproval item : filteredData) {
                    item.setTblindex01(String.valueOf(rowNumber));
                    rowNumber++;
                }
                tblInsProposApproval.setItems(filteredData);
                if (filteredData.isEmpty()) {
                    ShowMessageFX.Information(null, "No Records", "No records found for the selected criteria.");
                    selectAllCheckBox.setSelected(false);
                }
                break;
            case "btnRefresh":
                tblInsProposApproval.refresh();
                txtFieldSearch.clear();
                datePickerFrom.setValue(null);
                datePickerTo.setValue(null);
                selectAllCheckBox.setSelected(false);
                loadTable();
                break;
            case "btnApprove":
            case "btnDisapprove":
                ObservableList<InsuranceProposalApproval> selectedItems = FXCollections.observableArrayList();

                for (InsuranceProposalApproval item : tblInsProposApproval.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
                    }
                }

                if (selectedItems.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected.");
                    return;
                }

                int lnApprovedCount = 0;
                int lnDisApprvCount = 0;
                String lsMessage = "";
                if (lsButton.equals("btnApprove")) {
                    if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to approve?")) {
                        return;
                    }
                    for (InsuranceProposalApproval selectedItem : selectedItems) {
                        String lsProposalNo = selectedItem.getTblindex04();

                        InsuranceProposalApproval originalItem = poInsProposalData.stream()
                                .filter(item -> item.getTblindex04().equals(lsProposalNo))
                                .findFirst()
                                .orElse(null);

                        if (originalItem != null) {
                            int originalIndex = poInsProposalData.indexOf(originalItem);
                            JSONObject loJSON = oTrans.approveProposal(originalIndex);

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
                } else {
                    if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to disapprove?")) {
                        return;
                    }
                    for (InsuranceProposalApproval selectedItem : selectedItems) {
                        String lsProposalNo = selectedItem.getTblindex04();

                        InsuranceProposalApproval originalItem = poInsProposalData.stream()
                                .filter(item -> item.getTblindex04().equals(lsProposalNo))
                                .findFirst()
                                .orElse(null);

                        if (originalItem != null) {
                            int originalIndex = poInsProposalData.indexOf(originalItem);
                            JSONObject loJSON = oTrans.disapproveProposal(originalIndex);

                            if (!"error".equals((String) loJSON.get("result"))) {
                                lsMessage = "Disapproved successfully.";
                                lnDisApprvCount++;
                            } else {
                                lsMessage = "Disapproved failed, Please try again or contact support.";
                            }
                        }
                    }

                    if (lnDisApprvCount > 0) {
                        ShowMessageFX.Information(null, pxeModuleName, lsMessage);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, lsMessage);
                    }
                }

                loadTable();
                selectAllCheckBox.setSelected(false);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
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
                case "INSURANCE PROPOSAL DATE":
                    CustomCommonUtil.setVisible(true, datePickerFrom, datePickerTo, btnFilter);
                    CustomCommonUtil.setManaged(true, datePickerFrom, datePickerTo, btnFilter);
                    txtFieldSearch.setText("");
                    tblInsProposApproval.setItems(poInsProposalData);
                    break;
                case "PROPOSAL NO":
                case "INSURANCE COMPANY":
                case "CUSTOMER NAME":
                case "PLATE/CSNO":
                    CustomCommonUtil.setVisible(true, txtFieldSearch, btnFilter);
                    CustomCommonUtil.setManaged(true, txtFieldSearch, btnFilter);
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblInsProposApproval.setItems(poInsProposalData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });
    }
}
