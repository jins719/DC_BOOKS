package app.com.dc_books;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


/**
 * Created by JINS on 6/28/2017.
 */

public class Full_Image_View extends AppCompatActivity {

    ImageView imageView;
    String image_path;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimageview);

        Bundle t=getIntent().getExtras();
        image_path=t.getString("path");

        imageView=(ImageView)findViewById(R.id.imageView25);
        toolbar=findViewById(R.id.toolbar);

        //PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);

        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.avtar);
        requestOptions.error(R.mipmap.avtar);

        Glide.with(Full_Image_View.this)
                .load(image_path).apply(requestOptions).into(imageView);


       /* try {
            imageView.setImageURI(Uri.parse(image_path));
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();
        }*/
    }


    public void onBackPressed() {
        Full_Image_View.this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                Full_Image_View.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
