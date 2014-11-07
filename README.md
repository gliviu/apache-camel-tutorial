apache-camel-tutorial
=====================

Collection of getting started tutorials for Apache Camel.

Run each tutorial using the `mvn compile exec...` command from the root folder of git project.

* camel-tutorial1
	* start with camel and a simple java app
	* moves files from source to destination folder; logs transferred information
	* make sure to update required paths in App.java according to your configuration
	* more info at [Getting started with Apache Camel using Java](http://saltnlight5.blogspot.ro/2013/08/getting-started-with-apache-camel-using.html)
	* run: `mvn compile exec:java -pl camel-tutorial1 -Dexec.mainClass="demo.App"`
* camel-tutorial2-activemq-receive
	* Start ActiveMq server using instructions at [Apache Camel + ActiveMQ Example](http://www.pretechsol.com/2013/08/apache-camel-activemq-exampe.html)
	* start this sample to receive messages dispatched by camel-tutorial2-activemq-send
	* leave this started before attempting following sample
	* run: `mvn compile exec:java -pl camel-tutorial2-activemq-receive -Dexec.mainClass="demo.App"`
* camel-tutorial2-activemq-send
	*  SendMessageJms.java - submits messages using Jms api
	*  SendMessageCamel.java - sends Jms messages using Camel api
	* run SendMessageJms: `mvn compile exec:java -pl camel-tutorial2-activemq-send -Dexec.mainClass="demo.SendMessageCamel"`
	* run SendMessageCamel: `mvn compile exec:java -pl camel-tutorial2-activemq-send -Dexec.mainClass="demo.SendMessageJms"`
* camel-tutorial2-activemq-receive-spring
	* Similar to camel-tutorial2-activemq-receive, using spring configuration. In addition to logging received messages, it moves them to another queue
	* Also shows an example of creating a ProducerTemplate with spring context.
	* run: `mvn compile exec:java -pl camel-tutorial2-activemq-receive-spring -Dexec.mainClass="demo.App"`
* camel-tutorial3-synchronous-msg
	* Illustrates basics of calling services synchronously.
	* run: `mvn compile exec:java -pl camel-tutorial3-synchronous-msg -Dexec.mainClass="demo.App"`
* camel-tutorial4-synchronous-pojo-json
	* The same principle as in previous example but this time entities are involved as request/response parameters.
	* A web rest service call is simulated by performing pojo<->json marshalling
	* run: `mvn compile exec:java -pl camel-tutorial4-synchronous-pojo-json -Dexec.mainClass="demo.App"`
* camel-tutorial5-synchronous-rest-server
	* Creates a real rest service using Jetty and Camel
	* Leave it started before running next example
	* run: `mvn compile exec:java -pl camel-tutorial5-synchronous-rest-server -Dexec.mainClass="demo.App"`
* camel-tutorial5-synchronous-rest-client
	* Calls the service exposed by camel-tutorial5-synchronous-rest-server
	* run: `mvn compile exec:java -pl camel-tutorial5-synchronous-rest-client -Dexec.mainClass="demo.App"`
* camel-tutorial6-synchronous-activemq-server
	* Creates a JMS server and responds synchronously to requests using technique described [here](http://activemq.apache.org/how-should-i-implement-request-response-with-jms.html)
	* run: `mvn compile exec:java -pl camel-tutorial6-synchronous-activemq-server -Dexec.mainClass="demo.App"`
* camel-tutorial6-synchronous-activemq-client
	* Calls the service exposed by camel-tutorial6-synchronous-activemq-server
	* run: `mvn compile exec:java -pl camel-tutorial6-synchronous-activemq-client -Dexec.mainClass="demo.SendMessageCamel"`
* camel-tutorial7-publish-subscribe
	* Demonstrates jms publish - subscribe mechanism using virtual topics
	* See [here](https://www.packtpub.com/books/content/using-virtual-destinations-advanced) for info 
	* run `mvn compile exec:java -pl camel-tutorial7-activemq-publish-subscribe -Dexec.mainClass="demo.Subscriber"` to activate the jms server and subscribe to the publisher which will be started next.
	* run `mvn compile exec:java -pl camel-tutorial7-activemq-publish-subscribe -Dexec.mainClass="demo.Publisher"` to publish messages
