package vn.thaihoc.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.thaihoc.laptopshop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product newProduct);

    void deleteById(long id);

    List<Product> findAll();

    Product findOneById(long id);

    // thuc te one voi all khong co tac dung , kieu du lieu tra ve moi co tac dung
}
