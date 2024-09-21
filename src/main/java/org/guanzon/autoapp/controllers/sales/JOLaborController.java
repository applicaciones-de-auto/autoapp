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
import org.guanzon.auto.main.service.JobOrder;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.TextFormatterUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class JOLaborController implements Initializable {

    private GRider oApp;
    private boolean pbLbrDsc;
    private String psLbrDsc = "";
    private int pnRow = 0;
    private boolean pbState = true;
    private boolean pbIsVSPJo = true;
    private final String pxeModuleName = "VSP Labor";
    private String psJO = "";
    private String psOrigDsc = "";
    private JobOrder oTransLabor;
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    ObservableList<String> cChargeType = FXCollections.observableArrayList("FREE OF CHARGE", "CHARGE");

    @FXML
    private Button btnAddLabor, btnEditLabor, btnCloseLabor;
    @FXML
    private TextField txtField01, txtField02, txtField04;
    @FXML
    private ComboBox<String> comboBox03;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setState(boolean fbValue) {
        pbState = fbValue;
    }

    public void setIsVSPJo(boolean fbValue) {
        pbIsVSPJo = fbValue;
    }

    public void setObject(JobOrder foValue) {
        oTransLabor = foValue;
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
        loadLaborFields();
        initFields();
    }

    private void initFielPattern() {
        Pattern pattern = Pattern.compile("[0-9,.]*");
        txtField04.setTextFormatter(new TextFormatterUtil(pattern));
    }

    private void initCapitalizationFields() {
        CustomCommonUtil.setCapsLockBehavior(txtField01);
        CustomCommonUtil.setCapsLockBehavior(txtField02);
    }

    private void initTextKeyPressed() {
        List<TextField> loTxtField = Arrays.asList(
                txtField01, txtField02, txtField04);
        //Detail & AddOns TextField Tab);
        loTxtField.forEach(tf -> tf.setOnKeyPressed(event -> txtField_KeyPressed(event)));
    }

    private void txtField_KeyPressed(KeyEvent event) {
        String lsTxtFieldID = ((TextField) event.getSource()).getId();
        JSONObject loJSON = new JSONObject();
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3) {
            switch (lsTxtFieldID) {
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
                txtField01, txtField02, txtField04);
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
                    oTransLabor.getJOLaborModel().getDetailModel(pnRow).setUnitPrce(new BigDecimal(lsValue.replace(",", "")));
                    break;
            }
            loadLaborFields();
        }
    };

    private void initCmboxFieldAction() {
        comboBox03.setOnAction(event -> {
            if (comboBox03.getSelectionModel().getSelectedIndex() >= 0) {
                oTransLabor.getJOLaborModel().getDetailModel(pnRow).setPayChrge(String.valueOf(comboBox03.getSelectionModel().getSelectedIndex()));
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
                    oTransLabor.getJOLaborModel().getDetailModel(pnRow).setLaborDsc("");
                }
            }
        });
    }

    private void loadLaborFields() {
        if (!psLbrDsc.isEmpty()) {
            oTransLabor.getJOLaborModel().getDetailModel(pnRow).setLaborDsc(psLbrDsc);
        }
        txtField01.setText(String.valueOf(oTransLabor.getJOLaborModel().getDetailModel(pnRow).getLaborCde()));
        txtField02.setText(String.valueOf(oTransLabor.getJOLaborModel().getDetailModel(pnRow).getLaborDsc()));
        if (oTransLabor.getJOLaborModel().getDetailModel(pnRow).getPayChrge() != null && !oTransLabor.getJOLaborModel().getDetailModel(pnRow).getPayChrge().equals("")) {
            comboBox03.getSelectionModel().select(Integer.parseInt(String.valueOf(oTransLabor.getJOLaborModel().getDetailModel(pnRow).getPayChrge())));
        }
        txtField04.setText(poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransLabor.getJOLaborModel().getDetailModel(pnRow).getUnitPrce()))));
        if (oTransLabor.getJOLaborModel().getDetailModel(pnRow).getEntryNo() > 0) {
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
                    oTransLabor.removeJOLabor(pnRow);
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
        return true;
    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    private void initFields() {
        if (pbState) {
            btnAddLabor.setVisible(true);
            btnAddLabor.setManaged(true);
            btnEditLabor.setVisible(false);
            btnEditLabor.setManaged(false);
            if (pbLbrDsc) {
                txtField02.setDisable(true);
            }
            switch (comboBox03.getSelectionModel().getSelectedIndex()) {
                case 0:
                case 1:
                    txtField04.setDisable(false);
                    break;
                default:
                    txtField04.setDisable(true);
                    break;
            }
        } else {
            if (!pbIsVSPJo) {
                btnAddLabor.setVisible(false);
                btnAddLabor.setManaged(false);
                btnEditLabor.setVisible(true);
                btnEditLabor.setManaged(true);
                if (!psJO.isEmpty()) {
                    setDisable(true, txtField04, comboBox03);
                }
            } else {
                btnAddLabor.setVisible(false);
                btnAddLabor.setManaged(false);
                btnEditLabor.setVisible(false);
                btnEditLabor.setManaged(false);
                setDisable(true, txtField01, txtField02, comboBox03, txtField04);
            }
        }
    }

}
