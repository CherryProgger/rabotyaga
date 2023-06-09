package com.example.mathgamegui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kotlin.Pair;

public class QuizActivity extends AppCompatActivity {
    String nickname = "";
    String goalStr = "Цель: ", currentStr = "Текущее число: ";
    ArrayList<Pair<Integer, Integer>> goals = new ArrayList<>();
    int goalNum = -1, currentNum = 0;
    int count = 0;
    String path = "users.txt";
    TextView tv_nickname, tv_count;
    Button btn_end, btn_add0, btn_eraseEnd, btn_add1, btn_eraseStart, btn_divine2, btn_multiply2, btn_next;
    ImageButton btn_leaderBoard;
    TextInputEditText et_num;
    TextInputLayout helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        helper = findViewById(R.id.textInputLayout);
        btn_next = findViewById(R.id.btn_next);
        et_num = findViewById(R.id.et_num);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_count = findViewById(R.id.tv_count);
        btn_end = findViewById(R.id.btn_end);
        btn_add0 = findViewById(R.id.btn_add0);
        btn_add1 = findViewById(R.id.btn_add1);
        btn_eraseEnd = findViewById(R.id.btn_eraseEnd);
        btn_eraseStart = findViewById(R.id.btn_eraseStart);
        btn_divine2 = findViewById(R.id.btn_divide2);
        btn_multiply2 = findViewById(R.id.btn_multiply2);
        btn_leaderBoard = findViewById(R.id.btn_leaderboard);

        Bundle extras = getIntent().getExtras();
        if (extras != null) nickname = extras.getString("key");

        loadGoals();
        count = auth();

        tv_nickname.setText(nickname);
        tv_count.setText(String.valueOf(count));


        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, LeaderBoardActivity.class);
                intent.putExtra("key", nickname);
                startActivity(intent);
            }
        });


        btn_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNum = Integer.parseInt(String.valueOf(currentNum) + "1");
                et_num.setText(currentStr + String.valueOf(currentNum));
                update();
            }
        });
        btn_add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNum = Integer.parseInt(String.valueOf(currentNum) + "0");
                et_num.setText(currentStr + String.valueOf(currentNum));
                update();
            }
        });
        btn_eraseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(currentNum).length() == 1) {
                    Toast.makeText(getBaseContext(), "Невозможно", Toast.LENGTH_SHORT).show();
                } else {
                    currentNum = Integer.parseInt(String.valueOf(currentNum).substring(1));
                    et_num.setText(currentStr + String.valueOf(currentNum));
                }
                update();
            }
        });
        btn_eraseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(currentNum).length() == 1) {
                    Toast.makeText(getBaseContext(), "Невозможно", Toast.LENGTH_SHORT).show();
                } else {
                    String val = String.valueOf(currentNum);
                    currentNum = Integer.parseInt(val.substring(0, val.length() - 1));
                    et_num.setText(currentStr + String.valueOf(currentNum));
                }
                update();
            }
        });
        btn_divine2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentNum % 2 != 0) {
                    Toast.makeText(getBaseContext(), "Невозможно", Toast.LENGTH_SHORT).show();
                } else {
                    currentNum /= 2;
                    et_num.setText(currentStr + String.valueOf(currentNum));
                }
                update();

            }
        });
        btn_multiply2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentNum *= 2;
                et_num.setText(currentStr + String.valueOf(currentNum));
                update();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });

    }

    private void loadGoals() {
        getGoals();
        //Toast.makeText(this.getBaseContext(), String.valueOf(goals.get(0).getSecond()), Toast.LENGTH_SHORT).show();
        currentNum = goals.get(count).getSecond();
        goalNum = goals.get(count).getFirst();
        String ss = goalStr + String.valueOf(goals.get(count).getFirst());
        String num = currentStr + String.valueOf(goals.get(count).getSecond());

        et_num.setText(num);
        helper.setHelperText(ss);
    }


    private void updateTask() {
        btn_next.setBackgroundColor(getResources().getColor(R.color.gray));
        currentNum = goals.get(count).getSecond();
        goalNum = goals.get(count).getFirst();
        String ss = goalStr + String.valueOf(goals.get(count).getFirst());
        String num = currentStr + String.valueOf(goals.get(count).getSecond());

        et_num.setText(num);
        helper.setHelperText(ss);

    }
    private void update() {
        if (goalNum == currentNum) {
            //Toast.makeText(this.getBaseContext(), "Совпадение", Toast.LENGTH_SHORT).show();
            btn_next.setBackgroundColor(getResources().getColor(R.color.green));
            int curCount;
            Context context = this.getBaseContext();
            File file = context.getFileStreamPath(path);
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
                    if (nick.equals(nickname)) {
                        curCount = Integer.parseInt(substringAfter(line)) + 1;
                        count++;
                        //Toast.makeText(this.getBaseContext(), "ПОпало", Toast.LENGTH_SHORT).show();
                        sb.append(nickname).append("_").append(String.valueOf(curCount)).append("\n");
                    } else sb.append(line).append("\n");
                }
                res = sb.toString();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(path, Context.MODE_PRIVATE);
                fos.write(res.getBytes());
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tv_count.setText(String.valueOf(count));

        }


    }

    private void getGoals() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(this.getAssets().open("goals.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                //Toast.makeText(this.getBaseContext(), substringBefore(line), Toast.LENGTH_SHORT).show();
                goals.add(new Pair<>(Integer.parseInt(substringBefore(line)), Integer.parseInt(substringAfter(line))));
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int auth() {
        int count = 0;
        Context context = this.getBaseContext();

        File file = context.getFileStreamPath(path);
        if (file.exists()) {
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
                    if (nick.equals(nickname)) {
                        exists = true;
                        count = Integer.parseInt(substringAfter(line));
                        break;
                    }
                    sb.append(line).append("\n");
                }
                res = sb.toString();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!exists) {
                res += nickname + "_0";
                FileOutputStream fos = null;
                try {
                    fos = context.openFileOutput(path, Context.MODE_PRIVATE);
                    fos.write(res.getBytes());
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            String text = nickname + "_0";
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(path, Context.MODE_PRIVATE);
                fos.write(text.getBytes());
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
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