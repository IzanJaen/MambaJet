package com.example.mambajet.data.local.db.mapper

import com.example.mambajet.data.local.db.entity.UserEntity
import com.example.mambajet.domain.UserProfile

fun UserEntity.toDomain() = UserProfile(
    id = id,
    email = email,
    username = username,
    birthdate = birthdate,
    address = address,
    country = country,
    phone = phone,
    acceptEmails = acceptEmails
)

fun UserProfile.toEntity() = UserEntity(
    id = id,
    email = email,
    username = username,
    birthdate = birthdate,
    address = address,
    country = country,
    phone = phone,
    acceptEmails = acceptEmails
)