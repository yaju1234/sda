package com.strapin.bean;

public class GnarFactorChartBean {
	private String Id;
	private int Image;
	private String Username;
	private String chart_text;
	
	public GnarFactorChartBean(String id, int image, String username,
			String chart_text) {
		super();
		Id = id;
		Image = image;
		Username = username;
		this.chart_text = chart_text;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public int getImage() {
		return Image;
	}
	public void setImage(int image) {
		Image = image;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getChart_text() {
		return chart_text;
	}
	public void setChart_text(String chart_text) {
		this.chart_text = chart_text;
	}
}
