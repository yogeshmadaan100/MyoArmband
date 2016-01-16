package com.imaniac.myo.moduleReceiver.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.imaniac.myo.R;
import com.imaniac.myo.moduleReceiver.model.Item;
import com.imaniac.myo.moduleReceiver.model.PreferenceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yogeshmadaan on 08/09/15.
 */
public class PreferencesExpandableAdapter extends BaseExpandableListAdapter {
    private LayoutInflater layoutInflater;
    private Context context = null;
    private List<PreferenceModel> preferenceModels;
    public PreferencesExpandableAdapter(Context context, List<PreferenceModel> preferenceModels)
    {
        this.context = context;
        this.preferenceModels = preferenceModels;
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getGroupCount() {
        return preferenceModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return preferenceModels.get(groupPosition).getItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return preferenceModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return preferenceModels.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view =layoutInflater.inflate(R.layout.layout_preferences_parent_list_item,null,false);
         TextView parentTitle;
        parentTitle = (TextView) view.findViewById(R.id.text_parent_title);
        parentTitle.setText(preferenceModels.get(groupPosition).getTitle());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if(convertView == null)
        {
            convertView =layoutInflater.inflate(R.layout.layout_preferences_child_list_item,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.headText = (TextView) convertView.findViewById(R.id.text_item);
            childViewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_item);
            childViewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_item);
            convertView.setTag(childViewHolder);
        }
        else
        {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        final Item item = preferenceModels.get(groupPosition).getItems().get(childPosition);
        childViewHolder.headText.setText(item.getTitle());
        childViewHolder.imageView.setImageResource(item.getIcon());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    static class ChildViewHolder{
        TextView headText;
        ImageView imageView;
        ToggleButton toggleButton;
    }
}
