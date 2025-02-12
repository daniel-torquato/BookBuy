package xyz.torquato.bookbuy.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import xyz.torquato.bookbuy.databinding.FragmentSecondBinding;
import xyz.torquato.bookbuy.ui.content.view.detailed.DetailedItemViewModel;

@AndroidEntryPoint
public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Inject
    DetailedItemViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buyHere.setOnClickListener(v -> {
            Log.d("MyTag", "Open browse to buy");
        });

        binding.favorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("MyTag", "Is favorite: " + isChecked);
        });
        Log.d("MyTag", "Second view created");
        viewModel.uiState.observe(getViewLifecycleOwner(), item -> {
            loadImage(item.largeThumbnailUrl);
            binding.itemTitle.setText(item.title);
            binding.itemAuthor.setText(item.author);
            binding.itemDescription.setText(item.description);
        });

    }

    private void loadImage(String url) {
        Glide.with(this).load(url).into(binding.thumbnail);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}