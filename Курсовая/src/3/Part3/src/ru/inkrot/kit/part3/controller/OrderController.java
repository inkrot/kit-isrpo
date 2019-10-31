package ru.inkrot.kit.part3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.inkrot.kit.part3.model.DataBase;
import ru.inkrot.kit.part3.model.Order;
import ru.inkrot.kit.part3.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.inkrot.kit.part3.controller.MainController.EDIT_DIALOG;
import static ru.inkrot.kit.part3.controller.MainController.selectedOrder;

public class OrderController {

    @FXML
    private Label orderDialogLabel;

    @FXML
    private TextField customerField;

    @FXML
    private TextField customerTelField;

    @FXML
    private ListView<Phone> phonesList;

    @FXML
    private Label phonesListLabel;

    @FXML
    private Button addOrderButton;

    public static MainController mainController;

    @FXML
    void initialize() {
        ResultSet rs = DataBase.getPhones();
        try {
            while(rs.next())
                phonesList.getItems().add(
                        new Phone(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("price")
                        )
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        phonesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        phonesList.setOnMouseClicked(event -> checkSelected());

        if (MainController.dialogType == EDIT_DIALOG) {
            Order order = selectedOrder;
            customerField.setText(order.getCustomer());
            customerTelField.setText(order.getCustomerTel());
            for (Phone phone : order.getPhones())
                phonesList.getSelectionModel().select(phone);
            orderDialogLabel.setText("Изменение заказа");
            addOrderButton.setText("Изменить");
            addOrderButton.setOnAction((event -> editOrder()));
        } else {
            addOrderButton.setOnAction((event -> addOrder()));
        }
    }

    private void checkSelected() {
        int count = phonesList.getSelectionModel().getSelectedItems().size();
        if (count > 0) phonesListLabel.setText("Товаров выбрано: " + count);
        else phonesListLabel.setText("Товары не выбраны");
    }

    private void addOrder() {
        String customer = customerField.getText();
        String customerTel = customerTelField.getText();
        List<Phone> selectedItems = phonesList.getSelectionModel().getSelectedItems();

        if (customer.length() > 0 && customerTel.length() > 0 && selectedItems.size() > 0) {
            DataBase.insertOrder(customer, customerTel, selectedItems);
            mainController.dialogStage.getScene().getWindow().hide();
            mainController.updateOrdersTable();
        }
    }

    private void editOrder() {
        String customer = customerField.getText();
        String customerTel = customerTelField.getText();
        List<Phone> selectedItems = phonesList.getSelectionModel().getSelectedItems();

        if (customer.length() > 0 && customerTel.length() > 0 && selectedItems.size() > 0) {
            DataBase.updateOrder(selectedOrder.getId(), customer, customerTel, selectedItems);
            mainController.dialogStage.getScene().getWindow().hide();
            mainController.updateOrdersTable();
        }
    }
}
