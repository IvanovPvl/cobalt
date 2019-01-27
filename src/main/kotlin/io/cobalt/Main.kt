package io.cobalt

import io.cobalt.storage.RocksDbBucket
import io.cobalt.storage.DefaultSerializer

fun main(args: Array<String>) {
    val store = RocksDbBucket(DefaultSerializer())
    val blockchain = Blockchain(store)
//    blockchain.addBlock("Send 1 BTC to Pavel")
//    blockchain.addBlock("Send 3 BTC to Ivan")

    for (block in blockchain) {
        print(block)
    }
}
