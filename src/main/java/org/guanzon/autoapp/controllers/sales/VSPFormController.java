/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.sales;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.autoapp.models.sales.ModelVSPLabor;
import org.guanzon.autoapp.models.sales.ModelVSPPart;
import org.guanzon.autoapp.utils.InputTextUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.autoapp.utils.UnloadForm;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VSPFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    UnloadForm poUnload = new UnloadForm(); //Used in Close Button
    private final String pxeModuleName = "Vehicle Sales Proposal"; //Form Title
    DecimalFormat poSetDecimalFormat = new DecimalFormat("###0.00");
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    public int pnEditMode;//Modifying fields
    private int pnCtr = 0;
    private String psTransNo = "";
    private int pnRow = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ObservableList<String> cInquiryType = FXCollections.observableArrayList("WALK-IN", "WEB INQUIRY", "PHONE-IN", "REFERRAL", "SALES CALL", "EVENT", "SERVICE", "OFFICE ACCOUNT", "CAREMITTANCE", "DATABASE", "UIO");
    ObservableList<String> cModeOfPayment = FXCollections.observableArrayList("CASH", "BANK PURCHASE ORDER", "BANK FINANCING", "COMPANY PURCHASE ORDER", "COMPANY FINANCING"); //Mode of Payment Values
    ObservableList<String> cFinPromoType = FXCollections.observableArrayList("NONE", "ALL-IN IN HOUSE", "ALL-IN PROMO");
    ObservableList<String> cTplType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o DEALER", "C/o BANK");
    ObservableList<String> cCompType = FXCollections.observableArrayList("NONE", "FOC", "C/o CLIENT", "C/o DEALER", "C/o BANK ");
    ObservableList<String> cCompYearType1 = FXCollections.observableArrayList("NONE", "FREE", "REGULAR RATE", "DISCOUNTED RATE", "FROM PROMO DISC");
    ObservableList<String> cCompYearType2 = FXCollections.observableArrayList("0", "1", "2", "3", "4");
    ObservableList<String> cLTOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE");
    ObservableList<String> cHMOType = FXCollections.observableArrayList("NONE", "FOC", "CHARGE", "C/o BANK");
    private ObservableList<ModelVSPLabor> laborData = FXCollections.observableArrayList();
    private ObservableList<ModelVSPPart> partData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TabPane ImTabPane;
    @FXML
    private Tab tabMain, tabDetails, tabAddOns;
    @FXML
    private Label lblDRNo, lblSINo, lblVSPStatus, lblPrint, lblRFNo, lblApproveDate;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnCancelVSP,
            btnApprove, btnGatePass, btnClose, btnJobOrderAdd, btnAdditionalLabor, btnAddParts,
            btnPaymentHistory, btnAddReservation, btnRemoveReservation;
    //Main TextField Tab
    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
            txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField18,
            txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25;
    //Detail & AddOns TextField Tab
    private TextField txtField30, txtField31, txtField32, txtField33, txtField34, txtField35, txtField36, txtField38,
            txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
            txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
            txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60, txtField62,
            txtField63, txtField65, txtField68, txtField70, txtField72, txtField73, txtField74, txtField75, txtField76,
            txtField77, txtField78, txtField79, txtField80, txtField81;
    @FXML
    private DatePicker datePicker09;
    @FXML
    private ComboBox<String> comboBox05, comboBox29, comboBox37, comboBox61,
            comboBox64, comboBox66, comboBox67, comboBox69, comboBox71;
    @FXML
    private TextArea textArea17, textArea26, textArea27, textArea28;
    @FXML
    private ToggleGroup tgUnitCategory;
    @FXML
    private RadioButton brandNewCat, preOwnedCat;
    @FXML
    private TableView<ModelVSPLabor> tblViewLabor;
    @FXML
    private TableColumn<ModelVSPLabor, String> tblindex01_labor, tblindex02_labor, tblindex03_labor, tblindex04_labor,
            tblindex05_labor, tblindex06_labor, tblindex07_labor, tblindex08_labor, tblindex09_labor;
    @FXML
    private CheckBox chckBoxSpecialAccount, chckBoxRustProof, chckBoxPermaShine, chckBoxUndercoat, chckBoxTint;
    @FXML
    private TableView<ModelVSPPart> tblViewParts;
    @FXML
    private TableColumn<ModelVSPPart, String> tblindex01_part, tblindex02_part, tblindex03_part, tblindex04_part,
            tblindex05_part, tblindex06_part, tblindex07_part, tblindex08_part, tblindex09_part, tblindex10_part,
            tblindex11_part;

    private Stage getStage() {
        return (Stage) btnClose.getScene().getWindow();
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initLaborTable();
        initPartsTable();
        datePicker09.setDayCellFactory(DateFormatCell);
        initCapitalizationFields();
        initTextKeyPressed();
        initTextFieldFocus();
        initCmboxFieldAction();
        initComboBoxItems();
        initTextPropertyAction();
        initButtonsClick();
        clearVSPFields();
        clearTables();
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void loadVSPFields() {

    }

    private void loadVSPPrint(String sTransno) throws SQLException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPFormPrint.fxml"));
////            VSPFormPrintController loControl = new VSPFormPrintController();
//            loControl.setGRider(oApp);
//            loControl.setTransNox(sTransno);
//            fxmlLoader.setController(loControl);
            //load the main interface
            Parent parent = fxmlLoader.load();
            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void addRowVSPLabor() {
        chckBoxRustProof.setOnAction(event -> {
            if (chckBoxRustProof.isSelected()) {
                initLaborDescript("RUSTPROOF", true);
                chckBoxRustProof.setSelected(false);
            }
        });
        chckBoxPermaShine.setOnAction(event -> {
            if (chckBoxPermaShine.isSelected()) {
                initLaborDescript("PERMASHINE", true);
                chckBoxPermaShine.setSelected(false);
            }
        });
        chckBoxUndercoat.setOnAction(event -> {
            if (chckBoxUndercoat.isSelected()) {
                initLaborDescript("UNDERCOAT", true);
                chckBoxUndercoat.setSelected(false);
            }
        });
        chckBoxTint.setOnAction(event -> {
            if (chckBoxTint.isSelected()) {
                initLaborDescript("TINT", true);
                chckBoxTint.setSelected(false);
            }
        });
    }

    private void initLaborDescript(String laborDescript, boolean withLabor) {
//        if (oTransVSP.addVSPLabor(laborDescript, withLabor)) {
//            loadLaborAdditionalDialog(oTransVSP.getVSPLaborCount(), true);
//        } else {
//            ShowMessageFX.Warning(getStage(), oTransVSP.getMessage(), "Warning", "Error while adding labor.");
//        }
    }

    private void tblLabor_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewLabor.getSelectionModel().getSelectedIndex() + 1;
            if (pnRow == 0) {
                return;
            }
            if (event.getClickCount() == 2) {
//                loadLaborWindowDialog(pnRow, false);
            }
        }
    }

    private void loadLaborWindowDialog(Integer fnRow, boolean fbIsAdd) throws IOException {

        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPLaborEntryDialog.fxml"));
//            VSPLaborEntryDialogController loControl = new VSPLaborEntryDialogController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransVSP);
//            loControl.setOrigDsc((String) oTrans.getVSPLaborDetail(fnRow, 7));
//            loControl.setState(fbIsAdd);
//            loControl.setJO((String) oTrans.getVSPLaborDetail(fnRow, 11));
//            fxmlLoader.setController(loControl);
//            loControl.setRow(fnRow);
            //load the main interface
            Parent parent = fxmlLoader.load();

            parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadLaborTable();
            loadVSPFields();

        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);

        }
    }

    private void loadLaborTable() {

    }

    private void initLaborTable() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewLabor.setEditable(true);
        } else {
            tblViewLabor.setEditable(false);
        }

        tblindex01_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex01_labor"));
        tblindex02_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex02_labor"));
        tblindex03_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex03_labor"));
        tblindex04_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex04_labor"));
        tblindex05_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex05_labor"));
        tblindex06_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex06_labor"));
        tblindex07_labor.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex08_labor.setCellValueFactory(new PropertyValueFactory<>("addOrNot"));
        tblindex09_labor.setCellValueFactory(new PropertyValueFactory<>("tblindex07_labor"));

        tblViewLabor.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewLabor.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

    }

    private void loadPartsWindowDialog(Integer fnRow, boolean isAdd) throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPPartsDialog.fxml"));
//            VSPPartsDialogController loControl = new VSPPartsDialogController();
//            loControl.setGRider(oApp);
//            loControl.setObject(oTransVSP);
//            loControl.setState(isAdd);
//            fxmlLoader.setController(loControl);
//            loControl.setRequest(false);
//            loControl.setRow(fnRow);
//            loControl.setOrigDsc((String) oTrans.getVSPPartsDetail(fnRow, 9));
//            loControl.setStockID((String) oTrans.getVSPPartsDetail(fnRow, 3));
//            loControl.setJO((String) oTrans.getVSPPartsDetail(fnRow, 11));
            //load the main interface
            Parent parent = fxmlLoader.load();
            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            //set the main interface as the scene/*
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            loadPartsTable();
            loadVSPFields();
        } catch (IOException e) {
            ShowMessageFX.Warning(getStage(), e.getMessage(), "Warning", null);
            System.exit(1);
        }

    }

    private void tblParts_Clicked(MouseEvent event) {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewParts.getSelectionModel().getSelectedIndex();
            if (pnRow == 0) {
                return;
            }
            if (event.getClickCount() == 2) {
//                loadPartsAdditionalDialog(pnRow, false);
                loadPartsTable();
            }
        }
    }

    private void loadPartsTable() {

    }

    private void initPartsTable() {
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            tblViewParts.setEditable(true);
        } else {
            tblViewParts.setEditable(false);
        }

        tblindex01_part.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part"));
        tblindex02_part.setCellValueFactory(new PropertyValueFactory<>("tblindex02_part"));
        tblindex03_part.setCellValueFactory(new PropertyValueFactory<>("tblindex03_part"));
        tblindex04_part.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot"));
        tblindex05_part.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part"));
        tblindex06_part.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part"));
        tblindex07_part.setCellValueFactory(new PropertyValueFactory<>("tblindex06_part"));
        tblindex08_part.setCellValueFactory(new PropertyValueFactory<>("tblindex07_part"));
        tblindex09_part.setCellValueFactory(new PropertyValueFactory<>("tblindex08_part"));
        tblindex10_part.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part"));
        tblindex11_part.setCellValueFactory(new PropertyValueFactory<>("tblindex10_part"));

        tblViewParts.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewParts.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }
    private Callback<DatePicker, DateCell> DateFormatCell = (final DatePicker param) -> new DateCell() {
        @Override
        public void updateItem(LocalDate fsItem, boolean fsEmpty) {
            super.updateItem(fsItem, fsEmpty);
            LocalDate loMinDate = InputTextUtil.strToDate(InputTextUtil.xsDateShort((Date) oApp.getServerDate())).minusDays(0);
            setDisable(fsEmpty || fsItem.isBefore(loMinDate));
        }
    };

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField03, txtField04, txtField06, txtField07, txtField08, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23,
                txtField24, txtField25, txtField36, txtField32, txtField62, txtField65, txtField81);
        loTxtField.forEach(tf -> InputTextUtil.setCapsLockBehavior(tf));

        List<TextArea> loTxtArea = Arrays.asList(textArea17, textArea26, textArea27, textArea28);

        loTxtArea.forEach(ta -> InputTextUtil.setCapsLockBehavior(ta));
    }

    private void initTextKeyPressed() {

    }

    private void initTextFieldFocus() {

    }

    private void initCmboxFieldAction() {
        if (!comboBox66.getSelectionModel().isSelected(0)) {
            comboBox67.getItems().remove("0");
        } else {
            comboBox67.getItems().add("0");
        }

    }

    private void initComboBoxItems() {
        comboBox05.setItems(cInquiryType);
        comboBox29.setItems(cModeOfPayment);
        comboBox37.setItems(cFinPromoType);
        comboBox61.setItems(cTplType);
        comboBox64.setItems(cCompType);
        comboBox66.setItems(cCompYearType1);
        comboBox67.setItems(cCompYearType2);
        comboBox69.setItems(cLTOType);
        comboBox71.setItems(cHMOType);
    }

    private void initTextPropertyAction() {

    }

    private void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnPrint, btnCancelVSP,
                btnApprove, btnGatePass, btnClose, btnJobOrderAdd, btnAdditionalLabor, btnAddParts,
                btnPaymentHistory, btnAddReservation, btnRemoveReservation);
    }

    @SuppressWarnings("unchecked")
    private void clearVSPFields() {
        List<TextField> loTxtFieldString = Arrays.asList(txtField01, txtField02, txtField03, txtField04, txtField06, txtField07, txtField08,
                txtField10, txtField11, txtField12, txtField13, txtField14, txtField15, txtField16, txtField18,
                txtField19, txtField20, txtField21, txtField22, txtField23, txtField24, txtField25, txtField81);
        loTxtFieldString.forEach(tf -> tf.setText(""));

        List<TextField> loTxtFieldDouble = Arrays.asList(txtField30, txtField31, txtField33, txtField34, txtField35, txtField38,
                txtField39, txtField40, txtField41, txtField42, txtField43, txtField44, txtField45,
                txtField46, txtField47, txtField48, txtField49, txtField50, txtField51, txtField52, txtField53,
                txtField54, txtField55, txtField56, txtField57, txtField58, txtField59, txtField60,
                txtField63, txtField68, txtField70, txtField72, txtField73, txtField74, txtField76,
                txtField77, txtField78, txtField79, txtField80);
        loTxtFieldDouble.forEach(tf -> tf.setText("0.00"));

        List<ComboBox> loComboBox = Arrays.asList(comboBox05, comboBox29, comboBox37, comboBox61,
                comboBox64, comboBox66, comboBox67, comboBox69, comboBox71);
        loComboBox.forEach(tf -> tf.setValue(null));
        brandNewCat.setSelected(false);
        preOwnedCat.setSelected(false);
        chckBoxSpecialAccount.setSelected(false);
        chckBoxRustProof.setSelected(false);
        chckBoxPermaShine.setSelected(false);
        chckBoxUndercoat.setSelected(false);
        chckBoxTint.setSelected(false);
        tgUnitCategory.selectToggle(null);
        switchToTab(tabMain, ImTabPane);
    }

    private void switchToTab(Tab foTab, TabPane foTabPane) {
        foTabPane.getSelectionModel().select(foTab);
    }

    private void clearTables() {
        laborData.clear();
        partData.clear();
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        //depends if empty or not
        setDisable(!(lbShow && !txtField15.getText().isEmpty()),
                txtField16, txtField18, txtField20, txtField21, txtField22, txtField23);
        tabAddOns.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        tabDetails.setDisable(!(lbShow && !txtField15.getText().isEmpty()));
        setDisable(!lbShow,
                txtField15, textArea28,
                //discount
                txtField51, txtField54, txtField56, txtField58,
                //comboBox
                comboBox29, comboBox37, comboBox61, comboBox64, comboBox66, comboBox67, comboBox69, comboBox71,
                //add ons
                btnJobOrderAdd, chckBoxRustProof, chckBoxPermaShine, chckBoxUndercoat, chckBoxTint, btnAdditionalLabor, btnAddParts
        );
        btnAdd.setVisible(!lbShow);
        btnAdd.setManaged(!lbShow);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnPrint.setVisible(false);
        btnPrint.setManaged(false);
        btnCancelVSP.setManaged(false);
        btnCancelVSP.setVisible(false);
        btnGatePass.setManaged(false);
        btnGatePass.setVisible(false);
        if (fnValue == EditMode.READY) {
            if (lblVSPStatus.getText().equals("Cancelled")) {
                btnCancelVSP.setVisible(false);
                btnCancelVSP.setManaged(false);
                btnEdit.setVisible(false);
                btnEdit.setManaged(false);
                tabAddOns.setDisable(false);
                tabDetails.setDisable(false);
                btnPrint.setVisible(false);
                btnPrint.setManaged(false);
            } else {
                btnCancelVSP.setVisible(true);
                btnCancelVSP.setManaged(true);
                btnEdit.setVisible(true);
                btnEdit.setManaged(true);
                btnPrint.setVisible(true);
                btnPrint.setManaged(true);
                tabAddOns.setDisable(false);
                tabDetails.setDisable(false);
            }
        }
    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

}
