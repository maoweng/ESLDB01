package com.javahelps.esldb01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

// import com.javahelps.esldb01.R.id;

public class MainActivity extends AppCompatActivity {
    // variables for DB
    DatabaseHelper dbHeplper;

    int wordPointer = 0;
    int totalWords = 0;
    String db_name = "multilanguage04.db";
    int questionMode = 0; // 0: description, 1: first language, 2: photo match with question_array
    int totalQuestions = 1;
    int questionPointer = 0;
    int answerOK = 0;
    int answerFail = 0;
    String[] rightAnswer = {"$0", "$1", "$2", "$3", "$4", "$5", "$6", "$7", "$8", "$9"};
    String[] answer = {"$0", "$1", "$2", "$3", "$4", "$5", "$6", "$7", "$8", "$9"};


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    // Start: added methods



    // read Vocubulary info to display same as startWords button onClick
    private void startingWord(int wordNum) {
        readDBinfo(0);      // get total number of DB's records
        displayWordPointer(wordPointer + 1);  // start from 1 not 0 for display
        wordDescription(wordPointer);
        wordExample(wordPointer);
        firstLanguage(wordPointer);
        showWordImage(wordPointer, 0);

    }


    private String readDBinfo(int mode) {

        String tb_name = "wordsList";
        String allinfo = "MaxRecord";
        SQLiteDatabase db;

        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        // db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tb_name, null);    // Quiry tb_name's data

        switch (mode) {
            case 0:
                totalWords = c.getCount();
                allinfo += c.getCount();
                break;

            case 1:
                if (c.getCount() > 0) {    // check data records?
                    String str = "Total " + c.getCount() + " records data\n";
                    str += "-----\n";

                    c.moveToFirst();    // move to first record
                    do {        // read records sequentially and cascate into String
                        str += "number:" + c.getString(0) + "\n";
                        str += "word:" + c.getString(1) + "\n";
                        str += "description:" + c.getString(2) + "\n";
                        str += "Example:" + c.getString(3) + "\n";
                        str += "-----\n";
                    } while (c.moveToNext());    // loop if data were valid
                    allinfo = str;
                }
                break;
            default:
                break;

        }
        //	TextView txv=(TextView)findViewById(R.id.txv);
        //	txv.setText(str);
        db.close();
        return allinfo;
    }

    private String readDBinfo(int column, int recNum) {

        String tb_name = "wordsList";
        String allinfo = "";
        SQLiteDatabase db;

        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        // db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tb_name, null);    // Quiry tb_name's data

        if (recNum < c.getCount()) {    // check data records?

            c.moveToPosition(recNum);
            allinfo = c.getString(column);
        } else {
            return "Data: Out of range.";
        }
        //	TextView txv=(TextView)findViewById(R.id.txv);
        //	txv.setText(str);
        db.close();
        return allinfo;
    }

    private String firstLanguageinfo(int column, int recNum) {

        String tb_name = "taiwanese";
        String allinfo = "";
        SQLiteDatabase db;

        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM " + tb_name, null);    // Quiry tb_name's data

        if (recNum < c.getCount()) {    // check data records?

            c.moveToPosition(recNum);
            allinfo = c.getString(column);
        } else {
            return "Data: Out of range. Max record: " + c.getCount();
        }
        db.close();
        return allinfo;
    }

    private void showWordImage(int point, int mode) {
        ImageView img = (ImageView) findViewById(R.id.wordImage);   // for mode = 0 only
        String word = readDBinfo(1, point);

        switch (mode) {
            case 0:
                findViewById(R.id.wordImage).setVisibility(View.VISIBLE);
                //        img = (ImageView) findViewById(R.id.wordImage);
                break;
            case 1:
                findViewById(R.id.exerciseImage).setVisibility(View.VISIBLE);

                img = (ImageView) findViewById(R.id.exerciseImage);
                break;
            default:
                break;
        }


        switch (word) {
            case "apple":                               // 0
                img.setImageResource(R.drawable.apple);
                break;
            case "banana":
                img.setImageResource(R.drawable.banana);
                break;
            case "orange":
                img.setImageResource(R.drawable.orange);
                break;
            case "peach":
                img.setImageResource(R.drawable.peach);
                break;
            case "pineapple":
                img.setImageResource(R.drawable.pineapple);
                break;
            case "strawberry":
                img.setImageResource(R.drawable.strawberry);
                break;
            case "book":
                img.setImageResource(R.drawable.books);
                break;
            case "computer":
                img.setImageResource(R.drawable.computer);
                break;
            case "rice":
                img.setImageResource(R.drawable.rice);
                break;
            case "noodle":
                img.setImageResource(R.drawable.noodles);
                break;
            default:
                findViewById(R.id.wordImage).setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void initMain() {
        readDBinfo(0);

        wordPointer = 0;
        startingWord(wordPointer);

        questionMode = 0;           // Description
        questionPointer = 0;
        resetExercise();
        findViewById(R.id.answerGroup).setVisibility(View.INVISIBLE);// initial totalwords for methods
    }


    // End: added methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//  added main code from here
        dbHeplper = new DatabaseHelper(getApplicationContext());
        try {
            dbHeplper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        initMain();

      //  initDebug();


//  added main code end of here
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    // --------------------------------------------------------------------------
// Event triggers methods

    /*
    public void practicExercise(View v) {                  // for spinner only
           String[] questions = getResources().getStringArray(R.array.question_array);
           questionMode = question.getSelectedItemPosition();
       }
   */
    public void show(View v) {
        int visible = View.VISIBLE, gone = View.GONE;
        TextView displayMsg = (TextView) findViewById(R.id.dispMsg);
        RadioGroup learningType =
                (RadioGroup) findViewById(R.id.learnType);

        switch (learningType.getCheckedRadioButtonId()) {
            case R.id.onWord:    // word learning
                displayMsg.setText("Word learning was selected.");
                findViewById(R.id.wordSelectSection).setVisibility(visible);
                findViewById(R.id.descriptionView).setVisibility(visible);
                findViewById(R.id.sentenceView).setVisibility(visible);
                findViewById(R.id.firstLanguageView).setVisibility(visible);
                findViewById(R.id.wordImage).setVisibility(visible);

                findViewById(R.id.exerciseSelSection).setVisibility(gone);
                findViewById(R.id.questionView).setVisibility(gone);
                findViewById(R.id.questionControlView).setVisibility(gone);
                findViewById(R.id.answerGroup).setVisibility(gone);
                findViewById(R.id.exerciseImage).setVisibility(gone);

                startingWord(wordPointer);

                break;
            case R.id.onExercise:    // Exercise practicing
                displayMsg.setText("Exercises practicing was selected.");
                findViewById(R.id.wordSelectSection).setVisibility(gone);
                findViewById(R.id.descriptionView).setVisibility(gone);
                findViewById(R.id.sentenceView).setVisibility(gone);
                findViewById(R.id.firstLanguageView).setVisibility(gone);
                findViewById(R.id.wordImage).setVisibility(gone);

                findViewById(R.id.exerciseSelSection).setVisibility(visible);
                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.questionControlView).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);
                findViewById(R.id.exerciseImage).setVisibility(visible);


                resetExercise();
                if (questionMode == 0) findViewById(R.id.exerciseImage).setVisibility(gone);

                break;

            case R.id.onBoth:    // Word learning and Exercise practicing
                displayMsg.setText("Exercises practicing was selected." + "\nWord learning was selected.");
                findViewById(R.id.wordSelectSection).setVisibility(visible);
                findViewById(R.id.descriptionView).setVisibility(visible);
                findViewById(R.id.sentenceView).setVisibility(visible);
                findViewById(R.id.firstLanguageView).setVisibility(visible);
                findViewById(R.id.wordImage).setVisibility(visible);

                findViewById(R.id.exerciseSelSection).setVisibility(visible);
                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.questionControlView).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);
                findViewById(R.id.exerciseImage).setVisibility(visible);

                if (questionMode == 0) findViewById(R.id.exerciseImage).setVisibility(gone);

                startingWord(wordPointer);
                //   displayQuestionPointer(1);
                resetExercise();
                break;

            default:            // ?? for other possible values and do nothing
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.javahelps.esldb01/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.javahelps.esldb01/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void displayWordPointer(int number) {
        TextView wordTextView = (TextView) findViewById(
                R.id.wordNumber);
        wordTextView.setText("" + number);
    }

    private void wordDescription(int number) {
        TextView wordTextView = (TextView) findViewById(
                R.id.descriptionView);
        String iMsg;
        iMsg = readDBinfo(1, wordPointer) + ": " + readDBinfo(2, wordPointer);
        wordTextView.setText(iMsg);
    }

    private void wordExample(int number) {
        TextView wordTextView = (TextView) findViewById(
                R.id.sentenceView);
        String iMsg;
        iMsg = readDBinfo(3, wordPointer);
        wordTextView.setText(iMsg);
    }

    private void firstLanguage(int number) {
        TextView wordTextView = (TextView) findViewById(
                R.id.firstLanguageView);
        String iMsg;
        iMsg = firstLanguageinfo(1, wordPointer);
        wordTextView.setText(iMsg);
    }

    // start from 1(reset) or last word
    public void startWords(View view) {
        wordPointer = 0;
        readDBinfo(0);      // get total number of DB's records
        displayWordPointer(wordPointer + 1);  // start from 1 not 0 for display
        wordDescription(wordPointer);
        wordExample(wordPointer);
        firstLanguage(wordPointer);
        showWordImage(wordPointer, 0);

    }

    // for word learning
    public void increasing(View view) {

        if (wordPointer + 1 < totalWords) wordPointer++;

        displayWordPointer(wordPointer + 1);

        wordDescription(wordPointer);
        wordExample(wordPointer);
        firstLanguage(wordPointer);
        showWordImage(wordPointer, 0);

    }

    public void increa100(View view) {


        if (wordPointer + 100 < totalWords) {
            wordPointer += 100;
        } else {
            wordPointer = totalWords - 1;
        }

        displayWordPointer(wordPointer + 1);

        wordDescription(wordPointer);
        wordExample(wordPointer);
        firstLanguage(wordPointer);
        showWordImage(wordPointer, 0);


    }

    public void decreasing(View view) {

        if (wordPointer > 0) wordPointer--;
        displayWordPointer(wordPointer + 1);

        wordDescription(wordPointer);
        wordExample(wordPointer);
        firstLanguage(wordPointer);
        showWordImage(wordPointer, 0);
    }

    public void decrea100(View view) {
        if (wordPointer > 99) {
            wordPointer -= 100;
        } else {
            wordPointer = 0;
        }
        displayWordPointer(wordPointer + 1);

        wordDescription(wordPointer);
        wordExample(wordPointer);
        firstLanguage(wordPointer);
        showWordImage(wordPointer, 0);
    }


    // on click start button, question may show from 1(reset) or last word
    public void startExercises(View view) {
        int visible = View.VISIBLE, gone = View.GONE;
        getAnswerText();

        questionPointer = 0;
        displayQuestionPointer(questionPointer + 1);

        //    displayQuestionPointer(wordPointer+1);  // start from 1 not 0 for display

        TextView displayMsg = (TextView) findViewById(R.id.dispMsg);
        RadioGroup qType =
                (RadioGroup) findViewById(R.id.questionType);

        switch (qType.getCheckedRadioButtonId()) {
            case R.id.onDescription:    // word learning
                displayMsg.setText("Exercise/Description was selected.");
                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);

                findViewById(R.id.exerciseImage).setVisibility(gone);
                //   findViewById(R.id.answerCheckbox).setVisibility(gone);

                questionMode = 0;


                break;
            case R.id.onFirst:    // Exercise practicing
                displayMsg.setText("Exercises/First language was selected.");
                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);

                findViewById(R.id.exerciseImage).setVisibility(gone);
                //   findViewById(R.id.answerCheckbox).setVisibility(gone);

                questionMode = 1;

                break;

            case R.id.onPhoto:    // Word learning and Exercise practicing
                displayMsg.setText("Exercises/Photo was selected.");

                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.exerciseImage).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);

                //   findViewById(R.id.answerCheckbox).setVisibility(gone);

                questionMode = 2;

                break;

            default:            // ?? for other possible values and do nothing
                break;

        }
        showQuestion();
    }


    private void displayQuestionPointer(int number) {
        TextView wordTextView = (TextView) findViewById(
                R.id.exerciseNumber);
        wordTextView.setText("" + number);
    }

    public void increExercise(View view) {

        getAnswerText();

        if (questionPointer + 1 < totalQuestions) {
            questionPointer++;

        }
        showQuestion();

        displayQuestionPointer(questionPointer + 1);

    }

    public void decreExercise(View view) {

        getAnswerText();

        if (questionPointer > 0) questionPointer--;
        displayQuestionPointer(questionPointer + 1);

        showQuestion();
    }

    private void resetExercise() {
        questionPointer = 0;
        displayQuestionPointer(questionPointer + 1);
        answerOK = 0;       // clear results
        answerFail = 0;     // clear results
        questionPointer = 0;
        for (int i = 0; i < 10; i++) {
            answer[i] = "";             // clear previous answer
            rightAnswer[i] = "";        // clear previous right answer
        }
        if (questionMode == 0) findViewById(R.id.exerciseImage).setVisibility(View.GONE);
    }

    // on click button(3) description/first language/photo
    public void showExercise(View v) {
        int visible = View.VISIBLE, gone = View.GONE;

        resetExercise();

        questionPointer = 0;
        displayQuestionPointer(questionPointer + 1);

        TextView displayMsg = (TextView) findViewById(R.id.dispMsg);
        RadioGroup qType =
                (RadioGroup) findViewById(R.id.questionType);

        switch (qType.getCheckedRadioButtonId()) {
            case R.id.onDescription:    // word learning
                displayMsg.setText("Exercise/Description was selected.");
                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);

                findViewById(R.id.exerciseImage).setVisibility(gone);
                //   findViewById(R.id.answerCheckbox).setVisibility(gone);

                questionMode = 0;
                showQuestion();
                break;
            case R.id.onFirst:    // Exercise practicing
                displayMsg.setText("Exercises/First language was selected.");
                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);

                findViewById(R.id.exerciseImage).setVisibility(gone);
                //   findViewById(R.id.answerCheckbox).setVisibility(gone);

                questionMode = 1;
                showQuestion();
                break;

            case R.id.onPhoto:    // Word learning and Exercise practicing
                displayMsg.setText("Exercises/Photo was selected.");

                findViewById(R.id.questionView).setVisibility(visible);
                findViewById(R.id.exerciseImage).setVisibility(visible);
                findViewById(R.id.answerGroup).setVisibility(visible);

                //   findViewById(R.id.answerCheckbox).setVisibility(gone);

                questionMode = 2;
                showQuestion();
                break;

            default:            // ?? for other possible values and do nothing
                break;

        }
    }

    private void showQuestion() {
        String imsg = "";
        int icolumn = 0;        // word number in question0x
        int inum = 0;
        SQLiteDatabase db;
        TextView txv0 = (TextView) findViewById(R.id.questionView);

        switch (questionMode) {
            case 0:                         // show Description
                String tb_name = "question01";


                db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
                // db = this.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM " + tb_name, null);    // Quiry tb_name's data
                totalQuestions = c.getCount();

                if (questionPointer > c.getCount()) {    // check data records?
                    imsg = "finished the questions.";

                } else {
                    c.moveToPosition(questionPointer);
                    inum = c.getInt(icolumn);
                    tb_name = "wordsList";
                    Cursor c01 = db.rawQuery("SELECT * FROM " + tb_name, null);
                    c01.moveToPosition(inum - 1);      // position start from 0 but id from 1
                    imsg = c01.getString(2);         // meaning of wordsList: column 2
                    rightAnswer[questionPointer] = c01.getString(1);       // word name of wordsList : column 1

                }
                txv0.setText(imsg);

                db.close();
                findViewById(R.id.answerGroup).setVisibility(View.VISIBLE);

                break;
            case 1:                         // show first language
                tb_name = "question02";

                db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
                // db = this.getReadableDatabase();
                Cursor c1 = db.rawQuery("SELECT * FROM " + tb_name, null);    // Quiry tb_name's data
                totalQuestions = c1.getCount();
                //   TextView txv1=(TextView)findViewById(R.id.questionView);
                if (questionPointer > c1.getCount()) {    // check data records?
                    imsg = "finished the questions.";

                } else {
                    c1.moveToPosition(questionPointer);
                    inum = c1.getInt(icolumn);              // get question number from question02

                    tb_name = "Taiwanese";          // first language
                    Cursor c11 = db.rawQuery("SELECT * FROM " + tb_name, null);
                    c11.moveToPosition(inum - 1);      // position start from 0 but id from 1
                    imsg = c11.getString(1);         // first language: column 1

                    tb_name = "wordsList";
                    Cursor c12 = db.rawQuery("SELECT * FROM " + tb_name, null);
                    c12.moveToPosition(inum - 1);      // position start from 0 but id from 1
                    rightAnswer[questionPointer] = c12.getString(1);       // word name of wordsList : column 1

                }
                txv0.setText(imsg);

                db.close();
                findViewById(R.id.answerGroup).setVisibility(View.VISIBLE);


                break;
            case 2:                         // show photo
                tb_name = "question03";

                db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
                // db = this.getReadableDatabase();
                Cursor c2 = db.rawQuery("SELECT * FROM " + tb_name, null);    // Quiry tb_name's data
                totalQuestions = c2.getCount();
                //   TextView txv1=(TextView)findViewById(R.id.questionView);
                if (questionPointer > c2.getCount()) {    // check data records?
                    imsg = "finished the questions.";

                } else {
                    c2.moveToPosition(questionPointer);
                    inum = c2.getInt(icolumn);              // get question number from question02

                    tb_name = "wordsList";          // find image name
                    Cursor c21 = db.rawQuery("SELECT * FROM " + tb_name, null);
                    c21.moveToPosition(inum - 1);      // position start from 0 but id from 1
                    //    imsg = c21.getString(1);         // word name: answer of the question
                    imsg = "Please type the name of the photo.";
                    showWordImage(inum - 1, 1);
                    rightAnswer[questionPointer] = c21.getString(1);       // word name of wordsList : column 1

                }
                txv0.setText(imsg);

                db.close();
                findViewById(R.id.answerGroup).setVisibility(View.VISIBLE);


                break;
            default:                        // something wrong
                break;
        }
    }

    // show answerOK and answerFail
    private void displayResult() {
        String imsg = "";
        //  compare answer and rightAnswer

        int itotal = answerOK + answerFail;
        imsg = "Total questions: " + itotal + "\nOK: " + answerOK + "\nFail: " + answerFail;


        TextView displayMsg = (TextView) findViewById(R.id.dispMsg);
        displayMsg.setText(imsg);
        answerOK = 0;
        answerFail = 0;

    }

    // to show next question
    public void nextExercises(View view) {
        getAnswerText();

        if (questionPointer + 1 < totalQuestions) {
            questionPointer++;


        }
        showQuestion();
        displayQuestionPointer(questionPointer + 1);

        EditText et=(EditText) findViewById(R.id.typeAnswer);
        et.setText("");

    }

    // get answer from EDitText
    private String getAnswerText() {
        String iAnswer;
        EditText et = (EditText) findViewById(R.id.typeAnswer);
        iAnswer = et.getText().toString().trim();
        answer[questionPointer] = iAnswer;

        // for debug
        //   showStatus("Answer of Exercise: " + iAnswer);

        return iAnswer;
    }

    // end Exercise submit and get result
    public void endExercises(View view) {

        for (int i = 0; i < totalQuestions; i++) {
            if (rightAnswer[i].length() != 0) {
                if (rightAnswer[i].equals(answer[i])) {
                    answerOK++;
                } else {
                    answerFail++;
                }

            }

        }


        displayResult();
    }



    public void onClearAnswer(View view) {

        findViewById(R.id.answerGroup).setVisibility(View.VISIBLE);

        EditText et=(EditText) findViewById(R.id.typeAnswer);
        et.setText("");

    }
// Event end
}
