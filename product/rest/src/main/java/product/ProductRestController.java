package product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import product.exceptions.ProductsNotFoundException;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by 11002237 on 05.06.2018.
 */
@RestController
@RequestMapping("/products")
public class ProductRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Collection<Product> readProducts() {
        logger.debug("Trying to find all products");
        return this.productRepository.findAll();
    }

    @GetMapping("/name/{productName}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Collection<Product> readProductsByName(@PathVariable String productName) {
        logger.debug("Trying to find products by name: {}", productName);
        return this.productRepository.findByName(productName);
    }

    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Collection<Product> readProductsByBrand(@PathVariable String brand) {
        logger.debug("Trying to find products by brand: {}", brand);
        return this.productRepository.findByBrand(brand);
    }

    @GetMapping("/name/{productName}/brand/{brand}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Collection<Product> readProductsByNameAndBrand(@PathVariable String productName, @PathVariable String brand) {
        logger.debug("Trying to find products by productName: {} and brand: {}", productName, brand);
        return this.productRepository.findByNameAndBrand(productName, brand);
    }

    @GetMapping("/leftovers")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Collection<Product> readProductsByNameAndBrand() {
        logger.debug("Trying to find leftovers");
        return this.productRepository.findByQuantityLessThanEqual(5);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@RequestBody Product input) {
        logger.debug("Trying to save product {} ", input.getName());
        Product result = this.productRepository.save(
                new Product(input.getName(), input.getBrand(), input.getPrice(), input.getQuantity())
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        logger.debug("Product was created with id {}", result.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody Product input) {
        Optional<Product> optional = this.productRepository.findById(
                input.getId()
        );
        if (!optional.isPresent()) {
            logger.error("product with id {} not found", input.getId());
            throw new ProductsNotFoundException(input.getId());
        }

        logger.debug("Trying to update product with id {}", input.getId());
        Product result = this.productRepository.save(
                new Product(input.getId(), input.getName(), input.getBrand(), input.getPrice(), input.getQuantity())
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        logger.debug("Product with id {} was updated", input.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestBody Product input) {
        Optional<Product> optional = this.productRepository.findById(
                input.getId()
        );
        if (optional.isPresent()) {
            this.productRepository.deleteById(
                    input.getId()
            );
        } else {
            logger.error("product with id {} not found", input.getId());
            throw new ProductsNotFoundException(input.getId());
        }

        logger.debug("product with id {} was deleted", input.getId());
      return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
}
