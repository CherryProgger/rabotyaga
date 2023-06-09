package com.example.mathgamegui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LeaderBoardActivity extends AppCompatActivity {

    Button btn_back;
    String path = "users.txt";
    ArrayList<Pair<Integer, String>> list = new ArrayList<>();
    TextView top1, top2, top3, top4, top5, top6;

    String nickname = "";
    int position = 0, solved = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        btn_back = findViewById(R.id.btn_back);
        top1 = findViewById(R.id.tv_top1);
        top2 = findViewById(R.id.tv_top2);
        top3 = findViewById(R.id.tv_top3);
        top4 = findViewById(R.id.tv_top4);
        top5 = findViewById(R.id.tv_top5);
        top6 = findViewById(R.id.tv_top6);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        auth();

        top1.setText("1) " + list.get(0).second + " - " + String.valueOf(list.get(0).first));
        top2.setText("2) " + list.get(1).second + " - " + String.valueOf(list.get(1).first));
        top3.setText("3) " + list.get(2).second + " - " + String.valueOf(list.get(2).first));
        top4.setText("4) " + list.get(3).second + " - " + String.valueOf(list.get(3).first));
        top5.setText("5) " + list.get(4).second + " - " + String.valueOf(list.get(4).first));

        Bundle extras = getIntent().getExtras();
        if (extras != null) nickname = extras.getString("key");

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).second.equals(nickname)) {
                position = i + 1;
                solved = list.get(i).first;
                break;
            }
        }

        top6.setText(String.valueOf(position) + ") " + nickname + " - " + String.valueOf(solved));

    }

    private void auth() {
        int count = 0;
        Context context = this.getBaseContext();

        File file = context.getFileStreamPath(path);

        boolean exists = false;
        String res = "";
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String nick = substringBefore(line);
                Integer kol = Integer.parseInt(substringAfter(line));
                list.add(new Pair<>(kol, nick));
                sb.append(line).append("\n");
            }
            res = sb.toString();
            if (fis != null) fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.sort(new Comparator<Pair<Integer, String>>() {
            @Override
            public int compare(Pair<Integer, String> t1, Pair<Integer, String> t2) {
                return t2.first - t1.first;
            }
        });
    }

    private String substringBefore(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_') {
                res = s.substring(0, i);
                break;
            }
        }
        return res;
    }

    private String substringAfter(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_') {
                res = s.substring(i + 1);
                break;
            }
        }
        return res;
    }
}