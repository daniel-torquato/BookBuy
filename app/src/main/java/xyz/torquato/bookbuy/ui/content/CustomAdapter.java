package xyz.torquato.bookbuy.ui.content;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.torquato.bookbuy.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final ArrayList<BookItem> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTitle;
        private final TextView itemAuthor;
        private final TextView itemDescription;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            itemTitle = view.findViewById(R.id.itemTitle);
            itemAuthor = view.findViewById(R.id.itemAuthor);
            itemDescription = view.findViewById(R.id.itemDescription);
        }

        public void setItem(BookItem item) {
            itemTitle.setText(item.title);
            itemAuthor.setText(item.author);
            itemDescription.setText(item.description);
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
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setItem(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
