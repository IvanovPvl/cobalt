package io.cobalt.storage

import io.cobalt.Block
import org.rocksdb.Options
import org.rocksdb.RocksDB

import io.cobalt.utf8

public interface Bucket {
    public fun getLastHash(): ByteArray?
    public fun getLastBlock(): Block?
    public fun getBlock(hash: ByteArray): Block?
    public fun putBlock(block: Block)
    public fun empty() = getLastHash() == null
}

public class InMemoryBucket : Bucket {
    private val store = mutableMapOf<ByteArray, Block>()
    private var lastHash: ByteArray = arrayOf<Byte>().toByteArray()

    override fun getLastHash() = this.lastHash

    override fun getLastBlock() = this.store[lastHash]

    override fun getBlock(hash: ByteArray) = this.store[hash]

    override fun putBlock(block: Block) {
        this.store[block.hash] = block
        this.lastHash = block.hash
    }
}

public class RocksDbBucket(private val serde: Serializer<Block>) : Bucket {
    private val store: RocksDB
    private var lastHash: ByteArray = arrayOf<Byte>().toByteArray()

    init {
        RocksDB.loadLibrary()
        val options = Options().setCreateIfMissing(true)
        store = RocksDB.open(options, "") // TODO: use settings
    }

    override fun getLastHash(): ByteArray? = this.lastHash

    override fun getLastBlock(): Block? {
        val bytes = this.store[this.lastHash]
        bytes?.let { it ->
            return this.serde.fromBinary(it)
        }
    }

    override fun getBlock(hash: ByteArray): Block? {
        val bytes = this.store[hash]
        bytes?.let { it ->
            return this.serde.fromBinary(it)
        }
    }

    override fun putBlock(block: Block) {
        val bytes = this.serde.toBinary(block)
        this.store.put(block.hash, bytes)
        this.lastHash = block.hash
    }
}
