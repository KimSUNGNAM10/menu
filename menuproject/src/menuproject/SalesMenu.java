package menuproject;

public class SalesMenu {
	int	menuId;
	String name;
	int	price;
	
	public SalesMenu(int menuId, String name, int price) {
		this.menuId = menuId;
		this.name = name;
		this.price = price;
	}
	
	public int getMenuId() {
		return menuId;
	}
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
}
