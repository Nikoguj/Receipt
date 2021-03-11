package com.back.receipt.service;

import com.back.receipt.domain.Product;
import com.back.receipt.domain.UserPartPriceProduct;
import com.back.receipt.domain.dto.UserPartPriceDto;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.repository.ProductRepository;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private Product product = new Product();

    @BeforeEach
    public void initz() {
        product.setName("name1");
        product.setQuantity(3);
        product.setPriceForOne(5.5);
        product.setPrice(16.5);

        product = productRepository.save(product);
    }

    @AfterEach
    public void deleteProduct() {
        try {
            productRepository.deleteById(product.getId());
        } catch (Exception e) {

        }
    }

    @Test
    void getProduct() {
        //Given

        //When
        Product returnProduct = null;
        try {
            returnProduct = productService.getProduct(product.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Assert.assertEquals(product.getId(), returnProduct.getId());
        Assert.assertEquals(product.getName(), returnProduct.getName());
        Assert.assertEquals(product.getQuantity(), returnProduct.getQuantity(), 0.02);
        Assert.assertEquals(product.getPrice(), returnProduct.getPrice(), 0.02);

        //Clean up
        productRepository.deleteById(returnProduct.getId());
    }

    @Test
    void setPartPriceForNewUser() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        Product saveProduct = productRepository.save(product);

        User user = new User();
        user.setId(1L);
        user.setUserName("username1");
        user.setPassword("passowrd1");
        user.setEmail("email1");
        user.setActive(true);

        User saveUser = userRepository.save(user);

        //When
        try {
            productService.setPartPriceForNewUser(saveProduct.getId(), saveUser.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        Product returnProduct = productRepository.findById(saveProduct.getId()).get();

        //Then
        Assert.assertEquals("username1", returnProduct.getUserPartPrice().get(0).getName());
        Assert.assertEquals(0.0, returnProduct.getUserPartPrice().get(0).getPartPrice(), 0.02);

        //Clean up
        userRepository.deleteById(saveUser.getId());
        productRepository.deleteById(saveProduct.getId());
    }

    @Test
    void setPartPriceForAddReceiptUser() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        Product saveProduct = productRepository.save(product);

        User user = new User();
        user.setId(1L);
        user.setUserName("username1");
        user.setPassword("passowrd1");
        user.setEmail("email1");
        user.setActive(true);

        User saveUser = userRepository.save(user);

        //When
        try {
            productService.setPartPriceForAddReceiptUser(saveProduct.getId(), saveUser.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        Product returnProduct = productRepository.findById(saveProduct.getId()).get();

        //Then
        Assert.assertEquals("username1", returnProduct.getUserPartPrice().get(0).getName());
        Assert.assertEquals(1.0, returnProduct.getUserPartPrice().get(0).getPartPrice(), 0.02);

        //Clean up
        userRepository.deleteById(saveUser.getId());
        productRepository.deleteById(saveProduct.getId());
    }

    @Test
    void getUserPartPriceProduct() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        UserPartPriceProduct userPartPriceProduct1 = new UserPartPriceProduct("user1", 10.0);
        userPartPriceProduct1.setProduct(product);

        UserPartPriceProduct userPartPriceProduct2 = new UserPartPriceProduct("user2", 7.55);
        userPartPriceProduct2.setProduct(product);

        product.setUserPartPrice(new ArrayList<>(Arrays.asList(
                userPartPriceProduct1, userPartPriceProduct2
        )));

        Product saveProduct = productRepository.save(product);
        //When
        List<UserPartPriceProduct> userPartPriceProductList = null;
        try {
            userPartPriceProductList = productService.getUserPartPriceProduct(saveProduct.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Assert.assertEquals(2, userPartPriceProductList.size());

        //Clean up
        productRepository.deleteById(saveProduct.getId());
    }

    @Test
    void setUserPartPriceFromList() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        UserPartPriceProduct userPartPriceProduct1 = new UserPartPriceProduct("user1", 10.0);
        userPartPriceProduct1.setProduct(product);

        UserPartPriceProduct userPartPriceProduct2 = new UserPartPriceProduct("user2", 7.55);
        userPartPriceProduct2.setProduct(product);

        product.setUserPartPrice(new ArrayList<>(Arrays.asList(
                userPartPriceProduct1, userPartPriceProduct2
        )));

        Product saveProduct = productRepository.save(product);

        Set<UserPartPriceProduct> userPartPriceProducts = new HashSet<>();
        userPartPriceProducts.add(new UserPartPriceProduct("user1", 0.5));
        userPartPriceProducts.add(new UserPartPriceProduct("user2", 0.5));

        //When
        try {
            productService.setUserPartPriceFromList(saveProduct.getId(), userPartPriceProducts);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Product returnProduct = productRepository.findById(saveProduct.getId()).get();

        Assert.assertEquals(0.5, returnProduct.getUserPartPrice().get(0).getPartPrice(), 0.02);
        Assert.assertEquals(0.5, returnProduct.getUserPartPrice().get(1).getPartPrice(), 0.02);

        //Clean up
        productRepository.deleteById(returnProduct.getId());
    }

    @Test
    void isSumEquals() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        UserPartPriceProduct userPartPriceProduct1 = new UserPartPriceProduct("user1", 10.0);
        userPartPriceProduct1.setProduct(product);

        UserPartPriceProduct userPartPriceProduct2 = new UserPartPriceProduct("user2", 7.55);
        userPartPriceProduct2.setProduct(product);

        product.setUserPartPrice(new ArrayList<>(Arrays.asList(
                userPartPriceProduct1, userPartPriceProduct2
        )));

        Product saveProduct = productRepository.save(product);

        Set<UserPartPriceDto> userPartPriceProducts = new HashSet<>();
        userPartPriceProducts.add(new UserPartPriceDto("user1", 0.5));
        userPartPriceProducts.add(new UserPartPriceDto("user2", 0.5));

        //When & Then
        Assert.assertEquals(true, productService.isSumEquals(saveProduct.getId(), userPartPriceProducts));

        //Clean up
        productRepository.deleteById(saveProduct.getId());
    }

    @Test
    void isLessZero() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        UserPartPriceProduct userPartPriceProduct1 = new UserPartPriceProduct("user1", 10.0);
        userPartPriceProduct1.setProduct(product);

        UserPartPriceProduct userPartPriceProduct2 = new UserPartPriceProduct("user2", 7.55);
        userPartPriceProduct2.setProduct(product);

        product.setUserPartPrice(new ArrayList<>(Arrays.asList(
                userPartPriceProduct1, userPartPriceProduct2
        )));

        Product saveProduct = productRepository.save(product);

        Set<UserPartPriceDto> userPartPriceProducts = new HashSet<>();
        userPartPriceProducts.add(new UserPartPriceDto("user1", -0.5));
        userPartPriceProducts.add(new UserPartPriceDto("user2", 0.5));

        //When & Then
        Assert.assertEquals(false, productService.isLessZero(userPartPriceProducts));

        //Clean up
        productRepository.deleteById(saveProduct.getId());
    }

    @Test
    void setUserPartPriceFromQuantity() {
        //Given
        Product product = new Product();
        product.setId(1L);
        product.setName("name1");
        product.setQuantity(1.0);
        product.setPriceForOne(1.0);
        product.setPriceWithoutDiscount(1.0);
        product.setDiscount(0.0);
        product.setPrice(1.0);

        UserPartPriceProduct userPartPriceProduct1 = new UserPartPriceProduct("user1", 10.0);
        userPartPriceProduct1.setProduct(product);

        UserPartPriceProduct userPartPriceProduct2 = new UserPartPriceProduct("user2", 7.55);
        userPartPriceProduct2.setProduct(product);

        product.setUserPartPrice(new ArrayList<>(Arrays.asList(
                userPartPriceProduct1, userPartPriceProduct2
        )));

        Product saveProduct = productRepository.save(product);

        Set<UserPartPriceProduct> userPartPriceProducts = new HashSet<>();
        userPartPriceProducts.add(new UserPartPriceProduct("user1", 0.5));
        userPartPriceProducts.add(new UserPartPriceProduct("user2", 0.5));

        //When
        try {
            productService.setUserPartPriceFromQuantity(saveProduct.getId(), userPartPriceProducts);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Product returnProduct = productRepository.findById(saveProduct.getId()).get();

        Assert.assertEquals(0.5, returnProduct.getUserPartPrice().get(0).getPartPrice(), 0.02);
        Assert.assertEquals(0.5, returnProduct.getUserPartPrice().get(1).getPartPrice(), 0.02);

        //Clean up
        productRepository.deleteById(returnProduct.getId());
    }
}