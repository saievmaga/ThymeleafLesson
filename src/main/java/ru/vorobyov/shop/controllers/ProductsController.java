package ru.vorobyov.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.vorobyov.shop.entities.Product;
import ru.vorobyov.shop.service.ProductService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping()
public class ProductsController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("")
	private RedirectView start() {
		return new RedirectView("products");
	}
	
	//получение всех товаров
	@GetMapping("/products")
	private String showProductsPage(Model model, @RequestParam (value = "sort_method", defaultValue = "fromMin") String sortMethod,
	                                            @RequestParam (value = "min", defaultValue = "0.0") String min,
	                                            @RequestParam (value = "max", defaultValue = "1500.0") String max) {
		List<Product> productList = null;
		
		if ("fromMin".equals(sortMethod)) {
			productList = productService.fromMin(Double.parseDouble(min));
		} else if ("toMax".equals(sortMethod)) {
			productList = productService.toMax(Double.parseDouble(max));
		} else if ("fromMinToMax".equals(sortMethod)) {
			productList = productService.fromMinToMax(Double.parseDouble(min), Double.parseDouble(max));
		}
		
		model.addAttribute("productList", productList);
		model.addAttribute("newProduct", new Product());
		
		return "products";
	}
	
	//получение товара по id
	@GetMapping("/products/{id}")
	private String getAllProducts(Model model, @PathVariable (name = "id") String id) {
		model.addAttribute("productList", productService.findById(Long.parseLong(id)));
		
		return "products";
	}
	
	//создание нового товара [ POST .../app/products ]
	@PostMapping("/products")
	private RedirectView addProduct(Model model, @ModelAttribute("newProduct") Product newProduct) {
		productService.add(newProduct);
		model.addAttribute("productList", productService.findAll());
		return new RedirectView("products");
	}
	
	@GetMapping("/products/delete")
	private String addProduct(Model model) {
		model.addAttribute("productList", productService.findAll());
		return "delete";
	}
	
	//удаление товара по id.[ GET .../app/products/delete/{id} ]
	@GetMapping("/products/delete/{id}")
	private String deleteById(Model model, @PathVariable (name = "id") String id) {
		productService.deleteById(Long.parseLong(id));
		model.addAttribute("productList", productService.findAll());
		return "delete";
	}
	
	@GetMapping("/products/update")
	private String updateDb(Model model) throws IOException, SQLException {
		List<String> dataUpdate = new ArrayList<>();
		dataUpdate = Files.readAllLines(Paths.get("src/main/resources/update.txt"), StandardCharsets.UTF_8);
		String url = "jdbc:mysql://localhost:3305/webhib?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true" +
			"&characterEncoding=UTF-8";
		String username = "mysql";
		String password = "mysql";
		Connection conn = DriverManager.getConnection(url, username, password);
		
		String sql = "UPDATE students SET score = ? WHERE id = ?;";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		
		
		conn.setAutoCommit(false);
		for (int i = 1; i < dataUpdate.size(); i++) {
			long id = Long.parseLong(dataUpdate.get(i).split(" ")[0]);
			int score = Integer.parseInt(dataUpdate.get(i).split(" ")[1]);
			
			preparedStatement.setInt(1, score);
			preparedStatement.setLong(2, id);
			
			preparedStatement.executeUpdate();
		}
		conn.setAutoCommit(true);
		
		model.addAttribute("productList", productService.findAll());
		return "delete";
	}
}
