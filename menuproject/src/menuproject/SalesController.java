package menuproject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

public class SalesController implements Initializable {
	@FXML
	Button btnChart, btnAdd;

	@FXML
	ScrollPane scrPane;

	@FXML
	VBox vbox_menu;

	ObservableList<SalesHistory> scores;

	class SalesButtonEvent implements EventHandler<ActionEvent> {
		private SalesMenu selMenu = null;
		
		SalesButtonEvent(SalesMenu menu){
			this.selMenu = menu;
		}
		
		@Override
		public void handle(ActionEvent event) {
			if (selMenu == null)
				return;
			buttonOrderAction(event, selMenu);
		}

	}
	
	void initShowMenu() {
		SalesDAO instance = SalesDAO.getInstance();
		instance.connect();

		//hbox들 삭제.
		while(vbox_menu.getChildren().size() > 0) {
			vbox_menu.getChildren().remove(0);
		}
		
		HBox hbox = null;
		List<SalesMenu> listMenu = instance.getMenuList();
		for (int i = 0; i < listMenu.size(); ++i) {
			if (i % 3 == 0) {
				hbox = new HBox();
				hbox.setAlignment(Pos.CENTER);
				vbox_menu.getChildren().add(hbox);
			}

			SalesMenu menu = listMenu.get(i);
			ImageView img = new ImageView(menu.getImage());
			Button btn = new Button(menu.toString(), img);

			btn.setContentDisplay(ContentDisplay.TOP);
			btn.setAlignment(Pos.BOTTOM_CENTER);

			btn.setOnAction(new SalesButtonEvent(menu));

			img.setFitWidth(200);
			img.setFitHeight(200);
			hbox.getChildren().add(btn);

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scores = FXCollections.observableArrayList();

		initShowMenu();

		btnChart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				buttonChartAction(arg0);
			}
		});

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				buttonAddAction(arg0);
			}
		});
	}

	public void buttonAddAction(ActionEvent ae) { // window style 지정';';
		Stage addStage = new Stage(StageStyle.UTILITY);
		addStage.initModality(Modality.WINDOW_MODAL);
		addStage.initOwner(btnAdd.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("AddMenu.fxml"));
			Scene scene = new Scene(parent);
			addStage.setResizable(false);
			addStage.setScene(scene);
			addStage.show();

			Button btnRouteAdd = (Button) parent.lookup("#route");
			btnRouteAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// 파일선택
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Open Resource File");
					fileChooser.getExtensionFilters()
							.addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
					File file = fileChooser.showOpenDialog(addStage);
					if (file != null) {
						// 파일의 경로
						String path = file.getPath();

						TextField txtRoute = (TextField) parent.lookup("#txtRoute");
						txtRoute.setText(path);
					}
				}
			});

			Button btnMenuAdd = (Button) parent.lookup("#btnok");
			btnMenuAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TextField txtRoute = (TextField) parent.lookup("#txtRoute");
					TextField txtMenuName = (TextField) parent.lookup("#txtMenuName");
					TextField txtPrice = (TextField) parent.lookup("#txtPrice");

					SalesDAO instance = SalesDAO.getInstance();
					instance.connect();
					if (instance.insertMenu(txtMenuName.getText(), Integer.parseInt(txtPrice.getText()),
							txtRoute.getText())) {
						System.out.println("메뉴추가 성공");
					} else {
						System.out.println("메뉴추가 실패");
					}
					initShowMenu();
				}

			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buttonOrderAction(ActionEvent ae, SalesMenu selMenu) {
		Stage addStage = new Stage(StageStyle.UTILITY);
		addStage.initModality(Modality.WINDOW_MODAL);
		addStage.initOwner(btnChart.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("oorder.fxml"));
			Scene scene = new Scene(parent);
			addStage.setResizable(false);
			addStage.setScene(scene);
			addStage.show();

			Button btnOrder = (Button) parent.lookup("#order");
			btnOrder.setOnAction((evt)-> {				
				SalesDAO.getInstance().buyMenuFromId(0, selMenu.menuId);
				addStage.close();
			});
			
			Button btnOrderCancel = (Button) parent.lookup("#ordercancel");
			btnOrderCancel.setOnAction((evt)->{
				addStage.close();
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buttonChartAction(ActionEvent ae) { 
		Stage chartStage = new Stage(StageStyle.UTILITY);
		chartStage.initModality(Modality.WINDOW_MODAL);
		chartStage.initOwner(btnChart.getScene().getWindow());

		SalesDAO dao = SalesDAO.getInstance();
		dao.connect();
		ObservableList<SalesHistory> list = dao.getDayHistory();
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("SalesChart.fxml"));
			LineChart barChart = (LineChart) parent.lookup("#lineChart");

			XYChart.Series<String, Integer> seriesSales = new XYChart.Series<String, Integer>();
			ObservableList<XYChart.Data<String, Integer>> datasSales = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				datasSales.add(new XYChart.Data(list.get(i).getDate(), list.get(i).getSales()));

			}
			seriesSales.setData(datasSales);
			seriesSales.setName("매출");

			barChart.setData(FXCollections.observableArrayList(seriesSales));

			Scene scene = new Scene(parent);
			chartStage.setScene(scene);
			chartStage.show();
			chartStage.setResizable(false);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
