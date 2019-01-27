package io.cobalt.core

import io.cobalt.core.storage.Bucket

class Blockchain(private val storage: Bucket) : Iterable<Block> {
    private var tip: ByteArray

    init {
        this.tip = when (this.storage.empty()) {
            true -> {
                val genesis = Block.genesis()
                this.storage.putBlock(genesis)
                genesis.hash
            }

            else -> {
                val lastBlock = this.storage.getLastBlock()
                lastBlock?.hash ?: arrayOf<Byte>().toByteArray()
            }
        }
    }

    fun addBlock(data: String) {
        val newBlock = Block.new(data.toByteArray(), this.tip)
        this.storage.putBlock(newBlock)
        this.tip = newBlock.hash
    }

    override fun iterator(): Iterator<Block> = BlockchainIterator()

    private inner class BlockchainIterator : Iterator<Block> {
        private var current: ByteArray = tip

        override fun hasNext() = !this.current.isEmpty()

        override fun next(): Block {
            val block = storage.getBlock(this.current)
            block?.let { b ->
                this.current = block.prevBlockHash
                return b
            }

            throw NoSuchElementException()
        }
    }
}
