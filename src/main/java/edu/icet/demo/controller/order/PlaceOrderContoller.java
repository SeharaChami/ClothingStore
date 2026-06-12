package edu.icet.demo.controller.order;

import edu.icet.demo.bo.BoFactory;
import edu.icet.demo.bo.custom.CustomerBo;
import edu.icet.demo.bo.custom.OrderBo;
import edu.icet.demo.bo.custom.PlaceOrderBo;
import edu.icet.demo.bo.custom.ProductBo;
import edu.icet.demo.model.Customer;
import edu.icet.demo.model.Order;
import edu.icet.demo.model.OrderDetail;
import edu.icet.demo.model.Product;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.BoType;
import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Point-of-sale screen: pick a customer (or register a new one), build a
 * cart of products and place the order.
 */
public class PlaceOrderContoller implements Initializable {

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblDate;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private Label lblCustomerName;

    @FXML
    private TextField txtCusName;

    @FXML
    private TextField txtCusEmail;

    @FXML
    private TextField txtCusAddress;

    @FXML
    private Button btnAddCustomer;

    @FXML
    private ComboBox<String> cmbProductId;

    @FXML
    private Label lblProductDetails;

    @FXML
    private TextField txtQty;

    @FXML
    private Button btnAddToCart;

    @FXML
    private TableView<OrderDetail> tblCart;

    @FXML
    private TableColumn<OrderDetail, String> colProductId;

    @FXML
    private TableColumn<OrderDetail, String> colProductName;

    @FXML
    private TableColumn<OrderDetail, Integer> colQty;

    @FXML
    private TableColumn<OrderDetail, Double> colUnitPrice;

    @FXML
    private TableColumn<OrderDetail, Double> colTotal;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnRemoveItem;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnBack;

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
    private final PlaceOrderBo placeOrderBo = BoFactory.getInstance().getBo(BoType.PLACE_ORDER);

    private final ObservableList<OrderDetail> cart = FXCollections.observableArrayList();
    private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tblCart.setItems(cart);

        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((obs, old, id) -> {
            if (id != null) {
                try {
                    Customer customer = customerBo.getCustomerById(id);
                    lblCustomerName.setText(customer == null ? "" : customer.getName());
                } catch (Exception e) {
                    Alerts.databaseError(e);
                }
            }
        });

        cmbProductId.getSelectionModel().selectedItemProperty().addListener((obs, old, id) -> {
            if (id != null) {
                try {
                    selectedProduct = productBo.getProductById(id);
                    lblProductDetails.setText(selectedProduct == null ? "" : selectedProduct.getName()
                            + "  |  price: " + String.format("%,.2f", selectedProduct.getPrice())
                            + "  |  in stock: " + selectedProduct.getQty());
                } catch (Exception e) {
                    Alerts.databaseError(e);
                }
            }
        });

        refresh();
    }

    @FXML
    void addCustomerOnAction(ActionEvent event) {
        String name = txtCusName.getText().trim();
        String email = txtCusEmail.getText().trim();
        String address = txtCusAddress.getText().trim();

        if (name.isEmpty()) {
            Alerts.error("Missing information", "Please enter the customer's name.");
            return;
        }
        if (!email.isEmpty() && !customerBo.isValidEmail(email)) {
            Alerts.error("Invalid email", "Please enter a valid email address.");
            return;
        }

        try {
            String id = customerBo.generateCustomerId();
            String empId = UserSession.getCurrentUser() == null
                    ? null : UserSession.getCurrentUser().getId();
            if (customerBo.insertCustomer(new Customer(id, name, email, address, empId))) {
                Alerts.info("Customer registered", "Customer \"" + name + "\" (" + id + ") was registered.");
                txtCusName.clear();
                txtCusEmail.clear();
                txtCusAddress.clear();
                cmbCustomerId.setItems(customerBo.getAllCustomerIds());
                cmbCustomerId.setValue(id);
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void addToCartOnAction(ActionEvent event) {
        if (selectedProduct == null) {
            Alerts.info("No product selected", "Please choose a product first.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText().trim());
        } catch (NumberFormatException e) {
            Alerts.error("Invalid quantity", "Please enter a whole number for the quantity.");
            return;
        }
        if (qty <= 0) {
            Alerts.error("Invalid quantity", "The quantity must be at least 1.");
            return;
        }

        int alreadyInCart = cart.stream()
                .filter(d -> d.getProductId().equals(selectedProduct.getId()))
                .mapToInt(OrderDetail::getQty)
                .sum();
        if (qty + alreadyInCart > selectedProduct.getQty()) {
            Alerts.error("Not enough stock",
                    "Only " + selectedProduct.getQty() + " unit(s) of \"" + selectedProduct.getName()
                            + "\" are in stock (cart already holds " + alreadyInCart + ").");
            return;
        }

        cart.removeIf(d -> d.getProductId().equals(selectedProduct.getId()));
        cart.add(new OrderDetail(selectedProduct.getId(), selectedProduct.getName(),
                qty + alreadyInCart, selectedProduct.getPrice()));
        txtQty.clear();
        updateTotal();
    }

    @FXML
    void removeItemOnAction(ActionEvent event) {
        OrderDetail selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alerts.info("Nothing selected", "Select a cart line to remove it.");
            return;
        }
        cart.remove(selected);
        updateTotal();
    }

    @FXML
    void placeOrderOnAction(ActionEvent event) {
        String customerId = cmbCustomerId.getValue();
        if (customerId == null) {
            Alerts.error("No customer", "Please select (or register) a customer for this order.");
            return;
        }
        if (cart.isEmpty()) {
            Alerts.error("Empty cart", "Please add at least one product to the cart.");
            return;
        }

        try {
            String orderId = orderBo.generateOrderId();
            String empId = UserSession.getCurrentUser() == null
                    ? null : UserSession.getCurrentUser().getId();
            double total = cart.stream().mapToDouble(OrderDetail::getTotal).sum();

            Order order = new Order(orderId, customerId, "COMPLETED", new Date(), total, empId);
            if (placeOrderBo.placeOrder(order, new ArrayList<>(cart))) {
                Alerts.info("Order placed",
                        "Order " + orderId + " was placed successfully.\nTotal: "
                                + String.format("%,.2f", total));
                cart.clear();
                updateTotal();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void backOnAction(ActionEvent event) {
        Navigation.navigate(btnBack, "/view/order/order-home-form.fxml");
    }

    private void refresh() {
        try {
            lblOrderId.setText(orderBo.generateOrderId());
            cmbCustomerId.setItems(customerBo.getAllCustomerIds());
            cmbProductId.setItems(productBo.getAllProductIds());
            selectedProduct = null;
            lblProductDetails.setText("");
            lblCustomerName.setText("");
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    private void updateTotal() {
        double total = cart.stream().mapToDouble(OrderDetail::getTotal).sum();
        lblTotal.setText(String.format("%,.2f", total));
    }
}
