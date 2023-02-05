package com.kyrillosg.rijksstudio.domain.common

interface SynchronousUseCase<Input, Output> {
    operator fun invoke(input: Input): Output
}