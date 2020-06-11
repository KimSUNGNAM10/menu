package menuproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.sun.javafx.tk.Toolkit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;



public class SalesDAO {
	Connection conn = null;

	static SalesDAO instance;
	static SalesDAO getInstance() {
		if(instance == null)
			instance = new SalesDAO();
		return instance;
	}
	
	private SalesDAO() {
	}
	
	public boolean connect() {
		if(conn != null)
			return true;
		
		String url = "jdbc:oracle:thin:@192.168.0.98:1521:xe";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, "hr", "hr");
			if(conn != null)
				return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//메뉴이름, 가격을 받아서 메뉴를 추가함.
	public boolean insertMenu(String menuName, int price, String path) {
		if(conn == null)
			return false;
		
		FileInputStream fis;
		File file = new File(path);
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		
		
		String sql = "INSERT INTO menu(menu_id, name, price, image) "
				+ "values(menu_seq.nextval, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, menuName);
			pstmt.setInt(2, price);
			pstmt.setBinaryStream(3, fis, file.length());
			int r = pstmt.executeUpdate();
			pstmt.close();
			System.out.println(r + "건 입력됨.");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void deleteMenu(int menuId) {
		
	}
	
	public void buyMenuFromId(int day, int menuId) {
		if(conn == null)
			return;
		
		String sql = String.format("insert into sales_history(order_no, sale_date, menu_id, price) "
				+ "values(sales_history_seq.nextval, "
				+ "sysdate-%d, "
				+ "%d, "
				+ "(SELECT price FROM menu WHERE menu_id = %d))"
				, day, menuId, menuId);
		
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			int r = pstmt.executeUpdate();
			System.out.println(r + "건 입력됨.");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void buyMenuFromName(String name) {
		if(conn == null)
			return;
	}
	
	//days=30 이면 30일치 더미데이터.
	public void insertDummyData(int days) {
		//특정 구간에 random개의 상품이 판매되는 더미 데이터 생성.
		for(int i=0; i<days; ++i) {
			//하루당 0~100개 데이터 삽입
			double randOrderCount = Math.random() * 100;	
			for(int j=0; j<randOrderCount; ++j) {	
				//메뉴는 메뉴번호  1~4번까지 랜덤하게 선택
				int randMenu = (int)(Math.random() * 4) + 1;
				buyMenuFromId(i, randMenu);
			}
		}
	}
	
	public List<SalesMenu> getMenuList() {
		if(conn == null)
			return null;
		
		List<SalesMenu> list = new ArrayList<>();
		String sql = "SELECT * FROM menu";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SalesMenu menu = new SalesMenu( rs.getInt("menu_id"), 
												rs.getString("name"),
												rs.getInt("price"),
												rs.getBinaryStream("image"));
				list.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//일별 판매기록 
	public ObservableList<SalesHistory> getDayHistory(){
		if(conn == null)
			return null;
		
		ObservableList<SalesHistory> list = FXCollections.observableArrayList();
		
		String sql  = "SELECT TO_CHAR(sale_date,'yyyymmdd') AS d, sum(price) AS sales" + 
					" FROM sales_history" + 
					" GROUP BY TO_CHAR(sale_date,'yyyymmdd')" + 
					" ORDER BY TO_CHAR(sale_date, 'yyyymmdd')"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				SalesHistory salesHistory = new SalesHistory(rs.getString("d"), rs.getInt("sales"));
				list.add(salesHistory);
				System.out.println(salesHistory.getDate() + " " + salesHistory.getSales());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//월별 판매기록
	public ObservableList<SalesHistory> getMonthHistory(){
		if(conn == null)
			return null;
					
		String sql  = "SELECT TO_CHAR(sale_date,'yyyymm') AS d, sum(price) AS sales" + 
				" FROM sales_history" + 
				" GROUP BY TO_CHAR(sale_date,'yyyymm')" + 
				" ORDER BY TO_CHAR(sale_date, 'yyyymm')"; 
		return null;
	}
	
	//년도별 판매기록
	public List<SalesHistory> getYearHistory(){
		return null;
	}

}
