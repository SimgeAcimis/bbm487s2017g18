package com.example.enes_karabulut.library;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("bookInfo");

        TextView title = (TextView) findViewById(R.id.title);
        TextView author = (TextView) findViewById(R.id.author);
        TextView details = (TextView) findViewById(R.id.details);
        TextView year = (TextView) findViewById(R.id.year);
        ImageView book_image;


        title.setText(UserActivity.bookArrayList.get(position).title);
        author.setText(UserActivity.bookArrayList.get(position).author);
        details.setText(UserActivity.bookArrayList.get(position).details);
        year.setText(UserActivity.bookArrayList.get(position).year);

        book_image = (ImageView)findViewById(R.id.imageView_book);
        loadImage(position,book_image);


    }

    public void loadImage(int position, ImageView book_image){

        if(UserActivity.bookArrayList.get(position).book_image != null){

            String img_url= UserActivity.bookArrayList.get(position).book_image;
            Picasso.with(BookDetailActivity.this).load(img_url).into(book_image);
        }
    }
}
