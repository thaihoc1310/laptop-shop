package vn.thaihoc.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartByUser(User user);
    // tu hieu user la user_id

}
