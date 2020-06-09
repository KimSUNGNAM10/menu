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
		SalesDAO db = SalesDAO.getInstance();
		if(db.connect()) {
			db.insertDummyData(30);
			
			
//			List<SalesMenu> list = db.getMenuList();
//			
//			Parent root = FXMLLoader.load(getClass().getResource("salesChart.fxml"));
//			Scene scene = new Scene(root);
//			primaryStage.setScene(scene);
//			primaryStage.show();
			
			
		}	
	}

	public static void main(String[] args) {
		launch(args);
	}
}
