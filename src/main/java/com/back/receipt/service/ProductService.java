package com.back.receipt.service;

import com.back.receipt.domain.Product;
import com.back.receipt.domain.UserPartPriceProduct;
import com.back.receipt.domain.dto.UserPartPriceDto;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.repository.ProductRepository;
import com.back.receipt.security.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public Product getProduct(final Long productId) throws MyResourceNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        optionalProduct.orElseThrow(() -> new MyResourceNotFoundException("Can't find this product"));
        Product product = optionalProduct.get();

        return product;
    }

    public void setPartPriceForNewUser(final Long productId, final Long userId) throws MyResourceNotFoundException {
        Product product = getProduct(productId);
        User user = userService.getUser(userId);

        UserPartPriceProduct userPartPriceProduct = new UserPartPriceProduct(user.getUserName(), 0.0);
        userPartPriceProduct.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct);
        productRepository.save(product);
    }

    public void setPartPriceForAddReceiptUser(final Long productId, final Long userId) throws MyResourceNotFoundException {
        Product product = getProduct(productId);
        User user = userService.getUser(userId);

        UserPartPriceProduct userPartPriceProduct = new UserPartPriceProduct(user.getUserName(), product.getPrice());
        userPartPriceProduct.setProduct(product);
        product.getUserPartPrice().add(userPartPriceProduct);
        productRepository.save(product);
    }

    public List<UserPartPriceProduct> getUserPartPriceProduct(final Long productId) throws MyResourceNotFoundException {
        Product product = getProduct(productId);

        return product.getUserPartPrice();
    }

    public void setUserPartPriceFromList(final Long productId, final Set<UserPartPriceProduct> userPartPriceProducts) throws MyResourceNotFoundException {
        Product product = getProduct(productId);

        double splitProductPrice = product.getPrice() / userPartPriceProducts.size();

        for (UserPartPriceProduct userPartPriceProduct : product.getUserPartPrice()) {
            for (UserPartPriceProduct userPartPriceProduct1 : userPartPriceProducts) {
                if (userPartPriceProduct.getName().equals(userPartPriceProduct1.getName())) {
                    userPartPriceProduct.setPartPrice(splitProductPrice);
                }
            }
        }

        for (UserPartPriceProduct userPartPriceProduct : product.getUserPartPrice()) {
            if (!userPartPriceProducts.contains(userPartPriceProduct)) {
                userPartPriceProduct.setPartPrice(0.0);
            }
        }

        productRepository.save(product);
    }

    public boolean isSumEquals(final Long productId, final Set<UserPartPriceDto> userPartPriceDtoSet) {
        Product product = null;
        try {
            product = getProduct(productId);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        double sum = 0.0;
        for (UserPartPriceDto userPartPriceDto : userPartPriceDtoSet) {
            sum = sum + userPartPriceDto.getPartPrice();
        }

        if (sum == product.getQuantity()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLessZero(final Set<UserPartPriceDto> userPartPriceDtoSet) {
        for(UserPartPriceDto userPartPriceDto: userPartPriceDtoSet) {
            if(userPartPriceDto.getPartPrice() < 0.0) {
                return false;
            }
        }
        return true;
    }

    public String sumDifference(final Long productId, final Set<UserPartPriceDto> userPartPriceDtoSet) {
        Product product = null;
        try {
            product = getProduct(productId);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        double sum = 0.0;
        for (UserPartPriceDto userPartPriceDto : userPartPriceDtoSet) {
            sum = sum + userPartPriceDto.getPartPrice();
        }

        return String.valueOf(String.format("%.02f", product.getQuantity() - sum));
    }

    public void setUserPartPriceFromQuantity(final Long productId, final Set<UserPartPriceProduct> userPartPriceProductSet) throws MyResourceNotFoundException {
        Product product = getProduct(productId);

        for (UserPartPriceProduct userPartPriceProduct : product.getUserPartPrice()) {
            for (UserPartPriceProduct userPartPriceProduct1 : userPartPriceProductSet) {
                if (userPartPriceProduct.getName().equals(userPartPriceProduct1.getName())) {
                    if(userPartPriceProduct1.getPartPrice() != 0) {
                        userPartPriceProduct.setPartPrice(product.getPrice()/product.getQuantity()*userPartPriceProduct1.getPartPrice());

                    }
                }
            }
        }


        productRepository.save(product);
    }
}