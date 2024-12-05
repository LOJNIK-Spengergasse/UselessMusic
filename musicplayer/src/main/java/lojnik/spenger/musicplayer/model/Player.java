package lojnik.spenger.musicplayer.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    //Konstanten
    private final ArrayList<Song> queue = new ArrayList<>();

    private final IntegerProperty index = new SimpleIntegerProperty(); //kein Integer, weil ich den ChangeListener in MusicController brauche

    //Variablen
    private MediaPlayer mp;
    private boolean playing, loop, shuffle;
    double volume, speed;

    //Konstruktor
    public Player() {
        loop = false;
        playing = false;
        setIndex(0);
    }

    //Queue Setter und Getter
    public Song addQueue(File f) {
        Song song = new Song(f.getName(), f);
        queue.add(song);
        return song;
    }

    public int getQueueLength() {
        return queue.size();
    }

    //Queue Index Setter und Getter
    public void setIndex(int i) {
        index.set(i);
    }

    public IntegerProperty getIndex() {
        return index;
    }

    //Queue Current Song Getter
    public String getCurrentSong() {
        if (queue.size() == 0) return "Welcome to Useless Music!";
        else return queue.get(index.get()).getName();
    }

    //Mediaplayer Setter und Getter
    public void setMediaPlayer() {
        mp = new MediaPlayer(new Media(queue.get(index.get()).getFile().toURI().toString()));
    }

    public boolean isMpNull() {
        return mp == null;
    }

    //Playing Setter und Getter
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return playing;
    }

    //play Methode
    public void play() {
        if (mp == null) {
            if (queue.isEmpty()) {
                playing = false;
                return;
            } else setMediaPlayer();
        }
        mp.play();
        setSpeed(speed);
        setVolume(volume);
        playing = true;
        mp.setOnEndOfMedia(this::doLoop);
    }

    //Pause Methode
    public void pause() {
        if (mp != null) {
            mp.pause();
        }
    }

    //Stop Methode
    public void stop() {
        mp.stop();
    }

    //Next Methode
    public void next() {
        if (mp == null && index.get() + 1 < queue.size()) {
            setIndex(index.get() + 1);
        } else if (loop) {
            doLoop();
        } else if (shuffle) {
            Random r = new Random();
            setIndex(r.nextInt(queue.size()));
            setMediaPlayer();
            play();
        } else if (index.get() + 1 < queue.size()) {
            setIndex(index.get() + 1);
            setMediaPlayer();
            play();
        } else {
            setMediaPlayer();
        }
    }

    //Prev Methode
    public void prev() {
        setIndex(index.get() - 1);
        if (mp != null) {
            setMediaPlayer();
            play();
        }
    }

    //Loop Setter und Getter
    public boolean setLoop() {
        if (!loop) return loop = true;
        else return loop = false;
    }

    public boolean isLoop() {
        return loop;
    }

    //Loop Methode
    private void doLoop() {
        if (loop) {
            setMediaPlayer();
            play();
        } else {
            next();
        }
    }

    //Shuffle Setter und Getter
    public boolean setShuffle() {
        if (!shuffle) return shuffle = true;
        else return shuffle = false;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    //Speed Setter
    public void setSpeed(double s) {
        speed = s;
        if (mp != null) {
            mp.setRate(speed);
        }
    }

    //Volume Setter
    public void setVolume(double v) {
        volume = v;
        mp.setVolume(v / 100);
    }

    //clear Methode
    public void clear() {
        queue.clear();
        setIndex(0);
        if(mp!=null) {
            mp.stop();
            mp = null;
        }
    }

    //Timer getter
    public Duration getCurrentTime() {
        return mp.getCurrentTime();
    }

    public Duration getEndTime() {
        return mp.getCycleDuration();
    }
}
