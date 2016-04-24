package com.alhaddadsoft.librarymustafamahmood;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.alhaddadsoft.librarymustafamahmood.adater.AppStatus;
import com.alhaddadsoft.librarymustafamahmood.adater.CustomListAdapter;
import com.alhaddadsoft.librarymustafamahmood.adater.CustomListAdapter2;
import com.alhaddadsoft.librarymustafamahmood.model.Movie;
import com.crashlytics.android.Crashlytics;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codechimp.apprater.AppRater;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.veinhorn.searchadapter.SearchAdapter;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String TAG_SaveAs = "SaveAs";

	private static final String url = "http://api.androidhive.info/json/movies.json";
	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<>();
	private List<Movie> movieList2 = new ArrayList<>();

	@InjectView(R.id.list) ListView gridView;
	@InjectView(R.id.etSearch) EditText editText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Answers(), new Crashlytics());
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/Roboto-Light.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build());
		setContentView(R.layout.activity_main);

		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#1b1b1b")));
		ButterKnife.inject(this);

		AppRater.app_launched(this);

		TextView emptyText = (TextView)findViewById(android.R.id.empty);
		gridView.setEmptyView(emptyText);

		//try {
		//	File yourFile = new File(Environment.getExternalStorageDirectory(), "/downloadManager/booksDownloadJson.txt");
		//	FileInputStream stream = new FileInputStream(yourFile);
			//String jsonStr = null;
			//try {
		//		FileChannel fc = stream.getChannel();
			//	MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			//	jsonStr = Charset.defaultCharset().decode(bb).toString();
			//	jsonobject = Charset.defaultCharset().decode(bb).toString();
		///	}
		//	finally {
		//		stream.close();
		//	}
		try{
			JSONObject jsonObj = new JSONObject(loadJSONFromAsset());
			JSONArray data  = jsonObj.getJSONArray("data");
			// looping through All nodes


			for (int i = 0; i < data.length(); i++) {


				String title = data.getJSONObject(i).getString("title");
				String setThumbnailUrl = data.getJSONObject(i).getString("image");
				String pdfUrl = data.getJSONObject(i).getString("pdfUrl");
				String SaveAs = data.getJSONObject(i).getString("SaveAs");
				String generEdit = data.getJSONObject(i).getString("genre");
				int setRating = (data.getJSONObject(i).getInt("rating"));
				int setYear = data.getJSONObject(i).getInt("releaseYear");
				File sdcard = Environment.getExternalStorageDirectory();
				File filepath = new File (sdcard,"Download/Mustafa_Mahmood/"+data.getJSONObject(i).getString("SaveAs"));
				//File fp = new File (Environment.getExternalStorageDirectory(),"downloadManager/"+c.getString("SaveAs")+".png");
				//	JSONObject c = data.getJSONObject(i);



				if (filepath.exists() && data.getJSONObject(i).getString("SaveAs").equals(filepath.getName())) {
					//String jsonobjectstring = c.getString("SaveAs");
					Movie movie = new Movie();
					//Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();

					movie.setTitle(title);
					movie.setThumbnailUrl(setThumbnailUrl);
					movie.setPdfUrl(pdfUrl);
					movie.setSaveAs(SaveAs);
					movie.setRating(((Number) setRating)
							.intValue());
					movie.setYear(setYear);
					// Genre is json array
					movie.setGenre(generEdit);
					// do what do you want on your interface
					movieList.add(movie);
				}else if (!filepath.exists()){

				}
				//jsonobject  = c.getString("SaveAs");





				hidePDialog();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final SearchAdapter adapters = new CustomListAdapter2(this,movieList).registerFilter(Movie.class, "title")
				.setIgnoreCase(true);
		gridView.setAdapter(adapters);
		// changing action bar color

		editText.addTextChangedListener(new TextWatcher() {
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapters.filter(s.toString());
			}
			@Override public void afterTextChanged(Editable s) {}
		});


	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public  boolean onOptionsItemSelected(MenuItem item){
	switch (item.getItemId())
	{
		case R.id.action_DownloadBook:
			//Intent i = new Intent(this , info.androidhive.customlistviewvolleydb.MainActivity.class);
		//	startActivity(i);
			// Creating volley request obj
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.activity2main);
			ButterKnife.inject(this);

			// checkif there is internet connection


			//list.setAdapter(adapter2);
			gridView = (ListView) dialog.findViewById(R.id.list);
			editText = (EditText) dialog.findViewById(R.id.etSearch);

					//File yourFile2 = new File (String.valueOf(getResources().openRawResource(R.raw.booksdownloadjson)));
					//	File yourFile = new File(Environment.getExternalStorageDirectory(), "/downloadManager/booksDownloadJson.txt");
					//	FileInputStream stream = new FileInputStream(yourFile));

//					String jsonStr = null;
					//			try {
					//					FileChannel fc = stream.getChannel();
					//					MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
					//					jsonStr = Charset.defaultCharset().decode(bb).toString();
					//			}
					//				finally {
					//					stream.close();
					//				}
					try {
						JSONObject jsonObj = new JSONObject(loadJSONFromAsset());

						// Getting data JSON Array nodes
						JSONArray data = jsonObj.getJSONArray("data");

						// looping through All nodes
						for (int i = 0; i < data.length(); i++) {
							JSONObject c = data.getJSONObject(i);
							Movie movie = new Movie();

							String title = c.getString("title");
							String setThumbnailUrl = c.getString("image");
							String pdfUrl = c.getString("pdfUrl");
							String SaveAs = c.getString("SaveAs");
							String generEdit = c.getString("genre");

							int setRating = (c.getInt("rating"));
							int setYear = c.getInt("releaseYear");

							// adding each child node to HashMap key => value
							movie.setTitle(title);
							movie.setThumbnailUrl(setThumbnailUrl);
							movie.setPdfUrl(pdfUrl);
							movie.setSaveAs(SaveAs);
							movie.setRating(((Number) setRating)
									.intValue());
							movie.setYear(setYear);

							// Genre is json array
							movie.setGenre(generEdit);

							// do what do you want on your interface
							movieList2.add(movie);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}


			final SearchAdapter adapter22 = new CustomListAdapter(this,movieList2).registerFilter(Movie.class, "title")
					.setIgnoreCase(true);
			gridView.setAdapter(adapter22);
			editText.addTextChangedListener(new TextWatcher() {
				@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
					adapter22.filter(s.toString());
				}
				@Override public void afterTextChanged(Editable s) {}
			});

			dialog.show();
			if (AppStatus.getInstance(this).isOnline()) {
				//Toast.makeText(this,"You are online!!!!",Toast.LENGTH_LONG).show();

			} else {
				//Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_LONG).show();
				//Log.v("Home", "############################You are not online!!!!");
				new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
						.setTitleText("no internet!")
						.setContentText("يجب توفر انترنت لتحميل الكتب ")
						.setCustomImage(R.drawable.warning_circle)
						.show();
			}
		case R.id.action_otherlibrary:
			final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(this);
			adapter.add(new MaterialSimpleListItem.Builder(this)
					.content("مكتبة : جبران خليل جبران")
					.icon(R.drawable.gbrank)
					.backgroundColor(Color.WHITE)
					.build());
			adapter.add(new MaterialSimpleListItem.Builder(this)
					.content("مكتبة : (نزار قباني + محمود درويش )")
					.icon(R.drawable.nizar_mahmood)
					.backgroundColor(Color.WHITE)
					.build());
			//adapter.add(new MaterialSimpleListItem.Builder(this)
			//		.content(R.string.add_account)
			//		.icon(R.drawable.ic_content_add)
			//		.iconPaddingDp(8)
			//		.build());
			new MaterialDialog.Builder(this)
					.title("مكتبات اخرى")
					.adapter(adapter, new MaterialDialog.ListCallback() {
						@Override
						public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
							MaterialSimpleListItem item = adapter.getItem(which);
							if(item.getContent().toString().equals("مكتبة : جبران خليل جبران")){

								//final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
								try {
									startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
											("market://details?id=com.alhaddadsoft.ammar.listviewbook" /*+ appPackageName*/)));
								} catch (android.content.ActivityNotFoundException anfe) {
									startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
											("http://play.google.com/store/apps/details?id=com.alhaddadsoft.ammar.listviewbook" /*+ appPackageName*/)));
								}
								onKeyMetric();
									//Toast.makeText(MainActivity.this, "You Clicked : "+ item.getContent().toString(), Toast.LENGTH_SHORT).show();

							} else if(item.getContent().equals("مكتبة : (نزار قباني + محمود درويش )")){
								try {
									startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
											("market://details?id=com.alhaddadsoft.adabiat" /*+ appPackageName*/)));
								} catch (android.content.ActivityNotFoundException anfe) {
									startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
											("http://play.google.com/store/apps/details?id=com.alhaddadsoft.adabiat" /*+ appPackageName*/)));
								}
								onKeyMetric2();

								//Toast.makeText(MainActivity.this, "You Clicked : "+ item.getContent().toString(), Toast.LENGTH_SHORT).show();

							}
						//	showToast(item.getContent().toString());
						}
					})
					.show();
			break;

			case R.id.about:
				new MaterialDialog.Builder(this)
						.title("About")
						.content(R.string.aboutpage)
						.positiveText("ok")
						.show();
				break;

		case R.id.share:
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = "أريد معك مشاركة برنامج (مكتبة دكتور مصطفى محمود ) تستطيع" +
					" تحميل المكتبة من الرابط التالي\n" +
					//TODO: change convert package name
					"https://play.google.com/store/apps/details?id=com.alhaddadsoft.librarymustafamahmood";
			sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "د.مصطفى محمود");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
			onKeyMetricshareapp();
			break;
	}

		return true;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
 @Override
 protected void attachBaseContext(Context newBase) {
	 super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
 }


	public String loadJSONFromAsset() {
		String json = null;
		try {
			InputStream is = MainActivity.this.getAssets().open("booksdownloadjson.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}
	// TODO: Move this method and use your own event name to track your key metrics
	public void onKeyMetric() {
		// TODO: Use your own string attributes to track common values over time
		// TODO: Use your own number attributes to track median value over time
		Answers.getInstance().logCustom(new CustomEvent("ضغط على المكتبات-جبران")
				.putCustomAttribute("Category", "جبران"));

		Answers.getInstance().logCustom(new CustomEvent("جبران clicks")
				.putCustomAttribute("Category", "جبران"));
	}
	public void onKeyMetric2() {
		// TODO: Use your own string attributes to track common values over time
		// TODO: Use your own number attributes to track median value over time
		Answers.getInstance().logCustom(new CustomEvent("ضغط على المكتبات")
				.putCustomAttribute("Category", "نزار قباني"));
		Answers.getInstance().logCustom(new CustomEvent("نزار clicks")
				.putCustomAttribute("Category", "نزار قباني"));
	}
	public void onKeyMetricshareapp() {
		// TODO: Use your own string attributes to track common values over time
		// TODO: Use your own number attributes to track median value over time
		Answers.getInstance().logCustom(new CustomEvent("مشاركة البرنامج")
			.putCustomAttribute("Category", "مشاركة البرنامج"));
		Answers.getInstance().logShare(new ShareEvent()
				.putMethod("General-app")
				.putContentName("عدد مرات مشاركة البرنامج")
				.putContentType("مشاركة البرنامج")
				.putContentId("601072000245858305"));
	}

}
