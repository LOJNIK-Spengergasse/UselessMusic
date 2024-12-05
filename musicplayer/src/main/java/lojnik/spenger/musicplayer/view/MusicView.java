package lojnik.spenger.musicplayer.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lojnik.spenger.musicplayer.controller.MusicController;
import lojnik.spenger.musicplayer.model.Player;
import lojnik.spenger.musicplayer.model.Song;

import java.util.Objects;

public class MusicView {
    //Konstanten
    private final MusicController mC; //Controller
    private final Player mp; //Model

    final private Image play = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/icons8-play-30.png")));
    final private Image pause = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/icons8-pause-30.png")));
    final private Image loop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/icons8-loop-30.png")));
    final private Image noloop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/icons8-noloop-30.png")));
    final private Image shuffle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/icons8-shuffle-30.png")));
    final private Image noshuffle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/icons8-noshuffle-30.png")));

    //Konstruktor
    public MusicView(MusicController mC, Player mp) {
        this.mC = mC;
        this.mp = mp;
    }

    //Button Bilder Wechsler
    public void changeIMG(boolean status, ImageView img, int type) { //type{1=playIMG, 2=loopIMG, 3=shuffleIMG}
        if (status) {
            switch (type) {
                case 1 -> img.setImage(play); //playIMG
                case 2 -> img.setImage(noloop); //loopIMG
                case 3 -> img.setImage(noshuffle); //shuffleIMG
            }
        } else {
            switch (type) {
                case 1 -> img.setImage(pause); //playIMG
                case 2 -> img.setImage(loop); //loopIMG
                case 3 -> img.setImage(shuffle); //shuffleIMG
            }
        }
    }

    //Button En/Disabler
    public void disableBt(Button bt, int type) { //type{1=play, 2=next, 3=prev}
        switch (type) {
            case 1: //play Button
                bt.setDisable(mp.getQueueLength() < 1);
                break;
            case 2: //next Button
                if (mp.isLoop() && mp.getQueueLength() != 0) bt.setDisable(false);
                else if (mp.isShuffle() && mp.getQueueLength() != 0) bt.setDisable(mp.getQueueLength() <= 1);
                else bt.setDisable(mp.getIndex().get() >= mp.getQueueLength() - 1);
                break;
            case 3: //prev Button
                if ((mp.isLoop() || mp.isShuffle()) && mp.getQueueLength() != 0) bt.setDisable(true);
                else bt.setDisable(mp.getIndex().get() <= 0);
                break;
        }
    }

    //Update Queue Status Menu
    public void changeQueueStatus(Menu l, String s) {
        l.setText(s);
    }

    //Update Queue ListView
    public void addQueueView(ListView queueView, Song s) {
        queueView.getItems().add(s.getName());
    }

    //Clear Queue+QueueStatus
    public void clearQueueView(ListView queueView) {
        queueView.getItems().clear();
    }

    //Update Current Song
    public void showCurrentSong(Label l, String s) {
        l.setText(s);
    }
}
