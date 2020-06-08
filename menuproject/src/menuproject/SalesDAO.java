package menuproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



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
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
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
	public void insertMenu(String menuName, int price) {
		String sql = "insert into menu(name, price) values(?, ?)";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, menuName);
			pstmt.setInt(2, price);
			
			int r = pstmt.executeUpdate();
			System.out.println(r + "건 입력됨.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void deleteMenu(int menuId) {
		
	}
	
	public void buyMenuFromId(int menuId) {
		if(conn == null)
			return;
		
	}
	
	public void buyMenuFromName(String name) {
		if(conn == null)
			return;
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
				SalesMenu menu = new SalesMenu(rs.getInt("menu_id"), 
												rs.getString("name"),
												rs.getInt("price"));
				list.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//일별 판매기록 
	public List<SalesHistory> getDayHistory(){
		return null;
	}
	
	//월별 판매기록
	public List<SalesHistory> getMonthHistory(){
		return null;
	}
	
	//년도별 판매기록
	public List<SalesHistory> getYearHistory(){
		return null;
	}
}
