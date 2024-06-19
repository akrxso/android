package com.seohyun.kimseohyun2091052;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button join;
    private Button login;
    private EditText email_login;
    private EditText pwd_login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        join = findViewById(R.id.main_join_btn);
        login = findViewById(R.id.main_login_btn);
        email_login = findViewById(R.id.main_email);
        pwd_login = findViewById(R.id.main_pwd);
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_login.getText().toString().trim();
                final String pwd = pwd_login.getText().toString().trim();

                // 이메일에 @가 포함되어 있는지 확인
                if (!isValidEmail(email)) {
                    Toast.makeText(MainActivity.this, "올바른 이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호가 6자 이상인지 확인
                if (pwd.length() < 6) {
                    Toast.makeText(MainActivity.this, "비밀번호는 6자 이상으로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공 시 StartActivity로 이동
                                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // 로그인 실패 시
                                    String errorMessage = "로그인 오류";
                                    if (task.getException() != null) {
                                        String exceptionMessage = task.getException().getMessage();
                                        if (exceptionMessage.contains("password is invalid")) {
                                            errorMessage = "비밀번호가 올바르지 않습니다";
                                        } else if (exceptionMessage.contains("no user record")) {
                                            errorMessage = "존재하지 않는 계정입니다";
                                        }
                                    }
                                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                                    // 사용자가 존재하지 않으면 회원가입을 유도하는 Toast 메시지 출력
                                    if (task.getException() instanceof com.google.firebase.auth.FirebaseAuthInvalidUserException) {
                                        Toast.makeText(MainActivity.this, "회원가입을 진행해주세요", Toast.LENGTH_SHORT).show();
                                        // 회원가입을 진행하도록 유도하는 코드 추가
                                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

    }

    // 이메일 유효성 검사 메소드
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
