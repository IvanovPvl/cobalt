package io.cobalt

class Blockchain : Iterable<Block> {
    private var blocks: MutableList<Block> = mutableListOf()

    init {
        val genesis = Block.new("Genesis Block".utf8Bytes(), arrayOf<Byte>().toByteArray())
        this.blocks.add(genesis)
    }

    fun addBlock(data: String) {
        val lastBlock = this.blocks.last()
        val newBlock = Block.new(data.utf8Bytes(), lastBlock.hash)
        this.blocks.add(newBlock)
    }

    override fun iterator(): Iterator<Block> = this.blocks.iterator()
}
