package com.example.mambajet.data.local.db.mapper

import com.example.mambajet.data.local.db.entity.TripEntity
import com.example.mambajet.domain.Trip

fun TripEntity.toDomain(): Trip = Trip(
    id = id,
    userId = userId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    description = description,
    totalBudget = totalBudget,
    spentBudget = spentBudget
)

fun Trip.toEntity(): TripEntity = TripEntity(
    id = id,
    userId = userId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    description = description,
    totalBudget = totalBudget,
    spentBudget = spentBudget
)