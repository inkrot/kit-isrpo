package ru.inkrot.kit.part3.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.inkrot.kit.part3.model.DataBase;
import ru.inkrot.kit.part3.model.Order;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Object, Object> ordersTableIdColumn;
    @FXML
    private TableColumn<Object, Object> ordersTableCustomerColumn;
    @FXML
    private TableColumn<Object, Object> ordersTableCustomerTelColumn;
    @FXML
    private TableColumn<Object, Object> ordersTablePriceColumn;
    @FXML
    private TableColumn<Object, Object> ordersTableDateColumn;

    @FXML
    private Button addOrderButton;

    public Stage dialogStage;
    public static Order selectedOrder;
    public static int dialogType;
    public static final int ADD_DIALOG = 1;
    public static final int EDIT_DIALOG = 2;

    @FXML
    void initialize() {

        ordersTable.setRowFactory(tableViewCallback);

        ordersTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ordersTableCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        ordersTableCustomerTelColumn.setCellValueFactory(new PropertyValueFactory<>("customerTel"));
        ordersTablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        ordersTableDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        addOrderButton.setOnAction((event -> showOrderDialog(ADD_DIALOG)));
        OrderController.mainController = this;

        updateOrdersTable();
    }

    Callback<TableView<Order>, TableRow<Order>> tableViewCallback = tableView -> {
        final TableRow<Order> row = new TableRow<>();
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setOnAction(event -> deleteOrder(row.getItem().getId()));
        rowMenu.getItems().addAll(removeItem);
        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))
                        .then(rowMenu)
                        .otherwise((ContextMenu)null));
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                selectedOrder = DataBase.getOrder(row.getItem().getId());
                showOrderDialog(EDIT_DIALOG);
            }
        });
        return row;
    };

    private void deleteOrder(int id) {
        DataBase.deleteOrder(id);
        updateOrdersTable();
    }

    public void updateOrdersTable() {
        ordersTable.getItems().clear();

        ResultSet rs = DataBase.getOrders();
        try {
            while(rs.next())
                ordersTable.getItems().add(
                        new Order(
                                rs.getInt("id"),
                                rs.getString("customer"),
                                rs.getString("customer_tel"),
                                rs.getInt("price"),
                                rs.getTimestamp("timestamp")
                        )
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showOrderDialog(int type) {
        dialogType = type;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/order_dialog.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(parent, 365, 431));
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
