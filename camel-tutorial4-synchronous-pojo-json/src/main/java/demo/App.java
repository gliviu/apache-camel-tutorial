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
		registry.put("orderService", new OrderService());
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				// Direct java call
				from("direct:javaCall").to("bean:orderService?method=createOrder");

				// Web flow simulation
				from("direct:webClient").marshal().json(JsonLibrary.Jackson)
						.convertBodyTo(String.class).to("direct:restService")
						.unmarshal()
						.json(JsonLibrary.Jackson, CreateOrderResponse.class);
				from("direct:restService").unmarshal()
						.json(JsonLibrary.Jackson, CreateOrderRequest.class)
						.to("bean:orderService?method=createOrder").marshal()
						.json(JsonLibrary.Jackson);
				
				
			}
		});
		context.setTracing(true);
		context.start();


		ProducerTemplate pt;

		LOG.info("=====================DIRECT JAVA CALL=====================");
		pt = context.createProducerTemplate();
		CreateOrderResponse response2 = (CreateOrderResponse) pt.requestBody(
				"direct:javaCall", new CreateOrderRequest("ORDER_ID_1",
						"goods", 4));
		LOG.info("Response: {}", response2.status);

		
		LOG.info("=====================WEB FLOW SIMULATION=====================");
		pt = context.createProducerTemplate();
		CreateOrderResponse response = (CreateOrderResponse) pt.requestBody(
				"direct:webClient", new CreateOrderRequest("ORDER_ID_2",
						"fish", 4));
		LOG.info("Response: {}", response.status);

		
		
		context.stop();
	}
}
