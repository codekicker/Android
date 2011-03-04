package de.codekicker.app.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;

public class QuestionDetails extends Activity implements OnClickListener {
	private ImageButton imageButtonUpvote;
	private ImageButton imageButtonDownvote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_details);
		Question selectedQuestion = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
		TextView textViewHeadline = (TextView) findViewById(R.id.textViewHeadline);
		TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
		imageButtonUpvote = (ImageButton) findViewById(R.id.imageButtonUpvote);
		imageButtonDownvote = (ImageButton) findViewById(R.id.imageButtonDownvote);
		TextView textViewAnswersCount = (TextView) findViewById(R.id.textViewAnswersCount);
		Button buttonAnswer = (Button) findViewById(R.id.buttonAnswer);
		buttonAnswer.setOnClickListener(this);
		imageButtonUpvote.setOnClickListener(this);
		imageButtonDownvote.setOnClickListener(this);
		textViewHeadline.setText(selectedQuestion.getHeadline());
		textViewQuestion.setText(selectedQuestion.getQuestion());
		textViewAnswersCount.setText(String.format(getString(R.string.answersCount), 1));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imageButtonUpvote:
			// TODO: Upvote
			imageButtonUpvote.setImageResource(R.drawable.upvoteselected);
			break;
		case R.id.imageButtonDownvote:
			// TODO: Downvote
			imageButtonDownvote.setImageResource(R.drawable.downvoteselected);
			break;
		case R.id.buttonAnswer:
			// TODO: Send answer
			break;
		}
	}
}