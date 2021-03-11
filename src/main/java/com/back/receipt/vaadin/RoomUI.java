package com.back.receipt.vaadin;

import com.back.receipt.container.ShopName;
import com.back.receipt.domain.Room;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.domain.UserDetails;
import com.back.receipt.service.RoomService;
import com.back.receipt.service.UserService;
import com.back.receipt.domain.dto.ReceiptDto;
import com.back.receipt.domain.mapper.ReceiptMapper;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import org.apache.catalina.webresources.FileResource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.nio.file.Paths;

@Route("room")
@PageTitle("Room")
public class RoomUI extends VerticalLayout implements HasUrlParameter<Long>, AfterNavigationObserver, BeforeEnterObserver {

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    private Long roomID;

    public RoomUI() {
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        roomID = parameter;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SplitLayout splitLayout = new SplitLayout();

        Grid<ReceiptDto> receiptGrid = new Grid<>();
        setupProductGrid(receiptGrid);

        receiptGrid.addItemClickListener(event1 -> {
            UI.getCurrent().navigate(ReceiptUI.class, event1.getItem().getId());
        });

        VerticalLayout firstColumn = setupFirstColumn();
        firstColumn.setAlignItems(Alignment.CENTER);
        firstColumn.setSizeFull();


        Button backToRoomListButton = createBackToRoomListButton();
        VerticalLayout uploadImage = createUploadImageButton(userDetails, receiptGrid);

        VerticalLayout exampleImagesLayout = null;
        try {
            exampleImagesLayout = createExampleImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        firstColumn.add(backToRoomListButton, uploadImage, exampleImagesLayout);

        refreshReceiptList(receiptGrid);

        splitLayout.addToPrimary(firstColumn);

        splitLayout.addToSecondary(receiptGrid);
        splitLayout.setSplitterPosition(20);

        add(splitLayout);

        receiptGrid.setSizeFull();
        splitLayout.setSizeFull();
        setSizeFull();
    }

    private VerticalLayout createExampleImages() throws IOException {
        Label label = new Label("Example Images");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setSizeFull();

        verticalLayout.add(label);

        for (int i = 1; i < 5; i++) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setSizeFull();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            byte[] imageBytes = new byte[0];
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                File file = new File(classLoader.getResource("exampleImage/vaadin/receiptBiedronka" + i + ".jpg").getFile());
                imageBytes = FileUtils.readFileToByteArray(file);
            } catch (Exception e) {
                String path = Paths.get(".").toAbsolutePath().normalize().toString();
                String path2 = File.separator + "vaadin" + File.separator + "receiptBiedronka" + i + ".jpg";
                File file = new File(path + path2);
                System.out.println(file.getAbsoluteFile().toString());
                imageBytes = FileUtils.readFileToByteArray(file);
            }

            byte[] finalImageBytes = imageBytes;
            StreamResource resource = new StreamResource("receiptBiedronka" + i + ".jpg", () -> new ByteArrayInputStream(finalImageBytes));
            Image image = new Image(resource, "receiptBiedronka" + i + ".jpg");
            image.setWidth("50%");
            image.setHeight("70");

            Anchor anchor = new Anchor(resource, "click to download");
            anchor.getElement().setAttribute("download", true);
            anchor.getStyle().set("display", "none");
            anchor.setId("anchor" + String.valueOf(i));

            Button button = new Button(new Icon(VaadinIcon.DOWNLOAD));

            int finalI = i;
            button.addClickListener(event -> {
                Page page = UI.getCurrent().getPage();
                page.executeJs("document.getElementById('anchor" + finalI + "').click();");
            });

            horizontalLayout.add(image, anchor, button);
            verticalLayout.add(horizontalLayout);

        }

        return verticalLayout;
    }

    private void setupProductGrid(Grid<ReceiptDto> receiptGrid) {
        receiptGrid.addColumn(ReceiptDto::getAddress)
                .setHeader("Address")
                .setWidth("35%");
        receiptGrid.addColumn(ReceiptDto::getDate)
                .setHeader("Date")
                .setWidth("25%");
        receiptGrid.addColumn(ReceiptDto::getFullPrice)
                .setHeader("Price")
                .setWidth("20%");
        receiptGrid.addColumn(ReceiptDto::getProductCount)
                .setHeader("Number of products")
                .setWidth("20%");
    }

    private VerticalLayout createUploadImageButton(UserDetails userDetails, Grid<ReceiptDto> grid) {

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setMargin(false);
        verticalLayout.setWidth("85%");

        ComboBox<String> choseShopComboBox = new ComboBox<>();
        choseShopComboBox.setLabel("Chose shop name");
        choseShopComboBox.setWidth("120%");
        choseShopComboBox.setItems(ShopName.BIEDRONKA);
        choseShopComboBox.setValue(ShopName.BIEDRONKA);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropLabel(new Label("Upload image in \".jpg\" format"));
        upload.setWidth("110%");
        upload.setAcceptedFileTypes(".jpg");

        Label label = new Label();
        label.getStyle().set("color", "red");
        label.setVisible(false);

        upload.addSucceededListener(event -> {
            label.setVisible(false);
            InputStream inputStream = buffer.getInputStream();

            try {
                roomService.addReceipt(inputStream, userDetails.getUsername(), roomID, choseShopComboBox.getValue());
            } catch (Exception e) {
                label.setText(e.getMessage());
                label.setVisible(true);
            }
            refreshReceiptList(grid);
        });

        verticalLayout.add(choseShopComboBox, upload, label);

        return verticalLayout;
    }

    private VerticalLayout setupFirstColumn() {
        VerticalLayout firstLayout = new VerticalLayout();
        firstLayout.setSizeFull();
        firstLayout.setAlignItems(Alignment.CENTER);
        return firstLayout;
    }

    private Button createBackToRoomListButton() {
        Icon backIcon = new Icon(VaadinIcon.ARROW_BACKWARD);
        Button button = new Button("Back to the room list", backIcon);
        button.setWidth("85%");

        button.addClickListener(event -> {
            UI.getCurrent().navigate(RoomsUI.class);
        });

        return button;
    }

    private void refreshReceiptList(Grid<ReceiptDto> receiptGrid) {
        try {
            receiptGrid.setItems(receiptMapper.mapToReceiptDtoList(roomService.getReceiptList(roomID)));
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
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
        if (user != null) {
            Room roomById = null;
            try {
                roomById = roomService.getRoomById(roomID);
            } catch (MyResourceNotFoundException e) {
                e.printStackTrace();
            }
            if (!user.getRoomList().contains(roomById)) {
                event.rerouteTo(LoginUI.class);
            }
            ;
        }
    }
}