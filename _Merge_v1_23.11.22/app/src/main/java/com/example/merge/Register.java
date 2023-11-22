package com.example.merge;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.database.*;
import com.google.firebase.auth.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class Register extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;                         // firebase 인증
    private DatabaseReference mDatabaseReference;               // realtime database
    private EditText input_name, input_email, input_pwd, input_pwdChk;        // email, pwd 버튼
    private Button register, cancel;                            // 취소 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");

        input_email = findViewById(R.id.id_input);
        input_pwd = findViewById(R.id.pwd_input);
        input_pwdChk = findViewById(R.id.confirm_input);
        input_name = findViewById(R.id.input_name);

        register = findViewById(R.id.register_button);
        cancel = findViewById(R.id.cancel_button);

        register.setOnClickListener(new View.OnClickListener() { //가입하는 과정에서 조건 추가됨
            @Override
            public void onClick(View view) {
                // 회원가입 시작
                String Email = input_email.getText().toString();
                String PWD = input_pwd.getText().toString();        // 6자리 이상
                String PWD_CHK = input_pwdChk.getText().toString();
                String Name = input_name.getText().toString();

                // Firebase 인증 진행
                mFirebaseAuth.createUserWithEmailAndPassword(Email, PWD).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!PWD_CHK.equals(PWD)){ //패스워드와 재입력한 패스워드가 같지 않을 때
                            Toast.makeText(Register.this, "비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()){
                            FirebaseUser FirebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 인증처리된 객체를 가져옴
                            UserAccount account = new UserAccount();
                            account.setIdTokken(FirebaseUser.getUid());
                            account.setEmailID(FirebaseUser.getEmail());                // firebase 에서 인증된 ID
                            account.setPassword(PWD);                                   // firebase 에서는 비밀번호 가져오는게 안됨 -> 사용자가 입력한 비밀번호
                            account.setName(Name);


                            // setValue -> database insert 동작
                            mDatabaseReference.child("UserAccount").child(FirebaseUser.getUid()).setValue(account);

                            Toast.makeText(Register.this, "회원가입 성공 !", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        else{
                            Toast.makeText(Register.this, "회원가입 실패 !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}