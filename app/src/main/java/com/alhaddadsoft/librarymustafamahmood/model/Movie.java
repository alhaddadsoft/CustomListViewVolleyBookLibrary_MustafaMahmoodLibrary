package com.alhaddadsoft.librarymustafamahmood.model;

public class Movie {
	private String title, thumbnailUrl , pdfUrl , SaveAs;
	private int year;
	private int rating;
	private String genre;

	public Movie() {
	}

	public Movie(String name, String thumbnailUrl,String pdfUrl , String SaveAs, int year, int rating,
				 String genre) {
		this.title = name;
		this.thumbnailUrl = thumbnailUrl;
		this.pdfUrl = pdfUrl;
		this.SaveAs = SaveAs;
		this.year = year;
		this.rating = rating;
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getSaveAs() {
		return SaveAs;
	}

	public void setSaveAs(String SaveAs) {
		this.SaveAs = SaveAs;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
