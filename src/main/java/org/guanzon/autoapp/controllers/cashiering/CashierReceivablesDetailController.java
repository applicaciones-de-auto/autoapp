/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.cashiering;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.auto.main.cashiering.CashierReceivables;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.models.cashiering.Cashier_Receivables_Detail;
import org.guanzon.autoapp.models.cashiering.Cashier_Receivables_Issue_Receipt;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class CashierReceivablesDetailController implements Initializable, ScreenInterface {

    private GRider oApp;
    private CashierReceivables oTrans;
    private final String pxeModuleName = "Cashier Receivables Details"; //Form Title
    private ObservableList<Cashier_Receivables_Detail> poCARDetailsData = FXCollections.observableArrayList();
    private ObservableList<Cashier_Receivables_Issue_Receipt> poCARIssueReceiptData = FXCollections.observableArrayList();
    ObservableList<String> cPayerType = FXCollections.observableArrayList("CUSTOMER", "BANK", "INSURANCE", "SUPPLIER", "ASSOCIATE");
    private String poTransNox = "";
    private int pnRow;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane stackPane;
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
    @FXML
    private TableView<Cashier_Receivables_Issue_Receipt> tblViewIssueReceipt;
    @FXML
    private TableColumn<Cashier_Receivables_Issue_Receipt, String> tblindex01_receipt, tblindex02_receipt, tblindex03_receipt;

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
        CustomCommonUtil.setDropShadow(AnchorPane, stackPane);
        initLoadTable();
        initLoadIssueTable();
        initCapitalizationFields();
        comboBox03.setItems(cPayerType);
        if (poTransNox != null) {
            if (!poTransNox.isEmpty()) {
                JSONObject loJSON = new JSONObject();
                loJSON = oTrans.openTransaction(poTransNox);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterField();
                    loadCARDetail();
                    loadCARIssueReceipts();
                }
            }
        }
        btnClose.setOnAction(this::handleButtonAction);
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField04, txtField07);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea05, textArea06, textArea08);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));

    }

    private boolean loadMasterField() {
        if (oTrans.getMasterModel().getDetailModel(pnRow).getTransNo() != null) {
            txtField01.setText(oTrans.getMasterModel().getDetailModel(pnRow).getTransNo());
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getTransactDte() != null) {
            datePicker02.setValue(CustomCommonUtil.strToDate(SQLUtil.dateFormat(
                    oTrans.getMasterModel().getDetailModel(pnRow).getTransactDte(), SQLUtil.FORMAT_SHORT_DATE)));
        }
        int lnPyrType = -1;

        if (oTrans.getMasterModel().getDetailModel(pnRow).getPayerCde() != null) {
            switch (oTrans.getMasterModel().getDetailModel(pnRow).getPayerCde()) {
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
        }
        comboBox03.getSelectionModel().select(lnPyrType);
        if (oTrans.getMasterModel().getDetailModel(pnRow).getTransNo() != null) {
            txtField04.setText(oTrans.getMasterModel().getDetailModel(pnRow).getFormNo());
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getSourceCD() != null) {
            textArea05.setText(oTrans.getMasterModel().getDetailModel(pnRow).getSourceCD());
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getPayerNme() != null) {
            txtField07.setText(oTrans.getMasterModel().getDetailModel(pnRow).getPayerNme());
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getPayerAdd() != null) {
            textArea08.setText(oTrans.getMasterModel().getDetailModel(pnRow).getPayerAdd());
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getGrossAmt() != null) {
            txtField09.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getDetailModel(pnRow).getGrossAmt()));
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getDiscAmt() != null) {
            txtField10.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getDetailModel(pnRow).getDiscAmt()));
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getTotalAmt() != null) {
            txtField11.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getDetailModel(pnRow).getTotalAmt()));
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getDeductn() != null) {
            txtField12.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getDetailModel(pnRow).getDeductn()));
        }
        if (oTrans.getMasterModel().getDetailModel(pnRow).getAmtPaid() != null) {
            txtField13.setText(CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getDetailModel(pnRow).getAmtPaid()));
        }
        textArea06.setText("");
        return true;
    }

    private void loadCARDetail() {
        poCARDetailsData.clear();
        String lsParticu = "";
        String lsGrsAmnt = "";
        String lsDscAmnt = "";
        String lsTtLAmnt = "";
        String lsDeAmntx = "";
        String lsPdAmntx = "";
        for (int lnCtr = 0; lnCtr <= oTrans.getDetailModel().getDetailList().size() - 1; lnCtr++) {
            if (oTrans.getDetailModel().getDetailModel(lnCtr).getTranType() != null) {
                lsParticu = oTrans.getDetailModel().getDetailModel(lnCtr).getTranType();
            }
            if (oTrans.getDetailModel().getDetailModel(lnCtr).getGrossAmt() != null) {
                lsGrsAmnt = CustomCommonUtil.setDecimalFormat(oTrans.getDetailModel().getDetailModel(lnCtr).getGrossAmt());
            }
            if (oTrans.getDetailModel().getDetailModel(lnCtr).getGrossAmt() != null) {
                lsDscAmnt = CustomCommonUtil.setDecimalFormat(oTrans.getDetailModel().getDetailModel(lnCtr).getDiscAmt());
            }
            if (oTrans.getDetailModel().getDetailModel(lnCtr).getGrossAmt() != null) {
                lsTtLAmnt = CustomCommonUtil.setDecimalFormat(oTrans.getDetailModel().getDetailModel(lnCtr).getTotalAmt());
            }
            if (oTrans.getDetailModel().getDetailModel(lnCtr).getGrossAmt() != null) {
                lsDeAmntx = CustomCommonUtil.setDecimalFormat(oTrans.getDetailModel().getDetailModel(lnCtr).getDeductn());
            }
            if (oTrans.getDetailModel().getDetailModel(lnCtr).getGrossAmt() != null) {
                lsPdAmntx = CustomCommonUtil.setDecimalFormat(oTrans.getDetailModel().getDetailModel(lnCtr).getAmtPaid());
            }
            poCARDetailsData.add(new Cashier_Receivables_Detail(
                    String.valueOf(lnCtr + 1),
                    lsParticu,
                    lsGrsAmnt,
                    lsDscAmnt,
                    lsTtLAmnt,
                    lsDeAmntx,
                    lsPdAmntx
            ));
            lsParticu = "";
            lsGrsAmnt = "";
            lsDscAmnt = "";
            lsDeAmntx = "";
            lsPdAmntx = "";
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

    private void loadCARIssueReceipts() {
        poCARIssueReceiptData.clear();
        String lsFrmType = "";
        String lsSiNoxxx = "";
        String lsSiDatex = "";
        String lsSiTotal = "";
        JSONObject loJSON = new JSONObject();
        loJSON = oTrans.loadReceipts();
        if ("success".equals((String) loJSON.get("result"))) {
            for (int lnCtr = 0; lnCtr <= oTrans.getMasterModel().getReceiptList().size() - 1; lnCtr++) {
                if (oTrans.getMasterModel().getReceiptModel(lnCtr).getDocType() != null) {
                    switch (oTrans.getMasterModel().getReceiptModel(lnCtr).getDocType()) {
                        case "0":
                            lsFrmType = "VSI";
                            break;
                        case "1":
                            lsFrmType = "CPSI";
                            break;
                        case "2":
                            lsFrmType = "OR";
                            break;
                        case "3":
                            lsFrmType = "BSI";
                            break;
                        case "4":
                            lsFrmType = "CR";
                            break;
                        case "5":
                            lsFrmType = "PSI";
                            break;
                        case "6":
                            lsFrmType = "BSI";
                            break;
                        case "7":
                            lsFrmType = "AR";
                            break;
                        default:
                            lsFrmType = "";
                            break;
                    }
                }
                if (oTrans.getMasterModel().getReceiptModel(lnCtr).getReferNo() != null) {
                    lsSiNoxxx = oTrans.getMasterModel().getReceiptModel(lnCtr).getReferNo();
                }
                if (oTrans.getMasterModel().getReceiptModel(lnCtr).getTransactDte() != null) {
                    lsSiDatex = CustomCommonUtil.xsDateShort(oTrans.getMasterModel().getReceiptModel(lnCtr).getTransactDte());
                }
                if (oTrans.getMasterModel().getReceiptModel(lnCtr).getTranTotl() != null) {
                    lsSiTotal = CustomCommonUtil.setDecimalFormat(oTrans.getMasterModel().getReceiptModel(lnCtr).getTranTotl());
                }
                poCARIssueReceiptData.add(new Cashier_Receivables_Issue_Receipt(
                        String.valueOf(lnCtr + 1),
                        lsFrmType,
                        lsSiNoxxx,
                        lsSiDatex,
                        lsSiTotal
                ));
                lsFrmType = "";
                lsSiNoxxx = "";
                lsSiDatex = "";
                lsSiTotal = "";
            }

        }
        tblViewIssueReceipt.setItems(poCARIssueReceiptData);
    }

    public void initLoadIssueTable() {
        tblindex01_receipt.setCellValueFactory(new PropertyValueFactory<>("tblindex01"));
        tblindex02_receipt.setCellValueFactory(new PropertyValueFactory<>("tblindex02"));
        tblindex03_receipt.setCellValueFactory(new PropertyValueFactory<>("tblindex03"));
        tblViewIssueReceipt.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewIssueReceipt.lookup("TableHeaderRow");
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
