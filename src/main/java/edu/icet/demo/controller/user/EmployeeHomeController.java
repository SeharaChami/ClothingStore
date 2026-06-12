package edu.icet.demo.controller.user;

import edu.icet.demo.model.User;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Employee landing page: choose which section of the shop to work in.
 */
public class EmployeeHomeController implements Initializable {

    private static final String MANAGE_PRODUCTS = "Manage Products";
    private static final String MANAGE_SUPPLIERS = "Manage Suppliers";
    private static final String ORDERS = "Orders";

    @FXML
    private Label lblWelcome;

    @FXML
    private ComboBox<String> servicesCmb;

    @FXML
    private Button btnContinue;

    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = UserSession.getCurrentUser();
        lblWelcome.setText(user == null ? "Welcome!" : "Welcome, " + user.getName() + "!");
        servicesCmb.setItems(FXCollections.observableArrayList(
                MANAGE_PRODUCTS, MANAGE_SUPPLIERS, ORDERS));
    }

    @FXML
    void btnContinueOnAction(ActionEvent event) {
        String selected = servicesCmb.getValue();
        if (selected == null) {
            Alerts.info("Choose a service", "Please select a service from the list first.");
            return;
        }
        switch (selected) {
            case MANAGE_PRODUCTS:
                Navigation.navigate(btnContinue, "/view/product/product-form.fxml");
                break;
            case MANAGE_SUPPLIERS:
                Navigation.navigate(btnContinue, "/view/supplier/supplier-form.fxml");
                break;
            case ORDERS:
                Navigation.navigate(btnContinue, "/view/order/order-home-form.fxml");
                break;
            default:
                break;
        }
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        if (Alerts.confirm("Log out", "Are you sure you want to log out?")) {
            UserSession.clear();
            Navigation.navigate(btnExit, "/view/user/login-home-form.fxml");
        }
    }
}
