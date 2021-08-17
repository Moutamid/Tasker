package com.moutamid.tasker.Activities;

import static android.view.LayoutInflater.from;
import static com.moutamid.tasker.R.id.dropdown_btn;
import static com.moutamid.tasker.R.id.parent;
import static com.moutamid.tasker.R.id.subtasksRecyclerview;
import static com.moutamid.tasker.R.id.tasksRecyclerView;
import static com.moutamid.tasker.R.id.visible;
import static com.moutamid.tasker.R.layout.layout_item_task;
import static com.moutamid.tasker.R.layout.layout_subtask_item;
import static com.moutamid.tasker.Utilities.Utils.getArrayList;
import static com.moutamid.tasker.Utilities.Utils.store;
import static com.moutamid.tasker.Utilities.Utils.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.moutamid.tasker.Models.TaskModel;
import com.moutamid.tasker.R;
import com.moutamid.tasker.Utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        findViewById(R.id.addTaskBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateTaskActivity.class));
            }
        });

        initRecyclerView();
    }

    /*
    // RecyclerView Library
        implementation 'androidx.recyclerview:recyclerview:1.1.0'*/

    private ArrayList<TaskModel> tasksArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private void initRecyclerView() {

        tasksArrayList.clear();
        tasksArrayList = getArrayList(Constants.STORED_ARRAYLIST, TaskModel.class);

        conversationRecyclerView = findViewById(tasksRecyclerView);
        //conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapterMessages();
        //        LinearLayoutManager layoutManagerUserFriends = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        //    int numberOfColumns = 3;
        //int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 50);
        //  recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        //    if (adapter.getItemCount() != 0) {

        //        noChatsLayout.setVisibility(View.GONE);
        //        chatsRecyclerView.setVisibility(View.VISIBLE);

        //    }

    }

    private class RecyclerViewAdapterMessages extends Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(layout_item_task, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position1) {

            int position = holder.getAdapterPosition();

            holder.title.setText(tasksArrayList.get(position).getName());
            holder.description.setText(tasksArrayList.get(position).getDescription());
            holder.category.setText(tasksArrayList.get(position).getCategory());

            holder.date.setText(tasksArrayList.get(position).getCondition());

            holder.dropDownBtn.setOnClickListener(dropdown_btnClickListener(holder));

            ArrayList<HashMap<String, String>> subtaskListhash = tasksArrayList.get(position).getSubTasks();

            holder.progressBar.setMax(subtaskListhash.size());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.progressBar.setMin(0);
            }

            int count = 0;

            for (HashMap<String, String> stringHashMap : subtaskListhash) {
                if (stringHashMap.get(Constants.VALUE).equals(Constants.TRUE)) {
                    count++;
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.progressBar.setProgress(count, true);
            }

            if (count == subtaskListhash.size()) {
                holder.completedImage.setVisibility(View.VISIBLE);
                holder.pendingImage.setVisibility(View.GONE);
            }

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                    intent.putExtra(Constants.PARAM, position);
                    startActivity(new Intent(MainActivity.this, CreateTaskActivity.class));

//                    Clicklistener(subtaskListhash);
                }
            });

            holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    tasksArrayList.remove(position);
                    adapter.notifyDataSetChanged();

                    store(Constants.STORED_ARRAYLIST, tasksArrayList);
                    return false;
                }
            });

        }

        private View.OnClickListener dropdown_btnClickListener(ViewHolderRightMessage holder) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.dropDownBtn.getRotation() == 90) {
                        holder.dropDownBtn.setRotation(270);
                        holder.description.setVisibility(View.VISIBLE);
                    } else {
                        holder.dropDownBtn.setRotation(90);
                        holder.description.setVisibility(View.GONE);
                    }

                }
            };
        }

        private void Clicklistener(ArrayList<HashMap<String, String>> subTasksList) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_subtask);
            dialog.setCancelable(true);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

            initRecyclerView1(dialog, subTasksList);

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
                    adapter.notifyDataSetChanged();

                    store(Constants.STORED_ARRAYLIST, tasksArrayList);
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(layoutParams);
        }

        @Override
        public int getItemCount() {
            if (tasksArrayList == null)
                return 0;
            return tasksArrayList.size();
        }

        public class ViewHolderRightMessage extends ViewHolder {

            TextView title, description, category, date;
            ProgressBar progressBar;
            CardView parentLayout;
            ImageView dropDownBtn, completedImage, pendingImage;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                title = v.findViewById(R.id.titleTextview);
                date = v.findViewById(R.id.dateTextvieww);
                progressBar = v.findViewById(R.id.progressbar_item_task);
                parentLayout = v.findViewById(R.id.parent_layout_task_item);
                description = v.findViewById(R.id.description_textview);
                category = v.findViewById(R.id.category_textview);
                dropDownBtn = v.findViewById(R.id.dropdown_btn);
                completedImage = v.findViewById(R.id.completedImage);
                pendingImage = v.findViewById(R.id.pendingImage);

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        tasksArrayList.clear();
        tasksArrayList = getArrayList(Constants.STORED_ARRAYLIST, TaskModel.class);
        adapter.notifyDataSetChanged();
    }


    private RecyclerView conversationRecyclerView1;
    private RecyclerViewAdapterMessages1 adapter1;

    private void initRecyclerView1(Dialog dialog, ArrayList<HashMap<String, String>> subTasksList) {

        conversationRecyclerView1 = dialog.findViewById(subtasksRecyclerview);
        //conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter1 = new RecyclerViewAdapterMessages1(subTasksList);
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

        ArrayList<HashMap<String, String>> subTasksList = new ArrayList<>();

        public RecyclerViewAdapterMessages1(ArrayList<HashMap<String, String>> subTasksList) {
            this.subTasksList = subTasksList;
        }

        @NonNull
        @Override
        public RecyclerViewAdapterMessages1.ViewHolderRightMessage1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(layout_subtask_item, parent, false);
            return new RecyclerViewAdapterMessages1.ViewHolderRightMessage1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewAdapterMessages1.ViewHolderRightMessage1 holder, int position1) {

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

                    adapter.notifyDataSetChanged();
                    store(Constants.STORED_ARRAYLIST, tasksArrayList);

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