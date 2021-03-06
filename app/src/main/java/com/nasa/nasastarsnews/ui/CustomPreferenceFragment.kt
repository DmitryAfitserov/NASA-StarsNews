package com.nasa.nasastarsnews.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.*
import com.nasa.nasastarsnews.MainActivity
import com.nasa.nasastarsnews.R

class CustomPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if(key == MainActivity.key_use_hd){
            MainActivity.isHDImage = sharedPreferences?.getBoolean(key, MainActivity.isHDImageDefault)!!

        } else if (key == MainActivity.key_language){
            MainActivity.language = sharedPreferences?.getString(key, MainActivity.languageDefault)!!
            Toast.makeText(context, R.string.settings_change_language, Toast.LENGTH_SHORT).show()
            manageSwitchTwoText()
        } else if (key == MainActivity.key_two_text){
            MainActivity.twoText = sharedPreferences?.getBoolean(key, MainActivity.twoTextDefault)!!

        }
    }


    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        manageSwitchTwoText()
    }


    private fun manageSwitchTwoText(){
        val languageValue = preferenceManager.sharedPreferences.getString(MainActivity.key_language, MainActivity.languageDefault)
        val switchPreference: CustomSwitchPreferences? = preferenceScreen.get<CustomSwitchPreferences>(MainActivity.key_two_text)

        if (languageValue == MainActivity.languageDefault){
            switchPreference?.isSelectable = false
            switchPreference?.isChecked = false
            Log.d("MyCont", "isSelectable = false")
        } else {
            switchPreference?.isSelectable = true
            Log.d("MyCont", "isSelectable = true")

        }

    }


}