package de.codekicker.app.android.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
		
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}
	};
	
	private int id;
	private String name;
	private String urlName;
	private int reputation;
	private String gravatarHash;
	private Bitmap gravatar;
	
	public User(int id, String name, String urlName, int reputation, String gravatarHash) {
		this(id, name, urlName, reputation, gravatarHash, null);
	}
	
	public User(int id, String name, String urlName, int reputation, String gravatarHash, Bitmap gravatar) {
		this.id = id;
		this.name = name;
		this.urlName = urlName;
		this.reputation = reputation;
		this.gravatarHash = gravatarHash;
		this.gravatar = gravatar;
	}
	
	private User(Parcel parcel) {
		id = parcel.readInt();
		name = parcel.readString();
		urlName = parcel.readString();
		reputation = parcel.readInt();
		gravatarHash = parcel.readString();
		gravatar = parcel.readParcelable(Bitmap.class.getClassLoader());
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrlName() {
		return urlName;
	}

	public int getReputation() {
		return reputation;
	}
	
	public String getGravatarHash() {
		return gravatarHash;
	}
	
	public Bitmap getGravatar() {
		return gravatar;
	}
	
	public void setGravatar(Bitmap gravatar) {
		this.gravatar = gravatar;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(urlName);
		dest.writeInt(reputation);
		dest.writeString(gravatarHash);
		dest.writeParcelable(gravatar, flags);
	}
}