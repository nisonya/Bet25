package bet25.live.win;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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

    public static EditText edMin, edSec;
    public CountDownTimer myTimer;
    public ConstraintLayout cl;
    public int i;
    ProgressBar pb;
    Button btnend;

    private static final String FILE_NAME="MY_FILE_NAME";
    private static final String URL_STRING="URL_STRING";
    public Bundle savedInst;
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
        savedInst = savedInstanceState;
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
                /*Intent intent = new Intent(MainActivity2.this, Timer.class);
                intent.putExtra("key", exercise.getTime_level());
                startActivity(intent);*/

                Dialog dialog = new Dialog(MainActivity2.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.timer_dialog);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button btnend= (Button) dialog.findViewById(R.id.endBTN1);
                pb = dialog.findViewById(R.id.circularProgressIndicator);
                pb.setVisibility(View.VISIBLE);
                edMin = dialog.findViewById(R.id.editTextMinuts);
                edSec = dialog.findViewById(R.id.editTextSeconds);

                int sec =0;
                int min =0;
                for (int j=0;j<exercise.getTime_level();j++){
                    if(sec==0) sec=30;
                    else {
                        sec=0;
                        min=+1;
                    }
                }
                Long seconds = ((long)min*60)+(long)sec;
                System.out.println(seconds+"!!!!!!!");
                int prog = seconds.intValue();
                pb.setVisibility(View.VISIBLE);
                //pb.setProgress(1);
                pb.setMax(prog);
                //setProgressValue(1,prog);
                timerCountDown(seconds);
                edMin.setEnabled(false);
                edSec.setEnabled(false);

                btnend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myTimer.cancel();
                        dialog.dismiss();
                    }
                });

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



    public void goToSett(View view) {
        System.out.println("###############");
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView text_dialog = (TextView) dialog.findViewById(R.id.dialog_remainder);
        Button btn_easy= (Button) dialog.findViewById(R.id.easy);
        btn_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        Button btn_ave= (Button) dialog.findViewById(R.id.average);
        btn_ave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        Button btn_hard= (Button) dialog.findViewById(R.id.hard);
        btn_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        /*FragmentManager manager = getSupportFragmentManager();
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(manager, "myDialog");*/
    }

    public void timerCountDown(Long time){
        myTimer = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long l) {
                int now = (int) l/1000;
                pb.setProgress(now);
                Long min = (l / 60000);
                Long sec = ((l % 60000) / 1000);
                edMin.setText(Long.toString(min));
                edSec.setText(Long.toString(sec));
            }

            @Override
            public void onFinish() {
                finish();
                //открытие диалогового окн

            }
        };
        myTimer.start();
    }
}