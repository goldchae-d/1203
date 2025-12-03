package com.example.camerax_mlkit.security

import android.util.Log
import com.example.camerax_mlkit.security.QrHashUtil.sha256

object QrRawWhitelist {
    private const val TAG = "QrHashVerifier" // 로그 태그

    // ⚠️ 주의: 아래 키 값들은 '실제 URL'을 sha256으로 변환한 값으로 채워야 합니다.
    // 개발 편의를 위해 앱 실행 시 로그를 보고 이 값을 복사해서 채워넣으세요.
    private val map: MutableMap<String, String> = linkedMapOf(
        // 예시: "https://qr.kakaopay.com/..."의 해시값 (가상)
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
                to "store_duksung_a",

        // 여기에 실제 사용하실 QR의 해시값을 추가하세요.
    )

    /**
     * 조회: 들어온 원문(raw)을 해싱한 뒤, 맵에 있는지 확인
     */
    fun locationOf(raw: String): String? {
        val cleanRaw = raw.trim()
        val hashed = sha256(cleanRaw) // 1. 해시 변환

        // ✅ [로그캣 확인 포인트]
        Log.d(TAG, "🔍 [검증 시작]")
        Log.d(TAG, "   👉 원본 데이터: $cleanRaw")
        Log.d(TAG, "   👉 변환된 해시: $hashed")

        val result = map[hashed] // 2. 맵에서 조회

        if (result != null) {
            Log.d(TAG, "   ✅ 검증 성공! 매장 ID: $result")
        } else {
            Log.d(TAG, "   ❌ 검증 실패 (등록되지 않은 해시)")
            // 개발 중에는 해시값을 복사하기 쉽도록 로그를 남겨둡니다.
            Log.d(TAG, "   (개발용) 이 값을 복사해서 map에 등록하세요: \"$hashed\"")
        }

        return result
    }

    /** * 런타임 등록 (필요 시 사용): 들어온 raw를 즉시 해싱해서 등록
     */
    fun registerRawForStore(raw: String, locationId: String) {
        val h = sha256(raw.trim())
        map[h] = locationId
        Log.d(TAG, "➕ 동적 등록 완료: $locationId (Hash: $h)")
    }

    // LV2 헬퍼 (그대로 유지)
    fun isAllowedAt(raw: String, ctxLocationId: String?): Boolean {
        val ctx  = ctxLocationId?.trim()?.lowercase() ?: return false
        val qrId = locationOf(raw)?.trim()?.lowercase() ?: return false
        return qrId == ctx
    }
}