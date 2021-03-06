package eproctor.commons;

import eproctor.proctor.ProctorFormController;
import eproctor.student.ReviewFormController;
import eproctor.student.StudentFormController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Frame form Controller class
 *<p> this method initiate the whole frame of the application after user login the eProctor
 * @author Chen Liyang
 * @author Gong Yue
 * @author Lu ShengLiang
 */
public class FrameFormController implements Initializable {

    private Stage selfStage;
    private AnchorPane studentView, proctorView, coordinatorView, settingView, reviewView;
    private StackPane aboutView;

    @FXML
    private BorderPane mainPane;

    @FXML
    private ImageView settingImageView;

    @FXML
    private ImageView aboutImageView;

    @FXML
    private ImageView logoutImageView;

    @FXML
    private Label settingLabel;

    @FXML
    private Label aboutLabel;

    @FXML
    private Label logoutLabel;

    /**
     * Initializes the controller class.
     *
     * @param url an absolute URL giving the base location of the image
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     *Configure the startup page of student and display student home page
     */
    @FXML
    public void openStudentForm() {
        if (studentView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproctor/student/StudentForm.fxml"));
            try {
                studentView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            StudentFormController studentFormController = loader.getController();
            studentFormController.setFrameFormController(this);
        }
        mainPane.setCenter(studentView);
    }

    /**
     *Configure the startup page of proctor and display proctor home page
     */
    @FXML
    public void openProctorForm() {
        if (proctorView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproctor/proctor/ProctorForm.fxml"));
            try {
                proctorView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        mainPane.setCenter(proctorView);
    }
    /**
     * Open setting page and display to user
     * @throws Exception 
     */
    @FXML
    private void openSettingForm() throws Exception {
        if (settingView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproctor/commons/SettingForm.fxml"));
            try {
                settingView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            SettingFormController settingFormController = loader.getController();
            settingFormController.setFrameFormController(this);
            settingFormController.setStage(selfStage);
        }
        mainPane.setCenter(settingView);
    }
    /**
     * Open about page and display to user
     */
    @FXML
    private void openAboutForm() {
        if (aboutView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproctor/commons/AboutForm.fxml"));
            try {
                aboutView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            AboutFormController aboutFormController = loader.getController();
            aboutFormController.setFrameFormController(this);
        }
        mainPane.setCenter(aboutView);
    }
    /**
     * Conducting the action after the user logout from eProctor
     * <p> reset the stage, control the logout process
     * @throws Exception 
     */
    @FXML
    private void logout() throws Exception {
        System.out.println("logout");
        selfStage.close();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproctor/commons/LoginForm.fxml"));
        Parent root = (Parent) loader.load();
        LoginFormController controller = (LoginFormController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setTitle("eProctor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    /**
     *This method display the result of the exam student took
     * <p> 
     * @param recordRow
     * @param courseRow
     */
    public void openReviewView(DatabaseInterface.RecordRowStudent recordRow, DatabaseInterface.CourseRow courseRow) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eproctor/student/ReviewForm.fxml"));
        try {
            reviewView = loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ReviewFormController controller = (ReviewFormController) loader.getController();
        controller.setFrameFormController(this);
        controller.setRecordRow(recordRow);
        controller.setCourseRow(courseRow);
        controller.updateDetails();

        mainPane.setCenter(reviewView);
    }

    /**
     *Perform the action of close the review page of user (Student , Proctor)
     */
    public void closeReviewView() {
        mainPane.setBottom(null);
        this.openStudentForm();
    }

    /**
     *Set tool like "Open setting view", "Open about view" and "log out"
     */
    public void setToolTips() {
        this.settingLabel.setTooltip(new Tooltip("Open setting view"));
        this.aboutLabel.setTooltip(new Tooltip("Open about view"));
        this.logoutLabel.setTooltip(new Tooltip("Log out"));
    }

    /**
     *Set background
     */
    public void setBackground() {
    }

    /**
     *Set selfStage by assign selfStage
     * @param selfStage
     */
    public void setSelfStage(Stage selfStage) {
        this.selfStage = selfStage;
    }
}
