package vn.thaihoc.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.Product;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.service.ProductService;
import vn.thaihoc.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    private final ProductService productService;
    private final UserService userService;

    public ItemController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/product/{id}")
    public String getMethodName(Model model, @PathVariable long id) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "client/product/product_detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        long productId = id;
        HttpSession session = request.getSession(false);
        this.productService.handleAddProductToCart((String) session.getAttribute("email"), productId, session);
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));

        List<CartDetail> cartDetails = new ArrayList<>();
        if (user.getCart() != null)
            cartDetails = user.getCart().getCartDetails();
        double totalPrice = 0;
        for (CartDetail cd : cartDetails)
            totalPrice += cd.getPrice() * cd.getQuantity();
        model.addAttribute("cart", user.getCart());
        model.addAttribute("cartDetails1", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "client/cart/cart_view";
    }

    @PostMapping("/delete-cart-product/{id}")
    public String deleteProductFromCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        this.productService.handleRemoveCartDetail(id, session);

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));

        List<CartDetail> cartDetails = new ArrayList<>();
        if (user.getCart() != null)
            cartDetails = user.getCart().getCartDetails();
        double totalPrice = 0;
        for (CartDetail cd : cartDetails)
            totalPrice += cd.getPrice() * cd.getQuantity();
        model.addAttribute("cart", user.getCart());
        model.addAttribute("cartDetails1", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "client/cart/cart_checkout";
    }

    @PostMapping("/confirm-checkout")
    public String confirmCheckOut(@ModelAttribute("cart") Cart cart, Model model) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<>() : cart.getCartDetails();
        this.productService.handleConfirmCheckout(cartDetails);
        return "redirect:/checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {
        HttpSession session = request.getSession(false);
        return "redirect:/";
    }

}
