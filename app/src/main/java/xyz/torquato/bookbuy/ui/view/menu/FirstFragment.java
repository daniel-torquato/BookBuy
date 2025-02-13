package xyz.torquato.bookbuy.ui.view.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import xyz.torquato.bookbuy.R;
import xyz.torquato.bookbuy.databinding.FragmentFirstBinding;
import xyz.torquato.bookbuy.domain.model.BookItem;

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

        var onClick = new OnThumbnailClick() {
            @Override
            public void run(int id) {
                Log.d("MyTag", "OnCLick " + id);
                OnOpenDetail();
                viewModel.OnSelect(id);
            }
        };

        CustomAdapter adapter = new CustomAdapter(dataSet, onClick);
        viewModel.bookMenu.observeForever(item -> {
            Log.d("MyTag", "Check view input " + item.content.size());
            dataSet.clear();
            dataSet.addAll(item.content);
            adapter.notifyDataSetChanged();
        });
        viewModel.isFiltered.observeForever(isFiltered -> {
            binding.favoriteFilter.setChecked(isFiltered);
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.contentList.setLayoutManager(layoutManager);
        binding.contentList.setAdapter(adapter);
        binding.favoriteFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setFavoriteFilter(isChecked);
        });

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

    void OnOpenDetail() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
    }
}