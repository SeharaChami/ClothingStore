package edu.icet.demo.controller.order;

import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Orders hub: place a new order or browse the order history.
 */
public class OrderHomeController {

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnExit;

    @FXML
    void placeOrderOnAction(ActionEvent event) {
        Navigation.navigate(btnPlaceOrder, "/view/order/place-order-form.fxml");
    }

    @FXML
    void historyOnAction(ActionEvent event) {
        Navigation.navigate(btnHistory, "/view/order/order-history-form.fxml");
    }

    @FXML
    void backOnAction(ActionEvent event) {
        Navigation.navigate(btnExit, UserSession.isAdmin()
                ? "/view/user/admin-form.fxml"
                : "/view/user/employee-home.fxml");
    }
}
