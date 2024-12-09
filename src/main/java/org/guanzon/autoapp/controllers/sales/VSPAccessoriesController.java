/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VSPAccessoriesController implements Initializable {

    private GRider oApp;
    private int pnRow = 0;
    private String psStockID = "";
    private String psJO = "";
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Accessories";
    private boolean pbRequest = false;
    private String psOrigDsc = "";
    private VehicleSalesProposal oTrans;
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06, txtField07, txtField08;
    @FXML
    private ComboBox<String> comboBox03;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnRow) {
        pnRow = fnRow;
    }

    public void setOrigDsc(String fsValue) {
        psOrigDsc = fsValue;
    }

    public void setJO(String fsValue) {
        psJO = fsValue;
    }

    public void setStockID(String fsValue) {
        psStockID = fsValue;
    }

    public void setRequest(boolean fsValue) {
        pbRequest = fsValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCapitalizationFields();
        initPatternFields();
        initTextFieldFocus();
        initTextKeyPressed();
        initButtonsClick();
        initComboBoxItems();
        initFieldsAction();
        initTextFieldsProperty();
        loadMasterFields();
        initFields();
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01);
        CustomCommonUtil.setCapsLockBehavior(txtField02);
    }

    private void loadMasterFields() {
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getBarCode() != null) {
            txtField01.setText(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(pnRow).getBarCode()));
        }
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getDescript() != null) {
            txtField02.setText(oTrans.getVSPPartsModel().getVSPParts(pnRow).getDescript());
        }
        int lnCrgType = -1;
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp() != null) {
            switch (oTrans.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp()) {
                case "0":
                    lnCrgType = 0;
                    break;
                case "1":
                    lnCrgType = 1;
                    break;
            }
            comboBox03.getSelectionModel().select(lnCrgType);
        }
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getSelPrice() != null) {
            txtField04.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPPartsModel().getVSPParts(pnRow).getSelPrice()));
        }
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getQuantity() != null) {
            txtField05.setText(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(pnRow).getQuantity()));
        }
        double lnAccesAmnt = Double.parseDouble(String.valueOf(oTrans.getVSPPartsModel().getVSPParts(pnRow).getSelPrice()));
        int lnAccQuan = oTrans.getVSPPartsModel().getVSPParts(pnRow).getQuantity();
        double lnTotalAmnt = lnAccesAmnt * lnAccQuan;
        txtField06.setText(CustomCommonUtil.setDecimalFormat(lnTotalAmnt));
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount() != null) {
            txtField07.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
        }
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getNtPrtAmt() != null) {
            txtField08.setText(CustomCommonUtil.setDecimalFormat(oTrans.getVSPPartsModel().getVSPParts(pnRow).getNtPrtAmt()));
        }
        if (oTrans.getVSPPartsModel().getVSPParts(pnRow).getEntryNo() > 0) {
            txtField02.setDisable(true);
        }
    }

    private void initPatternFields() {
        Pattern patternInt = Pattern.compile("[0-9]*");
        txtField05.setTextFormatter(new TextFormatterUtil(patternInt));
        Pattern patternDec = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(patternDec));
        txtField07.setTextFormatter(new TextFormatterUtil(patternDec));
    }

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05, txtField07);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();
        double lnPrtsAmt = Double.parseDouble(txtField04.getText().replace(",", ""));
        double lnPrtsDsc = Double.parseDouble(txtField07.getText().replace(",", ""));
        int lnPrtQuan = Integer.parseInt(txtField05.getText());
        double lnTtlAmnt = lnPrtsAmt * lnPrtQuan;
        JSONObject loJSON = new JSONObject();
        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    txtField04.setText(CustomCommonUtil.setDecimalFormat(lsValue.replace(",", "")));
                    break;
                case 5:
                    String lsOrigQuan = "0";
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    if (!pbRequest) {
                        lsOrigQuan = String.valueOf(oTrans.getVSPPartsModel().getVSPParts(pnRow).getQuantity());
                    }
                    loJSON = oTrans.checkVSPJOParts(oTrans.getVSPPartsModel().getVSPParts(pnRow).getStockID(), Integer.parseInt(lsValue), pnRow);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField05.setText(lsValue);
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        txtField05.setText(lsOrigQuan);
                    }
                    break;
                case 7:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    String lsOrigDisc = "0.00";
                    if (!pbRequest) {
                        lsOrigDisc = String.valueOf(oTrans.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount());
                    }
                    if (lnPrtsDsc > lnTtlAmnt) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Discount cannot be greater than Parts Total Amount.");
                        txtField07.setText(CustomCommonUtil.setDecimalFormat(lsOrigDisc));
                        if (pbState) {
                            lsValue = "0.00";
                        }
                    } else {
                        txtField07.setText(CustomCommonUtil.setDecimalFormat(lsValue.replace(",", "")));
                    }
                    break;
            }
            computeTotalPartsFromType();
        }
    };

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05, txtField07);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField01":
                    loJSON = oTrans.searchParts("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField01.setText(oTrans.getVSPPartsModel().getVSPParts(pnRow).getBarCode());
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                        txtField01.clear();
                        return;
                    }
                    break;
            }
            initFields();
            event.consume();
            CommonUtils.SetNextFocus((TextField) event.getSource());
        } else if (event.getCode()
                == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextField) event.getSource());
        }
    }

    private void initButtonsClick() {
        btnClose.setOnAction(this::handleButtonAction);
        btnAdd.setOnAction(this::handleButtonAction);
        btnEdit.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEdit":
            case "btnAdd":
                if (isValidEntry()) {
                    if (setToClass()) {
                        if (lsButton.equals("btnAdd")) {
                            CommonUtils.closeStage(btnAdd);
                        } else {
                            CommonUtils.closeStage(btnEdit);
                        }
                    }
                }
                break;
            case "btnClose":
                JSONObject loJSON = new JSONObject();
                if (pbState) {
                    oTrans.removeVSPLabor(pnRow);
                } else {
                    loJSON = oTrans.searchParts(psOrigDsc, pnRow, false);
                    if (!"error".equals((String) loJSON.get("result"))) {
                    }
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private boolean setToClass() {
        oTrans.getVSPPartsModel().getDetailModel(pnRow).setDescript(txtField02.getText());
        oTrans.getVSPPartsModel().getDetailModel(pnRow).setChrgeTyp(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
        oTrans.getVSPPartsModel().getDetailModel(pnRow).setSelPrice(new BigDecimal(txtField04.getText().replace(",", "")));
        oTrans.getVSPPartsModel().getDetailModel(pnRow).setQuantity(Integer.valueOf(txtField05.getText()));
        oTrans.getVSPPartsModel().getDetailModel(pnRow).setPartsDscount(new BigDecimal(txtField07.getText().replace(",", "")));
        oTrans.getVSPPartsModel().getDetailModel(pnRow).setNtPrtAmt(new BigDecimal(txtField08.getText().replace(",", "")));
        return true;
    }

    private void computeTotalPartsFromType() {
        double lnPartsAmnt = Double.parseDouble(txtField04.getText().replace(",", ""));
        double lnPartsDscx = Double.parseDouble(txtField07.getText().replace(",", ""));
        int lnQuantDscx = Integer.parseInt(txtField05.getText().replace(",", ""));
        double lnTtlAmntxx = lnPartsAmnt * lnQuantDscx;
        double lnNetPrceFr = lnTtlAmntxx - lnTtlAmntxx;
        double lnNetPrceCh = lnTtlAmntxx - lnPartsDscx;
        txtField06.setText(CustomCommonUtil.setDecimalFormat(lnTtlAmntxx));
        switch (comboBox03.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField07.setText(CustomCommonUtil.setDecimalFormat(lnTtlAmntxx));
                txtField08.setText(CustomCommonUtil.setDecimalFormat(lnNetPrceFr));
                break;
            case 1:
                txtField08.setText(CustomCommonUtil.setDecimalFormat(lnNetPrceCh));
                break;
            default:
                CustomCommonUtil.setText("0.00", txtField04, txtField06, txtField07, txtField08);
        }
    }

    private void initComboBoxItems() {
        comboBox03.setItems(cChargeType);
    }

    private void initFieldsAction() {
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
                    txtField07.setText("0.00");
                }
                computeTotalPartsFromType();
                initFields();
            }
        }
        );
    }

    private void initTextFieldsProperty() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTrans.getVSPPartsModel().getVSPParts(pnRow).setStockID("");
                    oTrans.getVSPPartsModel().getVSPParts(pnRow).setBarCode("");
                }
            }
        });
    }

    private void initFields() {
        CustomCommonUtil.setDisable(true, txtField01, txtField02, comboBox03, txtField04, txtField05, txtField07);
        CustomCommonUtil.setVisible(false, btnAdd, btnEdit);
        CustomCommonUtil.setManaged(false, btnAdd, btnEdit);
        if (pbState) {
            CustomCommonUtil.setDisable(false, txtField02, comboBox03);
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
        } else {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }

        if (pbRequest) {
            if (psJO.isEmpty()) {
                txtField01.setDisable(false);
                txtField05.setDisable(false);
            }
        } else {
            txtField05.setDisable(txtField04.getText().equals("0.00"));
            if (psJO.isEmpty()) {
                switch (comboBox03.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        txtField04.setDisable(false);
                        txtField05.setDisable(txtField04.getText().equals("0.00"));
                        break;
                    case 1:
                        txtField04.setDisable(false);
                        txtField05.setDisable(txtField04.getText().equals("0.00"));
                        txtField07.setDisable(txtField04.getText().equals("0.00"));
                        break;
                }
            }
        }
    }

    private boolean isValidEntry() {
        if (txtField02.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, "Warning", "Please input Accessories Description");
            txtField02.requestFocus();
            return false;
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (!txtField04.getText().equals("0.00")) {
            if (txtField05.getText().equals(0) || txtField05.getText().equals("0")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter quantity higher than zero.");
                return false;
            }
        }
        if (!txtField05.getText().equals("0")) {
            if (txtField04.getText().equals("0.00")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter amount.");
                return false;
            }
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
            if (txtField08.getText().equals("0.00")) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid Charge Type.");
                return false;
            }
            if (Double.parseDouble(txtField08.getText().replace(",", "")) < 0.00) {
                ShowMessageFX.Warning(null, "Warning", "Please enter valid net price amount.");
                return false;
            }
        }
        return true;
    }

}
