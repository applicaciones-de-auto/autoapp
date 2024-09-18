/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
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
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.controllers.service.TechnicianServiceController;
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

    private JobOrder oTransSJO;
    private GRider oApp;
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    private String psVSPTransNo = "";
    private int pnRow = -1;
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
    private TableColumn<Labor, String> tblindex01_labor;
    @FXML
    private TableColumn<Labor, String> tblindex02_labor, tblindex03_labor, tblindex04_labor, tblindex05_labor;
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
    private TableColumn<Part, String> tblindex01_part, tblindex02_part, tblindex03_part, tblindex04_part, tblindex05_part, tblindex06_part;

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
        oTransSJO = new JobOrder(oApp, false, oApp.getBranchCode());
        initLaborTable();
        initAccessoriesTable();
        initCapitalizationFields();
        initFieldActions();
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        initButtonsClick();
        textArea15.focusedProperty().addListener(txtArea_Focus);
        clearFields();
        clearTables();
        initTableKeyPressed();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
        Platform.runLater(() -> {
            if (pbIsVSP) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
                loJSON = oTransSJO.searchVSP(psVSPTransNo, true);
                if (!"error".equals((String) loJSON.get("result"))) {
                    loadSJOFields();
                    pnEditMode = oTransSJO.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    clearFields();
                }
            }
        });
    }

    private void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    private void loadSJOFields() {
        txtField01.setText(oTransSJO.getMasterModel().getMasterModel().getVSPNo());
        txtField02.setText(oTransSJO.getMasterModel().getMasterModel().getDSNo());
        if (oTransSJO.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker03.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransSJO.getMasterModel().getMasterModel().getTransactDte())));
        }
        txtField04.setText(poGetDecimalFormat.format(oTransSJO.getMasterModel().getMasterModel().getLaborAmt()));
        txtField05.setText(poGetDecimalFormat.format(oTransSJO.getMasterModel().getMasterModel().getPartsAmt()));
        txtField06.setText(poGetDecimalFormat.format(oTransSJO.getMasterModel().getMasterModel().getTranAmt()));
        txtField07.setText(oTransSJO.getMasterModel().getMasterModel().getOwnrNm());
        textArea08.setText(oTransSJO.getMasterModel().getMasterModel().getAddress());
        txtField09.setText(oTransSJO.getMasterModel().getMasterModel().getCoBuyrNm());
        txtField10.setText(oTransSJO.getMasterModel().getMasterModel().getCSNo());
        txtField11.setText(oTransSJO.getMasterModel().getMasterModel().getPlateNo());
        txtField12.setText(oTransSJO.getMasterModel().getMasterModel().getFrameNo());
        txtField13.setText(oTransSJO.getMasterModel().getMasterModel().getEngineNo());
        textArea14.setText(oTransSJO.getMasterModel().getMasterModel().getVhclDesc());
        textArea15.setText(oTransSJO.getMasterModel().getMasterModel().getRemarks());
        switch (oTransSJO.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lblJobOrderStatus.setText("Active");
                break;
            case TransactionStatus.STATE_CLOSED:
                lblJobOrderStatus.setText("Approved");
                break;
            case TransactionStatus.STATE_CANCELLED:
                lblJobOrderStatus.setText("Cancelled");
                break;
            case TransactionStatus.STATE_POSTED:
                lblJobOrderStatus.setText("Posted");
                break;
            default:
                lblJobOrderStatus.setText("");
                break;
        }
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
                        oTransSJO.getMasterModel().getMasterModel().setVSPNo("");
                        clearFieldsNoVS();
                    }
                }
                initFields(pnEditMode);
            }
        });
    }

    private void clearFieldsNoVS() {
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
                    loJSON = oTransSJO.searchVSP(lsValue.trim(), false);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadSJOFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
                    break;
            }
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
            initFields(pnEditMode);
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
                oTransSJO = new JobOrder(oApp, false, oApp.getBranchCode());
                loJSON = oTransSJO.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    oTransSJO.getMasterModel().getMasterModel().setJobType("");
                    oTransSJO.getMasterModel().getMasterModel().setPaySrce("3");
                    oTransSJO.getMasterModel().getMasterModel().setWorkCtgy("2");
                    oTransSJO.getMasterModel().getMasterModel().setLaborTyp("");
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
                            switchToTab(tabMain, ImTabPane);
                            loadSJOFields();
                            loadLaborTable();
                            loadAccessoriesTable();
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
                    oTransSJO = new JobOrder(oApp, false, oApp.getBranchCode());
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
                loJSON = oTransSJO.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    switchToTab(tabMain, ImTabPane);
                    loadSJOFields();
                    loadLaborTable();
                    loadAccessoriesTable();
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
            case "btnCancelJobOrder":
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
            case "btnAddTechnician":
                try {
                loadTechnicianServiceWindowDialog();
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
        for (int lnCtr = 0; lnCtr <= oTransSJO.getJOLaborList().size() - 1; lnCtr++) {
            if (oTransSJO.getJOLaborModel().getDetailModel(lnCtr).getUnitPrce() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getJOLaborModel().getDetailModel(lnCtr).getUnitPrce())));
            }

            if (oTransSJO.getJOLaborModel().getDetailModel(lnCtr).getPayChrge().equals("0")) {
                lbChargeType = true;
            }
            laborData.add(new Labor(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransSJO.getJOLaborModel().getDetailModel(lnCtr).getTransNo()),
                    String.valueOf(oTransSJO.getJOLaborModel().getDetailModel(lnCtr).getLaborCde()),
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    String.valueOf(oTransSJO.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc()),
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
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
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
//
            JOVSPLaborController loControl = new JOVSPLaborController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransSJO);
            loControl.setTrans(oTransSJO.getMasterModel().getMasterModel().getTransNo());
            fxmlLoader.setController(loControl);

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
        for (int lnCtr = 0; lnCtr <= oTransSJO.getJOPartsList().size() - 1; lnCtr++) {
            if (oTransSJO.getJOPartsModel().getJOParts(lnCtr).getDescript() != null) {
                lsPartsDesc = String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getDescript());
            }
            if (oTransSJO.getJOPartsModel().getJOParts(lnCtr).getUnitPrce() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getUnitPrce())));
            }
            if (oTransSJO.getJOPartsModel().getJOParts(lnCtr).getQtyEstmt() != null) {
                lsQuantity = String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getQtyEstmt());
            }
            if (oTransSJO.getJOPartsModel().getJOParts(lnCtr).getPayChrge().equals("0")) {
                lbChargeType = true;
            }
            if (oTransSJO.getJOPartsModel().getJOParts(lnCtr).getBarCode() != null) {
                lsBarCode = String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getBarCode());
            }
            accessoriesData.add(new Part(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getTransNo()),
                    String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getStockID()),
                    String.valueOf(oTransSJO.getJOPartsModel().getJOParts(lnCtr).getDescript()),
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
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
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
        laborData.clear();
        accessoriesData.clear();
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        datePicker03.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oApp.getServerDate())));
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
        laborTab.setDisable(txtField01.getText().isEmpty());
        accessoriesTab.setDisable(txtField01.getText().isEmpty());
        technicianTab.setDisable(txtField01.getText().isEmpty());
        accessoriesMatTab.setDisable(true);
        issuanceTab.setDisable(true);
        btnAddLabor.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        btnAddAccessories.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        btnAddTechnician.setDisable(!(lbShow && !txtField01.getText().isEmpty()));
        if (fnValue == EditMode.UPDATE) {
            txtField01.setDisable(true);
        }
        if (fnValue == EditMode.READY) {
            if (!lblJobOrderStatus.getText().equals("Cancelled")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
//                btnPrint.setVisible(true);
//                btnPrint.setManaged(true);
                btnCancelJobOrder.setVisible(true);
                btnCancelJobOrder.setManaged(true);
//                btnDone.setVisible(true);
//                btnDone.setManaged(true);
            }
        }
    }

    private Labor getLaborSelectedItem() {
        return tblViewLabor.getSelectionModel().getSelectedItem();
    }

    private Part getPartSelectedItem() {
        return tblViewAccessories.getSelectionModel().getSelectedItem();
    }

    private void initTableKeyPressed() {
        tblViewLabor.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this labor?")) {
                        Labor selectedVSPLabor = getLaborSelectedItem();
                        int removeCount = 0;
                        if (selectedVSPLabor != null) {
                            String lsRow = selectedVSPLabor.getTblindex01_labor();
                            int lnRow = Integer.parseInt(lsRow);
                            oTransSJO.removeJOLabor(lnRow - 1);
                            removeCount++;
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Removed labor successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Removed labor failed");
                        }
                        loadSJOFields();
                        loadLaborTable();
                    } else {
                        return;
                    }
                }
            }
        });
        tblViewAccessories.setOnKeyPressed(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (ShowMessageFX.YesNo(null, "Remove Confirmation", "Are you sure you want to remove this accessories?")) {
                        Part selectedVSPPart = getPartSelectedItem();
                        int removeCount = 0;
                        if (selectedVSPPart != null) {
                            String lsRow = selectedVSPPart.getTblindex01_part();
                            int lnRow = Integer.parseInt(lsRow);
                            oTransSJO.removeJOParts(lnRow - 1);
                            removeCount++;
                        }
                        if (removeCount >= 1) {
                            ShowMessageFX.Information(null, pxeModuleName, "Removed accessories successfully");
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Removed accessories failed");
                        }
                        loadSJOFields();
                        loadAccessoriesTable();
                    }
                } else {
                    return;
                }
            }
        });
    }

    private void loadTechnicianServiceWindowDialog() throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/service/TechnicianService.fxml"));
            TechnicianServiceController loControl = new TechnicianServiceController();
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

    @FXML
    private void tblViewLabor_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewLabor.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewLabor.getItems().size()) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid labor information.");
                return;
            }
        }
    }

    @FXML
    private void tblViewAccessories_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewAccessories.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewAccessories.getItems().size()) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid accessories information.");
                return;
            }
        }
    }
}
