package product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by User on 31.05.2018.
 */
@Entity
@Table(
        name="product",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"name", "brand"})
)
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String brand;

    private Double price;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
