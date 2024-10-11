package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.example.bakalarkaapp.XmlUtils
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

class EyesightDifferRepo(context: Context) {
//    private val mappedClass: Data = XmlUtils().
//        parseXmlData(context, "eyesight_differ_data", Data::class.java)
//    val data: List<DifferItem> = mappedClass.data
}

@JsonRootName("answers")
class Answers {
    @JsonProperty("correctAnswer")
    var correctAnswer: List<String> = ArrayList()
}

@JsonRootName("qaPair")
class QaPair {
    @JsonProperty("answers")
    var answers: Answers = Answers()

    @JsonProperty("question")
    var question: String = ""
}

@JsonRootName("questionAndAnswers")
class QuestionAndAnswers {
    @JsonProperty("QaPair")
    var qaPairs: List<QaPair> = ArrayList()
}

@JsonRootName("differItem")
class DifferItem {
    @JsonProperty("imageId")
    var imageId: String = ""

    @JsonProperty("questionAndAnswers")
    var questionAndAnswers: QuestionAndAnswers = QuestionAndAnswers()
}

@JsonRootName("dataItems")
class DifferData {
    @JsonProperty("differItem")
    var data: List<DifferItem> = ArrayList()
}