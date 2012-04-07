package server;

@SuppressWarnings("serial")
class UnknownClientException extends Exception {
	
	private String client;
	
	public UnknownClientException(String client){
		this.client = client;
	}
	
	public String toString() {
		return "Niedozwolona nazwa klienta: " + client;
	}
}
