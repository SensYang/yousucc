package com.yousucc.ui.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yousucc.R;
import com.yousucc.database.tables.Contacts;
import com.yousucc.ui.adapter.ContactsAdapter;
import com.yousucc.ui.base.BaseFragment;
import com.yousucc.ui.widget.TopNavigationBar;
import com.yousucc.ui.widget.indexlist.QuickIndexBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
public class ContactsFragment extends BaseFragment {

    @Bind(R.id.topNavigationBar)
    TopNavigationBar topNavigationBar;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.quickIndexBar)
    QuickIndexBar quickIndexBar;
    private View parentView;
    private View topView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.fragment_main_contacts, null, false);
            ButterKnife.bind(this, parentView);
        }
        return parentView;
    }

    @Override
    public void onFirstUserVisible() {
        topView = LayoutInflater.from(getActivity()).inflate(R.layout.top_contacts, null);
        listView.addHeaderView(topView);
        contactsAdapter = new ContactsAdapter(getActivity());
        listView.setAdapter(contactsAdapter);
        quickIndexBar.setListView(listView);
        listView.setOnItemClickListener(contactsAdapter);
        initData();
    }

    private void initData() {
        Contacts contacts00 = new Contacts("杨要钢");
        contacts00.setSessionId("15738334046");
        contactsAdapter.addContacts(contacts00);

        Contacts contacts0 = new Contacts("李宗");
        contacts0.setSessionId("18530928807");
        contactsAdapter.addContacts(contacts0);

        Contacts contacts1 = new Contacts("李宗");
        contacts1.setSessionId("13592365213");
        contactsAdapter.addContacts(contacts1);

        Contacts contacts2 = new Contacts("李丹");
        contacts2.setSessionId("18500191860");
        contactsAdapter.addContacts(contacts2);

        Contacts contacts3 = new Contacts("白百何");
        contacts3.setSessionId("12345678902");
        contactsAdapter.addContacts(contacts3);

        Contacts contacts4 = new Contacts("白衣天使");
        contacts4.setSessionId("12345678903");
        contactsAdapter.addContacts(contacts4);

        Contacts contacts5 = new Contacts("陈超");
        contacts5.setSessionId("12345678904");
        contactsAdapter.addContacts(contacts5);

        Contacts contacts6 = new Contacts("杜小丽");
        contacts6.setSessionId("12345678905");
        contactsAdapter.addContacts(contacts6);

        Contacts contacts7 = new Contacts("宋娟");
        contacts7.setSessionId("12345678906");
        contactsAdapter.addContacts(contacts7);

        Contacts contacts8 = new Contacts("贾忆");
        contacts8.setSessionId("12345678907");
        contactsAdapter.addContacts(contacts8);

        Contacts contacts9 = new Contacts("张大炮");
        contacts9.setSessionId("12345678908");
        contactsAdapter.addContacts(contacts9);

        Contacts contacts10 = new Contacts("张伟");
        contacts10.setSessionId("12345678909");
        contactsAdapter.addContacts(contacts10);

        Contacts contacts11 = new Contacts("吕子乔");
        contacts11.setSessionId("12345678910");
        contactsAdapter.addContacts(contacts11);

        Contacts contacts12 = new Contacts("曾小贤");
        contacts12.setSessionId("12345678911");
        contactsAdapter.addContacts(contacts12);

        Contacts contacts13 = new Contacts("关谷");
        contacts13.setSessionId("12345678912");
        contactsAdapter.addContacts(contacts13);

        Contacts contacts14 = new Contacts("胡一菲");
        contacts14.setSessionId("12345678913");
        contactsAdapter.addContacts(contacts14);

        Contacts contacts15 = new Contacts("罗兰");
        contacts15.setSessionId("12345678914");
        contactsAdapter.addContacts(contacts15);

        Contacts contacts16 = new Contacts("美嘉");
        contacts16.setSessionId("12345678915");
        contactsAdapter.addContacts(contacts16);

        Contacts contacts17 = new Contacts("陆展博");
        contacts17.setSessionId("12345678916");
        contactsAdapter.addContacts(contacts17);

        Contacts contacts18 = new Contacts("356d");
        contacts18.setSessionId("12345678917");
        contactsAdapter.addContacts(contacts18);

        Contacts contacts19 = new Contacts("343a");
        contacts19.setSessionId("12345678918");
        contactsAdapter.addContacts(contacts19);

        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
