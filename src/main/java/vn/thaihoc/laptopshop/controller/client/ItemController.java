package vn.thaihoc.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.thaihoc.laptopshop.domain.Cart;
import vn.thaihoc.laptopshop.domain.CartDetail;
import vn.thaihoc.laptopshop.domain.User;
import vn.thaihoc.laptopshop.service.OrderService;
import vn.thaihoc.laptopshop.service.ProductService;
import vn.thaihoc.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public ItemController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/product/{id}")
    public String getMethodName(Model model, @PathVariable long id) {
        model.addAttribute("product", this.productService.getProductById(id));
        return "client/product/product_detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request,
            @RequestParam("root") Optional<String> rooturl) {
        long productId = id;
        HttpSession session = request.getSession(false);
        this.productService.handleAddProductToCart((String) session.getAttribute("email"), productId, session, 1);
        if (rooturl != null && rooturl.isPresent()) {
            return "redirect:/" + rooturl.get();
        } else
            return "redirect:/";
    }

    @PostMapping("/add-product-by-quantity/{id}")
    public String addProductByQuantityToCart(@PathVariable long id, HttpServletRequest request,
            @RequestParam("addquantity") long addquantity) {
        HttpSession session = request.getSession(false);
        this.productService.handleAddProductToCart((String) session.getAttribute("email"), id, session, addquantity);
        return "redirect:/product/{id}";
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
        User user = new User();
        user.setId((long) session.getAttribute("id"));
        this.orderService.handlePlaceOrder(user, session, receiverName, receiverAddress, receiverPhone);
        return "redirect:/thank-for-order";
    }

    @GetMapping("/thank-for-order")
    public String thankForOrder() {
        return "client/cart/thankyou";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));
        model.addAttribute("orders", user.getOrders());
        return "client/account/order_history";
    }

}
