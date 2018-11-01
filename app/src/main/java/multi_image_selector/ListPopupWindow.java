package multi_image_selector;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * Created by SensYang on 2016/4/18 0018.
 */
public class ListPopupWindow extends PopupWindow {
    ListView listView;
    View anchorView;

    public ListPopupWindow(Context context) {
        listView = new ListView(context);
        setContentView(listView);
    }

    public AbsListView getListView() {
        return listView;
    }

    public void setAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void setAnchorView(View anchorView) {
        this.anchorView = anchorView;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    public void show() {
        showAsDropDown(anchorView);
    }
}
