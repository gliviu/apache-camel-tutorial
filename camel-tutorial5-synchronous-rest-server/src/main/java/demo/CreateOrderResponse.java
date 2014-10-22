package demo;

public class CreateOrderResponse {
	public String status;
	public String[] errors;

	public CreateOrderResponse() {

	}

	public CreateOrderResponse(String responseStatus, String[] errors) {
		this.status = responseStatus;
		this.errors = errors;
	}
}