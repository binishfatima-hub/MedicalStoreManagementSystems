package com.example.medicarebackend.controller;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.BillSummary;
import com.example.medicarebackend.dto.PlaceOrderRequest;
import com.example.medicarebackend.model.Order;
import com.example.medicarebackend.service.OrderService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Orders / bills.
 *
 *   POST   /api/orders                    place an order (checkout page)
 *   GET    /api/orders/lines              every order line (admin)
 *   GET    /api/orders/bills              every bill (admin)
 *   GET    /api/orders/bills/user/{id}    one user's bills (My Orders)
 *   GET    /api/orders/bills/{billNumber} one full bill (printable bill page)
 *   DELETE /api/orders/bills/{billNumber} delete a bill (admin)
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/lines")
    public List<Order> allLines() {
        return orderService.findAllLines();
    }

    @GetMapping("/bills")
    public List<BillSummary> allBills() {
        return orderService.findAllBills();
    }

    @GetMapping("/bills/user/{userId}")
    public List<BillSummary> billsOfUser(@PathVariable Integer userId) {
        return orderService.findBillsOfUser(userId);
    }

    @GetMapping("/bills/{billNumber}")
    public ApiResponse oneBill(@PathVariable String billNumber) {

        return orderService.findBill(billNumber)
                .map(bill -> ApiResponse.ok("Found", bill))
                .orElse(ApiResponse.fail("Bill not found"));
    }

    @DeleteMapping("/bills/{billNumber}")
    public ApiResponse deleteBill(@PathVariable String billNumber) {
        return orderService.deleteBill(billNumber);
    }
}
