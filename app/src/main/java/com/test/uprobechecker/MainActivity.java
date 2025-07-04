package com.test.uprobechecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.test.uprobechecker.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'uprobechecker' library on application startup.
    static {
        System.loadLibrary("uprobechecker");
    }

    private ActivityMainBinding binding;
    boolean uprobe_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                uprobe_flag = is_find_upobes();
                // 后台线程执行的任务
                runOnUiThread(() -> {
                    if(uprobe_flag){
                        tv.setText("Find uprobe");
                    }else {
                        tv.setText("Not found uprobe");
                    }

                });
            }
        };

        timer.schedule(task, 1000, 1000);
    }


    public native boolean is_find_upobes();
}