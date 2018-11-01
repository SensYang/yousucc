package com.yousucc.ui.activity.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.yousucc.R;
import com.yousucc.utils.StringUtil;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class POIAdapter extends AdapterBase<PoiItem> {
    public ImageView tmpView;
    public int flag = 0;
    HashMap<Integer, View> lmap = new HashMap<>();

    public POIAdapter(Context pContext) {
        super(pContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View pConvertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (lmap.get(position) == null) {
            viewHolder = new ViewHolder();
            pConvertView = getLayoutInflater().inflate(R.layout.item_map_info, parent, false);
            viewHolder.locationTitleTV = (TextView) pConvertView.findViewById(R.id.locationTitleTV);
            viewHolder.locationSubTitleTV = (TextView) pConvertView.findViewById(R.id.locationSubTitleTV);
            viewHolder.mapIV = (ImageView) pConvertView.findViewById(R.id.mapIV);
            pConvertView.setTag(viewHolder);
            lmap.put(position, pConvertView);
        } else {
            pConvertView = lmap.get(position);
            viewHolder = (ViewHolder) pConvertView.getTag();
        }

        PoiItem _MkPoiInfo = getItem(position);
        viewHolder.locationTitleTV.setText(_MkPoiInfo.getTitle());

        if (!StringUtil.isEmpty(_MkPoiInfo.getSnippet())) {
            viewHolder.locationSubTitleTV.setText(_MkPoiInfo.getSnippet());
        } else {
            viewHolder.locationSubTitleTV.setText(_MkPoiInfo.getTitle());
        }

        if (position == 0 && flag == 0) {
            flag = 1;
            tmpView = viewHolder.mapIV;
            viewHolder.locationTitleTV.setText("[当前位置]");
            viewHolder.mapIV.setVisibility(View.VISIBLE);
            viewHolder.locationSubTitleTV.setTextColor(Color.parseColor("#000000"));
        } else if (position != 0) {
            viewHolder.locationSubTitleTV.setTextColor(Color.parseColor("#a8a8a8"));
        }
        return pConvertView;
    }

    public class ViewHolder {
        TextView locationTitleTV;
        TextView locationSubTitleTV;
        ImageView mapIV;
    }

}
