package com.back.receipt.service;

import com.back.receipt.domain.*;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.repository.ProductRepository;
import com.back.receipt.repository.ReceiptRepository;
import com.back.receipt.repository.RoomRepository;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ReceiptServiceTest {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void getRecepById() {
        //Given
        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);

        Receipt saveReceipt = receiptRepository.save(receipt);

        //When
        Receipt returnReceipt = null;
        try {
            returnReceipt = receiptService.getRecepById(saveReceipt.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Assert.assertNotNull(returnReceipt.getId());
        Assert.assertEquals(receipt.getAddress(), returnReceipt.getAddress());
        Assert.assertEquals(receipt.getDate(), returnReceipt.getDate());
        Assert.assertEquals(receipt.getFullPrice(), returnReceipt.getFullPrice(), 0.02);

        //Clean up
        receiptRepository.deleteById(saveReceipt.getId());
    }

    @Test
    void getProductList() {
        //Given
        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);

        Product product1 = new Product();
        product1.setName("name1");
        product1.setReceipt(receipt);

        Product product2 = new Product();
        product2.setName("name2");
        product2.setReceipt(receipt);

        List<Product> productList1 = new ArrayList<>(Arrays.asList(
                product1, product2
        ));
        receipt.setProductList(productList1);

        Receipt saveReceipt = receiptRepository.save(receipt);

        //When
        List<Product> productList = null;
        try {
            productList = receiptService.getProductList(saveReceipt.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Assert.assertEquals(2, productList.size());
        Assert.assertTrue(productList.contains(product1));
        Assert.assertTrue(productList.contains(product2));

        //Clean up
        receiptRepository.deleteById(saveReceipt.getId());
    }

    @Test
    void setPartPriceForNewUser() {
        //Given
        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);

        User user = new User();
        user.setUserName("username1");
        user.setPassword("password1");
        user.setEmail("email1");
        user.setActive(true);

        Receipt saveReceipt = receiptRepository.save(receipt);
        User saveUser = userRepository.save(user);

        //When
        try {
            receiptService.setPartPriceForNewUser(saveReceipt.getId(), saveUser.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Receipt returnReceipt = receiptRepository.findById(saveReceipt.getId()).get();
        Assert.assertEquals(0.0, returnReceipt.getUserPartPrice().get(0).getPartPrice(), 0.02);

        //Clean up
        receiptRepository.deleteById(saveReceipt.getId());
        userRepository.deleteById(saveUser.getId());
    }

    @Test
    void setPartPriceForAddReceiptUser() {
        //Given
        Room room = new Room();
        room.setPassword("password");

        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);

        Product product = new Product();
        product.setName("product1");

        product.setReceipt(receipt);
        receipt.setProductList(new ArrayList<>(Arrays.asList(product)));

        receipt.setRoom(room);
        room.setReceiptList(new ArrayList<>(Arrays.asList(receipt)));

        User user = new User();
        user.setUserName("username1");
        user.setPassword("password1");
        user.setEmail("email1");
        user.setActive(true);

        room.setUserList(new ArrayList<>(Arrays.asList(user)));
        user.setRoomList(new ArrayList<>(Arrays.asList(room)));

        User saveUser = userRepository.save(user);
        Room saveRoom = roomRepository.save(room);

        //When
        try {
            receiptService.setPartPriceForAddReceiptUser(saveRoom.getReceiptList().get(0).getId(), saveUser.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Receipt returnReceipt = receiptRepository.findById(saveRoom.getReceiptList().get(0).getId()).get();
        Assert.assertEquals(140.50, returnReceipt.getUserPartPrice().get(0).getPartPrice(), 0.02);
        Assert.assertEquals("username1", returnReceipt.getUserPartPrice().get(0).getName());

        //Clean up
        saveUser.getRoomList().remove(0);
        saveRoom.getUserList().remove(0);

        roomRepository.save(saveRoom);
        userRepository.save(saveUser);

        roomRepository.deleteById(saveRoom.getId());
        userRepository.deleteById(saveUser.getId());
    }

    @Test
    void getUserPartPriceWithoutOwner() {
        //Given
        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);


        List<UserPartPriceReceipt> userPartPriceReceiptList = new ArrayList<>();

        UserPartPriceReceipt userPartPriceReceipt1 = new UserPartPriceReceipt("username1", 10.0);
        userPartPriceReceipt1.setReceipt(receipt);
        UserPartPriceReceipt userPartPriceReceipt2 = new UserPartPriceReceipt("username2", 20.0);
        userPartPriceReceipt2.setReceipt(receipt);
        UserPartPriceReceipt userPartPriceReceipt3 = new UserPartPriceReceipt("username3", 30.0);
        userPartPriceReceipt3.setReceipt(receipt);

        userPartPriceReceiptList.add(userPartPriceReceipt1);
        userPartPriceReceiptList.add(userPartPriceReceipt2);
        userPartPriceReceiptList.add(userPartPriceReceipt3);

        receipt.setUserPartPrice(userPartPriceReceiptList);

        User user = new User();
        user.setUserName("username1");
        user.setPassword("password1");
        user.setEmail("email1");
        user.setActive(true);

        User saveUser = userRepository.save(user);
        receipt.setOwnerId(saveUser.getId());
        Receipt saveReceipt = receiptRepository.save(receipt);

        //When
        List<UserPartPriceReceipt> userPartPriceWithoutOwner = new ArrayList<>();
        try {
            userPartPriceWithoutOwner = receiptService.getUserPartPriceWithoutOwner(saveReceipt.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }


        //Then
        Assert.assertEquals(2, userPartPriceWithoutOwner.size());
        Assert.assertTrue(userPartPriceWithoutOwner.contains(userPartPriceReceipt2));
        Assert.assertTrue(userPartPriceWithoutOwner.contains(userPartPriceReceipt3));


        //Clean up
        userRepository.deleteById(saveUser.getId());
        receiptRepository.deleteById(saveReceipt.getId());
    }

    @Test
    void getOwnerPartPrice() {
        //Given
        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);


        List<UserPartPriceReceipt> userPartPriceReceiptList = new ArrayList<>();

        UserPartPriceReceipt userPartPriceReceipt1 = new UserPartPriceReceipt("username1", 10.0);
        userPartPriceReceipt1.setReceipt(receipt);
        UserPartPriceReceipt userPartPriceReceipt2 = new UserPartPriceReceipt("username2", 20.0);
        userPartPriceReceipt2.setReceipt(receipt);
        UserPartPriceReceipt userPartPriceReceipt3 = new UserPartPriceReceipt("username3", 30.0);
        userPartPriceReceipt3.setReceipt(receipt);

        userPartPriceReceiptList.add(userPartPriceReceipt1);
        userPartPriceReceiptList.add(userPartPriceReceipt2);
        userPartPriceReceiptList.add(userPartPriceReceipt3);

        receipt.setUserPartPrice(userPartPriceReceiptList);

        User user = new User();
        user.setUserName("username1");
        user.setPassword("password1");
        user.setEmail("email1");
        user.setActive(true);

        User saveUser = userRepository.save(user);
        receipt.setOwnerId(saveUser.getId());
        Receipt saveReceipt = receiptRepository.save(receipt);

        //When
        UserPartPriceReceipt userPartPriceWithoutOwner = new UserPartPriceReceipt();
        try {
            userPartPriceWithoutOwner = receiptService.getOwnerPartPrice(saveReceipt.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }


        //Then
        Assert.assertEquals("username1", userPartPriceWithoutOwner.getName());
        Assert.assertEquals(10.0, userPartPriceWithoutOwner.getPartPrice(), 0.02);


        //Clean up
        userRepository.deleteById(saveUser.getId());
        receiptRepository.deleteById(saveReceipt.getId());
    }

    @Test
    void refreshUserPartPriceReceipt() {
        //Given
        Receipt receipt = new Receipt();
        receipt.setAddress("address1");
        receipt.setDate("date1");
        receipt.setFullPrice(140.50);

        List<UserPartPriceReceipt> userPartPriceReceiptList = new ArrayList<>();

        UserPartPriceReceipt userPartPriceReceipt1 = new UserPartPriceReceipt("username1", 10.0);
        userPartPriceReceipt1.setReceipt(receipt);
        UserPartPriceReceipt userPartPriceReceipt2 = new UserPartPriceReceipt("username2", 20.0);
        userPartPriceReceipt2.setReceipt(receipt);
        UserPartPriceReceipt userPartPriceReceipt3 = new UserPartPriceReceipt("username3", 30.0);
        userPartPriceReceipt3.setReceipt(receipt);

        userPartPriceReceiptList.add(userPartPriceReceipt1);
        userPartPriceReceiptList.add(userPartPriceReceipt2);
        userPartPriceReceiptList.add(userPartPriceReceipt3);

        receipt.setUserPartPrice(userPartPriceReceiptList);

        Product product = new Product();
        product.setName("product1");
        UserPartPriceProduct userPartPriceProduct1 = new UserPartPriceProduct("username1", 2.0);
        userPartPriceProduct1.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct1);

        UserPartPriceProduct userPartPriceProduct2 = new UserPartPriceProduct("username2", 6.0);
        userPartPriceProduct2.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct2);

        UserPartPriceProduct userPartPriceProduct3 = new UserPartPriceProduct("username3", 7.0);
        userPartPriceProduct3.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct3);
        product.setReceipt(receipt);

        Product product2 = new Product();
        product2.setName("product1");
        UserPartPriceProduct userPartPriceProduct4 = new UserPartPriceProduct("username1", 6.0);
        userPartPriceProduct4.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct4);

        UserPartPriceProduct userPartPriceProduct5 = new UserPartPriceProduct("username2", 9.0);
        userPartPriceProduct5.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct5);

        UserPartPriceProduct userPartPriceProduct6 = new UserPartPriceProduct("username3", 11.0);
        userPartPriceProduct6.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct6);
        product2.setReceipt(receipt);

        receipt.getProductList().add(product);
        receipt.getProductList().add(product2);

        User user = new User();
        user.setUserName("username1");
        user.setPassword("password1");
        user.setEmail("email1");
        user.setActive(true);

        User saveUser = userRepository.save(user);
        receipt.setOwnerId(saveUser.getId());
        Receipt saveReceipt = receiptRepository.save(receipt);

        //When
        try {
            receiptService.refreshUserPartPriceReceipt(saveReceipt.getId());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        //Then
        Receipt returnReceipt = receiptRepository.findById(saveReceipt.getId()).get();

        Assert.assertTrue(returnReceipt.getUserPartPrice().contains(new UserPartPriceReceipt("username1", 10.0)));
        Assert.assertTrue(returnReceipt.getUserPartPrice().contains(new UserPartPriceReceipt("username2", 15.0)));
        Assert.assertTrue(returnReceipt.getUserPartPrice().contains(new UserPartPriceReceipt("username3", 18.0)));

        //Clean up
        receiptRepository.deleteById(saveReceipt.getId());
        userRepository.deleteById(saveUser.getId());
    }
}