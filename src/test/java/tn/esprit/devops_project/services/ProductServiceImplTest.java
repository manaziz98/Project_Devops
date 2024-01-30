package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class ProductServiceImplTest {
    @Autowired
    ProductServiceImpl productService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addProduct() {
        Product product = new Product();
        product.setTitle("Test Product");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setPrice(100.0F);
        product.setQuantity(10);

        assertThrows(NullPointerException.class,() -> this.productService.addProduct(product, 120l)) ;
        this.productService.addProduct(product, 1l);

        assertEquals(this.productService.retreiveAllProduct().size(), 3);
        assertEquals(this.productService.retrieveProduct(3L).getTitle(), "Test Product");
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveProduct() {


        assertThrows(NullPointerException.class,() -> this.productService.retrieveProduct(33L)) ;
        final Product product = this.productService.retrieveProduct(1L);
        assertEquals("Product 1", product.getTitle());

    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retreiveAllProduct() {
        final List <Product> productList = this.productService.retreiveAllProduct();
        assertEquals(productList.size(),2);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveProductByCategory() {
        final List<Product> electronicsProducts = this.productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);
        assertEquals(1, electronicsProducts.size());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void deleteProduct() {
        int productCount = this.productService.retreiveAllProduct().size();
        this.productService.deleteProduct(1L);

        int updatedProductCount = this.productService.retreiveAllProduct().size();
        assertEquals(productCount -1, updatedProductCount);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retreiveProductStock() {
        final List<Product> productInStock = this.productService.retreiveProductStock(1L);
        assertEquals(2,productInStock.size());
    }

}