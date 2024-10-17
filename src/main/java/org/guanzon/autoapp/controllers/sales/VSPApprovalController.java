/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.sales.VSPApproval;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VSPApprovalController implements Initializable, ScreenInterface, GApprovalInterface {

    private GRider oApp;
    private VehicleSalesProposal oTrans;
    private final String pxeModuleName = "VSP Approval"; //Form Title
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.0");
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("VSP NO",
            "VSP DATE",
            "TARGET DELIVERY DATE",
            "BUYING CUSTOMER NAME",
            "PLATE/CS NO",
            "BRANCH");
    private ObservableList<VSPApproval> poVSPApprovalData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnRefresh, btnFilter, btnClose;
    @FXML
    private TableView<VSPApproval> tblVhclApproval;
    @FXML
    private TableColumn<VSPApproval, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08,
            tblindex09, tblindex10, tblindex11, tblindex12, tblindex13, tblindex14, tblindex15, tblindex16, tblindex17;
    @FXML
    private TableColumn<VSPApproval, Boolean> tblindex02;
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
        oTrans = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
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
                FilteredList<VSPApproval> filteredData = new FilteredList<>(poVSPApprovalData, p -> true);
                switch (lsSelectedFilter) {
                    case "VSP DATE":
                        if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null) {
                            LocalDate fromDate = datePickerFrom.getValue();
                            LocalDate toDate = datePickerTo.getValue();
                            filteredData.setPredicate(item -> {
                                LocalDate itemDate;
                                itemDate = CustomCommonUtil.strToDate(item.getTblindex03());
                                return (itemDate != null && !itemDate.isBefore(fromDate) && !itemDate.isAfter(toDate));
                            });
                        } else {
                            ShowMessageFX.Information(null, "Filter Error", "Please select both a 'From' and 'To' date.");
                            selectAllCheckBox.setSelected(false);
                        }
                        break;
                    case "TARGET DELIVERY DATE":
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
                    case "VSP NO":
                        String lsVSPNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsVSPNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex02().toLowerCase().contains(lsVSPNoSearch)
                            );
                        }
                        break;
                    case "BUYING CUSTOMER NAME":
                        String lsCustomNameSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsCustomNameSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex05().toLowerCase().contains(lsCustomNameSearch));
                        }
                        break;
                    case "PLATE/CS NO":
                        String lsPlateCSSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsPlateCSSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex07().toLowerCase().contains(lsPlateCSSearch));
                        }
                        break;
                    case "BRANCH":
                        String lsBranchSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsBranchSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex08().toLowerCase().contains(lsBranchSearch));
                        }
                        break;
                }
                int rowNumber = 1;
                for (VSPApproval item : filteredData) {
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
                ObservableList<VSPApproval> selectedItems = FXCollections.observableArrayList();

                for (VSPApproval item : tblVhclApproval.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItems.add(item);
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

                for (VSPApproval selectedItem : selectedItems) {
                    String lsVspNo = selectedItem.getTblindex02();

                    VSPApproval originalItem = poVSPApprovalData.stream()
                            .filter(item -> item.getTblindex02().equals(lsVspNo))
                            .findFirst()
                            .orElse(null);

                    if (originalItem != null) {
                        int originalIndex = poVSPApprovalData.indexOf(originalItem);
                        JSONObject loJSON = oTrans.approveVSP(originalIndex);

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
        poVSPApprovalData.clear();
        String lsVSPNoxxxxxx = "";
        String lsVSPDatexxxx = "";
        String lsTargetDatex = "";
        String lsBuyerNamexx = "";
        String lsCoBuyerName = "";
        String lsPlateCSNoxx = "";
        String lsBranchNamex = "";
        String lsVSPStatusxx = "";
        String lsGrossLaborx = "0.00";
        String lsGrossAccesx = "0.00";
        String lsGrossAmntxx = "0.00";
        String lsReserveAmnt = "0.00";
        String lsDownPayment = "0.00";
        String lsNetPaymentx = "0.00";
        String lsOverAllDisc = "0.00";
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.loadVSPForApproval();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTrans.getVSPList().size() - 1; lnCtr++) {
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getVSPNO() != null) {
                    lsVSPNoxxxxxx = String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getVSPNO());
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getTransactDte() != null) {
                    lsVSPDatexxxx = CustomCommonUtil.xsDateShort(oTrans.getVSPModel().getDetailModel(lnCtr).getTransactDte());
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getDelvryDt() != null) {
                    lsTargetDatex = CustomCommonUtil.xsDateShort(oTrans.getVSPModel().getDetailModel(lnCtr).getDelvryDt());
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getBuyCltNm() != null) {
                    lsBuyerNamexx = String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getBuyCltNm());
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getCoCltNm() != null) {
                    lsCoBuyerName = String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getCoCltNm());
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getPlateNo() != null && oTrans.getVSPModel().getDetailModel(lnCtr).getCSNo() != null) {
                    lsPlateCSNoxx = String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getPlateNo()) + "/" + String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getCSNo());
                } else {
                    lsPlateCSNoxx = String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getCSNo());
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getBranchNm() != null) {
                    lsBranchNamex = String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getBranchNm());
                }
                switch (oTrans.getVSPModel().getDetailModel(lnCtr).getTranStat()) {
                    case TransactionStatus.STATE_OPEN:
                        lsVSPStatusxx = "Active";
                        break;
                    case TransactionStatus.STATE_CLOSED:
                        lsVSPStatusxx = "Approved";
                        break;
                    case TransactionStatus.STATE_CANCELLED:
                        lsVSPStatusxx = "Cancelled";
                        break;
                    case TransactionStatus.STATE_POSTED:
                        lsVSPStatusxx = "Posted";
                        break;
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getLaborAmt() != null) {
                    lsGrossLaborx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getLaborAmt())));
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getAccesAmt() != null) {
                    lsGrossAccesx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getAccesAmt())));
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getTranTotl() != null) {
                    lsGrossAmntxx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getTranTotl())));
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getResrvFee() != null) {
                    lsReserveAmnt = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getResrvFee())));
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getAmtPaid() != null) {
                    lsDownPayment = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getAmtPaid())));
                }
                if (oTrans.getVSPModel().getDetailModel(lnCtr).getNetTTotl() != null) {
                    lsNetPaymentx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSPModel().getDetailModel(lnCtr).getNetTTotl())));
                }

                if (oTrans.getVSPModel().getDetailModel(lnCtr).getAddlDsc() != null
                        && oTrans.getVSPModel().getDetailModel(lnCtr).getPromoDsc() != null
                        && oTrans.getVSPModel().getDetailModel(lnCtr).getFleetDsc() != null
                        && oTrans.getVSPModel().getDetailModel(lnCtr).getSPFltDsc() != null
                        && oTrans.getVSPModel().getDetailModel(lnCtr).getBndleDsc() != null
                        && oTrans.getVSPModel().getDetailModel(lnCtr).getToLabDsc() != null
                        && oTrans.getVSPModel().getDetailModel(lnCtr).getToPrtDsc() != null) {
                    double lnCashDiscx = oTrans.getVSPModel().getDetailModel(lnCtr).getAddlDsc().doubleValue();
                    double lnPromoDisc = oTrans.getVSPModel().getDetailModel(lnCtr).getPromoDsc().doubleValue();
                    double lnSplFlDisc = oTrans.getVSPModel().getDetailModel(lnCtr).getFleetDsc().doubleValue();
                    double lnStdFlDisc = oTrans.getVSPModel().getDetailModel(lnCtr).getSPFltDsc().doubleValue();
                    double lnBndleDisc = oTrans.getVSPModel().getDetailModel(lnCtr).getBndleDsc().doubleValue();
                    double lnLaborDisc = oTrans.getVSPModel().getDetailModel(lnCtr).getToLabDsc().doubleValue();
                    double lnAccesDisc = oTrans.getVSPModel().getDetailModel(lnCtr).getToPrtDsc().doubleValue();
                    double lnDisc = lnCashDiscx + lnPromoDisc + lnSplFlDisc + lnStdFlDisc + lnBndleDisc + lnLaborDisc + lnAccesDisc;
                    lsOverAllDisc = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lnDisc)));
                }
                poVSPApprovalData.add(new VSPApproval(
                        String.valueOf(lnCtr + 1),
                        lsVSPNoxxxxxx.toUpperCase(),
                        lsVSPDatexxxx.toUpperCase(),
                        lsTargetDatex.toUpperCase(),
                        lsBuyerNamexx.toUpperCase(),
                        lsCoBuyerName.toUpperCase(),
                        lsPlateCSNoxx.toUpperCase(),
                        lsBranchNamex.toUpperCase(),
                        lsVSPStatusxx.toUpperCase(),
                        lsGrossLaborx,
                        lsGrossAccesx,
                        lsGrossAmntxx,
                        lsReserveAmnt,
                        lsDownPayment,
                        lsNetPaymentx,
                        lsOverAllDisc,
                        "",
                        ""));
                lsVSPNoxxxxxx = "";
                lsVSPDatexxxx = "";
                lsTargetDatex = "";
                lsBuyerNamexx = "";
                lsCoBuyerName = "";
                lsPlateCSNoxx = "";
                lsBranchNamex = "";
                lsVSPStatusxx = "";
                lsGrossLaborx = "";
                lsGrossAccesx = "";
                lsGrossAmntxx = "";
                lsReserveAmnt = "";
                lsDownPayment = "";
                lsNetPaymentx = "";
                lsOverAllDisc = "";

            }
            tblVhclApproval.setItems(poVSPApprovalData);
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
        tblindex11.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        tblindex12.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        tblindex13.setCellValueFactory(new PropertyValueFactory<>("tblindex12"));
        tblindex14.setCellValueFactory(new PropertyValueFactory<>("tblindex13"));
        tblindex15.setCellValueFactory(new PropertyValueFactory<>("tblindex14"));
        tblindex16.setCellValueFactory(new PropertyValueFactory<>("tblindex15"));
        tblindex17.setCellValueFactory(new PropertyValueFactory<>("tblindex16"));

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
                case "VSP DATE":
                case "TARGET DELIVERY DATE":
                    CustomCommonUtil.setVisible(true, datePickerFrom, datePickerTo, btnFilter);
                    CustomCommonUtil.setManaged(true, datePickerFrom, datePickerTo, btnFilter);
                    txtFieldSearch.setText("");
                    tblVhclApproval.setItems(poVSPApprovalData);
                    break;
                case "VSP NO":
                case "BUYING CUSTOMER NAME":
                case "CUSTOMER NAME":
                case "PLATE/CS NO":
                case "BRANCH":
                    txtFieldSearch.setText("");
                    CustomCommonUtil.setVisible(true, txtFieldSearch, btnFilter);
                    CustomCommonUtil.setManaged(true, txtFieldSearch, btnFilter);
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblVhclApproval.setItems(poVSPApprovalData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });

    }
}
