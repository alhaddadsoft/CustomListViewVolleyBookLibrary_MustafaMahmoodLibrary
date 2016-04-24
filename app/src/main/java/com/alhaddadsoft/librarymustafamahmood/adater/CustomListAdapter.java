package com.alhaddadsoft.librarymustafamahmood.adater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.alhaddadsoft.librarymustafamahmood.DownloadBook;
import com.alhaddadsoft.librarymustafamahmood.MainActivity;
import com.alhaddadsoft.librarymustafamahmood.R;
import com.alhaddadsoft.librarymustafamahmood.app.AppController;
import com.alhaddadsoft.librarymustafamahmood.model.Movie;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.artifex.mupdfdemo.MuPDFActivity;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.veinhorn.searchadapter.SearchAdapter;

public class CustomListAdapter extends SearchAdapter<Movie>   {


	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	String saveAsString,pdfUrlString,titleName,imageUrl , generstring;
	Drawable drawable;
	int ratingInt;
	int releaseYeariInt;


	MaterialDialog.Builder builder;
	 MaterialDialog dialog;
	Bitmap b;
	class ViewHolder {
		@InjectView(R.id.title) TextView title;
		@InjectView(R.id.genre) TextView genre;
		@InjectView(R.id.pdfUrl) TextView pdfurl;
		@InjectView(R.id.SaveAs) TextView saveas;
		@InjectView(R.id.rating) TextView rating;
		@InjectView(R.id.releaseYear) TextView releaseyear ;
		@InjectView(R.id.thumbnail) NetworkImageView thumbNail ;
		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}}

	File sdcard = Environment.getExternalStorageDirectory();

	public CustomListAdapter( Context context , List<Movie> movieItems ) {
		super(movieItems ,context);
	}

	@Override
	public View getView(final int position,  View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null){
			convertView = layoutInflater.inflate(R.layout.list_row, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();


		// thumbnail image
		viewHolder.thumbNail.setImageUrl(filteredContainer.get(position).getThumbnailUrl(), imageLoader);

		// title
		viewHolder.title.setText(filteredContainer.get(position).getTitle());
		// pdfUrl
		viewHolder.pdfurl.setText(filteredContainer.get(position).getPdfUrl());
		//SaveAs
		viewHolder.saveas.setText(filteredContainer.get(position).getSaveAs());
		// rating
		viewHolder.rating.setText("حجم الكتاب: " + String.valueOf(filteredContainer.get(position).getRating())+" ميجا بايت");
		//genre
		viewHolder.genre.setText(filteredContainer.get(position).getGenre());
		// release year
		viewHolder.releaseyear.setText(String.valueOf(filteredContainer.get(position).getYear()));

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveAsString = filteredContainer.get(position).getSaveAs();
				pdfUrlString = filteredContainer.get(position).getPdfUrl();
				titleName = filteredContainer.get(position).getTitle();
				imageUrl = filteredContainer.get(position).getThumbnailUrl();
				ratingInt  = filteredContainer.get(position).getRating();
				releaseYeariInt = filteredContainer.get(position).getYear();
				generstring = filteredContainer.get(position).getGenre();
				drawable = viewHolder.thumbNail.getDrawable();

				File filepath = new File( sdcard,"Download/Mustafa_Mahmood/"+filteredContainer.get(position).getSaveAs());
				//bitmap = Bitmap.createBitmap(viewHolder.thumbNail.getDrawingCache());
				try {
					b = ((BitmapDrawable)drawable).getBitmap();
				}catch (Throwable throwable){
					throwable.printStackTrace();
				}
				try {
					b.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(Environment.getExternalStorageDirectory()+"/Download/Mustafa_Mahmood/" + saveAsString+".png"));
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}

				if(filepath.exists()){
					OpenPdfdialog();
				}
				else {
					showDownloadDialog();
				}
			}

		});
		return convertView;
	}
	public void showDownloadDialog(){
		 builder = new MaterialDialog.Builder(context)
				.title(titleName)
				.content(generstring)
				.positiveText("تحميل")
				.negativeText("مشاركة")
				.neutralText("رجوع")
				.icon(drawable)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


						Intent d = new Intent(context, DownloadBook.class);
						d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						d.putExtra("dwnload_file_path", pdfUrlString);
						d.putExtra("save_file_As", saveAsString);
						d.putExtra("total_size",String.valueOf(ratingInt) );
						d.putExtra("getTitle", titleName);

						//d.putExtra("pdfUrlString",pdfUrlString);
						//d.putExtra("saveAsString", saveAsString);
						//d.putExtra("imageUrl", imageUrl);
						//d.putExtra("titleName", titleName);
						//d.putExtra("ratingInt", ratingInt);
						//d.putExtra("releaseYeariInt", releaseYeariInt);
						//d.putExtra("generstring", generstring);
						context.startActivity(d);
						dialog.dismiss();
						((MainActivity)context).finish();					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						String shareBody = "أعجبت بكتاب " + titleName + " " + "للدكتور مصطفى محمود وأريد مشاركة الكتاب معك، تستطيع تحميل الكتاب من مكتبة الدكتور مصطفى محمود على الأندرويد من خلال" +
								" تحميل البرنامج من جوجل بلاي\n" +
								//TODO: change convert package name
								"https://play.google.com/store/apps/details?id=com.alhaddadsoft.librarymustafamahmood";
						sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "د.مصطفى محمود");
						sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
						context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

						onKeyMetricsharebook();
					}
				})
				.onNeutral(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

					}
				});
		dialog = builder.build();
		dialog.show();
	}
	public void OpenPdfdialog(){
		new MaterialDialog.Builder(context)
				.title("الكتاب موجود هل تريد تحميله من جديد ؟")
				.content(generstring)
				.positiveText("نعم")
				.negativeText("لا")
				.neutralText("فتح")
				.icon(drawable)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						Intent d = new Intent(context, DownloadBook.class);
						d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						d.putExtra("dwnload_file_path", pdfUrlString);
						d.putExtra("save_file_As", saveAsString);
						d.putExtra("total_size",String.valueOf(ratingInt) );
						d.putExtra("getTitle", titleName);

						//d.putExtra("pdfUrlString",pdfUrlString);
						//d.putExtra("saveAsString", saveAsString);
						//d.putExtra("imageUrl", imageUrl);
						//d.putExtra("titleName", titleName);
						//d.putExtra("ratingInt", ratingInt);
						//d.putExtra("releaseYeariInt", releaseYeariInt);
						//d.putExtra("generstring", generstring);
						context.startActivity(d);
						dialog.dismiss();
						((MainActivity)context).finish();
					}})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {



					}
				})
				.onNeutral(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download/Mustafa_Mahmood/" +saveAsString);

						Intent intent = new Intent(context, MuPDFActivity.class);

						intent.setAction(Intent.ACTION_VIEW);

						intent.setData(uri);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						context.startActivity(intent);
					}
				})
				.show();
	}
	public void addbooktolibrary(){
		File sdcard = Environment.getExternalStorageDirectory();
		File oldFileName = new File( sdcard,"downloadManager/json.txt");
		File tmpFileName = new File( sdcard,"downloadManager/json.tmp.txt");

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(tmpFileName));

			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("\"data\": [\n" +
						"{"))
					line = line.replace("\"data\": [\n" +
							"{","\"data\": [\n" +
							"{\n" +
							"   \"title\": \"على حافة الزلزال\",\n" +
							"        \"image\": \"http://mostafamahmoud.net/wp-content/uploads/2015/09/431794_42795b9f1c9a577a8bc88e55e8827aad.jpg\",\n" +
							"\t\t\"pdfUrl\": \"http://al-hakawati.net/arabic/civilizations/diwanindex6a22.pdf\",\n" +
							"\t\t\"SaveAs\": \"book1\",\n" +
							"        \"rating\": 8.3,\n" +
							"        \"releaseYear\": 2014,\n" +
							"        \"genre\": \"إنه وباء القرن الواحد والعشرين وطاعون المستقبل والمسلمون المستهدفون في كل مكان عليهم أن يضموا الأيدي ليصدوا هذا الخطر المتسلل عبر الحدود المفتوحة في كل مكان ، وعبر الهواء والفضائيات والإعلام وعبر الصحيفة والخبر والفيلم والكتاب ووسائل غسيل المخ التي تعبر الحدود وتنساح في كل بيت ، فالكلمة هي الطلقة الخفية وهي القذيفة والصاروخ وهي الجاسوس الخفي الذي يغتال القلب ويلوث الضمير بكل ألوان الفساد ، وبروتوكولات حكماء آل صهيون هي ما تفتقت عنه أذهان دهاقنة الصهاينة في إفساد شعوب العالم وحكوماته ، وقد لعنهم أنبياؤهم في التوراة كما لعنهم المسيح في الأناجيل الثلاثة كما لعنهم الله في قرآنه .\"\n" +
							"    },\n" +
							"    {");
				bw.write(line+"\n");
			}

		} catch (Exception e) {
			return;
		} finally {
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				//
			}
			try {
				if(bw != null)
					bw.close();
			} catch (IOException e) {
				//
			}
		}
		boolean deleted = false,rename = false;
		if(oldFileName.exists()){

			deleted = oldFileName.delete(); //this returns a boolean variable.
		}
		if(deleted){
			rename = tmpFileName.renameTo(oldFileName);

		}
		if(!deleted||!rename){
			// log some type of warning here or even throw an exception
		}
		Toast.makeText(context, "تمت إطافة الكتاب إلى المكتبة تستطيع فتح الكتاب الآن اعد تشغيل البرنامج لإنهاء الإعدادات الجدية",Toast.LENGTH_LONG).show();
	}
	public void onKeyMetricsharebook() {
		// TODO: Use your own string attributes to track common values over time
		// TODO: Use your own number attributes to track median value over time
		Answers.getInstance().logCustom(new CustomEvent("مشاركة كتاب")
				.putCustomAttribute("Category", "مشاركة كتاب"+titleName));

		Answers.getInstance().logShare(new ShareEvent()
				.putMethod("General-book")
				.putContentName("عدد مرات مشاركة كتاب")
				.putContentType("مشاركة كتاب")
				.putContentId("601072000245858304"));
	}
}
