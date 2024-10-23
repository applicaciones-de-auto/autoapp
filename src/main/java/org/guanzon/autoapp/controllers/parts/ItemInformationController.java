package org.guanzon.autoapp.controllers.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.parts.InventoryInformation;
import org.guanzon.autoapp.controllers.components.ViewPhotoController;
import org.guanzon.autoapp.controllers.parameters.BrandController;
import org.guanzon.autoapp.controllers.parameters.CategoryController;
import org.guanzon.autoapp.controllers.parameters.InvTypeController;
import org.guanzon.autoapp.controllers.parameters.MeasurementController;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.models.parts.ItemInfoModelYear;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class ItemInformationController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private InventoryInformation oTrans;
    private String pxeModuleName = "Item Information";
    private int pnEditMode;
    private double xOffset, yOffset = 0;
    private int pnRow = -1;
    private int lnCtr;
    private String psFileName = "";
    private String psFileUrl = "";
    private String imgIdentfier = "";
    private Image poImage;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    UnloadForm poUnload = new UnloadForm(); //Object for closing form
    private ObservableList<ItemInfoModelYear> modelData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnAdd, btnClose, btnSave, btnEdit, btnCancel, btnBrowse,
            btnInvType, btnCategory, btnMeasurement, btnBrandName,
            btnModelAdd, btnModelDel, btnModelExpand,
            btnLoadCamera, btnUpload, btnLoadPhoto, btnRemoveImage,
            btnDeactivate, btnActive;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField13, txtField14;
    @FXML
    private Button btnSupsDel, btnSupsAdd;
    @FXML
    private TableView<?> tblSupersede;
    @FXML
    private TableColumn<?, ?> tblindex01_sups;
    @FXML
    private TableColumn<?, ?> tblindex02_sups;
    @FXML
    private TableColumn<?, ?> tblindex03_sups;
    @FXML
    private TableColumn<?, ?> tblindex04_sups;
    @FXML
    private TableView<ItemInfoModelYear> tblModelView;
    @FXML
    private TableColumn<ItemInfoModelYear, String> tblindexModel01, tblindexModel03, tblindexModel04, tblindexModel05;
    @FXML
    private TableColumn<ItemInfoModelYear, Boolean> tblindexModel02;
    @FXML
    private CheckBox selectAllModelCheckBox;
    @FXML
    private ImageView imgPartsPic;
    @FXML
    private Label lblStatus;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new InventoryInformation(oApp, false, oApp.getBranchCode());
        initModelTable();
        initCapitalizationFields();
        initPatternFields();
        initLimiterFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initTextFieldsProperty();
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10,
                txtField11, txtField12, txtField13, txtField14);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getModel().getModel().getStockID());
        txtField02.setText(oTrans.getModel().getModel().getBarCode());
        txtField03.setText(oTrans.getModel().getModel().getBrandNme());
        txtField04.setText(oTrans.getModel().getModel().getDescript());
        txtField05.setText(oTrans.getModel().getModel().getBriefDsc());
        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getModel().getModel().getUnitPrce()))));
        txtField07.setText(oTrans.getModel().getModel().getInvTypDs());
        txtField08.setText(oTrans.getModel().getModel().getCatgeDs1());
        txtField09.setText(""); //oTrans.getModel().getModel().getLocation()
        txtField10.setText(oTrans.getModel().getModel().getMeasurNm());
        txtField11.setText(""); //quantity applied of inventory // unit
        txtField12.setText("");//quantity
        txtField13.setText(""); //Equivalent Unit for Transferred Materials
        txtField13.setText(""); // quantity

        if (oTrans.getModel().getModel().getFileName() != null) {
            String imageFilePath = String.valueOf(oTrans.getModel().getModel().getFileName());
            String imageName = imageFilePath.substring(imageFilePath.lastIndexOf('/') + 1);
            if (imageFilePath == null || imageFilePath.isEmpty()) {
                Image loImage = new Image("file:D:/Integrated Automotive System/autoapp/src/main/resources/org/guanzon/autoapp/images/no-image-available.png");
                imgPartsPic.setImage(loImage);
            } else {
                psFileUrl = imageFilePath;
                psFileName = imageName;
                poImage = new Image(imageFilePath);
                Image image = new Image(imageFilePath);
                imgPartsPic.setImage(image);
            }
        }
        if (String.valueOf(oTrans.getModel().getModel().getRecdStat()).equals("1")) {
            lblStatus.setText("Active");
        } else {
            lblStatus.setText("Deactivated");
        }
        return true;
    }

    @Override
    public void initPatternFields() {

    }

    @Override
    public void initLimiterFields() {

    }

    @Override
    public void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(txtField02, txtField04, txtField05, txtField06, txtField09,
                txtField11, txtField12, txtField14);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2:
                    oTrans.getModel().getModel().setBarCode(lsValue);
                    oTrans.getModel().getModel().setTrimBCde(lsValue);
                    checkExistingItemInformation();
                    break;
                case 4:
                    oTrans.getModel().getModel().setDescript(lsValue);
                    break;
                case 5:
                    oTrans.getModel().getModel().setBriefDsc(lsValue);
                    break;
                case 6:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTrans.getModel().getModel().setUnitPrce(Double.valueOf(lsValue.replace(",", "")));
                    txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getModel().getModel().getUnitPrce()))));
                    break;

            }
        } else {
            loTxtField.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField07, txtField08, txtField09, txtField10,
                txtField11, txtField12, txtField13, txtField14);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (null != event.getCode()) {
            switch (event.getCode()) {
                case ENTER:
                case F3:
                    switch (txtFieldID) {
                        case "txtField03":
                            loJSON = oTrans.searchBrand(lsValue, true);
                            if (!"error".equals((String) loJSON.get("result"))) {
                                txtField03.setText(oTrans.getModel().getModel().getBrandNme());
                            } else {
                                txtField03.clear();
                                txtField03.requestFocus();
                                return;
                            }
                            break;
                        case "txtField07":
                            loJSON = oTrans.searchInvType(lsValue, true);
                            if (!"error".equals((String) loJSON.get("result"))) {
                                txtField07.setText(oTrans.getModel().getModel().getInvTypDs());
                            } else {
                                txtField07.clear();
                                txtField07.requestFocus();
                                return;
                            }
                            break;
                        case "txtField08":
                            loJSON = oTrans.searchInvCategory(lsValue, true);
                            if (!"error".equals((String) loJSON.get("result"))) {
                                txtField08.setText(oTrans.getModel().getModel().getCatgeDs1());
                            } else {
                                txtField08.clear();
                                txtField08.requestFocus();
                                return;
                            }
                            break;
                        case "txtField10":
                            loJSON = oTrans.searchMeasure(lsValue, true);
                            if (!"error".equals((String) loJSON.get("result"))) {
                                txtField10.setText(oTrans.getModel().getModel().getMeasurNm());
                            } else {
                                txtField10.clear();
                                txtField10.requestFocus();
                                return;
                            }
                            break;
                        case "txtField13":
                            break;
                    }
                    initFields(pnEditMode);
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                case UP:
                    event.consume();
                    CommonUtils.SetPreviousFocus((TextField) event.getSource());
                    break;
                case DOWN:
                    event.consume();
                    CommonUtils.SetNextFocus((TextField) event.getSource());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void textArea_KeyPressed(KeyEvent event) {

    }

    @Override
    public void initButtonsClick() {
        List<Button> buttons = Arrays.asList(btnAdd, btnClose, btnSave, btnEdit, btnCancel, btnBrowse,
                btnInvType, btnCategory, btnMeasurement, btnBrandName,
                btnModelAdd, btnModelDel, btnModelExpand,
                btnLoadCamera, btnUpload, btnLoadPhoto, btnRemoveImage,
                btnDeactivate, btnActive);
        buttons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTrans = new InventoryInformation(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newRecord();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateRecord();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Inventory Information Saving....", "Are you sure, do you want to save?")) {
                    if (txtField02.getText().matches("[^a-zA-Z].*")) {
                        ShowMessageFX.Warning(null, "Inventory Information", "Please enter valid Item information.");
                        txtField02.setText("");
                        return;
                    }
                    if (txtField02.getText().trim().equals("")) {
                        ShowMessageFX.Warning(null, "Inventory Information", "Please enter value Item information.");
                        txtField02.setText("");
                        return;
                    }
                    oTrans.getModel().getModel().setInvStat("0");
                    oTrans.getModel().getModel().setGenuine("0");
                    loJSON = oTrans.saveRecord();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openRecord(oTrans.getModel().getModel().getStockID());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadModelTable();
                            initFields(pnEditMode);
                            pnEditMode = oTrans.getEditMode();
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTrans = new InventoryInformation(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                oTrans.getModel().getModel().setFileName("");
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search Inventory Information", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchRecord("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadModelTable();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Inventory Information", (String) loJSON.get("message"));
                    clearFields();
                    clearTables();
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure you want to close this Tab?")) {
                    if (poUnload != null) {
                        poUnload.unloadForm(AnchorMain, oApp, pxeModuleName);
                    } else {
                        ShowMessageFX.Warning(null, "Warning", "Please notify the system administrator to configure the null value at the close button.");
                    }
                }
                break;
            case "btnDeactivate":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getStockID();
                    loJSON = oTrans.deactivateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Inventory Information", (String) loJSON.get("message"));
                        return;
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getStockID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadModelTable();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnActive":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure, do you want to change status?") == true) {
                    String fsValue = oTrans.getModel().getModel().getStockID();
                    loJSON = oTrans.activateRecord(fsValue);
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Inventory Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "Inventory Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openRecord(oTrans.getModel().getModel().getStockID());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadModelTable();
                        initFields(pnEditMode);
                        pnEditMode = oTrans.getEditMode();
                    }
                }
                break;
            case "btnBrandName":
                loadBrandNameWindow();
                break;
            case "btnInvType":
                loadInventoryTypeWindow();
                break;
            case "btnCategory":
                loadCategoryWindow();
                break;
            case "btnMeasurement":
                loadMeasurementWindow();
                break;
            case "btnModelAdd":
                loadModelWindow();
                loadModelTable();
                break;
            case "btnModelDel":
                ObservableList<ItemInfoModelYear> selectedItems = FXCollections.observableArrayList();
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Are you sure you want to remove?")) {
                    for (ItemInfoModelYear item : tblModelView.getItems()) {
                        if (item.getSelect().isSelected()) {
                            selectedItems.add(item);
                        }
                    }

                    // Maps to hold the model codes and their corresponding years
                    Map<String, List<Integer>> modelYearMap = new HashMap<>();

                    // Loop through selected items and populate the map
                    for (ItemInfoModelYear item : selectedItems) {
                        String lsModelCode = item.getTblindexModel08();
                        int lnYear = item.getTblindexModel06().isEmpty() ? 0 : Integer.parseInt(item.getTblindexModel06());

                        // Add the year to the corresponding model code in the map
                        modelYearMap.computeIfAbsent(lsModelCode, k -> new ArrayList<>()).add(lnYear);
                    }

                    // Debug: Print out the modelYearMap to ensure it contains the correct data
                    for (Map.Entry<String, List<Integer>> entry : modelYearMap.entrySet()) {
                        System.out.println("Model Code: " + entry.getKey() + ", Years: " + entry.getValue());
                    }

                    // Now loop through the map and remove the models
                    String lsMessage = "";
                    int removeCount = 0;
                    for (Map.Entry<String, List<Integer>> entry : modelYearMap.entrySet()) {
                        String modelCode = entry.getKey();
                        Integer[] lnModelYears = entry.getValue().toArray(new Integer[0]);

                        // Debug: Check the content of lnModelYears before passing it to removeInvModel_Year
                        System.out.println("Removing Model Code: " + modelCode + " with Years: " + Arrays.toString(lnModelYears));

                        try {
                            // Call the removeModel method
                            loJSON = oTrans.removeInvModel_Year(modelCode, lnModelYears);
                            if (!"error".equals((String) loJSON.get("result"))) {
                                lsMessage = (String) loJSON.get("message");
                                removeCount++;
                            } else {
                                lsMessage = (String) loJSON.get("message");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Error removing model: " + modelCode + ", Years: " + Arrays.toString(lnModelYears));
                            e.printStackTrace();
                        }
                    }
                    if (removeCount >= 1) {
                        ShowMessageFX.Information(null, pxeModuleName, lsMessage);
                        selectAllModelCheckBox.setSelected(false);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, lsMessage);
                    }
                    // Reload the table after removal
                }
                loadModelTable();
                break;
            case "btnModelExpand":
                loadModelExpandWindow();
                break;
            case "btnLoadCamera":
                break;
            case "btnUpload":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    FileChooser fileChooser = new FileChooser();
                    // Set the title and extension filters if desired
                    fileChooser.setTitle("Select Image File");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                    // Show the file chooser dialog
                    File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
                    if (selectedFile != null) {
                        // Load the selected image file
                        Image image = new Image(selectedFile.toURI().toString());
                        imgPartsPic.setImage(image);

                        psFileUrl = selectedFile.toURI().toString();
                        psFileName = selectedFile.getName();
                        poImage = new Image(selectedFile.toURI().toString());
                        oTrans.getModel().getModel().setFileName(psFileUrl);
                    }
                }
                break;
            case "btnLoadPhoto":
                if (!oTrans.getModel().getModel().getFileName().isEmpty() && oTrans.getModel().getModel().getFileName() != null) {
                    loadPhotoWindow();
                }
                break;
            case "btnRemoveImage":
                break;
            default:
                ShowMessageFX.Warning(null, "Integrated Automotive System", "Please contact admin to assist about no button available");
                break;
        }
        initFields(pnEditMode);
    }

    @Override
    public void initComboBoxItems() {

    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void initTextFieldsProperty() {
        txtField03.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getModel().getModel().setBrandCde("");
                        oTrans.getModel().getModel().setBrandNme("");
                    }
                }
            }
        });
        txtField10.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getModel().getModel().setMeasurID("");
                        oTrans.getModel().getModel().setMeasurNm("");
                    }
                }
            }
        });
    }

    @Override
    public void clearTables() {
        modelData.clear();
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField04,
                txtField05, txtField06, txtField07, txtField08, txtField09, txtField10,
                txtField11, txtField12, txtField13, txtField14);
        Image loImageError = new Image("file:D:/Integrated Automotive System/autoapp/src/main/resources/org/guanzon/autoapp/images/no-image-available.png");
        imgPartsPic.setImage(loImageError);
        selectAllModelCheckBox.setSelected(false);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        tblindexModel02.setVisible(lbShow);
        CustomCommonUtil.setDisable(true, btnSupsAdd, btnSupsDel, txtField09);
        btnLoadPhoto.setDisable(false);
        CustomCommonUtil.setDisable(!lbShow, txtField02, txtField03, txtField04, txtField05,
                txtField06, txtField07, txtField08, txtField10, txtField13,
                btnModelAdd, btnModelDel, btnModelExpand, btnRemoveImage, btnLoadCamera,
                btnUpload);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setVisible(false, btnDeactivate, btnEdit, btnActive);
        CustomCommonUtil.setManaged(false, btnDeactivate, btnEdit, btnActive);
        if (fnValue == EditMode.UNKNOWN) {
            btnLoadPhoto.setDisable(true);
        }
        if (fnValue == EditMode.READY) {
            if (oTrans.getModel().getModel().getRecdStat().equals("1")) {
                CustomCommonUtil.setVisible(true, btnDeactivate, btnEdit);
                CustomCommonUtil.setManaged(true, btnDeactivate, btnEdit);
            } else {
                btnActive.setVisible(true);
                btnActive.setManaged(true);
            }
        }
    }

    private boolean checkExistingItemInformation() {
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.checkExistingRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            if (ShowMessageFX.YesNo(null, pxeModuleName, (String) loJSON.get("message"))) {
                loJSON = oTrans.openRecord((String) loJSON.get("sStockIDx"));
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadModelTable();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /*OPEN PHOTO WINDOW*/
    private void loadPhotoWindow() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/components/ViewPhoto.fxml"));

            ViewPhotoController loControl = new ViewPhotoController();
            loControl.setGRider(oApp);
            if (!oTrans.getModel().getModel().getFileName().isEmpty()) {
                loControl.setPicName(psFileName);
                loControl.setPicUrl(psFileUrl);
            } else {
                loControl.setPicName("");
                loControl.setPicUrl("");
            }
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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", e.getMessage());
            System.exit(1);
        }

    }

    private void loadBrandNameWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/Brand.fxml"));
            BrandController loControl = new BrandController();
            loControl.setGRider(oApp);
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
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning");
            System.exit(1);
        }
    }

    private void loadInventoryTypeWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/InvType.fxml"));
            InvTypeController loControl = new InvTypeController();
            loControl.setGRider(oApp);
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
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning");
            System.exit(1);
        }
    }

    private void loadCategoryWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/Category.fxml"));
            CategoryController loControl = new CategoryController();
            loControl.setGRider(oApp);
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
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning");
            System.exit(1);
        }
    }

    private void loadMeasurementWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/Measurement.fxml"));
            MeasurementController loControl = new MeasurementController();
            loControl.setGRider(oApp);
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
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning");
            System.exit(1);
        }
    }

    private void loadModelExpandWindow() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parts/ItemInfoExpandModel.fxml"));
            ItemInfoExpandModelController loControl = new ItemInfoExpandModelController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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
            stage.setTitle("");
            stage.showAndWait();
            loadModelTable();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning");
            System.exit(1);
        }
    }

    //to add models
    private void loadModelWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parts/ItemInfoModel.fxml"));
            ItemInfoModelController loControl = new ItemInfoModelController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
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
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(null, e.getMessage(), "Warning");
            System.exit(1);
        }
    }

    private void loadModelTable() {
        modelData.clear();
        String lsYearModl = "";
        String lsMakex = "";
        String lsModel = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getInventoryModelYearList().size() - 1; lnCtr++) {
            if (!oTrans.getInventoryModelYear(lnCtr, "nYearModl").equals(0)) {
                lsYearModl = String.valueOf(oTrans.getInventoryModelYear(lnCtr, "nYearModl"));
            }
            if (oTrans.getInventoryModelYear(lnCtr, "sMakeDesc") != null) {
                lsMakex = String.valueOf(oTrans.getInventoryModelYear(lnCtr, "sMakeDesc"));
            }
            if (oTrans.getInventoryModelYear(lnCtr, "sModelDsc") != null) {
                lsModel = String.valueOf(oTrans.getInventoryModelYear(lnCtr, "sModelDsc"));
            }
            modelData.add(new ItemInfoModelYear(
                    String.valueOf(lnCtr + 1), // ROW
                    "",
                    lsMakex,
                    "",
                    lsModel,
                    lsYearModl,
                    String.valueOf(lnCtr),
                    String.valueOf(oTrans.getInventoryModelYear(lnCtr, "sModelCde"))
            ));
            lsYearModl = "";
        }

    }

    private void initModelTable() {
        tblindexModel01.setCellValueFactory(new PropertyValueFactory<>("tblindexModel01"));
        tblindexModel02.setCellValueFactory(new PropertyValueFactory<>("select"));
        tblModelView.getItems().forEach(item -> {
            CheckBox loSelectCheckBox = item.getSelect();
            loSelectCheckBox.setOnAction(event -> {
                if (tblModelView.getItems().stream().allMatch(tableItem -> tableItem.getSelect().isSelected())) {
                    selectAllModelCheckBox.setSelected(true);
                } else {
                    selectAllModelCheckBox.setSelected(false);
                }
            });
        });
        selectAllModelCheckBox.setOnAction(event -> {
            boolean newValue = selectAllModelCheckBox.isSelected();
            if (!tblModelView.getItems().isEmpty()) {
                tblModelView.getItems().forEach(item -> item.getSelect().setSelected(newValue));
            }
        });
        tblindexModel03.setCellValueFactory(new PropertyValueFactory<>("tblindexModel03"));
        tblindexModel04.setCellValueFactory(new PropertyValueFactory<>("tblindexModel05"));
        tblindexModel05.setCellValueFactory(new PropertyValueFactory<>("tblindexModel06"));
        tblModelView.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblModelView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
        modelData.clear();
        tblModelView.setItems(modelData);
    }
}
