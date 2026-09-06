package com.example.medicarebackend.repository;

import com.example.medicarebackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    /** All order lines, newest first - used by the sales report. */
    List<Order> findAllByOrderByOrderDateDesc();

    /** Only the logged-in user's own orders. */
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);

    /** All lines of one bill, so we can print it. */
    List<Order> findByBillNumber(String billNumber);

    /** Sales report between two dates. */
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(
            LocalDateTime from, LocalDateTime to);
}
