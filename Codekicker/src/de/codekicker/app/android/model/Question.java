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
	
	private int id;
	private String headline;
	private String question;
	private int ratings;
	private int answers;
	private int views;
	private String[] tags;
	private String fromUsername;
	private String elapsedTime;
	
	public Question(int id,
					String headline,
					String question,
					int ratings,
					int answers,
					int views,
					String[] tags,
					String fromUsername,
					String elapsedTime) {
		this.id = id;
		this.headline = headline;
		this.question = question;
		this.ratings = ratings;
		this.answers = answers;
		this.views = views;
		this.tags = tags;
		this.fromUsername = fromUsername;
		this.elapsedTime = elapsedTime;
	}
	
	private Question(Parcel parcel) {
		id = parcel.readInt();
		headline = parcel.readString();
		question = parcel.readString();
		ratings = parcel.readInt();
		answers = parcel.readInt();
		views = parcel.readInt();
		tags = parcel.createStringArray();
		fromUsername = parcel.readString();
		elapsedTime = parcel.readString();
	}
	
	public int getId() {
		return id;
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
	
	public String getElapsedTime() {
		return elapsedTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(headline);
		dest.writeString(question);
		dest.writeInt(ratings);
		dest.writeInt(answers);
		dest.writeInt(views);
		dest.writeStringArray(tags);
		dest.writeString(fromUsername);
		dest.writeString(elapsedTime);
	}
}