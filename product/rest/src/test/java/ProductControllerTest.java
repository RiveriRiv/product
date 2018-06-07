import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import product.Application;
import product.Product;
import product.ProductRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ProductControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private List<Product> products = new ArrayList<>();

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        this.productRepository.deleteAllInBatch();

        this.products.add(productRepository.save(new Product("product1", "brand1", 5.5, 4)));
        this.products.add(productRepository.save(new Product("product2", "brand1", 5.2, 6)));
        this.products.add(productRepository.save(new Product("product3", "brand2", 4.8, 5)));
    }

    @Test
    @WithMockUser(username="admin")
    public void productNotFound() throws Exception {
        mockMvc.perform(put("/products/update")
                .with(csrf())
                .content(this.json(new Product(100L,null, null, null, null)))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin")
    public void addProduct() throws Exception {
        mockMvc.perform(post("/products/add")
                .with(csrf())
                .content(this.json(new Product("name", "brand", 0.0, 0)))
                .contentType(contentType))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="user")
    public void readProductByName() throws Exception {
        Product product = this.products.get(0);
        mockMvc.perform(get("/products/name/"
                + product.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].id", is(product.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(product.getName())))
                .andExpect(jsonPath("$[0].brand", is(product.getBrand())))
                .andExpect(jsonPath("$[0].price", is(product.getPrice())))
                .andExpect(jsonPath("$[0].quantity", is(product.getQuantity())));
    }

    @Test
    @WithMockUser(username="user")
    public void readProductByNameAndBrand() throws Exception {
        Product product = this.products.get(1);
        mockMvc.perform(get("/products/name/brand/" + product.getName() + "&"
                + product.getBrand()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].id", is(product.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(product.getName())))
                .andExpect(jsonPath("$[0].brand", is(product.getBrand())))
                .andExpect(jsonPath("$[0].price", is(product.getPrice())))
                .andExpect(jsonPath("$[0].quantity", is(product.getQuantity())));
    }

    @Test
    @WithMockUser(username="user")
    public void readAllProducts() throws Exception {
        Product product1 = this.products.get(0);
        Product product2 = this.products.get(1);
        Product product3 = this.products.get(2);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(product1.getName())))
                .andExpect(jsonPath("$[0].brand", is(product1.getBrand())))
                .andExpect(jsonPath("$[0].price", is(product1.getPrice())))
                .andExpect(jsonPath("$[0].quantity", is(product1.getQuantity())))

                .andExpect(jsonPath("$[1].id", is(product2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(product2.getName())))
                .andExpect(jsonPath("$[1].brand", is(product2.getBrand())))
                .andExpect(jsonPath("$[1].price", is(product2.getPrice())))
                .andExpect(jsonPath("$[1].quantity", is(product2.getQuantity())))

                .andExpect(jsonPath("$[2].id", is(product3.getId().intValue())))
                .andExpect(jsonPath("$[2].name", is(product3.getName())))
                .andExpect(jsonPath("$[2].brand", is(product3.getBrand())))
                .andExpect(jsonPath("$[2].price", is(product3.getPrice())))
                .andExpect(jsonPath("$[2].quantity", is(product3.getQuantity())));
    }

    @Test
    @WithMockUser(username="admin")
    public void deleteProduct() throws Exception {
        Product product = this.products.get(1);

        String productJson = json(new Product(
                product.getId(), product.getName(), product.getBrand(), product.getPrice(), product.getQuantity()
                )
        );

        mockMvc.perform(delete("/products/delete")
                .with(csrf())
                .contentType(contentType)
                .content(productJson))
                .andExpect(status().isNoContent());

        productJson = json(new Product(
                        1000000L, product.getName(), product.getBrand(), product.getPrice(), product.getQuantity()
                )
        );

        mockMvc.perform(delete("/products/delete")
                .with(csrf())
                .contentType(contentType)
                .content(productJson))
                .andExpect(status().is4xxClientError());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
