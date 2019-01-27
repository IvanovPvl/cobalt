package io.cobalt.storage

import java.io.Serializable
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

interface Serializer<T : Serializable> {
    fun toBinary(data: T): ByteArray
    fun fromBinary(bytes: ByteArray): T
}

class DefaultSerializer<T : Serializable> : Serializer<T> {
    override fun toBinary(data: T): ByteArray {
        ByteArrayOutputStream().use { stream ->
            ObjectOutputStream(stream).use { oos ->
                oos.writeObject(data)
                return stream.toByteArray()
            }
        }
    }

    override fun fromBinary(bytes: ByteArray): T {
        ByteArrayInputStream(bytes).use { stream ->
            ObjectInputStream(stream).use { ois ->
                return ois.readObject() as T
            }
        }
    }
}
