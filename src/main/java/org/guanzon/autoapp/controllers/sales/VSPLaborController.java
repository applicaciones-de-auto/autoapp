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
import javafx.scene.control.CheckBox;
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
public class VSPLaborController implements Initializable {

    private GRider oApp;
    private boolean pbLbrDsc;
    private String psLbrDsc = "";
    private int pnRow = 0;
    private boolean pbState = true;
    private final String pxeModuleName = "VSP Labor";
    private String psJO = "";
    private String psOrigDsc = "";
    private VehicleSalesProposal oTransVSPLabor;
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

    private void initFielPattern() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(pattern));
        txtField05.setTextFormatter(new TextFormatterUtil(pattern));
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01);
        CustomCommonUtil.setCapsLockBehavior(txtField02);
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
            switch (lnIndex) {
                case 4:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborAmt(new BigDecimal(lsValue.replace(",", "")));
                    break;
                case 5:
                    if (lsValue.isEmpty()) {
                        lsValue = "0.00";
                    }
                    oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(lsValue.replace(",", "")));
                    break;
            }
            loadLaborFields();
        }
    };

    private void initCmboxFieldAction() {
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setChrgeTyp(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
                if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
                    oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(0.00));
                }
                loadLaborFields();
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

    private void loadLaborFields() {
        if (!psLbrDsc.isEmpty()) {
            oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDsc(psLbrDsc);
        }
        txtField01.setText(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborCde()));
        txtField02.setText(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDsc()));
        if (oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp() != null && !oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp().equals("")) {
            comboBox03.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getChrgeTyp())));
        }

        double lnLaborAmnt = Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt()));
        double lnLaborDsc = Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()));
        txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborAmt()))));
        switch (comboBox03.getSelectionModel().getSelectedIndex()) {
            case 0:
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(lnLaborAmnt));
                txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()))));
                double lnNetPriceFree = lnLaborAmnt - lnLaborAmnt;
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPriceFree));
                txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt()))));
                break;
            case 1:
                if (lnLaborDsc > lnLaborAmnt) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Amount cannot be less than Discount.");
                    oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setLaborDscount(new BigDecimal(0.00));
                }
                txtField05.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()))));
                double lnNetPriceCharge = lnLaborAmnt - Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getLaborDscount()));
                oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).setNtLabAmt(new BigDecimal(lnNetPriceCharge));
                txtField06.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPLabor.getVSPLaborModel().getVSPLabor(pnRow).getNtLabAmt()))));
                break;
            default:
                txtField04.setText("0.00");
                txtField05.setText("0.00");
                txtField06.setText("0.00");
                break;
        }
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

    private void initButtonsClick() {
        btnCloseLabor.setOnAction(this::handleButtonAction);
        btnAddLabor.setOnAction(this::handleButtonAction);
        btnEditLabor.setOnAction(this::handleButtonAction);
    }

    private void handleButtonAction(ActionEvent event) {
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
        if (txtField04.getText().equals(0.00) || txtField04.getText().equals("0.00")) {
            ShowMessageFX.Warning(null, "Warning", "Please enter amount.");
            return false;
        }
        if (comboBox03.getSelectionModel().getSelectedIndex() == 1) {
            if (txtField06.getText().equals("0.00")) {
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
                setDisable(true, txtField04, txtField05);
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
                setDisable(true, txtField04, comboBox03);
            }
        }
    }

}
