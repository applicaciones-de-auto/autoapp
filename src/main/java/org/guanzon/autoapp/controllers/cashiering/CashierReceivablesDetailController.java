/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.auto.main.cashiering.CashierReceivables;
import org.guanzon.auto.model.cashiering.Model_Cashier_Receivables;
import org.guanzon.auto.model.cashiering.Model_Cashier_Receivables_Detail;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.cashiering.Cashier_Receivables_Detail;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.UnloadForm;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class CashierReceivablesDetailController implements Initializable, ScreenInterface {

    private GRider oApp;
    private CashierReceivables oTrans;
    private final String pxeModuleName = "Cashier Receivables"; //Form Title
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private ObservableList<Cashier_Receivables_Detail> poCARDetailsData = FXCollections.observableArrayList();
    ObservableList<String> cPayerType = FXCollections.observableArrayList("CUSTOMER", "BANK", "INSURANCE", "SUPPLIER", "ASSOCIATE");
    private String poTransNox = "";
    private int pnRow;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btnClose;
    @FXML
    private TextField txtField01, txtField04, txtField07, txtField09, txtField10, txtField11, txtField12, txtField13;
    @FXML
    private DatePicker datePicker02;
    @FXML
    private ComboBox<String> comboBox03;
    @FXML
    private TextArea textArea05, textArea06, textArea08;
    @FXML
    private TableView<Cashier_Receivables_Detail> tblViewCARDetail;
    @FXML
    private TableColumn<Cashier_Receivables_Detail, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07;

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setTransNo(String fsValue) {
        poTransNox = fsValue;
    }

    public void setObject(CashierReceivables foValue) {
        oTrans = foValue;
    }

    public void setRow(int fnValue) {
        pnRow = fnValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initLoadTable();
        comboBox03.setItems(cPayerType);
        if (poTransNox != null) {
            if (!poTransNox.isEmpty()) {
                JSONObject loJSON = new JSONObject();
                loJSON = oTrans.openTransaction(poTransNox);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterField();
                    loadCARDetail();
                }
            }
        }
        btnClose.setOnAction(this::handleButtonAction);
    }

    private boolean loadMasterField() {
        txtField01.setText(getCARModel().getTransNo() != null ? getCARModel().getTransNo() : "");
        datePicker02.setValue(getCARModel().getTransactDte() != null
                ? CustomCommonUtil.strToDate(SQLUtil.dateFormat(getCARModel().getTransactDte(), SQLUtil.FORMAT_SHORT_DATE))
                : LocalDate.of(1900, Month.JANUARY, 1));
        int lnPyrType = -1;
        switch (getCARModel().getPayerCde()) {
            case "c":
                lnPyrType = 0;
                break;
            case "b":
                lnPyrType = 1;
                break;
            case "i":
                lnPyrType = 2;
                break;
            case "s":
                lnPyrType = 3;
                break;
            case "a":
                lnPyrType = 4;
                break;
        }
        comboBox03.getSelectionModel().select(lnPyrType);
        txtField04.setText(getCARModel().getFormNo() != null ? getCARModel().getFormNo() : "");
        textArea05.setText(getCARModel().getSourceCD() != null ? getCARModel().getSourceCD() : "");
        textArea06.setText("");
        txtField07.setText(getCARModel().getPayerNme() != null ? getCARModel().getPayerNme() : "");
        textArea08.setText(getCARModel().getAddress() != null ? getCARModel().getAddress() : "");
        txtField09.setText(getCARModel().getGrossAmt() != null ? getNumberToFormat(getCARModel().getGrossAmt()) : "");
        txtField10.setText(getCARModel().getDiscAmt() != null ? getNumberToFormat(getCARModel().getDiscAmt()) : "");
        txtField11.setText(getCARModel().getTotalAmt() != null ? getNumberToFormat(getCARModel().getTotalAmt()) : "");
        txtField12.setText(getCARModel().getDeductn() != null ? getNumberToFormat(getCARModel().getDeductn()) : "");
        txtField13.setText(getCARModel().getAmtPaid() != null ? getNumberToFormat(getCARModel().getAmtPaid()) : "");
        return true;
    }

    private Model_Cashier_Receivables getCARModel() {
        return oTrans.getMasterModel().getDetailModel(pnRow);
    }

    private Model_Cashier_Receivables_Detail getCARDetailModel(int fnRow) {
        return oTrans.getDetailModel().getDetailModel(fnRow);
    }

    private String getNumberToFormat(Object foTblClmn) {
        return poGetDecimalFormat.format(Double.parseDouble(String.valueOf(foTblClmn)));
    }

    private void loadCARDetail() {
        poCARDetailsData.clear();
        for (int lnCtr = 0; lnCtr <= oTrans.getDetailModel().getDetailList().size() - 1; lnCtr++) {
            String lsParticu = getCARDetailModel(lnCtr).getTranType() != null ? getCARDetailModel(lnCtr).getTranType() : "";
            String lsGrsAmnt = getCARDetailModel(lnCtr).getGrossAmt() != null ? getNumberToFormat(getCARDetailModel(lnCtr).getGrossAmt()) : "";
            String lsDscAmnt = getCARDetailModel(lnCtr).getDiscAmt() != null ? getNumberToFormat(getCARDetailModel(lnCtr).getDiscAmt()) : "";
            String lsTtLAmnt = getCARDetailModel(lnCtr).getTotalAmt() != null ? getNumberToFormat(getCARDetailModel(lnCtr).getTotalAmt()) : "";
            String lsDeAmntx = getCARDetailModel(lnCtr).getDeductn() != null ? getNumberToFormat(getCARDetailModel(lnCtr).getDeductn()) : "";
            String lsPdAmntx = getCARDetailModel(lnCtr).getAmtPaid() != null ? getNumberToFormat(getCARDetailModel(lnCtr).getAmtPaid()) : "";
            poCARDetailsData.add(new Cashier_Receivables_Detail(
                    String.valueOf(lnCtr + 1),
                    lsParticu,
                    lsGrsAmnt,
                    lsDscAmnt,
                    lsTtLAmnt,
                    lsDeAmntx,
                    lsPdAmntx
            ));
        }
        tblViewCARDetail.setItems(poCARDetailsData);
    }

    public void initLoadTable() {
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex04"));
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05"));
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("tblindex06"));
        tblindex07.setCellValueFactory(new PropertyValueFactory<>("tblindex07"));

        tblViewCARDetail.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewCARDetail.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    public void handleButtonAction(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;
        }
    }
}
