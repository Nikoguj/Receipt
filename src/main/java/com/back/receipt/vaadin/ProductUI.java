package com.back.receipt.vaadin;

import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.UserPartPriceProduct;
import com.back.receipt.domain.UserPartPriceReceipt;
import com.back.receipt.domain.dto.UserPartPriceDto;
import com.back.receipt.domain.mapper.MapperUserPartPrice;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.security.domain.User;
import com.back.receipt.service.ProductService;
import com.back.receipt.service.ReceiptService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.back.receipt.math.Round.roundDouble;

@Component
public class ProductUI extends UI {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MapperUserPartPrice mapperUserPartPrice;

    private Long receiptId;
    private Long productId;
    private Grid<UserPartPriceDto> userPartPriceDtoGrid;
    private Grid<UserPartPriceDto> userPartPriceProductGrid;

    public ProductUI() {
    }

    public HorizontalLayout productUI() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout firstLayout = new VerticalLayout();
        setupFirstLayout(firstLayout);

        VerticalLayout secondLayout = new VerticalLayout();
        setupSecondLayout(secondLayout);

        VerticalLayout thirdLayout = new VerticalLayout();
        setupThirdLayout(thirdLayout);

        setupLayouts(firstLayout, secondLayout, thirdLayout);

        horizontalLayout.add(firstLayout, secondLayout, thirdLayout);
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    private void setupLayouts(VerticalLayout firstLayout, VerticalLayout secondLayout, VerticalLayout thirdLayout) {
        List<VerticalLayout> verticalLayoutList = new ArrayList<>();
        verticalLayoutList.add(firstLayout);
        verticalLayoutList.add(secondLayout);
        verticalLayoutList.add(thirdLayout);

        for (VerticalLayout verticalLayout : verticalLayoutList) {
            verticalLayout.getStyle().set("border-style", "groove");
            verticalLayout.getStyle().set("border-color", "#cfe5ff");
            verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        }
    }

    private void setupFirstLayout(VerticalLayout firstLayout) {
        Label label = new Label("Choose whose the product is");

        Grid<UserPartPriceDto> firstColumnGrid = new Grid<>();
        firstColumnGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        firstColumnGrid.setWidth("100%");
        firstColumnGrid.setHeight("170px");

        List<UserPartPriceDto> usernames = new ArrayList<>();
        try {
            usernames = mapperUserPartPrice.mapToUserPartProductDtoList(productService.getUserPartPriceProduct(productId));
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
        firstColumnGrid.addColumn(UserPartPriceDto::getUsername)
                .setHeader("Username");
        firstColumnGrid.setItems(usernames);

        Button button = new Button("Submit");
        Icon icon = new Icon(VaadinIcon.CHECK);
        icon.setColor("green");
        icon.setVisible(false);

        button.addClickListener(event -> {
            icon.setVisible(false);

            Set<UserPartPriceDto> selectedItems = firstColumnGrid.getSelectedItems();
            Set<UserPartPriceProduct> userPartPriceProducts = mapperUserPartPrice.mapToUserPartPriceProductSet(selectedItems);
            try {
                productService.setUserPartPriceFromList(productId, userPartPriceProducts);
                receiptService.refreshUserPartPriceReceipt(receiptId);
                firstColumnGrid.deselectAll();
                icon.setVisible(true);
            } catch (MyResourceNotFoundException e) {
                e.printStackTrace();
            }
            try {
                refreshUserPartPriceGrid();
            } catch (MyResourceNotFoundException e) {
                e.printStackTrace();
            }
        });

        firstLayout.add(label, firstColumnGrid, new HorizontalLayout(button, icon));

    }

    private void setupSecondLayout(VerticalLayout secondLayout) {
        Label label = new Label("Enter the quantity of the product that belongs to the user");
        Receipt receipt = null;
        try {
            receipt = receiptService.getRecepById(receiptId);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        Set<UserPartPriceDto> userPartPriceDtoSet = new HashSet<>();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setSizeFull();
        verticalLayout.add(label);

        for (User user : receipt.getRoom().getUserList()) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setId("horizontalLayout");
            horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            TextField textField = new TextField();
            textField.setId("secondLayoutTextField");
            textField.setLabel(user.getUserName());

            UserPartPriceDto userPartPriceDto = new UserPartPriceDto();
            Binder<UserPartPriceDto> binder = new Binder<>(UserPartPriceDto.class);
            binder.setBean(userPartPriceDto);

            userPartPriceDto.setUsername(user.getUserName());

            Binder.Binding<UserPartPriceDto, String> binding = binder.forField(textField)
                    .withValidator(value -> {
                        try {
                            Double.valueOf(value);
                            return true;
                        } catch (Exception e) {
                            return false;
                        }

                    }, "Enter the correct value.")
                    .bind(UserPartPriceDto::getPartPriceString, UserPartPriceDto::setPartPriceString);

            textField.addValueChangeListener(event -> binding.validate());
            binder.writeBeanIfValid(userPartPriceDto);

            userPartPriceDtoSet.add(userPartPriceDto);

            horizontalLayout.add(textField);
            verticalLayout.add(horizontalLayout);

        }

        Button button = new Button("Submit");
        Icon icon = new Icon(VaadinIcon.CHECK);
        icon.setColor("green");
        icon.setVisible(false);

        Label sumlabel = new Label("The sum does not equal to the quantity of the product.");
        sumlabel.setVisible(false);
        button.addClickListener(event -> {
            icon.setVisible(false);
            if (productService.isSumEquals(productId, userPartPriceDtoSet)) {
                if (productService.isLessZero(userPartPriceDtoSet)) {
                    sumlabel.setVisible(false);
                    try {
                        productService.setUserPartPriceFromQuantity(productId, mapperUserPartPrice.mapToUserPartPriceProductSet(userPartPriceDtoSet));
                        receiptService.refreshUserPartPriceReceipt(receiptId);

                        List<com.vaadin.flow.component.Component> componentList = verticalLayout.getChildren().filter(component -> {
                            if (component.getId().isPresent() && component.getId().get().equals("horizontalLayout")) {
                                return true;
                            } else {
                                return false;
                            }
                        }).collect(Collectors.toList());

                        for (com.vaadin.flow.component.Component component : componentList) {
                            component.getChildren().filter(component1 -> component1 instanceof TextField)
                                    .map(component1 -> ((TextField) component1))
                                    .forEach(textField -> textField.setValue("0.0"));
                        }

                        icon.setVisible(true);
                    } catch (MyResourceNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        refreshUserPartPriceGrid();
                    } catch (MyResourceNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    sumlabel.setText("Value below 0");
                    sumlabel.setVisible(true);
                }
            } else {
                sumlabel.setText("The sum does not equal to the quantity of the product. The difference is: " + productService.sumDifference(productId, userPartPriceDtoSet));
                sumlabel.setVisible(true);
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(button, icon, sumlabel);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(horizontalLayout);

        secondLayout.add(verticalLayout);
    }

    private void setupThirdLayout(VerticalLayout thirdLayout) {
        Label label = new Label("Current money settlement");

        userPartPriceProductGrid = new Grid<>();

        userPartPriceProductGrid.addColumn(UserPartPriceDto::getUsername)
                .setWidth("50%")
                .setHeader("Username");
        userPartPriceProductGrid.addColumn(UserPartPriceDto::getPartPrice)
                .setWidth("50%")
                .setHeader("Amount of money");
        userPartPriceProductGrid.setSizeFull();

        List<UserPartPriceProduct> userPartPrice = null;
        try {
            userPartPrice = productService.getUserPartPriceProduct(productId);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        for (UserPartPriceProduct userPartPriceProduct : userPartPrice) {
            userPartPriceProduct.setPartPrice(roundDouble(userPartPriceProduct.getPartPrice(), 2));
        }
        List<UserPartPriceDto> userPartPriceDtoList = mapperUserPartPrice.mapToUserPartProductDtoList(userPartPrice);
        userPartPriceProductGrid.setItems(userPartPriceDtoList);

        thirdLayout.add(label, userPartPriceProductGrid);
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUserPartPriceDtoGrid(Grid<UserPartPriceDto> userPartPriceDtoGrid) {
        this.userPartPriceDtoGrid = userPartPriceDtoGrid;
    }

    public void refreshUserPartPriceGrid() throws MyResourceNotFoundException {
        List<UserPartPriceReceipt> userPartPriceList = receiptService.getUserPartPriceWithoutOwner(receiptId);

        for (UserPartPriceReceipt userPartPriceReceipt : userPartPriceList) {
            userPartPriceReceipt.setPartPrice(roundDouble(userPartPriceReceipt.getPartPrice(), 2));
        }
        List<UserPartPriceDto> userPartPriceReceiptDtoList = mapperUserPartPrice.mapToUserPartReceiptDtoList(userPartPriceList);
        userPartPriceDtoGrid.setItems(userPartPriceReceiptDtoList);

        List<UserPartPriceProduct> userPartPriceProductList = productService.getUserPartPriceProduct(productId);

        for (UserPartPriceProduct userPartPriceProduct : userPartPriceProductList) {
            userPartPriceProduct.setPartPrice(roundDouble(userPartPriceProduct.getPartPrice(), 2));
        }
        List<UserPartPriceDto> userPartPriceProductDtoList = mapperUserPartPrice.mapToUserPartProductDtoList(userPartPriceProductList);
        userPartPriceProductGrid.setItems(userPartPriceProductDtoList);
    }

}
