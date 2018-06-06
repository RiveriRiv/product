package product;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by User on 31.05.2018.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Product> findByName(String name);
    Collection<Product> findByNameAndBrand(String name, String brand);
}
