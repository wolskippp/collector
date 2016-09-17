package com.wolski_msk.collector.sample.fragments.AddingScreen;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wolski_msk.collector.sample.R;
import com.wolski_msk.collector.sample.utils.utils;

/**
 * Created by wpiot on 15.09.2016.
 */
public class AddingStage2 extends Fragment  implements View.OnClickListener{

    Boolean moneyObject = null;
    Boolean borrowLend = null;
    String topTitle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    public static Fragment newInstance()
    {
        return new AddingStage2();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
         moneyObject = getArguments().getBoolean("moneyObject");
         borrowLend = getArguments().getBoolean("borrowLend");
         topTitle    = getArguments().getString("topTitle");




        view =  inflater.inflate(R.layout.add_object, container, false);

        Button addPicture = (Button)view.findViewById(R.id.addPictureButton);
        TextInputEditText amount = (TextInputEditText)view.findViewById(R.id.amountInputLayout) ;
        TextInputEditText editText2 = (TextInputEditText)view.findViewById(R.id.editText2) ;
        ((TextView)view.findViewById(R.id.text_label_object)).setText(utils.CapitalizeFirstLetter(topTitle));

        if(moneyObject)
        {
            editText2.setVisibility(View.GONE);
            addPicture.setVisibility(View.GONE);
        }
        else
        {
            amount.setVisibility(View.GONE);
        }




        return view;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();



    }
}
