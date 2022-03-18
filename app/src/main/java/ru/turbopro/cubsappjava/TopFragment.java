package ru.turbopro.cubsappjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import ru.turbopro.cubsappjava.databinding.FragmentTopBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentTopBinding binding;

    public TopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopFragment newInstance(String param1, String param2) {
        TopFragment fragment = new TopFragment();
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

        //getActivity().setContentView(binding.getRoot());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTopBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();

        int[] imageIds = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
                R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i};
        String[] names = {"Christopher","Craig","Sergio","Mubariz","Mike","Michael","Toa","Ivana","Alex"};
        String[] points = {"8","9","7","6","5", "5","7","2","7"};
        String[] statuses = {"Beginner","King","Master","Pre-master","Advanced","Beginner","Pre-king","Good","So so"};

        ArrayList<ArrayList<String>> achivs = new ArrayList<>();
        ArrayList<String> achiv1 = new ArrayList<String>();
        achiv1.add("1");
        achiv1.add("2");
        achiv1.add("3");
        achivs.add(achiv1);
        ArrayList<String> achiv2 = new ArrayList<String>();
        achiv2.add("1");
        achiv2.add("2");
        achivs.add(achiv2);
        ArrayList<String> achiv3 = new ArrayList<String>();
        achiv3.add("1");
        achiv3.add("2");
        achiv3.add("3");
        achivs.add(achiv3);
        ArrayList<String> achiv4 = new ArrayList<String>();
        achiv4.add("1");
        achiv4.add("2");
        achiv4.add("3");
        achivs.add(achiv4);
        ArrayList<String> achiv5 = new ArrayList<String>();
        achiv5.add("1");
        achiv5.add("2");
        achivs.add(achiv5);
        ArrayList<String> achiv6 = new ArrayList<String>();
        achiv6.add("1");
        achiv6.add("2");
        achiv6.add("3");
        achivs.add(achiv6);
        ArrayList<String> achiv7 = new ArrayList<String>();
        achiv7.add("1");
        achiv7.add("2");
        achiv7.add("3");
        achivs.add(achiv7);
        ArrayList<String> achiv8 = new ArrayList<String>();
        achiv8.add("1");
        achiv8.add("2");
        achivs.add(achiv8);
        ArrayList<String> achiv9 = new ArrayList<String>();
        achiv9.add("1");
        achiv9.add("2");
        achiv9.add("3");
        achivs.add(achiv9);
        ArrayList<UserTop> userArrayList = new ArrayList<>();

        for(int i = 0; i < imageIds.length; i++){
            UserTop user = new UserTop(names[i], statuses[i], points[i], achivs.get(i), imageIds[i]);
            userArrayList.add(user);
        }

        ListTopAdapter listAdapter = new ListTopAdapter(getActivity(), userArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getActivity(), UserTopActivity.class);
            i.putExtra("name", names[position]);
            i.putExtra("status", statuses[position]);
            i.putExtra("points", points[position]);
            i.putExtra("achievs", achivs.get(position));
            i.putExtra("imageid", imageIds[position]);
            startActivity(i);
        });
        return viewBinding;
    }
}