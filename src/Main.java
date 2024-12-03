import javafx.application.Application;
import javafx.stage.Stage;
import testtt.Testtt;

public class Main extends Application {
    public static void main(String[] args) {
        try {
            Testtt visual = new Testtt();

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}