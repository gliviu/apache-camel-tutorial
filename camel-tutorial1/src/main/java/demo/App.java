package demo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://saltnlight5.blogspot.ro/2013/08/getting-started-with-apache-camel-using.html
 * 
 */
public class App {
	static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		LOG.info("START");
		final CamelContext context = new DefaultCamelContext();
		SimpleRegistry registry = new SimpleRegistry();
		((DefaultCamelContext) context).setRegistry(registry);
		registry.put("log", new MsgLogger());
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://c:/tmp/00/camelSourceFolder").convertBodyTo(String.class).to("bean:log").to("file://c:/tmp/00/camelDestFolder");
			}
		});
		context.setTracing(true);
		context.start();

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			LOG.info("END");
		}
	}

	public static class MsgLogger {
		public void process(Exchange msg) throws Exception {
			LOG.info("Message logger IN: {}", msg.getIn().getBody());
			if(msg.hasOut()){
				LOG.info("Message logger OUT: {}", msg.getOut().getBody());
			}
		}
	}
}
