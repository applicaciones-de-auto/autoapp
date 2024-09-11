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
import javafx.scene.input.KeyCode;
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
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VehicleDeliveryReceiptController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleDeliveryReceipt oTransVDR;
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
        oTransVDR = new VehicleDeliveryReceipt(oApp, false, oApp.getBranchCode());
        clearFields();
        initCapitalizationFields();
        initFieldActions();
        initButtonsClick();
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        datePicker03.setDayCellFactory(targetDate);
        textArea13.focusedProperty().addListener(txtArea_Focus);
        comboBox02.setItems(cFormItems);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }
    private Callback<DatePicker, DateCell> targetDate = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate foItem, boolean fbEmpty) {
            super.updateItem(foItem, fbEmpty);
            LocalDate loToday = LocalDate.now();
            setDisable(fbEmpty || foItem.isBefore(loToday));
        }
    };

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField04, txtField05, txtField07, txtField08, txtField09, txtField10, txtField11);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea06, textArea12, textArea13, textArea14);

        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    private void initFieldActions() {
        comboBox02.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (comboBox02.getSelectionModel().getSelectedIndex() >= 0) {
                    oTransVDR.getMasterModel().getMasterModel().setCustType(String.valueOf(comboBox02.getSelectionModel().getSelectedIndex()));
                    if (comboBox02.getSelectionModel().getSelectedIndex() == 1) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Supplier is Under Development, please choose customer to proceed.");
                        return;
                    }
                    initFields(pnEditMode);
                }
            }
        });
        datePicker03.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTransVDR.getMasterModel().getMasterModel().setDelvryDte(SQLUtil.toDate(datePicker03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTransVDR.getMasterModel().getMasterModel().setVSPNO("");
                    }
                }
            }
        });
    }

    private void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnSave, btnCancel, btnBrowse, btnClose, btnEdit, btnPrint, btnCancelVDR);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTransVDR = new VehicleDeliveryReceipt(oApp, false, oApp.getBranchCode());
                loJSON = oTransVDR.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVDRFields();
                    pnEditMode = oTransVDR.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransVDR.updateTransaction();
                pnEditMode = oTransVDR.getEditMode();
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
                    loJSON = oTransVDR.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "Vehicle Delivery Receipt Information", (String) loJSON.get("message"));
                        loJSON = oTransVDR.openTransaction(oTransVDR.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadVDRFields();
                            pnEditMode = oTransVDR.getEditMode();
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
                    oTransVDR = new VehicleDeliveryReceipt(oApp, false, oApp.getBranchCode());
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
                loJSON = oTransVDR.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadVDRFields();
                    pnEditMode = oTransVDR.getEditMode();
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
                    loJSON = oTransVDR.cancelTransaction(oTransVDR.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "VDR Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "VDR Information", (String) loJSON.get("message"));
                    }
                    loJSON = oTransVDR.openTransaction(oTransVDR.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        loadVDRFields();
                        pnEditMode = oTransVDR.getEditMode();
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
                    oTransVDR.getMasterModel().getMasterModel().setRemarks(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

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
                case "txtField04":
                    loJSON = oTransVDR.searchVSP(lsValue.trim(), false);
                    if (!"error".equals(loJSON.get("result"))) {
                        loadVDRFields();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        return;
                    }
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

    private void loadVDRPrint() {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VDRPrint.fxml"));
            VDRPrintController loControl = new VDRPrintController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVDR);
            loControl.setTransNox(oTransVDR.getMasterModel().getMasterModel().getTransNo());
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

    private void loadVDRFields() {
        txtField01.setText(oTransVDR.getMasterModel().getMasterModel().getReferNo());
        if (oTransVDR.getMasterModel().getMasterModel().getClientTp() != null) {
            comboBox02.getSelectionModel().select(Integer.parseInt(oTransVDR.getMasterModel().getMasterModel().getCustType()));
        }
        if (oTransVDR.getMasterModel().getMasterModel().getDelvryDte() != null) {
            datePicker03.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTransVDR.getMasterModel().getMasterModel().getTransactDte())));
        }
        txtField04.setText(oTransVDR.getMasterModel().getMasterModel().getVSPNO());
        txtField05.setText(oTransVDR.getMasterModel().getMasterModel().getBuyCltNm());
        textArea06.setText(oTransVDR.getMasterModel().getMasterModel().getAddress());
        txtField07.setText(oTransVDR.getMasterModel().getMasterModel().getCoCltNm());
        if (oTransVDR.getMasterModel().getMasterModel().getIsVhclNw() != null) {
            if (oTransVDR.getMasterModel().getMasterModel().getIsVhclNw().equals("0")) {
                radioBrandNew.setSelected(true);
            } else {
                radioPreOwned.setSelected(true);
            }
        }
        txtField08.setText(oTransVDR.getMasterModel().getMasterModel().getCSNo());
        txtField09.setText(oTransVDR.getMasterModel().getMasterModel().getPlateNo());
        txtField10.setText(oTransVDR.getMasterModel().getMasterModel().getEngineNo());
        txtField11.setText(oTransVDR.getMasterModel().getMasterModel().getFrameNo());
        textArea12.setText(oTransVDR.getMasterModel().getMasterModel().getVhclFDsc());
        textArea13.setText(oTransVDR.getMasterModel().getMasterModel().getRemarks());
        textArea14.setText("");
        lblVSINo.setText("");
        switch (oTransVDR.getMasterModel().getMasterModel().getTranStat()) {
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
    }

    private void clearFields() {
        txtField01.setText("");
        comboBox02.setValue(null);
        datePicker03.setValue(LocalDate.of(1999, Month.JANUARY, 1));
        txtField04.setText("");
        txtField05.setText("");
        textArea06.setText("");
        txtField07.setText("");
        radioBrandNew.setSelected(false);
        radioPreOwned.setSelected(false);
        txtField08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        txtField11.setText("");
        textArea12.setText("");
        textArea13.setText("");
        textArea14.setText("");
        lblVDRStatus.setText("");
        lblVSINo.setText("");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField04.setDisable(true);
        datePicker03.setDisable(true);
        comboBox02.setDisable(true);
        textArea13.setDisable(!lbShow);
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
        btnCancelVDR.setVisible(false);
        btnCancelVDR.setManaged(false);
        if (fnValue == EditMode.ADDNEW) {
            comboBox02.setDisable(!lbShow);
            datePicker03.setDisable(!lbShow);
            if (comboBox02.getSelectionModel().getSelectedIndex() == 0) {
                txtField04.setDisable(!lbShow);
            }
        }
        if (fnValue == EditMode.READY) {
            if (!lblVDRStatus.getText().equals("Cancelled")) {
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                btnCancelVDR.setVisible(true);
                btnCancelVDR.setManaged(true);
            }
        }
    }

}
