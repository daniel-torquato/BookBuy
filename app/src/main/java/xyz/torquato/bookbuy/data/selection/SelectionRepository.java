package xyz.torquato.bookbuy.data.selection;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SelectionRepository {

    @Inject
    SelectionRepository() {
        Log.d("MyTag", "Example created");
    }

    public final MutableLiveData<Integer> selectedItemId = new MutableLiveData<>();

    public void setSelectedItemId(Integer id) {
        Log.d("MyTag", "Select item " + id);
        selectedItemId.setValue(id);
    }

}
