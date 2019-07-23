package allgoritm.com.youla.fragments.main

import allgoritm.com.youla.admob.demo.R
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import java.lang.Exception

class PreferenceFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_preferences, rootKey)
        preferenceScreen.findPreference<EditTextPreference>(getString(R.string.pref_key_stride))?.setOnPreferenceClickListener {
            val text = (it as? EditTextPreference)?.text
            val number = try {
                Integer.valueOf(text!!)
            } catch (e: Exception) {
                0
            }

            if (number in 2..20) {
                true
            } else {
                Toast.makeText(context, R.string.error_stride_toast, Toast.LENGTH_LONG).show()
                false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
    }

}