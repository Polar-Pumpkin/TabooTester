package org.serverct.parrot.tabootester.sample

import taboolib.common.LifeCycle
import taboolib.common.inject.Injector
import taboolib.common.platform.Awake
import java.util.function.Supplier

interface Module {

    // ...

    companion object {
        val modules: MutableList<Module> = ArrayList()
    }

    @Awake
    object ModuleRegister : Injector.Classes {

        override val lifeCycle: LifeCycle = LifeCycle.ENABLE
        override val priority: Byte = 0

        override fun inject(clazz: Class<*>, instance: Supplier<*>) {
            if (Module::class.java !in clazz.interfaces) {
                return
            }
            modules += instance.get() as Module
        }

        override fun postInject(clazz: Class<*>, instance: Supplier<*>) {
        }

    }

}