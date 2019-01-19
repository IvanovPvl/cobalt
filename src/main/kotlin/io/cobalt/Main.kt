package io.cobalt

fun main(args: Array<String>) {
    val blockchain = Blockchain()
    blockchain.addBlock("Send 1 BTC to Pavel")
    blockchain.addBlock("Send 3 BTC to Ivan")

    for (block in blockchain) {
        print(block)
    }
}
