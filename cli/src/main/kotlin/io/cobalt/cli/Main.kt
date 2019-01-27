package io.cobalt.cli

import io.cobalt.core.Blockchain
import io.cobalt.core.storage.RocksDbBucket
import io.cobalt.core.storage.DefaultSerializer

fun main(args: Array<String>) {
    val store = RocksDbBucket(DefaultSerializer())
    val blockchain = Blockchain(store)
    blockchain.addBlock("Send 1 BTC to Pavel")
    blockchain.addBlock("Send 3 BTC to Ivan")

    for (block in blockchain) {
        print(block)
    }
}
