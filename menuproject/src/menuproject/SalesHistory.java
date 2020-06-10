package menuproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesHistory {
	SimpleStringProperty date;		//날짜
	SimpleIntegerProperty sales;	//날짜별 매출
	
	public SalesHistory(String date, int sales) {
		this.date = new SimpleStringProperty(date);
		this.sales = new SimpleIntegerProperty(sales);
	}


	public String getDate() {
		return date.get();
	}
	public void setDate(String date) {
		this.date.set(date);
	}
	
	public void setCode(int sales) {
		this.sales.set(sales);
	}
	public SimpleStringProperty codeDate() {
		return this.date;
	}
	
	public void setSales(int sales) {
		this.sales.set(sales);
	}
	
	public int getSales() {
		return sales.get();
	}
	
	public SimpleIntegerProperty codeSales() {
		return this.sales;
	}

}
