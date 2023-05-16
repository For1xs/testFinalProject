package com.example.testfinalproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private CalendarView mainCalendar;

    private DatabaseReference databaseReference;
    private String stringDateSelected;
    private DatabaseReference eventDatabaseReference;

    private Button saveEventButton;
    private EditText editTextNameOfTheCompetition;
    private EditText editTextDistance;
    private EditText editTextСategory;
    private EditText editTextResult;
    private ArrayList<Event> membersOfEvent = new ArrayList<>();
    private TextView textView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> listData;
    private Button goToSecondActivity;
    private ImageButton goToThirdActivity;
    private ImageButton goToFourthActivity;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        goToSecondActivity = findViewById(R.id.goToSecondActivity);
        goToThirdActivity = findViewById(R.id.goToThirdActivity);
        goToFourthActivity = findViewById(R.id.goToFourthActivity);


        mainCalendar = findViewById(R.id.mainCalendar);


        goToSecondActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimeToCategoryActivityPage2.class);
            startActivity(intent);
        });
        goToThirdActivity.setOnClickListener(v ->{
            Intent intent = new Intent(this, YourCategoryPage3.class);
            startActivity(intent);
        });
        goToFourthActivity.setOnClickListener(v ->{
            Intent intent = new Intent(this, CalculateTimePage4.class);
            startActivity(intent);
        });





        initializeMainCalendarSelectedDayChange();
        listData = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.dialog_set_event, listData);
        databaseReference = FirebaseDatabase.getInstance("https://finalprojectworkingvariant1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        eventDatabaseReference = databaseReference.child("Calendar");



    }

    private void initializeMainCalendarSelectedDayChange(){
        mainCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            stringDateSelected = dayOfMonth + ":" + (month + 1) + ":" + year;
            calendarClicked();
        });

    }


    private void calendarClicked(){
        setEventDialog();

        eventDatabaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {            @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() != null) {
                Event event = snapshot.getValue(Event.class);
                editTextNameOfTheCompetition.setText(event.nameOfTheCompetition);
                editTextDistance.setText(event.distance);
                editTextСategory.setText(event.category);
                editTextResult.setText(event.result);

            }
            else {
                editTextNameOfTheCompetition.setText(null);
                editTextDistance.setText(null);
                editTextСategory.setText(null);
                editTextResult.setText(null);

            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }


    private void setEventDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater()
                .inflate(R.layout.dialog_set_event, null);
        editTextNameOfTheCompetition = constraintLayout.findViewById(R.id.editTextNameOfTheCompetition);
        editTextDistance = constraintLayout.findViewById(R.id.editTextDistance);
        editTextСategory = constraintLayout.findViewById(R.id.editTextСategory);
        editTextResult = constraintLayout.findViewById(R.id.editTextResult);
        saveEventButton = constraintLayout.findViewById(R.id.saveEventButton);



        saveEventButton.setOnClickListener( v-> {




            String id = stringDateSelected;
            String nameOfTheCompetition = editTextNameOfTheCompetition.getText().toString();
            String distance = editTextDistance.getText().toString();
            String category = editTextСategory.getText().toString();
            String result = editTextResult.getText().toString();

            Event newEvent = new Event(id, nameOfTheCompetition, distance, category, result);
            eventDatabaseReference.child(stringDateSelected).setValue(newEvent);


        });

        builder.setView(constraintLayout);
        builder.show();



    }


}