/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.clients.Vehicle_Gatepass;
import org.guanzon.autoapp.models.general.JobDone;
import org.guanzon.autoapp.models.general.VehicleComponents;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VehicleGatePassController implements Initializable, ScreenInterface {

    private GRider oApp;
    private Vehicle_Gatepass oTransVGP;
    private String pxeModuleName = "Vehicle Gate Pass";
    private boolean pbIsVSP = true;
    private String psVSPTransNo = "";
    private boolean pbOpenEvent = false;
    private boolean pbIsClicked = false;
    private int pnEditMode = -1;
    private ObservableList<JobDone> jobDoneData = FXCollections.observableArrayList();
    private ObservableList<VehicleComponents> compoData = FXCollections.observableArrayList();
    ObservableList<String> cVhclSrc = FXCollections.observableArrayList("VEHILE SALES");
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
    private Button btnAdd, btnBrowse, btnSave, btnEdit, btnCancel, btnPrint, btnClose;

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

    public void setOpenEvent(boolean fbValue) {
        pbOpenEvent = fbValue;
    }

    public void setIsClicked(boolean fbValue) {
        pbIsClicked = fbValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransVGP = new Vehicle_Gatepass(oApp, false, oApp.getBranchCode());
        initCapitalizationFields();
        initJobDoneTable();
        initVehicleComponentsTable();
        clearFields();
        clearTables();
        initFieldAction();
        initButtonsClick();
        comboBox03.setItems(cVhclSrc);
        comboBox04.setItems(cPurpose);
        initFields(pnEditMode);
        Platform.runLater(() -> {
            if (pbIsVSP) {
                btnAdd.fire();
                JSONObject loJSON = new JSONObject();
                loJSON = oTransVGP.searchVSP(psVSPTransNo, true);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVGPFields();
                    loadJobDoneTable();
                    pnEditMode = oTransVGP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    clearFields();
                    clearTables();
                }
            }
        });
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea12);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    private void initFieldAction() {
        comboBox03.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVGP.getMasterModel().getMasterModel().setSourceGr(String.valueOf(comboBox03.getValue()));
            }
        }
        );
        datePicker02.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVGP.getMasterModel().getMasterModel().setTransactDte(SQLUtil.toDate(datePicker02.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
    }

    private void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnBrowse, btnEdit, btnCancel, btnSave, btnPrint, btnClose);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                clearTables();
                oTransVGP = new Vehicle_Gatepass(oApp, false, oApp.getBranchCode());
                loJSON = oTransVGP.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVGPFields();
                    loadJobDoneTable();
                    pnEditMode = oTransVGP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransVGP.updateTransaction();
                pnEditMode = oTransVGP.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "Vehicle Gate Pass Information Saving....", "Are you sure, do you want to save?")) {
                    loJSON = oTransVGP.saveTransaction();
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
                    oTransVGP = new Vehicle_Gatepass(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Gate Pass Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                        pnEditMode = EditMode.UNKNOWN;
                    } else {
                        pnEditMode = oTransVGP.getEditMode();
                        return;
                    }
                }
                loJSON = oTransVGP.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVGPFields();
                    pnEditMode = oTransVGP.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Gate Pass Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form")) {
                    CommonUtils.closeStage(btnClose);
                } else {
                    return;
                }
                break;
            case "btnPrint":
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                break;
        }
        initFields(pnEditMode);
    }

    private void loadVGPFields() {
        if (oTransVGP.getMasterModel().getMasterModel().getTransNo() != null) {
            txtField01.setText(oTransVGP.getMasterModel().getMasterModel().getTransNo());
        }
        if (oTransVGP.getMasterModel().getMasterModel().getTransactDte() != null && !String.valueOf(oTransVGP.getMasterModel().getMasterModel().getTransactDte()).isEmpty()) {
            datePicker02.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransVGP.getMasterModel().getMasterModel().getTransactDte())));
        }
        int lsVhclSource = -1;
        if (oTransVGP.getMasterModel().getMasterModel().getSourceGr() != null && !String.valueOf(oTransVGP.getMasterModel().getMasterModel().getSourceGr()).isEmpty()) {
            switch (oTransVGP.getMasterModel().getMasterModel().getSourceGr()) {
                case "VEHICLE SALES":
                    lsVhclSource = 0;
                    break;
            }
            comboBox03.getSelectionModel().select(lsVhclSource);
        }
        comboBox04.setValue(null);
        txtField05.setText(oTransVGP.getVSPModel().getMasterModel().getUDRNo());
        txtField06.setText(oTransVGP.getVSPModel().getMasterModel().getBuyCltNm());
        textArea07.setText(oTransVGP.getVSPModel().getMasterModel().getAddress());
        txtField08.setText(oTransVGP.getVSPModel().getMasterModel().getCSNo());
        txtField09.setText(oTransVGP.getVSPModel().getMasterModel().getPlateNo());
        txtField10.setText(oTransVGP.getVSPModel().getMasterModel().getFrameNo());
        txtField11.setText(oTransVGP.getVSPModel().getMasterModel().getEngineNo());
        textArea12.setText(oTransVGP.getVSPModel().getMasterModel().getVhclFDsc());
    }

    private void clearFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11);
        loTxtField.forEach(tf -> tf.setText(""));
        datePicker02.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        textArea07.setText("");
        textArea12.setText("");
        comboBox03.setValue(null);
        comboBox04.setValue(null);

    }

    private void clearTables() {
        jobDoneData.clear();
        compoData.clear();
    }

//    private void loadJobDoneTable() {
//        jobDoneData.clear();
//        for (int lnLaborRow = 0; lnLaborRow <= oTransVGP.getVSPLaborList().size() - 1; lnLaborRow++) {
//
//        }
//        for (int lnPartsRow = 0; lnPartsRow <= oTransVGP.getVSPPartsList().size() - 1; lnPartsRow++) {
//
//        }
//        tblViewJobDone.setItems(jobDoneData);
//    }
    private void loadJobDoneTable() {
        jobDoneData.clear();

        // First, add labor data to the jobDoneData list
        String lsLaborJONo = "";
        int lnRow = 1;
        for (int lnLaborRow = 0; lnLaborRow <= oTransVGP.getVSPLaborList().size() - 1; lnLaborRow++) {
            if (oTransVGP.getVSPLaborModel().getDetailModel(lnLaborRow).getDSNo() != null) {
                lsLaborJONo = oTransVGP.getVSPLaborModel().getDetailModel(lnLaborRow).getDSNo();
            }
            jobDoneData.add(new JobDone(String.valueOf(lnRow),
                    "LABOR",
                    String.valueOf(oTransVGP.getVSPLaborModel().getDetailModel(lnLaborRow).getLaborCde()),
                    String.valueOf(oTransVGP.getVSPLaborModel().getDetailModel(lnLaborRow).getLaborDsc()),
                    "1",
                    lsLaborJONo,
                    "",
                    String.valueOf(oTransVGP.getVSPLaborModel().getDetailModel(lnLaborRow).getRemarks()))
            );
            lnRow++;
            lsLaborJONo = "";
        }
        String lsPartsJONo = "";
        for (int lnPartsRow = 0; lnPartsRow <= oTransVGP.getVSPPartsList().size() - 1; lnPartsRow++) {
            if (oTransVGP.getVSPPartsModel().getDetailModel(lnPartsRow).getDSNo() != null) {
                lsPartsJONo = oTransVGP.getVSPPartsModel().getDetailModel(lnPartsRow).getDSNo();
            }
            jobDoneData.add(new JobDone(String.valueOf(lnRow),
                    "PARTS",
                    String.valueOf(oTransVGP.getVSPPartsModel().getDetailModel(lnPartsRow).getBarCode()),
                    String.valueOf(oTransVGP.getVSPPartsModel().getDetailModel(lnPartsRow).getPartDesc()),
                    String.valueOf(oTransVGP.getVSPPartsModel().getDetailModel(lnPartsRow).getQuantity()),
                    lsPartsJONo,
                    "",
                    "")
            );
            lsPartsJONo = "";
            lnRow++;
        }

        tblViewJobDone.setItems(jobDoneData);
    }

    private void initJobDoneTable() {
        tblindex01_done.setCellValueFactory(new PropertyValueFactory<>("tblindex01_done"));
        tblindex02_done.setCellValueFactory(new PropertyValueFactory<>("tblindex02_done"));
        tblindex03_done.setCellValueFactory(new PropertyValueFactory<>("tblindex04_done"));
        tblindex04_done.setCellValueFactory(new PropertyValueFactory<>("tblindex05_done"));
        tblindex05_done.setCellValueFactory(new PropertyValueFactory<>(""));
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

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        if (pbOpenEvent) {
            btnSave.setManaged(lbShow);
            btnSave.setVisible(lbShow);
            btnCancel.setManaged(false);
            btnCancel.setVisible(false);
            btnPrint.setManaged(false);
            btnPrint.setVisible(false);
            btnEdit.setManaged(false);
            btnEdit.setVisible(false);
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnBrowse.setVisible(false);
            btnBrowse.setManaged(false);
            comboBox03.setDisable(true);
            comboBox04.setDisable(true);
            txtField05.setDisable(true);
        } else {
            btnAdd.setVisible(!lbShow);
            btnAdd.setManaged(!lbShow);
            btnCancel.setVisible(lbShow);
            btnCancel.setManaged(lbShow);
            btnSave.setVisible(lbShow);
            btnSave.setManaged(lbShow);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
            btnPrint.setManaged(false);
            btnPrint.setVisible(false);
            if (fnValue == EditMode.READY) {
                btnPrint.setManaged(true);
                btnPrint.setVisible(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
            }
        }

        if (pbIsClicked) {
            btnSave.setManaged(false);
            btnSave.setVisible(false);
        }
    }
}
