package menuproject;

import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SalesMain extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {

		//더미 데이터 10일치 삽입.
//		SalesDAO.getInstance().connect();
//		SalesDAO.getInstance().insertDummyData(10);


		Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		}

	public static void main(String[] args) {
		launch(args);
	}
}
