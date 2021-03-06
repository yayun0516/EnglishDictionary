package org.lxh.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends Activity {
	private Button tojson;
	RequestQueue mQueue;
	StringRequest stringRequest;
	Gson gson;
	String str;
	private TextView textView;
	private EditText editText;
	String string = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textView = (TextView) findViewById(R.id.text);
		editText = (EditText) findViewById(R.id.edit);
		tojson = (Button) findViewById(R.id.btn);
		gson = new Gson();
		mQueue = Volley.newRequestQueue(MainActivity.this);
		tojson.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				string = editText.getText().toString();
				String requestUrl = getRequestUrl(string);
				stringRequest = new StringRequest(requestUrl,
						new Response.Listener<String>() {
							public void onResponse(String response) {

								Log.d("TAG", response);
								System.out.println("response=" + response);
								Status1 status = gson.fromJson(response,
										Status1.class);
								StringBuffer buffer = new StringBuffer();// 保存所用字符串

								RetData2 retData2 = status.getRetData();// 第二个对象的获取

								System.out.println("from=" + retData2.getFrom());
								DictResult3 dictResult3;
								dictResult3 = retData2.getDictResult();// 第三个对象的获取
								buffer.append("单词："
										+ dictResult3.getWord_name() + "\n");
								System.out.println("word_name="
										+ dictResult3.getWord_name());
								List<Symbols4> symbols4s = dictResult3
										.getSymbols();// 第四个是对象数组哦，获取对象数组
								buffer.append("音标"
										+ symbols4s.get(0).getPh_en() + "\n");// symbols4s.get(0)用于获取第一个对象
								List<Parts> parts = symbols4s.get(0).getParts();// 同理，最后一个也是对象数组
								for (int i = 0; i < parts.size(); i++) {
									buffer.append("part:"
											+ parts.get(i).getParts()// parts.get(i)获取对象List中的各个对象
											+ "\n");
									buffer.append("词义：");
									for (int j = 0; j < parts.get(i).getMeans().length; j++) {
										String[] aStrings = parts.get(i)
												.getMeans();
										buffer.append(aStrings[j]);
									}
									buffer.append("\n");
								}

								textView.setText(buffer);

							}
						}, new Response.ErrorListener() {
							public void onErrorResponse(VolleyError error) {
								Log.e("TAG", error.getMessage(), error);
							}

						});
				mQueue.add(stringRequest);
			}
		});

	}

	private String getRequestUrl(String word) {
		String url = null;
		if (word != null) {
			url = "http://apistore.baidu.com/microservice/dictionary?query="
					+ word + "&from=en&to=zh";
		}
		return url;
	}

}