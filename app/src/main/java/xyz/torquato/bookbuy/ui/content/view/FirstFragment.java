package xyz.torquato.bookbuy.ui.content.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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


        CustomAdapter adapter = new CustomAdapter(dataSet);
        viewModel.bookMenu.observe(getViewLifecycleOwner(), item -> {
            Log.d("MyTag", "Check view input " + item.content.size());
            dataSet.clear();
            dataSet.addAll(item.content);
            adapter.notifyDataSetChanged();
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        binding.contentList.setLayoutManager(layoutManager);
        binding.contentList.setAdapter(adapter);

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                viewModel.Search(s, 0, 5);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}