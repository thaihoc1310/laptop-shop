package vn.thaihoc.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.thaihoc.laptopshop.domain.Order;
import vn.thaihoc.laptopshop.service.OrderService;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getOrder(Model model) {
        List<Order> orders = this.orderService.getAllOrders();
        model.addAttribute("orders1", orders);
        return "admin/order/order_view";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(Model model, @PathVariable("id") long id) {
        Optional<Order> optionorder = this.orderService.getOrderById(id);
        if (optionorder.isPresent()) {
            Order order = optionorder.get();
            model.addAttribute("cartDetails1", order.getOrderDetails());
            model.addAttribute("id", id);
        }
        return "admin/order/order-inf";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable("id") long id) {
        model.addAttribute("id", id);
        return "admin/order/delete-order";
    }

    @PostMapping("/admin/order/delete")
    public String deleteOrder(@RequestParam("id") long id) {
        Optional<Order> optionorder = this.orderService.getOrderById(id);
        if (optionorder.isPresent()) {
            Order order = optionorder.get();
            order.getOrderDetails().forEach(orderDetail -> {
                this.orderService.deleteOrderDetailById(orderDetail.getId());
            });
        }
        this.orderService.deleteOrderById(id);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable("id") long id) {
        Optional<Order> optionorder = this.orderService.getOrderById(id);
        if (optionorder.isPresent()) {
            Order order = optionorder.get();
            model.addAttribute("order1", order);
        }
        return "admin/order/update-order";
    }

    @PostMapping("/admin/order/update")
    public String updateOrder(@RequestParam("status") String status, @RequestParam("id") long id) {
        Optional<Order> optionorder = this.orderService.getOrderById(id);
        if (optionorder.isPresent()) {
            Order order = optionorder.get();
            order.setStatus(status);
            this.orderService.saveOrder(order);
        }
        return "redirect:/admin/order";
    }
}
