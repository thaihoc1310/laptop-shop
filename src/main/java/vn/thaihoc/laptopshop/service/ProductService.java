package vn.thaihoc.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public void handleAddProductToCart(String email, long productId) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findCartByUser(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(1);
                cart = this.cartRepository.save(newCart);
            }
            // save cart_detail
            // find prooduct by id
            Product product = this.productRepository.findProductById(productId);
            if (product != null) {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(1);
                this.cartDetailRepository.save(cartDetail);

            }
        }

    }
}
