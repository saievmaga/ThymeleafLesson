package ru.vorobyov.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.shop.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findById(long id);
	
	List<Product>  findByPriceAfter(double max);
	
	List<Product> findByPriceBefore(double min);
	
	List<Product> findByPriceAfterAndPriceBefore(double min, double max);
	
	List<Product> findAll();
	
	@Override
	void deleteById(Long aLong);
	

}
