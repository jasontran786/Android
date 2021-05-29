package com.example.temperatureconverter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static String log="";
    private String[] options= new String[]{"Celsius", "Fahrenheit", "Kelvin"};
    HashMap<String,String> fomulas=fomula();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinnerLeft=(Spinner) findViewById(R.id.DropdownLeft);
        Spinner spinnerRight=(Spinner) findViewById(R.id.DropdownRight);
        EditText input=(EditText) findViewById(R.id.input) ;
        EditText output=(EditText) findViewById(R.id.output) ;
        TextView fomula = (TextView) findViewById(R.id.fomula);
        TextView logTextView = (TextView) findViewById(R.id.log);
        AlertDialog alert= new AlertDialog.Builder(this).create();
        Button save =(Button) findViewById(R.id.save);
        Button delete =(Button) findViewById(R.id.detele);
        addElement(spinnerLeft, options);
        addElement(spinnerRight, options);
        logTextView.setMovementMethod(new ScrollingMovementMethod());
        spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String from=spinnerLeft.getSelectedItem().toString();
                String to=spinnerRight.getSelectedItem().toString();
                display(input,output,from,to,fomula,logTextView,alert);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
              
            }

        });
        spinnerRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String from=spinnerLeft.getSelectedItem().toString();
                String to=spinnerRight.getSelectedItem().toString();
                display(input,output,from,to,fomula,logTextView,alert);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String from=spinnerLeft.getSelectedItem().toString();
                String to=spinnerRight.getSelectedItem().toString();
                String value=input.getText().toString();
                String result=convert(value,from,to);
                output.setText(result);
                String str=from.charAt(0)+" to "+to.charAt(0)+" : "+value+" -> "+result;
                if(!value.isEmpty()){
                    saveToLog(str);
                }else {
                    showAlert(alert,"Nothing to save");
                }
                showLog(logTextView);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLog();
                showLog(logTextView);
            }
        });


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String from=spinnerLeft.getSelectedItem().toString();
                String to=spinnerRight.getSelectedItem().toString();
                display(input,output,from,to,fomula,logTextView,alert);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void addElement(Spinner spin, String[] arr){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    public void display(EditText input, EditText output, String from, String to, TextView fomula,TextView log, AlertDialog alert){
        fomula.setText(this.fomulas.get(from+to));
        try {
            String value=input.getText().toString();
            String result=convert(value,from,to);
            output.setText(result);
        }catch (NumberFormatException e){
            showAlert(alert,e.getMessage());
        }catch (Error e){
            showAlert(alert,e.getMessage());
        }
    }

    public void showAlert(AlertDialog alert,String message){
        alert.setMessage(message);
        alert.show();
    }

    public static void saveToLog(String str){
        log=str+"\n"+log;
    }

    public static void deleteLog(){
        log="";
    }

    public static void showLog(TextView logTextView){
        logTextView.setText(log);
    }

    public static String convert(String input,String from, String to){
        if(input.isEmpty()){
            return "";
        }
        double value;
        try {
            value = Double.parseDouble(input);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Decimal number");
        }catch (Error e){
            throw new NumberFormatException("Something went wrong");
        }
        switch (from){
            case "Celsius":
                if(to.equalsIgnoreCase("Fahrenheit")){
                    value=(9.0/5) * value + 32;
                }else if(to.equalsIgnoreCase("Kelvin")){
                    value+=273;
                }
                break;
            case "Fahrenheit":
                if(to.equalsIgnoreCase("Celsius")){
                    value=(5.0/9)*(value - 32);
                }else if(to.equalsIgnoreCase("Kelvin")){
                    value=(value - 32)*(5.0/9) + 273;
                }
                break;
            case "Kelvin":
                if(to.equalsIgnoreCase("Celsius")){
                    value=value - 273;
                }else if(to.equalsIgnoreCase("Fahrenheit")){
                    value=(value - 273)*(9.0/5) + 32;
                }
                break;
            default:
                break;
        }
        return String.valueOf(value);
    }

    private HashMap<String,String> fomula(){
        HashMap<String,String> fomula=new HashMap<String,String>();
        fomula.put("CelsiusFahrenheit","°F = 9/5*(°C) + 32");
        fomula.put("KelvinFahrenheit","°F = 9/5*(K - 273) + 32");
        fomula.put("FahrenheitCelsius","°C = 5/9*(°F - 32)");
        fomula.put("CelsiusKelvin","K = °C + 273");
        fomula.put("KelvinCelsius","°C = K - 273");
        fomula.put("FahrenheitKelvin","K = 5/9*(°F - 32) + 273");
        return fomula;
    }
}