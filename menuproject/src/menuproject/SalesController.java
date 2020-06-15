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

	//액션 이벤트 상속받음.
	class SalesButtonEvent implements EventHandler<ActionEvent> {
		private SalesMenu selMenu = null;
		
		SalesButtonEvent(SalesMenu menu){
			this.selMenu = menu;
		}
		
		
		//기존 람다식에서 호출되는 이벤트에서는 어떤 메뉴가 선택되었는지 모름.
		//그래서 상속받은 객체에 menu를담아놓고, 호출시킴.
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
		
		//DAO를 이용해서 메뉴리스트를 가지고옴.
		List<SalesMenu> listMenu = instance.getMenuList();
		for (int i = 0; i < listMenu.size(); ++i) {
			//3개가 채워졌을 때, 혹은 0개일 때 hbox를  생성.
			if (i % 3 == 0) {
				hbox = new HBox();
				hbox.setAlignment(Pos.CENTER);
				vbox_menu.getChildren().add(hbox);
			}

			//hbox에다가 menu를 삽입.
			SalesMenu menu = listMenu.get(i);
			ImageView img = new ImageView(menu.getImage());
			Button btn = new Button(menu.toString(), img);

			//컨텐츠가 버튼의 위쪽에 있도록(버튼 글씨가 아래에 있고, 컨텐츠 이미지가 위쪽에 있다)
			btn.setContentDisplay(ContentDisplay.TOP);
			
			//버튼의 위치가 센터가 오도록.
			btn.setAlignment(Pos.BOTTOM_CENTER);

			//이벤트를 처리하는 객체를 세팅.
			btn.setOnAction(new SalesButtonEvent(menu));
			
			//기존 하던데로 하면, 뭐가 클릭 되었는지 알 수 없다.
//			btn.setOnAction(new EventHandler<ActionEvent>() {
//
//				@Override
//				public void handle(ActionEvent arg0) {
//					
//				}
//			});		
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
		addStage.setTitle("메뉴추가");
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
				//DAO에서 구매할 때, menuId가 필요한데, 이걸 selMenu에서 끌어옴.
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
			//그래프 껍데기 가져오기.
			Parent parent = FXMLLoader.load(getClass().getResource("SalesChart.fxml"));
			
			//그래프 그리는 차트접근을 위해서 lookup() 으로 객체 가져옴.
			LineChart barChart = (LineChart) parent.lookup("#lineChart");
 
			//BarChart에서 어떤 데이터가 들어가는지 그 형식을 세팅함.
			//XYChart에다가, String, Integer가 중요.
			//String축에는 date가 들어가고 Integer축에는 매출금액이 들어감.
			XYChart.Series<String, Integer> seriesSales = new XYChart.Series<String, Integer>();
			
			//실제 데이터를 삽입하기 위해서 빈 리스트를 생성. 
			ObservableList<XYChart.Data<String, Integer>> datasSales = FXCollections.observableArrayList();

			//dao를 통해 가져온 실제 데이터를 빈 리스트에다가 넣어줌.
			for (int i = 0; i < list.size(); i++) {
				datasSales.add(new XYChart.Data(list.get(i).getDate(), list.get(i).getSales()));
			}
			seriesSales.setData(datasSales);
			seriesSales.setName("일매출");

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
