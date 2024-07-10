package vn.thaihoc.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.repository.CartDetailRepository;
import vn.thaihoc.laptopshop.repository.CartRepository;
import vn.thaihoc.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository,
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository,
            UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
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

            // save cart_detail
            // find prooduct by id

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
}
