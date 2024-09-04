/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
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
import org.guanzon.autoapp.utils.InputTextFormatterUtil;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPAccessoriesDialogController implements Initializable {

    private GRider oApp;
    private int pnRow = 0;
    private String psStockID = "";
    private String psJO = "";
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Accessories";
    private boolean pbRequest = false;
    private String psOrigDsc = "";
    private VehicleSalesProposal oTransVSPAccessories;
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");
    @FXML
    private Button btnAdd, btnEdit, btnClose;
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField05, txtField06, txtField07;
    @FXML
    private ComboBox<String> comboBox04;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTransVSPAccessories = foValue;
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
        comboBox04.setItems(cChargeType);
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initTextPropertyAction();
        initButtonsClick();
        initFielPattern();
        loadPartsFields();
        initFields();
    }

    private void loadPartsFields() {
        txtField01.setText(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getStockID()));
        txtField02.setText(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getDescript()));
        txtField03.setText(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getQuantity()));
        if (oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp() != null && !oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp().isEmpty()) {
            comboBox04.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp())));
        }
        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice()))));
        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()))));
        txtField07.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getNtPrtAmt()))));
        if (oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getEntryNo() > 0) {
            txtField02.setDisable(true);
        }
    }

    private void initFielPattern() {
        Pattern patternInt = Pattern.compile("[0-9]*");
        txtField03.setTextFormatter(new InputTextFormatterUtil(patternInt));
        Pattern patternDec = Pattern.compile("[0-9,.]*");
        txtField05.setTextFormatter(new InputTextFormatterUtil(patternDec));
        txtField06.setTextFormatter(new InputTextFormatterUtil(patternDec));
    }

    private void initCapitalizationFields() {
        InputTextUtil.setCapsLockBehavior(txtField01);
        InputTextUtil.setCapsLockBehavior(txtField02);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField03, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField01":
                    loJSON = oTransVSPAccessories.searchParts("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField01.setText(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getDescript());
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                        txtField01.clear();
                        txtField01.requestFocus();
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

    private void initTextFieldFocus() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField03, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.focusedProperty().addListener(txtField_Focus));
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField loTxtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(loTxtField.getId().substring(8, 10));
        String lsValue = loTxtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus

            lsValue = lsValue.replace(",", "");

            switch (lnIndex) {
                case 2:
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setDescript(lsValue);
                    break;
                case 3:
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    int lnQnty = Integer.parseInt(lsValue);
                    if (lnQnty < 0) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please enter valid quantity value.");
                        return;
                    }
                    if (lnQnty <= 0) {
                        if (!txtField05.getText().equals("0.00")) {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please enter higher than zero if the accessory amount has value.");
                            return;
                        }
                    }
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setQuantity(lnQnty);
                    double lnDscAmount = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice())) - Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
                    double lnNetPrtAmt = lnDscAmount * lnQnty;
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrtAmt));
                    txtField07.setText(poGetDecimalFormat.format(lnNetPrtAmt));
                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    if (comboBox04.getSelectionModel().getSelectedIndex() == 0) {
                        handlePartsAmountFree(lsValue);
                    } else {
                        handlePartsAmountCharge(lsValue);
                    }
                    break;
                case 6:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    handlePartsDiscount(lsValue);
                    break;
            }
        }
    };

    private void handlePartsAmountFree(String lsValue) {
        try {
            BigDecimal partAmt = new BigDecimal(lsValue);
            BigDecimal partDsc = oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount();

            if (partDsc.compareTo(partAmt) > 0) {
                showWarning("Discount cannot be greater than the accessories amount.");
                resetPartsAmount();
                resetPartsDiscount();
                return;
            }
            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setSelPrice(partAmt);
            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(partAmt);

            double lnNetPrice = calculateNetPrice(partAmt.doubleValue(), partAmt.doubleValue());

            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrice));

            txtField05.setText(poGetDecimalFormat.format(partAmt.doubleValue()));
            txtField06.setText(poGetDecimalFormat.format(partAmt.doubleValue()));
            txtField07.setText(poGetDecimalFormat.format(lnNetPrice));
        } catch (NumberFormatException e) {
            showWarning("Invalid number format for accessories amount.");
            resetPartsAmount();
            resetPartsDiscount();
        }
    }

    private void handlePartsAmountCharge(String lsValue) {
        try {
            BigDecimal accessoriesAmt = new BigDecimal(lsValue);
            BigDecimal accessoriesDsc = oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount();

            if (accessoriesDsc.compareTo(accessoriesAmt) > 0) {
                showWarning("Discount cannot be greater than the accessories amount.");
                resetPartsAmount();
                resetPartsDiscount();
                return;
            }
            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setSelPrice(accessoriesAmt);

            double lnNetPrice = calculateNetPrice(accessoriesAmt.doubleValue(),
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount().doubleValue());

            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrice));

            txtField05.setText(poGetDecimalFormat.format(accessoriesAmt.doubleValue()));
            txtField07.setText(poGetDecimalFormat.format(lnNetPrice));
        } catch (NumberFormatException e) {
            showWarning("Invalid number format for accessories amount.");
            resetPartsAmount();
            resetPartsDiscount();
        }
    }

    private void handlePartsDiscount(String lsValue) {
        try {
            BigDecimal accessoriesDsc = new BigDecimal(lsValue);
            BigDecimal accessoriesAmt = oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice();

            if (accessoriesDsc.compareTo(accessoriesAmt) > 0) {
                showWarning("Discount cannot be greater than the accessories amount.");
                resetPartsDiscount();
                return;
            }

            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(accessoriesDsc);

            double lnNetPrice = calculateNetPrice(accessoriesAmt.doubleValue(), accessoriesDsc.doubleValue());

            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrice));

            txtField06.setText(poGetDecimalFormat.format(accessoriesDsc.doubleValue()));
            txtField07.setText(poGetDecimalFormat.format(lnNetPrice));
        } catch (NumberFormatException e) {
            showWarning("Invalid number format for discount amount.");
            resetPartsDiscount();
        }
    }

    private double calculateNetPrice(double partAmt, double partDscount) {
        return partAmt - partDscount;
    }

    private void showWarning(String message) {
        ShowMessageFX.Warning(null, pxeModuleName, message);
    }

    private void resetPartsAmount() {
        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setSelPrice(BigDecimal.ZERO);
        txtField05.setText("0.00");
        if (comboBox04.getSelectionModel().getSelectedIndex() == 0) {
            resetPartsDiscount();
        }

        double lnNetPrice = calculateNetPrice(0.00,
                oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount().doubleValue());

        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrice));
        txtField07.setText(poGetDecimalFormat.format(lnNetPrice));
    }

    private void resetPartsDiscount() {
        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(BigDecimal.ZERO);
        txtField06.setText("0.00");

        double lnNetPrice = calculateNetPrice(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice().doubleValue(),
                0.00);

        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrice));
        txtField07.setText(poGetDecimalFormat.format(lnNetPrice));
    }

    private void initCmboxFieldAction() {
        comboBox04.setOnAction(event -> {
            if (comboBox04.getSelectionModel().getSelectedIndex() >= 0) {
                oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setChrgeTyp(String.valueOf(comboBox04.getSelectionModel().getSelectedIndex()));
                switch (comboBox04.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice()))));
                        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()))));
                        double lnNetPrice = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice())) - Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrice));
                        txtField07.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getNtPrtAmt()))));
                        break;
                    case 1:
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(0.00));
                        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()))));
                        double lnNetPrices = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice())) - Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPrices));
                        txtField07.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getNtPrtAmt()))));
                        break;
                    default:
                        txtField05.setText("0.00");
                        txtField06.setText("0.00");
                        txtField07.setText("0.00");
                        break;
                }
                initFields();
            }
        }
        );
    }

    private void initTextPropertyAction() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setStockID("");
                }
            }
        });
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
                    CommonUtils.closeStage(btnClose);
                } else {
                    return;
                }
                break;
            case "btnClose":
                if (pbState) {
                    oTransVSPAccessories.removeVSPParts(pnRow);
                }
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private boolean isValidEntry() {
        if (txtField02.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, "Warning", "Please input Accessories Description");
            txtField02.requestFocus();
            return false;
        }
        if (!txtField05.getText().equals(0.00) || !txtField05.getText().equals("0.00")) {
            if (txtField03.getText().equals(0) || txtField03.getText().equals("0")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter quantity higher than zero.");
                return false;
            }
        }
        if (comboBox04.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (comboBox04.getSelectionModel().getSelectedIndex() == 1) {
            String accessoriesAmount = txtField05.getText().replace(",", ""); // Remove commas from the input string
            try {
                double amount = Double.parseDouble(accessoriesAmount);
                if (amount == 0.00 || amount < 0.00) {
                    ShowMessageFX.Warning(null, "Warning", "Please input accessories Amount");
                    txtField05.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                ShowMessageFX.Warning(null, "Warning", "Invalid accessories Amount");
                txtField05.requestFocus();
                return false;
            }

        }
        return true;
    }

    private void initFields() {

        if (pbState) {
            btnAdd.setVisible(true);
            btnAdd.setManaged(true);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else {
            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
            if (!psJO.isEmpty()) {
                txtField02.setDisable(true);
                txtField05.setDisable(true);
                comboBox04.setDisable(true);
                txtField06.setDisable(true);
            } else {
                if (pbRequest) {
//                    if (!oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount().toString().isEmpty()) {
//                        txtField01.setDisable(false);
//                    } else {
//                        txtField01.setDisable(true);
//                    }
                }

            }
            if (!txtField01.getText().isEmpty()) {
                txtField02.setDisable(true);
            }
        }
        if (pbRequest) {
            txtField01.setDisable(false);
            txtField02.setDisable(true);
            txtField03.setDisable(true);
            comboBox04.setDisable(true);
            txtField05.setDisable(true);
            txtField06.setDisable(true);
        } else {
            txtField01.setDisable(true);
            txtField02.setDisable(false);
            txtField03.setDisable(false);
            comboBox04.setDisable(false);
            txtField05.setDisable(false);
            txtField06.setDisable(false);

        }
        switch (comboBox04.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField05.setDisable(false);
                txtField06.setDisable(true);
                break;
            case 1:
                txtField05.setDisable(false);
                txtField06.setDisable(txtField05.getText().isEmpty());
                break;
            default:
                txtField05.setDisable(true);
                txtField06.setDisable(true);
                break;
        }
    }

}
