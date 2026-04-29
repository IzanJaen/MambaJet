package com.example.mambajet.data.local.db.mapper

import com.example.mambajet.data.local.db.entity.AccessLogEntity
import com.example.mambajet.domain.AccessLog

fun AccessLogEntity.toDomain() = AccessLog(
    id = id,
    userId = userId,
    event = event,
    timestamp = timestamp
)

fun AccessLog.toEntity() = AccessLogEntity(
    userId = userId,
    event = event,
    timestamp = timestamp
)