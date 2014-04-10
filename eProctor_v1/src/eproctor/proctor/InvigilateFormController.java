/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eproctor.proctor;

import eproctor.commons.DatabaseInterface;
import eproctor.commons.MessagePull;
import eproctor.commons.MessageSend;
import eproctor.commons.VideoServerInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yue
 */
public class InvigilateFormController implements Initializable {

    private Stage selfStage;
    private ArrayList<DatabaseInterface.StudentRow> students;
    private String courseCode, sessionCode;

    public void setCourseCode(String courseCode) {
        System.out.println("xxxxxxx: " + courseCode);
        this.courseCode = courseCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public void setStudents(ArrayList<DatabaseInterface.StudentRow> students) {
        this.students = students;
    }

    public void showStudents() {
        for (DatabaseInterface.StudentRow student : students) {
            InfoPane temp = new InfoPane();
            temp.setStudent(student);
            temp.startReceive();
            flowPane.getChildren().add(temp);
        }
    }

    @FXML
    FlowPane flowPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    void setStage(Stage stage) {
        selfStage = stage;
    }

    public class InfoPane extends TitledPane {

        private DatabaseInterface.StudentRow student;

        private ImageView imgWebcam;
//        private ImageView imgDesktop;
//        private Button btnTerminate;
        private TextArea msgReceived;
        private TextField msgToSend;
        private Button btnSend;
        private VideoServerInterface serviceReceiveImage;
        private ChoiceBox msgType;
        private MessagePull mp;

        public InfoPane() {
            initializeUI();
        }

        private void initializeUI() {
            btnSend = new Button("Send");
            btnSend.setOnAction((ActionEvent e) -> {
                btnSend.setDisable(true);
                String typeTemp = (String) msgType.getValue();
                int type;
                if (typeTemp.equals("message")) {
                    type = 0;
                } else if (typeTemp.equals("warning")) {
                    type = 1;
                } else {
                    type = 2;
                }

                MessageSend ms = new MessageSend(DatabaseInterface.username, student.getUsername(), courseCode, sessionCode, msgToSend.getText(), new Date(), type);
                ms.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                    @Override
                    public void handle(WorkerStateEvent t) {
                        System.out.println("message sent.");
                        msgToSend.setText("");
//                        msgProgressIndicator.setProgress(100);
                        btnSend.setDisable(false);
                    }
                });
                ms.setOnFailed(new EventHandler<WorkerStateEvent>() {

                    @Override
                    public void handle(WorkerStateEvent t) {
                        System.out.println("message failed.");
//                        msgProgressIndicator.setProgress(100);
                        btnSend.setDisable(false);
                    }
                });
                ms.start();
            });
            mp = new MessagePull(DatabaseInterface.username, courseCode, sessionCode);
            mp.start();
            
            msgReceived = new TextArea();
            msgReceived.setMinWidth(170);
            msgToSend = new TextField();
            msgToSend.setMinWidth(180);
            VBox chatBox = new VBox(5);
            chatBox.setAlignment(Pos.BOTTOM_RIGHT);
            chatBox.setMaxSize(180, 80);
            Separator separator = new Separator();
            separator.setOrientation(Orientation.VERTICAL);
            msgType = new ChoiceBox(FXCollections.observableArrayList("message", "warning", "expelling"));
            msgType.setMinWidth(100);
            HBox sendLine = new HBox(20);
            sendLine.getChildren().addAll(msgType, btnSend);
            chatBox.getChildren().addAll(msgToSend, sendLine);
//            btnTerminate = new Button("Terminate Exam");
            HBox bottom = new HBox();
            bottom.setAlignment(Pos.CENTER);
            bottom.setMinSize(400, 100);
            bottom.setMaxSize(400, 100);
//            bottom.getChildren().addAll(btnTerminate, chatBox);
            bottom.getChildren().addAll(msgReceived, separator, chatBox);
            Image image1 = new Image("/eproctor/images/studentHome.png");
            imgWebcam = new ImageView();
            imgWebcam.setImage(image1);
            imgWebcam.setFitWidth(400);
//            imgWebcam.setFitHeight(300);
            imgWebcam.setFitHeight(600);
//            Image image2 = new Image("/eproctor/images/loginScreen.png");
//            imgDesktop = new ImageView();
//            imgDesktop.setImage(image2);
//            imgDesktop.setFitWidth(400);
//            imgDesktop.setFitHeight(300);
            VBox pane = new VBox();
            pane.setMinSize(400, 700);
//            pane.getChildren().addAll(imgWebcam, imgDesktop, bottom);
            pane.getChildren().addAll(imgWebcam, bottom);
            this.setContent(pane);
            this.setText(student.getUsername());
        }

        public void setStudent(DatabaseInterface.StudentRow student) {
            this.student = student;
        }

        public void startReceive() {
            System.out.println("student's name: " + student.getName());
            System.out.println("InfoPane: " + DatabaseInterface.userCode + " " + student.getUsername() + " " + "localhost" + 6002 + courseCode + sessionCode);
            serviceReceiveImage = new VideoServerInterface(DatabaseInterface.username, student.getUsername(), "localhost", 6002, courseCode, sessionCode);
            imgWebcam.imageProperty().bind(serviceReceiveImage.valueProperty());
            serviceReceiveImage.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent t) {
                    System.out.println("serviceReceiveImage cancelled.");
                }
            });
            serviceReceiveImage.setOnFailed(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent t) {
                    System.out.println("serviceReceiveImage failed.");
                }
            });
            serviceReceiveImage.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent t) {
                    System.out.println("serviceReceiveImage succeeded.");
                }
            });
            serviceReceiveImage.start();
        }

    }
}
