package com.example.network.repository

import com.example.model.w3w.W3WResponse
import javax.inject.Inject

class FakeW3WDataSource @Inject constructor() : W3WDataSource {

    private val wordPool = listOf(
        "책상", "컴퓨터", "음료수", "의자", "마우스", "키보드", "모니터", "창문", "식물", "조명",
        "커튼", "노트북", "마우스패드", "전화기", "책장", "가방", "펜", "노트", "컵", "카메라",
        "시계", "달력", "사진", "연필", "파일", "책갈피", "USB", "볼펜", "라이터", "램프"
    )

    override suspend fun get3WordAddress(latitude: Double, longitude: Double): W3WResponse {
        val randomWords = wordPool.shuffled().take(3)
        return W3WResponse(words = randomWords.joinToString("/"))
    }

}