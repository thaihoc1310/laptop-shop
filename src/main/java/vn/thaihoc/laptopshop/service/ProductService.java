package vn.thaihoc.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.thaihoc.laptopshop.repository.CartDetailRepository;
import vn.thaihoc.laptopshop.repository.CartRepository;
import vn.thaihoc.laptopshop.repository.ProductRepository;
import vn.thaihoc.laptopshop.service.specification.ProductSpecs;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService) {
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

    Specification<Product> getCombinedSpec(Pageable pageable, List<String> price) {
        Specification<Product> combinedSpec = Specification.where(null);
        // disjunction => combinedSpec = NULL:
        // (create specification , begin with null value and can combined with
        // operators("toan tu "))
        double minv = 0;
        double maxv = 1000000000;
        boolean check = false;
        for (String s : price) {
            if (s.equals("10-15-trieu")) {
                minv = 10000000;
                maxv = 15000000;
            } else if (s.equals("15-20-trieu")) {
                minv = 15000000;
                maxv = 20000000;
            } else if (s.equals("20-30-trieu")) {
                minv = 20000000;
                maxv = 30000000;
            } else if (s.equals("duoi-10-trieu")) {
                maxv = 10000000;
            } else if (s.equals("tren-20-trieu")) {
                minv = 20000000;
                maxv = 1000000000;
            }
            if (minv != 0 && maxv != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(minv, maxv);
                combinedSpec = combinedSpec.or(rangeSpec);
                check = true;
            }
        }
        return combinedSpec;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    // public Page<Product> getAllProducts(Pageable pageable, double maxPrice) {
    // return this.productRepository.findAll(ProductSpecs.maxPrice(maxPrice),
    // pageable);
    // }
    public Page<Product> getAllProducts(Pageable pageable, ProductCriteriaDTO productCriteriaDTO) {
        Specification<Product> combinedSpec = Specification.where(null);
        // if (productCriteriaDTO.getTarget() == null && productCriteriaDTO.getFactory()
        // == null &&
        // productCriteriaDTO.getPrice() == null) {
        // return this.productRepository.findAll(pageable);
        // }
        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpec = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpec);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpec = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpec);
        }
        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            combinedSpec = combinedSpec.and(getCombinedSpec(pageable, productCriteriaDTO.getPrice().get()));
        }
        return this.productRepository.findAll(combinedSpec, pageable);
    }

    public Product getProductById(long id) {
        return this.productRepository.findProductById(id);
    }

    public List<CartDetail> getAllCartDetailsByCart(Cart cart) {
        return this.cartDetailRepository.findAllByCart(cart);
    }

    public long countProducts() {
        return this.productRepository.count();
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session, long addquantity) {
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
                    cartDetail.setQuantity(addquantity);

                    // update cart sum
                    long sum = cart.getSum() + addquantity;
                    cart.setSum(sum);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum", sum);
                } else {
                    cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
                    cartDetail.setQuantity(cartDetail.getQuantity() + addquantity);
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
