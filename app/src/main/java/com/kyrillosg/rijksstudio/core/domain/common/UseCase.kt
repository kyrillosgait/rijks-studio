package com.kyrillosg.rijksstudio.core.domain.common

interface UseCase<Input, Output> {
    suspend operator fun invoke(input: Input): Output
}