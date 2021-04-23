package com.example.v4u.bottom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.v4u.R;
import com.example.v4u.category.babycarecat.babycare_cat;
import com.example.v4u.category.beveragecat.beverages_cat;
import com.example.v4u.category.breakfastcat.breakfast_cat;
import com.example.v4u.category.grocerycat.grocery_staples;
import com.example.v4u.category.householdcat.household_cat;
import com.example.v4u.category.packagedcat.packaged_cat;
import com.example.v4u.category.personalcat.personal_cat;
import com.example.v4u.category.snackscat.snacks_cat;

public class Category_Fragment extends Fragment {

    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Category_Fragment() {
        // Required empty public constructor
    }

    public static Category_Fragment newInstance(String param1, String param2) {
        Category_Fragment fragment = new Category_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview= inflater.inflate(R.layout.fragment_category_,container,false);
        context=rootview.getContext();

        CardView cvGrocery=rootview.findViewById(R.id.cvGrocery);
        CardView cvSnacks=rootview.findViewById(R.id.cvSnacks);
        CardView cvBreakfast=rootview.findViewById(R.id.cvBreakfast);
        CardView cvBeverages=rootview.findViewById(R.id.cvBeverages);
        CardView cvHousehold=rootview.findViewById(R.id.cvHousehold);
        CardView cvPersonal=rootview.findViewById(R.id.cvPersonal);
        CardView cvPackaged=rootview.findViewById(R.id.cvPackaged);
        CardView cvBabycare=rootview.findViewById(R.id.cvBabycare);

        cvGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, grocery_staples.class);
                startActivity(intent);
            }
        });
        cvSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, snacks_cat.class);
                startActivity(intent);
            }
        });
        cvBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, breakfast_cat.class);
                startActivity(intent);
            }
        });
        cvBeverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, beverages_cat.class);
                startActivity(intent);
            }
        });
        cvHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, household_cat.class);
                startActivity(intent);
            }
        });
        cvPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, personal_cat.class);
                startActivity(intent);
            }
        });
        cvPackaged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, packaged_cat.class);
                startActivity(intent);
            }
        });
        cvBabycare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, babycare_cat.class);
                startActivity(intent);
            }
        });
        return rootview;
    }
}