package com.moengage.machinecoding.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moengage.machinecoding.models.Question
import com.moengage.machinecoding.models.QuizInfo
import com.moengage.machinecoding.network.OnboardingNetworkManager
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class QuizViewModel : ViewModel() {
    var quizInfo: QuizInfo? = null
    private val _currentQuestion = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int>
        get() = _currentQuestion

    fun fetchQuizInfo(context: Context) {
        if (quizInfo != null) return
        quizInfo = QuizInfo()
        val rawResponse = String(
            OnboardingNetworkManager.getOnboardingQuestions(context),
            StandardCharsets.UTF_8
        )
        parseAndAddQuestion(rawResponse)
    }

    private fun parseAndAddQuestion(rawResponse: String) {
        val jsonObject = JSONObject(rawResponse)
        jsonObject.getJSONArray("questions").let {
            val size = it.length() - 1
            for (i in 0..size) {
                val questionJson = it.getJSONObject(i)
                quizInfo?.questions?.add(
                    Question(
                        questionJson.getString("text"),
                        questionJson.getString("type")
                    )
                )
            }
        }
    }

    fun nextQuestion(text: String) {
        if (quizInfo == null) return
        quizInfo!!.questions[currentQuestionIndex.value!!].answer = text
        _currentQuestion.value = _currentQuestion.value?.plus(1)
    }

    fun previousQuestion() {
        if (currentQuestionIndex.value != 0)
            _currentQuestion.value = _currentQuestion.value?.minus(1)
    }

    fun restart(context: Context) {
        quizInfo = null
        fetchQuizInfo(context)
        _currentQuestion.value = 0
    }
}