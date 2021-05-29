package com.example.geonames.Helpers;

import java.util.Locale;

public class LanguageHelper {
	public static String GetLanguageName(String languageCode, String presentLanguageCode){
		Locale loc = new Locale(languageCode);
		String name = loc.getDisplayLanguage(new Locale(presentLanguageCode));
		return name;
	}

	public static String GetLanguageName(String languageCode){
		Locale loc = new Locale(languageCode);
		String name = loc.getDisplayLanguage(Locale.getDefault());
		return name;
	}

	public static String GetLanguageNativeName(String languageCode){
		return GetLanguageName(languageCode,languageCode);
	}
}
