/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.text.DecimalFormat;
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
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.cashiering.CashierReceivables;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.cashiering.Cashier_Receivables;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
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
    ObservableList<String> cFormType = FXCollections.observableArrayList("CREATE AR", "CREATE CR", "CREATE SERVICE INVOICE", "CREATE PARTS SI",
            "CREATE BILLING STATEMENT", "CONVERT TO SOA");
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
        String[] arr = {};
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
        JSONObject loJSON = new JSONObject();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr < arr.length; lnCtr++) {
                poCARData.add(new Cashier_Receivables(
                        String.valueOf(lnCtr + 1),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
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

    @Override
    public void initLoadTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindex09.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        tblindex10.setCellValueFactory(new PropertyValueFactory<>("tblindex10"));
        tblindex11.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        tblindex12.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        tblindex13.setCellValueFactory(new PropertyValueFactory<>("tblindex12"));
        tblindex14.setCellValueFactory(new PropertyValueFactory<>("tblindex13"));
        tblindex15.setCellValueFactory(new PropertyValueFactory<>("tblindex14"));
        tblindex16.setCellValueFactory(new PropertyValueFactory<>("tblindex15"));
        tblindex17.setCellValueFactory(new PropertyValueFactory<>("tblindex16"));
        tblindex18.setCellValueFactory(new PropertyValueFactory<>("tblindex17"));
        tblindex19.setCellValueFactory(new PropertyValueFactory<>("tblindex18"));
        tblindex20.setCellValueFactory(new PropertyValueFactory<>("tblindex19"));
        tblindex21.setCellValueFactory(new PropertyValueFactory<>("tblindex20"));
        tblindex22.setCellValueFactory(new PropertyValueFactory<>("tblindex21"));
        tblindex23.setCellValueFactory(new PropertyValueFactory<>("tblindex22"));

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
                datePickerFrom.setValue(null);
                datePickerTo.setValue(null);
                selectAllCheckBox.setSelected(false);
                loadTable();
                break;
            case "btnProceed":
                switch (comboBoxFormType.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
                break;
            case "btnPrint":
                break;
            case "btnExcelExport":
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
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
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblViewCAR.setItems(poCARData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });
    }
}
