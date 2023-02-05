package com.kyrillosg.rijksstudio.domain.common

interface UseCase<Input, Output> {
    suspend operator fun invoke(input: Input): Output
}