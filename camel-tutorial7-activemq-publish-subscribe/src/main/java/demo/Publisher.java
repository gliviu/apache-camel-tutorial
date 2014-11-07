package demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous jms message service. 
 */
public class Publisher {
	static Logger LOG = LoggerFactory.getLogger(Publisher.class);

	public static void main(String[] args) throws Exception {
		
		final CamelContext context = new DefaultCamelContext();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:60000");
		context.addComponent("test-jms",
				JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:publishFoo").log("Message sent: ${in.body}").to("test-jms:topic:VirtualTopic.Foo");
			}
		});
		context.start();

		ProducerTemplate producerTemplate = context.createProducerTemplate();
        for (int i = 0; i < 1000; ++i) {
    		producerTemplate.sendBody("direct:publishFoo", ""+i);
        }
		
		context.stop();
	}
}
