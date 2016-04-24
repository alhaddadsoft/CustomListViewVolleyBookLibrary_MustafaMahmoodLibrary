package com.alhaddadsoft.librarymustafamahmood.adater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.veinhorn.searchadapter.SearchAdapter;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.alhaddadsoft.librarymustafamahmood.R;
import com.alhaddadsoft.librarymustafamahmood.app.AppController;
import com.alhaddadsoft.librarymustafamahmood.model.Movie;

public class CustomListAdapter2 extends SearchAdapter<Movie>   {


	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	String saveAsString,pdfUrlString,titleName,imageUrl , generstring;
	int ratingInt;
	int releaseYeariInt;

	Drawable drawable;
	Bitmap bitmap;
	File sdcard = Environment.getExternalStorageDirectory();

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

	public CustomListAdapter2(Context context , List<Movie> movieItems ) {
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
		//thumbnail
		//viewHolder.thumbNail.setImageUrl(filteredContainer.get(position).getThumbnailUrl(), imageLoader);
		// title
		//viewHolder.title.setText(filteredContainer.get(position).getTitle());
		// pdfUrl
		//viewHolder.pdfurl.setText(filteredContainer.get(position).getPdfUrl());
		//SaveAs
		//viewHolder.saveas.setText(filteredContainer.get(position).getSaveAs());
		// rating
		//viewHolder.rating.setText("Rating: " + String.valueOf(filteredContainer.get(position).getRating()));
		 //genre
		//viewHolder.genre.setText(filteredContainer.get(position).getGenre());
		// release year
		//viewHolder.releaseyear.setText(String.valueOf(filteredContainer.get(position).getYear()));

		//viewHolder.thumbNail.setVisibility(View.INVISIBLE);
		viewHolder.thumbNail.setImageUrl(filteredContainer.get(position).getThumbnailUrl(), imageLoader);

		//viewHolder.thumbNail.setImageDrawable(Drawable.createFromPath(fp.getPath()));
			//viewHolder.thumbNail.setImageUrl(filteredContainer.get(position).getThumbnailUrl(), imageLoader);
			viewHolder.title.setText(filteredContainer.get(position).getTitle());
			viewHolder.pdfurl.setText(filteredContainer.get(position).getPdfUrl());
			viewHolder.saveas.setText(filteredContainer.get(position).getSaveAs());
			viewHolder.rating.setVisibility(View.INVISIBLE);
			viewHolder.rating.setText("حجم الكتاب: " + String.valueOf(filteredContainer.get(position).getRating())+" ميجا بايت");
			viewHolder.genre.setText(filteredContainer.get(position).getGenre());
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
				File sdcard = Environment.getExternalStorageDirectory();
				File filepath = new File( sdcard,"Download/Mustafa_Mahmood/"+filteredContainer.get(position).getSaveAs());
				if(filepath.exists()){
					OpenPdfdialog();
				}
				else {
					Toast.makeText(context, "الكتاب غير موجود الرجاء تحميله من جديد", Toast.LENGTH_LONG).show();
					}
				}

		});
		return convertView;
	}
	public void OpenPdfdialog(){
		File fp = new File (sdcard,"Download/Mustafa_Mahmood/"+saveAsString+".png");
		Bitmap myBitmap = BitmapFactory.decodeFile(sdcard+"/downloadManager/book1.pdf.png");
		Drawable myDrawable = new BitmapDrawable(context.getResources(), fp.getAbsolutePath());

		new MaterialDialog.Builder(context)
				.title(titleName)
				.content(generstring)
				.positiveText("مشاركة الكتاب")
				.negativeText("قراءة")
				.neutralText("رجوع")
				.icon(myDrawable)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						String shareBody = "أعجبت بكتاب " + titleName + " " + "للدكتور مصطفى محمود واريد م الكتاب معك، تستطيع تحميل الكتاب من مكتبة الدكتور مصطفى محمود على الأندرويد من خلال" +
								" تحميل المكتبة من سوق بلاي\n" +
								//TODO: change convert package name
								"https://play.google.com/store/apps/details?id=com.alhaddadsoft.librarymustafamahmood";
						sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "د.مصطفى محمود");
						sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
						context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

						onKeyMetricsharebook();
					}})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
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
				.onNeutral(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

					}
				})
				.show();
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