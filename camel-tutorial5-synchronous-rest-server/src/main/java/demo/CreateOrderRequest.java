package demo;

public class CreateOrderRequest {
	public String orderId;
	public String unit;
	public Integer quantity;

	public CreateOrderRequest() {

	}

	public CreateOrderRequest(String orderId, String unit, Integer quantity) {
		this.orderId = orderId;
		this.unit = unit;
		this.quantity = quantity;
	}
}