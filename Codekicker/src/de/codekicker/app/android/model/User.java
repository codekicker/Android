package de.codekicker.app.android.model;

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
	private String gravatarID;
	
	public User(int id, String name, String urlName, int reputation, String gravatarID) {
		this.id = id;
		this.name = name;
		this.urlName = urlName;
		this.reputation = reputation;
		this.gravatarID = gravatarID;
	}
	
	private User(Parcel parcel) {
		id = parcel.readInt();
		name = parcel.readString();
		urlName = parcel.readString();
		reputation = parcel.readInt();
		gravatarID = parcel.readString();
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
	
	public String getGravatarID() {
		return gravatarID;
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
		dest.writeString(gravatarID);
	}
}