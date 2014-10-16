package demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Sends a message using Jms API.
 * See http://www.pretechsol.com/2013/08/apache-camel-activemq-exampe.html
 * Start Activemq Messaging System
 * 1. Download activemq from http://activemq.apache.org/download-archives.html
 * 2. Extract activemq zip file and start ActiveMQ (Click on activemq.bat file for windows)
 * 3. Check activemq console using http://localhost:8161/admin  (Use default credentials admin/admin to login) 
 */
public class SendMessageJms {
	public static void main(String[] args) throws JMSException, InterruptedException {
		// Create a ConnectionFactory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"admin", "admin", "tcp://localhost:61616"); // default activemq credentials and broker url
		// Create a Connection
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// Create a Session
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// Create the destination
		Destination destination = session.createQueue("testMQ");
		// Create a MessageProducer from the Session to the Queue
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		// Create a messages
		TextMessage message = session.createTextMessage("Hello World Jms");
		producer.send(message);

		session.close();
		connection.close();
		System.out.println("Message sent");

	}
}