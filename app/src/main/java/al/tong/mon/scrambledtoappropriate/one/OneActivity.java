package al.tong.mon.scrambledtoappropriate.one;

import android.databinding.DataBindingUtil;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.Collections;
import java.util.Vector;

import al.tong.mon.scrambledtoappropriate.R;
import al.tong.mon.scrambledtoappropriate.condition.CheckAvailable;
import al.tong.mon.scrambledtoappropriate.databinding.ActivityOneBinding;

public class OneActivity extends AppCompatActivity {

    ActivityOneBinding binding;
    OneAdapter adapter;
    CheckAvailable checkAvailable;
    Vector<One> mOne;
    int[] imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_one);

        // span과 이미지 갯수만 변경되면 모든 M x N 적용가능
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        binding.oneViews.setLayoutManager(layoutManager);
        adapter = new OneAdapter(this);
        binding.oneViews.setAdapter(adapter);
        mOne = new Vector<>();
        imgs = new int[]{
                R.drawable.img1, R.drawable.img2, R.drawable.img3,
                R.drawable.img4, R.drawable.img5, R.drawable.img6,
                R.drawable.img7, R.drawable.img8, R.drawable.img9
        };

        int randInt = (int) (Math.random() * imgs.length);
        for (int i = 0; i < imgs.length; i++) {
            if (i == randInt) {
                mOne.add(new One(R.drawable.img_white, i, true));
            } else {
                mOne.add(new One(imgs[i], i, false));
            }
        }

        Collections.shuffle(mOne);
        adapter.update(mOne);

        binding.oneViews.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int length = binding.oneViews.getWidth() / 3;
                adapter.setLength(length);
                adapter.notifyDataSetChanged();
                checkAvailable = new CheckAvailable(mOne, 3);
                binding.oneViews.addOnItemTouchListener(itemTouchListener);
                binding.oneViews.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        binding.restartBtn.setOnClickListener(view -> recreate());
    }

    RecyclerView.OnItemTouchListener itemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView parent, @NonNull MotionEvent evt) {
            int action = evt.getAction();
            if (action == MotionEvent.ACTION_UP) {
                View child = parent.findChildViewUnder(evt.getX(), evt.getY());
                assert child != null;
                int pos = parent.getChildAdapterPosition(child);
                int newPos = checkAvailable.check(pos);
                if (newPos != -100) {
                    adapter.change(pos, newPos);
                }
                int good_job = 0;
                for (int i = 0; i < mOne.size(); i++) {
                    if (i == mOne.get(i).getTag()) {
                        good_job++;
                    }
                }
                if (good_job == mOne.size()) {
                    Vector<One> one = adapter.currentOne();
                    for (int i = 0; i < one.size(); i++) {
                        boolean empty = one.get(i).isEmpty();
                        if (empty) {
                            adapter.finish(i);
                            binding.oneViews.removeOnItemTouchListener(itemTouchListener);
                            break;
                        }
                    }
                    Toast.makeText(OneActivity.this, "참 잘했어요.", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    };
}
