package com.example.licenta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.licenta.data.MealPlan;
import com.example.licenta.data.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    ImageView leftArrow,rightArrow;
    TextView Day,noDay,Description;
    ListView listTasks;
    List<Task> tasks;
    ArrayList<String> tasksString=new ArrayList<>();
    int index;
    MealPlan plan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        leftArrow=rootView.findViewById(R.id.bt_leftArrow);
        rightArrow=rootView.findViewById(R.id.bt_rightArrow);
        Day=rootView.findViewById(R.id.tv_date);
        Description=rootView.findViewById(R.id.tv_dayDescription);
        listTasks=rootView.findViewById(R.id.lv_Task);
        Bundle extras =getActivity().getIntent().getExtras();
        index= extras.getInt("index");
        plan =  extras.getParcelable("plan");
        if(plan.getCurrentDay()==1)
            Day.setText("Day 1");
        if(plan.getCurrentDay()==2)
            Day.setText("Day 2");
        if(plan.getCurrentDay()==3)
            Day.setText("Day 3");
        if(plan.getCurrentDay()==4)
            Day.setText("Day 4");

        tasks=plan.getTasksPerDay(plan.getCurrentDay());
        TasksFragment.MyAdapter adapter=new TasksFragment.MyAdapter(getActivity(),tasks);
        listTasks.setAdapter(adapter);
        Description.setText(plan.getDescriptionPerDay(plan.getCurrentDay()-1));

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plan.getCurrentDay()>1)
                {
                    plan.setCurrentDay(plan.getCurrentDay() - 1);
                    Intent intent =new Intent(getActivity(),StartedPlanActivity.class);
                    intent.putExtra("plan",plan);
                    intent.putExtra("index",index);

                    startActivity(intent);

                }
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plan.getCurrentDay()!=plan.getNoDays())
                {
                    plan.setCurrentDay(plan.getCurrentDay()+1);
                    FirebaseDatabase.getInstance().getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(index)).setValue(plan);

                    Intent intent =new Intent(getActivity(),StartedPlanActivity.class);
                    intent.putExtra("plan",plan);
                    intent.putExtra("index",index);

                    startActivity(intent);
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public class MyAdapter extends ArrayAdapter<Task> {
        public MyAdapter(Context context, List<Task> string_list) {
            super(context, 0, string_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item4, parent, false);
            }
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView txtview = (TextView) convertView.findViewById(R.id.tv_TaskName);
            txtview.setText(tasks.get(position).getDescription());

            return convertView;
        }
    }

}