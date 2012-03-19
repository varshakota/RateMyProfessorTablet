package extended.cs.sdsu.edu.network;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class GETNetworkConnection extends AsyncTask<String, Void, String> {

	public String makeGetRequest(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responsebody = null;
		try {
			responsebody = httpClient.execute(httpget, responseHandler);
		} catch (Exception e) {
			Log.e("RateMyProfessor", e.getMessage(), e);
		}
		httpClient.getConnectionManager().shutdown();
		return responsebody;
	}

	@Override
	protected String doInBackground(String... url) {

		return makeGetRequest(url[0]);
	}

}
