package main.java.product;

/**
 * Created by 11002237 on 05.06.2018.
 */
@RestController
@RequestMapping("/{userId}/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{productName}")
    public Collection<Product> readProductsByName(@PathVariable String productName) {

    }


}
