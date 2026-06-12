package edu.icet.demo.controller.user;

import edu.icet.demo.bo.BoFactory;
import edu.icet.demo.bo.custom.CustomerBo;
import edu.icet.demo.bo.custom.UserBo;
import edu.icet.demo.model.User;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.BoType;
import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Shop-owner dashboard: navigation hub plus employee account management.
 */
public class Admincontroller implements Initializable {

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private TextField txtEmployeeName;

    @FXML
    private TextField txtEmployeeEmail;

    @FXML
    private PasswordField txtEmployeePassword;

    @FXML
    private TextField txtEmployeeAddress;

    @FXML
    private Button btnNewEmployee;

    @FXML
    private Button btnManageProducts;

    @FXML
    private Button btnManageSuppliers;

    @FXML
    private Button btnManageOrders;

    @FXML
    private Button btnLogout;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = UserSession.getCurrentUser();
        lblWelcome.setText(user == null ? "Welcome!" : "Welcome, " + user.getName() + "!");
        refreshNextEmployeeId();
    }

    @FXML
    void addEmployeeOnAction(ActionEvent event) {
        String name = txtEmployeeName.getText().trim();
        String email = txtEmployeeEmail.getText().trim();
        String password = txtEmployeePassword.getText();
        String address = txtEmployeeAddress.getText().trim();

        if (name.isEmpty() || password.isEmpty()) {
            Alerts.error("Missing information", "A username and a password are required.");
            return;
        }
        if (password.length() < 6) {
            Alerts.error("Weak password", "Please use a password of at least 6 characters.");
            return;
        }
        if (!email.isEmpty() && !customerBo.isValidEmail(email)) {
            Alerts.error("Invalid email", "Please enter a valid email address.");
            return;
        }

        try {
            String id = userBo.generateUserId();
            boolean added = userBo.addUser(new User(id, name, email, password, "EMPLOYEE", address));
            if (added) {
                Alerts.info("Employee added",
                        "Employee account \"" + name + "\" (" + id + ") was created successfully.");
                txtEmployeeName.clear();
                txtEmployeeEmail.clear();
                txtEmployeePassword.clear();
                txtEmployeeAddress.clear();
                refreshNextEmployeeId();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void manageProductsOnAction(ActionEvent event) {
        Navigation.navigate(btnManageProducts, "/view/product/product-form.fxml");
    }

    @FXML
    void manageSuppliersOnAction(ActionEvent event) {
        Navigation.navigate(btnManageSuppliers, "/view/supplier/supplier-form.fxml");
    }

    @FXML
    void manageOrdersOnAction(ActionEvent event) {
        Navigation.navigate(btnManageOrders, "/view/order/order-home-form.fxml");
    }

    @FXML
    void logoutOnAction(ActionEvent event) {
        if (Alerts.confirm("Log out", "Are you sure you want to log out?")) {
            UserSession.clear();
            Navigation.navigate(btnLogout, "/view/user/login-home-form.fxml");
        }
    }

    private void refreshNextEmployeeId() {
        try {
            lblEmployeeId.setText("Next employee id: " + userBo.generateUserId());
        } catch (Exception e) {
            lblEmployeeId.setText("Next employee id: (auto)");
        }
    }
}
