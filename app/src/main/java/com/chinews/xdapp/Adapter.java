package com.chinews.xdapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<String> mTitle;
    private final List<String> mContent;

    Adapter(List<String> data,List<String> content) {
        mTitle = data;
        mContent = content;
    }

    // 建立ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private final TextView title;
        private final TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView36);
            content = itemView.findViewById(R.id.textView);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            // 設置title要顯示的內容
            holder.title.setText(mTitle.get(position));
            holder.content.setText(mContent.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mTitle.size();
    }
}