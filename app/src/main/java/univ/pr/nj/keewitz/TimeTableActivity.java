package univ.pr.nj.keewitz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class TimeTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        WebView wb = (WebView) findViewById(R.id.time_table);
        wb.getSettings().setJavaScriptEnabled(true);

        // Google Calendar For M2DL
        //wb.loadUrl("https://calendar.google.com/calendar/embed?src=master.developpement.logiciel@gmail.com&color=%23668CD9&mode=WEEK&ctz=Europe/Paris&showTitle=0&showNav=1&showDate=1&showTabs=1&showCalendars=0&hl=fr");

        // Celcat Calendar For M2DL
        wb.loadUrl("https://edt.univ-tlse3.fr/FSI/2017_2018/M2/M2_INF_DL/g251745.html");
    }
}
