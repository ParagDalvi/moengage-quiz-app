package com.moengage.machinecoding

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.moengage.machinecoding.viewmodels.QuizViewModel

class MainActivity : AppCompatActivity() {
    lateinit var tvQuestion: TextView
    lateinit var etAnswer: EditText
    lateinit var quizViewModel: QuizViewModel
    lateinit var btnNext: Button
    lateinit var btnPrev: Button
    lateinit var btnRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        quizViewModel.fetchQuizInfo(this)
        initUi()
        observeData()
    }

    private fun observeData() {
        quizViewModel.currentQuestionIndex.observe(this) {
            if (quizViewModel.quizInfo == null) return@observe
            if (it == quizViewModel.quizInfo?.questions!!.size) {
                gameOver()
            } else {
                val question = quizViewModel.quizInfo!!.questions[it]

                tvQuestion.text = question.question

                when (question.type) {
                    "string" -> {
                        etAnswer.inputType = InputType.TYPE_CLASS_TEXT
                    }
                    "integer" -> {
                        etAnswer.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    "email" -> {
                        etAnswer.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    else -> etAnswer.inputType = InputType.TYPE_CLASS_TEXT
                }

                if (question.answer != null)
                    etAnswer.setText(question.answer)
            }
        }
    }

    private fun gameOver() {
        etAnswer.visibility = View.GONE
        btnPrev.visibility = View.GONE
        btnNext.visibility = View.GONE

        btnRestart.visibility = View.VISIBLE

        var result = ""
        quizViewModel.quizInfo?.questions?.forEachIndexed { i, question ->
            result += "${i + 1}. ${question.question}\n"
            result += "${question.answer}\n\n"
        }
        tvQuestion.text = result
    }

    private fun initUi() {
        tvQuestion = findViewById(R.id.tv_question)
        etAnswer = findViewById(R.id.et_answer)
        btnNext = findViewById(R.id.btn_next)
        btnNext.setOnClickListener { handleNextClick() }
        btnPrev = findViewById(R.id.btn_prev)
        btnPrev.setOnClickListener { quizViewModel.previousQuestion() }
        btnRestart = findViewById(R.id.btn_restart)
        btnRestart.setOnClickListener { handleRestart() }
    }

    private fun handleRestart() {
        btnRestart.visibility = View.GONE
        etAnswer.visibility = View.VISIBLE
        btnPrev.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        quizViewModel.restart(this)
    }

    private fun handleNextClick() {
        if (etAnswer.text.isEmpty()) {
            Toast.makeText(this, "Please enter answer", Toast.LENGTH_SHORT).show()
            return
        }
        quizViewModel.nextQuestion(etAnswer.text.toString())
        etAnswer.text.clear()
    }
}