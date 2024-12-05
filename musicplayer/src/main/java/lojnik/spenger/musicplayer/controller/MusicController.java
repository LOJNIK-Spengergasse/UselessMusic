package lojnik.spenger.musicplayer.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lojnik.spenger.musicplayer.model.Player;
import lojnik.spenger.musicplayer.view.MusicView;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MusicController implements Initializable {
    //Konstanten
    private final Player mp; //Model
    private final MusicView mV; //View

    private final int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private final String[] filetypes = {"*.mp3", "*.m4a", "*.wav"};

    //Variablen

    private Timer timer;
    private TimerTask task;
    //FXML Variablen
    @FXML
    private Button playBt, nextBt, prevBt;

    @FXML
    private ImageView playIMG, loopIMG, shuffleIMG;

    @FXML
    private ListView<String> queueView = new ListView<>();

    @FXML
    private Slider volumeSl;

    @FXML
    private Menu queueStatus;

    @FXML
    private ProgressBar progressBr;

    @FXML
    private Label songName;
    @FXML
    private ComboBox<String> speedBx;

    //Kontruktor
    public MusicController() {
        this.mp = new Player();
        this.mV = new MusicView(this, this.mp);
    }

    //init
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int speed : speeds) {
            speedBx.getItems().add(speed + "%");
        }
        speedBx.setOnAction(this::changeSpeed);
        volumeSl.valueProperty().addListener((observableValue, number, t1) -> changeVolume());
        mp.getIndex().addListener((observableValue, number, t1) -> checkBt());
    }

    //Play/Pause Button
    @FXML
    protected void playBtClick() {
        if (mp.isPlaying()) {
            mV.changeIMG(true, playIMG, 1);
            cancelTimer();
            mp.pause();
        } else {
            mV.changeIMG(false, playIMG, 1);
            beginTimer();
            mp.play();
            changeSpeed(null);
            changeVolume();
        }

    }

    //Skip Buttons
    @FXML
    protected void onNextButtonClick() {
        if (mp.isMpNull()) {
            mp.next();
            return;
        }
        mp.stop();
        if (mp.isPlaying()) cancelTimer();
        mp.next();
        changeSpeed(null);
        changeVolume();
        beginTimer();
        mV.changeIMG(false, playIMG, 1);
        checkBt();
    }

    @FXML
    protected void prevBtClick() {
        if (mp.isMpNull()) {
            mp.prev();
            return;
        }
        mp.stop();
        if (mp.isPlaying()) cancelTimer();
        mp.prev();
        changeSpeed(null);
        changeVolume();
        beginTimer();
        mV.changeIMG(false, playIMG, 1);
        checkBt();
    }

    //Loop Button
    @FXML
    protected void loop() {
        mV.changeIMG(mp.setLoop(), loopIMG, 2);
        checkBt();
    }

    //Shuffle Button
    @FXML
    protected void shuffle() {
        mV.changeIMG(mp.setShuffle(), shuffleIMG, 3);
        checkBt();
    }

    //Speed ComboBox
    private void changeSpeed(ActionEvent event) {
        if (speedBx.getValue() == null) mp.setSpeed(1.0);
        else
            mp.setSpeed(Integer.parseInt(speedBx.getValue().substring(0, speedBx.getValue().length() - 1)) * 0.01); //substring entfernt prozent zeichen
    }

    //Volume Slider
    private void changeVolume() {
        mp.setVolume(volumeSl.getValue());
    }

    //FileChooser/Select Button
    @FXML
    protected void filechoose(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            FileChooser fc = new FileChooser();
            fc.setTitle("Open Audio Files");
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio", filetypes));
            for (String filetype : filetypes) {
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(filetype, "*." + filetype));
            }
            List<File> files = fc.showOpenMultipleDialog(node.getScene().getWindow());
            for (File f : files) {
                mV.addQueueView(queueView, mp.addQueue(f));
            }
            checkBt();
        } catch (NullPointerException npe) {
        } //Einfach nur das kein Error ausgegeben wird
    }

    //File Drag and Drop Events
    @FXML
    protected void queueDrag(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    protected void queueDragDropped(DragEvent event) {
        List<File> files = event.getDragboard().getFiles();
        int good = 0;
        for (File f : files) {
            if (fileCheck(f)) {
                mV.addQueueView(queueView, mp.addQueue(f));
                good++;
            }
        }
        if (files.size() > good) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Unfit files");
            a.setContentText("You submitted " + (files.size() - good) + " unfit file(s)! \nPlease make sure to only select Audio files! \n" + Arrays.toString(filetypes));
            a.show();
        }
        checkBt();
    }

    //File Typ Checker
    private boolean fileCheck(File f) {
        String filename = f.toString();
        String type = "*";
        if (filename.contains(".")) type += filename.substring(filename.lastIndexOf("."));
        else return false;
        for (String filetype : filetypes) {
            if (type.equals(filetype)) return true;
        }
        return false;
    }

    //Disable Buttons
    public void checkBt() {
        mV.disableBt(playBt, 1);
        mV.disableBt(nextBt, 2);
        mV.disableBt(prevBt, 3);
        queueStatus();
        showCurrentSong();

    }

    //Update Queue Status Menu
    @FXML
    protected void queueStatus() {
        mV.changeQueueStatus(queueStatus, "Queue: " + (mp.getIndex().get() + 1) + " / " + mp.getQueueLength());
    }

    //Update Current Song Label
    public void showCurrentSong() {
        mV.showCurrentSong(songName, mp.getCurrentSong());
    }

    //clear/reset MenuItem
    @FXML
    protected void clear() {
        mV.clearQueueView(queueView);
        if(mp.isPlaying()) cancelTimer();
        mp.clear();
        mV.changeIMG(true, playIMG, 1);
        mp.setPlaying(false);
        checkBt();
        mV.changeQueueStatus(queueStatus, "Queue: 0 / 0");
    }

    //exit Menu Item
    @FXML
    protected void exit() {
        Platform.exit();
    }

    //Progress Bar Timer
    private void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                mp.setPlaying(true);
                double current = mp.getCurrentTime().toSeconds();
                double end = mp.getEndTime().toSeconds();
                //System.out.println(current+"/"+end);
                progressBr.setProgress(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    private void cancelTimer() {
        mp.setPlaying(false);
        timer.cancel();
    }

    //Impressum MenuItem
    @FXML
    protected void openImpressum() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lojnik/spenger/musicplayer/impressum-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage impressum = new Stage();
        impressum.setScene(new Scene(root));
        impressum.setResizable(false);
        impressum.setTitle("Impressum");
        impressum.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lojnik/spenger/musicplayer/pictures/Useless-Music.png"))));
        impressum.show();
    }
}