package ru.vorobyov.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.shop.entities.Product;
import org.springframework.data.domain.*;
import ru.vorobyov.shop.repositories.ProductRepository;
import java.util.List;

@Service

public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> fromMin(double min, Pageable pageable) {
        return productRepository.findByPriceGreaterThanEqual(min, pageable);
    }

    public Page<Product> toMax(double max, Pageable pageable) {
        return productRepository.findByPriceBefore(max, pageable);
    }

    public Page<Product> fromMinToMax(double min, double max, Pageable pageable) {
        return productRepository.findByPriceBetween(min, max, pageable);
    }

    public Page<Product> findById(long id, Pageable pageable) {
        return productRepository.findById(id, pageable);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    public void add(Product newProduct) {
        productRepository.save(newProduct);
    };
    
    public void deleteById (Long id) {
        productRepository.deleteById(id);
    }
    
}
