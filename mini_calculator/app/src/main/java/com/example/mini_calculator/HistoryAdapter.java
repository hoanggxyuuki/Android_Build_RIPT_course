package com.example.mini_calculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<CalculationHistory> historyList;

    public HistoryAdapter() {
        this.historyList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CalculationHistory history = historyList.get(position);
        holder.tvExpression.setText(history.getExpression());
        holder.tvResult.setText("= " + history.getResult());
        holder.tvTime.setText(history.getTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void addHistory(CalculationHistory history) {
        historyList.add(0, history);
        notifyItemInserted(0);
    }

    public void clearHistory() {
        historyList.clear();
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvExpression, tvResult, tvTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpression = itemView.findViewById(R.id.tvHistoryExpression);
            tvResult = itemView.findViewById(R.id.tvHistoryResult);
            tvTime = itemView.findViewById(R.id.tvHistoryTime);
        }
    }
}

