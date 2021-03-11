package com.back.receipt.service;

import com.back.receipt.container.ShopName;
import com.back.receipt.domain.Product;
import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.Room;
import com.back.receipt.repository.ReceiptRepository;
import com.back.receipt.repository.RoomRepository;
import com.back.receipt.security.config.PasswordConfig;
import com.back.receipt.security.domain.Role;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.RoleRepository;
import com.back.receipt.security.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private RoomRepository roomRepository;

    private User user = new User();
    String exampleImagePath;

    @BeforeEach
    public void initz() {
        exampleImagePath = "exampleImage/paragBiedro.jpeg";
        user.setUserName("testUser");
        user.setPassword(passwordConfig.passwordEncoder().encode("test"));
        user.setEmail("testUser@user.com");

        createRoleIfNotFound("ROLE_ADMIN");
        Optional<Role> adminRole = roleRepository.findByRole("ROLE_ADMIN");

        user.setRoles(Collections.singletonList(adminRole.get()));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    void createRoleIfNotFound(String roleName) {

        Optional<Role> findRole = roleRepository.findByRole(roleName);
        if (!findRole.isPresent()) {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
            return;
        }
        findRole.get();
    }

    @Test
    void addReceipt() throws Exception {
        //Given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(exampleImagePath).getFile());
        //MultipartFile multipartFile = new MockMultipartFile("paragBiedro.jpeg", new FileInputStream(file));
        InputStream inputStream = new FileInputStream(file);

        Room room = userService.createRoom("testUser");

        Product product1 = new Product("Jaja kl M x10szt C", 3.0, 3.99, 0, 0, 11.97);
        Product product2 = new Product("Cukier Biały 1kg B", 1.0, 2.49, 0, 0, 2.49);
        Product product3 = new Product("Chleb kr 500g", 1.0, 1.59, 0.0, 0.0, 1.59);
        Product product4 = new Product("JajkoNiespodzLE 20g A", 1.0, 3.69, 0.0, 0.0, 3.69);
        Product product5 = new Product("Mleko UHT 1,5 11 C", 1.0, 1.95, 0.0, 0.0, 1.95);
        Product product6 = new Product("Ser Swiatowid 500g C", 1.0, 9.69, 0.0, 0.0, 9.69);
        Product product7 = new Product("Olej Wyborny 11", 1.0, 4.69, 0.0, 0.0, 4.69);
        Product product8 = new Product("Marg RAMA Poje 1kg", 1.0, 8.99, 8.99, 4.0, 4.99);
        Product product9 = new Product("Sok Pom Riviva 21 C", 1.0, 5.35, 0.0, 0.0, 5.35);

        List<Product> exceptedProductList = new ArrayList<>(Arrays.asList(
                product1,
                product2,
                product3,
                product4,
                product5,
                product6,
                product7,
                product8,
                product9
        ));

        //When
        Receipt receipt = roomService.addReceipt(inputStream, user.getUserName(), room.getId(), ShopName.BIEDRONKA);

        //Then
        Assert.assertEquals("Sklep 6127 ul. Konstruktorska 10c, 02-673 Warszawa", receipt.getAddress());
        Assert.assertEquals("2019-09-19 13:15", receipt.getDate());
        Assert.assertEquals(46.41, receipt.getFullPrice(), 0.001);
        Assert.assertEquals(9, receipt.getProductList().size());

        //Clean up
        receipt.setRoom(null);

        receiptRepository.save(receipt);
        receiptRepository.deleteById(receipt.getId());

        Room room1 = roomRepository.findById(room.getId()).get();
        User user1 = userRepository.findByUserName(user.getUserName()).get();

        user1.getRoomList().remove(room1);
        room1.getUserList().remove(user1);
        roomRepository.save(room1);
        userRepository.save(user1);
        userRepository.deleteById(user1.getId());

        room1 = roomRepository.findById(room.getId()).get();
        roomRepository.deleteById(room1.getId());
    }
}