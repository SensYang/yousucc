package com.litesuits.common.net;

/**
 * Created by Calvin on 15-10-21.
 */
public interface RequestCallback {
	void onSuccess(String response);

	void onFailure(int statusCode, String responseString);
}
