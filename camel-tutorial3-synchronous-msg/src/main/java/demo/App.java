package demo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.impl.SimpleRegistry;
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
		registry.put("processor1", new Processor1());
		registry.put("processor2", new Processor2());
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:process1").to("bean:processor1");
				from("direct:process2").to("bean:processor2");
			}
		});
		context.start();

		ProducerTemplate pt;

		// Process1
		pt = context.createProducerTemplate();
		String response = (String) pt.requestBody(
				"direct:process1", "PROCESS1");
		LOG.info("Response: {}", response);

		// Process2
		pt = context.createProducerTemplate();
		String response2 = (String) pt.requestBody(
				"direct:process2", "PROCESS2");
		LOG.info("Response: {}", response2);

		
		context.stop();
	}

	// Using camel Exchange
	public static class Processor1 {
		public void process(Exchange msg) throws Exception {
			LOG.info("Processing order: {}", msg.getIn().getBody());
			DefaultMessage out = new DefaultMessage();
			out.setBody("SUCCESS1");
			msg.setOut(out);
		}
	}

	// Standard java processor
	public static class Processor2 {
		public String process(String request)
				throws Exception {
			LOG.info("Processing order: {}", request);
			return "SUCCESS2";
		}
	}
}
