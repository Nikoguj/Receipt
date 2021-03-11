package com.back.receipt.repository;

import com.back.receipt.domain.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
}
