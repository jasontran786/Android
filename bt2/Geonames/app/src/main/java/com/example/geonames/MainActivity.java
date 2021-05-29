package com.example.geonames;

import android.content.Intent;
import android.os.Bundle;

import com.example.geonames.Models.Country;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geonames.Helpers.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

	private String languageCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		languageCode = Locale.getDefault().getLanguage();

		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		loadCountriesList();
	}

	private void loadCountriesList() {
		OkHttpClient client = new OkHttpClient();

		String url = "http://api.geonames.org/countryInfoJSON?lang="+languageCode+"&username="+getResources().getString(R.string.user_name);

		Request request = new Request.Builder()
				.url(url)
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				if(response.isSuccessful()){
					final String result = response.body().string();

					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								JSONObject obj = new JSONObject(result);
								JSONArray arr = obj.getJSONArray("geonames");


								LinearLayout list = findViewById(R.id.countries_list);
								list.removeAllViews();
								for(short i = 0; i < arr.length(); i++){
									Country country = convertToCountry(arr.getJSONObject(i));
									list.addView(createCountryButton(country));
								}
							}
							catch (Exception e){
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
	}

	private Country convertToCountry(JSONObject obj) throws JSONException {
		Country country = new Country();

		country.AreaInSqKm = obj.getDouble("areaInSqKm");
		country.Capital = obj.getString("capital");
		country.Continent = obj.getString("continent");
		country.ContinentName = obj.getString("continentName");
		country.CountryCode = obj.getString("countryCode");
		country.CountryName = obj.getString("countryName");
		country.CurrencyCode = obj.getString("currencyCode");
		country.East = obj.getDouble("east");
		country.FipsCode = obj.getString("fipsCode");
		country.GeonameId = obj.getInt("geonameId");
		country.IsoAlpha3 = obj.getString("isoAlpha3");
		country.IsoNumeric = (short)obj.getInt("isoNumeric");
		country.Languages = obj.getString("languages").split(",");
		country.North = obj.getDouble("north");
		country.Population = obj.getInt("population");
		country.South = obj.getDouble("south");
		country.West = obj.getDouble("west");
		return country;
	}

	private Button createCountryButton(final Country country){
		LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
				LinearLayoutCompat.LayoutParams.FILL_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT
		);

		Button button = new Button(this);
		button.setLayoutParams(params);
		button.setText(country.CountryName);
		button.setTag(country);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Country c = (Country)v.getTag();
				//Toast.makeText(v.getContext(), c.CountryName, Toast.LENGTH_LONG).show();
				Intent i = new Intent("com.example.geonames.CountryDetailActivity");
				i.putExtra("country_name", country.CountryName);
				i.putExtra("country_population", country.Population);
				i.putExtra("country_area", country.AreaInSqKm);
				i.putExtra("country_code", country.CountryCode);
				startActivity(i);
			}
		});
		return button;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
        createMenu(menu);
		return true;
	}

    private void createMenu(Menu menu){
		String[] list = Locale.getISOLanguages();
        for(int i=0;i<list.length;i++){
            MenuItem item = menu.add(0,i,i,LanguageHelper.GetLanguageNativeName(list[i]));{
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            }
        }
    }

    private boolean menuChoice(MenuItem item){
	    //Toast.makeText(this, LanguageHelper.GetLanguageName(Locale.getISOLanguages()[item.getItemId()]), Toast.LENGTH_LONG).show();
	    languageCode = Locale.getISOLanguages()[item.getItemId()];
	    loadCountriesList();
	    return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return menuChoice(item);
	}
}
