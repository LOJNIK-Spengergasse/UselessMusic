package lojnik.spenger.musicplayer.model;

import java.io.File;

public class Song {
    //Variablen
    private String name;
    private File file;

    //Konstruktor
    public Song(String name, File file) {
        this.name = name;
        this.file = file;
    }

    //File Getter
    public File getFile() {
        return file;
    }

    //Name Getter
    public String getName() {
        return name;
    }
}
