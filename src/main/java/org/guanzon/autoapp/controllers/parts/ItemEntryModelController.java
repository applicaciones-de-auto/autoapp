/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parts;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
import org.guanzon.autoapp.models.parts.ModelItemEntryModelYear;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class ItemEntryModelController implements Initializable {

    private GRider oApp;
    private InventoryInformation oTransInventoryModel;
    private final String pxeModuleName = "Item Entry Model";
    ObservableList<String> cItems = FXCollections.observableArrayList("MAKE", "MODEL");
    private ObservableList<ModelItemEntryModelYear> itemModeldata = FXCollections.observableArrayList();
    private ObservableList<ModelItemEntryModelYear> itemModelYear = FXCollections.observableArrayList();
    @FXML
    private Button btnAdd, btnClose;
    @FXML
    private TableView<ModelItemEntryModelYear> tblVModelList;
    @FXML
    private CheckBox selectModelAll;
    @FXML
    private TableView<ModelItemEntryModelYear> tblVYear;
    @FXML
    private CheckBox selectYearAll;
    @FXML
    private TextField txtSeeks01, txtSeeks02;
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private CheckBox chckNoYear;
    @FXML
    private TableColumn<ModelItemEntryModelYear, Boolean> tblindexModel01Select;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModel02;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModel03;
    @FXML
    private TableColumn<ModelItemEntryModelYear, Boolean> tblindexModelYr01;
    @FXML
    private TableColumn<ModelItemEntryModelYear, String> tblindexModelYr02;

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
        initItemModelTable();
        initItemModelYearTable();
        CheckNoYear();
        initCapitalizationFields();
        initTextKeyPressed();
        initButtonClick();
        initComboFields();
        loadItemModelTable();
        loadItemModelYearTable();
    }

    private void loadItemModelYearTable() {
        itemModelYear.clear();
        for (int lnCtr = 0; lnCtr <= oTransInventoryModel.getInventoryModelList().size() - 1; lnCtr++) {
            itemModelYear.add(new ModelItemEntryModelYear(
                    String.valueOf(lnCtr + 1), // ROW
                    "",
                    "",
                    "",
                    "",
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "nYearModl")),
                    "",
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "sModelCde"))
            ));
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
            if (newValue) {
                tblVYear.getItems().forEach(item -> item.getSelect().setSelected(false));
                selectYearAll.setSelected(false);
            }
        });

        tblindexModelYr02.setCellValueFactory(new PropertyValueFactory<>("tblindexModel06"));
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

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtSeeks01, txtSeeks02);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));
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
                        String lsFilterMake = txtSeeks01.getText();
                        FilteredList<ModelItemEntryModelYear> filteredTxtFieldMake = new FilteredList<>(itemModeldata);
                        filteredTxtFieldMake.setPredicate(clients -> {
                            if (lsFilterMake.isEmpty()) {
                                return true;
                            } else {
                                String make = clients.getTblindexModel03();
                                return make.contains(lsFilterMake);
                            }
                        });
                        tblVModelList.setItems(filteredTxtFieldMake);
                        if (filteredTxtFieldMake.isEmpty()) {
                            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                        }
                        break;
                    case "txtSeeks02":
                        String lsFilterModel = txtSeeks02.getText();
                        FilteredList<ModelItemEntryModelYear> filteredTxtFieldModel = new FilteredList<>(itemModeldata);
                        filteredTxtFieldModel.setPredicate(clients -> {
                            if (lsFilterModel.isEmpty()) {
                                return true;
                            } else {
                                String make = clients.getTblindexModel05();
                                return make.contains(lsFilterModel);
                            }
                        });
                        tblVModelList.setItems(filteredTxtFieldModel);
                        if (filteredTxtFieldModel.isEmpty()) {
                            ShowMessageFX.Information(null, pxeModuleName, "No record found!");
                        }
                        break;

                }
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
                ObservableList<ModelItemEntryModelYear> selectedItemsModel = FXCollections.observableArrayList();
                ObservableList<ModelItemEntryModelYear> selectedItemsYear = FXCollections.observableArrayList();

                // Check if any items are selected in tblVModelList
                boolean lbIsAnyModelItemSelected = false;
                boolean lbIsAnyYearItemSelected = false;
                for (ModelItemEntryModelYear item : tblVModelList.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItemsModel.add(item);
                        lbIsAnyModelItemSelected = true;
                    }
                }

                for (ModelItemEntryModelYear item : tblVYear.getItems()) {
                    if (item.getSelect().isSelected()) {
                        selectedItemsYear.add(item);
                        lbIsAnyYearItemSelected = true;
                    }
                }

                if (!lbIsAnyModelItemSelected && !lbIsAnyYearItemSelected) {
                    ShowMessageFX.Information(null, pxeModuleName, "No items selected to add.");
                } else if (!chckNoYear.isSelected() && !lbIsAnyYearItemSelected) {
                    ShowMessageFX.Information(null, pxeModuleName, "Please either check the \"No Year Model\" checkbox or select items in the table for model years.");
                } else {
                    int addCount = 0;
                    boolean lbIsExist = false;
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to add?")) {
                        if (chckNoYear.isSelected()) {
                            for (ModelItemEntryModelYear item : selectedItemsModel) {
                                String lsMakeDesc = item.getTblindexModel03();
                                String lsModelDesc = item.getTblindexModel05();
                                String lsModelCode = item.getTblindexModel08();
                                for (int lnCtr = 0; lnCtr <= oTransInventoryModel.getInventoryModelList().size() - 1; lnCtr++) {
                                    if (oTransInventoryModel.getInventoryModel(lnCtr, "sModelCde").toString().equals(lsModelCode)) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add Model, " + lsModelDesc + " already exist.");
                                    }
                                    lbIsExist = true;
                                    break;
                                }
                            }
                            if (!lbIsExist) {
                                oTransInventoryModel.addInventoryModel();
                                addCount++;
                            }
                        }

                    } else {
                        //Inv Model Year
                        for (ModelItemEntryModelYear item : selectedItemsModel) {
                            String lsMakeDesc = item.getTblindexModel03();
                            String lsModelDesc = item.getTblindexModel05();
                            String lsModelCode = item.getTblindexModel08();
                            for (ModelItemEntryModelYear items : selectedItemsYear) {
                                String Year = items.getTblindexModel06();
                                for (int lnCtr = 0; lnCtr <= oTransInventoryModel.getInventoryModelYearList().size() - 1; lnCtr++) {
                                    if (oTransInventoryModel.getInventoryModel(lnCtr, "sModelCde").toString().equals(lsModelCode)) {
                                        ShowMessageFX.Error(null, pxeModuleName, "Skipping, Failed to add Model, " + lsModelDesc + " " + Year + "already exist.");
                                    }
                                    lbIsExist = true;
                                    break;
                                }
                                if (!lbIsExist) {
                                    oTransInventoryModel.addInventoryModelYear();
                                    addCount++;
                                }

                            }

                        }
                    }
                    if (addCount >= 1) {
                        ShowMessageFX.Information(null, pxeModuleName, "Added Vehicle Model successfully.");
                    } else {
                        ShowMessageFX.Error(null, pxeModuleName, "Failed to add vehicle model");
                    }
                }
                CommonUtils.closeStage(btnAdd);
                break;
        }
    }

    private void loadItemModelTable() {
        itemModeldata.clear(); // Clear the previous data in the list
        for (int lnCtr = 0; lnCtr <= oTransInventoryModel.getInventoryModelList().size() - 1; lnCtr++) {
            itemModeldata.add(new ModelItemEntryModelYear(
                    String.valueOf(lnCtr + 1), // ROW
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "sMakeIDxx")),
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "sMakeDesc")),
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "sModelIDx")),
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "sModelDsc")),
                    "",
                    "",
                    String.valueOf(oTransInventoryModel.getInventoryModel(lnCtr, "sModelCde"))
            )
            );
        }
        tblVModelList.setItems(itemModeldata);
    }

    @SuppressWarnings("unchecked")
    private void initItemModelTable() {
        tblindexModel01Select.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblindexModel02.setCellValueFactory(new PropertyValueFactory<>("tblindexModel03"));
        // Set up listener for "Select All" checkbox
        tblindexModel03.setCellValueFactory(new PropertyValueFactory<>("tblindexModel05"));
        tblVModelList.getItems().forEach(new Consumer<ModelItemEntryModelYear>() {
            @Override
            public void accept(ModelItemEntryModelYear item) {
                CheckBox loSelectCheckBoxModel = item.getSelect();
                loSelectCheckBoxModel.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Check if any checkbox is selected
                        boolean lbAnySelected = false;
                        for (ModelItemEntryModelYear itemModel : tblVModelList.getItems()) {
                            if (itemModel.getSelect().isSelected()) {
                                if (itemModel.getTblindexModel05().equals("COMMON")) {
                                    tblVYear.setDisable(true);
                                    chckNoYear.setSelected(true);
                                    chckNoYear.setVisible(false);

                                    for (ModelItemEntryModelYear itemModel2 : tblVModelList.getItems()) {
                                        if (itemModel2.getSelect().isSelected()) {
                                            if (!itemModel2.getTblindexModel05().equals("COMMON")) {
                                                itemModel2.getSelect().setSelected(false);
                                            }
                                        }
                                    }
                                } else {
                                    tblVYear.setDisable(false);
                                    chckNoYear.setSelected(false);
                                    chckNoYear.setVisible(true);
                                    lbAnySelected = true;
                                }
                            }
                        }

                        if (!chckNoYear.isSelected()) {
                            tblVYear.setDisable(false);
                            chckNoYear.setVisible(true);
                            chckNoYear.setSelected(false);
                        }

                        // Check if all checkboxes are selected and update selectModelAll accordingly
                        boolean allSelected = tblVModelList.getItems().stream()
                                .allMatch(new Predicate<ModelItemEntryModelYear>() {
                                    @Override
                                    public boolean test(ModelItemEntryModelYear tableItem) {
                                        return tableItem.getSelect().isSelected();
                                    }
                                });
                        selectModelAll.setSelected(allSelected);
                        tblVYear.setDisable(!lbAnySelected);
                        chckNoYear.setVisible(lbAnySelected);
                        chckNoYear.setSelected(!lbAnySelected);

                    }
                });
            }
        });

        selectModelAll.setOnAction(event -> {
            boolean selectAll = selectModelAll.isSelected();

            // Check/uncheck all items' checkboxes
            tblVModelList.getItems().forEach(new Consumer<ModelItemEntryModelYear>() {
                @Override
                public void accept(ModelItemEntryModelYear item) {
                    if (item.getTblindexModel05().equals("COMMON")) {
                        item.getSelect().setSelected(false);
                        chckNoYear.setVisible(true);
                    } else {
                        item.getSelect().setSelected(selectAll);
                    }

                }
            });

            // Enable/disable tableViewYear and unselect checkboxNoYear accordingly
            if (selectAll) {
                tblVYear.setDisable(false);
                chckNoYear.setVisible(true);
                chckNoYear.setSelected(false);
            } else {
                tblVYear.setDisable(true);
                chckNoYear.setVisible(false);
                chckNoYear.setSelected(true);
            }
        });
    }

    public void initComboFields() {
        tblVYear.setDisable(true);
        chckNoYear.setSelected(true);
        chckNoYear.setVisible(false);
        comboFilter.setOnAction(e -> {
            String lsSelectedFilter = comboFilter.getSelectionModel().getSelectedItem();
            txtSeeks01.setVisible(false);
            txtSeeks01.setManaged(false);
            txtSeeks02.setVisible(false);
            txtSeeks02.setManaged(false);
            // Show relevant controls based on selected filter
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
