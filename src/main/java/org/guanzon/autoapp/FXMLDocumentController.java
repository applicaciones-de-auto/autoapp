/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package org.guanzon.autoapp;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.utils.ScreenInterface;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.autoapp.controllers.general.ActivityApprovalController;
import org.guanzon.autoapp.controllers.general.ActivityInformationController;
import org.guanzon.autoapp.controllers.general.CustomerController;
import org.guanzon.autoapp.controllers.general.CustomerVehicleInfoController;
import org.guanzon.autoapp.controllers.general.ReferralAgentController;
import org.guanzon.autoapp.controllers.general.SalesExecutiveController;
import org.guanzon.autoapp.controllers.parameters.ActivitySourceTypeController;
import org.guanzon.autoapp.controllers.parameters.BankController;
import org.guanzon.autoapp.controllers.parameters.BankBranchInformationController;
import org.guanzon.autoapp.controllers.parameters.BinController;
import org.guanzon.autoapp.controllers.parameters.BrandController;
import org.guanzon.autoapp.controllers.parameters.CategoryController;
import org.guanzon.autoapp.controllers.parameters.InsuranceBranchInformationController;
import org.guanzon.autoapp.controllers.parameters.InsuranceCompanyController;
import org.guanzon.autoapp.controllers.parameters.InvTypeController;
import org.guanzon.autoapp.controllers.parameters.ItemLocationController;
import org.guanzon.autoapp.controllers.parameters.MeasurementController;
import org.guanzon.autoapp.controllers.parameters.SectionController;
import org.guanzon.autoapp.controllers.parameters.VehicleColorController;
import org.guanzon.autoapp.controllers.parameters.VehicleDescriptionController;
import org.guanzon.autoapp.controllers.parameters.VehicleEngineFormatController;
import org.guanzon.autoapp.controllers.parameters.VehicleFrameFormatController;
import org.guanzon.autoapp.controllers.parameters.VehicleMakeController;
import org.guanzon.autoapp.controllers.parameters.VehicleModelController;
import org.guanzon.autoapp.controllers.parameters.VehicleTypeController;
import org.guanzon.autoapp.controllers.parameters.WareHouseController;
import org.guanzon.autoapp.controllers.parts.ItemInformationController;
import org.guanzon.autoapp.controllers.parts.VSPAccessoriesRequestController;
import org.guanzon.autoapp.controllers.sales.SalesJobOrderController;
import org.guanzon.autoapp.controllers.sales.VSPController;
import org.guanzon.autoapp.controllers.sales.VehicleInquiryController;
import org.guanzon.autoapp.utils.UnloadForm;

/**
 *
 * @author xurpas
 */
public class FXMLDocumentController implements Initializable, ScreenInterface {

    private GRider oApp;
    private int targetTabIndex = -1;
    private double tabsize;
    private String sSalesInvoiceType = "";
    private String sVehicleInfoType = "";
    private String sJobOrderType = "";
    private String sSalesInfoType = "";
    private final String psGeneralPath = "/org/guanzon/autoapp/views/general/";
    private final String psParameterPath = "/org/guanzon/autoapp/views/parameters/";
    private final String psInsurancePath = "/org/guanzon/autoapp/views/insurance/";
    private final String psPartsPath = "/org/guanzon/autoapp/views/parts/";
    private final String psSalesPath = "/org/guanzon/autoapp/views/sales/";
    private final String psServicePath = "/org/guanzon/autoapp/views/service/";
    // Variables to track the window movement
    private double xOffset = 0;
    private double yOffset = 0;
    FXMLMenuParameterForm param = new FXMLMenuParameterForm();
    List<String> tabName = new ArrayList<>();
    @FXML
    private Label AppUser;
    @FXML
    private Pane view;
    @FXML
    private Label DateAndTime;
    @FXML
    public StackPane workingSpace;
    @FXML
    private Pane btnClose;
    @FXML
    private Pane btnMin;
    @FXML
    private MenuItem mnuSupplierInfo;
    @FXML
    private TabPane tabpane;
    @FXML
    private MenuItem mnuCustomerInfo;
    @FXML
    private MenuItem mnuVhclDesc;
    @FXML
    private Menu menusales;
    private MenuItem mnuVhclInquiry;
    @FXML
    private MenuItem mnuSalesAgent;
    @FXML
    private MenuItem mnuUnitRecv;
    @FXML
    private MenuItem mnuVhclEntry;
    @FXML
    private MenuItem mnuBank;
    @FXML
    private MenuItem mnuVhclRsrvApp;
    @FXML
    private MenuItem mnuActivity;
    @FXML
    private MenuItem mnuActivityApproval;
    @FXML
    private MenuItem mnuActType;
    @FXML
    private MenuItem mnuItemEntry;
    @FXML
    private MenuItem mnuBinEntry;
    @FXML
    private MenuItem mnuInvLocEntry;
    @FXML
    private MenuItem mnuMeasureEntry;
    @FXML
    private MenuItem mnuSectionEntry;
    @FXML
    private MenuItem mnuWarehsEntry;
    @FXML
    private MenuItem mnuVhclMakeEntry;
    @FXML
    private MenuItem mnuVhclModelEntry;
    @FXML
    private MenuItem mnuVhclTypeEntry;
    @FXML
    private MenuItem mnuVhclColorEntry;
    @FXML
    private MenuItem mnuBrandEntry;
    @FXML
    private MenuItem mnuCategoryEntry;
    @FXML
    private MenuItem mnuInvTypeEntry;
    @FXML
    private MenuItem mnuUnitDeliveryReceipt;
    @FXML
    private MenuItem mnuAckReceipt;
    @FXML
    private MenuItem mnuBillingStmt;
    @FXML
    private MenuItem mnuColReceipt;
    @FXML
    private MenuItem mnuOfcReceipt;
    @FXML
    private MenuItem mnuPartsSalesInv;
    @FXML
    private MenuItem mnuVSPEntry;
    @FXML
    private MenuItem mnuVhclSalesInv;
    @FXML
    private MenuItem mnuCustVhclInfo;
    @FXML
    private MenuItem mnuSalesJobOrder;
    @FXML
    private MenuItem mnuSalesPartsRequest;
    @FXML
    private MenuItem mnuServiceJobOrder;
    @FXML
    private MenuItem mnuAddOnsApproval;
    @FXML
    private MenuItem mnuSalesExecutive;
    @FXML
    private MenuItem mnuInsurInfo;
    @FXML
    private Menu menusales1;
    @FXML
    private MenuItem mnuInquiry;
    @FXML
    private MenuItem mnuVhclEngEntry;
    @FXML
    private MenuItem mnuVhclFrmEntry;
    @FXML
    private MenuItem mnuBankInfo;
    @FXML
    private MenuItem mnuInsurInfo1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Load Main Frame
        setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        if (oApp != null) {
            loadUserInfo();
            //initMenu();
            getTime();
            setTabPane();
//            //
//            List<String> tabs = new ArrayList<>();
//            tabs = TabsStateManager.loadCurrentTab();
//            if (tabs.size() > 0) {
//                if (ShowMessageFX.YesNo(null, "Integrated Automotive System Application", "You want to restore unclosed tabs?") == true) {
//                    for (String tabName : tabs) {
//                        triggerMenu(tabName);
//                    }
//                } else {
//                    for (String tabName : tabs) {
//                        TabsStateManager.closeTab(tabName);
//                    }
//                    TabsStateManager.saveCurrentTab(new ArrayList<>());
//                    return;
//                }
//            }
        } else {
            System.out.println("GRider object is not properly initialized.");
        }
    }

    private void loadUserInfo() {
        ResultSet name;
        String lsQuery = "SELECT b.sCompnyNm "
                + " FROM xxxSysUser a"
                + " LEFT JOIN GGC_ISysDBF.Client_Master b"
                + " ON a.sEmployNo  = b.sClientID"
                + " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
        name = oApp.executeQuery(lsQuery);
        try {
            if (name.next()) {
                AppUser.setText(name.getString("sCompnyNm") + " || " + oApp.getBranchName());
                System.setProperty("user.name", name.getString("sCompnyNm"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setTabPane() {
        // set up the drag and drop listeners on the tab pane
        tabpane.setOnDragDetected(event -> {
            Dragboard db = tabpane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tabpane.getSelectionModel().getSelectedItem().getText());
            db.setContent(content);
            event.consume();
        });

        tabpane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        tabpane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String tabText = db.getString();
                int draggedTabIndex = findTabIndex(tabText);
                //double mouseP , mousePCom;
                double mouseX = event.getX();
                double mouseY = event.getY();
                Bounds headerBounds = tabpane.lookup(".tab-header-area").getBoundsInParent();
                Point2D mouseInScene = tabpane.localToScene(mouseX, mouseY);
                Point2D mouseInHeader = tabpane.sceneToLocal(mouseInScene);
                double tabHeaderHeight = tabpane.lookup(".tab-header-area").getBoundsInParent().getHeight();
                System.out.println("mouseY " + mouseY);
                System.out.println("tabHeaderHeight " + tabHeaderHeight);

//                    mouse is over the tab header area
//                    mouseP = ((mouseInHeader.getX() / headerBounds.getWidth()));
//                    tabsize = tabpane.getTabs().size();
//                    mousePCom = mouseP * tabsize;
//                    targetTabIndex = (int) Math.round(mousePCom) ;
//
//                    double tabWidth = headerBounds.getWidth() / tabpane.getTabs().size();
//                    targetTabIndex = (int) ((mouseX - headerBounds.getMinX()) / tabWidth);
                targetTabIndex = (int) (mouseX / 180);
                System.out.println("targetTabIndex " + targetTabIndex);
                if (mouseY < tabHeaderHeight) {
                    //if (headerBounds.contains(mouseInHeader)) {
                    System.out.println("mouseInHeader.getX() " + mouseInHeader.getX());
                    System.out.println("headerBounds.getWidth() " + headerBounds.getWidth());
                    System.out.println("tabsize " + tabpane.getTabs().size());
                    System.out.println("tabText " + tabText);
                    System.out.println("draggedTabIndex " + draggedTabIndex);

                    if (draggedTabIndex != targetTabIndex) {
                        Tab draggedTab = tabpane.getTabs().remove(draggedTabIndex);
                        if (targetTabIndex > tabpane.getTabs().size()) {
                            targetTabIndex = tabpane.getTabs().size();
                        }
                        tabpane.getTabs().add(targetTabIndex, draggedTab);
                        tabpane.getSelectionModel().select(draggedTab);
                        success = true;

                    }
                    //}
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
        tabpane.setOnDragDone(event -> {
            event.consume();
        });

    }

    public ContextMenu createContextMenu(TabPane tabPane, Tab tab, GRider oApp) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem closeTabItem = new MenuItem("Close Tab");
        MenuItem closeOtherTabsItem = new MenuItem("Close Other Tabs");
        MenuItem closeAllTabsItem = new MenuItem("Close All Tabs");

        closeTabItem.setOnAction(event -> closeSelectTabs(tabPane, tab));
        closeOtherTabsItem.setOnAction(event -> closeOtherTabs(tabPane, tab));
        closeAllTabsItem.setOnAction(event -> closeAllTabs(tabPane, oApp));

        contextMenu.getItems().add(closeTabItem);
        contextMenu.getItems().add(closeOtherTabsItem);
        contextMenu.getItems().add(closeAllTabsItem);

        tab.setContextMenu(contextMenu);

        closeOtherTabsItem.visibleProperty().bind(Bindings.size(tabPane.getTabs()).greaterThan(1));

        return contextMenu;
    }

    private void closeSelectTabs(TabPane tabPane, Tab tab) {
        if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?")) {
            Tabclose(tabPane);
            tabName.remove(tab.getText());
//            TabsStateManager.saveCurrentTab(tabName);
//            TabsStateManager.closeTab(tab.getText());
            tabPane.getTabs().remove(tab);
        }

    }

    private void closeOtherTabs(TabPane tabPane, Tab currentTab) {
        if (ShowMessageFX.YesNo(null, "Close Other Tab", "Are you sure, do you want to close other tab?")) {
            tabPane.getTabs().removeIf(tab -> tab != currentTab);
            List<String> currentTabNameList = Collections.singletonList(currentTab.getText());
            tabName.retainAll(currentTabNameList);
//            TabsStateManager.saveCurrentTab(tabName);
            for (Tab tab : tabPane.getTabs()) {
                String formName = tab.getText();
//                TabsStateManager.closeTab(formName);
            }
        }
    }

    private void closeAllTabs(TabPane tabPane, GRider oApp) {
        if (ShowMessageFX.YesNo(null, "Close All Tabs", "Are you sure, do you want to close all tabs?")) {
            tabName.clear();
//            TabsStateManager.saveCurrentTab(tabName);
            // Close all tabs using your TabsStateManager
            for (Tab tab : tabPane.getTabs()) {
                String formName = tab.getText();
//                TabsStateManager.closeTab(formName);
            }
            tabPane.getTabs().clear();
            UnloadForm unload = new UnloadForm();
            StackPane myBox = (StackPane) tabpane.getParent();
            myBox.getChildren().clear();
            myBox.getChildren().add(unload.getScene("FXMLMainScreen.fxml", oApp));

        }
    }

    private void triggerMenu(String sFormName) {
        switch (sFormName) {
            /*DIRECTORY*/
            case "Activity Information":
                mnuActivity.fire();
                break;
            case "Activity Approval":
                mnuActivityApproval.fire();
                break;
            case "Customer":
                mnuCustomerInfo.fire();
                break;
            case "Customer Vehicle Information":
                mnuCustVhclInfo.fire();
                break;
            case "Vehicle Sales Information":
                mnuVhclEntry.fire();
                break;
//            case "Supplier":
//                break;
            /*SALES*/
            case "Referral Agent":
                mnuSalesAgent.fire();
            case "Sales Executive":
                mnuSalesExecutive.fire();
                break;
            case "Vehicle Description":
                mnuVhclDesc.fire();
                break;
            case "Vehicle Receiving":

                break;
            case "Vehicle Inquiry":
                mnuVhclInquiry.fire();
                break;
            case "Vehicle Reservation Approval":
                mnuVhclRsrvApp.fire();
                break;
            case "Vehicle Delivery Receipt":
                mnuUnitDeliveryReceipt.fire();
                break;
            case "Vehicle Sales Proposal":
                mnuVSPEntry.fire();
                break;
            case "VSP Approval":
                mnuAddOnsApproval.fire();
                break;
            case "Sales Job Order":
                mnuSalesJobOrder.fire();
                break;
            /*ACCOUNTING*/
            case "Bank":
                mnuBank.fire();
                break;
            /*CASHIERING*/
            case "Acknowledgement Receipt":
                mnuAckReceipt.fire();
                break;
            case "Billing Statement":
                mnuBillingStmt.fire();
                break;
            case "Collection Receipt":
                mnuColReceipt.fire();
                break;
            case "Official Receipt":
                mnuOfcReceipt.fire();
                break;
            case "Parts Sales Invoice":
                mnuPartsSalesInv.fire();
                break;
            case "Vehicle Sales Invoice":
                mnuVhclSalesInv.fire();
                break;
            /*PARTS*/
            case "Item Information":
                mnuItemEntry.fire();
                break;
//            case "Parts Requisition":
//                mnuPartsRequisition.fire();
//                break;
            case "Vehicle Sales Parts Request":
                mnuSalesPartsRequest.fire();
                break;
            /*SERVICE*/
            case "Service Job Order":
                mnuServiceJobOrder.fire();
                break;
            /*INSURANCE*/
            case "Insurance":
                mnuInsurInfo.fire();
                break;
        }

    }

    private int findTabIndex(String tabText) {
        ObservableList<Tab> tabs = tabpane.getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getText().equals(tabText)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    /*LOAD ANIMATE FOR ANCHORPANE MAIN HOME*/
    public AnchorPane loadAnimateAnchor(String fsFormName) {
        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        AnchorPane root;
        try {
            root = (AnchorPane) fxmlLoader.load();
            FadeTransition ft = new FadeTransition(Duration.millis(1500));
            ft.setNode(root);
            ft.setFromValue(1);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();
            return root;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    /*SET SCENE FOR WORKPLACE - STACKPANE - ANCHORPANE*/
    public void setScene(AnchorPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    /*LOAD ANIMATE FOR TABPANE*/
    public TabPane loadAnimate(String fsFormName) {
        //set fxml controller class
        if (tabpane.getTabs().size() == 0) {
            tabpane = new TabPane();
        }

        setTabPane();

        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        //Add new tab;
        Tab newTab = new Tab(SetTabTitle(fsFormName));
        newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-font-family: arial;");
        //tabIds.add(fsFormName);
        newTab.setContent(new javafx.scene.control.Label("Content of Tab " + fsFormName));
        newTab.setContextMenu(createContextMenu(tabpane, newTab, oApp));
        // Attach a context menu to each tab
        tabName.add(SetTabTitle(fsFormName));

        // Save the list of tab IDs to the JSON file
//        TabsStateManager.saveCurrentTab(tabName);
        try {
            Node content = fxmlLoader.load();
            newTab.setContent(content);
            tabpane.getTabs().add(newTab);
            tabpane.getSelectionModel().select(newTab);
            //newTab.setOnClosed(event -> {
            newTab.setOnCloseRequest(event -> {
                if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {
                    Tabclose();
                    //tabIds.remove(newTab.getText());
                    tabName.remove(newTab.getText());
                    // Save the list of tab IDs to the JSON file
//                    TabsStateManager.saveCurrentTab(tabName);
//                    TabsStateManager.closeTab(newTab.getText());
                } else {
                    // Cancel the close request
                    event.consume();
                }

            });

            newTab.setOnSelectionChanged(event -> {
                ObservableList<Tab> tabs = tabpane.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getText().equals(newTab.getText())) {
                        tabName.remove(newTab.getText());
                        tabName.add(newTab.getText());
                        // Save the list of tab IDs to the JSON file
//                        TabsStateManager.saveCurrentTab(tabName);
                        break;
                    }
                }

            });
            return (TabPane) tabpane;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ScreenInterface getController(String fsValue) {
        switch (fsValue) {
            case "FXMLMainScreen.fxml":
                return new FXMLMainScreenController();
//            /*DIRECTORY*/
            case psGeneralPath + "ActivityInformation.fxml":
                return new ActivityInformationController();
            case psGeneralPath + "ActivityApproval.fxml":
                return new ActivityApprovalController();
            case "ActivitySourceType.fxml":
                return new ActivitySourceTypeController();
            case psGeneralPath + "Customer.fxml":
                return new CustomerController();
            case psGeneralPath + "CustomerVehicleInfo.fxml":
                return new CustomerVehicleInfoController();
            case "SalesExecutive.fxml":
                return new SalesExecutiveController();
            case psGeneralPath + "ReferralAgent.fxml":
                return new ReferralAgentController();
////               case "SupplierInfo.fxml":
////                    return new SupplierInfoController();
//            /*SALES*/
            case "VehicleDescription.fxml":
                return new VehicleDescriptionController();
            case "VehicleMake.fxml":
                return new VehicleMakeController();
            case "VehicleModel.fxml":
                return new VehicleModelController();
            case "VehicleType.fxml":
                return new VehicleTypeController();
            case "VehicleColor.fxml":
                return new VehicleColorController();
            case "VehicleFrameFormat.fxml":
                return new VehicleFrameFormatController();
            case "VehicleEngineFormat.fxml":
                return new VehicleEngineFormatController();
            case psSalesPath + "SalesJobOrder.fxml":
                return new SalesJobOrderController();
//            case "VehicleEntryForm.fxml":
//                return new VehicleEntryFormController();
//            case "UnitReceivingForm.fxml":
//                return new UnitReceivingFormController();
            case psSalesPath + "VehicleInquiry.fxml":
                return new VehicleInquiryController();
//            case "VehicleSalesApproval.fxml":
//                return new VehicleSalesApprovalController();
            case "Bank.fxml":
                return new BankController();
            case "BankBranchInformation.fxml":
                return new BankBranchInformationController();
            case "InsuranceCompany.fxml":
                return new InsuranceCompanyController();
            case "InsuranceBranchInformation.fxml":
                return new InsuranceBranchInformationController();
//            case "UnitDeliveryReceiptForm.fxml":
//                return new UnitDeliveryReceiptFormController();
            case psSalesPath + "VSP.fxml":
                return new VSPController();
//            case "VSPAddOnsApproval.fxml":
//                return new VSPAddOnsApprovalController();
//
//            /*PARTS*/
            case psPartsPath + "ItemInformation.fxml":
                return new ItemInformationController();
//            case "PartsRequisitionForm.fxml":
////                return new PartsRequisitionFormController();
            case "VSPAccessoriesRequest.fxml":
                return new VSPAccessoriesRequestController();
//
//            /*PARAMETERS*/
            case "ItemLocation.fxml":
                return new ItemLocationController();
            case "Bin.fxml":
                return new BinController();
            case "Section.fxml":
                return new SectionController();
            case "WareHouse.fxml":
                return new WareHouseController();
            case "Category.fxml":
                return new CategoryController();
            case "InvType.fxml":
                return new InvTypeController();
            case "Measurement.fxml":
                return new MeasurementController();
            case "Brand.fxml":
                return new BrandController();
//            case "Invoice.fxml":
//                return new InvoiceController();
//            case "VehicleSalesInvoice.fxml":
//                return new VehicleSalesInvoiceController();
//            /*SERVICE*/
//            case "JobOrder.fxml":
//                return new JobOrderController();
//            /*SERVICE*/
//            case "InsuranceInformation_1.fxml":
//                return new InsuranceInformation_1Controller();

            default:
                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Screen Interface for " + fsValue);
                return null;
        }
    }

    public String SetTabTitle(String menuaction) {
        switch (menuaction) {
            /*DIRECTORY*/
            case psGeneralPath + "ActivityInformation.fxml":
                return "Activity Information";
            case psGeneralPath + "ActivityApproval.fxml":
                return "Activity Approval";
            case psGeneralPath + "Customer.fxml":
                return "Customer";
            case "SalesExecutive.fxml":
                return "Sales Executive Information";
            case psGeneralPath + "ReferralAgent.fxml":
                return "Referral Agent Information";
            case psGeneralPath + "CustomerVehicleInfo.fxml":
//                if (sVehicleInfoType.isEmpty()) {
//                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
//                    return null;
//                }
                return sVehicleInfoType;
            case psGeneralPath + "VehicleInformation.fxml":
//                if (sVehicleInfoType.isEmpty()) {
//                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
//                    return null;
//                }
                return sVehicleInfoType;
            //return "Customer Vehicle Information";
            case "SupplierInfo.fxml":
                return "Supplier";
            /*SALES*/
//            case "Vehicle.fxml":
//                return "Vehicle Information";
            case "UnitReceiving.fxml":
                return "Unit Receiving";
            case psSalesPath + "VehicleInquiry.fxml":
                return "Vehicle Inquiry";
            case "VehicleSalesApproval.fxml":
                return "Vehicle Reservation Approval";
            case "UnitDeliveryReceipt.fxml":
                return "Unit Delivery Receipt";
            case psSalesPath + "VSP.fxml":
                return "Vehicle Sales Proposal";
            case "VSPApproval.fxml":
                return "VSP Approval";
            case psSalesPath + "SalesJobOrder.fxml":
                return "Sales Job Order Information";
            /*CASHIERING*/
            case "Invoice.fxml":
                if (sSalesInvoiceType.isEmpty()) {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                    return null;
                }
                return sSalesInvoiceType;
            case "VehicleSalesInvoice.fxml":
                return "Vehicle Sales Invoice";
            /*PARTS*/
            case psPartsPath + "ItemInformation.fxml":
                return "Item Information";
            case "PartsRequisition.fxml":
                return "Parts Requisition";
            case psPartsPath + "VSPAccessoriesRequest.fxml":
                return "Vehicle Sales Parts Request";
            /**/
            case "JobOrder.fxml":
                if (sJobOrderType.isEmpty()) {
                    ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                    return null;
                }
                return sJobOrderType;
            case "InsuranceInformation_1.fxml":
                return "Insurance";
            default:
                ShowMessageFX.Warning(null, "Warning", "Notify System Admin to Configure Tab Title for " + menuaction);
                return null;
        }
    }

    //Load Main Screen if no tab remain
    public void Tabclose() {
        int tabsize = tabpane.getTabs().size();
        if (tabsize == 1) {
            setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        }
    }

    public void Tabclose(TabPane tabpane) {
        int tabsize = tabpane.getTabs().size();
        if (tabsize == 1) {
            setScene(loadAnimateAnchor("FXMLMainScreen.fxml"));
        }
    }

    /*SET SCENE FOR WORKPLACE - STACKPANE - TABPANE*/
    public void setScene2(TabPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    /*Check opened tab*/
    public int checktabs(String tabtitle) {
        for (Tab tab : tabpane.getTabs()) {
            if (tab.getText().equals(tabtitle)) {
                tabpane.getSelectionModel().select(tab);
                return 0;
            }
        }
        return 1;
    }

    public TabPane getTabPane() {
        //return (TabPane) workingSpace.getChildren().add(tabpane);
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add((TabPane) tabpane);
        //return (TabPane) workingSpace.getChildren().get(0);
        return (TabPane) workingSpace.lookup("#tabpane");
    }

    public MenuItem getMenuItem() {
        return mnuVhclDesc;
    }

    public Menu getMenu() {
        return menusales;
    }

    public StackPane getStactPane() {
        return workingSpace;
    }

    /*MENU ACTIONS OPENING FXML's*/
    @FXML
    private void mnuCustomerInfoClick(ActionEvent event) {
        String sformname = psGeneralPath + "Customer.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSupplierInfoClick(ActionEvent event) {
        String sformname = "SupplierInfo.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*SALES*/
    @FXML
    private void mnuVhclSalesEntryClick(ActionEvent event) {
        sVehicleInfoType = "Vehicle Sales Information";
        String sformname = psGeneralPath + "CustomerVehicleInfo.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesJobOrderClick(ActionEvent event) {
        sJobOrderType = "Sales Job Order";
        String sformname = psSalesPath + "SalesJobOrder.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesAgentClick(ActionEvent event) {
        String sformname = psGeneralPath + "ReferralAgent.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesExecutiveClick(ActionEvent event) {
        String sformname = "SalesExecutive.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psGeneralPath);
    }

    @FXML
    private void mnuUnitRecvClick(ActionEvent event) {
        String sformname = "UnitReceiving.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVhclRsrvAppClick(ActionEvent event) {
        String sformname = "VehicleSalesApproval.fxml";

        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuInquiryClick(ActionEvent event) {
        String sformname = psSalesPath + "VehicleInquiry.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuUnitDeliveryReceiptClick(ActionEvent event) {
        String sformname = "UnitDeliveryReceipt.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVSPEntryClick(ActionEvent event) {
        String sformname = psSalesPath + "VSP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuCustVhclInfoClick(ActionEvent event) {
        sVehicleInfoType = "Customer Vehicle Information";
        String sformname = psGeneralPath + "CustomerVehicleInfo.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    private void mnuVhclEntryClick(ActionEvent event) {
        sVehicleInfoType = "Vehicle Sales Information";
        String sformname = psGeneralPath + "CustomerVehicleInfo.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
//        String sformname = "VehicleEntryForm.fxml";
//        //check tab
//        if (checktabs(SetTabTitle(sformname)) == 1) {
//            setScene2(loadAnimate(sformname));
//        }
    }

    @FXML
    private void mnuAddOnsApprovalClick(ActionEvent event) {
        String sformname = "VSPApproval.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*VEHICLE DESCRIPTION AND PARAMETERS*/
    @FXML
    public void mnuVhclDescClick(ActionEvent event) {
        String sformname = "VehicleDescription.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuVhclMakeEntryClicked(ActionEvent event) {
        String sformname = "VehicleMake.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuVhclModelEntryClicked(ActionEvent event) {
        String sformname = "VehicleModel.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuVhclTypeEntryClicked(ActionEvent event) {
        String sformname = "VehicleType.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuVhclColorEntryClicked(ActionEvent event) {
        String sformname = "VehicleColor.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuVhclEngEntryClicked(ActionEvent event) {
        String sformname = "VehicleEngineFormat.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);

    }

    @FXML
    private void mnuVhclFrmEntryClicked(ActionEvent event) {
        String sformname = "VehicleFrameFormat.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);

    }


    /*Bank*/
    @FXML
    private void mnuBankClick(ActionEvent event) {
        String sformname = "Bank.fxml";
        //check tab
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuBankInfoClick(ActionEvent event) {
        String sformname = "BankBranchInformation.fxml";
        //check tab
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    /*CASHIERING*/
    @FXML
    private void mnuAckReceiptClick(ActionEvent event) {
        sSalesInvoiceType = "Acknowledgement Receipt";
        String sformname = "Invoice.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }

    }

    @FXML
    private void mnuBillingStmtClick(ActionEvent event) {
        sSalesInvoiceType = "Billing Statement";
        String sformname = "Invoice.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuColReceiptClick(ActionEvent event) {
        sSalesInvoiceType = "Collection Receipt";
        String sformname = "Invoice.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuOfcReceiptClick(ActionEvent event) {
        sSalesInvoiceType = "Official Receipt";
        String sformname = "Invoice.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuPartsSalesInvClick(ActionEvent event) {
        sSalesInvoiceType = "Parts Sales Invoice";
        String sformname = "Invoice.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuVhclSalesInvClick(ActionEvent event) {
        String sformname = "VehicleSalesInvoice.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*ACTIVITY*/
    @FXML
    private void mnuActivityClick(ActionEvent event) {
        String sformname = psGeneralPath + "ActivityInformation.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuActivityApprovClick(ActionEvent event) {
        String sformname = psGeneralPath + "ActivityApproval.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuActivityTypeClick(ActionEvent event) {
        String sformname = "ActivitySourceType.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);

    }

    /*PARTS*/
    @FXML
    private void mnuItemEntryClicked(ActionEvent event) {
        String sformname = psPartsPath + "ItemInformation.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    private void mnuPartsRequisitionClicked(ActionEvent event) {
        String sformname = "PartsRequisition.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuSalesPartsRequestClicked(ActionEvent event) {
        String sformname = "VSPAccessoriesRequest.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psPartsPath);
    }

    @FXML
    private void mnuBinEntryClicked(ActionEvent event) {
        String sformname = "Bin.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuInvLocEntryClicked(ActionEvent event) {
        String sformname = "ItemLocation.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuMeasureEntryClicked(ActionEvent event) {
        String sformname = "Measurement.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuSectionEntryClicked(ActionEvent event) {
        String sformname = "Section.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuWarehsEntryClicked(ActionEvent event) {
        String sformname = "WareHouse.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuBrandEntryClicked(ActionEvent event) {
        String sformname = "Brand.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuCategoryEntryClicked(ActionEvent event) {
        String sformname = "Category.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuInvTypeEntryClicked(ActionEvent event) {
        String sformname = "InvType.fxml";
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuInsurClick(ActionEvent event) {
        String sformname = "InsuranceCompany.fxml";
        //check tab
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    @FXML
    private void mnuInsurInfoClick(ActionEvent event) {
        String sformname = "InsuranceBranchInformation.fxml";
        //check tab
        param.FXMLMenuParameterForm(getController(sformname), oApp, sformname, psParameterPath);
    }

    /*Service*/
    @FXML
    private void mnuJobOrderClick(ActionEvent event) {
        sJobOrderType = "Service Job Order";
        String sformname = "JobOrder.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*Service*/
 /*SET CURRENT TIME*/
    private void getTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            String temp = "" + second;

            Date date = new Date();
            String strTimeFormat = "hh:mm:";
            String strDateFormat = "MMMM dd, yyyy";
            String secondFormat = "ss";

            DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

            String formattedTime = timeFormat.format(date);
            String formattedDate = dateFormat.format(date);

            DateAndTime.setText(formattedDate + " || " + formattedTime);

        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    private void handleButtonMinimizeClick(MouseEvent event) {
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleButtonCloseClick(MouseEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        //stage.close();
        //Close Stage
        event.consume();
        logout(stage);
    }
//
//    //close whole application

    public void logout(Stage stage) {
        if (ShowMessageFX.YesNo(null, "Integrated Automotive System", "Are you sure, do you want to close?") == true) {
//            if (tabName.size() > 0) {
//                for (String tabsName : tabName) {
//                    TabsStateManager.closeTab(tabsName);
//                }
//                TabsStateManager.saveCurrentTab(new ArrayList<>());
//            }

//            List<String> tabsName = new ArrayList<>();
//            tabsName = TabsStateManager.loadCurrentTab();
//            if (tabsName.size() > 0) {
//                for (String sTabName : tabsName) {
//                    TabsStateManager.closeTab(sTabName);
//                }
//                TabsStateManager.saveCurrentTab(new ArrayList<>());
//            }
            System.out.println("You successfully logged out!");
            stage.close();
        }
    }
//
//    /*USER ACCESS*/
//    private void initMenu() {
//
//        //Directory
//        mnuCustomerInfo.setVisible("015;027;042;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuCustVhclInfo.setVisible("015;027;042;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        //Parameters
//        mnuActType.setVisible("027;042;A012;042;A008;026".contains(oApp.getDepartment()));
//
//        mnuBinEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuBrandEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuCategoryEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuInvLocEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuInvTypeEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuMeasureEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuSectionEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuWarehsEntry.setVisible("010;026".contains(oApp.getDepartment()));
//
//        mnuVhclColorEntry.setVisible("027;042;A012;042;A008;026 ".contains(oApp.getDepartment()));
//        mnuVhclEngFrmEntry.setVisible("027;042;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVhclMakeEntry.setVisible("027;042;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVhclModelEntry.setVisible("027;042;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVhclTypeEntry.setVisible("027;042;A012;042;A008;026".contains(oApp.getDepartment()));
//        //Parts
//        mnuItemEntry.setVisible("010;026".contains(oApp.getDepartment()));
//        mnuSalesPartsRequest.setVisible("010;026".contains(oApp.getDepartment()));
//        //Service
//        mnuServiceJobOrder.setVisible("027;026".contains(oApp.getDepartment()));
//        //Vehicle Sales
//        mnuInquiry.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuSalesJobOrder.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuSalesAgent.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuSalesExecutive.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVhclDesc.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVhclEntry.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVhclRsrvApp.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuVSPEntry.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuAddOnsApproval.setVisible("015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        mnuUnitDeliveryReceipt.setVisible("0015;050;A011;A012;042;A008;026".contains(oApp.getDepartment()));
//        //Insurance
//        mnuInsurInfo.setVisible("A008;A006;026".contains(oApp.getDepartment()));
//        //Cashiering
//        mnuAckReceipt.setVisible("A008;026".contains(oApp.getDepartment()));
//        mnuBillingStmt.setVisible("A008;026".contains(oApp.getDepartment()));
//        mnuColReceipt.setVisible("A008;026".contains(oApp.getDepartment()));
//        mnuOfcReceipt.setVisible("A008;026".contains(oApp.getDepartment()));
//        mnuPartsSalesInv.setVisible("A008;026".contains(oApp.getDepartment()));
//        mnuVhclSalesInv.setVisible("A008;026".contains(oApp.getDepartment()));
//        //Bank Transasctions
//        mnuBank.setVisible("A008;026".contains(oApp.getDepartment()));
//    }

}
