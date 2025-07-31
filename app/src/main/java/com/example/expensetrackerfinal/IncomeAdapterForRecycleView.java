// This file is create to bind the income list data in an adapter to further display it in the recycle view

package com.example.expensetrackerfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncomeAdapterForRecycleView extends RecyclerView.Adapter<IncomeAdapterForRecycleView.IncomeViewHolder> {
    private List<income_db> incomeList;

    public IncomeAdapterForRecycleView(List<income_db> incomeList) {
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.defininglayoutofiteminrecycleview, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        income_db income = incomeList.get(position);
        holder.tvCategory.setText(income.getIncome_category());
        holder.tvDate.setText(income.getIncome_date());
        holder.tvDetail.setText(income.getIncome_detail());
        holder.tvAmount.setText("₹" + income.getIncome_value());
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDate, tvDetail, tvAmount;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvIncomeCategory);
            tvDate = itemView.findViewById(R.id.tvIncomeDate);
            tvDetail = itemView.findViewById(R.id.tvIncomeDetail);
            tvAmount = itemView.findViewById(R.id.tvIncomeAmount);
        }
    }
}

