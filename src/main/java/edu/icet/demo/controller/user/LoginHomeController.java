package edu.icet.demo.controller.user;

import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Welcome / landing screen shown on application start.
 */
public class LoginHomeController {

    @FXML
    private Button btnlogin;

    @FXML
    private Button btnregister;

    @FXML
    void btnloginOnAction(ActionEvent event) {
        Navigation.navigate(btnlogin, "/view/user/home-form.fxml");
    }

    @FXML
    void btnregisterOnAction(ActionEvent event) {
        Alerts.info("Staff accounts",
                "Staff accounts are created by the shop owner.\n\n"
                        + "Please log in as the owner (admin) and add new employees "
                        + "from the Admin Dashboard.");
    }
}
