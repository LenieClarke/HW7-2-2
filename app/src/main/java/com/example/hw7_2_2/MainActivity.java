package com.example.hw7_2_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText inputPhoneNumber;
    private EditText inputSMS;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        inputSMS = findViewById(R.id.inputSMS);
        Button callBtn = findViewById(R.id.callBtn);
        Button sendBtn = findViewById(R.id.sendBtn);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callByNumber();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSByNumber();
            }
        });

    }

    private void callByNumber() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Разрешение не получено
            // Делаем запрос на добавление разрешения звонка
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            makeCall();
        }
    }

    private void makeCall() {
        // Разрешение уже получено
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + inputPhoneNumber.getText().toString()));
        // Звоним
        startActivity(dialIntent);
    }

    private void sendSMSByNumber() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sendingSMS();
        }
    }

    private void sendingSMS() {
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage(inputPhoneNumber.getText().toString(),null,
                inputSMS.getText().toString(),null,null);
        Toast.makeText(this, getString(R.string.SMSWasSentToast), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // Проверяем результат запроса на право позвонить
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение получено, осуществляем звонок
                    makeCall();
                } else {
                    // Разрешение не дано. Закрываем приложение
                    finish();
                }
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendingSMS();
                } else {
                    finish();
                }
            }
        }
    }
}