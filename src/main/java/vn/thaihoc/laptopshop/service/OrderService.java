package vn.thaihoc.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.thaihoc.laptopshop.domain.Order;
import vn.thaihoc.laptopshop.repository.OrderDetailRepository;
import vn.thaihoc.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public void saveOrder(Order order) {
        this.orderRepository.save(order);
    }

    public Optional<Order> getOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public void deleteOrderById(long id) {
        this.orderRepository.deleteById(id);
    }

    public void deleteOrderDetailById(long id) {
        this.orderDetailRepository.deleteById(id);
    }
}