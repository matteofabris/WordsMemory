package com.example.wordsmemory.ui.vocabulary.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsmemory.R
import com.example.wordsmemory.adpater.VocabularyItemAdapter
import com.example.wordsmemory.databinding.VocabularyWordsFragmentBinding
import com.example.wordsmemory.helper.SwipeToDeleteCallback
import com.example.wordsmemory.ui.vocabulary.VocabularyFragment
import com.example.wordsmemory.ui.vocabulary.VocabularyFragmentDirections
import com.example.wordsmemory.ui.vocabulary.category.CategoryFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@AndroidEntryPoint
class VocabularyWordsFragment(private val _categoryId: Int = -1) : Fragment() {

    private val _viewModel: VocabularyWordsViewModel by viewModels()
    private lateinit var _binding: VocabularyWordsFragmentBinding
    private var _addClicked = false
    private val _showAddOrEditVocabularyItemSheet: (Int) -> Boolean = {
        if (!_addClicked) {
            _addClicked = true

            val navDirection =
                if (parentFragment is VocabularyFragment)
                    VocabularyFragmentDirections.actionVocabularyFragmentToAddOrEditVocabularyItemSheet(
                        it
                    )
                else CategoryFragmentDirections.actionCategoryFragmentToAddOrEditVocabularyItemSheet(
                    it
                )

            findNavController().navigate(navDirection)

            lifecycleScope.launch(Dispatchers.IO) {
                delay(500)
                _addClicked = false
            }
            true
        } else false
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewModel.initVocabularyList(_categoryId)
        _binding = VocabularyWordsFragmentBinding.inflate(inflater)
        _binding.viewModel = _viewModel

        setupVocabularyList()
        setupAddButtonListener()

        return _binding.root
    }

    private fun setupVocabularyList() {
        val vocabularyAdapter = VocabularyItemAdapter(
            VocabularyItemAdapter.OnLongClickListener { _showAddOrEditVocabularyItemSheet.invoke(it.id) }
        )
        _binding.vocabularyList.adapter = vocabularyAdapter

        setListDivider()
        setSwipeGesture()

        _viewModel.vocabularyList.observe(
            viewLifecycleOwner,
            { it?.let { vocabularyAdapter.addHeaderAndSubmitList(it) } })
    }

    private fun setupAddButtonListener() {
        _binding.addButton.setOnClickListener {
            _showAddOrEditVocabularyItemSheet.invoke(-1)
        }
    }

    private fun setListDivider() {
        if (context == null) return

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val listDivider =
            ContextCompat.getDrawable(requireContext(), R.drawable.vocabulary_list_divider)
                ?: return

        itemDecoration.setDrawable(listDivider)
        _binding.vocabularyList.addItemDecoration(itemDecoration)
    }

    private fun setSwipeGesture() {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                _viewModel.removeItem((viewHolder as VocabularyItemAdapter.VocabularyItemViewHolder).itemId)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(_binding.vocabularyList)
    }
}