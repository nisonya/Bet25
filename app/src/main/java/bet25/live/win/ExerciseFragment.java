package bet25.live.win;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExerciseFragment extends Fragment {


    ArrayList<Exercise> exercises;
    SQLiteDatabase database;
    ExerciseAdapter exerciseAdapter;
    RecyclerView exerciseList;
    String[] name_muscle= {"Shoulders","Traps", "Quads", "Hamstrings", "Calves", "Chest",
            "Triceps", "Obliques", "Biceps", "Traps (mid-back)", "Lower back","Lats","ALL"};
    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
/*
        DBHelper dbHelper = new DBHelper(getContext());
        database= dbHelper.getWritableDatabase();
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, name_muscle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        exerciseList = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        exerciseList.setLayoutManager(layoutManager2);
        //add muscles data
        exercises = new ArrayList<>();
        getExForMuscles(1);
        ExerciseAdapter.OnExClickListener onExClickListener = new ExerciseAdapter.OnExClickListener() {
            @Override
            public void onExClickLis( Exercise exercise) {
                System.out.println("CLICKK");
                Fragment fragment = new StopWatchFragment();;
                Bundle bundle = new Bundle();
                bundle.putInt("key", exercise.getTime_level());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentCV, fragment);
                fragmentTransaction.commit();
            }
        };
        exerciseAdapter = new ExerciseAdapter(exercises, onExClickListener);
        exerciseList.setAdapter(exerciseAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getExForMuscles(i+1);
                exerciseList.setAdapter(exerciseAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    /*public void getExForMuscles(int id_muscle){
        exercises.clear();
        ContentValues cv = new ContentValues();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, "muscle_id =="+id_muscle, null, null, null,null);
        if(id_muscle==13){
            cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null,null);
        }
        if(cursor.moveToFirst()){
            int idIndex =cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex =cursor.getColumnIndex(DBHelper.KEY_NAME);
            int id_muscleIndex =cursor.getColumnIndex(DBHelper.KEY_MUSCLE_ID);
            int timeLevelIndex =cursor.getColumnIndex(DBHelper.KEY_TIME_LEVEL);
            int photoIndex =cursor.getColumnIndex(DBHelper.KEY_PHOTO);
            do {
                Exercise mExercise = new Exercise(cursor.getInt(idIndex),
                        cursor.getInt(id_muscleIndex), cursor.getString(nameIndex), cursor.getInt(timeLevelIndex),
                        cursor.getString(photoIndex));
                exercises.add(mExercise);
            }while(cursor.moveToNext());
        }
        else{
            Log.d("mLog","0 rows");
        }

        cursor.close();
    }*/
}