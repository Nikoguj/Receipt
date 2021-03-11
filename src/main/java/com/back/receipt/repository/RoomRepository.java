package com.back.receipt.repository;

import com.back.receipt.domain.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface RoomRepository extends CrudRepository<Room, Long> {

    Optional<Room> findFirstBy();
}