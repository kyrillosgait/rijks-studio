package com.kyrillosg.rijksstudio.core.domain.common

interface SynchronousUseCase<Input, Output> {
    operator fun invoke(input: Input): Output
}
