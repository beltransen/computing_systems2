package com.uc3m.beltransen.checklist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // Parameters names
    public static final String LIST_ID_TAG= "LIST_ID";

    // Parameters
    private CheckList checkList;
    private ListView tasksListView;
    private EditText newTaskEditText;
    private Button newTaskButton;
    private ArrayAdapter<String> tasksAdapter;

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param checkList Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    static ListFragment newInstance(CheckList checkList) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelable("list", checkList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            checkList = (CheckList) getArguments().getParcelable("list");
        }else{
            checkList = new CheckList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        tasksListView = (ListView) view.findViewById(R.id.tasks_list);
        newTaskEditText = (EditText) view.findViewById(R.id.edittext_addtask);
        newTaskButton = (Button) view.findViewById(R.id.button_addtask);
        checkList.clear();

        // Read file and fill adapter with tasks
        try {
            Scanner scanner = new Scanner(getContext().openFileInput(checkList.getName().replace(' ','_')+".txt"));
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.isEmpty()){
                    break;
                }
                String [] fields = line.split(",");
                Task currentTask = new Task(fields[0], fields[1].compareTo("DONE")==0);
                checkList.addTask(currentTask);
            }
        }catch (IOException e) {
            Log.e("ListFragment", e.getMessage());
        }

        ArrayList<String> tasksArray = new ArrayList<>();
        for (int i =0; i< checkList.size(); ++i){
            tasksArray.add(checkList.get(i).getName() + " ("+ (checkList.get(i).isDone() ? "DONE" : "PENDING") +")");
        }
        tasksAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, tasksArray);
        tasksListView.setAdapter(tasksAdapter);

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("ListFragment","Changed status of task '"+checkList.get(position).getName()+
                        "' in list '"+getChecklistName().toString()+"'");
                checkList.get(position).setDone(!checkList.get(position).isDone());
                updateChecklist();
            }
        });

        tasksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.d("ListFragment","Removed task '"+checkList.get(position).getName()+
                        "' from list '"+getChecklistName().toString()+"'");
                checkList.removeTask(position);
                updateChecklist();
                Toast.makeText(getContext(), "Task removed successfully", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = newTaskEditText.getText().toString();
                if(s.isEmpty()){
                    Toast.makeText(getContext(),"Task name is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                Task t = new Task(s, false);
                checkList.addTask(t);
                newTaskEditText.setText("");
                updateChecklist();
                Log.d("ListFragment","Added task '"+s+"' to list '"+getChecklistName().toString()+"'");
            }
        });
        return view;
    }

    private void updateChecklist(){
        ArrayList<String> tasksArray = new ArrayList<>();
        for (int i =0; i< checkList.size(); ++i){
            tasksArray.add(checkList.get(i).getName() + " ("+ (checkList.get(i).isDone() ? "DONE" : "PENDING") +")");
        }

        File file=new File(getActivity().getFilesDir()+"/"+checkList.getName().replace(' ','_')+".txt");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(file, false));
            String output = "";
            for (Task task : checkList.getTasks()){
                output += task.getName()+","+ (task.isDone() ? "DONE" : "PENDING") +"\n";
            }
            writer.write(output);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Error creating new task", Toast.LENGTH_SHORT).show();
        }

        tasksAdapter.clear();
        tasksAdapter.addAll(tasksArray);
        tasksAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public CharSequence getChecklistName() {
        return checkList.getName();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}