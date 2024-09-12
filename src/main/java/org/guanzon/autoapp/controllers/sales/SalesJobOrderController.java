/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.models.general.Technician;
import org.guanzon.autoapp.models.sales.Labor;
import org.guanzon.autoapp.models.sales.Part;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class SalesJobOrderController implements Initializable, ScreenInterface {

    private VehicleSalesProposal oTransSJO;
    private GRider oApp;
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    private String psVSPTransNo = "";
    private boolean pbIsVSP = false;
    private final String pxeModuleName = "Sales Job Order"; //Form Title
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private ObservableList<Labor> laborData = FXCollections.observableArrayList();
    private ObservableList<Part> accessoriesData = FXCollections.observableArrayList();
    @FXML
    AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle, lblJobOrderStatus;
    @FXML
    private Button btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCancelJobOrder, btnClose, btnDone, btnAddLabor, btnAddAccessories;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Tab tabMain, laborTab, accessoriesTab, technicianTab, accessoriesMatTab, issuanceTab;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06, txtField07, txtField09, txtField10, txtField11, txtField12, txtField13;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private TextArea textArea14, textArea08, textArea15;
    @FXML
    private TableView<Labor> tblViewLabor;
    @FXML
    private TableColumn<Labor, String> tblindex01_labor, tblindex02_labor, tblindex03_labor, tblindex04_labor, tblindex05_labor, tblindex06_labor,
            tblindex07_labor, tblindex08_labor;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableView<Technician> tblViewTechnician;
    @FXML
    private TableColumn<Technician, String> tblindex01_tech, tblindex02_tech, tblindex03_tech;
    @FXML
    private TableView<?> tblViewPaintings;
    @FXML
    private TableView<?> tblViewPaintings1;
    @FXML
    private Button btnAddTechnician;
    @FXML
    private TableColumn<Part, String> tblindex01_part, tblindex02_part, tblindex03_part, tblindex04_part, tblindex05_part, tblindex06_part, tblindex07_part, tblindex08_part, tblindex09_part, tblindex10_part;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setIsVSPState(boolean fbValue) {
        pbIsVSP = fbValue;
    }

    public void setVSPTrans(String fsValue) {
        psVSPTransNo = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initLaborTable();
        initAccessoriesTable();
        initCapitalizationFields();
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        initButtonsClick();
        textArea15.focusedProperty().addListener(txtArea_Focus);
        clearFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
        Platform.runLater(() -> {
            if (pbIsVSP) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
//                loJSON = oTransSJO.searchVSP(psVSPTransNo, true);
//                if (!"error".equals((String) loJSON.get("result"))) {
//                    loadSJOFields();
//                    pnEditMode = oTransSJO.getEditMode();
//                    initFields(pnEditMode);
//                } else {
//                    clearFields();
//                }
            }
        });

    }

    private void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    private void loadSJOFields() {
        txtField01.setText("");
        txtField02.setText("");
        datePicker03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        txtField04.setText("0.00");
        txtField05.setText("0.00");
        txtField06.setText("0.00");
        txtField07.setText("");
        textArea08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        textArea14.setText("");
        textArea15.setText("");

    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField07,
                txtField09, txtField10, txtField11, txtField12, txtField13);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea08, textArea14, textArea15);

        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        TextArea loTextArea = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTextArea.getId().substring(8, 10));
        String lsValue = loTextArea.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 15:
                    oTransSJO.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    private void initFieldActions() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransSJO.getMasterModel().getMasterModel().setVSPNO("");
                    }
                }
            }
        });
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField lsTxtField = (TextField) event.getSource();
        String txtFieldID = ((TextField) event.getSource()).getId();
        String lsValue = "";
        if (lsTxtField.getText() == null) {
            lsValue = "";
        } else {
            lsValue = lsTxtField.getText();
        }
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (txtFieldID) {
                case "txtField01":
//                    loJSON = oTransSJO.searchVSP(lsValue.trim(), false);
//                    if (!"error".equals(loJSON.get("result"))) {
////                        loadSJOFields();
//                    } else {
//                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                        return;
//                    }
                    break;
            }
            initFields(pnEditMode);
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        }
    }

    private void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnCancelJobOrder, btnClose, btnDone, btnAddLabor, btnAddAccessories);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                switchToTab(tabMain, ImTabPane);
                oTransSJO = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                loJSON = oTransSJO.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadSJOFields();
                    pnEditMode = oTransSJO.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransSJO.updateTransaction();
                pnEditMode = oTransSJO.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Sales Job Order Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTransSJO.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Job Order Information", (String) loJSON.get("message"));
                        loJSON = oTransSJO.openTransaction(oTransSJO.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadSJOFields();
                            pnEditMode = oTransSJO.getEditMode();
                            initFields(pnEditMode);
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    switchToTab(tabMain, ImTabPane);
                    oTransSJO = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Sales Job Order Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransSJO.searchTransaction("", true, false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadSJOFields();
                    pnEditMode = oTransSJO.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Sales Job Order Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnPrint":
//                loadSJOPrint();
                break;
            case "btnCancelSJO":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this SJO?")) {
                    loJSON = oTransSJO.cancelTransaction(oTransSJO.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "SJO Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "SJO Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransSJO.openTransaction(oTransSJO.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadSJOFields();
                        pnEditMode = oTransSJO.getEditMode();
                        initFields(pnEditMode);
                    }
                }
                break;
            case "btnDone":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to done this SJO?")) {
                }
                break;
            case "btnAddLabor":
                try {
                loadLaborWindowDialog();
            } catch (IOException ex) {
                Logger.getLogger(SalesJobOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnAddAccessories":
                try {
                loadPartsWindowDialog();
            } catch (IOException ex) {
                Logger.getLogger(SalesJobOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                break;
        }
        initFields(pnEditMode);
    }

    private void loadLaborTable() {
        laborData.clear();
        boolean lbAdditional = false;
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsDiscAmount = "";
        String lsNetAmount = "";
        String lsJoNoxx = "";
        for (int lnCtr = 0; lnCtr <= oTransSJO.getVSPLaborList().size() - 1; lnCtr++) {
            if (oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getLaborAmt())));
            }
            if (oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount() != null) {
                lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDscount())));
            }
            if (oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt() != null) {
                lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getNtLabAmt())));
            }
            if (oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getAddtl().equals("1")) {
                lbAdditional = true;
            }
            if (oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo() != null) {
                lsJoNoxx = oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getDSNo();
            }
            laborData.add(new Labor(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getTransNo()),
                    String.valueOf(oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getLaborCde()),
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    String.valueOf(oTransSJO.getVSPLaborModel().getVSPLabor(lnCtr).getLaborDsc()),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    lsJoNoxx,
                    lbAdditional,
                    lbChargeType
            ));
            lbAdditional = false;
            lbChargeType = false;
            lsGrsAmount = "";
            lsDiscAmount = "";
            lsNetAmount = "";
            lsJoNoxx = "";
        }
        tblViewLabor.setItems(laborData);
    }

    private void initLaborTable() {
        tblindex01_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex03_labor"));
        tblindex03_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_labor"));
        tblindex06_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex06_labor"));
        tblindex07_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex08_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex10_labor"));
        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void loadLaborWindowDialog() throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/JOVSPLabor.fxml"));

            JOVSPLaborController loControl = new JOVSPLaborController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransSJO);
            loControl.setTrans(oTransSJO.getMasterModel().getMasterModel().getTransNo());
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
            loadLaborTable();
            loadSJOFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private void loadAccessoriesTable() {
        accessoriesData.clear();
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsQuantity = "";
        String lsDiscAmount = "";
        String lsTotalAmount = "";
        String lsNetAmount = "";
        String lsPartsDesc = "";
        String lsBarCode = "";
        String lsJoNoxx = "";
        for (int lnCtr = 0; lnCtr <= oTransSJO.getVSPPartsList().size() - 1; lnCtr++) {
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice())));
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                lsQuantity = String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null && oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                BigDecimal lsGrsAmt = new BigDecimal(String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice()));
                int lsQuan = oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getQuantity();
                lsTotalAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lsGrsAmt.doubleValue() * lsQuan)));
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount() != null) {
                lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount())));
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt() != null) {
                lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt())));
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc() != null) {
                lsPartsDesc = String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc());
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getBarCode() != null) {
                lsBarCode = String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getBarCode());
            }
            if (oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getDSNo() != null) {
                lsJoNoxx = oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getDSNo();
            }
            accessoriesData.add(new Part(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getTransNo()),
                    String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getStockID()),
                    String.valueOf(oTransSJO.getVSPPartsModel().getVSPParts(lnCtr).getDescript()),
                    lsQuantity,
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    lsBarCode,
                    "",
                    lsJoNoxx,
                    lsPartsDesc,
                    lsTotalAmount,
                    lbChargeType
            ));
            lbChargeType = false;
            lsGrsAmount = "";
            lsQuantity = "";
            lsDiscAmount = "";
            lsTotalAmount = "";
            lsNetAmount = "";
            lsPartsDesc = "";
            lsBarCode = "";
            lsJoNoxx = "";
        }
        tblViewAccessories.setItems(accessoriesData);
    }

    private void initAccessoriesTable() {
        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("tblindex13_part"));
        tblindex07_part.setCellValueFactory(new PropertyValueFactory<>("tblindex07_part"));
        tblindex08_part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_part"));
        tblindex09_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex10_part.setCellValueFactory(new PropertyValueFactory<>("tblindex11_part"));
        tblViewAccessories.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void loadPartsWindowDialog() throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/JOVSPAccessories.fxml"));

            JOVSPAccessoriesController loControl = new JOVSPAccessoriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransSJO);
            loControl.setTrans(oTransSJO.getMasterModel().getMasterModel().getTransNo());
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
            loadAccessoriesTable();
            loadSJOFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private void clearTables() {
        initLaborTable();
        initAccessoriesTable();
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        datePicker03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        txtField04.setText("0.00");
        txtField05.setText("0.00");
        txtField06.setText("0.00");
        txtField07.setText("");
        textArea08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        textArea14.setText("");
        textArea15.setText("");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField01.setDisable(!lbShow);
        textArea15.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnPrint.setDisable(true);
        btnCancelJobOrder.setVisible(false);
        btnCancelJobOrder.setManaged(false);
        btnDone.setVisible(false);
        btnDone.setManaged(false);
        laborTab.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        accessoriesTab.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        technicianTab.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        accessoriesMatTab.setDisable(true);
        issuanceTab.setDisable(true);
        btnAddLabor.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        btnAddAccessories.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        if (fnValue == EditMode.READY) {
            if (!lblJobOrderStatus.getText().equals("Cancelled")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                btnCancelJobOrder.setVisible(true);
                btnCancelJobOrder.setManaged(true);
                btnDone.setVisible(true);
                btnDone.setManaged(true);
            }
        }
    }
}
