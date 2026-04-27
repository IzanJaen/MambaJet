package com.example.mambajet.data.local.db.mapper

import com.example.mambajet.data.local.db.entity.ActivityEntity
import com.example.mambajet.domain.Activity
import com.example.mambajet.domain.PlanType

fun ActivityEntity.toDomain(): Activity = Activity(
    id = id,
    tripId = tripId,
    title = title,
    description = description,
    date = date,
    time = time,
    cost = cost,
    type = PlanType.valueOf(type)
)

fun Activity.toEntity(): ActivityEntity = ActivityEntity(
    id = id,
    tripId = tripId,
    title = title,
    description = description,
    date = date,
    time = time,
    cost = cost,
    type = type.name
)