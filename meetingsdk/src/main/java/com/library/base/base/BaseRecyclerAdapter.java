package com.library.base.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView.Adapter 基类
 * @author : jerome
 */
public abstract class BaseRecyclerAdapter<D, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {


    protected Context context;
    protected List<D> itemList;
    private OnItemListener itemListener;
    private int itemLayout;

    public  BaseRecyclerAdapter(Context context, List<D> data, int itemLayout) {
        this.context = context;
        this.itemList = data;
        this.itemLayout = itemLayout;
    }

    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    public void addItems(List<D> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void setItems(List<D> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        itemList.clear();
        notifyDataSetChanged();
    }


    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return createHold(LayoutInflater.from(context).inflate(itemLayout, null));
    }

    @Override
    public void onBindViewHolder(final T holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemListener) {
                    itemListener.OnItemClickListener(v, holder.getLayoutPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != itemListener) {
                    itemListener.OnItemLongClickListener(v, holder.getLayoutPosition());
                }
                return true;
            }
        });
        bindData(holder, position, itemList.size()-1>=position?itemList.get(position):null);
    }

    protected abstract void bindData(T holder, int position, D item);

    protected abstract T createHold(View view);

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnItemListener(OnItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public interface OnItemListener {
        void OnItemClickListener(View view, int position);

        void OnItemLongClickListener(View view, int position);
    }

}
