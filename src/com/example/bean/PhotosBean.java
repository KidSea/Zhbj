package com.example.bean;

import java.util.ArrayList;
/**
 * ��ͼ����
 * @author Kevin
 * @date 2015-10-22
 */
public class PhotosBean {

	public PhotosData data;

	public class PhotosData {
		public ArrayList<PhotoNews> news;
	}

	public class PhotoNews {
		public int id;
		public String listimage;
		public String title;
	}
}
