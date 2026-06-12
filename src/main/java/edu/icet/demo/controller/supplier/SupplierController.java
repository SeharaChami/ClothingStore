package edu.icet.demo.controller.supplier;

import edu.icet.demo.bo.BoFactory;
import edu.icet.demo.bo.custom.CustomerBo;
import edu.icet.demo.bo.custom.SupplierBo;
import edu.icet.demo.model.Supplier;
import edu.icet.demo.util.Alerts;
import edu.icet.demo.util.BoType;
import edu.icet.demo.util.Navigation;
import edu.icet.demo.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Supplier management (add / update / delete / list).
 */
public class SupplierController implements Initializable {

    @FXML
    private TableView<Supplier> tblSuppliers;

    @FXML
    private TableColumn<Supplier, String> colSupId;

    @FXML
    private TableColumn<Supplier, String> colSupName;

    @FXML
    private TableColumn<Supplier, String> colCompany;

    @FXML
    private TableColumn<Supplier, String> colEmail;

    @FXML
    private Label lblSupId;

    @FXML
    private TextField txtSupName;

    @FXML
    private TextField txtCompany;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnNewSupplier;

    @FXML
    private Button btnUpdateSupplier;

    @FXML
    private Button btnDeleteSupplier;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnBack;

    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    private String selectedId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                selectedId = selected.getId();
                lblSupId.setText(selected.getId());
                txtSupName.setText(selected.getName());
                txtCompany.setText(selected.getCompany());
                txtEmail.setText(selected.getEmail());
            }
        });

        refresh();
    }

    @FXML
    void addSupplierOnAction(ActionEvent event) {
        Supplier supplier = readForm(null);
        if (supplier == null) {
            return;
        }
        try {
            supplier.setId(supplierBo.generateSupplierId());
            if (supplierBo.insertSupplier(supplier)) {
                Alerts.info("Supplier added", "Supplier \"" + supplier.getName() + "\" was added successfully.");
                clearForm();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void updateSupplierOnAction(ActionEvent event) {
        if (selectedId == null) {
            Alerts.info("No supplier selected", "Select a supplier in the table to update it.");
            return;
        }
        Supplier supplier = readForm(selectedId);
        if (supplier == null) {
            return;
        }
        try {
            if (supplierBo.updateSupplier(supplier)) {
                Alerts.info("Supplier updated", "Supplier " + selectedId + " was updated successfully.");
                clearForm();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void deleteSupplierOnAction(ActionEvent event) {
        if (selectedId == null) {
            Alerts.info("No supplier selected", "Select a supplier in the table to delete it.");
            return;
        }
        if (!Alerts.confirm("Delete supplier", "Delete supplier " + selectedId + " permanently?")) {
            return;
        }
        try {
            if (supplierBo.deleteSupplierById(selectedId)) {
                Alerts.info("Supplier deleted", "Supplier " + selectedId + " was removed.");
                clearForm();
                refresh();
            }
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    @FXML
    void clearOnAction(ActionEvent event) {
        clearForm();
    }

    @FXML
    void backOnAction(ActionEvent event) {
        Navigation.navigate(btnBack, UserSession.isAdmin()
                ? "/view/user/admin-form.fxml"
                : "/view/user/employee-home.fxml");
    }

    private Supplier readForm(String id) {
        String name = txtSupName.getText().trim();
        String company = txtCompany.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty()) {
            Alerts.error("Missing information", "Please enter the supplier's name.");
            return null;
        }
        if (!email.isEmpty() && !customerBo.isValidEmail(email)) {
            Alerts.error("Invalid email", "Please enter a valid email address.");
            return null;
        }
        return new Supplier(id, name, email, company);
    }

    private void refresh() {
        try {
            tblSuppliers.setItems(supplierBo.getAllSuppliers());
            lblSupId.setText(supplierBo.generateSupplierId());
        } catch (Exception e) {
            Alerts.databaseError(e);
        }
    }

    private void clearForm() {
        selectedId = null;
        tblSuppliers.getSelectionModel().clearSelection();
        txtSupName.clear();
        txtCompany.clear();
        txtEmail.clear();
        try {
            lblSupId.setText(supplierBo.generateSupplierId());
        } catch (Exception ignored) {
            lblSupId.setText("(auto)");
        }
    }
}
