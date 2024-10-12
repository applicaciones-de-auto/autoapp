package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.clients.Vehicle_Gatepass;
import org.guanzon.autoapp.interfaces.GRecordInterface;
import org.guanzon.autoapp.models.general.JobDone;
import org.guanzon.autoapp.models.general.VehicleComponents;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleGatePassController implements Initializable, ScreenInterface, GRecordInterface {

    private GRider oApp;
    private Vehicle_Gatepass oTrans;
    private String pxeModuleName = "Vehicle Gate Pass";
    private boolean pbIsVSP = true;
    private String psVSPTransNo = "";
    private String psVGPTransNo = "";
    private boolean pbOpenEvent = false;
    private boolean pbIsClicked = false;
    private int pnEditMode = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    public boolean bState = false;
    private ObservableList<JobDone> jobDoneData = FXCollections.observableArrayList();
    private ObservableList<VehicleComponents> compoData = FXCollections.observableArrayList();
    ObservableList<String> cVhclSrc = FXCollections.observableArrayList("VEHICLE SALES");
    ObservableList<String> cPurpose = FXCollections.observableArrayList("DELIVER TO CUSTOMER");

    @FXML
    private TextField txtField01, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11;
    @FXML
    private DatePicker datePicker02;
    @FXML
    private ComboBox<String> comboBox03, comboBox04;
    @FXML
    private TextArea textArea07, textArea12;
    @FXML
    private TableView<JobDone> tblViewJobDone;
    @FXML
    private TableColumn<JobDone, String> tblindex01_done, tblindex02_done, tblindex03_done, tblindex04_done,
            tblindex05_done, tblindex06_done, tblindex07_done;
    @FXML
    private TableView<VehicleComponents> tblViewVehicleComponents;
    @FXML
    private TableColumn<VehicleComponents, Boolean> tblIndex01_VComp;
    @FXML
    private TableColumn<VehicleComponents, String> tblIndex02_VComp;
    @FXML
    private Button btnAdd, btnBrowse, btnSave, btnEdit, btnCancel, btnPrint, btnClose, btnVGPCancel;
    @FXML
    private Label lblStatus;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setIsVSPState(boolean fbValue) {
        pbIsVSP = fbValue;
    }

    public void setVSPTransNo(String fsValue) {
        psVSPTransNo = fsValue;
    }

    public void setVGPTransNo(String fsValue) {
        psVGPTransNo = fsValue;
    }

    public void setOpenEvent(boolean fbValue) {
        pbOpenEvent = fbValue;
    }

    public void setIsClicked(boolean fbValue) {
        pbIsClicked = fbValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new Vehicle_Gatepass(oApp, false, oApp.getBranchCode());

        initJobDoneTable();
        initVehicleComponentsTable();

        initCapitalizationFields();
        initFieldAction();
        initButtonsClick();
        clearTables();
        clearFields();
        initFields(pnEditMode);

        Platform.runLater(() -> {
            JSONObject loJSON = new JSONObject();
            if (!pbIsClicked) {
                if (pbIsVSP) {
                    btnAdd.fire();
                    loJSON = oTrans.searchVSP(psVSPTransNo, true);
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadJobDoneTable();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                    } else {
                        clearFields();
                        clearTables();
                    }
                }
            } else {
                loJSON = oTrans.openTransaction(psVGPTransNo);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadJobDoneTable();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("result"));
                }
            }
        });
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea12);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        if (oTrans.getMasterModel().getMasterModel().getTransNo() != null) {
            txtField01.setText(oTrans.getMasterModel().getMasterModel().getTransNo());
        }
        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null && !String.valueOf(oTrans.getMasterModel().getMasterModel().getTransactDte()).isEmpty()) {
            datePicker02.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTransactDte())));
        }
        int lsVhclSource = -1;
        if (oTrans.getMasterModel().getMasterModel().getSourceGr() != null && !String.valueOf(oTrans.getMasterModel().getMasterModel().getSourceGr()).isEmpty()) {
            switch (oTrans.getMasterModel().getMasterModel().getSourceGr()) {
                case "VEHICLE SALES":
                    lsVhclSource = 0;
                    break;
            }
            comboBox03.getSelectionModel().select(lsVhclSource);
        }
        comboBox04.setValue(null);
        txtField05.setText(oTrans.getVSPModel().getMasterModel().getUDRNo());
        txtField06.setText(oTrans.getVSPModel().getMasterModel().getBuyCltNm());
        textArea07.setText(oTrans.getVSPModel().getMasterModel().getAddress());
        txtField08.setText(oTrans.getVSPModel().getMasterModel().getCSNo());
        txtField09.setText(oTrans.getVSPModel().getMasterModel().getPlateNo());
        txtField10.setText(oTrans.getVSPModel().getMasterModel().getFrameNo());
        txtField11.setText(oTrans.getVSPModel().getMasterModel().getEngineNo());
        textArea12.setText(oTrans.getVSPModel().getMasterModel().getVhclFDsc());
        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lblStatus.setText("Active");
                break;
            case TransactionStatus.STATE_CLOSED:
                lblStatus.setText("Approved");
                break;
            case TransactionStatus.STATE_CANCELLED:
                lblStatus.setText("Cancelled");
                break;
            case TransactionStatus.STATE_POSTED:
                lblStatus.setText("Posted");
                break;
            default:
                lblStatus.setText("");
                break;
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
    }

    private void initFieldAction() {
        comboBox03.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setSourceGr(String.valueOf(comboBox03.getValue()));
            }
        }
        );
        datePicker02.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setTransactDte(SQLUtil.toDate(datePicker02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
    }

    @Override
    public void initTextKeyPressed() {
    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {

    }

    @Override
    public void textArea_KeyPressed(KeyEvent event) {

    }

    @Override
    public void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnBrowse, btnEdit, btnCancel, btnSave, btnPrint, btnClose, btnVGPCancel);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTrans = new Vehicle_Gatepass(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadJobDoneTable();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Gate Pass Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Sales Job Order Information", (String) loJSON.get("message"));
                        CommonUtils.closeStage(btnSave);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTrans = new Vehicle_Gatepass(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Gate Pass Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        pnEditMode = oTrans.getEditMode();
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Gate Pass Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnPrint":
                loadVGPPrint();
                break;
            case "btnVGPCancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this VGP?")) {
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "VGP Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "VGP Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        loadJobDoneTable();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
                        CommonUtils.closeStage(btnVGPCancel);
                    }
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
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
    }

    @Override
    public void clearTables() {
        jobDoneData.clear();
        compoData.clear();
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField05, txtField06, txtField08,
                txtField09, txtField10, txtField11);
        datePicker02.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        CustomCommonUtil.setText("", textArea07, textArea12);
        CustomCommonUtil.setValue(null, comboBox03, comboBox04);
        lblStatus.setText("");
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setVisible(false, btnPrint, btnVGPCancel, btnEdit);
        CustomCommonUtil.setManaged(false, btnPrint, btnVGPCancel, btnEdit);
        if (pbOpenEvent) {
            btnSave.setManaged(lbShow);
            btnSave.setVisible(lbShow);
            CustomCommonUtil.setVisible(false, btnCancel, btnAdd, btnBrowse);
            CustomCommonUtil.setManaged(false, btnCancel, btnAdd, btnBrowse);
            CustomCommonUtil.setDisable(true, comboBox03, comboBox04, txtField05);
            if (fnValue == EditMode.READY) {
                if (!lblStatus.getText().equals("Cancelled")) {
                    CustomCommonUtil.setVisible(true, btnPrint, btnVGPCancel);
                    CustomCommonUtil.setManaged(true, btnPrint, btnVGPCancel);
                }
            }
        } else {
            btnAdd.setVisible(!lbShow);
            btnAdd.setManaged(!lbShow);
            CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
            CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
            CustomCommonUtil.setVisible(false, btnEdit, btnPrint);
            CustomCommonUtil.setManaged(false, btnEdit, btnPrint);
            if (fnValue == EditMode.READY) {
                if (!lblStatus.getText().equals("Cancelled")) {
                    btnPrint.setManaged(true);
                    btnPrint.setVisible(true);
                    if (!pbOpenEvent) {
                        btnEdit.setVisible(true);
                        btnEdit.setManaged(true);
                    }
                    btnVGPCancel.setManaged(true);
                    btnVGPCancel.setVisible(true);
                }
            }
        }

        if (pbIsClicked) {
            btnSave.setManaged(false);
            btnSave.setVisible(false);
            CustomCommonUtil.setDisable(true, comboBox03, comboBox04, txtField05, datePicker02);
        }
    }

    private void loadJobDoneTable() {
        jobDoneData.clear();
        String lsItem = "";
        String lsDescID = "";
        String lsDesc = "";
        String lsJoNo = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getVGPItemList().size() - 1; lnCtr++) {
            switch (String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getItemType())) {
                case "l":
                    lsItem = "LABOR";
                    lsDescID = String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getLaborCde());
                    lsDesc = String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getLaborDsc());
                    break;
                case "p":
                    lsItem = "PARTS";
                    lsDescID = String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getStockID());
                    lsDesc = String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getStockDsc());
                    break;
            }
            if (oTrans.getVGPItemModel().getDetailModel(lnCtr).getDSNo() != null) {
                lsJoNo = String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getDSNo());
            }
            jobDoneData.add(new JobDone(
                    String.valueOf(lnCtr + 1),
                    lsItem,
                    lsDescID,
                    lsDesc,
                    String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getQuantity()), // Assuming 1 as a placeholder
                    lsJoNo,
                    String.valueOf(oTrans.getVGPItemModel().getDetailModel(lnCtr).getReleased()),
                    ""
            ));
            lsItem = "";
            lsDescID = "";
            lsDesc = "";

        }
        tblViewJobDone.setItems(jobDoneData);
        jobDoneData.stream()
                .filter(jd -> jd.getTblindex04_done().startsWith("s"))
                .forEach(e -> System.out.println("Job Done Value: " + e));
    }

    private void initJobDoneTable() {
        tblindex01_done.setCellValueFactory(new PropertyValueFactory<>("tblindex01_done"));
        tblindex02_done.setCellValueFactory(new PropertyValueFactory<>("tblindex02_done"));
        tblindex03_done.setCellValueFactory(new PropertyValueFactory<>("tblindex04_done"));
        tblindex04_done.setCellValueFactory(new PropertyValueFactory<>("tblindex05_done"));
        tblindex05_done.setCellValueFactory(new PropertyValueFactory<>("tblindex07_done"));
        tblindex06_done.setCellValueFactory(new PropertyValueFactory<>("tblindex06_done"));
        tblindex07_done.setCellValueFactory(new PropertyValueFactory<>("tblindex08_done"));
        tblViewJobDone.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewJobDone.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void initVehicleComponentsTable() {
        tblIndex01_VComp.setCellValueFactory(new PropertyValueFactory<>("tblindex01_vc"));
        tblIndex02_VComp.setCellValueFactory(new PropertyValueFactory<>("tblindex03_vc"));
        tblViewJobDone.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewJobDone.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    public void loadVGPPrint() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/general/VGPPrint.fxml"));
            VGPPrintController loControl = new VGPPrintController();
            loControl.setGRider(oApp);
            loControl.setObject(oTrans);
            loControl.setTransNo(oTrans.getMasterModel().getMasterModel().getTransNo());
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();
            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();

        } catch (IOException e) {
            ShowMessageFX.Warning(e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
}
