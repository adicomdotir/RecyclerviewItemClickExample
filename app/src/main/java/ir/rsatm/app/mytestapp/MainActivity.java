package ir.rsatm.app.mytestapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Model> viewModels = new ArrayList<>();
        viewModels.add(new Model("One", 1));
        viewModels.add(new Model("Two", 2));
        viewModels.add(new Model("Three", 3));
        viewModels.add(new Model("Four", 4));

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // specify an adapter
        MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(viewModels, R.layout.item_layout);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, boolean b) {
                TextView textView = (TextView) view.findViewById(R.id.textview);
                Log.e("TAG", textView.getText().toString());
                Log.e("TAG", String.valueOf(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}

class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<Model> items;
    private int itemLayout;

    public MyRecyclerAdapter(List<Model> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model item = items.get(position);
        holder.text.setText(item.getName());
        holder.textview.setText("" + item.getNumber());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView textview;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textview);
            textview = (TextView) itemView.findViewById(R.id.textview2);
        }
    }
}

class Model {
    private int number;
    private String name;

    Model(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

interface RecyclerViewClickListener {
    void onClick(View view, int position, boolean isLongClick);
    void onLongClick(View view, int position);
}

class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private RecyclerViewClickListener clickListener;

    public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child), false);
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

