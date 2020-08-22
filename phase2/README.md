Before Running & Instructions:
-------------
1. This project uses maven. Please install it (or add it to path) and import pom.xml.
2. This project requires two types of databases:
    - one of MySQL / MariaDB
    - Redis
3. The required databases have been set up on my server, and the default configuration will access those databases. So in short, the program will run without any local databases if you have internet connection.
4. You can configure to use local database in redis.yaml and database.yaml
5. The default administrative has username: admin and password: admin???
6. Validation: although most of the validation is covered. I do not have extra time to go over all Commands. So unless something has an optional in it, it is recommended to fill in all blanks (when there are TextFields / ComboBoxes).
7. Try to edit item and user by doubling clicking table cells.
8. After clicking the checkout button in Cart, a trade pane will show up. Please Drag and Drop Items from the table on the left to the table on the right.

Features:
-------------
1. Allow users to enter their home city when creating an account, so that the system will only show them other users in the same city.
2. Monetize the system. In other words, give users the option to sell or trade items.
3. Administrative users can manage, edit Items, Users, assign Permissions, manage SupportTickets.
4. Multi-Way trade (unlimited participants).
5. Permissions and PermissionGroup. Enforced Permission for every operation a User operates. PermissionGroups include Banned / Guest / System etc.
6. JavaFX GUI and the following:
    1. Widgets and Alerts
    1. Filter Market / Inventory / Cart / Users using a combination of TextFields, ComboBoxes, CheckBoxes, Toggle etc. (They will work together to process a single list)
    1. Edit values directly in TableView. This includes editing a column with a TextField or a ComboBox. 
    1. Experiment this with editing Users using administrative account or edit Items in Inventory etc. (by double clicking every cell)
7. Concurrency, auto-reload and asynchronous database executions
8. Support Tickets
9. Item Category & User Avatar
10. Mandatory Extensions

**Important: thanks to the contribution problem:**
1. Monetization is half implemented. (The account balance is not implemented)
2. UNDO is incomplete. I really don't have the time to implement all undo operations. The rest can be implemented easily by overriding the undoUnchecked() method in Command's subclasses.
3. The suggestion extension is replaced by the Market. However, the suggestion can be implemented using the exiting Market classes. 
4. Bugs: <del>Due to the fact this became a personal (or mostly two-people) project</del> we weren't able to fully test the project. We did our best to fix most bugs.
5. Permissions: 
    - Few commands are still missing permissions. Adding a single field in their @CommandProperty will resolve this. 
    - However, if any PermissionGroup is missing permissions and there are existing users with that PermissionGroup in the database.
    - Editing the config won't automatically update those existing users. We can update the permission group using administrative accounts in the GUI.
6. Javadoc: 

Database:
-------------
1. This project supports MariaDB and MySQL thanks to hibernate's dialects.
2. The database made everything harder to implement since I wasn't familiar with ORM either.
3. All database operations are asynchronous.
4. We tried to follow Java Persistence API and there are no SQL statements.
5. PostgreSQL can also be supported by adjusting the annotations to add schemas.
6. Provided databases (running in docker containers and only used for this project, server is located in Canada) (a local database is a bit more preferable):
    - MySQL: host: muddy.ca | port: 3308 | database: group | user: member | password: aC4YD6G4J@Y
    - MariaDB: host: muddy.ca | port: 3311 | database: group | user: member | password: aC4YD6G4J@Y
    - redis: host: muddy.ca | port: 6380 | password: p8kgf3I!R77k

Dependencies:
-------------
1. Hibernate
    - Used as an Object-Relational Mapping framework
2. JFoenix
    - the material design library for JavaFX
3. Log4j2
    - A fully functioning and configured logger with ConsoleHandler and FileHandler were implemented in phase1
    - Pre-existing libraries like log4j2 offer better extensibility and configuration
4. Jedis
    - The publish-subscribe pattern allows multiple concurrently running applications to subscribe to the channel and publish any changes they have.
    - We implemented the controllers so that any controller (if registered) with the desired name will be reloaded if such publishing happens from another application.
5. Jackson
    - The reflection approach to map objects into CSVs in phase 1 was kind of incomplete
    - This library offers a better implementation to map an object to a yaml file, or a json file
6. org.json and commons-io
    - Used to parse a Json file into a nested map in GeoConfig
    
Design Patterns:
-------------
Observer / PubSub: 
* ReReReRe
* Reloadable
* Refreshable
* Redis
* Shutdownable
* ShutdownHook

Strategy: 
* ConfigStrategy
* LocalStrategy
* FormatStrategy
* YamlStrategy
* JsonStrategy

Dependency Injection:
* CommandFactory
* ControllerFactory
* All Controllers, All Commands and All Data Access Objects

Factory Method:
* UserFactory
* ValidatorFactory
* ImageFactory
* NodeFactory
* NotificationFactory
* PopupFactory
* CommandFactory
* ControllerFactory
* SideOption

Fa&#231;ade:
* ControllerResources
* GatewayBundle
* ConfigBundle
* EntityGatewayBundle
* DatabaseResourceBundle
* WidgetBundle (inner class)
* CartItemWrapper
* ResultStatusWrapper
* AbstractController

Builder:
* TableViewGenerator
* ListViewGenerator

Adapter:
* CartItemWrapper

Iterator:
* TradeUserOrderBundleIterator

Command:
* Command and its subclasses

Javadoc:
-------------
The phase 2 instruction didn't mention javadoc. 

And there are so many classes that may require a javadoc, I couldn't add detailed javadoc for every class on my own in such short period of time and <del>I shouldn't expect my group member to finish the javadoc.</del>

Javadoc can be found in packages from address to item's command (alphabetic order). The rest javadocs are auto-generated and are missing details.

If anything is unclear in the code, feel free to ask me if you'd like to.