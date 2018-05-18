package cmpe275eat.takeoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;


public class HomeMenu extends Fragment {

    private ImageView gate;
    String current_select = "";

    Spinner sort_type_spinner;
    ArrayAdapter<CharSequence> sort_type_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.home_menu, container,false);

        sort_type_spinner = (Spinner) view.findViewById(R.id.my_spinner);
        sort_type_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_type, android.R.layout.simple_spinner_item);
        sort_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_type_spinner.setAdapter(sort_type_adapter);

        gate = (ImageView) view.findViewById(R.id.gate);
        gate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_select = sort_type_spinner.getSelectedItem().toString();
                Intent intent = new Intent(getActivity(), OrderActivity.class);

                intent.putExtra("sortType", current_select);

                startActivity(intent);
            }
        });

        return view;
    }
}
