/*
 * Copyright (c) 2013 Menny Even-Danan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anysoftkeyboard.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.preference.PreferenceFragment;
import android.view.View;

import com.anysoftkeyboard.quicktextkeys.ui.QuickKeysOrderedListFragment;
import com.sevencupsoftea.ears.R;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.experiences.TransitionExperiences;

//import com.menny.android.anysoftkeyboard.R;

public class QuickTextSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.prefs_quick_text_addons_prefs);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findPreference(getString(R.string.settings_key_active_quick_text_key)).setOnPreferenceClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainSettingsActivity.setActivityTitle(this, getString(R.string.quick_text_keys_group));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.settings_key_active_quick_text_key))) {
            Activity activity = getActivity();
            if (activity != null && activity instanceof FragmentChauffeurActivity) {
                ((FragmentChauffeurActivity)activity).addFragmentToUi(new QuickKeysOrderedListFragment(), TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
                return true;
            }
        }
        return false;
    }
}
