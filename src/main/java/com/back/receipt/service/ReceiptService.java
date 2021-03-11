package com.back.receipt.service;

import com.back.receipt.domain.Product;
import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.UserPartPriceProduct;
import com.back.receipt.domain.UserPartPriceReceipt;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.repository.ReceiptRepository;
import com.back.receipt.security.domain.User;
import com.google.api.client.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public Receipt getRecepById(final Long id) throws MyResourceNotFoundException {

        Optional<Receipt> receiptOptional = receiptRepository.findById(id);
        receiptOptional.orElseThrow(() -> new MyResourceNotFoundException("Receipt not found"));
        Receipt receipt = receiptOptional.get();

        return receipt;

    }

    public List<Product> getProductList(final Long receiptId) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(receiptId);

        return receipt.getProductList();
    }

    public void setPartPriceForNewUser(final Long receiptId, final Long userId) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(receiptId);
        User user = userService.getUser(userId);

        UserPartPriceReceipt userPartPriceReceipt = new UserPartPriceReceipt(user.getUserName(), 0.0);
        userPartPriceReceipt.setReceipt(receipt);
        receipt.getUserPartPrice().add(userPartPriceReceipt);
        for (Product product : receipt.getProductList()) {
            productService.setPartPriceForNewUser(product.getId(), userId);
        }
        receiptRepository.save(receipt);
    }

    public void setPartPriceForAddReceiptUser(final Long receiptId, final Long userId) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(receiptId);
        User user = userService.getUser(userId);

        for (User user1 : receipt.getRoom().getUserList()) {
            if (!user1.getUserName().equals(user.getUserName())) {
                setPartPriceForNewUser(receiptId, user1.getId());
            }
        }

        UserPartPriceReceipt userPartPriceReceipt = new UserPartPriceReceipt(user.getUserName(), receipt.getFullPrice());
        userPartPriceReceipt.setReceipt(receipt);
        receipt.getUserPartPrice().add(userPartPriceReceipt);
        for (Product product : receipt.getProductList()) {
            productService.setPartPriceForAddReceiptUser(product.getId(), userId);
        }
        receiptRepository.save(receipt);
    }

    public List<UserPartPriceReceipt> getUserPartPriceWithoutOwner(final Long id) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(id);

        List<UserPartPriceReceipt> listToReturn = new ArrayList<>();
        for (UserPartPriceReceipt userPartPriceReceipt : receipt.getUserPartPrice()) {
            if (!userPartPriceReceipt.getName().equals(userService.getUser(receipt.getOwnerId()).getUserName())) {
                listToReturn.add(userPartPriceReceipt);
            }
        }

        return listToReturn;
    }

    public UserPartPriceReceipt getOwnerPartPrice(final Long id) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(id);

        Double owner = 0.0;
        for (UserPartPriceReceipt userPartPriceReceipt : receipt.getUserPartPrice()) {
            if (userPartPriceReceipt.getName().equals(userService.getUser(receipt.getOwnerId()).getUserName())) {
                owner = userPartPriceReceipt.getPartPrice();
            }
        }

        return new UserPartPriceReceipt(userService.getUser(receipt.getOwnerId()).getUserName(), owner);

    }

    public void refreshUserPartPriceReceipt(final Long receiptId) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(receiptId);
        User user = userService.getUser(receipt.getOwnerId());

        for (UserPartPriceReceipt userPartPriceReceipt : receipt.getUserPartPrice()) {
            if (!userPartPriceReceipt.getName().equals(user.getUserName())) {
                userPartPriceReceipt.setPartPrice(0.0);
                for (Product product : receipt.getProductList()) {
                    for (UserPartPriceProduct userPartPriceProduct : product.getUserPartPrice()) {
                        if (userPartPriceReceipt.getName().equals(userPartPriceProduct.getName())) {
                            userPartPriceReceipt.setPartPrice(userPartPriceReceipt.getPartPrice() + userPartPriceProduct.getPartPrice());
                        }
                    }
                }
            }
        }

        receiptRepository.save(receipt);
    }

    public void divideReceipt(final Long receiptId) throws MyResourceNotFoundException {
        Receipt receipt = getRecepById(receiptId);

        for(Product product: receipt.getProductList()) {
            Set<UserPartPriceProduct> userPartPriceProducts = new HashSet<>();
            for(User user: receipt.getRoom().getUserList()) {
                userPartPriceProducts.add(new UserPartPriceProduct(user.getUserName(), product.getPrice()/receipt.getRoom().getUserList().size()));
            }

            productService.setUserPartPriceFromList(product.getId(), userPartPriceProducts);
            refreshUserPartPriceReceipt(receiptId);
        }

    }


}











