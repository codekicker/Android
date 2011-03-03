package de.codekicker.app.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;

public class QuestionDetails extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_details);
		Question selectedQuestion = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
		TextView headlineTextView = (TextView) findViewById(R.id.headlineTextView);
		TextView questionTextView = (TextView) findViewById(R.id.questionTextView);
		Button answerButton = (Button) findViewById(R.id.answerButton);
		answerButton.setOnClickListener(this);
		headlineTextView.setText(selectedQuestion.getHeadline());
		questionTextView.setText(selectedQuestion.getQuestion());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.answerButton:
			// TODO: Send answer
			break;
		}
	}
}