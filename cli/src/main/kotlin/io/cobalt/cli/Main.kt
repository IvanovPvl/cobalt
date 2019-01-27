package io.cobalt.cli

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option

import io.cobalt.core.Blockchain
import io.cobalt.core.storage.RocksDbBucket
import io.cobalt.core.storage.DefaultSerializer

class BlockchainCommand : CliktCommand() {
    override fun run() = Unit
}

class PrintChainCommand : CliktCommand(name = "print-chain", help = "Print the blockchain") {
    override fun run() {
        val store = RocksDbBucket(DefaultSerializer())
        val blockchain = Blockchain(store)
        for (block in blockchain) {
            print(block)
        }
    }
}

class AddBlockCommand : CliktCommand(name = "add-block", help = "Add block to blockchain") {
    private val data: String? by option("-d", "--data")

    override fun run() {
        val store = RocksDbBucket(DefaultSerializer())
        val blockchain = Blockchain(store)
        this.data?.let { d -> blockchain.addBlock(d)}
    }
}

fun main(args: Array<String>) = BlockchainCommand()
    .subcommands(PrintChainCommand(), AddBlockCommand())
    .main(args)
