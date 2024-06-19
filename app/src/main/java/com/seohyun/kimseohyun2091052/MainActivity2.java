package com.seohyun.kimseohyun2091052;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private EditText registerEmail;
    private EditText registerPwd;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Firestore 인스턴스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        registerEmail = findViewById(R.id.register_email);
        registerPwd = findViewById(R.id.register_pwd);
        registerBtn = findViewById(R.id.register_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Firestore 인스턴스 초기화

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registerEmail.getText().toString().trim();
                final String password = registerPwd.getText().toString().trim();

                // 비밀번호 길이 확인
                if (password.length() < 6) {
                    Toast.makeText(MainActivity2.this, "비밀번호는 6자 이상으로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공 시 Firestore에 사용자 정보 저장
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();
                                        String userEmail = user.getEmail();

                                        // 사용자 정보를 Firestore에 저장
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("email", userEmail);

                                        db.collection("users").document(userId)
                                                .set(userMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MainActivity2.this, "Firestore에 사용자 정보 저장 성공", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(MainActivity2.this, "Firestore에 사용자 정보 저장 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // 회원가입 실패 시 오류 메시지 표시
                                    Toast.makeText(MainActivity2.this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
