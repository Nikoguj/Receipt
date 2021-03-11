package com.back.receipt.service;

import com.back.receipt.biedronka.BiedronkaReceiptExtractor;
import com.back.receipt.container.ShopName;
import com.back.receipt.domain.Product;
import com.back.receipt.domain.Receipt;
import com.back.receipt.exception.FailAddReceipt;
import com.back.receipt.google.domain.GoogleBoundingPoly;
import com.back.receipt.google.domain.GoogleResponse;
import com.back.receipt.google.domain.GoogleTextAnnotation;
import com.back.receipt.google.domain.GoogleVertex;
import com.back.receipt.image.ImageConverter;
import com.back.receipt.domain.Room;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.google.GoogleClient;
import com.back.receipt.google.mapper.GoogleMapper;
import com.back.receipt.repository.ProductRepository;
import com.back.receipt.repository.ReceiptRepository;
import com.back.receipt.repository.RoomRepository;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private BiedronkaReceiptExtractor biedronkaReceiptExtractor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private GoogleMapper googleMapper;

    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private ImageConverter imageConverter;

    public Receipt addReceipt(final InputStream file, final String username, final Long roomId, final String shop) throws Exception {

        Optional<User> userOptional = userRepository.findByUserName(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        User user = userOptional.get();

        Optional<Room> roomOptional = roomRepository.findById(roomId);
        roomOptional.orElseThrow(() -> new MyResourceNotFoundException("Username not found"));
        Room room = roomOptional.get();

        if (user.getRoomList().contains(room)) {
            String fileInString = imageConverter.encodeToString(file);
            GoogleResponse googleResponse = googleMapper.mapToGoogleResponse(googleClient.getGoogleDto(fileInString));

            clearGoogleResponseNullBoundingPoly(googleResponse);

            switch (shop) {
                case ShopName.BIEDRONKA:
                    Receipt receipt;
                    try {
                        receipt = biedronkaReceiptExtractor.extract(googleResponse);
                        receipt.setOwnerId(user.getId());
                    } catch (Exception e) {
                        throw new FailAddReceipt("Error while adding a receipt: " + e.getMessage() + ". Take a different picture. " +
                                "Make sure all values on the receipt are visible.");
                    }
                    if (receipt != null) {
                        for (Product product : receipt.getProductList()) {
                            product.setReceipt(receipt);
                        }
                        room.getReceiptList().add(receipt);
                        receipt.setRoom(room);

                        receiptRepository.save(receipt);
                        receiptService.setPartPriceForAddReceiptUser(receipt.getId(), user.getId());
                        return receipt;
                    }
                default:
                    break;
            }
        } else {
            throw new HTTPException(403);
        }

        throw new FailAddReceipt("Error while adding a receipt.");
    }

    private void clearGoogleResponseNullBoundingPoly(GoogleResponse googleResponse) {
        for(GoogleTextAnnotation googleTextAnnotation: googleResponse.getGoogleResponsesList().get(0).getTextAnnotations()) {
            GoogleBoundingPoly googleBoundingPoly = googleTextAnnotation.getBoundingPoly();
            for(GoogleVertex googleVertex: googleBoundingPoly.getVertices()) {
                if(googleVertex.getX() == null) {
                    googleVertex.setX(0);
                }
                if(googleVertex.getY() == null) {
                    googleVertex.setY(0);
                }
            }
        }
    }

    public List<Receipt> getReceiptList(final Long roomID) throws MyResourceNotFoundException {
        Optional<Room> roomOptional = roomRepository.findById(roomID);
        roomOptional.orElseThrow(() -> new MyResourceNotFoundException("Room ID not found"));
        Room room = roomOptional.get();

        return room.getReceiptList();
    }

    public Room getRoomById(final Long roomID) throws MyResourceNotFoundException {
        Optional<Room> roomOptional = roomRepository.findById(roomID);
        roomOptional.orElseThrow(() -> new MyResourceNotFoundException("Room ID not found"));
        Room room = roomOptional.get();

        return room;
    }

    public void setPartPriceForNewUser(final Long roomId, final Long userId) throws MyResourceNotFoundException {
        Room room = getRoomById(roomId);

        for(Receipt receipt: room.getReceiptList()) {
            Runnable runnable = () -> {
                try {
                    receiptService.setPartPriceForNewUser(receipt.getId(), userId);
                } catch (MyResourceNotFoundException e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }

    }

}
