package product;


import javax.persistence.*;

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

    private Product() {}

    public Product(final String name, final String brand, final Double price, final Integer quantity) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
    }

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
