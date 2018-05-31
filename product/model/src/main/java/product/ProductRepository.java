package product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by User on 31.05.2018.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByroductName(String name);
}
