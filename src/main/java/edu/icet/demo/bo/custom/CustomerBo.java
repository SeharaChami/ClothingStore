package edu.icet.demo.bo.custom;

import edu.icet.demo.bo.SuperBo;
import edu.icet.demo.model.Customer;
import javafx.collections.ObservableList;

public interface CustomerBo extends SuperBo {

    String generateCustomerId();

    boolean insertCustomer(Customer customer);

    ObservableList<Customer> getAllCustomers();

    ObservableList<String> getAllCustomerIds();

    Customer getCustomerById(String id);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomerById(String id);

    boolean isValidEmail(String email);
}
