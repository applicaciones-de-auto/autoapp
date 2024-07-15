/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.autoapp.controllers.general;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.autoapp.controllers.parameters.VehicleDescriptionEntryController;
import org.guanzon.autoapp.controllers.parameters.VehicleEngineFormatEntryController;
import org.guanzon.autoapp.controllers.parameters.VehicleFrameFormatEntryController;
import org.guanzon.autoapp.models.general.ModelVehicleOwnerHistory;
import org.guanzon.autoapp.models.general.ModelVehicleWarehouseHistory;
import org.guanzon.autoapp.utils.ScreenInterface;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerVehicleInfoFormController implements Initializable, ScreenInterface {

    private GRider oApp;
    private final String pxeModuleName = "Customer Vehicle Information"; //Form Title
    private int pnEditMode;
    private boolean bBtnVhclAvl = false;
    private int pnRow = -1;
    private int lnCtr;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean pbisVhclSales = false;
    private ObservableList<ModelVehicleOwnerHistory> vhclOwnerHistoryData = FXCollections.observableArrayList();
    private ObservableList<ModelVehicleWarehouseHistory> vhclWrhHistoryData = FXCollections.observableArrayList();
    ObservableList<String> cSoldStats = FXCollections.observableArrayList("NON SALES CUSTOMER", "AVAILABLE FOR SALE", "VSP", "SOLD");
    ObservableList<String> cIsVhclnew = FXCollections.observableArrayList("BRAND NEW", "PRE-OWNED");
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Button btnAdd, btnEdit, btnSave, btnCancel, btnBrowse, btnTransfer, btnClose, btnVhclAvl, btnVhclDesc, btnVhclMnl, btnEngine, btnFrame, btnWareHouse;
    @FXML
    private TextField txtField13;
    @FXML
    private VBox vboxSales;
    @FXML
    private AnchorPane anchorPurch;
    @FXML
    private GridPane gridPurch;
    @FXML
    private AnchorPane anchorSold;
    @FXML
    private GridPane gridSold;
    @FXML
    private AnchorPane anchorMisc;
    @FXML
    private GridPane gridMisc;
    @FXML
    private TextField txtField01, txtField03, txtField05, txtField07, txtField09, txtField06, txtField08, txtField10, txtField11, txtField15, txtField12, txtField14,
            txtField16, txtField20, txtField19, txtField23, txtField22, txtField21, txtField24, txtField25;
    @FXML
    private TextArea textArea02, textArea04;
    @FXML
    private ComboBox<String> comboBox18;
    @FXML
    private ComboBox<String> comboBox17;
    @FXML
    private DatePicker datePicker26;
    @FXML
    private TextArea textArea27;
    @FXML
    private TableView<ModelVehicleOwnerHistory> tblViewVhclOwnHsty;
    @FXML
    private TableColumn<ModelVehicleOwnerHistory, String> tblViewVhclOwnHsty01, tblViewVhclOwnHsty02, tblViewVhclOwnHsty03, tblViewVhclOwnHsty04, tblViewVhclOwnHsty05;
    @FXML
    private TableView<ModelVehicleWarehouseHistory> tblViewVhclWrhHsty;
    @FXML
    private TableColumn<ModelVehicleWarehouseHistory, String> tblViewVhclWrhHsty01, tblViewVhclWrhHsty02, tblViewVhclWrhHsty03, tblViewVhclWrhHsty04, tblViewVhclWrhHsty05;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void loadVehicleDescriptionWindow() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleDescriptionEntry.fxml"));
            VehicleDescriptionEntryController loControl = new VehicleDescriptionEntryController();
            loControl.setGRider(oApp);
            fxmlLoader.setController(loControl);
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
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", pxeModuleName);
            System.exit(1);
        }
    }

    private void loadEngineWindow(Integer fnCodeType, Boolean fbState) {
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleEngineFormatEntry.fxml"));

            VehicleEngineFormatEntryController loControl = new VehicleEngineFormatEntryController();
            loControl.setGRider(oApp);
//            loControl.setMakeID((String) oTransVehicle.getMaster(23));
//            loControl.setMakeDesc((String) oTransVehicle.getMaster(24));
//            loControl.setModelID((String) oTransVehicle.getMaster(25));
//            loControl.setModelDesc((String) oTransVehicle.getMaster(26));
//            loControl.setCodeType(fnCodeType);
//            loControl.setState(fbState);
//            loControl.setOpenEvent(true);
            fxmlLoader.setController(loControl);

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
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", pxeModuleName);
            System.exit(1);
        }
    }

    private void loadFrameWindow(Integer fnCodeType, Boolean fbState) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/guanzon/autoapp/views/parameters/VehicleFrameFormatEntry.fxml"));
            VehicleFrameFormatEntryController loControl = new VehicleFrameFormatEntryController();
            loControl.setGRider(oApp);
//            loControl.setMakeID((String) oTransVehicle.getMaster(23));
//            loControl.setMakeDesc((String) oTransVehicle.getMaster(24));
//            loControl.setModelID((String) oTransVehicle.getMaster(25));
//            loControl.setModelDesc((String) oTransVehicle.getMaster(26));
//            loControl.setCodeType(fnCodeType);
//            loControl.setState(fbState);
//            loControl.setOpenEvent(true);
            fxmlLoader.setController(loControl);

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
            ShowMessageFX.Warning(null, e.getMessage(), "Warning", pxeModuleName);
            System.exit(1);
        }
    }
}
