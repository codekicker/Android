package de.codekicker.app.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
	public static final Parcelable.Creator<Question> CREATOR = new Creator<Question>() {
		@Override
		public Question[] newArray(int size) {
			return new Question[size];
		}
		
		@Override
		public Question createFromParcel(Parcel source) {
			return new Question(source);
		}
	};
	
	private String headline;
	private String question;
	private int ratings;
	private int answers;
	private int views;
	private String[] tags;
	private String fromUsername;
	
	public Question(String headline,
					String question,
					int ratings,
					int answers,
					int views,
					String[] tags,
					String fromUsername) {
		this.headline = headline;
		this.question = question;
		this.ratings = ratings;
		this.answers = answers;
		this.views = views;
		this.tags = tags;
		this.fromUsername = fromUsername;
	}
	
	private Question(Parcel parcel) {
		headline = parcel.readString();
		question = parcel.readString();
		ratings = parcel.readInt();
		answers = parcel.readInt();
		views = parcel.readInt();
		tags = parcel.createStringArray();
		fromUsername = parcel.readString();
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public int getRatings() {
		return ratings;
	}
	
	public int getAnswers() {
		return answers;
	}
	
	public int getViews() {
		return views;
	}
	
	public String[] getTags() {
		return tags;
	}
	
	public String getFromUsername() {
		return fromUsername;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(headline);
		dest.writeString(question);
		dest.writeInt(ratings);
		dest.writeInt(answers);
		dest.writeInt(views);
		dest.writeStringArray(tags);
		dest.writeString(fromUsername);
	}
}