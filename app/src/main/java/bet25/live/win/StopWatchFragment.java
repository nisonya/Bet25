package bet25.live.win;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StopWatchFragment extends Fragment {

    public static EditText edMin, edSec;
    public CountDownTimer myTimer;
    public int i = 0;
    ProgressBar pb;
    Button btnend;

    public StopWatchFragment() {
        super(R.layout.fragment_stop_watch);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            i = bundle.getInt("key", 1);
        }
        System.out.println(i+" !!!!!!!!!!!!!!!!");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = view.findViewById(R.id.circularProgressIndicator);
        pb.setVisibility(View.VISIBLE);
        btnend= view.findViewById(R.id.endBTN);
        edMin = view.findViewById(R.id.editTextMinuts);
        edSec = view.findViewById(R.id.editTextSeconds);
        int sec =0;
        int min =0;
        for (int j=0;i<i;j++){
            if(sec==0) sec=30;
            else {
                sec=0;
                min=+1;
            }
        }
        Long seconds = ((long)min*60)+(long)sec;
        int prog = seconds.intValue();
        pb.setVisibility(View.VISIBLE);
        //pb.setProgress(1);
        pb.setMax(prog);
        timerCountDown(seconds);
        edMin.setEnabled(false);
        edSec.setEnabled(false);

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTimer.cancel();
                pb.setMax(0);
                getActivity().getSupportFragmentManager().beginTransaction().remove(StopWatchFragment.this).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stop_watch, container, false);
    }
    //таймер
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
                edMin.setText("0");
                edSec.setText("00");
                edMin.setEnabled(true);
                edSec.setEnabled(true);
                //открытие диалогового окна

                getActivity().getSupportFragmentManager().beginTransaction().remove(StopWatchFragment.this).commit();

            }
        };
        myTimer.start();
    }

}