module javabash {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens javabash to javafx.fxml;
    exports javabash;
}
