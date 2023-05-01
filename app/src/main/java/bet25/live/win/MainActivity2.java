package bet25.live.win;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.Locale;

import bet25.live.win.databinding.ActivityMain2Binding;


public class MainActivity2 extends AppCompatActivity {


    ArrayList<Exercise> exercises;
    ExerciseAdapter exerciseAdapter;
    RecyclerView exerciseList;
    String[] name_muscle= {"Shoulders","Traps", "Quads", "Hamstrings", "Calves", "Chest",
            "Triceps", "Obliques", "Biceps", "Traps (mid-back)", "Lower back","Lats","ALL"};



    private static final String FILE_NAME="MY_FILE_NAME";
    private static final String URL_STRING="URL_STRING";
    String url_FB;
    String url_SP;
    SQLiteDatabase database;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    private FirebaseRemoteConfig mfirebaseRemoteConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //проверка сохранена ли ссылка
        url_SP = getSharedPrefStr();
        if(url_SP=="") {
            //подключение к FireBase
            getFireBaseUrlConnection();
            getURLStr();
        }else{
            //проверка на подключение к интернету
            if(!hasConnection(this)){
                Intent intent = new Intent(MainActivity2.this, NoInternet.class);
                startActivity(intent);
            }
            else{//запускаем WebView
                browse(url_SP);
            }
        }
    }

    //включение WebView
    public void browse(String url){
        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    //проверка эмулятора
    private boolean checkIsEmu() {

        String phoneModel = Build.MODEL;
        String buildProduct = Build.PRODUCT;
        String buildHardware = Build.HARDWARE;
        String brand = Build.BRAND;
        return (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.toLowerCase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware.equals("goldfish")
                || brand.contains("google")
                || buildHardware.equals("vbox86")
                || buildProduct.equals("sdk")
                || buildProduct.equals("google_sdk")
                || buildProduct.equals("sdk_x86")
                || buildProduct.equals("vbox86p")
                || Build.BOARD.toLowerCase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.toLowerCase(Locale.getDefault()).contains("nox")
                || buildHardware.toLowerCase(Locale.getDefault()).contains("nox")
                || buildProduct.toLowerCase(Locale.getDefault()).contains("nox"))
                || (brand.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                ||"google_sdk".equals(Build.PRODUCT)
                || "sdk_gphone_x86_arm".equals(Build.PRODUCT)
                ||"sdk_google_phone_x86".equals(Build.PRODUCT);
    }

    //проверка интернет подключения
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    //получение ссылки и обработка вызова заглушки/WebView
    public void getURLStr(){
        try {
            mfirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                Log.i("Fire", String.valueOf(task.getResult()));
                                url_FB = mfirebaseRemoteConfig.getString("url");
                                if (url_FB.isEmpty() || checkIsEmu() ) {
                                    plug();
                                } else {
                                    Log.i("Fire", url_FB);
                                    saveToSP();
                                    browse(url_FB);
                                }

                            } else {
                                url_FB = "";
                                plug();
                                Log.i("Fire", "null2");
                            }
                        }
                    });
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            Intent intent = new Intent(MainActivity2.this, NoInternet.class);
            startActivity(intent);
        }
    }
    //получение локальной ссылки
    public String getSharedPrefStr(){
        sPref = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        String url_SP = sPref.getString(URL_STRING,"");
        return url_SP;
    }
    //подключение к Firebase
    public void getFireBaseUrlConnection(){
        //подключение к FireBase
        mfirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mfirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mfirebaseRemoteConfig.setDefaultsAsync(R.xml.url_values);
    }
    //вызыв зваглушки
    public void plug(){
        DBHelper dbHelper = new DBHelper(this);
        database= dbHelper.getWritableDatabase();
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, name_muscle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        exerciseList = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        exerciseList.setLayoutManager(layoutManager2);
        //add muscles data
        exercises = new ArrayList<>();
        getExForMuscles(1);
        ExerciseAdapter.OnExClickListener onExClickListener = new ExerciseAdapter.OnExClickListener() {
            @Override
            public void onExClickLis( Exercise exercise) {
                System.out.println("CLICKK");
                Intent intent = new Intent(MainActivity2.this, Timer.class);
                intent.putExtra("key", exercise.getTime_level());
                startActivity(intent);

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
        });
    }
    //сохранение ссылки локально
    public void saveToSP(){
        ed = sPref.edit();
        ed.putString(URL_STRING, url_FB);
        ed.apply();
        browse(url_FB);
    }

    public void getExForMuscles(int id_muscle){
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
    }


}