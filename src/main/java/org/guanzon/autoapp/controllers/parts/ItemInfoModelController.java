/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.parts.InventoryInformation;
import org.guanzon.autoapp.models.parts.ItemInfoModelYear;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ItemInfoModelController implements Initializable {

    private GRider oApp;
    private InventoryInformation oTransInventoryModel;
    private final String pxeModuleName = "Item Model";
    ObservableList<String> cItems = FXCollections.observableArrayList("MAKE", "MODEL");
    private ObservableList<ItemInfoModelYear> itemModeldata = FXCollections.observableArrayList();
    private ObservableList<ItemInfoModelYear> itemModelYear = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableView<ItemInfoModelYear> tblVModelList;
    @FXML
    private CheckBox selectModelAll, chckNoYear, selectYearAll;
    @FXML
    private TableView<ItemInfoModelYear> tblVYear;
    @FXML
    private TextField txtSeeks01, txtSeeks02;
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private TableColumn<ItemInfoModelYear, CheckBox> tblindexModel01Select;
    @FXML
    private TableColumn<ItemInfoModelYear, String> tblindexModel02, tblindexModel03;
    @FXML
    private TableColumn<ItemInfoModelYear, Boolean> tblindexModelYr01;
    @FXML
    private TableColumn<ItemInfoModelYear, String> tblindexModelYr02;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private Stage getStage() {
        return (Stage) txtSeeks01.getScene().getWindow();
    }

    public void setObject(InventoryInformation foValue) {
        oTransInventoryModel = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboFilter.setItems(cItems);

        initCapitalizationFields();
        initTextKeyPressed();
        initButtonClick();
        initFieldsAction();
        CheckNoYear();
        loadItemModelTable();
        initItemModelTable();
        initItemModelYearTable();
        loadItemModelYearTable();
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtSeeks01, txtSeeks02);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    private void loadItemModelTable() {
        itemModeldata.clear(); // Clear the previous data in the list
        JSONObject loJSON = oTransInventoryModel.loadModel();
        if ("success".equals((String) loJSON.get("result"))) {
            try {
                String lsMakeName = "";
                for (int lnCtr = 1; lnCtr <= oTransInventoryModel.getModelCount(); lnCtr++) {
                    if (!oTransInventoryModel.getModelDetail(lnCtr, "sModelDsc").equals("COMMON")) {
                        if (oTransInventoryModel.getModelDetail(lnCtr, "sMakeDesc") != null) {
                            lsMakeName = String.valueOf(oTransInventoryModel.getModelDetail(lnCtr, "sMakeDesc"));
                        }
                    }
                    itemModeldata.add(new ItemInfoModelYear(
                            String.valueOf(lnCtr), // ROW
                            String.valueOf(oTransInventoryModel.getModelDetail(lnCtr, "sMakeIDxx")),
                            lsMakeName,
                            String.valueOf(oTransInventoryModel.getModelDetail(lnCtr, "sModelIDx")),
                            String.valueOf(oTransInventoryModel.getModelDetail(lnCtr, "sModelDsc")),
                            "",
                            "",
                            String.valueOf(oTransInventoryModel.getModelDetail(lnCtr, "sModelCde"))
                    )
                    );

                }
            } catch (SQLException ex) {
                Logger.getLogger(ItemInfoModelController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        tblVModelList.setItems(itemModeldata);
    }

    private void initItemModelTable() {
        tblindexModel01Select.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSelect()));
        tblindexModel01Select.setCellFactory(column -> new TableCell<ItemInfoModelYear, CheckBox>() {
            @Override
            protected void updateItem(CheckBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);

                    // Add action listener to checkbox
                    item.setOnAction(event -> {
                        ItemInfoModelYear modelItem = getTableView().getItems().get(getIndex());

                        if (modelItem.getTblindexModel05().equals("COMMON")) {
                            // If "COMMON" is selected, unselect all other checkboxes and deselect "Select All"
                            tblVModelList.getItems().forEach(otherItem -> {
                                if (!otherItem.getTblindexModel05().equals("COMMON")) {
                                    otherItem.getSelect().setSelected(false);
                                }
                            });
                            selectModelAll.setSelected(false);
                        } else {
                            // If a non-COMMON item is selected, unselect the "COMMON" checkbox
                            tblVModelList.getItems().stream()
                                    .filter(otherItem -> otherItem.getTblindexModel05().equals("COMMON"))
                                    .forEach(commonItem -> commonItem.getSelect().setSelected(false));
                        }

                        updateYearAndCheckNoYearVisibility();
                    });
                }
            }
        });

        tblindexModel02.setCellValueFactory(new PropertyValueFactory<>("tblindexModel03"));
        tblindexModel03.setCellValueFactory(new PropertyValueFactory<>("tblindexModel05"));

        selectModelAll.setOnAction(event -> {
            boolean selectAll = selectModelAll.isSelected();

            tblVModelList.getItems().forEach(item -> {
                if (!item.getTblindexModel05().equals("COMMON")) {
                    item.getSelect().setSelected(selectAll);
                }
            });

            updateYearAndCheckNoYearVisibility();
        });

        tblVModelList.getItems().forEach(item -> {
            item.getSelect().setOnAction(event -> {
                if (!item.getTblindexModel05().equals("COMMON")) {
                    updateYearAndCheckNoYearVisibility();
                }
            });
        });

        tblVModelList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVModelList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void loadItemModelYearTable() {
        itemModelYear.clear();
        try {
            int currentYear = LocalDate.now().getYear() + 1; // Starting year
            int endYear = 1900;    // Ending year

            for (int year = currentYear; year >= endYear; year--) {
                itemModelYear.add(new ItemInfoModelYear(
                        String.valueOf(year), // ROW
                        "",
                        "",
                        "",
                        "",
                        String.valueOf(year),
                        String.valueOf(year),
                        String.valueOf(oTransInventoryModel.getModelDetail(year, "sModelCde"))
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemInfoModelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblVYear.setItems(itemModelYear);
    }

    private void initItemModelYearTable() {
        tblindexModelYr01.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblVYear.getItems().forEach(item -> {
            CheckBox loSelectCheckBoxYear = item.getSelect();
            loSelectCheckBoxYear.setOnAction(event -> {
                if (tblVYear.isDisabled()) {
                    loSelectCheckBoxYear.setSelected(false); // Unselect the checkbox
                } else if (tblVYear.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectYearAll.setSelected(true);
                } else {
                    selectYearAll.setSelected(false);
                }
            });
        });
        selectYearAll.setOnAction(event -> {
            boolean lbIsSelectedAll = selectYearAll.isSelected();
            tblVYear.getItems().forEach(item -> item.getSelect().setSelected(lbIsSelectedAll));
        });

        tblVYear.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // If tblVYear is disabled
                // Unselect all checkboxes
                tblVYear.getItems().forEach(item -> item.getSelect().setSelected(false));
                // Unselect the "Select All" checkbox
                selectYearAll.setSelected(false);
            }
        });

        tblindexModelYr02.setCellValueFactory(new PropertyValueFactory<>("tblindexModel06"));
        tblVYear.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblVYear.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtSeeks01, txtSeeks02);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String txtFieldID = ((TextField) event.getSource()).getId();
        switch (event.getCode()) {
            case ENTER:
                switch (txtFieldID) {
                    case "txtSeeks01":
                        String lsFilterMake = txtSeeks01.getText().trim().toLowerCase();
                        FilteredList<ItemInfoModelYear> filteredTxtFieldMake = new FilteredList<>(itemModeldata);
                        filteredTxtFieldMake.setPredicate(clients -> {
                            if (lsFilterMake.isEmpty()) {
                                return true;
                            } else {
                                String make = clients.getTblindexModel03().toLowerCase();
                                return make.contains(lsFilterMake);
                            }
                        });
                        tblVModelList.setItems(filteredTxtFieldMake);
                        if (filteredTxtFieldMake.isEmpty()) {
                            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                        }
                        break;
                    case "txtSeeks02":
                        String lsFilterModel = txtSeeks02.getText().trim().toLowerCase();
                        FilteredList<ItemInfoModelYear> filteredTxtFieldModel = new FilteredList<>(itemModeldata);
                        filteredTxtFieldModel.setPredicate(clients -> {
                            if (lsFilterModel.isEmpty()) {
                                return true;
                            } else {
                                String make = clients.getTblindexModel05().toLowerCase();
                                return make.contains(lsFilterModel);
                            }
                        });
                        tblVModelList.setItems(filteredTxtFieldModel);
                        if (filteredTxtFieldModel.isEmpty()) {
                            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                        }
                        break;
                }
                break;
        }
    }

    private void initButtonClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnClose);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnAdd":
                ObservableList<ItemInfoModelYear> selectedItemsModel = FXCollections.observableArrayList();
                ObservableList<ItemInfoModelYear> selectedItemsYear = FXCollections.observableArrayList();

                // Check if any items are selected in tblVModelList
                boolean lbIsAnyModelItemSelected = false;
                boolean lbIsAnyYearItemSelected = false;
                for (ItemInfoModelYear item : tblVModelList.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItemsModel.add(item);
                        lbIsAnyModelItemSelected = true;
                    }
                }

                for (ItemInfoModelYear item : tblVYear.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItemsYear.add(item);
                        lbIsAnyYearItemSelected = true;
                    }
                }

                if (!lbIsAnyModelItemSelected && !lbIsAnyYearItemSelected) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                    return;
                } else if (!chckNoYear.isSelected() && !lbIsAnyYearItemSelected) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please either check the \"No Year Model\" checkbox or select items in the table for model years.");
                    return;
                } else {
                    String lsMessage = "";
                    int addCount = 0;
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                        if (chckNoYear.isSelected()) {
                            for (ItemInfoModelYear item : selectedItemsModel) {
                                String lsMakeIDx = item.getTblindexModel02();
                                String lsMakeDesc = item.getTblindexModel03();
                                String lsModelIDx = item.getTblindexModel04();
                                String lsModelDesc = item.getTblindexModel05();
                                loJSON = oTransInventoryModel.addInvModel_Year(lsModelIDx, lsModelDesc, lsMakeIDx, lsMakeDesc, 0, true);
                                if (!"error".equals((String) loJSON.get("result"))) {
                                    lsMessage = (String) loJSON.get("message");
                                    addCount++;
                                } else {
                                    lsMessage = (String) loJSON.get("message");
                                }
                            }
                        } else {
                            //Inv Model Year
                            for (ItemInfoModelYear item : selectedItemsModel) {
                                String lsMakeIDx = item.getTblindexModel02();
                                String lsMakeDesc = item.getTblindexModel03();
                                String lsModelIDx = item.getTblindexModel04();
                                String lsModelDesc = item.getTblindexModel05();
                                for (ItemInfoModelYear items : selectedItemsYear) {
                                    String lsYear = items.getTblindexModel06();
                                    int lnYears = Integer.parseInt(lsYear);
                                    loJSON = oTransInventoryModel.addInvModel_Year(lsModelIDx, lsModelDesc, lsMakeIDx, lsMakeDesc, lnYears, false);
                                    if (!"error".equals((String) loJSON.get("result"))) {
                                        lsMessage = (String) loJSON.get("message");
                                        addCount++;
                                    } else {
                                        lsMessage = (String) loJSON.get("message");
                                    }
                                }
                            }
                        }
                        if (addCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Added model successfully.");
                            CommonUtils.closeStage(btnClose);
                        } else {
                            ShowMessageFX.Error(null, pxeModuleName, lsMessage);
                        }
                    }
                    break;
                }
        }

    }

    private void CheckNoYear() {
        chckNoYear.setOnAction(event -> {
            if (chckNoYear.isSelected()) {
                tblVYear.setDisable(true);
            } else {
                tblVYear.setDisable(false);
            }
        });
    }

    private void updateYearAndCheckNoYearVisibility() {
        boolean isAnySelected = tblVModelList.getItems().stream()
                .anyMatch(item -> item.getSelect().isSelected() && !item.getTblindexModel05().equals("COMMON"));
        tblVYear.setDisable(!isAnySelected);
        chckNoYear.setVisible(isAnySelected);
        chckNoYear.setSelected(!isAnySelected);
    }

    public void initFieldsAction() {
        tblVYear.setDisable(true);
        chckNoYear.setSelected(true);
        chckNoYear.setVisible(false);
        comboFilter.setOnAction(e -> {
            String lsSelectedFilter = comboFilter.getSelectionModel().getSelectedItem();
            CustomCommonUtil.setVisible(false, txtSeeks01, txtSeeks02);
            CustomCommonUtil.setManaged(false, txtSeeks01, txtSeeks02);
            switch (lsSelectedFilter) {
                case "MAKE":
                    txtSeeks01.setText("");
                    txtSeeks01.setVisible(true);
                    txtSeeks01.setManaged(true);
                    tblVModelList.setItems(itemModeldata);
                    break;
                case "MODEL":
                    txtSeeks02.setText("");
                    txtSeeks02.setVisible(true);
                    txtSeeks02.setManaged(true);
                    tblVModelList.setItems(itemModeldata);
                    break;
            }
        });
    }
}
