package de.codekicker.app.android.model;

import java.util.ArrayList;
import java.util.Date;
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
	private String title;
	private String urlName;
	private Date askDate;
	private String questionBody;
	private boolean hasAcceptedAnswer;
	private int voteScore;
	private int answerCount;
	private int viewCount;
	private String[] tags;
	private User user;
	private List<Answer> answers;
	
	public Question(int id, String title, String urlName, Date askDate,
					String questionBody, boolean hasAcceptedAnswer, int voteScore,
					int answerCount, int viewCount, String[] tags, User user) {
		this.id = id;
		this.title = title;
		this.urlName = urlName;
		this.askDate = askDate;
		this.questionBody = questionBody;
		this.hasAcceptedAnswer = hasAcceptedAnswer;
		this.voteScore = voteScore;
		this.answerCount = answerCount;
		this.viewCount = viewCount;
		this.tags = tags;
		this.user = user;
		answers = new ArrayList<Answer>();
	}
	
	private Question(Parcel parcel) {
		id = parcel.readInt();
		title = parcel.readString();
		urlName = parcel.readString();
		askDate = new Date(parcel.readLong());
		questionBody = parcel.readString();
		hasAcceptedAnswer = parcel.readInt() == 1;
		voteScore = parcel.readInt();
		answerCount = parcel.readInt();
		viewCount = parcel.readInt();
		tags = parcel.createStringArray();
		user = parcel.readParcelable(User.class.getClassLoader());
		answers = new ArrayList<Answer>();
		parcel.readTypedList(answers, Answer.CREATOR);
	}
	
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getUrlName() {
		return urlName;
	}

	public Date getAskDate() {
		return askDate;
	}

	public String getQuestionBody() {
		return questionBody;
	}

	public boolean hasAcceptedAnswer() {
		return hasAcceptedAnswer;
	}

	public int getVoteScore() {
		return voteScore;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public String[] getTags() {
		return tags;
	}

	public User getUser() {
		return user;
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
		dest.writeString(title);
		dest.writeString(urlName);
		dest.writeLong(askDate.getTime());
		dest.writeString(questionBody);
		dest.writeInt(hasAcceptedAnswer ? 1 : 0);
		dest.writeInt(voteScore);
		dest.writeInt(answerCount);
		dest.writeInt(viewCount);
		dest.writeStringArray(tags);
		dest.writeParcelable(user, flags);
		dest.writeTypedList(answers);
	}
}