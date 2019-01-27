package io.cobalt.storage

import org.rocksdb.Options
import org.rocksdb.RocksDB

import io.cobalt.utf8
import io.cobalt.Block

public interface Bucket {
    public fun getLastBlock(): Block?
    public fun getBlock(hash: ByteArray): Block?
    public fun putBlock(block: Block)
    public fun empty(): Boolean
}

public class InMemoryBucket : Bucket {
    private val store = mutableMapOf<ByteArray, Block>()
    private var lastHash = arrayOf<Byte>().toByteArray()

    override fun getLastBlock() = this.store[lastHash]

    override fun getBlock(hash: ByteArray) = this.store[hash]

    override fun putBlock(block: Block) {
        this.store[block.hash] = block
        this.lastHash = block.hash
    }

    override fun empty() = this.lastHash.isEmpty()
}

public class RocksDbBucket(private val serde: Serializer<Block>) : Bucket {
    private val store: RocksDB
    private val lastKey = "last".toByteArray()

    init {
        RocksDB.loadLibrary()
        val options = Options().setCreateIfMissing(true)
        store = RocksDB.open(options, "") // TODO: use settings
    }

    override fun getLastBlock(): Block? {
        val lastHash = this.store[this.lastKey]
        return lastHash?.let { l -> getBlock(l) }
    }

    override fun getBlock(hash: ByteArray): Block? {
        val bytes = this.store[hash]
        return bytes?.let { b -> this.serde.fromBinary(b)}
    }

    override fun putBlock(block: Block) {
        val bytes = this.serde.toBinary(block)
        this.store.put(block.hash, bytes)
        this.store.put(this.lastKey, block.hash)
    }

    override fun empty(): Boolean {
        val last = this.store[this.lastKey]
        if (last != null) {
            return last.isEmpty()
        }

        return true
    }
}
