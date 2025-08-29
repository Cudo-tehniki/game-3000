package zombi_shooter;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sound {

    private Map<String, Clip> soundClips;
    private Clip groundMusic;
    private boolean soundEnable;
    private float volume;

    public Sound() {
        this.soundClips = new HashMap<>();
        this.soundEnable = true;
        this.volume = 0.5f;
    }

    public void loadSound(String fileName, String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            soundClips.put(fileName, clip);
            setVolume(clip, volume);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }

    public void setVolume(Clip clip, float volume) {
        try {
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
        } catch (Exception e){
            throw new RuntimeException("не удалось установить громкость");
        }
    }

    public void playSound(String fileName){
        if(!soundEnable){
            return;
        }
        Clip clip = soundClips.get(fileName);
        if(clip != null){
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.out.println("звук не найден " + fileName);
        }
    }

    public void stopBackgroundSound(){
        if(groundMusic != null){
            groundMusic.stop();
        }
    }

    public void playBackgroudMusic(String musicName){
        if (!soundEnable){
            return;
        }
        stopBackgroundSound();
        groundMusic = soundClips.get(musicName);
        if(groundMusic != null){
            groundMusic.setFramePosition(0);
            groundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.out.println("фоновая музыка не найдена " + musicName);
        }
    }

    public void setSoundEnable(boolean soundEnable){
        this.soundEnable = soundEnable;
        if(!soundEnable){
            stopBackgroundSound();
        }

    }

    public void setMasterVolume(float volume){
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        for (Clip clip : soundClips.values()){
            setVolume(clip, this.volume);
        }
    }

    public void zombHit(String name){
        playSound(name);
    }

    public Map<String, Clip> getSoundClips() {
        return soundClips;
    }

    public void setSoundClips(Map<String, Clip> soundClips) {
        this.soundClips = soundClips;
    }

    public Clip getGroundMusic() {
        return groundMusic;
    }

    public void setGroundMusic(Clip groundMusic) {
        this.groundMusic = groundMusic;
    }

    public boolean isSoundEnable() {
        return soundEnable;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
