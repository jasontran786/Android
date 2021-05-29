package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String baseCurrency, currencyToConvert, currencyExchange="";
    float exchangeRate = 0;
    static String logText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpinnerSetup();
        ButtonSetup();
    }

    public static void updateLog(TextView log,String text){
        logText="\n"+text+logText;
        log.setText(logText);
    }

    public void Convert(String baseCurrency, String currencyToConvert,TextView log, String currencyExchange, float exchangeRate, String nhap)
    {
        currencyExchange = currencyExchange.substring(currencyExchange.indexOf("=") + 1);
        currencyExchange = currencyExchange.substring(0, currencyExchange.indexOf("<"));
        currencyExchange=currencyExchange.trim();
        currencyExchange = currencyExchange.substring(0, currencyExchange.indexOf(" "));
        exchangeRate=Float.parseFloat(currencyExchange);
        float inputNumber = Float.parseFloat(nhap);
        float ket_qua = inputNumber*exchangeRate;
        TextView result = (TextView) findViewById(R.id.result);
        String output=String.format("%.3f", ket_qua)+" "+currencyToConvert.toUpperCase();
        result.setText(output);
        updateLog(log,output);
    }

    public void ButtonSetup()
    {
        Button convert = (Button) findViewById(R.id.bt_convert);
        TextView nhap = (TextView) findViewById(R.id.et_input);
        TextView log = (TextView) findViewById(R.id.log);
        log.setMovementMethod(new ScrollingMovementMethod());
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = nhap.getText().toString();
                if (baseCurrency.equals(currencyToConvert))
                {
                    Toast.makeText(MainActivity.this, "Convert currency can't not be the same as base currency", Toast.LENGTH_SHORT).show();
                }
                else if(!check.equals(null) && !check.equals("")){
                    Convert(baseCurrency,currencyToConvert,log,currencyExchange,exchangeRate,check);
                }
            }
        });
    }

    public void SpinnerSetup()
    {
        Spinner input = findViewById(R.id.spinner_firstConversion);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input.setAdapter(adapter1);
        input.setOnItemSelectedListener(this);
        input.setSelection(161);

        Spinner output = findViewById(R.id.spinner_secondConversion);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        output.setAdapter(adapter2);
        output.setOnItemSelectedListener(this);
        output.setSelection(157);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.spinner_firstConversion: {
                baseCurrency = parent.getItemAtPosition(position).toString();
                new ProcessInBackground().execute();
                break;
            }
            case R.id.spinner_secondConversion: {
                currencyToConvert = parent.getItemAtPosition(position).toString();
                new ProcessInBackground().execute();
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int updateCurrencyExchange(String input)
    {
        currencyExchange=input;
        return 0;
    }

    public InputStream getInputStream(URL url)
    {
        try {
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Void, Void, String> {
        Exception exception = null;
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Busy loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String exchange="";
            try {
                baseCurrency=baseCurrency.toLowerCase();
                currencyToConvert=currencyToConvert.toLowerCase();
                URL url = new URL("https://" + baseCurrency + ".fxexchangerate.com/" + currencyToConvert + ".xml");
                Log.d("URL: ","https://" + baseCurrency + ".fxexchangerate.com/" + currencyToConvert + ".xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(url.openConnection().getInputStream(), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                exchange=xpp.nextText();
                                break;
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType=xpp.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }
            return exchange;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currencyExchange=s;
            progressDialog.dismiss();
        }
    }
}