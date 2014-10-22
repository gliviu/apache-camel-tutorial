package demo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 */
public class App {
	static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		LOG.info("START");
		final CamelContext context = new DefaultCamelContext();
		SimpleRegistry registry = new SimpleRegistry();
		((DefaultCamelContext) context).setRegistry(registry);
		registry.put("orderService", new OrderService());
		registry.put("test", new TestBean1());
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// Create order service
				from("jetty:http://localhost:8085/myapp/orders/createOrder")
						.unmarshal()
						.json(JsonLibrary.Jackson, CreateOrderRequest.class)
						.to("bean:orderService?method=createOrder").marshal()
						.json(JsonLibrary.Jackson);

				// Delete order service
				from("jetty:http://localhost:8085/myapp/orders/deleteOrder")
						.unmarshal().json(JsonLibrary.Jackson, String.class)
						.to("bean:orderService?method=deleteOrder").marshal()
						.json(JsonLibrary.Jackson);
			}
		});
		context.setTracing(true);
		context.start();

		LOG.info("Server started. Waiting for connections");
		LOG.info("=======================================");
		Thread.sleep(Long.MAX_VALUE);
	}

	public static class TestBean1 {
		public String process(String request) throws Exception {
			LOG.info("Processing order: {}", request);
			return "Success";
		}
	}
}
