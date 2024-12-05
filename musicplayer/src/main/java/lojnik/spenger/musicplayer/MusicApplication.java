package lojnik.spenger.musicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MusicApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MusicApplication.class.getResource("music-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Useless Music");
        stage.setScene(scene);
        stage.setMinHeight(520);
        stage.setMinWidth(650);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/Useless-Music.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}