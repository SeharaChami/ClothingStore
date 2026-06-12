package edu.icet.demo.controller.order;

import edu.icet.demo.bo.BoFactory;
import edu.icet.demo.bo.custom.OrderBo;
import edu.icet.demo.model.Order;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.BoType;
import edu.icet.demo.util.Navigation;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Read-only list of all orders placed so far.
 */
public class OrderHistoryController implements Initializable {

    @FXML
    private TableView<Order> tblOrders;

    @FXML
    private TableColumn<Order, String> colOrderId;

    @FXML
    private TableColumn<Order, String> colCusId;

    @FXML
    private TableColumn<Order, String> colDate;

    @FXML
    private TableColumn<Order, Double> colAmount;

    @FXML
    private TableColumn<Order, String> colStatus;

    @FXML
    private Label lblSummary;

    @FXML
    private Button btnBack;

    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDate() == null ? "" : format.format(cell.getValue().getDate())));

        try {
            tblOrders.setItems(orderBo.getAllOrders());
            double total = tblOrders.getItems().stream().mapToDouble(Order::getAmount).sum();
            lblSummary.setText(tblOrders.getItems().size() + " orders  |  total revenue: "
                    + String.format("%,.2f", total));
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void backOnAction(ActionEvent event) {
        Navigation.navigate(btnBack, "/view/order/order-home-form.fxml");
    }
}
