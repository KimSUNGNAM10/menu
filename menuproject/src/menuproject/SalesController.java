package menuproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class SalesController implements Initializable {
	@FXML
	Button btnChart, btnOrder;
 
	ObservableList<SalesHistory> scores;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scores = FXCollections.observableArrayList();
		
		btnChart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				buttonChartAction(arg0);
			}
		});
//		
//		btnOrder.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				buttonAddAction(arg0);
//			}
//		});
		// TODO Auto-generated method stub
		 
	}
//	
//	public void buttonAddAction(ActionEvent ae) { 
//		Stage addStage = new Stage(StageStyle.UTILITY);
//		addStage.initModality(Modality.WINDOW_MODAL);
//		addStage.initOwner(btnOrder.getScene().getWindow());
//
//		try {
//			Parent parent = FXMLLoader.load(getClass().getResource("AddForm.fxml"));
//			Scene scene = new Scene(parent);
//			addStage.setResizable(false);
//			addStage.setScene(scene);
//			addStage.show();
//
//			Button btnFormAdd = (Button) parent.lookup("#btnFormAdd");
//			btnFormAdd.setOnAction(new EventHandler<ActionEvent>() {
//
//				@Override
//				public void handle(ActionEvent event) {
//					TextField txtName = (TextField) parent.lookup("#txtName");
//					TextField txtKorean = (TextField) parent.lookup("#txtKorean");
//					TextField txtMath = (TextField) parent.lookup("#txtMath");
//					TextField txtEnglish = (TextField) parent.lookup("#txtEnglish");
//
//					SalesHistory student = new SalesHistory(txtName.getText(), 
//							Integer.parseInt(txtKorean.getText())
//							);
//					scores.add(student);
//					addStage.close();
//				}
//
//			});
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	
	public void buttonChartAction(ActionEvent ae) { //sdsdss
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
