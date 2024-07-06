/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.views.sales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehicleInquiryFormController implements Initializable {

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblJobOrderStatus;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private Tab tabCustomerInquiry;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField78;
    @FXML
    private TextField txtField84;
    @FXML
    private TextField txtField75;
    @FXML
    private TextField txtField76;
    @FXML
    private ComboBox<?> comboBox80;
    @FXML
    private TextField txtField36;
    @FXML
    private TextField txtField37;
    @FXML
    private TextField txtField7811;
    @FXML
    private TextArea textArea33;
    @FXML
    private TableView<?> tblPriorityUnit;
    @FXML
    private TableColumn<?, ?> trgvIndex01;
    @FXML
    private TableColumn<?, ?> trgvIndex02;
    @FXML
    private TableColumn<?, ?> trgvIndex021;
    @FXML
    private TableColumn<?, ?> trgvIndex0211;
    @FXML
    private Button btnTargetVhclRemove;
    @FXML
    private Button btnTargetVhclAdd1;
    @FXML
    private TableView<?> tblPromosOffered;
    @FXML
    private TableColumn<?, ?> prmoIndex01;
    @FXML
    private TableColumn<?, ?> prmoIndex02;
    @FXML
    private TableColumn<?, ?> prmoIndex03;
    @FXML
    private Tab tabInquiryProcess;
    @FXML
    private TextField txtField211;
    @FXML
    private TableView<?> tblRequirementsInfo;
    @FXML
    private TableColumn<?, ?> rqrmIndex01;
    @FXML
    private TableColumn<?, ?> rqrmIndex02;
    @FXML
    private TableColumn<?, ?> rqrmIndex03;
    @FXML
    private TableColumn<?, ?> rqrmIndex04;
    @FXML
    private ComboBox<?> cmbInqpr01;
    @FXML
    private ComboBox<?> cmbInqpr02;
    @FXML
    private Button btnASadd;
    @FXML
    private Button btnASremove;
    @FXML
    private Button btnASprint;
    @FXML
    private Button btnAScancel;
    @FXML
    private TableView<?> tblAdvanceSlip;
    @FXML
    private TableColumn<?, ?> vsasCheck01;
    @FXML
    private TableColumn<?, ?> vsasIndex01;
    @FXML
    private TableColumn<?, ?> vsasIndex02;
    @FXML
    private TableColumn<?, ?> vsasIndex03;
    @FXML
    private TableColumn<?, ?> vsasIndex04;
    @FXML
    private TableColumn<?, ?> vsasIndex05;
    @FXML
    private TableColumn<?, ?> vsasIndex06;
    @FXML
    private TableColumn<?, ?> vsasIndex07;
    @FXML
    private TextField txtPymtc01;
    @FXML
    private Button btnPymtcon;
    @FXML
    private TextField txtPymtc011;
    @FXML
    private TextField txtPymtc0111;
    @FXML
    private Tab tabBankHistory;
    @FXML
    private Button btnBankAppNew;
    @FXML
    private Button btnBankAppView;
    @FXML
    private Button btnBankAppUpdate;
    @FXML
    private Button btnBankAppCancel;
    @FXML
    private TableView<?> tblBankApplication;
    @FXML
    private TableColumn<?, ?> bankCheck01;
    @FXML
    private TableColumn<?, ?> bankIndex01;
    @FXML
    private TableColumn<?, ?> bankIndex02;
    @FXML
    private TableColumn<?, ?> bankIndex03;
    @FXML
    private TableColumn<?, ?> bankIndex04;
    @FXML
    private TableColumn<?, ?> bankIndex05;
    @FXML
    private TableColumn<?, ?> bankIndex07;
    @FXML
    private Tab tabFollowingHistory;
    @FXML
    private Button btnFollowUp;
    @FXML
    private TableView<?> tblFollowHistory;
    @FXML
    private TableColumn<?, ?> flwpIndex01;
    @FXML
    private TableColumn<?, ?> flwpIndex02;
    @FXML
    private TableColumn<?, ?> flwpIndex03;
    @FXML
    private TableColumn<?, ?> flwpIndex04;
    @FXML
    private TableColumn<?, ?> flwpIndex05;
    @FXML
    private TableColumn<?, ?> flwpIndex06;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnConvertSales;
    @FXML
    private Button btnPrintRefund;
    @FXML
    private Button btnLostSale;
    @FXML
    private Button btnProcess;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnRqstApproval;
    @FXML
    private TextField txtField01;
    @FXML
    private Button btnTargetVhclAdd;
    @FXML
    private Button btnPromoRemove;
    @FXML
    private Button btnPromoAdd;
    @FXML
    private TextArea txtAreaRemarks;
    @FXML
    private TextArea txtAreaMISRemarks;
    @FXML
    private Button btnProcMngrApproval;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void tblPriorityUnit_Clicked(MouseEvent event) {
    }

}
