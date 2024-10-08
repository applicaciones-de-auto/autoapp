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
import javafx.scene.Node;
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
 * @author AutoGroup Programmers
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
    private VehicleSalesProposal oTransVSPAccessories;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
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
        comboBox03.setItems(cChargeType);
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initTextPropertyAction();
        initButtonsClick();
        initFielPattern();
        loadPartsFields();
        initFields();
    }

    private void initFielPattern() {
        Pattern patternInt = Pattern.compile("[0-9]*");
        txtField05.setTextFormatter(new TextFormatterUtil(patternInt));
        Pattern patternDec = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(patternDec));
        txtField07.setTextFormatter(new TextFormatterUtil(patternDec));
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01);
        CustomCommonUtil.setCapsLockBehavior(txtField02);
    }

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
                    loJSON = oTransVSPAccessories.searchParts("", pnRow, true);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        txtField01.setText(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getBarCode());
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
        JSONObject loJSON = new JSONObject();
        if (lsValue == null) {
            return;
        }

        if (!nv) { // Lost Focus
            switch (lnIndex) {
                case 2:
                    if (lsValue.isEmpty()) {
                        lsValue = "";
                    }
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setDescript(lsValue);
                    break;
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setSelPrice(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 5:
                    String lsOrigQuan = "0";
                    if (lsValue.isEmpty()) {
                        lsValue = "0";
                    }
                    if (!pbRequest) {
                        lsOrigQuan = String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getQuantity());
                    }
                    loJSON = oTransVSPAccessories.checkVSPJOParts(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getStockID(), Integer.parseInt(lsValue), pnRow);
                    if (!"error".equals((String) loJSON.get("result"))) {
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setQuantity(Integer.parseInt(lsValue));
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setQuantity(Integer.parseInt(lsOrigQuan));
                    }
                    break;
                case 7:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    String lsOrigDisc = "0.00";
                    double lnAccesAmnt = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice()));
                    double lnAccessDsc = Double.parseDouble(String.valueOf(txtField07.getText().replace(",", "")));
                    int lnAccQuan = Integer.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getQuantity());
                    double lnTotalAmnt = lnAccesAmnt * lnAccQuan;

                    if (!pbRequest) {
                        lsOrigDisc = String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount());
                    }
                    if (lnAccessDsc > lnTotalAmnt) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Total Amount cannot be less than Discount.");
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(lsOrigDisc));
                        if (pbState) {
                            oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(0.00));
                        }
                    } else {
                        oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(lsValue.replace(",", "")));
                    }
                    break;
            }
            loadPartsFields();
        }
    };

    private void initCmboxFieldAction() {
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setChrgeTyp(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
                if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(0.00));
                }
                loadPartsFields();
                initFields();
            }
        }
        );
    }

    private void loadPartsFields() {
        if (oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getBarCode() != null) {
            txtField01.setText(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getBarCode()));
        }
        txtField02.setText(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getDescript()));
        if (oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp() != null && !oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp().isEmpty()) {
            comboBox03.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getChrgeTyp())));
        }
        //compute depends if charge or free
        double lnAccesAmnt = Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getSelPrice()));;
        int lnAccQuan = Integer.parseInt(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getQuantity()));
        double lnTotalAmnt = lnAccesAmnt * lnAccQuan;
        txtField04.setText(poGetDecimalFormat.format(lnAccesAmnt));
        txtField05.setText(String.valueOf(lnAccQuan));
        txtField06.setText(poGetDecimalFormat.format(lnTotalAmnt));
        switch (comboBox03.getSelectionModel().getSelectedIndex()) {
            case 0:
                oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setPartsDscount(new BigDecimal(lnTotalAmnt));
                txtField07.setText(poGetDecimalFormat.format(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
                double lnNetPriceFree = lnTotalAmnt - lnTotalAmnt;
                oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPriceFree));
                txtField08.setText(poGetDecimalFormat.format(lnNetPriceFree));
                break;
            case 1:
                txtField07.setText(poGetDecimalFormat.format(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
                double lnNetPriceCharge = lnTotalAmnt - Double.parseDouble(String.valueOf(oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getPartsDscount()));
                oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setNtPrtAmt(new BigDecimal(lnNetPriceCharge));
                txtField08.setText(poGetDecimalFormat.format(lnNetPriceCharge));
                break;
            default:
                txtField04.setText("0.00");
                txtField06.setText("0.00");
                txtField07.setText("0.00");
                txtField08.setText("0.00");
                break;
        }
        if (oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).getEntryNo() > 0) {
            txtField02.setDisable(true);
        }
    }

    private void initTextPropertyAction() {
        txtField01.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isEmpty()) {
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setStockID("");
                    oTransVSPAccessories.getVSPPartsModel().getVSPParts(pnRow).setBarCode("");
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
        if (comboBox03.getSelectionModel().getSelectedIndex() < 0) {
            ShowMessageFX.Warning(null, "Warning", "Please select Charge Type");
            return false;
        }
        if (!txtField04.getText().equals(0.00) || !txtField04.getText().equals("0.00")) {
            if (txtField05.getText().equals(0) || txtField05.getText().equals("0")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter quantity higher than zero.");
                return false;
            }
        }
        if (!txtField05.getText().equals(0) || !txtField05.getText().equals("0")) {
            if (txtField04.getText().equals(0.00) || txtField04.getText().equals("0.00")) {
                ShowMessageFX.Warning(null, "Warning", "Please enter amount.");
                return false;
            }
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
            if (txtField08.getText().equals("0.00")) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid purchase type.");
                return false;
            }
        }
        return true;
    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    private void initFields() {
        txtField01.setDisable(true);
        setDisable(true, txtField02, comboBox03, txtField04, txtField05, txtField07);
        if (pbState) {
            setDisable(false, txtField02, comboBox03);
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
                setDisable(true, txtField02, comboBox03, txtField04, txtField05, txtField06);
            }
            if (!txtField01.getText().isEmpty()) {
                txtField02.setDisable(true);
            }
        }
        if (pbRequest) {
            txtField01.setDisable(false);
        }
        if (psJO.isEmpty()) {
            switch (comboBox03.getSelectionModel().getSelectedIndex()) {
                case 0:
                    txtField04.setDisable(false);
                    txtField05.setDisable(txtField04.getText().isEmpty());
                    txtField07.setDisable(true);
                    break;
                case 1:
                    txtField04.setDisable(false);
                    txtField05.setDisable(txtField04.getText().isEmpty());
                    txtField07.setDisable(txtField04.getText().isEmpty());
                    break;
                default:
                    setDisable(true, txtField04, txtField05, txtField07);
                    break;
            }
        } else {
            txtField05.setDisable(false);
            txtField07.setDisable(false);
        }
    }

}
