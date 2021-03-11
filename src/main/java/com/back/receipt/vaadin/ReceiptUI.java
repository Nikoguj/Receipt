package com.back.receipt.vaadin;

import com.back.receipt.domain.Product;
import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.Room;
import com.back.receipt.domain.UserPartPriceReceipt;
import com.back.receipt.domain.dto.ProductDto;
import com.back.receipt.domain.dto.UserPartPriceDto;
import com.back.receipt.domain.mapper.MapperUserPartPrice;
import com.back.receipt.domain.mapper.ProductMapper;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.domain.UserDetails;
import com.back.receipt.service.ReceiptService;
import com.back.receipt.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;

import static com.back.receipt.math.Round.roundDouble;

@Route("receipt")
@PageTitle("Receipt")
public class ReceiptUI extends VerticalLayout implements HasUrlParameter<Long>, AfterNavigationObserver, BeforeEnterObserver {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private MapperUserPartPrice mapperUserPartPrice;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductUI productUI;

    @Autowired
    private UserService userService;

    private Long receiptId;
    private Grid<UserPartPriceDto> userPartPriceDtoGrid = null;

    public ReceiptUI() {
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        receiptId = parameter;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        SplitLayout splitLayout = new SplitLayout();

        Grid<ProductDto> productDtoGrid = new Grid<>();
        productDtoGrid.setItemDetailsRenderer(new ComponentRenderer<>(item -> {
            productUI.setReceiptId(receiptId);
            productUI.setProductId(item.getId());
            productUI.setUserPartPriceDtoGrid(userPartPriceDtoGrid);
            return productUI.productUI();
        }));

        SplitLayout firstColumnLayout = new SplitLayout();
        firstColumnLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        firstColumnLayout.setSizeFull();
        setupFirstColumnLayout(firstColumnLayout);

        splitLayout.addToPrimary(firstColumnLayout);

        productDtoGrid.setSizeFull();
        splitLayout.addToSecondary(productDtoGrid);

        refreshProductList(productDtoGrid);

        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(20);

        setSizeFull();
        add(splitLayout);
    }

    private void refreshProductList(Grid<ProductDto> productDtoGrid) {
        try {
            List<Product> productList = receiptService.getProductList(receiptId);
            List<ProductDto> productDtos = productMapper.mapToProductDtoList(productList);
            productDtoGrid.addColumn(ProductDto::getName)
                    .setHeader("Name")
                    .setWidth("25%");
            productDtoGrid.addColumn(ProductDto::getQuantity)
                    .setHeader("Quantity")
                    .setWidth("15%");
            productDtoGrid.addColumn(ProductDto::getPrice)
                    .setHeader("Price")
                    .setWidth("15%");
            productDtoGrid.addColumn(ProductDto::getPriceForOne)
                    .setHeader("Price For One")
                    .setWidth("15%");
            productDtoGrid.addColumn(ProductDto::getPriceWithoutDiscount)
                    .setHeader("Price Without Discount")
                    .setWidth("15%");
            productDtoGrid.addColumn(ProductDto::getDiscount)
                    .setHeader("Discount")
                    .setWidth("15%");
            productDtoGrid.setItems(productDtos);
            final ListDataProvider<ProductDto> dataProvider = DataProvider.ofCollection(productDtos);
            productDtoGrid.setDataProvider(dataProvider);
            dataProvider.setSortOrder(ProductDto::getName, SortDirection.ASCENDING);
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupFirstColumnLayout(SplitLayout firstColumnLayout) {
        firstColumnLayout.setSplitterPosition(31);

        VerticalLayout verticalLayout = new VerticalLayout();
        Button backToReceiptListButton = createBackToReceiptListButton();

        VerticalLayout verticalLayout1 = null;
        try {
            verticalLayout1 = createOwnerPartPriceGrid();
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }

        verticalLayout.add(backToReceiptListButton, verticalLayout1);
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(Alignment.CENTER);
        firstColumnLayout.addToPrimary(verticalLayout);

        SplitLayout secondSplitLayout = new SplitLayout();
        secondSplitLayout.setSplitterPosition(93);
        secondSplitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        secondSplitLayout.setSizeFull();

        VerticalLayout verticalLayout2 = new VerticalLayout();

        try {
            userPartPriceDtoGrid = createUserPartPriceGrid();
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
        verticalLayout2.add(userPartPriceDtoGrid);

        HorizontalLayout operationsReceipt = new HorizontalLayout();
        setupOperationsReceipt(operationsReceipt);

        secondSplitLayout.addToPrimary(verticalLayout2);
        secondSplitLayout.addToSecondary(operationsReceipt);

        firstColumnLayout.addToSecondary(secondSplitLayout);
    }

    private void setupOperationsReceipt(HorizontalLayout operationsReceipt) {
        operationsReceipt.setAlignItems(Alignment.CENTER);
        operationsReceipt.setJustifyContentMode(JustifyContentMode.CENTER);

        Button divideUsersButton = new Button("Divide the receipt into users");
        divideUsersButton.setWidth("85%");


        divideUsersButton.addClickListener(event -> {
            try {
                receiptService.divideReceipt(receiptId);
                UI.getCurrent().getPage().reload();
            } catch (MyResourceNotFoundException e) {
                e.printStackTrace();
            }
        });
        operationsReceipt.add(divideUsersButton);

    }

    private Button createBackToReceiptListButton() {
        Icon backIcon = new Icon(VaadinIcon.ARROW_BACKWARD);
        Button button = new Button("Back to the receipt list", backIcon);
        button.setWidth("85%");

        button.addClickListener(event -> {
            try {
                UI.getCurrent().navigate(RoomUI.class, receiptService.getRecepById(receiptId).getRoom().getId());
            } catch (MyResourceNotFoundException e) {
                e.printStackTrace();
            }
        });

        return button;
    }

    private VerticalLayout createOwnerPartPriceGrid() throws MyResourceNotFoundException {
        VerticalLayout verticalLayout = new VerticalLayout();

        UserPartPriceReceipt ownerPartPrice = receiptService.getOwnerPartPrice(receiptId);

        verticalLayout.add(new Label("Who paid for the receipt:"));
        verticalLayout.add(new Label(ownerPartPrice.getName()));
        verticalLayout.add(new Label("How much did the person pay:"));
        verticalLayout.add(new Label(String.valueOf(ownerPartPrice.getPartPrice())));

        return verticalLayout;
    }

    private Grid<UserPartPriceDto> createUserPartPriceGrid() throws MyResourceNotFoundException {
        Grid<UserPartPriceDto> mapGrid = new Grid<>();
        mapGrid.addColumn(UserPartPriceDto::getUsername)
                .setWidth("60%")
                .setHeader("Username");
        mapGrid.addColumn(UserPartPriceDto::getPartPrice)
                .setWidth("40%")
                .setHeader("To pay");
        mapGrid.setSizeFull();

        List<UserPartPriceReceipt> userPartPrice = receiptService.getUserPartPriceWithoutOwner(receiptId);

        for (UserPartPriceReceipt userPartPriceReceipt : userPartPrice) {
            userPartPriceReceipt.setPartPrice(roundDouble(userPartPriceReceipt.getPartPrice(), 2));
        }
        List<UserPartPriceDto> userPartPriceDtoList = mapperUserPartPrice.mapToUserPartReceiptDtoList(userPartPrice);
        mapGrid.setItems(userPartPriceDtoList);

        return mapGrid;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        try {
            user = userService.getUserByUsername(userDetails.getUsername());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
        if(user != null) {
            Receipt receipt = null;
            try {
                receipt = receiptService.getRecepById(receiptId);
            } catch (MyResourceNotFoundException e) {
                e.printStackTrace();
            }

            boolean contains = false;
            for(Room room: user.getRoomList()) {
                for(Receipt receipt1: room.getReceiptList()) {
                    if(receipt1.equals(receipt)) {
                        contains = true;
                    }
                }
            }

            if(!contains) {
                event.rerouteTo(LoginUI.class);
            }

        }
    }
}
