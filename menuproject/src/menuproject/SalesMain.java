package menuproject;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SalesMain extends Application {
	class SalesButtonEvent implements EventHandler<ActionEvent> {
		private SalesMenu menu = null;
		SalesButtonEvent(SalesMenu menu){
			this.menu = menu;
		}
		
		@Override
		public void handle(ActionEvent event) {
			if (menu == null)
				return;
			
			System.out.println(menu.name);
		}

	}

	void testInsertDummyData() {
//		//더미 데이터 10일치 삽입.
		SalesDAO dao = SalesDAO.getInstance();
		dao.connect();
		dao.insertDummyData(10);

	}
 
	void testInsertMenu(Stage stage) {
		//파일선택
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			//파일의 경로
			String path = file.getPath();
			SalesDAO instance = SalesDAO.getInstance();
			instance.connect();
			instance.insertMenu("테스트 메뉴", 1000, path);
		}
	}

	public void testSelectMenu() {
		SalesDAO instance = SalesDAO.getInstance();
		instance.connect();
		instance.getMenuList();
	}

	public void testCodeStage(Stage stage) {

		SalesDAO instance = SalesDAO.getInstance();
		instance.connect();
		List<SalesMenu> listMenu = instance.getMenuList();

		VBox root = new VBox();
		root.setPrefWidth(670);
		root.setPrefHeight(500);

		VBox vb = new VBox();

		ScrollPane scrp = new ScrollPane();
		scrp.setPrefSize(100, 400);
		scrp.setContent(vb);

		root.getChildren().add(scrp);

		HBox hbox = null;
		for (int i = 0; i < listMenu.size(); ++i) {
			if (i % 3 == 0) {
				hbox = new HBox();
				hbox.setAlignment(Pos.CENTER);
				vb.getChildren().add(hbox);
			}

			SalesMenu menu = listMenu.get(i);
			ImageView img = new ImageView(menu.getImage());
			Button btn = new Button(menu.toString(), img);

			btn.setContentDisplay(ContentDisplay.TOP);
			btn.setAlignment(Pos.BOTTOM_CENTER);

			
			;
			
			btn.setOnAction(new SalesButtonEvent(menu));

			img.setFitWidth(200);
			img.setFitHeight(200);
			hbox.getChildren().add(btn);

		}

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SalesDAO instance = SalesDAO.getInstance();
		if(instance.connect()) {
			System.out.println("접속 성공");
		}else {
			System.out.println("접속 실패");
		}
		
		Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
