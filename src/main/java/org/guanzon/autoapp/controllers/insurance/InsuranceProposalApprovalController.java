/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.insurance;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.models.insurance.InsuranceProposalApproval;
import org.guanzon.autoapp.interfaces.GTransactionInterface;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class InsuranceProposalApprovalController implements Initializable, ScreenInterface, GTransactionInterface {

    private GRider oApp;
    private String pxeModuleName = "Insurance Proposal Approval";
    private int pnEditMode = -1;
    private UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    ObservableList<String> cComboFilter = FXCollections.observableArrayList("PROPOSAL DATE", "PROPOSAL NO", "INSURANCE COMPANY", "CUSTOMER NAME", "PLATE/CSNO");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose, btnApprove, btnDisapprove;
    @FXML
    private TableView<InsuranceProposalApproval> tblVhclApproval;
    @FXML
    private CheckBox selectAllCheckBox;
    @FXML
    private TableColumn<InsuranceProposalApproval, String> tblindex01, tblindex03, tblindex04, tblindex05, tblindex06, tblindex07, tblindex08, tblindex09, tblindex10;
    @FXML
    private TableColumn<InsuranceProposalApproval, Boolean> tblindex02;
    @FXML
    private ComboBox<String> comboBoxFilter;
    @FXML
    private DatePicker datePickerFrom, datePickerTo;
    @FXML
    private Label lblTo, lblFrom;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnRefresh;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCapitalizationFields();
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
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    @Override
    public void initCapitalizationFields() {

    }

    @Override
    public void initTextFieldFocus() {

    }

    @Override
    public void initTextKeyPressed() {

    }

    @Override
    public void initButtonsClick() {

    }

    @Override
    public void initComboBoxItems() {
        comboBoxFilter.setItems(cComboFilter);
    }

    @Override
    public void initFieldsAction() {

    }

    @Override
    public void initTextFieldsProperty() {

    }

    @Override
    public void clearTables() {

    }

    @Override
    public void clearFields() {

    }

    @Override
    public void initFields(int fnValue) {

    }

    @Override
    public void loadMasterFields() {

    }

}
