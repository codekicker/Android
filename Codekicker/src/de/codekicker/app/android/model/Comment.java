package de.codekicker.app.android.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
	public static final Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {
		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}

		@Override
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}
	};
	
	private Date createDate;
	private String textBody;
	private User user;
	
	public Comment(Date createDate, String textBody, User user) {
		this.createDate = createDate;
		this.textBody = textBody;
		this.user = user;
	}
	
	private Comment(Parcel parcel) {
		createDate = new Date(parcel.readLong());
		textBody = parcel.readString();
		user = parcel.readParcelable(User.class.getClassLoader());
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getTextBody() {
		return textBody;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(createDate.getTime());
		dest.writeString(textBody);
		dest.writeParcelable(user, flags);
	}
}