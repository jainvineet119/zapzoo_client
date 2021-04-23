package com.example.v4u.bottom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.v4u.NoProductAvailable;
import com.example.v4u.R;
import com.example.v4u.ShopsList;
import com.example.v4u.category.beveragecat.beverages_cat;
import com.example.v4u.category.breakfastcat.breakfast_cat;
import com.example.v4u.category.grocerycat.grocery_staples;
import com.example.v4u.category.householdcat.household_cat;
import com.example.v4u.category.packagedcat.packaged_cat;
import com.example.v4u.category.personalcat.personal_cat;
import com.example.v4u.category.snackscat.snacks_cat;

import java.util.ArrayList;
import java.util.List;
public class Home_Fragment extends Fragment {

    Context context;
    private SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Home");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_home_,container,false);
        context=rootview.getContext();

        // first category
        CardView cvGrocery=rootview.findViewById(R.id.hcAtta);
        CardView cvSnacks=rootview.findViewById(R.id.hcBiscuit);
        CardView cvBreakfast=rootview.findViewById(R.id.hcBreakfast);
        CardView cvBeverages=rootview.findViewById(R.id.hcTea);
        CardView cvHousehold=rootview.findViewById(R.id.hcDetergnets);
        CardView cvPersonal=rootview.findViewById(R.id.hcSoaps);
        CardView cvPackaged=rootview.findViewById(R.id.hcNoodles);
        CardView cvSpices=rootview.findViewById(R.id.hcSalt);
        CardView cvHygiene=rootview.findViewById(R.id.hcHygiene);

        //first slidable
        CardView fsBreakfast=rootview.findViewById(R.id.fsBreakfast);
        CardView fsChocolate=rootview.findViewById(R.id.fsChocolate);
        CardView fsHealthy=rootview.findViewById(R.id.fsHealthy);
        CardView fsInstant=rootview.findViewById(R.id.fsInstant);
        CardView fsIntimate=rootview.findViewById(R.id.fsIntimate);
        CardView fsSalt=rootview.findViewById(R.id.fsSalt);

        TextView shop_name = rootview.findViewById(R.id.shop_name);
        shop_name.setText(""+sharedPreferences.getString("shop_name", "Empty Shop"));

        //first banner
        LinearLayout fbHousehold=rootview.findViewById(R.id.bannerLayout);

        // second slidable
        CardView ssBeverages=rootview.findViewById(R.id.ssBeverages);
        CardView ssBiscuit=rootview.findViewById(R.id.ssBiscuit);
        CardView ssNachos=rootview.findViewById(R.id.ssNachos);
        CardView ssNuts=rootview.findViewById(R.id.ssNuts);
        CardView ssPest=rootview.findViewById(R.id.ssPest);
        CardView ssSnacks=rootview.findViewById(R.id.ssSnacks);

        //second banner
        LinearLayout sbAtta=rootview.findViewById(R.id.bannerLayout2);

        // second category
        CardView cv2Ghee=rootview.findViewById(R.id.cv2Ghee);
        CardView cv2Haircare=rootview.findViewById(R.id.cv2Haircare);
        CardView cv2Deodrants=rootview.findViewById(R.id.cv2Deodrants);
        CardView cv2Spices=rootview.findViewById(R.id.cv2Spices);

        //third banner
        LinearLayout tbBeauty=rootview.findViewById(R.id.bannerLayout3);


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
        cvSpices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,NoProductAvailable.class);
                startActivity(intent);
            }
        });
        cvHygiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });


        fsBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, breakfast_cat.class);
                startActivity(intent);
            }
        });
        fsChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });
        fsHealthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });
        fsInstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, packaged_cat.class);
                startActivity(intent);
            }
        });
        fsSalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });
        fsIntimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });



        fbHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, household_cat.class);
                startActivity(intent);
            }
        });



        ssBeverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, beverages_cat.class);
                startActivity(intent);
            }
        });
        ssBiscuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, snacks_cat.class);
                startActivity(intent);
            }
        });
        ssNachos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, snacks_cat.class);
                startActivity(intent);
            }
        });
        ssNuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });
        ssPest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });
        ssSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, snacks_cat.class);
                startActivity(intent);
            }
        });


        sbAtta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, grocery_staples.class);
                startActivity(intent);
            }
        });



        cv2Ghee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, grocery_staples.class);
                startActivity(intent);
            }
        });
        cv2Deodrants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });
        cv2Haircare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, personal_cat.class);
                startActivity(intent);
            }
        });
        cv2Spices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoProductAvailable.class);
                startActivity(intent);
            }
        });



        tbBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, personal_cat.class);
                startActivity(intent);
            }
        });


        return rootview;
    }
}