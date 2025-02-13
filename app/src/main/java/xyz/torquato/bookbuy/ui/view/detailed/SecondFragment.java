package xyz.torquato.bookbuy.ui.view.detailed;

import android.content.Intent;
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
            Intent buyIntent = viewModel.getBuyIntent();
            if (buyIntent != null) {
                startActivity(buyIntent);
            }
        });

        binding.favorite.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.OnPerformFavorite(isChecked)
        );
        Log.d("MyTag", "Second view created");
        viewModel.uiState.observe(getViewLifecycleOwner(), item -> {
            loadImage(item.largeThumbnailUrl);
            binding.itemTitle.setText(item.title);
            binding.itemAuthor.setText(item.author);
            binding.itemDescription.setText(item.description);
            binding.buyHere.setEnabled(item.hasBuyLink);
            binding.favorite.setChecked(item.isFavorite);
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