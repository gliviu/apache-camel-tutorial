package demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Sends a message using Camel API.
 * See http://www.pretechsol.com/2013/08/apache-camel-activemq-exampe.html
 * Start Activemq Messaging System
 * 1. Download activemq from http://activemq.apache.org/download-archives.html
 * 2. Extract activemq zip file and start ActiveMQ (Click on activemq.bat file for windows)
 * 3. Check activemq console using http://localhost:8161/admin  (Use default credentials admin/admin to login) 
 */
public class SendMessageCamel {
	public static void main(String[] args) throws Exception {
		final CamelContext context = new DefaultCamelContext();
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin", "admin", "tcp://localhost:61616"); // default activemq credentials and broker url
		context.addComponent("test-jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		// Produce message
		ProducerTemplate pt = context.createProducerTemplate();
		
		context.setTracing(true);
		context.start();

		pt.sendBody("test-jms:testMQ", "Hello World Camel");

		System.out.println("Message sent");

        context.stop();
	}
}