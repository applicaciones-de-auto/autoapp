/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.main.sales.VehicleDeliveryReceipt;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleDeliveryReceiptController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private VehicleDeliveryReceipt oTrans;
    private double xOffset = 0;
    private double yOffset = 0;
    public int pnEditMode = -1;//Modifying fields
    private final String pxeModuleName = "Vehicle Delivery Receipt"; //Form Title
    ObservableList<String> cFormItems = FXCollections.observableArrayList("CUSTOMER", "SUPPLIER");
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btnAdd, btnSave, btnCancel, btnBrowse, btnClose, btnEdit, btnPrint, btnCancelVDR;
    @FXML
    private TextField txtField01, txtField04, txtField05, txtField07, txtField08, txtField09, txtField10, txtField11;
    @FXML
    private TextArea textArea06, textArea12, textArea13, textArea14;
    @FXML
    private Label lblVDRStatus, lblVSINo;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private ComboBox<String> comboBox02;
    @FXML
    private RadioButton radioBrandNew, radioPreOwned;
    @FXML
    private ToggleGroup carCategory;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleDeliveryReceipt(oApp, false, oApp.getBranchCode());

        datePicker03.setDayCellFactory(targetDate);
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        clearFields();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    @Override
    public void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField04, txtField05, txtField07, txtField08, txtField09, txtField10, txtField11);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea06, textArea12, textArea13, textArea14);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getReferNo());
        if (oTrans.getMasterModel().getMasterModel().getClientTp() != null) {
            comboBox02.getSelectionModel().select(Integer.parseInt(oTrans.getMasterModel().getMasterModel().getCustType()));
        }
        if (oTrans.getMasterModel().getMasterModel().getDelvryDte() != null) {
            datePicker03.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTransactDte())));
        }
        txtField04.setText(oTrans.getMasterModel().getMasterModel().getVSPNO());
        txtField05.setText(oTrans.getMasterModel().getMasterModel().getBuyCltNm());
        textArea06.setText(oTrans.getMasterModel().getMasterModel().getAddress());
        txtField07.setText(oTrans.getMasterModel().getMasterModel().getCoCltNm());
        if (oTrans.getMasterModel().getMasterModel().getIsVhclNw() != null) {
            if (oTrans.getMasterModel().getMasterModel().getIsVhclNw().equals("0")) {
                radioBrandNew.setSelected(true);
            } else {
                radioPreOwned.setSelected(true);
            }
        }
        txtField08.setText(oTrans.getMasterModel().getMasterModel().getCSNo());
        txtField09.setText(oTrans.getMasterModel().getMasterModel().getPlateNo());
        txtField10.setText(oTrans.getMasterModel().getMasterModel().getEngineNo());
        txtField11.setText(oTrans.getMasterModel().getMasterModel().getFrameNo());
        textArea12.setText(oTrans.getMasterModel().getMasterModel().getVhclFDsc());
        textArea13.setText(oTrans.getMasterModel().getMasterModel().getRemarks());
        textArea14.setText("");
        lblVSINo.setText(oTrans.getMasterModel().getMasterModel().getSINo());
        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lblVDRStatus.setText("Active");
                break;
            case TransactionStatus.STATE_CLOSED:
                lblVDRStatus.setText("Approved");
                break;
            case TransactionStatus.STATE_CANCELLED:
                lblVDRStatus.setText("Cancelled");
                break;
            case TransactionStatus.STATE_POSTED:
                lblVDRStatus.setText("Posted");
                break;
            default:
                lblVDRStatus.setText("");
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
        textArea13.focusedProperty().addListener(txtArea_Focus);
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
                case 13:
                    oTrans.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
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
                        case "txtField04":
                            loJSON = oTrans.searchVSP(lsValue.trim(), false);
                            if (!"error".equals(loJSON.get("result"))) {
                                loadMasterFields();
                            } else {
                                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                return;
                            }
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
        List<Button> loButtons = Arrays.asList(btnAdd, btnSave, btnCancel, btnBrowse, btnClose, btnEdit, btnPrint, btnCancelVDR);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new VehicleDeliveryReceipt(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
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
                if (ShowMessageFX.YesNo(null, "Vehicle Delivery Receipt Information Saving....", "Are you sure, do you want to save?")) {
                    if (comboBox02.getSelectionModel().getSelectedIndex() < 0) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select Customer Type.");
                        return;
                    }
                    if (comboBox02.getSelectionModel().getSelectedIndex() == 1) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Supplier is Under Development, please choose Customer to proceed.");
                        return;
                    }
                    loJSON = oTrans.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Delivery Receipt Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            pnEditMode = oTrans.getEditMode();
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
                    oTrans = new VehicleDeliveryReceipt(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Delivery Receipt Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    pnEditMode = oTrans.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Delivery Receipt Information", (String) loJSON.get("message"));
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnPrint":
                loadVDRPrint();
                break;
            case "btnCancelVDR":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this VDR?")) {
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "VDR Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "VDR Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadMasterFields();
                        pnEditMode = oTrans.getEditMode();
                        initFields(pnEditMode);
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
        comboBox02.setItems(cFormItems);
    }

    @Override
    public void initFieldsAction() {
        comboBox02.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox02.getSelectionModel().getSelectedIndex() >= 0) {
                    if (comboBox02.getSelectionModel().getSelectedIndex() == 1) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Supplier is Under Development, please choose customer to proceed.");
                        Platform.runLater(() -> {
                            comboBox02.getSelectionModel().select(0);
                        });
                    }
                    oTrans.getMasterModel().getMasterModel().setCustType(String.valueOf(comboBox02.getSelectionModel().getSelectedIndex()));
                    initFields(pnEditMode);
                }
            }
        });
        datePicker03.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setDelvryDte(SQLUtil.toDate(datePicker03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });

    }

    @Override
    public void initTextFieldsProperty() {
        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getMasterModel().getMasterModel().setVSPNO("");
                        oTrans.getMasterModel().getMasterModel().setClientID("");
                        oTrans.getMasterModel().getMasterModel().setBuyCltNm("");
                        oTrans.getMasterModel().getMasterModel().setCoCltNm("");
                        oTrans.getMasterModel().getMasterModel().setCSNo("");
                        oTrans.getMasterModel().getMasterModel().setPlateNo("");
                        oTrans.getMasterModel().getMasterModel().setEngineNo("");
                        oTrans.getMasterModel().getMasterModel().setFrameNo("");
                        oTrans.getMasterModel().getMasterModel().setVhclFDsc("");
                        oTrans.getMasterModel().getMasterModel().setIsVhclNw("");
                        clearVSPFields();
                    }
                }
            }
        });
    }

    private void clearVSPFields() {
        CustomCommonUtil.setText("", txtField05,
                txtField07, txtField08, txtField09, txtField10, txtField11);
        CustomCommonUtil.setText("", textArea06, textArea12, textArea13, textArea14);
        CustomCommonUtil.setSelected(false, radioBrandNew, radioPreOwned);
    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField04, txtField05,
                txtField07, txtField08, txtField09, txtField10, txtField11);
        CustomCommonUtil.setText("", textArea06, textArea12, textArea13, textArea14);
        comboBox02.setValue(null);
        datePicker03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
        CustomCommonUtil.setSelected(false, radioBrandNew, radioPreOwned);
        CustomCommonUtil.setText("", lblVDRStatus, lblVSINo);
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        CustomCommonUtil.setDisable(true, txtField04, datePicker03, comboBox02);
        textArea13.setDisable(!lbShow);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnCancelVDR);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnCancelVDR);
        if (fnValue == EditMode.ADDNEW) {
            CustomCommonUtil.setDisable(!lbShow, comboBox02, datePicker03);
            if (comboBox02.getSelectionModel().getSelectedIndex() == 0) {
                txtField04.setDisable(!lbShow);
            }
        }
        if (fnValue == EditMode.READY) {
            if (!oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CANCELLED)) {
                CustomCommonUtil.setVisible(true, btnEdit, btnCancelVDR);
                CustomCommonUtil.setManaged(true, btnEdit, btnCancelVDR);
            }
            if (oTrans.getMasterModel().getMasterModel().getTranStat().equals(TransactionStatus.STATE_CLOSED)) {
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
            }
        }
    }

    private void loadVDRPrint() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VDRPrint.fxml"));
            VDRPrintController loControl = new VDRPrintController();
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
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }
    }

    private Callback<DatePicker, DateCell> targetDate = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate loToday = LocalDate.now();
            setDisable(fbEmpty || foItem.isBefore(loToday));
        }
    };
}
