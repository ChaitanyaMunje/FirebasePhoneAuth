package com.example.chaitanya.application;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.FirebaseException;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthInvalidUserException;
        import com.google.firebase.auth.FirebaseAuthUserCollisionException;
        import com.google.firebase.auth.PhoneAuthCredential;
        import com.google.firebase.auth.PhoneAuthProvider;
        import com.steelkiwi.library.IncrementProductView;
        import com.steelkiwi.library.listener.OnStateListener;

        import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText edittxtphone,edittxrCode;
    FirebaseAuth mAuth;
    Button requestotp,login;
    String CodeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        edittxrCode=(EditText)findViewById(R.id.EdittextCode);
        edittxtphone=(EditText)findViewById(R.id.phonenumber);
        requestotp=(Button)findViewById(R.id.request);
        login=(Button)findViewById(R.id.login);
        requestotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySigninCode();
            }
        });

    }

    private void verifySigninCode() {
        String code=edittxrCode.getText().toString();
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(CodeSent,code);
        signinwithPhoneAuthCrediantial(credential);
    }

    private void signinwithPhoneAuthCrediantial(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       Toast.makeText(MainActivity.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
                      Intent intent=new Intent(MainActivity.this,Home.class);
                      startActivity(intent);
                      finish();
                   }
                   else
                   {
                       if (task.getException()instanceof FirebaseAuthInvalidUserException){
                           Toast.makeText(MainActivity.this, "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
                       }
                   }
                    }
                });
    }

    private void sendVerificationCode() {
        String phone=edittxtphone.getText().toString();
        if (phone.isEmpty()){
            edittxtphone.setError("Phone Number is Required");
            edittxtphone.requestFocus();
            return;
        }
        if (phone.length()<10){
            edittxtphone.setError("Please enter valid phone number");
            edittxtphone.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeSent=s;
        }
    };
}
