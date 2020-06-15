package menuproject;

public class SalesHistory {
	String date;		//날짜
	int sales;	//날짜별 매출
	
	public SalesHistory(String date, int sales) {
		this.date = date;
		this.sales = sales;
	}

	public String getDate() {
		return date;
	}
	
	public int getSales() {
		return sales;
	}
	

}
