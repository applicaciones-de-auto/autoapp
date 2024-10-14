package org.guanzon.autoapp.controllers.cashiering;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import org.guanzon.auto.main.cashiering.VehicleSalesInvoice;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VehicleSalesInvoiceController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private VehicleSalesInvoice oTrans;
    private int pnEditMode = -1;
    private String pxeModuleName = "Vehicle Sales Invoice";
    ObservableList<String> cCustomerType = FXCollections.observableArrayList("CUSTOMER", "SUPPLIER"); // Customer Type Values
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btnClose, btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnVSICancel;
    @FXML
    private Label lbStatus;
    @FXML
    private TextField txtField01, txtField04, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11, txtField12,
            txtField13, txtField14, txtField18, txtField19, txtField20, txtField21, txtField22;
    @FXML
    private DatePicker datePicker03;
    @FXML
    private ComboBox<String> comboBox02;
    @FXML
    private TextArea textArea07, textArea15, textArea16, textArea17;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new VehicleSalesInvoice(oApp, false, oApp.getBranchCode());
        datePicker03.setDayCellFactory(callB);
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
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField04, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11, txtField12,
                txtField13, txtField14);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea15, textArea16, textArea17);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    @Override
    public boolean loadMasterFields() {
        JSONObject loJSON = new JSONObject();
        txtField01.setText(oTrans.getMasterModel().getMasterModel().getReferNo());

        if (oTrans.getMasterModel().getMasterModel().getTransactDte() != null) {
            datePicker03.setValue(CustomCommonUtil.strToDate(CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getMasterModel().getTransactDte())));
        }
        if (oTrans.getVSISourceList().size() > 0) {
            if (oTrans.getVSISourceModel().getDetailModel().getCustType() != null) {
                if (!oTrans.getVSISourceModel().getDetailModel().getCustType().isEmpty()) {
                    comboBox02.getSelectionModel().select(Integer.parseInt(oTrans.getVSISourceModel().getDetailModel().getCustType()));
                }
            }
            txtField04.setText(oTrans.getVSISourceModel().getDetailModel().getUDRNo());
            txtField05.setText(""); // NO PO GETTERS
            txtField06.setText(oTrans.getMasterModel().getMasterModel().getBuyCltNm());
            textArea07.setText(oTrans.getMasterModel().getMasterModel().getAddress());
            txtField08.setText(oTrans.getMasterModel().getMasterModel().getTaxIDNo());
            txtField09.setText(oTrans.getVSISourceModel().getDetailModel().getCoCltNm());
            txtField10.setText(oTrans.getVSISourceModel().getDetailModel().getSEName());
            txtField11.setText(oTrans.getVSISourceModel().getDetailModel().getCSNo());
            txtField12.setText(oTrans.getVSISourceModel().getDetailModel().getPlateNo());
            txtField13.setText(oTrans.getVSISourceModel().getDetailModel().getEngineNo());
            txtField14.setText(oTrans.getVSISourceModel().getDetailModel().getFrameNo());
            textArea15.setText(oTrans.getVSISourceModel().getDetailModel().getVhclFDsc());
            if (oTrans.getVSISourceModel().getDetailModel().getUnitPrce() != null) {
                txtField18.setText(
                        poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSISourceModel().getDetailModel().getUnitPrce()))));
            }
            txtField19.setText(
                    poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getVSISourceModel().getDetailModel().getVatRate()))));
        }
        textArea16.setText(
                "");
        textArea17.setText(
                "");

        txtField20.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getVatAmt()))));
        txtField21.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getDiscount()))));
        txtField22.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTrans.getMasterModel().getMasterModel().getTranTotl()))));
        switch (oTrans.getMasterModel().getMasterModel().getTranStat()) {
            case TransactionStatus.STATE_OPEN:
                lbStatus.setText("Active");
                break;
            case TransactionStatus.STATE_CLOSED:
                lbStatus.setText("Approved");
                break;
            case TransactionStatus.STATE_CANCELLED:
                lbStatus.setText("Cancelled");
                break;
            case TransactionStatus.STATE_POSTED:
                lbStatus.setText("Posted");
                break;
            default:
                lbStatus.setText("");
                break;
        }
        return true;
    }

    @Override
    public void initPatternFields() {
        Pattern decOnly = Pattern.compile("[0-9,.]*");
        txtField20.setTextFormatter(new TextFormatterUtil(decOnly));  //Discount
    }

    @Override
    public void initLimiterFields() {
    }

    @Override
    public void initTextFieldFocus() {
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea16, textArea17);
        loTxtArea.forEach(tf -> tf.focusedProperty().addListener(txtArea_Focus));
        txtField01.focusedProperty().addListener(textField_Focus);
    }
    final ChangeListener<? super Boolean> textField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 1:
                    oTrans.getMasterModel().getMasterModel().setReferNo(lsValue);
                    break;
            }
        } else {
            loTxtField.selectAll();
        }
    };
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
                case 16:
//                    oTrans.getMasterModel().getMasterModel().set(lsValue);
                    break;
            }
        } else {
            loTextArea.selectAll();
        }
    };

    @Override
    public void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField04, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11, txtField12,
                txtField13, txtField14, txtField18, txtField19, txtField20, txtField21, txtField22);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
        /*TextArea*/
        List<TextArea> loTxtArea = Arrays.asList(textArea07, textArea15, textArea16, textArea17);
        loTxtArea.forEach(tf -> tf.setOnKeyPressed(event -> textArea_KeyPressed(event)));
    }

    @Override
    public void txtField_KeyPressed(KeyEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
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
                    case TAB:
                    case ENTER:
                    case F3:
                        switch (txtFieldID) {
                            case "txtField04":
                                String lsCustType = String.valueOf(comboBox02.getSelectionModel().getSelectedIndex());
                                loJSON = oTrans.searchVDR(lsValue, false, lsCustType);
                                if (!"error".equals(loJSON.get("result"))) {
                                    loadMasterFields();
                                } else {
                                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                                    txtField04.setText("");
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
    }

    @Override
    public void textArea_KeyPressed(KeyEvent event) {
        String textAreaID = ((TextArea) event.getSource()).getId();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (textAreaID) {
            }
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    @Override
    public void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnClose, btnAdd, btnEdit, btnCancel, btnSave, btnBrowse, btnPrint, btnVSICancel);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnAdd":
                clearFields();
                oTrans = new VehicleSalesInvoice(oApp, false, oApp.getBranchCode());
                loJSON = oTrans.newTransaction();
                if ("success".equals((String) loJSON.get("result"))) {
                    oTrans.getMasterModel().getMasterModel().setDocType("0");
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
                    ShowMessageFX.Warning((String) loJSON.get("message"), "Warning", null);
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to save?")) {
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
                        ShowMessageFX.Information(null, "VSI Information", (String) loJSON.get("message"));
                        loJSON = oTrans.openTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
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
                    oTrans = new VehicleSalesInvoice(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if ((pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE)) {
                    if (ShowMessageFX.YesNo(null, "Search VSI Information Confirmation", "You have unsaved data. Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTrans.searchTransaction("");
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    initFields(pnEditMode);
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, "Search VSI Information Confirmation", (String) loJSON.get("message"));
                }
                break;

            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            case "btnPrint":
                try {
                loadVSIPrint();
            } catch (SQLException ex) {
                Logger.getLogger(VehicleSalesInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
            case "btnVSICancel":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure, do you want to cancel this VSI?")) {
                    loJSON = oTrans.cancelTransaction(oTrans.getMasterModel().getMasterModel().getTransNo());
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "VSI Information", (String) loJSON.get("message"));
                    } else {
                        ShowMessageFX.Warning(null, "VSI Information", (String) loJSON.get("message"));
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
        comboBox02.setItems(cCustomerType);
    }

    @Override
    public void initFieldsAction() {
        datePicker03.setOnAction(e -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                oTrans.getMasterModel().getMasterModel().setTransactDte(SQLUtil.toDate(datePicker03.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
            }
        });
        txtField04.textProperty().addListener((observable, oldValue, newValue) -> {
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                if (newValue != null) {
                    if (newValue.isEmpty()) {
                        oTrans.getVSISourceModel().getDetailModel().setUDRNo("");
                        clearFieldsNoVSINo();
                    }
                }
                initFields(pnEditMode);
            }
        });
        comboBox02.setOnAction(event -> {
            if (pnEditMode == EditMode.ADDNEW) {
                if (comboBox02.getSelectionModel().getSelectedIndex() >= 0) {
                    oTrans.getMasterModel().getMasterModel().setClientTp(String.valueOf(comboBox02.getSelectionModel().getSelectedIndex()));
                }
                initFields(pnEditMode);
            }
        });
    }

    @Override
    public void initTextFieldsProperty() {

    }

    @Override
    public void clearTables() {

    }

    private void clearFieldsNoVSINo() {
        CustomCommonUtil.setText("", txtField05, txtField06, txtField08, txtField09, txtField10, txtField11, txtField12,
                txtField13, txtField14, txtField18, txtField19, txtField20, txtField21, txtField22);
        CustomCommonUtil.setText("0.00", txtField18, txtField19, txtField20, txtField21, txtField22);
        CustomCommonUtil.setText("", textArea07, textArea15, textArea16, textArea17);
        txtField19.setText("0");
    }

    @Override
    public void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField04, txtField05, txtField06, txtField08, txtField09, txtField10, txtField11, txtField12,
                txtField13, txtField14, txtField18, txtField19, txtField20, txtField21, txtField22);
        CustomCommonUtil.setText("0.00", txtField18, txtField19, txtField20, txtField21, txtField22);
        CustomCommonUtil.setText("", textArea07, textArea15, textArea16, textArea17);
        txtField19.setText("0");
        comboBox02.setValue("");
        datePicker03.setValue(LocalDate.of(1900, Month.JANUARY, 1));
    }

    @Override
    public void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        CustomCommonUtil.setDisable(!lbShow, txtField01, comboBox02, datePicker03);
        txtField04.setDisable(!(lbShow && !comboBox02.getValue().isEmpty()));

        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        CustomCommonUtil.setVisible(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setManaged(lbShow, btnSave, btnCancel);
        CustomCommonUtil.setVisible(false, btnEdit, btnPrint, btnVSICancel);
        CustomCommonUtil.setManaged(false, btnEdit, btnPrint, btnVSICancel);
        if (fnValue == EditMode.READY) {
            if (!lbStatus.getText().equals("Cancelled")) {
                CustomCommonUtil.setVisible(true, btnEdit, btnPrint, btnVSICancel);
                CustomCommonUtil.setManaged(true, btnEdit, btnPrint, btnVSICancel);
            }
        }
        if (fnValue == EditMode.UPDATE) {
            CustomCommonUtil.setDisable(true, txtField01, comboBox02, txtField04);
        }
    }

    private void loadVSIPrint() throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/cashiering/VSiPrint.fxml"));
            VSIPrintController loControl = new VSIPrintController();
            loControl.setGRider(oApp);
            loControl.setVSIObject(oTrans);
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
    private Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || item.compareTo(datePicker03.getValue()) < 0);

                }

            };
        }

    };
}
