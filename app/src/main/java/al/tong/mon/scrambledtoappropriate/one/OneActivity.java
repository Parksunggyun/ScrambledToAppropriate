package al.tong.mon.scrambledtoappropriate.one;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    int[] imgs3X3 = new int[]{
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img_white
    };

    private static final int N = 3;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_one);

        // span과 이미지 갯수만 변경되면 모든 M x N 적용가능
        GridLayoutManager layoutManager = new GridLayoutManager(this, N);
        binding.oneViews.setLayoutManager(layoutManager);
        adapter = new OneAdapter(this);
        binding.oneViews.setAdapter(adapter);

        binding.oneViews.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int length = binding.oneViews.getWidth() / N;

                SliceImageView siv = new SliceImageView(OneActivity.this);
                siv.setWH(length, length);
                Bitmap[] bitmaps = siv.getBit3X3();
                mOne = new Vector<>();
                for (int i = 0; i < bitmaps.length; i++) {
                    if (i == Math.pow(N, 2) - 1) {
                        mOne.add(new One(bitmaps[i], i, true));
                    } else {
                        mOne.add(new One(bitmaps[i], i, false));
                    }
                }
                adapter.update(mOne);
                checkAvailable = new CheckAvailable(mOne, N);
                int shufflePos = imgs3X3.length - 1;
                int[] ways = {-1, 1, -N, N};
                int shuffleCount = (int) (Math.random() * 500) + 500;
                for (int i = 0; i < shuffleCount; i++) {
                    int wayIdx = (int) (Math.random() * 4);
                    shufflePos += ways[wayIdx];
                    if (shufflePos < imgs3X3.length && shufflePos > -1) {
                        int[] result = checkAvailable.check(shufflePos);
                        if (result[0] != 100) {
                            adapter.change(shufflePos, result[0]);
                            shufflePos = result[0];
                        }
                    } else {
                        shufflePos -= ways[wayIdx];
                    }
                }
                adapter.update(mOne);
                adapter.setLength(length);
                adapter.notifyDataSetChanged();
                binding.oneViews.addOnItemTouchListener(itemTouchListener);
                binding.oneViews.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        binding.restartBtn.setOnClickListener(view -> recreate());
    }

    private int getAnim(int anim) {
        int animation = 0;
        switch (anim) {
            case 1:
                animation = R.anim.down_to_up;
                break;
            case 2:
                animation = R.anim.up_to_down;
                break;
            case 3:
                animation = R.anim.right_to_left;
                break;
            case 4:
                animation = R.anim.left_to_right;
                break;
        }
        return animation;
    }

    RecyclerView.OnItemTouchListener itemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView parent, @NonNull MotionEvent evt) {
            int action = evt.getAction();
            if (action == MotionEvent.ACTION_UP) {
                View child = parent.findChildViewUnder(evt.getX(), evt.getY());
                assert child != null;
                int pos = parent.getChildAdapterPosition(child);
                Log.e("pos", String.valueOf(pos));
                if (pos != -1) {
                    int[] result = checkAvailable.check(pos);
                    int newPos = result[0];
                    int anim = getAnim(result[1]);
                    Log.e("new pos = " + newPos, "anim = " + result[1]);
                    if (newPos != 100) {
                        Animation animation = AnimationUtils.loadAnimation(OneActivity.this, anim);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Vector<One> one = adapter.change(pos, newPos);
                                int good_job = 0;
                                for (int i = 0; i < mOne.size(); i++) {
                                    if (i == mOne.get(i).getTag()) {
                                        good_job++;
                                    }
                                }
                                Log.e("current good job", String.valueOf(good_job));
                                if (good_job == mOne.size()) {
                                    binding.showCorrectImageView.setVisibility(View.VISIBLE);
                                    binding.showCorrectImageView.setImageResource(R.drawable.ggg);
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

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        child.startAnimation(animation);
                    }
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

    class SliceImageView extends View {
        Bitmap img;
        int imgW, imgH;
        Bitmap[] bit3X3 = new Bitmap[9];

        int width = 0;
        int height = 0;

        public void setWH(int width, int height) {
            this.width = width;
            this.height = height;
            binding.correctImageView.setImageResource(R.drawable.ggg);
            img = BitmapFactory.decodeResource(getResources(), R.drawable.ggg);
            img = Bitmap.createScaledBitmap(img, width, height, true);
            imgW = img.getWidth() / N;
            imgH = img.getHeight() / N;

            for (int i = 0; i < Math.pow(N, 2); i++) {
                bit3X3[i] = Bitmap.createBitmap(img, (i % N) * imgW, (i / N) * imgH, imgW, imgH);
            }
            bit3X3[8] = Bitmap.createBitmap(img, 0, 0, 1, 1);
        }

        public SliceImageView(Context context) {
            super(context);
        }

        public Bitmap[] getBit3X3() {
            return bit3X3;
        }
    }

}
