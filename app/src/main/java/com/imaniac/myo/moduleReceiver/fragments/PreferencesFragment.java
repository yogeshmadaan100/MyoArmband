package com.imaniac.myo.moduleReceiver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.imaniac.myo.R;
import com.imaniac.myo.moduleReceiver.adapters.PreferencesExpandableAdapter;
import com.imaniac.myo.moduleReceiver.model.Item;
import com.imaniac.myo.moduleReceiver.model.PreferenceModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PreferencesFragment extends Fragment {
    @Bind(R.id.expanded_list_view)
    ExpandableListView expandableListView;
    PreferencesExpandableAdapter preferencesExpandableAdapter;
    public static PreferencesFragment newInstance() {
        PreferencesFragment fragment = new PreferencesFragment();
        return fragment;
    }

    public PreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_preferences, container, false);
        ButterKnife.bind(this,rootView);
        expandableListView.setAdapter(new PreferencesExpandableAdapter(getActivity(),createData()));
        expandableListView.expandGroup(0);
        return rootView;
    }
    public List<PreferenceModel> createData()
    {
        List<PreferenceModel> preferenceModels = new ArrayList<>();
        PreferenceModel preferenceModel = new PreferenceModel();
        preferenceModel.setTitle("Alerts");
        List<Item> items = new ArrayList<>();
        items.add(new Item(android.R.drawable.ic_menu_call,"Call Alert"));
        items.add(new Item(android.R.drawable.ic_dialog_email,"Message Alert"));
        items.add(new Item(android.R.drawable.ic_dialog_alert, "Other Alert"));
        preferenceModel.setItems(items);
        preferenceModels.add(preferenceModel);
        return preferenceModels;
    }

}
