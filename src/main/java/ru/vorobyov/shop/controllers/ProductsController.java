package ru.vorobyov.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import ru.vorobyov.shop.entities.Product;
import ru.vorobyov.shop.service.ProductService;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/shop")
public class ProductsController {

    Logger logger = Logger.getLogger("ProductsController.class");

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    private String start(Model model) {
        model.addAttribute("sort_method", "fromMin");
        model.addAttribute("min", 0.0);
        model.addAttribute("max", 9999.0);

        return "products";

        //получение всех товаров
        @GetMapping("/products")
        private String showProductsPage (Model model,
                @RequestParam(value = "sort_method", defaultValue = "fromMin") String sortMethod,
                @RequestParam(value = "min", defaultValue = "0.0") String min,
                @RequestParam(value = "max", defaultValue = "9999.0") String max,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "5") int size){

            model.addAttribute("sort_method", sortMethod);
            model.addAttribute("min", Double.parseDouble(min));
            model.addAttribute("max", Double.parseDouble(max));

            Page<Product> productPage = null;
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "price"));

            if ("fromMin".equals(sortMethod)) {
                productPage = productService.fromMin(Double.parseDouble(min), pageable);
            } else if ("toMax".equals(sortMethod)) {
                productPage = productService.toMax(Double.parseDouble(max), pageable);
            } else if ("fromMinToMax".equals(sortMethod)) {
                productPage = productService.fromMinToMax(Double.parseDouble(min), Double.parseDouble(max), pageable);
            }

            model.addAttribute("productPage", productPage);

            int totalPages = productPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }

            model.addAttribute("newProduct", new Product());

            return "products";
        }

        //создание нового товара [ POST .../app/products ]
        @PostMapping("/products")
        private String addProduct (Model model, @ModelAttribute("newProduct") Product newProduct){
            productService.add(newProduct);
            return "redirect:/shop/products";
        }

        @GetMapping("/products/{id}")
        private String getProductById (Model model, @PathVariable(name = "id") String id){

            Pageable currentPage = PageRequest.of(0, 10);
            model.addAttribute("productPage", productService.findById(Long.parseLong(id), currentPage));
            return "products";
        }

        //удаление товара по id.[ GET .../app/products/delete/{id} ]
        @GetMapping("/products/delete/{id}")
        private String deleteById (Model model,@ModelAttribute(name = "id") long id){
            productService.deleteById(id);
            return "redirect:/shop/products";
        }

    }
}
