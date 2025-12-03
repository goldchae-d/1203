package com.example.camerax_mlkit.security

import java.security.MessageDigest

object QrHashUtil {
    // 문자열을 받아 SHA-256 해시값(Hex String)으로 반환
    fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}