package com.hsbremen.student.mkss.restservice;

import com.hsbremen.student.mkss.restservice.model.Order;
import com.hsbremen.student.mkss.restservice.model.Product;
import com.hsbremen.student.mkss.restservice.repository.OrderRepository;
import com.hsbremen.student.mkss.restservice.util.Status;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
		info = @Info(
				title = "Order Rest Service API",
				description = "" +
						"This is the documentation for the Rest API doc. " +
						"Exercise Lab 08 MKSS ",
				contact = @Contact(name = "Lab Group 1", url = "https://github.com/s4profi/L07-mkss-rest-rabbitmq", email = "tha001@stud.hs-bremen.de"),
				version = "1.0.0"),
		servers = @Server(url = "http://localhost:8080")

)
@SpringBootApplication
public class OrderSystemApplication {

	private static final Logger log = LoggerFactory.getLogger(OrderSystemApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(OrderSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(OrderRepository repository) {
		return (args) -> {
			// save a few items
			Product item1 = new Product("Burger", 2, 3);
			Product item2 = new Product("Cheese", 1, 2);
			Product item3 = new Product("LETS GO", 2, 3);
			Product item4 = new Product("IT MIGHT NOT BE THE RIGHT TIME", 2, 2);
			Product item5 = new Product("BUT THERES SOMETHING ABOUT US I HAVE TO SAY", 2, 2);
			Order order = new Order();
			order.setCustomerName("Customer 1");
			order.addProduct(item1);
			order.addProduct(item2);
			order.addProduct(item3);
			order.addProduct(item4);
			order.addProduct(item5);
			order.setStatus(Status.IN_PREPARATION);
			repository.save(order);

			// fetch all orders
			log.info("Orders found with findAll():");
			log.info("-------------------------------");
			for (Order orderTemp : repository.findAll()) {
				log.info(orderTemp.toString());
			}
			log.info("");
		};

	}
}
