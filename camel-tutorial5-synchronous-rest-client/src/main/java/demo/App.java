package demo;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
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
		registry.put("test", new TestBean1());
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// 'Create order' http client
				from("direct:createOrder")
						.marshal()
						.json(JsonLibrary.Jackson)
						.to("jetty:http://localhost:8085/myapp/orders/createOrder")
						.unmarshal()
						.json(JsonLibrary.Jackson, CreateOrderResponse.class);

				// 'Delete order client' http client
				from("direct:deleteOrder")
						.marshal()
						.json(JsonLibrary.Jackson)
						.to("jetty:http://localhost:8085/myapp/orders/deleteOrder")
						.unmarshal().json(JsonLibrary.Jackson, Boolean.class);
			}
		});
		context.start();

		ProducerTemplate pt;

		LOG.info("=====================CREATE ORDER=====================");
		pt = context.createProducerTemplate();
		CreateOrderResponse response = (CreateOrderResponse) pt.requestBody(
				"direct:createOrder", new CreateOrderRequest("ORDER_ID_3",
						"stuff", 2));
		LOG.info("Order created: {}", response.status);

		LOG.info("=====================DELETE ORDER=====================");
		pt = context.createProducerTemplate();
		Boolean response2 = (Boolean) pt.requestBody("direct:deleteOrder",
				"ORDER_ID_4");
		LOG.info("Order deleted: {}", response2);

		context.stop();
	}

	public static class TestBean1 {
		public String process(String request) throws Exception {
			LOG.info("Processing order: {}", request);
			return "Success";
		}
	}
}
