package product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Product Not Found")
public class ProductsNotFoundException extends RuntimeException {

    public ProductsNotFoundException(String productName) {
        super("products not found by name '" + productName + "'.");
    }

    public ProductsNotFoundException(String productName, String brand) {
        super("products not found by name '" + productName + "' and brand '" + brand + "'.");
    }

    public ProductsNotFoundException(Long id) {
        super("products not found by id ' {}" + id + "'.");
    }
}
