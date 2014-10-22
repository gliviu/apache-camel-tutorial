package demo;

public class OrderService {
	public CreateOrderResponse createOrder(CreateOrderRequest request) throws Exception {
		App.LOG.info("Creating order: {}", request.orderId);
		return new CreateOrderResponse("SUCCESS", new String[] { "warn1",
				"warn2" });
	}
	public Boolean deleteOrder(String orderId) throws Exception {
		App.LOG.info("Deleting order: {}", orderId);
		return new Boolean(true);
	}
}