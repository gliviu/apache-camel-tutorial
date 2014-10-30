package demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous jms message service. 
 */
public class App {
	private static final String SERVER_ADDRESS = "tcp://localhost:60000";
	static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		
		// create JMS server
		startServer();
		
		final CamelContext context = new DefaultCamelContext();
		SimpleRegistry registry = new SimpleRegistry();
		((DefaultCamelContext) context).setRegistry(registry);
		registry.put("service", new Service());

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin", "admin", SERVER_ADDRESS);
		context.addComponent("test-jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("test-jms:testMQ").convertBodyTo(String.class).to(
						"bean:service");
			}
		});
		context.start();

		Thread.sleep(Long.MAX_VALUE);
	}

	private static void startServer() {
        try {
            //This message broker is embedded
            BrokerService broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.addConnector(SERVER_ADDRESS);
            broker.start();
        } catch (Exception e) {
            LOG.error("JMS server error: ", e);
        }
	}

	public static class Service {
		public String service1(String str) throws Exception {
			LOG.info("Received: {}", str);
			return "service1 response";
		}

	}
}
