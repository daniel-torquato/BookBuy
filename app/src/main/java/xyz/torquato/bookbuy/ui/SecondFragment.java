package xyz.torquato.bookbuy.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import xyz.torquato.bookbuy.R;
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

        binding.buttonSecond.setOnClickListener(v ->
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment)
        );

        Log.d("MyTag", "Second view created");
        viewModel.uiState.observe(getViewLifecycleOwner(), item -> {
            Log.d("MyTag", "Item Details Title: " + item.title);
            Log.d("MyTag", "Item Details Author: " + item.author);
            Log.d("MyTag", "Item Details Description: " + item.description);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}