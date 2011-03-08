package de.codekicker.app.android.model;

import java.util.ArrayList;
import java.util.List;

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
	private int answersCount;
	private int views;
	private String[] tags;
	private String fromUsername;
	private String elapsedTime;
	private List<Answer> answers;
	
	public Question(int id,
					String headline,
					String question,
					int ratings,
					int answersCount,
					int views,
					String[] tags,
					String fromUsername,
					String elapsedTime) {
		this.id = id;
		this.headline = headline;
		this.question = question;
		this.ratings = ratings;
		this.answersCount = answersCount;
		this.views = views;
		this.tags = tags;
		this.fromUsername = fromUsername;
		this.elapsedTime = elapsedTime;
		answers = new ArrayList<Answer>();
	}
	
	private Question(Parcel parcel) {
		id = parcel.readInt();
		headline = parcel.readString();
		question = parcel.readString();
		ratings = parcel.readInt();
		answersCount = parcel.readInt();
		views = parcel.readInt();
		tags = parcel.createStringArray();
		fromUsername = parcel.readString();
		elapsedTime = parcel.readString();
		answers = new ArrayList<Answer>();
		parcel.readTypedList(answers, Answer.CREATOR);
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
	
	public int getAnswersCount() {
		return answersCount;
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
	
	public void add(Answer answer) {
		answers.add(answer);
	}
	
	public Iterable<Answer> getAnswers() {
		return answers;
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
		dest.writeInt(answersCount);
		dest.writeInt(views);
		dest.writeStringArray(tags);
		dest.writeString(fromUsername);
		dest.writeString(elapsedTime);
		dest.writeList(answers);
	}
}