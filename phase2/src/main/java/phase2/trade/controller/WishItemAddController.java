package phase2.trade.controller;

public class WishItemAddController {
    /*
    private ItemListType itemListType;
    private RegularUser user;

    public TableView<Item> tableView;

    public TableView<Item> suggestedItems;

    public VBox root;

    private GatewayBundle gatewayBundle;

    public WishItemAddController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    private TableColumn<Item, String> getTableColumn(String name, String fieldName) {
        TableColumn<Item, String> column = new TableColumn<>(name);
        column.setMinWidth(100);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Regular User's Items
        TableColumn<Item, String> nameColumn = getTableColumn("Name", "name");
        TableColumn<Item, String> descriptionColumn = getTableColumn("Description", "description");
        TableColumn<Item, String> categoryColumn = getTableColumn("Category", "category");
        TableColumn<Item, String> ownershipColumn = getTableColumn("Ownership", "ownership");
        TableColumn<Item, String> quantityColumn = getTableColumn("Quantity", "quantity");
        TableColumn<Item, String> priceColumn = getTableColumn("Price", "price");
        TableColumn<Item, String> willingnessColumn = getTableColumn("Willingness", "willingness");

        TableColumn<Item, String> suggestedNameColumn = getTableColumn("Name", "name");
        TableColumn<Item, String> suggestedDescriptionColumn = getTableColumn("Description", "description");
        TableColumn<Item, String> suggestedCategoryColumn = getTableColumn("Category", "category");
        TableColumn<Item, String> suggestedPriceColumn = getTableColumn("Price", "price");

        ObservableList<Item> displayData = FXCollections.observableArrayList(user.getItemList(itemListType).getSetOfItems());

        tableView = new TableView<>();
        tableView.setItems(displayData);
        tableView.getColumns().addAll(FXCollections.observableArrayList(nameColumn, descriptionColumn, categoryColumn,
                ownershipColumn, quantityColumn, willingnessColumn, priceColumn));

        // Recommending suggested items
        ObservableList<Item> suggestedItemsDisplay = getSuggestions();
        suggestedItems = new TableView<>();
        suggestedItems.setItems(suggestedItemsDisplay);
        tableView.getColumns().addAll(FXCollections.observableArrayList(suggestedNameColumn, suggestedDescriptionColumn,
                suggestedCategoryColumn, suggestedPriceColumn));


        // Adding Items
        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setMinWidth(100);

        TextField priceInput = new TextField();
        priceInput.setPromptText("Description");

        TextField quantityInput = new TextField();
        quantityInput.setPromptText("Quantity");

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10)); // padding around entire layout
        hbox.setSpacing(10);
        hbox.getChildren().addAll(addButton, deleteButton);

        addButton.setOnAction(event -> {
            addWindow(displayData);
        });
        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;
            deleteButton.setDisable(true);

            Command<Long[]> remove = new RemoveItem(gatewayBundle, user, itemListType, getItemIdsFrom(itemsSelected));
            remove.execute((result, resultStatus) -> {
                Platform.runLater(() -> {
                    itemsSelected.forEach(displayData::remove);
                    deleteButton.setDisable(false);
                });
            });
        });

        root.getChildren().addAll(tableView, hbox, suggestedItems);
    }

    public EventHandler<ActionEvent> getWillingnessHandler(JFXButton button, Willingness willingness) {
        return event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;
            button.setDisable(true);
            Command<Item> command = new AlterWillingness(gatewayBundle, user, willingness, getItemIdsFrom(itemsSelected));
            command.execute((result, resultStatus) -> Platform.runLater(() -> {
                itemsSelected.forEach(item -> item.setWillingness(willingness));
                button.setDisable(false);
            }));
        };
    }

    private Set<Long> getItemIdsFrom(ObservableList<Item> observableList){
        Set<Long> ids = new HashSet<>();
        for (Item item : observableList) {
            ids.add(item.getUid());
        }
        return ids;
    }

    private ObservableList<Item> getSelected() {
        ObservableList<Item> itemsSelected;
        itemsSelected = tableView.getSelectionModel().getSelectedItems();
        return itemsSelected;
    }

    private ObservableList<Item> getSuggestions(){
        ObservableList<Item> suggestions = FXCollections.observableArrayList();
        // for (User currUser: gatewayBundle.getEntityBundle().getUserGateway().findAll()){
            // GetItems getItems = new GetItems(gatewayBundle, user, ItemListType.INVENTORY);
            // if (!currUser.equals(this.user)) { // want to suggest items of same category
                // suggestions.addAll(getItems.execute(););
            // }
        // }
        return suggestions;
    }

    public void addWindow(ObservableList<Item> displayData) {
        // AddItemController addItemController = new AddItemController(gatewayBundle, user, itemListType, displayData);
        // getSceneManager().loadParent("add_item.fxml", addItemController);
    }*/
}
