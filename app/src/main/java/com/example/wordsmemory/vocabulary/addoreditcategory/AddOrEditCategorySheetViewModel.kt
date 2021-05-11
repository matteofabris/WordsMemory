package com.example.wordsmemory.vocabulary.addoreditcategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wordsmemory.database.VocabularyDao
import com.example.wordsmemory.model.Category
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

class AddCategorySheetViewModel(
    private val _dbDao: VocabularyDao,
    private val _selectedCategoryId: Int? = null
) : ViewModel() {

    private lateinit var _category: Category

    val addButtonText = MutableLiveData("Add")
    val category = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            if (_selectedCategoryId != null) {
                _category = _dbDao.getCategoryById(_selectedCategoryId)
                category.value = _category.category
                addButtonText.value = "Update"
            }
        }
    }

    fun insertOrUpdateCategory() {
        viewModelScope.launch {
            if (_selectedCategoryId != null) {
                _category.category = category.value!!.lowercase(Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                _dbDao.updateCategory(_category)
            } else {
                _dbDao.insertCategory(
                    Category(
                        category.value!!.lowercase(Locale.getDefault())
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    )
                )
            }
        }
    }
}

class AddCategorySheetViewModelFactory(
    private val _dataSource: VocabularyDao,
    private val _selectedCategoryId: Int? = null
) : ViewModelProvider.Factory {
    @InternalCoroutinesApi
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCategorySheetViewModel::class.java)) {
            return AddCategorySheetViewModel(_dataSource, _selectedCategoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}