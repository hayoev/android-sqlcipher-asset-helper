package tj.yahya.android_sqlcipher_asset_helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DemoDatabase db = new DemoDatabase(getApplicationContext());
        ArrayList list = db.getUsers();

        TextView textView = (TextView) findViewById(R.id.textView);

        for (Object l : list) {
            textView.setText( textView.getText()+ "\n"+ l.toString());
        }
    }
}
