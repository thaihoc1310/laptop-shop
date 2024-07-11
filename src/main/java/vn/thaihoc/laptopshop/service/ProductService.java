package vn.thaihoc.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.Order;
import vn.thaihoc.laptopshop.domain.OrderDetail;
import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.repository.CartDetailRepository;
import vn.thaihoc.laptopshop.repository.CartRepository;
import vn.thaihoc.laptopshop.repository.OrderDetailRepository;
import vn.thaihoc.laptopshop.repository.OrderRepository;
import vn.thaihoc.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public void saveProduct(Product newProduct) {
        this.productRepository.save(newProduct);
    }

    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findProductById(id);
    }

    public List<CartDetail> getAllCartDetailsByCart(Cart cart) {
        return this.cartDetailRepository.findAllByCart(cart);
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findCartByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cart.setSum(0);
                this.cartRepository.save(cart);
            }

            Product product = this.productRepository.findProductById(productId);
            if (product != null) {
                CartDetail cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
                if (cartDetail == null) {
                    cartDetail = new CartDetail();
                    cartDetail.setCart(cart);
                    cartDetail.setProduct(product);
                    cartDetail.setPrice(product.getPrice());
                    cartDetail.setQuantity(1);

                    // update cart sum
                    long sum = cart.getSum() + 1;
                    cart.setSum(sum);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum", sum);
                } else {
                    cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
                    cartDetail.setQuantity(cartDetail.getQuantity() + 1);
                }
                this.cartDetailRepository.save(cartDetail);
            }
        }

    }

    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetail = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetail.isPresent()) {
            CartDetail curcartDetail = cartDetail.get();
            this.cartDetailRepository.deleteById(cartDetailId);
            Cart cart = curcartDetail.getCart();
            long sum = cart.getSum() - 1;
            if (sum == 0) {
                this.cartRepository.deleteById(cart.getId());
            } else {
                cart.setSum(sum);
                this.cartRepository.save(cart);
            }
            session.setAttribute("sum", sum);

        }
    }

    public void handleConfirmCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cd : cartDetails) {
            Optional<CartDetail> cartDetail = this.cartDetailRepository.findById(cd.getId());
            if (cartDetail.isPresent()) {
                CartDetail curCartDetail = cartDetail.get();
                curCartDetail.setQuantity(cd.getQuantity());
                this.cartDetailRepository.save(curCartDetail);
            }
        }
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
