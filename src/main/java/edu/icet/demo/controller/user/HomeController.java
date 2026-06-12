package edu.icet.demo.controller.user;

import edu.icet.demo.bo.BoFactory;
import edu.icet.demo.bo.custom.UserBo;
import edu.icet.demo.model.User;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.BoType;
import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Sign-in screen: verifies the username/email + password against the
 * database and routes the user to the matching dashboard.
 */
public class HomeController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnlogin;

    @FXML
    private Button btnBack;

    @FXML
    private Label lblError;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    @FXML
    void btnloginOnAction(ActionEvent event) {
        lblError.setVisible(false);

        String username = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both your username and password.");
            return;
        }

        try {
            User user = userBo.authenticate(username, password);
            if (user == null) {
                showError("Invalid username or password. Please try again.");
                txtPassword.clear();
                return;
            }

            UserSession.setCurrentUser(user);
            if (UserSession.isAdmin()) {
                Navigation.navigate(btnlogin, "/view/user/admin-form.fxml");
            } else {
                Navigation.navigate(btnlogin, "/view/user/employee-home.fxml");
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        Navigation.navigate(btnBack, "/view/user/login-home-form.fxml");
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}
