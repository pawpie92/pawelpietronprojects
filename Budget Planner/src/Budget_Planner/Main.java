package Budget_Planner;

import Budget_Planner.Model.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.listTransactions();
        primaryStage.setTitle("Budget Planner");
        primaryStage.setScene(new Scene(root, 420, 600));
        primaryStage.show();
    }



    @Override
    public void init() throws Exception {
        super.init();

        if(!DataSource.getInstance().open())
        {
            System.err.println("ERROR: Couldn't connect to the database");;
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
