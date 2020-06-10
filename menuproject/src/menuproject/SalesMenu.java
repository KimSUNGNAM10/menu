package menuproject;

import java.io.InputStream;

import javafx.scene.image.Image;

public class SalesMenu {
	int	menuId;
	String name;
	int	price;
	Image image;
	
	public SalesMenu(int menuId, String name, int price, InputStream ifsImage) {
		this.menuId = menuId;
		this.name = name;
		this.price = price;
		if(ifsImage != null)
			this.image = new Image(ifsImage);
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
	public Image getImage() {
		return image;
	}
	public String toString() {
		return name + "\r\n" + String.valueOf(price) + "원";
	}
}
