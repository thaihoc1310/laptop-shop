package vn.thaihoc.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.Order;
import vn.thaihoc.laptopshop.domain.OrderDetail;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.repository.CartDetailRepository;
import vn.thaihoc.laptopshop.repository.CartRepository;
import vn.thaihoc.laptopshop.repository.OrderDetailRepository;
import vn.thaihoc.laptopshop.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
            CartRepository cartRepository, CartDetailRepository cartDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
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

    public long countOrders() {
        return this.orderRepository.count();
    }

    public void deleteOrderDetailById(long id) {
        this.orderDetailRepository.deleteById(id);
    }

    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone) {
        // create order detail
        Cart cart = this.cartRepository.findCartByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            // create order
            Order order = new Order();
            order.setUser(user);
            order.setReceiverName(receiverName);
            order.setReceiverAddress(receiverAddress);
            order.setReceiverPhone(receiverPhone);
            order.setStatus("PENDING");
            double sum = 0;
            for (CartDetail cd : cartDetails) {
                sum += cd.getPrice() * cd.getQuantity();
            }
            order.setTotalPrice(sum);
            // save and get id
            order = this.orderRepository.save(order);
            for (CartDetail cd : cartDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cd.getProduct());
                orderDetail.setPrice(cd.getPrice());
                orderDetail.setQuantity(cd.getQuantity());
                this.orderDetailRepository.save(orderDetail);
                this.cartDetailRepository.deleteById(cd.getId());
            }
            this.cartRepository.deleteById(cart.getId());
        }
        // update session
        session.setAttribute("sum", 0);
    }
}