package de.codekicker.app.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
	public static final Parcelable.Creator<Answer> CREATOR = new Creator<Answer>() {
		@Override
		public Answer[] newArray(int size) {
			return new Answer[size];
		}
		
		@Override
		public Answer createFromParcel(Parcel source) {
			return new Answer(source);
		}
	};
	
	private String text;
	private String username;
	private String elapsedTime;
	private int reputation;
	
	public Answer(String text,
				  String username,
				  String elapsedTime,
				  int reputation) {
		this.text = text;
		this.username = username;
		this.elapsedTime = elapsedTime;
		this.reputation = reputation;
	}

	public Answer(Parcel parcel) {
		text = parcel.readString();
		username = parcel.readString();
		elapsedTime = parcel.readString();
		reputation = parcel.readInt();
	}

	public String getText() {
		return text;
	}

	public String getUsername() {
		return username;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public int getReputation() {
		return reputation;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(text);
		dest.writeString(username);
		dest.writeString(elapsedTime);
		dest.writeInt(reputation);
	}
}