package com.example.expensetrackerfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapterForRecycleView extends RecyclerView.Adapter<ExpenseAdapterForRecycleView.ExpenseViewHolder> {
    private List<expense_db> expenseList;

    public ExpenseAdapterForRecycleView(List<expense_db> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.defininglayoutofiteminrecycleview, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        expense_db expense = expenseList.get(position);
        holder.tvCategory.setText(expense.getExpense_category());
        holder.tvDate.setText(expense.getExpense_date());
        holder.tvDetail.setText(expense.getExpense_detail());
        holder.tvAmount.setText("₹" + expense.getExpense_value());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDate, tvDetail, tvAmount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvIncomeCategory);
            tvDate = itemView.findViewById(R.id.tvIncomeDate);
            tvDetail = itemView.findViewById(R.id.tvIncomeDetail);
            tvAmount = itemView.findViewById(R.id.tvIncomeAmount);
        }
    }
}

