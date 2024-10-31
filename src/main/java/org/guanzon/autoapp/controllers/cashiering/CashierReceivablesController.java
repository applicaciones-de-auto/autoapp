/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.cashiering.CashierReceivables;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.cashiering.Cashier_Receivables;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ExcelExporterUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.guanzon.autoapp.FXMLDocumentController;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class CashierReceivablesController implements Initializable, ScreenInterface, GApprovalInterface {

    private GRider oApp;
    private CashierReceivables oTrans;
    private final String pxeModuleName = "Cashier Receivables"; //Form Title
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    ObservableList<String> cFormType = FXCollections.observableArrayList("CREATE ACKNOWLEDGEMENT RECEIPT", "CREATE COLLECTION RECEIPT", "CREATE SERVICE INVOICE",
            "CREATE PARTS SSALES INVOICE", "CREATE BILLING STATEMENT", "CONVERT TO SOA");
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("CAR NO",
            "CUSTOMER NAME",
            "REFERENCE NO");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private ObservableList<Cashier_Receivables> poCARData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private ComboBox<String> comboBoxFilter, comboBoxFormType;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnPrint, btnExcelExport, btnClose, btnViewTrans, btnProceed, btnRefresh, btnFilter;
    @FXML
    private DatePicker datePickerFrom, datePickerTo;
    @FXML
    private TableView<Cashier_Receivables> tblViewCAR;
    @FXML
    private TableColumn<Cashier_Receivables, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07,
            tblindex08, tblindex09, tblindex10, tblindex11, tblindex12, tblindex13, tblindex14, tblindex15, tblindex16, tblindex17,
            tblindex18, tblindex19, tblindex20, tblindex21, tblindex22, tblindex23;
    @FXML
    private TableColumn<Cashier_Receivables, Boolean> tblindex02;
    @FXML
    private CheckBox selectAllCheckBox;
    private FXMLDocumentController fdController = new FXMLDocumentController();

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new CashierReceivables(oApp, false, oApp.getBranchCode());

        datePickerFrom.setValue(LocalDate.now());
        datePickerTo.setValue(LocalDate.now());
        comboBoxFilter.setItems(cComboFilter);
        comboBoxFormType.setItems(cFormType);
        CustomCommonUtil.setCapsLockBehavior(txtFieldSearch);
        initLoadTable();
        loadTable();
        initButtonsClick();
        initFields();
    }

    @Override
    public void loadTable() {
        poCARData.clear();
        String lsCARNoxx = "";
        String lsDatexxx = "";
        String lsTypexxx = "";
        String lsCustNme = "";
        String lsRefNoxx = "";
        String lsParticu = "";
        String lsLocatnx = "";
        String lsGrsAmnt = "";
        String lsDscAmnt = "";
        String lsTrnAmnt = "";
        String lsDeAmntx = "";
        String lsPdAmntx = "";
        String lsSiNoxxx = "";
        String lsCCNoxxx = "";
        String lsPRNoxxx = "";
        String lsSOANoxx = "";
        String lsJrnNoxx = "";
        String lsTrnFrmx = "";
        String lsTrnByxx = "";
        String lsTrnToxx = "";
        String lsTrnDtex = "";

        if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null) {
            LocalDate fromDate = datePickerFrom.getValue();
            LocalDate toDate = datePickerTo.getValue();
            JSONObject loJSON = new JSONObject();
            loJSON = oTrans.loadTransaction(CustomCommonUtil.xsDateShort(fromDate),
                    CustomCommonUtil.xsDateShort(toDate));
            if ("success".equals((String) loJSON.get("result"))) {
                for (int lnCtr = 0; lnCtr <= oTrans.getMasterModel().getDetailList().size() - 1; lnCtr++) {
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getTransNo() != null) {
                        lsCARNoxx = String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getTransNo());
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getTransactDte() != null) {
                        lsDatexxx = CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getDetailModel(lnCtr).getTransactDte());
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getPayerCde() != null) {
                        lsTypexxx = String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getPayerCde());
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getPayerNme() != null) {
                        lsCustNme = String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getPayerNme());
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getReferNo() != null) {
                        lsRefNoxx = String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getReferNo());
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getSourceCD() != null) {
                        lsParticu = String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getSourceCD());
                    }
//            if (oTrans.getMasterModel().getDetailModel(lnCtr).getLocation()!= null) {
//                lsLocatnx = String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getLocation());
//            }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getGrossAmt() != null) {
                        lsGrsAmnt = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getGrossAmt())));
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getDiscAmt() != null) {
                        lsDscAmnt = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getDiscAmt())));
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getTotalAmt() != null) {
                        lsTrnAmnt = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getTotalAmt())));
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getDeductn() != null) {
                        lsDeAmntx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getDeductn())));
                    }
                    if (oTrans.getMasterModel().getDetailModel(lnCtr).getAmtPaid() != null) {
                        lsPdAmntx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getAmtPaid())));
                    }
//            if (oTrans.getMasterModel().getDetailModel(lnCtr).get() != null) {
//                lsSiNoxxx = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getDetailModel(lnCtr).getAmtPaid())));
//            }
                    poCARData.add(new Cashier_Receivables(
                            String.valueOf(lnCtr + 1),
                            lsCARNoxx,
                            lsDatexxx,
                            "",
                            lsTypexxx,
                            lsCustNme,
                            lsRefNoxx,
                            lsParticu,
                            lsLocatnx,
                            lsGrsAmnt,
                            lsDscAmnt,
                            lsTrnAmnt,
                            lsDeAmntx,
                            lsPdAmntx,
                            lsSiNoxxx,
                            lsCCNoxxx,
                            lsPRNoxxx,
                            lsSOANoxx,
                            lsJrnNoxx,
                            lsTrnFrmx,
                            lsTrnByxx,
                            lsTrnToxx,
                            lsTrnDtex,
                            ""
                    ));
                    lsCARNoxx = "";
                    lsDatexxx = "";
                    lsTypexxx = "";
                    lsCustNme = "";
                    lsRefNoxx = "";
                    lsParticu = "";
                    lsLocatnx = "";
                    lsGrsAmnt = "";
                    lsDscAmnt = "";
                    lsTrnAmnt = "";
                    lsDeAmntx = "";
                    lsPdAmntx = "";
                    lsSiNoxxx = "";
                    lsCCNoxxx = "";
                    lsPRNoxxx = "";
                    lsSOANoxx = "";
                    lsJrnNoxx = "";
                    lsTrnFrmx = "";
                    lsTrnByxx = "";
                    lsTrnToxx = "";
                    lsTrnDtex = "";
                    tblViewCAR.setItems(poCARData);
                }
            }
        }
    }

    @Override
    public void initLoadTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindex09.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        tblindex10.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        tblindex11.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        tblindex12.setCellValueFactory(new PropertyValueFactory<>("tblindex12"));
        tblindex13.setCellValueFactory(new PropertyValueFactory<>("tblindex13"));
        tblindex14.setCellValueFactory(new PropertyValueFactory<>("tblindex14"));
        tblindex15.setCellValueFactory(new PropertyValueFactory<>("tblindex15"));
        tblindex16.setCellValueFactory(new PropertyValueFactory<>("tblindex16"));
        tblindex17.setCellValueFactory(new PropertyValueFactory<>("tblindex17"));
        tblindex18.setCellValueFactory(new PropertyValueFactory<>("tblindex18"));
        tblindex19.setCellValueFactory(new PropertyValueFactory<>("tblindex19"));
        tblindex20.setCellValueFactory(new PropertyValueFactory<>("tblindex20"));
        tblindex21.setCellValueFactory(new PropertyValueFactory<>("tblindex21"));
        tblindex22.setCellValueFactory(new PropertyValueFactory<>("tblindex22"));
        tblindex23.setCellValueFactory(new PropertyValueFactory<>("tblindex23"));

        tblViewCAR.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewCAR.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllCheckBox.setSelected(true);
                } else {
                    selectAllCheckBox.setSelected(false);
                }
            });
        });
        selectAllCheckBox.setOnAction(event -> {
            boolean newValue = selectAllCheckBox.isSelected();
            tblViewCAR.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblViewCAR.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewCAR.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnPrint, btnExcelExport, btnClose,
                btnViewTrans, btnProceed, btnRefresh, btnFilter);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnFilter":
                String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
                FilteredList<Cashier_Receivables> filteredData = new FilteredList<>(poCARData, p -> true); // Start with all items
                switch (lsSelectedFilter) {
                    case "CAR NO":
                        String carNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (carNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex03().toLowerCase().contains(carNoSearch)
                            );
                        }
                        break;
                    case "CUSTOMER NAME":
                        String custNameSearch = txtFieldSearch.getText().toLowerCase();
                        if (custNameSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex06().toLowerCase().contains(custNameSearch));
                        }
                        break;
                    case "REFERENCE NO":
                        String refNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (refNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex07().toLowerCase().contains(refNoSearch));
                        }
                        break;
                }

                int rowNumber = 1;
                for (Cashier_Receivables item : filteredData) {
                    item.setTblindex01(String.valueOf(rowNumber));
                    rowNumber++;
                }
                tblViewCAR.setItems(filteredData);
                if (filteredData.isEmpty()) {
                    ShowMessageFX.Information(null, "No Records", "No records found for the selected criteria.");
                    selectAllCheckBox.setSelected(false);
                }
                break;
            case "btnViewTrans":
                loadTable();
                if (tblViewCAR.getItems().isEmpty()) {
                    ShowMessageFX.Warning(null, pxeModuleName, "No records found");
                    selectAllCheckBox.setSelected(false);
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnRefresh":
                tblViewCAR.refresh();
                txtFieldSearch.clear();
                datePickerFrom.setValue(LocalDate.now());
                datePickerTo.setValue(LocalDate.now());
                selectAllCheckBox.setSelected(false);
                loadTable();
                break;
            case "btnProceed":
                switch (comboBoxFormType.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        proceedToOtherForm(0);
                        break;
                    case 1:
                        proceedToOtherForm(1);
                        break;
                    case 2:
                        proceedToOtherForm(2);
                        break;
                    case 3:
                        proceedToOtherForm(3);
                        break;
                    case 4:
                        proceedToOtherForm(4);
                        break;
                    case 5:
                        ShowMessageFX.Warning(null, pxeModuleName, "SOA is underdevelopment");
                        break;
                }
                break;
            case "btnPrint":

                break;
            case "btnExcelExport":
                ObservableList<Cashier_Receivables> cashDataToExport = FXCollections.observableArrayList();

                // Get selected items from the currently displayed table (either filtered or unfiltered)
                for (Cashier_Receivables item : tblViewCAR.getItems()) {
                    cashDataToExport.add(item);
                }

                // If no items are selected, show an error message
                if (cashDataToExport.isEmpty()) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items to export.");
                    return;
                }

                // Ask for confirmation
                if (!ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to save as excel?")) {
                    return;
                }
                List<String> lsHeaders = Arrays.asList("Car No",
                        "Date",
                        "Type",
                        "Customer Name",
                        "Reference No",
                        "Particulars",
                        "Location",
                        "Gross Amount",
                        "Discount Amount",
                        "Trans Amount",
                        "Deduct Amount",
                        "Paid Amount",
                        "SI No",
                        "CC Slip No",
                        "PR No",
                        "SOA No",
                        "Journal Entry No",
                        "Trans From",
                        "Transferred By",
                        "Trans To",
                        "Transferred Date");
                List<Map<String, Object>> dataToExport = new ArrayList<>();
                for (Cashier_Receivables item : cashDataToExport) {
                    dataToExport.add(getRowData(lsHeaders, item));
                }
                ExcelExporterUtil.exportDataToExcel(lsHeaders, dataToExport, pxeModuleName);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private Map<String, Object> getRowData(List<String> fsHeadersValue, Cashier_Receivables fsItemValue) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put(fsHeadersValue.get(0), fsItemValue.getTblindex02());
        rowData.put(fsHeadersValue.get(1), fsItemValue.getTblindex03());
        rowData.put(fsHeadersValue.get(2), fsItemValue.getTblindex05());
        rowData.put(fsHeadersValue.get(3), fsItemValue.getTblindex06());
        rowData.put(fsHeadersValue.get(4), fsItemValue.getTblindex07());
        rowData.put(fsHeadersValue.get(5), fsItemValue.getTblindex08());
        rowData.put(fsHeadersValue.get(6), fsItemValue.getTblindex09());
        rowData.put(fsHeadersValue.get(7), fsItemValue.getTblindex10());
        rowData.put(fsHeadersValue.get(8), fsItemValue.getTblindex11());
        rowData.put(fsHeadersValue.get(9), fsItemValue.getTblindex12());
        rowData.put(fsHeadersValue.get(10), fsItemValue.getTblindex13());
        rowData.put(fsHeadersValue.get(11), fsItemValue.getTblindex14());
        rowData.put(fsHeadersValue.get(12), fsItemValue.getTblindex15());
        rowData.put(fsHeadersValue.get(13), fsItemValue.getTblindex16());
        rowData.put(fsHeadersValue.get(14), fsItemValue.getTblindex17());
        rowData.put(fsHeadersValue.get(15), fsItemValue.getTblindex18());
        rowData.put(fsHeadersValue.get(16), fsItemValue.getTblindex19());
        rowData.put(fsHeadersValue.get(17), fsItemValue.getTblindex20());
        rowData.put(fsHeadersValue.get(18), fsItemValue.getTblindex21());
        rowData.put(fsHeadersValue.get(19), fsItemValue.getTblindex22());
        rowData.put(fsHeadersValue.get(20), fsItemValue.getTblindex23());
        return rowData;
    }

    private void proceedToOtherForm(int fnFormName) {
        switch (fnFormName) {
            case 0:
                loadInvoiceWindow("Acknowledge Receivables");
                break;
            case 1:
                loadInvoiceWindow("Collection Receipt");
                break;
            case 2:
                loadInvoiceWindow("Service Invoice");
                break;
            case 3:
                loadInvoiceWindow("Parts Sales Invoice");
                break;
            case 4:
                loadInvoiceWindow("Billing Statement");
                break;
            case 5:
                loadInvoiceWindow("");
                break;
        }

    }

    private void loadInvoiceWindow(String fsFormName) {
        try {
            String lsFormName = fsFormName;
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/Invoice.fxml"));
            InvoiceController loControl = new InvoiceController();
            loControl.setGRider(oApp);
            loControl.setIsCARState(true);
            fxmlLoader.setController(loControl);
            Node tabContent = AnchorMain.getParent();
            Parent tabContentParent = tabContent.getParent();

            if (tabContentParent instanceof TabPane) {
                TabPane tabpane = (TabPane) tabContentParent;
                Tab existingTab = tabpane.getTabs().stream().
                        filter(tabPane -> tabPane.getText().equals(lsFormName))
                        .findFirst().orElse(null);
                if (existingTab != null) {
                    if (ShowMessageFX.YesNo(null, pxeModuleName,
                            fsFormName + " is already open.\n"
                            + "Do you want to switch to the existing tab and create a new " + fsFormName + " ?")) {
                        tabpane.getSelectionModel().select(existingTab);
                        Parent parent = fxmlLoader.load();
                        existingTab.setContent(parent);
                        closeInvoiceTab(existingTab, loControl, lsFormName);
                        return;
                    } else {
                        return;
                    }
                }
                Parent parent = fxmlLoader.load();
                // Create a new tab and set its content
                Tab loNewTab = new Tab(lsFormName, parent);
                loNewTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
                loNewTab.setContextMenu(fdController.createContextMenu(tabpane, loNewTab, oApp));
                tabpane.getTabs().add(loNewTab);
                tabpane.getSelectionModel().select(loNewTab);
                closeInvoiceTab(loNewTab, loControl, lsFormName);
            }
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
        }
    }

    private void closeInvoiceTab(Tab foTab, InvoiceController foController, String fsFormName) {
        foTab.setOnCloseRequest(event -> {
            if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this tab?")) {
                if (poUnload == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                    return;
                }
                // Ensure loControl.AnchorMain is initialized before calling unload
                if (foController.AnchorMain == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "AnchorMain is not initialized properly. Cannot unload form.");
                    return;
                }
                try {
                    poUnload.unloadForm(foController.AnchorMain, oApp, fsFormName);
                } catch (Exception e) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Error unloading form: " + e.getMessage());
                }
            } else {
                event.consume(); // Prevent tab close
            }
        }
        );
    }

    @Override
    public void initFields() {
        comboBoxFilter.setOnAction(e -> {
            String lsSelectedFilter = comboBoxFilter.getSelectionModel().getSelectedItem();
            switch (lsSelectedFilter) {
                case "CAR NO":
                case "CUSTOMER NAME":
                case "REFERENCE NO":
                    txtFieldSearch.clear();
                    tblViewCAR.setItems(poCARData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });
    }
}
