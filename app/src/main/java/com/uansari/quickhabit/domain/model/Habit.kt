package com.uansari.quickhabit.domain.model

data class Habit(
    val id: Int=0,
    val name: String,
    val emoji: String,
    val createdAt: Long,
)
