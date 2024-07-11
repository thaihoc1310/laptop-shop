package vn.thaihoc.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.thaihoc.laptopshop.service.OrderService;
import vn.thaihoc.laptopshop.service.ProductService;
import vn.thaihoc.laptopshop.service.UserService;

@Controller
public class DashBoardController {
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    public DashBoardController(OrderService orderService, ProductService productService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getDashBoard(Model model) {
        model.addAttribute("countUsers", this.userService.countUsers());
        model.addAttribute("countProducts", this.productService.countProducts());
        model.addAttribute("countOrders", this.orderService.countOrders());
        return "admin/dashboard/dashboard_view";
    }
}
