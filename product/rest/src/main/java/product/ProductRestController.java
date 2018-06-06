package product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Created by 11002237 on 05.06.2018.
 */
@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{productName}")
    public Collection<Product> readProductsByName(@PathVariable String productName) {
        return this.productRepository.findByName(productName);
    }

    @GetMapping("/{productName}/{brand}")
    public Collection<Product> readProductsByNameAndBrand(@PathVariable String productName, @PathVariable String brand) {
        return this.productRepository.findByNameAndBrand(productName, brand);
    }

    @GetMapping("/leftovers")
    public Collection<Product> readProductsByNameAndBrand() {
        return this.productRepository.findByNameAndBrand("", "");
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Product input) {
        Product result = this.productRepository.save(
                new Product(input.getName(), input.getBrand(), input.getPrice(), input.getQuantity())
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Product input) {
        Product result = this.productRepository.save(
                new Product(input.getName(), input.getBrand(), input.getPrice(), input.getQuantity())
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Product input) {
        this.productRepository.delete(
                new Product(input.getName(), input.getBrand(), input.getPrice(), input.getQuantity())
        );
      /*  URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();*/
      return null;
    }
}
