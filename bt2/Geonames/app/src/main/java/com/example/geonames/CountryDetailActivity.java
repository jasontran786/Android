package com.example.geonames;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CountryDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_detail);

		String countryName = getIntent().getStringExtra("country_name");
		int countryPopulation = getIntent().getIntExtra("country_population", 0);
		double countryArea = getIntent().getDoubleExtra("country_area", 0);
		String countryCode = getIntent().getStringExtra("country_code");

		TextView countryNameTextView = findViewById(R.id.country_name);
		TextView countryPopulationTextView = findViewById(R.id.country_population);
		TextView countryAreaTextView = findViewById(R.id.country_area);
		ImageView countryFlagImageView = findViewById(R.id.country_flag);

		countryNameTextView.setText(countryName);
		countryPopulationTextView.setText(Integer.toString(countryPopulation));
		countryAreaTextView.setText(Double.toString(countryArea));
		Picasso.get().load("http://www.geonames.org/flags/x/"+countryCode.toLowerCase()+".gif").into(countryFlagImageView);
	}
}
