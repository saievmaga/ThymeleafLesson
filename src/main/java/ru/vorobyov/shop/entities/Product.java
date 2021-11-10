package ru.vorobyov.shop.entities;

import javax.persistence.*;

@Entity
@Table(name = "product_list")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	private double price;
	
	public Product(String title, double price) {
		this.title = title;
		this.price = price;
	}
	
	public Product() {
	}
	
	public long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}
