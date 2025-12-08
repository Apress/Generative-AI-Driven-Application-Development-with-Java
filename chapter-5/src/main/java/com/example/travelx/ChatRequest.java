package com.example.travelx;

class ChatRequest {
	private String message;
	private String prompt = "Please limit the responses to known facts. If you do not know about the context, simply say 'Sorry, I am not aware of this context. Please let me know if I can help you with any other requests.'";

	public String getMessage() {
 	   	return prompt + " " + message;
	}

	public void setMessage(String message) {
    		this.message = prompt + " " + message;
	}
}