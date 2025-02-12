package xyz.torquato.bookbuy.ui.content.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.torquato.bookbuy.R;
import xyz.torquato.bookbuy.domain.BookItem;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final ArrayList<BookItem> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView smallThumbnail;

        private final View parent;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            smallThumbnail = view.findViewById(R.id.smallThumbnail);
            parent = view;
        }

        public void setItem(BookItem item) {
            // TODO: load in parallel and just some at time.
            Glide.with(parent).load(item.largeThumbnailUrl).into(smallThumbnail);
        }
    }



    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(ArrayList<BookItem> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);


        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = (viewGroup.getWidth() / 2);
        lp.height = (viewGroup.getWidth() / 8 * 5);
        view.setLayoutParams(lp);


        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.setItem(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
