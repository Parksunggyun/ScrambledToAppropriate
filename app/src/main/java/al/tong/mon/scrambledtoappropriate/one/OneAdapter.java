package al.tong.mon.scrambledtoappropriate.one;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Vector;

import al.tong.mon.scrambledtoappropriate.R;
import al.tong.mon.scrambledtoappropriate.databinding.ItemOneBinding;

class OneAdapter extends RecyclerView.Adapter<OneAdapter.OneHolder> {

    private Vector<One> mOne = new Vector<>();

    private Activity activity;
    private Context context;

    private int width = 0, height = 0;
    Bitmap[] bitmaps = new Bitmap[9];

    public OneAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @NonNull
    @Override
    public OneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOneBinding binding = ItemOneBinding.inflate(LayoutInflater.from(context), parent, false);
        return new OneHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OneHolder holder, int position) {
        ItemOneBinding binding = holder.binding;
        if (width != 0 && height != 0) {
            binding.itemCardView.getLayoutParams().width = width;
            binding.itemCardView.getLayoutParams().height = height;
        }
        One one = mOne.get(position);
        Bitmap image = one.getImage();
        binding.itemCardView.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return mOne.size();
    }

    void update(Vector<One> ones) {
        this.mOne = ones;
        notifyDataSetChanged();
    }

    void setLength(int length) {
        this.width = length;
        this.height = length;
    }

     Vector<One> change(int oldPos, int newPos) {
        //6, 9
        One oldOne = mOne.get(oldPos);
        One newOne = mOne.get(newPos);
        mOne.remove(newPos);
        mOne.add(newPos, oldOne);
        mOne.remove(oldPos);
        mOne.add(oldPos, newOne);
        notifyDataSetChanged();
         return mOne;
    }

    Vector<One> currentOne() {
        return this.mOne;
    }

    void finish(int pos) {
        mOne.get(pos).setImage(bitmaps[pos]);
        notifyItemChanged(pos);
    }

    class OneHolder extends RecyclerView.ViewHolder {

        ItemOneBinding binding;

        OneHolder(ItemOneBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
