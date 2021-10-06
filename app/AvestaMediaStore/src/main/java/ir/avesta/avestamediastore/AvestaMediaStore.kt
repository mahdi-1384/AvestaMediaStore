package ir.avesta.testavestamediastore

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import ir.avesta.lastavestamediastore.getAllProjectionByUri
import ir.avesta.lastavestamediastore.mGetString
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaConstructor

abstract class AvestaMediaStore<T>(
    private val resolver: ContentResolver,

    private val dataClass: T
) {

    abstract fun getUri(): Uri
    abstract fun getProjection(): Array<String>?
    abstract fun getSortOrder(): String

    private val mainConstructor by lazy { ((dataClass!!::class.constructors as ArrayList<*>)[0] as KFunction<*>) }
    private val parametersSize by lazy { mainConstructor.parameters.size }

    val allProjection = getAllProjectionByUri(getUri())!!

    open fun getSelection(): Array<Pair<String, String?>>? = null

    fun getCursor(): Cursor? {
        return resolver.query(
            getUri(),
            getProjection(),
            getSelection()?.joinToString(
                transform = { it.first },
                separator = " = ? OR ",
                postfix = " = ?"
            ),
            getSelection()?.map {
                it.second
            }?.toTypedArray(),
            getSortOrder()
        )
    }

    fun getContents() = getContents(getCursor())

    open fun getContents(cursor: Cursor?): ArrayList<T> {
        val result = arrayListOf<T>()

        cursor?.let {

            val projection = (getProjection() ?: allProjection)
            whileLoop@ while (cursor.moveToNext()) {
                val params = mutableListOf<String>()

                for (i in 0 until parametersSize) {
                    try {
                        params.add(cursor.mGetString(projection[i]) ?: continue@whileLoop)

                    } catch (e: ArrayIndexOutOfBoundsException) {
                        throw RuntimeException("The data class parameters count is not equal to the getProjection array size")
                    }
                }

                val dataObj = mainConstructor.javaConstructor!!.newInstance(*params.toTypedArray()) as T

                result.add(dataObj)
            }
        }

        return result
    }
}