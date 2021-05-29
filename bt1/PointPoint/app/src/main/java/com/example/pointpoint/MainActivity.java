package com.example.pointpoint;

import androidx.appcompat.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView mTextView;
    public static String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button placeOrder = (Button) findViewById(R.id.button2);
        placeOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String order = "";
        RadioGroup size = (RadioGroup) findViewById(R.id.size);
        RadioGroup tortilla = (RadioGroup) findViewById(R.id.tortilla);
        order = order + "Size: " + ((RadioButton) findViewById(size.getCheckedRadioButtonId())).getText();
        order = order + "\nTortilla: " + ((RadioButton) findViewById(tortilla.getCheckedRadioButtonId())).getText();
//        Toast.makeText(MainActivity.this, order, Toast.LENGTH_SHORT).show();
        order += "\nFillings:";
        if (((CheckBox) findViewById(R.id.f1)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f1)).getText();
        }
        if (((CheckBox) findViewById(R.id.f2)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f2)).getText();
        }
        if (((CheckBox) findViewById(R.id.f3)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f3)).getText();
        }
        if (((CheckBox) findViewById(R.id.f4)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f4)).getText();
        }
        if (((CheckBox) findViewById(R.id.f5)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f5)).getText();
        }
        if (((CheckBox) findViewById(R.id.f6)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f6)).getText();
        }
        if (((CheckBox) findViewById(R.id.f7)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f7)).getText();
        }
        if (((CheckBox) findViewById(R.id.f8)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f8)).getText();
        }
        if (((CheckBox) findViewById(R.id.f9)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f9)).getText();
        }
        if (((CheckBox) findViewById(R.id.f10)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.f10)).getText();
        }
        order += "\nBaverage:";
        if (((CheckBox) findViewById(R.id.b1)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.b1)).getText();
        }
        if (((CheckBox) findViewById(R.id.b2)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.b2)).getText();
        }
        if (((CheckBox) findViewById(R.id.b3)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.b3)).getText();
        }
        if (((CheckBox) findViewById(R.id.b4)).isChecked()) {
            order = order + " " + ((CheckBox) findViewById(R.id.b4)).getText();
        }
        orderDetail(order);
        String message = orderDetail(order).toString();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0928628184", null, "Taco " + message, null, null);

    }

    public String orderDetail(String str) {
        return str;
    }

    public void ShowLocation(View v) {
        Button next = (Button) findViewById(R.id.button4);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), webview.class);
                startActivityForResult(myIntent, 0);
            }

        });
    }
}