module lojnik.spenger.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens lojnik.spenger.musicplayer to javafx.fxml;
    exports lojnik.spenger.musicplayer;
    exports lojnik.spenger.musicplayer.controller;
    opens lojnik.spenger.musicplayer.controller to javafx.fxml;
    exports lojnik.spenger.musicplayer.view;
    opens lojnik.spenger.musicplayer.view to javafx.fxml;
}