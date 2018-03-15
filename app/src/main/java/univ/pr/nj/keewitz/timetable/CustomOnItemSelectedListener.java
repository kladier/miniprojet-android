package univ.pr.nj.keewitz.timetable;

import android.view.View;
import android.widget.AdapterView;

import univ.pr.nj.keewitz.utils.FirebaseUtils;

/**
 * Created by what on 15/03/18.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String url = "";
        switch (parent.getItemAtPosition(pos).toString()) {
            case "M1 DL":
                url = "https://edt.univ-tlse3.fr/FSI/2017_2018/M1/M1_INF_DL/g241675.html";
                break;
            case "M2 DL":
                url = "https://edt.univ-tlse3.fr/FSI/2017_2018/M2/M2_INF_DL/g251745.html";
                break;
            case "L3 info":
                url = "https://edt.univ-tlse3.fr/FSI/2017_2018/L3/L3_Info/g235934.html";
                break;
        }
        FirebaseUtils.writeValue(url, "timetableUrl" );
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}