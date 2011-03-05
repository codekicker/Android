package de.codekicker.app.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;

public class QuestionDetails extends Activity implements OnClickListener {
	private Handler handler = new Handler();
	private ProgressDialog progressDialog;
	private ImageButton imageButtonUpvote;
	private ImageButton imageButtonDownvote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
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
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				progressDialog.hide();
			}
		}, 5000);
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