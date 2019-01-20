package io.cobalt

import io.cobalt.storage.Bucket

class Blockchain(private val storage: Bucket) {
    private var tip: ByteArray?

    init {
        this.tip = when (this.storage.empty()) {
            true -> {
                val genesis = Block.genesis()
                this.storage.putBlock(genesis)
                genesis.hash
            }

            else -> this.storage.getLastHash()
        }
    }

    fun addBlock(data: String) {
        val lastHash = this.storage.getLastHash()
        lastHash?.let { hash ->
            val newBlock = Block.new(data.utf8Bytes(), hash)
            this.storage.putBlock(newBlock)
            this.tip = newBlock.hash
        }
    }
}
