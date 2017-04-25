package com.example.enes_karabulut.library;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class UserActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_DETAILS ="details";
    private static final String TAG_YEAR ="year";
    private static final String TAG_URL ="book_image";

    private static final String TAG = "UserActivity";
    private TextView greetingTextView;
    private Button btnLogOut;

    JSONArray books = null;

    ArrayList<HashMap<String, String>> bookList;
    public static ArrayList<Book> bookArrayList = new ArrayList<>();

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bundle = getIntent().getExtras();
        String user = bundle.getString("1907");
        greetingTextView = (TextView) findViewById(R.id.greeting_text_view);
        btnLogOut = (Button) findViewById(R.id.logout_button);
        greetingTextView.setText("Hello, "+ user);

        // Progress dialog
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        list = (ListView) findViewById(R.id.listView_books);
        bookList = new ArrayList<HashMap<String,String>>();
        getData();
    }


    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            books = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<books.length();i++){

                JSONObject c = books.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String author = c.getString(TAG_AUTHOR);
                String year = c.getString(TAG_YEAR);
                String details = c.getString(TAG_DETAILS);
                String book_image = c.getString(TAG_URL);

                HashMap<String,String> bookHash = new HashMap<>();
                Book book = new Book();

                book.title =  c.getString(TAG_TITLE);
                book.author = c.getString(TAG_AUTHOR);
                book.year = c.getString(TAG_YEAR);
                book.details = c.getString(TAG_DETAILS);
                book.book_image = c.getString(TAG_URL);

                bookArrayList.add(book);

                bookHash.put(TAG_TITLE,title);
                bookHash.put(TAG_AUTHOR,author);
                bookHash.put(TAG_DETAILS,details);
                bookHash.put(TAG_YEAR,year);
                bookHash.put(TAG_URL,book_image);

                bookList.add(bookHash);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object listItem = list.getItemAtPosition(position);

                        Intent i = new Intent(getApplicationContext(), BookDetailActivity.class);
                        i.putExtra("bookInfo", position);
                        startActivity(i);

                    }
                });
            }

            ListAdapter adapter = new SimpleAdapter(
                    UserActivity.this, bookList, R.layout.list_item,
                    new String[]{TAG_TITLE,TAG_AUTHOR,TAG_YEAR},
                    new int[]{R.id.title,R.id.author,R.id.year}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://www.pcarsiv.com/library/getData.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}


