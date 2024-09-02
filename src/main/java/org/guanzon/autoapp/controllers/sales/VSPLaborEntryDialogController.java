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
import javafx.scene.control.CheckBox;
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
 * @author AutoGroup Programmers
 */
public class VSPLaborEntryDialogController implements Initializable {

    private GRider oApp;
    private boolean pbLbrDsc;
    private String psLbrDsc = "";
    private int pnRow = 0;
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Labor";
    private String psJO = "";
    private String psOrigDsc = "";
    private VehicleSalesProposal oTransVSPLabor;
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");

    @FXML
    private Button btnAddLabor, btnEditLabor, btnCloseLabor;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField05, txtField06;
    @FXML
    private ComboBox<String> comboBox03;
    @FXML
    private CheckBox checkBoxIsAdd;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setObject(VehicleSalesProposal foValue) {
        oTransVSPLabor = foValue;
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

    public void setLbrDesc(String fsValue) {
        psLbrDsc = fsValue;
    }

    public void setWithLabor(boolean fbValue) {
        pbLbrDsc = fbValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCapitalizationFields();
        comboBox03.setItems(cChargeType);
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initTextPropertyAction();
        initButtonsClick();
        initFielPattern();
        if (pbState) {
            if (pbLbrDsc) {
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setAddtl("0");
            } else {
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setAddtl("1");
            }
        }

        loadLaborFields();
        initFields();
    }

    private void loadLaborFields() {
        if (!psLbrDsc.isEmpty()) {
            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDsc(psLbrDsc);
        }
        txtField01.setText(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborCde()));
        txtField02.setText(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc()));
        if (oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp() != null && !oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp().equals("")) {
            comboBox03.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp())));
        }
        txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt()))));
        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()))));
        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt()))));
        if (oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getAddtl().equals("0")) {
            checkBoxIsAdd.setSelected(false);
            txtField02.setDisable(true);
        } else {
            checkBoxIsAdd.setSelected(true);
        }
        if (oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getEntryNo() > 0) {
            txtField02.setDisable(true);
        }
    }

    private void initFielPattern() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new InputTextFormatterUtil(pattern));
        txtField05.setTextFormatter(new InputTextFormatterUtil(pattern));
    }

    private void initCapitalizationFields() {
        InputTextUtil.setCapsLockBehavior(txtField01);
        InputTextUtil.setCapsLockBehavior(txtField02);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04, txtField05, txtField06);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
                case "txtField02":
                    loJSON = oTransVSPLabor.searchLabor("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField01.setText(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborCde()));
                        txtField02.setText(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc()));
                    } else {
                        ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                        txtField02.clear();
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
                txtField01, txtField02, txtField04, txtField05, txtField06);
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
            if (lsValue.isEmpty()) {
                lsValue = "0.00";
            }

            lsValue = lsValue.replace(",", "");

            switch (lnIndex) {
                case 4:
                    if (comboBox03.getSelectionModel().getSelectedIndex() == 0) {
                        handleLaborAmountFree(lsValue);
                    } else {
                        handleLaborAmountCharge(lsValue);
                    }
                    break;
                case 5:
                    handleLaborDiscount(lsValue);
                    break;
            }
        }
    };

    private void handleLaborAmountFree(String lsValue) {
        try {
            BigDecimal laborAmt = new BigDecimal(lsValue);
            BigDecimal laborDsc = oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount();

            if (laborDsc.compareTo(laborAmt) > 0) {
                showWarning("Discount cannot be greater than the labor amount.");
                resetLaborAmount();
                resetLaborDiscount();
                return;
            }
            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborAmt(laborAmt);
            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(laborAmt);

            double lnNetPrice = calculateNetPrice(laborAmt.doubleValue(), laborAmt.doubleValue());

            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrice));

            txtField04.setText(poGetDecimalFormat.format(laborAmt.doubleValue()));
            txtField05.setText(poGetDecimalFormat.format(laborAmt.doubleValue()));
            txtField06.setText(poGetDecimalFormat.format(lnNetPrice));
        } catch (NumberFormatException e) {
            showWarning("Invalid number format for labor amount.");
            resetLaborAmount();
            resetLaborDiscount();
        }
    }

    private void handleLaborAmountCharge(String lsValue) {
        try {
            BigDecimal laborAmt = new BigDecimal(lsValue);
            BigDecimal laborDsc = oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount();

            if (laborDsc.compareTo(laborAmt) > 0) {
                showWarning("Discount cannot be greater than the labor amount.");
                resetLaborAmount();
                resetLaborDiscount();
                return;
            }
            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborAmt(laborAmt);

            double lnNetPrice = calculateNetPrice(laborAmt.doubleValue(),
                    oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount().doubleValue());

            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrice));

            txtField04.setText(poGetDecimalFormat.format(laborAmt.doubleValue()));
            txtField06.setText(poGetDecimalFormat.format(lnNetPrice));
        } catch (NumberFormatException e) {
            showWarning("Invalid number format for labor amount.");
            resetLaborAmount();
            resetLaborDiscount();
        }
    }

    private void handleLaborDiscount(String lsValue) {
        try {
            BigDecimal laborDsc = new BigDecimal(lsValue);
            BigDecimal laborAmt = oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt();

            if (laborDsc.compareTo(laborAmt) > 0) {
                showWarning("Discount cannot be greater than the labor amount.");
                resetLaborDiscount();
                return;
            }

            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(laborDsc);

            double lnNetPrice = calculateNetPrice(laborAmt.doubleValue(), laborDsc.doubleValue());

            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrice));

            txtField05.setText(poGetDecimalFormat.format(laborDsc.doubleValue()));
            txtField06.setText(poGetDecimalFormat.format(lnNetPrice));
        } catch (NumberFormatException e) {
            showWarning("Invalid number format for discount amount.");
            resetLaborDiscount();
        }
    }

    private double calculateNetPrice(double laborAmt, double laborDscount) {
        return laborAmt - laborDscount;
    }

    private void showWarning(String message) {
        ShowMessageFX.Warning(null, pxeModuleName, message);
    }

    private void resetLaborAmount() {
        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborAmt(BigDecimal.ZERO);
        txtField04.setText("0.00");
        if (comboBox03.getSelectionModel().getSelectedIndex() == 0) {
            resetLaborDiscount();
        }

        double lnNetPrice = calculateNetPrice(0.00,
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount().doubleValue());

        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrice));
        txtField06.setText(poGetDecimalFormat.format(lnNetPrice));
    }

    private void resetLaborDiscount() {
        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(BigDecimal.ZERO);
        txtField05.setText("0.00");

        double lnNetPrice = calculateNetPrice(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt().doubleValue(),
                0.00);

        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrice));
        txtField06.setText(poGetDecimalFormat.format(lnNetPrice));
    }

    private void initCmboxFieldAction() {
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setChrgeTyp(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
                switch (comboBox03.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt()))));
                        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()))));
                        double lnNetPrice = Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt())) - Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()));
                        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrice));
                        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt()))));
                        break;
                    case 1:
                        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(0.00));
                        txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()))));
                        double lnNetPrices = Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt())) - Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()));
                        oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPrices));
                        txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt()))));
                        break;
                    default:
                        txtField04.setText("0.00");
                        txtField05.setText("0.00");
                        txtField06.setText("0.00");
                        break;
                }
                initFields();
            }
        }
        );
    }

    private void initTextPropertyAction() {
        txtField02.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDsc("");
                }
            }
        });
    }

    private void initButtonsClick() {
        btnCloseLabor.setOnAction(this::handleButtonAction);
        btnAddLabor.setOnAction(this::handleButtonAction);
        btnEditLabor.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnEditLabor":
            case "btnAddLabor":
                if (isValidEntry()) {
                    CommonUtils.closeStage(btnCloseLabor);
                } else {
                    return;
                }
                break;
            case "btnCloseLabor":
                if (pbState) {
                    oTransVSPLabor.removeVSPLabor(pnRow);
                }
                CommonUtils.closeStage(btnCloseLabor);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }

    }

    private boolean isValidEntry() {
        if (txtField02.getText().trim().isEmpty()) {
            ShowMessageFX.Warning(null, "Warning", "Please input Labor Description");
            txtField02.requestFocus();
            return false;
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
            String laborAmount = txtField04.getText().replace(",", ""); // Remove commas from the input string
            try {
                double amount = Double.parseDouble(laborAmount);
                if (amount == 0.00 || amount < 0.00) {
                    ShowMessageFX.Warning(null, "Warning", "Please input Labor Amount");
                    txtField04.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                // Handle the case where laborAmount is not a valid number
                ShowMessageFX.Warning(null, "Warning", "Invalid Labor Amount");
                txtField04.requestFocus();
                return false;
            }

        }
        return true;
    }

    private void initFields() {
        switch (comboBox03.getSelectionModel().getSelectedIndex()) {
            case 0:
                txtField04.setDisable(false);
                txtField05.setDisable(true);
                break;
            case 1:
                txtField04.setDisable(false);
                txtField05.setDisable(txtField04.getText().isEmpty());
                break;
            default:
                txtField04.setDisable(true);
                txtField05.setDisable(true);
                break;
        }
        if (pbState) {
            btnAddLabor.setVisible(true);
            btnAddLabor.setManaged(true);
            btnEditLabor.setVisible(false);
            btnEditLabor.setManaged(false);
            if (pbLbrDsc) {
                txtField02.setDisable(true);
            }
        } else {
            btnAddLabor.setVisible(false);
            btnAddLabor.setManaged(false);
            btnEditLabor.setVisible(true);
            btnEditLabor.setManaged(true);
            if (!psJO.isEmpty()) {
                txtField04.setDisable(true);
                comboBox03.setDisable(true);
            }
        }
    }

}
