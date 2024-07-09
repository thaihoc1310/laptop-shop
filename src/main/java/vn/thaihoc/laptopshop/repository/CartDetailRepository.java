package vn.thaihoc.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.Product;
import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    boolean existsByCartAndProduct(Cart cart, Product product);

    CartDetail findByCartAndProduct(Cart cart, Product product);

    List<CartDetail> findAllByCart(Cart cart);
}
