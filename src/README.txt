How to setup (VSCode):

1. Create a MySQL schema called "moviedb"
2. Update the mySQLPassword String to your root password in MainScene.java and Login.java
3. Run the table commands in your schema.
4. Add this configuration to launch.json
    "vmArgs": "--module-path \"lib/JavaFX/javafx-sdk-19.0.2.1/lib\" --add-modules javafx.controls,javafx.fxml --add-modules=javafx.swing,javafx.graphics,javafx.fxml,javafx.media,javafx.web --add-reads javafx.graphics=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.charts=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.iio=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.iio.common=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.css=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.runtime=ALL-UNNAMED"
