package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/employees")
	public ModelAndView employee() {
		return mav("employee.html");
	}

	@RequestMapping(value = "/ui/supervisor/user")
	public ModelAndView admin() {
		return mav("user.html");
	}

	@RequestMapping(value = "/ui/brands")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/products")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventories")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/ui/order-item/{id}")
	public ModelAndView orderIterm(@PathVariable Integer id) {
		return mav("orderItem.html", id);
	}

	@RequestMapping(value = "/ui/supervisor/inventory-report")
	public ModelAndView inventoryReport() {
		return mav("inventoryReport.html");
	}

	@RequestMapping(value = "/ui/supervisor/daily-report")
	public ModelAndView dailyReport() {
		return mav("dailyReport.html");
	}
	@RequestMapping(value = "/ui/supervisor/sales-report")
	public ModelAndView salesReport() {
		return mav("salesReport.html");
	}

}
