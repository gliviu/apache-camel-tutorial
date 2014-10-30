package demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends synchronous request to JMS service.
 */
public class SendMessageCamel {
	static Logger LOG = LoggerFactory.getLogger(SendMessageCamel.class);
	public static void main(String[] args) throws Exception {
		final CamelContext context = new DefaultCamelContext();
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin", "admin", "tcp://localhost:60000"); // default activemq credentials and broker url
		context.addComponent("test-jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:test").setExchangePattern(ExchangePattern.InOut).to("test-jms:testMQ");
			}
		});
		
		
		// Produce message
		ProducerTemplate pt = context.createProducerTemplate();
		
		
		context.start();

		String response = (String)pt.requestBody("direct:test", "Hello World Camel");
		LOG.info("Response: {}", response);

        context.stop();
	}
}