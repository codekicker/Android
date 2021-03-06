package de.codekicker.app.android.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import de.codekicker.app.android.business.VoteType;

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
	private final boolean isQuestion;
	private final Date createDate;
	private final String textBody;
	private final int voteScore;
	private VoteType voteType;
	private final boolean isAccepted;
	private final User user;
	private final List<Comment> comments;
	
	public Answer(int id, boolean isQuestion, Date createDate, String textBody, int voteScore, VoteType voteType, boolean isAccepted, User user, List<Comment> comments) {
		this.id = id;
		this.isQuestion = isQuestion;
		this.createDate = createDate;
		this.textBody = textBody;
		this.voteScore = voteScore;
		this.voteType = voteType;
		this.isAccepted = isAccepted;
		this.user = user;
		this.comments = comments;
	}

	private Answer(Parcel parcel) {
		id = parcel.readInt();
		isQuestion = parcel.readInt() == 1;
		createDate = new Date(parcel.readLong());
		textBody = parcel.readString();
		voteScore = parcel.readInt();
		voteType = (VoteType) parcel.readSerializable();
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
	
	public VoteType getVoteType() {
		return voteType;
	}
	
	public void setVoteType(VoteType voteType) {
		this.voteType = voteType;
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
		dest.writeInt(isQuestion ? 1 : 0);
		dest.writeLong(createDate.getTime());
		dest.writeString(textBody);
		dest.writeInt(voteScore);
		dest.writeSerializable(voteType);
		dest.writeInt(isAccepted ? 1 : 0);
		dest.writeParcelable(user, flags);
		dest.writeTypedList(comments);
	}
	
	@Override
	public String toString() {
		return textBody;
	}
}