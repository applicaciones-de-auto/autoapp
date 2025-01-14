/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.cashiering.SalesInvoice;
import org.guanzon.autoapp.models.cashiering.Deductibles;
import org.guanzon.autoapp.models.cashiering.ManuallyDeductibles;
import org.json.simple.JSONObject;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.guanzon.autoapp.utils.CustomCommonUtil;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class PRDeductibleController implements Initializable {

    private GRider oApp;
    private SalesInvoice oTrans;
    private final String pxeModuleName = "Deductibles";
    private ObservableList<ManuallyDeductibles> manuallyData = FXCollections.observableArrayList();
    private ObservableList<Deductibles> deductData = FXCollections.observableArrayList();
    private int pnRow;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cComboSearch = FXCollections.observableArrayList("RECEIPT DATE", "RECEIPT NO", "PAYER NAME");
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button btnAddDeductibles, btnRefresh, btnApplyReceipts, btnClose, btnSearch;
    @FXML
    private TextField txtField01, txtFieldSearch;
    @FXML
    private CheckBox selectAllBox;
    @FXML
    private TableView<ManuallyDeductibles> tblViewManually;
    @FXML
    private TableColumn<Deductibles, String> tblindex01_Manually, tblindex03_Manually, tblindex04_Manually, tblindex05_Manually, tblindex06_Manually;
    @FXML
    private TableColumn<Deductibles, Boolean> tblindex02_Manually;
    @FXML
    private TableView<Deductibles> tblViewDeductibles;
    @FXML
    private TableColumn<Deductibles, String> tblindex01_Deductibles, tblindex02_Deductibles, tblindex03_Deductibles, tblindex04_Deductibles;
    @FXML
    private ComboBox<String> comboBoxSearch;
    @FXML
    private Label lblFrom, lblTo;
    @FXML
    private DatePicker datePickerTo, datePickerFrom;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setObject(SalesInvoice foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnValue) {
        pnRow = fnValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        initManuallyTable();
        initDeductiblesTable();
        initButtonsClick();
        initTableKeyPressed();
        comboBoxSearch.setItems(cComboSearch);
        txtFieldSearch.setOnKeyPressed(this::txtField_KeyPressed);
        initFields();
    }

    private void loadManuallyTable() {
        manuallyData.clear();
        String lsRcptNox = "";
        String lsRcptDte = "";
        String lsPartclr = "";
        String lsTotalxx = "";
        String lsPayrNme = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getSIAdvancesList().size() - 1; lnCtr++) {
//            if (oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getSourceNo() != null) {
//                lsRcptNox = oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getSourceNo();
//            }
//            if (oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getTransDate() != null) {
//                lsRcptDte = oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getTransDate();
//            }
//            if (oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getTransType() != null) {
//                lsPartclr = oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getTransType();
//            }
//            if (oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getAdvAmt() != null) {
//                lsTotalxx = CustomCommonUtil.setDecimalFormat(oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getAdvAmt());
//            }
//            if (oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getPyrName() != null) {
//                lsPayrNme = oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getPyrName();
//            }
            manuallyData.add(new ManuallyDeductibles(
                    String.valueOf(lnCtr + 1),
                    lsRcptNox,
                    lsRcptDte,
                    lsPartclr,
                    lsTotalxx,
                    lsPayrNme,
                    ""));
            lsRcptNox = "";
            lsRcptDte = "";
            lsPartclr = "";
            lsTotalxx = "";
            lsPayrNme = "";
        }
        tblViewManually.setItems(manuallyData);
    }

    private void initManuallyTable() {
        tblindex01_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_Manually.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindex03_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex04_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex05_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex06_Manually.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblViewManually.getItems().forEach(item -> {
            CheckBox selectCheckBox = item.getSelect();
            selectCheckBox.setOnAction(event -> {
                if (tblViewManually.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllBox.setSelected(true);
                } else {
                    selectAllBox.setSelected(false);
                }
            });
        });
        selectAllBox.setOnAction(event -> {
            boolean newValue = selectAllBox.isSelected();
            tblViewManually.getItems().forEach(item -> item.getSelect().setSelected(newValue));
        });
        tblViewManually.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewManually.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        manuallyData.clear();
        tblViewManually.setItems(manuallyData);
    }

    private void loadDeductiblesTable() {
        deductData.clear();
        String lsDdctAmt = "";
        String lsRefNoxx = "";
        String lsPartclr = "";
//        for (int lnCtr = 0; lnCtr <= oTrans.getSIDeductList().size() - 1; lnCtr++) {
        deductData.add(new Deductibles(
                //                String.valueOf(lnCtr + 1),
                "",
                lsDdctAmt,
                lsRefNoxx,
                lsPartclr,
                ""
        ));
        lsDdctAmt = "";
        lsRefNoxx = "";
        lsPartclr = "";
//        }
        tblViewDeductibles.setItems(deductData);
    }

    private void initDeductiblesTable() {
        tblindex01_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04_Deductibles.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblViewDeductibles.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewDeductibles.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        deductData.clear();
        tblViewDeductibles.setItems(deductData);
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtFieldSearch":
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAddDeductibles, btnRefresh, btnApplyReceipts, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnSearch":
                String lsSelectedSearch = comboBoxSearch.getSelectionModel().getSelectedItem();
                FilteredList<ManuallyDeductibles> searchData = new FilteredList<>(manuallyData, p -> true);
                switch (lsSelectedSearch) {
                    case "RECEIPT DATE":
                        if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null) {
                            LocalDate fromDate = datePickerFrom.getValue();
                            LocalDate toDate = datePickerTo.getValue();
                            searchData.setPredicate(item -> {
                                LocalDate itemDate;
                                itemDate = CustomCommonUtil.strToDate(item.getTblindex03());
                                return (itemDate != null && !itemDate.isBefore(fromDate) && !itemDate.isAfter(toDate));
                            });
                        } else {
                            ShowMessageFX.Information(null, "Filter Error", "Please select both a 'From' and 'To' date.");
                            selectAllBox.setSelected(false);
                        }
                        break;
                    case "RECEIPT NO":
                        String lsReceiptNoSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsReceiptNoSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllBox.setSelected(false);
                            loadManuallyTable();
                        } else {
                            searchData.setPredicate(item
                                    -> item.getTblindex02().toLowerCase().contains(lsReceiptNoSearch)
                            );
                        }
                        break;
                    case "PAYER NAME":
                        String lsPayerNameSearch = txtFieldSearch.getText().toLowerCase();
                        if (lsPayerNameSearch.isEmpty()) {
                            ShowMessageFX.Information(null, "Filter Error", "Please enter a value first.");
                            selectAllBox.setSelected(false);
                            loadManuallyTable();
                        } else {
                            searchData.setPredicate(item
                                    -> item.getTblindex05().toLowerCase().contains(lsPayerNameSearch));
                        }
                        break;
                }
                int rowNumber = 1;
                for (ManuallyDeductibles item : searchData) {
                    item.setTblindex01(String.valueOf(rowNumber)); // Reset the row number for filtered items
                    rowNumber++;
                }
                tblViewManually.setItems(searchData);
                if (searchData.isEmpty()) {
                    ShowMessageFX.Information(null, "No Records", "No records found for the selected criteria.");
                    selectAllBox.setSelected(false);
                }
                break;
            case "btnAddDeductibles":
                ObservableList<ManuallyDeductibles> selectedItems = FXCollections.observableArrayList();
                for (ManuallyDeductibles item : tblViewManually.getItems()) {
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

                int lnAddedCount = 0;
                String lsMessage = "";
                for (ManuallyDeductibles selectedItem : selectedItems) {
                    String receiptNo = selectedItem.getTblindex02();
                    ManuallyDeductibles originalItem = manuallyData.stream()
                            .filter(item -> item.getTblindex02().equals(receiptNo))
                            .findFirst()
                            .orElse(null);
                    if (originalItem != null) {
                        int originalIndex = manuallyData.indexOf(originalItem);
//                        loJSON = oTrans.addDeductibles(originalIndex);
                        if ("success".equals((String) loJSON.get("result"))) {
                            for (int lnCtr = 0; lnCtr <= oTrans.getSIAdvancesList().size() - 1; lnCtr++) {
//                                oTrans.getSIDeductModel().getDetail(lnCtr).setDeductAmt(oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getAdvAmt());
//                                oTrans.getSIDeductModel().getDetail(lnCtr).setReferNoxx(oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getReferNo());
//                                oTrans.getSIDeductModel().getDetail(lnCtr).setDescriptx(oTrans.getSIAdvancesModel().getDetailModel(lnCtr).getTranType());
                            }
                            if (!"error".equals((String) loJSON.get("result"))) {
                                lsMessage = "Added successfully.";
                                lnAddedCount++;
                            } else {
                                lsMessage = "Added failed, Please try again or contact support.";
                            }
                        }
                    }
                }

                if (lnAddedCount > 0) {
                    ShowMessageFX.Information(null, pxeModuleName, lsMessage);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, lsMessage);
                }
                loadDeductiblesTable();
                selectAllBox.setSelected(false);
                break;
            case "btnRefresh":
                CommonUtils.closeStage(btnRefresh);
                break;
            case "btnApplyReceipts":
                CommonUtils.closeStage(btnApplyReceipts);
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }

    private void initFields() {
        CustomCommonUtil.setVisible(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnSearch);
        CustomCommonUtil.setManaged(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnSearch);
        comboBoxSearch.setOnAction(e -> {
            CustomCommonUtil.setVisible(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnSearch);
            CustomCommonUtil.setManaged(false, txtFieldSearch, lblFrom, lblTo, datePickerFrom, datePickerTo, btnSearch);
            String lsSelectedFilter = comboBoxSearch.getSelectionModel().getSelectedItem();
            switch (lsSelectedFilter) {
                case "RECEIPT DATE":
                    CustomCommonUtil.setVisible(true, datePickerFrom, datePickerTo, btnSearch);
                    CustomCommonUtil.setManaged(true, datePickerFrom, datePickerTo, btnSearch);
                    txtFieldSearch.setText("");
                    tblViewManually.setItems(manuallyData);
                    break;
                case "RECEIPT NO":
                case "PAYER NAME":
                    txtFieldSearch.setText("");
                    CustomCommonUtil.setVisible(true, txtFieldSearch, btnSearch);
                    CustomCommonUtil.setManaged(true, txtFieldSearch, btnSearch);
                    datePickerFrom.setValue(null);
                    datePickerTo.setValue(null);
                    tblViewManually.setItems(manuallyData);
                    break;
            }
            selectAllBox.setSelected(false);
            loadManuallyTable();
        });

    }

    private Deductibles getDeductSelectedItem() {
        return tblViewDeductibles.getSelectionModel().getSelectedItem();
    }

    private void initTableKeyPressed() {
        tblViewDeductibles.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this deductables detail?")) {
                    Deductibles selectedTrans = getDeductSelectedItem();
                    int removeCount = 0;
                    if (selectedTrans != null) {
                        String lsRow = selectedTrans.getTblindex01();
                        int lnRow = Integer.parseInt(lsRow);
                        oTrans.removeSIDetail(lnRow - 1);
                        removeCount++;
                    }
                    if (removeCount >= 1) {
                        ShowMessageFX.Information(null, pxeModuleName, "Removed deductables detail successfully");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Removed deductables detail failed");
                    }
                    loadDeductiblesTable();
                }
            } else {
                return;
            }
        }
        );
    }

    @FXML
    private void tblDeduct_Click(MouseEvent event) {
        pnRow = tblViewDeductibles.getSelectionModel().getSelectedIndex();
        if (pnRow < 0 || pnRow >= tblViewDeductibles.getItems().size()) {
            ShowMessageFX.Warning(null, pxeModuleName, "Please select valid deduct details.");
            return;
        }
        if (event.getClickCount() == 2) {
            loadUpdateDeductWindow(pnRow);
        }

    }

    private void loadUpdateDeductWindow(int fnRow) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/UpdateDeductibles.fxml"));
            UpdateDeductiblesController loControl = new UpdateDeductiblesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setRow(fnRow);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("");
            stage.showAndWait();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);

        }
    }
}
