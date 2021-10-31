package com.example.wordsmemory.interactors

import com.example.wordsmemory.data.manager.VocabularyManager

class GetVocabularyItems(private val _vocabularyManager: VocabularyManager) {
    operator fun invoke() = _vocabularyManager.getVocabularyItems()
}