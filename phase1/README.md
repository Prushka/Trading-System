Before Running:
-------------
1. Mark src as Source Root
2. Mark resources as Resources Root

The program won't run without doing these 2 steps

Configuration:
-------------
* The default configuration files can be found in resources folder: **language.properties** and **trade.properties**.
* **language.properties**: contains all language identifiers and English text. This file will stay in the resources folder.
* **trade.properties**: contains all trade related configurations. This file will be save to config/trade.properties the first time this program runs.

Log:
-------------
* No checked exception stacktrace will be printed in the console. It will print something to let you check the log files if something wrong happened. This is to be configured by another file.
* Log files are located at **log/{date time}.log**. These files also contains debug records.

Storage:
-------------
* We used CSV for every entity class. Files are located in **/data** folder.
* Serialization can be easily swapped by replacing **CSVRepository** to **SerializationRepository** in ControllerDispatcher.
* The CSV mapping is achieved by reflection, which can be found in MappableBase.
* The password is encrypted using md5, which will need to be improved.

Commands:
-------------
* back : To return to the previous menu levels if possible, the input will be kept
* next : To go back to the next levels after using back, the input will be kept

Reference:
-------------
* __[Ansi Color](https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println)__
* __[Read file from Resource Folder](https://stackoverflow.com/questions/15749192/how-do-i-load-a-file-from-resource-folder)__
* __[Curiously recurring generic patterns](https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class/)__
* __[Instantiation of enums using Reflection](https://stackoverflow.com/questions/3735927/java-instantiating-an-enum-using-reflection)__
* __[The order of Fields in Reflection](https://stackoverflow.com/questions/1097807/java-reflection-is-the-order-of-class-fields-and-methods-standardized)__
* __[Get generic type of java.util.List in Reflection](https://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list)__
* __[Regex for validating Email String](https://www.geeksforgeeks.org/check-email-address-valid-not-java/)__
* __[Regex for checking if a string is numberic](https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java)__
* __[Crypt with md5](https://stackoverflow.com/questions/6592010/encrypt-and-decrypt-a-password-in-java)__

Detailed information with author name can be found in the @author part in javadoc