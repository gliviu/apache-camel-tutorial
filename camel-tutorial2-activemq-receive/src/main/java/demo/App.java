package demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receives JMS messages and logs them. 
 * See http://www.pretechsol.com/2013/08/apache-camel-activemq-exampe.html 
 * 1. Start Activemq Messaging System 
 * 		1. Download activemq from http://activemq.apache.org/download-archives.html 
 * 		2. Extract activemq zip file and start ActiveMQ (Click on activemq.bat file for windows) 
 * 		3. Check activemq console using http://localhost:8161/admin (Use default credentials admin/admin to login) 
 * 2. Use camel-tutorial2-activemq-send to submit messages
 */
public class App {
	static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		final CamelContext context = new DefaultCamelContext();
		SimpleRegistry registry = new SimpleRegistry();
		((DefaultCamelContext) context).setRegistry(registry);
		registry.put("log", new MsgLogger());

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin", "admin", "tcp://localhost:61616");
		context.addComponent("test-jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("test-jms:testMQ").convertBodyTo(String.class).to(
						"bean:log");
			}
		});
		context.start();

		Thread.sleep(Long.MAX_VALUE);
	}

	public static class MsgLogger {
		public void process(Message msg) throws Exception {
			LOG.info("Logger: {}", msg);
		}

	}
}
