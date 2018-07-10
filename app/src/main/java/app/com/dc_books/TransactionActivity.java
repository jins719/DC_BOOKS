package app.com.dc_books;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionActivity extends AppCompatActivity {

    TextView t_paymentstate;
    TextView t_amount;

    ImageView image;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transation_success);

        t_paymentstate=findViewById(R.id.textView64);
        t_amount=findViewById(R.id.textView66);

        image=findViewById(R.id.imageView20);


        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        t_paymentstate.setText(getIntent().getStringExtra("State"));
        t_amount.setText(getResources().getString(R.string.Rs)+getIntent().getStringExtra("Amount"));


        if(getIntent().getStringExtra("State").equals("Success"))
        {
            image.setImageDrawable(getResources().getDrawable(R.mipmap.icontick));

        }else
        {
            image.setImageDrawable(getResources().getDrawable(R.mipmap.iconblock));
        }



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                Intent i = new Intent(TransactionActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(TransactionActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
