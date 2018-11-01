package com.yousucc.ui.activity.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 本类是所有Adapter的父类，可以用于ListView、Spinner和GridView。
 * 用来封装和业务无关的代码。
 */
public abstract class AdapterBase<T> extends BaseAdapter {
    // 上下文对象
    private Context mContext;
    // 存放数据的List
    private List<T> mItemList;
    // 用来根据Item布局文件生成View
    private LayoutInflater mLayoutInflater;

    /**
     * 构造Adapter。
     *
     * @param pContext 上下文对象
     * @param pList    数据源。子类Adapter的数据源若是其自己创建的，这里传入null。然后调用setList()方法设置数据源。
     */
    public AdapterBase(Context pContext, List<T> pList) {
        super();
        setContext(pContext);
        setLayoutInflater(mContext);
        setItemList(pList);
    }

    public AdapterBase(Context pContext) {
        this(pContext, null);
    }

    @Override
    public int getCount() {
        if (null == mItemList) {
            return 0;
        }
        return mItemList.size();
    }

    /**
     * 得到和指定位置的Item对象。
     */
    @Override
    public T getItem(int pPosition) {
        return mItemList.get(pPosition);
    }

    /**
     * 得到和指定位置的Item的ID。
     * 默认返回Item在List中的位置，建议子类覆盖。
     * <p/>
     * AdapterView的getItemIdAtPosition()方法其实就是通过调用本方法实现的，若本方法实现的不对的话，getItemIdAtPosition()将无法得到正确的结果。
     */
    @Override
    public long getItemId(int pPosition) {
        return pPosition;
    }

    /**
     * 声明Item的ID是否是底层稳定的。
     * 默认返回false，建议子类覆盖。
     *
     * @return 若相同的ID一定表示相同的对象，返回true；否则返回false。
     */
    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    /**
     * 返回true表示所有Item都是可选择、可点击的。
     * 若Item不是全部可选的，则子类需要重写本方法。
     */
    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    /**
     * 返回指定位置的Item是否可选择、可点击。
     * 若指定位置的Item不是可选的，则子类需要重写本方法。
     * 若位置是无效的时，结果是不确定的，这种情况下应该抛出一个ArrayIndexOutOfBoundsException。
     */
    @Override
    public boolean isEnabled(int pPosition) {
        return super.isEnabled(pPosition);
    }

    /**
     * 传入位置，返回该位置的Item应该使用的View的类型。
     * 在{@link #getView}中，调用这个方法，根据返回的类型值，创建不同的View。
     * 若子类使用的List中包含多个类的对象，想要根据对象不同生成不同的View，需要重写本方法。
     *
     * @param pPosition The position of the item within the adapter's data set whose view type we
     *                  want.
     * @return 代表View类型的整型值。范围必须在0到{@link #getViewTypeCount} - 1之间。也可以返回{@link #IGNORE_ITEM_VIEW_TYPE}。
     * Two views should share the same type if one can be converted to the other in {@link #getView}.
     * @see #IGNORE_ITEM_VIEW_TYPE
     */
    @Override
    public int getItemViewType(int pPosition) {
        return super.getItemViewType(pPosition);
    }

    /**
     * <p>
     * 返回{@link #getView}会创建几种View类型。每种类型代表一组在{@link #getView}中可以重用的View。
     * 如果Adapter中所有Item使用的View类型相同，本方法应该返回1。（默认就是返回1。）
     * 若子类重写了{@link #getItemViewType}，则也应该重写本方法。
     * </p>
     * <p>
     * 只有当Adapter和{@link AdapterView}绑定时，本方法才会被调用。
     * </p>
     *
     * @return 这个Adapter将创建几种类型的View
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    /**
     * 返回Spinner的下拉弹出框所用的View。
     * 默认返回getView()，用于Spinner时,需要子类重写本方法。
     *
     * @return 返回的View必须实现Checkable接口，
     * 否则用于Spinner时，弹出对话框不会高亮显示当前选中的项
     */
    @Override
    public View getDropDownView(int pPosition, View pConvertView, ViewGroup pParent) {
        return getView(pPosition, pConvertView, pParent);
    }

    /**
     * 设置数据源，此方法应该在生成Adapter时调用。
     *
     * @param pItemList 包含数据的List。
     */
    public void setItemList(List<T> pItemList) {
        if (pItemList != null) {
            mItemList = new ArrayList<>(pItemList);
        } else {
            mItemList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void addItemList(List<T> pItemList) {
        if (pItemList != null) {
            mItemList.addAll(pItemList);
        }
        notifyDataSetChanged();
    }

    public void addItemList(T newItem) {
        if (null != mItemList) {
            mItemList.add(newItem);
        }
        notifyDataSetChanged();
    }

    public void delItemList(int position) {
        if (null != mItemList) {
            mItemList.remove(position);
        }
        notifyDataSetChanged();
    }

    public void modifyItemList(int position, T newItem) {
        if (null != mItemList) {
            if (-1 != position) {
                mItemList.remove(position);
                mItemList.add(position, newItem);
            }
        }
        notifyDataSetChanged();
    }

    public List<T> getItemList() {
        return mItemList;
    }

    public void clearItemList() {
        mItemList.clear();
    }

    protected void setContext(Context pContext) {
        mContext = pContext;
    }

    protected Context getContext() {
        return mContext;
    }

    protected void setLayoutInflater(Context pContext) {
        mLayoutInflater = LayoutInflater.from(pContext);
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

}
