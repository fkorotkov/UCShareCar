package ridesharers.ucsc.edu.ucsharecar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ridesharers.ucsc.edu.ucsharecar.dummy.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * An activity representing a list of Posts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PostListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private boolean mTwoPane;

    private final String TAG = "UCShareCar_PostList";

    //vars
    private ArrayList<PostInfo> postList = new ArrayList<>();

    private BackendClient backend;
    private Context postListContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        ImageButton my_page = findViewById(R.id.my_page);
        my_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostListActivity.this, MyPage.class);
                startActivity(intent);
            }
        });

        ImageButton add_report = findViewById(R.id.add_report);
        add_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostListActivity.this, CreateReportActivity.class);
                startActivity(intent);
            }
        });


        ImageButton search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Get the backend object
        backend = BackendClient.getSingleton(this);
        setupRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!backend.hasSession()) {
            Log.w(TAG, "Sending user to login page");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final PostListAdapter adapter = new PostListAdapter(this, postList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "setupRecyclerView: init recycleview.");
        //These are just test code, but the app should be able to get the info from the data base
        postList.add(new PostInfo(new Date(), new Date(), "start", "end", "memo", true, null, null, null, 5));

        backend.getAllPosts(new Response.Listener<ArrayList<PostInfo>>() {
            @Override
            public void onResponse(ArrayList<PostInfo> response) {
                postList.addAll(response);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                Toast.makeText(getApplicationContext(), (String) error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
