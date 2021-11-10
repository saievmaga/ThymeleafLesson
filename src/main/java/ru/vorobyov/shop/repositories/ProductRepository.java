package ru.vorobyov.shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.shop.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findById(long id, Pageable pageable);

	Page<Product> findByPriceGreaterThanEqual(double min, Pageable pageable);

	Page<Product> findByPriceBefore(double max, Pageable pageable);

	Page<Product> findByPriceBetween(double min, double max, Pageable pageable);
	
	List<Product> findAll();
	
	@Override
	void deleteById(Long aLong);
	

}
