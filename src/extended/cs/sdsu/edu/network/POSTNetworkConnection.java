package extended.cs.sdsu.edu.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class POSTNetworkConnection extends
		AsyncTask<String, Void, HttpResponse> {

	private StringEntity postComments;
	private HttpResponse response;

	public HttpResponse postNetworkConnection(String url, String comments) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		try {
			postComments = new StringEntity(comments);

			httpPost.setHeader("Content-Type",
					"application/jason;charset =UTF-8");
			httpPost.setEntity(postComments);
			response = httpClient.execute(httpPost);
		} catch (Exception e) {
			Log.e("RateMyProfessor", e.getMessage(), e);
		}
		httpClient.getConnectionManager().shutdown();
		return response;
	}

	@Override
	protected HttpResponse doInBackground(String... params) {
		return postNetworkConnection(params[0], params[1]);
	}

}
