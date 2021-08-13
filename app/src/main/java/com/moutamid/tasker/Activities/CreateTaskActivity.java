package com.moutamid.tasker.Activities;

import static android.view.LayoutInflater.from;
import static com.moutamid.tasker.R.id.importantRadioBtn_create_task;
import static com.moutamid.tasker.R.id.subtasksRecyclerview;
import static com.moutamid.tasker.R.id.urgentRadioBtn_create_task;
import static com.moutamid.tasker.R.layout.layout_subtask_item;
import static com.moutamid.tasker.Utilities.Utils.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.moutamid.tasker.Models.TaskModel;
import com.moutamid.tasker.R;
import com.moutamid.tasker.Utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTaskActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> subTasksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        findViewById(R.id.done_btn_create_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText name = findViewById(R.id.task_name_create_task);
                EditText description = findViewById(R.id.task_description_create_task);
                EditText condition = findViewById(R.id.task_condition_create_task);

                RadioButton impRadioBtn = findViewById(importantRadioBtn_create_task);
                RadioButton urgentRadioBtn = findViewById(urgentRadioBtn_create_task);

                String nameStr = name.getText().toString();
                String descriptionStr = description.getText().toString();
                String conditionStr = condition.getText().toString();

                String category;
                if (impRadioBtn.isChecked()) {
                    category = impRadioBtn.getText().toString();
                } else {
                    category = urgentRadioBtn.getText().toString();
                }

                if (!isEmpty(nameStr, descriptionStr, conditionStr, category)) {
                    return;
                }

                TaskModel taskModel = new TaskModel();

                taskModel.setName(nameStr);
                taskModel.setCategory(category);
                taskModel.setCondition(conditionStr);
                taskModel.setDescription(descriptionStr);
                taskModel.setSubTasks(subTasksList);

                tasksArrayList.clear();
                tasksArrayList = getArrayList(Constants.STORED_ARRAYLIST, TaskModel.class);
                tasksArrayList.add(taskModel);
                store(Constants.STORED_ARRAYLIST, tasksArrayList);

                finish();
            }
        });

        findViewById(R.id.subtaskBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CreateTaskActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_subtask);
                dialog.setCancelable(true);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

                initRecyclerView1(dialog);

                dialog.findViewById(R.id.done_btn_create_sub_task).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // CODE HERE
                        dialog.dismiss();
                    }
                });

                EditText subtaskEt = dialog.findViewById(R.id.subtask_edittext);

                dialog.findViewById(R.id.add_btn_create_sub_task).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // CODE HERE
                        //TODO: add subtasks

                        if (subtaskEt.getText().toString().isEmpty()) {
                            toast("Please enter a name!");
                            return;
                        }

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(Constants.NAME, subtaskEt.getText().toString());
                        hashMap.put(Constants.VALUE, Constants.FALSE);

                        subTasksList.add(hashMap);

                        subtaskEt.setText("");

                        adapter1.notifyDataSetChanged();

                    }
                });
                dialog.show();
                dialog.getWindow().setAttributes(layoutParams);
            }
        });

    }

    private boolean isEmpty(String nameStr, String descriptionStr, String conditionStr, String category) {
        if (nameStr.isEmpty()) {
            toast("Please enter a name!");
            return false;
        }

        if (descriptionStr.isEmpty()) {
            toast("Please enter a description!");
            return false;
        }

        if (conditionStr.isEmpty()) {
            toast("Please enter a condition!");
            return false;
        }

        if (category.isEmpty()) {
            toast("Please select a category!");
            return false;
        }

        if (subTasksList.isEmpty()) {
            toast("Please add some sub tasks!");
            return false;
        }
        return true;
    }

    private ArrayList<TaskModel> tasksArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView1;
    private RecyclerViewAdapterMessages1 adapter1;

    private void initRecyclerView1(Dialog dialog) {

        conversationRecyclerView1 = dialog.findViewById(subtasksRecyclerview);
        //conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter1 = new RecyclerViewAdapterMessages1();
        //        LinearLayoutManager layoutManagerUserFriends = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        //    int numberOfColumns = 3;
        //int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 50);
        //  recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView1.setLayoutManager(linearLayoutManager);
        conversationRecyclerView1.setHasFixedSize(true);
        conversationRecyclerView1.setNestedScrollingEnabled(false);

        conversationRecyclerView1.setAdapter(adapter1);

        //    if (adapter.getItemCount() != 0) {

        //        noChatsLayout.setVisibility(View.GONE);
        //        chatsRecyclerView.setVisibility(View.VISIBLE);

        //    }

    }

    private class RecyclerViewAdapterMessages1 extends Adapter
            <RecyclerViewAdapterMessages1.ViewHolderRightMessage1> {

        @NonNull
        @Override
        public ViewHolderRightMessage1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(layout_subtask_item, parent, false);
            return new ViewHolderRightMessage1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage1 holder, int position1) {

            int position = holder.getAdapterPosition();

            HashMap<String, String> hashMap = subTasksList.get(position);

            holder.checkBox.setText(hashMap.get(Constants.NAME));

            holder.checkBox.setChecked(hashMap.get(Constants.VALUE).equals(Constants.TRUE));

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        subTasksList.get(position)
                                .put(Constants.VALUE, Constants.TRUE);
                    } else {
                        subTasksList.get(position)
                                .put(Constants.VALUE, Constants.FALSE);

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if (subTasksList == null)
                return 0;
            return subTasksList.size();
        }

        public class ViewHolderRightMessage1 extends ViewHolder {

            CheckBox checkBox;

            public ViewHolderRightMessage1(@NonNull View v) {
                super(v);
                checkBox = v.findViewById(R.id.isEnabledCheckbox);

            }
        }

    }

}