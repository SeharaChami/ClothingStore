package edu.icet.demo.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.bo.custom.CustomerBo;
import edu.icet.demo.dao.DaoFactory;
import edu.icet.demo.dao.custom.CustomerDao;
import edu.icet.demo.entity.CustomerEntity;
import edu.icet.demo.model.Customer;
import edu.icet.demo.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerBoimpl implements CustomerBo {

    private final CustomerDao customerDao = DaoFactory.getInstance().getDao(DaoType.CUSTOMER);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String generateCustomerId() {
        String lastCustomerId = customerDao.getLatestId();
        if (lastCustomerId == null) {
            return "C0001";
        }
        int number = Integer.parseInt(lastCustomerId.substring(1));
        return String.format("C%04d", number + 1);
    }

    @Override
    public boolean insertCustomer(Customer customer) {
        return customerDao.insert(mapper.convertValue(customer, CustomerEntity.class));
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        customerDao.searchAll().forEach(entity ->
                customers.add(mapper.convertValue(entity, Customer.class)));
        return customers;
    }

    @Override
    public ObservableList<String> getAllCustomerIds() {
        ObservableList<String> ids = FXCollections.observableArrayList();
        customerDao.searchAll().forEach(entity -> ids.add(entity.getId()));
        return ids;
    }

    @Override
    public Customer getCustomerById(String id) {
        CustomerEntity entity = customerDao.search(id);
        return entity == null ? null : mapper.convertValue(entity, Customer.class);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return customerDao.update(mapper.convertValue(customer, CustomerEntity.class));
    }

    @Override
    public boolean deleteCustomerById(String id) {
        return customerDao.delete(id);
    }

    @Override
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.+-]+@([\\w-]+\\.)+[\\w-]{2,}$");
    }
}
