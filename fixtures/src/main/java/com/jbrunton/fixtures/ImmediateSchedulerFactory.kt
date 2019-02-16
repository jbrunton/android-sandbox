package com.jbrunton.fixtures

import com.jbrunton.entities.SchedulerFactory
import io.reactivex.schedulers.Schedulers

class ImmediateSchedulerFactory : SchedulerFactory {
    override val IO = Schedulers.trampoline()
    override val Main = Schedulers.trampoline()
}