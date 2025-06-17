package com.goiaba

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform