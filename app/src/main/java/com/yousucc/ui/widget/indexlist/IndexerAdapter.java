package com.yousucc.ui.widget.indexlist;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.yousucc.database.tables.base.ComparableBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2016/4/2.
 */
public abstract class IndexerAdapter extends BaseAdapter implements SectionIndexer {
    private SparseIntArray positionToSection = new SparseIntArray();
    private SparseIntArray sectionToPosition = new SparseIntArray();
    private List<Character> indexList = new ArrayList<>();
    private Object[] sections;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        char header = getItem(position).getHeader();
        String headerString = null;
        if (header != -1 && (position == 0 || header != -1 && header != getItem(position - 1).getHeader())) {
            headerString = header + "";
        }
        return getView(position, convertView, parent, headerString);
    }

    public abstract View getView(int position, View convertView, ViewGroup parent, String headerString);

    private void initSections() {
        positionToSection.clear();
        sectionToPosition.clear();
        indexList.clear();

        indexList.add('↑');
        positionToSection.put(0, 0);
        sectionToPosition.put(0, 0);

        int count = getCount();
        for (int i = 0; i < count; i++) {
            char header = getItem(i).getHeader();
            int section = indexList.size() - 1;
            if (section > -1 && indexList.get(section) != header) {
                indexList.add(header);
                sectionToPosition.put(section + 1, i+1);
            }
            positionToSection.put(i+1, section);
        }
        sections = indexList.toArray();
    }

    @Override
    public abstract ComparableBean getItem(int position);

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initSections();
    }

    @Override
    public Object[] getSections() {
        if (sections == null) initSections();
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionToPosition.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return positionToSection.get(position);
    }

}
