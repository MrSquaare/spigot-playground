package fr.mrsquaare.spigotplayground.utilities

class Reflector {
    companion object {
        fun <T : Any> getPrivateField(instance: Any, fieldName: String, clazz: Class<*> = instance.javaClass): T? {
            return try {
                val field = clazz.getDeclaredField(fieldName)

                field.isAccessible = true

                field.get(instance) as T?
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()

                null
            }
        }

        @JvmStatic
        fun <T : Any> setPrivateField(
            instance: Any,
            fieldName: String,
            value: T,
            clazz: Class<*> = instance.javaClass
        ) {
            try {
                val field = clazz.getDeclaredField(fieldName)

                field.isAccessible = true

                field.set(instance, value)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun <T : Any> getPrivateMethod(
            instance: Any,
            methodName: String,
            vararg args: Any,
            clazz: Class<*> = instance.javaClass
        ): T? {
            return try {
                val argClasses = args.map { it.javaClass }.toTypedArray()
                val method = clazz.getDeclaredMethod(methodName, *argClasses)

                method.isAccessible = true

                method.invoke(instance) as T?
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()

                null
            }
        }
    }
}
