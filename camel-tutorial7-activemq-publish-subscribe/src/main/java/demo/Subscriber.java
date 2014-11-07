package demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous jms message service. 
 */
public class Subscriber {
	private static final String SERVER_ADDRESS = "tcp://localhost:60000";
	static Logger LOG = LoggerFactory.getLogger(Subscriber.class);

	public static void main(String[] args) throws Exception {
		
		// create JMS server
		startServer();
		
		final CamelContext context = new DefaultCamelContext();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:60000");
		context.addComponent("test-jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("test-jms:queue:Consumer.A.VirtualTopic.Foo").convertBodyTo(String.class).log("A: ${in.body}");
				from("test-jms:queue:Consumer.B.VirtualTopic.Foo").convertBodyTo(String.class).log("B: ${in.body}");
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

}
