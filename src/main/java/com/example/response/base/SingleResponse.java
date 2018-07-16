/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.response.base;

import com.example.constant.ResponseCode;
import com.example.constant.ResponseStatus;

public class SingleResponse extends BaseResponse {

	private Object data;

	public SingleResponse(String status, String code, String message , Object data ) {
		super(status, code, message);
		this.setData(data);
	}

	private SingleResponse(Builder builder) {
		super(builder);
		this.data = builder.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public static Builder builder(){
		return new Builder();
	}


    public static class Builder extends BaseBuilder<Builder>{
		private Object data;

		public Builder data(Object data) {
			this.data = data;
			return this;
		}

		public Builder fromPrototype(SingleResponse prototype) {
			super.fromPrototype(prototype);
			data = prototype.data;
			return this;
		}

		public SingleResponse build() {
			return new SingleResponse(this);
		}

		@Override
		protected Builder getInstance() {
			return this;
		}
	}

	public static SingleResponse found(Object data){
		return (SingleResponse) builder()
				.data(data)
				.code(ResponseCode.RESPONSE_CODE_SUCCESS)
				.status(ResponseStatus.RESPONSE_STATUS_SUCCESS)
				.message("Found")
				.build();
	}

	public static SingleResponse create(Object data){
		return (SingleResponse) builder()
				.data(data)
				.code(ResponseCode.RESPONSE_CODE_CREATED)
				.status(ResponseStatus.RESPONSE_STATUS_SUCCESS)
				.message("Created")
				.build();
	}


	public static BaseResponse updated(Object data) {
		return (SingleResponse) builder()
				.data(data)
				.code(ResponseCode.RESPONSE_CODE_SUCCESS)
				.status(ResponseStatus.RESPONSE_STATUS_SUCCESS)
				.message("Updated")
				.build();
	}

	public static SingleResponse notFound(){
		return (SingleResponse) builder()
				.data(null)
				.code(ResponseCode.RESPONSE_CODE_SUCCESS)
				.status(ResponseStatus.RESPONSE_STATUS_SUCCESS)
				.message("Not Found")
				.build();
	}

}
