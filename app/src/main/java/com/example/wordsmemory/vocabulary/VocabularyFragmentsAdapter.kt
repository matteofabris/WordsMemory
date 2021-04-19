package com.example.wordsmemory.vocabulary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wordsmemory.vocabulary.words.VocabularyWordsFragment
import com.example.wordsmemory.vocabulary.categories.VocabularyCategoriesFragment
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class VocabularyFragmentsAdapter(fragment: Fragment, private val itemsCount: Int) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return VocabularyWordsFragment()
            1 -> return VocabularyCategoriesFragment()
        }

        return Fragment()
    }
}