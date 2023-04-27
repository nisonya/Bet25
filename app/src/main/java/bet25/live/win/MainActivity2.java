package bet25.live.win;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Locale;

import bet25.live.win.databinding.ActivityMain2Binding;


public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;
    RecyclerView musclesList, exersiseList;
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
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = new StopwatchFragment();;
            switch (item.getItemId()) {
                case R.id.stopwatch:
                    fragment = new StopwatchFragment();
                    break;
                case R.id.new_training:
                    fragment = new NewTrainingFragment();
                    break;
                case R.id.history:
                    fragment = new HistoryFragment();
                    break;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentCV, fragment);
            fragmentTransaction.commit();

            return true;
        });*/
    }
    //сохранение ссылки локально
    public void saveToSP(){
        ed = sPref.edit();
        ed.putString(URL_STRING, url_FB);
        ed.apply();
        browse(url_FB);
    }

}