package demo;

import org.apache.camel.ProducerTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates both sending and receiving jms messages.
 * Receives JMS message, logs it and moves it in another queue.
 * See http://www.pretechsol.com/2013/08/apache-camel-activemq-exampe.html 
 * 1. Start Activemq Messaging System 
 * 		1. Download activemq from http://activemq.apache.org/download-archives.html 
 * 		2. Extract activemq zip file and start ActiveMQ (Click on activemq.bat file for windows) 
 * 		3. Check activemq console using http://localhost:8161/admin (Use default credentials admin/admin to login) 
 * 2. Use camel-tutorial2-activemq-send to submit messages
 */
public class App {
	public static void main(String[] args) throws InterruptedException {

		// Starts component for message receiving 
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("camelspring.xml");

		// Send jms message
		ProducerTemplate camelTemplate = context.getBean("camelTemplate", ProducerTemplate.class);
		System.out.println("Message Sending started");
		camelTemplate.sendBody("jms:queue:testMQ","Hello World Camel Spring");
		System.out.println("Message sent");

		Thread.sleep(Long.MAX_VALUE);
		context.close();

	}
}
