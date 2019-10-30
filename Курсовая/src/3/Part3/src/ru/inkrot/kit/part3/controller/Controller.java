package ru.inkrot.kit.part3.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private TabPane adminTabPane;

    // ================================================================

    @FXML
    private Tab librariansTab;
    @FXML
    private TableView<DataBaseEntity> librariansTable;
    @FXML
    private TableColumn<Object, Object> librariansIdColumn;
    @FXML
    private TableColumn<Object, Object> librariansNameColumn;
    @FXML
    private TableColumn<Object, Object> librariansLoginColumn;
    @FXML
    private TableColumn<Object, Object> librariansPasswordColumn;

    // ================================================================

    @FXML
    private Tab readersTab;
    @FXML
    private TableView<DataBaseEntity> readersTable;
    @FXML
    private TableColumn<Object, Object> readersIdColumn;
    @FXML
    private TableColumn<Object, Object> readersNameColumn;

    // ================================================================

    @FXML
    private Tab booksTab;
    @FXML
    private TableView<DataBaseEntity> booksTable;
    @FXML
    private TableColumn<?, ?> booksIdColumn;
    @FXML
    private TableColumn<?, ?> booksNameColumn;
    @FXML
    private TableColumn<?, ?> booksAuthorColumn;
    @FXML
    private TableColumn<?, ?> booksYearColumn;
    @FXML
    private TableColumn<?, ?> booksIsbnColumn;
    @FXML
    private TableColumn<?, ?> booksQuantityColumn;

    // ================================================================

    @FXML
    private Tab authorsTab;
    @FXML
    private TableColumn<Object, Object> authorsIdColumn;
    @FXML
    private TableColumn<Object, Object> authorsNameColumn;
    @FXML
    private TableView<DataBaseEntity> authorsTable;

    // ================================================================

    @FXML
    private Button addOrderButton;

    public Stage addDialogStage;

    @FXML
    void initialize() {
        librariansTable.setRowFactory((tableViewCallback));

        librariansIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        librariansNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        librariansLoginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        librariansPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        addOrderButton.setOnAction((event -> showAddOrderDialog()));

        updateOrdersTable();
    }

    Callback<TableView<DataBaseEntity>, TableRow<DataBaseEntity>> tableViewCallback = tableView -> {
        final TableRow<DataBaseEntity> row = new TableRow<>();
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Удалить");
        removeItem.setOnAction(event -> deleteEntity(row.getTableView(), row.getItem().getId()));
        rowMenu.getItems().addAll(removeItem);
        row.contextMenuProperty().bind(
                Bindings.when(Bindings.isNotNull(row.itemProperty()))
                        .then(rowMenu)
                        .otherwise((ContextMenu)null));
        return row;
    };

    public void showAddDialog(int addEntityType, String fxmlPath, int w, int h) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        try {
            Parent parent = fxmlLoader.load();
            addDialogStage = new Stage();
            ((AdminAddController) fxmlLoader.getController()).initAddDialog(this, addEntityType);
            addDialogStage.initModality(Modality.APPLICATION_MODAL);
            addDialogStage.setScene(new Scene(parent, w, h));
            addDialogStage.setResizable(false);
            addDialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
