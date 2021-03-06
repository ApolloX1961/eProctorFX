package eproctor.commons;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *This class is to dealing with time issue, including the timing of action event and concurrent processing issue
 * @author Chenliyang
 */
public class Timer {

    /**
     *initiate a timer
     * <p> this will returns the a timer object
     * @param count 
     * @param heading
     * @param lbl
     * @param eh
     * @return
     */
    public static Timeline produceATimer(Integer count, String heading, Label lbl, EventHandler<ActionEvent> eh) {
        return null;
        
//        Timeline timer =  new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                //count = count - 1;
//                System.out.println("count: " + count);
//                lbl.setText("time to exam: " + intSecToReadableSecond(count, 4));
//            }
//        }));
//        timer.setCycleCount(count);
//        timer.setOnFinished(eh);
//        
//        return timer;
    }

    /**
     *This method is to transform formation of time into second that can be read by user
     * @param t passing-in time
     * @param level integer stands for sec, min, hour, day
     * @return return readable string
     */
    public static String intSecToReadableSecond(int t, int level) {
        int sec = t % 60;
        int min = (t / 60) % 60;
        int hour = (t / 3600) % 24;
        int day = (t / (3600 * 24)) % 7;
//        int day = (t / (3600 * 24)) % 7;
//        int week = t / (3600 * 24 * 7);

        String sol = "";
        if (level >= 4) {
            sol = sec + " seconds" + sol;
        }

        if (level >= 3 && min != 0) {
            sol = min + " minute " + sol;
        }

        if (level >= 2 && hour != 0) {
            sol = hour + " hour " + sol;
        }

        if (level >= 1 && day != 0) {
            sol = day + " day " + sol;
        }

//        if (level >= 0 && week != 0)
//            sol = week + " week " + sol;
        return sol;
    }
}
