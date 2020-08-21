Before Running:
-------------
1. The project uses maven. Please import pom.xml.
2. The project requires two types of databases:
    - one of MySQL / MariaDB
    - Redis
3. The required databases have been set up on my server, and the default configuration will access those databases. So in short, the program will run without any local databases if you have internet connection.
4. You can configure to use local database in redis.yaml and database.yaml

Features:
-------------
1. Allow users to enter their home city when creating an account, so that the system will only show them other users in the same city.
2. Monetize the system. In other words, give users the option to sell or trade items.
3. Administrative users can manage, edit Items, Users, assign Permissions, manage SupportTickets.
4. Multi-Way trade (unlimited participants).
5. Permissions and PermissionGroup. Enforced Permission for every operation a User operates. PermissionGroups include Banned / Guest etc.
6. JavaFX GUI and the following:
    - Filter Market / Inventory / Cart / Users / using a combination of TextFields, ComboBoxes, CheckBoxes etc. 
    - Edit values directly in TableView. This includes editing a column with a TextField or a ComboBox. Experiment this with editing Users using administrative account or edit Items in Inventory etc.
7. Concurrency, auto-reload and asynchronous database executions
8. Support Tickets
9. Mandatory Extensions

**Important: thanks to the contribution problem:**
1. Monetization is half implemented. (The account balance is not implemented)
2. UNDO is incomplete. I really don't have the time to implement all undo operations. The rest can be implemented easily by overriding the undoUnchecked() method in Command's subclasses.
3. The suggestion extension is replaced by the Market. However, the suggestion can be implemented using the exiting Market classes. 
4. Bugs: <del>Due to the fact this became a personal (or mostly two-people) project</del> we weren't able to fully test the project. We did our best to fix most bugs.

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
* Reload
* Refresh
* Redis

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