package xyz.torquato.bookbuy.ui.content.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import xyz.torquato.bookbuy.databinding.FragmentFirstBinding;
import xyz.torquato.bookbuy.domain.BookItem;
import xyz.torquato.bookbuy.ui.content.BookMenuViewModel;

@AndroidEntryPoint
public class FirstFragment extends Fragment {

    @Inject
    BookMenuViewModel viewModel;

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        var dataSet = new ArrayList<BookItem>();
        viewModel.bookMenu.observe(getViewLifecycleOwner(), item -> {
            dataSet.clear();
            dataSet.addAll(item.content);
        });

        CustomAdapter adapter = new CustomAdapter(dataSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        binding.contentList.setLayoutManager(layoutManager);
        binding.contentList.setAdapter(adapter);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.searchBar.addTextChangedListener(watcher);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}