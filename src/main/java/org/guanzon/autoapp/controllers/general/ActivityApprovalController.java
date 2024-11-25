package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
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
import org.guanzon.auto.main.sales.Activity;
import org.guanzon.autoapp.interfaces.GApprovalInterface;
import org.guanzon.autoapp.models.general.ActivityApproval;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ActivityApprovalController implements Initializable, ScreenInterface, GApprovalInterface {

    private GRider oApp;
    private Activity oTrans;
    private final String pxeModuleName = "Activity Approval"; //Form Title
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("ACTIVITY NO", "ACTIVITY DATE", "ACTIVITY TITLE", "ACTIVITY TYPE",
            "PERSON IN CHARGE", "DEPARTMENT");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");

    private ObservableList<ActivityApproval> poActApprovalData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnApproved;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Label lbTotalBudget;
    @FXML
    private Button btnRefresh, btnFilter, btnClose;
    @FXML
    private TableView<ActivityApproval> tblViewActApproval;
    @FXML
    private TableColumn<ActivityApproval, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09;
    @FXML
    private TableColumn<ActivityApproval, Boolean> tblindexselect;
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
        oTrans = new Activity(oApp, false, oApp.getBranchCode());
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
                FilteredList<ActivityApproval> filteredData = new FilteredList<>(poActApprovalData, p -> true);
                switch (lsSelectedFilter) {
                    case "ACTIVITY DATE":
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
                    case "ACTIVITY NO":
                        String lsActivityNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsActivityNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                            loadTable();
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex02().toLowerCase().contains(lsActivityNoSearch)
                            );
                        }
                        break;
                    case "ACTIVITY TITLE":
                        String lsActivityTitleSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsActivityTitleSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                            loadTable();
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex06().toLowerCase().contains(lsActivityTitleSearch));
                        }
                        break;
                    case "ACTIVITY TYPE":
                        String lsActivityTypeSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsActivityTypeSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                            loadTable();
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex05().toLowerCase().contains(lsActivityTypeSearch));
                        }
                        break;
                    case "DEPARTMENT":
                        String lsDepartSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsDepartSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                            selectAllCheckBox.setSelected(false);
                            loadTable();
                            loadTable();
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex08().toLowerCase().contains(lsDepartSearch));
                        }
                        break;
                    case "PERSON IN CHARGE":
                        String lsPersonInChargeSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsPersonInChargeSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllCheckBox.setSelected(false);
                            loadTable();
                        } else {
                            filteredData.setPredicate(item
                                    -> item.getTblindex07().toLowerCase().contains(lsPersonInChargeSearch));
                        }
                        break;
                }
                int rowNumber = 1;
                for (ActivityApproval item : filteredData) {
                    item.setTblindex01(String.valueOf(rowNumber)); // Reset the row number for filtered items
                    rowNumber++;
                }
                tblViewActApproval.setItems(filteredData);
                if (filteredData.isEmpty()) {
                    ShowMessageFX.Information(null, "No Records", "No records found for the selected criteria.");
                    selectAllCheckBox.setSelected(false);
                    lbTotalBudget.setText("₱ " + poGetDecimalFormat.format(0.00));
                }
                break;
            case "btnApproved":
                ObservableList<ActivityApproval> selectedItems = FXCollections.observableArrayList();
                for (ActivityApproval item : tblViewActApproval.getItems()) {
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

                for (ActivityApproval selectedItem : selectedItems) {
                    String activityNo = selectedItem.getTblindex02();

                    ActivityApproval originalItem = poActApprovalData.stream()
                            .filter(item -> item.getTblindex02().equals(activityNo))
                            .findFirst()
                            .orElse(null);
                    if (originalItem != null) {
                        int originalIndex = poActApprovalData.indexOf(originalItem);
                        JSONObject loJSON = oTrans.approveActivity(originalIndex);

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
                break;
            case "btnRefresh":
                tblViewActApproval.refresh();
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
        poActApprovalData.clear();
        String lsActivityNo = "";
        String lsActivityType = "";
        String lsDateFrom = "";
        String lsDateThru = "";
        String lsActivityTitle = "";
        String lsPersonCharge = "";
        String lsDepartment = "";
        String lsBranchInCharge = "";
        String lsActLocation = "";
        String lsBudgetProposal = "";
        double lnTotalBudget = 0.00;

        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.loadActivityForApproval();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTrans.getActivityList().size() - 1; lnCtr++) {
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getActTitle() != null) {
                    lsActivityNo = String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getActNo());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getActTitle() != null) {
                    lsActivityTitle = String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getActTitle());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getEventTyp() != null && !oTrans.getActivityModel().getDetailModel(lnCtr).getEventTyp().trim().isEmpty()) {
                    switch (String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getEventTyp())) {
                        case "eve":
                            lsActivityType = "EVENT";
                            break;
                        case "sal":
                            lsActivityType = "SALES CALL";
                            break;
                        case "pro":
                            lsActivityType = "PROMO";
                            break;
                    }
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getDateFrom() != null) {
                    lsDateFrom = CustomCommonUtil.xsDateShort(oTrans.getActivityModel().getDetailModel(lnCtr).getDateFrom());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getDateThru() != null) {
                    lsDateThru = CustomCommonUtil.xsDateShort(oTrans.getActivityModel().getDetailModel(lnCtr).getDateThru());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getEmpInCharge() != null) {
                    lsPersonCharge = String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getEmpInCharge());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getDeptName() != null) {
                    lsDepartment = String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getDeptName());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getBranchNm() != null) {
                    lsBranchInCharge = String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getBranchNm());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getLocation() != null) {
                    lsActLocation = String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getLocation());
                }
                if (oTrans.getActivityModel().getDetailModel(lnCtr).getRcvdBdgt() != null) {
                    lsBudgetProposal = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getRcvdBdgt())));
                    lnTotalBudget += oTrans.getActivityModel().getDetailModel(lnCtr).getRcvdBdgt();
                }

                poActApprovalData.add(new ActivityApproval(
                        String.valueOf(lnCtr + 1),
                        lsActivityNo.toUpperCase(),
                        String.valueOf(oTrans.getActivityModel().getDetailModel(lnCtr).getActvtyID()),
                        lsDateFrom,
                        lsActivityType.toUpperCase(),
                        lsActivityTitle.toUpperCase(),
                        lsPersonCharge.toUpperCase(),
                        lsDepartment.toUpperCase(),
                        lsBranchInCharge.toUpperCase(),
                        lsActLocation.toUpperCase(),
                        lsBudgetProposal,
                        lsDateThru));
            }

            lsActivityNo = "";
            lsDateFrom = "";
            lsDateThru = "";
            lsActivityTitle = "";
            lsPersonCharge = "";
            lsDepartment = "";
            lsBranchInCharge = "";
            lsActLocation = "";
            lsBudgetProposal = "";
            lbTotalBudget.setText("₱ " + poGetDecimalFormat.format(lnTotalBudget));
            tblViewActApproval.setItems(poActApprovalData);
        }
    }

    @Override
    public void initLoadTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
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
            ActivityApproval rowData = cellData.getValue();

            // Get the values you want to concatenate
            String lsDateFrom = rowData.getTblindex04();
            String lsDateThru = rowData.getTblindex12();

            // Concatenate the values
            String concatenatedValue = lsDateFrom + " - " + lsDateThru;

            // Create a new ObservableValue containing the concatenated value
            ObservableValue<String> observableValue = new SimpleStringProperty(concatenatedValue);

            return observableValue;
        });

        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex08"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex09"));
        tblindex08.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex09.setCellValueFactory(new PropertyValueFactory<>("tblindex11"));
        tblViewActApproval.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewActApproval.lookup("TableHeaderRow");
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
                case "ACTIVITY DATE":
                    CustomCommonUtil.setVisible(true, datePickerFrom, datePickerTo, btnFilter);
                    CustomCommonUtil.setManaged(true, datePickerFrom, datePickerTo, btnFilter);
                    txtFieldSearch.setText("");
                    tblViewActApproval.setItems(poActApprovalData);
                    break;
                case "ACTIVITY NO":
                case "ACTIVITY TITLE":
                case "PERSON IN CHARGE":
                case "ACTIVITY TYPE":
                case "DEPARTMENT":
                    txtFieldSearch.setText("");
                    CustomCommonUtil.setVisible(true, txtFieldSearch, btnFilter);
                    CustomCommonUtil.setManaged(true, txtFieldSearch, btnFilter);
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblViewActApproval.setItems(poActApprovalData);
                    break;
            }
            selectAllCheckBox.setSelected(false);
            loadTable();
        });

    }
}
