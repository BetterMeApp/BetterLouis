package com.example.louis.myapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends MenuDrawer {
    private static final String TAG = "DetailActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mTaskTitle;
    private TextView mDescription;
    private TextView mDateStarted;
    private TextView mTimeStarted;
    private TextView mGoal;
    private TextView mTaskTally;
    private TextView mMinuteNumber;
    private TextView mCheckedBoxes;
    private ImageView mLogo;
    private ImageView mImagePhoto;
    private ImageView mLogoBackground;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    public int getLayoutId() {
        int id = R.layout.activity_detail;
        return id;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ImageView imageView = (ImageView) findViewById(R.id.logo);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // user is logged in
                } else {
                    finish();
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance();
        String userId = "xZEHwfTM4jbNOJRBikKvQhzpkbh2";
        mDatabaseRef = mDatabase.getReference("users").child(userId);
//        Query queryDatabase = mDatabaseRef.child("users").child("user");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String task = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: Task changed");
                DataSnapshot pushUpSnapshot = dataSnapshot.child("Push-ups");
                Iterable<DataSnapshot> pushUpChildren = pushUpSnapshot.getChildren();


                boolean isCompleted = pushUpSnapshot.child("completed").getValue(Boolean.class);
                String time = pushUpSnapshot.child("time").getValue(String.class);
                String date = pushUpSnapshot.child("date").getValue(String.class);
                String title = pushUpSnapshot.child("title").getValue(String.class);
                String description = pushUpSnapshot.child("description").getValue(String.class);
                Integer goal = Integer.valueOf(pushUpSnapshot.child("goal").getValue().toString());
                String goalString = goal.toString();
                Integer tally = Integer.valueOf(pushUpSnapshot.child("done").getValue().toString());
                String tallyString = tally.toString();
                TextView taskTitle = (TextView)findViewById(R.id.task_title);
                taskTitle.setText(title);

                TextView taskDescription = (TextView)findViewById(R.id.description);
                taskDescription.setText(description);

                TextView taskTime = (TextView)findViewById(R.id.time_started);
                taskTime.setText(time);

                TextView taskDate = (TextView)findViewById(R.id.date_started);
                taskDate.setText(date);

                TextView taskGoal = (TextView)findViewById(R.id.goal);
                taskGoal.setText(goalString);

                TextView taskTally = (TextView)findViewById(R.id.task_tally);
                taskTally.setText(tallyString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error - " + databaseError.getMessage());

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);

    }

    private void configureLayout() {
        mTaskTitle = (TextView) findViewById(R.id.task_title);
        mDescription = (TextView) findViewById(R.id.description);
        mDateStarted = (TextView) findViewById(R.id.date_started);
        mTimeStarted = (TextView) findViewById(R.id.time_started);
        mGoal = (TextView) findViewById(R.id.goal);
        mTaskTally = (TextView) findViewById(R.id.task_tally);
        mMinuteNumber = (TextView) findViewById(R.id.minute_number);
        mCheckedBoxes = (TextView) findViewById(R.id.checked_boxes);
        mLogo = (ImageView) findViewById(R.id.logo);
        mImagePhoto = (ImageView) findViewById(R.id.image_photo);
        mLogoBackground = (ImageView) findViewById(R.id.logo_background);

    }
}
