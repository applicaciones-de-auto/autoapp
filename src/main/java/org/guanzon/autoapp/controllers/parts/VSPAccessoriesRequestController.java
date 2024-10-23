package org.guanzon.autoapp.controllers.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.auto.main.sales.VehicleSalesProposal;
import org.guanzon.autoapp.controllers.sales.VSPAccessoriesController;
import org.guanzon.autoapp.models.sales.Part;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.interfaces.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author John Dave
 */
public class VSPAccessoriesRequestController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposal oTransVSPRequest;
    private String pxeModuleName = "VSP Accessories Request";
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private ObservableList<Part> accessoriesData = FXCollections.observableArrayList();
    private int pnEditMode;
    private int pnRow = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btnClose, btnCancel, btnBrowse, btnEdit, btnSave;
    @FXML
    private TextField txtField01, txtField02, txtField04, txtField03, txtField06, txtField07;
    @FXML
    private TextArea textArea05;
    @FXML
    private TableView<Part> tblViewAccessories;
    @FXML
    private TableColumn<Part, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06;

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTransVSPRequest = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
        initMasterTable();
        initCapitalizationFields();
        initButtonsClick();
        tblViewAccessories.setOnMouseClicked(this::tblAccessories_Click);
        pnEditMode = EditMode.UNKNOWN;
        initFields(pnEditMode);
    }

    private void initCapitalizationFields() {
        List<TextField> loTxtField = Arrays.asList(txtField01, txtField02, txtField04, txtField03, txtField06, txtField07);
        loTxtField.forEach(tf -> CustomCommonUtil.setCapsLockBehavior(tf));
        List<TextArea> loTxtArea = Arrays.asList(textArea05);
        loTxtArea.forEach(ta -> CustomCommonUtil.setCapsLockBehavior(ta));
    }

    private void loadMasterFields() {
        txtField01.setText(oTransVSPRequest.getMasterModel().getMasterModel().getBuyCltNm());
        txtField02.setText(oTransVSPRequest.getMasterModel().getMasterModel().getCoCltNm());
        txtField03.setText(oTransVSPRequest.getMasterModel().getMasterModel().getSEName());
        txtField04.setText(oTransVSPRequest.getMasterModel().getMasterModel().getAgentNm());
        textArea05.setText(oTransVSPRequest.getMasterModel().getMasterModel().getVhclFDsc());
        txtField06.setText(oTransVSPRequest.getMasterModel().getMasterModel().getCSNo());
        txtField07.setText(oTransVSPRequest.getMasterModel().getMasterModel().getPlateNo());
    }

    private void loadMasterTable() {
        accessoriesData.clear();
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsQuantity = "";
        String lsDiscAmount = "";
        String lsTotalAmount = "";
        String lsNetAmount = "";
        String lsPartsDesc = "";
        String lsBarCode = "";
        for (int lnCtr = 0; lnCtr <= oTransVSPRequest.getVSPPartsList().size() - 1; lnCtr++) {
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice())));
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                lsQuantity = String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null && oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                BigDecimal lsGrsAmt = new BigDecimal(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice()));
                int lsQuan = Integer.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
                lsTotalAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(lsGrsAmt.doubleValue() * lsQuan)));
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount() != null) {
                lsDiscAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getPartsDscount())));
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt() != null) {
                lsNetAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt())));
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getChrgeTyp().equals("0")) {
                lbChargeType = true;
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc() != null) {
                lsPartsDesc = String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc());
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getBarCode() != null) {
                lsBarCode = String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getBarCode());
            }
            accessoriesData.add(new Part(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getTransNo()),
                    String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getStockID()),
                    String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getDescript()),
                    lsQuantity,
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    lsBarCode,
                    "",
                    "",
                    lsPartsDesc,
                    lsTotalAmount,
                    lbChargeType
            ));
            lbChargeType = false;
            lsGrsAmount = "";
            lsQuantity = "";
            lsDiscAmount = "";
            lsTotalAmount = "";
            lsNetAmount = "";
            lsPartsDesc = "";
            lsBarCode = "";
        }
        tblViewAccessories.setItems(accessoriesData);
    }

    private void initMasterTable() {
        if (pnEditMode == EditMode.UPDATE) {
            tblViewAccessories.setEditable(true);
        } else {
            tblViewAccessories.setEditable(false);
        }
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part")); // tblindexRow
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part")); // sales accessories description
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex09_part")); // accessories number
        tblindex04.setCellValueFactory(new PropertyValueFactory<>("tblindex12_part")); // accessories description
        tblindex05.setCellValueFactory(new PropertyValueFactory<>("tblindex05_part")); // quantity
        tblindex06.setCellValueFactory(new PropertyValueFactory<>("FreeOrNot")); // foc

        tblViewAccessories.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblViewAccessories.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }

    private void initButtonsClick() {
        List<Button> loButtons = Arrays.asList(btnClose, btnSave, btnCancel, btnBrowse, btnEdit);
        loButtons.forEach(button -> button.setOnAction(this::handleButtonAction));
    }

    private void handleButtonAction(ActionEvent event) {
        JSONObject loJSON = new JSONObject();
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnCancel":
                if (ShowMessageFX.YesNo(null, "Cancel Confirmation", "Are you sure you want to cancel?")) {
                    clearFields();
                    clearTables();
                    oTransVSPRequest = new VehicleSalesProposal(oApp, false, oApp.getBranchCode());
                    pnEditMode = EditMode.UNKNOWN;
                }
                break;
            case "btnBrowse":
                if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                    if (ShowMessageFX.YesNo(null, "Search Vehicle Sales Accessories Request Information Confirmation", "Are you sure you want to browse a new record?")) {
                    } else {
                        return;
                    }
                }
                loJSON = oTransVSPRequest.searchTransaction("", false, true);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadMasterFields();
                    loadMasterTable();
                    pnEditMode = oTransVSPRequest.getEditMode();
                    initFields(pnEditMode);
                } else {
                    ShowMessageFX.Warning(null, "Search Vehicle Sales Accessories Request Information Confirmation", (String) loJSON.get("message"));
                }
                break;
            case "btnEdit":
                loJSON = oTransVSPRequest.updateTransaction();
                pnEditMode = oTransVSPRequest.getEditMode();
                if ("error".equals((String) loJSON.get("result"))) {
                    ShowMessageFX.Warning(null, "Warning", (String) loJSON.get("message"));
                }
                break;
            case "btnSave":
                if (ShowMessageFX.YesNo(null, "VSP Parts Request Saving....", "Are you sure, do you want to save?")) {
                    boolean lbIsEmpty = false;
                    for (int lnCtr = 0; lnCtr <= oTransVSPRequest.getVSPPartsList().size() - 1; lnCtr++) {
                        if (!oTransVSPRequest.getVSPPartsModel().getDetailModel(lnCtr).getStockID().isEmpty()) {
                            lbIsEmpty = true;
                            break;
                        }
                    }

                    if (!lbIsEmpty) {
                        ShowMessageFX.Warning(null, pxeModuleName, "No Accessories Number detected, Please select or enter value in any row.");
                        return;
                    }
                    loJSON = oTransVSPRequest.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "VSP Parts Request Information", (String) loJSON.get("message"));
                        loJSON = oTransVSPRequest.openTransaction(oTransVSPRequest.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadMasterFields();
                            loadMasterTable();
                            pnEditMode = oTransVSPRequest.getEditMode();
                            initFields(pnEditMode);
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
                    }
                } else {
                    return;
                }
                break;
            case "btnClose":
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to close this form?")) {
                    CommonUtils.closeStage(btnClose);
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                break;
        }
        initFields(pnEditMode);

    }

    private void clearTables() {
        accessoriesData.clear();
    }

    private void clearFields() {
        CustomCommonUtil.setText("", txtField01, txtField02, txtField03, txtField04,
                txtField06, txtField07);
        textArea05.setText("");
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.UPDATE);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        CustomCommonUtil.setVisible(lbShow, btnCancel, btnSave);
        CustomCommonUtil.setManaged(lbShow, btnCancel, btnSave);
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }
    }

    private void loadAccessoriesWindowDialog(Integer fnRow, boolean fbIsAdd) throws IOException {
        /**
         * if state = true : ADD else if state = false : UPDATE *
         */
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/sales/VSPAccessories.fxml"));
            VSPAccessoriesController loControl = new VSPAccessoriesController();
            loControl.setGRider(oApp);
            loControl.setObject(oTransVSPRequest);
            loControl.setState(fbIsAdd);
            fxmlLoader.setController(loControl);
            loControl.setRequest(true);
            loControl.setRow(fnRow);
            if (!oTransVSPRequest.getVSPPartsModel().getVSPParts(fnRow).getDescript().isEmpty()) {
                loControl.setOrigDsc(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(fnRow).getDescript()));
            }
            loControl.setStockID(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(fnRow).getStockID()));
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
//            JSONObject loJSON = new JSONObject();
//            loJSON = oTransVSPRequest.saveTransaction();
//            if ("success".equals((String) loJSON.get("result"))) {
//                ShowMessageFX.Information(null, pxeModuleName, "Successfully updated parts request information!");
//                loJSON = oTransVSPRequest.openTransaction(oTransVSPRequest.getMasterModel().getMasterModel().getTransNo());
//                if ("success".equals((String) loJSON.get("result"))) {
            loadMasterFields();
            loadMasterTable();
//                    pnEditMode = EditMode.UPDATE;
//                    initFields(pnEditMode);
//                } else {
//                    ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                }
//            } else {
//                ShowMessageFX.Warning(null, pxeModuleName, (String) loJSON.get("message"));
//                return;
//            }
        } catch (IOException e) {
            ShowMessageFX.Warning(null, "Warning", e.getMessage());
            System.exit(1);
        }

    }

    @FXML
    private void tblAccessories_Click(MouseEvent event) {
        if (pnEditMode == EditMode.UPDATE) {
            pnRow = tblViewAccessories.getSelectionModel().getSelectedIndex();
            if (pnRow < 0 || pnRow >= tblViewAccessories.getItems().size()) {
                ShowMessageFX.Warning(null, "Warning", "Please select valid accessories information.");
                return;
            }
            if (event.getClickCount() == 2) {
                try {
                    loadAccessoriesWindowDialog(pnRow, false);
                    loadMasterTable();

                } catch (IOException ex) {
                    Logger.getLogger(VSPAccessoriesRequestController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

}
