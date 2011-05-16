package de.codekicker.app.android.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

	private final int id;
	private final Date createDate;
	private final String textBody;
	private final int voteScore;
	private final boolean isAccepted;
	private final User user;
	private final List<Comment> comments;
	
	public Answer(int id, Date createDate, String textBody, int voteScore, boolean isAccepted, User user, List<Comment> comments) {
		this.id = id;
		this.createDate = createDate;
		this.textBody = textBody;
		this.voteScore = voteScore;
		this.isAccepted = isAccepted;
		this.user = user;
		this.comments = comments;
	}

	private Answer(Parcel parcel) {
		id = parcel.readInt();
		createDate = new Date(parcel.readLong());
		textBody = parcel.readString();
		voteScore = parcel.readInt();
		isAccepted = parcel.readInt() == 1;
		user = parcel.readParcelable(User.class.getClassLoader());
		comments = new ArrayList<Comment>();
		parcel.readTypedList(comments, Comment.CREATOR);
	}
	
	public int getId() {
		return id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getTextBody() {
		return textBody;
	}
	
	public int getVoteScore() {
		return voteScore;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public User getUser() {
		return user;
	}

	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(createDate.getTime());
		dest.writeString(textBody);
		dest.writeInt(voteScore);
		dest.writeInt(isAccepted ? 1 : 0);
		dest.writeParcelable(user, flags);
		dest.writeTypedList(comments);
	}
	
	@Override
	public String toString() {
		return textBody;
	}
}