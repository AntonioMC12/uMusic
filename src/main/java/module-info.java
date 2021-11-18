module es.antoniomc.uMusic {
    requires transitive javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires jbcrypt;
	requires javafx.graphics;

    opens es.antoniomc.uMusic to javafx.fxml;
    exports es.antoniomc.uMusic;
}
