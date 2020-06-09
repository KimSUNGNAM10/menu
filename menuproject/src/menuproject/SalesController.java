package menuproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class SalesController implements Initializable {
	@FXML
	Button btnChart, btnOrder;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		btnChart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				buttonChartAction(arg0);
			}
		});

	public void buttonChartAction(ActionEvent ae) {
		Stage chartStage = new Stage(StageStyle.UTILITY);
		chartStage.initModality(Modality.WINDOW_MODAL);
		chartStage.initOwner(btnChart.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("SalesChart.fxml"));
			BarChart barChart = (BarChart) parent.lookup("#lineChart");

			XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
			ObservableList<XYChart.Data<String, Integer>> datas = FXCollections.observableArrayList();
			
			for (int i = 0; i < scores.size(); i++) {
				datas.add(new XYChart.Data(scores.get(i).getName(), scores.get(i).getkorean()));

			}
			series.setData(datas);
			series.setName("");

			barChart.setData(FXCollections.observableArrayList(series));

			Scene scene = new Scene(parent);
			chartStage.setScene(scene);
			chartStage.show();
			chartStage.setResizable(false);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
