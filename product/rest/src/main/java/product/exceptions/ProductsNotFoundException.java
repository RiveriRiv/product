package product.exceptions;

public class ProductsNotFoundException extends RuntimeException {

    public ProductsNotFoundException(String productName) {
        super("products not found by name '" + productName + "'.");
    }

    public ProductsNotFoundException(String productName, String brand) {
        super("products not found by name '" + productName + "' and brand '" + brand + "'.");
    }
}
