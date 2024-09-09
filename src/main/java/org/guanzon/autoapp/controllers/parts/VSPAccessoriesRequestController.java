/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.parts;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.IOException;
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
import org.guanzon.autoapp.models.sales.VSPPart;
import org.guanzon.autoapp.utils.CustomCommonUtil;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author AutoGroup Programmers
 */
public class VSPAccessoriesRequestController implements Initializable, ScreenInterface {

    private GRider oApp;
    private VehicleSalesProposal oTransVSPRequest;
    private String pxeModuleName = "VSP Accessories Request";
    DecimalFormat poGetDecimalFormat = new DecimalFormat("#,##0.00");
    private ObservableList<VSPPart> accessoriesData = FXCollections.observableArrayList();
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
    private TableView<VSPPart> tblViewAccessories;
    @FXML
    private TableColumn<VSPPart, String> tblindex01, tblindex02, tblindex03, tblindex04, tblindex05, tblindex06;

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
        initAccessoriesTable();
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
                    clearAccessoriesFields();
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
                loJSON = oTransVSPRequest.searchTransaction("", false);
                if ("success".equals((String) loJSON.get("result"))) {
                    loadAccessoriesFields();
                    loadAccessoriesTable();
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
                    loJSON = oTransVSPRequest.saveTransaction();
                    if ("success".equals((String) loJSON.get("result"))) {
                        ShowMessageFX.Information(null, "VSP Parts Request Information", (String) loJSON.get("message"));
                        loJSON = oTransVSPRequest.openTransaction(oTransVSPRequest.getMasterModel().getMasterModel().getTransNo());
                        if ("success".equals((String) loJSON.get("result"))) {
                            loadAccessoriesFields();
                            loadAccessoriesTable();
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
                CommonUtils.closeStage(btnClose);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Please notify the system administrator to configure the null value at the close button.");
                break;
        }
        initFields(pnEditMode);

    }

    private void loadAccessoriesFields() {
        txtField01.setText(oTransVSPRequest.getMasterModel().getMasterModel().getBuyCltNm());
        txtField02.setText(oTransVSPRequest.getMasterModel().getMasterModel().getCoCltNm());
        txtField03.setText(oTransVSPRequest.getMasterModel().getMasterModel().getSEName());
        txtField04.setText(oTransVSPRequest.getMasterModel().getMasterModel().getAgentNm());
        textArea05.setText(oTransVSPRequest.getMasterModel().getMasterModel().getVhclDesc());
        txtField06.setText(oTransVSPRequest.getMasterModel().getMasterModel().getCSNo());
        txtField07.setText(oTransVSPRequest.getMasterModel().getMasterModel().getPlateNo());
    }

    private void loadAccessoriesTable() {
        accessoriesData.clear();
        boolean lbChargeType = false;
        String lsGrsAmount = "";
        String lsDiscAmount = "";
        String lsNetAmount = "";
        String lsQuantity = "";
        String lsPartsDesc = "";
        double totalAmount = 0.00;
        for (int lnCtr = 0; lnCtr <= oTransVSPRequest.getVSPPartsList().size() - 1; lnCtr++) {
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity() != null) {
                lsQuantity = String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity());
            }
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice() != null) {
                lsGrsAmount = poGetDecimalFormat.format(Double.parseDouble(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getSelPrice())));
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
            totalAmount = Integer.parseInt(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getQuantity())) * Double.parseDouble(String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getNtPrtAmt()));
            String lsTotalAmount = poGetDecimalFormat.format(totalAmount);
            if (oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc() != null) {
                lsPartsDesc = String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getPartDesc());
            }
            accessoriesData.add(new VSPPart(
                    String.valueOf(lnCtr + 1),
                    String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getTransNo()),
                    String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getStockID()),
                    String.valueOf(oTransVSPRequest.getVSPPartsModel().getVSPParts(lnCtr).getDescript()),
                    lsQuantity,
                    lsGrsAmount,
                    lsDiscAmount,
                    lsNetAmount,
                    "",
                    "",
                    "",
                    lsPartsDesc,
                    lsTotalAmount,
                    lbChargeType
            ));
            lbChargeType = false;
        }
        tblViewAccessories.setItems(accessoriesData);
    }

    private void initAccessoriesTable() {
        if (pnEditMode == EditMode.UPDATE) {
            tblViewAccessories.setEditable(true);
        } else {
            tblViewAccessories.setEditable(false);
        }
        tblindex01.setCellValueFactory(new PropertyValueFactory<>("tblindex01_part")); // tblindexRow
        tblindex02.setCellValueFactory(new PropertyValueFactory<>("tblindex04_part")); // sales accessories description
        tblindex03.setCellValueFactory(new PropertyValueFactory<>("tblindex03_part")); // accessories number
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
            loadAccessoriesFields();
            loadAccessoriesTable();
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

    private void clearAccessoriesFields() {
        txtField01.setText("");
        txtField02.setText("");
        txtField04.setText("");
        txtField03.setText("");
        txtField06.setText("");
        txtField07.setText("");
        textArea05.setText("");
    }

    private void clearTables() {
        accessoriesData.clear();
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
                    loadAccessoriesTable();

                } catch (IOException ex) {
                    Logger.getLogger(VSPAccessoriesRequestController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    private void initFields(int fnValue) {
        boolean lbShow = (fnValue == EditMode.UPDATE);
        btnEdit.setVisible(false);
        btnEdit.setManaged(false);
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);
        btnSave.setVisible(lbShow);
        btnSave.setManaged(lbShow);
        if (fnValue == EditMode.READY) {
            btnEdit.setVisible(true);
            btnEdit.setManaged(true);
        }

    }
}
